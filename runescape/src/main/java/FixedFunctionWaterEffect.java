import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Water without shader support. An eye linear S coordinate ramps a fade texture with view depth, and where volume
 * textures exist an object linear R coordinate walks the animation through the water normal volume instead of
 * swapping between the sliced frames.
 */
@OriginalClass("client!fe")
public final class FixedFunctionWaterEffect extends TextureEffect {

    private static final int ANIMATION_PERIOD = 4000;

    private static final int FRAME_COUNT = 16;

    private static final float TEXTURE_SCALE = 0.25F;

    @OriginalMember(owner = "client!fe", name = "k", descriptor = "Lclient!bea;")
    public Class36 displayList;

    @OriginalMember(owner = "client!fe", name = "g", descriptor = "Lclient!sa;")
    public final Class329 textures;

    @OriginalMember(owner = "client!fe", name = "r", descriptor = "Lclient!wu;")
    public final Class93_Sub4 fadeRamp;

    @OriginalMember(owner = "client!fe", name = "<init>", descriptor = "(Lclient!qha;Lclient!sa;)V")
    public FixedFunctionWaterEffect(@OriginalArg(0) GlToolkit toolkit, @OriginalArg(1) Class329 textures) {
        super(toolkit);
        this.textures = textures;
        this.buildDisplayList();
        this.fadeRamp = new Class93_Sub4(super.toolkit, OpenGL.GL_ALPHA, 2, new byte[] { 0, -1 }, OpenGL.GL_ALPHA);
        this.fadeRamp.method9448();
    }

    @OriginalMember(owner = "client!fe", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
        super.toolkit.method7031(OpenGL.GL_MODULATE, GL_ADD);
    }

    @OriginalMember(owner = "client!fe", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        if (super.toolkit.anInt8008 > 0) {
            @Pc(15) float depthScale = -0.5F / (float) super.toolkit.anInt8008;
            super.toolkit.method7014(1);
            Static512.aFloatArray49[3] = super.toolkit.aFloat132 * depthScale + 0.25F;
            Static512.aFloatArray49[0] = 0.0F;
            Static512.aFloatArray49[1] = 0.0F;
            Static512.aFloatArray49[2] = depthScale;
            OpenGL.glPushMatrix();
            OpenGL.glLoadIdentity();
            OpenGL.glTexGenfv(OpenGL.GL_S, OpenGL.GL_EYE_PLANE, Static512.aFloatArray49, 0);
            OpenGL.glPopMatrix();
            super.toolkit.method6995(0.5F, (float) super.toolkit.anInt8008);
            super.toolkit.method7001(this.fadeRamp);
            super.toolkit.method7014(0);
        }
        this.displayList.method1005(ENABLE_LIST);
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glPushMatrix();
        OpenGL.glScalef(TEXTURE_SCALE, TEXTURE_SCALE, 1.0F);
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
    }

    @OriginalMember(owner = "client!fe", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
        if ((effectParam1 & 0x1) == 1) {
            if (this.textures.aBoolean655) {
                super.toolkit.method7001(this.textures.aClass93_Sub3_2);
                Static512.aFloatArray49[3] = (float) (super.toolkit.anInt7987 % ANIMATION_PERIOD) / (float) ANIMATION_PERIOD;
                Static512.aFloatArray49[0] = 0.0F;
                Static512.aFloatArray49[2] = 0.0F;
                Static512.aFloatArray49[1] = 0.0F;
                OpenGL.glTexGenfv(OpenGL.GL_R, OpenGL.GL_OBJECT_PLANE, Static512.aFloatArray49, 0);
            } else {
                @Pc(24) int frame = super.toolkit.anInt7987 % ANIMATION_PERIOD * FRAME_COUNT / ANIMATION_PERIOD;
                super.toolkit.method7001(this.textures.aClass93_Sub2Array3[frame]);
            }
        } else if (this.textures.aBoolean655) {
            super.toolkit.method7001(this.textures.aClass93_Sub3_2);
            Static512.aFloatArray49[3] = 0.0F;
            Static512.aFloatArray49[0] = 0.0F;
            Static512.aFloatArray49[2] = 0.0F;
            Static512.aFloatArray49[1] = 0.0F;
            OpenGL.glTexGenfv(OpenGL.GL_R, OpenGL.GL_OBJECT_PLANE, Static512.aFloatArray49, 0);
        } else {
            super.toolkit.method7001(this.textures.aClass93_Sub2Array3[0]);
        }
    }

    @OriginalMember(owner = "client!fe", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        this.displayList.method1005(DISABLE_LIST);
        if (super.toolkit.anInt8008 > 0) {
            super.toolkit.method7014(1);
            super.toolkit.method7001(null);
            super.toolkit.method6995(1.0F, 0.0F);
            super.toolkit.method7014(0);
        }
        super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glPopMatrix();
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
    }

    @OriginalMember(owner = "client!fe", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
    }

    @OriginalMember(owner = "client!fe", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!fe", name = "b", descriptor = "(B)V")
    public void buildDisplayList() {
        this.displayList = new Class36(super.toolkit, DISPLAY_LIST_COUNT);
        this.displayList.method1002(ENABLE_LIST);
        super.toolkit.method7014(1);
        super.toolkit.method7031(GL_ADD, GL_REPLACE);
        super.toolkit.method7021(OpenGL.GL_PREVIOUS, OpenGL.GL_SRC_COLOR, 0);
        OpenGL.glTexGeni(OpenGL.GL_S, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_EYE_LINEAR);
        OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_S);
        super.toolkit.method7014(0);
        OpenGL.glTexEnvf(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_RGB_SCALE, 2.0F);
        if (this.textures.aBoolean655) {
            OpenGL.glTexGeni(OpenGL.GL_R, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_OBJECT_LINEAR);
            OpenGL.glTexGeni(OpenGL.GL_Q, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_OBJECT_LINEAR);
            OpenGL.glTexGenfv(OpenGL.GL_Q, OpenGL.GL_OBJECT_PLANE, new float[] { 0.0F, 0.0F, 0.0F, 1.0F }, 0);
            OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_R);
            OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_Q);
        }
        this.displayList.method1004();
        this.displayList.method1002(DISABLE_LIST);
        super.toolkit.method7014(1);
        super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
        super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_COLOR, 0);
        OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_S);
        super.toolkit.method7014(0);
        OpenGL.glTexEnvf(OpenGL.GL_TEXTURE_ENV, OpenGL.GL_RGB_SCALE, 1.0F);
        if (this.textures.aBoolean655) {
            OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_R);
            OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_Q);
        }
        this.displayList.method1004();
    }
}
