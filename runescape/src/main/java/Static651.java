import com.jagex.EntityMoveFlag;
import com.jagex.Static14;
import com.jagex.game.Animator;
import com.jagex.game.MoveSpeed;
import com.jagex.game.runetek6.config.bastype.BASType;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static651 {

    @OriginalMember(owner = "client!uja", name = "a", descriptor = "(IIIILclient!cg;)V")
    public static void basTick(@OriginalArg(0) int moveSpeed, @OriginalArg(1) int deltaYaw, @OriginalArg(2) int moveFlags, @OriginalArg(4) PathingEntity entity) {
        @Pc(9) BASType basType = entity.getBASType();
        @Pc(19) Animator animator = entity.animator;
        @Pc(29) int turnRemaining = entity.yawTarget - entity.yaw.value & 0x3FFF;

        if (moveSpeed == MoveSpeed.STATIONARY) {
            if (turnRemaining == 0 && entity.anInt10749 <= 25) {
                if (!entity.ready || !basType.isReady(animator.getAnimationId())) {
                    animator.update(true, basType.ready());
                    entity.ready = animator.isAnimating();
                }
            } else {
                if (deltaYaw < 0 && basType.readyTurnCcw != -1) {
                    animator.update(true, basType.readyTurnCcw);
                    entity.ready = false;
                } else if (deltaYaw > 0 && basType.readyTurnCw != -1) {
                    animator.update(true, basType.readyTurnCw);
                    entity.ready = false;
                } else if (!entity.ready || !basType.isReady(animator.getAnimationId())) {
                    animator.update(true, basType.ready());
                    entity.ready = entity.animator.isAnimating();
                }
            }
        } else if (entity.target != -1 && (turnRemaining >= 10240 || turnRemaining <= 2048)) {
            @Pc(172) int moveYawDelta = EntityMoveFlag.FLAG_TO_YAW[moveFlags] - entity.yaw.value & 0x3FFF;

            if (moveSpeed == MoveSpeed.RUN && basType.run != -1) {
                if (moveYawDelta > 2048 && moveYawDelta <= 6144 && basType.runFollowTurnCw != -1) {
                    animator.update(true, basType.runFollowTurnCw);
                } else if (moveYawDelta >= 10240 && moveYawDelta < 14336 && basType.runFollowTurnCcw != -1) {
                    animator.update(true, basType.runFollowTurnCcw);
                } else if (moveYawDelta <= 6144 || moveYawDelta >= 10240 || basType.runFollowTurn180 == -1) {
                    animator.update(true, basType.run);
                } else {
                    animator.update(true, basType.runFollowTurn180);
                }
            } else if (moveSpeed == MoveSpeed.CRAWL && basType.crawl != -1) {
                if (moveYawDelta > 2048 && moveYawDelta <= 6144 && basType.crawlFollowTurnCw != -1) {
                    animator.update(true, basType.crawlFollowTurnCw);
                } else if (moveYawDelta >= 10240 && moveYawDelta < 14336 && basType.crawlFollowTurnCcw != -1) {
                    animator.update(true, basType.crawlFollowTurnCcw);
                } else if (moveYawDelta <= 6144 || moveYawDelta >= 10240 || basType.crawlFollowTurn180 == -1) {
                    animator.update(true, basType.crawl);
                } else {
                    animator.update(true, basType.crawlFollowTurn180);
                }
            } else {
                if (moveYawDelta > 2048 && moveYawDelta <= 6144 && basType.walkFollowTurnCw != -1) {
                    animator.update(true, basType.walkFollowTurnCw);
                } else if (moveYawDelta >= 10240 && moveYawDelta < 14336 && basType.walkFollowTurnCcw != -1) {
                    animator.update(true, basType.walkFollowTurnCcw);
                } else if (moveYawDelta <= 6144 || moveYawDelta >= 10240 || basType.walkFollowTurn180 == -1) {
                    animator.update(true, basType.walk);
                } else {
                    animator.update(true, basType.walkFollowTurn180);
                }
            }

            entity.ready = false;
        } else if (turnRemaining == 0 && entity.anInt10749 <= 25) {
            if (moveSpeed == MoveSpeed.RUN && basType.run != -1) {
                animator.update(true, basType.run);
            } else if (moveSpeed == MoveSpeed.CRAWL && basType.crawl != -1) {
                animator.update(true, basType.crawl);
            } else {
                animator.update(true, basType.walk);
            }

            entity.ready = false;
        } else {
            if (moveSpeed == MoveSpeed.RUN && basType.run != -1) {
                if (deltaYaw < 0 && basType.runTurnCcw != -1) {
                    animator.update(true, basType.runTurnCcw);
                } else if (deltaYaw <= 0 || basType.runTurnCw == -1) {
                    animator.update(true, basType.run);
                } else {
                    animator.update(true, basType.runTurnCw);
                }
            } else if (moveSpeed == MoveSpeed.CRAWL && basType.crawl != -1) {
                if (deltaYaw < 0 && basType.crawlTurnCcw != -1) {
                    animator.update(true, basType.crawlTurnCcw);
                } else if (deltaYaw <= 0 || basType.crawlTurnCw == -1) {
                    animator.update(true, basType.crawl);
                } else {
                    animator.update(true, basType.crawlTurnCw);
                }
            } else {
                if (deltaYaw < 0 && basType.walkTurnCcw != -1) {
                    animator.update(true, basType.walkTurnCcw);
                } else if (deltaYaw <= 0 || basType.walkTurnCw == -1) {
                    animator.update(true, basType.walk);
                } else {
                    animator.update(true, basType.walkTurnCw);
                }
            }

            entity.ready = false;
        }
    }

    @OriginalMember(owner = "client!uja", name = "a", descriptor = "(I)Z")
    public static boolean method8516() {
        return Static14.loadNativeLibrary("jaclib") ? Static14.loadNativeLibrary("hw3d") : false;
    }

}
