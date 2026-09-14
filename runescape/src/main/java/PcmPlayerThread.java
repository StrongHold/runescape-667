import com.jagex.game.runetek6.client.GameShell;
import com.jagex.sign.SignLink;
import com.jagex.core.util.JagException;
import com.jagex.core.util.TimeUtils;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!faa")
public final class PcmPlayerThread implements Runnable {

    @OriginalMember(owner = "client!faa", name = "g", descriptor = "Lclient!vq;")
    public SignLink signLink;

    @OriginalMember(owner = "client!faa", name = "f", descriptor = "[Lclient!cd;")
    public final PcmPlayer[] players = new PcmPlayer[2];

    @OriginalMember(owner = "client!faa", name = "h", descriptor = "Z")
    public volatile boolean stopping = false;

    @OriginalMember(owner = "client!faa", name = "e", descriptor = "Z")
    public volatile boolean running = false;

    @OriginalMember(owner = "client!faa", name = "run", descriptor = "()V")
    @Override
    public void run() {
        this.running = true;
        try {
            while (!this.stopping) {
                for (@Pc(12) int index = 0; index < 2; index++) {
                    @Pc(21) PcmPlayer player = this.players[index];
                    if (player != null) {
                        player.method3594();
                    }
                }
                TimeUtils.sleep(10L);
                GameShell.waitForEvents(this.signLink, null);
            }
        } catch (@Pc(49) Exception exception) {
            JagException.sendTrace(exception, null);
        } finally {
            @Pc(59) Object unused = null;
            this.running = false;
        }
    }
}
