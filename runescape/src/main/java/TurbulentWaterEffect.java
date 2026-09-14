import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Water for cards with ARB vertex programs but no GLSL. The vertex program looks world position up in a table of
 * noise held in the program local parameters and offsets the transformed texture coordinate by it, so the surface
 * ripples without a volume texture.
 */
@OriginalClass("client!wt")
public final class TurbulentWaterEffect extends TextureEffect {

    private static final int NOISE_PARAM_COUNT = 64;

    private static final int TURBULENCE_PARAM = 64;

    private static final int TIME_PARAM = 65;

    private static final int AMBIENT_PARAM = 66;

    private static final int NOISE_ROW_COUNT = 256;

    private static final float NOISE_AMPLITUDE = 0.4F;

    private static final float NOISE_UNIT = 4096.0F;

    private static final int NOISE_PERIOD = 5000;

    private static final int NOISE_STRIDE = 128;

    private static final int ANIMATION_PERIOD = 4000;

    private static final int FRAME_COUNT = 16;

    private static final float LOW_TURBULENCE = 0.025F;

    private static final float MEDIUM_TURBULENCE = 0.05F;

    private static final float HIGH_TURBULENCE = 0.1F;

    private static final int OPAQUE_BLACK = 0xFF000000;

    @OriginalMember(owner = "client!wt", name = "n", descriptor = "I")
    public int noiseUpdatedAt;

    @OriginalMember(owner = "client!wt", name = "q", descriptor = "F")
    public float animationPhase;

    @OriginalMember(owner = "client!wt", name = "f", descriptor = "Lclient!bea;")
    public Class36 displayList;

    @OriginalMember(owner = "client!wt", name = "g", descriptor = "Lclient!sa;")
    public final Class329 textures;

    @OriginalMember(owner = "client!wt", name = "h", descriptor = "Lclient!cn;")
    public Class71 vertexProgram;

    @OriginalMember(owner = "client!wt", name = "e", descriptor = "[F")
    public float[] noise;

