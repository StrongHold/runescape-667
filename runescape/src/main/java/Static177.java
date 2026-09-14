import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static177 {

    @OriginalMember(owner = "client!fia", name = "n", descriptor = "D")
    public static double aDouble12;

    @OriginalMember(owner = "client!fia", name = "i", descriptor = "[[B")
    public static byte[][] aByteArrayArray5;

    @OriginalMember(owner = "client!fia", name = "o", descriptor = "Ljava/lang/Object;")
    public static Object anObject6;

    @OriginalMember(owner = "client!fia", name = "g", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___13 = new CutsceneActionType(32);

    @OriginalMember(owner = "client!fia", name = "a", descriptor = "(IBIIIII)V")
    public static void addLocationOccluder(@OriginalArg(0) int occlusionType, @OriginalArg(2) int occlusionOffset, @OriginalArg(3) int z, @OriginalArg(4) int x, @OriginalArg(5) int level, @OriginalArg(6) int occlusionHeight) {
        if (occlusionType != 8 && occlusionType != 16) {
            @Pc(163) Tile tile = Static334.activeTiles[level][x][z];
            if (tile == null) {
                tile = new Tile(level);
            }
            if (occlusionType == 1) {
                tile.aShort86 = (short) occlusionOffset;
                tile.aShort84 = (short) occlusionHeight;
            } else if (occlusionType == 2) {
                tile.aShort83 = (short) occlusionHeight;
                tile.aShort85 = (short) occlusionOffset;
            }
            if (Static442.occludersDirty) {
                Static416.rebuildActiveOccluders();
            }
            return;
        }
        @Pc(22) int x1;
        @Pc(26) int x2;
        @Pc(30) int z1;
        @Pc(34) int z2;
        @Pc(42) int y1;
        @Pc(54) int y2;
        if (occlusionType != 8) {
            x1 = Static340.anInt5586 + (x << EnvironmentLight.anInt1066);
            x2 = x1 - Static340.anInt5586;
            z1 = z << EnvironmentLight.anInt1066;
            z2 = z1 + Static340.anInt5586;
            y1 = Static706.floor[level].getHeight(x + 1, z);
            y2 = Static706.floor[level].getHeight(x, z + 1);
            Static285.locOccluders[Static150.locOccluderCount++] = new LocOccluder(occlusionType, level, x1, x2, x2, x1, y1, y2, y2 - occlusionHeight, y1 - occlusionHeight, z1, z2, z2, z1);
            return;
        }
        x1 = x << EnvironmentLight.anInt1066;
        x2 = Static340.anInt5586 + x1;
        z1 = z << EnvironmentLight.anInt1066;
        z2 = z1 + Static340.anInt5586;
        y1 = Static706.floor[level].getHeight(x, z);
        y2 = Static706.floor[level].getHeight(x - -1, z + 1);
        Static285.locOccluders[Static150.locOccluderCount++] = new LocOccluder(occlusionType, level, x1, x2, x2, x1, y1, y2, y2 - occlusionHeight, -occlusionHeight + y1, z1, z2, z2, z1);
    }

    @OriginalMember(owner = "client!fia", name = "a", descriptor = "(IIILclient!tla;Lclient!tla;)V")
    public static void setWallDecor(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) WallDecor primaryDecor, @OriginalArg(4) WallDecor secondaryDecor) {
        @Pc(4) Tile tile = Static347.getTile(level, x, z);
        if (tile == null) {
            return;
        }
        tile.wallDecor = primaryDecor;
        tile.wallDecor2 = secondaryDecor;
        @Pc(22) int groundIndex = Static246.ground == Static693.underwaterGround ? 1 : 0;
        if (!primaryDecor.isStationary()) {
            primaryDecor.nextEntity = Static468.dynamicEntities[groundIndex];
            Static468.dynamicEntities[groundIndex] = primaryDecor;
        } else if (primaryDecor.isTransparent(0)) {
            primaryDecor.nextEntity = Static398.transparentStationaryEntities[groundIndex];
            Static398.transparentStationaryEntities[groundIndex] = primaryDecor;
        } else {
            primaryDecor.nextEntity = Static576.opaqueStationaryEntities[groundIndex];
            Static576.opaqueStationaryEntities[groundIndex] = primaryDecor;
            Static75.hasOpaqueStationaryEntities = true;
        }
        if (secondaryDecor == null) {
            return;
        }
        if (secondaryDecor.isStationary()) {
            if (secondaryDecor.isTransparent(0)) {
                secondaryDecor.nextEntity = Static398.transparentStationaryEntities[groundIndex];
                Static398.transparentStationaryEntities[groundIndex] = secondaryDecor;
                return;
            }
            secondaryDecor.nextEntity = Static576.opaqueStationaryEntities[groundIndex];
            Static576.opaqueStationaryEntities[groundIndex] = secondaryDecor;
            Static75.hasOpaqueStationaryEntities = true;
            return;
        }
        secondaryDecor.nextEntity = Static468.dynamicEntities[groundIndex];
        Static468.dynamicEntities[groundIndex] = secondaryDecor;
    }
}
