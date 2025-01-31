package steve_gall.minecolonies_solcarrot.core.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.controls.TextField;
import com.ldtteam.blockui.views.BOWindow;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.util.constant.WindowConstants;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoL;

public class FoodNoniconCitizensWindow extends FoodNoniconWindow
{
	private final TextField inputField;
	private final Button exceptCompletedButton;
	private final Text citizenText;
	private final ScrollingList citizenList;
	private final Text eatenFoodText;
	private final ScrollingList eatenFoodList;
	private final Text notEatenFoodText;
	private final ScrollingList notEatenFoodList;

	private final List<ICitizenDataView> filteredCitizens = new ArrayList<>();
	private final List<ItemStack> eatenFoods = new ArrayList<>();
	private final List<ItemStack> notEatenFoods = new ArrayList<>();

	private boolean exceptCompleted = false;
	private int inputFilterRequested = 0;
	private int selectedCitizenIndex = -1;
	private int lastFoodCitizenIndex = -1;

	public FoodNoniconCitizensWindow(ColonyFoodData foodData, @Nullable BOWindow parent)
	{
		super(foodData, MineColoniesSoL.rl("gui/foo_nomicon_citizens_window.xml").toString(), parent);

		this.inputField = this.window.findPaneOfTypeByID(WindowConstants.INPUT_FILTER, TextField.class);
		this.exceptCompletedButton = this.window.findPaneOfTypeByID(BUTTON_EXCEPT_COMPLETLED, Button.class);
		this.citizenText = this.findPaneOfTypeByID(LIST_CITIZENS + "Text", Text.class);
		this.citizenList = this.window.findPaneOfTypeByID(LIST_CITIZENS, ScrollingList.class);
		this.eatenFoodText = this.findPaneOfTypeByID(LIST_EATEN_FOODS + "Text", Text.class);
		this.eatenFoodList = this.window.findPaneOfTypeByID(LIST_EATEN_FOODS, ScrollingList.class);
		this.notEatenFoodText = this.findPaneOfTypeByID(LIST_NOT_EATEN_FOODS + "Text", Text.class);
		this.notEatenFoodList = this.window.findPaneOfTypeByID(LIST_NOT_EATEN_FOODS, ScrollingList.class);

		this.inputField.setHandler(this::onFieldInput);
		this.citizenList.setDataProvider(this.filteredCitizens::size, this::updateCitizenRow);
		this.eatenFoodList.setDataProvider(this.eatenFoods::size, this::updateEatenRow);
		this.notEatenFoodList.setDataProvider(this.notEatenFoods::size, this::updateNotEatenRow);
	}

	@Override
	public void onOpened()
	{
		super.onOpened();

		this.inputField.setFocus();
		this.onExceptCompletedChanged();

		var allFoods = this.colonyFoodData.getAllFoods();
		this.eatenFoodText.setText(Component.translatable("minecolonies_sol.gui.eaten_foods", this.eatenFoods.size(), allFoods.size()));
		this.notEatenFoodText.setText(Component.translatable("minecolonies_sol.gui.not_eaten_foods", this.notEatenFoods.size(), allFoods.size()));
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.inputFilterRequested > 0 && --this.inputFilterRequested == 0)
		{
			this.updateCitizenList();
		}

