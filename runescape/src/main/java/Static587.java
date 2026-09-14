import com.jagex.game.world.GameWorld;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static587 {

    @OriginalMember(owner = "client!sia", name = "m", descriptor = "I")
    public static int anInt8673 = 0;

    @OriginalMember(owner = "client!sia", name = "n", descriptor = "Z")
    public static boolean aBoolean663 = false;

    @OriginalMember(owner = "client!sia", name = "b", descriptor = "[I")
    public static final int[] anIntArray689 = new int[]{16776960, 16711680, 65280, 65535, 16711935, 16777215};

    @OriginalMember(owner = "client!sia", name = "b", descriptor = "(I)V")
    public static void updateWorldPings() {
        if (!WorldList.pingWorlds) {
            return;
        }
        while (true) {
            while (WorldList.activeWorlds.length > Static419.pingWorldIndex) {
                @Pc(26) GameWorld world = WorldList.activeWorlds[Static419.pingWorldIndex];
                if (world != null && world.ping == -1) {
                    if (Static522.pingRequest == null) {
                        Static522.pingRequest = Static151.pingWorker.ping(world.address);
                    }
                    @Pc(54) int ping = Static522.pingRequest.ping;
                    if (ping == -1) {
                        return;
                    }
                    Static419.pingWorldIndex++;
                    Static522.pingRequest = null;
                    world.ping = ping;
                } else {
                    Static419.pingWorldIndex++;
                }
            }
            return;
        }
    }

}
