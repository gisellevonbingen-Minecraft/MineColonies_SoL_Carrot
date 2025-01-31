package steve_gall.minecolonies_solcarrot.api.common;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.ICitizenDataView;

import net.minecraft.world.item.Item;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodData;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodDataView;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoLAPI;

public interface IMineColoniesSoLAPI
{
	@NotNull
	static IMineColoniesSoLAPI instance()
	{
		return MineColoniesSoLAPI.INSTANCE;
	}

	@NotNull
	ICitizenFoodDataView getFoodDataView(@NotNull ICitizenDataView citizen);

	@NotNull
	ICitizenFoodData getFoodData(@NotNull ICitizenData citizen);

	boolean shouldCount(@NotNull Item item);

	int getBonusHeartsPerMilestone();

	boolean isMilestoneComplete(int milestone);

	int getFoodCountForMaxBonus();

	int getMilestone(int eatenCount);

	public default int getBonusHearts(int milestone)
	{
		return this.getBonusHeartsPerMilestone() * milestone;
	}

	public default int getBonusHealth(int milestone)
	{
		return this.getBonusHearts(milestone) * 2;
	}

}
