import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static604 {

    @OriginalMember(owner = "client!tb", name = "b", descriptor = "[I")
    public static final int[] anIntArray708 = new int[16];

    @OriginalMember(owner = "client!tb", name = "a", descriptor = "(JI)V")
    public static void method7903(@OriginalArg(0) long arg0) {
        @Pc(9) int local9 = PlayerEntity.self.x + Static508.anInt7627;
        @Pc(15) int local15 = PlayerEntity.self.z + Static145.anInt2561;
        if (Static494.anInt7409 - local9 < -2000 || Static494.anInt7409 - local9 > 2000 || Static38.anInt920 - local15 < -2000 || Static38.anInt920 - local15 > 2000) {
            Static38.anInt920 = local15;
            Static494.anInt7409 = local9;
        }
        @Pc(68) int local68;
        @Pc(76) int local76;
        if (local9 != Static494.anInt7409) {
            local68 = local9 - Static494.anInt7409;
            local76 = (int) (arg0 * (long) local68 / 320L);
            if (local68 <= 0) {
                if (local76 == 0) {
                    local76 = -1;
                } else if (local76 < local68) {
                    local76 = local68;
                }
            } else if (local76 == 0) {
                local76 = 1;
            } else if (local68 < local76) {
                local76 = local68;
            }
            Static494.anInt7409 += local76;
        }
        if (local15 != Static38.anInt920) {
            local68 = local15 - Static38.anInt920;
            local76 = (int) ((long) local68 * arg0 / 320L);
            if (local68 > 0) {
                if (local76 == 0) {
                    local76 = 1;
                } else if (local76 > local68) {
                    local76 = local68;
                }
            } else if (local76 == 0) {
                local76 = -1;
            } else if (local68 > local76) {
                local76 = local68;
            }
            Static38.anInt920 += local76;
        }
        Camera.playerCameraPitch += Camera.angleAxisY * (float) arg0 / 6.0F;
        Camera.playerCameraYaw += (float) arg0 * Camera.angleAxisX / 6.0F;
        Static723.clampPlayerCamera();
    }

    @OriginalMember(owner = "client!tb", name = "a", descriptor = "(III)Z")
    public static boolean method7904(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return (arg0 & 0x34) != 0;
    }

}
