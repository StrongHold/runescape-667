import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Sinks geometry towards the water plane and fades it out with depth, using an ARB vertex program instead of texture
 * coordinate generation. Depth comes either from the vertex texture coordinate or from a plane equation, and the
 * program comes in a lit and an unlit form, so four programs cover every combination.
 *
 * <p>{@link GlToolkit} drives {@link #method5797()} and {@link #method5798()} directly whenever the water plane or the
 * viewport changes, so both keep their original names.
 */
@OriginalClass("client!nia")
public final class UnderwaterEffect extends TextureEffect {

    private static final int NO_WATER_PLANE = Integer.MAX_VALUE;

    private static final int FOG_PARAM = 0;

    private static final int WATER_PLANE_PARAM = 1;

    private static final float FADE_START_FRACTION = 0.25F;

    private static final float DISTORT_START_FRACTION = 0.125F;

    private static final float BIAS_SCALE = 255.0F;

    @OriginalMember(owner = "client!nia", name = "g", descriptor = "Z")
    public boolean lit;

    @OriginalMember(owner = "client!nia", name = "j", descriptor = "Z")
    public boolean programActive;

    @OriginalMember(owner = "client!nia", name = "i", descriptor = "Z")
    public boolean untexturedBound = false;

    @OriginalMember(owner = "client!nia", name = "s", descriptor = "Lclient!cn;")
    public Class71 vertexDepthProgram;

    @OriginalMember(owner = "client!nia", name = "u", descriptor = "Lclient!cn;")
    public Class71 litVertexDepthProgram;

    @OriginalMember(owner = "client!nia", name = "t", descriptor = "Lclient!cn;")
    public Class71 planeDepthProgram;

    @OriginalMember(owner = "client!nia", name = "q", descriptor = "Lclient!cn;")
    public Class71 litPlaneDepthProgram;

    @OriginalMember(owner = "client!nia", name = "w", descriptor = "Z")
    public final boolean supported;

    @OriginalMember(owner = "client!nia", name = "l", descriptor = "Lclient!rq;")
    public Class93_Sub2 fadeRamp;

    @OriginalMember(owner = "client!nia", name = "<init>", descriptor = "(Lclient!qha;)V")
    public UnderwaterEffect(@OriginalArg(0) GlToolkit toolkit) {
        super(toolkit);
        if (super.toolkit.aBoolean608) {
            this.vertexDepthProgram = Static622.method6854("!!ARBvp1.0\nATTRIB  iPos         = vertex.position;\nATTRIB  iColour      = vertex.color;\nATTRIB  iTexCoord    = vertex.texcoord[0];\nOUTPUT  oPos         = result.position;\nOUTPUT  oColour      = result.color;\nOUTPUT  oTexCoord0   = result.texcoord[0];\nOUTPUT  oTexCoord1   = result.texcoord[1];\nOUTPUT  oFogCoord    = result.fogcoord;\nPARAM   fogParams    = program.local[0];\nPARAM   waterPlane   = program.local[1];\nPARAM   tMatrix[4]   = { state.matrix.texture[0] };\nPARAM   pMatrix[4]   = { state.matrix.projection };\nPARAM   mvMatrix[4]  = { state.matrix.modelview };\nTEMP    viewPos, fogFactor;\nDP4   viewPos.x, mvMatrix[0], iPos;\nDP4   viewPos.y, mvMatrix[1], iPos;\nDP4   viewPos.z, mvMatrix[2], iPos;\nDP4   viewPos.w, mvMatrix[3], iPos;\nSUB   fogFactor.x, -viewPos.z, fogParams.x;\nMUL   fogFactor.x, fogFactor.x, 0.001953125;\nMAD   fogFactor.y, iTexCoord.z, fogParams.z, fogParams.w;\nSUB   fogFactor.z, -viewPos.z, fogParams.y;\nMUL   fogFactor.z, fogFactor.z, 0.00390625;\nMUL   fogFactor.x, fogFactor.x, fogFactor.y;\nMIN   fogFactor, fogFactor, 1;\nMAX   fogFactor, fogFactor, 0;\nMUL   fogFactor.z, fogFactor.z, iTexCoord.z;\nMAD   viewPos.xyz, waterPlane.xyzw, fogFactor.zzzz, viewPos.xyzw;\nMAX   oTexCoord1.xyz, fogFactor.xxxx, fogFactor.yyyy;\nMOV   oTexCoord1.w, 1;\nMOV   oColour, iColour;\nDP4   oPos.x, pMatrix[0], viewPos;\nDP4   oPos.y, pMatrix[1], viewPos;\nDP4   oPos.z, pMatrix[2], viewPos;\nDP4   oPos.w, pMatrix[3], viewPos;\nMOV   oFogCoord.x, viewPos.z;\nDP3   oTexCoord0.x, tMatrix[0], iTexCoord;\nDP3   oTexCoord0.y, tMatrix[1], iTexCoord;\nMOV   oTexCoord0.zw, iTexCoord;\nEND\n", OpenGL.GL_VERTEX_PROGRAM_ARB, super.toolkit, (byte) 101);
            this.litVertexDepthProgram = Static622.method6854("!!ARBvp1.0\nATTRIB  iPos         = vertex.position;\nATTRIB  iNormal      = vertex.normal;\nATTRIB  iColour      = vertex.color;\nATTRIB  iTexCoord    = vertex.texcoord[0];\nOUTPUT  oPos         = result.position;\nOUTPUT  oColour      = result.color;\nOUTPUT  oTexCoord0   = result.texcoord[0];\nOUTPUT  oTexCoord1   = result.texcoord[1];\nOUTPUT  oFogCoord    = result.fogcoord;\nPARAM   fogParams    = program.local[0];\nPARAM   waterPlane   = program.local[1];\nPARAM   tMatrix[4]   = { state.matrix.texture[0] };\nPARAM   pMatrix[4]   = { state.matrix.projection };\nPARAM   mvMatrix[4]  = { state.matrix.modelview };\nTEMP    viewPos, viewNormal, fogFactor, colour, ndotl;\nDP4   viewPos.x, mvMatrix[0], iPos;\nDP4   viewPos.y, mvMatrix[1], iPos;\nDP4   viewPos.z, mvMatrix[2], iPos;\nDP4   viewPos.w, mvMatrix[3], iPos;\nSUB   fogFactor.x, -viewPos.z, fogParams.x;\nMUL   fogFactor.x, fogFactor.x, 0.001953125;\nMAD   fogFactor.y, iTexCoord.z, fogParams.z, fogParams.w;\nSUB   fogFactor.z, -viewPos.z, fogParams.y;\nMUL   fogFactor.z, fogFactor.z, 0.00390625;\nMUL   fogFactor.x, fogFactor.x, fogFactor.y;\nMIN   fogFactor, fogFactor, 1;\nMAX   fogFactor, fogFactor, 0;\nMUL   fogFactor.z, fogFactor.z, iTexCoord.z;\nMAD   viewPos.xyz, waterPlane.xyzw, fogFactor.zzzz, viewPos.xyzw;\nMAX   oTexCoord1.xyz, fogFactor.xxxx, fogFactor.yyyy;\nMOV   oTexCoord1.w, 1;\nDP3   viewNormal.x, mvMatrix[0], iNormal;\nDP3   viewNormal.y, mvMatrix[1], iNormal;\nDP3   viewNormal.z, mvMatrix[2], iNormal;\nDP3   ndotl.x, viewNormal, state.light[0].position;\nDP3   ndotl.y, viewNormal, state.light[1].position;\nMAX   ndotl, ndotl, 0;\nMOV   colour, state.lightmodel.ambient;\nMAD   colour, state.light[0].diffuse, ndotl.xxxx, colour;\nMAD   colour, state.light[1].diffuse, ndotl.yyyy, colour;\nMUL   oColour, iColour, colour;\nDP4   oPos.x, pMatrix[0], viewPos;\nDP4   oPos.y, pMatrix[1], viewPos;\nDP4   oPos.z, pMatrix[2], viewPos;\nDP4   oPos.w, pMatrix[3], viewPos;\nMOV   oFogCoord.x, viewPos.z;\nDP3   oTexCoord0.x, tMatrix[0], iTexCoord;\nDP3   oTexCoord0.y, tMatrix[1], iTexCoord;\nMOV   oTexCoord0.zw, iTexCoord;\nEND\n", OpenGL.GL_VERTEX_PROGRAM_ARB, super.toolkit, (byte) 106);
            this.planeDepthProgram = Static622.method6854("!!ARBvp1.0\nATTRIB  iPos         = vertex.position;\nATTRIB  iColour      = vertex.color;\nATTRIB  iTexCoord    = vertex.texcoord[0];\nOUTPUT  oPos         = result.position;\nOUTPUT  oColour      = result.color;\nOUTPUT  oTexCoord0   = result.texcoord[0];\nOUTPUT  oTexCoord1   = result.texcoord[1];\nOUTPUT  oFogCoord    = result.fogcoord;\nPARAM   fogParams    = program.local[0];\nPARAM   waterPlane   = program.local[1];\nPARAM   pMatrix[4]   = { state.matrix.projection };\nPARAM   mvMatrix[4]  = { state.matrix.modelview };\nPARAM   texMatrix[4] = { state.matrix.texture[0] };\nTEMP    viewPos, fogFactor, depth;\nDP4   viewPos.x, mvMatrix[0], iPos;\nDP4   viewPos.y, mvMatrix[1], iPos;\nDP4   viewPos.z, mvMatrix[2], iPos;\nDP4   viewPos.w, mvMatrix[3], iPos;\nSUB   fogFactor.x, -viewPos.z, fogParams.x;\nMUL   fogFactor.x, fogFactor.x, 0.001953125;\nDP4   depth, waterPlane, viewPos;\nMAD   fogFactor.y, -depth, fogParams.z, fogParams.w;\nSUB   fogFactor.z, -viewPos.z, fogParams.y;\nMUL   fogFactor.z, fogFactor.z, 0.00390625;\nMIN   fogFactor, fogFactor, 1;\nMAX   fogFactor, fogFactor, 0;\nMUL   fogFactor.z, fogFactor.z, -depth;\nMAD   viewPos.xyz, waterPlane.xyzw, fogFactor.zzzz, viewPos.xyzw;\nMAX   oTexCoord1.xyz, fogFactor.xxxx, fogFactor.yyyy;\nMOV   oTexCoord1.w, 1;\nMOV   oColour, iColour;\nDP4   oPos.x, pMatrix[0], viewPos;\nDP4   oPos.y, pMatrix[1], viewPos;\nDP4   oPos.z, pMatrix[2], viewPos;\nDP4   oPos.w, pMatrix[3], viewPos;\nMOV   oFogCoord.x, viewPos.z;\nDP4   oTexCoord0.x, texMatrix[0], iTexCoord;\nDP4   oTexCoord0.y, texMatrix[1], iTexCoord;\nDP4   oTexCoord0.z, texMatrix[2], iTexCoord;\nMOV   oTexCoord0.w, 1;\nEND\n", OpenGL.GL_VERTEX_PROGRAM_ARB, super.toolkit, (byte) 110);
            this.litPlaneDepthProgram = Static622.method6854("!!ARBvp1.0\nATTRIB  iPos         = vertex.position;\nATTRIB  iNormal      = vertex.normal;\nATTRIB  iColour      = vertex.color;\nATTRIB  iTexCoord    = vertex.texcoord[0];\nOUTPUT  oPos         = result.position;\nOUTPUT  oColour      = result.color;\nOUTPUT  oTexCoord0   = result.texcoord[0];\nOUTPUT  oTexCoord1   = result.texcoord[1];\nOUTPUT  oFogCoord    = result.fogcoord;\nPARAM   fogParams    = program.local[0];\nPARAM   waterPlane   = program.local[1];\nPARAM   pMatrix[4]   = { state.matrix.projection };\nPARAM   mvMatrix[4]  = { state.matrix.modelview };\nPARAM   texMatrix[4] = { state.matrix.texture[0] };\nTEMP    viewPos, viewNormal, fogFactor, depth, colour, ndotl;\nDP4   viewPos.x, mvMatrix[0], iPos;\nDP4   viewPos.y, mvMatrix[1], iPos;\nDP4   viewPos.z, mvMatrix[2], iPos;\nDP4   viewPos.w, mvMatrix[3], iPos;\nSUB   fogFactor.x, -viewPos.z, fogParams.x;\nMUL   fogFactor.x, fogFactor.x, 0.001953125;\nDP4   depth, waterPlane, viewPos;\nMAD   fogFactor.y, -depth, fogParams.z, fogParams.w;\nSUB   fogFactor.z, -viewPos.z, fogParams.y;\nMUL   fogFactor.z, fogFactor.z, 0.00390625;\nMIN   fogFactor, fogFactor, 1;\nMAX   fogFactor, fogFactor, 0;\nMUL   fogFactor.z, fogFactor.z, -depth;\nMAD   viewPos.xyz, waterPlane.xyzw, fogFactor.zzzz, viewPos.xyzw;\nMAX   oTexCoord1.xyz, fogFactor.xxxx, fogFactor.yyyy;\nMOV   oTexCoord1.w, 1;\nDP3   viewNormal.x, mvMatrix[0], iNormal;\nDP3   viewNormal.y, mvMatrix[1], iNormal;\nDP3   viewNormal.z, mvMatrix[2], iNormal;\nDP3   ndotl.x, viewNormal, state.light[0].position;\nDP3   ndotl.y, viewNormal, state.light[1].position;\nMAX   ndotl, ndotl, 0;\nMOV   colour, state.lightmodel.ambient;\nMAD   colour, state.light[0].diffuse, ndotl.xxxx, colour;\nMAD   colour, state.light[1].diffuse, ndotl.yyyy, colour;\nMUL   oColour, iColour, colour;\nDP4   oPos.x, pMatrix[0], viewPos;\nDP4   oPos.y, pMatrix[1], viewPos;\nDP4   oPos.z, pMatrix[2], viewPos;\nDP4   oPos.w, pMatrix[3], viewPos;\nMOV   oFogCoord.x, viewPos.z;\nDP4   oTexCoord0.x, texMatrix[0], iTexCoord;\nDP4   oTexCoord0.y, texMatrix[1], iTexCoord;\nDP4   oTexCoord0.z, texMatrix[2], iTexCoord;\nMOV   oTexCoord0.w, 1;\nEND\n", OpenGL.GL_VERTEX_PROGRAM_ARB, super.toolkit, (byte) 116);
            if (this.litPlaneDepthProgram != null & this.litVertexDepthProgram != null & this.vertexDepthProgram != null & this.planeDepthProgram != null) {
                this.fadeRamp = new Class93_Sub2(toolkit, OpenGL.GL_TEXTURE_2D, OpenGL.GL_ALPHA, 2, 1, false, new byte[] { 0, -1 }, OpenGL.GL_ALPHA, false);
                this.fadeRamp.method2946(false, false);
                this.supported = true;
            } else {
                this.supported = false;
            }
        } else {
            this.supported = false;
        }
    }

    @OriginalMember(owner = "client!nia", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
        if (texture != null) {
            if (this.untexturedBound) {
                super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_COLOR, 0);
                super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
                this.untexturedBound = false;
            }
            super.toolkit.method7001(texture);
            super.toolkit.method6991(colourOp);
        } else if (!this.untexturedBound) {
            super.toolkit.method7001(super.toolkit.aClass93_Sub2_5);
            super.toolkit.method6991(1);
            super.toolkit.method7021(OpenGL.GL_PREVIOUS, OpenGL.GL_SRC_COLOR, 0);
            super.toolkit.method7029(0, OpenGL.GL_PREVIOUS);
            this.untexturedBound = true;
        }
    }

    @OriginalMember(owner = "client!nia", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        this.lit = lit;
        super.toolkit.method7014(1);
        super.toolkit.method7001(this.fadeRamp);
        super.toolkit.method7031(GL_REPLACE, GL_INTERPOLATE);
        super.toolkit.method7021(OpenGL.GL_CONSTANT, OpenGL.GL_SRC_COLOR, 0);
        super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_ALPHA, 2);
        super.toolkit.method7029(0, OpenGL.GL_PREVIOUS);
        super.toolkit.method7014(0);
        this.method5797();
    }

    @OriginalMember(owner = "client!nia", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.programActive) {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, 0);
            OpenGL.glDisable(OpenGL.GL_FRAGMENT_PROGRAM_ARB);
            OpenGL.glDisable(OpenGL.GL_VERTEX_PROGRAM_ARB);
            this.programActive = false;
        }
        super.toolkit.method7014(1);
        super.toolkit.method7001(null);
        super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
        super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_COLOR, 0);
        super.toolkit.method7021(OpenGL.GL_CONSTANT, OpenGL.GL_SRC_ALPHA, 2);
        super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
        super.toolkit.method7014(0);
        if (this.untexturedBound) {
            super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_COLOR, 0);
            super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
            this.untexturedBound = false;
        }
    }

    @OriginalMember(owner = "client!nia", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
    }

    @OriginalMember(owner = "client!nia", name = "b", descriptor = "(I)V")
    public void method5797() {
        @Pc(14) Matrix_Sub3 viewMatrix = super.toolkit.aClass73_Sub3_4;
        if (this.lit) {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, super.toolkit.anInt8006 == NO_WATER_PLANE ? this.litVertexDepthProgram.anInt1805 : this.litPlaneDepthProgram.anInt1805);
        } else {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, super.toolkit.anInt8006 == NO_WATER_PLANE ? this.vertexDepthProgram.anInt1805 : this.planeDepthProgram.anInt1805);
        }
        viewMatrix.method7143(0.0F, (float) super.toolkit.anInt8006, -1.0F, 0.0F, Static319.aFloatArray26);
        OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, WATER_PLANE_PARAM, Static319.aFloatArray26[0], Static319.aFloatArray26[1], Static319.aFloatArray26[2], Static319.aFloatArray26[3]);
        OpenGL.glEnable(OpenGL.GL_VERTEX_PROGRAM_ARB);
        this.programActive = true;
        this.method5798();
    }

    @OriginalMember(owner = "client!nia", name = "c", descriptor = "(I)V")
    public void method5798() {
        if (this.programActive) {
            @Pc(19) int farClip = super.toolkit.XA();
            @Pc(23) int nearClip = super.toolkit.i();
            @Pc(34) float distortStart = (float) farClip - (float) (farClip - nearClip) * DISTORT_START_FRACTION;
            @Pc(46) float fadeStart = -((float) (farClip - nearClip) * FADE_START_FRACTION) + (float) farClip;
            OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, FOG_PARAM, fadeStart, distortStart, 1.0F / (float) super.toolkit.anInt8013, (float) super.toolkit.anInt8029 / BIAS_SCALE);
            super.toolkit.method7014(1);
            super.toolkit.method6985(super.toolkit.anInt8026);
            super.toolkit.method7014(0);
        }
    }

    @OriginalMember(owner = "client!nia", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
    }

    @OriginalMember(owner = "client!nia", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.supported;
    }
}
