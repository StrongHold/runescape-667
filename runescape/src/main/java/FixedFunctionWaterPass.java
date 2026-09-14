import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!cs")
public final class FixedFunctionWaterPass extends RenderPass {

    @OriginalMember(owner = "client!cs", name = "n", descriptor = "F")
    public float wavePhase = 0.0F;

    @OriginalMember(owner = "client!cs", name = "t", descriptor = "Lclient!ae;")
    public final Class7 water;

    private static final int WAVE_CYCLE_MILLIS = 4000;

    private static final int WAVE_FRAME_COUNT = 16;

    private static final int WAVE_TEXTURE_UNIT = 1;

    private static final int BASE_TEXTURE_UNIT = 0;

    private static final int UNTEXTURED_FLAG = 0x80;

    private static final int ANIMATED_FLAG = 0x1;

    private static final float WAVE_TEXTURE_SCALE = 0.125F;

    private static final int OPAQUE_BLACK = 0xFF000000;

    @OriginalMember(owner = "client!cs", name = "<init>", descriptor = "(Lclient!am;Lclient!ae;)V")
    public FixedFunctionWaterPass(@OriginalArg(0) NativeToolkit toolkit, @OriginalArg(1) Class7 water) {
        super(toolkit);
        this.water = water;
    }

    @OriginalMember(owner = "client!cs", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
        super.toolkit.method8138(WAVE_TEXTURE_UNIT);
        if ((effectParam1 & UNTEXTURED_FLAG) != 0) {
            super.toolkit.method8088(null);
        } else if ((effectParam2 & ANIMATED_FLAG) == 1) {
            if (this.water.aBoolean7) {
                this.wavePhase = (float) (super.toolkit.anInt9164 % WAVE_CYCLE_MILLIS) / (float) WAVE_CYCLE_MILLIS;
                super.toolkit.method8088(this.water.anInterface2_2);
            } else {
                @Pc(37) int frame = super.toolkit.anInt9164 % WAVE_CYCLE_MILLIS * WAVE_FRAME_COUNT / WAVE_CYCLE_MILLIS;
                super.toolkit.method8088(this.water.anInterface18Array2[frame]);
            }
        } else if (this.water.aBoolean7) {
            super.toolkit.method8088(this.water.anInterface2_2);
        } else {
            super.toolkit.method8088(this.water.anInterface18Array2[0]);
        }
        super.toolkit.method8138(BASE_TEXTURE_UNIT);
    }

    @OriginalMember(owner = "client!cs", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method8088(texture);
    }

    @OriginalMember(owner = "client!cs", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        super.toolkit.method8138(WAVE_TEXTURE_UNIT);
        super.toolkit.method8094(Static185.aClass121_3, Static438.aClass121_5);
        super.toolkit.method8125(Static189.aClass168_2, true, false, 0);
        super.toolkit.method8142(Static188.aClass168_1, 0);
        super.toolkit.method8055(0);
        super.toolkit.method8138(BASE_TEXTURE_UNIT);
        super.toolkit.method8112(OPAQUE_BLACK);
        super.toolkit.method8142(Static454.aClass168_5, 0);
        this.onTextureMatrixChanged();
    }

    @OriginalMember(owner = "client!cs", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        super.toolkit.method8138(WAVE_TEXTURE_UNIT);
        super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
        super.toolkit.method8080(0, Static189.aClass168_2);
        super.toolkit.method8142(Static189.aClass168_2, 0);
        super.toolkit.method8055(1);
        super.toolkit.method8088(null);
        super.toolkit.method8138(BASE_TEXTURE_UNIT);
        super.toolkit.method8142(Static189.aClass168_2, 0);
    }

    @OriginalMember(owner = "client!cs", name = "a", descriptor = "(Z)V")
    @Override
    public void onTextureMatrixChanged() {
        if (super.toolkit.method8026() == BASE_TEXTURE_UNIT) {
            @Pc(9) Matrix_Sub1 baseMatrix = super.toolkit.method8066();
            super.toolkit.method8138(WAVE_TEXTURE_UNIT);
            @Pc(19) Matrix_Sub1 waveMatrix = super.toolkit.method8082();
            waveMatrix.apply(baseMatrix);
            waveMatrix.method1896(WAVE_TEXTURE_SCALE, WAVE_TEXTURE_SCALE, 1.0F);
            waveMatrix.method1882(this.wavePhase, 0.0F, 0.0F);
            super.toolkit.method8073(Static104.aClass370_1);
            super.toolkit.method8138(BASE_TEXTURE_UNIT);
        }
    }

    @OriginalMember(owner = "client!cs", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
        super.toolkit.method8094(Static185.aClass121_3, Static209.aClass121_4);
    }

    @OriginalMember(owner = "client!cs", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return this.water.method116();
    }
}
