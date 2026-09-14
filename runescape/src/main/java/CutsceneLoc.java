import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!w")
public final class CutsceneLoc {

    @OriginalMember(owner = "client!w", name = "f", descriptor = "I")
    public int rotation;

    @OriginalMember(owner = "client!w", name = "h", descriptor = "I")
    public int level;

    @OriginalMember(owner = "client!w", name = "b", descriptor = "I")
    public int x;

    @OriginalMember(owner = "client!w", name = "k", descriptor = "I")
    public int z;

    @OriginalMember(owner = "client!w", name = "e", descriptor = "I")
    public final int id;

    @OriginalMember(owner = "client!w", name = "m", descriptor = "I")
    public final int shape;

    @OriginalMember(owner = "client!w", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneLoc(@OriginalArg(0) Packet packet) {
        this.id = packet.gSmart2or4null();
        this.shape = packet.g1();
    }

    @OriginalMember(owner = "client!w", name = "b", descriptor = "(B)V")
    public void remove() {
        Static553.changeLocation(this.x, this.z, this.level, this.shape, this.rotation, Static461.LOC_LAYERS_BY_SHAPE[this.shape], -1);
    }

    @OriginalMember(owner = "client!w", name = "a", descriptor = "(IIBII)V")
    public void add(@OriginalArg(0) int x, @OriginalArg(1) int rotation, @OriginalArg(3) int z, @OriginalArg(4) int level) {
        Static553.changeLocation(x, z, level, this.shape, rotation, Static461.LOC_LAYERS_BY_SHAPE[this.shape], this.id);
        this.z = z;
        this.rotation = rotation;
        this.level = level;
        this.x = x;
    }
}
