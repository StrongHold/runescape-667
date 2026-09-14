import com.jagex.Entity;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Sprite;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.keyboard.KeyboardMonitor;
import rs2.client.event.mouse.MouseMonitor;

import java.awt.Color;

public final class Static208 {

    @OriginalMember(owner = "client!gha", name = "b", descriptor = "(IIIII)V")
    public static void method3105(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        @Pc(5) int local5 = Static676.crossX;
        @Pc(7) int local7 = Static305.crossY;
        if (OrthoMode.toolkitActive) {
            local5 += OrthoMode.method2283();
            local7 += Static422.method5771();
        }
        @Pc(30) Sprite local30;
        if (Static616.crossType == 1) {
            local30 = Sprites.cross[Static481.crossDuration / 100];
            local30.render(local5 - 8, local7 + -8);
            OrthoMode.method8927(local5 - 8, local30.scaleWidth() + -8 + local5, local7 - 8, local7 - 8 - -local30.scaleHeight());
        }
        if (Static616.crossType == 2) {
            local30 = Sprites.cross[Static481.crossDuration / 100 + 4];
            local30.render(local5 - 8, local7 + -8);
            OrthoMode.method8927(local5 - 8, local30.scaleWidth() + local5 + -8, local7 - 8, local30.scaleHeight() + -8 + local7);
        }
        Static494.method6597();
    }

    @OriginalMember(owner = "client!gha", name = "a", descriptor = "(Z)V")
    public static void method3106() {
        KeyboardMonitor.instance.remove();
        MouseMonitor.instance.remove();
        client.aClient1.addcanvas();
        GameShell.canvas.setBackground(Color.black);
        Static470.currentCursor = -1;
        KeyboardMonitor.instance = KeyboardMonitor.create(GameShell.canvas);
        MouseMonitor.instance = MouseMonitor.create(GameShell.canvas);
    }

    @OriginalMember(owner = "client!gha", name = "a", descriptor = "(Lclient!eo;Z[[[BIB)Z")
    public static boolean method3107(@OriginalArg(0) Entity arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) byte[][][] arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4) {
        if (!Static581.aBoolean657) {
            return false;
        }
        @Pc(9) int local9 = arg0.x >> EnvironmentLight.anInt1066;
        @Pc(11) int local11 = local9;
        @Pc(16) int local16 = arg0.z >> EnvironmentLight.anInt1066;
        @Pc(18) int local18 = local16;
        if (arg0 instanceof PositionEntity) {
            local11 = ((PositionEntity) arg0).x2;
            local18 = ((PositionEntity) arg0).z2;
            local9 = ((PositionEntity) arg0).x1;
            local16 = ((PositionEntity) arg0).z1;
        }
        for (@Pc(39) int local39 = local9; local39 <= local11; local39++) {
            for (@Pc(42) int local42 = local16; local42 <= local18; local42++) {
                if (arg0.virtualLevel < Static299.tileMaxLevel && local39 >= Static441.anInt6691 && local39 < Static77.anInt1613 && local42 >= Static220.baseTileZ && local42 < Static692.anInt10370) {
                    if ((arg2 == null || arg0.level < arg3 || arg2[arg0.level][local39][local42] != arg4) && arg0.method9275() && !arg0.method9284((byte) 59, Static665.aToolkit_15)) {
                        return false;
                    }
                    if (!arg1 && local39 >= Static403.anInt6246 - 16 && local39 <= Static403.anInt6246 + 16 && local42 >= Static550.anInt8271 - 16 && local42 <= Static550.anInt8271 + 16) {
                        if (Static661.aBoolean457) {
                            Static684.aClass302Array1[Static29.anInt702++].method6809(arg0);
                            Static29.anInt702 %= Static549.anInt9424;
                        } else {
                            arg0.method9289(Static665.aToolkit_15, -5);
                        }
                    }
                }
            }
        }
        return true;
    }
}
