import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Sprite;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!mb")
public final class Sprite_Sub2 extends Sprite {

    @OriginalMember(owner = "client!mb", name = "w", descriptor = "Lclient!gb;")
    public Class93_Sub2_Sub1 aClass93_Sub2_Sub1_2;

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "I")
    public int anInt6027 = 0;

    @OriginalMember(owner = "client!mb", name = "i", descriptor = "I")
    public int anInt6031 = 0;

    @OriginalMember(owner = "client!mb", name = "k", descriptor = "I")
    public int anInt6037 = 0;

    @OriginalMember(owner = "client!mb", name = "n", descriptor = "Z")
    public boolean aBoolean456 = false;

    @OriginalMember(owner = "client!mb", name = "v", descriptor = "I")
    public int anInt6040 = 0;

    @OriginalMember(owner = "client!mb", name = "y", descriptor = "I")
    public int anInt6041 = 0;

    @OriginalMember(owner = "client!mb", name = "u", descriptor = "Lclient!qha;")
    public final GlToolkit aClass19_Sub3_28;

    @OriginalMember(owner = "client!mb", name = "c", descriptor = "Lclient!gb;")
    public final Class93_Sub2_Sub1 aClass93_Sub2_Sub1_3;

    @OriginalMember(owner = "client!mb", name = "<init>", descriptor = "(Lclient!qha;IIZ)V")
    public Sprite_Sub2(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) boolean arg3) {
        this.aClass19_Sub3_28 = arg0;
        this.aClass93_Sub2_Sub1_3 = Static709.method9251(arg2, arg1, arg3 ? 6408 : 6407, arg0);
    }

    @OriginalMember(owner = "client!mb", name = "<init>", descriptor = "(Lclient!qha;IIII)V")
    public Sprite_Sub2(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4) {
        this.aClass19_Sub3_28 = arg0;
        this.aClass93_Sub2_Sub1_3 = Static295.method4353(arg1, arg4, arg3, arg2, arg0);
    }

    @OriginalMember(owner = "client!mb", name = "<init>", descriptor = "(Lclient!qha;II[III)V")
    public Sprite_Sub2(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int[] arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        this.aClass19_Sub3_28 = arg0;
        this.aClass93_Sub2_Sub1_3 = Static88.method1706(arg0, arg1, arg5, arg3, arg2, arg4);
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(III)V")
    @Override
    public void copyAlpha(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int channel) {
        OpenGL.glPixelTransferf(OpenGL.GL_RED_SCALE, 0.5F);
        OpenGL.glPixelTransferf(OpenGL.GL_RED_BIAS, 0.499F);
        OpenGL.glPixelTransferf(OpenGL.GL_GREEN_SCALE, 0.5F);
        OpenGL.glPixelTransferf(OpenGL.GL_GREEN_BIAS, 0.499F);
        OpenGL.glPixelTransferf(OpenGL.GL_BLUE_SCALE, 0.5F);
        OpenGL.glPixelTransferf(OpenGL.GL_BLUE_BIAS, 0.499F);
        this.aClass93_Sub2_Sub1_2 = Static295.method4353(0, this.aClass93_Sub2_Sub1_3.anInt3257, this.aClass93_Sub2_Sub1_3.anInt3259, 0, this.aClass19_Sub3_28);
        this.anInt6031 = 3;
        OpenGL.glPixelTransferf(OpenGL.GL_RED_SCALE, 1.0F);
        OpenGL.glPixelTransferf(OpenGL.GL_RED_BIAS, 0.0F);
        OpenGL.glPixelTransferf(OpenGL.GL_GREEN_SCALE, 1.0F);
        OpenGL.glPixelTransferf(OpenGL.GL_GREEN_BIAS, 0.0F);
        OpenGL.glPixelTransferf(OpenGL.GL_BLUE_SCALE, 1.0F);
        OpenGL.glPixelTransferf(OpenGL.GL_BLUE_BIAS, 0.0F);
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(IIIIIIII)V")
    @Override
    protected void renderImpl(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode) {
        this.aClass93_Sub2_Sub1_3.method9438(true);
        this.aClass19_Sub3_28.method7018();
        this.aClass19_Sub3_28.setBlendMode(mode);
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        if (this.aBoolean456) {
            @Pc(52) float local52 = (float) width / (float) this.scaleWidth();
            @Pc(59) float local59 = (float) height / (float) this.scaleHeight();
            @Pc(68) float local68 = (float) x + local52 * (float) this.anInt6041;
            @Pc(77) float local77 = local59 * (float) this.anInt6027 + (float) y;
            @Pc(86) float local86 = (float) this.aClass93_Sub2_Sub1_3.anInt3259 * local52 + local68;
            @Pc(95) float local95 = (float) this.aClass93_Sub2_Sub1_3.anInt3257 * local59 + local77;
            if (this.aClass93_Sub2_Sub1_2 == null) {
                this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
                this.aClass19_Sub3_28.method6991(op);
                OpenGL.glBegin(OpenGL.GL_QUADS);
                OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2f(local68, local77);
                OpenGL.glTexCoord2f(0.0F, 0.0F);
                OpenGL.glVertex2f(local68, local95);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
                OpenGL.glVertex2f(local86, local95);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2f(local86, local77);
                OpenGL.glEnd();
            } else {
                this.method5425(op);
                this.aClass93_Sub2_Sub1_2.method9438(true);
                OpenGL.glBegin(OpenGL.GL_QUADS);
                OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, 0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2f(local68, local77);
                OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, 0.0F, 0.0F);
                OpenGL.glTexCoord2f(0.0F, 0.0F);
                OpenGL.glVertex2f(local68, local95);
                OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
                OpenGL.glVertex2f(local86, local95);
                OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2f(local86, local77);
                OpenGL.glEnd();
                this.method5422();
            }
        } else if (this.aClass93_Sub2_Sub1_2 == null) {
            this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
            this.aClass19_Sub3_28.method6991(op);
            OpenGL.glBegin(OpenGL.GL_QUADS);
            OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glVertex2i(x, y);
            OpenGL.glTexCoord2f(0.0F, 0.0F);
            OpenGL.glVertex2i(x, y + height);
            OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
            OpenGL.glVertex2i(width + x, height + y);
            OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glVertex2i(width + x, y);
            OpenGL.glEnd();
        } else {
            this.method5425(op);
            this.aClass93_Sub2_Sub1_2.method9438(true);
            OpenGL.glBegin(OpenGL.GL_QUADS);
            OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, 0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glVertex2i(x, y);
            OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, 0.0F, 0.0F);
            OpenGL.glTexCoord2f(0.0F, 0.0F);
            OpenGL.glVertex2i(x, y + height);
            OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
            OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
            OpenGL.glVertex2i(x + width, y + height);
            OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glVertex2i(width + x, y);
            OpenGL.glEnd();
            this.method5422();
        }
    }

    @OriginalMember(owner = "client!mb", name = "b", descriptor = "()I")
    @Override
    public int scaleWidth() {
        return this.aClass93_Sub2_Sub1_3.anInt3259 + this.anInt6041 + this.anInt6037;
    }

    @OriginalMember(owner = "client!mb", name = "c", descriptor = "()I")
    @Override
    public int getWidth() {
        return this.aClass93_Sub2_Sub1_3.anInt3259;
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(FFFFFFILclient!aa;II)V")
    @Override
    protected void renderParallelogramImpl(@OriginalArg(0) float centerX, @OriginalArg(1) float centerY, @OriginalArg(2) float x1, @OriginalArg(3) float y1, @OriginalArg(4) float x2, @OriginalArg(5) float y2, @OriginalArg(7) ClippingMask mask, @OriginalArg(8) int maskX, @OriginalArg(9) int maskY) {
        @Pc(7) Class93_Sub2_Sub1 local7 = ((ClippingMask_Sub3) mask).aClass93_Sub2_Sub1_5;
        @Pc(14) float local14;
        @Pc(18) float local18;
        @Pc(25) float local25;
        @Pc(32) float local32;
        if (this.aBoolean456) {
            local14 = (float) this.scaleWidth();
            local18 = (float) this.scaleHeight();
            local25 = (x1 - centerX) / local14;
            local32 = (y1 - centerY) / local14;
            @Pc(39) float local39 = (x2 - centerX) / local18;
            @Pc(46) float local46 = (y2 - centerY) / local18;
            @Pc(52) float local52 = local39 * (float) this.anInt6027;
            @Pc(58) float local58 = (float) this.anInt6027 * local46;
            @Pc(64) float local64 = (float) this.anInt6041 * local25;
            @Pc(70) float local70 = local32 * (float) this.anInt6041;
            @Pc(77) float local77 = -local25 * (float) this.anInt6037;
            @Pc(84) float local84 = (float) this.anInt6037 * -local32;
            @Pc(91) float local91 = (float) this.anInt6040 * -local39;
            y1 = y1 + local84 + local58;
            centerX = local52 + (local64 + centerX);
            @Pc(110) float local110 = -local46 * (float) this.anInt6040;
            x1 = local52 + (local77 + x1);
            x2 = local91 + (x2 + local64);
            centerY = local70 + centerY + local58;
            y2 = local110 + (local70 + y2);
        }
        local14 = x2 + (x1 - centerX);
        local18 = y2 - centerY + y1;
        this.aClass93_Sub2_Sub1_3.method9438(true);
        this.aClass19_Sub3_28.method7018();
        this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
        this.aClass19_Sub3_28.method6991(1);
        this.aClass19_Sub3_28.method7014(1);
        this.aClass19_Sub3_28.method7001(local7);
        this.aClass19_Sub3_28.method7031(8448, 7681);
        this.aClass19_Sub3_28.method7021(34168, 768, 0);
        this.aClass19_Sub3_28.setBlendMode(1);
        local25 = local7.aFloat67 / (float) local7.anInt3259;
        local32 = local7.aFloat68 / (float) local7.anInt3257;
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glColor3f(1.0F, 1.0F, 1.0F);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, 0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, local25 * (centerX - (float) maskX), local7.aFloat68 - (centerY - (float) maskY) * local32);
        OpenGL.glVertex2f(centerX, centerY);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, 0.0F, 0.0F);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, ((float) -maskX + x2) * local25, local7.aFloat68 - local32 * ((float) -maskY + y2));
        OpenGL.glVertex2f(x2, y2);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, ((float) -maskX + local14) * local25, local7.aFloat68 - ((float) -maskY + local18) * local32);
        OpenGL.glVertex2f(local14, local18);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, local25 * ((float) -maskX + x1), local7.aFloat68 - (y1 - (float) maskY) * local32);
        OpenGL.glVertex2f(x1, y1);
        OpenGL.glEnd();
        this.aClass19_Sub3_28.method7021(5890, 768, 0);
        this.aClass19_Sub3_28.method6991(0);
        this.aClass19_Sub3_28.method7001(null);
        this.aClass19_Sub3_28.method7014(0);
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(IILclient!aa;II)V")
    @Override
    public void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) ClippingMask mask, @OriginalArg(3) int maskX, @OriginalArg(4) int maskY) {
        @Pc(6) ClippingMask_Sub3 local6 = (ClippingMask_Sub3) mask;
        @Pc(9) Class93_Sub2_Sub1 local9 = local6.aClass93_Sub2_Sub1_5;
        this.aClass93_Sub2_Sub1_3.method9438(false);
        this.aClass19_Sub3_28.method7018();
        this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
        this.aClass19_Sub3_28.method6991(1);
        this.aClass19_Sub3_28.method7014(1);
        this.aClass19_Sub3_28.method7001(local9);
        this.aClass19_Sub3_28.method7031(8448, 7681);
        this.aClass19_Sub3_28.method7021(34168, 768, 0);
        this.aClass19_Sub3_28.setBlendMode(1);
        @Pc(62) int local62 = x + this.anInt6041;
        @Pc(67) int local67 = y + this.anInt6027;
        @Pc(73) int local73 = this.aClass93_Sub2_Sub1_3.anInt3259 + local62;
        @Pc(79) int local79 = this.aClass93_Sub2_Sub1_3.anInt3257 + local67;
        @Pc(86) float local86 = local9.aFloat67 / (float) local9.anInt3259;
        @Pc(93) float local93 = local9.aFloat68 / (float) local9.anInt3257;
        @Pc(100) float local100 = local86 * (float) (local62 - maskX);
        @Pc(108) float local108 = local86 * (float) (local73 - maskX);
        @Pc(118) float local118 = local9.aFloat68 - (float) (local67 - maskY) * local93;
        @Pc(128) float local128 = local9.aFloat68 - local93 * (float) (local79 - maskY);
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glColor3f(1.0F, 1.0F, 1.0F);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, 0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, local100, local118);
        OpenGL.glVertex2i(local62, local67);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, 0.0F, 0.0F);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, local100, local128);
        OpenGL.glVertex2i(local62, this.aClass93_Sub2_Sub1_3.anInt3257 + local67);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, local108, local128);
        OpenGL.glVertex2i(this.aClass93_Sub2_Sub1_3.anInt3259 + local62, local67 - -this.aClass93_Sub2_Sub1_3.anInt3257);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE0, this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, local108, local118);
        OpenGL.glVertex2i(this.aClass93_Sub2_Sub1_3.anInt3259 + local62, local67);
        OpenGL.glEnd();
        this.aClass19_Sub3_28.method7021(5890, 768, 0);
        this.aClass19_Sub3_28.method6991(0);
        this.aClass19_Sub3_28.method7001(null);
        this.aClass19_Sub3_28.method7014(0);
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "([I)V")
    @Override
    public void projectOffsets(@OriginalArg(0) int[] destination) {
        destination[2] = this.anInt6037;
        destination[3] = this.anInt6040;
        destination[1] = this.anInt6027;
        destination[0] = this.anInt6041;
    }

    @OriginalMember(owner = "client!mb", name = "c", descriptor = "(IIII)V")
    @Override
    public void setOffsets(@OriginalArg(0) int x1, @OriginalArg(1) int y1, @OriginalArg(2) int x2, @OriginalArg(3) int y2) {
        this.anInt6027 = y1;
        this.anInt6041 = x1;
        this.anInt6037 = x2;
        this.anInt6040 = y2;
        this.aBoolean456 = this.anInt6041 != 0 || this.anInt6027 != 0 || this.anInt6037 != 0 || this.anInt6040 != 0;
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "()I")
    @Override
    public int scaleHeight() {
        return this.anInt6040 + this.aClass93_Sub2_Sub1_3.anInt3257 + this.anInt6027;
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(IIIII)V")
    @Override
    public void render(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int op, @OriginalArg(3) int color, @OriginalArg(4) int mode) {
        this.aClass93_Sub2_Sub1_3.method9438(false);
        this.aClass19_Sub3_28.method7018();
        this.aClass19_Sub3_28.setBlendMode(mode);
        OpenGL.glColor4ub((byte) (color >> 16), (byte) (color >> 8), (byte) color, (byte) (color >> 24));
        @Pc(37) int local37 = x + this.anInt6041;
        @Pc(42) int local42 = y + this.anInt6027;
        if (this.aClass93_Sub2_Sub1_2 == null) {
            this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
            this.aClass19_Sub3_28.method6991(op);
            OpenGL.glBegin(OpenGL.GL_QUADS);
            OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glVertex2i(local37, local42);
            OpenGL.glTexCoord2f(0.0F, 0.0F);
            OpenGL.glVertex2i(local37, local42 + this.aClass93_Sub2_Sub1_3.anInt3257);
            OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
            OpenGL.glVertex2i(this.aClass93_Sub2_Sub1_3.anInt3259 + local37, this.aClass93_Sub2_Sub1_3.anInt3257 + local42);
            OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
            OpenGL.glVertex2i(local37 + this.aClass93_Sub2_Sub1_3.anInt3259, local42);
            OpenGL.glEnd();
            return;
        }
        this.method5425(op);
        this.aClass93_Sub2_Sub1_2.method9438(false);
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, 0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glVertex2i(local37, local42);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, 0.0F, 0.0F);
        OpenGL.glTexCoord2f(0.0F, 0.0F);
        OpenGL.glVertex2i(local37, local42 + this.aClass93_Sub2_Sub1_3.anInt3257);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
        OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
        OpenGL.glVertex2i(this.aClass93_Sub2_Sub1_3.anInt3259 + local37, this.aClass93_Sub2_Sub1_3.anInt3257 + local42);
        OpenGL.glMultiTexCoord2f(OpenGL.GL_TEXTURE1, this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glVertex2i(this.aClass93_Sub2_Sub1_3.anInt3259 + local37, local42);
        OpenGL.glEnd();
        this.method5422();
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(FFFFFFIIII)V")
    @Override
    protected void renderImpl(@OriginalArg(0) float x, @OriginalArg(1) float y, @OriginalArg(2) float width, @OriginalArg(3) float height, @OriginalArg(4) float op, @OriginalArg(5) float colour, @OriginalArg(6) int mode, @OriginalArg(7) int filter) {
        @Pc(6) float local6;
        @Pc(10) float local10;
        if (this.aBoolean456) {
            local6 = (float) this.scaleWidth();
            local10 = (float) this.scaleHeight();
            @Pc(16) float local16 = (width - x) / local6;
            @Pc(22) float local22 = (height - y) / local6;
            @Pc(29) float local29 = (op - x) / local10;
            @Pc(36) float local36 = (colour - y) / local10;
            @Pc(42) float local42 = (float) this.anInt6027 * local29;
            @Pc(48) float local48 = (float) this.anInt6027 * local36;
            @Pc(54) float local54 = (float) this.anInt6041 * local16;
            @Pc(60) float local60 = local22 * (float) this.anInt6041;
            @Pc(67) float local67 = (float) this.anInt6037 * -local16;
            @Pc(74) float local74 = (float) this.anInt6037 * -local22;
            @Pc(81) float local81 = -local29 * (float) this.anInt6040;
            y = y + local60 + local48;
            op = local81 + (local54 + op);
            height = local48 + (height + local74);
            x = local54 + x + local42;
            width = local67 + width + local42;
            @Pc(118) float local118 = -local36 * (float) this.anInt6040;
            colour = local118 + (local60 + colour);
        }
        local6 = op + (width - x);
        local10 = colour - y + height;
        this.aClass93_Sub2_Sub1_3.method9438(true);
        this.aClass19_Sub3_28.method7018();
        this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
        this.aClass19_Sub3_28.setBlendMode(1);
        this.aClass19_Sub3_28.method6991(mode);
        OpenGL.glColor4ub((byte) (filter >> 16), (byte) (filter >> 8), (byte) filter, (byte) (filter >> 24));
        OpenGL.glBegin(OpenGL.GL_QUADS);
        OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glVertex2f(x, y);
        OpenGL.glTexCoord2f(0.0F, 0.0F);
        OpenGL.glVertex2f(op, colour);
        OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
        OpenGL.glVertex2f(local6, local10);
        OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
        OpenGL.glVertex2f(width, height);
        OpenGL.glEnd();
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(I)V")
    public void method5422() {
        this.aClass19_Sub3_28.method7014(1);
        this.aClass19_Sub3_28.method7001(null);
        this.aClass19_Sub3_28.method7031(8448, 8448);
        this.aClass19_Sub3_28.method7021(34168, 768, 1);
        this.aClass19_Sub3_28.method7029(0, 5890);
        this.aClass19_Sub3_28.method7014(0);
        this.aClass19_Sub3_28.method7021(34168, 768, 1);
    }

    @OriginalMember(owner = "client!mb", name = "b", descriptor = "(IIIIIII)V")
    @Override
    public void renderTiled(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour, @OriginalArg(6) int mode) {
        @Pc(9) int local9 = width + x;
        this.aClass93_Sub2_Sub1_3.method9438(false);
        @Pc(18) int local18 = height + y;
        this.aClass19_Sub3_28.method7018();
        this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
        this.aClass19_Sub3_28.setBlendMode(mode);
        this.aClass19_Sub3_28.method6991(op);
        OpenGL.glColor4ub((byte) (colour >> 16), (byte) (colour >> 8), (byte) colour, (byte) (colour >> 24));
        if (this.aClass93_Sub2_Sub1_3.aBoolean261 && !this.aBoolean456) {
            @Pc(74) float local74 = this.aClass93_Sub2_Sub1_3.aFloat68 * (float) height / (float) this.aClass93_Sub2_Sub1_3.anInt3257;
            @Pc(86) float local86 = this.aClass93_Sub2_Sub1_3.aFloat67 * (float) width / (float) this.aClass93_Sub2_Sub1_3.anInt3259;
            OpenGL.glBegin(OpenGL.GL_QUADS);
            OpenGL.glTexCoord2f(0.0F, local74);
            OpenGL.glVertex2i(x, y);
            OpenGL.glTexCoord2f(0.0F, 0.0F);
            OpenGL.glVertex2i(x, local18);
            OpenGL.glTexCoord2f(local86, 0.0F);
            OpenGL.glVertex2i(local9, local18);
            OpenGL.glTexCoord2f(local86, local74);
            OpenGL.glVertex2i(local9, y);
            OpenGL.glEnd();
            return;
        }
        OpenGL.glPushMatrix();
        OpenGL.glTranslatef((float) this.anInt6041, (float) this.anInt6027, 0.0F);
        @Pc(126) int local126 = this.scaleWidth();
        @Pc(129) int local129 = this.scaleHeight();
        @Pc(135) int local135 = this.aClass93_Sub2_Sub1_3.anInt3257 + y;
        OpenGL.glBegin(OpenGL.GL_QUADS);
        @Pc(139) int local139 = y;
        @Pc(150) int local150;
        while (local135 <= local18) {
            local150 = x + this.aClass93_Sub2_Sub1_3.anInt3259;
            @Pc(152) int local152 = x;
            while (local150 <= local9) {
                OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local152, local139);
                OpenGL.glTexCoord2f(0.0F, 0.0F);
                OpenGL.glVertex2i(local152, local135);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, 0.0F);
                OpenGL.glVertex2i(local150, local135);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local150, local139);
                local152 += local126;
                local150 += local126;
            }
            if (local152 < local9) {
                @Pc(223) float local223 = (float) (local9 - local152) * this.aClass93_Sub2_Sub1_3.aFloat67 / (float) this.aClass93_Sub2_Sub1_3.anInt3259;
                OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local152, local139);
                OpenGL.glTexCoord2f(0.0F, 0.0F);
                OpenGL.glVertex2i(local152, local135);
                OpenGL.glTexCoord2f(local223, 0.0F);
                OpenGL.glVertex2i(local9, local135);
                OpenGL.glTexCoord2f(local223, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local9, local139);
            }
            local135 += local129;
            local139 += local129;
        }
        if (local18 > local139) {
            @Pc(296) float local296 = this.aClass93_Sub2_Sub1_3.aFloat68 * (float) (this.aClass93_Sub2_Sub1_3.anInt3257 + local139 - local18) / (float) this.aClass93_Sub2_Sub1_3.anInt3257;
            @Pc(302) int local302 = x + this.aClass93_Sub2_Sub1_3.anInt3259;
            local150 = x;
            while (local302 <= local9) {
                OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local150, local139);
                OpenGL.glTexCoord2f(0.0F, local296);
                OpenGL.glVertex2i(local150, local18);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, local296);
                OpenGL.glVertex2i(local302, local18);
                OpenGL.glTexCoord2f(this.aClass93_Sub2_Sub1_3.aFloat67, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local302, local139);
                local150 += local126;
                local302 += local126;
            }
            if (local9 > local150) {
                @Pc(371) float local371 = this.aClass93_Sub2_Sub1_3.aFloat67 * (float) (local9 - local150) / (float) this.aClass93_Sub2_Sub1_3.anInt3259;
                OpenGL.glTexCoord2f(0.0F, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local150, local139);
                OpenGL.glTexCoord2f(0.0F, local296);
                OpenGL.glVertex2i(local150, local18);
                OpenGL.glTexCoord2f(local371, local296);
                OpenGL.glVertex2i(local9, local18);
                OpenGL.glTexCoord2f(local371, this.aClass93_Sub2_Sub1_3.aFloat68);
                OpenGL.glVertex2i(local9, local139);
            }
        }
        OpenGL.glEnd();
        OpenGL.glPopMatrix();
    }

    @OriginalMember(owner = "client!mb", name = "d", descriptor = "()I")
    @Override
    public int getHeight() {
        return this.aClass93_Sub2_Sub1_3.anInt3257;
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(IIIIII)V")
    @Override
    public void copyRect(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int op, @OriginalArg(5) int colour) {
        if (!this.aClass19_Sub3_28.aBoolean604) {
            this.aClass93_Sub2_Sub1_3.method2943(height, x, y, colour, op, width);
            return;
        }
        @Pc(17) int[] local17 = this.aClass19_Sub3_28.na(op, colour, width, height);
        if (local17 == null) {
            return;
        }
        for (@Pc(21) int local21 = 0; local21 < local17.length; local21++) {
            local17[local21] |= 0xFF000000;
        }
        this.method5423(x, y, width, height, local17, width);
        return;
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(IIII[III)V")
    public void method5423(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int[] arg4, @OriginalArg(6) int arg5) {
        this.aClass93_Sub2_Sub1_3.method2942(arg4, arg0, arg3, arg2, arg1, arg5);
    }

    @OriginalMember(owner = "client!mb", name = "a", descriptor = "(ZI)V")
    public void method5425(@OriginalArg(1) int arg0) {
        this.aClass19_Sub3_28.method7014(1);
        this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_3);
        this.aClass19_Sub3_28.method7031(7681, this.aClass19_Sub3_28.method6977(arg0));
        this.aClass19_Sub3_28.method7021(34167, 768, 1);
        this.aClass19_Sub3_28.method7029(0, 34168);
        this.aClass19_Sub3_28.method7014(0);
        this.aClass19_Sub3_28.method7001(this.aClass93_Sub2_Sub1_2);
        this.aClass19_Sub3_28.method7031(7681, 34479);
        this.aClass19_Sub3_28.method7021(34166, 768, 1);
        if (this.anInt6031 == 0) {
            this.aClass19_Sub3_28.method6979(0.5F, 1.0F, 0.0F, 0.5F);
        } else if (this.anInt6031 == 1) {
            this.aClass19_Sub3_28.method6979(0.5F, 0.5F, 0.0F, 1.0F);
        } else if (this.anInt6031 == 2) {
            this.aClass19_Sub3_28.method6979(1.0F, 0.5F, 0.0F, 0.5F);
        } else if (this.anInt6031 == 3) {
            this.aClass19_Sub3_28.method6979(128.5F, 128.5F, 0.0F, 128.5F);
            return;
        }
    }
}
