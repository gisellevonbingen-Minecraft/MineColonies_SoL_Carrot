package steve_gall.minecolonies_solcarrot.core.common.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.minecolonies.api.colony.ICitizenDataView;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import steve_gall.minecolonies_solcarrot.api.common.IMineColoniesSoLAPI;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodDataView;
import steve_gall.minecolonies_solcarrot.core.common.util.ItemUtils;

public class CitizenFoodDataView implements ICitizenFoodDataView
{
	private final ICitizenDataView citizen;

	private final Set<Item> eatenFoods;
	private int count;
	private int milestone;

	public CitizenFoodDataView(ICitizenDataView citizen)
	{
		this.citizen = citizen;

		this.eatenFoods = new HashSet<>();
		this.count = 0;
		this.milestone = 0;
	}

	public void deserialize(FriendlyByteBuf buffer)
	{
		this.eatenFoods.clear();
		this.eatenFoods.addAll(ItemUtils.decodeItems(ArrayList::new, buffer));
		this.count = buffer.readInt();
		this.milestone = buffer.readInt();
	}

	@Override
	public @NotNull ICitizenDataView citizen()
	{
		return this.citizen;
	}

	@Override
	public boolean wasEaten(@NotNull Item item)
	{
		return this.eatenFoods.contains(item);
	}

	@Override
	public @NotNull Set<Item> getEatens()
	{
		return ImmutableSet.copyOf(this.eatenFoods);
	}

	@Override
	public int eatenCount()
	{
		return this.count;
	}

	@Override
	public int milestone()
	{
		return this.milestone;
	}

	@Override
	public boolean milestoneComplete()
	{
		return IMineColoniesSoLAPI.instance().isMilestoneComplete(this.milestone);
	}

}
