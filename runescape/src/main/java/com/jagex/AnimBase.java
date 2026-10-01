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
    public final int transformCount;

    @OriginalMember(owner = "client!qda", name = "k", descriptor = "[I")
    public final int[] originMasks;

    @OriginalMember(owner = "client!qda", name = "y", descriptor = "[Z")
    public final boolean[] shadowed;

    @OriginalMember(owner = "client!qda", name = "u", descriptor = "[[I")
    public final int[][] transformLabels;

    @OriginalMember(owner = "client!qda", name = "v", descriptor = "[I")
    public final int[] transformTypes;

    @OriginalMember(owner = "client!qda", name = "<init>", descriptor = "(I[B)V")
    public AnimBase(@OriginalArg(0) int id, @OriginalArg(1) byte[] data) {
        this.id = id;
        @Pc(9) Packet packet = new Packet(data);
        this.transformCount = packet.g1();
        this.originMasks = new int[this.transformCount];
        this.shadowed = new boolean[this.transformCount];
        this.transformLabels = new int[this.transformCount][];
        this.transformTypes = new int[this.transformCount];
        for (@Pc(36) int group = 0; group < this.transformCount; group++) {
            this.transformTypes[group] = packet.g1();
            if (this.transformTypes[group] == 6) {
                this.transformTypes[group] = 2;
            }
        }
        for (@Pc(68) int group = 0; group < this.transformCount; group++) {
            this.shadowed[group] = packet.g1() == 1;
        }
        for (@Pc(89) int group = 0; group < this.transformCount; group++) {
            this.originMasks[group] = packet.g2();
        }
        for (@Pc(108) int group = 0; group < this.transformCount; group++) {
            this.transformLabels[group] = new int[packet.g1()];
        }
        for (@Pc(128) int group = 0; group < this.transformCount; group++) {
            for (@Pc(131) int label = 0; label < this.transformLabels[group].length; label++) {
                this.transformLabels[group][label] = packet.g1();
            }
        }
    }
}
