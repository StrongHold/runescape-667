import jagdx.IDirect3DBaseTexture;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!pl")
public abstract class D3DTexture {

    protected static final int BASE_LEVEL = 0;

    protected static final int SINGLE_LEVEL = 1;

    /**
     * A level count of zero asks Direct3D for the full mipmap chain down to 1x1.
     */
    protected static final int ALL_LEVELS = 0;

    protected static final int NO_USAGE = 0;

    protected static final int NO_LOCK_FLAGS = 0;

    protected static final int D3DUSAGE_AUTOGENMIPMAP = 0x400;

    protected static final int D3DLOCK_READONLY = 0x10;

    protected static final int D3DFMT_A8R8G8B8 = 21;

    protected static final int D3DPOOL_MANAGED = 1;

    @OriginalMember(owner = "client!pl", name = "c", descriptor = "Lclient!nga;")
    public Class259 filter = Static60.aClass259_3;

    @OriginalMember(owner = "client!pl", name = "e", descriptor = "Lclient!eba;")
    protected final Class92 format;

    @OriginalMember(owner = "client!pl", name = "b", descriptor = "Z")
    public final boolean mipmapped;

    @OriginalMember(owner = "client!pl", name = "a", descriptor = "Lclient!wda;")
    protected final Class397 type;

    @OriginalMember(owner = "client!pl", name = "d", descriptor = "Lclient!kea;")
    protected final D3DToolkit toolkit;

    @OriginalMember(owner = "client!pl", name = "<init>", descriptor = "(Lclient!kea;Lclient!eba;Lclient!wda;ZI)V")
    protected D3DTexture(@OriginalArg(0) D3DToolkit toolkit, @OriginalArg(1) Class92 format, @OriginalArg(2) Class397 type, @OriginalArg(3) boolean mipmapped, @OriginalArg(4) int texelCount) {
        this.format = format;
        this.mipmapped = mipmapped;
        this.type = type;
        this.toolkit = toolkit;
    }

    @OriginalMember(owner = "client!pl", name = "a", descriptor = "(BLclient!nga;)V")
    public void method9041(@OriginalArg(1) Class259 filter) {
        this.filter = filter;
    }

    @OriginalMember(owner = "client!pl", name = "c", descriptor = "(I)Lclient!jagdx/IDirect3DBaseTexture;")
    public abstract IDirect3DBaseTexture getTexture();
}
