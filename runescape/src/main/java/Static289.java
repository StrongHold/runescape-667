import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static289 {

    @OriginalMember(owner = "client!jba", name = "b", descriptor = "F")
    public static float aFloat84;

    @OriginalMember(owner = "client!jba", name = "a", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___19 = new CutsceneActionType(1);

    /**
     * Draws a horizontal line of {@code rgb} on row {@code y} between {@code x0} and {@code x1},
     * clipped to the current clip bounds. The row is dropped when it falls outside the vertical
     * bounds, and both ends are clamped to the horizontal bounds.
     */
    @OriginalMember(owner = "client!jba", name = "a", descriptor = "(IIIII)V")
    public static void drawHorizontalLineClipped(@OriginalArg(0) int x1, @OriginalArg(1) int y, @OriginalArg(3) int rgb, @OriginalArg(4) int x0) {
        if (Static724.anInt10930 <= y && y <= Static273.anInt4395) {
            @Pc(26) int clippedX0 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, x0);
            @Pc(32) int clippedX1 = Static670.method8732(Static180.anInt2995, Static111.anInt2219, x1);
            Static297.drawHorizontalLineUnclipped(y, clippedX1, rgb, clippedX0);
        }
    }
}
