import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static446 {

    /**
     * Per tile record of what the software occluders hide, indexed by level, tile x and tile z.
     * A tile holds the current value of {@link Static675#anInt10155} once it has been found hidden
     * and the negated value once it has been found visible, so the counter's increment at the start
     * of a frame invalidates every entry at once.
     */
    @OriginalMember(owner = "client!oaa", name = "p", descriptor = "[[[I")
    public static int[][][] tileOcclusionCache;

    /**
     * Fills a rectangle and draws its border into the texture shape raster held in
     * {@link Static723#anIntArrayArray266}, for the case where the rectangle already lies inside the
     * raster bounds. {@link Static547#fillAndOutlineRectClipped} is the counterpart that clamps.
     */
    @OriginalMember(owner = "client!oaa", name = "a", descriptor = "(IIIZIIII)V")
    public static void fillAndOutlineRectUnclipped(@OriginalArg(0) int y1, @OriginalArg(1) int x1, @OriginalArg(2) int lineColour, @OriginalArg(4) int fillColour, @OriginalArg(5) int x0, @OriginalArg(6) int lineWidth, @OriginalArg(7) int y0) {
        @Pc(9) int topEnd = lineWidth + y0;
        @Pc(19) int bottomStart = y1 - lineWidth;
        for (@Pc(21) int y = y0; y < topEnd; y++) {
            Static696.method9037(x1, lineColour, x0, Static723.anIntArrayArray266[y]);
        }
        @Pc(45) int leftEnd = x0 + lineWidth;
        @Pc(50) int rightStart = x1 - lineWidth;
        for (@Pc(52) int y = y1; y > bottomStart; y--) {
            Static696.method9037(x1, lineColour, x0, Static723.anIntArrayArray266[y]);
        }
        for (@Pc(72) int y = topEnd; y <= bottomStart; y++) {
            @Pc(80) int[] row = Static723.anIntArrayArray266[y];
            Static696.method9037(leftEnd, lineColour, x0, row);
            Static696.method9037(rightStart, fillColour, leftEnd, row);
            Static696.method9037(x1, lineColour, rightStart, row);
        }
    }
}