		this.updateFoodList();
	}

	@Override
	public boolean click(double mx, double my)
	{
		var b = super.click(mx, my);

		if (b)
		{
			return b;
		}

		var citizenIndex = this.getHoveredRow(this.citizenList);

		if (citizenIndex > -1)
		{
			this.selectedCitizenIndex = citizenIndex;
			this.updateFoodList();
			return true;
		}

		return false;
	}

	@Override
	public void onButtonClicked(Button button)
	{
		super.onButtonClicked(button);

		if (Objects.equals(button.getID(), BUTTON_EXCEPT_COMPLETLED))
		{
			this.exceptCompleted ^= true;
			this.onExceptCompletedChanged();
		}

	}

	private void onExceptCompletedChanged()
	{
		var excpet = this.exceptCompleted;
		this.exceptCompletedButton.setText(excpet ? O : X);
		this.exceptCompletedButton.setColors((excpet ? ChatFormatting.BLACK : ChatFormatting.RED).getColor());

		this.updateCitizenList();
	}

	protected void onFieldInput(TextField input)
	{
		if (input == this.inputField)
		{
			this.inputFilterRequested = 10;
		}

	}

	protected void updateCitizenList()
	{
		this.selectedCitizenIndex = -1;
		this.filteredCitizens.clear();

		var field = this.inputField.getText().toLowerCase(Locale.ENGLISH);
		var citizens = this.colonyFoodData.getCitizenFoodEntrySet().stream().map(Entry::getKey).filter(this::testCitizenForList).toArray(ICitizenDataView[]::new);
		Arrays.stream(citizens).filter(i -> this.filterCitizen(field, i)).forEach(this.filteredCitizens::add);
		this.filteredCitizens.sort(this::compareCitizen);

		this.citizenList.setScrollY(0.0D);
		this.citizenList.refreshElementPanes();

		this.citizenText.setText(Component.translatable("minecolonies_sol.gui.citizens", this.filteredCitizens.size(), citizens.length));
	}

	protected void updateFoodList()
	{
		var citizenIndex = this.selectedCitizenIndex;

		if (this.lastFoodCitizenIndex == citizenIndex)
		{
			return;
		}

		this.lastFoodCitizenIndex = citizenIndex;
		this.eatenFoods.clear();
		this.notEatenFoods.clear();

		var allFoods = this.colonyFoodData.getAllFoods();

		if (citizenIndex > -1)
		{
			var citizen = this.filteredCitizens.get(citizenIndex);
			var foodData = this.colonyFoodData.getFoodData(citizen);

			for (var item : allFoods)
			{
				(foodData.wasEaten(item.getItem()) ? this.eatenFoods : this.notEatenFoods).add(item);
			}

		}

		this.eatenFoods.sort(this::compareFood);
		this.notEatenFoods.sort(this::compareFood);
		
		this.eatenFoodList.setScrollY(0.0D);
		this.eatenFoodList.refreshElementPanes();
		this.eatenFoodText.setText(Component.translatable("minecolonies_sol.gui.eaten_foods", this.eatenFoods.size(), allFoods.size()));

		this.notEatenFoodList.setScrollY(0.0D);
		this.notEatenFoodList.refreshElementPanes();
		this.notEatenFoodText.setText(Component.translatable("minecolonies_sol.gui.not_eaten_foods", this.notEatenFoods.size(), allFoods.size()));
	}

	protected boolean filterCitizen(String filter, ICitizenDataView citizen)
	{
		if (filter.isEmpty())
		{
			return true;
		}
		else if (citizen.getName().toLowerCase().contains(filter.toLowerCase()))
		{
			return true;
		}

		return false;
	}

	protected boolean testCitizenForList(ICitizenDataView citizen)
	{
		var foodData = this.colonyFoodData.getFoodData(citizen);

		if (foodData == null)
		{
			return false;
		}
		else if (this.exceptCompleted && foodData.milestoneComplete())
		{
			return false;
		}

		return true;
	}

	protected void updateCitizenRow(int index, Pane row)
	{
		var citizen = this.filteredCitizens.get(index);

		if (citizen == null)
		{
			return;
		}

		this.updateCitizenRow(row, citizen);

		var nameLabel = row.findPaneOfTypeByID(TEXT_CITIZEN_NAME, Text.class);
		nameLabel.setColors(this.getCitizenLabelColor(citizen, index).getColor());
	}

	protected void updateEatenRow(int index, Pane row)
	{
		var item = this.eatenFoods.get(index);
		this.updateFoodRow(row, item);
	}

	protected void updateNotEatenRow(int index, Pane row)
	{
		var item = this.notEatenFoods.get(index);
		this.updateFoodRow(row, item);
	}

	protected ChatFormatting getCitizenLabelColor(ICitizenDataView citizen, int index)
	{
		return this.selectedCitizenIndex == index ? ChatFormatting.GOLD : ChatFormatting.WHITE;
	}

}