    @OriginalMember(owner = "client!wt", name = "<init>", descriptor = "(Lclient!qha;Lclient!sa;)V")
    public TurbulentWaterEffect(@OriginalArg(0) GlToolkit toolkit, @OriginalArg(1) Class329 textures) {
        super(toolkit);
        this.textures = textures;
        if (super.toolkit.aBoolean608 && super.toolkit.anInt8003 >= 2) {
            this.vertexProgram = Static622.method6854("!!ARBvp1.0\nOPTION  ARB_position_invariant;\nATTRIB  iPos         = vertex.position;\nATTRIB  iColour      = vertex.color;\nATTRIB  iTexCoord    = vertex.texcoord[0];\nOUTPUT  oColour      = result.color;\nOUTPUT  oTexCoord0   = result.texcoord[0];\nOUTPUT  oTexCoord1   = result.texcoord[1];\nOUTPUT  oFogCoord    = result.fogcoord;\nPARAM   time         = program.local[65];\nPARAM   turbulence   = program.local[64];\nPARAM   lightAmbient = program.local[66]; \nPARAM   pMatrix[4]   = { state.matrix.projection };\nPARAM   mvMatrix[4]  = { state.matrix.modelview };\nPARAM   ivMatrix[4]  = { state.matrix.texture[1] };\nPARAM   texMatrix[4]  = { state.matrix.texture[0] };\nPARAM   fNoise[64]   = { program.local[0..63] };\nTEMP    noise, viewPos, worldPos, texCoord;\nADDRESS noiseAddr;\nDP4   viewPos.x, mvMatrix[0], iPos;\nDP4   viewPos.y, mvMatrix[1], iPos;\nDP4   viewPos.z, mvMatrix[2], iPos;\nDP4   viewPos.w, mvMatrix[3], iPos;\nMOV   oFogCoord.x, -viewPos.z;\nDP4   worldPos.x, ivMatrix[0], viewPos;\nDP4   worldPos.y, ivMatrix[1], viewPos;\nDP4   worldPos.z, ivMatrix[2], viewPos;\nDP4   worldPos.w, ivMatrix[3], viewPos;\nADD   noise.x, worldPos.x, worldPos.z;SUB   noise.y, worldPos.z, worldPos.x;MUL   noise, noise, 0.0001220703125;\nFRC   noise, noise;\nMUL   noise, noise, 64;\nARL   noiseAddr.x, noise.x;\nMOV   noise.x, fNoise[noiseAddr.x].x;\nARL   noiseAddr.x, noise.y;\nMOV   noise.y, fNoise[noiseAddr.x].y;\nMUL   noise, noise, turbulence.x;\nDP4   texCoord.x, texMatrix[0], iTexCoord;\nDP4   texCoord.y, texMatrix[1], iTexCoord;\nADD   oTexCoord0.xy, texCoord, noise;\nMOV   oTexCoord0.z, 0;\nMOV   oTexCoord0.w, 1;\nMUL   oTexCoord1.xy, texCoord, 0.125;\nMOV   oTexCoord1.zw, time.xxxw;\nMUL   oColour.xyz, iColour, lightAmbient;\nMOV   oColour.w, iColour.w;\nEND", OpenGL.GL_VERTEX_PROGRAM_ARB, super.toolkit, (byte) 122);
            if (this.vertexProgram != null) {
                @Pc(44) int[][] noiseX = Static490.method6551(NOISE_AMPLITUDE);
                @Pc(55) int[][] noiseY = Static490.method6551(NOISE_AMPLITUDE);
                @Pc(57) int index = 0;
                this.noise = new float[NOISE_ROW_COUNT * NOISE_PARAM_COUNT * 2];
                for (@Pc(63) int row = 0; row < NOISE_ROW_COUNT; row++) {
                    @Pc(68) int[] rowX = noiseX[row];
                    @Pc(72) int[] rowY = noiseY[row];
                    for (@Pc(74) int column = 0; column < NOISE_PARAM_COUNT; column++) {
                        this.noise[index++] = (float) rowX[column] / NOISE_UNIT;
                        this.noise[index++] = (float) rowY[column] / NOISE_UNIT;
                    }
                }
                this.buildDisplayList();
            }
        }
    }

