package steve_gall.minecolonies_solcarrot.core.common.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoL;
import steve_gall.minecolonies_solcarrot.core.common.item.FoodNomiconItem;

public class ModItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineColoniesSoL.MOD_ID);
	public static final RegistryObject<Item> FOOD_NOMICON = REGISTER.register("food_nomicon", () -> new FoodNomiconItem(new Item.Properties()));

	private ModItems()
	{

	}

}
