package steve_gall.minecolonies_solcarrot.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minecolonies.api.creativetab.ModCreativeTabs;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import steve_gall.minecolonies_solcarrot.core.client.MineColoniesSoLClient;
import steve_gall.minecolonies_solcarrot.core.common.command.ModCommands;
import steve_gall.minecolonies_solcarrot.core.common.compat.TweaksCompat;
import steve_gall.minecolonies_solcarrot.core.common.config.MineColoniesSoLConfigClient;
import steve_gall.minecolonies_solcarrot.core.common.config.MineColoniesSoLConfigCommon;
import steve_gall.minecolonies_solcarrot.core.common.config.MineColoniesSoLConfigServer;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;

@Mod(MineColoniesSoL.MOD_ID)
public class MineColoniesSoL
{
	public static final String MOD_ID = "minecolonies_solcarrot";
	public static final Logger LOGGER = LogManager.getLogger();

	public MineColoniesSoL(FMLModContainer modContainer, Dist dist)
	{
		modContainer.registerConfig(ModConfig.Type.CLIENT, MineColoniesSoLConfigClient.SPEC);
		modContainer.registerConfig(ModConfig.Type.COMMON, MineColoniesSoLConfigCommon.SPEC);
		modContainer.registerConfig(ModConfig.Type.SERVER, MineColoniesSoLConfigServer.SPEC);

		var fml_bus = modContainer.getEventBus();
		ModItems.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onBuildCreativeModeTabContents);

		if (ModList.get().isLoaded("minecolonies_tweaks"))
		{
			new TweaksCompat();
		}
		
		var forge_bus = NeoForge.EVENT_BUS;
		forge_bus.addListener((RegisterCommandsEvent e) -> ModCommands.register(e.getDispatcher()));

		if (dist.isClient())
		{
			new MineColoniesSoLClient(modContainer);
		}

	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModItems.FOOD_NOMICON.get());
		}

	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

}
