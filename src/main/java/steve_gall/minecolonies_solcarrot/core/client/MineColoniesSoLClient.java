package steve_gall.minecolonies_solcarrot.core.client;

import com.minecolonies.api.colony.IColonyView;

import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_solcarrot.core.client.gui.ColonyFoodData;
import steve_gall.minecolonies_solcarrot.core.client.gui.FoodNoniconCitizensWindow;

public class MineColoniesSoLClient
{
	public MineColoniesSoLClient(FMLModContainer modContainer)
	{
		var fml_bus = modContainer.getEventBus();
		var forge_bus = NeoForge.EVENT_BUS;
	}

	public static void open(IColonyView colony)
	{
		new FoodNoniconCitizensWindow(new ColonyFoodData(colony), null).open();
	}

}
