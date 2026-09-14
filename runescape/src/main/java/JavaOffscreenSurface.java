import com.jagex.graphics.OffscreenSurface;
import com.jagex.graphics.Sprite;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!du")
public final class JavaOffscreenSurface implements OffscreenSurface {

    @OriginalMember(owner = "client!du", name = "i", descriptor = "Lclient!iaa;")
    public final JavaToolkit toolkit;

    @OriginalMember(owner = "client!du", name = "a", descriptor = "[I")
    public final int[] raster;

    @OriginalMember(owner = "client!du", name = "n", descriptor = "I")
    public final int width;

    @OriginalMember(owner = "client!du", name = "g", descriptor = "I")
    public final int height;

    @OriginalMember(owner = "client!du", name = "l", descriptor = "Lclient!hia;")
    public JavaDepthBuffer depth;

    @OriginalMember(owner = "client!du", name = "b", descriptor = "[F")
    public float[] depthBuffer;

    @OriginalMember(owner = "client!du", name = "<init>", descriptor = "(Lclient!iaa;Lclient!st;Lclient!hia;)V")
    public JavaOffscreenSurface(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) Sprite sprite, @OriginalArg(2) JavaDepthBuffer depth) {
        this.toolkit = toolkit;
        if (sprite instanceof JavaRgbSprite) {
            @Pc(35) JavaRgbSprite rgb = (JavaRgbSprite) sprite;
            this.width = rgb.width;
            this.height = rgb.height;
            this.raster = rgb.pixels;
        } else if (sprite instanceof JavaArgbSprite) {
            @Pc(13) JavaArgbSprite argb = (JavaArgbSprite) sprite;
            this.raster = argb.pixels;
            this.width = argb.width;
            this.height = argb.height;
        } else {
            throw new RuntimeException();
        }
        if (depth != null) {
            this.depth = depth;
            if (this.width != this.depth.width || this.depth.height != this.height) {
                throw new RuntimeException();
            }
            this.depthBuffer = this.depth.depths;
        }
    }

    /**
     * Copies a rectangle out of the toolkit's main surface into the top left corner of this
     * offscreen surface. The depth values always travel with it, the colour raster only when
     * copyRaster is set.
     */
    @OriginalMember(owner = "client!du", name = "a", descriptor = "(IIIIIIZZ)V")
    @Override
    public void method9039(@OriginalArg(0) int srcX, @OriginalArg(1) int srcY, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(6) boolean copyRaster) {
        Static22.method588(0, width, this.toolkit.mainDepthBuffer, copyRaster ? this.toolkit.surface.raster : null, this.toolkit.surface.width, this.depthBuffer, this.width, 0, this.raster, height, srcX, srcY);
    }

    /**
     * Copies a rectangle out of this offscreen surface back into the toolkit's main surface,
     * moving both the colour raster and the depth values.
     */
    @OriginalMember(owner = "client!du", name = "b", descriptor = "(IIIIIIZZ)V")
    @Override
    public void method9040(@OriginalArg(0) int srcX, @OriginalArg(1) int srcY, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int dstX, @OriginalArg(5) int dstY) {
        Static22.method588(dstY, width, this.depthBuffer, this.raster, this.width, this.toolkit.mainDepthBuffer, this.toolkit.surface.width, dstX, this.toolkit.surface.raster, height, srcX, srcY);
    }
}
