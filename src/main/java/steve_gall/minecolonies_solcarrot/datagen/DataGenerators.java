package steve_gall.minecolonies_solcarrot.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var existingFileHelper = event.getExistingFileHelper();
		var lookupProvider = event.getLookupProvider();

		generator.addProvider(event.includeServer(), new RecipeGenerator(output, lookupProvider));

		generator.addProvider(event.includeClient(), new ItemModelGenerator(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new LanguageGenerator(output, "en_us"));
	}

}
