import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

/**
 * A texture coordinate generation and texture environment strategy, chosen per texture by
 * {@code TextureMetrics.effectType} and driven by {@link Class98}.
 *
 * <p>The constants below are OpenGL enums that {@code jaggl.OpenGL} does not declare. That class is bound to a
 * prebuilt native library and cannot be extended, so they are declared here instead.
 */
@OriginalClass("client!ua")
public abstract class TextureEffect {

    protected static final int GL_ADD = 0x104;

    protected static final int GL_REPLACE = 0x1E01;

    protected static final int GL_INTERPOLATE = 0x8575;

    protected static final int GL_PRIMARY_COLOR = 0x8577;

    protected static final int GL_FRAGMENT_SHADER_ARB = 0x8B30;

    protected static final int GL_VERTEX_SHADER_ARB = 0x8B31;

    /**
     * Effects that touch a lot of fixed function state compile it into a pair of display lists, one that enters the
     * effect and one that restores the default state.
     */
    protected static final int DISPLAY_LIST_COUNT = 2;

    protected static final char ENABLE_LIST = 0;

    protected static final char DISABLE_LIST = 1;

    @OriginalMember(owner = "client!ua", name = "d", descriptor = "Lclient!qha;")
    protected final GlToolkit toolkit;

    @OriginalMember(owner = "client!ua", name = "<init>", descriptor = "(Lclient!qha;)V")
    public TextureEffect(@OriginalArg(0) GlToolkit toolkit) {
        this.toolkit = toolkit;
    }

    @OriginalMember(owner = "client!ua", name = "a", descriptor = "(Lclient!kd;II)V")
    public abstract void bindTexture(@OriginalArg(0) Class93 texture, @OriginalArg(1) int colourOp);

    @OriginalMember(owner = "client!ua", name = "a", descriptor = "(I)V")
    public abstract void disable();

    @OriginalMember(owner = "client!ua", name = "a", descriptor = "(III)V")
    public abstract void setEffectParams(@OriginalArg(1) int effectParam2, @OriginalArg(2) int effectParam1);

    @OriginalMember(owner = "client!ua", name = "a", descriptor = "(IZ)V")
    public abstract void applyTextureCombine(@OriginalArg(1) boolean lit);

    @OriginalMember(owner = "client!ua", name = "a", descriptor = "(B)Z")
    public abstract boolean isSupported();

    @OriginalMember(owner = "client!ua", name = "b", descriptor = "(ZI)V")
    public abstract void enable(@OriginalArg(0) boolean lit);
}
