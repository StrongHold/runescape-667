import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static282 {

    @OriginalMember(owner = "client!is", name = "a", descriptor = "(IIIII)Z")
    public static boolean method3976(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(4) int arg3) {
        if (!Static18.occlude || !Static29.aBoolean60) {
            return false;
        } else if (Static432.occludedPixelCount < 100) {
            return false;
        } else if (Static588.isTileOccluded(arg3, arg1, arg2)) {
            @Pc(38) int local38 = arg2 << EnvironmentLight.anInt1066;
            @Pc(42) int local42 = arg3 << EnvironmentLight.anInt1066;
            if (Static318.isBoxOccluded(Static340.anInt5586, arg0, local38, local42, Static246.ground[arg1].getHeight(arg2, arg3), Static340.anInt5586)) {
                Static356.anInt5773++;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!is", name = "a", descriptor = "(FFFIB)F")
    public static float method3978(@OriginalArg(0) float arg0, @OriginalArg(1) float arg1, @OriginalArg(2) float arg2, @OriginalArg(3) int arg3) {
        @Pc(15) float[] local15 = Static558.aFloatArrayArray2[arg3];
        return arg0 * local15[2] + local15[0] * arg1 + arg2 * local15[1];
    }
}
