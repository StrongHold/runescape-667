import com.jagex.graphics.ClippingMask;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!te")
public final class JavaIndexedSprite extends JavaSprite {

    @OriginalMember(owner = "client!te", name = "D", descriptor = "[B")
    public final byte[] pixels;

    @OriginalMember(owner = "client!te", name = "I", descriptor = "[I")
    public final int[] palette;

    @OriginalMember(owner = "client!te", name = "<init>", descriptor = "(Lclient!iaa;[B[III)V")
    public JavaIndexedSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) byte[] pixels, @OriginalArg(2) int[] palette, @OriginalArg(3) int width, @OriginalArg(4) int height) {
        super(toolkit, width, height);
        this.pixels = pixels;
        this.palette = palette;
    }

    @OriginalMember(owner = "client!te", name = "a", descriptor = "(IIIII)V")
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
        @Pc(292) int local292;
        @Pc(295) int local295;
        @Pc(299) int local299;
        @Pc(311) int local311;
        @Pc(327) int local327;
        @Pc(335) int local335;
        @Pc(468) int lerpColour;
        @Pc(319) int local319;
        if (mode != 0) {
            @Pc(1076) byte local1076;
            @Pc(783) int local783;
            @Pc(791) int local791;
            @Pc(799) int local799;
            @Pc(811) int local811;
            @Pc(556) byte local556;
            if (mode == 1) {
                if (op == 1) {
                    for (local174 = -height; local174 < 0; local174++) {
                        for (local181 = -width; local181 < 0; local181++) {
                            local556 = this.pixels[srcIndex++];
                            if (local556 == 0) {
                                dstIndex++;
                            } else {
                                local295 = this.palette[local556 & 0xFF] | 0xFF000000;
                                local319 = raster[dstIndex];
                                raster[dstIndex++] = ((local295 & 0xFF00FF) * 255 + (local319 & 0xFF00FF) * 0 >> 8 & 0xFFFF00FF) + (((local295 & 0xFF00FF00) >>> 8) * 255 + ((local319 & 0xFF00FF00) >>> 8) * 0 & 0xFF00FF00);
                            }
                        }
                        dstIndex += dstStep;
                        srcIndex += srcStep;
                    }
                } else {
                    @Pc(661) byte local661;
                    if (op == 0) {
                        if ((color & 0xFFFFFF) == 16777215) {
                            local174 = color >>> 24;
                            local181 = 256 - local174;
                            for (local292 = -height; local292 < 0; local292++) {
                                for (local295 = -width; local295 < 0; local295++) {
                                    local661 = this.pixels[srcIndex++];
                                    if (local661 == 0) {
                                        dstIndex++;
                                    } else {
                                        local311 = this.palette[local661 & 0xFF];
                                        local319 = raster[dstIndex];
                                        raster[dstIndex++] = ((local311 & 0xFF00FF) * local174 + (local319 & 0xFF00FF) * local181 & 0xFF00FF00) + ((local311 & 0xFF00) * local174 + (local319 & 0xFF00) * local181 & 0xFF0000) >> 8;
                                    }
                                }
                                dstIndex += dstStep;
                                srcIndex += srcStep;
                            }
                        } else {
                            local174 = color >> 16 & 0xFF;
                            local181 = color >> 8 & 0xFF;
                            local292 = color & 0xFF;
                            local295 = color >>> 24;
                            local299 = 256 - local295;
                            for (local311 = -height; local311 < 0; local311++) {
                                for (local319 = -width; local319 < 0; local319++) {
                                    @Pc(763) byte local763 = this.pixels[srcIndex++];
                                    if (local763 == 0) {
                                        dstIndex++;
                                    } else {
                                        local335 = this.palette[local763 & 0xFF];
                                        if (local295 == 255) {
                                            local783 = (local335 & 0xFF0000) * local174 & 0xFF000000;
                                            local791 = (local335 & 0xFF00) * local181 & 0xFF0000;
                                            local799 = (local335 & 0xFF) * local292 & 0xFF00;
                                            raster[dstIndex++] = (local783 | local791 | local799) >>> 8;
                                        } else {
                                            local783 = (local335 & 0xFF0000) * local174 & 0xFF000000;
                                            local791 = (local335 & 0xFF00) * local181 & 0xFF0000;
                                            local799 = (local335 & 0xFF) * local292 & 0xFF00;
                                            local335 = (local783 | local791 | local799) >>> 8;
                                            local811 = raster[dstIndex];
                                            raster[dstIndex++] = ((local335 & 0xFF00FF) * local295 + (local811 & 0xFF00FF) * local299 & 0xFF00FF00) + ((local335 & 0xFF00) * local295 + (local811 & 0xFF00) * local299 & 0xFF0000) >> 8;
                                        }
                                    }
                                }
                                dstIndex += dstStep;
                                srcIndex += srcStep;
                            }
                        }
                    } else if (op == 3) {
                        local174 = color >>> 24;
                        local181 = 256 - local174;
                        for (local292 = -height; local292 < 0; local292++) {
                            for (local295 = -width; local295 < 0; local295++) {
                                local661 = this.pixels[srcIndex++];
                                local311 = local661 > 0 ? this.palette[local661] : 0;
                                local319 = local311 + color;
                                local327 = (local311 & 0xFF00FF) + (color & 0xFF00FF);
                                local335 = (local327 & 0x1000100) + (local319 - local327 & 0x10000);
                                local335 = local319 - local335 | local335 - (local335 >>> 8);
                                if (local311 == 0 && local174 != 255) {
                                    local311 = local335;
                                    local335 = raster[dstIndex];
                                    local335 = ((local311 & 0xFF00FF) * local174 + (local335 & 0xFF00FF) * local181 & 0xFF00FF00) + ((local311 & 0xFF00) * local174 + (local335 & 0xFF00) * local181 & 0xFF0000) >> 8;
                                }
                                raster[dstIndex++] = local335;
                            }
                            dstIndex += dstStep;
                            srcIndex += srcStep;
                        }
                    } else if (op == 2) {
                        local174 = color >>> 24;
                        local181 = 256 - local174;
                        local292 = (color & 0xFF00FF) * local181 & 0xFF00FF00;
                        local295 = (color & 0xFF00) * local181 & 0xFF0000;
                        lerpColour = (local292 | local295) >>> 8;
                        for (local299 = -height; local299 < 0; local299++) {
                            for (local311 = -width; local311 < 0; local311++) {
                                local1076 = this.pixels[srcIndex++];
                                if (local1076 == 0) {
                                    dstIndex++;
                                } else {
                                    local327 = this.palette[local1076 & 0xFF];
                                    local292 = (local327 & 0xFF00FF) * local174 & 0xFF00FF00;
                                    local295 = (local327 & 0xFF00) * local174 & 0xFF0000;
                                    raster[dstIndex++] = ((local292 | local295) >>> 8) + lerpColour;
                                }
                            }
                            dstIndex += dstStep;
                            srcIndex += srcStep;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                for (local174 = -height; local174 < 0; local174++) {
                    for (local181 = -width; local181 < 0; local181++) {
                        local556 = this.pixels[srcIndex++];
                        if (local556 == 0) {
                            dstIndex++;
                        } else {
                            local295 = this.palette[local556 & 0xFF];
                            local299 = raster[dstIndex];
                            local311 = local295 + local299;
                            local319 = (local295 & 0xFF00FF) + (local299 & 0xFF00FF);
                            local299 = (local319 & 0x1000100) + (local311 - local319 & 0x10000);
                            raster[dstIndex++] = local311 - local299 | local299 - (local299 >>> 8);
                        }
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else if (op == 0) {
                local174 = color >> 16 & 0xFF;
                local181 = color >> 8 & 0xFF;
                local292 = color & 0xFF;
                for (local295 = -height; local295 < 0; local295++) {
                    for (local299 = -width; local299 < 0; local299++) {
                        @Pc(1254) byte local1254 = this.pixels[srcIndex++];
                        if (local1254 == 0) {
                            dstIndex++;
                        } else {
                            local319 = this.palette[local1254 & 0xFF];
                            local327 = (local319 & 0xFF0000) * local174 & 0xFF000000;
                            local335 = (local319 & 0xFF00) * local181 & 0xFF0000;
                            local783 = (local319 & 0xFF) * local292 & 0xFF00;
                            local319 = (local327 | local335 | local783) >>> 8;
                            local791 = raster[dstIndex];
                            local799 = local319 + local791;
                            local811 = (local319 & 0xFF00FF) + (local791 & 0xFF00FF);
                            local791 = (local811 & 0x1000100) + (local799 - local811 & 0x10000);
                            raster[dstIndex++] = local799 - local791 | local791 - (local791 >>> 8);
                        }
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else if (op == 3) {
                for (local174 = -height; local174 < 0; local174++) {
                    for (local181 = -width; local181 < 0; local181++) {
                        local556 = this.pixels[srcIndex++];
                        local295 = local556 > 0 ? this.palette[local556] : 0;
                        local299 = local295 + color;
                        local311 = (local295 & 0xFF00FF) + (color & 0xFF00FF);
                        local319 = (local311 & 0x1000100) + (local299 - local311 & 0x10000);
                        local295 = local299 - local319 | local319 - (local319 >>> 8);
                        @Pc(1415) int local1415 = raster[dstIndex];
                        local299 = local295 + local1415;
                        local311 = (local295 & 0xFF00FF) + (local1415 & 0xFF00FF);
                        @Pc(1437) int local1437 = (local311 & 0x1000100) + (local299 - local311 & 0x10000);
                        raster[dstIndex++] = local299 - local1437 | local1437 - (local1437 >>> 8);
                    }
                    dstIndex += dstStep;
                    srcIndex += srcStep;
                }
            } else if (op == 2) {
                local174 = color >>> 24;
                local181 = 256 - local174;
                local292 = (color & 0xFF00FF) * local181 & 0xFF00FF00;
                local295 = (color & 0xFF00) * local181 & 0xFF0000;
                lerpColour = (local292 | local295) >>> 8;
                for (local299 = -height; local299 < 0; local299++) {
                    for (local311 = -width; local311 < 0; local311++) {
                        local1076 = this.pixels[srcIndex++];
                        if (local1076 == 0) {
                            dstIndex++;
                        } else {
                            local327 = this.palette[local1076 & 0xFF];
                            local292 = (local327 & 0xFF00FF) * local174 & 0xFF00FF00;
                            local295 = (local327 & 0xFF00) * local174 & 0xFF0000;
                            @Pc(1546) int local1546 = ((local292 | local295) >>> 8) + lerpColour;
                            local335 = raster[dstIndex];
                            local783 = local1546 + local335;
                            local791 = (local1546 & 0xFF00FF) + (local335 & 0xFF00FF);
                            @Pc(1572) int local1572 = (local791 & 0x1000100) + (local783 - local791 & 0x10000);
                            raster[dstIndex++] = local783 - local1572 | local1572 - (local1572 >>> 8);
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
                    raster[dstIndex++] = this.palette[this.pixels[srcIndex++] & 0xFF];
                    raster[dstIndex++] = this.palette[this.pixels[srcIndex++] & 0xFF];
                    raster[dstIndex++] = this.palette[this.pixels[srcIndex++] & 0xFF];
                    raster[dstIndex++] = this.palette[this.pixels[srcIndex++] & 0xFF];
                }
                local181 += 3;
                while (dstIndex < local181) {
                    raster[dstIndex++] = this.palette[this.pixels[srcIndex++] & 0xFF];
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else if (op == 0) {
            local174 = color >> 16 & 0xFF;
            local181 = color >> 8 & 0xFF;
            local292 = color & 0xFF;
            for (local295 = -height; local295 < 0; local295++) {
                for (local299 = -width; local299 < 0; local299++) {
                    local311 = this.palette[this.pixels[srcIndex++] & 0xFF];
                    local319 = (local311 & 0xFF0000) * local174 & 0xFF000000;
                    local327 = (local311 & 0xFF00) * local181 & 0xFF0000;
                    local335 = (local311 & 0xFF) * local292 & 0xFF00;
                    raster[dstIndex++] = (local319 | local327 | local335) >>> 8;
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else if (op == 3) {
            for (local174 = -height; local174 < 0; local174++) {
                for (local181 = -width; local181 < 0; local181++) {
                    local292 = this.palette[this.pixels[srcIndex++] & 0xFF];
                    local295 = local292 + color;
                    local299 = (local292 & 0xFF00FF) + (color & 0xFF00FF);
                    local311 = (local299 & 0x1000100) + (local295 - local299 & 0x10000);
                    raster[dstIndex++] = local295 - local311 | local311 - (local311 >>> 8);
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else if (op == 2) {
            local174 = color >>> 24;
            local181 = 256 - local174;
            local292 = (color & 0xFF00FF) * local181 & 0xFF00FF00;
            local295 = (color & 0xFF00) * local181 & 0xFF0000;
            lerpColour = (local292 | local295) >>> 8;
            for (local299 = -height; local299 < 0; local299++) {
                for (local311 = -width; local311 < 0; local311++) {
                    local319 = this.palette[this.pixels[srcIndex++] & 0xFF];
                    local292 = (local319 & 0xFF00FF) * local174 & 0xFF00FF00;
                    local295 = (local319 & 0xFF00) * local174 & 0xFF0000;
                    raster[dstIndex++] = ((local292 | local295) >>> 8) + lerpColour;
                }
                dstIndex += dstStep;
                srcIndex += srcStep;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!te", name = "a", descriptor = "([I[III)V")
    @Override
    protected void blitParallelogramMasked(@OriginalArg(0) int[] lineOffsets, @OriginalArg(1) int[] lineWidths, @OriginalArg(2) int maskOffsetX, @OriginalArg(3) int maskOffsetY) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        @Pc(206) int skip;
        @Pc(11) int row;
        @Pc(16) int maskIndex;
        @Pc(28) int dstIndex;
        @Pc(30) int u;
        @Pc(32) int v;
        @Pc(34) int column;
        @Pc(64) int maskStart;
        @Pc(69) int maskCount;
        @Pc(75) int maskSkip;
        @Pc(122) byte index;
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
                                index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                                if (index == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = this.palette[index & 0xFF];
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
                        if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                            @Pc(199) int vOverrun;
                            if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                                skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                                column += skip;
                                v += JavaSpriteBlitState.dvDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(227) int vBound;
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
                                index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                                if (index == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = this.palette[index & 0xFF];
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
                        if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                            if (v < 0) {
                                skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                                column += skip;
                                v += JavaSpriteBlitState.dvDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(410) int vBound;
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
                                index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                                if (index == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = this.palette[index & 0xFF];
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
                        if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                            @Pc(567) int uOverrun;
                            if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                                skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                                column += skip;
                                u += JavaSpriteBlitState.duDx * skip;
                                dstIndex += skip;
                            }
                            @Pc(595) int uBound;
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
                                index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                                if (index == 0) {
                                    dstIndex++;
                                } else {
                                    raster[dstIndex++] = this.palette[index & 0xFF];
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
                        @Pc(739) int uOverrun;
                        if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(773) int uBound;
                        if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        @Pc(785) int vOverrun;
                        if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(819) int vBound;
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
                            index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                            if (index == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = this.palette[index & 0xFF];
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
                        @Pc(969) int uOverrun;
                        if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1003) int uBound;
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
                        @Pc(1051) int vBound;
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
                            index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                            if (index == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = this.palette[index & 0xFF];
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
                    if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                        if (u < 0) {
                            skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1244) int uBound;
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
                            index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                            if (index == 0) {
                                dstIndex++;
                            } else {
                                raster[dstIndex++] = this.palette[index & 0xFF];
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
                    @Pc(1428) int uBound;
                    if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    @Pc(1440) int vOverrun;
                    if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(1474) int vBound;
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
                        index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                        if (index == 0) {
                            dstIndex++;
                        } else {
                            raster[dstIndex++] = this.palette[index & 0xFF];
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
                    @Pc(1660) int uBound;
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
                    @Pc(1708) int vBound;
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
                        index = this.pixels[(v >> 12) * super.anInt9302 + (u >> 12)];
                        if (index == 0) {
                            dstIndex++;
                        } else {
                            raster[dstIndex++] = this.palette[index & 0xFF];
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

    @OriginalMember(owner = "client!te", name = "b", descriptor = "(II)V")
    @Override
    protected void blitParallelogram(@OriginalArg(0) int op) {
        @Pc(3) int[] raster = super.toolkit.surfaceRaster;
        @Pc(963) int skip;
        @Pc(11) int row;
        @Pc(14) int dstIndex;
        @Pc(16) int u;
        @Pc(18) int v;
        @Pc(20) int column;
        @Pc(57) int texel;
        @Pc(60) int dst;
        @Pc(241) byte index;
        @Pc(270) int src;
        @Pc(279) int local279;
        @Pc(283) int local283;
        @Pc(287) int local287;
        @Pc(359) int local359;
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
                                index = this.pixels[texel];
                                if (index != 0) {
                                    raster[dst] = this.palette[index & 0xFF];
                                }
                            } else if (op == 0) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local279 = JavaSpriteBlitState.colour >>> 24;
                                        local283 = 256 - local279;
                                        local287 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local279 | local283 | local287) >>> 8;
                                    } else {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local279 | local283 | local287) >>> 8;
                                        local359 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                index = this.pixels[texel];
                                src = index > 0 ? this.palette[index] : 0;
                                local279 = JavaSpriteBlitState.colour;
                                local283 = src + local279;
                                local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                                local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                                local359 = local283 - local359 | local359 - (local359 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local359;
                                    local359 = raster[dst];
                                    local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local359;
                            } else if (op == 2) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                    if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                        @Pc(956) int vOverrun;
                        if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(984) int vBound;
                        if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.anInt9302 + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    raster[dst] = this.palette[index & 0xFF];
                                }
                            } else if (op == 0) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local279 = JavaSpriteBlitState.colour >>> 24;
                                        local283 = 256 - local279;
                                        local287 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local279 | local283 | local287) >>> 8;
                                    } else {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local279 | local283 | local287) >>> 8;
                                        local359 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                index = this.pixels[texel];
                                src = index > 0 ? this.palette[index] : 0;
                                local279 = JavaSpriteBlitState.colour;
                                local283 = src + local279;
                                local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                                local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                                local359 = local283 - local359 | local359 - (local359 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local359;
                                    local359 = raster[dst];
                                    local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local359;
                            } else if (op == 2) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                    if (u >= 0 && u - (super.anInt9302 << 12) < 0) {
                        if (v < 0) {
                            skip = (JavaSpriteBlitState.dvDx - v - 1) / JavaSpriteBlitState.dvDx;
                            column += skip;
                            v += JavaSpriteBlitState.dvDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(1937) int vBound;
                        if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                            column = vBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.anInt9302 + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    raster[dst] = this.palette[index & 0xFF];
                                }
                            } else if (op == 0) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local279 = JavaSpriteBlitState.colour >>> 24;
                                        local283 = 256 - local279;
                                        local287 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local279 | local283 | local287) >>> 8;
                                    } else {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local279 | local283 | local287) >>> 8;
                                        local359 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                index = this.pixels[texel];
                                src = index > 0 ? this.palette[index] : 0;
                                local279 = JavaSpriteBlitState.colour;
                                local283 = src + local279;
                                local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                                local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                                local359 = local283 - local359 | local359 - (local359 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local359;
                                    local359 = raster[dst];
                                    local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local359;
                            } else if (op == 2) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                    if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                        @Pc(2864) int uOverrun;
                        if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                            skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                            column += skip;
                            u += JavaSpriteBlitState.duDx * skip;
                            dstIndex += skip;
                        }
                        @Pc(2892) int uBound;
                        if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                            column = uBound;
                        }
                        while (column < 0) {
                            texel = (v >> 12) * super.anInt9302 + (u >> 12);
                            dst = dstIndex++;
                            if (op == 1) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    raster[dst] = this.palette[index & 0xFF];
                                }
                            } else if (op == 0) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                        local279 = JavaSpriteBlitState.colour >>> 24;
                                        local283 = 256 - local279;
                                        local287 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                                    } else if (JavaSpriteBlitState.alpha == 255) {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        raster[dst] = (local279 | local283 | local287) >>> 8;
                                    } else {
                                        local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                        local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                        local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                        src = (local279 | local283 | local287) >>> 8;
                                        local359 = raster[dst];
                                        raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                    }
                                }
                            } else if (op == 3) {
                                index = this.pixels[texel];
                                src = index > 0 ? this.palette[index] : 0;
                                local279 = JavaSpriteBlitState.colour;
                                local283 = src + local279;
                                local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                                local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                                local359 = local283 - local359 | local359 - (local359 >>> 8);
                                if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                    src = local359;
                                    local359 = raster[dst];
                                    local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                                raster[dst] = local359;
                            } else if (op == 2) {
                                index = this.pixels[texel];
                                if (index != 0) {
                                    src = this.palette[index & 0xFF];
                                    local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                    raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                    @Pc(3806) int uOverrun;
                    if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(3840) int uBound;
                    if ((uBound = (u - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    @Pc(3852) int vOverrun;
                    if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(3886) int vBound;
                    if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.anInt9302 + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                raster[dst] = this.palette[index & 0xFF];
                            }
                        } else if (op == 0) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                src = this.palette[index & 0xFF];
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local279 = JavaSpriteBlitState.colour >>> 24;
                                    local283 = 256 - local279;
                                    local287 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                                } else if (JavaSpriteBlitState.alpha == 255) {
                                    local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local279 | local283 | local287) >>> 8;
                                } else {
                                    local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local279 | local283 | local287) >>> 8;
                                    local359 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                            }
                        } else if (op == 3) {
                            index = this.pixels[texel];
                            src = index > 0 ? this.palette[index] : 0;
                            local279 = JavaSpriteBlitState.colour;
                            local283 = src + local279;
                            local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                            local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                            local359 = local283 - local359 | local359 - (local359 >>> 8);
                            if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                src = local359;
                                local359 = raster[dst];
                                local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                            raster[dst] = local359;
                        } else if (op == 2) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                src = this.palette[index & 0xFF];
                                local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                    @Pc(4806) int uOverrun;
                    if ((uOverrun = u - (super.anInt9302 << 12)) >= 0) {
                        skip = (JavaSpriteBlitState.duDx - uOverrun) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        v += JavaSpriteBlitState.dvDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(4840) int uBound;
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
                    @Pc(4888) int vBound;
                    if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                        column = vBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.anInt9302 + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                raster[dst] = this.palette[index & 0xFF];
                            }
                        } else if (op == 0) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                src = this.palette[index & 0xFF];
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local279 = JavaSpriteBlitState.colour >>> 24;
                                    local283 = 256 - local279;
                                    local287 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                                } else if (JavaSpriteBlitState.alpha == 255) {
                                    local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local279 | local283 | local287) >>> 8;
                                } else {
                                    local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local279 | local283 | local287) >>> 8;
                                    local359 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                            }
                        } else if (op == 3) {
                            index = this.pixels[texel];
                            src = index > 0 ? this.palette[index] : 0;
                            local279 = JavaSpriteBlitState.colour;
                            local283 = src + local279;
                            local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                            local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                            local359 = local283 - local359 | local359 - (local359 >>> 8);
                            if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                src = local359;
                                local359 = raster[dst];
                                local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                            raster[dst] = local359;
                        } else if (op == 2) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                src = this.palette[index & 0xFF];
                                local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                if (v >= 0 && v - (super.anInt9306 << 12) < 0) {
                    if (u < 0) {
                        skip = (JavaSpriteBlitState.duDx - u - 1) / JavaSpriteBlitState.duDx;
                        column += skip;
                        u += JavaSpriteBlitState.duDx * skip;
                        dstIndex += skip;
                    }
                    @Pc(5851) int uBound;
                    if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                        column = uBound;
                    }
                    while (column < 0) {
                        texel = (v >> 12) * super.anInt9302 + (u >> 12);
                        dst = dstIndex++;
                        if (op == 1) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                raster[dst] = this.palette[index & 0xFF];
                            }
                        } else if (op == 0) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                src = this.palette[index & 0xFF];
                                if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                    local279 = JavaSpriteBlitState.colour >>> 24;
                                    local283 = 256 - local279;
                                    local287 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                                } else if (JavaSpriteBlitState.alpha == 255) {
                                    local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    raster[dst] = (local279 | local283 | local287) >>> 8;
                                } else {
                                    local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                    local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                    local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                    src = (local279 | local283 | local287) >>> 8;
                                    local359 = raster[dst];
                                    raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                                }
                            }
                        } else if (op == 3) {
                            index = this.pixels[texel];
                            src = index > 0 ? this.palette[index] : 0;
                            local279 = JavaSpriteBlitState.colour;
                            local283 = src + local279;
                            local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                            local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                            local359 = local283 - local359 | local359 - (local359 >>> 8);
                            if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                                src = local359;
                                local359 = raster[dst];
                                local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                            raster[dst] = local359;
                        } else if (op == 2) {
                            index = this.pixels[texel];
                            if (index != 0) {
                                src = this.palette[index & 0xFF];
                                local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                                local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                                raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                @Pc(6805) int uBound;
                if ((uBound = (u + 1 - (super.anInt9302 << 12) - JavaSpriteBlitState.duDx) / JavaSpriteBlitState.duDx) > column) {
                    column = uBound;
                }
                @Pc(6817) int vOverrun;
                if ((vOverrun = v - (super.anInt9306 << 12)) >= 0) {
                    skip = (JavaSpriteBlitState.dvDx - vOverrun) / JavaSpriteBlitState.dvDx;
                    column += skip;
                    u += JavaSpriteBlitState.duDx * skip;
                    v += JavaSpriteBlitState.dvDx * skip;
                    dstIndex += skip;
                }
                @Pc(6851) int vBound;
                if ((vBound = (v - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                    column = vBound;
                }
                while (column < 0) {
                    texel = (v >> 12) * super.anInt9302 + (u >> 12);
                    dst = dstIndex++;
                    if (op == 1) {
                        index = this.pixels[texel];
                        if (index != 0) {
                            raster[dst] = this.palette[index & 0xFF];
                        }
                    } else if (op == 0) {
                        index = this.pixels[texel];
                        if (index != 0) {
                            src = this.palette[index & 0xFF];
                            if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                local279 = JavaSpriteBlitState.colour >>> 24;
                                local283 = 256 - local279;
                                local287 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                            } else if (JavaSpriteBlitState.alpha == 255) {
                                local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                raster[dst] = (local279 | local283 | local287) >>> 8;
                            } else {
                                local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                src = (local279 | local283 | local287) >>> 8;
                                local359 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                        }
                    } else if (op == 3) {
                        index = this.pixels[texel];
                        src = index > 0 ? this.palette[index] : 0;
                        local279 = JavaSpriteBlitState.colour;
                        local283 = src + local279;
                        local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                        local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                        local359 = local283 - local359 | local359 - (local359 >>> 8);
                        if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                            src = local359;
                            local359 = raster[dst];
                            local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                        }
                        raster[dst] = local359;
                    } else if (op == 2) {
                        index = this.pixels[texel];
                        if (index != 0) {
                            src = this.palette[index & 0xFF];
                            local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                            local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                            raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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
                @Pc(7807) int uBound;
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
                @Pc(7855) int vBound;
                if ((vBound = (v + 1 - (super.anInt9306 << 12) - JavaSpriteBlitState.dvDx) / JavaSpriteBlitState.dvDx) > column) {
                    column = vBound;
                }
                while (column < 0) {
                    texel = (v >> 12) * super.anInt9302 + (u >> 12);
                    dst = dstIndex++;
                    if (op == 1) {
                        index = this.pixels[texel];
                        if (index != 0) {
                            raster[dst] = this.palette[index & 0xFF];
                        }
                    } else if (op == 0) {
                        index = this.pixels[texel];
                        if (index != 0) {
                            src = this.palette[index & 0xFF];
                            if ((JavaSpriteBlitState.colour & 0xFFFFFF) == 16777215) {
                                local279 = JavaSpriteBlitState.colour >>> 24;
                                local283 = 256 - local279;
                                local287 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * local279 + (local287 & 0xFF00FF) * local283 & 0xFF00FF00) + ((src & 0xFF00) * local279 + (local287 & 0xFF00) * local283 & 0xFF0000) >> 8;
                            } else if (JavaSpriteBlitState.alpha == 255) {
                                local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                raster[dst] = (local279 | local283 | local287) >>> 8;
                            } else {
                                local279 = (src & 0xFF0000) * JavaSpriteBlitState.red & 0xFF000000;
                                local283 = (src & 0xFF00) * JavaSpriteBlitState.green & 0xFF0000;
                                local287 = (src & 0xFF) * JavaSpriteBlitState.blue & 0xFF00;
                                src = (local279 | local283 | local287) >>> 8;
                                local359 = raster[dst];
                                raster[dst] = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                            }
                        }
                    } else if (op == 3) {
                        index = this.pixels[texel];
                        src = index > 0 ? this.palette[index] : 0;
                        local279 = JavaSpriteBlitState.colour;
                        local283 = src + local279;
                        local287 = (src & 0xFF00FF) + (local279 & 0xFF00FF);
                        local359 = (local287 & 0x1000100) + (local283 - local287 & 0x10000);
                        local359 = local283 - local359 | local359 - (local359 >>> 8);
                        if (src == 0 && JavaSpriteBlitState.alpha != 255) {
                            src = local359;
                            local359 = raster[dst];
                            local359 = ((src & 0xFF00FF) * JavaSpriteBlitState.alpha + (local359 & 0xFF00FF) * JavaSpriteBlitState.invAlpha & 0xFF00FF00) + ((src & 0xFF00) * JavaSpriteBlitState.alpha + (local359 & 0xFF00) * JavaSpriteBlitState.invAlpha & 0xFF0000) >> 8;
                        }
                        raster[dst] = local359;
                    } else if (op == 2) {
                        index = this.pixels[texel];
                        if (index != 0) {
                            src = this.palette[index & 0xFF];
                            local279 = (src & 0xFF00FF) * JavaSpriteBlitState.alpha & 0xFF00FF00;
                            local283 = (src & 0xFF00) * JavaSpriteBlitState.alpha & 0xFF0000;
                            raster[dst++] = ((local279 | local283) >>> 8) + JavaSpriteBlitState.lerpColour;
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

    @OriginalMember(owner = "client!te", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void copyRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour) {
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!te", name = "a", descriptor = "(III)V")
    @Override
    public void copyAlpha(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int channel) {
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!te", name = "a", descriptor = "(IIIIIIIII)V")
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
        @Pc(353) int local353;
        @Pc(361) int local361;
        @Pc(364) int local364;
        @Pc(386) int local386;
        @Pc(402) int local402;
        @Pc(410) int local410;
        @Pc(589) int local589;
        @Pc(484) byte local484;
        @Pc(394) int local394;
        if (mode != 0) {
            @Pc(1318) byte local1318;
            @Pc(963) int local963;
            @Pc(971) int local971;
            @Pc(979) int local979;
            @Pc(991) int local991;
            if (mode == 1) {
                if (op == 1) {
                    local262 = u;
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.anInt9302;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local484 = this.pixels[(u >> 16) + local273];
                                if (local484 != 0) {
                                    raster[offset] = this.palette[local484 & 0xFF];
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
                    @Pc(815) byte local815;
                    if (op == 0) {
                        local262 = u;
                        if ((colour & 0xFFFFFF) == 16777215) {
                            local265 = colour >>> 24;
                            local273 = 256 - local265;
                            for (local276 = -height; local276 < 0; local276++) {
                                local353 = (v >> 16) * super.anInt9302;
                                for (local361 = -width; local361 < 0; local361++) {
                                    if ((float) z < depth[offset]) {
                                        local815 = this.pixels[(u >> 16) + local353];
                                        if (local815 != 0) {
                                            local386 = this.palette[local815 & 0xFF];
                                            local394 = raster[offset];
                                            raster[offset] = ((local386 & 0xFF00FF) * local265 + (local394 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local386 & 0xFF00) * local265 + (local394 & 0xFF00) * local273 & 0xFF0000) >> 8;
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
                            local353 = colour >>> 24;
                            local361 = 256 - local353;
                            for (local364 = -height; local364 < 0; local364++) {
                                local386 = (v >> 16) * super.anInt9302;
                                for (local394 = -width; local394 < 0; local394++) {
                                    if ((float) z < depth[offset]) {
                                        @Pc(943) byte local943 = this.pixels[(u >> 16) + local386];
                                        if (local943 != 0) {
                                            local410 = this.palette[local943 & 0xFF];
                                            if (local353 == 255) {
                                                local963 = (local410 & 0xFF0000) * local265 & 0xFF000000;
                                                local971 = (local410 & 0xFF00) * local273 & 0xFF0000;
                                                local979 = (local410 & 0xFF) * local276 & 0xFF00;
                                                raster[offset] = (local963 | local971 | local979) >>> 8;
                                                depth[offset] = (float) z;
                                            } else {
                                                local963 = (local410 & 0xFF0000) * local265 & 0xFF000000;
                                                local971 = (local410 & 0xFF00) * local273 & 0xFF0000;
                                                local979 = (local410 & 0xFF) * local276 & 0xFF00;
                                                local410 = (local963 | local971 | local979) >>> 8;
                                                local991 = raster[offset];
                                                raster[offset] = ((local410 & 0xFF00FF) * local353 + (local991 & 0xFF00FF) * local361 & 0xFF00FF00) + ((local410 & 0xFF00) * local353 + (local991 & 0xFF00) * local361 & 0xFF0000) >> 8;
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
                            local353 = (v >> 16) * super.anInt9302;
                            for (local361 = -width; local361 < 0; local361++) {
                                if ((float) z < depth[offset]) {
                                    local815 = this.pixels[(u >> 16) + local353];
                                    local386 = local815 > 0 ? this.palette[local815] : 0;
                                    local394 = local386 + colour;
                                    local402 = (local386 & 0xFF00FF) + (colour & 0xFF00FF);
                                    local410 = (local402 & 0x1000100) + (local394 - local402 & 0x10000);
                                    local410 = local394 - local410 | local410 - (local410 >>> 8);
                                    if (local386 == 0 && local265 != 255) {
                                        local386 = local410;
                                        local410 = raster[offset];
                                        local410 = ((local386 & 0xFF00FF) * local265 + (local410 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local386 & 0xFF00) * local265 + (local410 & 0xFF00) * local273 & 0xFF0000) >> 8;
                                    }
                                    raster[offset] = local410;
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
                        local589 = (local273 | local276) >>> 8;
                        local353 = u;
                        for (local361 = -height; local361 < 0; local361++) {
                            local364 = (v >> 16) * super.anInt9302;
                            for (local386 = -width; local386 < 0; local386++) {
                                if ((float) z < depth[offset]) {
                                    local1318 = this.pixels[(u >> 16) + local364];
                                    if (local1318 != 0) {
                                        local402 = this.palette[local1318 & 0xFF];
                                        local273 = (local402 & 0xFF00FF) * local262 & 0xFF00FF00;
                                        local276 = (local402 & 0xFF00) * local262 & 0xFF0000;
                                        raster[offset] = ((local273 | local276) >>> 8) + local589;
                                        depth[offset] = (float) z;
                                    }
                                }
                                u += uStep;
                                offset++;
                            }
                            v += vStep;
                            u = local353;
                            offset += dstStep;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.anInt9302;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local484 = this.pixels[(u >> 16) + local273];
                            if (local484 != 0) {
                                local361 = this.palette[local484 & 0xFF];
                                local364 = raster[offset];
                                local386 = local361 + local364;
                                local394 = (local361 & 0xFF00FF) + (local364 & 0xFF00FF);
                                local364 = (local394 & 0x1000100) + (local386 - local394 & 0x10000);
                                raster[offset] = local386 - local364 | local364 - (local364 >>> 8);
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
                for (local353 = -height; local353 < 0; local353++) {
                    local361 = (v >> 16) * super.anInt9302;
                    for (local364 = -width; local364 < 0; local364++) {
                        if ((float) z < depth[offset]) {
                            @Pc(1552) byte local1552 = this.pixels[(u >> 16) + local361];
                            if (local1552 != 0) {
                                local394 = this.palette[local1552 & 0xFF];
                                local402 = (local394 & 0xFF0000) * local265 & 0xFF000000;
                                local410 = (local394 & 0xFF00) * local273 & 0xFF0000;
                                local963 = (local394 & 0xFF) * local276 & 0xFF00;
                                local394 = (local402 | local410 | local963) >>> 8;
                                local971 = raster[offset];
                                local979 = local394 + local971;
                                local991 = (local394 & 0xFF00FF) + (local971 & 0xFF00FF);
                                local971 = (local991 & 0x1000100) + (local979 - local991 & 0x10000);
                                raster[offset] = local979 - local971 | local971 - (local971 >>> 8);
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
                            local484 = this.pixels[(u >> 16) + local273];
                            local361 = local484 > 0 ? this.palette[local484] : 0;
                            local364 = local361 + colour;
                            local386 = (local361 & 0xFF00FF) + (colour & 0xFF00FF);
                            local394 = (local386 & 0x1000100) + (local364 - local386 & 0x10000);
                            local361 = local364 - local394 | local394 - (local394 >>> 8);
                            @Pc(1741) int local1741 = raster[offset];
                            local364 = local361 + local1741;
                            local386 = (local361 & 0xFF00FF) + (local1741 & 0xFF00FF);
                            @Pc(1763) int local1763 = (local386 & 0x1000100) + (local364 - local386 & 0x10000);
                            raster[offset] = local364 - local1763 | local1763 - (local1763 >>> 8);
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
                local589 = (local273 | local276) >>> 8;
                local353 = u;
                for (local361 = -height; local361 < 0; local361++) {
                    local364 = (v >> 16) * super.anInt9302;
                    for (local386 = -width; local386 < 0; local386++) {
                        if ((float) z < depth[offset]) {
                            local1318 = this.pixels[(u >> 16) + local364];
                            if (local1318 != 0) {
                                local402 = this.palette[local1318 & 0xFF];
                                local273 = (local402 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local402 & 0xFF00) * local262 & 0xFF0000;
                                @Pc(1902) int local1902 = ((local273 | local276) >>> 8) + local589;
                                local410 = raster[offset];
                                local963 = local1902 + local410;
                                local971 = (local1902 & 0xFF00FF) + (local410 & 0xFF00FF);
                                @Pc(1928) int local1928 = (local971 & 0x1000100) + (local963 - local971 & 0x10000);
                                raster[offset] = local963 - local1928 | local1928 - (local1928 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local353;
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
                        raster[offset] = this.palette[this.pixels[(u >> 16) + local273] & 0xFF];
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
            for (local353 = -height; local353 < 0; local353++) {
                local361 = (v >> 16) * super.anInt9302;
                for (local364 = -width; local364 < 0; local364++) {
                    if ((float) z < depth[offset]) {
                        local386 = this.palette[this.pixels[(u >> 16) + local361] & 0xFF];
                        local394 = (local386 & 0xFF0000) * local262 & 0xFF000000;
                        local402 = (local386 & 0xFF00) * local265 & 0xFF0000;
                        local410 = (local386 & 0xFF) * local273 & 0xFF00;
                        raster[offset] = (local394 | local402 | local410) >>> 8;
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
                        local484 = this.pixels[(u >> 16) + local273];
                        local361 = local484 > 0 ? this.palette[local484] : 0;
                        local364 = local361 + colour;
                        local386 = (local361 & 0xFF00FF) + (colour & 0xFF00FF);
                        local394 = (local386 & 0x1000100) + (local364 - local386 & 0x10000);
                        raster[offset] = local364 - local394 | local394 - (local394 >>> 8);
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
            local589 = (local273 | local276) >>> 8;
            local353 = u;
            for (local361 = -height; local361 < 0; local361++) {
                local364 = (v >> 16) * super.anInt9302;
                for (local386 = -width; local386 < 0; local386++) {
                    if ((float) z < depth[offset]) {
                        local394 = this.palette[this.pixels[(u >> 16) + local364] & 0xFF];
                        local273 = (local394 & 0xFF00FF) * local262 & 0xFF00FF00;
                        local276 = (local394 & 0xFF00) * local262 & 0xFF0000;
                        raster[offset] = ((local273 | local276) >>> 8) + local589;
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local353;
                offset += dstStep;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!te", name = "b", descriptor = "(IIIIIIIII)V")
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
        @Pc(353) int local353;
        @Pc(361) int local361;
        @Pc(364) int local364;
        @Pc(386) int local386;
        @Pc(402) int local402;
        @Pc(410) int local410;
        @Pc(589) int local589;
        @Pc(484) byte local484;
        @Pc(394) int local394;
        if (mode != 0) {
            @Pc(1318) byte local1318;
            @Pc(963) int local963;
            @Pc(971) int local971;
            @Pc(979) int local979;
            @Pc(991) int local991;
            if (mode == 1) {
                if (op == 1) {
                    local262 = u;
                    for (local265 = -height; local265 < 0; local265++) {
                        local273 = (v >> 16) * super.anInt9302;
                        for (local276 = -width; local276 < 0; local276++) {
                            if ((float) z < depth[offset]) {
                                local484 = this.pixels[(u >> 16) + local273];
                                if (local484 != 0) {
                                    raster[offset] = this.palette[local484 & 0xFF];
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
                    @Pc(815) byte local815;
                    if (op == 0) {
                        local262 = u;
                        if ((colour & 0xFFFFFF) == 16777215) {
                            local265 = colour >>> 24;
                            local273 = 256 - local265;
                            for (local276 = -height; local276 < 0; local276++) {
                                local353 = (v >> 16) * super.anInt9302;
                                for (local361 = -width; local361 < 0; local361++) {
                                    if ((float) z < depth[offset]) {
                                        local815 = this.pixels[(u >> 16) + local353];
                                        if (local815 != 0) {
                                            local386 = this.palette[local815 & 0xFF];
                                            local394 = raster[offset];
                                            raster[offset] = ((local386 & 0xFF00FF) * local265 + (local394 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local386 & 0xFF00) * local265 + (local394 & 0xFF00) * local273 & 0xFF0000) >> 8;
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
                            local353 = colour >>> 24;
                            local361 = 256 - local353;
                            for (local364 = -height; local364 < 0; local364++) {
                                local386 = (v >> 16) * super.anInt9302;
                                for (local394 = -width; local394 < 0; local394++) {
                                    if ((float) z < depth[offset]) {
                                        @Pc(943) byte local943 = this.pixels[(u >> 16) + local386];
                                        if (local943 != 0) {
                                            local410 = this.palette[local943 & 0xFF];
                                            if (local353 == 255) {
                                                local963 = (local410 & 0xFF0000) * local265 & 0xFF000000;
                                                local971 = (local410 & 0xFF00) * local273 & 0xFF0000;
                                                local979 = (local410 & 0xFF) * local276 & 0xFF00;
                                                raster[offset] = (local963 | local971 | local979) >>> 8;
                                                depth[offset] = (float) z;
                                            } else {
                                                local963 = (local410 & 0xFF0000) * local265 & 0xFF000000;
                                                local971 = (local410 & 0xFF00) * local273 & 0xFF0000;
                                                local979 = (local410 & 0xFF) * local276 & 0xFF00;
                                                local410 = (local963 | local971 | local979) >>> 8;
                                                local991 = raster[offset];
                                                raster[offset] = ((local410 & 0xFF00FF) * local353 + (local991 & 0xFF00FF) * local361 & 0xFF00FF00) + ((local410 & 0xFF00) * local353 + (local991 & 0xFF00) * local361 & 0xFF0000) >> 8;
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
                            local353 = (v >> 16) * super.anInt9302;
                            for (local361 = -width; local361 < 0; local361++) {
                                if ((float) z < depth[offset]) {
                                    local815 = this.pixels[(u >> 16) + local353];
                                    local386 = local815 > 0 ? this.palette[local815] : 0;
                                    local394 = local386 + colour;
                                    local402 = (local386 & 0xFF00FF) + (colour & 0xFF00FF);
                                    local410 = (local402 & 0x1000100) + (local394 - local402 & 0x10000);
                                    local410 = local394 - local410 | local410 - (local410 >>> 8);
                                    if (local386 == 0 && local265 != 255) {
                                        local386 = local410;
                                        local410 = raster[offset];
                                        local410 = ((local386 & 0xFF00FF) * local265 + (local410 & 0xFF00FF) * local273 & 0xFF00FF00) + ((local386 & 0xFF00) * local265 + (local410 & 0xFF00) * local273 & 0xFF0000) >> 8;
                                    }
                                    raster[offset] = local410;
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
                        local589 = (local273 | local276) >>> 8;
                        local353 = u;
                        for (local361 = -height; local361 < 0; local361++) {
                            local364 = (v >> 16) * super.anInt9302;
                            for (local386 = -width; local386 < 0; local386++) {
                                if ((float) z < depth[offset]) {
                                    local1318 = this.pixels[(u >> 16) + local364];
                                    if (local1318 != 0) {
                                        local402 = this.palette[local1318 & 0xFF];
                                        local273 = (local402 & 0xFF00FF) * local262 & 0xFF00FF00;
                                        local276 = (local402 & 0xFF00) * local262 & 0xFF0000;
                                        raster[offset] = ((local273 | local276) >>> 8) + local589;
                                        depth[offset] = (float) z;
                                    }
                                }
                                u += uStep;
                                offset++;
                            }
                            v += vStep;
                            u = local353;
                            offset += dstStep;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            } else if (mode != 2) {
                throw new IllegalArgumentException();
            } else if (op == 1) {
                local262 = u;
                for (local265 = -height; local265 < 0; local265++) {
                    local273 = (v >> 16) * super.anInt9302;
                    for (local276 = -width; local276 < 0; local276++) {
                        if ((float) z < depth[offset]) {
                            local484 = this.pixels[(u >> 16) + local273];
                            if (local484 != 0) {
                                local361 = this.palette[local484 & 0xFF];
                                local364 = raster[offset];
                                local386 = local361 + local364;
                                local394 = (local361 & 0xFF00FF) + (local364 & 0xFF00FF);
                                local364 = (local394 & 0x1000100) + (local386 - local394 & 0x10000);
                                raster[offset] = local386 - local364 | local364 - (local364 >>> 8);
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
                for (local353 = -height; local353 < 0; local353++) {
                    local361 = (v >> 16) * super.anInt9302;
                    for (local364 = -width; local364 < 0; local364++) {
                        if ((float) z < depth[offset]) {
                            @Pc(1552) byte local1552 = this.pixels[(u >> 16) + local361];
                            if (local1552 != 0) {
                                local394 = this.palette[local1552 & 0xFF];
                                local402 = (local394 & 0xFF0000) * local265 & 0xFF000000;
                                local410 = (local394 & 0xFF00) * local273 & 0xFF0000;
                                local963 = (local394 & 0xFF) * local276 & 0xFF00;
                                local394 = (local402 | local410 | local963) >>> 8;
                                local971 = raster[offset];
                                local979 = local394 + local971;
                                local991 = (local394 & 0xFF00FF) + (local971 & 0xFF00FF);
                                local971 = (local991 & 0x1000100) + (local979 - local991 & 0x10000);
                                raster[offset] = local979 - local971 | local971 - (local971 >>> 8);
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
                            local484 = this.pixels[(u >> 16) + local273];
                            local361 = local484 > 0 ? this.palette[local484] : 0;
                            local364 = local361 + colour;
                            local386 = (local361 & 0xFF00FF) + (colour & 0xFF00FF);
                            local394 = (local386 & 0x1000100) + (local364 - local386 & 0x10000);
                            local361 = local364 - local394 | local394 - (local394 >>> 8);
                            @Pc(1741) int local1741 = raster[offset];
                            local364 = local361 + local1741;
                            local386 = (local361 & 0xFF00FF) + (local1741 & 0xFF00FF);
                            @Pc(1763) int local1763 = (local386 & 0x1000100) + (local364 - local386 & 0x10000);
                            raster[offset] = local364 - local1763 | local1763 - (local1763 >>> 8);
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
                local589 = (local273 | local276) >>> 8;
                local353 = u;
                for (local361 = -height; local361 < 0; local361++) {
                    local364 = (v >> 16) * super.anInt9302;
                    for (local386 = -width; local386 < 0; local386++) {
                        if ((float) z < depth[offset]) {
                            local1318 = this.pixels[(u >> 16) + local364];
                            if (local1318 != 0) {
                                local402 = this.palette[local1318 & 0xFF];
                                local273 = (local402 & 0xFF00FF) * local262 & 0xFF00FF00;
                                local276 = (local402 & 0xFF00) * local262 & 0xFF0000;
                                @Pc(1902) int local1902 = ((local273 | local276) >>> 8) + local589;
                                local410 = raster[offset];
                                local963 = local1902 + local410;
                                local971 = (local1902 & 0xFF00FF) + (local410 & 0xFF00FF);
                                @Pc(1928) int local1928 = (local971 & 0x1000100) + (local963 - local971 & 0x10000);
                                raster[offset] = local963 - local1928 | local1928 - (local1928 >>> 8);
                                depth[offset] = (float) z;
                            }
                        }
                        u += uStep;
                        offset++;
                    }
                    v += vStep;
                    u = local353;
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
                        raster[offset] = this.palette[this.pixels[(u >> 16) + local273] & 0xFF];
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
            for (local353 = -height; local353 < 0; local353++) {
                local361 = (v >> 16) * super.anInt9302;
                for (local364 = -width; local364 < 0; local364++) {
                    if ((float) z < depth[offset]) {
                        local386 = this.palette[this.pixels[(u >> 16) + local361] & 0xFF];
                        local394 = (local386 & 0xFF0000) * local262 & 0xFF000000;
                        local402 = (local386 & 0xFF00) * local265 & 0xFF0000;
                        local410 = (local386 & 0xFF) * local273 & 0xFF00;
                        raster[offset] = (local394 | local402 | local410) >>> 8;
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
                        local484 = this.pixels[(u >> 16) + local273];
                        local361 = local484 > 0 ? this.palette[local484] : 0;
                        local364 = local361 + colour;
                        local386 = (local361 & 0xFF00FF) + (colour & 0xFF00FF);
                        local394 = (local386 & 0x1000100) + (local364 - local386 & 0x10000);
                        raster[offset] = local364 - local394 | local394 - (local394 >>> 8);
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
            local589 = (local273 | local276) >>> 8;
            local353 = u;
            for (local361 = -height; local361 < 0; local361++) {
                local364 = (v >> 16) * super.anInt9302;
                for (local386 = -width; local386 < 0; local386++) {
                    if ((float) z < depth[offset]) {
                        local394 = this.palette[this.pixels[(u >> 16) + local364] & 0xFF];
                        local273 = (local394 & 0xFF00FF) * local262 & 0xFF00FF00;
                        local276 = (local394 & 0xFF00) * local262 & 0xFF0000;
                        raster[offset] = ((local273 | local276) >>> 8) + local589;
                        depth[offset] = (float) z;
                    }
                    u += uStep;
                    offset++;
                }
                v += vStep;
                u = local353;
                offset += dstStep;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!te", name = "a", descriptor = "(IILclient!aa;II)V")
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
                @Pc(316) byte index = this.pixels[srcIndex++];
                if (index == 0) {
                    dstIndex++;
                } else {
                    raster[dstIndex++] = this.palette[index & 0xFF];
                }
            }
            srcIndex += skip + srcStep;
            dstIndex += skip + dstStep;
        }
    }

    @OriginalMember(owner = "client!te", name = "a", descriptor = "(IIIIIIII)V")
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
            @Pc(342) int local342;
            @Pc(350) int local350;
            @Pc(353) int local353;
            @Pc(368) int local368;
            @Pc(384) int local384;
            @Pc(392) int local392;
            @Pc(554) int local554;
            @Pc(454) byte local454;
            @Pc(376) int local376;
            if (mode != 0) {
                @Pc(1217) byte local1217;
                @Pc(888) int local888;
                @Pc(896) int local896;
                @Pc(904) int local904;
                @Pc(916) int local916;
                if (mode == 1) {
                    if (op == 1) {
                        local265 = u;
                        for (local268 = -height; local268 < 0; local268++) {
                            local276 = (v >> 16) * super.anInt9302;
                            for (local279 = -width; local279 < 0; local279++) {
                                local454 = this.pixels[(u >> 16) + local276];
                                if (local454 == 0) {
                                    offset++;
                                } else {
                                    raster[offset++] = this.palette[local454 & 0xFF];
                                }
                                u += uStep;
                            }
                            v += vStep;
                            u = local265;
                            offset += dstStep;
                        }
                    } else {
                        @Pc(750) byte local750;
                        if (op == 0) {
                            local265 = u;
                            if ((colour & 0xFFFFFF) == 16777215) {
                                local268 = colour >>> 24;
                                local276 = 256 - local268;
                                for (local279 = -height; local279 < 0; local279++) {
                                    local342 = (v >> 16) * super.anInt9302;
                                    for (local350 = -width; local350 < 0; local350++) {
                                        local750 = this.pixels[(u >> 16) + local342];
                                        if (local750 == 0) {
                                            offset++;
                                        } else {
                                            local368 = this.palette[local750 & 0xFF];
                                            local376 = raster[offset];
                                            raster[offset++] = ((local368 & 0xFF00FF) * local268 + (local376 & 0xFF00FF) * local276 & 0xFF00FF00) + ((local368 & 0xFF00) * local268 + (local376 & 0xFF00) * local276 & 0xFF0000) >> 8;
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
                                local342 = colour >>> 24;
                                local350 = 256 - local342;
                                for (local353 = -height; local353 < 0; local353++) {
                                    local368 = (v >> 16) * super.anInt9302;
                                    for (local376 = -width; local376 < 0; local376++) {
                                        @Pc(868) byte local868 = this.pixels[(u >> 16) + local368];
                                        if (local868 == 0) {
                                            offset++;
                                        } else {
                                            local392 = this.palette[local868 & 0xFF];
                                            if (local342 == 255) {
                                                local888 = (local392 & 0xFF0000) * local268 & 0xFF000000;
                                                local896 = (local392 & 0xFF00) * local276 & 0xFF0000;
                                                local904 = (local392 & 0xFF) * local279 & 0xFF00;
                                                raster[offset++] = (local888 | local896 | local904) >>> 8;
                                            } else {
                                                local888 = (local392 & 0xFF0000) * local268 & 0xFF000000;
                                                local896 = (local392 & 0xFF00) * local276 & 0xFF0000;
                                                local904 = (local392 & 0xFF) * local279 & 0xFF00;
                                                local392 = (local888 | local896 | local904) >>> 8;
                                                local916 = raster[offset];
                                                raster[offset++] = ((local392 & 0xFF00FF) * local342 + (local916 & 0xFF00FF) * local350 & 0xFF00FF00) + ((local392 & 0xFF00) * local342 + (local916 & 0xFF00) * local350 & 0xFF0000) >> 8;
                                            }
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
                                local342 = (v >> 16) * super.anInt9302;
                                for (local350 = -width; local350 < 0; local350++) {
                                    local750 = this.pixels[(u >> 16) + local342];
                                    local368 = local750 > 0 ? this.palette[local750] : 0;
                                    local376 = local368 + colour;
                                    local384 = (local368 & 0xFF00FF) + (colour & 0xFF00FF);
                                    local392 = (local384 & 0x1000100) + (local376 - local384 & 0x10000);
                                    local392 = local376 - local392 | local392 - (local392 >>> 8);
                                    if (local368 == 0 && local268 != 255) {
                                        local368 = local392;
                                        local392 = raster[offset];
                                        local392 = ((local368 & 0xFF00FF) * local268 + (local392 & 0xFF00FF) * local276 & 0xFF00FF00) + ((local368 & 0xFF00) * local268 + (local392 & 0xFF00) * local276 & 0xFF0000) >> 8;
                                    }
                                    raster[offset++] = local392;
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
                            local554 = (local276 | local279) >>> 8;
                            local342 = u;
                            for (local350 = -height; local350 < 0; local350++) {
                                local353 = (v >> 16) * super.anInt9302;
                                for (local368 = -width; local368 < 0; local368++) {
                                    local1217 = this.pixels[(u >> 16) + local353];
                                    if (local1217 == 0) {
                                        offset++;
                                    } else {
                                        local384 = this.palette[local1217 & 0xFF];
                                        local276 = (local384 & 0xFF00FF) * local265 & 0xFF00FF00;
                                        local279 = (local384 & 0xFF00) * local265 & 0xFF0000;
                                        raster[offset++] = ((local276 | local279) >>> 8) + local554;
                                    }
                                    u += uStep;
                                }
                                v += vStep;
                                u = local342;
                                offset += dstStep;
                            }
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } else if (mode != 2) {
                    throw new IllegalArgumentException();
                } else if (op == 1) {
                    local265 = u;
                    for (local268 = -height; local268 < 0; local268++) {
                        local276 = (v >> 16) * super.anInt9302;
                        for (local279 = -width; local279 < 0; local279++) {
                            local454 = this.pixels[(u >> 16) + local276];
                            if (local454 == 0) {
                                offset++;
                            } else {
                                local350 = this.palette[local454 & 0xFF];
                                local353 = raster[offset];
                                local368 = local350 + local353;
                                local376 = (local350 & 0xFF00FF) + (local353 & 0xFF00FF);
                                local353 = (local376 & 0x1000100) + (local368 - local376 & 0x10000);
                                raster[offset++] = local368 - local353 | local353 - (local353 >>> 8);
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
                    for (local342 = -height; local342 < 0; local342++) {
                        local350 = (v >> 16) * super.anInt9302;
                        for (local353 = -width; local353 < 0; local353++) {
                            @Pc(1431) byte local1431 = this.pixels[(u >> 16) + local350];
                            if (local1431 == 0) {
                                offset++;
                            } else {
                                local376 = this.palette[local1431 & 0xFF];
                                local384 = (local376 & 0xFF0000) * local268 & 0xFF000000;
                                local392 = (local376 & 0xFF00) * local276 & 0xFF0000;
                                local888 = (local376 & 0xFF) * local279 & 0xFF00;
                                local376 = (local384 | local392 | local888) >>> 8;
                                local896 = raster[offset];
                                local904 = local376 + local896;
                                local916 = (local376 & 0xFF00FF) + (local896 & 0xFF00FF);
                                local896 = (local916 & 0x1000100) + (local904 - local916 & 0x10000);
                                raster[offset++] = local904 - local896 | local896 - (local896 >>> 8);
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
                            local454 = this.pixels[(u >> 16) + local276];
                            local350 = local454 > 0 ? this.palette[local454] : 0;
                            local353 = local350 + colour;
                            local368 = (local350 & 0xFF00FF) + (colour & 0xFF00FF);
                            local376 = (local368 & 0x1000100) + (local353 - local368 & 0x10000);
                            local350 = local353 - local376 | local376 - (local376 >>> 8);
                            @Pc(1610) int local1610 = raster[offset];
                            local353 = local350 + local1610;
                            local368 = (local350 & 0xFF00FF) + (local1610 & 0xFF00FF);
                            @Pc(1632) int local1632 = (local368 & 0x1000100) + (local353 - local368 & 0x10000);
                            raster[offset++] = local353 - local1632 | local1632 - (local1632 >>> 8);
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
                    local554 = (local276 | local279) >>> 8;
                    local342 = u;
                    for (local350 = -height; local350 < 0; local350++) {
                        local353 = (v >> 16) * super.anInt9302;
                        for (local368 = -width; local368 < 0; local368++) {
                            local1217 = this.pixels[(u >> 16) + local353];
                            if (local1217 == 0) {
                                offset++;
                            } else {
                                local384 = this.palette[local1217 & 0xFF];
                                local276 = (local384 & 0xFF00FF) * local265 & 0xFF00FF00;
                                local279 = (local384 & 0xFF00) * local265 & 0xFF0000;
                                @Pc(1759) int local1759 = ((local276 | local279) >>> 8) + local554;
                                local392 = raster[offset];
                                local888 = local1759 + local392;
                                local896 = (local1759 & 0xFF00FF) + (local392 & 0xFF00FF);
                                @Pc(1785) int local1785 = (local896 & 0x1000100) + (local888 - local896 & 0x10000);
                                raster[offset++] = local888 - local1785 | local1785 - (local1785 >>> 8);
                            }
                            u += uStep;
                        }
                        v += vStep;
                        u = local342;
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
                        raster[offset++] = this.palette[this.pixels[(u >> 16) + local276] & 0xFF];
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
                for (local342 = -height; local342 < 0; local342++) {
                    local350 = (v >> 16) * super.anInt9302;
                    for (local353 = -width; local353 < 0; local353++) {
                        local368 = this.palette[this.pixels[(u >> 16) + local350] & 0xFF];
                        local376 = (local368 & 0xFF0000) * local265 & 0xFF000000;
                        local384 = (local368 & 0xFF00) * local268 & 0xFF0000;
                        local392 = (local368 & 0xFF) * local276 & 0xFF00;
                        raster[offset++] = (local376 | local384 | local392) >>> 8;
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
                        local454 = this.pixels[(u >> 16) + local276];
                        local350 = local454 > 0 ? this.palette[local454] : 0;
                        local353 = local350 + colour;
                        local368 = (local350 & 0xFF00FF) + (colour & 0xFF00FF);
                        local376 = (local368 & 0x1000100) + (local353 - local368 & 0x10000);
                        raster[offset++] = local353 - local376 | local376 - (local376 >>> 8);
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
                local554 = (local276 | local279) >>> 8;
                local342 = u;
                for (local350 = -height; local350 < 0; local350++) {
                    local353 = (v >> 16) * super.anInt9302;
                    for (local368 = -width; local368 < 0; local368++) {
                        local376 = this.palette[this.pixels[(u >> 16) + local353] & 0xFF];
                        local276 = (local376 & 0xFF00FF) * local265 & 0xFF00FF00;
                        local279 = (local376 & 0xFF00) * local265 & 0xFF0000;
                        raster[offset++] = ((local276 | local279) >>> 8) + local554;
                        u += uStep;
                    }
                    v += vStep;
                    u = local342;
                    offset += dstStep;
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
