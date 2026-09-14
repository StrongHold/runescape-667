import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!oj")
public final class NoEffectPass extends RenderPass {

    @OriginalMember(owner = "client!oj", name = "<init>", descriptor = "(Lclient!am;)V")
    public NoEffectPass(@OriginalArg(0) NativeToolkit toolkit) {
        super(toolkit);
    }

    @OriginalMember(owner = "client!oj", name = "a", descriptor = "(ZII)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2) {
    }

    @OriginalMember(owner = "client!oj", name = "a", descriptor = "(ZZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(0) boolean lit) {
    }

    @OriginalMember(owner = "client!oj", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return false;
    }

    @OriginalMember(owner = "client!oj", name = "e", descriptor = "(I)V")
    @Override
    public void disable() {
    }

    @OriginalMember(owner = "client!oj", name = "a", descriptor = "(Lclient!mw;IB)V")
    @Override
    public void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method8088(texture);
        super.toolkit.method8054(colourOp);
    }

    @OriginalMember(owner = "client!oj", name = "a", descriptor = "(IZ)V")
    @Override
    public void enable(@OriginalArg(1) boolean lit) {
    }
}
