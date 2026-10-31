package org.tastytrash.spatialGUI.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.tastytrash.spatialGUI.api.SpatialGUIAPI;

@Pseudo
@Mixin(targets = "com.github.exopandora.shouldersurfing.client.renderer.CrosshairRenderer", remap = false)
public class ShoulderSurfingCrosshairMixin {
    @ModifyReturnValue(method = "isCrosshairVisible", at = @At("RETURN"), require = 0)
    private boolean spatialGUI$hideCrosshairInThirdPerson(boolean original) {
        return original && !SpatialGUIAPI.isThirdPersonMode();
    }
}
