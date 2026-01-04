package steve_gall.minecolonies_solcarrot.core.common.compat;

import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;
import steve_gall.minecolonies_tweaks.api.client.gui.ResourceScrollBookElementEvent;

public class TweaksCompat
{
	public TweaksCompat()
	{
		var forge_bus = NeoForge.EVENT_BUS;
		forge_bus.addListener(this::onResourceScrollBookElement);

	}

	private void onResourceScrollBookElement(ResourceScrollBookElementEvent e)
	{
		if (e.getStack().is(ModItems.FOOD_NOMICON.get()))
		{
			e.register(new FoodNomiconElement(e.getStack()));
		}

	}

}
