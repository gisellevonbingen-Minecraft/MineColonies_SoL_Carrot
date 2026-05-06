package steve_gall.minecolonies_solcarrot.core.common.compat;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.BOScreen;
import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.items.component.ColonyId;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_solcarrot.api.common.IMineColoniesSoLAPI;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodDataView;
import steve_gall.minecolonies_solcarrot.core.client.gui.ColonyFoodData;
import steve_gall.minecolonies_solcarrot.core.client.gui.FoodNoniconCitizensWindow;
import steve_gall.minecolonies_solcarrot.core.common.item.FoodNomiconItem;
import steve_gall.minecolonies_tweaks.api.client.gui.ResourceScrollBookElement;

public class FoodNomiconElement extends ResourceScrollBookElement
{
	private ColonyFoodData food = null;
	private Component completed = EMPTY;
	private Component least = EMPTY;
	private Component tooltip = EMPTY;

	public FoodNomiconElement(@NotNull ItemStack stack)
	{
		super(stack);
	}

	@Override
	public void onOpenClicked()
	{
		super.onOpenClicked();

		if (this.food == null)
		{
			return;
		}

		var mc = Minecraft.getInstance();
		BOWindow window = null;

		if (mc.screen instanceof BOScreen screen)
		{
			window = screen.getWindow();
		}

		new FoodNoniconCitizensWindow(this.food, window).open();
	}

	@Override
	public void update()
	{
		super.update();

		ColonyId colonyId = null;

		if (this.stack.getItem() instanceof FoodNomiconItem item)
		{
			colonyId = item.getColonyId(this.stack);
		}

		var colonyManager = IMinecoloniesAPI.getInstance().getColonyManager();
		var view = colonyId == null ? null : colonyManager.getColonyView(colonyId.id(), colonyId.dimension());

		if (view == null)
		{
			this.valid = false;
			this.food = null;
			this.completed = COLONY_NOT_SETTED;
			this.least = EMPTY;
			this.tooltip = EMPTY;
			return;
		}
		else
		{
			this.valid = true;
			this.food = new ColonyFoodData(view);
		}

		var count = 0;
		var completes = 0;
		ICitizenFoodDataView least = null;

		for (var entry : this.food.getCitizenFoodEntrySet())
		{
			count++;

			var citizen = entry.getValue();

			if (citizen.milestoneComplete())
			{
				completes++;
			}
			else if (least == null || least.eatenCount() > citizen.eatenCount())
			{
				least = citizen;
			}

		}

		var max = IMineColoniesSoLAPI.instance().getFoodCountForMaxBonus();
		this.completed = Component.translatable("minecolonies_sol.gui.completed_citizens", completes, count);
		this.least = least == null ? EMPTY : Component.translatable("minecolonies_sol.gui.least_ate_citizen", least.citizen().getName());
		this.tooltip = least == null ? EMPTY : Component.literal(least.citizen().getName() + "\n").append(Component.translatable("minecolonies_sol.gui.eaten_count", least.eatenCount() + " / " + max));
	}

	@Override
	public void update(int index, @NotNull Pane rowPane)
	{
		super.update(index, rowPane);

		if (!this.valid)
		{
			return;
		}

		this.getDesc1Label(rowPane).setText(this.completed);
		this.getDesc2Label(rowPane).setText(this.least);
		this.getTooltip2(rowPane).setText(this.tooltip);
	}

}
