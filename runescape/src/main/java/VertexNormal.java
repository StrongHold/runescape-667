import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!um")
public final class VertexNormal {

    @OriginalMember(owner = "client!um", name = "d", descriptor = "I")
    public int magnitude;

    @OriginalMember(owner = "client!um", name = "b", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!um", name = "a", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!um", name = "c", descriptor = "I")
    public int y;

    @OriginalMember(owner = "client!um", name = "<init>", descriptor = "()V")
    public VertexNormal() {
        /* empty */
    }

    @OriginalMember(owner = "client!um", name = "<init>", descriptor = "(Lclient!um;)V")
    public VertexNormal(@OriginalArg(0) VertexNormal other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.magnitude = other.magnitude;
    }
}
