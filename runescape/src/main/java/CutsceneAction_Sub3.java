import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!ds")
public final class CutsceneAction_Sub3 extends CutsceneAction {

    @OriginalMember(owner = "client!ds", name = "h", descriptor = "I")
    public final int locIndex;

    @OriginalMember(owner = "client!ds", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction_Sub3(@OriginalArg(0) Packet packet) {
        super(packet);
        this.locIndex = packet.g2();
    }

    @OriginalMember(owner = "client!ds", name = "b", descriptor = "(I)V")
    @Override
    public void execute() {
        Static507.cutsceneLocs[this.locIndex].remove();
    }
}
