import com.jagex.core.constants.MainLogicStep;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static630 {

    @OriginalMember(owner = "client!tv", name = "a", descriptor = "(Lclient!r;III[Z)Z")
    public static boolean method8357(@OriginalArg(0) Shadow shadow, @OriginalArg(1) int maxLevel, @OriginalArg(2) int x, @OriginalArg(3) int z, @OriginalArg(4) boolean[] arg4) {
        @Pc(1) boolean found = false;

        if (Static246.ground != Static693.underwaterGround) {
            @Pc(12) int averageHeight = Static706.floor[maxLevel].averageHeight(x, z);

            for (@Pc(14) int level = 0; level <= maxLevel; level++) {
                @Pc(19) Ground ground = Static706.floor[level];

                if (ground != null) {
                    @Pc(29) int deltaHeight = averageHeight - ground.averageHeight(x, z);

                    if (arg4 != null) {
                        arg4[level] = ground.method7874(shadow, x, deltaHeight, z);

                        if (!arg4[level]) {
                            continue;
                        }
                    }

                    ground.CA(shadow, x, deltaHeight, z, 0, false);
                    found = true;
                }
            }
        }

        return found;
    }

    @OriginalMember(owner = "client!tv", name = "b", descriptor = "(I)V")
    public static void method8358() {
        if (ClientOptions.instance.animateBackground.getValue() == 0 && Static164.areaLevel != Camera.renderingLevel) {
            MapArea.updateMapArea(false, Static62.areaCenterX, MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD, Static525.areaCenterZ);
        } else {
            Minimap.redrawIfLevelChanged(Toolkit.active);
            if (Static514.anInt7680 != Camera.renderingLevel) {
                Static77.method1561();
            }
        }
    }
}
