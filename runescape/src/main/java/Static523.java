import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static523 {

    @OriginalMember(owner = "client!qi", name = "n", descriptor = "I")
    public static int cameraY;

    @OriginalMember(owner = "client!qi", name = "m", descriptor = "I")
    public static int pickCameraZ;

    @OriginalMember(owner = "client!qi", name = "p", descriptor = "I")
    public static int consoleScriptLine = -1;

    @OriginalMember(owner = "client!qi", name = "a", descriptor = "(ZI)V")
    public static void refreshLoadingScreen(@OriginalArg(0) boolean repaint) {
        if (Loading.renderer == null) {
            Loading.startRenderer();
        }
        if (repaint) {
            Loading.renderer.repaint();
        }
    }

}
