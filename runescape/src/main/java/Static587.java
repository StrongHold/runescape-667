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
    public static void method7704() {
        if (!WorldList.pingWorlds) {
            return;
        }
        while (true) {
            while (WorldList.activeWorlds.length > Static419.anInt6434) {
                @Pc(26) GameWorld local26 = WorldList.activeWorlds[Static419.anInt6434];
                if (local26 != null && local26.ping == -1) {
                    if (Static522.pingRequest == null) {
                        Static522.pingRequest = Static151.pingWorker.ping(local26.address);
                    }
                    @Pc(54) int local54 = Static522.pingRequest.ping;
                    if (local54 == -1) {
                        return;
                    }
                    Static419.anInt6434++;
                    Static522.pingRequest = null;
                    local26.ping = local54;
                } else {
                    Static419.anInt6434++;
                }
            }
            return;
        }
    }

}
