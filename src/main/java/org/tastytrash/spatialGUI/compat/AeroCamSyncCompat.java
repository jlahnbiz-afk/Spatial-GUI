package org.tastytrash.spatialGUI.compat;

//? if aerocamsync {
/*import com.playsi.aero_cam_sync.api.AcsHandle;
import com.playsi.aero_cam_sync.api.AeroCamSyncApi;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;
import org.joml.Quaternionf;
import org.joml.Vector3f;
*///?}

public final class AeroCamSyncCompat {
    private AeroCamSyncCompat() {}

    public static void tiltRay(float[] ray, float partialTick) {
        //? if aerocamsync {
        /*if (!LOADED) return;
        Quaternionf tilt = Bridge.lookTilt(partialTick);
        if (tilt == null) return;
        Vector3f v = tilt.transform(new Vector3f(ray[0], ray[1], ray[2]));
        ray[0] = v.x;
        ray[1] = v.y;
        ray[2] = v.z;
        *///?}
    }

    //? if aerocamsync {
    /*private static final boolean LOADED = ModList.get().isLoaded("aero_cam_sync");

    private static final class Bridge {
        private static final AcsHandle ACS = AeroCamSyncApi.forMod("spatial_gui");

        static Quaternionf lookTilt(float partialTick) {
            var player = Minecraft.getInstance().player;
            return player == null ? null : ACS.state(player, partialTick).lookTilt();
        }
    }
    *///?}
}