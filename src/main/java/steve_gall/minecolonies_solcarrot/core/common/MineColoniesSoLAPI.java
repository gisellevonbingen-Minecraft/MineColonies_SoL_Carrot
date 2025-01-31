package steve_gall.minecolonies_solcarrot.core.common;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.util.ItemStackUtils;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import steve_gall.minecolonies_solcarrot.api.common.IMineColoniesSoLAPI;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodData;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodDataView;
import steve_gall.minecolonies_solcarrot.core.common.config.MineColoniesSoLConfigServer;
import steve_gall.minecolonies_solcarrot.core.common.entity.ICitizenDataExtensions;
import steve_gall.minecolonies_solcarrot.core.common.entity.ICitizenDataViewExtensions;

public class MineColoniesSoLAPI implements IMineColoniesSoLAPI
{
	public static final MineColoniesSoLAPI INSTANCE = new MineColoniesSoLAPI();

	private MineColoniesSoLAPI()
	{

	}

	@Override
	public @NotNull ICitizenFoodDataView getFoodDataView(@NotNull ICitizenDataView citizen)
	{
		return ((ICitizenDataViewExtensions) citizen).minecolonies_sol$getFood();
	}

	@Override
	public @NotNull ICitizenFoodData getFoodData(@NotNull ICitizenData citizen)
	{
		return ((ICitizenDataExtensions) citizen).minecolonies_sol$getFood();
	}

	private boolean followCE()
	{
		return ModList.get().isLoaded("solcarrot") && MineColoniesSoLConfigServer.INSTANCE.followCE.get();
	}

	@Override
	public boolean shouldCount(@NotNull Item item)
	{
		if (!ItemStackUtils.ISFOOD.test(item.getDefaultInstance()))
		{
			return false;
		}
		else if (this.followCE())
		{
			return SOLCarrotConfig.isAllowed(item);
		}
		else
		{
			return MineColoniesSoLConfigServer.INSTANCE.shouldCount(item);
		}

	}

	@Override
	public int getBonusHeartsPerMilestone()
	{
		if (this.followCE())
		{
			return SOLCarrotConfig.getHeartsPerMilestone();
		}
		else
		{
			return MineColoniesSoLConfigServer.INSTANCE.heartsPerMilestones.get();
		}

	}

	@Override
	public boolean isMilestoneComplete(int milestone)
	{
		if (this.followCE())
		{
			return milestone >= SOLCarrotConfig.getMilestoneCount();
		}
		else
		{
			return milestone >= MineColoniesSoLConfigServer.INSTANCE.milestones.get().size();
		}

	}

	private List<Integer> getMilestones()
	{
		if (this.followCE())
		{
			return SOLCarrotConfig.getMilestones();
		}
		else
		{
			return new ArrayList<>(MineColoniesSoLConfigServer.INSTANCE.milestones.get());
		}

	}

	@Override
	public int getFoodCountForMaxBonus()
	{
		var foodCount = 0;

		for (var m : this.getMilestones())
		{
			foodCount = Math.max(foodCount, m);
		}

		return foodCount;
	}

	@Override
	public int getMilestone(int eatenCount)
	{
		var milestone = 0;

		for (var criteria : this.getMilestones())
		{
			if (eatenCount >= criteria)
			{
				milestone++;
			}

		}

		return milestone;
	}

}
