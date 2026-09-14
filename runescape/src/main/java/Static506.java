import com.jagex.core.util.SystemTimer;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static506 {

    @OriginalMember(owner = "client!pv", name = "v", descriptor = "[I")
    public static final int[] RENDER_DISTANCE = new int[]{28, 35, 40, 44};

    @OriginalMember(owner = "client!pv", name = "d", descriptor = "(B)V")
    public static void method8313() {
        if (Static173.anInt2913 < 0) {
            return;
        }
        @Pc(18) long local18 = SystemTimer.safetime();
        Static173.anInt2913 = (int) ((long) Static173.anInt2913 + Static344.aLong169 - local18);
        if (Static173.anInt2913 > 0) {
            @Pc(35) int local35 = (Static173.anInt2913 << 8) / Static587.anInt8673;
            @Pc(40) int local40 = 255 - local35;
            @Pc(45) float local45 = (float) local35 / 255.0F;
            @Pc(50) float local50 = 1.0F - local45;
            Static448.anInt6801 = (local40 * (Static346.aEnvironment_1.sunColour & 0xFF00FF) + (Static386.anInt6062 & 0xFF00FF) * local35 & 0xFF00FF00) + (local35 * (Static386.anInt6062 & 0xFF00) + (local40 * (Static346.aEnvironment_1.sunColour & 0xFF00)) & 0xFF0000) >>> 8;
            Static318.aFloat210 = Static620.aFloat197 + (Static346.aEnvironment_1.ambient - Static620.aFloat197) * local50;
            Static688.aFloat216 = local50 * (Static346.aEnvironment_1.sunIntensity - Static659.aFloat213) + Static659.aFloat213;
            Static171.anInt2882 = Static679.anInt10273 * local35 + Static346.aEnvironment_1.fogRange * local40 >> 8;
            Static683.aFloat215 = (Static346.aEnvironment_1.reverseSunIntensity - Static671.aFloat214) * local50 + Static671.aFloat214;
            Static151.aFloat218 = Static600.aFloat179 + (Static346.aEnvironment_1.aFloat203 - Static600.aFloat179) * local50;
            Static251.anInt4037 = (local40 * (Static346.aEnvironment_1.fogColour & 0xFF00FF) + (Static360.anInt5820 & 0xFF00FF) * local35 & 0xFF00FF00) + (local35 * (Static360.anInt5820 & 0xFF00) + (Static346.aEnvironment_1.fogColour & 0xFF00) * local40 & 0xFF0000) >>> 8;
            Static133.aFloat63 = Static538.aFloat174 + (Static346.aEnvironment_1.aFloat201 - Static538.aFloat174) * local50;
            Static57.aFloat29 = local50 * (Static346.aEnvironment_1.aFloat200 - Static203.aFloat69) + Static203.aFloat69;
            if (Static74.aClass67_3 != Static346.aEnvironment_1.cubeMap) {
                Static425.aClass67_6 = Static425.toolkit.method8007(Static74.aClass67_3, Static346.aEnvironment_1.cubeMap, local50, Static425.aClass67_6);
            }
            if (Static346.aEnvironment_1.skyBox != Static74.aSkyBox_1) {
                if (Static74.aSkyBox_1 == null) {
                    Static456.aSkyBox_3 = Static346.aEnvironment_1.skyBox;
                    if (Static456.aSkyBox_3 != null) {
                        Static456.aSkyBox_3.method3160(0, local40);
                    }
                } else {
                    Static456.aSkyBox_3 = Static74.aSkyBox_1;
                    if (Static456.aSkyBox_3 != null) {
                        Static456.aSkyBox_3.method3160(255, local40);
                    }
                }
            }
        } else {
            Static448.anInt6801 = Static346.aEnvironment_1.sunColour;
            Static151.aFloat218 = Static346.aEnvironment_1.aFloat203;
            Static318.aFloat210 = Static346.aEnvironment_1.ambient;
            Static133.aFloat63 = Static346.aEnvironment_1.aFloat201;
            Static683.aFloat215 = Static346.aEnvironment_1.reverseSunIntensity;
            Static425.aClass67_6 = Static346.aEnvironment_1.cubeMap;
            Static171.anInt2882 = Static346.aEnvironment_1.fogRange;
            Static251.anInt4037 = Static346.aEnvironment_1.fogColour;
            Static688.aFloat216 = Static346.aEnvironment_1.sunIntensity;
            Static57.aFloat29 = Static346.aEnvironment_1.aFloat200;
            if (Static456.aSkyBox_3 != null) {
                Static456.aSkyBox_3.method3169();
            }
            Static173.anInt2913 = -1;
            Static456.aSkyBox_3 = Static346.aEnvironment_1.skyBox;
        }
        Static344.aLong169 = local18;
    }
}
