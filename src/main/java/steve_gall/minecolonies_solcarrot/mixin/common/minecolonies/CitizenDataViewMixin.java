package steve_gall.minecolonies_solcarrot.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.core.colony.CitizenDataView;

import net.minecraft.network.RegistryFriendlyByteBuf;
import steve_gall.minecolonies_solcarrot.api.common.colony.ICitizenFoodDataView;
import steve_gall.minecolonies_solcarrot.core.common.entity.CitizenFoodDataView;
import steve_gall.minecolonies_solcarrot.core.common.entity.ICitizenDataViewExtensions;

@Mixin(value = CitizenDataView.class, remap = false)
public abstract class CitizenDataViewMixin implements ICitizenDataViewExtensions
{
	@Unique
	private final CitizenFoodDataView minecolonies_sol$food = new CitizenFoodDataView(this);

	@Inject(method = "deserialize", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void deserialize(RegistryFriendlyByteBuf buf, CallbackInfo ci)
	{
		this.minecolonies_sol$food.deserialize(buf);
	}

	@Override
	public @NotNull ICitizenFoodDataView minecolonies_sol$getFood()
	{
		return this.minecolonies_sol$food;
	}

}
