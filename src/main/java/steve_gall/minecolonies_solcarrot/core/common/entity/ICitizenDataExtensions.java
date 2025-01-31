package steve_gall.minecolonies_solcarrot.core.common.entity;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodData;

public interface ICitizenDataExtensions extends ICitizenData
{
	@NotNull
	ICitizenFoodData minecolonies_sol$getFood();

	public default void minecolonies_sol$onEat(ItemStack item)
	{
		var foodData = this.minecolonies_sol$getFood();
		foodData.addEaten(item);
	}

}
