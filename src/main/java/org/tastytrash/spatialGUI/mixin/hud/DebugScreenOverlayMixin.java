package org.tastytrash.spatialGUI.mixin.hud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tastytrash.spatialGUI.SpatialGUI;
import org.tastytrash.spatialGUI.api.SpatialGUIAPI;
import org.tastytrash.spatialGUI.client.SpatialGUIClient;
import org.tastytrash.spatialGUI.config.ConfigManager;

//? if <=1.21.1 {
/*import net.minecraft.client.gui.components.DebugScreenOverlay;
@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void spatialGUI$hideDebugOverlay(CallbackInfo ci) {
        if (SpatialGUIAPI.isActive() && ConfigManager.config().hideHud) {
            ci.cancel();
        }
    }
}
*///?} else {

@Mixin(net.minecraft.client.Minecraft.class)
public class DebugScreenOverlayMixin {}
//?}