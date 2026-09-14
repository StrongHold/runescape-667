import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static584 {

    @OriginalMember(owner = "client!sga", name = "a", descriptor = "(IIILclient!kp;Lclient!kp;)V")
    public static void setWall(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) Wall wall, @OriginalArg(4) Wall adjacentWall) {
        @Pc(4) Tile tile = Static347.getTile(level, x, z);
        if (tile == null) {
            return;
        }
        tile.wall = wall;
        tile.adjacentWall = adjacentWall;
        @Pc(22) int groundIndex = Static246.ground == Static693.underwaterGround ? 1 : 0;
        if (!wall.isStationary()) {
            wall.nextEntity = Static468.dynamicEntities[groundIndex];
            Static468.dynamicEntities[groundIndex] = wall;
        } else if (wall.isTransparent(0)) {
            wall.nextEntity = Static398.transparentStationaryEntities[groundIndex];
            Static398.transparentStationaryEntities[groundIndex] = wall;
        } else {
            wall.nextEntity = Static576.opaqueStationaryEntities[groundIndex];
            Static576.opaqueStationaryEntities[groundIndex] = wall;
            Static75.hasOpaqueStationaryEntities = true;
        }
        if (adjacentWall == null) {
            return;
        }
        if (adjacentWall.isStationary()) {
            if (adjacentWall.isTransparent(0)) {
                adjacentWall.nextEntity = Static398.transparentStationaryEntities[groundIndex];
                Static398.transparentStationaryEntities[groundIndex] = adjacentWall;
                return;
            }
            adjacentWall.nextEntity = Static576.opaqueStationaryEntities[groundIndex];
            Static576.opaqueStationaryEntities[groundIndex] = adjacentWall;
            Static75.hasOpaqueStationaryEntities = true;
            return;
        }
        adjacentWall.nextEntity = Static468.dynamicEntities[groundIndex];
        Static468.dynamicEntities[groundIndex] = adjacentWall;
    }

}
