package test.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.core.event.GatherDataHandler;

import net.minecraftforge.data.event.GatherDataEvent;

@Mixin(value = GatherDataHandler.class, remap = false)
public class GatherDataHandlerMixin
{
	@Inject(method = "dataGeneratorSetup", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private static void dataGeneratorSetup(GatherDataEvent event, CallbackInfo ci)
	{
		ci.cancel();
	}

}
