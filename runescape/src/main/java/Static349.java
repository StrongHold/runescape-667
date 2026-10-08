import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static349 {

    @OriginalMember(owner = "client!kw", name = "a", descriptor = "(ZJ)V")
    public static void method5121(@OriginalArg(1) long arg0) {
        @Pc(7) int local7 = Camera.anInt6262;
        @Pc(19) int local19;
        @Pc(27) int local27;
        if (Static494.anInt7409 != local7) {
            local19 = local7 - Static494.anInt7409;
            local27 = (int) (arg0 * (long) local19 / 320L);
            if (local19 > 0) {
                if (local27 == 0) {
                    local27 = 1;
                } else if (local19 < local27) {
                    local27 = local19;
                }
            } else if (local27 == 0) {
                local27 = -1;
            } else if (local19 > local27) {
                local27 = local19;
            }
            Static494.anInt7409 += local27;
        }
        @Pc(75) int local75 = Camera.anInt4018;
        if (local75 != Static38.anInt920) {
            local19 = local75 - Static38.anInt920;
            local27 = (int) ((long) local19 * arg0 / 320L);
            if (local19 <= 0) {
                if (local27 == 0) {
                    local27 = -1;
                } else if (local19 > local27) {
                    local27 = local19;
                }
            } else if (local27 == 0) {
                local27 = 1;
            } else if (local19 < local27) {
                local27 = local19;
            }
            Static38.anInt920 += local27;
        }
        Camera.playerCameraYaw += Camera.angleAxisX * (float) arg0 / 40.0F * 8.0F;
        Camera.playerCameraPitch += Camera.angleAxisY * (float) arg0 / 40.0F * 8.0F;
        Static723.clampPlayerCamera();
    }
}
