import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!fm")
public final class CutsceneAction_Sub6 extends CutsceneAction {

    @OriginalMember(owner = "client!fm", name = "o", descriptor = "I")
    public final int anInt3025;

    @OriginalMember(owner = "client!fm", name = "k", descriptor = "I")
    public final int anInt3020;

    @OriginalMember(owner = "client!fm", name = "j", descriptor = "Ljava/lang/String;")
    public final String aString33;

    @OriginalMember(owner = "client!fm", name = "g", descriptor = "I")
    public final int anInt3026;

    @OriginalMember(owner = "client!fm", name = "i", descriptor = "I")
    public final int anInt3021;

    @OriginalMember(owner = "client!fm", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction_Sub6(@OriginalArg(0) Packet arg0) {
        super(arg0);
        this.anInt3025 = arg0.g2();
        this.anInt3020 = arg0.g2();
        this.aString33 = arg0.gjstr();
        this.anInt3026 = arg0.g4();
        this.anInt3021 = arg0.g2();
    }

    @OriginalMember(owner = "client!fm", name = "b", descriptor = "(I)V")
    @Override
    public void execute() {
        TextCoordList.add(Camera.renderingLevel, this.anInt3025, Static102.averageHeight(Camera.renderingLevel, this.anInt3025, this.anInt3020), this.anInt3020, this.aString33, this.anInt3026, this.anInt3021);
    }
}
