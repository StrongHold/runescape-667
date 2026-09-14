import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!gg")
public final class CutsceneAction_Sub7 extends CutsceneAction {

    @OriginalMember(owner = "client!gg", name = "h", descriptor = "I")
    public final int locIndex;

    @OriginalMember(owner = "client!gg", name = "g", descriptor = "I")
    public final int animation;

    @OriginalMember(owner = "client!gg", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction_Sub7(@OriginalArg(0) Packet packet) {
        super(packet);
        this.locIndex = packet.g2();
        this.animation = packet.gSmart2or4null();
    }

    @OriginalMember(owner = "client!gg", name = "b", descriptor = "(I)V")
    @Override
    public void execute() {
        @Pc(8) CutsceneLoc loc = Static507.cutsceneLocs[this.locIndex];
        Static198.animateLocation(loc.level, loc.x, loc.z, loc.shape, loc.rotation, Static461.LOC_LAYERS_BY_SHAPE[loc.shape], this.animation);
    }
}
