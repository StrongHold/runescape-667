import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!hba")
public final class TextureShapeRectangle extends TextureShape {

    @OriginalMember(owner = "client!hba", name = "l", descriptor = "I")
    public final int top;

    @OriginalMember(owner = "client!hba", name = "o", descriptor = "I")
    public final int left;

    @OriginalMember(owner = "client!hba", name = "t", descriptor = "I")
    public final int right;

    @OriginalMember(owner = "client!hba", name = "s", descriptor = "I")
    public final int bottom;

    @OriginalMember(owner = "client!hba", name = "<init>", descriptor = "(IIIIIII)V")
    public TextureShapeRectangle(@OriginalArg(0) int left, @OriginalArg(1) int top, @OriginalArg(2) int right, @OriginalArg(3) int bottom, @OriginalArg(4) int fillColour, @OriginalArg(5) int lineColour, @OriginalArg(6) int lineWidth) {
        super(fillColour, lineColour, lineWidth);
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    @OriginalMember(owner = "client!hba", name = "c", descriptor = "(III)V")
    @Override
    public void outline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        @Pc(10) int x0 = width * this.left >> 12;
        @Pc(17) int x1 = this.right * width >> 12;
        @Pc(29) int y0 = height * this.top >> 12;
        @Pc(36) int y1 = this.bottom * height >> 12;
        Static168.method2637(y1, super.lineColour, super.lineWidth, x1, y0, x0);
    }

    @OriginalMember(owner = "client!hba", name = "a", descriptor = "(III)V")
    @Override
    public void fillAndOutline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        @Pc(15) int x0 = this.left * width >> 12;
        @Pc(22) int x1 = width * this.right >> 12;
        @Pc(29) int y0 = height * this.top >> 12;
        @Pc(36) int y1 = height * this.bottom >> 12;
        Static264.method9459(super.fillColour, x1, super.lineColour, y0, y1, x0, super.lineWidth);
    }

    @OriginalMember(owner = "client!hba", name = "b", descriptor = "(III)V")
    @Override
    public void fill(@OriginalArg(0) int width, @OriginalArg(1) int height) {
        @Pc(10) int x0 = this.left * width >> 12;
        @Pc(22) int x1 = width * this.right >> 12;
        @Pc(29) int y0 = this.top * height >> 12;
        @Pc(36) int y1 = height * this.bottom >> 12;
        Static624.method8330(x1, x0, super.fillColour, y1, y0);
    }
}
