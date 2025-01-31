package steve_gall.minecolonies_solcarrot.core.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

public class FoodNoniconFoodsWindow extends FoodNoniconWindow
{
	private final TextField inputField;
	private final Button exceptCompletedButton;
	private final Text foodText;
	private final ScrollingList foodList;
	private final Text ateCitizenText;
	private final ScrollingList ateCitizenList;
	private final Text notAteCitizenText;
	private final ScrollingList notAteCitizenList;

	private final List<ItemStack> filteredFoods = new ArrayList<>();
	private final List<ICitizenDataView> ateCitizens = new ArrayList<>();
	private final List<ICitizenDataView> notAteCitizens = new ArrayList<>();

	private boolean exceptCompleted = false;
	private int inputFilterRequested = 0;
	private int selectedFoodIndex = -1;
	private int lastCitizenFoodIndex = -1;

	public FoodNoniconFoodsWindow(ColonyFoodData foodData, @Nullable BOWindow parent)
	{
		super(foodData, MineColoniesSoL.rl("gui/foo_nomicon_foods_window.xml").toString(), parent);

		this.inputField = this.window.findPaneOfTypeByID(WindowConstants.INPUT_FILTER, TextField.class);
		this.exceptCompletedButton = this.window.findPaneOfTypeByID(BUTTON_EXCEPT_COMPLETLED, Button.class);
		this.foodText = this.findPaneOfTypeByID(LIST_FOODS + "Text", Text.class);
		this.foodList = this.window.findPaneOfTypeByID(LIST_FOODS, ScrollingList.class);
		this.ateCitizenText = this.findPaneOfTypeByID(LIST_ATE_CITIZENS + "Text", Text.class);
		this.ateCitizenList = this.window.findPaneOfTypeByID(LIST_ATE_CITIZENS, ScrollingList.class);
		this.notAteCitizenText = this.findPaneOfTypeByID(LIST_NOT_ATE_CITIZENS + "Text", Text.class);
		this.notAteCitizenList = this.window.findPaneOfTypeByID(LIST_NOT_ATE_CITIZENS, ScrollingList.class);

		this.inputField.setHandler(this::onFieldInput);
		this.foodList.setDataProvider(this.filteredFoods::size, this::updateFoodRow);
		this.ateCitizenList.setDataProvider(this.ateCitizens::size, this::updateAteRow);
		this.notAteCitizenList.setDataProvider(this.notAteCitizens::size, this::updateNotAteRow);
	}

	@Override
	public void onOpened()
	{
		super.onOpened();

		this.inputField.setFocus();
		this.onExceptCompletedChanged();

		var citizenCount = this.colonyFoodData.getCitizenCount();
		this.ateCitizenText.setText(Component.translatable("minecolonies_sol.gui.ate_citizens", this.ateCitizens.size(), citizenCount));
		this.notAteCitizenText.setText(Component.translatable("minecolonies_sol.gui.not_ate_citizens", this.notAteCitizens.size(), citizenCount));
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.inputFilterRequested > 0 && --this.inputFilterRequested == 0)
		{
			this.updateFoodList();
		}

		this.updateCitizenList();
	}

	@Override
	public boolean click(double mx, double my)
	{
		var b = super.click(mx, my);

		if (b)
		{
			return b;
		}

		var foodIndex = this.getHoveredRow(this.foodList);

		if (foodIndex > -1)
		{
			this.selectedFoodIndex = foodIndex;
			this.updateCitizenList();
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

		this.updateFoodList();
	}

	protected void onFieldInput(TextField input)
	{
		if (input == this.inputField)
		{
			this.inputFilterRequested = 10;
		}

	}

	protected void updateFoodList()
	{
		this.selectedFoodIndex = -1;
		this.filteredFoods.clear();

		var field = this.inputField.getText().toLowerCase(Locale.ENGLISH);
		var allFoods = this.colonyFoodData.getAllFoods().stream().filter(this::testFoodForList).toArray(ItemStack[]::new);
		Arrays.stream(allFoods).filter(i -> this.filterFood(field, i)).forEach(this.filteredFoods::add);
		this.filteredFoods.sort(this::compareFood);

		this.foodList.setScrollY(0.0D);
		this.foodList.refreshElementPanes();
		this.foodText.setText(Component.translatable("minecolonies_sol.gui.foods", this.filteredFoods.size(), allFoods.length));
	}

	protected void updateCitizenList()
	{
		var foodIndex = this.selectedFoodIndex;

		if (this.lastCitizenFoodIndex == foodIndex)
		{
			return;
		}

		this.lastCitizenFoodIndex = foodIndex;
		this.ateCitizens.clear();
		this.notAteCitizens.clear();

		if (foodIndex > -1)
		{
			var stack = this.filteredFoods.get(foodIndex);

			for (var entry : this.colonyFoodData.getCitizenFoodEntrySet())
			{
				(entry.getValue().wasEaten(stack.getItem()) ? this.ateCitizens : this.notAteCitizens).add(entry.getKey());
			}

		}

		var citizenCount = this.colonyFoodData.getCitizenCount();

		this.ateCitizens.sort(this::compareCitizen);
		this.ateCitizenList.setScrollY(0.0D);
		this.ateCitizenList.refreshElementPanes();
		this.ateCitizenText.setText(Component.translatable("minecolonies_sol.gui.ate_citizens", this.ateCitizens.size(), citizenCount));

		this.notAteCitizens.sort(this::compareCitizen);
		this.notAteCitizenList.setScrollY(0.0D);
		this.notAteCitizenList.refreshElementPanes();
		this.notAteCitizenText.setText(Component.translatable("minecolonies_sol.gui.not_ate_citizens", this.notAteCitizens.size(), citizenCount));
	}

	protected boolean filterFood(String filter, ItemStack stack)
	{
		if (filter.isEmpty())
		{
			return true;
		}
		else if (stack.getHoverName().getString().toLowerCase().contains(filter.toLowerCase()))
		{
			return true;
		}

		return false;
	}

	protected boolean testFoodForList(ItemStack stack)
	{
		if (this.exceptCompleted && this.colonyFoodData.isCompletedFood(stack.getItem()))
		{
			return false;
		}

		return true;
	}

	protected void updateFoodRow(int index, Pane row)
	{
		var food = this.filteredFoods.get(index);
		this.updateFoodRow(row, food);

		var nameLabel = row.findPaneOfTypeByID(TEXT_FOOD_NAME, Text.class);
		nameLabel.setColors(this.getFoodLabelColor(food, index).getColor());
	}

	protected void updateAteRow(int index, Pane row)
	{
		var citizen = this.ateCitizens.get(index);
		this.updateCitizenRow(row, citizen);
	}

	protected void updateNotAteRow(int index, Pane row)
	{
		var citizen = this.notAteCitizens.get(index);
		this.updateCitizenRow(row, citizen);
	}

	protected ChatFormatting getFoodLabelColor(ItemStack stack, int index)
	{
		return this.selectedFoodIndex == index ? ChatFormatting.GOLD : ChatFormatting.WHITE;
	}

}
