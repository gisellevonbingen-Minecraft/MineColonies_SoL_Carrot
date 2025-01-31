package steve_gall.minecolonies_solcarrot.api.common.colony;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ICitizenFoodData
{
	@NotNull
	ICitizenData citizen();

	int milestone();

	boolean milestoneComplete();

	boolean addEaten(@NotNull ItemStack stack);

	boolean wasEaten(@NotNull Item item);

	@NotNull
	Set<Item> getEatens();

	void clearEatens();

	int eatenCount();
}
