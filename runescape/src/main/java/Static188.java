import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static188 {

    @OriginalMember(owner = "client!fq", name = "o", descriptor = "F")
    public static float aFloat65;

    @OriginalMember(owner = "client!fq", name = "k", descriptor = "Lclient!hla;")
    public static final Class168 aClass168_1 = new Class168();

    @OriginalMember(owner = "client!fq", name = "l", descriptor = "I")
    public static int cameraPitchFloor = 0;

    @OriginalMember(owner = "client!fq", name = "a", descriptor = "(BIIIIIIIII)V")
    public static void method2856(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5, @OriginalArg(7) int arg6, @OriginalArg(8) int arg7, @OriginalArg(9) int arg8) {
        if (arg7 == arg6 && arg0 == arg5 && arg4 == arg8 && arg2 == arg3) {
            Static418.method7862(arg8, arg2, arg7, arg1, arg0);
            return;
        }
        @Pc(32) int local32 = arg7;
        @Pc(34) int local34 = arg0;
        @Pc(38) int local38 = arg7 * 3;
        @Pc(42) int local42 = arg0 * 3;
        @Pc(46) int local46 = arg6 * 3;
        @Pc(50) int local50 = arg5 * 3;
        @Pc(54) int local54 = arg4 * 3;
        @Pc(58) int local58 = arg3 * 3;
        @Pc(67) int local67 = local46 + arg8 - arg7 - local54;
        @Pc(78) int local78 = arg2 + local50 - arg0 - local58;
        @Pc(87) int local87 = local54 + local38 - local46 - local46;
        @Pc(97) int local97 = local58 + local42 - local50 - local50;
        @Pc(102) int local102 = local46 - local38;
        @Pc(107) int local107 = local50 - local42;
        for (@Pc(109) int local109 = 128; local109 <= 4096; local109 += 128) {
            @Pc(117) int local117 = local109 * local109 >> 12;
            @Pc(123) int local123 = local117 * local109 >> 12;
            @Pc(127) int local127 = local67 * local123;
            @Pc(131) int local131 = local78 * local123;
            @Pc(135) int local135 = local117 * local87;
            @Pc(139) int local139 = local97 * local117;
            @Pc(143) int local143 = local109 * local102;
            @Pc(147) int local147 = local107 * local109;
            @Pc(157) int local157 = (local135 + local127 + local143 >> 12) + arg7;
            @Pc(168) int local168 = arg0 + (local139 + local131 + local147 >> 12);
            Static418.method7862(local157, local168, local32, arg1, local34);
            local32 = local157;
            local34 = local168;
        }
    }

}
