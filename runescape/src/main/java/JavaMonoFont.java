import com.jagex.IndexedImage;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.ClippingMask;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!vh")
public final class JavaMonoFont extends Font {

    @OriginalMember(owner = "client!vh", name = "B", descriptor = "Lclient!iaa;")
    public final JavaToolkit toolkit;

    @OriginalMember(owner = "client!vh", name = "C", descriptor = "[I")
    public final int[] glyphWidth;

    @OriginalMember(owner = "client!vh", name = "x", descriptor = "[I")
    public final int[] glyphHeight;

    @OriginalMember(owner = "client!vh", name = "D", descriptor = "[[B")
    public final byte[][] glyphRaster;

    @OriginalMember(owner = "client!vh", name = "z", descriptor = "[I")
    public final int[] glyphOffsetY;

    @OriginalMember(owner = "client!vh", name = "y", descriptor = "[I")
    public final int[] glyphOffsetX;

    @OriginalMember(owner = "client!vh", name = "<init>", descriptor = "(Lclient!iaa;Lclient!ve;[Lclient!wp;[I[I)V")
    public JavaMonoFont(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) FontMetrics metrics, @OriginalArg(2) IndexedImage[] glyphs, @OriginalArg(3) int[] glyphWidth, @OriginalArg(4) int[] glyphHeight) {
        super(toolkit, metrics);
        this.toolkit = toolkit;
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        this.glyphRaster = new byte[glyphs.length][];
        this.glyphOffsetY = new int[glyphs.length];
        this.glyphOffsetX = new int[glyphs.length];
        for (@Pc(29) int i = 0; i < glyphs.length; i++) {
            this.glyphRaster[i] = glyphs[i].raster;
            this.glyphOffsetY[i] = glyphs[i].offY1;
            this.glyphOffsetX[i] = glyphs[i].offX1;
        }
    }

    @OriginalMember(owner = "client!vh", name = "a", descriptor = "([B[IIIIIIIIIIILclient!aa;II)V")
    public void blitOpaqueMasked(@OriginalArg(0) byte[] src, @OriginalArg(1) int[] dst, @OriginalArg(2) int colour, @OriginalArg(3) int srcIndex, @OriginalArg(4) int dstIndex, @OriginalArg(5) int width, @OriginalArg(6) int height, @OriginalArg(7) int dstStep, @OriginalArg(8) int srcStep, @OriginalArg(9) int x, @OriginalArg(10) int y, @OriginalArg(11) int glyphWidth, @OriginalArg(12) ClippingMask mask, @OriginalArg(13) int maskX, @OriginalArg(14) int maskY) {
        @Pc(2) JavaClippingMask javaMask = (JavaClippingMask) mask;
        @Pc(5) int[] lineOffsets = javaMask.lineOffsets;
        @Pc(8) int[] lineWidths = javaMask.lineWidths;
        @Pc(10) int startY = y;
        if (maskY > y) {
            startY = maskY;
            dstIndex += (maskY - y) * this.toolkit.surfaceWidth;
            srcIndex += (maskY - y) * glyphWidth;
        }
        @Pc(50) int endY = maskY + lineOffsets.length < y + height ? maskY + lineOffsets.length : y + height;
        for (@Pc(52) int row = startY; row < endY; row++) {
            @Pc(61) int lineX = maskX + lineOffsets[row - maskY];
            @Pc(67) int lineWidth = lineWidths[row - maskY];
            @Pc(69) int remaining = width;
            @Pc(76) int skip;
            if (x > lineX) {
                skip = x - lineX;
                if (skip >= lineWidth) {
                    srcIndex += width + srcStep;
                    dstIndex += width + dstStep;
                    continue;
                }
                lineWidth -= skip;
            } else {
                skip = lineX - x;
                if (skip >= width) {
                    srcIndex += width + srcStep;
                    dstIndex += width + dstStep;
                    continue;
                }
                srcIndex += skip;
                remaining = width - skip;
                dstIndex += skip;
            }
            skip = 0;
            if (remaining < lineWidth) {
                lineWidth = remaining;
            } else {
                skip = remaining - lineWidth;
            }
            for (@Pc(143) int column = 0; column < lineWidth; column++) {
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    dst[dstIndex++] = colour;
                }
            }
            srcIndex += skip + srcStep;
            dstIndex += skip + dstStep;
        }
    }

    @OriginalMember(owner = "client!vh", name = "b", descriptor = "([B[IIIIIIIIIIILclient!aa;II)V")
    public void blitBlendedMasked(@OriginalArg(0) byte[] src, @OriginalArg(1) int[] dst, @OriginalArg(2) int colour, @OriginalArg(3) int srcIndex, @OriginalArg(4) int dstIndex, @OriginalArg(5) int width, @OriginalArg(6) int height, @OriginalArg(7) int dstStep, @OriginalArg(8) int srcStep, @OriginalArg(9) int x, @OriginalArg(10) int y, @OriginalArg(11) int glyphWidth, @OriginalArg(12) ClippingMask mask, @OriginalArg(13) int maskX, @OriginalArg(14) int maskY) {
        @Pc(2) JavaClippingMask javaMask = (JavaClippingMask) mask;
        @Pc(5) int[] lineOffsets = javaMask.lineOffsets;
        @Pc(8) int[] lineWidths = javaMask.lineWidths;
        @Pc(14) int clipOffsetX = x - this.toolkit.clipX1;
        @Pc(16) int startY = y;
        if (maskY > y) {
            startY = maskY;
            dstIndex += (maskY - y) * this.toolkit.surfaceWidth;
            srcIndex += (maskY - y) * glyphWidth;
        }
        @Pc(56) int endY = maskY + lineOffsets.length < y + height ? maskY + lineOffsets.length : y + height;
        @Pc(60) int alpha = colour >>> 24;
        @Pc(64) int invAlpha = 255 - alpha;
        for (@Pc(66) int row = startY; row < endY; row++) {
            @Pc(75) int lineX = lineOffsets[row - maskY] + maskX;
            @Pc(81) int lineWidth = lineWidths[row - maskY];
            @Pc(83) int remaining = width;
            @Pc(90) int skip;
            if (clipOffsetX > lineX) {
                skip = clipOffsetX - lineX;
                if (skip >= lineWidth) {
                    srcIndex += width + srcStep;
                    dstIndex += width + dstStep;
                    continue;
                }
                lineWidth -= skip;
            } else {
                skip = lineX - clipOffsetX;
                if (skip >= width) {
                    srcIndex += width + srcStep;
                    dstIndex += width + dstStep;
                    continue;
                }
                srcIndex += skip;
                remaining = width - skip;
                dstIndex += skip;
            }
            skip = 0;
            if (remaining < lineWidth) {
                lineWidth = remaining;
            } else {
                skip = remaining - lineWidth;
            }
            for (@Pc(158) int column = -lineWidth; column < 0; column++) {
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    @Pc(182) int blendedColour = ((colour & 0xFF00FF) * alpha & 0xFF00FF00) + ((colour & 0xFF00) * alpha & 0xFF0000) >> 8;
                    @Pc(186) int background = dst[dstIndex];
                    dst[dstIndex++] = (((background & 0xFF00FF) * invAlpha & 0xFF00FF00) + ((background & 0xFF00) * invAlpha & 0xFF0000) >> 8) + blendedColour;
                }
            }
            srcIndex += skip + srcStep;
            dstIndex += skip + dstStep;
        }
    }

    @OriginalMember(owner = "client!vh", name = "fa", descriptor = "(CIIIZ)V")
    @Override
    protected void fa(@OriginalArg(0) char c, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int colour, @OriginalArg(4) boolean shadow) {
        x += this.glyphOffsetX[c];
        y += this.glyphOffsetY[c];
        @Pc(18) int width = this.glyphWidth[c];
        @Pc(23) int height = this.glyphHeight[c];
        @Pc(27) int dstStride = this.toolkit.surfaceWidth;
        @Pc(33) int dstIndex = x + y * dstStride;
        @Pc(37) int dstStep = dstStride - width;
        @Pc(39) int srcStep = 0;
        @Pc(41) int srcIndex = 0;
        @Pc(52) int clip;
        if (y < this.toolkit.clipY1) {
            clip = this.toolkit.clipY1 - y;
            height -= clip;
            y = this.toolkit.clipY1;
            srcIndex += clip * width;
            dstIndex += clip * dstStride;
        }
        if (y + height > this.toolkit.clipY2) {
            height -= y + height - this.toolkit.clipY2;
        }
        if (x < this.toolkit.clipX1) {
            clip = this.toolkit.clipX1 - x;
            width -= clip;
            x = this.toolkit.clipX1;
            srcIndex += clip;
            dstIndex += clip;
            srcStep += clip;
            dstStep += clip;
        }
        if (x + width > this.toolkit.clipX2) {
            clip = x + width - this.toolkit.clipX2;
            width -= clip;
            srcStep += clip;
            dstStep += clip;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        if ((colour & -16777216) == -16777216) {
            this.blitOpaque(this.glyphRaster[c], this.toolkit.surfaceRaster, colour, srcIndex, dstIndex, width, height, dstStep, srcStep);
        } else if ((colour & 0xFF000000) != 0) {
            this.blitBlended(this.glyphRaster[c], this.toolkit.surfaceRaster, colour, srcIndex, dstIndex, width, height, dstStep, srcStep);
        }
    }

    @OriginalMember(owner = "client!vh", name = "a", descriptor = "(CIIIZLclient!aa;II)V")
    @Override
    protected void renderSymbol(@OriginalArg(0) char c, @OriginalArg(1) int x, @OriginalArg(2) int y, @OriginalArg(3) int colour, @OriginalArg(4) boolean shadow, @OriginalArg(5) ClippingMask mask, @OriginalArg(6) int offsetX, @OriginalArg(7) int offsetY) {
        if (mask == null) {
            this.fa(c, x, y, colour, shadow);
            return;
        }
        x += this.glyphOffsetX[c];
        y += this.glyphOffsetY[c];
        @Pc(28) int width = this.glyphWidth[c];
        @Pc(33) int height = this.glyphHeight[c];
        @Pc(37) int dstStride = this.toolkit.surfaceWidth;
        @Pc(43) int dstIndex = x + y * dstStride;
        @Pc(47) int dstStep = dstStride - width;
        @Pc(49) int srcStep = 0;
        @Pc(51) int srcIndex = 0;
        @Pc(62) int clip;
        if (y < this.toolkit.clipY1) {
            clip = this.toolkit.clipY1 - y;
            height -= clip;
            y = this.toolkit.clipY1;
            srcIndex = clip * width;
            dstIndex += clip * dstStride;
        }
        if (y + height > this.toolkit.clipY2) {
            height -= y + height - this.toolkit.clipY2;
        }
        if (x < this.toolkit.clipX1) {
            clip = this.toolkit.clipX1 - x;
            width -= clip;
            x = this.toolkit.clipX1;
            srcIndex += clip;
            dstIndex += clip;
            srcStep = clip;
            dstStep += clip;
        }
        if (x + width > this.toolkit.clipX2) {
            clip = x + width - this.toolkit.clipX2;
            width -= clip;
            srcStep += clip;
            dstStep += clip;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        if ((colour & -16777216) == -16777216) {
            this.blitOpaqueMasked(this.glyphRaster[c], this.toolkit.surfaceRaster, colour, srcIndex, dstIndex, width, height, dstStep, srcStep, x, y, this.glyphWidth[c], mask, offsetX, offsetY);
        } else {
            this.blitBlendedMasked(this.glyphRaster[c], this.toolkit.surfaceRaster, colour, srcIndex, dstIndex, width, height, dstStep, srcStep, x, y, this.glyphWidth[c], mask, offsetX, offsetY);
        }
    }

    @OriginalMember(owner = "client!vh", name = "b", descriptor = "([B[IIIIIIII)V")
    public void blitBlended(@OriginalArg(0) byte[] src, @OriginalArg(1) int[] dst, @OriginalArg(2) int colour, @OriginalArg(3) int srcIndex, @OriginalArg(4) int dstIndex, @OriginalArg(5) int width, @OriginalArg(6) int height, @OriginalArg(7) int dstStep, @OriginalArg(8) int srcStep) {
        @Pc(3) int alpha = colour >>> 24;
        @Pc(7) int invAlpha = 255 - alpha;
        for (@Pc(10) int row = -height; row < 0; row++) {
            for (@Pc(14) int column = -width; column < 0; column++) {
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    @Pc(38) int blendedColour = ((colour & 0xFF00FF) * alpha & 0xFF00FF00) + ((colour & 0xFF00) * alpha & 0xFF0000) >> 8;
                    @Pc(42) int background = dst[dstIndex];
                    dst[dstIndex++] = (((background & 0xFF00FF) * invAlpha & 0xFF00FF00) + ((background & 0xFF00) * invAlpha & 0xFF0000) >> 8) + blendedColour;
                }
            }
            dstIndex += dstStep;
            srcIndex += srcStep;
        }
    }

    @OriginalMember(owner = "client!vh", name = "a", descriptor = "([B[IIIIIIII)V")
    public void blitOpaque(@OriginalArg(0) byte[] src, @OriginalArg(1) int[] dst, @OriginalArg(2) int colour, @OriginalArg(3) int srcIndex, @OriginalArg(4) int dstIndex, @OriginalArg(5) int width, @OriginalArg(6) int height, @OriginalArg(7) int dstStep, @OriginalArg(8) int srcStep) {
        @Pc(4) int blocks = -(width >> 2);
        @Pc(9) int remainder = -(width & 0x3);
        for (@Pc(12) int row = -height; row < 0; row++) {
            for (@Pc(15) int block = blocks; block < 0; block++) {
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    dst[dstIndex++] = colour;
                }
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    dst[dstIndex++] = colour;
                }
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    dst[dstIndex++] = colour;
                }
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    dst[dstIndex++] = colour;
                }
            }
            for (@Pc(69) int column = remainder; column < 0; column++) {
                if (src[srcIndex++] == 0) {
                    dstIndex++;
                } else {
                    dst[dstIndex++] = colour;
                }
            }
            dstIndex += dstStep;
            srcIndex += srcStep;
        }
    }
}
