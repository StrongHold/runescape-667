import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static114 {

    @OriginalMember(owner = "client!dk", name = "v", descriptor = "I")
    public static int toolkitType = -1;

    @OriginalMember(owner = "client!dk", name = "a", descriptor = "(III)Lclient!tla;")
    public static WallDecor getWallDecor(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(7) Tile local7 = Static334.activeTiles[arg0][arg1][arg2];
        return local7 == null ? null : local7.wallDecor;
    }
}
