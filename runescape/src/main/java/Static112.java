import com.jagex.core.io.Packet;
import com.jagex.graphics.Fonts;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.loading.screen.op.instance.BackgroundImageInstance;

public final class Static112 {

    @OriginalMember(owner = "client!dj", name = "n", descriptor = "Z")
    public static boolean locationModelsUploaded = false;

    @OriginalMember(owner = "client!dj", name = "a", descriptor = "(ILclient!cg;)I")
    public static int turnTick(@OriginalArg(1) PathingEntity entity) {
        if (entity.yawSpeed == 0) {
            return 0;
        }

        if (entity.target != -1) {
            @Pc(24) PathingEntity target = null;

            if (entity.target < 32768) {
                @Pc(54) NPCEntityNode node = (NPCEntityNode) NPCList.local.get(entity.target);

                if (node != null) {
                    target = node.npc;
                }
            } else if (entity.target >= 32768) {
                target = PlayerList.highResolutionPlayers[entity.target - 32768];
            }

            if (target != null) {
                @Pc(67) int deltaX = entity.x - target.x;
                @Pc(74) int deltaZ = entity.z - target.z;

                if (deltaX != 0 || deltaZ != 0) {
                    entity.turn((int) (Math.atan2(deltaX, deltaZ) * 2607.5945876176133D) & 0x3FFF);
                }
            }
        }

        if (entity instanceof PlayerEntity) {
            @Pc(104) PlayerEntity player = (PlayerEntity) entity;

            if (player.turnAngle != -1 && (player.pathPointer == 0 || player.delayedWalkingTicks > 0)) {
                player.turn(player.turnAngle);
                player.turnAngle = -1;
            }
        } else if (entity instanceof NPCEntity) {
            @Pc(138) NPCEntity npc = (NPCEntity) entity;

            if (npc.turnToX != -1 && (npc.pathPointer == 0 || npc.delayedWalkingTicks > 0)) {
                @Pc(67) int local67 = npc.x - (npc.turnToX - WorldMap.areaBaseX - WorldMap.areaBaseX) * 256;
                @Pc(74) int local74 = npc.z - (npc.turnToZ - WorldMap.areaBaseZ - WorldMap.areaBaseZ) * 256;

                if (local67 != 0 || local74 != 0) {
                    npc.turn((int) (Math.atan2(local67, local74) * 2607.5945876176133D) & 0x3FFF);
                }

                npc.turnToX = -1;
            }
        }

        return entity.method9303();
    }

    @OriginalMember(owner = "client!dj", name = "a", descriptor = "(BLclient!ge;)Lclient!jt;")
    public static BackgroundImageInstance method2106(@OriginalArg(1) Packet arg0) {
        @Pc(7) int local7 = arg0.g2();
        return new BackgroundImageInstance(local7);
    }

    @OriginalMember(owner = "client!dj", name = "e", descriptor = "(I)V")
    public static void method2109() {
        if (Static158.objSpriteToolkit != null) {
            Static158.objSpriteToolkit.free();
            Fonts.objSpriteFont = null;
            Static158.objSpriteToolkit = null;
        }
    }
}
