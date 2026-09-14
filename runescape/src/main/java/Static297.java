import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static297 {

    /**
     * Fills the pixels of row {@code y} between {@code x0} and {@code x1} with {@code rgb}. The two
     * ends may arrive in either order. Nothing is clipped, so both ends must already lie inside the
     * clip bounds.
     */
    @OriginalMember(owner = "client!jha", name = "a", descriptor = "(IIIII)V")
    public static void drawHorizontalLineUnclipped(@OriginalArg(0) int y, @OriginalArg(1) int x1, @OriginalArg(3) int rgb, @OriginalArg(4) int x0) {
        if (x0 <= x1) {
            Static696.fillHorizontalSpan(x1, rgb, x0, Static723.anIntArrayArray266[y]);
        } else {
            Static696.fillHorizontalSpan(x0, rgb, x1, Static723.anIntArrayArray266[y]);
        }
    }
}
