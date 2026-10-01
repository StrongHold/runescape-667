import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static50 {

    @OriginalMember(owner = "client!bm", name = "c", descriptor = "Z")
    public static boolean previousFocus = true;

    @OriginalMember(owner = "client!bm", name = "e", descriptor = "Z")
    public static boolean highWaterDetail = false;

    @OriginalMember(owner = "client!bm", name = "a", descriptor = "(Lclient!cg;B)V")
    public static void animationTick(@OriginalArg(0) PathingEntity arg0) {
        @Pc(8) Animator local8 = arg0.animator;
        if (local8.isAnimating() && local8.tick(1) && local8.isFinished()) {
            if (arg0.ready) {
                local8.update(true, arg0.getBASType().ready());
                arg0.ready = local8.isAnimating();
            }
            local8.resetImmediately();
        }
        @Pc(75) Animator local75;
        for (@Pc(50) int local50 = 0; local50 < arg0.spotAnims.length; local50++) {
            if (arg0.spotAnims[local50].id != -1) {
                local75 = arg0.spotAnims[local50].animator;
                if (local75.isDelayed()) {
                    @Pc(88) SpotAnimationType local88 = SpotAnimationTypeList.instance.list(arg0.spotAnims[local50].id);
                    @Pc(92) SeqType local92 = local75.getAnimation();
                    if (local88.loopSeq) {
                        if (local92.animatingPrecedence == 3) {
                            if (arg0.animationPathPointer > 0 && TimeUtils.clock >= arg0.exactMoveT1 && arg0.exactMoveT2 < TimeUtils.clock) {
                                local75.update(true, -1);
                                arg0.spotAnims[local50].id = -1;
                                continue;
                            }
                        } else if (local92.animatingPrecedence == 1 && arg0.animationPathPointer > 0 && arg0.exactMoveT1 <= TimeUtils.clock && TimeUtils.clock > arg0.exactMoveT2) {
                            continue;
                        }
                    }
                }
                if (local75.tick(1) && local75.isFinished()) {
                    local75.update(true, -1);
                    arg0.spotAnims[local50].id = -1;
                }
            }
        }
        local75 = arg0.actionAnimator;
        if (local75.isAnimating()) {
            label83:
            {
                @Pc(214) SeqType local214 = local75.getAnimation();
                if (local214.animatingPrecedence == 3) {
                    if (arg0.animationPathPointer > 0 && arg0.exactMoveT1 <= TimeUtils.clock && TimeUtils.clock > arg0.exactMoveT2) {
                        arg0.actionAnimations = null;
                        local75.update(true, -1);
                        break label83;
                    }
                } else if (local214.animatingPrecedence == 1) {
                    if (arg0.animationPathPointer > 0 && TimeUtils.clock >= arg0.exactMoveT1 && arg0.exactMoveT2 < TimeUtils.clock) {
                        local75.setDelay(1);
                        break label83;
                    }
                    local75.setDelay(0);
                }
                if (local75.tick(1) && local75.isFinished()) {
                    arg0.actionAnimations = null;
                    local75.update(true, -1);
                }
            }
        }
        for (@Pc(313) int local313 = 0; local313 < arg0.wornAnimators.length; local313++) {
            @Pc(320) DelayedEntityAnimator local320 = arg0.wornAnimators[local313];
            if (local320 != null) {
                if (local320.entityDelay > 0) {
                    local320.entityDelay--;
                } else if (local320.tick(1) && local320.isFinished()) {
                    arg0.wornAnimators[local313] = null;
                }
            }
        }
    }

}
