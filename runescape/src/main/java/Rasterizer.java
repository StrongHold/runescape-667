import com.jagex.math.ColourUtils;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!lb")
public final class Rasterizer {

    @OriginalMember(owner = "client!lb", name = "l", descriptor = "I")
    public int minY;

    @OriginalMember(owner = "client!lb", name = "x", descriptor = "I")
    public int minX;

    @OriginalMember(owner = "client!lb", name = "q", descriptor = "I")
    public int width;

    @OriginalMember(owner = "client!lb", name = "f", descriptor = "I")
    public int height;

    @OriginalMember(owner = "client!lb", name = "B", descriptor = "I")
    public int fogColour;

    @OriginalMember(owner = "client!lb", name = "m", descriptor = "Z")
    public boolean halfBlend = false;

    @OriginalMember(owner = "client!lb", name = "u", descriptor = "Z")
    public boolean clamp = false;

    @OriginalMember(owner = "client!lb", name = "H", descriptor = "Z")
    public final boolean depthDisabled = false;

    @OriginalMember(owner = "client!lb", name = "p", descriptor = "Z")
    public boolean fastScanline = true;

    @OriginalMember(owner = "client!lb", name = "o", descriptor = "I")
    public int alpha = 0;

    @OriginalMember(owner = "client!lb", name = "r", descriptor = "[I")
    public final int[] lineOffsets = new int[4096];

    @OriginalMember(owner = "client!lb", name = "g", descriptor = "Z")
    public boolean wireframe = false;

    @OriginalMember(owner = "client!lb", name = "j", descriptor = "I")
    public int textureSize3 = 0;

    @OriginalMember(owner = "client!lb", name = "e", descriptor = "I")
    public int textureSize2 = 0;

    @OriginalMember(owner = "client!lb", name = "G", descriptor = "I")
    public int textureMask3 = 0;

    @OriginalMember(owner = "client!lb", name = "h", descriptor = "I")
    public int textureSize = 0;

    @OriginalMember(owner = "client!lb", name = "w", descriptor = "I")
    public int textureBlendMode = 0;

    @OriginalMember(owner = "client!lb", name = "k", descriptor = "[I")
    public int[] texels3 = null;

    @OriginalMember(owner = "client!lb", name = "i", descriptor = "F")
    public float textureScale2 = 0.0F;

