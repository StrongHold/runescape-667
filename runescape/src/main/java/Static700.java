import com.jagex.Client;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static700 {

    @OriginalMember(owner = "client!wca", name = "a", descriptor = "(B)V")
    public static void freeScene() {
        Scene.free();
        for (@Pc(16) int level = 0; level < 4; level++) {
            Client.collisionMaps[level].reset();
        }
        Minimap.reset();
        client.cacheReset();
        VideoManager.stop();
        System.gc();
        Toolkit.active.ya();
    }

}
