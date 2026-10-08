import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static379 {

    @OriginalMember(owner = "client!lu", name = "a", descriptor = "(Z)V")
    public static void setUnderwater(@OriginalArg(0) boolean arg0) {
        if (arg0) {
            Static334.activeTiles = Static420.underwaterTiles;
            Static246.ground = Static693.underwaterGround;
        } else {
            Static334.activeTiles = Static478.floorTiles;
            Static246.ground = Static706.floor;
        }
        Static299.tileMaxLevel = Static334.activeTiles.length;
    }
}
