import com.jagex.game.runetek6.client.GameShell;
import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static150 {

    @OriginalMember(owner = "client!en", name = "h", descriptor = "I")
    public static int anInt2634;

    @OriginalMember(owner = "client!en", name = "i", descriptor = "I")
    public static int drawOrder = 0;

    @OriginalMember(owner = "client!en", name = "a", descriptor = "(B)V")
    public static void method2455() {
        Static173.method2690();
        Static517.method6822(ClientOptions.instance.stereoSound.getValue() == 1);
        Static719.aPcmPlayer_5 = Static638.method8394(GameShell.signLink, 0, 22050, GameShell.canvas);
        Static697.method9120(Static48.method1100(null));
        Static559.aPcmPlayer_3 = Static638.method8394(GameShell.signLink, 1, 2048, GameShell.canvas);
        Static559.aPcmPlayer_3.method3582(SoundManager.activeStreams);
    }

    @OriginalMember(owner = "client!en", name = "a", descriptor = "(BLclient!ge;)Lclient!hba;")
    public static TextureShapeRectangle readRectangle(@OriginalArg(1) Packet packet) {
        return new TextureShapeRectangle(packet.g2s(), packet.g2s(), packet.g2s(), packet.g2s(), packet.g3(), packet.g3(), packet.g1());
    }
}
