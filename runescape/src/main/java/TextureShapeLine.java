import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * A straight segment stroked with the line colour. It is built with no fill colour, so only
 * {@link #outline} is ever reached.
 */
@OriginalClass("client!qba")
public final class TextureShapeLine extends TextureShape {

    @OriginalMember(owner = "client!qba", name = "p", descriptor = "I")
    public final int endY;

    @OriginalMember(owner = "client!qba", name = "s", descriptor = "I")
    public final int startY;

    @OriginalMember(owner = "client!qba", name = "u", descriptor = "I")
    public final int startX;

    @OriginalMember(owner = "client!qba", name = "o", descriptor = "I")
    public final int endX;

    @OriginalMember(owner = "client!qba", name = "<init>", descriptor = "(IIIIII)V")
    public TextureShapeLine(@OriginalArg(0) int startX, @OriginalArg(1) int startY, @OriginalArg(2) int endX, @OriginalArg(3) int endY, @OriginalArg(4) int lineColour, @OriginalArg(5) int lineWidth) {
        super(-1, lineColour, lineWidth);
        this.endY = endY;
        this.startY = startY;
        this.startX = startX;
        this.endX = endX;
    }

    @OriginalMember(owner = "client!qba", name = "c", descriptor = "(III)V")
    @Override
    public void outline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        @Pc(20) int x0 = this.startX * width >> 12;
        @Pc(27) int x1 = width * this.endX >> 12;
        @Pc(34) int y0 = this.startY * height >> 12;
        @Pc(41) int y1 = height * this.endY >> 12;
        Static418.method7862(x1, y1, x0, super.lineColour, y0);
    }

    @OriginalMember(owner = "client!qba", name = "b", descriptor = "(III)V")
    @Override
    public void fill(@OriginalArg(0) int width, @OriginalArg(1) int height) {
        /* empty */
    }

    @OriginalMember(owner = "client!qba", name = "a", descriptor = "(III)V")
    @Override
    public void fillAndOutline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        /* empty */
    }
}
