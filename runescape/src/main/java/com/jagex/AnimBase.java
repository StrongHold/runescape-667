package com.jagex;

import com.jagex.core.datastruct.key.Node;
import com.jagex.core.io.Packet;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qda")
public final class AnimBase extends Node {

    @OriginalMember(owner = "client!qda", name = "w", descriptor = "I")
    public final int id;

    @OriginalMember(owner = "client!qda", name = "r", descriptor = "I")
    public final int anInt7690;

    @OriginalMember(owner = "client!qda", name = "k", descriptor = "[I")
    public final int[] anIntArray618;

    @OriginalMember(owner = "client!qda", name = "y", descriptor = "[Z")
    public final boolean[] aBooleanArray25;

    @OriginalMember(owner = "client!qda", name = "u", descriptor = "[[I")
    public final int[][] anIntArrayArray196;

    @OriginalMember(owner = "client!qda", name = "v", descriptor = "[I")
    public final int[] anIntArray619;

    @OriginalMember(owner = "client!qda", name = "<init>", descriptor = "(I[B)V")
    public AnimBase(@OriginalArg(0) int id, @OriginalArg(1) byte[] data) {
        this.id = id;
        @Pc(9) Packet packet = new Packet(data);
        this.anInt7690 = packet.g1();
        this.anIntArray618 = new int[this.anInt7690];
        this.aBooleanArray25 = new boolean[this.anInt7690];
        this.anIntArrayArray196 = new int[this.anInt7690][];
        this.anIntArray619 = new int[this.anInt7690];
        for (@Pc(36) int group = 0; group < this.anInt7690; group++) {
            this.anIntArray619[group] = packet.g1();
            if (this.anIntArray619[group] == 6) {
                this.anIntArray619[group] = 2;
            }
        }
        for (@Pc(68) int group = 0; group < this.anInt7690; group++) {
            this.aBooleanArray25[group] = packet.g1() == 1;
        }
        for (@Pc(89) int group = 0; group < this.anInt7690; group++) {
            this.anIntArray618[group] = packet.g2();
        }
        for (@Pc(108) int group = 0; group < this.anInt7690; group++) {
            this.anIntArrayArray196[group] = new int[packet.g1()];
        }
        for (@Pc(128) int group = 0; group < this.anInt7690; group++) {
            for (@Pc(131) int label = 0; label < this.anIntArrayArray196[group].length; label++) {
                this.anIntArrayArray196[group][label] = packet.g1();
            }
        }
    }
}
