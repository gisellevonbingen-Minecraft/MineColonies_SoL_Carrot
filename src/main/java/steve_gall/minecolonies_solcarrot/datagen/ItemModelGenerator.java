package steve_gall.minecolonies_solcarrot.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoL;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;

public class ItemModelGenerator extends ItemModelProvider
{
	public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, MineColoniesSoL.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		this.generated(ModItems.FOOD_NOMICON);
	}

	private void generated(RegistryObject<Item> object)
	{
		var path = object.getId().getPath();
		this.singleTexture(path, this.mcLoc("item/generated"), "layer0", MineColoniesSoL.rl("item/" + path));
	}

}
