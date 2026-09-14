import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static417 {

    @OriginalMember(owner = "client!nd", name = "C", descriptor = "I")
    public static int anInt6399;

    @OriginalMember(owner = "client!nd", name = "B", descriptor = "I")
    public static int secondNearestDistance;

    @OriginalMember(owner = "client!nd", name = "y", descriptor = "I")
    public static int anInt6400 = -60;

    @OriginalMember(owner = "client!nd", name = "a", descriptor = "(II[BIBII)Z")
    public static boolean method5723(@OriginalArg(1) int arg0, @OriginalArg(2) byte[] arg1, @OriginalArg(3) int arg2, @OriginalArg(5) int arg3, @OriginalArg(6) int arg4) {
        @Pc(16) int local16 = arg2 % 8;
        @Pc(23) int local23;
        if (local16 == 0) {
            local23 = 0;
        } else {
            local23 = 8 - local16;
        }
        @Pc(39) int local39 = -((arg3 + 8 - 1) / 8);
        @Pc(48) int local48 = -((arg2 + 7) / 8);
        for (@Pc(50) int local50 = local39; local50 < 0; local50++) {
            for (@Pc(56) int local56 = local48; local56 < 0; local56++) {
                if (arg1[arg0] == 0) {
                    return true;
                }
                arg0 += 8;
            }
            arg0 -= local23;
            if (arg1[arg0 - 1] == 0) {
                return true;
            }
            arg0 += arg4;
        }
        return false;
    }
}
