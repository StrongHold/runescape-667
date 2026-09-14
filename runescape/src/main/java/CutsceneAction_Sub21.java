import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!sha")
public final class CutsceneAction_Sub21 extends CutsceneAction {

    @OriginalMember(owner = "client!sha", name = "j", descriptor = "I")
    public final int actorIndex;

    @OriginalMember(owner = "client!sha", name = "g", descriptor = "I")
    public final int pathIndex;

    @OriginalMember(owner = "client!sha", name = "i", descriptor = "I")
    public final int level;

    @OriginalMember(owner = "client!sha", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction_Sub21(@OriginalArg(0) Packet packet) {
        super(packet);
        this.actorIndex = packet.g2();
        this.pathIndex = packet.g2();
        this.level = packet.g1();
    }

    @OriginalMember(owner = "client!sha", name = "b", descriptor = "(I)V")
    @Override
    public void execute() {
        @Pc(8) Actor actor = CutsceneManager.actors[this.actorIndex];
        @Pc(13) CutscenePath path = Static183.cutscenePaths[this.pathIndex];
        path.walk(actor, this.level);
    }
}
