import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!se")
public final class Node_Sub14_Sub12 extends Node_Sub14 {

    @OriginalMember(owner = "client!se", name = "r", descriptor = "Ljava/lang/String;")
    public String aString105;

    @OriginalMember(owner = "client!se", name = "m", descriptor = "I")
    public int anInt8603;

    @OriginalMember(owner = "client!se", name = "a", descriptor = "(Lclient!hi;I)V")
    @Override
    public void method8617(@OriginalArg(0) Class164 arg0) {
        arg0.method3481(this.aString105, this.anInt8603);
    }

    @OriginalMember(owner = "client!se", name = "a", descriptor = "(ILclient!ge;)V")
    @Override
    public void method8615(@OriginalArg(1) Packet arg0) {
        this.anInt8603 = arg0.method7349();
        this.aString105 = arg0.gjstr();
    }
}