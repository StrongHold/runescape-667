import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!q")
public final class CutsceneAction_Sub17 extends CutsceneAction {

    @OriginalMember(owner = "client!q", name = "g", descriptor = "I")
    public final int anInt7622;

    @OriginalMember(owner = "client!q", name = "l", descriptor = "[I")
    public final int[] anIntArray611;

    @OriginalMember(owner = "client!q", name = "m", descriptor = "I")
    public final int anInt7624;

    @OriginalMember(owner = "client!q", name = "k", descriptor = "I")
    public final int anInt7625;

    @OriginalMember(owner = "client!q", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction_Sub17(@OriginalArg(0) Packet arg0) {
        super(arg0);
        this.anInt7622 = arg0.g2();
        this.anIntArray611 = new int[4];
        this.anInt7624 = arg0.gSmart2or4null();
        Arrays.set(this.anIntArray611, 0, this.anIntArray611.length, this.anInt7624);
        this.anInt7625 = arg0.g4();
    }

    @OriginalMember(owner = "client!q", name = "b", descriptor = "(I)V")
    @Override
    public void execute() {
        @Pc(10) PathingEntity local10 = CutsceneManager.actors[this.anInt7622].entity();
        if (this.anInt7625 == 0) {
            PathingEntity.animate(this.anIntArray611, 0, false, local10);
        } else {
            Static310.animateWorn(new int[]{this.anInt7625}, new int[]{this.anInt7624}, new int[1], local10);
        }
    }
}
