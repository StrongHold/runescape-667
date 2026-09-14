import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!qd")
public final class FaceNormal {

    @OriginalMember(owner = "client!qd", name = "b", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!qd", name = "a", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!qd", name = "c", descriptor = "I")
    public int y;

    @OriginalMember(owner = "client!qd", name = "<init>", descriptor = "()V")
    public FaceNormal() {
        /* empty */
    }

    @OriginalMember(owner = "client!qd", name = "<init>", descriptor = "(Lclient!qd;)V")
    public FaceNormal(@OriginalArg(0) FaceNormal other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
}
