import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import rs2.client.clan.ClanSettings;

@OriginalClass("client!df")
public final class Node_Sub14_Sub2 extends Node_Sub14 {

    @OriginalMember(owner = "client!df", name = "t", descriptor = "J")
    public long aLong78 = -1L;

    @OriginalMember(owner = "client!df", name = "s", descriptor = "Ljava/lang/String;")
    public String aString17 = null;

    @OriginalMember(owner = "client!df", name = "a", descriptor = "(Lclient!hi;I)V")
    @Override
    public void method8617(@OriginalArg(0) ClanSettings arg0) {
        arg0.doAddMember(this.aString17, this.aLong78, 0);
    }

    @OriginalMember(owner = "client!df", name = "a", descriptor = "(ILclient!ge;)V")
    @Override
    public void method8615(@OriginalArg(1) Packet arg0) {
        if (arg0.g1() != 255) {
            arg0.pos--;
            this.aLong78 = arg0.g8();
        }
        this.aString17 = arg0.fastgstr();
        if (Static148.aBoolean215) {
            System.out.println("memberhash:" + this.aLong78 + " membername:" + this.aString17);
        }
    }
}
