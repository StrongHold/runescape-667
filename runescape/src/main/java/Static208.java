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
        @Pc(5) int x = Static676.crossX;
        @Pc(7) int y = Static305.crossY;
        if (OrthoMode.toolkitActive) {
            x += OrthoMode.method2283();
            y += Static422.method5771();
        }
        @Pc(30) Sprite cross;
        if (Static616.crossType == 1) {
            cross = Sprites.cross[Static481.crossDuration / 100];
            cross.render(x - 8, y + -8);
            OrthoMode.method8927(x - 8, cross.scaleWidth() + -8 + x, y - 8, y - 8 - -cross.scaleHeight());
        }
        if (Static616.crossType == 2) {
            cross = Sprites.cross[Static481.crossDuration / 100 + 4];
            cross.render(x - 8, y + -8);
            OrthoMode.method8927(x - 8, cross.scaleWidth() + x + -8, y - 8, cross.scaleHeight() + -8 + y);
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
    public static boolean cullEntity(@OriginalArg(0) Entity entity, @OriginalArg(1) boolean underwater, @OriginalArg(2) byte[][][] roofStamps, @OriginalArg(3) int levels, @OriginalArg(4) byte roofStamp) {
        if (!Static581.aBoolean657) {
            return false;
        }
        @Pc(9) int minTileX = entity.x >> EnvironmentLight.anInt1066;
        @Pc(11) int maxTileX = minTileX;
        @Pc(16) int minTileZ = entity.z >> EnvironmentLight.anInt1066;
        @Pc(18) int maxTileZ = minTileZ;
        if (entity instanceof PositionEntity) {
            maxTileX = ((PositionEntity) entity).x2;
            maxTileZ = ((PositionEntity) entity).z2;
            minTileX = ((PositionEntity) entity).x1;
            minTileZ = ((PositionEntity) entity).z1;
        }
        for (@Pc(39) int tileX = minTileX; tileX <= maxTileX; tileX++) {
            for (@Pc(42) int tileZ = minTileZ; tileZ <= maxTileZ; tileZ++) {
                if (entity.virtualLevel < Static299.tileMaxLevel && tileX >= Static441.anInt6691 && tileX < Static77.anInt1613 && tileZ >= Static220.baseTileZ && tileZ < Static692.anInt10370) {
                    if ((roofStamps == null || entity.level < levels || roofStamps[entity.level][tileX][tileZ] != roofStamp) && entity.method9275() && !entity.method9284((byte) 59, Static665.aToolkit_15)) {
                        return false;
                    }
                    if (!underwater && tileX >= Static403.anInt6246 - 16 && tileX <= Static403.anInt6246 + 16 && tileZ >= Static550.anInt8271 - 16 && tileZ <= Static550.anInt8271 + 16) {
                        if (Static661.aBoolean457) {
                            MapArea.renderQueues[Static29.anInt702++].method6809(entity);
                            Static29.anInt702 %= Static549.anInt9424;
                        } else {
                            entity.method9289(Static665.aToolkit_15, -5);
                        }
                    }
                }
            }
        }
        return true;
    }
}
