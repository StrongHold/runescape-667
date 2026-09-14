import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static157 {

    @OriginalMember(owner = "client!eu", name = "a", descriptor = "(IIIILclient!nda;)V")
    public static void setObjStack(@OriginalArg(0) int level, @OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) int y, @OriginalArg(4) Class8_Sub2_Sub5 entity) {
        @Pc(4) Tile tile = Static347.getTile(level, x, z);
        if (tile == null) {
            return;
        }
        entity.x = (x << EnvironmentLight.anInt1066) + EnvironmentLight.anInt3993;
        entity.y = y;
        entity.z = (z << EnvironmentLight.anInt1066) + EnvironmentLight.anInt3993;
        tile.aClass8_Sub2_Sub5_1 = entity;
        @Pc(36) int groundIndex = Static246.ground == Static693.underwaterGround ? 1 : 0;
        if (entity.isStationary()) {
            if (entity.isTransparent(0)) {
                entity.nextEntity = Static398.transparentStationaryEntities[groundIndex];
                Static398.transparentStationaryEntities[groundIndex] = entity;
                return;
            }
            entity.nextEntity = Static576.opaqueStationaryEntities[groundIndex];
            Static576.opaqueStationaryEntities[groundIndex] = entity;
            Static75.hasOpaqueStationaryEntities = true;
            return;
        }
        entity.nextEntity = Static468.dynamicEntities[groundIndex];
        Static468.dynamicEntities[groundIndex] = entity;
    }
}
