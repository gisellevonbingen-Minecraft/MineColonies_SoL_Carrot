package steve_gall.minecolonies_solcarrot.api.common.colony;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenDataView;

import net.minecraft.world.item.Item;

public interface ICitizenFoodDataView
{
	@NotNull
	ICitizenDataView citizen();

	int milestone();

	boolean milestoneComplete();

	boolean wasEaten(@NotNull Item item);

	@NotNull
	Set<Item> getEatens();

	int eatenCount();
}
