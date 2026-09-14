import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static732 {

    @OriginalMember(owner = "client!qs", name = "b", descriptor = "(II)I")
    public static int scaleRgb(@OriginalArg(0) int rgb, @OriginalArg(1) int light) {
        @Pc(7) int local7 = (rgb & 0xFF0000) * light >> 23;
        if (local7 < 2) {
            local7 = 2;
        } else if (local7 > 253) {
            local7 = 253;
        }
        @Pc(26) int local26 = (rgb & 0xFF00) * light >> 15;
        if (local26 < 2) {
            local26 = 2;
        } else if (local26 > 253) {
            local26 = 253;
        }
        @Pc(45) int local45 = (rgb & 0xFF) * light >> 7;
        if (local45 < 2) {
            local45 = 2;
        } else if (local45 > 253) {
            local45 = 253;
        }
        return local7 << 16 | local26 << 8 | local45;
    }
}
