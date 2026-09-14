import com.jagex.js5.js5;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!at")
public final class GlTransparentWaterPass extends RenderPass {

    @OriginalMember(owner = "client!at", name = "l", descriptor = "Lclient!tca;")
    public final GlxToolkit glToolkit;

    @OriginalMember(owner = "client!at", name = "s", descriptor = "Lclient!ae;")
    public final Class7 water;

    @OriginalMember(owner = "client!at", name = "o", descriptor = "Lclient!fr;")
    public final Class135 vertexProgram;

    private static final int WAVE_CYCLE_MILLIS = 4000;

    private static final int WAVE_FRAME_COUNT = 16;

    private static final int WAVE_PHASE_PARAM = 0;

    @OriginalMember(owner = "client!at", name = "<init>", descriptor = "(Lclient!tca;Lclient!sb;Lclient!ae;)V")
    public GlTransparentWaterPass(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) js5 programs, @OriginalArg(2) Class7 water) {
        super(toolkit);
        this.glToolkit = toolkit;
        this.water = water;
        if (programs != null && this.water.method116() && this.glToolkit.aBoolean708) {
            this.vertexProgram = Static294.method4338(this.glToolkit, programs.getfile("gl", "transparent_water"));
        } else {
            this.vertexProgram = null;
        }
    }

    @OriginalMember(owner = "client!at", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        super.toolkit.method8080(0, Static189.aClass168_2);
        OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, 0);
        OpenGL.glDisable(OpenGL.GL_FRAGMENT_PROGRAM_ARB);
        OpenGL.glDisable(OpenGL.GL_VERTEX_PROGRAM_ARB);
    }

    @OriginalMember(owner = "client!at", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.vertexProgram != null;
    }

    @OriginalMember(owner = "client!at", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
    }

    @OriginalMember(owner = "client!at", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, this.vertexProgram.anInt3106);
        OpenGL.glEnable(OpenGL.GL_VERTEX_PROGRAM_ARB);
        super.toolkit.method8080(0, Static188.aClass168_1);
    }

    @OriginalMember(owner = "client!at", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
        super.toolkit.method8094(Static438.aClass121_5, Static185.aClass121_3);
    }

    @OriginalMember(owner = "client!at", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
        if (this.water.aBoolean7) {
            @Pc(12) float phase = (float) (super.toolkit.anInt9164 % WAVE_CYCLE_MILLIS) / (float) WAVE_CYCLE_MILLIS;
            super.toolkit.method8088(this.water.anInterface2_2);
            OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, WAVE_PHASE_PARAM, phase, 0.0F, 0.0F, 1.0F);
        } else {
            @Pc(38) int frame = super.toolkit.anInt9164 % WAVE_CYCLE_MILLIS * WAVE_FRAME_COUNT / WAVE_CYCLE_MILLIS;
            super.toolkit.method8088(this.water.anInterface18Array2[frame]);
            OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, WAVE_PHASE_PARAM, 0.0F, 0.0F, 0.0F, 1.0F);
        }
    }
}
