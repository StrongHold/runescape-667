import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.datastruct.key.Deque;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static159 {

    @OriginalMember(owner = "client!ew", name = "a", descriptor = "[I")
    public static final int[] pooledCounts = new int[5];

    @OriginalMember(owner = "client!ew", name = "g", descriptor = "I")
    public static int anInt2788 = 0;

    @OriginalMember(owner = "client!ew", name = "d", descriptor = "Lclient!sia;")
    public static Deque changes = new Deque();

    @OriginalMember(owner = "client!ew", name = "a", descriptor = "(Z)V")
    public static void method2575() {
        if (MainLogicStep.isLoggedOut(MainLogicManager.step) || MainLogicStep.isAtLobbyScreen(MainLogicManager.step)) {
            Static127.method2243(Static412.anInt6358, Camera.x >> 12, Camera.z >> 12);
        } else {
            @Pc(20) int local20 = PlayerEntity.self.pathX[0] >> 3;
            @Pc(27) int local27 = PlayerEntity.self.pathZ[0] >> 3;
            if (local20 >= 0 && Static720.mapWidth >> 3 > local20 && local27 >= 0 && Static501.mapLength >> 3 > local27) {
                Static127.method2243(Static412.anInt6358, local20, local27);
            } else {
                Static127.method2243(0, Static720.mapWidth >> 4, Static501.mapLength >> 4);
            }
        }
        Static506.updateEnvironment();
        Static588.method7713();
        Static683.method8928();
        Static442.method5969();
    }
}
