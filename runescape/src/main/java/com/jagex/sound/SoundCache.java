package com.jagex.sound;

import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.js5.js5;
import com.jagex.sound.vorbis.VorbisSound;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!fca")
public final class SoundCache {

    @OriginalMember(owner = "client!fca", name = "k", descriptor = "Lclient!av;")
    public final IterableHashTable vorbisSounds = new IterableHashTable(256);

    @OriginalMember(owner = "client!fca", name = "c", descriptor = "Lclient!av;")
    public final IterableHashTable sounds = new IterableHashTable(256);

    @OriginalMember(owner = "client!fca", name = "d", descriptor = "Lclient!sb;")
    public final js5 vorbisJs5;

    @OriginalMember(owner = "client!fca", name = "i", descriptor = "Lclient!sb;")
    public final js5 synthSoundsJs5;

    @OriginalMember(owner = "client!fca", name = "<init>", descriptor = "(Lclient!sb;Lclient!sb;)V")
    public SoundCache(@OriginalArg(0) js5 arg0, @OriginalArg(1) js5 arg1) {
        this.vorbisJs5 = arg1;
        this.synthSoundsJs5 = arg0;
    }

    @OriginalMember(owner = "client!fca", name = "a", descriptor = "(III[I)Lclient!sq;")
    public VariableRateSoundPacket readSynthSound(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int[] arg2) {
        @Pc(15) int local15 = arg0 ^ (arg1 >>> 12 | (arg1 & 0x10000FFF) << 4);
        @Pc(27) int local27 = local15 | arg1 << 16;
        @Pc(30) long local30 = local27;
        @Pc(37) VariableRateSoundPacket local37 = (VariableRateSoundPacket) this.sounds.get(local30);
        if (local37 != null) {
            return local37;
        } else if (arg2 == null || arg2[0] > 0) {
            @Pc(62) SynthSound local62 = SynthSound.get(this.synthSoundsJs5, arg1, arg0);
            if (local62 == null) {
                return null;
            }
            local37 = local62.sample();
            this.sounds.put(local30, local37);
            if (arg2 != null) {
                arg2[0] -= local37.data.length;
            }
            return local37;
        } else {
            return null;
        }
    }

    @OriginalMember(owner = "client!fca", name = "a", descriptor = "([IIIB)Lclient!sq;")
    public VariableRateSoundPacket readVorbisSound(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        @Pc(15) int local15 = (arg1 >>> 12 | (arg1 & 0xB0000FFF) << 4) ^ arg2;
        @Pc(21) int local21 = local15 | arg1 << 16;
        @Pc(26) long local26 = (long) local21 ^ 0x100000000L;
        @Pc(33) VariableRateSoundPacket local33 = (VariableRateSoundPacket) this.sounds.get(local26);
        if (local33 != null) {
            return local33;
        } else if (arg0 == null || arg0[0] > 0) {
            @Pc(59) VorbisSound local59 = (VorbisSound) this.vorbisSounds.get(local26);
            if (local59 == null) {
                local59 = VorbisSound.create(this.vorbisJs5, arg1, arg2);
                if (local59 == null) {
                    return null;
                }
                this.vorbisSounds.put(local26, local59);
            }
            local33 = local59.method8502(arg0);
            if (local33 == null) {
                return null;
            } else {
                local59.unlink();
                this.sounds.put(local26, local33);
                return local33;
            }
        } else {
            return null;
        }
    }

    @OriginalMember(owner = "client!fca", name = "a", descriptor = "(Z[II)Lclient!sq;")
    public VariableRateSoundPacket getSynthSound(@OriginalArg(1) int[] arg0, @OriginalArg(2) int arg1, SoundCache class123) {
        if (class123.synthSoundsJs5.groupSize() == 1) {
            return class123.readSynthSound(arg1, 0, arg0);
        } else if (class123.synthSoundsJs5.fileLimit(arg1) == 1) {
            return class123.readSynthSound(0, arg1, arg0);
        } else {
            throw new RuntimeException();
        }
    }

    @OriginalMember(owner = "client!fca", name = "a", descriptor = "([IBI)Lclient!sq;")
    public VariableRateSoundPacket getVorbisSound(@OriginalArg(0) int[] arg0, @OriginalArg(2) int arg1) {
        if (this.vorbisJs5.groupSize() == 1) {
            return this.readVorbisSound(arg0, 0, arg1);
        } else if (this.vorbisJs5.fileLimit(arg1) == 1) {
            return this.readVorbisSound(arg0, arg1, 0);
        } else {
            throw new RuntimeException();
        }
    }
}
