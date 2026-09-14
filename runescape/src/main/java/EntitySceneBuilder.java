import com.jagex.Static148;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class EntitySceneBuilder {

    @OriginalMember(owner = "client!client", name = "a", descriptor = "()V")
    public static void addOffCentreEntities() {
        @Pc(1) int playerCount = PlayerList.highResolutionCount;
        @Pc(3) int[] playerSlots = PlayerList.highResolutionSlots;
        @Pc(9) int count;
        if (CutsceneManager.state == 3) {
            count = CutsceneManager.actors.length;
        } else {
            count = Static353.noNpcs ? playerCount : playerCount + NPCList.size;
        }
        for (@Pc(21) int i = 0; i < count; i++) {
            @Pc(36) PathingEntity entity;
            if (CutsceneManager.state == 3) {
                @Pc(29) Actor actor = CutsceneManager.actors[i];
                if (!actor.initialised) {
                    continue;
                }
                entity = actor.entity();
            } else {
                if (i < playerCount) {
                    entity = PlayerList.highResolutionPlayers[playerSlots[i]];
                } else {
                    entity = ((NPCEntityNode) NPCList.local.get(NPCList.slots[i - playerCount])).npc;
                }
                if (entity.drawPriority < 0) {
                    continue;
                }
            }
            @Pc(68) int size = entity.getSize();
            if ((size & 0x1) == 0) {
                if ((entity.x & 0x1FF) == 0 && (entity.z & 0x1FF) == 0) {
                    continue;
                }
            } else if ((entity.x & 0x1FF) == 256 && (entity.z & 0x1FF) == 256) {
                continue;
            }
            entity.y = Static102.averageHeight(entity.level, entity.x, entity.z);
            Static102.addPositionEntity(entity, true);
        }
    }

    @OriginalMember(owner = "client!client", name = "n", descriptor = "(I)V")
    public static void addTileAlignedEntities(@OriginalArg(0) int level) {
        @Pc(1) int playerCount = PlayerList.highResolutionCount;
        @Pc(3) int[] playerSlots = PlayerList.highResolutionSlots;
        @Pc(9) int count;
        if (CutsceneManager.state == 3) {
            count = CutsceneManager.actors.length;
        } else {
            count = Static353.noNpcs ? playerCount : playerCount + NPCList.size;
        }
        for (@Pc(21) int i = 0; i < count; i++) {
            @Pc(36) PathingEntity entity;
            if (CutsceneManager.state == 3) {
                @Pc(29) Actor actor = CutsceneManager.actors[i];
                if (!actor.initialised) {
                    continue;
                }
                entity = actor.entity();
            } else {
                if (i < playerCount) {
                    entity = PlayerList.highResolutionPlayers[playerSlots[i]];
                } else {
                    entity = ((NPCEntityNode) NPCList.local.get(NPCList.slots[i - playerCount])).npc;
                }
                if (entity.level != level) {
                    continue;
                }
                if (entity.drawPriority < 0) {
                    entity.visible = false;
                    continue;
                }
            }
            entity.anInt10735 = 0;
            @Pc(80) int size = entity.getSize();
            if ((size & 0x1) == 0) {
                if ((entity.x & 0x1FF) != 0 || (entity.z & 0x1FF) != 0) {
                    entity.visible = false;
                    continue;
                }
            } else if ((entity.x & 0x1FF) != 256 || (entity.z & 0x1FF) != 256) {
                entity.visible = false;
                continue;
            }
            if (CutsceneManager.state != 3) {
                @Pc(135) int local135;
                @Pc(140) int local140;
                @Pc(166) int local166;
                if (size == 1) {
                    local135 = entity.x >> 9;
                    local140 = entity.z >> 9;
                    if (entity.drawPriority != Static341.entityDrawPriorities[local135][local140]) {
                        entity.visible = true;
                        continue;
                    }
                    if (Static148.anIntArrayArray64[local135][local140] > 1) {
                        local166 = Static148.anIntArrayArray64[local135][local140]--;
                        entity.visible = true;
                        continue;
                    }
                } else {
                    local135 = (size - 1) * 256 + 252;
                    local140 = entity.x - local135 >> 9;
                    @Pc(196) int local196 = entity.z - local135 >> 9;
                    @Pc(203) int local203 = entity.x + local135 >> 9;
                    @Pc(210) int local210 = entity.z + local135 >> 9;
                    if (!Static426.method1017(local203, local210, local140, local196, entity.drawPriority)) {
                        for (@Pc(221) int local221 = local140; local221 <= local203; local221++) {
                            for (@Pc(224) int local224 = local196; local224 <= local210; local224++) {
                                if (entity.drawPriority == Static341.entityDrawPriorities[local221][local224]) {
                                    local166 = Static148.anIntArrayArray64[local221][local224]--;
                                }
                            }
                        }
                        entity.visible = true;
                        continue;
                    }
                }
            }
            entity.visible = false;
            entity.y = Static102.averageHeight(entity.level, entity.x, entity.z);
            Static102.addPositionEntity(entity, true);
        }
    }

    @OriginalMember(owner = "client!client", name = "b", descriptor = "()V")
    public static void calculateDrawPriorities() {
        @Pc(1) int playerCount = PlayerList.highResolutionCount;
        @Pc(3) int[] playerSlots = PlayerList.highResolutionSlots;
        @Pc(8) int idleAnimations = ClientOptions.instance.idleAnimations.getValue();
        @Pc(30) boolean crowded = idleAnimations == 1 && playerCount > 200 || idleAnimations == 0 && playerCount > 50;
        @Pc(103) int local103;
        for (@Pc(32) int i = 0; i < playerCount; i++) {
            @Pc(39) PlayerEntity player = PlayerList.highResolutionPlayers[playerSlots[i]];
            if (!player.hasModel()) {
                player.drawPriority = -1;
            } else if (player.hideOnMap) {
                player.drawPriority = -1;
            } else {
                player.updateBounds();
                if (player.x1 >= 0 && player.z1 >= 0 && player.x2 < Static720.mapWidth && player.z2 < Static501.mapLength) {
                    player.aBoolean129 = player.ready ? crowded : false;
                    if (player == PlayerEntity.self) {
                        player.drawPriority = Integer.MAX_VALUE;
                    } else {
                        local103 = 0;
                        if (!player.visible) {
                            local103++;
                        }
                        if (player.healthClock > TimeUtils.clock) {
                            local103 += 2;
                        }
                        local103 += 5 - player.getSize() << 2;
                        if (player.showPIcon || player.clanmate) {
                            local103 += 512;
                        } else {
                            if (Static150.drawOrder == 0) {
                                local103 += 32;
                            } else {
                                local103 += 128;
                            }
                            local103 += 256;
                        }
                        player.drawPriority = local103 + 1;
                    }
                } else {
                    player.drawPriority = -1;
                }
            }
        }
        for (@Pc(155) int i = 0; i < NPCList.size; i++) {
            @Pc(166) NPCEntity npc = ((NPCEntityNode) NPCList.local.get(NPCList.slots[i])).npc;
            if (npc.hasType() && npc.type.isVisible(TimedVarDomain.instance)) {
                npc.updateBounds();
                if (npc.x1 >= 0 && npc.z1 >= 0 && npc.x2 < Static720.mapWidth && npc.z2 < Static501.mapLength) {
                    @Pc(213) int priority = 0;
                    if (!npc.visible) {
                        priority++;
                    }
                    if (npc.healthClock > TimeUtils.clock) {
                        priority += 2;
                    }
                    priority += 5 - npc.getSize() << 2;
                    if (Static150.drawOrder == 0) {
                        if (npc.type.isFollower) {
                            priority += 64;
                        } else {
                            priority += 128;
                        }
                    } else if (Static150.drawOrder == 1) {
                        if (npc.type.isFollower) {
                            priority += 32;
                        } else {
                            priority += 64;
                        }
                    }
                    if (npc.type.renderHighPriority) {
                        priority += 1024;
                    } else if (!npc.type.aBoolean503) {
                        priority += 256;
                    }
                    npc.drawPriority = priority + 1;
                } else {
                    npc.drawPriority = -1;
                }
            } else {
                npc.drawPriority = -1;
            }
        }
        for (local103 = 0; local103 < Static527.hintArrows.length; local103++) {
            @Pc(292) HintArrow hintArrow = Static527.hintArrows[local103];
            if (hintArrow != null) {
                if (hintArrow.type == 1) {
                    @Pc(308) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(hintArrow.entity);
                    if (node != null) {
                        @Pc(313) NPCEntity npc = node.npc;
                        if (npc.drawPriority >= 0) {
                            npc.drawPriority += 2048;
                        }
                    }
                } else if (hintArrow.type == 10) {
                    @Pc(333) PlayerEntity player = PlayerList.highResolutionPlayers[hintArrow.entity];
                    if (player != null && player != PlayerEntity.self && player.drawPriority >= 0) {
                        player.drawPriority += 2048;
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!client", name = "c", descriptor = "()V")
    public static void buildEntityStacks() {
        Static172.anInt2893 = 0;
        for (@Pc(3) int i = 0; i < NPCList.size; i++) {
            @Pc(14) NPCEntity npc = ((NPCEntityNode) NPCList.local.get(NPCList.slots[i])).npc;
            if (npc.visible && npc.method9304((byte) -123) != -1) {
                @Pc(34) int offset = (npc.getSize() - 1) * 256 + 252;
                @Pc(41) int tileX = npc.x - offset >> 9;
                @Pc(48) int tileZ = npc.z - offset >> 9;
                @Pc(55) PathingEntity occupant = Static184.method2798(tileX, tileZ, npc.level);
                if (occupant != null) {
                    @Pc(60) int id = occupant.slot;
                    if (occupant instanceof NPCEntity) {
                        id += 2048;
                    }
                    if (occupant.anInt10735 == 0 && occupant.method9304((byte) -121) != -1) {
                        Static324.anIntArray390[Static172.anInt2893] = id;
                        Static212.anIntArray283[Static172.anInt2893] = id;
                        Static172.anInt2893++;
                        occupant.anInt10735++;
                    }
                    Static324.anIntArray390[Static172.anInt2893] = id;
                    Static212.anIntArray283[Static172.anInt2893] = npc.slot + 2048;
                    Static172.anInt2893++;
                    occupant.anInt10735++;
                }
            }
        }
        Static163.method8852(Static212.anIntArray283, Static172.anInt2893 - 1, Static324.anIntArray390, 0);
    }

    @OriginalMember(owner = "client!client", name = "d", descriptor = "()V")
    public static void clearTilePriorities() {
        for (@Pc(1) int x = 0; x < Static720.mapWidth; x++) {
            @Pc(6) int[] priorities = Static341.entityDrawPriorities[x];
            for (@Pc(8) int z = 0; z < Static501.mapLength; z++) {
                priorities[z] = 0;
            }
        }
    }

    @OriginalMember(owner = "client!client", name = "l", descriptor = "(I)V")
    public static void recordTilePriorities(@OriginalArg(0) int level) {
        @Pc(1) int playerCount = PlayerList.highResolutionCount;
        @Pc(3) int[] playerSlots = PlayerList.highResolutionSlots;
        @Pc(9) int count;
        if (CutsceneManager.state == 3) {
            count = CutsceneManager.actors.length;
        } else {
            count = playerCount + NPCList.size;
        }
        for (@Pc(16) int i = 0; i < count; i++) {
            @Pc(31) PathingEntity entity;
            if (CutsceneManager.state == 3) {
                @Pc(24) Actor actor = CutsceneManager.actors[i];
                if (!actor.initialised) {
                    continue;
                }
                entity = actor.entity();
            } else {
                if (i < playerCount) {
                    entity = PlayerList.highResolutionPlayers[playerSlots[i]];
                } else {
                    entity = ((NPCEntityNode) NPCList.local.get(NPCList.slots[i - playerCount])).npc;
                }
                if (entity.level != level || entity.drawPriority < 0) {
                    continue;
                }
            }
            @Pc(69) int size = entity.getSize();
            if ((size & 0x1) == 0) {
                if ((entity.x & 0x1FF) != 0 || (entity.z & 0x1FF) != 0) {
                    continue;
                }
            } else if ((entity.x & 0x1FF) != 256 || (entity.z & 0x1FF) != 256) {
                continue;
            }
            @Pc(113) int local113;
            @Pc(118) int local118;
            @Pc(155) int local155;
            if (size == 1) {
                local113 = entity.x >> 9;
                local118 = entity.z >> 9;
                if (entity.drawPriority > Static341.entityDrawPriorities[local113][local118]) {
                    Static341.entityDrawPriorities[local113][local118] = entity.drawPriority;
                    Static148.anIntArrayArray64[local113][local118] = 1;
                } else if (entity.drawPriority == Static341.entityDrawPriorities[local113][local118]) {
                    local155 = Static148.anIntArrayArray64[local113][local118]++;
                }
            } else {
                local113 = (size - 1) * 256 + 60;
                local118 = entity.x - local113 >> 9;
                @Pc(182) int local182 = entity.z - local113 >> 9;
                @Pc(189) int local189 = entity.x + local113 >> 9;
                @Pc(196) int local196 = entity.z + local113 >> 9;
                for (@Pc(198) int local198 = local118; local198 <= local189; local198++) {
                    for (@Pc(201) int local201 = local182; local201 <= local196; local201++) {
                        if (entity.drawPriority > Static341.entityDrawPriorities[local198][local201]) {
                            Static341.entityDrawPriorities[local198][local201] = entity.drawPriority;
                            Static148.anIntArrayArray64[local198][local201] = 1;
                        } else if (entity.drawPriority == Static341.entityDrawPriorities[local198][local201]) {
                            local155 = Static148.anIntArrayArray64[local198][local201]++;
                        }
                    }
                }
            }
        }
    }
}
