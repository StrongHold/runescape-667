import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static211 {

    @OriginalMember(owner = "client!gj", name = "j", descriptor = "Lclient!cja;")
    public static PingRequest pingRequest;

    @OriginalMember(owner = "client!gj", name = "a", descriptor = "(IIIIII)V")
    public static void method5000(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4) {
        @Pc(7) int local7 = 0;
        @Pc(9) int local9 = arg1;
        @Pc(13) int local13 = arg2 * arg2;
        @Pc(17) int local17 = arg1 * arg1;
        @Pc(21) int local21 = local17 << 1;
        @Pc(25) int local25 = local13 << 1;
        @Pc(29) int local29 = arg1 << 1;
        @Pc(38) int local38 = local21 + (1 - local29) * local13;
        @Pc(47) int local47 = local17 - local25 * (local29 - 1);
        @Pc(51) int local51 = local13 << 2;
        @Pc(55) int local55 = local17 << 2;
        @Pc(63) int local63 = local21 * 3;
        @Pc(71) int local71 = local25 * ((arg1 << 1) - 3);
        @Pc(77) int local77 = local55;
        Static696.method9037(arg2 + arg4, arg3, arg4 - arg2, Static723.anIntArrayArray266[arg0]);
        @Pc(96) int local96 = (arg1 - 1) * local51;
        while (local9 > 0) {
            if (local38 < 0) {
                while (local38 < 0) {
                    local47 += local77;
                    local38 += local63;
                    local7++;
                    local63 += local55;
                    local77 += local55;
                }
            }
            if (local47 < 0) {
                local47 += local77;
                local38 += local63;
                local63 += local55;
                local7++;
                local77 += local55;
            }
            local38 += -local96;
            local47 += -local71;
            local71 -= local51;
            local96 -= local51;
            local9--;
            @Pc(172) int local172 = arg0 - local9;
            @Pc(176) int local176 = local9 + arg0;
            @Pc(180) int local180 = local7 + arg4;
            @Pc(185) int local185 = arg4 - local7;
            Static696.method9037(local180, arg3, local185, Static723.anIntArrayArray266[local172]);
            Static696.method9037(local180, arg3, local185, Static723.anIntArrayArray266[local176]);
        }
    }
}
