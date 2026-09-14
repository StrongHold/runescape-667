import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

/**
 * Leaves texture coordinates alone and only suppresses scene lighting, so the texture is drawn at full brightness.
 */
@OriginalClass("client!tfa")
public final class UnlitEffect extends TextureEffect {

    @OriginalMember(owner = "client!tfa", name = "<init>", descriptor = "(Lclient!qha;)V")
    public UnlitEffect(@OriginalArg(0) GlToolkit toolkit) {
        super(toolkit);
    }

    @OriginalMember(owner = "client!tfa", name = "a", descriptor = "(IZ)V")
    @Override
    public void applyTextureCombine(@OriginalArg(1) boolean lit) {
    }

    @OriginalMember(owner = "client!tfa", name = "a", descriptor = "(I)V")
    @Override
    public void disable() {
        super.toolkit.method7040(false);
    }

    @OriginalMember(owner = "client!tfa", name = "b", descriptor = "(ZI)V")
    @Override
    public void enable(@OriginalArg(0) boolean lit) {
        super.toolkit.method7040(true);
    }

    @OriginalMember(owner = "client!tfa", name = "a", descriptor = "(Lclient!kd;II)V")
    @Override
    public void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp) {
        super.toolkit.method7001(texture);
        super.toolkit.method6991(colourOp);
    }

    @OriginalMember(owner = "client!tfa", name = "a", descriptor = "(B)Z")
    @Override
    public boolean isSupported() {
        return true;
    }

    @OriginalMember(owner = "client!tfa", name = "a", descriptor = "(III)V")
    @Override
    public void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1) {
    }
}
