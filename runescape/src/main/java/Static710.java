import com.jagex.ClientProt;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static710 {

    @OriginalMember(owner = "client!wha", name = "l", descriptor = "I")
    public static int varclanUpdateCount = 0;

    @OriginalMember(owner = "client!wha", name = "a", descriptor = "(ILclient!hda;Lclient!hda;)V")
    public static void ifButtonDSend(@OriginalArg(1) Component source, @OriginalArg(2) Component target) {
        @Pc(15) ClientMessage message = ClientMessage.create(ClientProt.IF_BUTTOND, ServerConnection.GAME.isaac);
        message.bitPacket.p2(target.invObject);
        message.bitPacket.p2_alt1(source.id);
        message.bitPacket.p2_alt2(source.invObject);
        message.bitPacket.p4_alt2(source.slot);
        message.bitPacket.p2_alt1(target.id);
        message.bitPacket.p4_alt3(target.slot);
        ServerConnection.GAME.send(message);
    }

    @OriginalMember(owner = "client!wha", name = "a", descriptor = "(I)V")
    public static void buildScene() {
        for (@Pc(10) Class8_Sub1 local10 = (Class8_Sub1) Static149.A_ENTITY_LIST___4.removeFirst(); local10 != null; local10 = (Class8_Sub1) Static149.A_ENTITY_LIST___4.removeFirst()) {
            Static703.method9171(local10);
        }
        @Pc(36) int local36;
        @Pc(38) int local38;
        if (ClientOptions.instance.animateBackground.getValue() == 1) {
            local36 = 0;
            local38 = 3;
        } else {
            local38 = Static164.areaLevel;
            local36 = Static164.areaLevel;
        }
        @Pc(56) int local56;
        if (CutsceneManager.state == 3) {
            for (local56 = local36; local56 <= local38; local56++) {
                EntitySceneBuilder.addTileAlignedEntities(local56);
            }
            EntitySceneBuilder.addOffCentreEntities();
            return;
        }
        EntitySceneBuilder.calculateDrawPriorities();
        for (local56 = local36; local56 <= local38; local56++) {
            EntitySceneBuilder.clearTilePriorities();
            EntitySceneBuilder.recordTilePriorities(local56);
            EntitySceneBuilder.addTileAlignedEntities(local56);
        }
        EntitySceneBuilder.buildEntityStacks();
        EntitySceneBuilder.addOffCentreEntities();
    }

    @OriginalMember(owner = "client!wha", name = "a", descriptor = "(III)Z")
    public static boolean method6713(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return false;
    }
}
