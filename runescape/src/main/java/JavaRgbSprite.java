import com.jagex.graphics.ClippingMask;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ap")
public final class JavaRgbSprite extends JavaSprite {

    @OriginalMember(owner = "client!ap", name = "D", descriptor = "[I")
    public final int[] pixels;

    @OriginalMember(owner = "client!ap", name = "<init>", descriptor = "(Lclient!iaa;[III)V")
    public JavaRgbSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int[] pixels, @OriginalArg(2) int width, @OriginalArg(3) int height) {
        super(toolkit, width, height);
        this.pixels = pixels;
    }

    @OriginalMember(owner = "client!ap", name = "<init>", descriptor = "(Lclient!iaa;II)V")
    public JavaRgbSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int width, @OriginalArg(2) int height) {
        super(toolkit, width, height);
        this.pixels = new int[width * height];
    }

    @OriginalMember(owner = "client!ap", name = "<init>", descriptor = "(Lclient!iaa;[IIIIIZ)V")
    public JavaRgbSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int[] pixels, @OriginalArg(2) int offset, @OriginalArg(3) int stride, @OriginalArg(4) int width, @OriginalArg(5) int height, @OriginalArg(6) boolean copy) {
        super(toolkit, width, height);
        if (copy) {
            this.pixels = new int[width * height];
        } else {
            this.pixels = pixels;
        }
        @Pc(21) int step = stride - super.width;
        @Pc(23) int index = 0;
        for (@Pc(25) int row = 0; row < height; row++) {
            for (@Pc(28) int column = 0; column < width; column++) {
                @Pc(34) int argb = pixels[offset++];
                if (argb >>> 24 == 255) {
                    this.pixels[index++] = (argb & 0xFFFFFF) == 0 ? -16777215 : argb;
                } else {
                    this.pixels[index++] = 0;
                }
            }
            offset += step;
        }
    }

    @OriginalMember(owner = "client!ap", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void copyRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int srcX, @OriginalArg(5) int srcY) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        for (@Pc(5) int row = 0; row < height; row++) {
            @Pc(15) int dstIndex = (y + row) * super.width + x;
            @Pc(25) int srcIndex = (srcY + row) * super.toolkit.surfaceWidth + srcX;
            for (@Pc(27) int column = 0; column < width; column++) {
                this.pixels[dstIndex + column] = raster[srcIndex + column];
            }
        }
    }

    @OriginalMember(owner = "client!ap", name = "b", descriptor = "(IIIIIIIII)V")
    @Override
    public void method8207(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int op, @OriginalArg(6) int colour, @OriginalArg(7) int mode) {
        if (width <= 0 || height <= 0) {
            return;
        }
        @Pc(9) int u = 0;
        @Pc(11) int v = 0;
        @Pc(20) int scaleWidth = super.leftMargin + super.width + super.rightMargin;
        @Pc(29) int scaleHeight = super.topMargin + super.height + super.bottomMargin;
        @Pc(35) int uStep = (scaleWidth << 16) / width;
        @Pc(41) int vStep = (scaleHeight << 16) / height;
        @Pc(55) int offset;
        if (super.leftMargin > 0) {
            offset = ((super.leftMargin << 16) + uStep - 1) / uStep;
            x += offset;
            u = offset * uStep - (super.leftMargin << 16);
        }
        if (super.topMargin > 0) {
            offset = ((super.topMargin << 16) + vStep - 1) / vStep;
            y += offset;
            v = offset * vStep - (super.topMargin << 16);
        }
        if (super.width < scaleWidth) {
            width = ((super.width << 16) + uStep - u - 1) / uStep;
        }
        if (super.height < scaleHeight) {
            height = ((super.height << 16) + vStep - v - 1) / vStep;
        }
        offset = x + y * super.toolkit.surfaceWidth;
        @Pc(147) int dstStep = super.toolkit.surfaceWidth - width;
        if (y + height > super.toolkit.clipY2) {
            height -= y + height - super.toolkit.clipY2;
        }
        @Pc(175) int clip;
        if (y < super.toolkit.clipY1) {
            clip = super.toolkit.clipY1 - y;
            height -= clip;
            offset += clip * super.toolkit.surfaceWidth;
            v += vStep * clip;
        }
        if (x + width > super.toolkit.clipX2) {
            clip = x + width - super.toolkit.clipX2;
            width -= clip;
            dstStep += clip;
        }
        if (x < super.toolkit.clipX1) {
            clip = super.toolkit.clipX1 - x;
            width -= clip;
            offset += clip;
            u += uStep * clip;
            dstStep += clip;
        }
        @Pc(249) float[] depth = super.toolkit.depthBuffer;
        @Pc(253) int[] raster = super.toolkit.surfaceRaster;
        @Pc(262) int local262;
        @Pc(265) int local265;
        @Pc(273) int local273;
        @Pc(276) int local276;
        @Pc(348) int local348;
        @Pc(356) int local356;
        @Pc(359) int local359;
        @Pc(376) int local376;
        @Pc(384) int local384;
        @Pc(392) int local392;
        @Pc(400) int local400;
        @Pc(569) int lerpColour;
        if (mode != 0) {
            @Pc(929) int local929;
            @Pc(937) int local937;
            @Pc(949) int local949;
            if (mode == 1) {
                if (op == 1) {
                    local262 = u;
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.width;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local348 = this.pixels[(u >> 16) + local273];
                                if (local348 != 0) {
                                    raster[offset] = local348;
                                    depth[offset] = (float) z;
                                }
                            }
                            u += uStep;
                            offset++;
                        }
                        v += vStep;
                        u = local262;
                        offset += dstStep;
                    }
                } else if (op == 0) {
                    local262 = u;
                    if ((colour & 0xFFFFFF) == 16777215) {
                        local265 = colour >>> 24;
                        local273 = 256 - local265;
                        for (local276 = -height; local276 < 0; local276++) {
                            local348 = (v >> 16) * super.width;
                            for (local356 = -width; local356 < 0; local356++) {
                                if ((float) z < depth[offset]) {
                                    local359 = this.pixels[(u >> 16) + local348];
                                    if (local359 != 0) {
                                        local376 = raster[offset];
                                        raster[offset] = ((local359 & 0xFF00FF) * local265 + (local376 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local359 & 0xFF00) * local265 + (local376 & 0xFF00) * local273 & 0xFF0000) >> 8;
                                        depth[offset] = (float) z;
                                    }
                                }
                                u += uStep;
                                offset++;
                            }
                            v += vStep;
                            u = local262;
                            offset += dstStep;
                        }
                    } else {
                        local265 = colour >> 16 & 0xFF;
                        local273 = colour >> 8 & 0xFF;
                        local276 = colour & 0xFF;
                        local348 = colour >>> 24;
                        local356 = 256 - local348;
                        for (local359 = -height; local359 < 0; local359++) {
                            local376 = (v >> 16) * super.width;
                            for (local384 = -width; local384 < 0; local384++) {
                                if ((float) z < depth[offset]) {
                                    local392 = this.pixels[(u >> 16) + local376];
                                    if (local392 != 0) {
                                        if (local348 == 255) {
                                            local400 = (local392 & 0xFF0000) * local265 & 0xFF000000;
                                            local929 = (local392 & 0xFF00) * local273 & 0xFF0000;
                                            local937 = (local392 & 0xFF) * local276 & 0xFF00;
                                            raster[offset] = (local400 | local929 | local937) >>> 8;
                                            depth[offset] = (float) z;
                                        } else {
                                            local400 = (local392 & 0xFF0000) * local265 & 0xFF000000;
                                            local929 = (local392 & 0xFF00) * local273 & 0xFF0000;
                                            local937 = (local392 & 0xFF) * local276 & 0xFF00;
                                            local392 = (local400 | local929 | local937) >>> 8;
                                            local949 = raster[offset];
                                            raster[offset] = ((local392 & 0xFF00FF) * local348 + (local949 & 0xFF00FF) * local356 & 0xFF00FF00) + ((local392 & 0xFF00) * local348 + (local949 & 0xFF00) * local356 & 0xFF0000) >> 8;
                                            depth[offset] = (float) z;
                                        }
                                    }
                                }
                                u += uStep;
                                offset++;
                            }
                            v += vStep;
                            u = local262;
                            offset += dstStep;
                        }
                    }
                } else if (op == 3) {
                    local262 = u;
                    local265 = colour >>> 24;
                    local273 = 256 - local265;
                    for (local276 = -height; local276 < 0; local276++) {
                        local348 = (v >> 16) * super.width;
                        for (local356 = -width; local356 < 0; local356++) {
                            if ((float) z < depth[offset]) {
                                local359 = this.pixels[(u >> 16) + local348];
                                local376 = local359 + colour;
                                local384 = (local359 & 0xFF00FF) + (colour & 0xFF00FF);
                                local392 = (local384 & 0x1000100) + (local376 - local384 & 0x10000);
                                local392 = local376 - local392 | local392 - (local392 >>> 8);
                                if (local359 == 0 && local265 != 255) {
                                    local359 = local392;
                                    local392 = raster[offset];
                                    local392 = ((local359 & 0xFF00FF) * local265 + (local392 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local359 & 0xFF00) * local265 + (local392 & 0xFF00) * local273 & 0xFF0000) >> 8;
                                }
                                raster[offset] = local392;
                                depth[offset] = (float) z;
                            }
                            u += uStep;
                            offset++;
                        }
                        v += vStep;
                        u = local262;
                        offset += dstStep;
                    }
                } else if (op == 2) {
                    local262 = colour >>> 24;
                    local265 = 256 - local262;
                    local273 = (colour & 0xFF00FF) * local265 & 0xFF00FF00;
                    local276 = (colour & 0xFF00) * local265 & 0xFF0000;
                    lerpColour = (local273 | local276) >>> 8;
                    local348 = u;
                    for (local356 = -height; local356 < 0; local356++) {
                        local359 = (v >> 16) * super.width;
                        for (local376 = -width; local376 < 0; local376++) {
                            if ((float) z < depth[offset]) {
                                local384 = this.pixels[(u >> 16) + local359];
                                if (local384 != 0) {
                                    local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                    local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                    raster[offset] = ((local273 | local276) >>> 8) + lerpColour;
                                    depth[offset] = (float) z;
                                }
                            }
                            u += uStep;
                            offset++;
                        }
                        v += vStep;
                        u = local348;
                        offset += dstStep;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.width;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.pixels[(u >> 16) + local273];
                            if (local348 != 0) {
                                local356 = raster[offset];
                                local359 = local348 + local356;
                                local376 = (local348 & 0xFF00FF) + (local356 & 0xFF00FF);
                                local356 = (local376 & 0x1000100) + (local359 - local376 & 0x10000);
                                raster[offset] = local359 - local356 | local356 - (local356 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local262;
                    offset += dstStep;
                }
            } else if (op == 0) {
                local262 = u;
                local265 = colour >> 16 & 0xFF;
                local273 = colour >> 8 & 0xFF;
                local276 = colour & 0xFF;
                for (local348 = -height; local348 < 0; local348++) {
                    local356 = (v >> 16) * super.width;
                    for (local359 = -width; local359 < 0; local359++) {
                        if ((float) z < depth[offset]) {
                            local376 = this.pixels[(u >> 16) + local356];
                            if (local376 != 0) {
                                local384 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                local392 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                local400 = (local376 & 0xFF) * local276 & 0xFF00;
                                local376 = (local384 | local392 | local400) >>> 8;
                                local929 = raster[offset];
                                local937 = local376 + local929;
                                local949 = (local376 & 0xFF00FF) + (local929 & 0xFF00FF);
                                local929 = (local949 & 0x1000100) + (local937 - local949 & 0x10000);
                                raster[offset] = local937 - local929 | local929 - (local929 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local262;
                    offset += dstStep;
                }
            } else if (op == 3) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.width;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.pixels[(u >> 16) + local273];
                            local356 = local348 + colour;
                            local359 = (local348 & 0xFF00FF) + (colour & 0xFF00FF);
                            local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                            local348 = local356 - local376 | local376 - (local376 >>> 8);
                            local376 = raster[offset];
                            local356 = local348 + local376;
                            local359 = (local348 & 0xFF00FF) + (local376 & 0xFF00FF);
                            local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                            raster[offset] = local356 - local376 | local376 - (local376 >>> 8);
                            depth[offset] = (float) z;
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local262;
                    offset += dstStep;
                }
            } else if (op == 2) {
                local262 = colour >>> 24;
                local265 = 256 - local262;
                local273 = (colour & 0xFF00FF) * local265 & 0xFF00FF00;
                local276 = (colour & 0xFF00) * local265 & 0xFF0000;
                lerpColour = (local273 | local276) >>> 8;
                local348 = u;
                for (local356 = -height; local356 < 0; local356++) {
                    local359 = (v >> 16) * super.width;
                    for (local376 = -width; local376 < 0; local376++) {
                        if ((float) z < depth[offset]) {
                            local384 = this.pixels[(u >> 16) + local359];
                            if (local384 != 0) {
                                local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                local384 = ((local273 | local276) >>> 8) + lerpColour;
                                local392 = raster[offset];
                                local400 = local384 + local392;
                                local929 = (local384 & 0xFF00FF) + (local392 & 0xFF00FF);
                                @Pc(1838) int local1838 = (local929 & 0x1000100) + (local400 - local929 & 0x10000);
                                raster[offset] = local400 - local1838 | local1838 - (local1838 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local348;
                    offset += dstStep;
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else if (op == 1) {
            local262 = u;
            for (local265 = -height; local265 < 0; local265++) {
                local273 = (v >> 16) * super.width;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        raster[offset] = this.pixels[(u >> 16) + local273];
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local262;
                offset += dstStep;
            }
        } else if (op == 0) {
            local262 = colour >> 16 & 0xFF;
            local265 = colour >> 8 & 0xFF;
            local273 = colour & 0xFF;
            local276 = u;
            for (local348 = -height; local348 < 0; local348++) {
                local356 = (v >> 16) * super.width;
                for (local359 = -width; local359 < 0; local359++) {
                    if ((float) z < depth[offset]) {
                        local376 = this.pixels[(u >> 16) + local356];
                        local384 = (local376 & 0xFF0000) * local262 & 0xFF000000;
                        local392 = (local376 & 0xFF00) * local265 & 0xFF0000;
                        local400 = (local376 & 0xFF) * local273 & 0xFF00;
                        raster[offset] = (local384 | local392 | local400) >>> 8;
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local276;
                offset += dstStep;
            }
        } else if (op == 3) {
            local262 = u;
            for (local265 = -height; local265 < 0; local265++) {
                local273 = (v >> 16) * super.width;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        local348 = this.pixels[(u >> 16) + local273];
                        local356 = local348 + colour;
                        local359 = (local348 & 0xFF00FF) + (colour & 0xFF00FF);
                        local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                        raster[offset] = local356 - local376 | local376 - (local376 >>> 8);
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local262;
                offset += dstStep;
            }
        } else if (op == 2) {
            local262 = colour >>> 24;
            local265 = 256 - local262;
            local273 = (colour & 0xFF00FF) * local265 & 0xFF00FF00;
            local276 = (colour & 0xFF00) * local265 & 0xFF0000;
            lerpColour = (local273 | local276) >>> 8;
            local348 = u;
            for (local356 = -height; local356 < 0; local356++) {
                local359 = (v >> 16) * super.width;
                for (local376 = -width; local376 < 0; local376++) {
                    if ((float) z < depth[offset]) {
                        local384 = this.pixels[(u >> 16) + local359];
                        local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                        local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                        raster[offset] = ((local273 | local276) >>> 8) + lerpColour;
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local348;
                offset += dstStep;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!ap", name = "a", descriptor = "(III)V")
    @Override
    public void copyAlpha(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int channel) {
        throw new IllegalStateException("Can't capture alpha into a java_sprite_24");
    }

    @OriginalMember(owner = "client!ap", name = "a", descriptor = "(IIIIIIIII)V")
    @Override
    public void method8208(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int op, @OriginalArg(6) int colour, @OriginalArg(7) int mode) {
        if (width <= 0 || height <= 0) {
            return;
        }
        @Pc(9) int u = 0;
        @Pc(11) int v = 0;
        @Pc(20) int scaleWidth = super.leftMargin + super.width + super.rightMargin;
        @Pc(29) int scaleHeight = super.topMargin + super.height + super.bottomMargin;
        @Pc(35) int uStep = (scaleWidth << 16) / width;
        @Pc(41) int vStep = (scaleHeight << 16) / height;
        @Pc(55) int offset;
        if (super.leftMargin > 0) {
            offset = ((super.leftMargin << 16) + uStep - 1) / uStep;
            x += offset;
            u = offset * uStep - (super.leftMargin << 16);
        }
        if (super.topMargin > 0) {
            offset = ((super.topMargin << 16) + vStep - 1) / vStep;
            y += offset;
            v = offset * vStep - (super.topMargin << 16);
        }
        if (super.width < scaleWidth) {
            width = ((super.width << 16) + uStep - u - 1) / uStep;
        }
        if (super.height < scaleHeight) {
            height = ((super.height << 16) + vStep - v - 1) / vStep;
        }
        offset = x + y * super.toolkit.surfaceWidth;
        @Pc(147) int dstStep = super.toolkit.surfaceWidth - width;
        if (y + height > super.toolkit.clipY2) {
            height -= y + height - super.toolkit.clipY2;
        }
        @Pc(175) int clip;
        if (y < super.toolkit.clipY1) {
            clip = super.toolkit.clipY1 - y;
            height -= clip;
            offset += clip * super.toolkit.surfaceWidth;
            v += vStep * clip;
        }
        if (x + width > super.toolkit.clipX2) {
            clip = x + width - super.toolkit.clipX2;
            width -= clip;
            dstStep += clip;
        }
        if (x < super.toolkit.clipX1) {
            clip = super.toolkit.clipX1 - x;
            width -= clip;
            offset += clip;
            u += uStep * clip;
            dstStep += clip;
        }
        @Pc(249) float[] depth = super.toolkit.depthBuffer;
        @Pc(253) int[] raster = super.toolkit.surfaceRaster;
        @Pc(262) int local262;
        @Pc(265) int local265;
        @Pc(273) int local273;
        @Pc(276) int local276;
        @Pc(348) int local348;
        @Pc(356) int local356;
        @Pc(359) int local359;
        @Pc(376) int local376;
        @Pc(384) int local384;
        @Pc(392) int local392;
        @Pc(400) int local400;
        @Pc(569) int lerpColour;
        if (mode != 0) {
            @Pc(929) int local929;
            @Pc(937) int local937;
            @Pc(949) int local949;
            if (mode == 1) {
                if (op == 1) {
                    local262 = u;
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.width;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local348 = this.pixels[(u >> 16) + local273];
                                if (local348 != 0) {
                                    raster[offset] = local348;
                                    depth[offset] = (float) z;
                                }
                            }
                            u += uStep;
                            offset++;
                        }
                        v += vStep;
                        u = local262;
                        offset += dstStep;
                    }
                } else if (op == 0) {
                    local262 = u;
                    if ((colour & 0xFFFFFF) == 16777215) {
                        local265 = colour >>> 24;
                        local273 = 256 - local265;
                        for (local276 = -height; local276 < 0; local276++) {
                            local348 = (v >> 16) * super.width;
                            for (local356 = -width; local356 < 0; local356++) {
                                if ((float) z < depth[offset]) {
                                    local359 = this.pixels[(u >> 16) + local348];
                                    if (local359 != 0) {
                                        local376 = raster[offset];
                                        raster[offset] = ((local359 & 0xFF00FF) * local265 + (local376 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local359 & 0xFF00) * local265 + (local376 & 0xFF00) * local273 & 0xFF0000) >> 8;
                                        depth[offset] = (float) z;
                                    }
                                }
                                u += uStep;
                                offset++;
                            }
                            v += vStep;
                            u = local262;
                            offset += dstStep;
                        }
                    } else {
                        local265 = colour >> 16 & 0xFF;
                        local273 = colour >> 8 & 0xFF;
                        local276 = colour & 0xFF;
                        local348 = colour >>> 24;
                        local356 = 256 - local348;
                        for (local359 = -height; local359 < 0; local359++) {
                            local376 = (v >> 16) * super.width;
                            for (local384 = -width; local384 < 0; local384++) {
                                if ((float) z < depth[offset]) {
                                    local392 = this.pixels[(u >> 16) + local376];
                                    if (local392 != 0) {
                                        if (local348 == 255) {
                                            local400 = (local392 & 0xFF0000) * local265 & 0xFF000000;
                                            local929 = (local392 & 0xFF00) * local273 & 0xFF0000;
                                            local937 = (local392 & 0xFF) * local276 & 0xFF00;
                                            raster[offset] = (local400 | local929 | local937) >>> 8;
                                            depth[offset] = (float) z;
                                        } else {
                                            local400 = (local392 & 0xFF0000) * local265 & 0xFF000000;
                                            local929 = (local392 & 0xFF00) * local273 & 0xFF0000;
                                            local937 = (local392 & 0xFF) * local276 & 0xFF00;
                                            local392 = (local400 | local929 | local937) >>> 8;
                                            local949 = raster[offset];
                                            raster[offset] = ((local392 & 0xFF00FF) * local348 + (local949 & 0xFF00FF) * local356 & 0xFF00FF00) + ((local392 & 0xFF00) * local348 + (local949 & 0xFF00) * local356 & 0xFF0000) >> 8;
                                            depth[offset] = (float) z;
                                        }
                                    }
                                }
                                u += uStep;
                                offset++;
                            }
                            v += vStep;
                            u = local262;
                            offset += dstStep;
                        }
                    }
                } else if (op == 3) {
                    local262 = u;
                    local265 = colour >>> 24;
                    local273 = 256 - local265;
                    for (local276 = -height; local276 < 0; local276++) {
                        local348 = (v >> 16) * super.width;
                        for (local356 = -width; local356 < 0; local356++) {
                            if ((float) z < depth[offset]) {
                                local359 = this.pixels[(u >> 16) + local348];
                                local376 = local359 + colour;
                                local384 = (local359 & 0xFF00FF) + (colour & 0xFF00FF);
                                local392 = (local384 & 0x1000100) + (local376 - local384 & 0x10000);
                                local392 = local376 - local392 | local392 - (local392 >>> 8);
                                if (local359 == 0 && local265 != 255) {
                                    local359 = local392;
                                    local392 = raster[offset];
                                    local392 = ((local359 & 0xFF00FF) * local265 + (local392 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local359 & 0xFF00) * local265 + (local392 & 0xFF00) * local273 & 0xFF0000) >> 8;
                                }
                                raster[offset] = local392;
                                depth[offset] = (float) z;
                            }
                            u += uStep;
                            offset++;
                        }
                        v += vStep;
                        u = local262;
                        offset += dstStep;
                    }
                } else if (op == 2) {
                    local262 = colour >>> 24;
                    local265 = 256 - local262;
                    local273 = (colour & 0xFF00FF) * local265 & 0xFF00FF00;
                    local276 = (colour & 0xFF00) * local265 & 0xFF0000;
                    lerpColour = (local273 | local276) >>> 8;
                    local348 = u;
                    for (local356 = -height; local356 < 0; local356++) {
                        local359 = (v >> 16) * super.width;
                        for (local376 = -width; local376 < 0; local376++) {
                            if ((float) z < depth[offset]) {
                                local384 = this.pixels[(u >> 16) + local359];
                                if (local384 != 0) {
                                    local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                    local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                    raster[offset] = ((local273 | local276) >>> 8) + lerpColour;
                                    depth[offset] = (float) z;
                                }
                            }
                            u += uStep;
                            offset++;
                        }
                        v += vStep;
                        u = local348;
                        offset += dstStep;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.width;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.pixels[(u >> 16) + local273];
                            if (local348 != 0) {
                                local356 = raster[offset];
                                local359 = local348 + local356;
                                local376 = (local348 & 0xFF00FF) + (local356 & 0xFF00FF);
                                local356 = (local376 & 0x1000100) + (local359 - local376 & 0x10000);
                                raster[offset] = local359 - local356 | local356 - (local356 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local262;
                    offset += dstStep;
                }
            } else if (op == 0) {
                local262 = u;
                local265 = colour >> 16 & 0xFF;
                local273 = colour >> 8 & 0xFF;
                local276 = colour & 0xFF;
                for (local348 = -height; local348 < 0; local348++) {
                    local356 = (v >> 16) * super.width;
                    for (local359 = -width; local359 < 0; local359++) {
                        if ((float) z < depth[offset]) {
                            local376 = this.pixels[(u >> 16) + local356];
                            if (local376 != 0) {
                                local384 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                local392 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                local400 = (local376 & 0xFF) * local276 & 0xFF00;
                                local376 = (local384 | local392 | local400) >>> 8;
                                local929 = raster[offset];
                                local937 = local376 + local929;
                                local949 = (local376 & 0xFF00FF) + (local929 & 0xFF00FF);
                                local929 = (local949 & 0x1000100) + (local937 - local949 & 0x10000);
                                raster[offset] = local937 - local929 | local929 - (local929 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local262;
                    offset += dstStep;
                }
            } else if (op == 3) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.width;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.pixels[(u >> 16) + local273];
                            local356 = local348 + colour;
                            local359 = (local348 & 0xFF00FF) + (colour & 0xFF00FF);
                            local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                            local348 = local356 - local376 | local376 - (local376 >>> 8);
                            local376 = raster[offset];
                            local356 = local348 + local376;
                            local359 = (local348 & 0xFF00FF) + (local376 & 0xFF00FF);
                            local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                            raster[offset] = local356 - local376 | local376 - (local376 >>> 8);
                            depth[offset] = (float) z;
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local262;
                    offset += dstStep;
                }
            } else if (op == 2) {
                local262 = colour >>> 24;
                local265 = 256 - local262;
                local273 = (colour & 0xFF00FF) * local265 & 0xFF00FF00;
                local276 = (colour & 0xFF00) * local265 & 0xFF0000;
                lerpColour = (local273 | local276) >>> 8;
                local348 = u;
                for (local356 = -height; local356 < 0; local356++) {
                    local359 = (v >> 16) * super.width;
                    for (local376 = -width; local376 < 0; local376++) {
                        if ((float) z < depth[offset]) {
                            local384 = this.pixels[(u >> 16) + local359];
                            if (local384 != 0) {
                                local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                local384 = ((local273 | local276) >>> 8) + lerpColour;
                                local392 = raster[offset];
                                local400 = local384 + local392;
                                local929 = (local384 & 0xFF00FF) + (local392 & 0xFF00FF);
                                @Pc(1838) int local1838 = (local929 & 0x1000100) + (local400 - local929 & 0x10000);
                                raster[offset] = local400 - local1838 | local1838 - (local1838 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local348;
                    offset += dstStep;
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else if (op == 1) {
            local262 = u;
            for (local265 = -height; local265 < 0; local265++) {
                local273 = (v >> 16) * super.width;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        raster[offset] = this.pixels[(u >> 16) + local273];
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local262;
                offset += dstStep;
            }
        } else if (op == 0) {
            local262 = colour >> 16 & 0xFF;
            local265 = colour >> 8 & 0xFF;
            local273 = colour & 0xFF;
            local276 = u;
            for (local348 = -height; local348 < 0; local348++) {
                local356 = (v >> 16) * super.width;
                for (local359 = -width; local359 < 0; local359++) {
                    if ((float) z < depth[offset]) {
                        local376 = this.pixels[(u >> 16) + local356];
                        local384 = (local376 & 0xFF0000) * local262 & 0xFF000000;
                        local392 = (local376 & 0xFF00) * local265 & 0xFF0000;
                        local400 = (local376 & 0xFF) * local273 & 0xFF00;
                        raster[offset] = (local384 | local392 | local400) >>> 8;
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local276;
                offset += dstStep;
            }
        } else if (op == 3) {
            local262 = u;
            for (local265 = -height; local265 < 0; local265++) {
                local273 = (v >> 16) * super.width;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        local348 = this.pixels[(u >> 16) + local273];
                        local356 = local348 + colour;
                        local359 = (local348 & 0xFF00FF) + (colour & 0xFF00FF);
                        local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                        raster[offset] = local356 - local376 | local376 - (local376 >>> 8);
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local262;
                offset += dstStep;
            }
        } else if (op == 2) {
            local262 = colour >>> 24;
            local265 = 256 - local262;
            local273 = (colour & 0xFF00FF) * local265 & 0xFF00FF00;
            local276 = (colour & 0xFF00) * local265 & 0xFF0000;
            lerpColour = (local273 | local276) >>> 8;
            local348 = u;
            for (local356 = -height; local356 < 0; local356++) {
                local359 = (v >> 16) * super.width;
                for (local376 = -width; local376 < 0; local376++) {
                    if ((float) z < depth[offset]) {
                        local384 = this.pixels[(u >> 16) + local359];
                        local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                        local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                        raster[offset] = ((local273 | local276) >>> 8) + lerpColour;
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local348;
                offset += dstStep;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!ap", name = "a", descriptor = "(IILclient!aa;II)V")
    @Override
    public void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) ClippingMask mask, @OriginalArg(3) int maskX, @OriginalArg(4) int maskY) {
        if (super.toolkit.stopped()) {
            throw new IllegalStateException();
        }
        x += super.leftMargin;
        y += super.topMargin;
        @Pc(20) int srcIndex = 0;
        @Pc(24) int dstStride = super.toolkit.surfaceWidth;
        @Pc(27) int width = super.width;
        @Pc(30) int height = super.height;
        @Pc(34) int dstStep = dstStride - width;
        @Pc(36) int srcStep = 0;
        @Pc(42) int dstIndex = x + y * dstStride;
        @Pc(53) int clip;
        if (y < super.toolkit.clipY1) {
            clip = super.toolkit.clipY1 - y;
            height -= clip;
            y = super.toolkit.clipY1;
            srcIndex = clip * width;
            dstIndex += clip * dstStride;
        }
        if (y + height > super.toolkit.clipY2) {
            height -= y + height - super.toolkit.clipY2;
        }
        if (x < super.toolkit.clipX1) {
            clip = super.toolkit.clipX1 - x;
            width -= clip;
            x = super.toolkit.clipX1;
            srcIndex += clip;
            dstIndex += clip;
            srcStep = clip;
            dstStep += clip;
        }
        if (x + width > super.toolkit.clipX2) {
            clip = x + width - super.toolkit.clipX2;
            width -= clip;
            srcStep += clip;
            dstStep += clip;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        @Pc(163) JavaClippingMask clippingMask = (JavaClippingMask) mask;
        @Pc(166) int[] lineOffsets = clippingMask.lineOffsets;
        @Pc(169) int[] lineWidths = clippingMask.lineWidths;
        @Pc(173) int[] raster = super.toolkit.surfaceRaster;
        @Pc(175) int startY = y;
        if (maskY > y) {
            startY = maskY;
            dstIndex += (maskY - y) * dstStride;
            srcIndex += (maskY - y) * super.width;
        }
        @Pc(215) int endY = maskY + lineOffsets.length < y + height ? maskY + lineOffsets.length : y + height;
        for (@Pc(217) int row = startY; row < endY; row++) {
            @Pc(226) int lineStart = lineOffsets[row - maskY] + maskX;
            @Pc(232) int lineWidth = lineWidths[row - maskY];
            @Pc(234) int count = width;
            @Pc(241) int skip;
            if (x > lineStart) {
                skip = x - lineStart;
                if (skip >= lineWidth) {
                    srcIndex += width + srcStep;
                    dstIndex += width + dstStep;
                    continue;
                }
                lineWidth -= skip;
            } else {
                skip = lineStart - x;
                if (skip >= width) {
                    srcIndex += width + srcStep;
                    dstIndex += width + dstStep;
                    continue;
                }
                srcIndex += skip;
                count = width - skip;
                dstIndex += skip;
            }
            skip = 0;
            if (count < lineWidth) {
                lineWidth = count;
            } else {
                skip = count - lineWidth;
            }
            for (@Pc(309) int column = -lineWidth; column < 0; column++) {
                @Pc(316) int src = this.pixels[srcIndex++];
                if (src == 0) {
                    dstIndex++;
                } else {
                    raster[dstIndex++] = src;
                }
            }
            srcIndex += skip + srcStep;
            dstIndex += skip + dstStep;
        }
    }

    @OriginalMember(owner = "client!ap", name = "b", descriptor = "(II)V")
    @Override
    protected void blitParallelogram(@OriginalArg(0) int op) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        @Pc(878) int skip;
        @Pc(11) int row;
        @Pc(14) int dstIndex;
        @Pc(16) int u;
        @Pc(18) int v;
        @Pc(20) int column;
        @Pc(57) int texel;
        @Pc(60) int dst;
        @Pc(223) int src;
        @Pc(251) int local251;
        @Pc(255) int local255;
        @Pc(259) int local259;
        @Pc(331) int local331;
        if (JavaSpriteBlitState.duDx == 0) {
            if (JavaSpriteBlitState.dvDx == 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU;
                    v = JavaSpriteBlitState.rowV;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (u >= 0 && v >= 0 && u - (super.width << 12) < 0 && v - (super.height << 12) < 0) {
                        while (column < 0) {
                            texel = (v >> 12) * super.width + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    raster[dst] = src;
                                }
                            } else if (op == 0) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local251 = JavaSpriteBlitState.colour >>> 24;
                                        local255 = 256 - local251;
                                        local259 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local251 | local255 | local259) >>> 8;
                                    } else {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local251 | local255 | local259) >>> 8;
                                        local331 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                src = this.pixels[texel];
                                local251 = JavaSpriteBlitState.colour;
                                local255 = src + local251;
                                local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                                local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                                local331 = local255 - local331 | local331 - (local331 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local331;
                                    local331 = raster[dst];
                                    local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local331;
                            } else if (op == 2) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                            column++;
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else if (JavaSpriteBlitState.dvDx < 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU;
                    v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (u >= 0 && u - (super.width << 12) < 0) {
                        @Pc(871) int vOverrun;
                        if ((vOverrun = v - (super.height << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(899) int vBound;
                        if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.width + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    raster[dst] = src;
                                }
                            } else if (op == 0) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local251 = JavaSpriteBlitState.colour >>> 24;
                                        local255 = 256 - local251;
                                        local259 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local251 | local255 | local259) >>> 8;
                                    } else {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local251 | local255 | local259) >>> 8;
                                        local331 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                src = this.pixels[texel];
                                local251 = JavaSpriteBlitState.colour;
                                local255 = src + local251;
                                local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                                local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                                local331 = local255 - local331 | local331 - (local331 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local331;
                                    local331 = raster[dst];
                                    local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local331;
                            } else if (op == 2) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                            v += JavaSpriteBlitState.dvDx;
                            column++;
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU;
                    v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (u >= 0 && u - (super.width << 12) < 0) {
                        if (v < 0) {
                            skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1767) int vBound;
                        if ((vBound = (v + 1 - (super.height << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.width + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    raster[dst] = src;
                                }
                            } else if (op == 0) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local251 = JavaSpriteBlitState.colour >>> 24;
                                        local255 = 256 - local251;
                                        local259 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local251 | local255 | local259) >>> 8;
                                    } else {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local251 | local255 | local259) >>> 8;
                                        local331 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                src = this.pixels[texel];
                                local251 = JavaSpriteBlitState.colour;
                                local255 = src + local251;
                                local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                                local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                                local331 = local255 - local331 | local331 - (local331 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local331;
                                    local331 = raster[dst];
                                    local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local331;
                            } else if (op == 2) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                            v += JavaSpriteBlitState.dvDx;
                            column++;
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            }
        } else if (JavaSpriteBlitState.duDx < 0) {
            if (JavaSpriteBlitState.dvDx == 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                    v = JavaSpriteBlitState.rowV;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (v >= 0 && v - (super.height << 12) < 0) {
                        @Pc(2609) int uOverrun;
                        if ((uOverrun = u - (super.width << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(2637) int uBound;
                        if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.width + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    raster[dst] = src;
                                }
                            } else if (op == 0) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local251 = JavaSpriteBlitState.colour >>> 24;
                                        local255 = 256 - local251;
                                        local259 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local251 | local255 | local259) >>> 8;
                                    } else {
                                        local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local251 | local255 | local259) >>> 8;
                                        local331 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                src = this.pixels[texel];
                                local251 = JavaSpriteBlitState.colour;
                                local255 = src + local251;
                                local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                                local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                                local331 = local255 - local331 | local331 - (local331 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local331;
                                    local331 = raster[dst];
                                    local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local331;
                            } else if (op == 2) {
                                src = this.pixels[texel];
                                if (src != 0) {
                                    local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                                }
                            } else {
                                throw new IllegalArgumentException();
                            }
                            u += JavaSpriteBlitState.duDx;
                            column++;
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else if (JavaSpriteBlitState.dvDx < 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                    v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                    column = JavaSpriteBlitState.negativeWidth;
                    @Pc(3466) int uOverrun;
                    if ((uOverrun = u - (super.width << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(3500) int uBound;
                    if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    @Pc(3512) int vOverrun;
                    if ((vOverrun = v - (super.height << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(3546) int vBound;
                    if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.width + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                raster[dst] = src;
                            }
                        } else if (op == 0) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local251 = JavaSpriteBlitState.colour >>> 24;
                                    local255 = 256 - local251;
                                    local259 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                                } else if (JavaSpriteBlitState.alpha == 255) {
                                    local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local251 | local255 | local259) >>> 8;
                                } else {
                                    local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local251 | local255 | local259) >>> 8;
                                    local331 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                            }
                        } else if (op == 3) {
                            src = this.pixels[texel];
                            local251 = JavaSpriteBlitState.colour;
                            local255 = src + local251;
                            local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                            local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                            local331 = local255 - local331 | local331 - (local331 >>> 8);
                            if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                src = local331;
                                local331 = raster[dst];
                                local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                            raster[dst] = local331;
                        } else if (op == 2) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                        u += JavaSpriteBlitState.duDx;
                        v += JavaSpriteBlitState.dvDx;
                        column++;
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                    v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                    column = JavaSpriteBlitState.negativeWidth;
                    @Pc(4381) int uOverrun;
                    if ((uOverrun = u - (super.width << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(4415) int uBound;
                    if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    if (v < 0) {
                        skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(4463) int vBound;
                    if ((vBound = (v + 1 - (super.height << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.width + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                raster[dst] = src;
                            }
                        } else if (op == 0) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local251 = JavaSpriteBlitState.colour >>> 24;
                                    local255 = 256 - local251;
                                    local259 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                                } else if (JavaSpriteBlitState.alpha == 255) {
                                    local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local251 | local255 | local259) >>> 8;
                                } else {
                                    local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local251 | local255 | local259) >>> 8;
                                    local331 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                            }
                        } else if (op == 3) {
                            src = this.pixels[texel];
                            local251 = JavaSpriteBlitState.colour;
                            local255 = src + local251;
                            local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                            local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                            local331 = local255 - local331 | local331 - (local331 >>> 8);
                            if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                src = local331;
                                local331 = raster[dst];
                                local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                            raster[dst] = local331;
                        } else if (op == 2) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                        u += JavaSpriteBlitState.duDx;
                        v += JavaSpriteBlitState.dvDx;
                        column++;
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            }
        } else if (JavaSpriteBlitState.dvDx == 0) {
            row = JavaSpriteBlitState.negativeHeight;
            while (row < 0) {
                dstIndex = JavaSpriteBlitState.rowOffset;
                u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                v = JavaSpriteBlitState.rowV;
                column = JavaSpriteBlitState.negativeWidth;
                if (v >= 0 && v - (super.height << 12) < 0) {
                    if (u < 0) {
                        skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(5341) int uBound;
                    if ((uBound = (u + 1 - (super.width << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.width + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                raster[dst] = src;
                            }
                        } else if (op == 0) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local251 = JavaSpriteBlitState.colour >>> 24;
                                    local255 = 256 - local251;
                                    local259 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                                } else if (JavaSpriteBlitState.alpha == 255) {
                                    local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local251 | local255 | local259) >>> 8;
                                } else {
                                    local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local251 | local255 | local259) >>> 8;
                                    local331 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                            }
                        } else if (op == 3) {
                            src = this.pixels[texel];
                            local251 = JavaSpriteBlitState.colour;
                            local255 = src + local251;
                            local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                            local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                            local331 = local255 - local331 | local331 - (local331 >>> 8);
                            if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                src = local331;
                                local331 = raster[dst];
                                local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                            raster[dst] = local331;
                        } else if (op == 2) {
                            src = this.pixels[texel];
                            if (src != 0) {
                                local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                        u += JavaSpriteBlitState.duDx;
                        column++;
                    }
                }
                row++;
                JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
            }
        } else if (JavaSpriteBlitState.dvDx < 0) {
            for (row = JavaSpriteBlitState.negativeHeight; row < 0; row++) {
                dstIndex = JavaSpriteBlitState.rowOffset;
                u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                column = JavaSpriteBlitState.negativeWidth;
                if (u < 0) {
                    skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                    column += skip;
                    u += JavaSpriteBlitState.duDx * skip;
                    v += JavaSpriteBlitState.dvDx * skip;
                    dstIndex += skip;
                }
                @Pc(6210) int uBound;
                if ((uBound = (u + 1 - (super.width << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                    column = uBound;
                }
                @Pc(6222) int vOverrun;
                if ((vOverrun = v - (super.height << 12)) >= 0) {
                    skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                    column += skip;
                    u += JavaSpriteBlitState.duDx * skip;
                    v += JavaSpriteBlitState.dvDx * skip;
                    dstIndex += skip;
                }
                @Pc(6256) int vBound;
                if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                    column = vBound;
                }
                while (column < 0) {
                    texel = (v >> 12) * super.width + (u >> 12);
                    dst = dstIndex++;
                    if (op == 1) {
                        src = this.pixels[texel];
                        if (src != 0) {
                            raster[dst] = src;
                        }
                    } else if (op == 0) {
                        src = this.pixels[texel];
                        if (src != 0) {
                            if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                local251 = JavaSpriteBlitState.colour >>> 24;
                                local255 = 256 - local251;
                                local259 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                            } else if (JavaSpriteBlitState.alpha == 255) {
                                local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                raster[dst] = (local251 | local255 | local259) >>> 8;
                            } else {
                                local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                src = (local251 | local255 | local259) >>> 8;
                                local331 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                        }
                    } else if (op == 3) {
                        src = this.pixels[texel];
                        local251 = JavaSpriteBlitState.colour;
                        local255 = src + local251;
                        local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                        local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                        local331 = local255 - local331 | local331 - (local331 >>> 8);
                        if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                            src = local331;
                            local331 = raster[dst];
                            local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                        }
                        raster[dst] = local331;
                    } else if (op == 2) {
                        src = this.pixels[texel];
                        if (src != 0) {
                            local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                            local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                            raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                    u += JavaSpriteBlitState.duDx;
                    v += JavaSpriteBlitState.dvDx;
                    column++;
                }
                JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
            }
        } else {
            for (row = JavaSpriteBlitState.negativeHeight; row < 0; row++) {
                dstIndex = JavaSpriteBlitState.rowOffset;
                u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                column = JavaSpriteBlitState.negativeWidth;
                if (u < 0) {
                    skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                    column += skip;
                    u += JavaSpriteBlitState.duDx * skip;
                    v += JavaSpriteBlitState.dvDx * skip;
                    dstIndex += skip;
                }
                @Pc(7127) int uBound;
                if ((uBound = (u + 1 - (super.width << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                    column = uBound;
                }
                if (v < 0) {
                    skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                    column += skip;
                    u += JavaSpriteBlitState.duDx * skip;
                    v += JavaSpriteBlitState.dvDx * skip;
                    dstIndex += skip;
                }
                @Pc(7175) int vBound;
                if ((vBound = (v + 1 - (super.height << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                    column = vBound;
                }
                while (column < 0) {
                    texel = (v >> 12) * super.width + (u >> 12);
                    dst = dstIndex++;
                    if (op == 1) {
                        src = this.pixels[texel];
                        if (src != 0) {
                            raster[dst] = src;
                        }
                    } else if (op == 0) {
                        src = this.pixels[texel];
                        if (src != 0) {
                            if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                local251 = JavaSpriteBlitState.colour >>> 24;
                                local255 = 256 - local251;
                                local259 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local251 + (local259 & 0xFF00FF) * local255 & 0xFF00FF00) + ((src & 0xFF00) * local251 + (local259 & 0xFF00) * local255 & 0xFF0000) >> 8;
                            } else if (JavaSpriteBlitState.alpha == 255) {
                                local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                raster[dst] = (local251 | local255 | local259) >>> 8;
                            } else {
                                local251 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local255 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local259 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                src = (local251 | local255 | local259) >>> 8;
                                local331 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                        }
                    } else if (op == 3) {
                        src = this.pixels[texel];
                        local251 = JavaSpriteBlitState.colour;
                        local255 = src + local251;
                        local259 = (src & 0xFF00FF) + (local251 & 0xFF00FF);
                        local331 = (local259 & 0x1000100) + (local255 - local259 & 0x10000);
                        local331 = local255 - local331 | local331 - (local331 >>> 8);
                        if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                            src = local331;
                            local331 = raster[dst];
                            local331 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local331 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local331 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                        }
                        raster[dst] = local331;
                    } else if (op == 2) {
                        src = this.pixels[texel];
                        if (src != 0) {
                            local251 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                            local255 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                            raster[dst++] = ((local251 | local255) >>> 8) + JavaSpriteBlitState.lerpColour;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                    u += JavaSpriteBlitState.duDx;
                    v += JavaSpriteBlitState.dvDx;
                    column++;
                }
                JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
            }
        }
    }

    @OriginalMember(owner = "client!ap", name = "a", descriptor = "([I[III)V")
    @Override
    protected void blitParallelogramMasked(@OriginalArg(0) int[] lineOffsets, @OriginalArg(1) int[] lineWidths, @OriginalArg(2) int maskOffsetX, @OriginalArg(3) int maskOffsetY) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        @Pc(201) int skip;
        @Pc(11) int row;
        @Pc(16) int maskIndex;
        @Pc(28) int dstIndex;
        @Pc(30) int u;
        @Pc(32) int v;
        @Pc(34) int column;
        @Pc(64) int maskStart;
        @Pc(69) int maskCount;
        @Pc(75) int maskSkip;
        @Pc(122) int src;
        if (JavaSpriteBlitState.duDx == 0) {
            if (JavaSpriteBlitState.dvDx == 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    maskIndex = row + maskOffsetY;
                    if (maskIndex >= 0) {
                        if (maskIndex >= lineOffsets.length) {
                            return;
                        }
                        dstIndex = JavaSpriteBlitState.rowOffset;
                        u = JavaSpriteBlitState.rowU;
                        v = JavaSpriteBlitState.rowV;
                        column = JavaSpriteBlitState.negativeWidth;
                        if (u >= 0 && v >= 0 && u - (super.width << 12) < 0 && v - (super.height << 12) < 0) {
                            maskStart = lineOffsets[maskIndex] - maskOffsetX;
                            maskCount = -lineWidths[maskIndex];
                            maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                            if (maskSkip > 0) {
                                dstIndex += maskSkip;
                                column += maskSkip;
                                u += JavaSpriteBlitState.duDx * maskSkip;
                                v += JavaSpriteBlitState.dvDx * maskSkip;
                            } else {
                                maskCount -= maskSkip;
                            }
                            if (column < maskCount) {
                                column = maskCount;
                            }
                            while (column < 0) {
                                src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                                if (src == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = src;
                                }
                                column++;
                            }
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else if (JavaSpriteBlitState.dvDx < 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    maskIndex = row + maskOffsetY;
                    if (maskIndex >= 0) {
                        if (maskIndex >= lineOffsets.length) {
                            return;
                        }
                        dstIndex = JavaSpriteBlitState.rowOffset;
                        u = JavaSpriteBlitState.rowU;
                        v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                        column = JavaSpriteBlitState.negativeWidth;
                        if (u >= 0 && u - (super.width << 12) < 0) {
                            @Pc(194) int vOverrun;
                            if ((vOverrun = v - (super.height << 12)) >= 0) {
                                skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                                column += skip;
                                v += JavaSpriteBlitState.dvDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(222) int vBound;
                            if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                                column = vBound;
                            }
                            maskStart = lineOffsets[maskIndex] - maskOffsetX;
                            maskCount = -lineWidths[maskIndex];
                            maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                            if (maskSkip > 0) {
                                dstIndex += maskSkip;
                                column += maskSkip;
                                u += JavaSpriteBlitState.duDx * maskSkip;
                                v += JavaSpriteBlitState.dvDx * maskSkip;
                            } else {
                                maskCount -= maskSkip;
                            }
                            if (column < maskCount) {
                                column = maskCount;
                            }
                            while (column < 0) {
                                src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                                if (src == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = src;
                                }
                                v += JavaSpriteBlitState.dvDx;
                                column++;
                            }
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    maskIndex = row + maskOffsetY;
                    if (maskIndex >= 0) {
                        if (maskIndex >= lineOffsets.length) {
                            return;
                        }
                        dstIndex = JavaSpriteBlitState.rowOffset;
                        u = JavaSpriteBlitState.rowU;
                        v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                        column = JavaSpriteBlitState.negativeWidth;
                        if (u >= 0 && u - (super.width << 12) < 0) {
                            if (v < 0) {
                                skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                                column += skip;
                                v += JavaSpriteBlitState.dvDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(400) int vBound;
                            if ((vBound = (v + 1 - (super.height << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                                column = vBound;
                            }
                            maskStart = lineOffsets[maskIndex] - maskOffsetX;
                            maskCount = -lineWidths[maskIndex];
                            maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                            if (maskSkip > 0) {
                                dstIndex += maskSkip;
                                column += maskSkip;
                                u += JavaSpriteBlitState.duDx * maskSkip;
                                v += JavaSpriteBlitState.dvDx * maskSkip;
                            } else {
                                maskCount -= maskSkip;
                            }
                            if (column < maskCount) {
                                column = maskCount;
                            }
                            while (column < 0) {
                                src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                                if (src == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = src;
                                }
                                v += JavaSpriteBlitState.dvDx;
                                column++;
                            }
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            }
        } else if (JavaSpriteBlitState.duDx < 0) {
            if (JavaSpriteBlitState.dvDx == 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    maskIndex = row + maskOffsetY;
                    if (maskIndex >= 0) {
                        if (maskIndex >= lineOffsets.length) {
                            return;
                        }
                        dstIndex = JavaSpriteBlitState.rowOffset;
                        u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                        v = JavaSpriteBlitState.rowV;
                        column = JavaSpriteBlitState.negativeWidth;
                        if (v >= 0 && v - (super.height << 12) < 0) {
                            @Pc(552) int uOverrun;
                            if ((uOverrun = u - (super.width << 12)) >= 0) {
                                skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                                column += skip;
                                u += JavaSpriteBlitState.duDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(580) int uBound;
                            if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                                column = uBound;
                            }
                            maskStart = lineOffsets[maskIndex] - maskOffsetX;
                            maskCount = -lineWidths[maskIndex];
                            maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                            if (maskSkip > 0) {
                                dstIndex += maskSkip;
                                column += maskSkip;
                                u += JavaSpriteBlitState.duDx * maskSkip;
                                v += JavaSpriteBlitState.dvDx * maskSkip;
                            } else {
                                maskCount -= maskSkip;
                            }
                            if (column < maskCount) {
                                column = maskCount;
                            }
                            while (column < 0) {
                                src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                                if (src == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = src;
                                }
                                u += JavaSpriteBlitState.duDx;
                                column++;
                            }
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else if (JavaSpriteBlitState.dvDx < 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    maskIndex = row + maskOffsetY;
                    if (maskIndex >= 0) {
                        if (maskIndex >= lineOffsets.length) {
                            return;
                        }
                        dstIndex = JavaSpriteBlitState.rowOffset;
                        u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                        v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                        column = JavaSpriteBlitState.negativeWidth;
                        @Pc(719) int uOverrun;
                        if ((uOverrun = u - (super.width << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(753) int uBound;
                        if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        @Pc(765) int vOverrun;
                        if ((vOverrun = v - (super.height << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(799) int vBound;
                        if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        maskStart = lineOffsets[maskIndex] - maskOffsetX;
                        maskCount = -lineWidths[maskIndex];
                        maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                        if (maskSkip > 0) {
                            dstIndex += maskSkip;
                            column += maskSkip;
                            u += JavaSpriteBlitState.duDx * maskSkip;
                            v += JavaSpriteBlitState.dvDx * maskSkip;
                        } else {
                            maskCount -= maskSkip;
                        }
                        if (column < maskCount) {
                            column = maskCount;
                        }
                        while (column < 0) {
                            src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                            if (src == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = src;
                            }
                            u += JavaSpriteBlitState.duDx;
                            v += JavaSpriteBlitState.dvDx;
                            column++;
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            } else {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    maskIndex = row + maskOffsetY;
                    if (maskIndex >= 0) {
                        if (maskIndex >= lineOffsets.length) {
                            return;
                        }
                        dstIndex = JavaSpriteBlitState.rowOffset;
                        u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                        v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                        column = JavaSpriteBlitState.negativeWidth;
                        @Pc(944) int uOverrun;
                        if ((uOverrun = u - (super.width << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(978) int uBound;
                        if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        if (v < 0) {
                            skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1026) int vBound;
                        if ((vBound = (v + 1 - (super.height << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        maskStart = lineOffsets[maskIndex] - maskOffsetX;
                        maskCount = -lineWidths[maskIndex];
                        maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                        if (maskSkip > 0) {
                            dstIndex += maskSkip;
                            column += maskSkip;
                            u += JavaSpriteBlitState.duDx * maskSkip;
                            v += JavaSpriteBlitState.dvDx * maskSkip;
                        } else {
                            maskCount -= maskSkip;
                        }
                        if (column < maskCount) {
                            column = maskCount;
                        }
                        while (column < 0) {
                            src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                            if (src == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = src;
                            }
                            u += JavaSpriteBlitState.duDx;
                            v += JavaSpriteBlitState.dvDx;
                            column++;
                        }
                    }
                    row++;
                    JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                    JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                    JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
                }
            }
        } else if (JavaSpriteBlitState.dvDx == 0) {
            row = JavaSpriteBlitState.negativeHeight;
            while (row < 0) {
                maskIndex = row + maskOffsetY;
                if (maskIndex >= 0) {
                    if (maskIndex >= lineOffsets.length) {
                        return;
                    }
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                    v = JavaSpriteBlitState.rowV;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (v >= 0 && v - (super.height << 12) < 0) {
                        if (u < 0) {
                            skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1214) int uBound;
                        if ((uBound = (u + 1 - (super.width << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        maskStart = lineOffsets[maskIndex] - maskOffsetX;
                        maskCount = -lineWidths[maskIndex];
                        maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                        if (maskSkip > 0) {
                            dstIndex += maskSkip;
                            column += maskSkip;
                            u += JavaSpriteBlitState.duDx * maskSkip;
                            v += JavaSpriteBlitState.dvDx * maskSkip;
                        } else {
                            maskCount -= maskSkip;
                        }
                        if (column < maskCount) {
                            column = maskCount;
                        }
                        while (column < 0) {
                            src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                            if (src == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = src;
                            }
                            u += JavaSpriteBlitState.duDx;
                            column++;
                        }
                    }
                }
                row++;
                JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
            }
        } else if (JavaSpriteBlitState.dvDx < 0) {
            row = JavaSpriteBlitState.negativeHeight;
            while (row < 0) {
                maskIndex = row + maskOffsetY;
                if (maskIndex >= 0) {
                    if (maskIndex >= lineOffsets.length) {
                        return;
                    }
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                    v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (u < 0) {
                        skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(1393) int uBound;
                    if ((uBound = (u + 1 - (super.width << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    @Pc(1405) int vOverrun;
                    if ((vOverrun = v - (super.height << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(1439) int vBound;
                    if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    maskStart = lineOffsets[maskIndex] - maskOffsetX;
                    maskCount = -lineWidths[maskIndex];
                    maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                    if (maskSkip > 0) {
                        dstIndex += maskSkip;
                        column += maskSkip;
                        u += JavaSpriteBlitState.duDx * maskSkip;
                        v += JavaSpriteBlitState.dvDx * maskSkip;
                    } else {
                        maskCount -= maskSkip;
                    }
                    if (column < maskCount) {
                        column = maskCount;
                    }
                    while (column < 0) {
                        src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                        if (src == 0) {
                            dstIndex++;
                        } else {
                            raster[dstIndex++] = src;
                        }
                        u += JavaSpriteBlitState.duDx;
                        v += JavaSpriteBlitState.dvDx;
                        column++;
                    }
                }
                row++;
                JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
            }
        } else {
            row = JavaSpriteBlitState.negativeHeight;
            while (row < 0) {
                maskIndex = row + maskOffsetY;
                if (maskIndex >= 0) {
                    if (maskIndex >= lineOffsets.length) {
                        return;
                    }
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU + JavaSpriteBlitState.uBias;
                    v = JavaSpriteBlitState.rowV + JavaSpriteBlitState.vBias;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (u < 0) {
                        skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(1620) int uBound;
                    if ((uBound = (u + 1 - (super.width << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    if (v < 0) {
                        skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(1668) int vBound;
                    if ((vBound = (v + 1 - (super.height << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    maskStart = lineOffsets[maskIndex] - maskOffsetX;
                    maskCount = -lineWidths[maskIndex];
                    maskSkip = maskStart + JavaSpriteBlitState.rowOffset - dstIndex;
                    if (maskSkip > 0) {
                        dstIndex += maskSkip;
                        column += maskSkip;
                        u += JavaSpriteBlitState.duDx * maskSkip;
                        v += JavaSpriteBlitState.dvDx * maskSkip;
                    } else {
                        maskCount -= maskSkip;
                    }
                    if (column < maskCount) {
                        column = maskCount;
                    }
                    while (column < 0) {
                        src = this.pixels[(v >> 12) * super.width + (u >> 12)];
                        if (src == 0) {
                            dstIndex++;
                        } else {
                            raster[dstIndex++] = src;
                        }
                        u += JavaSpriteBlitState.duDx;
                        v += JavaSpriteBlitState.dvDx;
                        column++;
                    }
                }
                row++;
                JavaSpriteBlitState.rowU += JavaSpriteBlitState.duDy;
                JavaSpriteBlitState.rowV += JavaSpriteBlitState.dvDy;
                JavaSpriteBlitState.rowOffset += JavaSpriteBlitState.dstStride;
            }
        }
    }

    @OriginalMember(owner = "client!ap", name = "a", descriptor = "(IIIIIIII)V")
    @Override
    protected void renderImpl(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode) {
        if (super.toolkit.stopped()) {
            throw new IllegalStateException();
        } else if (width > 0 && height > 0) {
            @Pc(18) int u = 0;
            @Pc(20) int v = 0;
            @Pc(24) int dstStride = super.toolkit.surfaceWidth;
            @Pc(33) int scaleWidth = super.leftMargin + super.width + super.rightMargin;
            @Pc(42) int scaleHeight = super.topMargin + super.height + super.bottomMargin;
            @Pc(48) int uStep = (scaleWidth << 16) / width;
            @Pc(54) int vStep = (scaleHeight << 16) / height;
            @Pc(68) int offset;
            if (super.leftMargin > 0) {
                offset = ((super.leftMargin << 16) + uStep - 1) / uStep;
                x += offset;
                u = offset * uStep - (super.leftMargin << 16);
            }
            if (super.topMargin > 0) {
                offset = ((super.topMargin << 16) + vStep - 1) / vStep;
                y += offset;
                v = offset * vStep - (super.topMargin << 16);
            }
            if (super.width < scaleWidth) {
                width = ((super.width << 16) + uStep - u - 1) / uStep;
            }
            if (super.height < scaleHeight) {
                height = ((super.height << 16) + vStep - v - 1) / vStep;
            }
            offset = x + y * dstStride;
            @Pc(156) int dstStep = dstStride - width;
            if (y + height > super.toolkit.clipY2) {
                height -= y + height - super.toolkit.clipY2;
            }
            @Pc(184) int clip;
            if (y < super.toolkit.clipY1) {
                clip = super.toolkit.clipY1 - y;
                height -= clip;
                offset += clip * dstStride;
                v += vStep * clip;
            }
            if (x + width > super.toolkit.clipX2) {
                clip = x + width - super.toolkit.clipX2;
                width -= clip;
                dstStep += clip;
            }
            if (x < super.toolkit.clipX1) {
                clip = super.toolkit.clipX1 - x;
                width -= clip;
                offset += clip;
                u += uStep * clip;
                dstStep += clip;
            }
            @Pc(256) int[] raster = super.toolkit.surfaceRaster;
            @Pc(265) int local265;
            @Pc(268) int local268;
            @Pc(276) int local276;
            @Pc(279) int local279;
            @Pc(337) int local337;
            @Pc(345) int local345;
            @Pc(348) int local348;
            @Pc(358) int local358;
            @Pc(366) int local366;
            @Pc(374) int local374;
            @Pc(382) int local382;
            @Pc(534) int lerpColour;
            if (mode != 0) {
                @Pc(854) int local854;
                @Pc(862) int local862;
                @Pc(874) int local874;
                if (mode == 1) {
                    if (op == 1) {
                        local265 = u;
                        for (local268 = -height; local268 < 0; local268++) {
                            local276 = (v >> 16) * super.width;
                            for (local279 = -width; local279 < 0; local279++) {
                                local337 = this.pixels[(u >> 16) + local276];
                                if (local337 == 0) {
                                    offset++;
                                } else {
                                    raster[offset++] = local337;
                                }
                                u += uStep;
                            }
                            v += vStep;
                            u = local265;
                            offset += dstStep;
                        }
                    } else if (op == 0) {
                        local265 = u;
                        if ((colour & 0xFFFFFF) == 16777215) {
                            local268 = colour >>> 24;
                            local276 = 256 - local268;
                            for (local279 = -height; local279 < 0; local279++) {
                                local337 = (v >> 16) * super.width;
                                for (local345 = -width; local345 < 0; local345++) {
                                    local348 = this.pixels[(u >> 16) + local337];
                                    if (local348 == 0) {
                                        offset++;
                                    } else {
                                        local358 = raster[offset];
                                        raster[offset++] = ((local348 & 0xFF00FF) * local268 + (local358 & 0xFF00FF) * local276 & 0xFF00FF00) + ((local348 & 0xFF00) * local268 + (local358 & 0xFF00) * local276 & 0xFF0000) >> 8;
                                    }
                                    u += uStep;
                                }
                                v += vStep;
                                u = local265;
                                offset += dstStep;
                            }
                        } else {
                            local268 = colour >> 16 & 0xFF;
                            local276 = colour >> 8 & 0xFF;
                            local279 = colour & 0xFF;
                            local337 = colour >>> 24;
                            local345 = 256 - local337;
                            for (local348 = -height; local348 < 0; local348++) {
                                local358 = (v >> 16) * super.width;
                                for (local366 = -width; local366 < 0; local366++) {
                                    local374 = this.pixels[(u >> 16) + local358];
                                    if (local374 == 0) {
                                        offset++;
                                    } else if (local337 == 255) {
                                        local382 = (local374 & 0xFF0000) * local268 & 0xFF000000;
                                        local854 = (local374 & 0xFF00) * local276 & 0xFF0000;
                                        local862 = (local374 & 0xFF) * local279 & 0xFF00;
                                        raster[offset++] = (local382 | local854 | local862) >>> 8;
                                    } else {
                                        local382 = (local374 & 0xFF0000) * local268 & 0xFF000000;
                                        local854 = (local374 & 0xFF00) * local276 & 0xFF0000;
                                        local862 = (local374 & 0xFF) * local279 & 0xFF00;
                                        local374 = (local382 | local854 | local862) >>> 8;
                                        local874 = raster[offset];
                                        raster[offset++] = ((local374 & 0xFF00FF) * local337 + (local874 & 0xFF00FF) * local345 & 0xFF00FF00) + ((local374 & 0xFF00) * local337 + (local874 & 0xFF00) * local345 & 0xFF0000) >> 8;
                                    }
                                    u += uStep;
                                }
                                v += vStep;
                                u = local265;
                                offset += dstStep;
                            }
                        }
                    } else if (op == 3) {
                        local265 = u;
                        local268 = colour >>> 24;
                        local276 = 256 - local268;
                        for (local279 = -height; local279 < 0; local279++) {
                            local337 = (v >> 16) * super.width;
                            for (local345 = -width; local345 < 0; local345++) {
                                local348 = this.pixels[(u >> 16) + local337];
                                local358 = local348 + colour;
                                local366 = (local348 & 0xFF00FF) + (colour & 0xFF00FF);
                                local374 = (local366 & 0x1000100) + (local358 - local366 & 0x10000);
                                local374 = local358 - local374 | local374 - (local374 >>> 8);
                                if (local348 == 0 && local268 != 255) {
                                    local348 = local374;
                                    local374 = raster[offset];
                                    local374 = ((local348 & 0xFF00FF) * local268 + (local374 & 0xFF00FF) * local276 & 0xFF00FF00) + ((local348 & 0xFF00) * local268 + (local374 & 0xFF00) * local276 & 0xFF0000) >> 8;
                                }
                                raster[offset++] = local374;
                                u += uStep;
                            }
                            v += vStep;
                            u = local265;
                            offset += dstStep;
                        }
                    } else if (op == 2) {
                        local265 = colour >>> 24;
                        local268 = 256 - local265;
                        local276 = (colour & 0xFF00FF) * local268 & 0xFF00FF00;
                        local279 = (colour & 0xFF00) * local268 & 0xFF0000;
                        lerpColour = (local276 | local279) >>> 8;
                        local337 = u;
                        for (local345 = -height; local345 < 0; local345++) {
                            local348 = (v >> 16) * super.width;
                            for (local358 = -width; local358 < 0; local358++) {
                                local366 = this.pixels[(u >> 16) + local348];
                                if (local366 == 0) {
                                    offset++;
                                } else {
                                    local276 = (local366 & 0xFF00FF) * local265 & 0xFF00FF00;
                                    local279 = (local366 & 0xFF00) * local265 & 0xFF0000;
                                    raster[offset++] = ((local276 | local279) >>> 8) + lerpColour;
                                }
                                u += uStep;
                            }
                            v += vStep;
                            u = local337;
                            offset += dstStep;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else if (mode != 2) {
                    throw new IllegalArgumentException();
                } else if (op == 1) {
                    local265 = u;
                    for (local268 = -height; local268 < 0; local268++) {
                        local276 = (v >> 16) * super.width;
                        for (local279 = -width; local279 < 0; local279++) {
                            local337 = this.pixels[(u >> 16) + local276];
                            if (local337 == 0) {
                                offset++;
                            } else {
                                local345 = raster[offset];
                                local348 = local337 + local345;
                                local358 = (local337 & 0xFF00FF) + (local345 & 0xFF00FF);
                                local345 = (local358 & 0x1000100) + (local348 - local358 & 0x10000);
                                raster[offset++] = local348 - local345 | local345 - (local345 >>> 8);
                            }
                            u += uStep;
                        }
                        v += vStep;
                        u = local265;
                        offset += dstStep;
                    }
                } else if (op == 0) {
                    local265 = u;
                    local268 = colour >> 16 & 0xFF;
                    local276 = colour >> 8 & 0xFF;
                    local279 = colour & 0xFF;
                    for (local337 = -height; local337 < 0; local337++) {
                        local345 = (v >> 16) * super.width;
                        for (local348 = -width; local348 < 0; local348++) {
                            local358 = this.pixels[(u >> 16) + local345];
                            if (local358 == 0) {
                                offset++;
                            } else {
                                local366 = (local358 & 0xFF0000) * local268 & 0xFF000000;
                                local374 = (local358 & 0xFF00) * local276 & 0xFF0000;
                                local382 = (local358 & 0xFF) * local279 & 0xFF00;
                                local358 = (local366 | local374 | local382) >>> 8;
                                local854 = raster[offset];
                                local862 = local358 + local854;
                                local874 = (local358 & 0xFF00FF) + (local854 & 0xFF00FF);
                                local854 = (local874 & 0x1000100) + (local862 - local874 & 0x10000);
                                raster[offset++] = local862 - local854 | local854 - (local854 >>> 8);
                            }
                            u += uStep;
                        }
                        v += vStep;
                        u = local265;
                        offset += dstStep;
                    }
                } else if (op == 3) {
                    local265 = u;
                    for (local268 = -height; local268 < 0; local268++) {
                        local276 = (v >> 16) * super.width;
                        for (local279 = -width; local279 < 0; local279++) {
                            local337 = this.pixels[(u >> 16) + local276];
                            local345 = local337 + colour;
                            local348 = (local337 & 0xFF00FF) + (colour & 0xFF00FF);
                            local358 = (local348 & 0x1000100) + (local345 - local348 & 0x10000);
                            local337 = local345 - local358 | local358 - (local358 >>> 8);
                            local358 = raster[offset];
                            local345 = local337 + local358;
                            local348 = (local337 & 0xFF00FF) + (local358 & 0xFF00FF);
                            local358 = (local348 & 0x1000100) + (local345 - local348 & 0x10000);
                            raster[offset++] = local345 - local358 | local358 - (local358 >>> 8);
                            u += uStep;
                        }
                        v += vStep;
                        u = local265;
                        offset += dstStep;
                    }
                } else if (op == 2) {
                    local265 = colour >>> 24;
                    local268 = 256 - local265;
                    local276 = (colour & 0xFF00FF) * local268 & 0xFF00FF00;
                    local279 = (colour & 0xFF00) * local268 & 0xFF0000;
                    lerpColour = (local276 | local279) >>> 8;
                    local337 = u;
                    for (local345 = -height; local345 < 0; local345++) {
                        local348 = (v >> 16) * super.width;
                        for (local358 = -width; local358 < 0; local358++) {
                            local366 = this.pixels[(u >> 16) + local348];
                            if (local366 == 0) {
                                offset++;
                            } else {
                                local276 = (local366 & 0xFF00FF) * local265 & 0xFF00FF00;
                                local279 = (local366 & 0xFF00) * local265 & 0xFF0000;
                                local366 = ((local276 | local279) >>> 8) + lerpColour;
                                local374 = raster[offset];
                                local382 = local366 + local374;
                                local854 = (local366 & 0xFF00FF) + (local374 & 0xFF00FF);
                                @Pc(1695) int local1695 = (local854 & 0x1000100) + (local382 - local854 & 0x10000);
                                raster[offset++] = local382 - local1695 | local1695 - (local1695 >>> 8);
                            }
                            u += uStep;
                        }
                        v += vStep;
                        u = local337;
                        offset += dstStep;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else if (op == 1) {
                local265 = u;
                for (local268 = -height; local268 < 0; local268++) {
                    local276 = (v >> 16) * super.width;
                    for (local279 = -width; local279 < 0; local279++) {
                        raster[offset++] = this.pixels[(u >> 16) + local276];
                        u += uStep;
                    }
                    v += vStep;
                    u = local265;
                    offset += dstStep;
                }
            } else if (op == 0) {
                local265 = colour >> 16 & 0xFF;
                local268 = colour >> 8 & 0xFF;
                local276 = colour & 0xFF;
                local279 = u;
                for (local337 = -height; local337 < 0; local337++) {
                    local345 = (v >> 16) * super.width;
                    for (local348 = -width; local348 < 0; local348++) {
                        local358 = this.pixels[(u >> 16) + local345];
                        local366 = (local358 & 0xFF0000) * local265 & 0xFF000000;
                        local374 = (local358 & 0xFF00) * local268 & 0xFF0000;
                        local382 = (local358 & 0xFF) * local276 & 0xFF00;
                        raster[offset++] = (local366 | local374 | local382) >>> 8;
                        u += uStep;
                    }
                    v += vStep;
                    u = local279;
                    offset += dstStep;
                }
            } else if (op == 3) {
                local265 = u;
                for (local268 = -height; local268 < 0; local268++) {
                    local276 = (v >> 16) * super.width;
                    for (local279 = -width; local279 < 0; local279++) {
                        local337 = this.pixels[(u >> 16) + local276];
                        local345 = local337 + colour;
                        local348 = (local337 & 0xFF00FF) + (colour & 0xFF00FF);
                        local358 = (local348 & 0x1000100) + (local345 - local348 & 0x10000);
                        raster[offset++] = local345 - local358 | local358 - (local358 >>> 8);
                        u += uStep;
                    }
                    v += vStep;
                    u = local265;
                    offset += dstStep;
                }
            } else if (op == 2) {
                local265 = colour >>> 24;
                local268 = 256 - local265;
                local276 = (colour & 0xFF00FF) * local268 & 0xFF00FF00;
                local279 = (colour & 0xFF00) * local268 & 0xFF0000;
                lerpColour = (local276 | local279) >>> 8;
                local337 = u;
                for (local345 = -height; local345 < 0; local345++) {
                    local348 = (v >> 16) * super.width;
                    for (local358 = -width; local358 < 0; local358++) {
                        local366 = this.pixels[(u >> 16) + local348];
                        local276 = (local366 & 0xFF00FF) * local265 & 0xFF00FF00;
                        local279 = (local366 & 0xFF00) * local265 & 0xFF0000;
                        raster[offset++] = ((local276 | local279) >>> 8) + lerpColour;
                        u += uStep;
                    }
                    v += vStep;
                    u = local337;
                    offset += dstStep;
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @OriginalMember(owner = "client!ap", name = "a", descriptor = "(IIIII)V")
    @Override
    public void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int op, @OriginalArg(3) int color, @OriginalArg(4) int mode) {
        if (super.toolkit.stopped()) {
            throw new IllegalStateException();
        }
        @Pc(12) int dstStride = super.toolkit.surfaceWidth;
        x += super.leftMargin;
        y += super.topMargin;
        @Pc(28) int dstIndex = y * dstStride + x;
        @Pc(30) int srcIndex = 0;
        @Pc(33) int height = super.height;
        @Pc(36) int width = super.width;
        @Pc(40) int dstStep = dstStride - width;
        @Pc(42) int srcStep = 0;
        @Pc(53) int clip;
        if (y < super.toolkit.clipY1) {
            clip = super.toolkit.clipY1 - y;
            height -= clip;
            y = super.toolkit.clipY1;
            srcIndex = clip * width;
            dstIndex += clip * dstStride;
        }
        if (y + height > super.toolkit.clipY2) {
            height -= y + height - super.toolkit.clipY2;
        }
        if (x < super.toolkit.clipX1) {
            clip = super.toolkit.clipX1 - x;
            width -= clip;
            x = super.toolkit.clipX1;
            srcIndex += clip;
            dstIndex += clip;
            srcStep = clip;
            dstStep += clip;
        }
        if (x + width > super.toolkit.clipX2) {
            clip = x + width - super.toolkit.clipX2;
            width -= clip;
            srcStep += clip;
            dstStep += clip;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        @Pc(164) int[] raster = super.toolkit.surfaceRaster;
        @Pc(174) int local174;
        @Pc(181) int local181;
        @Pc(267) int local267;
        @Pc(270) int local270;
        @Pc(274) int local274;
        @Pc(281) int local281;
        @Pc(289) int local289;
        @Pc(297) int local297;
        @Pc(305) int local305;
        @Pc(433) int lerpColour;
        if (mode != 0) {
            @Pc(764) int local764;
            @Pc(772) int local772;
            @Pc(784) int local784;
            if (mode == 1) {
                if (op == 1) {
                    for (local174 = -height; local174 < 0; local174++) {
                        local181 = dstIndex + width - 3;
                        while (dstIndex < local181) {
                            local267 = this.pixels[srcIndex++];
                            if (local267 == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = local267;
                            }
                            local267 = this.pixels[srcIndex++];
                            if (local267 == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = local267;
                            }
                            local267 = this.pixels[srcIndex++];
                            if (local267 == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = local267;
                            }
                            local267 = this.pixels[srcIndex++];
                            if (local267 == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = local267;
                            }
                        }
                        local181 += 3;
                        while (dstIndex < local181) {
                            local267 = this.pixels[srcIndex++];
                            if (local267 == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = local267;
                            }
                        }
                        dstIndex += dstStep;
                        srcIndex += srcStep;
                    }
                } else if (op == 0) {
                    if ((color & 0xFFFFFF) == 16777215) {
                        local174 = color >>> 24;
                        local181 = 256 - local174;
                        for (local267 = -height; local267 < 0; local267++) {
                            for (local270 = -width; local270 < 0; local270++) {
                                local274 = this.pixels[srcIndex++];
                                if (local274 == 0) {
                                    dstIndex++;
                                } else {
                                    local281 = raster[dstIndex];
                                    raster[dstIndex++] = ((local274 & 0xFF00FF) * local174 + (local281 & 0xFF00FF) * local181 & 0xFF00FF00) + ((local274 & 0xFF00) * local174 + (local281 & 0xFF00) * local181 & 0xFF0000) >> 8;
                                }
                            }
                            dstIndex += dstStep;
                            srcIndex += srcStep;
                        }
                    } else {
                        local174 = color >> 16 & 0xFF;
                        local181 = color >> 8 & 0xFF;
                        local267 = color & 0xFF;
                        local270 = color >>> 24;
                        local274 = 256 - local270;
                        for (local281 = -height; local281 < 0; local281++) {
                            for (local289 = -width; local289 < 0; local289++) {
                                local297 = this.pixels[srcIndex++];
                                if (local297 == 0) {
                                    dstIndex++;
                                } else if (local270 == 255) {
                                    local305 = (local297 & 0xFF0000) * local174 & 0xFF000000;
                                    local764 = (local297 & 0xFF00) * local181 & 0xFF0000;
                                    local772 = (local297 & 0xFF) * local267 & 0xFF00;
                                    raster[dstIndex++] = (local305 | local764 | local772) >>> 8;
                                } else {
                                    local305 = (local297 & 0xFF0000) * local174 & 0xFF000000;
                                    local764 = (local297 & 0xFF00) * local181 & 0xFF0000;
                                    local772 = (local297 & 0xFF) * local267 & 0xFF00;
                                    local297 = (local305 | local764 | local772) >>> 8;
                                    local784 = raster[dstIndex];
                                    raster[dstIndex++] = ((local297 & 0xFF00FF) * local270 + (local784 & 0xFF00FF) * local274 & 0xFF00FF00) + ((local297 & 0xFF00) * local270 + (local784 & 0xFF00) * local274 & 0xFF0000) >> 8;
                                }
                            }
                            dstIndex += dstStep;
                            srcIndex += srcStep;
                        }
                    }
                } else if (op == 3) {
                    local174 = color >>> 24;
                    local181 = 256 - local174;
                    for (local267 = -height; local267 < 0; local267++) {
                        for (local270 = -width; local270 < 0; local270++) {
                            local274 = this.pixels[srcIndex++];
                            local281 = local274 + color;
                            local289 = (local274 & 0xFF00FF) + (color & 0xFF00FF);
                            local297 = (local289 & 0x1000100) + (local281 - local289 & 0x10000);
                            local297 = local281 - local297 | local297 - (local297 >>> 8);
                            if (local274 == 0 && local174 != 255) {
                                local274 = local297;
                                local297 = raster[dstIndex];
                                local297 = ((local274 & 0xFF00FF) * local174 + (local297 & 0xFF00FF) * local181 & 0xFF00FF00) + ((local274 & 0xFF00) * local174 + (local297 & 0xFF00) * local181 & 0xFF0000) >> 8;
                            }
                            raster[dstIndex++] = local297;
                        }
                        dstIndex += dstStep;
                        srcIndex += srcStep;
                    }
                } else if (op == 2) {
                    local174 = color >>> 24;
                    local181 = 256 - local174;
                    local267 = (color & 0xFF00FF) * local181 & 0xFF00FF00;
                    local270 = (color & 0xFF00) * local181 & 0xFF0000;
                    lerpColour = (local267 | local270) >>> 8;
                    for (local274 = -height; local274 < 0; local274++) {
                        for (local281 = -width; local281 < 0; local281++) {
                            local289 = this.pixels[srcIndex++];
                            if (local289 == 0) {
                                dstIndex++;
                            } else {
                                local267 = (local289 & 0xFF00FF) * local174 & 0xFF00FF00;
                                local270 = (local289 & 0xFF00) * local174 & 0xFF0000;
                                raster[dstIndex++] = ((local267 | local270) >>> 8) + lerpColour;
                            }
                        }
                        dstIndex += dstStep;
                        srcIndex += srcStep;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                for (local174 = -height; local174 < 0; local174++) {
                    for (local181 = -width; local181 < 0; local181++) {
                        local267 = this.pixels[srcIndex++];
                        if (local267 == 0) {
                            dstIndex++;
                        } else {
                            local270 = raster[dstIndex];
                            local274 = local267 + local270;
                            local281 = (local267 & 0xFF00FF) + (local270 & 0xFF00FF);
                            local270 = (local281 & 0x1000100) + (local274 - local281 & 0x10000);
                            raster[dstIndex++] = local274 - local270 | local270 - (local270 >>> 8);
                        }
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else if (op == 0) {
                local174 = color >> 16 & 0xFF;
                local181 = color >> 8 & 0xFF;
                local267 = color & 0xFF;
                for (local270 = -height; local270 < 0; local270++) {
                    for (local274 = -width; local274 < 0; local274++) {
                        local281 = this.pixels[srcIndex++];
                        if (local281 == 0) {
                            dstIndex++;
                        } else {
                            local289 = (local281 & 0xFF0000) * local174 & 0xFF000000;
                            local297 = (local281 & 0xFF00) * local181 & 0xFF0000;
                            local305 = (local281 & 0xFF) * local267 & 0xFF00;
                            local281 = (local289 | local297 | local305) >>> 8;
                            local764 = raster[dstIndex];
                            local772 = local281 + local764;
                            local784 = (local281 & 0xFF00FF) + (local764 & 0xFF00FF);
                            local764 = (local784 & 0x1000100) + (local772 - local784 & 0x10000);
                            raster[dstIndex++] = local772 - local764 | local764 - (local764 >>> 8);
                        }
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else if (op == 3) {
                for (local174 = -height; local174 < 0; local174++) {
                    for (local181 = -width; local181 < 0; local181++) {
                        local267 = this.pixels[srcIndex++];
                        local270 = local267 + color;
                        local274 = (local267 & 0xFF00FF) + (color & 0xFF00FF);
                        local281 = (local274 & 0x1000100) + (local270 - local274 & 0x10000);
                        local267 = local270 - local281 | local281 - (local281 >>> 8);
                        local281 = raster[dstIndex];
                        local270 = local267 + local281;
                        local274 = (local267 & 0xFF00FF) + (local281 & 0xFF00FF);
                        local281 = (local274 & 0x1000100) + (local270 - local274 & 0x10000);
                        raster[dstIndex++] = local270 - local281 | local281 - (local281 >>> 8);
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else if (op == 2) {
                local174 = color >>> 24;
                local181 = 256 - local174;
                local267 = (color & 0xFF00FF) * local181 & 0xFF00FF00;
                local270 = (color & 0xFF00) * local181 & 0xFF0000;
                lerpColour = (local267 | local270) >>> 8;
                for (local274 = -height; local274 < 0; local274++) {
                    for (local281 = -width; local281 < 0; local281++) {
                        local289 = this.pixels[srcIndex++];
                        if (local289 == 0) {
                            dstIndex++;
                        } else {
                            local267 = (local289 & 0xFF00FF) * local174 & 0xFF00FF00;
                            local270 = (local289 & 0xFF00) * local174 & 0xFF0000;
                            local289 = ((local267 | local270) >>> 8) + lerpColour;
                            local297 = raster[dstIndex];
                            local305 = local289 + local297;
                            local764 = (local289 & 0xFF00FF) + (local297 & 0xFF00FF);
                            @Pc(1497) int local1497 = (local764 & 0x1000100) + (local305 - local764 & 0x10000);
                            raster[dstIndex++] = local305 - local1497 | local1497 - (local1497 >>> 8);
                        }
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else if (op == 1) {
            for (local174 = -height; local174 < 0; local174++) {
                local181 = dstIndex + width - 3;
                while (dstIndex < local181) {
                    raster[dstIndex++] = this.pixels[srcIndex++];
                    raster[dstIndex++] = this.pixels[srcIndex++];
                    raster[dstIndex++] = this.pixels[srcIndex++];
                    raster[dstIndex++] = this.pixels[srcIndex++];
                }
                local181 += 3;
                while (dstIndex < local181) {
                    raster[dstIndex++] = this.pixels[srcIndex++];
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else if (op == 0) {
            local174 = color >> 16 & 0xFF;
            local181 = color >> 8 & 0xFF;
            local267 = color & 0xFF;
            for (local270 = -height; local270 < 0; local270++) {
                for (local274 = -width; local274 < 0; local274++) {
                    local281 = this.pixels[srcIndex++];
                    local289 = (local281 & 0xFF0000) * local174 & 0xFF000000;
                    local297 = (local281 & 0xFF00) * local181 & 0xFF0000;
                    local305 = (local281 & 0xFF) * local267 & 0xFF00;
                    raster[dstIndex++] = (local289 | local297 | local305) >>> 8;
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else if (op == 3) {
            for (local174 = -height; local174 < 0; local174++) {
                for (local181 = -width; local181 < 0; local181++) {
                    local267 = this.pixels[srcIndex++];
                    local270 = local267 + color;
                    local274 = (local267 & 0xFF00FF) + (color & 0xFF00FF);
                    local281 = (local274 & 0x1000100) + (local270 - local274 & 0x10000);
                    raster[dstIndex++] = local270 - local281 | local281 - (local281 >>> 8);
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else if (op == 2) {
            local174 = color >>> 24;
            local181 = 256 - local174;
            local267 = (color & 0xFF00FF) * local181 & 0xFF00FF00;
            local270 = (color & 0xFF00) * local181 & 0xFF0000;
            lerpColour = (local267 | local270) >>> 8;
            for (local274 = -height; local274 < 0; local274++) {
                for (local281 = -width; local281 < 0; local281++) {
                    local289 = this.pixels[srcIndex++];
                    local267 = (local289 & 0xFF00FF) * local174 & 0xFF00FF00;
                    local270 = (local289 & 0xFF00) * local174 & 0xFF0000;
                    raster[dstIndex++] = ((local267 | local270) >>> 8) + lerpColour;
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
