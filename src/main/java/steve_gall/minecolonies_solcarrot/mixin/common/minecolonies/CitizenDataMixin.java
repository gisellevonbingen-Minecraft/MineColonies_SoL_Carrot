package steve_gall.minecolonies_solcarrot.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.core.colony.CitizenData;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodData;
import steve_gall.minecolonies_solcarrot.core.common.entity.CitizenFoodData;
import steve_gall.minecolonies_solcarrot.core.common.entity.ICitizenDataExtensions;

@Mixin(value = CitizenData.class, remap = false)
public abstract class CitizenDataMixin implements ICitizenDataExtensions
{
	@Unique
	private final CitizenFoodData minecolonies_sol$food = new CitizenFoodData(this);

	@Inject(method = "deserializeNBT", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound, CallbackInfo ci)
	{
		this.minecolonies_sol$food.deserializeNBT(provider, compound.getCompound("minecolonies_sol$food"));
	}

	@Inject(method = "serializeNBT", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void serializeNBT(HolderLookup.Provider provider, CallbackInfoReturnable<CompoundTag> cir)
	{
		var compound = cir.getReturnValue();
		compound.put("minecolonies_sol$food", this.minecolonies_sol$food.serializeNBT(provider));
	}

	@Inject(method = "serializeViewNetworkData", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void serializeViewNetworkData(RegistryFriendlyByteBuf buf, CallbackInfo ci)
	{
		this.minecolonies_sol$food.serializeViewNetworkData(buf);
	}

	@Override
	public @NotNull ICitizenFoodData minecolonies_sol$getFood()
	{
		return this.minecolonies_sol$food;
	}

}
