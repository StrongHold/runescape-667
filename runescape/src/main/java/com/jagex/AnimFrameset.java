package com.jagex;

import com.jagex.core.datastruct.key.Node2;
import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.io.Packet;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!rw")
public final class AnimFrameset extends Node2 {

    @OriginalMember(owner = "client!qa", name = "q", descriptor = "Lclient!sb;")
    private static js5 anims;

    @OriginalMember(owner = "client!iha", name = "c", descriptor = "Lclient!sb;")
    private static js5 bases;

    @OriginalMember(owner = "client!uea", name = "a", descriptor = "(Lclient!sb;IZLclient!sb;)V")
    public static void init(@OriginalArg(0) js5 anims, @OriginalArg(3) js5 bases) {
        AnimFrameset.anims = bases;
        AnimFrameset.bases = anims;
    }

    @OriginalMember(owner = "client!rw", name = "t", descriptor = "[Lclient!nb;")
    public AnimFrame[] frames;

    @OriginalMember(owner = "client!rw", name = "E", descriptor = "[[B")
    public byte[][] frameData;

    @OriginalMember(owner = "client!rw", name = "x", descriptor = "I")
    public final int id;

    @OriginalMember(owner = "client!rw", name = "<init>", descriptor = "(I)V")
    public AnimFrameset(@OriginalArg(0) int id) {
        this.id = id;
    }

    @OriginalMember(owner = "client!rw", name = "a", descriptor = "(I)Z")
    public boolean isReady() {
        if (this.frames != null) {
            return true;
        }

        if (this.frameData == null) {
            @Pc(14) js5 animsLock = anims;
            synchronized (anims) {
                if (!anims.requestgroupdownload(this.id)) {
                    return false;
                }

                @Pc(36) int[] fileIds = anims.fileIds(this.id);
                this.frameData = new byte[fileIds.length][];
                for (@Pc(43) int i = 0; i < fileIds.length; i++) {
                    this.frameData[i] = anims.getfile(fileIds[i], this.id);
                }
            }
        }

        @Pc(69) boolean loaded = true;
        for (@Pc(71) int i = 0; i < this.frameData.length; i++) {
            @Pc(77) byte[] data = this.frameData[i];
            @Pc(82) Packet packet = new Packet(data);
            packet.pos = 1;
            @Pc(43) int id = packet.g2();
            @Pc(91) js5 basesLock = bases;
            synchronized (bases) {
                loaded &= bases.fileready(id);
            }
        }

        if (!loaded) {
            return false;
        }

        @Pc(123) Deque bases = new Deque();
        @Pc(125) js5 animsLock = anims;
        @Pc(36) int[] fileIds;
        synchronized (anims) {
            @Pc(133) int count = anims.fileLimit(this.id);
            this.frames = new AnimFrame[count];
            fileIds = anims.fileIds(this.id);
        }

        for (@Pc(43) int i = 0; i < fileIds.length; i++) {
            @Pc(167) byte[] data = this.frameData[i];
            @Pc(172) Packet packet = new Packet(data);
            packet.pos = 1;

            @Pc(179) int baseId = packet.g2();
            @Pc(181) AnimBase base = null;
            for (@Pc(188) AnimBase existing = (AnimBase) bases.first(); existing != null; existing = (AnimBase) bases.next()) {
                if (existing.id == baseId) {
                    base = existing;
                    break;
                }
            }

            if (base == null) {
                @Pc(209) js5 basesLock = AnimFrameset.bases;
                synchronized (AnimFrameset.bases) {
                    base = new AnimBase(baseId, AnimFrameset.bases.getfile(baseId));
                }
                bases.addLast(base);
            }

            this.frames[fileIds[i]] = new AnimFrame(data, base);
        }

        this.frameData = null;
        return true;
    }

    @OriginalMember(owner = "client!rw", name = "a", descriptor = "(BI)Z")
    public boolean hasAlphaTransform(@OriginalArg(1) int frame) {
        return this.frames[frame].hasAlphaTransform;
    }

    @OriginalMember(owner = "client!rw", name = "c", descriptor = "(II)Z")
    public boolean hasBillboardTransform(@OriginalArg(1) int frame) {
        return this.frames[frame].hasBillboardTransform;
    }

    @OriginalMember(owner = "client!rw", name = "b", descriptor = "(II)Z")
    public boolean hasColourTransform(@OriginalArg(0) int frame) {
        return this.frames[frame].hasColourTransform;
    }
}
