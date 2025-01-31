package steve_gall.minecolonies_solcarrot.core.client.gui;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.views.BOWindow;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.core.client.gui.AbstractWindowSkeleton;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_solcarrot.api.common.IMineColoniesSoLAPI;

public abstract class FoodNoniconWindow extends AbstractWindowSkeleton
{
	public static final Component O = Component.literal("O");
	public static final Component X = Component.literal("X");

	public static final String BUTTON_CITIZENS_MODE = "citizensMode";
	public static final String BUTTON_FOODS_MODE = "foodsMode";

	public static final String LIST_CITIZENS = "citizens";
	public static final String LIST_ATE_CITIZENS = "ateCitizens";
	public static final String LIST_NOT_ATE_CITIZENS = "notAteCitizens";
	public static final String LIST_FOODS = "foods";
	public static final String LIST_EATEN_FOODS = "eatenFoods";
	public static final String LIST_NOT_EATEN_FOODS = "notEatenFoods";

	public static final String TEXT_CITIZEN_NAME = "citizenName";
	public static final String TEXT_CITIZEN_STATUS_1 = "citizenStatus1";
	public static final String TEXT_CITIZEN_STATUS_2 = "citizenStatus2";

	public static final String TEXT_FOOD_NAME = "foodName";
	public static final String TEXT_FOOD_STATUS = "foodStatus";
	public static final String ICON_FOOD = "foodIcon";

	public static final String BUTTON_EXCEPT_COMPLETLED = "exceptCompleted";

	private BOWindow parent;

	protected final ColonyFoodData colonyFoodData;
	protected final int maxCount = IMineColoniesSoLAPI.instance().getFoodCountForMaxBonus();

	public FoodNoniconWindow(ColonyFoodData foodData, String resource, @Nullable BOWindow parent)
	{
		super(resource, parent);
		this.colonyFoodData = foodData;
		this.parent = parent;
	}

	@Override
	public void onButtonClicked(Button button)
	{
		super.onButtonClicked(button);

		if (Objects.equals(button.getID(), BUTTON_CITIZENS_MODE))
		{
			new FoodNoniconCitizensWindow(this.colonyFoodData, this.parent).open();
		}
		else if (Objects.equals(button.getID(), BUTTON_FOODS_MODE))
		{
			new FoodNoniconFoodsWindow(this.colonyFoodData, this.parent).open();
		}

	}

	protected int getHoveredRow(ScrollingList list)
	{
		var children = list.getContainer().getChildren();

		for (var i = 0; i < children.size(); i++)
		{
			if (children.get(i).wasCursorInPane())
			{
				return i;
			}

		}

		return -1;
	}

	protected int compareCitizen(ICitizenDataView citizen1, ICitizenDataView citizen2)
	{
		var foodData1 = this.colonyFoodData.getFoodData(citizen1);
		var foodData2 = this.colonyFoodData.getFoodData(citizen2);

		if ((foodData1 != null) != (foodData2 != null))
		{
			return Boolean.compare(foodData1 != null, foodData2 != null);
		}
		else if (foodData1 == null)
		{
			return 0;
		}

		var count1 = foodData1.eatenCount();
		var count2 = foodData2.eatenCount();

		if (count1 != count2)
		{
			return Integer.compare(count2, count1);
		}

		return citizen1.getName().compareTo(citizen2.getName());
	}

	protected int compareFood(ItemStack stack1, ItemStack stack2)
	{
		var count1 = this.colonyFoodData.getEatenCitizens(stack1.getItem()).size();
		var count2 = this.colonyFoodData.getEatenCitizens(stack2.getItem()).size();

		if (count1 != count2)
		{
			return Integer.compare(count2, count1);
		}

		return this.colonyFoodData.compareFood(stack1, stack2);
	}

	protected void updateCitizenRow(Pane row, ICitizenDataView citizen)
	{
		var foodData = this.colonyFoodData.getFoodData(citizen);

		var nameLabel = row.findPaneOfTypeByID(TEXT_CITIZEN_NAME, Text.class);
		nameLabel.setText(Component.literal(citizen.getName()));

		var statusLabel1 = row.findPaneOfTypeByID(TEXT_CITIZEN_STATUS_1, Text.class);
		var statusLabel2 = row.findPaneOfTypeByID(TEXT_CITIZEN_STATUS_2, Text.class);

		if (foodData != null)
		{
			var count = foodData.eatenCount();
			var complete = count >= this.maxCount;
			statusLabel1.setText(Component.translatable("minecolonies_sol.gui.eaten_count", Component.literal(count + " / " + this.maxCount).withStyle(complete ? ChatFormatting.GREEN : ChatFormatting.WHITE)));
			statusLabel2.setText(Component.translatable("minecolonies_sol.gui.bonus_heart", IMineColoniesSoLAPI.instance().getBonusHearts(foodData.milestone())));
		}
		else
		{
			statusLabel1.setText(Component.empty());
			statusLabel2.setText(Component.empty());
		}

	}

	protected void updateFoodRow(Pane row, ItemStack stack)
	{
		var nameLabel = row.findPaneOfTypeByID(TEXT_FOOD_NAME, Text.class);
		nameLabel.setText(stack.getHoverName());

		var foodIcon = row.findPaneOfTypeByID(ICON_FOOD, ItemIcon.class);
		foodIcon.setItem(stack);

		var stautsLabel = row.findPaneOfTypeByID(TEXT_FOOD_STATUS, Text.class);
		var complete = this.colonyFoodData.isCompletedFood(stack.getItem());
		stautsLabel.setText(Component.translatable("minecolonies_sol.gui.ate_count", Component.literal(this.colonyFoodData.getEatenCitizens(stack.getItem()).size() + " / " + this.colonyFoodData.getCitizenCount()).withStyle(complete ? ChatFormatting.GREEN : ChatFormatting.WHITE)));
	}

}
