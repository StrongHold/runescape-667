import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static172 {

    @OriginalMember(owner = "client!ffa", name = "m", descriptor = "I")
    public static int lastMouseX = -1;

    @OriginalMember(owner = "client!ffa", name = "l", descriptor = "I")
    public static int anInt2893 = 0;

    @OriginalMember(owner = "client!ffa", name = "i", descriptor = "Lclient!eba;")
    public static final Class92 aClass92_8 = new Class92(4);

    @OriginalMember(owner = "client!ffa", name = "k", descriptor = "[I")
    public static final int[] anIntArray251 = new int[8];

    @OriginalMember(owner = "client!ffa", name = "a", descriptor = "(IIIIIBIIII)Z")
    public static boolean isTriangleOccluded(@OriginalArg(0) int xB, @OriginalArg(1) int zA, @OriginalArg(2) int yB, @OriginalArg(3) int yC, @OriginalArg(4) int yA, @OriginalArg(6) int zB, @OriginalArg(7) int zC, @OriginalArg(8) int xA, @OriginalArg(9) int xC) {
        if (!Static706.method9224(zA, yA, xA)) {
            return false;
        }
        @Pc(23) int screenYA = Static35.anIntArray58[1];
        @Pc(27) int depthA = Static35.anIntArray58[2];
        @Pc(31) int screenXA = Static35.anIntArray58[0];
        if (!Static706.method9224(zB, yB, xB)) {
            return false;
        }
        @Pc(44) int screenXB = Static35.anIntArray58[0];
        @Pc(48) int screenYB = Static35.anIntArray58[1];
        @Pc(52) int depthB = Static35.anIntArray58[2];
        if (Static706.method9224(zC, yC, xC)) {
            @Pc(65) int screenYC = Static35.anIntArray58[1];
            @Pc(69) int screenXC = Static35.anIntArray58[0];
            @Pc(73) int depthC = Static35.anIntArray58[2];
            return Static264.rasteriseTriangle(screenXA, screenYA, depthC, depthB, screenYC, screenXC, screenYB, depthA, screenXB);
        } else {
            return false;
        }
    }
}
