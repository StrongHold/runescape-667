import com.jagex.game.runetek6.client.GameShell;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.mouse.MouseLog;
import rs2.client.event.mouse.MouseMonitor;

public final class Static443 {

    @OriginalMember(owner = "client!nw", name = "a", descriptor = "(Z)V")
    public static void method5981() {
        if (InterfaceManager.topLevelInterface == -1) {
            return;
        }

        @Pc(13) int mouseX = MouseMonitor.instance.getRecordedX();
        @Pc(22) int mouseY = MouseMonitor.instance.getRecordedY();
        @Pc(27) MouseLog log = (MouseLog) Static226.mouseLogs.first();
        if (log != null) {
            mouseX = log.getX();
            mouseY = log.getY();
        }

        @Pc(44) int parentX = 0;
        @Pc(46) int parentY = 0;
        if (OrthoMode.toolkitActive) {
            parentX = OrthoMode.method2283();
            parentY = Static422.method5771();
        }

        InterfaceManager.mainLogic(InterfaceManager.topLevelInterface, parentX, parentY, parentX + mouseX, parentY + GameShell.canvasHei, parentX, parentY, mouseX, mouseY, mouseY + parentY, parentX + GameShell.canvasWid);
        if (WorldMap.component != null) {
            WorldMap.tick(mouseX + parentX, mouseY - -parentY);
        }
    }

}
