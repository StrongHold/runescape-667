import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static588 {

    @OriginalMember(owner = "client!sj", name = "a", descriptor = "(ZZ)Z")
    public static boolean method7712(@OriginalArg(0) boolean arg0, @OriginalArg(1) boolean arg1) {
        return arg0 | arg1;
    }

    @OriginalMember(owner = "client!sj", name = "a", descriptor = "(I)V")
    public static void method7713() {
        Static425.toolkit.xa(((float) ClientOptions.instance.brightness.getValue() * 0.1F + 0.7F) * Static318.aFloat210);
        Static425.toolkit.ZA(Static448.anInt6801, Static688.aFloat216, Static683.aFloat215, (float) (Static344.anInt5617 << 2), (float) (Static417.anInt6400 << 2), (float) (Static331.anInt5441 << 2));
        Static425.toolkit.method7973(Static425.aClass67_6);
    }

    @OriginalMember(owner = "client!sj", name = "a", descriptor = "(ZIII)Z")
    public static boolean method7714(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        if (!Static18.occlude || !Static29.aBoolean60) {
            return false;
        } else if (Static432.occludedPixelCount < 100) {
            return false;
        } else {
            @Pc(37) int local37 = Static446.anIntArrayArrayArray9[arg1][arg2][arg0];
            if (-Static675.anInt10155 == local37) {
                return false;
            } else if (Static675.anInt10155 == local37) {
                return true;
            } else if (Static693.underwaterGround == Static246.ground) {
                return false;
            } else {
                @Pc(64) int local64 = arg2 << EnvironmentLight.anInt1066;
                @Pc(68) int local68 = arg0 << EnvironmentLight.anInt1066;
                if (Static172.isTriangleOccluded(local64 + Static340.anInt5586 - 1, local68 + 1, Static246.ground[arg1].getHeight(arg2 + 1, arg0 + 1), Static246.ground[arg1].getHeight(arg2, arg0 + 1), Static246.ground[arg1].getHeight(arg2, arg0), local68 + Static340.anInt5586 - 1, Static340.anInt5586 + local68 + -1, local64 + 1, local64 + 1) && Static172.isTriangleOccluded(Static340.anInt5586 + local64 - 1, local68 + 1, Static246.ground[arg1].getHeight(arg2 + 1, arg0), Static246.ground[arg1].getHeight(arg2 + 1, arg0 + 1), Static246.ground[arg1].getHeight(arg2, arg0), local68 + 1, Static340.anInt5586 + local68 + -1, local64 + 1, Static340.anInt5586 + -1 + local64)) {
                    Static298.occludedGroundCount++;
                    Static446.anIntArrayArrayArray9[arg1][arg2][arg0] = Static675.anInt10155;
                    return true;
                } else {
                    Static446.anIntArrayArrayArray9[arg1][arg2][arg0] = -Static675.anInt10155;
                    return false;
                }
            }
        }
    }

}
