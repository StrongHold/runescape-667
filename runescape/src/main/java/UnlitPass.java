import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!jka")
public final class UnlitPass extends RenderPass {

    @OriginalMember(owner = "client!jka", name = "<init>", descriptor = "(Lclient!am;)V")
    public UnlitPass(@OriginalArg(0) NativeToolkit toolkit) {
        super(toolkit);
    }

    @OriginalMember(owner = "client!jka", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
        super.toolkit.method8132(false);
    }

    @OriginalMember(owner = "client!jka", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method8088(texture);
        super.toolkit.method8054(colourOp);
    }

    @OriginalMember(owner = "client!jka", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
    }

    @OriginalMember(owner = "client!jka", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!jka", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
        super.toolkit.method8132(true);
    }

    @OriginalMember(owner = "client!jka", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
    }
}
