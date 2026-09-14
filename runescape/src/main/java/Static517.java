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
    public static final CutsceneActionType HIT = new CutsceneActionType(15);

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(ZIII)V")
    public static void configureAudio(@OriginalArg(0) boolean stereo) {
        if (22050 > 48000) {
            throw new IllegalArgumentException();
        }
        Static156.soundThreadPriority = 2;
        Audio.sampleRate = 22050;
        QueueBuss.stereo = stereo;
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(Z[[[BIBIIZ)V")
    public static void renderScenePass(@OriginalArg(0) boolean underwater, @OriginalArg(1) byte[][][] roofStamps, @OriginalArg(2) int levels, @OriginalArg(3) byte roofStamp, @OriginalArg(4) int orthoZoom, @OriginalArg(5) int entitySkipFlags, @OriginalArg(6) boolean trackOrthoTiles) {
        @Pc(6) int ground = underwater ? 1 : 0;
        Static546.onscreenOpaqueEntityCount = 0;
        Static645.onscreenTransparentEntityCount = 0;
        Static675.anInt10155++;
        @Pc(22) Entity entity;
        if ((entitySkipFlags & 0x2) == 0) {
            for (entity = Static576.opaqueStationaryEntities[ground]; entity != null; entity = entity.nextEntity) {
                if (!Static208.method3107(entity, underwater, roofStamps, levels, roofStamp)) {
                    projectToScreen(entity);
                    if (entity.anInt10697 != -1) {
                        Static48.aEntityArray3[Static546.onscreenOpaqueEntityCount++] = entity;
                    }
                }
            }
        }
        @Pc(157) int local157;
        if ((entitySkipFlags & 0x1) == 0) {
            for (entity = Static398.transparentStationaryEntities[ground]; entity != null; entity = entity.nextEntity) {
                if (!Static208.method3107(entity, underwater, roofStamps, levels, roofStamp)) {
                    projectToScreen(entity);
                    if (entity.anInt10697 != -1) {
                        Static395.aEntityArray11[Static645.onscreenTransparentEntityCount++] = entity;
                    }
                }
            }
            for (@Pc(98) Entity dynamic = Static468.dynamicEntities[ground]; dynamic != null; dynamic = dynamic.nextEntity) {
                if (!Static208.method3107(dynamic, underwater, roofStamps, levels, roofStamp)) {
                    if (dynamic.isTransparent(0)) {
                        projectToScreen(dynamic);
                        if (dynamic.anInt10697 != -1) {
                            Static395.aEntityArray11[Static645.onscreenTransparentEntityCount++] = dynamic;
                        }
                    } else {
                        projectToScreen(dynamic);
                        if (dynamic.anInt10697 != -1) {
                            Static48.aEntityArray3[Static546.onscreenOpaqueEntityCount++] = dynamic;
                        }
                    }
                }
            }
            if (!underwater) {
                for (local157 = 0; local157 < Static125.dynamicEntityCount; local157++) {
                    if (!Static208.method3107(Static679.aPositionEntity[local157], underwater, roofStamps, levels, roofStamp)) {
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
                Static632.method8368(Static48.aEntityArray3[local225], trackOrthoTiles);
            }
        }
        if (Static442.aBoolean500) {
            Static665.aToolkit_15.method8009(0, null);
        }
        if ((entitySkipFlags & 0x2) == 0) {
            for (local225 = Static296.tileMinLevel; local225 < Static299.tileMaxLevel; local225++) {
                @Pc(304) boolean[][] visibleTiles;
                @Pc(316) int local316;
                @Pc(323) int tileX;
                @Pc(325) int offsetZ;
                @Pc(263) int maxOffsetX;
                if (local225 < levels || roofStamps == null) {
                    maxOffsetX = Static258.aBooleanArrayArray3.length;
                    if (Static441.anInt6691 + Static258.aBooleanArrayArray3.length > Static619.tileMaxX) {
                        maxOffsetX -= Static441.anInt6691 + Static258.aBooleanArrayArray3.length - Static619.tileMaxX;
                    }
                    local157 = Static258.aBooleanArrayArray3[0].length;
                    if (Static220.baseTileZ + Static258.aBooleanArrayArray3[0].length > Static662.tileMaxZ) {
                        local157 -= Static220.baseTileZ + Static258.aBooleanArrayArray3[0].length - Static662.tileMaxZ;
                    }
                    visibleTiles = Static142.aBooleanArrayArray1;
                    if (Static581.aBoolean657) {
                        if (Static661.aBoolean457) {
                            visibleTiles = Static433.aBooleanArrayArrayArray5[local225];
                        }
                        for (local316 = Static231.anInt3734; local316 < maxOffsetX; local316++) {
                            tileX = local316 + Static441.anInt6691 - Static231.anInt3734;
                            for (offsetZ = Static13.anInt148; offsetZ < local157; offsetZ++) {
                                if (Static258.aBooleanArrayArray3[local316][offsetZ] && !Static588.method7714(offsetZ + Static220.baseTileZ - Static13.anInt148, local225, tileX)) {
                                    visibleTiles[local316][offsetZ] = true;
                                } else {
                                    visibleTiles[local316][offsetZ] = false;
                                }
                            }
                        }
                    }
                    if (Static661.aBoolean457) {
                        if (orthoZoom >= 0) {
                            Static246.ground[local225].renderTilesAtDepth(0, 0, 0, null, false, orthoZoom, entitySkipFlags);
                        } else {
                            Static246.ground[local225].renderTiles(0, 0, 0, null, false, entitySkipFlags);
                        }
                        for (local316 = 0; local316 < Static32.anInt772; local316++) {
                            Static684.aClass302Array1[local316].addGround(new GroundRenderTask(local225 + 1));
                        }
                    } else if (orthoZoom >= 0) {
                        Static246.ground[local225].renderTilesAtDepth(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, true, orthoZoom, entitySkipFlags);
                    } else {
                        Static246.ground[local225].renderTiles(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, true, entitySkipFlags);
                    }
                } else {
                    maxOffsetX = Static258.aBooleanArrayArray3.length;
                    if (Static441.anInt6691 + Static258.aBooleanArrayArray3.length > Static619.tileMaxX) {
                        maxOffsetX -= Static441.anInt6691 + Static258.aBooleanArrayArray3.length - Static619.tileMaxX;
                    }
                    local157 = Static258.aBooleanArrayArray3[0].length;
                    if (Static220.baseTileZ + Static258.aBooleanArrayArray3[0].length > Static662.tileMaxZ) {
                        local157 -= Static220.baseTileZ + Static258.aBooleanArrayArray3[0].length - Static662.tileMaxZ;
                    }
                    visibleTiles = Static142.aBooleanArrayArray1;
                    if (Static581.aBoolean657) {
                        if (Static661.aBoolean457) {
                            visibleTiles = Static433.aBooleanArrayArrayArray5[local225];
                        }
                        for (local316 = Static231.anInt3734; local316 < maxOffsetX; local316++) {
                            tileX = local316 + Static441.anInt6691 - Static231.anInt3734;
                            for (offsetZ = Static13.anInt148; offsetZ < local157; offsetZ++) {
                                visibleTiles[local316][offsetZ] = false;
                                if (Static258.aBooleanArrayArray3[local316][offsetZ]) {
                                    @Pc(344) int tileZ = offsetZ + Static220.baseTileZ - Static13.anInt148;
                                    for (@Pc(346) int searchLevel = local225; searchLevel >= 0; searchLevel--) {
                                        if (Static334.activeTiles[searchLevel][tileX][tileZ] != null && Static334.activeTiles[searchLevel][tileX][tileZ].level == local225) {
                                            if ((searchLevel < levels || roofStamps[searchLevel][tileX][tileZ] != roofStamp) && !Static588.method7714(tileZ, local225, tileX)) {
                                                visibleTiles[local316][offsetZ] = true;
                                                break;
                                            }
                                            visibleTiles[local316][offsetZ] = false;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Static661.aBoolean457) {
                        if (orthoZoom >= 0) {
                            Static246.ground[local225].renderTilesAtDepth(0, 0, 0, null, false, orthoZoom, entitySkipFlags);
                        } else {
                            Static246.ground[local225].renderTiles(0, 0, 0, null, false, entitySkipFlags);
                        }
                        for (local316 = 0; local316 < Static32.anInt772; local316++) {
                            Static684.aClass302Array1[local316].addGround(new GroundRenderTask(local225 + 1));
                        }
                    } else if (orthoZoom >= 0) {
                        Static246.ground[local225].renderTilesAtDepth(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, false, orthoZoom, entitySkipFlags);
                    } else {
                        Static246.ground[local225].renderTiles(Static403.anInt6246, Static550.anInt8271, Static35.anInt813, Static142.aBooleanArrayArray1, false, entitySkipFlags);
                    }
                }
            }
        }
        if (Static645.onscreenTransparentEntityCount > 0) {
            Static498.method6650(Static395.aEntityArray11, 0, Static645.onscreenTransparentEntityCount - 1);
            for (local225 = 0; local225 < Static645.onscreenTransparentEntityCount; local225++) {
                Static632.method8368(Static395.aEntityArray11[local225], trackOrthoTiles);
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
