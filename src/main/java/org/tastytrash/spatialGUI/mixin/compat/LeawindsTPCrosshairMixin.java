package org.tastytrash.spatialGUI.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.tastytrash.spatialGUI.api.SpatialGUIAPI;

@Pseudo
@Mixin(targets = "io.github.leawind.thirdperson.internal.logic.scheduler.MinecraftHudIntegration", remap = false)
public class LeawindsTPCrosshairMixin {
    @ModifyReturnValue(method = "shouldRenderCrosshair", at = @At("RETURN"), require = 0)
    private static boolean spatialGUI$hideCrosshairInThirdPerson(boolean original) {
        return original && !SpatialGUIAPI.isThirdPersonMode();
    }
}
