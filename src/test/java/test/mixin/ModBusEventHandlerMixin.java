package test.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ldtteam.domumornamentum.event.handlers.ModBusEventHandler;

import net.minecraftforge.data.event.GatherDataEvent;

@Mixin(value = ModBusEventHandler.class, remap = false)
public abstract class ModBusEventHandlerMixin
{
	@Inject(method = "dataGeneratorSetup", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private static void dataGeneratorSetup(GatherDataEvent event, CallbackInfo ci)
	{
		ci.cancel();
	}

}
