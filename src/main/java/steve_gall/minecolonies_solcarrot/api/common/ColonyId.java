package steve_gall.minecolonies_solcarrot.api.common;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.IColonyView;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ColonyId
{
	public static final String TAG_DIMENSION_ID = "dimensionId";
	public static final String TAG_COLONY_ID = "colonyId";

	public static final Codec<ColonyId> CODEC = RecordCodecBuilder.create(builder -> builder.group(//
			Level.RESOURCE_KEY_CODEC.fieldOf("dimensionId").forGetter(ColonyId::getDimensionId), //
			Codec.INT.fieldOf("colonyId").forGetter(ColonyId::getColonyId) //
	).apply(builder, ColonyId::new));

	@NotNull
	private final ResourceKey<Level> dimensionId;
	private final int colonyId;

	public ColonyId(@NotNull CompoundTag tag)
	{
		this.dimensionId = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString(TAG_DIMENSION_ID)));
		this.colonyId = tag.getInt(TAG_COLONY_ID);
	}

	public ColonyId(@NotNull FriendlyByteBuf buffer)
	{
		this.dimensionId = buffer.readResourceKey(Registries.DIMENSION);
		this.colonyId = buffer.readInt();
	}

	public ColonyId(@NotNull ResourceKey<Level> dimensionId, int colonyId)
	{
		this.dimensionId = dimensionId;
		this.colonyId = colonyId;
	}

	public ColonyId(@NotNull IColony colony)
	{
		this(colony.getDimension(), colony.getID());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.dimensionId, this.colonyId);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		else if (obj instanceof ColonyId other)
		{
			return this.dimensionId == other.dimensionId //
					&& this.colonyId == other.colonyId;
		}
		else
		{
			return false;
		}

	}

	@Nullable
	public IColony getColony()
	{
		return IColonyManager.getInstance().getColonyByDimension(this.colonyId, this.dimensionId);
	}

	@Nullable
	public IColonyView getColonyView()
	{
		return IColonyManager.getInstance().getColonyView(this.colonyId, this.dimensionId);
	}

	@NotNull
	public CompoundTag serializeNBT()
	{
		var tag = new CompoundTag();
		tag.putString(TAG_DIMENSION_ID, this.dimensionId.location().toString());
		tag.putInt(TAG_COLONY_ID, this.colonyId);

		return tag;
	}

	public void serializeBuffer(@NotNull FriendlyByteBuf buffer)
	{
		buffer.writeResourceKey(this.dimensionId);
		buffer.writeInt(this.colonyId);
	}

	@NotNull
	public ResourceKey<Level> getDimensionId()
	{
		return this.dimensionId;
	}

	public int getColonyId()
	{
		return colonyId;
	}

}
