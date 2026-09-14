import org.openrs2.deob.annotation.OriginalMember;
import rs2.client.clan.settings.ClanSettings;

public final class Static408 {

    @OriginalMember(owner = "client!ms", name = "a", descriptor = "(I)V")
    public static void method5632() {
        client.aClient1.load_jagmisc();
        ServerConnection.active.clear();
        ServerConnection.active.idleReadTicks = 0;
        Static249.rebootTimer = 0;
        ServerConnection.active.antepenultimateProt = null;
        ServerConnection.active.penultimateProt = null;
        ServerConnection.active.lastProt = null;
        ServerConnection.active.bitPacket.pos = 0;
        Static102.clearChatHistory();
        FriendsList.status = 0;
        FriendChat.name = null;
        FriendsList.count = 0;
        ClanSettings.listened = null;
        ClanSettings.affined = null;
        FriendChat.count = 0;
        FriendChat.users = null;
    }

}
