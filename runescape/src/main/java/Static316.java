import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static316 {

    @OriginalMember(owner = "client!ka", name = "a", descriptor = "(IIIIIIIIII)V")
    public static void drawCurve(@OriginalArg(0) int startX, @OriginalArg(1) int controlStartY, @OriginalArg(3) int controlEndY, @OriginalArg(4) int lineColour, @OriginalArg(5) int controlEndX, @OriginalArg(6) int endY, @OriginalArg(7) int startY, @OriginalArg(8) int endX, @OriginalArg(9) int controlStartX) {
        if (startX >= Static180.anInt2995 && startX <= Static111.anInt2219 && Static180.anInt2995 <= controlStartX && Static111.anInt2219 >= controlStartX && controlEndX >= Static180.anInt2995 && controlEndX <= Static111.anInt2219 && Static180.anInt2995 <= endX && endX <= Static111.anInt2219 && startY >= Static724.anInt10930 && startY <= Static273.anInt4395 && controlStartY >= Static724.anInt10930 && Static273.anInt4395 >= controlStartY && controlEndY >= Static724.anInt10930 && controlEndY <= Static273.anInt4395 && endY >= Static724.anInt10930 && endY <= Static273.anInt4395) {
            Static181.drawCurveUnclipped(startY, controlStartY, lineColour, controlEndX, startX, controlEndY, endY, controlStartX, endX);
        } else {
            Static188.method2856(startY, lineColour, endY, controlEndY, controlEndX, controlStartY, controlStartX, startX, endX);
        }
    }

}
