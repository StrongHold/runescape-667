import com.jagex.core.io.Packet;
import com.jagex.sound.SoundCache;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static12 {

    @OriginalMember(owner = "client!ah", name = "h", descriptor = "Lclient!fca;")
    public static SoundCache aClass123_4;

    @OriginalMember(owner = "client!ah", name = "a", descriptor = "(Lclient!ge;I)V")
    public static void method5164(@OriginalArg(0) Packet arg0) {
        while (true) {
            @Pc(20) int local20 = arg0.g1();
            if (local20 == 0) {
                Static482.anInt7228 = arg0.g2();
                Static134.anInt10330 = arg0.g2();
            } else if (local20 == 255) {
                return;
            }
        }
    }
}
