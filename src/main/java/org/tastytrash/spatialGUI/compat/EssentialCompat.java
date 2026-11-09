package org.tastytrash.spatialGUI.compat;

import com.mojang.blaze3d.platform.Window;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else if neoforge {
/*import net.neoforged.fml.loading.LoadingModList;
*///?} else if forge {
/*import net.minecraftforge.fml.loading.LoadingModList;
*///?}

public class EssentialCompat {

    private EssentialCompat() {}

    private static boolean isModLoaded(String id) {
        //? if fabric {
        return FabricLoader.getInstance().isModLoaded(id);
        //?} else {
        /*return LoadingModList.get().getModFileById(id) != null;
        *///?}
    }

    private static final class Presence {
        static final boolean AVAILABLE = EssentialAvailability.detect(
                EssentialCompat::isModLoaded,
                resource -> EssentialCompat.class.getClassLoader().getResource(resource) != null);
    }

    private static final class Walker {
        static final StackWalker INSTANCE = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    }

    public static boolean isIntegrationAvailable() {
        return Presence.AVAILABLE;
    }

    public static boolean isEssentialCaller() {
        if (!isIntegrationAvailable()) return false;
        // Caller identity remains live when vanilla and Essential query scale in one frame.
        return Walker.INSTANCE
                .walk(s -> s.dropWhile(f -> {
                    String name = f.getDeclaringClass().getName();
                    return name.equals(EssentialCompat.class.getName())
                            || name.equals(Window.class.getName())
                            || name.equals("org.tastytrash.spatialGUI.mixin.screen.WindowMixin");
                }).findFirst())
                .map(f -> f.getDeclaringClass().getName().startsWith("gg.essential."))
                .orElse(false);
    }
}
