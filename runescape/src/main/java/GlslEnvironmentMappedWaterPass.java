import com.jagex.js5.js5;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!fd")
public final class GlslEnvironmentMappedWaterPass extends RenderPass {

    @OriginalMember(owner = "client!fd", name = "t", descriptor = "Z")
    public boolean active;

    @OriginalMember(owner = "client!fd", name = "s", descriptor = "Lclient!ae;")
    public final Class7 water;

    @OriginalMember(owner = "client!fd", name = "j", descriptor = "Lclient!rda;")
    public Class317 program;

    @OriginalMember(owner = "client!fd", name = "p", descriptor = "Z")
    public final boolean supported;

    private static final int GL_VERTEX_SHADER_ARB = 35633;

    private static final int GL_FRAGMENT_SHADER_ARB = 35632;

    private static final int NORMAL_SAMPLER_UNIT = 0;

    private static final int ENV_MAP_SAMPLER_UNIT = 1;

    private static final int WAVE_CYCLE_MILLIS = 40000;

    private static final int WAVE_SPEED_MASK = 0x3;

    private static final int WAVE_SCALE_SHIFT = 3;

    private static final int WAVE_SCALE_MASK = 0x7;

    private static final float WAVE_SCALE_UNIT = 32.0F;

    private static final int BREAK_DEPTH_MASK = 0xFFFF;

    private static final int BREAK_OFFSET_SHIFT = 16;

    private static final int BREAK_OFFSET_MASK = 0x3;

    private static final float BREAK_OFFSET_UNIT = 8.0F;

    private static final float SUN_EXPONENT_MIN = 96.0F;

    private static final float SUN_EXPONENT_RANGE = 928.0F;

    @OriginalMember(owner = "client!fd", name = "<init>", descriptor = "(Lclient!tca;Lclient!sb;Lclient!ae;)V")
    public GlslEnvironmentMappedWaterPass(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(1) js5 shaders, @OriginalArg(2) Class7 water) {
        super(toolkit);
        this.water = water;
        if (shaders != null && toolkit.aBoolean703 && toolkit.aBoolean707) {
            @Pc(29) Class265 vertexShader = Static34.method884(GL_VERTEX_SHADER_ARB, shaders.getfile("gl", "environment_mapped_water_v"), toolkit);
            @Pc(43) Class265 fragmentShader = Static34.method884(GL_FRAGMENT_SHADER_ARB, shaders.getfile("gl", "environment_mapped_water_f"), toolkit);
            this.program = Static223.method9088(new Class265[]{vertexShader, fragmentShader}, toolkit);
            this.supported = this.program != null && this.water.method115();
        } else {
            this.supported = false;
        }
    }

    @OriginalMember(owner = "client!fd", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
        if (this.active) {
            @Pc(8) int waveSpeed = 0x1 << (effectParam1 & WAVE_SPEED_MASK);
            @Pc(19) float waveScale = (float) (0x1 << (effectParam1 >> WAVE_SCALE_SHIFT & WAVE_SCALE_MASK)) / WAVE_SCALE_UNIT;
            @Pc(23) int breakDepth = effectParam2 & BREAK_DEPTH_MASK;
            @Pc(32) float breakOffset = (float) (effectParam2 >> BREAK_OFFSET_SHIFT & BREAK_OFFSET_MASK) / BREAK_OFFSET_UNIT;
            @Pc(36) long handle = this.program.aLong253;
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "time"), (float) (waveSpeed * super.toolkit.anInt9164 % WAVE_CYCLE_MILLIS) / (float) WAVE_CYCLE_MILLIS);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "scale"), waveScale);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "breakWaterDepth"), (float) breakDepth);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "breakWaterOffset"), breakOffset);
        }
    }

    @OriginalMember(owner = "client!fd", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.supported;
    }

    @OriginalMember(owner = "client!fd", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
    }

    @OriginalMember(owner = "client!fd", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        if (!this.active) {
            super.toolkit.method8088(texture);
            super.toolkit.method8054(colourOp);
        }
    }

    @OriginalMember(owner = "client!fd", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        @Pc(17) Interface8 envMap = super.toolkit.method8145();
        if (this.supported && envMap != null) {
            super.toolkit.method8138(ENV_MAP_SAMPLER_UNIT);
            super.toolkit.method8088(envMap);
            super.toolkit.method8138(NORMAL_SAMPLER_UNIT);
            super.toolkit.method8088(this.water.anInterface2_1);
            @Pc(50) long handle = this.program.aLong253;
            OpenGL.glUseProgramObjectARB(handle);
            OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(handle, "normalSampler"), NORMAL_SAMPLER_UNIT);
            OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(handle, "envMapSampler"), ENV_MAP_SAMPLER_UNIT);
            OpenGL.glUniform3fARB(OpenGL.glGetUniformLocationARB(handle, "sunDir"), -super.toolkit.aFloatArray60[0], -super.toolkit.aFloatArray60[1], -super.toolkit.aFloatArray60[2]);
            OpenGL.glUniform4fARB(OpenGL.glGetUniformLocationARB(handle, "sunColour"), super.toolkit.aFloat191, super.toolkit.aFloat184, super.toolkit.aFloat195, 1.0F);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "sunExponent"), Math.abs(super.toolkit.aFloatArray60[1]) * SUN_EXPONENT_RANGE + SUN_EXPONENT_MIN);
            this.active = true;
        }
    }

    @OriginalMember(owner = "client!fd", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.active) {
            super.toolkit.method8138(ENV_MAP_SAMPLER_UNIT);
            super.toolkit.method8088(null);
            super.toolkit.method8138(NORMAL_SAMPLER_UNIT);
            super.toolkit.method8088(null);
            OpenGL.glUseProgramObjectARB(0L);
            this.active = false;
        }
    }
}
