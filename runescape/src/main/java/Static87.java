import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static87 {

    @OriginalMember(owner = "client!cn", name = "c", descriptor = "I")
    public static int updateZoneLevel;

    @OriginalMember(owner = "client!cn", name = "h", descriptor = "I")
    public static int tradeChatFilter = 0;

    @OriginalMember(owner = "client!cn", name = "a", descriptor = "(IIIII)V")
    public static void drawVerticalLineUnclipped(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        @Pc(8) int local8;
        if (arg0 >= arg1) {
            for (local8 = arg1; local8 < arg0; local8++) {
                Static723.anIntArrayArray266[local8][arg3] = arg2;
            }
        } else {
            for (local8 = arg0; local8 < arg1; local8++) {
                Static723.anIntArrayArray266[local8][arg3] = arg2;
            }
        }
    }

}
