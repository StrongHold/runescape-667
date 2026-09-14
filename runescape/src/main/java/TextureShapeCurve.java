import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * A cubic bezier stroked with the line colour. It is built with no fill colour, so only
 * {@link #outline} is ever reached.
 */
@OriginalClass("client!pfa")
public final class TextureShapeCurve extends TextureShape {

    @OriginalMember(owner = "client!pfa", name = "t", descriptor = "I")
    public final int endY;

    @OriginalMember(owner = "client!pfa", name = "s", descriptor = "I")
    public final int controlEndY;

    @OriginalMember(owner = "client!pfa", name = "w", descriptor = "I")
    public final int controlEndX;

    @OriginalMember(owner = "client!pfa", name = "r", descriptor = "I")
    public final int endX;

    @OriginalMember(owner = "client!pfa", name = "k", descriptor = "I")
    public final int startX;

    @OriginalMember(owner = "client!pfa", name = "n", descriptor = "I")
    public final int controlStartY;

    @OriginalMember(owner = "client!pfa", name = "x", descriptor = "I")
    public final int controlStartX;

    @OriginalMember(owner = "client!pfa", name = "l", descriptor = "I")
    public final int startY;

    @OriginalMember(owner = "client!pfa", name = "<init>", descriptor = "(IIIIIIIIII)V")
    public TextureShapeCurve(@OriginalArg(0) int startX, @OriginalArg(1) int startY, @OriginalArg(2) int controlStartX, @OriginalArg(3) int controlStartY, @OriginalArg(4) int controlEndX, @OriginalArg(5) int controlEndY, @OriginalArg(6) int endX, @OriginalArg(7) int endY, @OriginalArg(8) int lineColour, @OriginalArg(9) int lineWidth) {
        super(-1, lineColour, lineWidth);
        this.endY = endY;
        this.controlEndY = controlEndY;
        this.controlEndX = controlEndX;
        this.endX = endX;
        this.startX = startX;
        this.controlStartY = controlStartY;
        this.controlStartX = controlStartX;
        this.startY = startY;
    }

    @OriginalMember(owner = "client!pfa", name = "c", descriptor = "(III)V")
    @Override
    public void outline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        @Pc(10) int x0 = width * this.startX >> 12;
        @Pc(17) int y0 = this.startY * height >> 12;
        @Pc(24) int x1 = this.controlStartX * width >> 12;
        @Pc(31) int y1 = height * this.controlStartY >> 12;
        @Pc(38) int x2 = width * this.controlEndX >> 12;
        @Pc(50) int y2 = this.controlEndY * height >> 12;
        @Pc(57) int x3 = width * this.endX >> 12;
        @Pc(64) int y3 = this.endY * height >> 12;
        Static316.method7478(x0, y1, y2, super.lineColour, x2, y3, y0, x3, x1);
    }

    @OriginalMember(owner = "client!pfa", name = "a", descriptor = "(III)V")
    @Override
    public void fillAndOutline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        /* empty */
    }

    @OriginalMember(owner = "client!pfa", name = "b", descriptor = "(III)V")
    @Override
    public void fill(@OriginalArg(0) int width, @OriginalArg(1) int height) {
        /* empty */
    }
}
