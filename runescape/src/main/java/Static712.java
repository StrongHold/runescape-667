import com.jagex.Client;
import com.jagex.core.constants.WindowMode;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.core.constants.ModeWhere;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Container;
import java.awt.Insets;

public final class Static712 {

    @OriginalMember(owner = "client!wj", name = "a", descriptor = "()V")
    public static void clearDynamicEntities() {
        for (@Pc(1) int i = 0; i < Static125.dynamicEntityCount; i++) {
            @Pc(6) PositionEntity entity = Static679.aPositionEntity[i];
            Static549.method8293(entity, true);
            Static679.aPositionEntity[i] = null;
        }
        Static125.dynamicEntityCount = 0;
    }

    @OriginalMember(owner = "client!wj", name = "k", descriptor = "(B)V")
    public static void method9329(@OriginalArg(0) byte arg0) {
        @Pc(7) client lock = client.aClient1;
        synchronized (client.aClient1) {
            if (GameShell.fsframe != null) {
                return;
            }

            @Pc(22) Container topContainer;
            if (GameShell.frame != null) {
                topContainer = GameShell.frame;
            } else if (GameShell.loaderApplet == null) {
                topContainer = GameShell.instance;
            } else {
                topContainer = GameShell.loaderApplet;
            }

            GameShell.frameWid = topContainer.getSize().width;
            GameShell.frameHei = topContainer.getSize().height;

            if (topContainer == GameShell.frame) {
                @Pc(44) Insets insets = GameShell.frame.getInsets();
                GameShell.frameHei -= insets.top + insets.bottom;
                GameShell.frameWid -= insets.right + insets.left;
            }

            if (InterfaceManager.getWindowMode() == WindowMode.FIXED) {
                GameShell.topMargin = 0;
                GameShell.canvasHei = Client.loadingScreenHeight;
                GameShell.leftMargin = (GameShell.frameWid - Client.loadingScreenWidth) / 2;
                GameShell.canvasWid = Client.loadingScreenWidth;
            } else {
                InterfaceManager.applyMaxScreenSize();
            }

            if (Client.modeWhere != ModeWhere.LIVE) {
                @Pc(101) boolean tooSmall;
                if (GameShell.canvasWid < 1024 && GameShell.canvasHei < 768) {
                    tooSmall = true;
                } else {
                    tooSmall = false;
                }
            }

            GameShell.canvas.setSize(GameShell.canvasWid, GameShell.canvasHei);

            if (Toolkit.active != null) {
                if (OrthoMode.toolkitActive) {
                    OrthoMode.method7606(GameShell.canvas);
                } else {
                    Toolkit.active.resizeCanvas(GameShell.canvas, GameShell.canvasWid, GameShell.canvasHei);
                }
            }

            if (topContainer == GameShell.frame) {
                @Pc(44) Insets insets = GameShell.frame.getInsets();
                GameShell.canvas.setLocation(insets.left + GameShell.leftMargin, insets.top + GameShell.topMargin);
            } else {
                GameShell.canvas.setLocation(GameShell.leftMargin, GameShell.topMargin);
            }

            if (InterfaceManager.topLevelInterface != -1) {
                InterfaceManager.refreshTopLevelInterface(true);
            }

            Static288.repaintMargins();
        }
        if (arg0 != 11) {
            OrthoMode.savedCamera = null;
        }
    }
}
