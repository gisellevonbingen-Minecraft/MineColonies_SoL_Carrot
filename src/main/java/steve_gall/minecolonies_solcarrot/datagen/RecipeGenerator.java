package steve_gall.minecolonies_solcarrot.datagen;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;

public class RecipeGenerator extends RecipeProvider
{
	public RecipeGenerator(DataGenerator generator)
	{
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		new ShapelessRecipeBuilder(ModItems.FOOD_NOMICON.get(), 1)//
				.requires(Items.BOOK).requires(Items.CARROT).requires(com.ldtteam.structurize.items.ModItems.buildTool.get())//
				.unlockedBy("has_book", has(Items.BOOK))//
				.save(consumer);
	}

}
