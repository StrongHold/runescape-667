import com.jagex.Entity;
import com.jagex.core.algorithms.Quicksort;
import com.jagex.game.runetek6.sound.Audio;
import com.jagex.sound.QueueBuss;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static517 {

    @OriginalMember(owner = "client!qea", name = "c", descriptor = "Lclient!dfa;")
    public static final Class77 aClass77_5 = new Class77();

    @OriginalMember(owner = "client!qea", name = "e", descriptor = "Lclient!hc;")
    public static final CutsceneActionType A_CUTSCENE_ACTION_TYPE___36 = new CutsceneActionType(15);

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(ZIII)V")
    public static void method6822(@OriginalArg(0) boolean stereo) {
        if (22050 > 48000) {
            throw new IllegalArgumentException();
        }
        Static156.anInt2679 = 2;
        Audio.sampleRate = 22050;
        QueueBuss.stereo = stereo;
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(Z[[[BIBIIZ)V")
    public static void method6823(@OriginalArg(0) boolean arg0, @OriginalArg(1) byte[][][] arg1, @OriginalArg(2) int arg2, @OriginalArg(3) byte arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) boolean arg6) {
        @Pc(6) int local6 = arg0 ? 1 : 0;
        Static546.onscreenOpaqueEntityCount = 0;
        Static645.onscreenTransparentEntityCount = 0;
        Static675.anInt10155++;
        @Pc(22) Entity local22;
        if ((arg5 & 0x2) == 0) {
            for (local22 = Static576.opaqueStationaryEntities[local6]; local22 != null; local22 = local22.nextEntity) {
                if (!Static208.method3107(local22, arg0, arg1, arg2, arg3)) {
                    projectToScreen(local22);
                    if (local22.anInt10697 != -1) {
                        Static48.aEntityArray3[Static546.onscreenOpaqueEntityCount++] = local22;
                    }
                }
            }
        }
        @Pc(157) int local157;
        if ((arg5 & 0x1) == 0) {
            for (local22 = Static398.transparentStationaryEntities[local6]; local22 != null; local22 = local22.nextEntity) {
                if (!Static208.method3107(local22, arg0, arg1, arg2, arg3)) {
                    projectToScreen(local22);
                    if (local22.anInt10697 != -1) {
                        Static395.aEntityArray11[Static645.onscreenTransparentEntityCount++] = local22;
                    }
                }
            }
            for (@Pc(98) Entity local98 = Static468.dynamicEntities[local6]; local98 != null; local98 = local98.nextEntity) {
                if (!Static208.method3107(local98, arg0, arg1, arg2, arg3)) {
                    if (local98.isTransparent(0)) {
                        projectToScreen(local98);
                        if (local98.anInt10697 != -1) {
                            Static395.aEntityArray11[Static645.onscreenTransparentEntityCount++] = local98;
                        }
                    } else {
                        projectToScreen(local98);
                        if (local98.anInt10697 != -1) {
                            Static48.aEntityArray3[Static546.onscreenOpaqueEntityCount++] = local98;
                        }
                    }
                }
            }
            if (!arg0) {
                for (local157 = 0; local157 < Static125.dynamicEntityCount; local157++) {
                    if (!Static208.method3107(Static679.aPositionEntity[local157], arg0, arg1, arg2, arg3)) {
                        projectToScreen(Static679.aPositionEntity[local157]);
                        if (Static679.aPositionEntity[local157].anInt10697 != -1) {
                            if (Static679.aPositionEntity[local157].isTransparent(0)) {
                                Static395.aEntityArray11[Static645.onscreenTransparentEntityCount++] = Static679.aPositionEntity[local157];
                            } else {
                                Static48.aEntityArray3[Static546.onscreenOpaqueEntityCount++] = Static679.aPositionEntity[local157];
                            }
                        }
                    }
                }
            }
        }
        @Pc(225) int local225;
        if (Static546.onscreenOpaqueEntityCount > 0) {
            Quicksort.quicksort(Static48.aEntityArray3, 0, Static546.onscreenOpaqueEntityCount - 1);
            for (local225 = 0; local225 < Static546.onscreenOpaqueEntityCount; local225++) {
                Static632.method8368(Static48.aEntityArray3[local225], arg6);
            }
        }
        if (Static442.aBoolean500) {
            Static665.aToolkit_15.method8009(0, null);
        }
        if ((arg5 & 0x2) == 0) {
            for (local225 = Static296.tileMinLevel; local225 < Static299.tileMaxLevel; local225++) {
                @Pc(304) boolean[][] local304;
                @Pc(316) int local316;
                @Pc(323) int local323;
                @Pc(325) int local325;
                @Pc(263) int local263;
                if (local225 < arg2 || arg1 == null) {
                    local263 = Static258.aBooleanArrayArray3.length;
                    if (Static441.anInt6691 + Static258.aBooleanArrayArray3.length > Static619.tileMaxX) {
                        local263 -= Static441.anInt6691 + Static258.aBooleanArrayArray3.length - Static619.tileMaxX;
                    }
                    local157 = Static258.aBooleanArrayArray3[0].length;
                    if (Static220.anInt3562 + Static258.aBooleanArrayArray3[0].length > Static662.tileMaxZ) {
                        local157 -= Static220.anInt3562 + Static258.aBooleanArrayArray3[0].length - Static662.tileMaxZ;
                    }
                    local304 = Static142.aBooleanArrayArray1;
                    if (Static581.aBoolean657) {
                        if (Static661.aBoolean457) {
                            local304 = Static433.aBooleanArrayArrayArray5[local225];
                        }
                        for (local316 = Static231.anInt3734; local316 < local263; local316++) {
                            local323 = local316 + Static441.anInt6691 - Static231.anInt3734;
                            for (local325 = Static13.anInt148; local325 < local157; local325++) {
                                if (Static258.aBooleanArrayArray3[local316][local325] && !Static588.method7714(local325 + Static220.anInt3562 - Static13.anInt148, local225, local323)) {
                                    local304[local316][local325] = true;
                                } else {
                                    local304[local316][local325] = false;
                                }
                            }
                        }
                    }
                    if (Static661.aBoolean457) {
                        if (arg4 >= 0) {
                            Static246.ground[local225].renderTilesAtDepth(0, 0, 0, null, false, arg4, arg5);
                        } else {
                            Static246.ground[local225].renderTiles(0, 0, 0, null, false, arg5);
                        }
                        for (local316 = 0; local316 < Static32.anInt772; local316++) {
                            Static684.aClass302Array1[local316].addGround(new GroundRenderTask(local225 + 1));
                        }
                    } else if (arg4 >= 0) {
                        Static246.ground[local225].renderTilesAtDepth(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, true, arg4, arg5);
                    } else {
                        Static246.ground[local225].renderTiles(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, true, arg5);
                    }
                } else {
                    local263 = Static258.aBooleanArrayArray3.length;
                    if (Static441.anInt6691 + Static258.aBooleanArrayArray3.length > Static619.tileMaxX) {
                        local263 -= Static441.anInt6691 + Static258.aBooleanArrayArray3.length - Static619.tileMaxX;
                    }
                    local157 = Static258.aBooleanArrayArray3[0].length;
                    if (Static220.anInt3562 + Static258.aBooleanArrayArray3[0].length > Static662.tileMaxZ) {
                        local157 -= Static220.anInt3562 + Static258.aBooleanArrayArray3[0].length - Static662.tileMaxZ;
                    }
                    local304 = Static142.aBooleanArrayArray1;
                    if (Static581.aBoolean657) {
                        if (Static661.aBoolean457) {
                            local304 = Static433.aBooleanArrayArrayArray5[local225];
                        }
                        for (local316 = Static231.anInt3734; local316 < local263; local316++) {
                            local323 = local316 + Static441.anInt6691 - Static231.anInt3734;
                            for (local325 = Static13.anInt148; local325 < local157; local325++) {
                                local304[local316][local325] = false;
                                if (Static258.aBooleanArrayArray3[local316][local325]) {
                                    @Pc(344) int local344 = local325 + Static220.anInt3562 - Static13.anInt148;
                                    for (@Pc(346) int local346 = local225; local346 >= 0; local346--) {
                                        if (Static334.activeTiles[local346][local323][local344] != null && Static334.activeTiles[local346][local323][local344].level == local225) {
                                            if ((local346 < arg2 || arg1[local346][local323][local344] != arg3) && !Static588.method7714(local344, local225, local323)) {
                                                local304[local316][local325] = true;
                                                break;
                                            }
                                            local304[local316][local325] = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Static661.aBoolean457) {
                        if (arg4 >= 0) {
                            Static246.ground[local225].renderTilesAtDepth(0, 0, 0, null, false, arg4, arg5);
                        } else {
                            Static246.ground[local225].renderTiles(0, 0, 0, null, false, arg5);
                        }
                        for (local316 = 0; local316 < Static32.anInt772; local316++) {
                            Static684.aClass302Array1[local316].addGround(new GroundRenderTask(local225 + 1));
                        }
                    } else if (arg4 >= 0) {
                        Static246.ground[local225].renderTilesAtDepth(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, false, arg4, arg5);
                    } else {
                        Static246.ground[local225].renderTiles(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, false, arg5);
                    }
                }
            }
        }
        if (Static645.onscreenTransparentEntityCount > 0) {
            Static498.method6650(Static395.aEntityArray11, 0, Static645.onscreenTransparentEntityCount - 1);
            for (local225 = 0; local225 < Static645.onscreenTransparentEntityCount; local225++) {
                Static632.method8368(Static395.aEntityArray11[local225], arg6);
            }
        }
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(Lclient!eo;)V")
    public static void projectToScreen(@OriginalArg(0) Entity entity) {
        Static665.aToolkit_15.H(entity.x, entity.y + (entity.getMinY(2) >> 1), entity.z, Static486.anIntArray591);
        entity.anInt10692 = Static486.anIntArray591[0];
        entity.anInt10698 = Static486.anIntArray591[1];
        entity.anInt10697 = Static486.anIntArray591[2];
    }
}
