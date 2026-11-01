package org.tastytrash.spatialGUI.mixin.screen;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.tastytrash.spatialGUI.client.SpatialGUIKeybinds;
import org.tastytrash.spatialGUI.editor.ProfilePicker;
//? if >1.21.1 {
import net.minecraft.client.input.KeyEvent;
//?}

@Mixin(EditBox.class)
public class EditBoxMixin {
    //? if >1.21.1 {
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void spatialGUI$profilePickerKey(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (ProfilePicker.handleTextFieldKey(event.key(), SpatialGUIKeybinds.matchesProfilePicker(event))) {
            cir.setReturnValue(true);
        }
    }
    //?} else {
    /*@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void spatialGUI$profilePickerKey(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (ProfilePicker.handleTextFieldKey(keyCode, SpatialGUIKeybinds.matchesProfilePicker(keyCode, scanCode))) {
            cir.setReturnValue(true);
        }
    }
    *///?}
}
