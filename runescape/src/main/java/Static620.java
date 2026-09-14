import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static620 {

    @OriginalMember(owner = "client!tka", name = "h", descriptor = "F")
    public static float aFloat197;

    @OriginalMember(owner = "client!tka", name = "a", descriptor = "(IIIIIII)V")
    public static void method8324(@OriginalArg(0) int rotateY, @OriginalArg(2) int z, @OriginalArg(3) int rotateZ, @OriginalArg(4) int y, @OriginalArg(5) int rotateX, @OriginalArg(6) int x) {
        Static271.anInt4363 = rotateY;
        Static427.anInt6480 = y;
        Static524.anInt8044 = rotateX;
        Static428.anInt6487 = x;
        Static707.anInt10641 = rotateZ;
        Static523.pickCameraZ = z;
    }
}
