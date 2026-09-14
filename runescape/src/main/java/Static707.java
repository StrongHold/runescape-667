import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static707 {

    @OriginalMember(owner = "client!wg", name = "c", descriptor = "I")
    public static int pickCameraRotateZ;

    @OriginalMember(owner = "client!wg", name = "a", descriptor = "(I[ILclient!qha;[III)Lclient!wl;")
    public static ClippingMask_Sub3 method9228(@OriginalArg(1) int[] arg0, @OriginalArg(2) GlToolkit arg1, @OriginalArg(3) int[] arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4) {
        @Pc(10) byte[] local10 = new byte[arg3 * arg4];
        for (@Pc(12) int local12 = 0; local12 < arg3; local12++) {
            @Pc(24) int local24 = local12 * arg4 + arg2[local12];
            for (@Pc(26) int local26 = 0; local26 < arg0[local12]; local26++) {
                local10[local24++] = -1;
            }
        }
        if (17787 != 17787) {
            pickCameraRotateZ = 5;
        }
        return new ClippingMask_Sub3(arg1, arg4, arg3, local10);
    }
}
