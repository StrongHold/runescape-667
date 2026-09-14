import com.jagex.Client;
import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Scene {

    @OriginalMember(owner = "client!ro", name = "h", descriptor = "I")
    public static final int DEFAULT_FOG_COLOUR = 13156520;

    @OriginalMember(owner = "client!ro", name = "a", descriptor = "()V")
    public static void free() {
        if (Static478.aTileArrayArrayArray3 != null) {
            for (@Pc(3) int level = 0; level < Static478.aTileArrayArrayArray3.length; level++) {
                for (@Pc(6) int x = 0; x < Static619.tileMaxX; x++) {
                    for (@Pc(9) int z = 0; z < Static662.tileMaxZ; z++) {
                        if (Static478.aTileArrayArrayArray3[level][x][z] != null) {
                            Static478.aTileArrayArrayArray3[level][x][z].method6550();
                        }
                        Static478.aTileArrayArrayArray3[level][x][z] = null;
                    }
                }
            }
        }
        Static478.aTileArrayArrayArray3 = null;
        Static706.floor = null;
        if (Static420.aTileArrayArrayArray2 != null) {
            for (int level = 0; level < Static420.aTileArrayArrayArray2.length; level++) {
                for (int x = 0; x < Static619.tileMaxX; x++) {
                    for (int z = 0; z < Static662.tileMaxZ; z++) {
                        if (Static420.aTileArrayArrayArray2[level][x][z] != null) {
                            Static420.aTileArrayArrayArray2[level][x][z].method6550();
                        }
                        Static420.aTileArrayArrayArray2[level][x][z] = null;
                    }
                }
            }
        }
        Static420.aTileArrayArrayArray2 = null;
        Static693.underwaterGround = null;
        Static334.activeTiles = null;
        Static246.ground = null;
        Static258.aBooleanArrayArray3 = null;
        Static142.aBooleanArrayArray1 = null;
        Static102.anIntArray184 = null;
        Static433.aBooleanArrayArrayArray5 = null;
        Static275.aBooleanArrayArrayArray4 = null;
        Static370.freeOccluders();
        if (Static679.aPositionEntity != null) {
            for (int i = 0; i < Static125.dynamicEntityCount; i++) {
                Static679.aPositionEntity[i] = null;
            }
            Static125.dynamicEntityCount = 0;
        }
        Static576.opaqueStationaryEntities = null;
        Static398.transparentStationaryEntities = null;
        Static468.dynamicEntities = null;
        if (Static48.aEntityArray3 != null) {
            for (int i = 0; i < Static48.aEntityArray3.length; i++) {
                Static48.aEntityArray3[i] = null;
            }
            Static546.onscreenOpaqueEntityCount = 0;
        }
        if (Static395.aEntityArray11 != null) {
            for (int i = 0; i < Static395.aEntityArray11.length; i++) {
                Static395.aEntityArray11[i] = null;
            }
            Static645.onscreenTransparentEntityCount = 0;
        }
        if (EnvironmentLight.aEnvironmentLightArray1 != null) {
            for (int i = 0; i < Static319.anInt5080; i++) {
                EnvironmentLight.aEnvironmentLightArray1[i] = null;
            }
            for (int level = 0; level < Static299.tileMaxLevel; level++) {
                for (int x = 0; x < Static619.tileMaxX; x++) {
                    for (@Pc(217) int z = 0; z < Static662.tileMaxZ; z++) {
                        Client.tileLightFlags[level][x][z] = 0L;
                    }
                }
            }
            Static319.anInt5080 = 0;
        }
        Static638.clearPickableEntityPool();
        Static514.activePickList = Static514.scenePickList;
        Static514.activePickList.clear();
        Static421.waterBias = null;
        Static62.waterColour = null;
        Static272.waterDepth = null;
        if (Static226.aClass46Array7 != null) {
            Static227.method3354();
            Static665.aToolkit_15.allocateThreads(1);
            Static665.aToolkit_15.linkThreads(0);
        }
        if (MapArea.renderQueues != null) {
            MapArea.renderQueues = null;
        }
        Static665.aToolkit_15 = null;
    }
}
