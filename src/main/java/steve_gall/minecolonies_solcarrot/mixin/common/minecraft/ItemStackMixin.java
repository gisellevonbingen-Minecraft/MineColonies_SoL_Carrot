package steve_gall.minecolonies_solcarrot.mixin.common.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_solcarrot.core.common.entity.ICitizenDataExtensions;

@Mixin(value = ItemStack.class, remap = true)
public abstract class ItemStackMixin
{
	@Inject(method = "finishUsingItem", remap = true, at = @At(value = "HEAD"), cancellable = false)
	private void finishUsingItem(Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir)
	{
		if (!level.isClientSide() && entity instanceof AbstractEntityCitizen citizen && citizen.getCitizenData() instanceof ICitizenDataExtensions extensions)
		{
			extensions.minecolonies_sol$onEat((ItemStack) (Object) this);
		}

	}

}
