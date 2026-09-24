import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!fga")
public final class FixedFunctionSpecularPass extends RenderPass {

    @OriginalMember(owner = "client!fga", name = "q", descriptor = "Z")
    public boolean active = false;

    @OriginalMember(owner = "client!fga", name = "s", descriptor = "Z")
    public boolean singleStage;

    @OriginalMember(owner = "client!fga", name = "l", descriptor = "[Lclient!fv;")
    public Interface8[] specularMaps;

    private static final int CUBE_FACE_COUNT = 6;

    private static final int CUBE_FACE_SIZE = 64;

    private static final int CUBE_FACE_TEXELS = CUBE_FACE_SIZE * CUBE_FACE_SIZE;

    private static final int MIN_TEXTURE_UNITS_FOR_TWO_STAGE = 3;

    private static final int SINGLE_STAGE_PEAK_ALPHA = 48;

    private static final int TWO_STAGE_PEAK_ALPHA = 127;

    private static final int ALPHA_SHIFT = 24;

    private static final double SHARP_EXPONENT = 96.0D;

    private static final double MEDIUM_EXPONENT = 36.0D;

    private static final double BROAD_EXPONENT = 12.0D;

    private static final int SPECULAR_MAP_ROTATION = 1024;

    @OriginalMember(owner = "client!fga", name = "<init>", descriptor = "(Lclient!am;)V")
    public FixedFunctionSpecularPass(@OriginalArg(0) NativeToolkit toolkit) {
        super(toolkit);
        if (toolkit.aBoolean685) {
            this.singleStage = toolkit.anInt9184 < MIN_TEXTURE_UNITS_FOR_TWO_STAGE;
            @Pc(28) int peakAlpha = this.singleStage ? SINGLE_STAGE_PEAK_ALPHA : TWO_STAGE_PEAK_ALPHA;
            @Pc(32) int[][] broadFaces = new int[CUBE_FACE_COUNT][CUBE_FACE_TEXELS];
            @Pc(36) int[][] sharpFaces = new int[CUBE_FACE_COUNT][CUBE_FACE_TEXELS];
            @Pc(40) int[][] mediumFaces = new int[CUBE_FACE_COUNT][CUBE_FACE_TEXELS];
            @Pc(42) int texel = 0;
            for (@Pc(44) int row = 0; row < CUBE_FACE_SIZE; row++) {
                for (@Pc(47) int column = 0; column < CUBE_FACE_SIZE; column++) {
                    @Pc(57) float u = (float) column * 2.0F / (float) CUBE_FACE_SIZE - 1.0F;
                    @Pc(66) float v = (float) row * 2.0F / (float) CUBE_FACE_SIZE - 1.0F;
                    @Pc(81) float z = (float) (1.0D / Math.sqrt(v * v + (u * u + 1.0F)));
                    @Pc(85) float y = v * z;
                    @Pc(89) float x = u * z;
                    for (@Pc(91) int face = 0; face < CUBE_FACE_COUNT; face++) {
                        @Pc(97) float facing;
                        if (face == 0) {
                            facing = -x;
                        } else if (face == 1) {
                            facing = x;
                        } else if (face == 2) {
                            facing = y;
                        } else if (face == 3) {
                            facing = -y;
                        } else if (face == 4) {
                            facing = z;
                        } else {
                            facing = -z;
                        }
                        @Pc(147) int sharpAlpha;
                        @Pc(156) int mediumAlpha;
                        @Pc(165) int broadAlpha;
                        if (facing > 0.0F) {
                            sharpAlpha = (int) (Math.pow(facing, SHARP_EXPONENT) * (double) peakAlpha);
                            mediumAlpha = (int) ((double) peakAlpha * Math.pow(facing, MEDIUM_EXPONENT));
                            broadAlpha = (int) ((double) peakAlpha * Math.pow(facing, BROAD_EXPONENT));
                        } else {
                            broadAlpha = 0;
                            mediumAlpha = 0;
                            sharpAlpha = 0;
                        }
                        sharpFaces[face][texel] = sharpAlpha << ALPHA_SHIFT;
                        mediumFaces[face][texel] = mediumAlpha << ALPHA_SHIFT;
                        broadFaces[face][texel] = broadAlpha << ALPHA_SHIFT;
                    }
                    texel++;
                }
            }
            this.specularMaps = new Interface8[3];
            this.specularMaps[0] = super.toolkit.method8063(CUBE_FACE_SIZE, sharpFaces, false);
            this.specularMaps[1] = super.toolkit.method8063(CUBE_FACE_SIZE, mediumFaces, false);
            this.specularMaps[2] = super.toolkit.method8063(CUBE_FACE_SIZE, broadFaces, false);
        }
    }

    @OriginalMember(owner = "client!fga", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method8088(texture);
        super.toolkit.method8054(colourOp);
    }

    @OriginalMember(owner = "client!fga", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        if (this.specularMaps == null || !lit) {
            super.toolkit.method8142(Static207.aClass168_4, 0);
        } else {
            super.toolkit.method8138(1);
            super.toolkit.method8097(Static360.aClass172_3);
            @Pc(33) Matrix_Sub1 textureMatrix = super.toolkit.method8082();
            textureMatrix.makeRotationX(SPECULAR_MAP_ROTATION);
            super.toolkit.method8073(Static104.aClass370_1);
            if (this.singleStage) {
                super.toolkit.method8094(Static185.aClass121_3, Static438.aClass121_5);
                super.toolkit.method8125(Static189.aClass168_2, true, false, 0);
                super.toolkit.method8142(Static207.aClass168_4, 0);
            } else {
                super.toolkit.method8094(Static209.aClass121_4, Static185.aClass121_3);
                super.toolkit.method8080(0, Static188.aClass168_1);
                super.toolkit.method8138(2);
                super.toolkit.method8094(Static185.aClass121_3, Static438.aClass121_5);
                super.toolkit.method8080(0, Static188.aClass168_1);
                super.toolkit.method8125(Static188.aClass168_1, true, false, 1);
                super.toolkit.method8142(Static207.aClass168_4, 0);
                super.toolkit.method8088(super.toolkit.anInterface17_3);
            }
            super.toolkit.method8138(0);
            this.active = true;
        }
    }

    @OriginalMember(owner = "client!fga", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!fga", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
        if (this.active) {
            super.toolkit.method8138(1);
            super.toolkit.method8088(this.specularMaps[effectParam1 - 1]);
            super.toolkit.method8138(0);
        }
    }

    @OriginalMember(owner = "client!fga", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
        super.toolkit.method8094(Static185.aClass121_3, Static209.aClass121_4);
    }

    @OriginalMember(owner = "client!fga", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.active) {
            super.toolkit.method8138(1);
            super.toolkit.method8088(null);
            super.toolkit.method8097(Static582.aClass172_4);
            super.toolkit.method8031();
            if (this.singleStage) {
                super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
                super.toolkit.method8080(0, Static189.aClass168_2);
                super.toolkit.method8142(Static189.aClass168_2, 0);
            } else {
                super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
                super.toolkit.method8080(0, Static189.aClass168_2);
                super.toolkit.method8138(2);
                super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
                super.toolkit.method8080(0, Static189.aClass168_2);
                super.toolkit.method8080(1, Static188.aClass168_1);
                super.toolkit.method8142(Static189.aClass168_2, 0);
                super.toolkit.method8088(null);
            }
            super.toolkit.method8138(0);
            this.active = false;
        } else {
            super.toolkit.method8142(Static189.aClass168_2, 0);
        }
        super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
    }
}
