package org.tastytrash.spatialGUI.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.tastytrash.spatialGUI.client.SpatialGUIClient;
import org.tastytrash.spatialGUI.config.SpatialGUIConfig.ViewMode;
import org.tastytrash.spatialGUI.config.ConfigManager;
import org.tastytrash.spatialGUI.render.SpatialGUIRenderer;
import org.tastytrash.spatialGUI.util.MouseHandlerUtil;

public class EditorMode {
    public record EditorButton(Supplier<Component> label, boolean rightSide, Runnable action, BooleanSupplier enabled, BooleanSupplier visible) {
        public EditorButton(Supplier<Component> label, boolean rightSide, Runnable action, BooleanSupplier enabled) {
            this(label, rightSide, action, enabled, () -> true);
        }
        public EditorButton(Supplier<Component> label, boolean rightSide, Runnable action) {
            this(label, rightSide, action, () -> true);
        }
    }

    public static final EditorButton[] BUTTONS = {
            new EditorButton(() -> Component.translatable("spatial-gui.editor.button.undo"), false, EditorHistory::undo, EditorHistory::canUndo),
            new EditorButton(() -> Component.translatable("spatial-gui.editor.button.redo"), false, EditorHistory::redo, EditorHistory::canRedo),
            new EditorButton(() -> Component.translatable("spatial-gui.editor.button.toggle_view"), false, EditorMode::toggleView),
            new EditorButton(EditorMode::guiScaleLabel, false, EditorMode::cycleGuiScale),
            new EditorButton(EditorMode::curvedScreenLabel, false, EditorMode::toggleCurvedScreen),
            new EditorButton(() -> Component.translatable("spatial-gui.editor.button.undo_all"), true, EditorHistory::undoAll, EditorHistory::canUndo),
            new EditorButton(() -> Component.translatable("spatial-gui.editor.button.set_default"), true, EditorProfiles::setAsDefault, EditorProfiles::canSetDefault),
            new EditorButton(() -> Component.translatable("spatial-gui.editor.button.save_quit"), true, () -> toggle(Minecraft.getInstance()))
    };

    public record EditorSlider(Supplier<Component> label, double min, double max, DoubleSupplier get, DoubleConsumer set, BooleanSupplier visible) {
        public EditorSlider(Supplier<Component> label, double min, double max, DoubleSupplier get, DoubleConsumer set) {
            this(label, min, max, get, set, () -> true);
        }
    }

    public static final EditorSlider[] SLIDERS = {
            new EditorSlider(() -> Component.translatable("spatial-gui.editor.slider.arc"), -180, 180, () -> {
                var config = ConfigManager.config();
                return SpatialGUIClient.getEffectiveFirstPersonMode() ? config.curvedScreenArcDegrees : config.thirdPersonCurvedScreenArcDegrees;
            }, degrees -> {
                var config = ConfigManager.config();
                if (SpatialGUIClient.getEffectiveFirstPersonMode()) config.curvedScreenArcDegrees = (int) Math.round(degrees);
                else config.thirdPersonCurvedScreenArcDegrees = (int) Math.round(degrees);
            }, () -> {
                var config = ConfigManager.config();
                boolean enabled = SpatialGUIClient.getEffectiveFirstPersonMode() ? config.curvedScreenEnabled : config.thirdPersonCurvedScreenEnabled;
                int degrees = SpatialGUIClient.getEffectiveFirstPersonMode() ? config.curvedScreenArcDegrees : config.thirdPersonCurvedScreenArcDegrees;
                return enabled && Math.abs(degrees) >= 5;
            }),
            new EditorSlider(() -> Component.translatable("spatial-gui.editor.slider.yaw"), 0, 360, EditorMode::yaw, EditorMode::setYaw),
            new EditorSlider(() -> Component.translatable("spatial-gui.editor.slider.pitch"), -90, 90, EditorMode::pitch, EditorMode::setPitch)
    };

    private static Boolean viewOverride;
    private static boolean previewing;
    private static boolean mouseFree;

    public static String[] getHints() {
        boolean firstPerson = SpatialGUIClient.getEffectiveFirstPersonMode();
        List<String> hints = new ArrayList<>(List.of(
                "spatial-gui.editor.hint.esc",
                "spatial-gui.editor.hint.scroll",
                "spatial-gui.editor.hint.rmb",
                "spatial-gui.editor.hint.mmb",
                firstPerson ? "spatial-gui.editor.hint.v_third" : "spatial-gui.editor.hint.v_first",
                "spatial-gui.editor.hint.ctrl_z",
                "spatial-gui.editor.hint.ctrl_y",
                "spatial-gui.editor.hint.space"
        ));
        if (SpatialGUIRenderer.isCrosshairModeActive()) {
            hints.add(mouseFree ? "spatial-gui.editor.hint.tab_grab" : "spatial-gui.editor.hint.tab_free");
        }
        return hints.toArray(new String[0]);
    }

