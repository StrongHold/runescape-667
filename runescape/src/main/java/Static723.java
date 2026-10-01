import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static723 {

    @OriginalMember(owner = "client!wu", name = "x", descriptor = "[[I")
    public static int[][] anIntArrayArray266;

    @OriginalMember(owner = "client!wu", name = "c", descriptor = "(B)V")
    public static void method9451() {
        @Pc(5) short local5 = 1024;
        @Pc(7) short local7 = 3072;
        if (OrthoMode.enabled) {
            if (Static129.orthoCameraLock) {
                local5 = 2048;
            }
            local7 = 4096;
        }
        if (Camera.playerCameraPitch < (float) local5) {
            Camera.playerCameraPitch = local5;
        }
        while (Camera.playerCameraYaw >= 16384.0F) {
            Camera.playerCameraYaw -= 16384.0F;
        }
        if ((float) local7 < Camera.playerCameraPitch) {
            Camera.playerCameraPitch = local7;
        }
        while (Camera.playerCameraYaw < 0.0F) {
            Camera.playerCameraYaw += 16384.0F;
        }
        @Pc(59) int local59 = Static494.anInt7409 >> 9;
        @Pc(63) int local63 = Static38.anInt920 >> 9;
        @Pc(77) int local77 = Static102.averageHeight(Camera.renderingLevel, Static494.anInt7409, Static38.anInt920);
        @Pc(79) int local79 = 0;
        @Pc(109) int local109;
        if (local59 > 3 && local63 > 3 && Static720.mapWidth - 4 > local59 && local63 < Static501.mapLength - 4) {
            for (local109 = local59 - 4; local109 <= local59 + 4; local109++) {
                for (@Pc(114) int local114 = local63 - 4; local114 <= local63 + 4; local114++) {
                    @Pc(117) int local117 = Camera.renderingLevel;
                    if (local117 < 3 && Static441.isBridgeAt(local114, local109)) {
                        local117++;
                    }
                    @Pc(132) int local132 = 0;
                    if (MapRegion.active.cameraHeights != null && MapRegion.active.cameraHeights[local117] != null) {
                        local132 = (MapRegion.active.cameraHeights[local117][local109][local114] & 0xFF) * 8 << 2;
                    }
                    if (Static246.ground != null && Static246.ground[local117] != null) {
                        @Pc(177) int local177 = local132 + local77 - Static246.ground[local117].getHeight(local109, local114);
                        if (local79 < local177) {
                            local79 = local177;
                        }
                    }
                }
            }
        }
        local109 = (local79 >> 2) * 1536;
        if (local109 > 786432) {
            local109 = 786432;
        }
        if (local109 < 262144) {
            local109 = 262144;
        }
        if (Static188.anInt3103 < local109) {
            Static188.anInt3103 += (local109 - Static188.anInt3103) / 24;
        } else if (Static188.anInt3103 > local109) {
            Static188.anInt3103 += (local109 - Static188.anInt3103) / 80;
        }
    }
}
