import com.jagex.ChangeLocationRequest;
import com.jagex.core.constants.LocLayer;
import com.jagex.game.Location;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static293 {

    @OriginalMember(owner = "client!av", name = "m", descriptor = "[I")
    public static final int[] XP_TABLE = new int[120];

    // $FF: synthetic field
    @OriginalMember(owner = "client!jfa", name = "Mb", descriptor = "Ljava/lang/Class;")
    public static Class locClass;

    @OriginalMember(owner = "client!jfa", name = "a", descriptor = "(ILclient!hma;)V")
    public static void snapshotOriginal(@OriginalArg(1) ChangeLocationRequest request) {
        if (Static334.activeTiles == null) {
            return;
        }

        @Pc(9) Location location = null;
        if (request.layer == LocLayer.WALL) {
            location = (Location) Static302.getWall(request.level, request.x, request.z);
        }
        if (request.layer == LocLayer.WALLDECOR) {
            location = Static114.getWallDecor(request.level, request.x, request.z);
        }
        if (request.layer == LocLayer.GROUND) {
            location = (Location) Static578.getEntity(request.level, request.x, request.z, locClass == null ? (locClass = getClass("com.jagex.game.Location")) : locClass);
        }
        if (request.layer == LocLayer.GROUNDDECOR) {
            location = (Location) Static687.getGroundDecor(request.level, request.x, request.z);
        }

        if (location == null) {
            request.originalShape = 0;
            request.originalId = -1;
            request.originalRotation = 0;
        } else {
            request.originalId = location.getId();
            request.originalShape = location.getShape();
            request.originalRotation = location.getRotation();
        }
    }

    static Class getClass(String name) {
        Class instance;
        try {
            instance = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw (NoClassDefFoundError) new NoClassDefFoundError().initCause(ex);
        }
        return instance;
    }

    @OriginalMember(owner = "client!jfa", name = "a", descriptor = "(III)Z")
    public static boolean method4333(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        return (Static139.method2358(arg0, arg1) | (arg0 & 0x2000) != 0 | Static401.method5575(arg0, arg1)) & Static279.method4074(arg1, arg0);
    }

    static {
        @Pc(85) int local85 = 0;
        for (@Pc(87) int local87 = 0; local87 < 120; local87++) {
            @Pc(92) int local92 = local87 + 1;
            @Pc(105) int local105 = (int) (Math.pow(2.0D, (double) local92 / 7.0D) * 300.0D + (double) local92);
            local85 += local105;
            XP_TABLE[local87] = local85 / 4;
        }
    }
}
