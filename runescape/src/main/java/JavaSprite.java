import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Sprite;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qc")
public abstract class JavaSprite extends Sprite {

    @OriginalMember(owner = "client!qc", name = "j", descriptor = "I")
    protected int bottomMargin;

    @OriginalMember(owner = "client!qc", name = "n", descriptor = "I")
    protected int rightMargin;

    @OriginalMember(owner = "client!qc", name = "v", descriptor = "I")
    protected int leftMargin;

    @OriginalMember(owner = "client!qc", name = "h", descriptor = "I")
    protected int topMargin;

    @OriginalMember(owner = "client!qc", name = "w", descriptor = "[I")
    public int[] savedClip;

    @OriginalMember(owner = "client!qc", name = "o", descriptor = "Lclient!iaa;")
    protected final JavaToolkit toolkit;

    @OriginalMember(owner = "client!qc", name = "z", descriptor = "I")
    public final int anInt9302;

    @OriginalMember(owner = "client!qc", name = "u", descriptor = "I")
    public final int anInt9306;

    @OriginalMember(owner = "client!qc", name = "<init>", descriptor = "(Lclient!iaa;II)V")
    public JavaSprite(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int width, @OriginalArg(2) int height) {
        this.toolkit = toolkit;
        this.anInt9302 = width;
        this.anInt9306 = height;
    }

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "()I")
    @Override
    public final int scaleHeight() {
        return this.topMargin + this.anInt9306 + this.bottomMargin;
    }

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "(IILclient!aa;II)V")
    public abstract void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) ClippingMask mask, @OriginalArg(3) int maskX, @OriginalArg(4) int maskY);

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "([I)V")
    @Override
    public final void projectOffsets(@OriginalArg(0) int[] destination) {
        destination[0] = this.leftMargin;
        destination[1] = this.topMargin;
        destination[2] = this.rightMargin;
        destination[3] = this.bottomMargin;
    }

    @OriginalMember(owner = "client!qc", name = "d", descriptor = "()I")
    @Override
    public final int getHeight() {
        return this.anInt9306;
    }

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "(IIIIIIII)V")
    protected abstract void renderImpl(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode);

    @OriginalMember(owner = "client!qc", name = "b", descriptor = "(IIIIIIIII)V")
    public abstract void method8207(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7);

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "(IIIIIIIII)V")
    public abstract void method8208(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6, @OriginalArg(7) int arg7);

    @OriginalMember(owner = "client!qc", name = "c", descriptor = "()I")
    @Override
    public final int getWidth() {
        return this.anInt9302;
    }

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "(FFFFFFILclient!aa;II)V")
    @Override
    protected final void renderParallelogramImpl(@OriginalArg(0) float centerX, @OriginalArg(1) float centerY, @OriginalArg(2) float x1, @OriginalArg(3) float y1, @OriginalArg(4) float x2, @OriginalArg(5) float y2, @OriginalArg(7) ClippingMask mask, @OriginalArg(8) int maskX, @OriginalArg(9) int maskY) {
        if (this.toolkit.stopped()) {
            throw new IllegalStateException();
        } else if (this.setupParallelogram(centerX, centerY, x1, y1, x2, y2)) {
            @Pc(22) JavaClippingMask clippingMask = (JavaClippingMask) mask;
            this.blitParallelogramMasked(clippingMask.lineOffsets, clippingMask.lineWidths, JavaSpriteBlitState.minX - maskX, -maskY - (JavaSpriteBlitState.negativeHeight - JavaSpriteBlitState.minY));
        }
    }

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "(FFFFFFIIII)V")
    @Override
    protected final void renderImpl(@OriginalArg(0) float centerX, @OriginalArg(1) float centerY, @OriginalArg(2) float x1, @OriginalArg(3) float y1, @OriginalArg(4) float x2, @OriginalArg(5) float y2, @OriginalArg(6) int op, @OriginalArg(7) int colour) {
        if (this.toolkit.stopped()) {
            throw new IllegalStateException();
        } else if (this.setupParallelogram(centerX, centerY, x1, y1, x2, y2)) {
            JavaSpriteBlitState.colour = colour;
            if (op != 1) {
                JavaSpriteBlitState.alpha = colour >>> 24;
                JavaSpriteBlitState.invAlpha = 256 - JavaSpriteBlitState.alpha;
                if (op == 0) {
                    JavaSpriteBlitState.red = colour >> 16 & 0xFF;
                    JavaSpriteBlitState.green = colour >> 8 & 0xFF;
                    JavaSpriteBlitState.blue = colour & 0xFF;
                } else if (op == 2) {
                    JavaSpriteBlitState.lerpAlpha = colour >>> 24;
                    JavaSpriteBlitState.lerpInvAlpha = 256 - JavaSpriteBlitState.lerpAlpha;
                    @Pc(73) int lerpRedBlue = (colour & 0xFF00FF) * JavaSpriteBlitState.lerpInvAlpha & 0xFF00FF00;
                    @Pc(81) int lerpGreen = (colour & 0xFF00) * JavaSpriteBlitState.lerpInvAlpha & 0xFF0000;
                    JavaSpriteBlitState.lerpColour = (lerpRedBlue | lerpGreen) >>> 8;
                }
            }
            if (op == 1) {
                this.blitParallelogram(1);
            } else if (op == 0) {
                this.blitParallelogram(0);
            } else if (op == 3) {
                this.blitParallelogram(3);
            } else if (op == 2) {
                this.blitParallelogram(2);
            }
        }
    }

    @OriginalMember(owner = "client!qc", name = "b", descriptor = "()I")
    @Override
    public final int scaleWidth() {
        return this.leftMargin + this.anInt9302 + this.rightMargin;
    }

    @OriginalMember(owner = "client!qc", name = "c", descriptor = "(IIII)V")
    @Override
    public final void setOffsets(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2) {
        this.leftMargin = x1;
        this.topMargin = y1;
        this.rightMargin = x2;
        this.bottomMargin = y2;
    }

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "(IIIII)V")
    public abstract void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int op, @OriginalArg(3) int color, @OriginalArg(4) int mode);

    @OriginalMember(owner = "client!qc", name = "b", descriptor = "(IIIIIII)V")
    @Override
    public final void renderTiled(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode) {
        if (this.toolkit.stopped()) {
            throw new IllegalStateException();
        }
        if (this.savedClip == null) {
            this.savedClip = new int[4];
        }
        this.toolkit.K(this.savedClip);
        this.toolkit.T(this.toolkit.clipX1, this.toolkit.clipY1, x + width, y + height);
        @Pc(40) int tileWidth = this.scaleWidth();
        @Pc(43) int tileHeight = this.scaleHeight();
        @Pc(51) int columns = (width + tileWidth - 1) / tileWidth;
        @Pc(59) int rows = (height + tileHeight - 1) / tileHeight;
        for (@Pc(61) int row = 0; row < rows; row++) {
            @Pc(66) int offsetY = row * tileHeight;
            for (@Pc(68) int column = 0; column < columns; column++) {
                this.render(x + column * tileWidth, y + offsetY, op, colour, mode);
            }
        }
        this.toolkit.KA(this.savedClip[0], this.savedClip[1], this.savedClip[2], this.savedClip[3]);
    }

    @OriginalMember(owner = "client!qc", name = "b", descriptor = "(II)V")
    protected abstract void blitParallelogram(@OriginalArg(0) int op);

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "([I[III)V")
    protected abstract void blitParallelogramMasked(@OriginalArg(0) int[] lineOffsets, @OriginalArg(1) int[] lineWidths, @OriginalArg(2) int maskOffsetX, @OriginalArg(3) int maskOffsetY);

    @OriginalMember(owner = "client!qc", name = "a", descriptor = "(FFFFFF)Z")
    public boolean setupParallelogram(@OriginalArg(0) float centerX, @OriginalArg(1) float centerY, @OriginalArg(2) float x1, @OriginalArg(3) float y1, @OriginalArg(4) float x2, @OriginalArg(5) float y2) {
        @Pc(8) int scaleWidth = this.leftMargin + this.anInt9302 + this.rightMargin;
        @Pc(17) int scaleHeight = this.topMargin + this.anInt9306 + this.bottomMargin;
        @Pc(34) float local34;
        @Pc(41) float local41;
        @Pc(48) float local48;
        @Pc(55) float local55;
        @Pc(61) float local61;
        @Pc(67) float local67;
        @Pc(73) float local73;
        @Pc(79) float local79;
        if (scaleWidth != this.anInt9302 || scaleHeight != this.anInt9306) {
            local34 = (x1 - centerX) / (float) scaleWidth;
            local41 = (y1 - centerY) / (float) scaleWidth;
            local48 = (x2 - centerX) / (float) scaleHeight;
            local55 = (y2 - centerY) / (float) scaleHeight;
            local61 = local48 * (float) this.topMargin;
            local67 = local55 * (float) this.topMargin;
            local73 = local34 * (float) this.leftMargin;
            local79 = local41 * (float) this.leftMargin;
            @Pc(86) float rightX = -local34 * (float) this.rightMargin;
            @Pc(93) float rightY = -local41 * (float) this.rightMargin;
            @Pc(100) float bottomX = -local48 * (float) this.bottomMargin;
            @Pc(107) float bottomY = -local55 * (float) this.bottomMargin;
            centerX += local73 + local61;
            centerY += local79 + local67;
            x1 += rightX + local61;
            y1 += rightY + local67;
            x2 += local73 + bottomX;
            y2 += local79 + bottomY;
        }
        local34 = x2 + x1 - centerX;
        local41 = y1 + y2 - centerY;
        if (centerX < x1) {
            local48 = centerX;
            local55 = x1;
        } else {
            local48 = x1;
            local55 = centerX;
        }
        if (x2 < local48) {
            local48 = x2;
        }
        if (local34 < local48) {
            local48 = local34;
        }
        if (x2 > local55) {
            local55 = x2;
        }
        if (local34 > local55) {
            local55 = local34;
        }
        if (centerY < y1) {
            local61 = centerY;
            local67 = y1;
        } else {
            local61 = y1;
            local67 = centerY;
        }
        if (y2 < local61) {
            local61 = y2;
        }
        if (local41 < local61) {
            local61 = local41;
        }
        if (y2 > local67) {
            local67 = y2;
        }
        if (local41 > local67) {
            local67 = local41;
        }
        if (local48 < (float) this.toolkit.clipX1) {
            local48 = (float) this.toolkit.clipX1;
        }
        if (local55 > (float) this.toolkit.clipX2) {
            local55 = (float) this.toolkit.clipX2;
        }
        if (local61 < (float) this.toolkit.clipY1) {
            local61 = (float) this.toolkit.clipY1;
        }
        if (local67 > (float) this.toolkit.clipY2) {
            local67 = (float) this.toolkit.clipY2;
        }
        local55 = local48 - local55;
        if (local55 >= 0.0F) {
            return false;
        }
        local67 = local61 - local67;
        if (local67 >= 0.0F) {
            return false;
        }
        JavaSpriteBlitState.dstStride = this.toolkit.surfaceWidth;
        JavaSpriteBlitState.rowOffset = (int) ((float) ((int) local61 * JavaSpriteBlitState.dstStride) + local48);
        local73 = (x1 - centerX) * (y2 - centerY) - (y1 - centerY) * (x2 - centerX);
        local79 = (x2 - centerX) * (y1 - centerY) - (y2 - centerY) * (x1 - centerX);
        JavaSpriteBlitState.duDx = (int) ((y2 - centerY) * 4096.0F * (float) this.anInt9302 / local73);
        JavaSpriteBlitState.dvDx = (int) ((y1 - centerY) * 4096.0F * (float) this.anInt9306 / local79);
        JavaSpriteBlitState.duDy = (int) ((x2 - centerX) * 4096.0F * (float) this.anInt9302 / local79);
        JavaSpriteBlitState.dvDy = (int) ((x1 - centerX) * 4096.0F * (float) this.anInt9306 / local73);
        JavaSpriteBlitState.xBias = (int) (local48 * 16.0F + 8.0F - (centerX + x1 + x2 + local34) / 4.0F * 16.0F);
        JavaSpriteBlitState.yBias = (int) (local61 * 16.0F + 8.0F - (centerY + y1 + y2 + local41) / 4.0F * 16.0F);
        JavaSpriteBlitState.rowU = (this.anInt9302 >> 1 << 12) + (JavaSpriteBlitState.yBias * JavaSpriteBlitState.duDy >> 4);
        JavaSpriteBlitState.rowV = (this.anInt9306 >> 1 << 12) + (JavaSpriteBlitState.yBias * JavaSpriteBlitState.dvDy >> 4);
        JavaSpriteBlitState.uBias = JavaSpriteBlitState.xBias * JavaSpriteBlitState.duDx >> 4;
        JavaSpriteBlitState.vBias = JavaSpriteBlitState.xBias * JavaSpriteBlitState.dvDx >> 4;
        JavaSpriteBlitState.minX = (int) local48;
        JavaSpriteBlitState.negativeWidth = (int) local55;
        JavaSpriteBlitState.minY = (int) local61;
        JavaSpriteBlitState.negativeHeight = (int) local67;
        return true;
    }
}
