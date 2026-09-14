import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static714 {

    @OriginalMember(owner = "client!wl", name = "i", descriptor = "[I")
    public static int[] roofMinX;

    @OriginalMember(owner = "client!wl", name = "a", descriptor = "(II[BIII[BII)V")
    public static void method9341(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) byte[] arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(6) byte[] arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7) {
        @Pc(10) int local10 = -(arg6 >> 2);
        @Pc(15) int local15 = -(arg6 & 0x3);
        for (@Pc(18) int local18 = -arg3; local18 < 0; local18++) {
            @Pc(26) int local26;
            for (@Pc(22) int local22 = local10; local22 < 0; local22++) {
                local26 = arg4++;
                arg2[local26] = (byte) (arg2[local26] - arg5[arg1++]);
                @Pc(39) int local39 = arg4++;
                arg2[local39] = (byte) (arg2[local39] - arg5[arg1++]);
                @Pc(52) int local52 = arg4++;
                arg2[local52] = (byte) (arg2[local52] - arg5[arg1++]);
                @Pc(65) int local65 = arg4++;
                arg2[local65] = (byte) (arg2[local65] - arg5[arg1++]);
            }
            for (@Pc(83) int local83 = local15; local83 < 0; local83++) {
                local26 = arg4++;
                arg2[local26] = (byte) (arg2[local26] - arg5[arg1++]);
            }
            arg1 += arg7;
            arg4 += arg0;
        }
    }
}