    @OriginalMember(owner = "client!wt", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
        if (this.displayList != null) {
            boolean untextured = (effectParam1 & 0x80) != 0;
            boolean animated = (effectParam2 & 0x1) == 1;
            boolean ambientLit = (effectParam1 & 0x40) == 0;
            super.toolkit.method7014(1);
            if (untextured) {
                super.toolkit.method7001(null);
            } else if (!animated) {
                if (this.textures.aBoolean655) {
                    super.toolkit.method7001(this.textures.aClass93_Sub3_2);
                } else {
                    super.toolkit.method7001(this.textures.aClass93_Sub2Array3[0]);
                }
                OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, TIME_PARAM, 0.0F, 0.0F, 0.0F, 1.0F);
            } else if (this.textures.aBoolean655) {
                super.toolkit.method7001(this.textures.aClass93_Sub3_2);
                OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, TIME_PARAM, this.animationPhase, 0.0F, 0.0F, 1.0F);
            } else {
                @Pc(64) int frame = super.toolkit.anInt7987 % ANIMATION_PERIOD * FRAME_COUNT / ANIMATION_PERIOD;
                super.toolkit.method7001(this.textures.aClass93_Sub2Array3[frame]);
                OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, TIME_PARAM, 0.0F, 0.0F, 0.0F, 1.0F);
            }
            super.toolkit.method7014(0);
            if (ambientLit) {
                Static632.aFloatArray70[1] = super.toolkit.aFloat149 * super.toolkit.aFloat143;
                Static632.aFloatArray70[2] = super.toolkit.aFloat149 * super.toolkit.aFloat137;
                Static632.aFloatArray70[0] = super.toolkit.aFloat149 * super.toolkit.aFloat148;
                OpenGL.glProgramLocalParameter4fvARB(OpenGL.GL_VERTEX_PROGRAM_ARB, AMBIENT_PARAM, Static632.aFloatArray70, 0);
            } else {
                OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, AMBIENT_PARAM, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            int turbulenceLevel = effectParam1 & 0x3;
            if (turbulenceLevel == 2) {
                OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, TURBULENCE_PARAM, MEDIUM_TURBULENCE, 1.0F, 1.0F, 1.0F);
            } else if (turbulenceLevel == 3) {
                OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, TURBULENCE_PARAM, HIGH_TURBULENCE, 1.0F, 1.0F, 1.0F);
            } else {
                OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, TURBULENCE_PARAM, LOW_TURBULENCE, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @OriginalMember(owner = "client!wt", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!wt", name = "b", descriptor = "(I)V")
    public void buildDisplayList() {
        this.displayList = new Class36(super.toolkit, DISPLAY_LIST_COUNT);
        this.displayList.method1002(ENABLE_LIST);
        super.toolkit.method7014(1);
        super.toolkit.method6985(OPAQUE_BLACK);
        super.toolkit.method7031(GL_REPLACE, GL_ADD);
        super.toolkit.method7029(0, OpenGL.GL_CONSTANT);
        super.toolkit.method7014(0);
        OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, this.vertexProgram.anInt1805);
        OpenGL.glEnable(OpenGL.GL_VERTEX_PROGRAM_ARB);
        this.displayList.method1004();
        this.displayList.method1002(DISABLE_LIST);
        super.toolkit.method7014(1);
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glLoadIdentity();
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        super.toolkit.method6991(0);
        super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
        super.toolkit.method7014(0);
        OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, 0);
        OpenGL.glDisable(OpenGL.GL_VERTEX_PROGRAM_ARB);
        OpenGL.glDisable(OpenGL.GL_FRAGMENT_PROGRAM_ARB);
        this.displayList.method1004();
    }

    @OriginalMember(owner = "client!wt", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.displayList != null) {
            this.displayList.method1005(DISABLE_LIST);
            super.toolkit.method7014(1);
            super.toolkit.method7001(null);
            super.toolkit.method7014(0);
        }
    }

    @OriginalMember(owner = "client!wt", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        if (this.displayList != null) {
            this.displayList.method1005(ENABLE_LIST);
            super.toolkit.method7014(1);
            OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
            OpenGL.glLoadMatrixf(super.toolkit.aClass73_Sub3_5.method7146(), 0);
            OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
            super.toolkit.method7014(0);
            if (this.noiseUpdatedAt != super.toolkit.anInt7987) {
                @Pc(58) int offset = super.toolkit.anInt7987 % NOISE_PERIOD * NOISE_STRIDE / NOISE_PERIOD;
                for (@Pc(60) int param = 0; param < NOISE_PARAM_COUNT; param++) {
                    OpenGL.glProgramLocalParameter4fvARB(OpenGL.GL_VERTEX_PROGRAM_ARB, param, this.noise, offset);
                    offset += 2;
                }
                if (this.textures.aBoolean655) {
                    this.animationPhase = (float) (super.toolkit.anInt7987 % ANIMATION_PERIOD) / (float) ANIMATION_PERIOD;
                } else {
                    OpenGL.glProgramLocalParameter4fARB(OpenGL.GL_VERTEX_PROGRAM_ARB, TIME_PARAM, 0.0F, 0.0F, 0.0F, 1.0F);
                }
                this.noiseUpdatedAt = super.toolkit.anInt7987;
            }
        }
    }

    @OriginalMember(owner = "client!wt", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method7001(texture);
        super.toolkit.method6991(colourOp);
    }

    @OriginalMember(owner = "client!wt", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
    }
}
