import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static412 {

    @OriginalMember(owner = "client!naa", name = "f", descriptor = "I")
    public static int anInt6357;

    @OriginalMember(owner = "client!naa", name = "d", descriptor = "I")
    public static final int anInt6358 = 5000;

    @OriginalMember(owner = "client!naa", name = "a", descriptor = "(IIIBIII)V")
    public static void method5692(@OriginalArg(0) int lineWidth, @OriginalArg(1) int radius, @OriginalArg(2) int lineColour, @OriginalArg(4) int centreY, @OriginalArg(5) int centreX, @OriginalArg(6) int fillColour) {
        if (centreX - radius >= Static180.anInt2995 && radius + centreX <= Static111.anInt2219 && Static724.anInt10930 <= centreY - radius && radius + centreY <= Static273.anInt4395) {
            Static123.method2210(lineColour, fillColour, centreY, radius, centreX, lineWidth);
        } else {
            Static51.fillAndOutlineCircleClipped(lineWidth, fillColour, lineColour, centreX, radius, centreY);
        }
    }

}
