import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Water drawn by a GLSL program. The vertex program derives the texture coordinates from the texture matrix and
 * animates the third coordinate with time, so the normal volume texture supplies the wave motion; the fragment
 * program reflects the environment cube map and adds a sun specular term.
 */
@OriginalClass("client!laa")
public final class ShaderWaterEffect extends TextureEffect {

    private static final int ANIMATION_PERIOD = 40000;

    private static final float SCALE_DIVISOR = 32.0F;

    private static final float BREAK_OFFSET_DIVISOR = 8.0F;

    private static final float SUN_EXPONENT_RANGE = 928.0F;

    private static final float MIN_SUN_EXPONENT = 96.0F;

    @OriginalMember(owner = "client!laa", name = "k", descriptor = "Z")
    public boolean supported = false;

    @OriginalMember(owner = "client!laa", name = "p", descriptor = "Z")
    public boolean active = false;

    @OriginalMember(owner = "client!laa", name = "e", descriptor = "Lclient!sa;")
    public final Class329 textures;

    @OriginalMember(owner = "client!laa", name = "q", descriptor = "Lclient!iha;")
    public Class179 program;

    @OriginalMember(owner = "client!laa", name = "<init>", descriptor = "(Lclient!qha;Lclient!sa;)V")
    public ShaderWaterEffect(@OriginalArg(0) GlToolkit toolkit, @OriginalArg(1) Class329 textures) {
        super(toolkit);
        this.textures = textures;
        if (this.textures.aClass93_Sub3_3 != null && super.toolkit.bf && super.toolkit.aBoolean619) {
            @Pc(34) Class240 vertexShader = Static245.method8629("uniform float time;\nuniform float scale;\nvarying vec3 wvVertex;\nvarying float waterDepth;\nvoid main() {\nwaterDepth = gl_MultiTexCoord0.z;\nvec4 ecVertex = gl_ModelViewMatrix*gl_Vertex;\nwvVertex.x = dot(gl_NormalMatrix[0], ecVertex.xyz);\nwvVertex.y = dot(gl_NormalMatrix[1], ecVertex.xyz);\nwvVertex.z = dot(gl_NormalMatrix[2], ecVertex.xyz);\ngl_TexCoord[0].x = dot(gl_TextureMatrix[0][0], gl_MultiTexCoord0)*scale;\ngl_TexCoord[0].y = dot(gl_TextureMatrix[0][1], gl_MultiTexCoord0)*scale;\ngl_TexCoord[0].z = time;\ngl_TexCoord[0].w = 1.0;\ngl_FogFragCoord = 1.0-clamp((gl_Fog.end+ecVertex.z)*gl_Fog.scale, 0.0, 1.0);\ngl_Position = ftransform();\n}\n", super.toolkit, GL_VERTEX_SHADER_ARB);
            @Pc(43) Class240 fragmentShader = Static245.method8629("varying vec3 wvVertex;\nvarying float waterDepth;\nuniform vec3 sunDir;\nuniform vec4 sunColour;\nuniform float sunExponent;\nuniform float breakWaterDepth;\nuniform float breakWaterOffset;\nuniform sampler3D normalSampler;\nuniform samplerCube envMapSampler;\nvoid main() {\nvec4 wnNormal = texture3D(normalSampler, gl_TexCoord[0].xyz).rbga;\nwnNormal.xyz = 2.0*wnNormal.xyz-1.0;\nvec3 wnVector = normalize(wvVertex);\nvec3 wnReflection = reflect(wnVector, wnNormal.xyz);\nvec3 envColour = textureCube(envMapSampler, wnReflection).rgb;\nvec4 specularColour = sunColour*pow(clamp(-dot(sunDir, wnReflection), 0.0, 1.0), sunExponent);\nfloat shoreFactor = clamp(waterDepth/breakWaterDepth-breakWaterOffset*wnNormal.w, 0.0, 1.0);\nfloat ndote = dot(wnVector, wnNormal.xyz);\nfloat fresnel = pow(1.0-abs(ndote), 2.0);\nvec4 surfaceColour = vec4(envColour, fresnel*shoreFactor)+specularColour*shoreFactor;\ngl_FragColor = vec4(mix(surfaceColour.rgb, gl_Fog.color.rgb, gl_FogFragCoord), surfaceColour.a);\n}\n", super.toolkit, GL_FRAGMENT_SHADER_ARB);
            this.program = Static173.method2691(super.toolkit, new Class240[] { vertexShader, fragmentShader });
            this.supported = this.program != null;
        }
    }

    @OriginalMember(owner = "client!laa", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
        if (!this.active) {
            super.toolkit.method7001(texture);
            super.toolkit.method6991(colourOp);
        }
    }

    @OriginalMember(owner = "client!laa", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.active) {
            super.toolkit.method7014(1);
            super.toolkit.method7001(null);
            super.toolkit.method7014(0);
            super.toolkit.method7001(null);
            OpenGL.glUseProgramObjectARB(0L);
            this.active = false;
        }
    }

    @OriginalMember(owner = "client!laa", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        @Pc(12) Class93_Sub1 environmentMap = super.toolkit.method6963();
        if (this.supported && environmentMap != null) {
            super.toolkit.method7014(1);
            super.toolkit.method7001(environmentMap);
            super.toolkit.method7014(0);
            super.toolkit.method7001(this.textures.aClass93_Sub3_3);
            @Pc(47) long handle = this.program.aLong136;
            OpenGL.glUseProgramObjectARB(handle);
            OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(handle, "normalSampler"), 0);
            OpenGL.glUniform1iARB(OpenGL.glGetUniformLocationARB(handle, "envMapSampler"), 1);
            OpenGL.glUniform3fARB(OpenGL.glGetUniformLocationARB(handle, "sunDir"), -super.toolkit.aFloatArray51[0], -super.toolkit.aFloatArray51[1], -super.toolkit.aFloatArray51[2]);
            OpenGL.glUniform4fARB(OpenGL.glGetUniformLocationARB(handle, "sunColour"), super.toolkit.aFloat148, super.toolkit.aFloat143, super.toolkit.aFloat137, 1.0F);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "sunExponent"), Math.abs(super.toolkit.aFloatArray51[1]) * SUN_EXPONENT_RANGE + MIN_SUN_EXPONENT);
            this.active = true;
        }
    }

    @OriginalMember(owner = "client!laa", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return false;
    }

    @OriginalMember(owner = "client!laa", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
        if (this.active) {
            @Pc(21) int timeScale = 0x1 << (effectParam1 & 0x3);
            @Pc(32) float scale = (float) (0x1 << (effectParam1 >> 3 & 0x7)) / SCALE_DIVISOR;
            @Pc(36) int breakWaterDepth = effectParam2 & 0xFFFF;
            @Pc(45) float breakWaterOffset = (float) (effectParam2 >> 16 & 0x3) / BREAK_OFFSET_DIVISOR;
            @Pc(49) long handle = this.program.aLong136;
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "time"), (float) (super.toolkit.anInt7987 * timeScale % ANIMATION_PERIOD) / (float) ANIMATION_PERIOD);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "scale"), scale);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "breakWaterDepth"), (float) breakWaterDepth);
            OpenGL.glUniform1fARB(OpenGL.glGetUniformLocationARB(handle, "breakWaterOffset"), breakWaterOffset);
        }
    }

    @OriginalMember(owner = "client!laa", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
    }
}
