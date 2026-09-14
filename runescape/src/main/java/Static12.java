import com.jagex.core.io.Packet;
import com.jagex.sound.SoundCache;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static12 {

    @OriginalMember(owner = "client!ah", name = "h", descriptor = "Lclient!fca;")
    public static SoundCache pendingSongSoundCache;

    @OriginalMember(owner = "client!ah", name = "a", descriptor = "(Lclient!ge;I)V")
    public static void decodeCutsceneHeader(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(20) int local20 = packet.g1();
            if (local20 == 0) {
                Static482.anInt7228 = packet.g2();
                Static134.anInt10330 = packet.g2();
            } else if (local20 == 255) {
                return;
            }
        }
    }
}
