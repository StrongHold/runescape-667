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
        @Pc(18) long now = SystemTimer.safetime();
        Static173.anInt2913 = (int) ((long) Static173.anInt2913 + Static344.aLong169 - now);
        if (Static173.anInt2913 > 0) {
            @Pc(35) int remaining = (Static173.anInt2913 << 8) / Static587.anInt8673;
            @Pc(40) int elapsed = 255 - remaining;
            @Pc(45) float remainingFraction = (float) remaining / 255.0F;
            @Pc(50) float elapsedFraction = 1.0F - remainingFraction;
            Static448.anInt6801 = (elapsed * (Static346.aEnvironment_1.sunColour & 0xFF00FF) + (Static386.anInt6062 & 0xFF00FF) * remaining & 0xFF00FF00) + (remaining * (Static386.anInt6062 & 0xFF00) + (elapsed * (Static346.aEnvironment_1.sunColour & 0xFF00)) & 0xFF0000) >>> 8;
            Static318.aFloat210 = Static620.aFloat197 + (Static346.aEnvironment_1.ambient - Static620.aFloat197) * elapsedFraction;
            Static688.aFloat216 = elapsedFraction * (Static346.aEnvironment_1.sunIntensity - Static659.aFloat213) + Static659.aFloat213;
            Static171.anInt2882 = Static679.anInt10273 * remaining + Static346.aEnvironment_1.fogRange * elapsed >> 8;
            Static683.aFloat215 = (Static346.aEnvironment_1.reverseSunIntensity - Static671.aFloat214) * elapsedFraction + Static671.aFloat214;
            Static151.aFloat218 = Static600.aFloat179 + (Static346.aEnvironment_1.aFloat203 - Static600.aFloat179) * elapsedFraction;
            Static251.anInt4037 = (elapsed * (Static346.aEnvironment_1.fogColour & 0xFF00FF) + (Static360.anInt5820 & 0xFF00FF) * remaining & 0xFF00FF00) + (remaining * (Static360.anInt5820 & 0xFF00) + (Static346.aEnvironment_1.fogColour & 0xFF00) * elapsed & 0xFF0000) >>> 8;
            Static133.aFloat63 = Static538.aFloat174 + (Static346.aEnvironment_1.aFloat201 - Static538.aFloat174) * elapsedFraction;
            Static57.aFloat29 = elapsedFraction * (Static346.aEnvironment_1.aFloat200 - Static203.aFloat69) + Static203.aFloat69;
            if (Static74.aClass67_3 != Static346.aEnvironment_1.cubeMap) {
                Static425.aClass67_6 = Static425.toolkit.method8007(Static74.aClass67_3, Static346.aEnvironment_1.cubeMap, elapsedFraction, Static425.aClass67_6);
            }
            if (Static346.aEnvironment_1.skyBox != Static74.aSkyBox_1) {
                if (Static74.aSkyBox_1 == null) {
                    Static456.activeSkyBox = Static346.aEnvironment_1.skyBox;
                    if (Static456.activeSkyBox != null) {
                        Static456.activeSkyBox.method3160(0, elapsed);
                    }
                } else {
                    Static456.activeSkyBox = Static74.aSkyBox_1;
                    if (Static456.activeSkyBox != null) {
                        Static456.activeSkyBox.method3160(255, elapsed);
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
            if (Static456.activeSkyBox != null) {
                Static456.activeSkyBox.method3169();
            }
            Static173.anInt2913 = -1;
            Static456.activeSkyBox = Static346.aEnvironment_1.skyBox;
        }
        Static344.aLong169 = now;
    }
}
