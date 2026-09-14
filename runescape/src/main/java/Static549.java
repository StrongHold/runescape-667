import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static549 {

    @OriginalMember(owner = "client!rea", name = "b", descriptor = "I")
    public static int anInt9424 = 1;

    @OriginalMember(owner = "client!rea", name = "a", descriptor = "(Lclient!qf;Z)V")
    public static void removePositionEntity(@OriginalArg(0) PositionEntity entity, @OriginalArg(1) boolean skipHide) {
        for (@Pc(2) int x = entity.x1; x <= entity.x2; x++) {
            for (@Pc(6) int z = entity.z1; z <= entity.z2; z++) {
                @Pc(16) Tile tile = Static334.activeTiles[entity.level][x][z];
                if (tile != null) {
                    @Pc(21) PositionEntityNode node = tile.head;
                    @Pc(23) PositionEntityNode previous = null;
                    while (node != null) {
                        if (node.entity == entity) {
                            if (previous == null) {
                                tile.head = node.node;
                            } else {
                                previous.node = node.node;
                            }
                            node.remove();
                            break;
                        }
                        previous = node;
                        node = node.node;
                    }
                }
            }
        }
        if (!skipHide) {
            Static109.hide(entity);
        }
    }
}
