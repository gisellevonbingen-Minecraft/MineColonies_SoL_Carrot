package steve_gall.minecolonies_solcarrot.core.client;

import com.minecolonies.api.colony.IColonyView;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_solcarrot.core.client.gui.ColonyFoodData;
import steve_gall.minecolonies_solcarrot.core.client.gui.FoodNoniconCitizensWindow;

public class MineColoniesSoLClient
{
	public MineColoniesSoLClient()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		var forge_bus = MinecraftForge.EVENT_BUS;
	}

	public static void open(IColonyView colony)
	{
		new FoodNoniconCitizensWindow(new ColonyFoodData(colony), null).open();
	}

}