    public static void toggle(Minecraft client) {
        if (client.level == null || !ConfigManager.config().enabled) return;
        Screen screen = client.screen;
        if (screen instanceof EditorScreen) {
            screen.onClose();
        } else {
            SpatialGUIClient.showScreen(client, new EditorScreen());
        }
    }

    public static void begin() {
        viewOverride = null;
        previewing = false;
        mouseFree = false;
    }

    public static void end() {
        viewOverride = null;
        mouseFree = false;
    }

    public static Boolean viewOverride() {
        return viewOverride;
    }

    public static void toggleView() {
        boolean firstPerson = !SpatialGUIClient.getEffectiveFirstPersonMode();
        viewOverride = firstPerson;
        ConfigManager.config().viewMode = firstPerson ? ViewMode.FIRST_PERSON : ViewMode.THIRD_PERSON;
        mouseFree = false;
    }

    private static double yaw() {
        var config = ConfigManager.config();
        return SpatialGUIClient.getEffectiveFirstPersonMode() ? config.firstPersonScreenYawOffset : config.screenYawOffset;
    }

    private static void setYaw(double value) {
        var config = ConfigManager.config();
        double rounded = Math.round(value);
        if (SpatialGUIClient.getEffectiveFirstPersonMode()) config.firstPersonScreenYawOffset = rounded;
        else config.screenYawOffset = rounded;
    }

    private static double pitch() {
        var config = ConfigManager.config();
        return SpatialGUIClient.getEffectiveFirstPersonMode() ? config.firstPersonScreenPitchOffset : config.screenPitchOffset;
    }

    private static void setPitch(double value) {
        var config = ConfigManager.config();
        double rounded = Math.round(value);
        if (SpatialGUIClient.getEffectiveFirstPersonMode()) config.firstPersonScreenPitchOffset = rounded;
        else config.screenPitchOffset = rounded;
    }

    private static int maxGuiScale() {
        var window = Minecraft.getInstance().getWindow();
        return Mth.clamp(Math.min(window.getWidth() / 320, window.getHeight() / 240), 1, 6);
    }

    public static void cycleGuiScale() {
        var config = ConfigManager.config();
        int max = maxGuiScale();
        int current = Math.min(config.guiScale, max);
        if (!config.overrideGuiScale) {
            config.overrideGuiScale = true;
            config.guiScale = 1;
        } else if (current >= max) {
            config.overrideGuiScale = false;
        } else {
            config.guiScale = current + 1;
        }
        previewing = false;
        var client = Minecraft.getInstance();
        if (client.screen != null) {
            //? if >1.21.1 {
            client.screen.resize(client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight());
            //?} else {
            /*client.screen.resize(client, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight());
             *///?}
        }
    }

    private static void toggleCurvedScreen() {
        var config = ConfigManager.config();
        if (SpatialGUIClient.getEffectiveFirstPersonMode()) {
            config.curvedScreenEnabled = !config.curvedScreenEnabled;
        } else {
            config.thirdPersonCurvedScreenEnabled = !config.thirdPersonCurvedScreenEnabled;
        }
    }

    private static Component guiScaleLabel() {
        var config = ConfigManager.config();
        Component value = config.overrideGuiScale
                ? Component.literal(String.valueOf(Math.min(config.guiScale, maxGuiScale())))
                : Component.translatable("spatial-gui.editor.value.default");
        return Component.translatable("spatial-gui.editor.button.gui_scale", value);
    }

    private static Component curvedScreenLabel() {
        var config = ConfigManager.config();
        boolean enabled = SpatialGUIClient.getEffectiveFirstPersonMode() ? config.curvedScreenEnabled : config.thirdPersonCurvedScreenEnabled;
        Component value = enabled
                ? Component.translatable("spatial-gui.editor.value.on")
                : Component.translatable("spatial-gui.editor.value.off");
        return Component.translatable("spatial-gui.editor.button.curved_screen", value);
    }

    public static boolean previewing() {
        return previewing;
    }

    public static void setPreviewing(boolean bool) {
        previewing = bool;
    }

    public static boolean mouseFree() {
        return mouseFree;
    }

    public static boolean cursorFree() {
        return mouseFree || !SpatialGUIRenderer.isCrosshairModeActive();
    }

    public static void resetMouseFree() {
        mouseFree = false;
    }

    public static void toggleMouseFree() {
        mouseFree = !mouseFree;
        MouseHandlerUtil.centerCursor();
    }

    public static void resetScreenDistance() {
        var config = ConfigManager.config();
        boolean fp = SpatialGUIClient.getEffectiveFirstPersonMode();
        if (fp) {
            config.firstPersonScreenDistance = 1.5;
        } else {
            config.screenDistance = 2.5;
        }
        EditorHistory.checkpoint();
    }
}