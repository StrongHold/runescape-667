import com.jagex.core.util.TimeUtils;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static527 {

    @OriginalMember(owner = "client!qka", name = "i", descriptor = "[Lclient!nba;")
    public static final HintArrow[] hintArrows = new HintArrow[8];

    @OriginalMember(owner = "client!qka", name = "a", descriptor = "(IBIII)V")
    public static void method7081(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        Static232.anInt3829 = arg1;
        Static240.anInt3955 = arg2;
        Static275.anInt4424 = arg0;
        Static168.anInt2842 = arg3;
    }

    @OriginalMember(owner = "client!qka", name = "c", descriptor = "(I)V")
    public static void method7083() {
        for (@Pc(15) ProjectileAnimationNode projectileNode = (ProjectileAnimationNode) Static505.projectiles.first(); projectileNode != null; projectileNode = (ProjectileAnimationNode) Static505.projectiles.next()) {
            @Pc(20) ProjectileAnimation projectile = projectileNode.projectileAnimation;

            if (TimeUtils.clock > projectile.t2) {
                projectileNode.unlink();
                projectile.stopParticles();
            } else if (TimeUtils.clock >= projectile.t1) {
                projectile.method816();

                if (projectile.entity2 > 0) {
                    if (CutsceneManager.state == 3) {
                        @Pc(61) PathingEntity entity = CutsceneManager.actors[projectile.entity2 - 1].entity();

                        if (entity != null && entity.x >= 0 && entity.x < Static720.mapWidth * 512 && entity.z >= 0 && entity.z < Static501.mapLength * 512) {
                            projectile.target(Static102.averageHeight(projectile.level, entity.x, entity.z) - projectile.y2, TimeUtils.clock, entity.z, entity.x);
                        }
                    } else {
                        @Pc(130) NPCEntityNode npcNode = (NPCEntityNode) NPCList.local.get(projectile.entity2 - 1);

                        if (npcNode != null) {
                            @Pc(135) NPCEntity npc = npcNode.npc;

                            if (npc.x >= 0 && npc.x < Static720.mapWidth * 512 && npc.z >= 0 && npc.z < Static501.mapLength * 512) {
                                projectile.target(Static102.averageHeight(projectile.level, npc.x, npc.z) - projectile.y2, TimeUtils.clock, npc.z, npc.x);
                            }
                        }
                    }
                }

                if (projectile.entity2 < 0) {
                    @Pc(193) int slot = -projectile.entity2 - 1;
                    @Pc(198) PlayerEntity entity;

                    if (slot == PlayerList.activePlayerSlot) {
                        entity = PlayerEntity.self;
                    } else {
                        entity = PlayerList.highResolutionPlayers[slot];
                    }

                    if (entity != null && entity.x >= 0 && Static720.mapWidth * 512 > entity.x && entity.z >= 0 && Static501.mapLength * 512 > entity.z) {
                        projectile.target(Static102.averageHeight(projectile.level, entity.x, entity.z) - projectile.y2, TimeUtils.clock, entity.z, entity.x);
                    }
                }

                projectile.tick(Static35.currentTick);
                Static102.addPositionEntity(projectile, true);
            }
        }
    }

    @OriginalMember(owner = "client!qka", name = "a", descriptor = "(III)V")
    public static void buildTileColumn(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(23) boolean local23 = Static334.activeTiles[0][arg1][arg2] != null && Static334.activeTiles[0][arg1][arg2].tile != null;
        for (@Pc(25) int local25 = arg0; local25 >= 0; local25--) {
            if (Static334.activeTiles[local25][arg1][arg2] == null) {
                @Pc(47) Tile local47 = Static334.activeTiles[local25][arg1][arg2] = new Tile(local25);
                if (local23) {
                    local47.level++;
                }
            }
        }
    }
}
