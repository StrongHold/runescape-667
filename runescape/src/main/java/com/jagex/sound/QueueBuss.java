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
    public QueueBuss(@OriginalArg(0) int channels) {
        this.channels = channels;
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(Z)D")
    public synchronized double time(@OriginalArg(0) boolean arg0) {
        if (this.blockCount < 1) {
            return -1.0D;
        }
        @Pc(16) DoublyLinkedNode_Sub2_Sub8 block = (DoublyLinkedNode_Sub2_Sub8) this.blocks.first();
        if (block == null) {
            return -1.0D;
        } else {
            if (arg0) {
                this.skip(87);
            }
            return block.aDouble10 - (double) ((float) block.aShortArrayArray3[0].length / (float) Audio.sampleRate);
        }
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss nextSubStream() {
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
    public synchronized void skip(@OriginalArg(0) int length) {
        if (this.paused) {
            return;
        }
        while (true) {
            @Pc(14) DoublyLinkedNode_Sub2_Sub8 block = this.peek();
            if (block == null) {
                if (this.finished) {
                    this.unlink();
                    recentUse.clear();
                }
                return;
            }
            if (block.aShortArrayArray3[0].length - this.offset > length) {
                this.offset += length;
                return;
            }
            length -= block.aShortArrayArray3[0].length - this.offset;
            this.removeFirst();
        }
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(IDI)Lclient!dk;")
    public DoublyLinkedNode_Sub2_Sub8 allocBlock(@OriginalArg(0) int length, @OriginalArg(1) double time) {
        @Pc(11) long key = length | this.channels << 0;
        @Pc(17) DoublyLinkedNode_Sub2_Sub8 block = (DoublyLinkedNode_Sub2_Sub8) recentUse.get(key);
        if (block == null) {
            block = new DoublyLinkedNode_Sub2_Sub8(new short[this.channels][length], time);
        } else {
            block.aDouble10 = time;
            recentUse.remove(key);
        }
        return block;
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(Lclient!dk;B)V")
    public synchronized void add(@OriginalArg(0) DoublyLinkedNode_Sub2_Sub8 block) {
        while (this.blockCount >= 100) {
            this.blocks.removeFirst();
            this.blockCount--;
        }
        this.blocks.addLast(block);
        if (-73 != -73) {
            this.time(true);
        }
        this.blockCount++;
    }

    @OriginalMember(owner = "client!wc", name = "b", descriptor = "(B)V")
    public synchronized void removeFirst() {
        @Pc(7) DoublyLinkedNode_Sub2_Sub8 block = this.peek();
        if (block != null) {
            block.unlink();
            this.blockCount--;
            this.offset = 0;
            recentUse.put(block, block.method2133());
        }
    }

    @OriginalMember(owner = "client!wc", name = "b", descriptor = "()I")
    @Override
    public int method9132() {
        return 1;
    }

    @OriginalMember(owner = "client!wc", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss firstSubStream() {
        return null;
    }

    @OriginalMember(owner = "client!wc", name = "c", descriptor = "(B)Lclient!dk;")
    public synchronized DoublyLinkedNode_Sub2_Sub8 peek() {
        return (DoublyLinkedNode_Sub2_Sub8) this.blocks.first();
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(IZ)V")
    public synchronized void setPaused(@OriginalArg(1) boolean paused) {
        this.paused = paused;
    }

    @OriginalMember(owner = "client!wc", name = "a", descriptor = "(IB)V")
    public void setVolume(@OriginalArg(0) int volume) {
        this.rightVolume = volume;
        this.leftVolume = volume;
    }

    @OriginalMember(owner = "client!wc", name = "b", descriptor = "([III)V")
    @Override
    public synchronized void fill(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int length) {
        if (this.paused) {
            return;
        }
        if (this.peek() != null) {
            @Pc(32) int end = length + offset;
            if (stereo) {
                end <<= 0x1;
            }
            @Pc(42) byte right = 0;
            if (this.channels == 2) {
                right = 1;
            }
            while (offset < end) {
                @Pc(56) DoublyLinkedNode_Sub2_Sub8 block = this.peek();
                if (block == null) {
                    return;
                }
                @Pc(62) short[][] samples = block.aShortArrayArray3;
                while (end > offset && this.offset < samples[0].length) {
                    if (stereo) {
                        mix[offset++] = samples[0][this.offset] * this.leftVolume;
                        mix[offset++] = this.rightVolume * samples[right][this.offset];
                    } else {
                        @Pc(70) int slot = offset++;
                        mix[slot] += this.rightVolume * samples[right][this.offset] + samples[0][this.offset] * this.leftVolume;
                    }
                    this.offset++;
                }
                if (this.offset >= samples[0].length) {
                    this.removeFirst();
                }
            }
        } else if (this.finished) {
            this.unlink();
            recentUse.clear();
        }
    }
}