    @OriginalMember(owner = "client!lb", name = "C", descriptor = "Z")
    public boolean textureRepeats = true;

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "I")
    public final int textureId = -1;

    @OriginalMember(owner = "client!lb", name = "v", descriptor = "[I")
    public int[] texels = null;

    @OriginalMember(owner = "client!lb", name = "d", descriptor = "F")
    public float textureScale3 = 0.0F;

    @OriginalMember(owner = "client!lb", name = "n", descriptor = "F")
    public float textureScale = 0.0F;

    @OriginalMember(owner = "client!lb", name = "z", descriptor = "[I")
    public int[] texels2 = null;

    @OriginalMember(owner = "client!lb", name = "F", descriptor = "I")
    public int textureMask = 0;

    @OriginalMember(owner = "client!lb", name = "E", descriptor = "I")
    public final int textureId3 = -1;

    @OriginalMember(owner = "client!lb", name = "c", descriptor = "I")
    public final int textureId2 = -1;

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "I")
    public int textureMask2 = 0;

    @OriginalMember(owner = "client!lb", name = "s", descriptor = "Lclient!iaa;")
    public final JavaToolkit toolkit;

    @OriginalMember(owner = "client!lb", name = "D", descriptor = "Lclient!wf;")
    public final JavaThreadResource threadResource;

    @OriginalMember(owner = "client!lb", name = "A", descriptor = "I")
    public final int surfaceWidth;

    @OriginalMember(owner = "client!lb", name = "t", descriptor = "[I")
    public final int[] raster;

    @OriginalMember(owner = "client!lb", name = "y", descriptor = "[F")
    public final float[] depthBuffer;

    @OriginalMember(owner = "client!lb", name = "<init>", descriptor = "(Lclient!iaa;Lclient!wf;)V")
    public Rasterizer(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) JavaThreadResource threadResource) {
        this.toolkit = toolkit;
        this.threadResource = threadResource;
        this.surfaceWidth = this.toolkit.surfaceWidth;
        this.raster = this.toolkit.surfaceRaster;
        this.depthBuffer = this.toolkit.depthBuffer;
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "()I")
    public int offsetY() {
        return this.lineOffsets[0] / this.surfaceWidth;
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "(FFFFFFFFFIII)V")
    public void renderTriangleRgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) int colourA, @OriginalArg(10) int colourB, @OriginalArg(11) int colourC) {
        if (this.wireframe) {
            this.toolkit.line((int) yB, (int) yA, (int) xB, colourA | 0xFF000000, (int) xA);
            this.toolkit.line((int) yC, (int) yB, (int) xC, colourA | 0xFF000000, (int) xB);
            this.toolkit.line((int) yA, (int) yC, (int) xA, colourA | 0xFF000000, (int) xC);
            return;
        }
        @Pc(52) float local52 = xB - xA;
        @Pc(56) float local56 = yB - yA;
        @Pc(60) float local60 = xC - xA;
        @Pc(64) float local64 = yC - yA;
        @Pc(68) float local68 = zB - zA;
        @Pc(72) float local72 = zC - zA;
        @Pc(81) float local81 = (float) ((colourB & 0xFF0000) - (colourA & 0xFF0000));
        @Pc(90) float local90 = (float) ((colourC & 0xFF0000) - (colourA & 0xFF0000));
        @Pc(99) float local99 = (float) ((colourB & 0xFF00) - (colourA & 0xFF00));
        @Pc(108) float local108 = (float) ((colourC & 0xFF00) - (colourA & 0xFF00));
        @Pc(117) float local117 = (float) ((colourB & 0xFF) - (colourA & 0xFF));
        @Pc(126) float local126 = (float) ((colourC & 0xFF) - (colourA & 0xFF));
        @Pc(138) float local138;
        if (yC == yB) {
            local138 = 0.0F;
        } else {
            local138 = (xC - xB) / (yC - yB);
        }
        @Pc(149) float local149;
        if (yB == yA) {
            local149 = 0.0F;
        } else {
            local149 = local52 / local56;
        }
        @Pc(160) float local160;
        if (yC == yA) {
            local160 = 0.0F;
        } else {
            local160 = local60 / local64;
        }
        @Pc(171) float local171 = local52 * local64 - local60 * local56;
        if (local171 == 0.0F) {
            return;
        }
        @Pc(186) float local186 = (local68 * local64 - local72 * local56) / local171;
        @Pc(196) float local196 = (local72 * local52 - local68 * local60) / local171;
        @Pc(206) float local206 = (local81 * local64 - local90 * local56) / local171;
        @Pc(216) float local216 = (local90 * local52 - local81 * local60) / local171;
        @Pc(226) float local226 = (local99 * local64 - local108 * local56) / local171;
        @Pc(236) float local236 = (local108 * local52 - local99 * local60) / local171;
        @Pc(246) float local246 = (local117 * local64 - local126 * local56) / local171;
        @Pc(256) float local256 = (local126 * local52 - local117 * local60) / local171;
        @Pc(310) float local310;
        @Pc(321) float local321;
        @Pc(332) float local332;
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                zA = zA - local186 * xA + local186;
                local310 = (float) (colourA & 0xFF0000) - local206 * xA + local206;
                local321 = (float) (colourA & 0xFF00) - local226 * xA + local226;
                local332 = (float) (colourA & 0xFF) - local246 * xA + local246;
                if (yB < yC) {
                    xC = xA;
                    if (yA < 0.0F) {
                        xC = xA - local160 * yA;
                        xA -= local149 * yA;
                        zA -= local196 * yA;
                        local310 -= local216 * yA;
                        local321 -= local236 * yA;
                        local332 -= local256 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local138 * yB;
                        yB = 0.0F;
                    }
                    if ((yA == yB || !(local160 < local149)) && (yA != yB || !(local160 > local138))) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xC, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xB += local138;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xB, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xB += local138;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    if (yA < 0.0F) {
                        xB = xA - local160 * yA;
                        xA -= local149 * yA;
                        zA -= local196 * yA;
                        local310 -= local216 * yA;
                        local321 -= local236 * yA;
                        local332 -= local256 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local138 * yC;
                        yC = 0.0F;
                    }
                    if (yA != yC && local160 < local149 || yA == yC && local138 > local149) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xA, zA, local186, local310, local206, local321, local226, local332, local246);
                            xB += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local138;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xB, zA, local186, local310, local206, local321, local226, local332, local246);
                            xB += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local138;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                zB = zB - local186 * xB + local186;
                local310 = (float) (colourB & 0xFF0000) - local206 * xB + local206;
                local321 = (float) (colourB & 0xFF00) - local226 * xB + local226;
                local332 = (float) (colourB & 0xFF) - local246 * xB + local246;
                if (yC < yA) {
                    xA = xB;
                    if (yB < 0.0F) {
                        xA = xB - local149 * yB;
                        xB -= local138 * yB;
                        zB -= local196 * yB;
                        local310 -= local216 * yB;
                        local321 -= local236 * yB;
                        local332 -= local256 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local160 * yC;
                        yC = 0.0F;
                    }
                    if ((yB == yC || !(local149 < local138)) && (yB != yC || !(local149 > local160))) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xA, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xC += local160;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xC, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xC += local160;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    if (yB < 0.0F) {
                        xC = xB - local149 * yB;
                        xB -= local138 * yB;
                        zB -= local196 * yB;
                        local310 -= local216 * yB;
                        local321 -= local236 * yB;
                        local332 -= local256 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local160 * yA;
                        yA = 0.0F;
                    }
                    if (local149 < local138) {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xB, zB, local186, local310, local206, local321, local226, local332, local246);
                            xC += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local160;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xC, zB, local186, local310, local206, local321, local226, local332, local246);
                            xC += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local160;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            zC = zC - local186 * xC + local186;
            local310 = (float) (colourC & 0xFF0000) - local206 * xC + local206;
            local321 = (float) (colourC & 0xFF00) - local226 * xC + local226;
            local332 = (float) (colourC & 0xFF) - local246 * xC + local246;
            if (yA < yB) {
                xB = xC;
                if (yC < 0.0F) {
                    xB = xC - local138 * yC;
                    xC -= local160 * yC;
                    zC -= local196 * yC;
                    local310 -= local216 * yC;
                    local321 -= local236 * yC;
                    local332 -= local256 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local149 * yA;
                    yA = 0.0F;
                }
                if (local138 < local160) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xA, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xA += local149;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xB, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xA += local149;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                if (yC < 0.0F) {
                    xA = xC - local138 * yC;
                    xC -= local160 * yC;
                    zC -= local196 * yC;
                    local310 -= local216 * yC;
                    local321 -= local236 * yC;
                    local332 -= local256 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local149 * yB;
                    yB = 0.0F;
                }
                if (local138 < local160) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xC, zC, local186, local310, local206, local321, local226, local332, local246);
                        xA += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local149;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xA, zC, local186, local310, local206, local321, local226, local332, local246);
                        xA += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local149;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "(Z)V")
    public void setWireframe(@OriginalArg(0) boolean wireframe) {
        this.wireframe = wireframe;
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "(FFFFFFFFFIII)V")
    public void renderTriangleArgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) int colourA, @OriginalArg(10) int colourB, @OriginalArg(11) int colourC) {
        if (this.wireframe) {
            this.toolkit.line((int) yB, (int) yA, (int) xB, colourA | 0xFF000000, (int) xA);
            this.toolkit.line((int) yC, (int) yB, (int) xC, colourA | 0xFF000000, (int) xB);
            this.toolkit.line((int) yA, (int) yC, (int) xA, colourA | 0xFF000000, (int) xC);
            return;
        }
        @Pc(52) float local52 = xB - xA;
        @Pc(56) float local56 = yB - yA;
        @Pc(60) float local60 = xC - xA;
        @Pc(64) float local64 = yC - yA;
        @Pc(68) float local68 = zB - zA;
        @Pc(72) float local72 = zC - zA;
        @Pc(81) float local81 = (float) ((colourB & 0xFF0000) - (colourA & 0xFF0000));
        @Pc(90) float local90 = (float) ((colourC & 0xFF0000) - (colourA & 0xFF0000));
        @Pc(99) float local99 = (float) ((colourB & 0xFF00) - (colourA & 0xFF00));
        @Pc(108) float local108 = (float) ((colourC & 0xFF00) - (colourA & 0xFF00));
        @Pc(117) float local117 = (float) ((colourB & 0xFF) - (colourA & 0xFF));
        @Pc(126) float local126 = (float) ((colourC & 0xFF) - (colourA & 0xFF));
        @Pc(138) float local138;
        if (yC == yB) {
            local138 = 0.0F;
        } else {
            local138 = (xC - xB) / (yC - yB);
        }
        @Pc(149) float local149;
        if (yB == yA) {
            local149 = 0.0F;
        } else {
            local149 = local52 / local56;
        }
        @Pc(160) float local160;
        if (yC == yA) {
            local160 = 0.0F;
        } else {
            local160 = local60 / local64;
        }
        @Pc(171) float local171 = local52 * local64 - local60 * local56;
        if (local171 == 0.0F) {
            return;
        }
        @Pc(186) float local186 = (local68 * local64 - local72 * local56) / local171;
        @Pc(196) float local196 = (local72 * local52 - local68 * local60) / local171;
        @Pc(206) float local206 = (local81 * local64 - local90 * local56) / local171;
        @Pc(216) float local216 = (local90 * local52 - local81 * local60) / local171;
        @Pc(226) float local226 = (local99 * local64 - local108 * local56) / local171;
        @Pc(236) float local236 = (local108 * local52 - local99 * local60) / local171;
        @Pc(246) float local246 = (local117 * local64 - local126 * local56) / local171;
        @Pc(256) float local256 = (local126 * local52 - local117 * local60) / local171;
        @Pc(310) float local310;
        @Pc(321) float local321;
        @Pc(332) float local332;
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                zA = zA - local186 * xA + local186;
                local310 = (float) (colourA & 0xFF0000) - local206 * xA + local206;
                local321 = (float) (colourA & 0xFF00) - local226 * xA + local226;
                local332 = (float) (colourA & 0xFF) - local246 * xA + local246;
                if (yB < yC) {
                    xC = xA;
                    if (yA < 0.0F) {
                        xC = xA - local160 * yA;
                        xA -= local149 * yA;
                        zA -= local196 * yA;
                        local310 -= local216 * yA;
                        local321 -= local236 * yA;
                        local332 -= local256 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local138 * yB;
                        yB = 0.0F;
                    }
                    if ((yA == yB || !(local160 < local149)) && (yA != yB || !(local160 > local138))) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xC, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xB += local138;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xB, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local160;
                            xB += local138;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    if (yA < 0.0F) {
                        xB = xA - local160 * yA;
                        xA -= local149 * yA;
                        zA -= local196 * yA;
                        local310 -= local216 * yA;
                        local321 -= local236 * yA;
                        local332 -= local256 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local138 * yC;
                        yC = 0.0F;
                    }
                    if (yA != yC && local160 < local149 || yA == yC && local138 > local149) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xA, zA, local186, local310, local206, local321, local226, local332, local246);
                            xB += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local138;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xB, zA, local186, local310, local206, local321, local226, local332, local246);
                            xB += local160;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, zA, local186, local310, local206, local321, local226, local332, local246);
                            xC += local138;
                            xA += local149;
                            zA += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                zB = zB - local186 * xB + local186;
                local310 = (float) (colourB & 0xFF0000) - local206 * xB + local206;
                local321 = (float) (colourB & 0xFF00) - local226 * xB + local226;
                local332 = (float) (colourB & 0xFF) - local246 * xB + local246;
                if (yC < yA) {
                    xA = xB;
                    if (yB < 0.0F) {
                        xA = xB - local149 * yB;
                        xB -= local138 * yB;
                        zB -= local196 * yB;
                        local310 -= local216 * yB;
                        local321 -= local236 * yB;
                        local332 -= local256 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local160 * yC;
                        yC = 0.0F;
                    }
                    if ((yB == yC || !(local149 < local138)) && (yB != yC || !(local149 > local160))) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xA, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xC += local160;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xC, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local149;
                            xC += local160;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    if (yB < 0.0F) {
                        xC = xB - local149 * yB;
                        xB -= local138 * yB;
                        zB -= local196 * yB;
                        local310 -= local216 * yB;
                        local321 -= local236 * yB;
                        local332 -= local256 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local160 * yA;
                        yA = 0.0F;
                    }
                    if (local149 < local138) {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xB, zB, local186, local310, local206, local321, local226, local332, local246);
                            xC += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local160;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xC, zB, local186, local310, local206, local321, local226, local332, local246);
                            xC += local149;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, zB, local186, local310, local206, local321, local226, local332, local246);
                            xA += local160;
                            xB += local138;
                            zB += local196;
                            local310 += local216;
                            local321 += local236;
                            local332 += local256;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            zC = zC - local186 * xC + local186;
            local310 = (float) (colourC & 0xFF0000) - local206 * xC + local206;
            local321 = (float) (colourC & 0xFF00) - local226 * xC + local226;
            local332 = (float) (colourC & 0xFF) - local246 * xC + local246;
            if (yA < yB) {
                xB = xC;
                if (yC < 0.0F) {
                    xB = xC - local138 * yC;
                    xC -= local160 * yC;
                    zC -= local196 * yC;
                    local310 -= local216 * yC;
                    local321 -= local236 * yC;
                    local332 -= local256 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local149 * yA;
                    yA = 0.0F;
                }
                if (local138 < local160) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xA, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xA += local149;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xB, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local138;
                        xA += local149;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                if (yC < 0.0F) {
                    xA = xC - local138 * yC;
                    xC -= local160 * yC;
                    zC -= local196 * yC;
                    local310 -= local216 * yC;
                    local321 -= local236 * yC;
                    local332 -= local256 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local149 * yB;
                    yB = 0.0F;
                }
                if (local138 < local160) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xC, zC, local186, local310, local206, local321, local226, local332, local246);
                        xA += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local149;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xA, zC, local186, local310, local206, local321, local226, local332, local246);
                        xA += local138;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawGouraudSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, zC, local186, local310, local206, local321, local226, local332, local246);
                        xB += local149;
                        xC += local160;
                        zC += local196;
                        local310 += local216;
                        local321 += local236;
                        local332 += local256;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "(FFFFFFFFFI)V")
    public void renderFlatTriangleArgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) int colour) {
        if (this.wireframe) {
            this.toolkit.line((int) yB, (int) yA, (int) xB, colour, (int) xA);
            this.toolkit.line((int) yC, (int) yB, (int) xC, colour, (int) xB);
            this.toolkit.line((int) yA, (int) yC, (int) xA, colour, (int) xC);
            return;
        }
        @Pc(46) float local46 = xB - xA;
        @Pc(50) float local50 = yB - yA;
        @Pc(54) float local54 = xC - xA;
        @Pc(58) float local58 = yC - yA;
        @Pc(62) float local62 = zB - zA;
        @Pc(66) float local66 = zC - zA;
        @Pc(68) float local68 = 0.0F;
        if (yB != yA) {
            local68 = (xB - xA) / (yB - yA);
        }
        @Pc(82) float local82 = 0.0F;
        if (yC != yB) {
            local82 = (xC - xB) / (yC - yB);
        }
        @Pc(96) float local96 = 0.0F;
        if (yC != yA) {
            local96 = (xA - xC) / (yA - yC);
        }
        @Pc(116) float local116 = local46 * local58 - local54 * local50;
        if (local116 == 0.0F) {
            return;
        }
        @Pc(131) float local131 = (local62 * local58 - local66 * local50) / local116;
        @Pc(141) float local141 = (local66 * local46 - local62 * local54) / local116;
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                zA = zA - local131 * xA + local131;
                if (yB < yC) {
                    xC = xA;
                    if (yA < 0.0F) {
                        xC = xA - local96 * yA;
                        xA -= local68 * yA;
                        zA -= local141 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local82 * yB;
                        yB = 0.0F;
                    }
                    if (yA != yB && local96 < local68 || yA == yB && local96 > local82) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xC, (int) xA, zA, local131);
                            xC += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xC, (int) xB, zA, local131);
                            xC += local96;
                            xB += local82;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xA, (int) xC, zA, local131);
                            xC += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xB, (int) xC, zA, local131);
                            xC += local96;
                            xB += local82;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    if (yA < 0.0F) {
                        xB = xA - local96 * yA;
                        xA -= local68 * yA;
                        zA -= local141 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local82 * yC;
                        yC = 0.0F;
                    }
                    if ((yA == yC || !(local96 < local68)) && (yA != yC || !(local82 > local68))) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xA, (int) xB, zA, local131);
                            xB += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xA, (int) xC, zA, local131);
                            xC += local82;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xB, (int) xA, zA, local131);
                            xB += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xC, (int) xA, zA, local131);
                            xC += local82;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                zB = zB - local131 * xB + local131;
                if (yC < yA) {
                    xA = xB;
                    if (yB < 0.0F) {
                        xA = xB - local68 * yB;
                        xB -= local82 * yB;
                        zB -= local141 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local96 * yC;
                        yC = 0.0F;
                    }
                    if (yB != yC && local68 < local82 || yB == yC && local68 > local96) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xA, (int) xB, zB, local131);
                            xA += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xA, (int) xC, zB, local131);
                            xA += local68;
                            xC += local96;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xB, (int) xA, zB, local131);
                            xA += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xC, (int) xA, zB, local131);
                            xA += local68;
                            xC += local96;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    if (yB < 0.0F) {
                        xC = xB - local68 * yB;
                        xB -= local82 * yB;
                        zB -= local141 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local96 * yA;
                        yA = 0.0F;
                    }
                    if (local68 < local82) {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xC, (int) xB, zB, local131);
                            xC += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xA, (int) xB, zB, local131);
                            xA += local96;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xB, (int) xC, zB, local131);
                            xC += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xB, (int) xA, zB, local131);
                            xA += local96;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            zC = zC - local131 * xC + local131;
            if (yA < yB) {
                xB = xC;
                if (yC < 0.0F) {
                    xB = xC - local82 * yC;
                    xC -= local96 * yC;
                    zC -= local141 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local68 * yA;
                    yA = 0.0F;
                }
                if (local82 < local96) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xB, (int) xC, zC, local131);
                        xB += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xB, (int) xA, zC, local131);
                        xB += local82;
                        xA += local68;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xC, (int) xB, zC, local131);
                        xB += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xA, (int) xB, zC, local131);
                        xB += local82;
                        xA += local68;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                if (yC < 0.0F) {
                    xA = xC - local82 * yC;
                    xC -= local96 * yC;
                    zC -= local141 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local68 * yB;
                    yB = 0.0F;
                }
                if (local82 < local96) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xA, (int) xC, zC, local131);
                        xA += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xB, (int) xC, zC, local131);
                        xB += local68;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xC, (int) xA, zC, local131);
                        xA += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanArgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xC, (int) xB, zC, local131);
                        xB += local68;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "([I[IIIIFFFFFFFFFFFFFFFF)V")
    public void drawTexturedSpanRgb(@OriginalArg(0) int[] dst, @OriginalArg(1) int[] texels, @OriginalArg(2) int rowOffset, @OriginalArg(3) int startX, @OriginalArg(4) int endX, @OriginalArg(5) float invZLeft, @OriginalArg(6) float invZRight, @OriginalArg(7) float uLeft, @OriginalArg(8) float uRight, @OriginalArg(9) float vLeft, @OriginalArg(10) float vRight, @OriginalArg(11) float fogLeft, @OriginalArg(12) float fogRight, @OriginalArg(13) float alphaLeft, @OriginalArg(14) float alphaRight, @OriginalArg(15) float redLeft, @OriginalArg(16) float redRight, @OriginalArg(17) float greenLeft, @OriginalArg(18) float greenRight, @OriginalArg(19) float blueLeft, @OriginalArg(20) float blueRight) {
        @Pc(3) int local3 = endX - startX;
        @Pc(8) float local8 = 1.0F / (float) local3;
        @Pc(14) float local14 = (invZRight - invZLeft) * local8;
        @Pc(20) float local20 = (uRight - uLeft) * local8;
        @Pc(26) float local26 = (vRight - vLeft) * local8;
        @Pc(32) float local32 = (fogRight - fogLeft) * local8;
        @Pc(38) float local38 = (alphaRight - alphaLeft) * local8;
        @Pc(44) float local44 = (redRight - redLeft) * local8;
        @Pc(50) float local50 = (greenRight - greenLeft) * local8;
        @Pc(56) float local56 = (blueRight - blueLeft) * local8;
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                invZLeft -= local14 * (float) startX;
                uLeft -= local20 * (float) startX;
                vLeft -= local26 * (float) startX;
                fogLeft -= local32 * (float) startX;
                alphaLeft -= local38 * (float) startX;
                redLeft -= local44 * (float) startX;
                greenLeft -= local50 * (float) startX;
                blueLeft -= local56 * (float) startX;
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        local3 = endX - startX;
        @Pc(138) int local138 = rowOffset + startX;
        while (local3-- > 0) {
            @Pc(143) float local143 = 1.0F / invZLeft;
            if (local143 < this.depthBuffer[local138]) {
                @Pc(159) int local159 = (int) (uLeft * local143 * (float) this.textureSize);
                if (this.textureRepeats) {
                    local159 &= this.textureMask;
                } else if (local159 < 0) {
                    local159 = 0;
                } else if (local159 > this.textureMask) {
                    local159 = this.textureMask;
                }
                @Pc(189) int local189 = (int) (vLeft * local143 * (float) this.textureSize);
                if (this.textureRepeats) {
                    local189 &= this.textureMask;
                } else if (local189 < 0) {
                    local189 = 0;
                } else if (local189 > this.textureMask) {
                    local189 = this.textureMask;
                }
                @Pc(220) int local220 = this.texels[local189 * this.textureSize + local159];
                @Pc(232) int local232;
                if (this.textureBlendMode == 2) {
                    local232 = local220 >> 24 & 0xFF;
                } else if (this.textureBlendMode == 1) {
                    local232 = local220 == 0 ? 0 : 255;
                } else {
                    local232 = (int) alphaLeft;
                }
                if (local232 != 0) {
                    @Pc(290) int local290;
                    @Pc(299) int local299;
                    @Pc(321) int local321;
                    if (local232 == 255) {
                        local290 = ((int) (redLeft * (float) (local220 >> 16 & 0xFF)) & 0xFF00 | 0xFF0000) << 8 | (int) (greenLeft * (float) (local220 >> 8 & 0xFF)) & 0xFF00 | (int) (blueLeft * (float) (local220 & 0xFF)) >> 8;
                        if (fogLeft != 0.0F) {
                            local299 = (int) (255.0F - fogLeft);
                            local321 = ((this.fogColour & 0xFF00FF) * (int) fogLeft & 0xFF00FF00 | (this.fogColour & 0xFF00) * (int) fogLeft & 0xFF0000) >>> 8;
                            local290 = (((local290 & 0xFF00FF) * local299 & 0xFF00FF00 | (local290 & 0xFF00) * local299 & 0xFF0000) >>> 8) + local321;
                        }
                        dst[local138] = local290;
                        this.depthBuffer[local138] = local143;
                    } else {
                        local290 = ((int) (redLeft * (float) (local220 >> 16 & 0xFF)) & 0xFF00 | 0xFF0000) << 8 | (int) (greenLeft * (float) (local220 >> 8 & 0xFF)) & 0xFF00 | (int) (blueLeft * (float) (local220 & 0xFF)) >> 8;
                        if (fogLeft != 0.0F) {
                            local299 = (int) (255.0F - fogLeft);
                            local321 = ((this.fogColour & 0xFF00FF) * (int) fogLeft & 0xFF00FF00 | (this.fogColour & 0xFF00) * (int) fogLeft & 0xFF0000) >>> 8;
                            local290 = (((local290 & 0xFF00FF) * local299 & 0xFF00FF00 | (local290 & 0xFF00) * local299 & 0xFF0000) >>> 8) + local321;
                        }
                        local299 = dst[local138];
                        local321 = 255 - local232;
                        local290 = ((local299 & 0xFF00FF) * local321 + (local290 & 0xFF00FF) * local232 & 0xFF00FF00) + ((local299 & 0xFF00) * local321 + (local290 & 0xFF00) * local232 & 0xFF0000) >> 8;
                        dst[local138] = local290;
                        this.depthBuffer[local138] = local143;
                    }
                }
            }
            local138++;
            invZLeft += local14;
            uLeft += local20;
            vLeft += local26;
            fogLeft += local32;
            alphaLeft += local38;
            redLeft += local44;
            greenLeft += local50;
            blueLeft += local56;
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "([I[FIIIIIFFFFFFFF)V")
    public void drawGouraudSpanArgb(@OriginalArg(0) int[] dst, @OriginalArg(1) float[] depths, @OriginalArg(2) int rowOffset, @OriginalArg(5) int startX, @OriginalArg(6) int endX, @OriginalArg(7) float z, @OriginalArg(8) float zStep, @OriginalArg(9) float red, @OriginalArg(10) float redStep, @OriginalArg(11) float green, @OriginalArg(12) float greenStep, @OriginalArg(13) float blue, @OriginalArg(14) float blueStep) {
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        @Pc(163) int local163;
        @Pc(168) int local168;
        @Pc(223) int local223;
        @Pc(496) int local496;
        @Pc(500) int local500;
        @Pc(508) int local508;
        @Pc(54) int local54;
        @Pc(88) int local88;
        @Pc(103) int local103;
        @Pc(58) float local58;
        @Pc(62) float local62;
        @Pc(66) float local66;
        if (this.depthDisabled) {
            rowOffset += startX;
            red += redStep * (float) startX;
            green += greenStep * (float) startX;
            blue += blueStep * (float) startX;
            if (this.fastScanline) {
                local54 = endX - startX >> 2;
                local58 = redStep * 4.0F;
                local62 = greenStep * 4.0F;
                local66 = blueStep * 4.0F;
                if (this.alpha == 0) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            dst[rowOffset] = local88;
                            dst[local103++] = local88;
                            dst[local103++] = local88;
                            rowOffset = local103 + 1;
                            dst[local103] = local88;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            dst[rowOffset++] = local88;
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                } else if (this.halfBlend) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            local496 = dst[rowOffset];
                            local500 = local88 + local496;
                            local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[rowOffset] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                            @Pc(537) int local537 = local103++;
                            @Pc(543) int local543 = dst[local537];
                            @Pc(547) int local547 = local88 + local543;
                            @Pc(555) int local555 = (local88 & 0xFF00FF) + (local543 & 0xFF00FF);
                            @Pc(565) int local565 = (local555 & 0x1000100) + (local547 - local555 & 0x10000);
                            dst[local537] = local547 - local565 | 0xFF000000 | local565 - (local565 >>> 8);
                            @Pc(584) int local584 = local103++;
                            @Pc(590) int local590 = dst[local584];
                            @Pc(594) int local594 = local88 + local590;
                            @Pc(602) int local602 = (local88 & 0xFF00FF) + (local590 & 0xFF00FF);
                            @Pc(612) int local612 = (local602 & 0x1000100) + (local594 - local602 & 0x10000);
                            dst[local584] = local594 - local612 | 0xFF000000 | local612 - (local612 >>> 8);
                            rowOffset = local103 + 1;
                            @Pc(637) int local637 = dst[local103];
                            @Pc(641) int local641 = local88 + local637;
                            @Pc(649) int local649 = (local88 & 0xFF00FF) + (local637 & 0xFF00FF);
                            @Pc(659) int local659 = (local649 & 0x1000100) + (local641 - local649 & 0x10000);
                            dst[local103] = local641 - local659 | 0xFF000000 | local659 - (local659 >>> 8);
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            local168 = rowOffset++;
                            local496 = dst[local168];
                            local500 = local88 + local496;
                            local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[local168] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                            local54--;
                        } while (local54 > 0);
                    }
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                            local223 = dst[rowOffset];
                            local103 = rowOffset + 1;
                            dst[rowOffset] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local223 = dst[local103];
                            dst[local103++] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local223 = dst[local103];
                            dst[local103++] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local223 = dst[local103];
                            rowOffset = local103 + 1;
                            dst[local103] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        do {
                            local223 = dst[rowOffset];
                            dst[rowOffset++] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                }
            } else {
                local54 = endX - startX;
                if (this.alpha == 0) {
                    do {
                        dst[rowOffset++] = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else if (this.halfBlend) {
                    do {
                        local168 = rowOffset++;
                        local223 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local496 = dst[local168];
                        local500 = local223 + local496;
                        local508 = (local223 & 0xFF00FF) + (local496 & 0xFF00FF);
                        local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                        dst[local168] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    do {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        local223 = dst[rowOffset];
                        dst[rowOffset++] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        local54--;
                    } while (local54 > 0);
                }
            }
            return;
        }
        rowOffset += startX - 1;
        z += zStep * (float) startX;
        red += redStep * (float) startX;
        green += greenStep * (float) startX;
        blue += blueStep * (float) startX;
        @Pc(1082) float local1082;
        if (this.threadResource.zWrite) {
            if (this.fastScanline) {
                local54 = endX - startX >> 2;
                local58 = redStep * 4.0F;
                local62 = greenStep * 4.0F;
                local66 = blueStep * 4.0F;
                if (this.alpha == 0) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            if (z < depths[local103]) {
                                dst[local103] = local88;
                                depths[local103] = z;
                            }
                            local1082 = z + zStep;
                            local103++;
                            if (local1082 < depths[local103]) {
                                dst[local103] = local88;
                                depths[local103] = local1082;
                            }
                            local1082 += zStep;
                            local103++;
                            if (local1082 < depths[local103]) {
                                dst[local103] = local88;
                                depths[local103] = local1082;
                            }
                            local1082 += zStep;
                            rowOffset = local103 + 1;
                            if (local1082 < depths[rowOffset]) {
                                dst[rowOffset] = local88;
                                depths[rowOffset] = local1082;
                            }
                            z = local1082 + zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset] = local88;
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                } else if (this.halfBlend) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            if (z < depths[local103]) {
                                local496 = dst[local103];
                                local500 = local88 + local496;
                                local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                                local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                                dst[local103] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                                depths[local103] = z;
                            }
                            local1082 = z + zStep;
                            local103++;
                            if (local1082 < depths[local103]) {
                                local496 = dst[local103];
                                local500 = local88 + local496;
                                local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                                local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                                dst[local103] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                                depths[local103] = local1082;
                            }
                            local1082 += zStep;
                            local103++;
                            if (local1082 < depths[local103]) {
                                local496 = dst[local103];
                                local500 = local88 + local496;
                                local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                                local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                                dst[local103] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                                depths[local103] = local1082;
                            }
                            local1082 += zStep;
                            rowOffset = local103 + 1;
                            if (local1082 < depths[rowOffset]) {
                                local496 = dst[rowOffset];
                                local500 = local88 + local496;
                                local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                                local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                                dst[rowOffset] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                                depths[rowOffset] = local1082;
                            }
                            z = local1082 + zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                local496 = dst[rowOffset];
                                local500 = local88 + local496;
                                local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                                local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                                dst[rowOffset] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                            local103 = rowOffset + 1;
                            if (z < depths[local103]) {
                                local223 = dst[local103];
                                dst[local103] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[local103] = z;
                            }
                            local1082 = z + zStep;
                            local103++;
                            if (local1082 < depths[local103]) {
                                local223 = dst[local103];
                                dst[local103] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[local103] = local1082;
                            }
                            local1082 += zStep;
                            local103++;
                            if (local1082 < depths[local103]) {
                                local223 = dst[local103];
                                dst[local103] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[local103] = local1082;
                            }
                            local1082 += zStep;
                            rowOffset = local103 + 1;
                            if (local1082 < depths[rowOffset]) {
                                local223 = dst[rowOffset];
                                dst[rowOffset] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[rowOffset] = local1082;
                            }
                            z = local1082 + zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                local223 = dst[rowOffset];
                                dst[rowOffset] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                }
            } else {
                local54 = endX - startX;
                if (this.alpha == 0) {
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else if (this.halfBlend) {
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local223 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            local496 = dst[rowOffset];
                            local500 = local223 + local496;
                            local508 = (local223 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[rowOffset] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                            local223 = dst[rowOffset];
                            dst[rowOffset] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                }
            }
        } else if (this.fastScanline) {
            local54 = endX - startX >> 2;
            local58 = redStep * 4.0F;
            local62 = greenStep * 4.0F;
            local66 = blueStep * 4.0F;
            if (this.alpha == 0) {
                if (local54 > 0) {
                    do {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += local58;
                        green += local62;
                        blue += local66;
                        local103 = rowOffset + 1;
                        if (z < depths[local103]) {
                            dst[local103] = local88;
                        }
                        local1082 = z + zStep;
                        local103++;
                        if (local1082 < depths[local103]) {
                            dst[local103] = local88;
                        }
                        local1082 += zStep;
                        local103++;
                        if (local1082 < depths[local103]) {
                            dst[local103] = local88;
                        }
                        local1082 += zStep;
                        rowOffset = local103 + 1;
                        if (local1082 < depths[rowOffset]) {
                            dst[rowOffset] = local88;
                        }
                        z = local1082 + zStep;
                        local54--;
                    } while (local54 > 0);
                }
                local54 = endX - startX & 0x3;
                if (local54 > 0) {
                    local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = local88;
                        }
                        z += zStep;
                        local54--;
                    } while (local54 > 0);
                    return;
                }
            } else if (this.halfBlend) {
                if (local54 > 0) {
                    do {
                        local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += local58;
                        green += local62;
                        blue += local66;
                        local103 = rowOffset + 1;
                        if (z < depths[local103]) {
                            local496 = dst[local103];
                            local500 = local88 + local496;
                            local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[local103] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                        }
                        local1082 = z + zStep;
                        local103++;
                        if (local1082 < depths[local103]) {
                            local496 = dst[local103];
                            local500 = local88 + local496;
                            local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[local103] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                        }
                        local1082 += zStep;
                        local103++;
                        if (local1082 < depths[local103]) {
                            local496 = dst[local103];
                            local500 = local88 + local496;
                            local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[local103] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                        }
                        local1082 += zStep;
                        rowOffset = local103 + 1;
                        if (local1082 < depths[rowOffset]) {
                            local496 = dst[rowOffset];
                            local500 = local88 + local496;
                            local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[rowOffset] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                        }
                        z = local1082 + zStep;
                        local54--;
                    } while (local54 > 0);
                }
                local54 = endX - startX & 0x3;
                if (local54 > 0) {
                    local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local496 = dst[rowOffset];
                            local500 = local88 + local496;
                            local508 = (local88 & 0xFF00FF) + (local496 & 0xFF00FF);
                            local496 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                            dst[rowOffset] = local500 - local496 | 0xFF000000 | local496 - (local496 >>> 8);
                        }
                        z += zStep;
                        local54--;
                    } while (local54 > 0);
                }
            } else {
                local163 = this.alpha;
                local168 = 256 - this.alpha;
                if (local54 > 0) {
                    do {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += local58;
                        green += local62;
                        blue += local66;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        local103 = rowOffset + 1;
                        if (z < depths[local103]) {
                            local223 = dst[local103];
                            dst[local103] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        local1082 = z + zStep;
                        local103++;
                        if (local1082 < depths[local103]) {
                            local223 = dst[local103];
                            dst[local103] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        local1082 += zStep;
                        local103++;
                        if (local1082 < depths[local103]) {
                            local223 = dst[local103];
                            dst[local103] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        local1082 += zStep;
                        rowOffset = local103 + 1;
                        if (local1082 < depths[rowOffset]) {
                            local223 = dst[rowOffset];
                            dst[rowOffset] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        z = local1082 + zStep;
                        local54--;
                    } while (local54 > 0);
                }
                local54 = endX - startX & 0x3;
                if (local54 > 0) {
                    local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local223 = dst[rowOffset];
                            dst[rowOffset] = (local168 | local223 >> 24) << 24 | local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        z += zStep;
                        local54--;
                    } while (local54 > 0);
                    return;
                }
            }
        } else {
            local54 = endX - startX;
            if (this.alpha == 0) {
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        dst[rowOffset] = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    }
                    z += zStep;
                    red += redStep;
                    green += greenStep;
                    blue += blueStep;
                    local54--;
                } while (local54 > 0);
            } else if (this.halfBlend) {
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local223 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local496 = dst[rowOffset];
                        local500 = local223 + local496;
                        local508 = (local223 & 0xFF00FF) + (local496 & 0xFF00FF);
                        @Pc(3256) int local3256 = (local508 & 0x1000100) + (local500 - local508 & 0x10000);
                        dst[rowOffset] = local500 - local3256 | 0xFF000000 | local3256 - (local3256 >>> 8);
                    }
                    z += zStep;
                    red += redStep;
                    green += greenStep;
                    blue += blueStep;
                    local54--;
                } while (local54 > 0);
            } else {
                local163 = this.alpha;
                local168 = 256 - this.alpha;
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        @Pc(3148) int local3148 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        local223 = dst[rowOffset];
                        dst[rowOffset] = (local168 | local223 >> 24) << 24 | local3148 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                    }
                    z += zStep;
                    red += redStep;
                    green += greenStep;
                    blue += blueStep;
                    local54--;
                } while (local54 > 0);
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "([I[IIIIFFFFFFFFFFFFFFFF)V")
    public void drawTexturedSpanArgb(@OriginalArg(0) int[] dst, @OriginalArg(1) int[] texels, @OriginalArg(2) int rowOffset, @OriginalArg(3) int startX, @OriginalArg(4) int endX, @OriginalArg(5) float invZLeft, @OriginalArg(6) float invZRight, @OriginalArg(7) float uLeft, @OriginalArg(8) float uRight, @OriginalArg(9) float vLeft, @OriginalArg(10) float vRight, @OriginalArg(11) float fogLeft, @OriginalArg(12) float fogRight, @OriginalArg(13) float alphaLeft, @OriginalArg(14) float alphaRight, @OriginalArg(15) float redLeft, @OriginalArg(16) float redRight, @OriginalArg(17) float greenLeft, @OriginalArg(18) float greenRight, @OriginalArg(19) float blueLeft, @OriginalArg(20) float blueRight) {
        @Pc(3) int local3 = endX - startX;
        @Pc(8) float local8 = 1.0F / (float) local3;
        @Pc(14) float local14 = (invZRight - invZLeft) * local8;
        @Pc(20) float local20 = (uRight - uLeft) * local8;
        @Pc(26) float local26 = (vRight - vLeft) * local8;
        @Pc(32) float local32 = (fogRight - fogLeft) * local8;
        @Pc(38) float local38 = (alphaRight - alphaLeft) * local8;
        @Pc(44) float local44 = (redRight - redLeft) * local8;
        @Pc(50) float local50 = (greenRight - greenLeft) * local8;
        @Pc(56) float local56 = (blueRight - blueLeft) * local8;
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                invZLeft -= local14 * (float) startX;
                uLeft -= local20 * (float) startX;
                vLeft -= local26 * (float) startX;
                fogLeft -= local32 * (float) startX;
                alphaLeft -= local38 * (float) startX;
                redLeft -= local44 * (float) startX;
                greenLeft -= local50 * (float) startX;
                blueLeft -= local56 * (float) startX;
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        local3 = endX - startX;
        @Pc(138) int local138 = rowOffset + startX;
        while (local3-- > 0) {
            @Pc(143) float local143 = 1.0F / invZLeft;
            if (local143 < this.depthBuffer[local138]) {
                @Pc(159) int local159 = (int) (uLeft * local143 * (float) this.textureSize);
                if (this.textureRepeats) {
                    local159 &= this.textureMask;
                } else if (local159 < 0) {
                    local159 = 0;
                } else if (local159 > this.textureMask) {
                    local159 = this.textureMask;
                }
                @Pc(189) int local189 = (int) (vLeft * local143 * (float) this.textureSize);
                if (this.textureRepeats) {
                    local189 &= this.textureMask;
                } else if (local189 < 0) {
                    local189 = 0;
                } else if (local189 > this.textureMask) {
                    local189 = this.textureMask;
                }
                @Pc(220) int local220 = this.texels[local189 * this.textureSize + local159];
                @Pc(232) int local232;
                if (this.textureBlendMode == 2) {
                    local232 = local220 >> 24 & 0xFF;
                } else if (this.textureBlendMode == 1) {
                    local232 = local220 == 0 ? 0 : 255;
                } else {
                    local232 = (int) alphaLeft;
                }
                if (local232 != 0) {
                    @Pc(290) int local290;
                    @Pc(299) int local299;
                    @Pc(321) int local321;
                    if (local232 == 255) {
                        local290 = ((int) (redLeft * (float) (local220 >> 16 & 0xFF)) & 0xFF00 | 0xFF0000) << 8 | (int) (greenLeft * (float) (local220 >> 8 & 0xFF)) & 0xFF00 | (int) (blueLeft * (float) (local220 & 0xFF)) >> 8;
                        if (fogLeft != 0.0F) {
                            local299 = (int) (255.0F - fogLeft);
                            local321 = ((this.fogColour & 0xFF00FF) * (int) fogLeft & 0xFF00FF00 | (this.fogColour & 0xFF00) * (int) fogLeft & 0xFF0000) >>> 8;
                            local290 = (((local290 & 0xFF00FF) * local299 & 0xFF00FF00 | (local290 & 0xFF00) * local299 & 0xFF0000) >>> 8) + local321;
                        }
                        dst[local138] = local232 << 24 | local290;
                        this.depthBuffer[local138] = local143;
                    } else {
                        local290 = ((int) (redLeft * (float) (local220 >> 16 & 0xFF)) & 0xFF00 | 0xFF0000) << 8 | (int) (greenLeft * (float) (local220 >> 8 & 0xFF)) & 0xFF00 | (int) (blueLeft * (float) (local220 & 0xFF)) >> 8;
                        if (fogLeft != 0.0F) {
                            local299 = (int) (255.0F - fogLeft);
                            local321 = ((this.fogColour & 0xFF00FF) * (int) fogLeft & 0xFF00FF00 | (this.fogColour & 0xFF00) * (int) fogLeft & 0xFF0000) >>> 8;
                            local290 = (((local290 & 0xFF00FF) * local299 & 0xFF00FF00 | (local290 & 0xFF00) * local299 & 0xFF0000) >>> 8) + local321;
                        }
                        local299 = dst[local138];
                        local321 = 255 - local232;
                        local290 = ((local299 & 0xFF00FF) * local321 + (local290 & 0xFF00FF) * local232 & 0xFF00FF00) + ((local299 & 0xFF00) * local321 + (local290 & 0xFF00) * local232 & 0xFF0000) >> 8;
                        dst[local138] = (local232 | dst[local138] >> 24) << 24 | local290;
                        this.depthBuffer[local138] = local143;
                    }
                }
            }
            local138++;
            invZLeft += local14;
            uLeft += local20;
            vLeft += local26;
            fogLeft += local32;
            alphaLeft += local38;
            redLeft += local44;
            greenLeft += local50;
            blueLeft += local56;
        }
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "([I[FIIIIIFFFFFFFF)V")
    public void drawGouraudSpanRgb(@OriginalArg(0) int[] dst, @OriginalArg(1) float[] depths, @OriginalArg(2) int rowOffset, @OriginalArg(5) int startX, @OriginalArg(6) int endX, @OriginalArg(7) float z, @OriginalArg(8) float zStep, @OriginalArg(9) float red, @OriginalArg(10) float redStep, @OriginalArg(11) float green, @OriginalArg(12) float greenStep, @OriginalArg(13) float blue, @OriginalArg(14) float blueStep) {
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        @Pc(163) int local163;
        @Pc(168) int local168;
        @Pc(223) int local223;
        @Pc(456) int local456;
        @Pc(460) int local460;
        @Pc(468) int local468;
        @Pc(54) int local54;
        @Pc(88) int local88;
        @Pc(103) int local103;
        @Pc(58) float local58;
        @Pc(62) float local62;
        @Pc(66) float local66;
        if (this.depthDisabled) {
            rowOffset += startX;
            red += redStep * (float) startX;
            green += greenStep * (float) startX;
            blue += blueStep * (float) startX;
            if (this.fastScanline) {
                local54 = endX - startX >> 2;
                local58 = redStep * 4.0F;
                local62 = greenStep * 4.0F;
                local66 = blueStep * 4.0F;
                if (this.alpha == 0) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            dst[rowOffset] = local88;
                            dst[local103++] = local88;
                            dst[local103++] = local88;
                            rowOffset = local103 + 1;
                            dst[local103] = local88;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            dst[rowOffset++] = local88;
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                } else if (this.halfBlend) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            local456 = dst[rowOffset];
                            local460 = local88 + local456;
                            local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[rowOffset] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                            @Pc(497) int local497 = local103++;
                            @Pc(503) int local503 = dst[local497];
                            @Pc(507) int local507 = local88 + local503;
                            @Pc(515) int local515 = (local88 & 0xFF00FF) + (local503 & 0xFF00FF);
                            @Pc(525) int local525 = (local515 & 0x1000100) + (local507 - local515 & 0x10000);
                            dst[local497] = local507 - local525 | 0xFF000000 | local525 - (local525 >>> 8);
                            @Pc(544) int local544 = local103++;
                            @Pc(550) int local550 = dst[local544];
                            @Pc(554) int local554 = local88 + local550;
                            @Pc(562) int local562 = (local88 & 0xFF00FF) + (local550 & 0xFF00FF);
                            @Pc(572) int local572 = (local562 & 0x1000100) + (local554 - local562 & 0x10000);
                            dst[local544] = local554 - local572 | 0xFF000000 | local572 - (local572 >>> 8);
                            rowOffset = local103 + 1;
                            @Pc(597) int local597 = dst[local103];
                            @Pc(601) int local601 = local88 + local597;
                            @Pc(609) int local609 = (local88 & 0xFF00FF) + (local597 & 0xFF00FF);
                            @Pc(619) int local619 = (local609 & 0x1000100) + (local601 - local609 & 0x10000);
                            dst[local103] = local601 - local619 | 0xFF000000 | local619 - (local619 >>> 8);
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            local168 = rowOffset++;
                            local456 = dst[local168];
                            local460 = local88 + local456;
                            local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[local168] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                            local54--;
                        } while (local54 > 0);
                    }
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                            local223 = dst[rowOffset];
                            local103 = rowOffset + 1;
                            dst[rowOffset] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local223 = dst[local103];
                            dst[local103++] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local223 = dst[local103];
                            dst[local103++] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local223 = dst[local103];
                            rowOffset = local103 + 1;
                            dst[local103] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        do {
                            local223 = dst[rowOffset];
                            dst[rowOffset++] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                }
            } else {
                local54 = endX - startX;
                if (this.alpha == 0) {
                    do {
                        dst[rowOffset++] = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else if (this.halfBlend) {
                    do {
                        local168 = rowOffset++;
                        local223 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local456 = dst[local168];
                        local460 = local223 + local456;
                        local468 = (local223 & 0xFF00FF) + (local456 & 0xFF00FF);
                        local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                        dst[local168] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    do {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        local223 = dst[rowOffset];
                        dst[rowOffset++] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        local54--;
                    } while (local54 > 0);
                }
            }
            return;
        }
        rowOffset += startX - 1;
        z += zStep * (float) startX;
        red += redStep * (float) startX;
        green += greenStep * (float) startX;
        blue += blueStep * (float) startX;
        @Pc(1034) float local1034;
        if (this.threadResource.zWrite) {
            if (this.fastScanline) {
                local54 = endX - startX >> 2;
                local58 = redStep * 4.0F;
                local62 = greenStep * 4.0F;
                local66 = blueStep * 4.0F;
                if (this.alpha == 0) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            if (z < depths[local103]) {
                                dst[local103] = local88;
                                depths[local103] = z;
                            }
                            local1034 = z + zStep;
                            local103++;
                            if (local1034 < depths[local103]) {
                                dst[local103] = local88;
                                depths[local103] = local1034;
                            }
                            local1034 += zStep;
                            local103++;
                            if (local1034 < depths[local103]) {
                                dst[local103] = local88;
                                depths[local103] = local1034;
                            }
                            local1034 += zStep;
                            rowOffset = local103 + 1;
                            if (local1034 < depths[rowOffset]) {
                                dst[rowOffset] = local88;
                                depths[rowOffset] = local1034;
                            }
                            z = local1034 + zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset] = local88;
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                } else if (this.halfBlend) {
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local103 = rowOffset + 1;
                            if (z < depths[local103]) {
                                local456 = dst[local103];
                                local460 = local88 + local456;
                                local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                                local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                                dst[local103] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                                depths[local103] = z;
                            }
                            local1034 = z + zStep;
                            local103++;
                            if (local1034 < depths[local103]) {
                                local456 = dst[local103];
                                local460 = local88 + local456;
                                local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                                local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                                dst[local103] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                                depths[local103] = local1034;
                            }
                            local1034 += zStep;
                            local103++;
                            if (local1034 < depths[local103]) {
                                local456 = dst[local103];
                                local460 = local88 + local456;
                                local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                                local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                                dst[local103] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                                depths[local103] = local1034;
                            }
                            local1034 += zStep;
                            rowOffset = local103 + 1;
                            if (local1034 < depths[rowOffset]) {
                                local456 = dst[rowOffset];
                                local460 = local88 + local456;
                                local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                                local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                                dst[rowOffset] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                                depths[rowOffset] = local1034;
                            }
                            z = local1034 + zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                local456 = dst[rowOffset];
                                local460 = local88 + local456;
                                local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                                local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                                dst[rowOffset] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    if (local54 > 0) {
                        do {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            red += local58;
                            green += local62;
                            blue += local66;
                            local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                            local103 = rowOffset + 1;
                            if (z < depths[local103]) {
                                local223 = dst[local103];
                                dst[local103] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[local103] = z;
                            }
                            local1034 = z + zStep;
                            local103++;
                            if (local1034 < depths[local103]) {
                                local223 = dst[local103];
                                dst[local103] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[local103] = local1034;
                            }
                            local1034 += zStep;
                            local103++;
                            if (local1034 < depths[local103]) {
                                local223 = dst[local103];
                                dst[local103] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[local103] = local1034;
                            }
                            local1034 += zStep;
                            rowOffset = local103 + 1;
                            if (local1034 < depths[rowOffset]) {
                                local223 = dst[rowOffset];
                                dst[rowOffset] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[rowOffset] = local1034;
                            }
                            z = local1034 + zStep;
                            local54--;
                        } while (local54 > 0);
                    }
                    local54 = endX - startX & 0x3;
                    if (local54 > 0) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                local223 = dst[rowOffset];
                                dst[rowOffset] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local54--;
                        } while (local54 > 0);
                        return;
                    }
                }
            } else {
                local54 = endX - startX;
                if (this.alpha == 0) {
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else if (this.halfBlend) {
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local223 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            local456 = dst[rowOffset];
                            local460 = local223 + local456;
                            local468 = (local223 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[rowOffset] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                } else {
                    local163 = this.alpha;
                    local168 = 256 - this.alpha;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                            local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                            local223 = dst[rowOffset];
                            dst[rowOffset] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        red += redStep;
                        green += greenStep;
                        blue += blueStep;
                        local54--;
                    } while (local54 > 0);
                }
            }
        } else if (this.fastScanline) {
            local54 = endX - startX >> 2;
            local58 = redStep * 4.0F;
            local62 = greenStep * 4.0F;
            local66 = blueStep * 4.0F;
            if (this.alpha == 0) {
                if (local54 > 0) {
                    do {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += local58;
                        green += local62;
                        blue += local66;
                        local103 = rowOffset + 1;
                        if (z < depths[local103]) {
                            dst[local103] = local88;
                        }
                        local1034 = z + zStep;
                        local103++;
                        if (local1034 < depths[local103]) {
                            dst[local103] = local88;
                        }
                        local1034 += zStep;
                        local103++;
                        if (local1034 < depths[local103]) {
                            dst[local103] = local88;
                        }
                        local1034 += zStep;
                        rowOffset = local103 + 1;
                        if (local1034 < depths[rowOffset]) {
                            dst[rowOffset] = local88;
                        }
                        z = local1034 + zStep;
                        local54--;
                    } while (local54 > 0);
                }
                local54 = endX - startX & 0x3;
                if (local54 > 0) {
                    local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = local88;
                        }
                        z += zStep;
                        local54--;
                    } while (local54 > 0);
                    return;
                }
            } else if (this.halfBlend) {
                if (local54 > 0) {
                    do {
                        local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += local58;
                        green += local62;
                        blue += local66;
                        local103 = rowOffset + 1;
                        if (z < depths[local103]) {
                            local456 = dst[local103];
                            local460 = local88 + local456;
                            local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[local103] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                        }
                        local1034 = z + zStep;
                        local103++;
                        if (local1034 < depths[local103]) {
                            local456 = dst[local103];
                            local460 = local88 + local456;
                            local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[local103] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                        }
                        local1034 += zStep;
                        local103++;
                        if (local1034 < depths[local103]) {
                            local456 = dst[local103];
                            local460 = local88 + local456;
                            local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[local103] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                        }
                        local1034 += zStep;
                        rowOffset = local103 + 1;
                        if (local1034 < depths[rowOffset]) {
                            local456 = dst[rowOffset];
                            local460 = local88 + local456;
                            local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[rowOffset] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                        }
                        z = local1034 + zStep;
                        local54--;
                    } while (local54 > 0);
                }
                local54 = endX - startX & 0x3;
                if (local54 > 0) {
                    local88 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local456 = dst[rowOffset];
                            local460 = local88 + local456;
                            local468 = (local88 & 0xFF00FF) + (local456 & 0xFF00FF);
                            local456 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                            dst[rowOffset] = local460 - local456 | 0xFF000000 | local456 - (local456 >>> 8);
                        }
                        z += zStep;
                        local54--;
                    } while (local54 > 0);
                }
            } else {
                local163 = this.alpha;
                local168 = 256 - this.alpha;
                if (local54 > 0) {
                    do {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        red += local58;
                        green += local62;
                        blue += local66;
                        local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        local103 = rowOffset + 1;
                        if (z < depths[local103]) {
                            local223 = dst[local103];
                            dst[local103] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        local1034 = z + zStep;
                        local103++;
                        if (local1034 < depths[local103]) {
                            local223 = dst[local103];
                            dst[local103] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        local1034 += zStep;
                        local103++;
                        if (local1034 < depths[local103]) {
                            local223 = dst[local103];
                            dst[local103] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        local1034 += zStep;
                        rowOffset = local103 + 1;
                        if (local1034 < depths[rowOffset]) {
                            local223 = dst[rowOffset];
                            dst[rowOffset] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        z = local1034 + zStep;
                        local54--;
                    } while (local54 > 0);
                }
                local54 = endX - startX & 0x3;
                if (local54 > 0) {
                    local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    local88 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local223 = dst[rowOffset];
                            dst[rowOffset] = local88 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                        }
                        z += zStep;
                        local54--;
                    } while (local54 > 0);
                    return;
                }
            }
        } else {
            local54 = endX - startX;
            if (this.alpha == 0) {
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        dst[rowOffset] = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                    }
                    z += zStep;
                    red += redStep;
                    green += greenStep;
                    blue += blueStep;
                    local54--;
                } while (local54 > 0);
            } else if (this.halfBlend) {
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local223 = (int) red & 0xFF0000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        local456 = dst[rowOffset];
                        local460 = local223 + local456;
                        local468 = (local223 & 0xFF00FF) + (local456 & 0xFF00FF);
                        @Pc(3112) int local3112 = (local468 & 0x1000100) + (local460 - local468 & 0x10000);
                        dst[rowOffset] = local460 - local3112 | 0xFF000000 | local3112 - (local3112 >>> 8);
                    }
                    z += zStep;
                    red += redStep;
                    green += greenStep;
                    blue += blueStep;
                    local54--;
                } while (local54 > 0);
            } else {
                local163 = this.alpha;
                local168 = 256 - this.alpha;
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local88 = (int) red & 0xFF0000 | 0xFF000000 | (int) green & 0xFF00 | (int) blue & 0xFF;
                        @Pc(3012) int local3012 = ((local88 & 0xFF00FF) * local168 >> 8 & 0xFF00FF) + ((local88 & 0xFF00) * local168 >> 8 & 0xFF00);
                        local223 = dst[rowOffset];
                        dst[rowOffset] = local3012 + ((local223 & 0xFF00FF) * local163 >> 8 & 0xFF00FF) + ((local223 & 0xFF00) * local163 >> 8 & 0xFF00);
                    }
                    z += zStep;
                    red += redStep;
                    green += greenStep;
                    blue += blueStep;
                    local54--;
                } while (local54 > 0);
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "([I[IIIIFFFFFFFFFFFFFFFFFFFF)V")
    public void drawBlendedTexturedSpan(@OriginalArg(0) int[] dst, @OriginalArg(1) int[] texels, @OriginalArg(2) int rowOffset, @OriginalArg(3) int startX, @OriginalArg(4) int endX, @OriginalArg(5) float invZLeft, @OriginalArg(6) float invZRight, @OriginalArg(7) float uLeft, @OriginalArg(8) float uRight, @OriginalArg(9) float vLeft, @OriginalArg(10) float vRight, @OriginalArg(11) float fogLeft, @OriginalArg(12) float fogRight, @OriginalArg(13) float alphaLeft, @OriginalArg(14) float alphaRight, @OriginalArg(15) float redLeft, @OriginalArg(16) float redRight, @OriginalArg(17) float greenLeft, @OriginalArg(18) float greenRight, @OriginalArg(19) float blueLeft, @OriginalArg(20) float blueRight, @OriginalArg(21) float weightLeft, @OriginalArg(22) float weightRight, @OriginalArg(23) float weight2Left, @OriginalArg(24) float weight2Right) {
        @Pc(3) int local3 = endX - startX;
        @Pc(8) float local8 = 1.0F / (float) local3;
        @Pc(14) float local14 = (invZRight - invZLeft) * local8;
        @Pc(20) float local20 = (uRight - uLeft) * local8;
        @Pc(26) float local26 = (vRight - vLeft) * local8;
        @Pc(32) float local32 = (fogRight - fogLeft) * local8;
        @Pc(38) float local38 = (redRight - redLeft) * local8;
        @Pc(44) float local44 = (greenRight - greenLeft) * local8;
        @Pc(50) float local50 = (blueRight - blueLeft) * local8;
        @Pc(56) float local56 = (weightRight - weightLeft) * local8;
        @Pc(62) float local62 = (weight2Right - weight2Left) * local8;
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                invZLeft -= local14 * (float) startX;
                uLeft -= local20 * (float) startX;
                vLeft -= local26 * (float) startX;
                fogLeft -= local32 * (float) startX;
                redLeft -= local38 * (float) startX;
                greenLeft -= local44 * (float) startX;
                blueLeft -= local50 * (float) startX;
                weightLeft -= local56 * (float) startX;
                weight2Left -= local62 * (float) startX;
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        local3 = endX - startX;
        @Pc(151) int local151 = rowOffset + startX;
        while (local3-- > 0) {
            @Pc(156) float local156 = 1.0F / invZLeft;
            if (local156 < this.depthBuffer[local151]) {
                @Pc(167) float local167 = uLeft * local156;
                @Pc(171) float local171 = vLeft * local156;
                @Pc(184) int local184 = (int) (local167 * (float) this.textureSize * this.textureScale) & this.textureMask;
                @Pc(197) int local197 = (int) (local171 * (float) this.textureSize * this.textureScale) & this.textureMask;
                @Pc(207) int local207 = this.texels[local197 * this.textureSize + local184];
                @Pc(220) int local220 = (int) (local167 * (float) this.textureSize2 * this.textureScale2) & this.textureMask2;
                @Pc(233) int local233 = (int) (local171 * (float) this.textureSize2 * this.textureScale2) & this.textureMask2;
                @Pc(243) int local243 = this.texels2[local233 * this.textureSize2 + local220];
                @Pc(256) int local256 = (int) (local167 * (float) this.textureSize3 * this.textureScale3) & this.textureMask3;
                @Pc(269) int local269 = (int) (local171 * (float) this.textureSize3 * this.textureScale3) & this.textureMask3;
                @Pc(279) int local279 = this.texels3[local269 * this.textureSize3 + local256];
                @Pc(285) float local285 = 1.0F - (weightLeft + weight2Left);
                @Pc(319) int local319 = ((int) (weightLeft * (float) (local207 >> 16 & 0xFF)) | 0xFF00) << 16 | (int) (weightLeft * (float) (local207 >> 8 & 0xFF)) << 8 | (int) (weightLeft * (float) (local207 & 0xFF));
                @Pc(353) int local353 = ((int) (weight2Left * (float) (local243 >> 16 & 0xFF)) | 0xFF00) << 16 | (int) (weight2Left * (float) (local243 >> 8 & 0xFF)) << 8 | (int) (weight2Left * (float) (local243 & 0xFF));
                @Pc(387) int local387 = ((int) (local285 * (float) (local279 >> 16 & 0xFF)) | 0xFF00) << 16 | (int) (local285 * (float) (local279 >> 8 & 0xFF)) << 8 | (int) (local285 * (float) (local279 & 0xFF));
                @Pc(393) int local393 = local319 + local353 + local387;
                @Pc(431) int local431 = ((int) (redLeft * (float) (local393 >> 16 & 0xFF)) & 0xFF00 | 0xFF0000) << 8 | (int) (greenLeft * (float) (local393 >> 8 & 0xFF)) & 0xFF00 | (int) (blueLeft * (float) (local393 & 0xFF)) >> 8;
                if (fogLeft != 0.0F) {
                    @Pc(440) int local440 = (int) (255.0F - fogLeft);
                    @Pc(462) int local462 = ((this.fogColour & 0xFF00FF) * (int) fogLeft & 0xFF00FF00 | (this.fogColour & 0xFF00) * (int) fogLeft & 0xFF0000) >>> 8;
                    local431 = (((local431 & 0xFF00FF) * local440 & 0xFF00FF00 | (local431 & 0xFF00) * local440 & 0xFF0000) >>> 8) + local462;
                }
                dst[local151] = local431;
                this.depthBuffer[local151] = local156;
            }
            local151++;
            invZLeft += local14;
            uLeft += local20;
            vLeft += local26;
            fogLeft += local32;
            redLeft += local38;
            greenLeft += local44;
            blueLeft += local50;
            weightLeft += local56;
            weight2Left += local62;
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "(FFFFFFFFFFFFFFFIIIIIIII)V")
    public void renderTexturedTriangleRgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) float uA, @OriginalArg(10) float uB, @OriginalArg(11) float uC, @OriginalArg(12) float vA, @OriginalArg(13) float vB, @OriginalArg(14) float vC, @OriginalArg(15) int colourA, @OriginalArg(16) int colourB, @OriginalArg(17) int colourC, @OriginalArg(18) int fogColour, @OriginalArg(19) int fogA, @OriginalArg(20) int fogB, @OriginalArg(21) int fogC, @OriginalArg(22) int texture) {
        if (texture != this.textureId) {
            this.texels = this.toolkit.getArgbTexture(texture);
            if (this.texels == null) {
                this.renderTriangleRgb((float) (int) yA, (float) (int) yB, (float) (int) yC, (float) (int) xA, (float) (int) xB, (float) (int) xC, (float) (int) zA, (float) (int) zB, (float) (int) zC, Static462.blendArgb(fogColour | fogA << 24, colourA), Static462.blendArgb(fogColour | fogB << 24, colourB), Static462.blendArgb(fogColour | fogC << 24, colourC));
                return;
            }
            this.textureSize = this.toolkit.smallTexture(texture) ? 64 : this.toolkit.textureSize;
            this.textureMask = this.textureSize - 1;
            this.textureBlendMode = this.toolkit.textureAlphaBlendMode(texture);
            this.textureRepeats = this.toolkit.textureRepeats(texture);
        }
        this.fogColour = fogColour;
        @Pc(106) float local106 = (float) (colourA >> 24 & 0xFF);
        @Pc(113) float local113 = (float) (colourB >> 24 & 0xFF);
        @Pc(120) float local120 = (float) (colourC >> 24 & 0xFF);
        @Pc(127) float local127 = (float) (colourA >> 16 & 0xFF);
        @Pc(134) float local134 = (float) (colourB >> 16 & 0xFF);
        @Pc(141) float local141 = (float) (colourC >> 16 & 0xFF);
        @Pc(148) float local148 = (float) (colourA >> 8 & 0xFF);
        @Pc(155) float local155 = (float) (colourB >> 8 & 0xFF);
        @Pc(162) float local162 = (float) (colourC >> 8 & 0xFF);
        @Pc(167) float local167 = (float) (colourA & 0xFF);
        @Pc(172) float local172 = (float) (colourB & 0xFF);
        @Pc(177) float local177 = (float) (colourC & 0xFF);
        uA /= zA;
        uB /= zB;
        uC /= zC;
        vA /= zA;
        vB /= zB;
        vC /= zC;
        zA = 1.0F / zA;
        zB = 1.0F / zB;
        zC = 1.0F / zC;
        @Pc(215) float local215 = 0.0F;
        @Pc(217) float local217 = 0.0F;
        @Pc(219) float local219 = 0.0F;
        @Pc(221) float local221 = 0.0F;
        @Pc(223) float local223 = 0.0F;
        @Pc(225) float local225 = 0.0F;
        @Pc(227) float local227 = 0.0F;
        @Pc(229) float local229 = 0.0F;
        @Pc(231) float local231 = 0.0F;
        @Pc(239) float local239;
        if (yB != yA) {
            local239 = yB - yA;
            local215 = (xB - xA) / local239;
            local217 = (zB - zA) / local239;
            local219 = (uB - uA) / local239;
            local221 = (vB - vA) / local239;
            local223 = (float) (fogB - fogA) / local239;
            local225 = (local113 - local106) / local239;
            local227 = (local134 - local127) / local239;
            local229 = (local155 - local148) / local239;
            local231 = (local172 - local167) / local239;
        }
        local239 = 0.0F;
        @Pc(298) float local298 = 0.0F;
        @Pc(300) float local300 = 0.0F;
        @Pc(302) float local302 = 0.0F;
        @Pc(304) float local304 = 0.0F;
        @Pc(306) float local306 = 0.0F;
        @Pc(308) float local308 = 0.0F;
        @Pc(310) float local310 = 0.0F;
        @Pc(312) float local312 = 0.0F;
        @Pc(320) float local320;
        if (yC != yB) {
            local320 = yC - yB;
            local239 = (xC - xB) / local320;
            local298 = (zC - zB) / local320;
            local300 = (uC - uB) / local320;
            local302 = (vC - vB) / local320;
            local304 = (float) (fogC - fogB) / local320;
            local306 = (local120 - local113) / local320;
            local308 = (local141 - local134) / local320;
            local310 = (local162 - local155) / local320;
            local312 = (local177 - local172) / local320;
        }
        local320 = 0.0F;
        @Pc(379) float local379 = 0.0F;
        @Pc(381) float local381 = 0.0F;
        @Pc(383) float local383 = 0.0F;
        @Pc(385) float local385 = 0.0F;
        @Pc(387) float local387 = 0.0F;
        @Pc(389) float local389 = 0.0F;
        @Pc(391) float local391 = 0.0F;
        @Pc(393) float local393 = 0.0F;
        if (yA != yC) {
            @Pc(401) float local401 = yA - yC;
            local320 = (xA - xC) / local401;
            local379 = (zA - zC) / local401;
            local381 = (uA - uC) / local401;
            local383 = (vA - vC) / local401;
            local385 = (float) (fogA - fogC) / local401;
            local387 = (local106 - local120) / local401;
            local389 = (local127 - local141) / local401;
            local391 = (local148 - local162) / local401;
            local393 = (local167 - local177) / local401;
        }
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yB < yC) {
                    xC = xA;
                    zC = zA;
                    uC = uA;
                    vC = vA;
                    fogC = fogA;
                    local120 = local106;
                    local141 = local127;
                    local162 = local148;
                    local177 = local167;
                    if (yA < 0.0F) {
                        xA -= local215 * yA;
                        xC -= local320 * yA;
                        zA -= local217 * yA;
                        zC -= local379 * yA;
                        uA -= local219 * yA;
                        uC -= local381 * yA;
                        vA -= local221 * yA;
                        vC -= local383 * yA;
                        fogA = (int) ((float) fogA - local223 * yA);
                        fogC = (int) ((float) fogC - local385 * yA);
                        local106 -= local225 * yA;
                        local120 -= local387 * yA;
                        local127 -= local225 * yA;
                        local141 -= local387 * yA;
                        local148 -= local225 * yA;
                        local162 -= local387 * yA;
                        local167 -= local225 * yA;
                        local177 -= local387 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local239 * yB;
                        zB -= local298 * yB;
                        uB -= local300 * yB;
                        vB -= local302 * yB;
                        fogB = (int) ((float) fogB - local304 * yB);
                        local113 -= local306 * yB;
                        local134 -= local308 * yB;
                        local155 -= local310 * yB;
                        local172 -= local312 * yB;
                        yB = 0.0F;
                    }
                    if (yA != yB && local320 < local215 || yA == yB && local320 > local239) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                            xA += local215;
                            xC += local320;
                            zA += local217;
                            zC += local379;
                            uA += local219;
                            uC += local381;
                            vA += local221;
                            vC += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local385);
                            local106 += local225;
                            local120 += local387;
                            local127 += local227;
                            local141 += local389;
                            local148 += local229;
                            local162 += local391;
                            local167 += local231;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                            xB += local239;
                            xC += local320;
                            zB += local298;
                            zC += local379;
                            uB += local300;
                            uC += local381;
                            vB += local302;
                            vC += local383;
                            fogB = (int) ((float) fogB + local304);
                            fogC = (int) ((float) fogC + local385);
                            local113 += local306;
                            local120 += local387;
                            local134 += local308;
                            local141 += local389;
                            local155 += local310;
                            local162 += local391;
                            local172 += local312;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                            xA += local215;
                            xC += local320;
                            zA += local217;
                            zC += local379;
                            uA += local219;
                            uC += local381;
                            vA += local221;
                            vC += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local385);
                            local106 += local225;
                            local120 += local387;
                            local127 += local227;
                            local141 += local389;
                            local148 += local229;
                            local162 += local391;
                            local167 += local231;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                            xB += local239;
                            xC += local320;
                            zB += local298;
                            zC += local379;
                            uB += local300;
                            uC += local381;
                            vB += local302;
                            vC += local383;
                            fogB = (int) ((float) fogB + local304);
                            fogC = (int) ((float) fogC + local385);
                            local113 += local306;
                            local120 += local387;
                            local134 += local308;
                            local141 += local389;
                            local155 += local310;
                            local162 += local391;
                            local172 += local312;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    zB = zA;
                    uB = uA;
                    vB = vA;
                    fogB = fogA;
                    local113 = local106;
                    local134 = local127;
                    local155 = local148;
                    local172 = local167;
                    if (yA < 0.0F) {
                        xA -= local215 * yA;
                        xB -= local320 * yA;
                        zA -= local217 * yA;
                        zB -= local379 * yA;
                        uA -= local219 * yA;
                        uB -= local381 * yA;
                        vA -= local221 * yA;
                        vB -= local383 * yA;
                        fogA = (int) ((float) fogA - local223 * yA);
                        fogB = (int) ((float) fogB - local385 * yA);
                        local106 -= local225 * yA;
                        local113 -= local387 * yA;
                        local127 -= local225 * yA;
                        local134 -= local387 * yA;
                        local148 -= local225 * yA;
                        local155 -= local387 * yA;
                        local167 -= local225 * yA;
                        local172 -= local387 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local239 * yC;
                        zC -= local298 * yC;
                        uC -= local300 * yC;
                        vC -= local302 * yC;
                        fogC = (int) ((float) fogC - local304 * yC);
                        local120 -= local306 * yC;
                        local141 -= local308 * yC;
                        local162 -= local310 * yC;
                        local177 -= local312 * yC;
                        yC = 0.0F;
                    }
                    if (yA != yC && local320 < local215 || yA == yC && local239 > local215) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                            xA += local215;
                            xB += local320;
                            zA += local217;
                            zB += local379;
                            uA += local219;
                            uB += local381;
                            vA += local221;
                            vB += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogB = (int) ((float) fogB + local385);
                            local106 += local225;
                            local113 += local387;
                            local127 += local227;
                            local134 += local389;
                            local148 += local229;
                            local155 += local391;
                            local167 += local231;
                            local172 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                            xC += local239;
                            xA += local215;
                            zC += local298;
                            zA += local217;
                            uC += local300;
                            uA += local219;
                            vC += local302;
                            vA += local221;
                            fogC = (int) ((float) fogC + local304);
                            fogA = (int) ((float) fogA + local223);
                            local120 += local306;
                            local106 += local225;
                            local141 += local308;
                            local127 += local227;
                            local162 += local310;
                            local148 += local229;
                            local177 += local312;
                            local167 += local231;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                            xB += local320;
                            xA += local215;
                            zB += local379;
                            zA += local217;
                            uB += local381;
                            uA += local219;
                            vB += local383;
                            vA += local221;
                            fogB = (int) ((float) fogB + local385);
                            fogA = (int) ((float) fogA + local223);
                            local113 += local387;
                            local106 += local225;
                            local134 += local389;
                            local127 += local227;
                            local155 += local391;
                            local148 += local229;
                            local172 += local393;
                            local167 += local231;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yA, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                            xA += local215;
                            xC += local239;
                            zA += local217;
                            zC += local298;
                            uA += local219;
                            uC += local300;
                            vA += local221;
                            vC += local302;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local304);
                            local106 += local225;
                            local120 += local306;
                            local127 += local227;
                            local141 += local308;
                            local148 += local229;
                            local162 += local310;
                            local167 += local231;
                            local177 += local312;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                if (yC < yA) {
                    xA = xB;
                    zA = zB;
                    uA = uB;
                    vA = vB;
                    fogA = fogB;
                    local106 = local113;
                    local127 = local134;
                    local148 = local155;
                    local167 = local172;
                    if (yB < 0.0F) {
                        xA = xB - local215 * yB;
                        xB -= local239 * yB;
                        zA = zB - local217 * yB;
                        zB -= local298 * yB;
                        uA = uB - local219 * yB;
                        uB -= local300 * yB;
                        vA = vB - local221 * yB;
                        vB -= local302 * yB;
                        fogA = (int) ((float) fogB - local223 * yB);
                        fogB = (int) ((float) fogB - local304 * yB);
                        local106 = local113 - local225 * yB;
                        local113 -= local306 * yB;
                        local127 = local134 - local227 * yB;
                        local134 -= local308 * yB;
                        local148 = local155 - local229 * yB;
                        local155 -= local310 * yB;
                        local167 = local172 - local231 * yB;
                        local172 -= local312 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local320 * yC;
                        zC -= local379 * yC;
                        uC -= local381 * yC;
                        vC -= local383 * yC;
                        fogC = (int) ((float) fogC - local385 * yC);
                        local120 -= local387 * yC;
                        local141 -= local389 * yC;
                        local162 -= local391 * yC;
                        local177 -= local393 * yC;
                        yC = 0.0F;
                    }
                    if ((yB == yC || !(local215 < local239)) && (yB != yC || !(local215 > local320))) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                            xB += local239;
                            xA += local215;
                            zB += local298;
                            zA += local217;
                            uB += local300;
                            uA += local219;
                            vB += local302;
                            vA += local221;
                            fogB = (int) ((float) fogB + local304);
                            fogA = (int) ((float) fogA + local223);
                            local113 += local306;
                            local106 += local225;
                            local134 += local308;
                            local127 += local227;
                            local155 += local310;
                            local148 += local229;
                            local172 += local312;
                            local167 += local231;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                            xC += local320;
                            xA += local215;
                            zC += local379;
                            zA += local217;
                            uC += local381;
                            uA += local219;
                            vC += local383;
                            vA += local221;
                            fogC = (int) ((float) fogC + local385);
                            fogA = (int) ((float) fogA + local223);
                            local120 += local387;
                            local106 += local225;
                            local141 += local389;
                            local127 += local227;
                            local162 += local391;
                            local148 += local229;
                            local177 += local393;
                            local167 += local231;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                            xA += local215;
                            xB += local239;
                            zA += local217;
                            zB += local298;
                            uA += local219;
                            uB += local300;
                            vA += local221;
                            vB += local302;
                            fogA = (int) ((float) fogA + local223);
                            fogB = (int) ((float) fogB + local304);
                            local106 += local225;
                            local113 += local306;
                            local127 += local227;
                            local134 += local308;
                            local148 += local229;
                            local155 += local310;
                            local167 += local231;
                            local172 += local312;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                            xA += local215;
                            xC += local320;
                            zA += local217;
                            zC += local379;
                            uA += local219;
                            uC += local381;
                            vA += local221;
                            vC += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local385);
                            local106 += local225;
                            local120 += local387;
                            local127 += local227;
                            local141 += local389;
                            local148 += local229;
                            local162 += local391;
                            local167 += local231;
                            local177 += local393;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    zC = zB;
                    uC = uB;
                    vC = vB;
                    fogC = fogB;
                    local120 = local113;
                    local141 = local134;
                    local162 = local155;
                    local177 = local172;
                    if (yB < 0.0F) {
                        xC = xB - local215 * yB;
                        xB -= local239 * yB;
                        zC = zB - local217 * yB;
                        zB -= local298 * yB;
                        uC = uB - local219 * yB;
                        uB -= local300 * yB;
                        vC = vB - local221 * yB;
                        vB -= local302 * yB;
                        fogC = (int) ((float) fogB - local223 * yB);
                        fogB = (int) ((float) fogB - local304 * yB);
                        local120 = local113 - local225 * yB;
                        local113 -= local306 * yB;
                        local141 = local134 - local227 * yB;
                        local134 -= local308 * yB;
                        local162 = local155 - local229 * yB;
                        local155 -= local310 * yB;
                        local177 = local172 - local231 * yB;
                        local172 -= local312 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local320 * yA;
                        zA -= local379 * yA;
                        uA -= local381 * yA;
                        vA -= local383 * yA;
                        fogA = (int) ((float) fogA - local385 * yA);
                        local106 -= local387 * yA;
                        local127 -= local389 * yA;
                        local148 -= local391 * yA;
                        local167 -= local393 * yA;
                        yA = 0.0F;
                    }
                    yC -= yA;
                    yA -= yB;
                    yB = (float) this.lineOffsets[(int) yB];
                    if (local215 < local239) {
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                            xC += local215;
                            xB += local239;
                            zC += local217;
                            zB += local298;
                            uC += local219;
                            uB += local300;
                            vC += local221;
                            vB += local302;
                            fogC = (int) ((float) fogC + local223);
                            fogB = (int) ((float) fogB + local304);
                            local120 += local225;
                            local113 += local306;
                            local141 += local227;
                            local134 += local308;
                            local162 += local229;
                            local155 += local310;
                            local177 += local231;
                            local172 += local312;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                            xA += local320;
                            xB += local239;
                            zA += local379;
                            zB += local298;
                            uA += local381;
                            uB += local300;
                            vA += local383;
                            vB += local302;
                            fogA = (int) ((float) fogA + local385);
                            fogB = (int) ((float) fogB + local304);
                            local106 += local387;
                            local113 += local306;
                            local127 += local389;
                            local134 += local308;
                            local148 += local391;
                            local155 += local310;
                            local167 += local393;
                            local172 += local312;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                            xB += local239;
                            xC += local215;
                            zB += local298;
                            zC += local217;
                            uB += local300;
                            uC += local219;
                            vB += local302;
                            vC += local221;
                            fogB = (int) ((float) fogB + local304);
                            fogC = (int) ((float) fogC + local223);
                            local113 += local306;
                            local120 += local225;
                            local134 += local308;
                            local141 += local227;
                            local155 += local310;
                            local162 += local229;
                            local172 += local312;
                            local177 += local231;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanRgb(this.raster, this.texels, (int) yB, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                            xB += local239;
                            xA += local320;
                            zB += local298;
                            zA += local379;
                            uB += local300;
                            uA += local381;
                            vB += local302;
                            vA += local383;
                            fogB = (int) ((float) fogB + local304);
                            fogA = (int) ((float) fogA + local385);
                            local113 += local306;
                            local106 += local387;
                            local134 += local308;
                            local127 += local389;
                            local155 += local310;
                            local148 += local391;
                            local172 += local312;
                            local167 += local393;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            if (yA < yB) {
                xB = xC;
                zB = zC;
                uB = uC;
                vB = vC;
                fogB = fogC;
                local113 = local120;
                local134 = local141;
                local155 = local162;
                local172 = local177;
                if (yC < 0.0F) {
                    xC -= local320 * yC;
                    xB -= local239 * yC;
                    zC -= local379 * yC;
                    zB -= local298 * yC;
                    uC -= local381 * yC;
                    uB -= local300 * yC;
                    vC -= local383 * yC;
                    vB -= local302 * yC;
                    fogC = (int) ((float) fogC - local385 * 3.0F);
                    fogB = (int) ((float) fogB - local304 * yC);
                    local120 -= local387 * yC;
                    local113 -= local306 * yC;
                    local141 -= local389 * yC;
                    local134 -= local308 * yC;
                    local162 -= local391 * yC;
                    local155 -= local310 * yC;
                    local177 -= local393 * yC;
                    local172 -= local312 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local215 * yA;
                    zA -= local217 * yA;
                    uA -= local219 * yA;
                    vA -= local221 * yA;
                    fogA = (int) ((float) fogA - local223 * yA);
                    local106 -= local225 * yA;
                    local127 -= local227 * yA;
                    local148 -= local229 * yA;
                    local167 -= local231 * yA;
                    yA = 0.0F;
                }
                if (local239 < local320) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                        xB += local239;
                        xC += local320;
                        zB += local298;
                        zC += local379;
                        uB += local300;
                        uC += local381;
                        vB += local302;
                        vC += local383;
                        fogB = (int) ((float) fogB + local304);
                        fogC = (int) ((float) fogC + local385);
                        local113 += local306;
                        local120 += local387;
                        local134 += local308;
                        local141 += local389;
                        local155 += local310;
                        local162 += local391;
                        local172 += local312;
                        local177 += local393;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                        xB += local239;
                        xA += local215;
                        zB += local298;
                        zA += local217;
                        uB += local300;
                        uA += local219;
                        vB += local302;
                        vA += local221;
                        fogB = (int) ((float) fogB + local304);
                        fogA = (int) ((float) fogA + local223);
                        local113 += local306;
                        local106 += local225;
                        local134 += local308;
                        local127 += local227;
                        local155 += local310;
                        local148 += local229;
                        local172 += local312;
                        local167 += local231;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                        xC += local320;
                        xB += local239;
                        zC += local379;
                        zB += local298;
                        uC += local381;
                        uB += local300;
                        vC += local383;
                        vB += local302;
                        fogC = (int) ((float) fogC + local385);
                        fogB = (int) ((float) fogB + local304);
                        local120 += local387;
                        local113 += local306;
                        local141 += local389;
                        local134 += local308;
                        local162 += local391;
                        local155 += local310;
                        local177 += local393;
                        local172 += local312;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                        xA += local215;
                        xB += local239;
                        zA += local217;
                        zB += local298;
                        uA += local219;
                        uB += local300;
                        vA += local221;
                        vB += local302;
                        fogA = (int) ((float) fogA + local223);
                        fogB = (int) ((float) fogB + local304);
                        local106 += local225;
                        local113 += local306;
                        local127 += local227;
                        local134 += local308;
                        local148 += local229;
                        local155 += local310;
                        local167 += local231;
                        local172 += local312;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                zA = zC;
                uA = uC;
                vA = vC;
                fogA = fogC;
                local106 = local120;
                local127 = local141;
                local148 = local162;
                local167 = local177;
                if (yC < 0.0F) {
                    xC -= local320 * yC;
                    xA -= local239 * yC;
                    zC -= local379 * yC;
                    zA -= local298 * yC;
                    uC -= local381 * yC;
                    uA -= local300 * yC;
                    vC -= local383 * yC;
                    vA -= local302 * yC;
                    fogC = (int) ((float) fogC - local385 * 3.0F);
                    fogA = (int) ((float) fogA - local304 * yC);
                    local120 -= local387 * yC;
                    local106 -= local306 * yC;
                    local141 -= local389 * yC;
                    local127 -= local308 * yC;
                    local162 -= local391 * yC;
                    local148 -= local310 * yC;
                    local177 -= local393 * yC;
                    local167 -= local312 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local215 * yB;
                    zB -= local217 * yB;
                    uB -= local219 * yB;
                    vB -= local221 * yB;
                    fogB = (int) ((float) fogB - local223 * yB);
                    local113 -= local225 * yB;
                    local134 -= local227 * yB;
                    local155 -= local229 * yB;
                    local172 -= local231 * yB;
                    yB = 0.0F;
                }
                if (local239 < local320) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                        xA += local239;
                        xC += local320;
                        zA += local298;
                        zC += local379;
                        uA += local300;
                        uC += local381;
                        vA += local302;
                        vC += local383;
                        fogA = (int) ((float) fogA + local304);
                        fogC = (int) ((float) fogC + local385);
                        local106 += local306;
                        local120 += local387;
                        local127 += local308;
                        local141 += local389;
                        local148 += local310;
                        local162 += local391;
                        local167 += local312;
                        local177 += local393;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                        xB += local215;
                        xC += local320;
                        zB += local217;
                        zC += local379;
                        uB += local219;
                        uC += local381;
                        vB += local221;
                        vC += local383;
                        fogB = (int) ((float) fogB + local223);
                        fogC = (int) ((float) fogC + local385);
                        local113 += local225;
                        local120 += local387;
                        local134 += local227;
                        local141 += local389;
                        local155 += local229;
                        local162 += local391;
                        local172 += local231;
                        local177 += local393;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                        xC += local320;
                        xA += local239;
                        zC += local379;
                        zA += local298;
                        uC += local381;
                        uA += local300;
                        vC += local383;
                        vA += local302;
                        fogC = (int) ((float) fogC + local385);
                        fogA = (int) ((float) fogA + local304);
                        local120 += local387;
                        local106 += local306;
                        local141 += local389;
                        local127 += local308;
                        local162 += local391;
                        local148 += local310;
                        local177 += local393;
                        local167 += local312;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanRgb(this.raster, this.texels, (int) yC, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                        xC += local320;
                        xB += local215;
                        zC += local379;
                        zB += local217;
                        uC += local381;
                        uB += local219;
                        vC += local383;
                        vB += local221;
                        fogC = (int) ((float) fogC + local385);
                        fogB = (int) ((float) fogB + local223);
                        local120 += local387;
                        local113 += local225;
                        local141 += local389;
                        local134 += local227;
                        local162 += local391;
                        local155 += local229;
                        local177 += local393;
                        local172 += local231;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "([I[FIIIIIFFFF)V")
    public void drawHslSpanRgb(@OriginalArg(0) int[] dst, @OriginalArg(1) float[] depths, @OriginalArg(2) int rowOffset, @OriginalArg(5) int startX, @OriginalArg(6) int endX, @OriginalArg(7) float hsl, @OriginalArg(8) float hslStep, @OriginalArg(9) float z, @OriginalArg(10) float zStep) {
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        rowOffset += startX - 1;
        hsl += hslStep * (float) startX;
        z += zStep * (float) startX;
        @Pc(186) int local186;
        @Pc(191) int local191;
        @Pc(233) int local233;
        @Pc(50) int local50;
        @Pc(64) int local64;
        @Pc(71) int local71;
        @Pc(54) float local54;
        @Pc(87) float local87;
        if (this.threadResource.zWrite) {
            if (this.fastScanline) {
                local50 = endX - startX >> 2;
                local54 = hslStep * 4.0F;
                if (this.alpha == 0) {
                    if (local50 > 0) {
                        do {
                            local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                            hsl += local54;
                            local71 = rowOffset + 1;
                            if (z < depths[local71]) {
                                dst[local71] = local64;
                                depths[local71] = z;
                            }
                            local87 = z + zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                dst[local71] = local64;
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                dst[local71] = local64;
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            rowOffset = local71 + 1;
                            if (local87 < depths[rowOffset]) {
                                dst[rowOffset] = local64;
                                depths[rowOffset] = local87;
                            }
                            z = local87 + zStep;
                            local50--;
                        } while (local50 > 0);
                    }
                    local50 = endX - startX & 0x3;
                    if (local50 > 0) {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset] = local64;
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local50--;
                        } while (local50 > 0);
                        return;
                    }
                } else {
                    local186 = this.alpha;
                    local191 = 256 - this.alpha;
                    if (local50 > 0) {
                        do {
                            local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                            hsl += local54;
                            local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                            local71 = rowOffset + 1;
                            if (z < depths[local71]) {
                                local233 = dst[local71];
                                dst[local71] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[local71] = z;
                            }
                            local87 = z + zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                local233 = dst[local71];
                                dst[local71] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                local233 = dst[local71];
                                dst[local71] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            rowOffset = local71 + 1;
                            if (local87 < depths[rowOffset]) {
                                local233 = dst[rowOffset];
                                dst[rowOffset] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[rowOffset] = local87;
                            }
                            z = local87 + zStep;
                            local50--;
                        } while (local50 > 0);
                    }
                    local50 = endX - startX & 0x3;
                    if (local50 <= 0) {
                        return;
                    }
                    local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                    local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local233 = dst[rowOffset];
                            dst[rowOffset] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        local50--;
                    } while (local50 > 0);
                }
            } else {
                local50 = endX - startX;
                if (this.alpha == 0) {
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = ColourUtils.HSV_TO_RGB[(int) hsl];
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        hsl += hslStep;
                        local50--;
                    } while (local50 > 0);
                } else {
                    local186 = this.alpha;
                    local191 = 256 - this.alpha;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                            local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                            local233 = dst[rowOffset];
                            dst[rowOffset] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                            depths[rowOffset] = z;
                        }
                        hsl += hslStep;
                        z += zStep;
                        local50--;
                    } while (local50 > 0);
                }
            }
        } else if (this.fastScanline) {
            local50 = endX - startX >> 2;
            local54 = hslStep * 4.0F;
            if (this.alpha == 0) {
                if (local50 > 0) {
                    do {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        hsl += local54;
                        local71 = rowOffset + 1;
                        if (z < depths[local71]) {
                            dst[local71] = local64;
                        }
                        local87 = z + zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            dst[local71] = local64;
                        }
                        local87 += zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            dst[local71] = local64;
                        }
                        local87 += zStep;
                        rowOffset = local71 + 1;
                        if (local87 < depths[rowOffset]) {
                            dst[rowOffset] = local64;
                        }
                        z = local87 + zStep;
                        local50--;
                    } while (local50 > 0);
                }
                local50 = endX - startX & 0x3;
                if (local50 > 0) {
                    local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = local64;
                        }
                        z += zStep;
                        local50--;
                    } while (local50 > 0);
                    return;
                }
            } else {
                local186 = this.alpha;
                local191 = 256 - this.alpha;
                if (local50 > 0) {
                    do {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        hsl += local54;
                        local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                        local71 = rowOffset + 1;
                        if (z < depths[local71]) {
                            local233 = dst[local71];
                            dst[local71] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        local87 = z + zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            local233 = dst[local71];
                            dst[local71] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        local87 += zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            local233 = dst[local71];
                            dst[local71] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        local87 += zStep;
                        rowOffset = local71 + 1;
                        if (local87 < depths[rowOffset]) {
                            local233 = dst[rowOffset];
                            dst[rowOffset] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        z = local87 + zStep;
                        local50--;
                    } while (local50 > 0);
                }
                local50 = endX - startX & 0x3;
                if (local50 <= 0) {
                    return;
                }
                local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local233 = dst[rowOffset];
                        dst[rowOffset] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                    }
                    z += zStep;
                    local50--;
                } while (local50 > 0);
            }
        } else {
            local50 = endX - startX;
            if (this.alpha == 0) {
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        dst[rowOffset] = ColourUtils.HSV_TO_RGB[(int) hsl];
                    }
                    z += zStep;
                    hsl += hslStep;
                    local50--;
                } while (local50 > 0);
            } else {
                local186 = this.alpha;
                local191 = 256 - this.alpha;
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                        local233 = dst[rowOffset];
                        dst[rowOffset] = local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                    }
                    hsl += hslStep;
                    z += zStep;
                    local50--;
                } while (local50 > 0);
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "([I[FIIIIIFF)V")
    public void drawFlatSpanArgb(@OriginalArg(0) int[] dst, @OriginalArg(1) float[] depths, @OriginalArg(2) int rowOffset, @OriginalArg(3) int colour, @OriginalArg(5) int startX, @OriginalArg(6) int endX, @OriginalArg(7) float z, @OriginalArg(8) float zStep) {
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        rowOffset += startX - 1;
        @Pc(29) int local29 = endX - startX >> 2;
        z += zStep * (float) startX;
        @Pc(278) int local278;
        @Pc(283) int local283;
        @Pc(315) int local315;
        @Pc(47) int local47;
        @Pc(303) int local303;
        @Pc(63) float local63;
        if (this.threadResource.zWrite) {
            if (this.alpha == 0) {
                while (true) {
                    local29--;
                    if (local29 < 0) {
                        local29 = endX - startX & 0x3;
                        while (true) {
                            local29--;
                            if (local29 < 0) {
                                return;
                            }
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset] = colour;
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                        }
                    }
                    local47 = rowOffset + 1;
                    if (z < depths[local47]) {
                        dst[local47] = colour;
                        depths[local47] = z;
                    }
                    local63 = z + zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47] = colour;
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47] = colour;
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    rowOffset = local47 + 1;
                    if (local63 < depths[rowOffset]) {
                        dst[rowOffset] = colour;
                        depths[rowOffset] = local63;
                    }
                    z = local63 + zStep;
                }
            } else if (this.alpha != 254) {
                local278 = this.alpha;
                local283 = 256 - this.alpha;
                local303 = ((colour & 0xFF00FF) * local283 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local283 >> 8 & 0xFF00);
                while (true) {
                    local29--;
                    if (local29 < 0) {
                        local29 = endX - startX & 0x3;
                        while (true) {
                            local29--;
                            if (local29 < 0) {
                                return;
                            }
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                local315 = dst[rowOffset];
                                dst[rowOffset] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                        }
                    }
                    local47 = rowOffset + 1;
                    if (z < depths[local47]) {
                        local315 = dst[local47];
                        dst[local47] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[local47] = z;
                    }
                    local63 = z + zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        local315 = dst[local47];
                        dst[local47] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        local315 = dst[local47];
                        dst[local47] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    rowOffset = local47 + 1;
                    if (local63 < depths[rowOffset]) {
                        local315 = dst[rowOffset];
                        dst[rowOffset] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[rowOffset] = local63;
                    }
                    z = local63 + zStep;
                }
            } else if (startX != 0 && endX <= this.width - 1) {
                while (true) {
                    local29--;
                    if (local29 < 0) {
                        local29 = endX - startX & 0x3;
                        while (true) {
                            local29--;
                            if (local29 < 0) {
                                return;
                            }
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset - 1] = dst[rowOffset];
                            }
                            z += zStep;
                        }
                    }
                    local47 = rowOffset + 1;
                    if (z < depths[local47]) {
                        dst[local47 - 1] = dst[local47];
                    }
                    local63 = z + zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47 - 1] = dst[local47];
                    }
                    local63 += zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47 - 1] = dst[local47];
                    }
                    local63 += zStep;
                    rowOffset = local47 + 1;
                    if (local63 < depths[rowOffset]) {
                        dst[rowOffset - 1] = dst[rowOffset];
                    }
                    z = local63 + zStep;
                }
            }
        } else if (this.alpha == 0) {
            while (true) {
                local29--;
                if (local29 < 0) {
                    local29 = endX - startX & 0x3;
                    while (true) {
                        local29--;
                        if (local29 < 0) {
                            return;
                        }
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = colour;
                        }
                        z += zStep;
                    }
                }
                local47 = rowOffset + 1;
                if (z < depths[local47]) {
                    dst[local47] = colour;
                }
                local63 = z + zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47] = colour;
                }
                local63 += zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47] = colour;
                }
                local63 += zStep;
                rowOffset = local47 + 1;
                if (local63 < depths[rowOffset]) {
                    dst[rowOffset] = colour;
                }
                z = local63 + zStep;
            }
        } else if (this.alpha != 254) {
            local278 = this.alpha;
            local283 = 256 - this.alpha;
            local303 = ((colour & 0xFF00FF) * local283 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local283 >> 8 & 0xFF00);
            while (true) {
                local29--;
                if (local29 < 0) {
                    local29 = endX - startX & 0x3;
                    while (true) {
                        local29--;
                        if (local29 < 0) {
                            return;
                        }
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local315 = dst[rowOffset];
                            dst[rowOffset] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        }
                        z += zStep;
                    }
                }
                local47 = rowOffset + 1;
                if (z < depths[local47]) {
                    local315 = dst[local47];
                    dst[local47] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                local63 = z + zStep;
                local47++;
                if (local63 < depths[local47]) {
                    local315 = dst[local47];
                    dst[local47] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                local63 += zStep;
                local47++;
                if (local63 < depths[local47]) {
                    local315 = dst[local47];
                    dst[local47] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                local63 += zStep;
                rowOffset = local47 + 1;
                if (local63 < depths[rowOffset]) {
                    local315 = dst[rowOffset];
                    dst[rowOffset] = (local283 | local315 >> 24) << 24 | local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                z = local63 + zStep;
            }
        } else if (startX != 0 && endX <= this.width - 1) {
            while (true) {
                local29--;
                if (local29 < 0) {
                    local29 = endX - startX & 0x3;
                    while (true) {
                        local29--;
                        if (local29 < 0) {
                            return;
                        }
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset - 1] = dst[rowOffset];
                        }
                        z += zStep;
                    }
                }
                local47 = rowOffset + 1;
                if (z < depths[local47]) {
                    dst[local47 - 1] = dst[local47];
                }
                local63 = z + zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47 - 1] = dst[local47];
                }
                local63 += zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47 - 1] = dst[local47];
                }
                local63 += zStep;
                rowOffset = local47 + 1;
                if (local63 < depths[rowOffset]) {
                    dst[rowOffset - 1] = dst[rowOffset];
                }
                z = local63 + zStep;
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "(FFFFFFFFFFFF)V")
    public void renderTriangleHslArgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) float hslA, @OriginalArg(10) float hslB, @OriginalArg(11) float hslC) {
        if (this.wireframe) {
            this.toolkit.line((int) yB, (int) yA, (int) xB, ColourUtils.HSV_TO_RGB[(int) hslA], (int) xA);
            this.toolkit.line((int) yC, (int) yB, (int) xC, ColourUtils.HSV_TO_RGB[(int) hslA], (int) xB);
            this.toolkit.line((int) yA, (int) yC, (int) xA, ColourUtils.HSV_TO_RGB[(int) hslA], (int) xC);
            return;
        }
        @Pc(55) float local55 = xB - xA;
        @Pc(59) float local59 = yB - yA;
        @Pc(63) float local63 = xC - xA;
        @Pc(67) float local67 = yC - yA;
        @Pc(71) float local71 = hslB - hslA;
        @Pc(75) float local75 = hslC - hslA;
        @Pc(79) float local79 = zB - zA;
        @Pc(83) float local83 = zC - zA;
        @Pc(95) float local95;
        if (yC == yB) {
            local95 = 0.0F;
        } else {
            local95 = (xC - xB) / (yC - yB);
        }
        @Pc(106) float local106;
        if (yB == yA) {
            local106 = 0.0F;
        } else {
            local106 = local55 / local59;
        }
        @Pc(117) float local117;
        if (yC == yA) {
            local117 = 0.0F;
        } else {
            local117 = local63 / local67;
        }
        @Pc(128) float local128 = local55 * local67 - local63 * local59;
        if (local128 == 0.0F) {
            return;
        }
        @Pc(143) float local143 = (local71 * local67 - local75 * local59) / local128;
        @Pc(153) float local153 = (local75 * local55 - local71 * local63) / local128;
        @Pc(163) float local163 = (local79 * local67 - local83 * local59) / local128;
        @Pc(173) float local173 = (local83 * local55 - local79 * local63) / local128;
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                hslA = hslA - local143 * xA + local143;
                zA = zA - local163 * xA + local163;
                if (yB < yC) {
                    xC = xA;
                    if (yA < 0.0F) {
                        xC = xA - local117 * yA;
                        xA -= local106 * yA;
                        hslA -= local153 * yA;
                        zA -= local173 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local95 * yB;
                        yB = 0.0F;
                    }
                    if ((yA == yB || !(local117 < local106)) && (yA != yB || !(local117 > local95))) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, hslA, local143, zA, local163);
                            xC += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xC, hslA, local143, zA, local163);
                            xC += local117;
                            xB += local95;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, hslA, local143, zA, local163);
                            xC += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xB, hslA, local143, zA, local163);
                            xC += local117;
                            xB += local95;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    if (yA < 0.0F) {
                        xB = xA - local117 * yA;
                        xA -= local106 * yA;
                        hslA -= local153 * yA;
                        zA -= local173 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local95 * yC;
                        yC = 0.0F;
                    }
                    if (yA != yC && local117 < local106 || yA == yC && local95 > local106) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xA, hslA, local143, zA, local163);
                            xB += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, hslA, local143, zA, local163);
                            xC += local95;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xB, hslA, local143, zA, local163);
                            xB += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, hslA, local143, zA, local163);
                            xC += local95;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                hslB = hslB - local143 * xB + local143;
                zB = zB - local163 * xB + local163;
                if (yC < yA) {
                    xA = xB;
                    if (yB < 0.0F) {
                        xA = xB - local106 * yB;
                        xB -= local95 * yB;
                        hslB -= local153 * yB;
                        zB -= local173 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local117 * yC;
                        yC = 0.0F;
                    }
                    if ((yB == yC || !(local106 < local95)) && (yB != yC || !(local106 > local117))) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, hslB, local143, zB, local163);
                            xA += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xA, hslB, local143, zB, local163);
                            xA += local106;
                            xC += local117;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, hslB, local143, zB, local163);
                            xA += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xC, hslB, local143, zB, local163);
                            xA += local106;
                            xC += local117;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    if (yB < 0.0F) {
                        xC = xB - local106 * yB;
                        xB -= local95 * yB;
                        hslB -= local153 * yB;
                        zB -= local173 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local117 * yA;
                        yA = 0.0F;
                    }
                    if (local106 < local95) {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xB, hslB, local143, zB, local163);
                            xC += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, hslB, local143, zB, local163);
                            xA += local117;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xC, hslB, local143, zB, local163);
                            xC += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, hslB, local143, zB, local163);
                            xA += local117;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            hslC = hslC - local143 * xC + local143;
            zC = zC - local163 * xC + local163;
            if (yA < yB) {
                xB = xC;
                if (yC < 0.0F) {
                    xB = xC - local95 * yC;
                    xC -= local117 * yC;
                    hslC -= local153 * yC;
                    zC -= local173 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local106 * yA;
                    yA = 0.0F;
                }
                if (local95 < local117) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, hslC, local143, zC, local163);
                        xB += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xA, hslC, local143, zC, local163);
                        xB += local95;
                        xA += local106;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, hslC, local143, zC, local163);
                        xB += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xB, hslC, local143, zC, local163);
                        xB += local95;
                        xA += local106;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                if (yC < 0.0F) {
                    xA = xC - local95 * yC;
                    xC -= local117 * yC;
                    hslC -= local153 * yC;
                    zC -= local173 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local106 * yB;
                    yB = 0.0F;
                }
                if (local95 < local117) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xC, hslC, local143, zC, local163);
                        xA += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, hslC, local143, zC, local163);
                        xB += local106;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xA, hslC, local143, zC, local163);
                        xA += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawHslSpanArgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, hslC, local143, zC, local163);
                        xB += local106;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "(FFFFFFFFFFFFFFFIIIIIIII)V")
    public void renderTexturedTriangleArgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) float uA, @OriginalArg(10) float uB, @OriginalArg(11) float uC, @OriginalArg(12) float vA, @OriginalArg(13) float vB, @OriginalArg(14) float vC, @OriginalArg(15) int colourA, @OriginalArg(16) int colourB, @OriginalArg(17) int colourC, @OriginalArg(18) int fogColour, @OriginalArg(19) int fogA, @OriginalArg(20) int fogB, @OriginalArg(21) int fogC, @OriginalArg(22) int texture) {
        if (texture != this.textureId) {
            this.texels = this.toolkit.getArgbTexture(texture);
            if (this.texels == null) {
                this.renderTriangleArgb((float) (int) yA, (float) (int) yB, (float) (int) yC, (float) (int) xA, (float) (int) xB, (float) (int) xC, (float) (int) zA, (float) (int) zB, (float) (int) zC, Static462.blendArgb(fogColour | fogA << 24, colourA), Static462.blendArgb(fogColour | fogB << 24, colourB), Static462.blendArgb(fogColour | fogC << 24, colourC));
                return;
            }
            this.textureSize = this.toolkit.smallTexture(texture) ? 64 : this.toolkit.textureSize;
            this.textureMask = this.textureSize - 1;
            this.textureBlendMode = this.toolkit.textureAlphaBlendMode(texture);
            this.textureRepeats = this.toolkit.textureRepeats(texture);
        }
        this.fogColour = fogColour;
        @Pc(106) float local106 = (float) (colourA >> 24 & 0xFF);
        @Pc(113) float local113 = (float) (colourB >> 24 & 0xFF);
        @Pc(120) float local120 = (float) (colourC >> 24 & 0xFF);
        @Pc(127) float local127 = (float) (colourA >> 16 & 0xFF);
        @Pc(134) float local134 = (float) (colourB >> 16 & 0xFF);
        @Pc(141) float local141 = (float) (colourC >> 16 & 0xFF);
        @Pc(148) float local148 = (float) (colourA >> 8 & 0xFF);
        @Pc(155) float local155 = (float) (colourB >> 8 & 0xFF);
        @Pc(162) float local162 = (float) (colourC >> 8 & 0xFF);
        @Pc(167) float local167 = (float) (colourA & 0xFF);
        @Pc(172) float local172 = (float) (colourB & 0xFF);
        @Pc(177) float local177 = (float) (colourC & 0xFF);
        uA /= zA;
        uB /= zB;
        uC /= zC;
        vA /= zA;
        vB /= zB;
        vC /= zC;
        zA = 1.0F / zA;
        zB = 1.0F / zB;
        zC = 1.0F / zC;
        @Pc(215) float local215 = 0.0F;
        @Pc(217) float local217 = 0.0F;
        @Pc(219) float local219 = 0.0F;
        @Pc(221) float local221 = 0.0F;
        @Pc(223) float local223 = 0.0F;
        @Pc(225) float local225 = 0.0F;
        @Pc(227) float local227 = 0.0F;
        @Pc(229) float local229 = 0.0F;
        @Pc(231) float local231 = 0.0F;
        @Pc(239) float local239;
        if (yB != yA) {
            local239 = yB - yA;
            local215 = (xB - xA) / local239;
            local217 = (zB - zA) / local239;
            local219 = (uB - uA) / local239;
            local221 = (vB - vA) / local239;
            local223 = (float) (fogB - fogA) / local239;
            local225 = (local113 - local106) / local239;
            local227 = (local134 - local127) / local239;
            local229 = (local155 - local148) / local239;
            local231 = (local172 - local167) / local239;
        }
        local239 = 0.0F;
        @Pc(298) float local298 = 0.0F;
        @Pc(300) float local300 = 0.0F;
        @Pc(302) float local302 = 0.0F;
        @Pc(304) float local304 = 0.0F;
        @Pc(306) float local306 = 0.0F;
        @Pc(308) float local308 = 0.0F;
        @Pc(310) float local310 = 0.0F;
        @Pc(312) float local312 = 0.0F;
        @Pc(320) float local320;
        if (yC != yB) {
            local320 = yC - yB;
            local239 = (xC - xB) / local320;
            local298 = (zC - zB) / local320;
            local300 = (uC - uB) / local320;
            local302 = (vC - vB) / local320;
            local304 = (float) (fogC - fogB) / local320;
            local306 = (local120 - local113) / local320;
            local308 = (local141 - local134) / local320;
            local310 = (local162 - local155) / local320;
            local312 = (local177 - local172) / local320;
        }
        local320 = 0.0F;
        @Pc(379) float local379 = 0.0F;
        @Pc(381) float local381 = 0.0F;
        @Pc(383) float local383 = 0.0F;
        @Pc(385) float local385 = 0.0F;
        @Pc(387) float local387 = 0.0F;
        @Pc(389) float local389 = 0.0F;
        @Pc(391) float local391 = 0.0F;
        @Pc(393) float local393 = 0.0F;
        if (yA != yC) {
            @Pc(401) float local401 = yA - yC;
            local320 = (xA - xC) / local401;
            local379 = (zA - zC) / local401;
            local381 = (uA - uC) / local401;
            local383 = (vA - vC) / local401;
            local385 = (float) (fogA - fogC) / local401;
            local387 = (local106 - local120) / local401;
            local389 = (local127 - local141) / local401;
            local391 = (local148 - local162) / local401;
            local393 = (local167 - local177) / local401;
        }
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yB < yC) {
                    xC = xA;
                    zC = zA;
                    uC = uA;
                    vC = vA;
                    fogC = fogA;
                    local120 = local106;
                    local141 = local127;
                    local162 = local148;
                    local177 = local167;
                    if (yA < 0.0F) {
                        xA -= local215 * yA;
                        xC -= local320 * yA;
                        zA -= local217 * yA;
                        zC -= local379 * yA;
                        uA -= local219 * yA;
                        uC -= local381 * yA;
                        vA -= local221 * yA;
                        vC -= local383 * yA;
                        fogA = (int) ((float) fogA - local223 * yA);
                        fogC = (int) ((float) fogC - local385 * yA);
                        local106 -= local225 * yA;
                        local120 -= local387 * yA;
                        local127 -= local225 * yA;
                        local141 -= local387 * yA;
                        local148 -= local225 * yA;
                        local162 -= local387 * yA;
                        local167 -= local225 * yA;
                        local177 -= local387 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local239 * yB;
                        zB -= local298 * yB;
                        uB -= local300 * yB;
                        vB -= local302 * yB;
                        fogB = (int) ((float) fogB - local304 * yB);
                        local113 -= local306 * yB;
                        local134 -= local308 * yB;
                        local155 -= local310 * yB;
                        local172 -= local312 * yB;
                        yB = 0.0F;
                    }
                    if (yA != yB && local320 < local215 || yA == yB && local320 > local239) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                            xA += local215;
                            xC += local320;
                            zA += local217;
                            zC += local379;
                            uA += local219;
                            uC += local381;
                            vA += local221;
                            vC += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local385);
                            local106 += local225;
                            local120 += local387;
                            local127 += local227;
                            local141 += local389;
                            local148 += local229;
                            local162 += local391;
                            local167 += local231;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                            xB += local239;
                            xC += local320;
                            zB += local298;
                            zC += local379;
                            uB += local300;
                            uC += local381;
                            vB += local302;
                            vC += local383;
                            fogB = (int) ((float) fogB + local304);
                            fogC = (int) ((float) fogC + local385);
                            local113 += local306;
                            local120 += local387;
                            local134 += local308;
                            local141 += local389;
                            local155 += local310;
                            local162 += local391;
                            local172 += local312;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                            xA += local215;
                            xC += local320;
                            zA += local217;
                            zC += local379;
                            uA += local219;
                            uC += local381;
                            vA += local221;
                            vC += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local385);
                            local106 += local225;
                            local120 += local387;
                            local127 += local227;
                            local141 += local389;
                            local148 += local229;
                            local162 += local391;
                            local167 += local231;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                            xB += local239;
                            xC += local320;
                            zB += local298;
                            zC += local379;
                            uB += local300;
                            uC += local381;
                            vB += local302;
                            vC += local383;
                            fogB = (int) ((float) fogB + local304);
                            fogC = (int) ((float) fogC + local385);
                            local113 += local306;
                            local120 += local387;
                            local134 += local308;
                            local141 += local389;
                            local155 += local310;
                            local162 += local391;
                            local172 += local312;
                            local177 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    zB = zA;
                    uB = uA;
                    vB = vA;
                    fogB = fogA;
                    local113 = local106;
                    local134 = local127;
                    local155 = local148;
                    local172 = local167;
                    if (yA < 0.0F) {
                        xA -= local215 * yA;
                        xB -= local320 * yA;
                        zA -= local217 * yA;
                        zB -= local379 * yA;
                        uA -= local219 * yA;
                        uB -= local381 * yA;
                        vA -= local221 * yA;
                        vB -= local383 * yA;
                        fogA = (int) ((float) fogA - local223 * yA);
                        fogB = (int) ((float) fogB - local385 * yA);
                        local106 -= local225 * yA;
                        local113 -= local387 * yA;
                        local127 -= local225 * yA;
                        local134 -= local387 * yA;
                        local148 -= local225 * yA;
                        local155 -= local387 * yA;
                        local167 -= local225 * yA;
                        local172 -= local387 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local239 * yC;
                        zC -= local298 * yC;
                        uC -= local300 * yC;
                        vC -= local302 * yC;
                        fogC = (int) ((float) fogC - local304 * yC);
                        local120 -= local306 * yC;
                        local141 -= local308 * yC;
                        local162 -= local310 * yC;
                        local177 -= local312 * yC;
                        yC = 0.0F;
                    }
                    if (yA != yC && local320 < local215 || yA == yC && local239 > local215) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                            xA += local215;
                            xB += local320;
                            zA += local217;
                            zB += local379;
                            uA += local219;
                            uB += local381;
                            vA += local221;
                            vB += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogB = (int) ((float) fogB + local385);
                            local106 += local225;
                            local113 += local387;
                            local127 += local227;
                            local134 += local389;
                            local148 += local229;
                            local155 += local391;
                            local167 += local231;
                            local172 += local393;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                            xC += local239;
                            xA += local215;
                            zC += local298;
                            zA += local217;
                            uC += local300;
                            uA += local219;
                            vC += local302;
                            vA += local221;
                            fogC = (int) ((float) fogC + local304);
                            fogA = (int) ((float) fogA + local223);
                            local120 += local306;
                            local106 += local225;
                            local141 += local308;
                            local127 += local227;
                            local162 += local310;
                            local148 += local229;
                            local177 += local312;
                            local167 += local231;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                            xB += local320;
                            xA += local215;
                            zB += local379;
                            zA += local217;
                            uB += local381;
                            uA += local219;
                            vB += local383;
                            vA += local221;
                            fogB = (int) ((float) fogB + local385);
                            fogA = (int) ((float) fogA + local223);
                            local113 += local387;
                            local106 += local225;
                            local134 += local389;
                            local127 += local227;
                            local155 += local391;
                            local148 += local229;
                            local172 += local393;
                            local167 += local231;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yA, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                            xA += local215;
                            xC += local239;
                            zA += local217;
                            zC += local298;
                            uA += local219;
                            uC += local300;
                            vA += local221;
                            vC += local302;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local304);
                            local106 += local225;
                            local120 += local306;
                            local127 += local227;
                            local141 += local308;
                            local148 += local229;
                            local162 += local310;
                            local167 += local231;
                            local177 += local312;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                if (yC < yA) {
                    xA = xB;
                    zA = zB;
                    uA = uB;
                    vA = vB;
                    fogA = fogB;
                    local106 = local113;
                    local127 = local134;
                    local148 = local155;
                    local167 = local172;
                    if (yB < 0.0F) {
                        xA = xB - local215 * yB;
                        xB -= local239 * yB;
                        zA = zB - local217 * yB;
                        zB -= local298 * yB;
                        uA = uB - local219 * yB;
                        uB -= local300 * yB;
                        vA = vB - local221 * yB;
                        vB -= local302 * yB;
                        fogA = (int) ((float) fogB - local223 * yB);
                        fogB = (int) ((float) fogB - local304 * yB);
                        local106 = local113 - local225 * yB;
                        local113 -= local306 * yB;
                        local127 = local134 - local227 * yB;
                        local134 -= local308 * yB;
                        local148 = local155 - local229 * yB;
                        local155 -= local310 * yB;
                        local167 = local172 - local231 * yB;
                        local172 -= local312 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local320 * yC;
                        zC -= local379 * yC;
                        uC -= local381 * yC;
                        vC -= local383 * yC;
                        fogC = (int) ((float) fogC - local385 * yC);
                        local120 -= local387 * yC;
                        local141 -= local389 * yC;
                        local162 -= local391 * yC;
                        local177 -= local393 * yC;
                        yC = 0.0F;
                    }
                    if ((yB == yC || !(local215 < local239)) && (yB != yC || !(local215 > local320))) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                            xB += local239;
                            xA += local215;
                            zB += local298;
                            zA += local217;
                            uB += local300;
                            uA += local219;
                            vB += local302;
                            vA += local221;
                            fogB = (int) ((float) fogB + local304);
                            fogA = (int) ((float) fogA + local223);
                            local113 += local306;
                            local106 += local225;
                            local134 += local308;
                            local127 += local227;
                            local155 += local310;
                            local148 += local229;
                            local172 += local312;
                            local167 += local231;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                            xC += local320;
                            xA += local215;
                            zC += local379;
                            zA += local217;
                            uC += local381;
                            uA += local219;
                            vC += local383;
                            vA += local221;
                            fogC = (int) ((float) fogC + local385);
                            fogA = (int) ((float) fogA + local223);
                            local120 += local387;
                            local106 += local225;
                            local141 += local389;
                            local127 += local227;
                            local162 += local391;
                            local148 += local229;
                            local177 += local393;
                            local167 += local231;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                            xA += local215;
                            xB += local239;
                            zA += local217;
                            zB += local298;
                            uA += local219;
                            uB += local300;
                            vA += local221;
                            vB += local302;
                            fogA = (int) ((float) fogA + local223);
                            fogB = (int) ((float) fogB + local304);
                            local106 += local225;
                            local113 += local306;
                            local127 += local227;
                            local134 += local308;
                            local148 += local229;
                            local155 += local310;
                            local167 += local231;
                            local172 += local312;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                            xA += local215;
                            xC += local320;
                            zA += local217;
                            zC += local379;
                            uA += local219;
                            uC += local381;
                            vA += local221;
                            vC += local383;
                            fogA = (int) ((float) fogA + local223);
                            fogC = (int) ((float) fogC + local385);
                            local106 += local225;
                            local120 += local387;
                            local127 += local227;
                            local141 += local389;
                            local148 += local229;
                            local162 += local391;
                            local167 += local231;
                            local177 += local393;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    zC = zB;
                    uC = uB;
                    vC = vB;
                    fogC = fogB;
                    local120 = local113;
                    local141 = local134;
                    local162 = local155;
                    local177 = local172;
                    if (yB < 0.0F) {
                        xC = xB - local215 * yB;
                        xB -= local239 * yB;
                        zC = zB - local217 * yB;
                        zB -= local298 * yB;
                        uC = uB - local219 * yB;
                        uB -= local300 * yB;
                        vC = vB - local221 * yB;
                        vB -= local302 * yB;
                        fogC = (int) ((float) fogB - local223 * yB);
                        fogB = (int) ((float) fogB - local304 * yB);
                        local120 = local113 - local225 * yB;
                        local113 -= local306 * yB;
                        local141 = local134 - local227 * yB;
                        local134 -= local308 * yB;
                        local162 = local155 - local229 * yB;
                        local155 -= local310 * yB;
                        local177 = local172 - local231 * yB;
                        local172 -= local312 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local320 * yA;
                        zA -= local379 * yA;
                        uA -= local381 * yA;
                        vA -= local383 * yA;
                        fogA = (int) ((float) fogA - local385 * yA);
                        local106 -= local387 * yA;
                        local127 -= local389 * yA;
                        local148 -= local391 * yA;
                        local167 -= local393 * yA;
                        yA = 0.0F;
                    }
                    yC -= yA;
                    yA -= yB;
                    yB = (float) this.lineOffsets[(int) yB];
                    if (local215 < local239) {
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                            xC += local215;
                            xB += local239;
                            zC += local217;
                            zB += local298;
                            uC += local219;
                            uB += local300;
                            vC += local221;
                            vB += local302;
                            fogC = (int) ((float) fogC + local223);
                            fogB = (int) ((float) fogB + local304);
                            local120 += local225;
                            local113 += local306;
                            local141 += local227;
                            local134 += local308;
                            local162 += local229;
                            local155 += local310;
                            local177 += local231;
                            local172 += local312;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                            xA += local320;
                            xB += local239;
                            zA += local379;
                            zB += local298;
                            uA += local381;
                            uB += local300;
                            vA += local383;
                            vB += local302;
                            fogA = (int) ((float) fogA + local385);
                            fogB = (int) ((float) fogB + local304);
                            local106 += local387;
                            local113 += local306;
                            local127 += local389;
                            local134 += local308;
                            local148 += local391;
                            local155 += local310;
                            local167 += local393;
                            local172 += local312;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        while (--yA >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                            xB += local239;
                            xC += local215;
                            zB += local298;
                            zC += local217;
                            uB += local300;
                            uC += local219;
                            vB += local302;
                            vC += local221;
                            fogB = (int) ((float) fogB + local304);
                            fogC = (int) ((float) fogC + local223);
                            local113 += local306;
                            local120 += local225;
                            local134 += local308;
                            local141 += local227;
                            local155 += local310;
                            local162 += local229;
                            local172 += local312;
                            local177 += local231;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawTexturedSpanArgb(this.raster, this.texels, (int) yB, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                            xB += local239;
                            xA += local320;
                            zB += local298;
                            zA += local379;
                            uB += local300;
                            uA += local381;
                            vB += local302;
                            vA += local383;
                            fogB = (int) ((float) fogB + local304);
                            fogA = (int) ((float) fogA + local385);
                            local113 += local306;
                            local106 += local387;
                            local134 += local308;
                            local127 += local389;
                            local155 += local310;
                            local148 += local391;
                            local172 += local312;
                            local167 += local393;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            if (yA < yB) {
                xB = xC;
                zB = zC;
                uB = uC;
                vB = vC;
                fogB = fogC;
                local113 = local120;
                local134 = local141;
                local155 = local162;
                local172 = local177;
                if (yC < 0.0F) {
                    xC -= local320 * yC;
                    xB -= local239 * yC;
                    zC -= local379 * yC;
                    zB -= local298 * yC;
                    uC -= local381 * yC;
                    uB -= local300 * yC;
                    vC -= local383 * yC;
                    vB -= local302 * yC;
                    fogC = (int) ((float) fogC - local385 * 3.0F);
                    fogB = (int) ((float) fogB - local304 * yC);
                    local120 -= local387 * yC;
                    local113 -= local306 * yC;
                    local141 -= local389 * yC;
                    local134 -= local308 * yC;
                    local162 -= local391 * yC;
                    local155 -= local310 * yC;
                    local177 -= local393 * yC;
                    local172 -= local312 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local215 * yA;
                    zA -= local217 * yA;
                    uA -= local219 * yA;
                    vA -= local221 * yA;
                    fogA = (int) ((float) fogA - local223 * yA);
                    local106 -= local225 * yA;
                    local127 -= local227 * yA;
                    local148 -= local229 * yA;
                    local167 -= local231 * yA;
                    yA = 0.0F;
                }
                if (local239 < local320) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                        xB += local239;
                        xC += local320;
                        zB += local298;
                        zC += local379;
                        uB += local300;
                        uC += local381;
                        vB += local302;
                        vC += local383;
                        fogB = (int) ((float) fogB + local304);
                        fogC = (int) ((float) fogC + local385);
                        local113 += local306;
                        local120 += local387;
                        local134 += local308;
                        local141 += local389;
                        local155 += local310;
                        local162 += local391;
                        local172 += local312;
                        local177 += local393;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local113, local106, local134, local127, local155, local148, local172, local167);
                        xB += local239;
                        xA += local215;
                        zB += local298;
                        zA += local217;
                        uB += local300;
                        uA += local219;
                        vB += local302;
                        vA += local221;
                        fogB = (int) ((float) fogB + local304);
                        fogA = (int) ((float) fogA + local223);
                        local113 += local306;
                        local106 += local225;
                        local134 += local308;
                        local127 += local227;
                        local155 += local310;
                        local148 += local229;
                        local172 += local312;
                        local167 += local231;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                        xC += local320;
                        xB += local239;
                        zC += local379;
                        zB += local298;
                        uC += local381;
                        uB += local300;
                        vC += local383;
                        vB += local302;
                        fogC = (int) ((float) fogC + local385);
                        fogB = (int) ((float) fogB + local304);
                        local120 += local387;
                        local113 += local306;
                        local141 += local389;
                        local134 += local308;
                        local162 += local391;
                        local155 += local310;
                        local177 += local393;
                        local172 += local312;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local106, local113, local127, local134, local148, local155, local167, local172);
                        xA += local215;
                        xB += local239;
                        zA += local217;
                        zB += local298;
                        uA += local219;
                        uB += local300;
                        vA += local221;
                        vB += local302;
                        fogA = (int) ((float) fogA + local223);
                        fogB = (int) ((float) fogB + local304);
                        local106 += local225;
                        local113 += local306;
                        local127 += local227;
                        local134 += local308;
                        local148 += local229;
                        local155 += local310;
                        local167 += local231;
                        local172 += local312;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                zA = zC;
                uA = uC;
                vA = vC;
                fogA = fogC;
                local106 = local120;
                local127 = local141;
                local148 = local162;
                local167 = local177;
                if (yC < 0.0F) {
                    xC -= local320 * yC;
                    xA -= local239 * yC;
                    zC -= local379 * yC;
                    zA -= local298 * yC;
                    uC -= local381 * yC;
                    uA -= local300 * yC;
                    vC -= local383 * yC;
                    vA -= local302 * yC;
                    fogC = (int) ((float) fogC - local385 * 3.0F);
                    fogA = (int) ((float) fogA - local304 * yC);
                    local120 -= local387 * yC;
                    local106 -= local306 * yC;
                    local141 -= local389 * yC;
                    local127 -= local308 * yC;
                    local162 -= local391 * yC;
                    local148 -= local310 * yC;
                    local177 -= local393 * yC;
                    local167 -= local312 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local215 * yB;
                    zB -= local217 * yB;
                    uB -= local219 * yB;
                    vB -= local221 * yB;
                    fogB = (int) ((float) fogB - local223 * yB);
                    local113 -= local225 * yB;
                    local134 -= local227 * yB;
                    local155 -= local229 * yB;
                    local172 -= local231 * yB;
                    yB = 0.0F;
                }
                if (local239 < local320) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local106, local120, local127, local141, local148, local162, local167, local177);
                        xA += local239;
                        xC += local320;
                        zA += local298;
                        zC += local379;
                        uA += local300;
                        uC += local381;
                        vA += local302;
                        vC += local383;
                        fogA = (int) ((float) fogA + local304);
                        fogC = (int) ((float) fogC + local385);
                        local106 += local306;
                        local120 += local387;
                        local127 += local308;
                        local141 += local389;
                        local148 += local310;
                        local162 += local391;
                        local167 += local312;
                        local177 += local393;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local113, local120, local134, local141, local155, local162, local172, local177);
                        xB += local215;
                        xC += local320;
                        zB += local217;
                        zC += local379;
                        uB += local219;
                        uC += local381;
                        vB += local221;
                        vC += local383;
                        fogB = (int) ((float) fogB + local223);
                        fogC = (int) ((float) fogC + local385);
                        local113 += local225;
                        local120 += local387;
                        local134 += local227;
                        local141 += local389;
                        local155 += local229;
                        local162 += local391;
                        local172 += local231;
                        local177 += local393;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local120, local106, local141, local127, local162, local148, local177, local167);
                        xC += local320;
                        xA += local239;
                        zC += local379;
                        zA += local298;
                        uC += local381;
                        uA += local300;
                        vC += local383;
                        vA += local302;
                        fogC = (int) ((float) fogC + local385);
                        fogA = (int) ((float) fogA + local304);
                        local120 += local387;
                        local106 += local306;
                        local141 += local389;
                        local127 += local308;
                        local162 += local391;
                        local148 += local310;
                        local177 += local393;
                        local167 += local312;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawTexturedSpanArgb(this.raster, this.texels, (int) yC, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local120, local113, local141, local134, local162, local155, local177, local172);
                        xC += local320;
                        xB += local215;
                        zC += local379;
                        zB += local217;
                        uC += local381;
                        uB += local219;
                        vC += local383;
                        vB += local221;
                        fogC = (int) ((float) fogC + local385);
                        fogB = (int) ((float) fogB + local223);
                        local120 += local387;
                        local113 += local225;
                        local141 += local389;
                        local134 += local227;
                        local162 += local391;
                        local155 += local229;
                        local177 += local393;
                        local172 += local231;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "(FFFFFFFFFFFFFFFIIIIIIIIFIFIF)V")
    public void renderBlendedTexturedTriangle(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) float uA, @OriginalArg(10) float uB, @OriginalArg(11) float uC, @OriginalArg(12) float vA, @OriginalArg(13) float vB, @OriginalArg(14) float vC, @OriginalArg(15) int colourA, @OriginalArg(16) int colourB, @OriginalArg(17) int colourC, @OriginalArg(18) int fogColour, @OriginalArg(19) int fogA, @OriginalArg(20) int fogB, @OriginalArg(21) int fogC, @OriginalArg(22) int texture, @OriginalArg(23) float textureScale, @OriginalArg(24) int texture2, @OriginalArg(25) float textureScale2, @OriginalArg(26) int texture3, @OriginalArg(27) float textureScale3) {
        if (texture != this.textureId) {
            this.texels = this.toolkit.getTexels(texture);
            if (this.texels == null) {
                this.renderTriangleRgb((float) (int) yA, (float) (int) yB, (float) (int) yC, (float) (int) xA, (float) (int) xB, (float) (int) xC, (float) (int) zA, (float) (int) zB, (float) (int) zC, Static462.blendArgb(fogColour | fogA << 24, colourA), Static462.blendArgb(fogColour | fogB << 24, colourB), Static462.blendArgb(fogColour | fogC << 24, colourC));
                return;
            }
            this.textureSize = this.toolkit.smallTexture(texture) ? 64 : this.toolkit.textureSize;
            this.textureMask = this.textureSize - 1;
            this.textureBlendMode = this.toolkit.textureAlphaBlendMode(texture);
        }
        this.textureScale = textureScale;
        if (texture2 != this.textureId2) {
            this.texels2 = this.toolkit.getTexels(texture2);
            if (this.texels2 == null) {
                this.renderTriangleRgb((float) (int) yA, (float) (int) yB, (float) (int) yC, (float) (int) xA, (float) (int) xB, (float) (int) xC, (float) (int) zA, (float) (int) zB, (float) (int) zC, Static462.blendArgb(fogColour | fogA << 24, colourA), Static462.blendArgb(fogColour | fogB << 24, colourB), Static462.blendArgb(fogColour | fogC << 24, colourC));
                return;
            }
            this.textureSize2 = this.toolkit.smallTexture(texture2) ? 64 : this.toolkit.textureSize;
            this.textureMask2 = this.textureSize2 - 1;
        }
        this.textureScale2 = textureScale2;
        if (texture3 != this.textureId3) {
            this.texels3 = this.toolkit.getTexels(texture3);
            if (this.texels3 == null) {
                this.renderTriangleRgb((float) (int) yA, (float) (int) yB, (float) (int) yC, (float) (int) xA, (float) (int) xB, (float) (int) xC, (float) (int) zA, (float) (int) zB, (float) (int) zC, Static462.blendArgb(fogColour | fogA << 24, colourA), Static462.blendArgb(fogColour | fogB << 24, colourB), Static462.blendArgb(fogColour | fogC << 24, colourC));
                return;
            }
            this.textureSize3 = this.toolkit.smallTexture(texture3) ? 64 : this.toolkit.textureSize;
            this.textureMask3 = this.textureSize3 - 1;
        }
        this.textureScale3 = textureScale3;
        this.fogColour = fogColour;
        @Pc(279) float local279 = (float) (colourA >> 24 & 0xFF);
        @Pc(286) float local286 = (float) (colourB >> 24 & 0xFF);
        @Pc(293) float local293 = (float) (colourC >> 24 & 0xFF);
        @Pc(300) float local300 = (float) (colourA >> 16 & 0xFF);
        @Pc(307) float local307 = (float) (colourB >> 16 & 0xFF);
        @Pc(314) float local314 = (float) (colourC >> 16 & 0xFF);
        @Pc(321) float local321 = (float) (colourA >> 8 & 0xFF);
        @Pc(328) float local328 = (float) (colourB >> 8 & 0xFF);
        @Pc(335) float local335 = (float) (colourC >> 8 & 0xFF);
        @Pc(340) float local340 = (float) (colourA & 0xFF);
        @Pc(345) float local345 = (float) (colourB & 0xFF);
        @Pc(350) float local350 = (float) (colourC & 0xFF);
        uA /= zA;
        uB /= zB;
        uC /= zC;
        vA /= zA;
        vB /= zB;
        vC /= zC;
        zA = 1.0F / zA;
        zB = 1.0F / zB;
        zC = 1.0F / zC;
        @Pc(388) float local388 = 1.0F;
        @Pc(390) float local390 = 0.0F;
        @Pc(392) float local392 = 0.0F;
        @Pc(394) float local394 = 0.0F;
        @Pc(396) float local396 = 1.0F;
        @Pc(398) float local398 = 0.0F;
        @Pc(400) float local400 = 0.0F;
        @Pc(402) float local402 = 0.0F;
        @Pc(404) float local404 = 0.0F;
        @Pc(406) float local406 = 0.0F;
        @Pc(408) float local408 = 0.0F;
        @Pc(410) float local410 = 0.0F;
        @Pc(412) float local412 = 0.0F;
        @Pc(414) float local414 = 0.0F;
        @Pc(416) float local416 = 0.0F;
        @Pc(418) float local418 = 0.0F;
        @Pc(420) float local420 = 0.0F;
        @Pc(428) float local428;
        if (yB != yA) {
            local428 = yB - yA;
            local400 = (xB - xA) / local428;
            local402 = (zB - zA) / local428;
            local404 = (uB - uA) / local428;
            local406 = (vB - vA) / local428;
            local408 = (float) (fogB - fogA) / local428;
            local410 = (local286 - local279) / local428;
            local412 = (local307 - local300) / local428;
            local414 = (local328 - local321) / local428;
            local416 = (local345 - local340) / local428;
            local418 = (local390 - local388) / local428;
            local420 = (local396 - local394) / local428;
        }
        local428 = 0.0F;
        @Pc(499) float local499 = 0.0F;
        @Pc(501) float local501 = 0.0F;
        @Pc(503) float local503 = 0.0F;
        @Pc(505) float local505 = 0.0F;
        @Pc(507) float local507 = 0.0F;
        @Pc(509) float local509 = 0.0F;
        @Pc(511) float local511 = 0.0F;
        @Pc(513) float local513 = 0.0F;
        @Pc(515) float local515 = 0.0F;
        @Pc(517) float local517 = 0.0F;
        @Pc(525) float local525;
        if (yC != yB) {
            local525 = yC - yB;
            local428 = (xC - xB) / local525;
            local499 = (zC - zB) / local525;
            local501 = (uC - uB) / local525;
            local503 = (vC - vB) / local525;
            local505 = (float) (fogC - fogB) / local525;
            local507 = (local293 - local286) / local525;
            local509 = (local314 - local307) / local525;
            local511 = (local335 - local328) / local525;
            local513 = (local350 - local345) / local525;
            local515 = (local392 - local390) / local525;
            local517 = (local398 - local396) / local525;
        }
        local525 = 0.0F;
        @Pc(596) float local596 = 0.0F;
        @Pc(598) float local598 = 0.0F;
        @Pc(600) float local600 = 0.0F;
        @Pc(602) float local602 = 0.0F;
        @Pc(604) float local604 = 0.0F;
        @Pc(606) float local606 = 0.0F;
        @Pc(608) float local608 = 0.0F;
        @Pc(610) float local610 = 0.0F;
        @Pc(612) float local612 = 0.0F;
        @Pc(614) float local614 = 0.0F;
        if (yA != yC) {
            @Pc(622) float local622 = yA - yC;
            local525 = (xA - xC) / local622;
            local596 = (zA - zC) / local622;
            local598 = (uA - uC) / local622;
            local600 = (vA - vC) / local622;
            local602 = (float) (fogA - fogC) / local622;
            local604 = (local279 - local293) / local622;
            local606 = (local300 - local314) / local622;
            local608 = (local321 - local335) / local622;
            local610 = (local340 - local350) / local622;
            local612 = (local388 - local392) / local622;
            local614 = (local394 - local398) / local622;
        }
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yB < yC) {
                    xC = xA;
                    zC = zA;
                    uC = uA;
                    vC = vA;
                    fogC = fogA;
                    local293 = local279;
                    local314 = local300;
                    local335 = local321;
                    local350 = local340;
                    local392 = local388;
                    local398 = local394;
                    if (yA < 0.0F) {
                        xA -= local400 * yA;
                        xC -= local525 * yA;
                        zA -= local402 * yA;
                        zC -= local596 * yA;
                        uA -= local404 * yA;
                        uC -= local598 * yA;
                        vA -= local406 * yA;
                        vC -= local600 * yA;
                        fogA = (int) ((float) fogA - local408 * yA);
                        fogC = (int) ((float) fogC - local602 * yA);
                        local279 -= local410 * yA;
                        local293 -= local604 * yA;
                        local300 -= local412 * yA;
                        local314 -= local606 * yA;
                        local321 -= local414 * yA;
                        local335 -= local608 * yA;
                        local340 -= local416 * yA;
                        local350 -= local610 * yA;
                        local388 -= local418 * yA;
                        local392 -= local612 * yA;
                        local394 -= local420 * yA;
                        local398 -= local614 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local428 * yB;
                        zB -= local499 * yB;
                        uB -= local501 * yB;
                        vB -= local503 * yB;
                        fogB = (int) ((float) fogB - local505 * yB);
                        local286 -= local507 * yB;
                        local307 -= local509 * yB;
                        local328 -= local511 * yB;
                        local345 -= local513 * yB;
                        local390 -= local515 * yB;
                        local396 -= local517 * yB;
                        yB = 0.0F;
                    }
                    if (yA != yB && local525 < local400 || yA == yB && local525 > local428) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local293, local279, local314, local300, local335, local321, local350, local340, local392, local388, local398, local394);
                            xA += local400;
                            xC += local525;
                            zA += local402;
                            zC += local596;
                            uA += local404;
                            uC += local598;
                            vA += local406;
                            vC += local600;
                            fogA = (int) ((float) fogA + local408);
                            fogC = (int) ((float) fogC + local602);
                            local279 += local410;
                            local293 += local604;
                            local300 += local412;
                            local314 += local606;
                            local321 += local414;
                            local335 += local608;
                            local340 += local416;
                            local350 += local610;
                            local388 += local418;
                            local392 += local612;
                            local394 += local420;
                            local398 += local420;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local293, local286, local314, local307, local335, local328, local350, local345, local392, local390, local398, local396);
                            xB += local428;
                            xC += local525;
                            zB += local499;
                            zC += local596;
                            uB += local501;
                            uC += local598;
                            vB += local503;
                            vC += local600;
                            fogB = (int) ((float) fogB + local505);
                            fogC = (int) ((float) fogC + local602);
                            local286 += local507;
                            local293 += local604;
                            local307 += local509;
                            local314 += local606;
                            local328 += local511;
                            local335 += local608;
                            local345 += local513;
                            local350 += local610;
                            local390 += local515;
                            local392 += local612;
                            local396 += local517;
                            local398 += local614;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local279, local293, local300, local314, local321, local335, local340, local350, local388, local392, local394, local398);
                            xA += local400;
                            xC += local525;
                            zA += local402;
                            zC += local596;
                            uA += local404;
                            uC += local598;
                            vA += local406;
                            vC += local600;
                            fogA = (int) ((float) fogA + local408);
                            fogC = (int) ((float) fogC + local602);
                            local279 += local410;
                            local293 += local604;
                            local300 += local412;
                            local314 += local606;
                            local321 += local414;
                            local335 += local608;
                            local340 += local416;
                            local350 += local610;
                            local388 += local418;
                            local392 += local612;
                            local394 += local420;
                            local398 += local614;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local286, local293, local307, local314, local328, local335, local345, local350, local390, local392, local396, local398);
                            xB += local428;
                            xC += local525;
                            zB += local499;
                            zC += local596;
                            uB += local501;
                            uC += local598;
                            vB += local503;
                            vC += local600;
                            fogB = (int) ((float) fogB + local505);
                            fogC = (int) ((float) fogC + local602);
                            local286 += local507;
                            local293 += local604;
                            local307 += local509;
                            local314 += local606;
                            local328 += local511;
                            local335 += local608;
                            local345 += local513;
                            local350 += local610;
                            local390 += local515;
                            local392 += local612;
                            local396 += local517;
                            local398 += local614;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    zB = zA;
                    uB = uA;
                    vB = vA;
                    fogB = fogA;
                    local286 = local279;
                    local307 = local300;
                    local328 = local321;
                    local345 = local340;
                    local390 = local388;
                    local396 = local394;
                    if (yA < 0.0F) {
                        xA -= local400 * yA;
                        xB -= local525 * yA;
                        zA -= local402 * yA;
                        zB -= local596 * yA;
                        uA -= local404 * yA;
                        uB -= local598 * yA;
                        vA -= local406 * yA;
                        vB -= local600 * yA;
                        fogA = (int) ((float) fogA - local408 * yA);
                        fogB = (int) ((float) fogB - local602 * yA);
                        local279 -= local410 * yA;
                        local286 -= local604 * yA;
                        local300 -= local412 * yA;
                        local307 -= local606 * yA;
                        local321 -= local414 * yA;
                        local328 -= local608 * yA;
                        local340 -= local416 * yA;
                        local345 -= local610 * yA;
                        local388 -= local418 * yA;
                        local390 -= local612 * yA;
                        local394 -= local420 * yA;
                        local396 -= local614 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local428 * yC;
                        zC -= local499 * yC;
                        uC -= local501 * yC;
                        vC -= local503 * yC;
                        fogC = (int) ((float) fogC - local505 * yC);
                        local293 -= local507 * yC;
                        local314 -= local509 * yC;
                        local335 -= local511 * yC;
                        local350 -= local513 * yC;
                        local392 -= local515 * yC;
                        local398 -= local517 * yC;
                        yC = 0.0F;
                    }
                    if (yA != yC && local525 < local400 || yA == yC && local428 > local400) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local286, local279, local307, local300, local328, local321, local345, local340, local390, local388, local396, local394);
                            xA += local400;
                            xB += local525;
                            zA += local402;
                            zB += local596;
                            uA += local404;
                            uB += local598;
                            vA += local406;
                            vB += local600;
                            fogA = (int) ((float) fogA + local408);
                            fogB = (int) ((float) fogB + local602);
                            local279 += local410;
                            local286 += local604;
                            local300 += local412;
                            local307 += local606;
                            local321 += local414;
                            local328 += local608;
                            local340 += local416;
                            local345 += local610;
                            local388 += local418;
                            local390 += local612;
                            local394 += local420;
                            local396 += local614;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local293, local279, local314, local300, local335, local321, local350, local340, local392, local388, local398, local394);
                            xC += local428;
                            xA += local400;
                            zC += local499;
                            zA += local402;
                            uC += local501;
                            uA += local404;
                            vC += local503;
                            vA += local406;
                            fogC = (int) ((float) fogC + local505);
                            fogA = (int) ((float) fogA + local408);
                            local293 += local507;
                            local279 += local410;
                            local314 += local509;
                            local300 += local412;
                            local335 += local511;
                            local321 += local414;
                            local350 += local513;
                            local340 += local416;
                            local392 += local515;
                            local388 += local418;
                            local398 += local517;
                            local394 += local420;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local279, local286, local300, local307, local321, local328, local340, local345, local388, local390, local394, local396);
                            xB += local525;
                            xA += local400;
                            zB += local596;
                            zA += local402;
                            uB += local598;
                            uA += local404;
                            vB += local600;
                            vA += local406;
                            fogB = (int) ((float) fogB + local602);
                            fogA = (int) ((float) fogA + local408);
                            local286 += local604;
                            local279 += local410;
                            local307 += local606;
                            local300 += local412;
                            local328 += local608;
                            local321 += local414;
                            local345 += local610;
                            local340 += local416;
                            local390 += local612;
                            local388 += local418;
                            local396 += local614;
                            local394 += local420;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yA, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local279, local293, local300, local314, local321, local335, local340, local350, local388, local392, local394, local398);
                            xA += local400;
                            xC += local428;
                            zA += local402;
                            zC += local499;
                            uA += local404;
                            uC += local501;
                            vA += local406;
                            vC += local503;
                            fogA = (int) ((float) fogA + local408);
                            fogC = (int) ((float) fogC + local505);
                            local279 += local410;
                            local293 += local507;
                            local300 += local412;
                            local314 += local509;
                            local321 += local414;
                            local335 += local511;
                            local340 += local416;
                            local350 += local513;
                            local388 += local418;
                            local392 += local515;
                            local394 += local420;
                            local398 += local517;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                if (yC < yA) {
                    xA = xB;
                    zA = zB;
                    uA = uB;
                    vA = vB;
                    fogA = fogB;
                    local279 = local286;
                    local300 = local307;
                    local321 = local328;
                    local340 = local345;
                    local388 = local390;
                    local394 = local396;
                    if (yB < 0.0F) {
                        xA = xB - local400 * yB;
                        xB -= local428 * yB;
                        zA = zB - local402 * yB;
                        zB -= local499 * yB;
                        uA = uB - local404 * yB;
                        uB -= local501 * yB;
                        vA = vB - local406 * yB;
                        vB -= local503 * yB;
                        fogA = (int) ((float) fogB - local408 * yB);
                        fogB = (int) ((float) fogB - local505 * yB);
                        local279 = local286 - local410 * yB;
                        local286 -= local507 * yB;
                        local300 = local307 - local412 * yB;
                        local307 -= local509 * yB;
                        local321 = local328 - local414 * yB;
                        local328 -= local511 * yB;
                        local340 = local345 - local416 * yB;
                        local345 -= local513 * yB;
                        local388 = local390 - local418 * yB;
                        local390 -= local515 * yB;
                        local394 = local396 - local420 * yB;
                        local396 -= local517 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local525 * yC;
                        zC -= local596 * yC;
                        uC -= local598 * yC;
                        vC -= local600 * yC;
                        fogC = (int) ((float) fogC - local602 * yC);
                        local293 -= local604 * yC;
                        local314 -= local606 * yC;
                        local335 -= local608 * yC;
                        local350 -= local610 * yC;
                        local392 -= local612 * yC;
                        local398 -= local614 * yC;
                        yC = 0.0F;
                    }
                    if (yB != yC && local400 < local428 || yB == yC && local400 > local525) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local279, local286, local300, local307, local321, local328, local340, local345, local388, local390, local394, local396);
                            xA += local400;
                            xB += local428;
                            zA += local402;
                            zB += local499;
                            uA += local404;
                            uB += local501;
                            vA += local406;
                            vB += local503;
                            fogA = (int) ((float) fogA + local408);
                            fogB = (int) ((float) fogB + local505);
                            local279 += local410;
                            local286 += local507;
                            local300 += local412;
                            local307 += local509;
                            local321 += local414;
                            local328 += local511;
                            local340 += local416;
                            local345 += local513;
                            local388 += local418;
                            local390 += local515;
                            local394 += local420;
                            local396 += local517;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local279, local293, local300, local314, local321, local335, local340, local350, local388, local392, local394, local398);
                            xA += local400;
                            xC += local525;
                            zA += local402;
                            zC += local596;
                            uA += local404;
                            uC += local598;
                            vA += local406;
                            vC += local600;
                            fogA = (int) ((float) fogA + local408);
                            fogC = (int) ((float) fogC + local602);
                            local279 += local410;
                            local293 += local604;
                            local300 += local412;
                            local314 += local606;
                            local321 += local414;
                            local335 += local608;
                            local340 += local416;
                            local350 += local610;
                            local388 += local418;
                            local392 += local612;
                            local394 += local420;
                            local398 += local614;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local286, local279, local307, local300, local328, local321, local345, local340, local390, local388, local396, local394);
                            xB += local428;
                            xA += local400;
                            zB += local499;
                            zA += local402;
                            uB += local501;
                            uA += local404;
                            vB += local503;
                            vA += local406;
                            fogB = (int) ((float) fogB + local505);
                            fogA = (int) ((float) fogA + local408);
                            local286 += local507;
                            local279 += local410;
                            local307 += local509;
                            local300 += local412;
                            local328 += local511;
                            local321 += local414;
                            local345 += local513;
                            local340 += local416;
                            local392 += local515;
                            local388 += local418;
                            local396 += local517;
                            local394 += local420;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local293, local279, local314, local300, local335, local321, local350, local340, local392, local388, local398, local394);
                            xC += local525;
                            xA += local400;
                            zC += local596;
                            zA += local402;
                            uC += local598;
                            uA += local404;
                            vC += local600;
                            vA += local406;
                            fogC = (int) ((float) fogC + local602);
                            fogA = (int) ((float) fogA + local408);
                            local293 += local604;
                            local279 += local410;
                            local314 += local606;
                            local300 += local412;
                            local335 += local608;
                            local321 += local414;
                            local350 += local610;
                            local340 += local416;
                            local392 += local612;
                            local388 += local418;
                            local398 += local614;
                            local394 += local420;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    zC = zB;
                    uC = uB;
                    vC = vB;
                    fogC = fogB;
                    local293 = local286;
                    local314 = local307;
                    local335 = local328;
                    local350 = local345;
                    local392 = local390;
                    local398 = local396;
                    if (yB < 0.0F) {
                        xC = xB - local400 * yB;
                        xB -= local428 * yB;
                        zC = zB - local402 * yB;
                        zB -= local499 * yB;
                        uC = uB - local404 * yB;
                        uB -= local501 * yB;
                        vC = vB - local406 * yB;
                        vB -= local503 * yB;
                        fogC = (int) ((float) fogB - local408 * yB);
                        fogB = (int) ((float) fogB - local505 * yB);
                        local293 = local286 - local410 * yB;
                        local286 -= local507 * yB;
                        local314 = local307 - local412 * yB;
                        local307 -= local509 * yB;
                        local335 = local328 - local414 * yB;
                        local328 -= local511 * yB;
                        local350 = local345 - local416 * yB;
                        local345 -= local513 * yB;
                        local392 = local390 - local418 * yB;
                        local390 -= local515 * yB;
                        local398 = local396 - local420 * yB;
                        local396 -= local517 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local525 * yA;
                        zA -= local596 * yA;
                        uA -= local598 * yA;
                        vA -= local600 * yA;
                        fogA = (int) ((float) fogA - local602 * yA);
                        local279 -= local604 * yA;
                        local300 -= local606 * yA;
                        local321 -= local608 * yA;
                        local340 -= local610 * yA;
                        local388 -= local612 * yA;
                        local394 -= local614 * yA;
                        yA = 0.0F;
                    }
                    yC -= yA;
                    yA -= yB;
                    yB = (float) this.lineOffsets[(int) yB];
                    if (local400 < local428) {
                        while (--yA >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local293, local286, local314, local307, local335, local328, local350, local345, local392, local390, local398, local396);
                            xC += local400;
                            xB += local428;
                            zC += local402;
                            zB += local499;
                            uC += local404;
                            uB += local501;
                            vC += local406;
                            vB += local503;
                            fogC = (int) ((float) fogC + local408);
                            fogB = (int) ((float) fogB + local505);
                            local293 += local410;
                            local286 += local507;
                            local314 += local412;
                            local307 += local509;
                            local335 += local414;
                            local328 += local511;
                            local350 += local416;
                            local345 += local513;
                            local392 += local418;
                            local390 += local515;
                            local398 += local420;
                            local396 += local517;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local279, local286, local300, local307, local321, local328, local340, local345, local388, local390, local394, local396);
                            xA += local525;
                            xB += local428;
                            zA += local596;
                            zB += local499;
                            uA += local598;
                            uB += local501;
                            vA += local600;
                            vB += local503;
                            fogA = (int) ((float) fogA + local602);
                            fogB = (int) ((float) fogB + local505);
                            local279 += local604;
                            local286 += local507;
                            local300 += local606;
                            local307 += local509;
                            local321 += local608;
                            local328 += local511;
                            local340 += local610;
                            local345 += local513;
                            local388 += local612;
                            local390 += local515;
                            local394 += local614;
                            local396 += local517;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        while (--yA >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local286, local293, local307, local314, local328, local335, local345, local350, local390, local392, local396, local398);
                            xB += local428;
                            xC += local400;
                            zB += local499;
                            zC += local402;
                            uB += local501;
                            uC += local404;
                            vB += local503;
                            vC += local406;
                            fogB = (int) ((float) fogB + local505);
                            fogC = (int) ((float) fogC + local408);
                            local286 += local507;
                            local293 += local410;
                            local307 += local509;
                            local314 += local412;
                            local328 += local511;
                            local335 += local414;
                            local345 += local513;
                            local350 += local416;
                            local390 += local515;
                            local392 += local418;
                            local396 += local517;
                            local398 += local420;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yB, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local286, local279, local307, local300, local328, local321, local345, local340, local390, local388, local396, local394);
                            xB += local428;
                            xA += local525;
                            zB += local499;
                            zA += local596;
                            uB += local501;
                            uA += local598;
                            vB += local503;
                            vA += local600;
                            fogB = (int) ((float) fogB + local505);
                            fogA = (int) ((float) fogA + local602);
                            local286 += local507;
                            local279 += local604;
                            local307 += local509;
                            local300 += local606;
                            local328 += local511;
                            local321 += local608;
                            local345 += local513;
                            local340 += local610;
                            local390 += local515;
                            local388 += local612;
                            local396 += local517;
                            local394 += local614;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            if (yA < yB) {
                xB = xC;
                zB = zC;
                uB = uC;
                vB = vC;
                fogB = fogC;
                local286 = local293;
                local307 = local314;
                local328 = local335;
                local345 = local350;
                local390 = local392;
                local396 = local398;
                if (yC < 0.0F) {
                    xC -= local525 * yC;
                    xB -= local428 * yC;
                    zC -= local596 * yC;
                    zB -= local499 * yC;
                    uC -= local598 * yC;
                    uB -= local501 * yC;
                    vC -= local600 * yC;
                    vB -= local503 * yC;
                    fogC = (int) ((float) fogC - local602 * 3.0F);
                    fogB = (int) ((float) fogB - local505 * yC);
                    local293 -= local604 * yC;
                    local286 -= local507 * yC;
                    local314 -= local606 * yC;
                    local307 -= local509 * yC;
                    local335 -= local608 * yC;
                    local328 -= local511 * yC;
                    local350 -= local610 * yC;
                    local345 -= local513 * yC;
                    local392 -= local612 * yC;
                    local390 -= local515 * yC;
                    local398 -= local614 * yC;
                    local396 -= local517 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local400 * yA;
                    zA -= local402 * yA;
                    uA -= local404 * yA;
                    vA -= local406 * yA;
                    fogA = (int) ((float) fogA - local408 * yA);
                    local279 -= local410 * yA;
                    local300 -= local412 * yA;
                    local321 -= local414 * yA;
                    local340 -= local416 * yA;
                    local388 -= local418 * yA;
                    local394 -= local420 * yA;
                    yA = 0.0F;
                }
                if (local428 < local525) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local286, local293, local307, local314, local328, local335, local345, local350, local390, local392, local396, local398);
                        xB += local428;
                        xC += local525;
                        zB += local499;
                        zC += local596;
                        uB += local501;
                        uC += local598;
                        vB += local503;
                        vC += local600;
                        fogB = (int) ((float) fogB + local505);
                        fogC = (int) ((float) fogC + local602);
                        local286 += local507;
                        local293 += local604;
                        local307 += local509;
                        local314 += local606;
                        local328 += local511;
                        local335 += local608;
                        local345 += local513;
                        local350 += local610;
                        local390 += local515;
                        local392 += local612;
                        local396 += local517;
                        local398 += local614;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xB, (int) xA, zB, zA, uB, uA, vB, vA, (float) fogB, (float) fogA, local286, local279, local307, local300, local328, local321, local345, local340, local390, local388, local396, local394);
                        xB += local428;
                        xA += local400;
                        zB += local499;
                        zA += local402;
                        uB += local501;
                        uA += local404;
                        vB += local503;
                        vA += local406;
                        fogB = (int) ((float) fogB + local505);
                        fogA = (int) ((float) fogA + local408);
                        local286 += local507;
                        local279 += local410;
                        local307 += local509;
                        local300 += local412;
                        local328 += local511;
                        local321 += local414;
                        local345 += local513;
                        local340 += local416;
                        local390 += local515;
                        local388 += local418;
                        local396 += local517;
                        local394 += local420;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local293, local286, local314, local307, local335, local328, local350, local345, local392, local390, local398, local396);
                        xC += local525;
                        xB += local428;
                        zC += local596;
                        zB += local499;
                        uC += local598;
                        uB += local501;
                        vC += local600;
                        vB += local503;
                        fogC = (int) ((float) fogC + local602);
                        fogB = (int) ((float) fogB + local505);
                        local293 += local604;
                        local286 += local507;
                        local314 += local606;
                        local307 += local509;
                        local335 += local608;
                        local328 += local511;
                        local350 += local610;
                        local345 += local513;
                        local392 += local612;
                        local390 += local515;
                        local398 += local614;
                        local396 += local517;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xA, (int) xB, zA, zB, uA, uB, vA, vB, (float) fogA, (float) fogB, local279, local286, local300, local307, local321, local328, local340, local345, local388, local390, local394, local396);
                        xA += local400;
                        xB += local428;
                        zA += local402;
                        zB += local499;
                        uA += local404;
                        uB += local501;
                        vA += local406;
                        vB += local503;
                        fogA = (int) ((float) fogA + local408);
                        fogB = (int) ((float) fogB + local505);
                        local279 += local410;
                        local286 += local507;
                        local300 += local412;
                        local307 += local509;
                        local321 += local414;
                        local328 += local511;
                        local340 += local416;
                        local345 += local513;
                        local388 += local418;
                        local390 += local515;
                        local394 += local420;
                        local396 += local517;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                zA = zC;
                uA = uC;
                vA = vC;
                fogA = fogC;
                local279 = local293;
                local300 = local314;
                local321 = local335;
                local340 = local350;
                local388 = local392;
                local394 = local398;
                if (yC < 0.0F) {
                    xC -= local525 * yC;
                    xA -= local428 * yC;
                    zC -= local596 * yC;
                    zA -= local499 * yC;
                    uC -= local598 * yC;
                    uA -= local501 * yC;
                    vC -= local600 * yC;
                    vA -= local503 * yC;
                    fogC = (int) ((float) fogC - local602 * 3.0F);
                    fogA = (int) ((float) fogA - local505 * yC);
                    local293 -= local604 * yC;
                    local279 -= local507 * yC;
                    local314 -= local606 * yC;
                    local300 -= local509 * yC;
                    local335 -= local608 * yC;
                    local321 -= local511 * yC;
                    local350 -= local610 * yC;
                    local340 -= local513 * yC;
                    local392 -= local612 * yC;
                    local388 -= local515 * yC;
                    local398 -= local614 * yC;
                    local394 -= local517 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local400 * yB;
                    zB -= local402 * yB;
                    uB -= local404 * yB;
                    vB -= local406 * yB;
                    fogB = (int) ((float) fogB - local408 * yB);
                    local286 -= local410 * yB;
                    local307 -= local412 * yB;
                    local328 -= local414 * yB;
                    local345 -= local416 * yB;
                    local390 -= local418 * yB;
                    local396 -= local420 * yB;
                    yB = 0.0F;
                }
                if (local428 < local525) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xA, (int) xC, zA, zC, uA, uC, vA, vC, (float) fogA, (float) fogC, local279, local293, local300, local314, local321, local335, local340, local350, local388, local392, local394, local398);
                        xA += local428;
                        xC += local525;
                        zA += local499;
                        zC += local596;
                        uA += local501;
                        uC += local598;
                        vA += local503;
                        vC += local600;
                        fogA = (int) ((float) fogA + local505);
                        fogC = (int) ((float) fogC + local602);
                        local279 += local507;
                        local293 += local604;
                        local300 += local509;
                        local314 += local606;
                        local321 += local511;
                        local335 += local608;
                        local340 += local513;
                        local350 += local610;
                        local388 += local515;
                        local392 += local612;
                        local394 += local517;
                        local398 += local614;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xB, (int) xC, zB, zC, uB, uC, vB, vC, (float) fogB, (float) fogC, local286, local293, local307, local314, local328, local335, local345, local350, local390, local392, local396, local398);
                        xB += local400;
                        xC += local525;
                        zB += local402;
                        zC += local596;
                        uB += local404;
                        uC += local598;
                        vB += local406;
                        vC += local600;
                        fogB = (int) ((float) fogB + local408);
                        fogC = (int) ((float) fogC + local602);
                        local286 += local410;
                        local293 += local604;
                        local307 += local412;
                        local314 += local606;
                        local328 += local414;
                        local335 += local608;
                        local345 += local416;
                        local350 += local610;
                        local390 += local418;
                        local392 += local612;
                        local396 += local420;
                        local398 += local614;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xC, (int) xA, zC, zA, uC, uA, vC, vA, (float) fogC, (float) fogA, local293, local279, local314, local300, local335, local321, local350, local340, local392, local388, local398, local394);
                        xC += local525;
                        xA += local428;
                        zC += local596;
                        zA += local499;
                        uC += local598;
                        uA += local501;
                        vC += local600;
                        vA += local503;
                        fogC = (int) ((float) fogC + local602);
                        fogA = (int) ((float) fogA + local505);
                        local293 += local604;
                        local279 += local507;
                        local314 += local606;
                        local300 += local509;
                        local335 += local608;
                        local321 += local511;
                        local350 += local610;
                        local340 += local513;
                        local392 += local612;
                        local388 += local515;
                        local398 += local614;
                        local394 += local517;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawBlendedTexturedSpan(this.raster, this.texels, (int) yC, (int) xC, (int) xB, zC, zB, uC, uB, vC, vB, (float) fogC, (float) fogB, local293, local286, local314, local307, local335, local328, local350, local345, local392, local390, local398, local396);
                        xC += local525;
                        xB += local400;
                        zC += local596;
                        zB += local402;
                        uC += local598;
                        uB += local404;
                        vC += local600;
                        vB += local406;
                        fogC = (int) ((float) fogC + local602);
                        fogB = (int) ((float) fogB + local408);
                        local293 += local604;
                        local286 += local410;
                        local314 += local606;
                        local307 += local412;
                        local335 += local608;
                        local328 += local414;
                        local350 += local610;
                        local345 += local416;
                        local392 += local612;
                        local390 += local418;
                        local398 += local614;
                        local396 += local420;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "(FFFFFFFFFFFF)V")
    public void renderTriangleHslRgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) float hslA, @OriginalArg(10) float hslB, @OriginalArg(11) float hslC) {
        if (this.wireframe) {
            this.toolkit.line((int) yB, (int) yA, (int) xB, ColourUtils.HSV_TO_RGB[(int) hslA], (int) xA);
            this.toolkit.line((int) yC, (int) yB, (int) xC, ColourUtils.HSV_TO_RGB[(int) hslA], (int) xB);
            this.toolkit.line((int) yA, (int) yC, (int) xA, ColourUtils.HSV_TO_RGB[(int) hslA], (int) xC);
            return;
        }
        @Pc(55) float local55 = xB - xA;
        @Pc(59) float local59 = yB - yA;
        @Pc(63) float local63 = xC - xA;
        @Pc(67) float local67 = yC - yA;
        @Pc(71) float local71 = hslB - hslA;
        @Pc(75) float local75 = hslC - hslA;
        @Pc(79) float local79 = zB - zA;
        @Pc(83) float local83 = zC - zA;
        @Pc(95) float local95;
        if (yC == yB) {
            local95 = 0.0F;
        } else {
            local95 = (xC - xB) / (yC - yB);
        }
        @Pc(106) float local106;
        if (yB == yA) {
            local106 = 0.0F;
        } else {
            local106 = local55 / local59;
        }
        @Pc(117) float local117;
        if (yC == yA) {
            local117 = 0.0F;
        } else {
            local117 = local63 / local67;
        }
        @Pc(128) float local128 = local55 * local67 - local63 * local59;
        if (local128 == 0.0F) {
            return;
        }
        @Pc(143) float local143 = (local71 * local67 - local75 * local59) / local128;
        @Pc(153) float local153 = (local75 * local55 - local71 * local63) / local128;
        @Pc(163) float local163 = (local79 * local67 - local83 * local59) / local128;
        @Pc(173) float local173 = (local83 * local55 - local79 * local63) / local128;
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                hslA = hslA - local143 * xA + local143;
                zA = zA - local163 * xA + local163;
                if (yB < yC) {
                    xC = xA;
                    if (yA < 0.0F) {
                        xC = xA - local117 * yA;
                        xA -= local106 * yA;
                        hslA -= local153 * yA;
                        zA -= local173 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local95 * yB;
                        yB = 0.0F;
                    }
                    if ((yA == yB || !(local117 < local106)) && (yA != yB || !(local117 > local95))) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, hslA, local143, zA, local163);
                            xC += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xC, hslA, local143, zA, local163);
                            xC += local117;
                            xB += local95;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, hslA, local143, zA, local163);
                            xC += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xB, hslA, local143, zA, local163);
                            xC += local117;
                            xB += local95;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    if (yA < 0.0F) {
                        xB = xA - local117 * yA;
                        xA -= local106 * yA;
                        hslA -= local153 * yA;
                        zA -= local173 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local95 * yC;
                        yC = 0.0F;
                    }
                    if (yA != yC && local117 < local106 || yA == yC && local95 > local106) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xB, (int) xA, hslA, local143, zA, local163);
                            xB += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xC, (int) xA, hslA, local143, zA, local163);
                            xC += local95;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xB, hslA, local143, zA, local163);
                            xB += local117;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yA, (int) xA, (int) xC, hslA, local143, zA, local163);
                            xC += local95;
                            xA += local106;
                            hslA += local153;
                            zA += local173;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                hslB = hslB - local143 * xB + local143;
                zB = zB - local163 * xB + local163;
                if (yC < yA) {
                    xA = xB;
                    if (yB < 0.0F) {
                        xA = xB - local106 * yB;
                        xB -= local95 * yB;
                        hslB -= local153 * yB;
                        zB -= local173 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local117 * yC;
                        yC = 0.0F;
                    }
                    if ((yB == yC || !(local106 < local95)) && (yB != yC || !(local106 > local117))) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, hslB, local143, zB, local163);
                            xA += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xA, hslB, local143, zB, local163);
                            xA += local106;
                            xC += local117;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, hslB, local143, zB, local163);
                            xA += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xC, hslB, local143, zB, local163);
                            xA += local106;
                            xC += local117;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    if (yB < 0.0F) {
                        xC = xB - local106 * yB;
                        xB -= local95 * yB;
                        hslB -= local153 * yB;
                        zB -= local173 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local117 * yA;
                        yA = 0.0F;
                    }
                    if (local106 < local95) {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xC, (int) xB, hslB, local143, zB, local163);
                            xC += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xA, (int) xB, hslB, local143, zB, local163);
                            xA += local117;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xC, hslB, local143, zB, local163);
                            xC += local106;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yB, (int) xB, (int) xA, hslB, local143, zB, local163);
                            xA += local117;
                            xB += local95;
                            hslB += local153;
                            zB += local173;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            hslC = hslC - local143 * xC + local143;
            zC = zC - local163 * xC + local163;
            if (yA < yB) {
                xB = xC;
                if (yC < 0.0F) {
                    xB = xC - local95 * yC;
                    xC -= local117 * yC;
                    hslC -= local153 * yC;
                    zC -= local173 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local106 * yA;
                    yA = 0.0F;
                }
                if (local95 < local117) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, hslC, local143, zC, local163);
                        xB += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xA, hslC, local143, zC, local163);
                        xB += local95;
                        xA += local106;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, hslC, local143, zC, local163);
                        xB += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xB, hslC, local143, zC, local163);
                        xB += local95;
                        xA += local106;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                if (yC < 0.0F) {
                    xA = xC - local95 * yC;
                    xC -= local117 * yC;
                    hslC -= local153 * yC;
                    zC -= local173 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local106 * yB;
                    yB = 0.0F;
                }
                if (local95 < local117) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xA, (int) xC, hslC, local143, zC, local163);
                        xA += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xB, (int) xC, hslC, local143, zC, local163);
                        xB += local106;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xA, hslC, local143, zC, local163);
                        xA += local95;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawHslSpanRgb(this.raster, this.depthBuffer, (int) yC, (int) xC, (int) xB, hslC, local143, zC, local163);
                        xB += local106;
                        xC += local117;
                        hslC += local153;
                        zC += local173;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "b", descriptor = "([I[FIIIIIFFFF)V")
    public void drawHslSpanArgb(@OriginalArg(0) int[] dst, @OriginalArg(1) float[] depths, @OriginalArg(2) int rowOffset, @OriginalArg(5) int startX, @OriginalArg(6) int endX, @OriginalArg(7) float hsl, @OriginalArg(8) float hslStep, @OriginalArg(9) float z, @OriginalArg(10) float zStep) {
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        rowOffset += startX - 1;
        hsl += hslStep * (float) startX;
        z += zStep * (float) startX;
        @Pc(186) int local186;
        @Pc(191) int local191;
        @Pc(233) int local233;
        @Pc(50) int local50;
        @Pc(64) int local64;
        @Pc(71) int local71;
        @Pc(54) float local54;
        @Pc(87) float local87;
        if (this.threadResource.zWrite) {
            if (this.fastScanline) {
                local50 = endX - startX >> 2;
                local54 = hslStep * 4.0F;
                if (this.alpha == 0) {
                    if (local50 > 0) {
                        do {
                            local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                            hsl += local54;
                            local71 = rowOffset + 1;
                            if (z < depths[local71]) {
                                dst[local71] = local64;
                                depths[local71] = z;
                            }
                            local87 = z + zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                dst[local71] = local64;
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                dst[local71] = local64;
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            rowOffset = local71 + 1;
                            if (local87 < depths[rowOffset]) {
                                dst[rowOffset] = local64;
                                depths[rowOffset] = local87;
                            }
                            z = local87 + zStep;
                            local50--;
                        } while (local50 > 0);
                    }
                    local50 = endX - startX & 0x3;
                    if (local50 > 0) {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        do {
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset] = local64;
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                            local50--;
                        } while (local50 > 0);
                        return;
                    }
                } else {
                    local186 = this.alpha;
                    local191 = 256 - this.alpha;
                    if (local50 > 0) {
                        do {
                            local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                            hsl += local54;
                            local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                            local71 = rowOffset + 1;
                            if (z < depths[local71]) {
                                local233 = dst[local71];
                                dst[local71] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[local71] = z;
                            }
                            local87 = z + zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                local233 = dst[local71];
                                dst[local71] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            local71++;
                            if (local87 < depths[local71]) {
                                local233 = dst[local71];
                                dst[local71] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[local71] = local87;
                            }
                            local87 += zStep;
                            rowOffset = local71 + 1;
                            if (local87 < depths[rowOffset]) {
                                local233 = dst[rowOffset];
                                dst[rowOffset] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                                depths[rowOffset] = local87;
                            }
                            z = local87 + zStep;
                            local50--;
                        } while (local50 > 0);
                    }
                    local50 = endX - startX & 0x3;
                    if (local50 <= 0) {
                        return;
                    }
                    local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                    local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local233 = dst[rowOffset];
                            dst[rowOffset] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        local50--;
                    } while (local50 > 0);
                }
            } else {
                local50 = endX - startX;
                if (this.alpha == 0) {
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = ColourUtils.HSV_TO_RGB[(int) hsl];
                            depths[rowOffset] = z;
                        }
                        z += zStep;
                        hsl += hslStep;
                        local50--;
                    } while (local50 > 0);
                } else {
                    local186 = this.alpha;
                    local191 = 256 - this.alpha;
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                            local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                            local233 = dst[rowOffset];
                            dst[rowOffset] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                            depths[rowOffset] = z;
                        }
                        hsl += hslStep;
                        z += zStep;
                        local50--;
                    } while (local50 > 0);
                }
            }
        } else if (this.fastScanline) {
            local50 = endX - startX >> 2;
            local54 = hslStep * 4.0F;
            if (this.alpha == 0) {
                if (local50 > 0) {
                    do {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        hsl += local54;
                        local71 = rowOffset + 1;
                        if (z < depths[local71]) {
                            dst[local71] = local64;
                        }
                        local87 = z + zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            dst[local71] = local64;
                        }
                        local87 += zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            dst[local71] = local64;
                        }
                        local87 += zStep;
                        rowOffset = local71 + 1;
                        if (local87 < depths[rowOffset]) {
                            dst[rowOffset] = local64;
                        }
                        z = local87 + zStep;
                        local50--;
                    } while (local50 > 0);
                }
                local50 = endX - startX & 0x3;
                if (local50 > 0) {
                    local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                    do {
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = local64;
                        }
                        z += zStep;
                        local50--;
                    } while (local50 > 0);
                    return;
                }
            } else {
                local186 = this.alpha;
                local191 = 256 - this.alpha;
                if (local50 > 0) {
                    do {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        hsl += local54;
                        local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                        local71 = rowOffset + 1;
                        if (z < depths[local71]) {
                            local233 = dst[local71];
                            dst[local71] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        local87 = z + zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            local233 = dst[local71];
                            dst[local71] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        local87 += zStep;
                        local71++;
                        if (local87 < depths[local71]) {
                            local233 = dst[local71];
                            dst[local71] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        local87 += zStep;
                        rowOffset = local71 + 1;
                        if (local87 < depths[rowOffset]) {
                            local233 = dst[rowOffset];
                            dst[rowOffset] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                        }
                        z = local87 + zStep;
                        local50--;
                    } while (local50 > 0);
                }
                local50 = endX - startX & 0x3;
                if (local50 <= 0) {
                    return;
                }
                local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local233 = dst[rowOffset];
                        dst[rowOffset] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                    }
                    z += zStep;
                    local50--;
                } while (local50 > 0);
            }
        } else {
            local50 = endX - startX;
            if (this.alpha == 0) {
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        dst[rowOffset] = ColourUtils.HSV_TO_RGB[(int) hsl];
                    }
                    z += zStep;
                    hsl += hslStep;
                    local50--;
                } while (local50 > 0);
            } else {
                local186 = this.alpha;
                local191 = 256 - this.alpha;
                do {
                    rowOffset++;
                    if (z < depths[rowOffset]) {
                        local64 = ColourUtils.HSV_TO_RGB[(int) hsl];
                        local64 = ((local64 & 0xFF00FF) * local191 >> 8 & 0xFF00FF) + ((local64 & 0xFF00) * local191 >> 8 & 0xFF00);
                        local233 = dst[rowOffset];
                        dst[rowOffset] = (local191 | local233 >> 24) << 24 | local64 + ((local233 & 0xFF00FF) * local186 >> 8 & 0xFF00FF) + ((local233 & 0xFF00) * local186 >> 8 & 0xFF00);
                    }
                    hsl += hslStep;
                    z += zStep;
                    local50--;
                } while (local50 > 0);
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "(FFFFFFFFFI)V")
    public void renderFlatTriangleRgb(@OriginalArg(0) float yA, @OriginalArg(1) float yB, @OriginalArg(2) float yC, @OriginalArg(3) float xA, @OriginalArg(4) float xB, @OriginalArg(5) float xC, @OriginalArg(6) float zA, @OriginalArg(7) float zB, @OriginalArg(8) float zC, @OriginalArg(9) int colour) {
        if (this.wireframe) {
            this.toolkit.line((int) yB, (int) yA, (int) xB, colour, (int) xA);
            this.toolkit.line((int) yC, (int) yB, (int) xC, colour, (int) xB);
            this.toolkit.line((int) yA, (int) yC, (int) xA, colour, (int) xC);
            return;
        }
        @Pc(46) float local46 = xB - xA;
        @Pc(50) float local50 = yB - yA;
        @Pc(54) float local54 = xC - xA;
        @Pc(58) float local58 = yC - yA;
        @Pc(62) float local62 = zB - zA;
        @Pc(66) float local66 = zC - zA;
        @Pc(68) float local68 = 0.0F;
        if (yB != yA) {
            local68 = (xB - xA) / (yB - yA);
        }
        @Pc(82) float local82 = 0.0F;
        if (yC != yB) {
            local82 = (xC - xB) / (yC - yB);
        }
        @Pc(96) float local96 = 0.0F;
        if (yC != yA) {
            local96 = (xA - xC) / (yA - yC);
        }
        @Pc(116) float local116 = local46 * local58 - local54 * local50;
        if (local116 == 0.0F) {
            return;
        }
        @Pc(131) float local131 = (local62 * local58 - local66 * local50) / local116;
        @Pc(141) float local141 = (local66 * local46 - local62 * local54) / local116;
        if (yA <= yB && yA <= yC) {
            if (!(yA >= (float) this.height)) {
                if (yB > (float) this.height) {
                    yB = (float) this.height;
                }
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                zA = zA - local131 * xA + local131;
                if (yB < yC) {
                    xC = xA;
                    if (yA < 0.0F) {
                        xC = xA - local96 * yA;
                        xA -= local68 * yA;
                        zA -= local141 * yA;
                        yA = 0.0F;
                    }
                    if (yB < 0.0F) {
                        xB -= local82 * yB;
                        yB = 0.0F;
                    }
                    if (yA != yB && local96 < local68 || yA == yB && local96 > local82) {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xC, (int) xA, zA, local131);
                            xC += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xC, (int) xB, zA, local131);
                            xC += local96;
                            xB += local82;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yB;
                        yB -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xA, (int) xC, zA, local131);
                            xC += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xB, (int) xC, zA, local131);
                            xC += local96;
                            xB += local82;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xB = xA;
                    if (yA < 0.0F) {
                        xB = xA - local96 * yA;
                        xA -= local68 * yA;
                        zA -= local141 * yA;
                        yA = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local82 * yC;
                        yC = 0.0F;
                    }
                    if ((yA == yC || !(local96 < local68)) && (yA != yC || !(local82 > local68))) {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xA, (int) xB, zA, local131);
                            xB += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xA, (int) xC, zA, local131);
                            xC += local82;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    } else {
                        yB -= yC;
                        yC -= yA;
                        yA = (float) this.lineOffsets[(int) yA];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xB, (int) xA, zA, local131);
                            xB += local96;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                        while (--yB >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yA, colour, (int) xC, (int) xA, zA, local131);
                            xC += local82;
                            xA += local68;
                            zA += local141;
                            yA += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (yB <= yC) {
            if (!(yB >= (float) this.height)) {
                if (yC > (float) this.height) {
                    yC = (float) this.height;
                }
                if (yA > (float) this.height) {
                    yA = (float) this.height;
                }
                zB = zB - local131 * xB + local131;
                if (yC < yA) {
                    xA = xB;
                    if (yB < 0.0F) {
                        xA = xB - local68 * yB;
                        xB -= local82 * yB;
                        zB -= local141 * yB;
                        yB = 0.0F;
                    }
                    if (yC < 0.0F) {
                        xC -= local96 * yC;
                        yC = 0.0F;
                    }
                    if (yB != yC && local68 < local82 || yB == yC && local68 > local96) {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xA, (int) xB, zB, local131);
                            xA += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xA, (int) xC, zB, local131);
                            xA += local68;
                            xC += local96;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yA -= yC;
                        yC -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xB, (int) xA, zB, local131);
                            xA += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xC, (int) xA, zB, local131);
                            xA += local68;
                            xC += local96;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                } else {
                    xC = xB;
                    if (yB < 0.0F) {
                        xC = xB - local68 * yB;
                        xB -= local82 * yB;
                        zB -= local141 * yB;
                        yB = 0.0F;
                    }
                    if (yA < 0.0F) {
                        xA -= local96 * yA;
                        yA = 0.0F;
                    }
                    if (local68 < local82) {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xC, (int) xB, zB, local131);
                            xC += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xA, (int) xB, zB, local131);
                            xA += local96;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    } else {
                        yC -= yA;
                        yA -= yB;
                        yB = (float) this.lineOffsets[(int) yB];
                        while (--yA >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xB, (int) xC, zB, local131);
                            xC += local68;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                        while (--yC >= 0.0F) {
                            this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yB, colour, (int) xB, (int) xA, zB, local131);
                            xA += local96;
                            xB += local82;
                            zB += local141;
                            yB += (float) this.surfaceWidth;
                        }
                    }
                }
            }
        } else if (!(yC >= (float) this.height)) {
            if (yA > (float) this.height) {
                yA = (float) this.height;
            }
            if (yB > (float) this.height) {
                yB = (float) this.height;
            }
            zC = zC - local131 * xC + local131;
            if (yA < yB) {
                xB = xC;
                if (yC < 0.0F) {
                    xB = xC - local82 * yC;
                    xC -= local96 * yC;
                    zC -= local141 * yC;
                    yC = 0.0F;
                }
                if (yA < 0.0F) {
                    xA -= local68 * yA;
                    yA = 0.0F;
                }
                if (local82 < local96) {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xB, (int) xC, zC, local131);
                        xB += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xB, (int) xA, zC, local131);
                        xB += local82;
                        xA += local68;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yB -= yA;
                    yA -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xC, (int) xB, zC, local131);
                        xB += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xA, (int) xB, zC, local131);
                        xB += local82;
                        xA += local68;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                }
            } else {
                xA = xC;
                if (yC < 0.0F) {
                    xA = xC - local82 * yC;
                    xC -= local96 * yC;
                    zC -= local141 * yC;
                    yC = 0.0F;
                }
                if (yB < 0.0F) {
                    xB -= local68 * yB;
                    yB = 0.0F;
                }
                if (local82 < local96) {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xA, (int) xC, zC, local131);
                        xA += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xB, (int) xC, zC, local131);
                        xB += local68;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                } else {
                    yA -= yB;
                    yB -= yC;
                    yC = (float) this.lineOffsets[(int) yC];
                    while (--yB >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xC, (int) xA, zC, local131);
                        xA += local82;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                    while (--yA >= 0.0F) {
                        this.drawFlatSpanRgb(this.raster, this.depthBuffer, (int) yC, colour, (int) xC, (int) xB, zC, local131);
                        xB += local68;
                        xC += local96;
                        zC += local141;
                        yC += (float) this.surfaceWidth;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "([I[FIIIIIFF)V")
    public void drawFlatSpanRgb(@OriginalArg(0) int[] dst, @OriginalArg(1) float[] depths, @OriginalArg(2) int rowOffset, @OriginalArg(3) int colour, @OriginalArg(5) int startX, @OriginalArg(6) int endX, @OriginalArg(7) float z, @OriginalArg(8) float zStep) {
        if (this.clamp) {
            if (endX > this.width) {
                endX = this.width;
            }
            if (startX < 0) {
                startX = 0;
            }
        }
        if (startX >= endX) {
            return;
        }
        rowOffset += startX - 1;
        @Pc(29) int local29 = endX - startX >> 2;
        z += zStep * (float) startX;
        @Pc(278) int local278;
        @Pc(283) int local283;
        @Pc(315) int local315;
        @Pc(47) int local47;
        @Pc(303) int local303;
        @Pc(63) float local63;
        if (this.threadResource.zWrite) {
            if (this.alpha == 0) {
                while (true) {
                    local29--;
                    if (local29 < 0) {
                        local29 = endX - startX & 0x3;
                        while (true) {
                            local29--;
                            if (local29 < 0) {
                                return;
                            }
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset] = colour;
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                        }
                    }
                    local47 = rowOffset + 1;
                    if (z < depths[local47]) {
                        dst[local47] = colour;
                        depths[local47] = z;
                    }
                    local63 = z + zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47] = colour;
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47] = colour;
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    rowOffset = local47 + 1;
                    if (local63 < depths[rowOffset]) {
                        dst[rowOffset] = colour;
                        depths[rowOffset] = local63;
                    }
                    z = local63 + zStep;
                }
            } else if (this.alpha != 254) {
                local278 = this.alpha;
                local283 = 256 - this.alpha;
                local303 = ((colour & 0xFF00FF) * local283 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local283 >> 8 & 0xFF00);
                while (true) {
                    local29--;
                    if (local29 < 0) {
                        local29 = endX - startX & 0x3;
                        while (true) {
                            local29--;
                            if (local29 < 0) {
                                return;
                            }
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                local315 = dst[rowOffset];
                                dst[rowOffset] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                                depths[rowOffset] = z;
                            }
                            z += zStep;
                        }
                    }
                    local47 = rowOffset + 1;
                    if (z < depths[local47]) {
                        local315 = dst[local47];
                        dst[local47] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[local47] = z;
                    }
                    local63 = z + zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        local315 = dst[local47];
                        dst[local47] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        local315 = dst[local47];
                        dst[local47] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[local47] = local63;
                    }
                    local63 += zStep;
                    rowOffset = local47 + 1;
                    if (local63 < depths[rowOffset]) {
                        local315 = dst[rowOffset];
                        dst[rowOffset] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        depths[rowOffset] = local63;
                    }
                    z = local63 + zStep;
                }
            } else if (startX != 0 && endX <= this.width - 1) {
                while (true) {
                    local29--;
                    if (local29 < 0) {
                        local29 = endX - startX & 0x3;
                        while (true) {
                            local29--;
                            if (local29 < 0) {
                                return;
                            }
                            rowOffset++;
                            if (z < depths[rowOffset]) {
                                dst[rowOffset - 1] = dst[rowOffset];
                            }
                            z += zStep;
                        }
                    }
                    local47 = rowOffset + 1;
                    if (z < depths[local47]) {
                        dst[local47 - 1] = dst[local47];
                    }
                    local63 = z + zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47 - 1] = dst[local47];
                    }
                    local63 += zStep;
                    local47++;
                    if (local63 < depths[local47]) {
                        dst[local47 - 1] = dst[local47];
                    }
                    local63 += zStep;
                    rowOffset = local47 + 1;
                    if (local63 < depths[rowOffset]) {
                        dst[rowOffset - 1] = dst[rowOffset];
                    }
                    z = local63 + zStep;
                }
            }
        } else if (this.alpha == 0) {
            while (true) {
                local29--;
                if (local29 < 0) {
                    local29 = endX - startX & 0x3;
                    while (true) {
                        local29--;
                        if (local29 < 0) {
                            return;
                        }
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset] = colour;
                        }
                        z += zStep;
                    }
                }
                local47 = rowOffset + 1;
                if (z < depths[local47]) {
                    dst[local47] = colour;
                }
                local63 = z + zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47] = colour;
                }
                local63 += zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47] = colour;
                }
                local63 += zStep;
                rowOffset = local47 + 1;
                if (local63 < depths[rowOffset]) {
                    dst[rowOffset] = colour;
                }
                z = local63 + zStep;
            }
        } else if (this.alpha != 254) {
            local278 = this.alpha;
            local283 = 256 - this.alpha;
            local303 = ((colour & 0xFF00FF) * local283 >> 8 & 0xFF00FF) + ((colour & 0xFF00) * local283 >> 8 & 0xFF00);
            while (true) {
                local29--;
                if (local29 < 0) {
                    local29 = endX - startX & 0x3;
                    while (true) {
                        local29--;
                        if (local29 < 0) {
                            return;
                        }
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            local315 = dst[rowOffset];
                            dst[rowOffset] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                        }
                        z += zStep;
                    }
                }
                local47 = rowOffset + 1;
                if (z < depths[local47]) {
                    local315 = dst[local47];
                    dst[local47] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                local63 = z + zStep;
                local47++;
                if (local63 < depths[local47]) {
                    local315 = dst[local47];
                    dst[local47] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                local63 += zStep;
                local47++;
                if (local63 < depths[local47]) {
                    local315 = dst[local47];
                    dst[local47] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                local63 += zStep;
                rowOffset = local47 + 1;
                if (local63 < depths[rowOffset]) {
                    local315 = dst[rowOffset];
                    dst[rowOffset] = local303 + ((local315 & 0xFF00FF) * local278 >> 8 & 0xFF00FF) + ((local315 & 0xFF00) * local278 >> 8 & 0xFF00);
                }
                z = local63 + zStep;
            }
        } else if (startX != 0 && endX <= this.width - 1) {
            while (true) {
                local29--;
                if (local29 < 0) {
                    local29 = endX - startX & 0x3;
                    while (true) {
                        local29--;
                        if (local29 < 0) {
                            return;
                        }
                        rowOffset++;
                        if (z < depths[rowOffset]) {
                            dst[rowOffset - 1] = dst[rowOffset];
                        }
                        z += zStep;
                    }
                }
                local47 = rowOffset + 1;
                if (z < depths[local47]) {
                    dst[local47 - 1] = dst[local47];
                }
                local63 = z + zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47 - 1] = dst[local47];
                }
                local63 += zStep;
                local47++;
                if (local63 < depths[local47]) {
                    dst[local47 - 1] = dst[local47];
                }
                local63 += zStep;
                rowOffset = local47 + 1;
                if (local63 < depths[rowOffset]) {
                    dst[rowOffset - 1] = dst[rowOffset];
                }
                z = local63 + zStep;
            }
        }
    }

    @OriginalMember(owner = "client!lb", name = "a", descriptor = "()I")
    public int offsetX() {
        return this.lineOffsets[0] % this.surfaceWidth;
    }
}
