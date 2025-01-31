package steve_gall.minecolonies_solcarrot.core.common.item;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.IColonyView;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_solcarrot.core.client.MineColoniesSoLClient;

public class FoodNomiconItem extends Item
{
	public FoodNomiconItem(Item.Properties properties)
	{
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		if (level.isClientSide())
		{
			var colony = IMinecoloniesAPI.getInstance().getColonyManager().getIColonyByOwner(level, player);

			if (colony instanceof IColonyView view)
			{
				MineColoniesSoLClient.open(view);
			}

		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

}
