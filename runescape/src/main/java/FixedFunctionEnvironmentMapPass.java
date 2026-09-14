import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!oo")
public final class FixedFunctionEnvironmentMapPass extends RenderPass {

    @OriginalMember(owner = "client!oo", name = "o", descriptor = "Z")
    public boolean active = false;

    private static final int ENV_MAP_TEXTURE_UNIT = 1;

    private static final int BASE_TEXTURE_UNIT = 0;

    @OriginalMember(owner = "client!oo", name = "<init>", descriptor = "(Lclient!am;)V")
    public FixedFunctionEnvironmentMapPass(@OriginalArg(0) NativeToolkit toolkit) {
        super(toolkit);
    }

    @OriginalMember(owner = "client!oo", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
        super.toolkit.method8094(Static185.aClass121_3, Static209.aClass121_4);
    }

    @OriginalMember(owner = "client!oo", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        if (this.active) {
            super.toolkit.method8138(ENV_MAP_TEXTURE_UNIT);
            super.toolkit.method8097(Static582.aClass172_4);
            super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
            super.toolkit.method8080(2, Static454.aClass168_5);
            super.toolkit.method8142(Static189.aClass168_2, 0);
            super.toolkit.method8031();
            super.toolkit.method8088(null);
            super.toolkit.method8138(BASE_TEXTURE_UNIT);
            this.active = false;
        } else {
            super.toolkit.method8142(Static189.aClass168_2, 0);
        }
        super.toolkit.method8094(Static209.aClass121_4, Static209.aClass121_4);
    }

    @OriginalMember(owner = "client!oo", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method8088(texture);
        super.toolkit.method8054(colourOp);
    }

    @OriginalMember(owner = "client!oo", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!oo", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        @Pc(8) Interface8 envMap = super.toolkit.method8145();
        if (envMap == null || !lit) {
            super.toolkit.method8142(Static207.aClass168_4, 0);
        } else {
            super.toolkit.method8138(ENV_MAP_TEXTURE_UNIT);
            super.toolkit.method8088(envMap);
            super.toolkit.method8097(Static320.aClass172_2);
            super.toolkit.method8138(ENV_MAP_TEXTURE_UNIT);
            super.toolkit.method8094(Static185.aClass121_3, Static725.aClass121_6);
            super.toolkit.method8125(Static188.aClass168_1, true, false, 2);
            super.toolkit.method8142(Static207.aClass168_4, 0);
            @Pc(59) Matrix_Sub1 textureMatrix = super.toolkit.method8082();
            textureMatrix.method1886(super.toolkit.method8068());
            super.toolkit.method8073(Static104.aClass370_1);
            super.toolkit.method8138(BASE_TEXTURE_UNIT);
            this.active = true;
        }
    }

    @OriginalMember(owner = "client!oo", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
    }
}
