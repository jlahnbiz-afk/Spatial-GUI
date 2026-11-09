package org.tastytrash.spatialGUI.compat;

import java.util.function.Predicate;

/** Detect the full mod, its bootstrap/container, or embedded classes without initializing them. */
public final class EssentialAvailability {
    private EssentialAvailability() {}

    public static boolean detect(Predicate<String> modPresent, Predicate<String> resourcePresent) {
        try {
            if (modPresent.test("essential") || modPresent.test("essential-loader") || modPresent.test("essential-container")) return true;
            return resourcePresent.test("gg/essential/Essential.class")
                    || resourcePresent.test("gg/essential/api/EssentialAPI.class")
                    || resourcePresent.test("gg/essential/loader/stage0/EssentialSetupPreLaunch.class")
                    || resourcePresent.test("gg/essential/loader/stage1/EssentialSetupPreLaunch.class");
        } catch (RuntimeException | LinkageError unavailable) {
            // An unusual loader retains caller inspection rather than disabling compatibility.
            return true;
        }
    }
}
