import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static318 {

    @OriginalMember(owner = "client!kb", name = "q", descriptor = "F")
    public static float aFloat210;

    @OriginalMember(owner = "client!kb", name = "a", descriptor = "(IIZI)I")
    public static int method8555(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2) {
        if (arg0 > 243) {
            arg1 >>= 0x4;
        } else if (arg0 > 217) {
            arg1 >>= 0x3;
        } else if (arg0 > 192) {
            arg1 >>= 0x2;
        } else if (arg0 > 179) {
            arg1 >>= 0x1;
        }
        return (arg0 >> 1) + (arg1 >> 5 << 7) + ((arg2 >> 2 & 0x3F) << 10);
    }

    @OriginalMember(owner = "client!kb", name = "a", descriptor = "(IIIIIII)Z")
    public static boolean method8557(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4, @OriginalArg(6) int arg5) {
        @Pc(8) int local8 = arg2 + arg0;
        @Pc(12) int local12 = arg1 + arg4;
        @Pc(16) int local16 = arg5 + arg3;
        if (!Static172.isTriangleOccluded(local8, arg3, local12, local12, local12, local16, local16, arg2, arg2)) {
            return false;
        } else if (Static172.isTriangleOccluded(local8, arg3, local12, local12, local12, arg3, local16, arg2, local8)) {
            if (arg2 >= Static499.anInt7492) {
                if (!Static172.isTriangleOccluded(local8, local16, local12, local12, arg4, arg3, local16, local8, local8)) {
                    return false;
                }
                if (!Static172.isTriangleOccluded(local8, local16, arg4, local12, arg4, arg3, arg3, local8, local8)) {
                    return false;
                }
            } else if (!Static172.isTriangleOccluded(arg2, local16, local12, local12, arg4, arg3, local16, arg2, arg2)) {
                return false;
            } else if (!Static172.isTriangleOccluded(arg2, local16, arg4, local12, arg4, arg3, arg3, arg2, arg2)) {
                return false;
            }
            if (arg3 >= Static715.anInt10810) {
                if (!Static172.isTriangleOccluded(local8, local16, local12, local12, arg4, local16, local16, arg2, arg2)) {
                    return false;
                }
                if (!Static172.isTriangleOccluded(local8, local16, arg4, local12, arg4, local16, local16, arg2, local8)) {
                    return false;
                }
            } else if (!Static172.isTriangleOccluded(local8, arg3, local12, local12, arg4, arg3, arg3, arg2, arg2)) {
                return false;
            } else if (!Static172.isTriangleOccluded(local8, arg3, arg4, local12, arg4, arg3, arg3, arg2, local8)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
