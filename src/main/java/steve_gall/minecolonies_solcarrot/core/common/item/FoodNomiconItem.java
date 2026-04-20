package steve_gall.minecolonies_solcarrot.core.common.item;

import java.util.List;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.items.component.ColonyId;
import com.minecolonies.api.items.component.ModDataComponents;
import com.minecolonies.api.tileentities.AbstractTileEntityColonyBuilding;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_solcarrot.core.client.MineColoniesSoLClient;

public class FoodNomiconItem extends Item
{
	public static final Component TOOLIP_HOW_TO_LINK = Component.translatable("minecolonies_sol.text.how_to_link");

	public static final Component MESSAGE_MISSING_COLONY = Component.translatable("minecolonies_sol.text.missing_colony");

	public static final Component TEXT_COLONY_MISSING = Component.translatable("minecolonies_sol.text.colony_missing").withStyle(ChatFormatting.GRAY);
	public static final Component TEXT_LINKED = Component.translatable("minecolonies_sol.text.linked");

	public FoodNomiconItem(Item.Properties properties)
	{
		super(properties.stacksTo(1));
	}

	public void setColonyId(ItemStack stack, ColonyId colonyId)
	{
		stack.set(ModDataComponents.COLONY_ID_COMPONENT, colonyId);
	}

	public ColonyId getColonyId(ItemStack stack)
	{
		return stack.get(ModDataComponents.COLONY_ID_COMPONENT);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		var level = context.getLevel();
		var stack = context.getItemInHand();
		var blockEntity = level.getBlockEntity(context.getClickedPos());

		if (level.isClientSide())
		{
			if (blockEntity instanceof AbstractTileEntityColonyBuilding)
			{

			}
			else
			{
				this.openWindow(stack, context.getPlayer());
			}

		}
		else if (blockEntity instanceof AbstractTileEntityColonyBuilding buildingEntity)
		{
			var building = buildingEntity.getBuilding();

			if (building != null)
			{
				var colony = building.getColony();
				setColonyId(stack, new ColonyId(colony.getID(), colony.getDimension()));
				context.getPlayer().sendSystemMessage(TEXT_LINKED);
			}

		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		var stack = player.getItemInHand(hand);

		if (level.isClientSide())
		{
			this.openWindow(stack, player);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	public void openWindow(ItemStack stack, Player player)
	{
		var colonyId = getColonyId(stack);

		if (colonyId == null)
		{
			if (player != null)
			{
				player.sendSystemMessage(MESSAGE_MISSING_COLONY);
			}

			return;
		}

		var colonyManager = IMinecoloniesAPI.getInstance().getColonyManager();
		var colonyView = colonyManager.getColonyView(colonyId.id(), colonyId.dimension());

		if (colonyView == null)
		{
			if (player != null)
			{
				player.sendSystemMessage(MESSAGE_MISSING_COLONY);
			}

			return;
		}

		MineColoniesSoLClient.open(colonyView);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, context, tooltip, flag);
		tooltip.add(TOOLIP_HOW_TO_LINK);

		if (context == null)
		{
			return;
		}

		var colonyId = getColonyId(stack);

		if (colonyId == null)
		{
			return;
		}

		tooltip.add(Component.empty());

		var colonyManager = IMinecoloniesAPI.getInstance().getColonyManager();
		var colonyView = colonyManager.getColonyView(colonyId.id(), colonyId.dimension());
		Component colonyName = null;

		if (colonyView == null)
		{
			colonyName = TEXT_COLONY_MISSING;
		}
		else
		{
			colonyName = Component.empty().append(colonyView.getName()).withStyle(ChatFormatting.DARK_PURPLE);
		}

		tooltip.add(Component.translatable("minecolonies_sol.text.linked_colony", colonyName));
	}

}
