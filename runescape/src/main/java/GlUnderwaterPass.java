import com.jagex.js5.js5;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!tj")
public final class GlUnderwaterPass extends RenderPass {

    @OriginalMember(owner = "client!tj", name = "k", descriptor = "Z")
    public boolean programBound;

    @OriginalMember(owner = "client!tj", name = "q", descriptor = "Z")
    public boolean lit;

    @OriginalMember(owner = "client!tj", name = "u", descriptor = "[F")
    public final float[] waterPlane = new float[4];

    @OriginalMember(owner = "client!tj", name = "A", descriptor = "Z")
    public boolean whiteTextureBound = false;

    @OriginalMember(owner = "client!tj", name = "s", descriptor = "Z")
    public final boolean supported;

    @OriginalMember(owner = "client!tj", name = "y", descriptor = "Lclient!fr;")
    public Class135 groundUnlitProgram;

    @OriginalMember(owner = "client!tj", name = "w", descriptor = "Lclient!fr;")
    public Class135 groundLitProgram;

    @OriginalMember(owner = "client!tj", name = "t", descriptor = "Lclient!fr;")
    public Class135 modelUnlitProgram;

    @OriginalMember(owner = "client!tj", name = "G", descriptor = "Lclient!fr;")
    public Class135 modelLitProgram;

    @OriginalMember(owner = "client!tj", name = "v", descriptor = "Lclient!og;")
    public Interface18 depthRamp;

    private static final int VP_PARAM_FOG = 0;

    private static final int VP_PARAM_WATER_PLANE = 1;

    private static final float FOG_FAR_FRACTION = 0.125F;

    private static final float FOG_NEAR_FRACTION = 0.25F;

    private static final float MAX_BYTE = 255.0F;

    private static final int RAMP_TEXTURE_UNIT = 1;

    private static final int BASE_TEXTURE_UNIT = 0;

    @OriginalMember(owner = "client!tj", name = "<init>", descriptor = "(Lclient!tca;Lclient!sb;)V")
    public GlUnderwaterPass(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) js5 programs) {
        super(toolkit);
        if (programs != null && toolkit.aBoolean708) {
            this.groundUnlitProgram = Static294.method4338(toolkit, programs.getfile("gl", "uw_ground_unlit"));
            this.groundLitProgram = Static294.method4338(toolkit, programs.getfile("gl", "uw_ground_lit"));
            this.modelUnlitProgram = Static294.method4338(toolkit, programs.getfile("gl", "uw_model_unlit"));
            this.modelLitProgram = Static294.method4338(toolkit, programs.getfile("gl", "uw_model_lit"));
            if (this.modelLitProgram != null & this.groundUnlitProgram != null & this.groundLitProgram != null & this.modelUnlitProgram != null) {
                this.depthRamp = super.toolkit.method8034(false, 1, 2, new int[]{0, -1});
                this.depthRamp.method9052(false, false);
                this.supported = true;
            } else {
                this.supported = false;
            }
        } else {
            this.supported = false;
        }
    }

    @OriginalMember(owner = "client!tj", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.supported;
    }

    @OriginalMember(owner = "client!tj", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        if (texture != null) {
            if (this.whiteTextureBound) {
                super.toolkit.method8080(0, Static189.aClass168_2);
                super.toolkit.method8142(Static189.aClass168_2, 0);
                this.whiteTextureBound = false;
            }
            super.toolkit.method8088(texture);
            super.toolkit.method8054(colourOp);
        } else if (!this.whiteTextureBound) {
            super.toolkit.method8088(super.toolkit.anInterface17_3);
            super.toolkit.method8054(1);
            super.toolkit.method8080(0, Static188.aClass168_1);
            super.toolkit.method8142(Static188.aClass168_1, 0);
            this.whiteTextureBound = true;
        }
    }

    @OriginalMember(owner = "client!tj", name = "a", descriptor = "(B)V")
    @Override
    public void onFogChanged() {
        if (this.programBound) {
            @Pc(19) int far = super.toolkit.XA();
            @Pc(23) int near = super.toolkit.i();
            @Pc(34) float fogFar = (float) far - (float) (far - near) * FOG_FAR_FRACTION;
            @Pc(46) float fogNear = -((float) (far - near) * FOG_NEAR_FRACTION) + (float) far;
            OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, VP_PARAM_FOG, fogNear, fogFar, 1.0F / (float) super.toolkit.method8105(), (float) super.toolkit.method8120() / MAX_BYTE);
            super.toolkit.method8138(RAMP_TEXTURE_UNIT);
            super.toolkit.method8112(super.toolkit.method8025());
            super.toolkit.method8138(BASE_TEXTURE_UNIT);
        }
    }

    @OriginalMember(owner = "client!tj", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        super.toolkit.method8138(RAMP_TEXTURE_UNIT);
        super.toolkit.method8088(null);
        super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
        super.toolkit.method8080(0, Static189.aClass168_2);
        super.toolkit.method8080(2, Static454.aClass168_5);
        super.toolkit.method8142(Static189.aClass168_2, 0);
        super.toolkit.method8138(BASE_TEXTURE_UNIT);
        if (this.whiteTextureBound) {
            super.toolkit.method8080(0, Static189.aClass168_2);
            super.toolkit.method8142(Static189.aClass168_2, 0);
            this.whiteTextureBound = false;
        }
        if (this.programBound) {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, 0);
            OpenGL.glDisable(OpenGL.GL_FRAGMENT_PROGRAM_ARB);
            OpenGL.glDisable(OpenGL.GL_VERTEX_PROGRAM_ARB);
            this.programBound = false;
        }
    }

    @OriginalMember(owner = "client!tj", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
    }

    @OriginalMember(owner = "client!tj", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
    }

    @OriginalMember(owner = "client!tj", name = "a", descriptor = "(I)V")
    @Override
    public void onUnderwaterSettingsChanged() {
        @Pc(8) int waterHeight = super.toolkit.method8092();
        @Pc(13) Matrix_Sub1 view = super.toolkit.method8118();
        if (this.lit) {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, ~waterHeight == Integer.MIN_VALUE ? this.groundLitProgram.anInt3106 : this.modelLitProgram.anInt3106);
        } else {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, waterHeight == Integer.MAX_VALUE ? this.groundUnlitProgram.anInt3106 : this.modelUnlitProgram.anInt3106);
        }
        OpenGL.glEnable(OpenGL.GL_VERTEX_PROGRAM_ARB);
        this.programBound = true;
        view.method1879((float) waterHeight, -1.0F, 0.0F, this.waterPlane, 0.0F);
        OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, VP_PARAM_WATER_PLANE, this.waterPlane[0], this.waterPlane[1], this.waterPlane[2], this.waterPlane[3]);
        this.onFogChanged();
    }

    @OriginalMember(owner = "client!tj", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        this.lit = lit;
        super.toolkit.method8138(RAMP_TEXTURE_UNIT);
        super.toolkit.method8088(this.depthRamp);
        super.toolkit.method8094(Static185.aClass121_3, Static725.aClass121_6);
        super.toolkit.method8080(0, Static454.aClass168_5);
        super.toolkit.method8125(Static189.aClass168_2, true, false, 2);
        super.toolkit.method8142(Static188.aClass168_1, 0);
        super.toolkit.method8138(BASE_TEXTURE_UNIT);
        this.onUnderwaterSettingsChanged();
    }
}
