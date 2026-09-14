import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Adds a specular highlight by feeding the interpolated normal into a cube map through {@code GL_NORMAL_MAP} texture
 * coordinate generation. Each cube map holds {@code pow(n, exponent)} for one highlight tightness.
 */
@OriginalClass("client!ej")
public final class NormalMapSpecularEffect extends TextureEffect {

    private static final int CUBE_FACE_COUNT = 6;

    private static final int CUBE_FACE_SIZE = 64;

    private static final int CUBE_FACE_TEXELS = CUBE_FACE_SIZE * CUBE_FACE_SIZE;

    private static final double HARD_EXPONENT = 96.0D;

    private static final double MEDIUM_EXPONENT = 36.0D;

    private static final double SOFT_EXPONENT = 12.0D;

    /**
     * The two texture unit path has no unit left to modulate the highlight down, so it is baked in darker.
     */
    private static final int TWO_UNIT_INTENSITY = 48;

    private static final int FULL_INTENSITY = 127;

    private static final float HIGHLIGHT_PITCH_DEGREES = 22.5F;

    @OriginalMember(owner = "client!ej", name = "m", descriptor = "Lclient!bea;")
    public Class36 displayList;

    @OriginalMember(owner = "client!ej", name = "i", descriptor = "Z")
    public boolean active = false;

    @OriginalMember(owner = "client!ej", name = "g", descriptor = "Z")
    public boolean twoUnitPath;

    @OriginalMember(owner = "client!ej", name = "q", descriptor = "[Lclient!ec;")
    public Class93_Sub1[] highlights;

    @OriginalMember(owner = "client!ej", name = "<init>", descriptor = "(Lclient!qha;)V")
    public NormalMapSpecularEffect(@OriginalArg(0) GlToolkit toolkit) {
        super(toolkit);
        if (toolkit.aBoolean598) {
            this.twoUnitPath = toolkit.anInt8003 < 3;
            @Pc(26) int intensity = this.twoUnitPath ? TWO_UNIT_INTENSITY : FULL_INTENSITY;
            @Pc(30) byte[][] soft = new byte[CUBE_FACE_COUNT][CUBE_FACE_TEXELS];
            @Pc(34) byte[][] hard = new byte[CUBE_FACE_COUNT][CUBE_FACE_TEXELS];
            @Pc(38) byte[][] medium = new byte[CUBE_FACE_COUNT][CUBE_FACE_TEXELS];
            @Pc(40) int texel = 0;
            for (@Pc(42) int x = 0; x < CUBE_FACE_SIZE; x++) {
                for (@Pc(45) int y = 0; y < CUBE_FACE_SIZE; y++) {
                    @Pc(55) float u = (float) x * 2.0F / (float) CUBE_FACE_SIZE - 1.0F;
                    @Pc(64) float v = (float) y * 2.0F / (float) CUBE_FACE_SIZE - 1.0F;
                    @Pc(79) float nz = (float) (1.0D / Math.sqrt(v * v + u * u + 1.0F));
                    @Pc(83) float nx = u * nz;
                    @Pc(87) float ny = v * nz;
                    for (@Pc(89) int face = 0; face < CUBE_FACE_COUNT; face++) {
                        @Pc(115) float axis;
                        if (face == 0) {
                            axis = -ny;
                        } else if (face == 1) {
                            axis = ny;
                        } else if (face == 2) {
                            axis = nx;
                        } else if (face == 3) {
                            axis = -nx;
                        } else if (face == 4) {
                            axis = nz;
                        } else {
                            axis = -nz;
                        }
                        @Pc(145) int hardLevel;
                        @Pc(144) int mediumLevel;
                        @Pc(142) int softLevel;
                        if (axis > 0.0F) {
                            hardLevel = (int) (Math.pow(axis, HARD_EXPONENT) * (double) intensity);
                            mediumLevel = (int) ((double) intensity * Math.pow(axis, MEDIUM_EXPONENT));
                            softLevel = (int) ((double) intensity * Math.pow(axis, SOFT_EXPONENT));
                        } else {
                            softLevel = 0;
                            mediumLevel = 0;
                            hardLevel = 0;
                        }
                        hard[face][texel] = (byte) hardLevel;
                        medium[face][texel] = (byte) mediumLevel;
                        soft[face][texel] = (byte) softLevel;
                    }
                    texel++;
                }
            }
            this.highlights = new Class93_Sub1[3];
            this.highlights[0] = new Class93_Sub1(super.toolkit, OpenGL.GL_ALPHA, CUBE_FACE_SIZE, false, hard, OpenGL.GL_ALPHA);
            this.highlights[1] = new Class93_Sub1(super.toolkit, OpenGL.GL_ALPHA, CUBE_FACE_SIZE, false, medium, OpenGL.GL_ALPHA);
            this.highlights[2] = new Class93_Sub1(super.toolkit, OpenGL.GL_ALPHA, CUBE_FACE_SIZE, false, soft, OpenGL.GL_ALPHA);
            this.buildDisplayList();
        }
    }

