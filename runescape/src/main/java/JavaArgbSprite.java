import com.jagex.graphics.ClippingMask;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nr")
public final class JavaArgbSprite extends JavaSprite {

    @OriginalMember(owner = "client!nr", name = "D", descriptor = "[I")
    public final int[] anIntArray528;

    @OriginalMember(owner = "client!nr", name = "<init>", descriptor = "(Lclient!iaa;[IIIIIZ)V")
    public JavaArgbSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int[] pixels, @OriginalArg(2) int offset, @OriginalArg(3) int stride, @OriginalArg(4) int width, @OriginalArg(5) int height, @OriginalArg(6) boolean copy) {
        super(toolkit, width, height);
        if (copy) {
            this.anIntArray528 = new int[width * height];
        } else {
            this.anIntArray528 = pixels;
        }
        @Pc(21) int step = stride - super.anInt9302;
        @Pc(23) int index = 0;
        for (@Pc(25) int row = 0; row < height; row++) {
            for (@Pc(28) int column = 0; column < width; column++) {
                this.anIntArray528[index++] = pixels[offset++];
            }
            offset += step;
        }
    }

    @OriginalMember(owner = "client!nr", name = "<init>", descriptor = "(Lclient!iaa;II)V")
    public JavaArgbSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int width, @OriginalArg(2) int height) {
        super(toolkit, width, height);
        this.anIntArray528 = new int[width * height];
    }

    @OriginalMember(owner = "client!nr", name = "<init>", descriptor = "(Lclient!iaa;[III)V")
    public JavaArgbSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int[] pixels, @OriginalArg(2) int width, @OriginalArg(3) int height) {
        super(toolkit, width, height);
        this.anIntArray528 = pixels;
    }

    @OriginalMember(owner = "client!nr", name = "a", descriptor = "(IIIII)V")
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
        @Pc(33) int height = super.anInt9306;
        @Pc(36) int width = super.anInt9302;
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
            @Pc(734) int local734;
            @Pc(742) int local742;
            @Pc(754) int local754;
            if (mode == 1) {
                if (op == 1) {
                    for (local174 = -height; local174 < 0; local174++) {
                        for (local181 = -width; local181 < 0; local181++) {
                            local267 = this.anIntArray528[srcIndex++];
                            local270 = local267 >>> 24;
                            local274 = 256 - local270;
                            local281 = raster[dstIndex];
                            raster[dstIndex++] = ((local267 & 0xFF00FF) * local270 + (local281 & 0xFF00FF) * local274 >> 8 & 0xFFFF00FF) + (((local267 & 0xFF00FF00) >>> 8) * local270 + ((local281 & 0xFF00FF00) >>> 8) * local274 & 0xFF00FF00);
                        }
                        dstIndex += dstStep;
                        srcIndex += srcStep;
                    }
                } else if (op == 0) {
                    if ((color & 0xFFFFFF) == 16777215) {
                        for (local174 = -height; local174 < 0; local174++) {
                            for (local181 = -width; local181 < 0; local181++) {
                                local267 = this.anIntArray528[srcIndex++];
                                local270 = (local267 >>> 24) * (color >>> 24) >> 8;
                                local274 = 256 - local270;
                                local281 = raster[dstIndex];
                                raster[dstIndex++] = ((local267 & 0xFF00FF) * local270 + (local281 & 0xFF00FF) * local274 & 0xFF00FF00) + ((local267 & 0xFF00) * local270 + (local281 & 0xFF00) * local274 & 0xFF0000) >> 8;
                            }
                            dstIndex += dstStep;
                            srcIndex += srcStep;
                        }
                    } else {
                        local174 = color >> 16 & 0xFF;
                        local181 = color >> 8 & 0xFF;
                        local267 = color & 0xFF;
                        for (local270 = -height; local270 < 0; local270++) {
                            for (local274 = -width; local274 < 0; local274++) {
                                local281 = this.anIntArray528[srcIndex++];
                                local289 = (local281 >>> 24) * (color >>> 24) >> 8;
                                local297 = 256 - local289;
                                if (local289 == 255) {
                                    local305 = (local281 & 0xFF0000) * local174 & 0xFF000000;
                                    local734 = (local281 & 0xFF00) * local181 & 0xFF0000;
                                    local742 = (local281 & 0xFF) * local267 & 0xFF00;
                                    raster[dstIndex++] = (local305 | local734 | local742) >>> 8;
                                } else {
                                    local305 = (local281 & 0xFF0000) * local174 & 0xFF000000;
                                    local734 = (local281 & 0xFF00) * local181 & 0xFF0000;
                                    local742 = (local281 & 0xFF) * local267 & 0xFF00;
                                    local281 = (local305 | local734 | local742) >>> 8;
                                    local754 = raster[dstIndex];
                                    raster[dstIndex++] = ((local281 & 0xFF00FF) * local289 + (local754 & 0xFF00FF) * local297 & 0xFF00FF00) + ((local281 & 0xFF00) * local289 + (local754 & 0xFF00) * local297 & 0xFF0000) >> 8;
                                }
                            }
                            dstIndex += dstStep;
                            srcIndex += srcStep;
                        }
                    }
                } else if (op == 3) {
                    for (local174 = -height; local174 < 0; local174++) {
                        for (local181 = -width; local181 < 0; local181++) {
                            local267 = this.anIntArray528[srcIndex++];
                            local270 = local267 + color;
                            local274 = (local267 & 0xFF00FF) + (color & 0xFF00FF);
                            local281 = (local274 & 0x1000100) + (local270 - local274 & 0x10000);
                            local281 = local270 - local281 | local281 - (local281 >>> 8);
                            local289 = (local281 >>> 24) * (color >>> 24) >> 8;
                            local297 = 256 - local289;
                            if (local289 != 255) {
                                local267 = local281;
                                local281 = raster[dstIndex];
                                local281 = ((local267 & 0xFF00FF) * local289 + (local281 & 0xFF00FF) * local297 & 0xFF00FF00) + ((local267 & 0xFF00) * local289 + (local281 & 0xFF00) * local297 & 0xFF0000) >> 8;
                            }
                            raster[dstIndex++] = local281;
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
                            local289 = this.anIntArray528[srcIndex++];
                            local297 = local289 >>> 24;
                            local305 = 256 - local297;
                            local267 = (local289 & 0xFF00FF) * local174 & 0xFF00FF00;
                            local270 = (local289 & 0xFF00) * local174 & 0xFF0000;
                            local289 = ((local267 | local270) >>> 8) + lerpColour;
                            local734 = raster[dstIndex];
                            raster[dstIndex++] = ((local289 & 0xFF00FF) * local297 + (local734 & 0xFF00FF) * local305 & 0xFF00FF00) + ((local289 & 0xFF00) * local297 + (local734 & 0xFF00) * local305 & 0xFF0000) >> 8;
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
                        local267 = this.anIntArray528[srcIndex++];
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
                        local281 = this.anIntArray528[srcIndex++];
                        if (local281 == 0) {
                            dstIndex++;
                        } else {
                            local289 = (local281 & 0xFF0000) * local174 & 0xFF000000;
                            local297 = (local281 & 0xFF00) * local181 & 0xFF0000;
                            local305 = (local281 & 0xFF) * local267 & 0xFF00;
                            local281 = (local289 | local297 | local305) >>> 8;
                            local734 = raster[dstIndex];
                            local742 = local281 + local734;
                            local754 = (local281 & 0xFF00FF) + (local734 & 0xFF00FF);
                            local734 = (local754 & 0x1000100) + (local742 - local754 & 0x10000);
                            raster[dstIndex++] = local742 - local734 | local734 - (local734 >>> 8);
                        }
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else if (op == 3) {
                for (local174 = -height; local174 < 0; local174++) {
                    for (local181 = -width; local181 < 0; local181++) {
                        local267 = this.anIntArray528[srcIndex++];
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
                        local289 = this.anIntArray528[srcIndex++];
                        if (local289 == 0) {
                            dstIndex++;
                        } else {
                            local267 = (local289 & 0xFF00FF) * local174 & 0xFF00FF00;
                            local270 = (local289 & 0xFF00) * local174 & 0xFF0000;
                            local289 = ((local267 | local270) >>> 8) + lerpColour;
                            local297 = raster[dstIndex];
                            local305 = local289 + local297;
                            local734 = (local289 & 0xFF00FF) + (local297 & 0xFF00FF);
                            @Pc(1505) int local1505 = (local734 & 0x1000100) + (local305 - local734 & 0x10000);
                            raster[dstIndex++] = local305 - local1505 | local1505 - (local1505 >>> 8);
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
                    raster[dstIndex++] = this.anIntArray528[srcIndex++];
                    raster[dstIndex++] = this.anIntArray528[srcIndex++];
                    raster[dstIndex++] = this.anIntArray528[srcIndex++];
                    raster[dstIndex++] = this.anIntArray528[srcIndex++];
                }
                local181 += 3;
                while (dstIndex < local181) {
                    raster[dstIndex++] = this.anIntArray528[srcIndex++];
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
                    local281 = this.anIntArray528[srcIndex++];
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
                    local267 = this.anIntArray528[srcIndex++];
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
                    local289 = this.anIntArray528[srcIndex++];
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

    @OriginalMember(owner = "client!nr", name = "b", descriptor = "(II)V")
    @Override
    protected void blitParallelogram(@OriginalArg(0) int op) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        @Pc(956) int skip;
        @Pc(11) int row;
        @Pc(14) int dstIndex;
        @Pc(16) int u;
        @Pc(18) int v;
        @Pc(20) int column;
        @Pc(57) int texel;
        @Pc(60) int dst;
        @Pc(223) int src;
        @Pc(227) int local227;
        @Pc(231) int local231;
        @Pc(235) int local235;
        @Pc(348) int local348;
        @Pc(356) int local356;
        @Pc(368) int local368;
        if (JavaSpriteBlitState.duDx == 0) {
            if (JavaSpriteBlitState.dvDx == 0) {
                row = JavaSpriteBlitState.negativeHeight;
                while (row < 0) {
                    dstIndex = JavaSpriteBlitState.rowOffset;
                    u = JavaSpriteBlitState.rowU;
                    v = JavaSpriteBlitState.rowV;
                    column = JavaSpriteBlitState.negativeWidth;
                    if (u >= 0 && v >= 0 && u - (super.anInt9302 << 12) < 0 && v - (super.anInt9306 << 12) < 0) {
                        while (column < 0) {
                            texel = (v >> 12) * super.anInt9302 + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            } else if (op == 0) {
                                src = this.anIntArray528[texel];
                                local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local231 = 256 - local227;
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local235 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                } else if (local227 == 255) {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local235 | local348 | local356) >>> 8;
                                } else {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local235 | local348 | local356) >>> 8;
                                    local368 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                }
                            } else if (op == 3) {
                                src = this.anIntArray528[texel];
                                local227 = JavaSpriteBlitState.colour;
                                local231 = src + local227;
                                local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                                local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                                local348 = local231 - local348 | local348 - (local348 >>> 8);
                                local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local368 = 256 - local356;
                                if (local356 != 255) {
                                    src = local348;
                                    local348 = raster[dst];
                                    local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                                }
                                raster[dst] = local348;
                            } else if (op == 2) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                                local356 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                    if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                        @Pc(949) int vOverrun;
                        if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(977) int vBound;
                        if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.anInt9302 + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            } else if (op == 0) {
                                src = this.anIntArray528[texel];
                                local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local231 = 256 - local227;
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local235 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                } else if (local227 == 255) {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local235 | local348 | local356) >>> 8;
                                } else {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local235 | local348 | local356) >>> 8;
                                    local368 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                }
                            } else if (op == 3) {
                                src = this.anIntArray528[texel];
                                local227 = JavaSpriteBlitState.colour;
                                local231 = src + local227;
                                local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                                local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                                local348 = local231 - local348 | local348 - (local348 >>> 8);
                                local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local368 = 256 - local356;
                                if (local356 != 255) {
                                    src = local348;
                                    local348 = raster[dst];
                                    local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                                }
                                raster[dst] = local348;
                            } else if (op == 2) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                                local356 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                    if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                        if (v < 0) {
                            skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1923) int vBound;
                        if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.anInt9302 + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            } else if (op == 0) {
                                src = this.anIntArray528[texel];
                                local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local231 = 256 - local227;
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local235 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                } else if (local227 == 255) {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local235 | local348 | local356) >>> 8;
                                } else {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local235 | local348 | local356) >>> 8;
                                    local368 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                }
                            } else if (op == 3) {
                                src = this.anIntArray528[texel];
                                local227 = JavaSpriteBlitState.colour;
                                local231 = src + local227;
                                local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                                local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                                local348 = local231 - local348 | local348 - (local348 >>> 8);
                                local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local368 = 256 - local356;
                                if (local356 != 255) {
                                    src = local348;
                                    local348 = raster[dst];
                                    local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                                }
                                raster[dst] = local348;
                            } else if (op == 2) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                                local356 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                    if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                        @Pc(2843) int uOverrun;
                        if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(2871) int uBound;
                        if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.anInt9302 + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            } else if (op == 0) {
                                src = this.anIntArray528[texel];
                                local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local231 = 256 - local227;
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local235 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                } else if (local227 == 255) {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local235 | local348 | local356) >>> 8;
                                } else {
                                    local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local235 | local348 | local356) >>> 8;
                                    local368 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                                }
                            } else if (op == 3) {
                                src = this.anIntArray528[texel];
                                local227 = JavaSpriteBlitState.colour;
                                local231 = src + local227;
                                local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                                local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                                local348 = local231 - local348 | local348 - (local348 >>> 8);
                                local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                                local368 = 256 - local356;
                                if (local356 != 255) {
                                    src = local348;
                                    local348 = raster[dst];
                                    local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                                }
                                raster[dst] = local348;
                            } else if (op == 2) {
                                src = this.anIntArray528[texel];
                                local227 = src >>> 24;
                                local231 = 256 - local227;
                                local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                                local356 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                    @Pc(3778) int uOverrun;
                    if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(3812) int uBound;
                    if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    @Pc(3824) int vOverrun;
                    if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(3858) int vBound;
                    if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.anInt9302 + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            src = this.anIntArray528[texel];
                            local227 = src >>> 24;
                            local231 = 256 - local227;
                            local235 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                        } else if (op == 0) {
                            src = this.anIntArray528[texel];
                            local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                            local231 = 256 - local227;
                            if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                local235 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            } else if (local227 == 255) {
                                local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                raster[dst] = (local235 | local348 | local356) >>> 8;
                            } else {
                                local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                src = (local235 | local348 | local356) >>> 8;
                                local368 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            }
                        } else if (op == 3) {
                            src = this.anIntArray528[texel];
                            local227 = JavaSpriteBlitState.colour;
                            local231 = src + local227;
                            local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                            local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                            local348 = local231 - local348 | local348 - (local348 >>> 8);
                            local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                            local368 = 256 - local356;
                            if (local356 != 255) {
                                src = local348;
                                local348 = raster[dst];
                                local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                            }
                            raster[dst] = local348;
                        } else if (op == 2) {
                            src = this.anIntArray528[texel];
                            local227 = src >>> 24;
                            local231 = 256 - local227;
                            local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                            local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                            src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                            local356 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                    @Pc(4771) int uOverrun;
                    if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(4805) int uBound;
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
                    @Pc(4853) int vBound;
                    if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.anInt9302 + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            src = this.anIntArray528[texel];
                            local227 = src >>> 24;
                            local231 = 256 - local227;
                            local235 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                        } else if (op == 0) {
                            src = this.anIntArray528[texel];
                            local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                            local231 = 256 - local227;
                            if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                local235 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            } else if (local227 == 255) {
                                local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                raster[dst] = (local235 | local348 | local356) >>> 8;
                            } else {
                                local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                src = (local235 | local348 | local356) >>> 8;
                                local368 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            }
                        } else if (op == 3) {
                            src = this.anIntArray528[texel];
                            local227 = JavaSpriteBlitState.colour;
                            local231 = src + local227;
                            local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                            local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                            local348 = local231 - local348 | local348 - (local348 >>> 8);
                            local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                            local368 = 256 - local356;
                            if (local356 != 255) {
                                src = local348;
                                local348 = raster[dst];
                                local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                            }
                            raster[dst] = local348;
                        } else if (op == 2) {
                            src = this.anIntArray528[texel];
                            local227 = src >>> 24;
                            local231 = 256 - local227;
                            local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                            local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                            src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                            local356 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                    if (u < 0) {
                        skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(5809) int uBound;
                    if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.anInt9302 + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            src = this.anIntArray528[texel];
                            local227 = src >>> 24;
                            local231 = 256 - local227;
                            local235 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                        } else if (op == 0) {
                            src = this.anIntArray528[texel];
                            local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                            local231 = 256 - local227;
                            if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                local235 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            } else if (local227 == 255) {
                                local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                raster[dst] = (local235 | local348 | local356) >>> 8;
                            } else {
                                local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                src = (local235 | local348 | local356) >>> 8;
                                local368 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                            }
                        } else if (op == 3) {
                            src = this.anIntArray528[texel];
                            local227 = JavaSpriteBlitState.colour;
                            local231 = src + local227;
                            local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                            local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                            local348 = local231 - local348 | local348 - (local348 >>> 8);
                            local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                            local368 = 256 - local356;
                            if (local356 != 255) {
                                src = local348;
                                local348 = raster[dst];
                                local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                            }
                            raster[dst] = local348;
                        } else if (op == 2) {
                            src = this.anIntArray528[texel];
                            local227 = src >>> 24;
                            local231 = 256 - local227;
                            local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                            local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                            src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                            local356 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                @Pc(6756) int uBound;
                if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                    column = uBound;
                }
                @Pc(6768) int vOverrun;
                if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                    skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                    column += skip;
                    u += JavaSpriteBlitState.duDx * skip;
                    v += JavaSpriteBlitState.dvDx * skip;
                    dstIndex += skip;
                }
                @Pc(6802) int vBound;
                if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                    column = vBound;
                }
                while (column < 0) {
                    texel = (v >> 12) * super.anInt9302 + (u >> 12);
                    dst = dstIndex++;
                    if (op == 1) {
                        src = this.anIntArray528[texel];
                        local227 = src >>> 24;
                        local231 = 256 - local227;
                        local235 = raster[dst];
                        raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                    } else if (op == 0) {
                        src = this.anIntArray528[texel];
                        local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                        local231 = 256 - local227;
                        if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                            local235 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                        } else if (local227 == 255) {
                            local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                            local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                            local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                            raster[dst] = (local235 | local348 | local356) >>> 8;
                        } else {
                            local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                            local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                            local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                            src = (local235 | local348 | local356) >>> 8;
                            local368 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                        }
                    } else if (op == 3) {
                        src = this.anIntArray528[texel];
                        local227 = JavaSpriteBlitState.colour;
                        local231 = src + local227;
                        local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                        local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                        local348 = local231 - local348 | local348 - (local348 >>> 8);
                        local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                        local368 = 256 - local356;
                        if (local356 != 255) {
                            src = local348;
                            local348 = raster[dst];
                            local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                        }
                        raster[dst] = local348;
                    } else if (op == 2) {
                        src = this.anIntArray528[texel];
                        local227 = src >>> 24;
                        local231 = 256 - local227;
                        local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                        local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                        src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                        local356 = raster[dst];
                        raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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
                @Pc(7751) int uBound;
                if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                    column = uBound;
                }
                if (v < 0) {
                    skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                    column += skip;
                    u += JavaSpriteBlitState.duDx * skip;
                    v += JavaSpriteBlitState.dvDx * skip;
                    dstIndex += skip;
                }
                @Pc(7799) int vBound;
                if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                    column = vBound;
                }
                while (column < 0) {
                    texel = (v >> 12) * super.anInt9302 + (u >> 12);
                    dst = dstIndex++;
                    if (op == 1) {
                        src = this.anIntArray528[texel];
                        local227 = src >>> 24;
                        local231 = 256 - local227;
                        local235 = raster[dst];
                        raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                    } else if (op == 0) {
                        src = this.anIntArray528[texel];
                        local227 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                        local231 = 256 - local227;
                        if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                            local235 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local235 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local235 & 0xFF00) * local231 & 0xFF0000) >> 8;
                        } else if (local227 == 255) {
                            local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                            local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                            local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                            raster[dst] = (local235 | local348 | local356) >>> 8;
                        } else {
                            local235 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                            local348 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                            local356 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                            src = (local235 | local348 | local356) >>> 8;
                            local368 = raster[dst];
                            raster[dst] = ((src & 0xFF00FF) * local227 + (local368 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local368 & 0xFF00) * local231 & 0xFF0000) >> 8;
                        }
                    } else if (op == 3) {
                        src = this.anIntArray528[texel];
                        local227 = JavaSpriteBlitState.colour;
                        local231 = src + local227;
                        local235 = (src & 0xFF00FF) + (local227 & 0xFF00FF);
                        local348 = (local235 & 0x1000100) + (local231 - local235 & 0x10000);
                        local348 = local231 - local348 | local348 - (local348 >>> 8);
                        local356 = (src >>> 24) * JavaSpriteBlitState.alpha >> 8;
                        local368 = 256 - local356;
                        if (local356 != 255) {
                            src = local348;
                            local348 = raster[dst];
                            local348 = ((src & 0xFF00FF) * local356 + (local348 & 0xFF00FF) * local368 & 0xFF00FF00) + ((src & 0xFF00) * local356 + (local348 & 0xFF00) * local368 & 0xFF0000) >> 8;
                        }
                        raster[dst] = local348;
                    } else if (op == 2) {
                        src = this.anIntArray528[texel];
                        local227 = src >>> 24;
                        local231 = 256 - local227;
                        local235 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                        local348 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                        src = ((local235 | local348) >>> 8) + JavaSpriteBlitState.lerpColour;
                        local356 = raster[dst];
                        raster[dst] = ((src & 0xFF00FF) * local227 + (local356 & 0xFF00FF) * local231 & 0xFF00FF00) + ((src & 0xFF00) * local227 + (local356 & 0xFF00) * local231 & 0xFF0000) >> 8;
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

    @OriginalMember(owner = "client!nr", name = "b", descriptor = "(IIIIIIIII)V")
    @Override
    public void method8207(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int op, @OriginalArg(6) int colour, @OriginalArg(7) int mode) {
        if (width <= 0 || height <= 0) {
            return;
        }
        @Pc(9) int u = 0;
        @Pc(11) int v = 0;
        @Pc(20) int scaleWidth = super.leftMargin + super.anInt9302 + super.rightMargin;
        @Pc(29) int scaleHeight = super.topMargin + super.anInt9306 + super.bottomMargin;
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
        if (super.anInt9302 < scaleWidth) {
            width = ((super.anInt9302 << 16) + uStep - u - 1) / uStep;
        }
        if (super.anInt9306 < scaleHeight) {
            height = ((super.anInt9306 << 16) + vStep - v - 1) / vStep;
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
            @Pc(975) int local975;
            @Pc(983) int local983;
            @Pc(995) int local995;
            if (mode == 1) {
                if (op == 1) {
                    local262 = u;
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.anInt9302;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local348 = this.anIntArray528[(u >> 16) + local273];
                                local356 = local348 >>> 24;
                                local359 = 256 - local356;
                                local376 = raster[offset];
                                raster[offset] = ((local348 & 0xFF00FF) * local356 + (local376 & 0xFF00FF) * local359 >> 8 & 0xFFFF00FF) + (((local348 & 0xFF00FF00) >>> 8) * local356 + ((local376 & 0xFF00FF00) >>> 8) * local359 & 0xFF00FF00);
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
                    local262 = u;
                    if ((colour & 0xFFFFFF) == 16777215) {
                        for (local265 = -height; local265 < 0; local265++) {
                            local273 = (v >> 16) * super.anInt9302;
                            for (local276 = -width; local276 < 0; local276++) {
                                if ((float) z < depth[offset]) {
                                    local348 = this.anIntArray528[(u >> 16) + local273];
                                    local356 = (local348 >>> 24) * (colour >>> 24) >> 8;
                                    local359 = 256 - local356;
                                    local376 = raster[offset];
                                    raster[offset] = ((local348 & 0xFF00FF) * local356 + (local376 & 0xFF00FF) * local359 & 0xFF00FF00) + ((local348 & 0xFF00) * local356 + (local376 & 0xFF00) * local359 & 0xFF0000) >> 8;
                                    depth[offset] = (float) z;
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
                        for (local348 = -height; local348 < 0; local348++) {
                            local356 = (v >> 16) * super.anInt9302;
                            for (local359 = -width; local359 < 0; local359++) {
                                if ((float) z < depth[offset]) {
                                    local376 = this.anIntArray528[(u >> 16) + local356];
                                    local384 = (local376 >>> 24) * (colour >>> 24) >> 8;
                                    local392 = 256 - local384;
                                    if (local384 == 255) {
                                        local400 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                        local975 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                        local983 = (local376 & 0xFF) * local276 & 0xFF00;
                                        raster[offset] = (local400 | local975 | local983) >>> 8;
                                        depth[offset] = (float) z;
                                    } else {
                                        local400 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                        local975 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                        local983 = (local376 & 0xFF) * local276 & 0xFF00;
                                        local376 = (local400 | local975 | local983) >>> 8;
                                        local995 = raster[offset];
                                        raster[offset] = ((local376 & 0xFF00FF) * local384 + (local995 & 0xFF00FF) * local392 & 0xFF00FF00) + ((local376 & 0xFF00) * local384 + (local995 & 0xFF00) * local392 & 0xFF0000) >> 8;
                                        depth[offset] = (float) z;
                                        @Pc(1038) int local1038 = (local995 >>> 24) + local384;
                                        if (local1038 > 255) {
                                            local1038 = 255;
                                        }
                                        raster[offset] |= local1038 << 24;
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
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.anInt9302;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local348 = this.anIntArray528[(u >> 16) + local273];
                                local356 = local348 + colour;
                                local359 = (local348 & 0xFF00FF) + (colour & 0xFF00FF);
                                local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                                local376 = local356 - local376 | local376 - (local376 >>> 8);
                                local384 = (local376 >>> 24) * (colour >>> 24) >> 8;
                                local392 = 256 - local384;
                                if (local384 != 255) {
                                    local348 = local376;
                                    local376 = raster[offset];
                                    local376 = ((local348 & 0xFF00FF) * local384 + (local376 & 0xFF00FF) * local392 & 0xFF00FF00) + ((local348 & 0xFF00) * local384 + (local376 & 0xFF00) * local392 & 0xFF0000) >> 8;
                                }
                                raster[offset] = local376;
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
                        local359 = (v >> 16) * super.anInt9302;
                        for (local376 = -width; local376 < 0; local376++) {
                            if ((float) z < depth[offset]) {
                                local384 = this.anIntArray528[(u >> 16) + local359];
                                local392 = local384 >>> 24;
                                local400 = 256 - local392;
                                local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                local384 = ((local273 | local276) >>> 8) + lerpColour;
                                local975 = raster[offset];
                                raster[offset] = ((local384 & 0xFF00FF) * local392 + (local975 & 0xFF00FF) * local400 & 0xFF00FF00) + ((local384 & 0xFF00) * local392 + (local975 & 0xFF00) * local400 & 0xFF0000) >> 8;
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
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.anInt9302;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.anIntArray528[(u >> 16) + local273];
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
                    local356 = (v >> 16) * super.anInt9302;
                    for (local359 = -width; local359 < 0; local359++) {
                        if ((float) z < depth[offset]) {
                            local376 = this.anIntArray528[(u >> 16) + local356];
                            if (local376 != 0) {
                                local384 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                local392 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                local400 = (local376 & 0xFF) * local276 & 0xFF00;
                                local376 = (local384 | local392 | local400) >>> 8;
                                local975 = raster[offset];
                                local983 = local376 + local975;
                                local995 = (local376 & 0xFF00FF) + (local975 & 0xFF00FF);
                                local975 = (local995 & 0x1000100) + (local983 - local995 & 0x10000);
                                raster[offset] = local983 - local975 | local975 - (local975 >>> 8);
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
                    local273 = (v >> 16) * super.anInt9302;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.anIntArray528[(u >> 16) + local273];
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
                    local359 = (v >> 16) * super.anInt9302;
                    for (local376 = -width; local376 < 0; local376++) {
                        if ((float) z < depth[offset]) {
                            local384 = this.anIntArray528[(u >> 16) + local359];
                            if (local384 != 0) {
                                local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                local384 = ((local273 | local276) >>> 8) + lerpColour;
                                local392 = raster[offset];
                                local400 = local384 + local392;
                                local975 = (local384 & 0xFF00FF) + (local392 & 0xFF00FF);
                                @Pc(1946) int local1946 = (local975 & 0x1000100) + (local400 - local975 & 0x10000);
                                raster[offset] = local400 - local1946 | local1946 - (local1946 >>> 8);
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
                local273 = (v >> 16) * super.anInt9302;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        raster[offset] = this.anIntArray528[(u >> 16) + local273];
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
                local356 = (v >> 16) * super.anInt9302;
                for (local359 = -width; local359 < 0; local359++) {
                    if ((float) z < depth[offset]) {
                        local376 = this.anIntArray528[(u >> 16) + local356];
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
                local273 = (v >> 16) * super.anInt9302;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        local348 = this.anIntArray528[(u >> 16) + local273];
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
                local359 = (v >> 16) * super.anInt9302;
                for (local376 = -width; local376 < 0; local376++) {
                    if ((float) z < depth[offset]) {
                        local384 = this.anIntArray528[(u >> 16) + local359];
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

    @OriginalMember(owner = "client!nr", name = "a", descriptor = "(III)V")
    @Override
    public void copyAlpha(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int channel) {
        if (channel == 0) {
            @Pc(203) int[] raster = super.toolkit.surfaceRaster;
            for (@Pc(205) int row = 0; row < super.anInt9306; row++) {
                @Pc(211) int dstIndex = row * super.anInt9302;
                @Pc(221) int srcIndex = (y + row) * super.toolkit.surfaceWidth + x;
                for (@Pc(223) int column = 0; column < super.anInt9302; column++) {
                    this.anIntArray528[dstIndex + column] = (this.anIntArray528[dstIndex + column] & 0xFFFFFF) | ((raster[srcIndex + column] << 8) & ~0xFFFFFF);
                }
            }
        } else if (channel == 1) {
            @Pc(203) int[] raster = super.toolkit.surfaceRaster;
            for (@Pc(205) int row = 0; row < super.anInt9306; row++) {
                @Pc(211) int dstIndex = row * super.anInt9302;
                @Pc(221) int srcIndex = (y + row) * super.toolkit.surfaceWidth + x;
                for (@Pc(223) int column = 0; column < super.anInt9302; column++) {
                    this.anIntArray528[dstIndex + column] = (this.anIntArray528[dstIndex + column] & 0xFFFFFF) | ((raster[srcIndex + column] << 16) & ~0xFFFFFF);
                }
            }
        } else if (channel == 2) {
            @Pc(203) int[] raster = super.toolkit.surfaceRaster;
            for (@Pc(205) int row = 0; row < super.anInt9306; row++) {
                @Pc(211) int dstIndex = row * super.anInt9302;
                @Pc(221) int srcIndex = (y + row) * super.toolkit.surfaceWidth + x;
                for (@Pc(223) int column = 0; column < super.anInt9302; column++) {
                    this.anIntArray528[dstIndex + column] = (this.anIntArray528[dstIndex + column] & 0xFFFFFF) | ((raster[srcIndex + column] << 24) & ~0xFFFFFF);
                }
            }
        } else if (channel == 3) {
            @Pc(203) int[] raster = super.toolkit.surfaceRaster;
            for (@Pc(205) int row = 0; row < super.anInt9306; row++) {
                @Pc(211) int dstIndex = row * super.anInt9302;
                @Pc(221) int srcIndex = row * super.toolkit.surfaceWidth;
                for (@Pc(223) int column = 0; column < super.anInt9302; column++) {
                    this.anIntArray528[dstIndex + column] = (this.anIntArray528[dstIndex + column] & 0xFFFFFF) | ((raster[srcIndex + column] == 0) ? 0 : ~0xFFFFFF);
                }
            }
        }
    }

    @OriginalMember(owner = "client!nr", name = "a", descriptor = "([I[III)V")
    @Override
    protected void blitParallelogramMasked(@OriginalArg(0) int[] lineOffsets, @OriginalArg(1) int[] lineWidths, @OriginalArg(2) int maskOffsetX, @OriginalArg(3) int maskOffsetY) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        @Pc(236) int skip;
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
        @Pc(126) int srcAlpha;
        @Pc(130) int srcInvAlpha;
        @Pc(134) int dst;
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
                        if (u >= 0 && v >= 0 && u - (super.anInt9302 << 12) < 0 && v - (super.anInt9306 << 12) < 0) {
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
                                src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                                srcAlpha = src >>> 24;
                                srcInvAlpha = 256 - srcAlpha;
                                dst = raster[dstIndex];
                                raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                        if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                            @Pc(229) int vOverrun;
                            if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                                skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                                column += skip;
                                v += JavaSpriteBlitState.dvDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(257) int vBound;
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
                                src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                                srcAlpha = src >>> 24;
                                srcInvAlpha = 256 - srcAlpha;
                                dst = raster[dstIndex];
                                raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                        if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                            if (v < 0) {
                                skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                                column += skip;
                                v += JavaSpriteBlitState.dvDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(470) int vBound;
                            if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
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
                                src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                                srcAlpha = src >>> 24;
                                srcInvAlpha = 256 - srcAlpha;
                                dst = raster[dstIndex];
                                raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                        if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                            @Pc(657) int uOverrun;
                            if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                                skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                                column += skip;
                                u += JavaSpriteBlitState.duDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(685) int uBound;
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
                                src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                                srcAlpha = src >>> 24;
                                srcInvAlpha = 256 - srcAlpha;
                                dst = raster[dstIndex];
                                raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                        @Pc(859) int uOverrun;
                        if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(893) int uBound;
                        if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        @Pc(905) int vOverrun;
                        if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(939) int vBound;
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
                            src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                            srcAlpha = src >>> 24;
                            srcInvAlpha = 256 - srcAlpha;
                            dst = raster[dstIndex];
                            raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                        @Pc(1119) int uOverrun;
                        if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1153) int uBound;
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
                        @Pc(1201) int vBound;
                        if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
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
                            src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                            srcAlpha = src >>> 24;
                            srcInvAlpha = 256 - srcAlpha;
                            dst = raster[dstIndex];
                            raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                    if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                        if (u < 0) {
                            skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1424) int uBound;
                        if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
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
                            src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                            srcAlpha = src >>> 24;
                            srcInvAlpha = 256 - srcAlpha;
                            dst = raster[dstIndex];
                            raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                    @Pc(1638) int uBound;
                    if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    @Pc(1650) int vOverrun;
                    if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(1684) int vBound;
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
                        src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                        srcAlpha = src >>> 24;
                        srcInvAlpha = 256 - srcAlpha;
                        dst = raster[dstIndex];
                        raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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
                    @Pc(1900) int uBound;
                    if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    if (v < 0) {
                        skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(1948) int vBound;
                    if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
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
                        src = this.anIntArray528[(v >> 12) * super.anInt9302 + (u >> 12)];
                        srcAlpha = src >>> 24;
                        srcInvAlpha = 256 - srcAlpha;
                        dst = raster[dstIndex];
                        raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
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

    @OriginalMember(owner = "client!nr", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void copyRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int srcX, @OriginalArg(5) int srcY) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        for (@Pc(5) int row = 0; row < height; row++) {
            @Pc(14) int dstIndex = (y + row) * width + x;
            @Pc(22) int srcIndex = (srcY + row) * width + srcX;
            for (@Pc(24) int column = 0; column < width; column++) {
                this.anIntArray528[dstIndex + column] = raster[srcIndex + column] & 0xFFFFFF;
            }
        }
    }

    @OriginalMember(owner = "client!nr", name = "a", descriptor = "(IILclient!aa;II)V")
    @Override
    public void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) ClippingMask mask, @OriginalArg(3) int maskX, @OriginalArg(4) int maskY) {
        if (super.toolkit.stopped()) {
            throw new IllegalStateException();
        }
        x += super.leftMargin;
        y += super.topMargin;
        @Pc(20) int srcIndex = 0;
        @Pc(24) int dstStride = super.toolkit.surfaceWidth;
        @Pc(27) int width = super.anInt9302;
        @Pc(30) int height = super.anInt9306;
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
            srcIndex += (maskY - y) * super.anInt9302;
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
                @Pc(316) int src = this.anIntArray528[srcIndex++];
                @Pc(320) int srcAlpha = src >>> 24;
                @Pc(324) int srcInvAlpha = 256 - srcAlpha;
                @Pc(328) int dst = raster[dstIndex];
                raster[dstIndex++] = ((src & 0xFF00FF) * srcAlpha + (dst & 0xFF00FF) * srcInvAlpha & 0xFF00FF00) + ((src & 0xFF00) * srcAlpha + (dst & 0xFF00) * srcInvAlpha & 0xFF0000) >> 8;
            }
            srcIndex += skip + srcStep;
            dstIndex += skip + dstStep;
        }
    }

    @OriginalMember(owner = "client!nr", name = "a", descriptor = "(IIIIIIII)V")
    @Override
    protected void renderImpl(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode) {
        if (super.toolkit.stopped()) {
            throw new IllegalStateException();
        } else if (width > 0 && height > 0) {
            @Pc(18) int u = 0;
            @Pc(20) int v = 0;
            @Pc(24) int dstStride = super.toolkit.surfaceWidth;
            @Pc(33) int scaleWidth = super.leftMargin + super.anInt9302 + super.rightMargin;
            @Pc(42) int scaleHeight = super.topMargin + super.anInt9306 + super.bottomMargin;
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
            if (super.anInt9302 < scaleWidth) {
                width = ((super.anInt9302 << 16) + uStep - u - 1) / uStep;
            }
            if (super.anInt9306 < scaleHeight) {
                height = ((super.anInt9306 << 16) + vStep - v - 1) / vStep;
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
                @Pc(897) int local897;
                @Pc(905) int local905;
                @Pc(917) int local917;
                if (mode == 1) {
                    if (op == 1) {
                        local265 = u;
                        for (local268 = -height; local268 < 0; local268++) {
                            local276 = (v >> 16) * super.anInt9302;
                            for (local279 = -width; local279 < 0; local279++) {
                                local337 = this.anIntArray528[(u >> 16) + local276];
                                local345 = local337 >>> 24;
                                local348 = 256 - local345;
                                local358 = raster[offset];
                                raster[offset++] = ((local337 & 0xFF00FF) * local345 + (local358 & 0xFF00FF) * local348 >> 8 & 0xFFFF00FF) + (((local337 & 0xFF00FF00) >>> 8) * local345 + ((local358 & 0xFF00FF00) >>> 8) * local348 & 0xFF00FF00);
                                u += uStep;
                            }
                            v += vStep;
                            u = local265;
                            offset += dstStep;
                        }
                    } else if (op == 0) {
                        local265 = u;
                        if ((colour & 0xFFFFFF) == 16777215) {
                            for (local268 = -height; local268 < 0; local268++) {
                                local276 = (v >> 16) * super.anInt9302;
                                for (local279 = -width; local279 < 0; local279++) {
                                    local337 = this.anIntArray528[(u >> 16) + local276];
                                    local345 = (local337 >>> 24) * (colour >>> 24) >> 8;
                                    local348 = 256 - local345;
                                    local358 = raster[offset];
                                    raster[offset++] = ((local337 & 0xFF00FF) * local345 + (local358 & 0xFF00FF) * local348 & 0xFF00FF00) + ((local337 & 0xFF00) * local345 + (local358 & 0xFF00) * local348 & 0xFF0000) >> 8;
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
                            for (local337 = -height; local337 < 0; local337++) {
                                local345 = (v >> 16) * super.anInt9302;
                                for (local348 = -width; local348 < 0; local348++) {
                                    local358 = this.anIntArray528[(u >> 16) + local345];
                                    local366 = (local358 >>> 24) * (colour >>> 24) >> 8;
                                    local374 = 256 - local366;
                                    if (local366 == 255) {
                                        local382 = (local358 & 0xFF0000) * local268 & 0xFF000000;
                                        local897 = (local358 & 0xFF00) * local276 & 0xFF0000;
                                        local905 = (local358 & 0xFF) * local279 & 0xFF00;
                                        raster[offset++] = (local382 | local897 | local905) >>> 8;
                                    } else {
                                        local382 = (local358 & 0xFF0000) * local268 & 0xFF000000;
                                        local897 = (local358 & 0xFF00) * local276 & 0xFF0000;
                                        local905 = (local358 & 0xFF) * local279 & 0xFF00;
                                        local358 = (local382 | local897 | local905) >>> 8;
                                        local917 = raster[offset];
                                        raster[offset++] = ((local358 & 0xFF00FF) * local366 + (local917 & 0xFF00FF) * local374 & 0xFF00FF00) + ((local358 & 0xFF00) * local366 + (local917 & 0xFF00) * local374 & 0xFF0000) >> 8;
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
                        for (local268 = -height; local268 < 0; local268++) {
                            local276 = (v >> 16) * super.anInt9302;
                            for (local279 = -width; local279 < 0; local279++) {
                                local337 = this.anIntArray528[(u >> 16) + local276];
                                local345 = local337 + colour;
                                local348 = (local337 & 0xFF00FF) + (colour & 0xFF00FF);
                                local358 = (local348 & 0x1000100) + (local345 - local348 & 0x10000);
                                local358 = local345 - local358 | local358 - (local358 >>> 8);
                                local366 = (local358 >>> 24) * (colour >>> 24) >> 8;
                                local374 = 256 - local366;
                                if (local366 != 255) {
                                    local337 = local358;
                                    local358 = raster[offset];
                                    local358 = ((local337 & 0xFF00FF) * local366 + (local358 & 0xFF00FF) * local374 & 0xFF00FF00) + ((local337 & 0xFF00) * local366 + (local358 & 0xFF00) * local374 & 0xFF0000) >> 8;
                                }
                                raster[offset++] = local358;
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
                            local348 = (v >> 16) * super.anInt9302;
                            for (local358 = -width; local358 < 0; local358++) {
                                local366 = this.anIntArray528[(u >> 16) + local348];
                                local374 = local366 >>> 24;
                                local382 = 256 - local374;
                                local276 = (local366 & 0xFF00FF) * local265 & 0xFF00FF00;
                                local279 = (local366 & 0xFF00) * local265 & 0xFF0000;
                                local366 = ((local276 | local279) >>> 8) + lerpColour;
                                local897 = raster[offset];
                                raster[offset++] = ((local366 & 0xFF00FF) * local374 + (local897 & 0xFF00FF) * local382 & 0xFF00FF00) + ((local366 & 0xFF00) * local374 + (local897 & 0xFF00) * local382 & 0xFF0000) >> 8;
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
                        local276 = (v >> 16) * super.anInt9302;
                        for (local279 = -width; local279 < 0; local279++) {
                            local337 = this.anIntArray528[(u >> 16) + local276];
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
                        local345 = (v >> 16) * super.anInt9302;
                        for (local348 = -width; local348 < 0; local348++) {
                            local358 = this.anIntArray528[(u >> 16) + local345];
                            if (local358 == 0) {
                                offset++;
                            } else {
                                local366 = (local358 & 0xFF0000) * local268 & 0xFF000000;
                                local374 = (local358 & 0xFF00) * local276 & 0xFF0000;
                                local382 = (local358 & 0xFF) * local279 & 0xFF00;
                                local358 = (local366 | local374 | local382) >>> 8;
                                local897 = raster[offset];
                                local905 = local358 + local897;
                                local917 = (local358 & 0xFF00FF) + (local897 & 0xFF00FF);
                                local897 = (local917 & 0x1000100) + (local905 - local917 & 0x10000);
                                raster[offset++] = local905 - local897 | local897 - (local897 >>> 8);
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
                        local276 = (v >> 16) * super.anInt9302;
                        for (local279 = -width; local279 < 0; local279++) {
                            local337 = this.anIntArray528[(u >> 16) + local276];
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
                        local348 = (v >> 16) * super.anInt9302;
                        for (local358 = -width; local358 < 0; local358++) {
                            local366 = this.anIntArray528[(u >> 16) + local348];
                            if (local366 == 0) {
                                offset++;
                            } else {
                                local276 = (local366 & 0xFF00FF) * local265 & 0xFF00FF00;
                                local279 = (local366 & 0xFF00) * local265 & 0xFF0000;
                                local366 = ((local276 | local279) >>> 8) + lerpColour;
                                local374 = raster[offset];
                                local382 = local366 + local374;
                                local897 = (local366 & 0xFF00FF) + (local374 & 0xFF00FF);
                                @Pc(1776) int local1776 = (local897 & 0x1000100) + (local382 - local897 & 0x10000);
                                raster[offset++] = local382 - local1776 | local1776 - (local1776 >>> 8);
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
                    local276 = (v >> 16) * super.anInt9302;
                    for (local279 = -width; local279 < 0; local279++) {
                        raster[offset++] = this.anIntArray528[(u >> 16) + local276];
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
                    local345 = (v >> 16) * super.anInt9302;
                    for (local348 = -width; local348 < 0; local348++) {
                        local358 = this.anIntArray528[(u >> 16) + local345];
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
                    local276 = (v >> 16) * super.anInt9302;
                    for (local279 = -width; local279 < 0; local279++) {
                        local337 = this.anIntArray528[(u >> 16) + local276];
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
                    local348 = (v >> 16) * super.anInt9302;
                    for (local358 = -width; local358 < 0; local358++) {
                        local366 = this.anIntArray528[(u >> 16) + local348];
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

    @OriginalMember(owner = "client!nr", name = "a", descriptor = "(IIIIIIIII)V")
    @Override
    public void method8208(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int op, @OriginalArg(6) int colour, @OriginalArg(7) int mode) {
        if (width <= 0 || height <= 0) {
            return;
        }
        @Pc(9) int u = 0;
        @Pc(11) int v = 0;
        @Pc(20) int scaleWidth = super.leftMargin + super.anInt9302 + super.rightMargin;
        @Pc(29) int scaleHeight = super.topMargin + super.anInt9306 + super.bottomMargin;
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
        if (super.anInt9302 < scaleWidth) {
            width = ((super.anInt9302 << 16) + uStep - u - 1) / uStep;
        }
        if (super.anInt9306 < scaleHeight) {
            height = ((super.anInt9306 << 16) + vStep - v - 1) / vStep;
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
            @Pc(975) int local975;
            @Pc(983) int local983;
            @Pc(995) int local995;
            if (mode == 1) {
                if (op == 1) {
                    local262 = u;
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.anInt9302;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local348 = this.anIntArray528[(u >> 16) + local273];
                                local356 = local348 >>> 24;
                                local359 = 256 - local356;
                                local376 = raster[offset];
                                raster[offset] = ((local348 & 0xFF00FF) * local356 + (local376 & 0xFF00FF) * local359 >> 8 & 0xFFFF00FF) + (((local348 & 0xFF00FF00) >>> 8) * local356 + ((local376 & 0xFF00FF00) >>> 8) * local359 & 0xFF00FF00);
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
                    local262 = u;
                    if ((colour & 0xFFFFFF) == 16777215) {
                        for (local265 = -height; local265 < 0; local265++) {
                            local273 = (v >> 16) * super.anInt9302;
                            for (local276 = -width; local276 < 0; local276++) {
                                if ((float) z < depth[offset]) {
                                    local348 = this.anIntArray528[(u >> 16) + local273];
                                    local356 = (local348 >>> 24) * (colour >>> 24) >> 8;
                                    local359 = 256 - local356;
                                    local376 = raster[offset];
                                    raster[offset] = ((local348 & 0xFF00FF) * local356 + (local376 & 0xFF00FF) * local359 & 0xFF00FF00) + ((local348 & 0xFF00) * local356 + (local376 & 0xFF00) * local359 & 0xFF0000) >> 8;
                                    depth[offset] = (float) z;
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
                        for (local348 = -height; local348 < 0; local348++) {
                            local356 = (v >> 16) * super.anInt9302;
                            for (local359 = -width; local359 < 0; local359++) {
                                if ((float) z < depth[offset]) {
                                    local376 = this.anIntArray528[(u >> 16) + local356];
                                    local384 = (local376 >>> 24) * (colour >>> 24) >> 8;
                                    local392 = 256 - local384;
                                    if (local384 == 255) {
                                        local400 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                        local975 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                        local983 = (local376 & 0xFF) * local276 & 0xFF00;
                                        raster[offset] = (local400 | local975 | local983) >>> 8;
                                        depth[offset] = (float) z;
                                    } else {
                                        local400 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                        local975 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                        local983 = (local376 & 0xFF) * local276 & 0xFF00;
                                        local376 = (local400 | local975 | local983) >>> 8;
                                        local995 = raster[offset];
                                        raster[offset] = ((local376 & 0xFF00FF) * local384 + (local995 & 0xFF00FF) * local392 & 0xFF00FF00) + ((local376 & 0xFF00) * local384 + (local995 & 0xFF00) * local392 & 0xFF0000) >> 8;
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
                    }
                } else if (op == 3) {
                    local262 = u;
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.anInt9302;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local348 = this.anIntArray528[(u >> 16) + local273];
                                local356 = local348 + colour;
                                local359 = (local348 & 0xFF00FF) + (colour & 0xFF00FF);
                                local376 = (local359 & 0x1000100) + (local356 - local359 & 0x10000);
                                local376 = local356 - local376 | local376 - (local376 >>> 8);
                                local384 = (local376 >>> 24) * (colour >>> 24) >> 8;
                                local392 = 256 - local384;
                                if (local384 != 255) {
                                    local348 = local376;
                                    local376 = raster[offset];
                                    local376 = ((local348 & 0xFF00FF) * local384 + (local376 & 0xFF00FF) * local392 & 0xFF00FF00) + ((local348 & 0xFF00) * local384 + (local376 & 0xFF00) * local392 & 0xFF0000) >> 8;
                                }
                                raster[offset] = local376;
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
                        local359 = (v >> 16) * super.anInt9302;
                        for (local376 = -width; local376 < 0; local376++) {
                            if ((float) z < depth[offset]) {
                                local384 = this.anIntArray528[(u >> 16) + local359];
                                local392 = local384 >>> 24;
                                local400 = 256 - local392;
                                local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                local384 = ((local273 | local276) >>> 8) + lerpColour;
                                local975 = raster[offset];
                                raster[offset] = ((local384 & 0xFF00FF) * local392 + (local975 & 0xFF00FF) * local400 & 0xFF00FF00) + ((local384 & 0xFF00) * local392 + (local975 & 0xFF00) * local400 & 0xFF0000) >> 8;
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
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.anInt9302;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.anIntArray528[(u >> 16) + local273];
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
                    local356 = (v >> 16) * super.anInt9302;
                    for (local359 = -width; local359 < 0; local359++) {
                        if ((float) z < depth[offset]) {
                            local376 = this.anIntArray528[(u >> 16) + local356];
                            if (local376 != 0) {
                                local384 = (local376 & 0xFF0000) * local265 & 0xFF000000;
                                local392 = (local376 & 0xFF00) * local273 & 0xFF0000;
                                local400 = (local376 & 0xFF) * local276 & 0xFF00;
                                local376 = (local384 | local392 | local400) >>> 8;
                                local975 = raster[offset];
                                local983 = local376 + local975;
                                local995 = (local376 & 0xFF00FF) + (local975 & 0xFF00FF);
                                local975 = (local995 & 0x1000100) + (local983 - local995 & 0x10000);
                                raster[offset] = local983 - local975 | local975 - (local975 >>> 8);
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
                    local273 = (v >> 16) * super.anInt9302;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local348 = this.anIntArray528[(u >> 16) + local273];
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
                    local359 = (v >> 16) * super.anInt9302;
                    for (local376 = -width; local376 < 0; local376++) {
                        if ((float) z < depth[offset]) {
                            local384 = this.anIntArray528[(u >> 16) + local359];
                            if (local384 != 0) {
                                local273 = (local384 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local384 & 0xFF00) * local262 & 0xFF0000;
                                local384 = ((local273 | local276) >>> 8) + lerpColour;
                                local392 = raster[offset];
                                local400 = local384 + local392;
                                local975 = (local384 & 0xFF00FF) + (local392 & 0xFF00FF);
                                @Pc(1926) int local1926 = (local975 & 0x1000100) + (local400 - local975 & 0x10000);
                                raster[offset] = local400 - local1926 | local1926 - (local1926 >>> 8);
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
                local273 = (v >> 16) * super.anInt9302;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        raster[offset] = this.anIntArray528[(u >> 16) + local273];
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
                local356 = (v >> 16) * super.anInt9302;
                for (local359 = -width; local359 < 0; local359++) {
                    if ((float) z < depth[offset]) {
                        local376 = this.anIntArray528[(u >> 16) + local356];
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
                local273 = (v >> 16) * super.anInt9302;
                for (local276 = -width; local276 < 0; local276++) {
                    if ((float) z < depth[offset]) {
                        local348 = this.anIntArray528[(u >> 16) + local273];
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
                local359 = (v >> 16) * super.anInt9302;
                for (local376 = -width; local376 < 0; local376++) {
                    if ((float) z < depth[offset]) {
                        local384 = this.anIntArray528[(u >> 16) + local359];
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
}
