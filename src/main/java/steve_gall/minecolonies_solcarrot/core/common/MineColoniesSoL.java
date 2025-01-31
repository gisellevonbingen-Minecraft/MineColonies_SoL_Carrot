package steve_gall.minecolonies_solcarrot.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_solcarrot.core.client.MineColoniesSoLClient;
import steve_gall.minecolonies_solcarrot.core.common.command.ModCommands;
import steve_gall.minecolonies_solcarrot.core.common.config.MineColoniesSoLConfigClient;
import steve_gall.minecolonies_solcarrot.core.common.config.MineColoniesSoLConfigCommon;
import steve_gall.minecolonies_solcarrot.core.common.config.MineColoniesSoLConfigServer;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;

@Mod(MineColoniesSoL.MOD_ID)
public class MineColoniesSoL
{
	public static final String MOD_ID = "minecolonies_solcarrot";
	public static final Logger LOGGER = LogManager.getLogger();

	public MineColoniesSoL()
	{
		var modLoadingContext = ModLoadingContext.get();
		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, MineColoniesSoLConfigClient.SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, MineColoniesSoLConfigCommon.SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.SERVER, MineColoniesSoLConfigServer.SPEC);

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModItems.REGISTER.register(fml_bus);

		var forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener((RegisterCommandsEvent e) -> ModCommands.register(e.getDispatcher()));

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MineColoniesSoLClient::new);
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

}
