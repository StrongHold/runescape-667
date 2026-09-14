import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static181 {

    /**
     * Strokes a cubic bezier into the texture plane as 32 straight segments. A bezier stays inside the
     * convex hull of its four control points, so the caller only has to prove those four are inside
     * the clip bounds; nothing here is clipped. Parameters are in 12.12 fixed point.
     */
    @OriginalMember(owner = "client!fl", name = "a", descriptor = "(IIIIIIIIII)V")
    public static void drawCurveUnclipped(@OriginalArg(0) int startY, @OriginalArg(1) int controlStartY, @OriginalArg(2) int lineColour, @OriginalArg(4) int controlEndX, @OriginalArg(5) int startX, @OriginalArg(6) int controlEndY, @OriginalArg(7) int endY, @OriginalArg(8) int controlStartX, @OriginalArg(9) int endX) {
        if (controlStartX == startX && controlStartY == startY && controlEndX == endX && endY == controlEndY) {
            Static409.method5658(startX, endX, lineColour, endY, startY);
            return;
        }
        @Pc(51) int penX = startX;
        @Pc(53) int penY = startY;
        @Pc(57) int startX3 = startX * 3;
        @Pc(61) int startY3 = startY * 3;
        @Pc(65) int controlStartX3 = controlStartX * 3;
        @Pc(69) int controlStartY3 = controlStartY * 3;
        @Pc(73) int controlEndX3 = controlEndX * 3;
        @Pc(77) int controlEndY3 = controlEndY * 3;
        @Pc(87) int cubicX = controlStartX3 + endX - controlEndX3 - startX;
        @Pc(97) int cubicY = controlStartY3 + endY - startY - controlEndY3;
        @Pc(106) int quadraticX = startX3 + controlEndX3 - controlStartX3 - controlStartX3;
        @Pc(116) int quadraticY = startY3 + controlEndY3 - controlStartY3 - controlStartY3;
        @Pc(121) int linearX = controlStartX3 - startX3;
        @Pc(126) int linearY = controlStartY3 - startY3;
        for (@Pc(128) int t = 128; t <= 4096; t += 128) {
            @Pc(136) int t2 = t * t >> 12;
            @Pc(142) int t3 = t * t2 >> 12;
            @Pc(146) int cubicTermX = t3 * cubicX;
            @Pc(150) int cubicTermY = cubicY * t3;
            @Pc(154) int quadraticTermX = t2 * quadraticX;
            @Pc(158) int quadraticTermY = t2 * quadraticY;
            @Pc(162) int linearTermX = t * linearX;
            @Pc(166) int linearTermY = t * linearY;
            @Pc(176) int x = startX + (cubicTermX + quadraticTermX + linearTermX >> 12);
            @Pc(188) int y = startY + (cubicTermY + quadraticTermY + linearTermY >> 12);
            Static409.method5658(penX, x, lineColour, y, penY);
            penY = y;
            penX = x;
        }
    }

}
