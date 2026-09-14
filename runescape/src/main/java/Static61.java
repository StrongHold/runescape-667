import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static61 {

    @OriginalMember(owner = "client!c", name = "a", descriptor = "(IIILclient!eia;)V")
    public static void setGroundDecor(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) GroundDecor decor) {
        @Pc(4) Tile tile = Static347.getTile(level, x, z);
        if (tile == null) {
            return;
        }
        tile.groundDecor = decor;
        @Pc(19) int groundIndex = Static246.ground == Static693.underwaterGround ? 1 : 0;
        if (decor.isStationary()) {
            if (decor.isTransparent(0)) {
                decor.nextEntity = Static398.transparentStationaryEntities[groundIndex];
                Static398.transparentStationaryEntities[groundIndex] = decor;
                return;
            }
            decor.nextEntity = Static576.opaqueStationaryEntities[groundIndex];
            Static576.opaqueStationaryEntities[groundIndex] = decor;
            Static75.hasOpaqueStationaryEntities = true;
            return;
        }
        decor.nextEntity = Static468.dynamicEntities[groundIndex];
        Static468.dynamicEntities[groundIndex] = decor;
    }

    @OriginalMember(owner = "client!c", name = "a", descriptor = "([J[Ljava/lang/Object;Z)V")
    public static void sortByKey(@OriginalArg(0) long[] keys, @OriginalArg(1) Object[] values) {
        Static542.method7200(values, keys, 0, keys.length - 1);
    }

}
