import com.jagex.game.Location;
import com.jagex.game.runetek6.config.loctype.LocTypeCustomisation;
import com.jagex.graphics.Ground;
import com.jagex.sound.midi.MidiSong;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static706 {

    @OriginalMember(owner = "client!wfa", name = "W", descriptor = "F")
    public static float aFloat217;

    @OriginalMember(owner = "client!wfa", name = "U", descriptor = "[Lclient!s;")
    public static Ground[] floor;

    @OriginalMember(owner = "client!wfa", name = "a", descriptor = "(IILclient!gp;III)V")
    public static void setLocCustomisation(@OriginalArg(0) int arg0, @OriginalArg(2) LocTypeCustomisation arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3, @OriginalArg(5) int arg4) {
        if (arg4 < 1 || arg2 < 1 || arg4 > Static720.mapWidth - 2 || Static501.mapLength - 2 < arg2) {
            return;
        }
        if (Static334.activeTiles == null) {
            return;
        }
        @Pc(52) Location local52 = MapRegion.active.getLoc(arg3, arg4, arg2, arg0);
        if (local52 == null) {
            return;
        }
        if (local52 instanceof DynamicLocation) {
            ((DynamicLocation) local52).customise(arg1);
            return;
        }
        if (!(local52 instanceof DynamicGroundDecor)) {
            if (local52 instanceof DynamicWall) {
                ((DynamicWall) local52).customise(arg1);
            } else if (local52 instanceof DynamicWallDecor) {
                ((DynamicWallDecor) local52).method6862(arg1);
                return;
            }
            return;
        }
        ((DynamicGroundDecor) local52).customise(arg1);
        return;
    }

    @OriginalMember(owner = "client!wfa", name = "a", descriptor = "(BLclient!bn;I)V")
    public static void method9221(@OriginalArg(1) MidiSong arg0, @OriginalArg(2) int arg1) {
        ClientOptions.instance.musicVolume.getValue();
        if (arg0 == null) {
            Static100.method1988();
        }
        Static719.aPcmPlayer_5.method3592();
        Static522.playSongNow(arg0);
    }

    @OriginalMember(owner = "client!wfa", name = "a", descriptor = "(IBII)Z")
    public static boolean method9224(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        Static107.aMatrix_3.project(arg2, arg1, arg0, Static35.anIntArray58);
        @Pc(13) int local13 = Static35.anIntArray58[2];
        if (local13 < 50) {
            return false;
        } else {
            Static35.anIntArray58[0] = Static35.anIntArray58[0] * Static1.anInt10797 / local13 + Static460.anInt6970;
            Static35.anIntArray58[2] = local13;
            Static35.anIntArray58[1] = Static407.anInt6286 + Static412.anInt6357 * Static35.anIntArray58[1] / local13;
            return true;
        }
    }

}
