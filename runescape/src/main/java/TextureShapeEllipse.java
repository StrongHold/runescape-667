import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!kk")
public final class TextureShapeEllipse extends TextureShape {

    @OriginalMember(owner = "client!kk", name = "r", descriptor = "I")
    public final int radiusX;

    @OriginalMember(owner = "client!kk", name = "s", descriptor = "I")
    public final int radiusY;

    @OriginalMember(owner = "client!kk", name = "t", descriptor = "I")
    public final int centerX;

    @OriginalMember(owner = "client!kk", name = "m", descriptor = "I")
    public final int centerY;

    @OriginalMember(owner = "client!kk", name = "<init>", descriptor = "(IIIIIII)V")
    public TextureShapeEllipse(@OriginalArg(0) int centerX, @OriginalArg(1) int centerY, @OriginalArg(2) int radiusX, @OriginalArg(3) int radiusY, @OriginalArg(4) int fillColour, @OriginalArg(5) int lineColour, @OriginalArg(6) int lineWidth) {
        super(fillColour, lineColour, lineWidth);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @OriginalMember(owner = "client!kk", name = "c", descriptor = "(III)V")
    @Override
    public void outline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        /* empty */
    }

    @OriginalMember(owner = "client!kk", name = "a", descriptor = "(III)V")
    @Override
    public void fillAndOutline(@OriginalArg(0) int height, @OriginalArg(2) int width) {
        @Pc(18) int x = this.centerX * width >> 12;
        @Pc(25) int rx = this.radiusX * width >> 12;
        @Pc(32) int y = this.centerY * height >> 12;
        @Pc(39) int ry = height * this.radiusY >> 12;
        Static136.method2347(y, super.lineWidth, ry, x, super.lineColour, super.fillColour, rx);
    }

    @OriginalMember(owner = "client!kk", name = "b", descriptor = "(III)V")
    @Override
    public void fill(@OriginalArg(0) int width, @OriginalArg(1) int height) {
        @Pc(10) int x = this.centerX * width >> 12;
        @Pc(17) int rx = this.radiusX * width >> 12;
        @Pc(24) int y = height * this.centerY >> 12;
        @Pc(31) int ry = height * this.radiusY >> 12;
        Static180.method2776(ry, super.fillColour, y, rx, x);
    }
}
