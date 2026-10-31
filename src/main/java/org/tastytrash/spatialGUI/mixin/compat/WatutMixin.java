package org.tastytrash.spatialGUI.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.tastytrash.spatialGUI.api.SpatialGUIAPI;
import org.tastytrash.spatialGUI.client.SpatialGUIClient;

@Pseudo
@Mixin(targets = "com.corosus.watut.PlayerStatusManagerClient", remap = false)
public class WatutMixin {

    @ModifyReturnValue(method = "shouldAnimate", at = @At("RETURN"), remap = false, require = 0)
    private boolean spatialGUI$hideWatutScreen(boolean original, Player player) {
        if (!original) return false;
        return !((SpatialGUIAPI.isActive() || SpatialGUIClient.renderer().isClosing()) && player == Minecraft.getInstance().player);
    }
}