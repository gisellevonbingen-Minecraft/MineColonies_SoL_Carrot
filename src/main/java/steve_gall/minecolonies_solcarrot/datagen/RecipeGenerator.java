package steve_gall.minecolonies_solcarrot.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import steve_gall.minecolonies_solcarrot.core.common.init.ModItems;

public class RecipeGenerator extends RecipeProvider
{
	public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(output, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput output)
	{
		new ShapelessRecipeBuilder(RecipeCategory.MISC, ModItems.FOOD_NOMICON.get(), 1)//
				.requires(Items.BOOK).requires(Items.CARROT).requires(com.ldtteam.structurize.items.ModItems.buildTool.get())//
				.unlockedBy("has_book", has(Items.BOOK))//
				.save(output);
	}

}
