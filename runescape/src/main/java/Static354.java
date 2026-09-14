import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static354 {

    @OriginalMember(owner = "client!ld", name = "k", descriptor = "Z")
    public static boolean fovClampsSaved = false;

    @OriginalMember(owner = "client!ld", name = "f", descriptor = "Z")
    public static boolean showProfiling = false;

    @OriginalMember(owner = "client!ld", name = "a", descriptor = "(BLclient!cg;)V")
    public static void exactMoveTick2(@OriginalArg(1) PathingEntity entity) {
        @Pc(6) Animator animator = entity.actionAnimator;

        if (TimeUtils.clock == entity.exactMoveT2 || !animator.isAnimating() || animator.method9090()) {
            @Pc(34) int total = entity.exactMoveT2 - entity.exactMoveT1;
            @Pc(40) int elapsed = TimeUtils.clock - entity.exactMoveT1;
            @Pc(52) int x1 = (entity.exactMoveX1 * 512) + (entity.getSize() * 256);
            @Pc(64) int z1 = (entity.exactMoveZ1 * 512) + (entity.getSize() * 256);
            @Pc(76) int x2 = (entity.exactMoveX2 * 512) + (entity.getSize() * 256);
            @Pc(88) int z2 = (entity.exactMoveZ2 * 512) + (entity.getSize() * 256);
            entity.x = ((x2 * elapsed) + (x1 * (total - elapsed))) / total;
            entity.z = ((z2 * elapsed) + (z1 * (total - elapsed))) / total;
        }

        entity.delayedWalkingTicks = 0;

        if (entity.exactMoveDirection == 0) {
            entity.turn(8192, false);
        }
        if (entity.exactMoveDirection == 1) {
            entity.turn(12288, false);
        }
        if (entity.exactMoveDirection == 2) {
            entity.turn(0, false);
        }
        if (entity.exactMoveDirection == 3) {
            entity.turn(4096, false);
        }
    }
}
