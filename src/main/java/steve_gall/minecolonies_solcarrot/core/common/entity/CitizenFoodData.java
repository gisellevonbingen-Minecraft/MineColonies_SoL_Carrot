package steve_gall.minecolonies_solcarrot.core.common.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.minecolonies.api.colony.ICitizenData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import steve_gall.minecolonies_solcarrot.api.common.IMineColoniesSoLAPI;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodData;
import steve_gall.minecolonies_solcarrot.core.common.MineColoniesSoL;
import steve_gall.minecolonies_solcarrot.core.common.util.ItemUtils;

public class CitizenFoodData implements ICitizenFoodData, INBTSerializable<CompoundTag>
{
	private static final UUID MAX_HEALTH_MODIFIER_ID = UUID.fromString("31091b53-759d-4a00-a064-4e53ac235e2b");

	private static final String TAG_EATENS = "eatens";

	private final ICitizenData citizen;
	private final Set<Item> eatens = new HashSet<>();

	private int cachedEatenCount = 0;
	private int cachedMilestone = 0;

	public CitizenFoodData(ICitizenData citizen)
	{
		this.citizen = citizen;
	}

	public void serializeViewNetworkData(FriendlyByteBuf buffer)
	{
		ItemUtils.encodeItems(buffer, this.eatens);
		buffer.writeInt(this.cachedEatenCount);
		buffer.writeInt(this.cachedMilestone);
	}

	@Override
	public @NotNull ICitizenData citizen()
	{
		return this.citizen;
	}

	@Override
	public int milestone()
	{
		return this.cachedMilestone;
	}

	private void setCacheCount(int eatenCount)
	{
		this.cachedEatenCount = eatenCount;
		this.cachedMilestone = IMineColoniesSoLAPI.instance().getMilestone(eatenCount);
		this.updateModifier();

		this.citizen.markDirty(20);
	}

	@Override
	public boolean milestoneComplete()
	{
		return IMineColoniesSoLAPI.instance().isMilestoneComplete(this.milestone());
	}

	@Override
	public boolean addEaten(@NotNull ItemStack stack)
	{
		var item = stack.getItem();
		var added = this.eatens.add(item);

		if (added && IMineColoniesSoLAPI.instance().shouldCount(item))
		{
			this.setCacheCount(this.cachedEatenCount + 1);
			return added;
		}
		else
		{
			this.updateModifier();
			return false;
		}

	}

	@Override
	public boolean wasEaten(@NotNull Item item)
	{
		return this.eatens.contains(item);
	}

	@Override
	public @NotNull Set<Item> getEatens()
	{
		return ImmutableSet.copyOf(this.eatens);
	}

	@Override
	public void clearEatens()
	{
		this.eatens.clear();
		this.setCacheCount(0);
	}

	@Override
	public int eatenCount()
	{
		return this.cachedEatenCount;
	}

	public boolean updateModifier()
	{
		var entity = this.citizen.getEntity().orElse(null);

		if (entity == null)
		{
			return false;
		}

		var bonusHealth = IMineColoniesSoLAPI.instance().getBonusHealth(this.milestone());
		var oldModifier = entity.getAttribute(Attributes.MAX_HEALTH).getModifier(MAX_HEALTH_MODIFIER_ID);

		if (oldModifier != null && oldModifier.getAmount() == bonusHealth)
		{
			return false;
		}

		var newModifier = new AttributeModifier(MAX_HEALTH_MODIFIER_ID, MineColoniesSoL.MOD_ID, bonusHealth, AttributeModifier.Operation.ADDITION);
		var oldMax = entity.getMaxHealth();

		if (oldModifier != null)
		{
			entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(oldModifier);
		}

		entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(newModifier);

		var newMax = entity.getMaxHealth();
		entity.setHealth(entity.getHealth() * (newMax / oldMax));

		return true;
	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		ItemUtils.deserializeItems(this.eatens, compound.getList(TAG_EATENS, Tag.TAG_STRING));

		this.setCacheCount((int) this.eatens.stream().filter(IMineColoniesSoLAPI.instance()::shouldCount).count());
	}

	@Override
	public CompoundTag serializeNBT()
	{
		var compound = new CompoundTag();
		compound.put(TAG_EATENS, ItemUtils.serializeItems(this.eatens));

		return compound;
	}

}
