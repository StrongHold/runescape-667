import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static578 {

    @OriginalMember(owner = "client!sda", name = "a", descriptor = "(IIILjava/lang/Class;)Lclient!qf;")
    public static PositionEntity getEntity(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) Class type) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        if (tile == null) {
            return null;
        }
        for (@Pc(15) PositionEntityNode node = tile.head; node != null; node = node.node) {
            @Pc(19) PositionEntity entity = node.entity;
            if (type.isAssignableFrom(entity.getClass()) && entity.x1 == x && entity.z1 == z) {
                return entity;
            }
        }
        return null;
    }

}
