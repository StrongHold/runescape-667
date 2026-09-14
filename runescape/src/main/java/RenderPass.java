import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

/**
 * GPU state applied around the geometry of one material, chosen by the material's effect type and by what the
 * backend supports.
 *
 * <p>{@link NativeToolkit} keeps at most one pass enabled. It calls {@code enable} when the effect type changes,
 * then {@code applyTextureCombine} and {@code setEffectParams} for every material drawn under that effect,
 * {@code bindTexture} in place of its own texture binding, an {@code on...Changed} hook whenever the matching
 * piece of toolkit state moves, and {@code disable} when another effect type takes over.
 */
@OriginalClass("client!rea")
public abstract class RenderPass {

    @OriginalMember(owner = "client!rea", name = "i", descriptor = "Lclient!am;")
    protected final NativeToolkit toolkit;

    @OriginalMember(owner = "client!rea", name = "<init>", descriptor = "(Lclient!am;)V")
    public RenderPass(@OriginalArg(0) NativeToolkit toolkit) {
        this.toolkit = toolkit;
    }

    @OriginalMember(owner = "client!rea", name = "c", descriptor = "(I)V")
    public void onModelMatrixChanged() {
    }

    @OriginalMember(owner = "client!rea", name = "e", descriptor = "(I)V")
    public abstract void disable();

    @OriginalMember(owner = "client!rea", name = "d", descriptor = "(I)V")
    public void onProjectionChanged() {
    }

    @OriginalMember(owner = "client!rea", name = "b", descriptor = "(I)V")
    public void onCameraChanged() {
    }

    @OriginalMember(owner = "client!rea", name = "b", descriptor = "(B)Z")
    public abstract boolean isSupported();

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(ZII)V")
    public abstract void setEffectParams(@OriginalArg(1) int effectParam1, @OriginalArg(2) int effectParam2);

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(B)V")
    public void onFogChanged() {
    }

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(ZZ)V")
    public abstract void applyTextureCombine(@OriginalArg(0) boolean lit);

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(Lclient!mw;IB)V")
    public abstract void bindTexture(@OriginalArg(0) Interface17 texture, @OriginalArg(1) int colourOp);

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(I)V")
    public void onUnderwaterSettingsChanged() {
    }

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(Z)V")
    public void onTextureMatrixChanged() {
    }

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(IZ)V")
    public abstract void enable(@OriginalArg(1) boolean lit);
}
