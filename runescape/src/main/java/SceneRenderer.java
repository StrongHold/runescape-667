import com.jagex.PickableEntity;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.graphics.EnvironmentLight;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class SceneRenderer {

    @OriginalMember(owner = "client!it", name = "a", descriptor = "(IIII[[[B[I[I[I[I[IIBIIZZIIZ)V")
    public static void renderScene(@OriginalArg(0) int clock, @OriginalArg(1) int cameraX, @OriginalArg(2) int cameraY, @OriginalArg(3) int cameraZ, @OriginalArg(4) byte[][][] roofStamps, @OriginalArg(5) int[] arg5, @OriginalArg(6) int[] arg6, @OriginalArg(7) int[] arg7, @OriginalArg(8) int[] arg8, @OriginalArg(9) int[] arg9, @OriginalArg(10) int levels, @OriginalArg(11) byte roofStamp, @OriginalArg(12) int playerTileX, @OriginalArg(13) int playerTileZ, @OriginalArg(14) boolean flickerDisabled, @OriginalArg(16) int orthoZoom, @OriginalArg(17) int entitySkipFlags, @OriginalArg(18) boolean arg17) {
        Static29.aBoolean60 = true;
        Static442.aBoolean500 = Static665.aToolkit_15.getMaxLights() > 0;
        Static581.aBoolean657 = true;
        Static403.anInt6246 = cameraX >> EnvironmentLight.anInt1066;
        Static550.anInt8271 = cameraZ >> EnvironmentLight.anInt1066;
        Static499.anInt7492 = cameraX;
        Static715.anInt10810 = cameraZ;
        Static523.anInt3882 = cameraY;
        Static441.anInt6691 = Static403.anInt6246 - Static35.anInt813;
        if (Static441.anInt6691 < 0) {
            Static231.anInt3734 = -Static441.anInt6691;
            Static441.anInt6691 = 0;
        } else {
            Static231.anInt3734 = 0;
        }
        Static220.anInt3562 = Static550.anInt8271 - Static35.anInt813;
        if (Static220.anInt3562 < 0) {
            Static13.anInt148 = -Static220.anInt3562;
            Static220.anInt3562 = 0;
        } else {
            Static13.anInt148 = 0;
        }
        Static77.anInt1613 = Static403.anInt6246 + Static35.anInt813;
        if (Static77.anInt1613 > Static619.tileMaxX) {
            Static77.anInt1613 = Static619.tileMaxX;
        }
        Static692.anInt10370 = Static550.anInt8271 + Static35.anInt813;
        if (Static692.anInt10370 > Static662.tileMaxZ) {
            Static692.anInt10370 = Static662.tileMaxZ;
        }
        @Pc(79) boolean[][] savedTileVisibility = Static258.aBooleanArrayArray3;
        @Pc(81) boolean[][] savedPointVisibility = Static142.aBooleanArrayArray1;
        @Pc(85) int local85;
        @Pc(88) int local88;
        @Pc(90) int local90;
        if (Static581.aBoolean657) {
            for (local85 = 0; local85 < Static35.anInt813 + Static35.anInt813 + 2; local85++) {
                local88 = 0;
                local90 = 0;
                for (@Pc(92) int local92 = 0; local92 < Static35.anInt813 + Static35.anInt813 + 2; local92++) {
                    if (local92 > 1) {
                        Static102.anIntArray184[local92 - 2] = local88;
                    }
                    local88 = local90;
                    @Pc(112) int tileX = Static403.anInt6246 + local85 - Static35.anInt813;
                    @Pc(118) int tileZ = Static550.anInt8271 + local92 - Static35.anInt813;
                    @Pc(138) int local138;
                    if (tileX >= 0 && tileZ >= 0 && tileX < Static619.tileMaxX && tileZ < Static662.tileMaxZ) {
                        local138 = tileX << EnvironmentLight.anInt1066;
                        @Pc(142) int worldZ = tileZ << EnvironmentLight.anInt1066;
                        @Pc(159) int topY = Static706.floor[Static706.floor.length - 1].getHeight(tileX, tileZ) - (0x3E8 << EnvironmentLight.anInt1066 - 7);
                        @Pc(188) int bottomY = (Static693.underwaterGround == null ? Static706.floor[0].getHeight(tileX, tileZ) + Static340.anInt5586 : Static693.underwaterGround[0].getHeight(tileX, tileZ) + Static340.anInt5586) + (0x3E8 << EnvironmentLight.anInt1066 - 7);
                        local90 = orthoZoom >= 0 ? Static665.aToolkit_15.r(local138, topY, worldZ, local138, bottomY, worldZ, orthoZoom) : Static665.aToolkit_15.JA(local138, topY, worldZ, local138, bottomY, worldZ);
                        Static142.aBooleanArrayArray1[local85][local92] = local90 == 0;
                    } else {
                        local90 = -1;
                        Static142.aBooleanArrayArray1[local85][local92] = false;
                    }
                    if (local85 > 0 && local92 > 0) {
                        local138 = Static102.anIntArray184[local92 - 1] & Static102.anIntArray184[local92] & local88 & local90;
                        Static258.aBooleanArrayArray3[local85 - 1][local92 - 1] = local138 == 0;
                    }
                }
                Static102.anIntArray184[Static35.anInt813 + Static35.anInt813] = local88;
                Static102.anIntArray184[Static35.anInt813 + Static35.anInt813 + 1] = local90;
            }
            if (orthoZoom >= 0) {
                Static29.aBoolean60 = false;
            } else {
                Static617.anIntArray726 = arg5;
                Static714.anIntArray880 = arg6;
                Static419.anIntArray500 = arg7;
                Static219.anIntArray288 = arg8;
                Static665.anIntArray779 = arg9;
                Static725.buildOcclusionBuffer(Static665.aToolkit_15, levels);
            }
        } else {
            if (Static222.aBooleanArrayArray2 == null) {
                Static222.aBooleanArrayArray2 = new boolean[Static619.tileMaxX + Static619.tileMaxX + 1][Static662.tileMaxZ + Static619.tileMaxX + 1];
            }
            for (local85 = 0; local85 < Static222.aBooleanArrayArray2.length; local85++) {
                for (local88 = 0; local88 < Static222.aBooleanArrayArray2[0].length; local88++) {
                    Static222.aBooleanArrayArray2[local85][local88] = true;
                }
            }
            Static142.aBooleanArrayArray1 = Static222.aBooleanArrayArray2;
            Static258.aBooleanArrayArray3 = Static222.aBooleanArrayArray2;
            Static441.anInt6691 = 0;
            Static220.anInt3562 = 0;
            Static77.anInt1613 = Static619.tileMaxX;
            Static692.anInt10370 = Static662.tileMaxZ;
            Static29.aBoolean60 = false;
        }
        Static497.method6623(Static665.aToolkit_15);
        if (!Static514.activePickList.retained) {
            @Pc(387) LinkedList entities = Static514.activePickList.entities;
            for (@Pc(392) PickableEntity entity = (PickableEntity) entities.first(); entity != null; entity = (PickableEntity) entities.next()) {
                entity.unlink();
                Static281.recycle(entity);
            }
        }
        if (Static442.aBoolean500) {
            for (local85 = 0; local85 < Static319.anInt5080; local85++) {
                EnvironmentLight.aEnvironmentLightArray1[local85].method8241(flickerDisabled, clock);
            }
        }
        if (Static661.aBoolean457) {
            Static346.anIntArray420 = Static665.aToolkit_15.Y();
            Static665.aToolkit_15.K(Static238.anIntArray307);
            local85 = (Static238.anIntArray307[2] - Static238.anIntArray307[0]) / Static549.anInt9424;
            for (local88 = 0; local88 < Static549.anInt9424 - 1; local88++) {
                Static537.anIntArray633[local88] = local85 * (local88 + 1) + Static621.anIntArray766[local88];
            }
            for (local90 = 0; local90 < Static226.aClass46Array7.length; local90++) {
                Static226.aClass46Array7[local90].method1107();
            }
        }
        if (Static420.aTileArrayArrayArray2 != null) {
            if (Static661.aBoolean457) {
                Static341.method5033(0);
            }
            Static379.method5355(true);
            Static665.aToolkit_15.ra(-1, 1583160, 40, 127);
            Static517.method6823(true, roofStamps, levels, roofStamp, orthoZoom, entitySkipFlags, arg17);
            if (Static661.aBoolean457) {
                Static245.method8630();
            }
            Static665.aToolkit_15.pa();
            Static379.method5355(false);
        }
        Static517.method6823(false, roofStamps, levels, roofStamp, orthoZoom, entitySkipFlags, arg17);
        if (Static661.aBoolean457) {
            for (local85 = 0; local85 < Static299.tileMaxLevel; local85++) {
                Static275.aBooleanArrayArrayArray4[local85] = Static433.aBooleanArrayArrayArray5[local85];
            }
            Static341.method5033(0);
            for (local88 = 0; local88 < Static226.aClass46Array7.length; local88++) {
                Static226.aClass46Array7[local88].method1107();
            }
        }
        if (Static661.aBoolean457) {
            Static245.method8630();
            for (local85 = 0; local85 < Static299.tileMaxLevel; local85++) {
                Static433.aBooleanArrayArrayArray5[local85] = Static275.aBooleanArrayArrayArray4[local85];
            }
            if (Static32.anInt772 == 2) {
                @Pc(601) int local601;
                if (Static134.aLongArray20[0] < Static134.aLongArray20[1]) {
                    if (Static537.anIntArray633[0] + Static621.anIntArray766[0] > Static238.anIntArray307[0]) {
                        local601 = Static621.anIntArray766[0]++;
                    }
                } else if (Static134.aLongArray20[0] > Static134.aLongArray20[1] && Static537.anIntArray633[0] + Static621.anIntArray766[0] < Static238.anIntArray307[2]) {
                    local601 = Static621.anIntArray766[0]--;
                }
            }
        }
        if (!Static581.aBoolean657) {
            Static258.aBooleanArrayArray3 = savedTileVisibility;
            Static142.aBooleanArrayArray1 = savedPointVisibility;
        }
        Static334.w2debug();
    }
}
