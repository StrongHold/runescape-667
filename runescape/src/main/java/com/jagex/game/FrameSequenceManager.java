package com.jagex.game;

import com.jagex.AnimFrameset;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!nfa")
public final class FrameSequenceManager {

    @OriginalMember(owner = "client!nfa", name = "c", descriptor = "Lclient!rw;")
    public AnimFrameset frameset;

    @OriginalMember(owner = "client!nfa", name = "f", descriptor = "I")
    public int frame;

    @OriginalMember(owner = "client!nfa", name = "a", descriptor = "Lclient!rw;")
    public AnimFrameset nextFrameset;

    @OriginalMember(owner = "client!nfa", name = "k", descriptor = "I")
    public int nextFrame;

    @OriginalMember(owner = "client!nfa", name = "e", descriptor = "I")
    public int functionMask;

    @OriginalMember(owner = "client!nfa", name = "h", descriptor = "Z")
    public boolean resolved = false;

    @OriginalMember(owner = "client!nfa", name = "a", descriptor = "(Lclient!bp;BLclient!cka;II[I)Z")
    public boolean resolve(@OriginalArg(0) SeqTypeList seqTL, @OriginalArg(2) SeqType animation, @OriginalArg(3) int nextFrame, @OriginalArg(4) int currentFrame, @OriginalArg(5) int[] frames) {
        if (this.resolved) {
            return true;
        } else if (frames.length <= currentFrame) {
            return false;
        } else {
            this.frame = frames[currentFrame];
            this.frameset = seqTL.getFrameset(this.frame >> 16);
            this.frame &= 0xFFFF;
            if (this.frameset == null) {
                return false;
            }
            if (animation.tweened && nextFrame != -1 && frames.length > nextFrame) {
                this.nextFrame = frames[nextFrame];
                this.nextFrameset = seqTL.getFrameset(this.nextFrame >> 16);
                this.nextFrame &= 0xFFFF;
            }
            if (animation.rotateNormals) {
                this.functionMask |= 0x200;
            }
            if (this.frameset.hasColourTransform(this.frame)) {
                this.functionMask |= 0x80;
            }
            if (this.frameset.hasAlphaTransform(this.frame)) {
                this.functionMask |= 0x100;
            }
            if (this.frameset.hasBillboardTransform(this.frame)) {
                this.functionMask |= 0x400;
            }
            if (this.nextFrameset != null) {
                if (this.nextFrameset.hasColourTransform(this.nextFrame)) {
                    this.functionMask |= 0x80;
                }
                if (this.nextFrameset.hasAlphaTransform(this.nextFrame)) {
                    this.functionMask |= 0x100;
                }
                if (this.nextFrameset.hasBillboardTransform(this.nextFrame)) {
                    this.functionMask |= 0x400;
                }
            }
            this.resolved = true;
            this.functionMask |= 0x20;
            return true;
        }
    }

    @OriginalMember(owner = "client!nfa", name = "a", descriptor = "(B)V")
    public void reset() {
        this.functionMask = 0;
        this.resolved = false;
        this.frameset = this.nextFrameset = null;
    }
}
