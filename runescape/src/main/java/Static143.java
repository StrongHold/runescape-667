import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.loading.screen.LoadingScreen;

public final class Static143 {

    @OriginalMember(owner = "client!eia", name = "I", descriptor = "I")
    public static int nearestDistance;

    @OriginalMember(owner = "client!eia", name = "B", descriptor = "Z")
    public static boolean aBoolean315;

    @OriginalMember(owner = "client!eia", name = "e", descriptor = "(B)V")
    public static void method3572() {
        if (Loading.screens == null) {
            return;
        }
        @Pc(14) LoadingScreen[] local14 = Loading.screens;
        for (@Pc(16) int local16 = 0; local16 < local14.length; local16++) {
            @Pc(21) LoadingScreen local21 = local14[local16];
            local21.init();
        }
    }

    @OriginalMember(owner = "client!eia", name = "a", descriptor = "(IIIIIIIII)V")
    public static void method3573(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(4) int arg2, @OriginalArg(6) int arg3, @OriginalArg(7) int arg4, @OriginalArg(8) int arg5) {
        Static418.method7860(arg4, arg3, arg0, arg1, 0);
    }
}
