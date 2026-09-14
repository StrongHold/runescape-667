import com.jagex.core.io.Packet;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static360 {

    @OriginalMember(owner = "client!lg", name = "f", descriptor = "I")
    public static int anInt5820;

    @OriginalMember(owner = "client!lg", name = "i", descriptor = "Lclient!hw;")
    public static final Class172 aClass172_3 = new Class172();

    @OriginalMember(owner = "client!lg", name = "d", descriptor = "(II)Z")
    public static boolean method5230(@OriginalArg(0) int cutsceneId) {
        if (Static5.anInt92 != cutsceneId || Static178.aClass247_1 == null) {
            Static298.method4385();
            Static5.anInt92 = cutsceneId;
            Static178.aClass247_1 = Static403.aClass247_2;
        }
        @Pc(53) int count;
        if (Static403.aClass247_2 == Static178.aClass247_1) {
            @Pc(36) byte[] data = js5.CUTSCENES.getfile(cutsceneId);
            if (data == null) {
                return false;
            }

            @Pc(46) Packet packet = new Packet(data);
            Static12.decodeCutsceneHeader(packet);
            count = packet.g1();
            for (@Pc(55) int i = 0; i < count; i++) {
                Static391.A_DEQUE___34.addLast(new Node_Sub35(packet));
            }
            @Pc(78) int splineCount = packet.gsmart();
            Camera.cutsceneSplines = new ScriptedCameraPath[splineCount];
            for (@Pc(83) int i = 0; i < splineCount; i++) {
                Camera.cutsceneSplines[i] = new ScriptedCameraPath(packet);
            }
            @Pc(108) int actorCount = packet.gsmart();
            CutsceneManager.actors = new Actor[actorCount];
            for (@Pc(113) int i = 0; i < actorCount; i++) {
                CutsceneManager.actors[i] = new Actor(packet, i);
            }
            @Pc(139) int locCount = packet.gsmart();
            Static507.cutsceneLocs = new CutsceneLoc[locCount];
            for (@Pc(144) int i = 0; i < locCount; i++) {
                Static507.cutsceneLocs[i] = new CutsceneLoc(packet);
            }
            @Pc(169) int pathCount = packet.gsmart();
            Static183.cutscenePaths = new CutscenePath[pathCount];
            for (@Pc(174) int i = 0; i < pathCount; i++) {
                Static183.cutscenePaths[i] = new CutscenePath(packet);
            }
            @Pc(195) int actionCount = packet.gsmart();
            Static401.aCutsceneActionArray1 = new CutsceneAction[actionCount];
            for (@Pc(200) int i = 0; i < actionCount; i++) {
                Static401.aCutsceneActionArray1[i] = CutsceneAction.decode(packet);
            }
            Static178.aClass247_1 = Static403.aClass247_3;
        }
        if (Static178.aClass247_1 == Static403.aClass247_3) {
            @Pc(227) boolean ready = true;
            @Pc(229) CutsceneAction[] actions = Static401.aCutsceneActionArray1;
            for (count = 0; count < actions.length; count++) {
                @Pc(237) CutsceneAction action = actions[count];
                if (!action.ready()) {
                    ready = false;
                }
            }
            if (!ready) {
                return false;
            }
            Static178.aClass247_1 = Static403.aClass247_4;
        }
        return true;
    }

}
