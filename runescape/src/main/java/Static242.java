import com.jagex.graphics.Font;
import com.jagex.game.LocalisedText;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static242 {

    @OriginalMember(owner = "client!hj", name = "k", descriptor = "Lclient!da;")
    public static Font aFont_7;

    @OriginalMember(owner = "client!hj", name = "b", descriptor = "Ljava/lang/Thread;")
    public static Thread aThread1;

    @OriginalMember(owner = "client!hj", name = "i", descriptor = "I")
    public static int anInt3971 = 0;

    @OriginalMember(owner = "client!hj", name = "a", descriptor = "(BLclient!hw;)I")
    public static int method3499(@OriginalArg(1) Class172 arg0) {
        if (arg0 == Static285.aClass172_1) {
            return 9216;
        } else if (Static360.aClass172_3 == arg0) {
            return 34065;
        } else if (Static320.aClass172_2 == arg0) {
            return 34066;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @OriginalMember(owner = "client!hj", name = "a", descriptor = "(I)Z")
    public static boolean method3500() {
        return Static135.anInt8223 != 0;
    }

    @OriginalMember(owner = "client!hj", name = "c", descriptor = "(I)V")
    public static void method3502() {
        Static525.aClass2_Sub2_Sub16_12 = new DoublyLinkedNode_Sub2_Sub16(LocalisedText.CANCEL.localise(client.language), "", InterfaceManager.targetEndCursor, 1012, -1, 0L, 0, 0, true, false, 0L, true);
    }

    @OriginalMember(owner = "client!hj", name = "a", descriptor = "(IZ)I")
    public static int method3503(@OriginalArg(1) boolean arg0) {
        @Pc(5) int local5 = Static448.anInt6796;
        if (local5 == 0) {
            return arg0 ? 0 : Static2.anInt45;
        } else if (local5 == 1) {
            return Static2.anInt45;
        } else if (local5 == 2) {
            return 0;
        } else {
            return 0;
        }
    }

    @OriginalMember(owner = "client!hj", name = "a", descriptor = "(Ljava/lang/String;II)V")
    public static void method3504(@OriginalArg(0) String arg0, @OriginalArg(1) int arg1) {
        @Pc(5) int local5 = Static338.anInt5564;
        @Pc(7) int[] local7 = Static210.anIntArray280;
        @Pc(9) boolean local9 = false;
        for (@Pc(19) int local19 = 0; local19 < local5; local19++) {
            @Pc(26) PlayerEntity local26 = PlayerList.highResolutionPlayers[local7[local19]];
            if (local26 != null && PlayerEntity.self != local26 && local26.accountName != null && local26.accountName.equalsIgnoreCase(arg0)) {
                @Pc(47) ClientProt local47 = null;
                if (arg1 == 1) {
                    local47 = Static424.A_CLIENT_PROT___79;
                } else if (arg1 == 4) {
                    local47 = Static587.A_CLIENT_PROT___104;
                } else if (arg1 == 5) {
                    local47 = Static675.A_CLIENT_PROT___118;
                } else if (arg1 == 6) {
                    local47 = Static173.A_CLIENT_PROT___30;
                } else if (arg1 == 7) {
                    local47 = Static273.A_CLIENT_PROT___55;
                } else if (arg1 == 9) {
                    local47 = Static571.A_CLIENT_PROT___103;
                }
                if (local47 != null) {
                    @Pc(108) ClientMessage local108 = ClientMessage.create(local47, ConnectionManager.GAME.cipher);
                    local108.buffer.p1(0);
                    local108.buffer.p2(local7[local19]);
                    ConnectionManager.GAME.send(local108);
                }
                local9 = true;
                break;
            }
        }
        if (!local9) {
            Static67.method6098(LocalisedText.UNABLETOFIND.localise(client.language) + arg0);
        }
    }
}
