import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Scrolls a normal pattern across the surface with eye linear S and T texture coordinate generation. The S plane
 * picks the world axis the flow runs along and the T plane carries the scroll offset, while an object linear R
 * coordinate advances the animation through the volume texture when one is available.
 */
@OriginalClass("client!ma")
public final class FlowingWaterEffect extends TextureEffect {

    private static final float FLOW_SPEED_STEP = 5.0E-4F;

    private static final float COARSE_TEXTURE_SCALE = 4.8828125E-4F;

    private static final float FINE_TEXTURE_SCALE = 9.765625E-4F;

    private static final int FRAME_COUNT = 16;

    private static final int FLOW_SPEED_MASK = 0x3;

    private static final int ANIMATION_SPEED_SHIFT = 3;

    private static final int FINE_SCALE_FLAG = 0x40;

    private static final int FLOW_ALONG_X_FLAG = 0x80;

    @OriginalMember(owner = "client!ma", name = "j", descriptor = "Lclient!sa;")
    public final Class329 textures;

    @OriginalMember(owner = "client!ma", name = "k", descriptor = "Lclient!bea;")
    public final Class36 displayList;

    @OriginalMember(owner = "client!ma", name = "<init>", descriptor = "(Lclient!qha;Lclient!sa;)V")
    public FlowingWaterEffect(@OriginalArg(0) GlToolkit toolkit, @OriginalArg(1) Class329 textures) {
        super(toolkit);
        this.textures = textures;
        this.displayList = new Class36(toolkit, DISPLAY_LIST_COUNT);
        this.displayList.method1002(ENABLE_LIST);
        super.toolkit.method7014(1);
        if (this.textures.aBoolean655) {
            OpenGL.glTexGeni(OpenGL.GL_R, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_OBJECT_LINEAR);
            OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_R);
        }
        OpenGL.glTexGeni(OpenGL.GL_S, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_EYE_LINEAR);
        OpenGL.glTexGeni(OpenGL.GL_T, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_EYE_LINEAR);
        OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_S);
        OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_T);
        super.toolkit.method7014(0);
        this.displayList.method1004();
        this.displayList.method1002(DISABLE_LIST);
        super.toolkit.method7014(1);
        if (this.textures.aBoolean655) {
            OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_R);
        }
        OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_S);
        OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_T);
        super.toolkit.method7014(0);
        this.displayList.method1004();
    }

    @OriginalMember(owner = "client!ma", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
    }

    @OriginalMember(owner = "client!ma", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!ma", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method7001(texture);
        super.toolkit.method6991(colourOp);
    }

    @OriginalMember(owner = "client!ma", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        this.displayList.method1005(DISABLE_LIST);
        super.toolkit.method7014(1);
        super.toolkit.method7001(null);
        super.toolkit.method7014(0);
    }

    @OriginalMember(owner = "client!ma", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        this.displayList.method1005(ENABLE_LIST);
        if (this.textures.aBoolean655) {
            super.toolkit.method7014(1);
            super.toolkit.method7001(this.textures.aClass93_Sub3_1);
            super.toolkit.method7014(0);
        }
    }

    @OriginalMember(owner = "client!ma", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
        @Pc(14) float flowSpeed = -FLOW_SPEED_STEP * (float) ((effectParam1 & FLOW_SPEED_MASK) + 1);
        @Pc(25) float animationSpeed = (float) ((effectParam1 >> ANIMATION_SPEED_SHIFT & FLOW_SPEED_MASK) + 1) * FLOW_SPEED_STEP;
        @Pc(37) float textureScale = (effectParam1 & FINE_SCALE_FLAG) == 0 ? COARSE_TEXTURE_SCALE : FINE_TEXTURE_SCALE;
        @Pc(49) boolean flowAlongX = (effectParam1 & FLOW_ALONG_X_FLAG) != 0;
        super.toolkit.method7014(1);
        if (flowAlongX) {
            Static617.aFloatArray69[1] = 0.0F;
            Static617.aFloatArray69[2] = 0.0F;
            Static617.aFloatArray69[0] = textureScale;
            Static617.aFloatArray69[3] = 0.0F;
        } else {
            Static617.aFloatArray69[1] = 0.0F;
            Static617.aFloatArray69[0] = 0.0F;
            Static617.aFloatArray69[3] = 0.0F;
            Static617.aFloatArray69[2] = textureScale;
        }
        OpenGL.glTexGenfv(OpenGL.GL_S, OpenGL.GL_EYE_PLANE, Static617.aFloatArray69, 0);
        Static617.aFloatArray69[2] = 0.0F;
        Static617.aFloatArray69[1] = textureScale;
        Static617.aFloatArray69[0] = 0.0F;
        Static617.aFloatArray69[3] = (float) super.toolkit.anInt7987 * flowSpeed % 1.0F;
        OpenGL.glTexGenfv(OpenGL.GL_T, OpenGL.GL_EYE_PLANE, Static617.aFloatArray69, 0);
        if (this.textures.aBoolean655) {
            Static617.aFloatArray69[3] = (float) super.toolkit.anInt7987 * animationSpeed % 1.0F;
            Static617.aFloatArray69[0] = 0.0F;
            Static617.aFloatArray69[1] = 0.0F;
            Static617.aFloatArray69[2] = 0.0F;
            OpenGL.glTexGenfv(OpenGL.GL_R, OpenGL.GL_OBJECT_PLANE, Static617.aFloatArray69, 0);
        } else {
            @Pc(148) int frame = (int) ((float) super.toolkit.anInt7987 * animationSpeed * (float) FRAME_COUNT);
            super.toolkit.method7001(this.textures.aClass93_Sub2Array4[frame % FRAME_COUNT]);
        }
        super.toolkit.method7014(0);
    }
}
