import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static547 {

    /**
     * Fills a rectangle and draws its border into the texture shape raster held in
     * {@link Static723#anIntArrayArray266}, clamping every edge to the raster bounds first.
     * {@link Static446#fillAndOutlineRectUnclipped} is the counterpart for a rectangle already
     * known to be inside them.
     */
    @OriginalMember(owner = "client!rda", name = "a", descriptor = "(IIIZIIII)V")
    public static void fillAndOutlineRectClipped(@OriginalArg(0) int y1, @OriginalArg(1) int x0, @OriginalArg(2) int lineColour, @OriginalArg(4) int y0, @OriginalArg(5) int x1, @OriginalArg(6) int lineWidth, @OriginalArg(7) int fillColour) {
        @Pc(25) int clippedY0 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, y0);
        @Pc(31) int clippedY1 = Static670.method8732(Static724.anInt10930, Static273.anInt4395, y1);
        @Pc(37) int clippedX0 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, x0);
        @Pc(43) int clippedX1 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, x1);
        @Pc(51) int topEnd = Static670.method8732(Static724.anInt10930, Static273.anInt4395, y0 + lineWidth);
        @Pc(60) int bottomStart = Static670.method8732(Static724.anInt10930, Static273.anInt4395, y1 - lineWidth);
        for (@Pc(62) int y = clippedY0; y < topEnd; y++) {
            Static696.method9037(clippedX1, lineColour, clippedX0, Static723.anIntArrayArray266[y]);
        }
        for (@Pc(84) int y = clippedY1; y > bottomStart; y--) {
            Static696.method9037(clippedX1, lineColour, clippedX0, Static723.anIntArrayArray266[y]);
        }
        @Pc(114) int leftEnd = Static670.method8732(Static180.anInt2995, Static111.anInt2219, lineWidth + x0);
        @Pc(123) int rightStart = Static670.method8732(Static180.anInt2995, Static111.anInt2219, x1 - lineWidth);
        for (@Pc(125) int y = topEnd; y <= bottomStart; y++) {
            @Pc(133) int[] row = Static723.anIntArrayArray266[y];
            Static696.method9037(leftEnd, lineColour, clippedX0, row);
            Static696.method9037(rightStart, fillColour, leftEnd, row);
            Static696.method9037(clippedX1, lineColour, rightStart, row);
        }
    }
}
