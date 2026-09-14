import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static298 {

    @OriginalMember(owner = "client!ji", name = "t", descriptor = "I")
    public static int occludedGroundCount;

    @OriginalMember(owner = "client!ji", name = "H", descriptor = "[I")
    public static int[] underwaterLocationGroups;

    @OriginalMember(owner = "client!ji", name = "E", descriptor = "Z")
    public static boolean parentalAdvertConsent = false;

    @OriginalMember(owner = "client!ji", name = "b", descriptor = "(I)V")
    public static void method4385() {
        CutsceneVarDomain.cache.clear();
        Static391.A_DEQUE___34.clear();
        Static507.cutsceneLocs = null;
        Camera.cutsceneSplines = null;
        CutsceneManager.actors = null;
        Static5.anInt92 = -1;
        Static457.anInt6933 = 1;
        Static482.anInt7228 = 0;
        if (Static354.fovClampsSaved) {
            Static25.minFov = Static267.savedMinFov;
            Static598.maxFov = Static465.savedMaxFov;
            Static552.minHorizontalFov = Static470.savedMinHorizontalFov;
            Static306.maxHorizontalFov = Static322.savedMaxHorizontalFov;
            Static354.fovClampsSaved = false;
        }
        Static183.cutscenePaths = null;
        Static134.anInt10330 = 0;
        Static178.aClass247_1 = null;
        Static401.aCutsceneActionArray1 = null;
    }

    @OriginalMember(owner = "client!ji", name = "a", descriptor = "(IZI)Z")
    public static boolean method4387(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return (arg1 & 0x37) != 0;
    }

}
