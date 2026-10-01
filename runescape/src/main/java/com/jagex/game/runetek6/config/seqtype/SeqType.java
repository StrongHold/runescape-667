package com.jagex.game.runetek6.config.seqtype;

import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!cka")
public final class SeqType {

    @OriginalMember(owner = "client!cka", name = "v", descriptor = "[I")
    public int[] soundRateMin;

    @OriginalMember(owner = "client!cka", name = "a", descriptor = "[I")
    public int[] soundRateMax;

    @OriginalMember(owner = "client!cka", name = "y", descriptor = "[[I")
    public int[][] soundInfo;

    @OriginalMember(owner = "client!cka", name = "z", descriptor = "[I")
    public int[] soundVolumes;

    @OriginalMember(owner = "client!cka", name = "e", descriptor = "[Z")
    public boolean[] blendFlags;

    @OriginalMember(owner = "client!cka", name = "k", descriptor = "[I")
    public int[] frameDurations;

    @OriginalMember(owner = "client!cka", name = "t", descriptor = "I")
    public int id;

    @OriginalMember(owner = "client!cka", name = "m", descriptor = "[I")
    public int[] secondaryFrames;

    @OriginalMember(owner = "client!cka", name = "B", descriptor = "[I")
    public int[] frames;

    @OriginalMember(owner = "client!cka", name = "l", descriptor = "I")
    public int loopOffset = -1;

    @OriginalMember(owner = "client!cka", name = "b", descriptor = "Z")
    public boolean rotateNormals = false;

    @OriginalMember(owner = "client!cka", name = "d", descriptor = "I")
    public int replayMode = SeqReplayMode.RESTART_LOOP;

    @OriginalMember(owner = "client!cka", name = "w", descriptor = "I")
    public int animatingPrecedence = -1;

    @OriginalMember(owner = "client!cka", name = "u", descriptor = "I")
    public int maxLoops = 99;

    @OriginalMember(owner = "client!cka", name = "f", descriptor = "I")
    public int priority = 5;

    @OriginalMember(owner = "client!cka", name = "j", descriptor = "Z")
    public boolean vorbisSound = false;

    @OriginalMember(owner = "client!cka", name = "g", descriptor = "Z")
    public boolean tweened = false;

    @OriginalMember(owner = "client!cka", name = "h", descriptor = "I")
    public int playerRightHand = -1;

    @OriginalMember(owner = "client!cka", name = "n", descriptor = "I")
    public int playerLeftHand = -1;

    @OriginalMember(owner = "client!cka", name = "C", descriptor = "I")
    public int walkingPrecedence = -1;

    @OriginalMember(owner = "client!cka", name = "b", descriptor = "(I)V")
    public void postDecode() {
        if (this.walkingPrecedence == -1) {
            if (this.blendFlags == null) {
                this.walkingPrecedence = 0;
            } else {
                this.walkingPrecedence = 2;
            }
        }

        if (this.animatingPrecedence == -1) {
            if (this.blendFlags == null) {
                this.animatingPrecedence = 0;
            } else {
                this.animatingPrecedence = 2;
            }
        }
    }

    @OriginalMember(owner = "client!cka", name = "a", descriptor = "(Lclient!ge;I)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(3) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(code, packet);
        }
    }

    @OriginalMember(owner = "client!cka", name = "a", descriptor = "(IBLclient!ge;)V")
    public void decode(@OriginalArg(0) int code, @OriginalArg(2) Packet packet) {
        if (code == 1) {
            @Pc(20) int count = packet.g2();

            this.frameDurations = new int[count];
            for (@Pc(26) int i = 0; i < count; i++) {
                this.frameDurations[i] = packet.g2();
            }

            this.frames = new int[count];
            for (@Pc(44) int i = 0; i < count; i++) {
                this.frames[i] = packet.g2();
            }
            for (@Pc(58) int i = 0; i < count; i++) {
                this.frames[i] = (packet.g2() << 16) + this.frames[i];
            }
        } else if (code == 2) {
            this.loopOffset = packet.g2();
        } else if (code == 3) {
            this.blendFlags = new boolean[256];

            @Pc(20) int count = packet.g1();
            for (@Pc(26) int i = 0; i < count; i++) {
                this.blendFlags[packet.g1()] = true;
            }
        } else if (code == 5) {
            this.priority = packet.g1();
        } else if (code == 6) {
            this.playerLeftHand = packet.g2();
        } else if (code == 7) {
            this.playerRightHand = packet.g2();
        } else if (code == 8) {
            this.maxLoops = packet.g1();
        } else if (code == 9) {
            this.animatingPrecedence = packet.g1();
        } else if (code == 10) {
            this.walkingPrecedence = packet.g1();
        } else if (code == 11) {
            this.replayMode = packet.g1();
        } else if (code == 12) {
            @Pc(20) int count = packet.g1();
            this.secondaryFrames = new int[count];

            for (@Pc(26) int i = 0; i < count; i++) {
                this.secondaryFrames[i] = packet.g2();
            }

            for (@Pc(44) int i = 0; i < count; i++) {
                this.secondaryFrames[i] += packet.g2() << 16;
            }
        } else if (code == 13) {
            @Pc(20) int count = packet.g2();
            this.soundInfo = new int[count][];
            for (@Pc(26) int i = 0; i < count; i++) {
                @Pc(44) int options = packet.g1();

                if (options > 0) {
                    this.soundInfo[i] = new int[options];
                    this.soundInfo[i][0] = packet.g3();

                    for (@Pc(58) int j = 1; j < options; j++) {
                        this.soundInfo[i][j] = packet.g2();
                    }
                }
            }
        } else if (code == 14) {
            this.rotateNormals = true;
        } else if (code == 15) {
            this.tweened = true;
        } else if (code == 16) {
            /* empty */
        } else if (code == 18) {
            this.vorbisSound = true;
        } else if (code == 19) {
            if (this.soundVolumes == null) {
                this.soundVolumes = new int[this.soundInfo.length];

                for (@Pc(20) int i = 0; i < this.soundInfo.length; i++) {
                    this.soundVolumes[i] = 255;
                }
            }
            this.soundVolumes[packet.g1()] = packet.g1();
        } else if (code == 20) {
            if (this.soundRateMin == null || this.soundRateMax == null) {
                this.soundRateMin = new int[this.soundInfo.length];
                this.soundRateMax = new int[this.soundInfo.length];

                for (@Pc(20) int i = 0; i < this.soundInfo.length; i++) {
                    this.soundRateMin[i] = 256;
                    this.soundRateMax[i] = 256;
                }
            }

            @Pc(20) int index = packet.g1();
            this.soundRateMin[index] = packet.g2();
            this.soundRateMax[index] = packet.g2();
        }
    }
}
