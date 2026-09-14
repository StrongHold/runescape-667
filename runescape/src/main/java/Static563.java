import com.jagex.Client;
import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static563 {

    @OriginalMember(owner = "client!ro", name = "h", descriptor = "I")
    public static final int anInt8460 = 13156520;

    @OriginalMember(owner = "client!ro", name = "a", descriptor = "()V")
    public static void method7461() {
        @Pc(3) int local3;
        @Pc(6) int local6;
        @Pc(9) int local9;
        if (Static478.aTileArrayArrayArray3 != null) {
            for (local3 = 0; local3 < Static478.aTileArrayArrayArray3.length; local3++) {
                for (local6 = 0; local6 < Static619.tileMaxX; local6++) {
                    for (local9 = 0; local9 < Static662.tileMaxZ; local9++) {
                        if (Static478.aTileArrayArrayArray3[local3][local6][local9] != null) {
                            Static478.aTileArrayArrayArray3[local3][local6][local9].method6550();
                        }
                        Static478.aTileArrayArrayArray3[local3][local6][local9] = null;
                    }
                }
            }
        }
        Static478.aTileArrayArrayArray3 = null;
        Static706.floor = null;
        if (Static420.aTileArrayArrayArray2 != null) {
            for (local3 = 0; local3 < Static420.aTileArrayArrayArray2.length; local3++) {
                for (local6 = 0; local6 < Static619.tileMaxX; local6++) {
                    for (local9 = 0; local9 < Static662.tileMaxZ; local9++) {
                        if (Static420.aTileArrayArrayArray2[local3][local6][local9] != null) {
                            Static420.aTileArrayArrayArray2[local3][local6][local9].method6550();
                        }
                        Static420.aTileArrayArrayArray2[local3][local6][local9] = null;
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
        Static370.method5280();
        if (Static679.aPositionEntity != null) {
            for (local3 = 0; local3 < Static125.dynamicEntityCount; local3++) {
                Static679.aPositionEntity[local3] = null;
            }
            Static125.dynamicEntityCount = 0;
        }
        Static576.opaqueStationaryEntities = null;
        Static398.transparentStationaryEntities = null;
        Static468.dynamicEntities = null;
        if (Static48.aEntityArray3 != null) {
            for (local3 = 0; local3 < Static48.aEntityArray3.length; local3++) {
                Static48.aEntityArray3[local3] = null;
            }
            Static546.onscreenOpaqueEntityCount = 0;
        }
        if (Static395.aEntityArray11 != null) {
            for (local3 = 0; local3 < Static395.aEntityArray11.length; local3++) {
                Static395.aEntityArray11[local3] = null;
            }
            Static645.onscreenTransparentEntityCount = 0;
        }
        if (EnvironmentLight.aEnvironmentLightArray1 != null) {
            for (local3 = 0; local3 < Static319.anInt5080; local3++) {
                EnvironmentLight.aEnvironmentLightArray1[local3] = null;
            }
            for (local6 = 0; local6 < Static299.tileMaxLevel; local6++) {
                for (local9 = 0; local9 < Static619.tileMaxX; local9++) {
                    for (@Pc(217) int local217 = 0; local217 < Static662.tileMaxZ; local217++) {
                        Client.tileLightFlags[local6][local9][local217] = 0L;
                    }
                }
            }
            Static319.anInt5080 = 0;
        }
        Static638.method8393();
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
        if (Static684.aClass302Array1 != null) {
            Static684.aClass302Array1 = null;
        }
        Static665.aToolkit_15 = null;
    }
}
