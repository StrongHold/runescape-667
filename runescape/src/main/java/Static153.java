import com.jagex.core.util.SystemTimer;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static153 {

    @OriginalMember(owner = "client!eq", name = "o", descriptor = "[I")
    public static int[] anIntArray235 = new int[2];

    @OriginalMember(owner = "client!eq", name = "a", descriptor = "(ILclient!uc;I)V")
    public static void method2461(@OriginalArg(0) int duration, @OriginalArg(1) Environment environment) {
        if (InterfaceManager.loginOpened) {
            duration = 0;
            InterfaceManager.loginOpened = false;
        }
        if (Static346.aEnvironment_1 != null && Static346.aEnvironment_1.equalTo(environment)) {
            return;
        }
        Static346.aEnvironment_1 = environment;
        Static344.aLong169 = SystemTimer.safetime();
        Static173.anInt2913 = duration;
        Static587.anInt8673 = duration;
        if (Static587.anInt8673 == 0) {
            Static506.method8313();
            return;
        }
        Static74.aSkyBox_1 = Static456.activeSkyBox;
        Static600.aFloat179 = Static151.aFloat218;
        Static671.aFloat214 = Static683.aFloat215;
        Static386.anInt6062 = Static448.anInt6801;
        Static203.aFloat69 = Static57.aFloat29;
        Static74.aClass67_3 = Static425.aClass67_6;
        Static538.aFloat174 = Static133.aFloat63;
        Static360.anInt5820 = Static251.anInt4037;
        Static659.aFloat213 = Static688.aFloat216;
        Static620.aFloat197 = Static318.aFloat210;
        Static679.anInt10273 = Static171.anInt2882;
        if (Static456.activeSkyBox == null) {
            return;
        }
        if (Static456.activeSkyBox.method3165()) {
            Static74.aSkyBox_1 = Static456.activeSkyBox.method3167();
            Static456.activeSkyBox = Static74.aSkyBox_1;
        }
        if (Static456.activeSkyBox != null && Static346.aEnvironment_1.skyBox != Static456.activeSkyBox) {
            Static456.activeSkyBox.method3163(Static346.aEnvironment_1.skyBox);
        }
    }
}
