import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static10 {

    @OriginalMember(owner = "client!afa", name = "a", descriptor = "(IIILjava/lang/Class;)V")
    public static void removeEntity(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) Class type) {
        @Pc(7) Tile tile = Static334.activeTiles[level][x][z];
        if (tile == null) {
            return;
        }
        for (@Pc(14) PositionEntityNode node = tile.head; node != null; node = node.node) {
            @Pc(18) PositionEntity entity = node.entity;
            if (type.isAssignableFrom(entity.getClass()) && entity.x1 == x && entity.z1 == z) {
                Static549.removePositionEntity(entity, false);
                return;
            }
        }
    }
}
