package steve_gall.minecolonies_solcarrot.datagen;

import java.util.function.Consumer;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;

public class RecipeGenerator extends RecipeProvider
{
	public RecipeGenerator(PackOutput output)
	{
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer)
	{
		new ShapelessRecipeBuilder(RecipeCategory.MISC, ModItems.FOOD_NOMICON.get(), 1)//
				.requires(Items.BOOK).requires(Items.CARROT).requires(com.ldtteam.structurize.items.ModItems.buildTool.get())//
				.unlockedBy("has_book", has(Items.BOOK))//
				.save(consumer);
	}

}
