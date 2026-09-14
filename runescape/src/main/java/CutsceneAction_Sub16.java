import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pe")
public final class CutsceneAction_Sub16 extends CutsceneAction {

    @OriginalMember(owner = "client!pe", name = "i", descriptor = "I")
    public final int locIndex;

    @OriginalMember(owner = "client!pe", name = "o", descriptor = "I")
    public final int z;

    @OriginalMember(owner = "client!pe", name = "p", descriptor = "I")
    public final int x;

    @OriginalMember(owner = "client!pe", name = "n", descriptor = "I")
    public final int level;

    @OriginalMember(owner = "client!pe", name = "g", descriptor = "I")
    public final int rotation;

    @OriginalMember(owner = "client!pe", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction_Sub16(@OriginalArg(0) Packet packet) {
        super(packet);
        this.locIndex = packet.g2();
        @Pc(11) int coord = packet.g4();
        this.z = coord & 0xFFFF;
        this.x = coord >>> 16;
        this.level = packet.g1();
        this.rotation = packet.g1();
    }

    @OriginalMember(owner = "client!pe", name = "b", descriptor = "(I)V")
    @Override
    public void execute() {
        Static507.cutsceneLocs[this.locIndex].add(this.x, this.rotation, this.z, this.level);
    }
}
