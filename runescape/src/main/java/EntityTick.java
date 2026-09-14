import com.jagex.core.util.TimeUtils;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class EntityTick {

    @OriginalMember(owner = "client!ph", name = "a", descriptor = "(ZBLclient!cg;)V")
    public static void tick(@OriginalArg(0) boolean cutscene, @OriginalArg(2) PathingEntity entity) {
        @Pc(7) int speed = -1;
        @Pc(16) int flags = 0;

        if (entity.exactMoveT1 > TimeUtils.clock) {
            Static441.exactMoveTick1(entity);
        } else if (entity.exactMoveT2 >= TimeUtils.clock) {
            Static354.exactMoveTick2(entity);
        } else {
            Static256.movementTick(entity, cutscene);
            speed = Static521.entityMoveSpeed;
            flags = Static524.entityMoveFlags;
        }

        if ((entity.x < 512) || (entity.z < 512) || (entity.x >= ((Static720.mapWidth * 512) - 512)) || (entity.z >= ((Static501.mapLength * 512) - 512))) {
            entity.actionAnimator.update(true, -1);
            for (@Pc(107) int local107 = 0; local107 < entity.spotAnims.length; local107++) {
                entity.spotAnims[local107].id = -1;
                entity.spotAnims[local107].animator.update(true, -1);
            }

            entity.exactMoveT1 = 0;
            speed = -1;
            entity.exactMoveT2 = 0;
            entity.actionAnimations = null;
            flags = 0;
            entity.x = entity.pathX[0] * 512 + entity.getSize() * 256;
            entity.z = entity.pathZ[0] * 512 + entity.getSize() * 256;
            entity.stopMoving();
        }

        if ((entity == PlayerEntity.self) && ((entity.x < 6144) || (entity.z < 6144) || (entity.x >= ((Static720.mapWidth * 512) - 6144)) || (((Static501.mapLength * 512) - 6144) <= entity.z))) {
            entity.actionAnimator.update(true, -1);
            for (@Pc(107) int local107 = 0; local107 < entity.spotAnims.length; local107++) {
                entity.spotAnims[local107].id = -1;
                entity.spotAnims[local107].animator.update(true, -1);
            }
            entity.exactMoveT1 = 0;
            entity.exactMoveT2 = 0;
            entity.actionAnimations = null;
            flags = 0;
            speed = -1;
            entity.x = entity.pathX[0] * 512 + entity.getSize() * 256;
            entity.z = entity.pathZ[0] * 512 + entity.getSize() * 256;
            entity.stopMoving();
        }

        @Pc(107) int deltaYaw = Static112.turnTick(entity);
        Static145.wornTargetTick(entity);
        Static651.basTick(speed, deltaYaw, flags, entity);
        PathingEntity.updateActionAnimator(entity, speed);
        Static50.animationTick(entity);
    }

    @OriginalMember(owner = "client!cl", name = "a", descriptor = "(B)V")
    public static void tickPlayers() {
        @Pc(7) int count = PlayerList.highResolutionCount;
        @Pc(9) int[] slots = PlayerList.highResolutionSlots;
        for (@Pc(16) int i = 0; i < count; i++) {
            @Pc(24) PlayerEntity player = PlayerList.highResolutionPlayers[slots[i]];
            if (player != null) {
                tick(false, player);
            }
        }
    }

    @OriginalMember(owner = "client!aha", name = "b", descriptor = "(I)V")
    public static void tickNpcs() {
        for (@Pc(7) int i = 0; i < NPCList.size; i++) {
            @Pc(13) int slot = NPCList.slots[i];
            @Pc(20) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(slot);
            if (node != null) {
                @Pc(25) NPCEntity npc = node.npc;
                tick(false, npc);
            }
        }
    }
}
