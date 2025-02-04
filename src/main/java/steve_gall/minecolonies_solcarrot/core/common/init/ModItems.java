package steve_gall.minecolonies_solcarrot.core.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoL;
import steve_gall.minecolonies_solcarrot.core.common.item.FoodNomiconItem;

public class ModItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, MineColoniesSoL.MOD_ID);
	public static final DeferredHolder<Item, FoodNomiconItem> FOOD_NOMICON = REGISTER.register("food_nomicon", () -> new FoodNomiconItem(new Item.Properties()));

	private ModItems()
	{

	}

}
