package org.tastytrash.spatialGUI.compat;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else if neoforge {
/*import net.neoforged.fml.ModList;
 *///?} else {
/*import net.minecraftforge.fml.ModList;
 *///?}

public final class RecipeViewerCompat {
    private static final String[] MOD_IDS = {"emi", "jei", "roughlyenoughitems", "rrv", "eiv"};
    private static Boolean detected = null;

    public static boolean isRecipeViewerLoaded() {
        if (detected == null) {
            boolean found = false;
            for (String id : MOD_IDS) found |= isLoaded(id);
            detected = found;
        }
        return detected;
    }

    private static boolean isLoaded(String id) {
        //? if fabric {
        return FabricLoader.getInstance().isModLoaded(id);
        //?} else {
        /*return ModList.get().isLoaded(id);
         *///?}
    }
}