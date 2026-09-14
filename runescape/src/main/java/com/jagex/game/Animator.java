package com.jagex.game;

import com.jagex.game.runetek6.config.seqtype.SeqReplayMode;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.graphics.Model;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!gu")
public class Animator {

    @OriginalMember(owner = "client!jh", name = "i", descriptor = "Z")
    public static boolean forceTweening = false;

    @OriginalMember(owner = "client!vt", name = "d", descriptor = "Lclient!bp;")
    public static SeqTypeList seqTL;

    @OriginalMember(owner = "client!bia", name = "a", descriptor = "(Lclient!bp;I)V")
    public static void setSeqTL(@OriginalArg(0) SeqTypeList seqTL) {
        Animator.seqTL = seqTL;
    }

    @OriginalMember(owner = "client!op", name = "a", descriptor = "(Lclient!gu;Lclient!ka;Lclient!gu;B)V")
    public static void blend(@OriginalArg(0) Animator animator, @OriginalArg(1) Model model, @OriginalArg(2) Animator other) {
        if (animator.resolveSequences() && other.resolveSequences()) {
            @Pc(12) SeqType thisAnimation = animator.animation;
            @Pc(15) SeqType otherAnimation = other.animation;
            model.method7477(other.frameOffset, animator.primarySequences.frame, animator.primarySequences.nextFrameset, other.primarySequences.nextFrame, animator.primarySequences.nextFrame, other.primarySequences.nextFrameset, animator.primarySequences.frameset, animator.frameOffset, thisAnimation.rotateNormals | otherAnimation.rotateNormals, otherAnimation.frameDurations[other.currentFrame], other.primarySequences.frameset, thisAnimation.blendFlags, thisAnimation.frameDurations[animator.currentFrame], other.primarySequences.frame);
        }
    }

    @OriginalMember(owner = "client!gu", name = "L", descriptor = "Lclient!cka;")
    public SeqType animation;

    @OriginalMember(owner = "client!gu", name = "b", descriptor = "I")
    public int delay;

    @OriginalMember(owner = "client!gu", name = "m", descriptor = "I")
    public int nextFrame;

    @OriginalMember(owner = "client!gu", name = "c", descriptor = "I")
    public int frameOffset;

    @OriginalMember(owner = "client!gu", name = "v", descriptor = "I")
    public int loopCount;

    @OriginalMember(owner = "client!gu", name = "j", descriptor = "I")
    public int currentFrame;

    @OriginalMember(owner = "client!gu", name = "G", descriptor = "Z")
    public boolean runSecondary = false;

    @OriginalMember(owner = "client!gu", name = "d", descriptor = "I")
    public int loopMode = 0;

    @OriginalMember(owner = "client!gu", name = "k", descriptor = "Z")
    public boolean finished = false;

    @OriginalMember(owner = "client!gu", name = "o", descriptor = "Lclient!nfa;")
    public final FrameSequenceManager primarySequences;

    @OriginalMember(owner = "client!gu", name = "J", descriptor = "Lclient!nfa;")
    public final FrameSequenceManager secondarySequences;

