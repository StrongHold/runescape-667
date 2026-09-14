package com.jagex.sound;

import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.LruCache;
import com.jagex.game.runetek6.sound.Audio;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!wc")
public final class QueueBuss extends AudioBuss {

    @OriginalMember(owner = "client!ko", name = "H", descriptor = "Lclient!ts;")
    public static final LruCache recentUse = new LruCache(64);

    @OriginalMember(owner = "client!ka", name = "f", descriptor = "Z")
    public static boolean stereo;

    @OriginalMember(owner = "client!wc", name = "o", descriptor = "Z")
    public boolean paused;

    @OriginalMember(owner = "client!wc", name = "J", descriptor = "I")
    public int offset;

    @OriginalMember(owner = "client!wc", name = "L", descriptor = "Z")
    public boolean finished;

    @OriginalMember(owner = "client!wc", name = "M", descriptor = "Lclient!sia;")
    public final Deque blocks = new Deque();

    @OriginalMember(owner = "client!wc", name = "E", descriptor = "I")
    public int blockCount = 0;

    @OriginalMember(owner = "client!wc", name = "u", descriptor = "I")
    public int rightVolume = 256;

    @OriginalMember(owner = "client!wc", name = "K", descriptor = "I")
    public int leftVolume = 256;

    @OriginalMember(owner = "client!wc", name = "C", descriptor = "I")
    public final int channels;

    @OriginalMember(owner = "client!wc", name = "<init>", descriptor = "(I)V")
    public QueueBuss(@OriginalArg(0) int arg0) {
        this.channels = arg0;
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(Z)D")
    public synchronized double time(@OriginalArg(0) boolean arg0) {
        if (this.blockCount < 1) {
            return -1.0D;
        }
        @Pc(16) DoublyLinkedNode_Sub2_Sub8 local16 = (DoublyLinkedNode_Sub2_Sub8) this.blocks.first();
        if (local16 == null) {
            return -1.0D;
        } else {
            if (arg0) {
                this.skip(87);
            }
            return local16.aDouble10 - (double) ((float) local16.aShortArrayArray3[0].length / (float) Audio.sampleRate);
        }
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss method9135() {
        return null;
    }

    @OriginalMember(owner = "client!wc", name = "d", descriptor = "(I)I")
    public synchronized int size() {
        return this.blockCount;
    }

    @OriginalMember(owner = "client!wc", name = "b", descriptor = "(Z)V")
    public synchronized void finish() {
        this.finished = true;
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(I)V")
    @Override
    public synchronized void skip(@OriginalArg(0) int arg0) {
        if (this.paused) {
            return;
        }
        while (true) {
            @Pc(14) DoublyLinkedNode_Sub2_Sub8 local14 = this.peek();
            if (local14 == null) {
                if (this.finished) {
                    this.unlink();
                    recentUse.clear();
                }
                return;
            }
            if (local14.aShortArrayArray3[0].length - this.offset > arg0) {
                this.offset += arg0;
                return;
            }
            arg0 -= local14.aShortArrayArray3[0].length - this.offset;
            this.removeFirst();
        }
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(IDI)Lclient!dk;")
    public DoublyLinkedNode_Sub2_Sub8 allocBlock(@OriginalArg(0) int arg0, @OriginalArg(1) double arg1) {
        @Pc(11) long local11 = arg0 | this.channels << 0;
        @Pc(17) DoublyLinkedNode_Sub2_Sub8 local17 = (DoublyLinkedNode_Sub2_Sub8) recentUse.get(local11);
        if (local17 == null) {
            local17 = new DoublyLinkedNode_Sub2_Sub8(new short[this.channels][arg0], arg1);
        } else {
            local17.aDouble10 = arg1;
            recentUse.remove(local11);
        }
        return local17;
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(Lclient!dk;B)V")
    public synchronized void add(@OriginalArg(0) DoublyLinkedNode_Sub2_Sub8 arg0) {
        while (this.blockCount >= 100) {
            this.blocks.removeFirst();
            this.blockCount--;
        }
        this.blocks.addLast(arg0);
        if (-73 != -73) {
            this.time(true);
        }
        this.blockCount++;
    }

    @OriginalMember(owner = "client!wc", name = "b", descriptor = "(B)V")
    public synchronized void removeFirst() {
        @Pc(7) DoublyLinkedNode_Sub2_Sub8 local7 = this.peek();
        if (local7 != null) {
            local7.unlink();
            this.blockCount--;
            this.offset = 0;
            recentUse.put(local7, local7.method2133());
        }
    }

    @OriginalMember(owner = "client!wc", name = "b", descriptor = "()I")
    @Override
    public int method9132() {
        return 1;
    }

    @OriginalMember(owner = "client!wc", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss method9133() {
        return null;
    }

    @OriginalMember(owner = "client!wc", name = "c", descriptor = "(B)Lclient!dk;")
    public synchronized DoublyLinkedNode_Sub2_Sub8 peek() {
        return (DoublyLinkedNode_Sub2_Sub8) this.blocks.first();
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(IZ)V")
    public synchronized void setPaused(@OriginalArg(1) boolean arg0) {
        this.paused = arg0;
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(IB)V")
    public void setVolume(@OriginalArg(0) int arg0) {
        this.rightVolume = arg0;
        this.leftVolume = arg0;
    }

    @OriginalMember(owner = "client!wc", name = "b", descriptor = "([III)V")
    @Override
    public synchronized void fill(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (this.paused) {
            return;
        }
        if (this.peek() != null) {
            @Pc(32) int local32 = arg2 + arg1;
            if (stereo) {
                local32 <<= 0x1;
            }
            @Pc(42) byte local42 = 0;
            if (this.channels == 2) {
                local42 = 1;
            }
            while (arg1 < local32) {
                @Pc(56) DoublyLinkedNode_Sub2_Sub8 local56 = this.peek();
                if (local56 == null) {
                    return;
                }
                @Pc(62) short[][] local62 = local56.aShortArrayArray3;
                while (local32 > arg1 && this.offset < local62[0].length) {
                    if (stereo) {
                        arg0[arg1++] = local62[0][this.offset] * this.leftVolume;
                        arg0[arg1++] = this.rightVolume * local62[local42][this.offset];
                    } else {
                        @Pc(70) int local70 = arg1++;
                        arg0[local70] += this.rightVolume * local62[local42][this.offset] + local62[0][this.offset] * this.leftVolume;
                    }
                    this.offset++;
                }
                if (this.offset >= local62[0].length) {
                    this.removeFirst();
                }
            }
        } else if (this.finished) {
            this.unlink();
            recentUse.clear();
        }
    }
}
