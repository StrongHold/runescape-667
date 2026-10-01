import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static269 {

    @OriginalMember(owner = "client!iha", name = "a", descriptor = "(IIIIIIBII)V")
    public static void addRoofOccluder(@OriginalArg(1) int z2, @OriginalArg(2) int x1, @OriginalArg(3) int y1, @OriginalArg(4) int z1, @OriginalArg(5) int y2, @OriginalArg(7) int level, @OriginalArg(8) int x2) {
        Static384.aLocOccluderArray2[Static317.anInt5046++] = new LocOccluder(4, level, x1, x2, x2, x1, y1, y2, y2, y1, z1, z1, z2, z2);
    }
}