    @OriginalMember(owner = "client!gu", name = "<init>", descriptor = "(Z)V")
    protected Animator(@OriginalArg(0) boolean runSecondary) {
        this.runSecondary = runSecondary;
        this.primarySequences = new FrameSequenceManager();

        if (this.runSecondary) {
            this.secondarySequences = new FrameSequenceManager();
        } else {
            this.secondarySequences = null;
        }
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(ILclient!cka;B)V")
    protected void newFrame(@OriginalArg(0) int frame, @OriginalArg(1) SeqType animation) {
        /* empty */
    }

    @OriginalMember(owner = "client!gu", name = "b", descriptor = "(I)I")
    public final int getDelay() {
        return this.delay;
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(ILclient!ka;I)V")
    public final void animate(@OriginalArg(1) Model model, @OriginalArg(2) int rotation) {
        if (this.animation == null || !this.resolveSequences()) {
            return;
        }

        model.method7487(this.primarySequences.frameset, this.frameOffset, this.animation.frameDurations[this.currentFrame], this.primarySequences.nextFrameset, this.primarySequences.frame, this.primarySequences.nextFrame, rotation, this.animation.rotateNormals);

        if (this.runSecondary && this.animation.secondaryFrames != null && this.secondarySequences.resolved) {
            model.method7487(this.secondarySequences.frameset, this.frameOffset, this.animation.frameDurations[this.currentFrame], this.secondarySequences.nextFrameset, this.secondarySequences.frame, this.secondarySequences.nextFrame, rotation, this.animation.rotateNormals);
        }
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(BI)Z")
    public final boolean method9090() {
        @Pc(24) int remaining;
        return this.animation == null | (remaining = 1 - this.delay) <= 0 ? false : this.animation.tweened | this.frameOffset + remaining > this.animation.frameDurations[this.currentFrame];
    }

    @OriginalMember(owner = "client!gu", name = "b", descriptor = "(II)V")
    public final void setDelay(@OriginalArg(0) int delay) {
        this.delay = delay;
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(IIBIZ)V")
    public final void update(@OriginalArg(0) int animationId, @OriginalArg(1) int delay, @OriginalArg(3) int loopMode, @OriginalArg(4) boolean randomize) {
        if (this.getAnimationId() == animationId) {
            return;
        }

        if (animationId == -1) {
            this.animation = null;
        } else {
            if (this.animation == null || animationId != this.animation.id) {
                this.animation = seqTL.list(animationId);
            } else if (this.animation.replayMode == SeqReplayMode.STOP) {
                return;
            }

            this.delay = delay;
            this.loopMode = loopMode;
            this.loopCount = 0;

            if (randomize) {
                this.currentFrame = (int) ((double) this.animation.frames.length * Math.random());
                this.frameOffset = (int) (Math.random() * (double) this.animation.frameDurations[this.currentFrame]);
            } else {
                this.frameOffset = 0;
                this.currentFrame = 0;
            }

            this.nextFrame = this.currentFrame + 1;
            if (this.nextFrame < 0 || this.nextFrame >= this.animation.frames.length) {
                this.nextFrame = -1;
            }

            if (this.delay == 0) {
                this.newFrame(this.currentFrame, this.animation);
            }

            this.finished = false;
        }

        this.resetSequences();
    }

    @OriginalMember(owner = "client!gu", name = "b", descriptor = "(BI)V")
    public final void reset(@OriginalArg(1) int delay) {
        this.currentFrame = 0;
        this.nextFrame = this.animation.frames.length <= 1 ? -1 : 1;
        this.frameOffset = 0;
        this.finished = false;
        this.delay = delay;
        this.loopCount = 0;

        if (this.animation != null) {
            this.newFrame(this.currentFrame, this.animation);
            this.resetSequences();
        }
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(ILclient!gu;)V")
    public final void copyFrom(@OriginalArg(1) Animator other) {
        this.animation = other.animation;
        this.runSecondary = other.runSecondary;
        this.finished = other.finished;
        this.delay = other.delay;
        this.loopCount = other.loopCount;
        this.nextFrame = other.nextFrame;
        this.currentFrame = other.currentFrame;
        this.frameOffset = other.frameOffset;
        this.resetSequences();
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(III)V")
    public final void update(@OriginalArg(0) int delay, @OriginalArg(1) int animationId) {
        this.update(animationId, delay, 0, false);
    }

    @OriginalMember(owner = "client!gu", name = "f", descriptor = "(I)V")
    public final void resetImmediately() {
        this.reset(0);
    }

    @OriginalMember(owner = "client!gu", name = "d", descriptor = "(B)I")
    public final int functionMask() {
        if (!this.resolveSequences()) {
            return 0;
        }

        @Pc(18) int mask = 0;
        if (this.resolveSequences()) {
            mask = this.primarySequences.functionMask | 0x0;
            if (this.runSecondary && this.animation.secondaryFrames != null) {
                mask |= this.secondarySequences.functionMask;
            }
        }
        return mask;
    }

    @OriginalMember(owner = "client!gu", name = "g", descriptor = "(I)Z")
    public final boolean isFinished() {
        return this.finished;
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(ZII)V")
    public final void method9104(@OriginalArg(0) boolean randomize, @OriginalArg(1) int animationId, @OriginalArg(2) int dummy) {
        if (dummy != 838828768) {
            this.method9104(true, 51, 15);
        }
        this.update(animationId, 0, 0, randomize);
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(Lclient!ka;I)V")
    public final void method9105(@OriginalArg(0) Model model) {
        if (this.resolveSequences()) {
            model.method7493(this.primarySequences.frame, this.primarySequences.frameset);
            if (this.runSecondary && this.animation.secondaryFrames != null && this.secondarySequences.resolved) {
                model.method7493(this.secondarySequences.frame, this.secondarySequences.frameset);
            }
        }
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(B)Z")
    public final boolean isDelayed() {
        return this.delay != 0;
    }

    @OriginalMember(owner = "client!gu", name = "b", descriptor = "(B)Lclient!cka;")
    public final SeqType getAnimation() {
        return this.animation;
    }

    @OriginalMember(owner = "client!gu", name = "c", descriptor = "(I)V")
    public void resetSequences() {
        this.primarySequences.reset();
        if (this.runSecondary) {
            this.secondarySequences.reset();
        }
    }

    @OriginalMember(owner = "client!gu", name = "e", descriptor = "(I)Z")
    public final boolean isAnimating() {
        return this.animation != null;
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(IIZLclient!ka;)V")
    public final void animatePartial(@OriginalArg(0) int groupMask, @OriginalArg(3) Model model) {
        if (this.resolveSequences()) {
            model.method7496(this.frameOffset, this.primarySequences.frameset, this.primarySequences.nextFrameset, this.primarySequences.frame, null, this.animation.frameDurations[this.currentFrame], this.animation.rotateNormals, this.primarySequences.nextFrame, groupMask);
            if (this.runSecondary && this.animation.secondaryFrames != null && this.secondarySequences.resolved) {
                model.method7496(this.frameOffset, this.secondarySequences.frameset, this.secondarySequences.nextFrameset, this.secondarySequences.frame, null, this.animation.frameDurations[this.currentFrame], this.animation.rotateNormals, this.secondarySequences.nextFrame, groupMask);
            }
        }
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(I)Z")
    public final boolean resolveSequences() {
        if (this.animation == null) {
            return false;
        }
        @Pc(30) boolean resolved = this.primarySequences.resolve(seqTL, this.animation, this.nextFrame, this.currentFrame, this.animation.frames);
        if (resolved && this.runSecondary && this.animation.secondaryFrames != null) {
            this.secondarySequences.resolve(seqTL, this.animation, this.nextFrame, this.currentFrame, this.animation.secondaryFrames);
        }
        return resolved;
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(II)Z")
    public final boolean tick(@OriginalArg(1) int time) {
        if (this.animation == null || time == 0) {
            return false;
        }

        if (this.delay > 0) {
            if (this.delay >= time) {
                this.delay -= time;
                return false;
            }

            time -= this.delay;
            this.delay = 0;
            this.newFrame(this.currentFrame, this.animation);
        }

        time += this.frameOffset;

        @Pc(68) boolean tween = forceTweening | this.animation.tweened;
        if (time > 100 && this.animation.loopOffset > 0) {
            @Pc(89) int loopStart = this.animation.frames.length - this.animation.loopOffset;

            while (this.currentFrame < loopStart && time > this.animation.frameDurations[this.currentFrame]) {
                time -= this.animation.frameDurations[this.currentFrame];
                this.currentFrame++;
            }

            if (loopStart <= this.currentFrame) {
                @Pc(134) int duration = 0;
                for (@Pc(136) int i = loopStart; i < this.animation.frames.length; i++) {
                    duration += this.animation.frameDurations[i];
                }

                if (this.loopMode == 0) {
                    this.loopCount += time / duration;
                }

                time %= duration;
            }

            this.nextFrame = this.currentFrame + 1;
            tween = true;

            if (this.nextFrame >= this.animation.frames.length) {
                this.nextFrame -= this.animation.loopOffset;

                if (this.nextFrame < 0 || this.animation.frames.length <= this.nextFrame) {
                    this.nextFrame = -1;
                }
            }
        }

        while (this.animation.frameDurations[this.currentFrame] < time) {
            time -= this.animation.frameDurations[this.currentFrame++];
            tween = true;

            if (this.animation.frames.length <= this.currentFrame) {
                if (this.animation.loopOffset != -1 && this.loopMode != 2) {
                    this.currentFrame -= this.animation.loopOffset;
                    if (this.loopMode == 0) {
                        this.loopCount++;
                    }
                }

                if (this.loopCount >= this.animation.maxLoops || this.currentFrame < 0 || this.currentFrame >= this.animation.frames.length) {
                    this.finished = true;
                    break;
                }
            }

            this.newFrame(this.currentFrame, this.animation);
            this.nextFrame = this.currentFrame + 1;

            if (this.nextFrame >= this.animation.frames.length) {
                this.nextFrame -= this.animation.loopOffset;
                if (this.nextFrame < 0 || this.nextFrame >= this.animation.frames.length) {
                    this.nextFrame = -1;
                }
            }
        }

        this.frameOffset = time;

        if (tween) {
            this.resetSequences();
        }

        return tween;
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(ZI)V")
    public final void update(@OriginalArg(0) boolean randomize, @OriginalArg(1) int animationId) {
        if (!randomize) {
            this.method9104(true, -1, 58);
        }
        this.update(animationId, 0, 0, false);
    }

    @OriginalMember(owner = "client!gu", name = "c", descriptor = "(B)I")
    public final int getAnimationId() {
        return this.animation == null ? -1 : this.animation.id;
    }

    @OriginalMember(owner = "client!gu", name = "a", descriptor = "(Z)V")
    public final void restartLoop() {
        this.loopCount = 0;
    }
}
