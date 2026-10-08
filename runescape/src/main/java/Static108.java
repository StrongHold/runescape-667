import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static108 {

    @OriginalMember(owner = "client!dga", name = "a", descriptor = "(IIBLclient!uc;)V")
    public static void setEnvironment(@OriginalArg(0) int zoneZ, @OriginalArg(1) int zoneX, @OriginalArg(3) Environment environment) {
        Static665.zoneEnvironments[zoneX][zoneZ] = environment;
    }

}