    @OriginalMember(owner = "client!ej", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!ej", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
        if (this.active) {
            super.toolkit.method7014(1);
            super.toolkit.method7001(this.highlights[effectParam1 - 1]);
            super.toolkit.method7014(0);
        }
    }

    @OriginalMember(owner = "client!ej", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.active) {
            if (!this.twoUnitPath) {
                super.toolkit.method7014(2);
                super.toolkit.method7001(null);
            }
            super.toolkit.method7014(1);
            super.toolkit.method7001(null);
            super.toolkit.method7014(0);
            this.displayList.method1005(DISABLE_LIST);
            this.active = false;
        } else {
            super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
        }
        super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
    }

    @OriginalMember(owner = "client!ej", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method7001(texture);
        super.toolkit.method6991(colourOp);
    }

    @OriginalMember(owner = "client!ej", name = "b", descriptor = "(B)V")
    public void buildDisplayList() {
        this.displayList = new Class36(super.toolkit, DISPLAY_LIST_COUNT);
        this.displayList.method1002(ENABLE_LIST);
        super.toolkit.method7014(1);
        OpenGL.glTexGeni(OpenGL.GL_S, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_NORMAL_MAP);
        OpenGL.glTexGeni(OpenGL.GL_T, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_NORMAL_MAP);
        OpenGL.glTexGeni(OpenGL.GL_R, OpenGL.GL_TEXTURE_GEN_MODE, OpenGL.GL_NORMAL_MAP);
        OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_S);
        OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_T);
        OpenGL.glEnable(OpenGL.GL_TEXTURE_GEN_R);
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glLoadIdentity();
        OpenGL.glRotatef(HIGHLIGHT_PITCH_DEGREES, 1.0F, 0.0F, 0.0F);
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        if (this.twoUnitPath) {
            super.toolkit.method7031(GL_REPLACE, GL_ADD);
            super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_ALPHA, 0);
            super.toolkit.method7029(0, GL_PRIMARY_COLOR);
        } else {
            super.toolkit.method7031(OpenGL.GL_MODULATE, GL_REPLACE);
            super.toolkit.method7021(OpenGL.GL_PREVIOUS, OpenGL.GL_SRC_COLOR, 0);
            super.toolkit.method7014(2);
            super.toolkit.method7031(GL_REPLACE, GL_ADD);
            super.toolkit.method7021(OpenGL.GL_PREVIOUS, OpenGL.GL_SRC_COLOR, 0);
            super.toolkit.method7021(OpenGL.GL_PREVIOUS, OpenGL.GL_SRC_ALPHA, 1);
            super.toolkit.method7029(0, GL_PRIMARY_COLOR);
        }
        super.toolkit.method7014(0);
        this.displayList.method1004();
        this.displayList.method1002(DISABLE_LIST);
        super.toolkit.method7014(1);
        OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_S);
        OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_T);
        OpenGL.glDisable(OpenGL.GL_TEXTURE_GEN_R);
        OpenGL.glMatrixMode(OpenGL.GL_TEXTURE);
        OpenGL.glLoadIdentity();
        OpenGL.glMatrixMode(OpenGL.GL_MODELVIEW);
        if (this.twoUnitPath) {
            super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
            super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_COLOR, 0);
            super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
        } else {
            super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
            super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_COLOR, 0);
            super.toolkit.method7014(2);
            super.toolkit.method7031(OpenGL.GL_MODULATE, OpenGL.GL_MODULATE);
            super.toolkit.method7021(OpenGL.GL_TEXTURE, OpenGL.GL_SRC_COLOR, 0);
            super.toolkit.method7021(OpenGL.GL_PREVIOUS, OpenGL.GL_SRC_COLOR, 1);
            super.toolkit.method7029(0, OpenGL.GL_TEXTURE);
        }
        super.toolkit.method7014(0);
        this.displayList.method1004();
    }

    @OriginalMember(owner = "client!ej", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
        super.toolkit.method7031(GL_REPLACE, OpenGL.GL_MODULATE);
    }

    @OriginalMember(owner = "client!ej", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        if (this.displayList == null || !lit) {
            super.toolkit.method7029(0, OpenGL.GL_PREVIOUS);
        } else {
            if (!this.twoUnitPath) {
                super.toolkit.method7014(2);
                super.toolkit.method7001(super.toolkit.aClass93_Sub2_5);
                super.toolkit.method7014(0);
            }
            this.displayList.method1005(ENABLE_LIST);
            this.active = true;
        }
    }
}
