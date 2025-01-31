package steve_gall.minecolonies_solcarrot.datagen;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		var generator = event.getGenerator();
		var existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(event.includeServer(), new RecipeGenerator(generator));

		generator.addProvider(event.includeClient(), new ItemModelGenerator(generator, existingFileHelper));
		generator.addProvider(event.includeClient(), new LanguageGenerator(generator, "en_us"));
	}

}
