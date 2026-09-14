package com.jagex.sound;

import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.game.runetek6.sound.Audio;
import com.jagex.js5.js5;
import com.jagex.sound.midi.MidiProgramNode;
import com.jagex.sound.midi.MidiSequence;
import com.jagex.sound.midi.MidiSong;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!bd")
public final class MixBuss extends AudioBuss {

    @OriginalMember(owner = "client!bd", name = "ab", descriptor = "J")
    public long aLong28;

    @OriginalMember(owner = "client!bd", name = "Ib", descriptor = "Z")
    public boolean aBoolean65;

    @OriginalMember(owner = "client!bd", name = "gb", descriptor = "I")
    public int anInt840;

    @OriginalMember(owner = "client!bd", name = "J", descriptor = "J")
    public long aLong29;

    @OriginalMember(owner = "client!bd", name = "hb", descriptor = "I")
    public int anInt841;

    @OriginalMember(owner = "client!bd", name = "cb", descriptor = "I")
    public int anInt842;

    @OriginalMember(owner = "client!bd", name = "jb", descriptor = "Z")
    public boolean aBoolean66;

    @OriginalMember(owner = "client!bd", name = "Cb", descriptor = "Lclient!bn;")
    public MidiSong aClass2_Sub8_1;

    @OriginalMember(owner = "client!bd", name = "D", descriptor = "[I")
    public final int[] anIntArray42 = new int[16];

    @OriginalMember(owner = "client!bd", name = "I", descriptor = "[I")
    public final int[] anIntArray45 = new int[16];

    @OriginalMember(owner = "client!bd", name = "w", descriptor = "[I")
    public final int[] anIntArray50 = new int[16];

    @OriginalMember(owner = "client!bd", name = "bb", descriptor = "[I")
    public final int[] anIntArray44 = new int[16];

    @OriginalMember(owner = "client!bd", name = "mb", descriptor = "[I")
    public final int[] anIntArray43 = new int[16];

    @OriginalMember(owner = "client!bd", name = "N", descriptor = "I")
    public final int anInt815 = 1000000;

    @OriginalMember(owner = "client!bd", name = "kb", descriptor = "[I")
    public final int[] anIntArray48 = new int[16];

    @OriginalMember(owner = "client!bd", name = "Gb", descriptor = "[I")
    public final int[] anIntArray47 = new int[16];

    @OriginalMember(owner = "client!bd", name = "o", descriptor = "[I")
    public final int[] anIntArray55 = new int[16];

    @OriginalMember(owner = "client!bd", name = "E", descriptor = "[I")
    public final int[] anIntArray53 = new int[16];

    @OriginalMember(owner = "client!bd", name = "Fb", descriptor = "[[Lclient!dha;")
    public final MusicPatchNode[][] keyVoices = new MusicPatchNode[16][128];

    @OriginalMember(owner = "client!bd", name = "Q", descriptor = "[I")
    public final int[] anIntArray46 = new int[16];

    @OriginalMember(owner = "client!bd", name = "xb", descriptor = "I")
    public int volume = 256;

    @OriginalMember(owner = "client!bd", name = "v", descriptor = "[I")
    public final int[] anIntArray49 = new int[16];

    @OriginalMember(owner = "client!bd", name = "db", descriptor = "[I")
    public final int[] anIntArray56 = new int[16];

    @OriginalMember(owner = "client!bd", name = "Z", descriptor = "[I")
    public final int[] anIntArray52 = new int[16];

    @OriginalMember(owner = "client!bd", name = "Mb", descriptor = "[[Lclient!dha;")
    public final MusicPatchNode[][] groupVoices = new MusicPatchNode[16][128];

    @OriginalMember(owner = "client!bd", name = "nb", descriptor = "[I")
    public final int[] anIntArray51 = new int[16];

    @OriginalMember(owner = "client!bd", name = "L", descriptor = "[I")
    public final int[] anIntArray57 = new int[16];

    @OriginalMember(owner = "client!bd", name = "y", descriptor = "[I")
    public final int[] anIntArray54 = new int[16];

    @OriginalMember(owner = "client!bd", name = "vb", descriptor = "Lclient!bha;")
    public final MidiSequence midiSequence = new MidiSequence();

    @OriginalMember(owner = "client!bd", name = "rb", descriptor = "Lclient!uka;")
    public final VoiceBuss voiceBuss = new VoiceBuss(this);

    @OriginalMember(owner = "client!bd", name = "q", descriptor = "Lclient!av;")
    public final IterableHashTable aIterableHashTable_7;

    @OriginalMember(owner = "client!bd", name = "<init>", descriptor = "()V")
    public MixBuss() {
        this.aIterableHashTable_7 = new IterableHashTable(128);
        this.method926(256, -1);
        this.method942(true);
    }

    @OriginalMember(owner = "client!bd", name = "<init>", descriptor = "(Lclient!bd;)V")
    public MixBuss(@OriginalArg(0) MixBuss arg0) {
        this.aIterableHashTable_7 = arg0.aIterableHashTable_7;
        this.method926(256, -1);
        this.method942(true);
    }

    @OriginalMember(owner = "client!vja", name = "a", descriptor = "(Lclient!sb;IZ)Lclient!cea;")
    public static MusicPatch method8917(@OriginalArg(0) js5 js5, @OriginalArg(1) int id) {
        @Pc(8) byte[] data = js5.getfile(id);
        return data == null ? null : new MusicPatch(data);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IJ)V")
    public void method910(@OriginalArg(1) long arg0) {
        while (this.aLong28 <= arg0) {
            @Pc(14) int local14 = this.anInt840;
            @Pc(17) int local17 = this.anInt841;
            @Pc(20) long local20 = this.aLong28;
            while (local17 == this.anInt841) {
                while (this.midiSequence.trackDeltas[local14] == local17) {
                    this.midiSequence.switchTrack(local14);
                    @Pc(33) int local33 = this.midiSequence.nextEvent(local14);
                    if (local33 == 1) {
                        this.midiSequence.endTrack();
                        this.midiSequence.updatePosition(local14);
                        if (this.midiSequence.isComplete()) {
                            if (!this.aBoolean65 || local17 == 0) {
                                this.method942(true);
                                this.midiSequence.finish();
                                return;
                            }
                            this.midiSequence.reset(local20);
                        }
                        break;
                    }
                    if ((local33 & 0x80) != 0 && (local33 & 0xF0) != 144) {
                        this.method924(local33);
                    }
                    this.midiSequence.step(local14);
                    this.midiSequence.updatePosition(local14);
                }
                this.aLong29 = local20;
                local14 = this.midiSequence.activeTrack();
                local17 = this.midiSequence.trackDeltas[local14];
                local20 = this.midiSequence.delayForDelta(local17);
            }
            this.anInt841 = local17;
            this.anInt840 = local14;
            this.aLong28 = local20;
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(BZ)V")
    public synchronized void method911(@OriginalArg(1) boolean arg0) {
        this.midiSequence.finish();
        this.aClass2_Sub8_1 = null;
        this.method942(arg0);
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "([III)V")
    @Override
    public synchronized void fill(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (this.midiSequence.isPlaying()) {
            @Pc(14) int local14 = this.midiSequence.timeDivision * this.anInt815 / Audio.sampleRate;
            do {
                @Pc(24) long local24 = this.aLong29 + (long) arg2 * (long) local14;
                if (this.aLong28 - local24 >= 0L) {
                    this.aLong29 = local24;
                    break;
                }
                @Pc(55) int local55 = (int) ((this.aLong28 + (long) local14 - this.aLong29 - 1L) / (long) local14);
                this.aLong29 += (long) local55 * (long) local14;
                this.voiceBuss.fill(arg0, arg1, local55);
                arg1 += local55;
                this.method932((byte) -89);
                arg2 -= local55;
            } while (this.midiSequence.isPlaying());
        }
        this.voiceBuss.fill(arg0, arg1, arg2);
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public synchronized AudioBuss method9133() {
        return this.voiceBuss;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Z)V")
    public synchronized void method912() {
        this.method911(true);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ZZLclient!bn;I)V")
    public synchronized void method913(@OriginalArg(0) boolean arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) MidiSong arg2) {
        this.method911(arg1);
        this.midiSequence.decode(arg2.midiData);
        this.aLong29 = 0L;
        this.aBoolean65 = arg0;
        @Pc(24) int local24 = this.midiSequence.trackCount();
        for (@Pc(26) int local26 = 0; local26 < local24; local26++) {
            this.midiSequence.switchTrack(local26);
            this.midiSequence.step(local26);
            this.midiSequence.updatePosition(local26);
        }
        if (18429 != 18429) {
            this.method917(68, 61);
        }
        this.anInt840 = this.midiSequence.activeTrack();
        this.anInt841 = this.midiSequence.trackDeltas[this.anInt840];
        this.aLong28 = this.midiSequence.delayForDelta(this.anInt841);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ILclient!dha;)I")
    public int sampleRate(@OriginalArg(1) MusicPatchNode arg0) {
        @Pc(14) int local14 = arg0.pitch + (arg0.portamentoFraction * arg0.portamentoOffset >> 12);
        local14 += (this.anIntArray48[arg0.channel] - 8192) * this.anIntArray52[arg0.channel] >> 12;
        @Pc(35) MusicPatchEnvelope local35 = arg0.envelope;
        @Pc(65) int local65;
        if (local35.vibratoRate > 0 && (local35.vibratoDepth > 0 || this.anIntArray51[arg0.channel] > 0)) {
            local65 = local35.vibratoDepth << 2;
            @Pc(70) int local70 = local35.vibratoDelay << 1;
            if (local70 > arg0.elapsed) {
                local65 = arg0.elapsed * local65 / local70;
            }
            local65 += this.anIntArray51[arg0.channel] >> 7;
            @Pc(104) double local104 = Math.sin((double) (arg0.vibratoPhase & 0x1FF) * 0.01227184630308513D);
            local14 += (int) (local104 * (double) local65);
        }
        local65 = (int) ((double) (arg0.sound.sampleRate * 256) * Math.pow(2.0D, (double) local14 * 3.255208333333333E-4D) / (double) Audio.sampleRate + 0.5D);
        return local65 >= 1 ? local65 : 1;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IIII)V")
    public void method915(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        @Pc(12) MusicPatchNode local12 = this.keyVoices[arg1][arg2];
        if (local12 == null) {
            return;
        }
        this.keyVoices[arg1][arg2] = null;
        if ((this.anIntArray56[arg1] & 0x2) == 0) {
            local12.releasePhase = 0;
            return;
        }
        for (@Pc(47) MusicPatchNode local47 = (MusicPatchNode) this.voiceBuss.voices.first(); local47 != null; local47 = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if (local12.channel == local47.channel && local47.releasePhase < 0 && local12 != local47) {
                local12.releasePhase = 0;
                break;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(II)V")
    public synchronized void setVolume(@OriginalArg(1) int volume) {
        this.volume = volume;
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "(II)V")
    public void method917(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        if ((this.anIntArray56[arg1] & 0x4) != 0) {
            for (@Pc(22) MusicPatchNode local22 = (MusicPatchNode) this.voiceBuss.voices.first(); local22 != null; local22 = (MusicPatchNode) this.voiceBuss.voices.next()) {
                if (arg1 == local22.channel) {
                    local22.retriggerPhase = 0;
                }
            }
        }
        if (arg0 != 0) {
            this.method937(61, -58, -70);
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(BI)V")
    public void method919(@OriginalArg(1) int arg0) {
        if (arg0 < 0) {
            for (@Pc(7) int local7 = 0; local7 < 16; local7++) {
                this.method919(local7);
            }
            return;
        }
        this.anIntArray53[arg0] = 12800;
        this.anIntArray55[arg0] = 8192;
        this.anIntArray45[arg0] = 16383;
        this.anIntArray48[arg0] = 8192;
        this.anIntArray51[arg0] = 0;
        this.anIntArray47[arg0] = 8192;
        this.method941(arg0);
        this.method917(0, arg0);
        this.anIntArray56[arg0] = 0;
        this.anIntArray44[arg0] = 32767;
        this.anIntArray52[arg0] = 256;
        this.anIntArray49[arg0] = 0;
        this.method928(8192, arg0);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!dha;B)I")
    public int method920(@OriginalArg(0) MusicPatchNode arg0) {
        if (this.anIntArray42[arg0.channel] == 0) {
            return 0;
        }
        @Pc(18) MusicPatchEnvelope local18 = arg0.envelope;
        @Pc(34) int local34 = this.anIntArray45[arg0.channel] * this.anIntArray53[arg0.channel] + 4096 >> 13;
        @Pc(42) int local42 = local34 * local34 + 16384 >> 15;
        @Pc(51) int local51 = arg0.volume * local42 + 16384 >> 15;
        @Pc(60) int local60 = this.volume * local51 + 128 >> 8;
        local34 = local60 * this.anIntArray42[arg0.channel] + 128 >> 8;
        if (local18.decay > 0) {
            local34 = (int) ((double) local34 * Math.pow(0.5D, (double) arg0.decayPhase * 1.953125E-5D * (double) local18.decay) + 0.5D);
        }
        @Pc(102) int local102;
        @Pc(110) int local110;
        @Pc(132) int local132;
        @Pc(144) int local144;
        if (local18.envelope != null) {
            local102 = arg0.envelopePhase;
            local110 = local18.envelope[arg0.envelopeIndex + 1];
            if (local18.envelope.length - 2 > arg0.envelopeIndex) {
                local132 = (local18.envelope[arg0.envelopeIndex] & 0xFF) << 8;
                local144 = (local18.envelope[arg0.envelopeIndex + 2] & 0xFF) << 8;
                local110 += (local18.envelope[arg0.envelopeIndex + 3] - local110) * (-local132 + local102) / (local144 - local132);
            }
            local34 = local110 * local34 + 32 >> 6;
        }
        if (arg0.releasePhase > 0 && local18.release != null) {
            local102 = arg0.releasePhase;
            local110 = local18.release[arg0.releaseIndex + 1];
            if (local18.release.length - 2 > arg0.releaseIndex) {
                local132 = (local18.release[arg0.releaseIndex] & 0xFF) << 8;
                local144 = (local18.release[arg0.releaseIndex + 2] & 0xFF) << 8;
                local110 += (local102 - local132) * (local18.release[arg0.releaseIndex + 3] - local110) / (local144 - local132);
            }
            local34 = local110 * local34 + 32 >> 6;
        }
        return local34;
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "()I")
    @Override
    public synchronized int method9132() {
        return 0;
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(Lclient!dha;B)Z")
    public boolean method921(@OriginalArg(0) MusicPatchNode arg0) {
        if (arg0.stream != null) {
            return false;
        }
        if (arg0.releasePhase >= 0) {
            arg0.unlink();
            if (arg0.exclusiveGroup > 0 && this.groupVoices[arg0.channel][arg0.exclusiveGroup] == arg0) {
                this.groupVoices[arg0.channel][arg0.exclusiveGroup] = null;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public synchronized AudioBuss method9135() {
        return null;
    }

    @OriginalMember(owner = "client!bd", name = "d", descriptor = "(B)Z")
    public synchronized boolean isPlaying() {
        return this.midiSequence.isPlaying();
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(ILclient!dha;)I")
    public int method923(@OriginalArg(1) MusicPatchNode arg0) {
        @Pc(9) int local9 = this.anIntArray55[arg0.channel];
        return local9 < 8192 ? local9 * arg0.pan + 32 >> 6 : 16384 - ((128 - arg0.pan) * (-local9 + 16384) + 32 >> 6);
    }

    @OriginalMember(owner = "client!bd", name = "d", descriptor = "(II)V")
    public void method924(@OriginalArg(0) int arg0) {
        @Pc(9) int local9 = arg0 & 0xF0;
        @Pc(16) int local16;
        @Pc(22) int local22;
        @Pc(28) int local28;
        if (local9 == 128) {
            local16 = arg0 & 0xF;
            local22 = arg0 >> 8 & 0x7F;
            local28 = arg0 >> 16 & 0x7F;
            this.method915(local28, local16, local22);
        } else if (local9 == 144) {
            local16 = arg0 & 0xF;
            local22 = arg0 >> 8 & 0x7F;
            local28 = arg0 >> 16 & 0x7F;
            if (local28 <= 0) {
                this.method915(64, local16, local22);
            } else {
                this.method936(local16, local22, local28);
            }
        } else if (local9 == 160) {
            local16 = arg0 & 0xF;
            local22 = arg0 >> 8 & 0x7F;
            local28 = arg0 >> 16 & 0x7F;
            this.method939(local28, local16, local22);
        } else if (local9 == 176) {
            local16 = arg0 & 0xF;
            local22 = arg0 >> 8 & 0x7F;
            local28 = arg0 >> 16 & 0x7F;
            if (local22 == 0) {
                this.anIntArray50[local16] = (local28 << 14) + (this.anIntArray50[local16] & 0xFFE03FFF);
            }
            if (local22 == 32) {
                this.anIntArray50[local16] = (local28 << 7) + (this.anIntArray50[local16] & 0xFFFFC07F);
            }
            if (local22 == 1) {
                this.anIntArray51[local16] = (this.anIntArray51[local16] & 0xFFFFC07F) + (local28 << 7);
            }
            if (local22 == 33) {
                this.anIntArray51[local16] = local28 + (this.anIntArray51[local16] & 0xFFFFFF80);
            }
            if (local22 == 5) {
                this.anIntArray47[local16] = (local28 << 7) + (this.anIntArray47[local16] & 0xFFFFC07F);
            }
            if (local22 == 37) {
                this.anIntArray47[local16] = (this.anIntArray47[local16] & 0xFFFFFF80) + local28;
            }
            if (local22 == 7) {
                this.anIntArray53[local16] = (local28 << 7) + (this.anIntArray53[local16] & 0xFFFFC07F);
            }
            if (local22 == 39) {
                this.anIntArray53[local16] = local28 + (this.anIntArray53[local16] & 0xFFFFFF80);
            }
            if (local22 == 10) {
                this.anIntArray55[local16] = (local28 << 7) + (this.anIntArray55[local16] & 0xFFFFC07F);
            }
            if (local22 == 42) {
                this.anIntArray55[local16] = local28 + (this.anIntArray55[local16] & 0xFFFFFF80);
            }
            if (local22 == 11) {
                this.anIntArray45[local16] = (this.anIntArray45[local16] & 0xFFFFC07F) + (local28 << 7);
            }
            if (local22 == 43) {
                this.anIntArray45[local16] = (this.anIntArray45[local16] & 0xFFFFFF80) + local28;
            }
            if (local22 == 64) {
                if (local28 < 64) {
                    this.anIntArray56[local16] &= 0xFFFFFFFE;
                } else {
                    this.anIntArray56[local16] |= 0x1;
                }
            }
            if (local22 == 65) {
                if (local28 >= 64) {
                    this.anIntArray56[local16] |= 0x2;
                } else {
                    this.method941(local16);
                    this.anIntArray56[local16] &= 0xFFFFFFFD;
                }
            }
            if (local22 == 99) {
                this.anIntArray44[local16] = (local28 << 7) + (this.anIntArray44[local16] & 0x7F);
            }
            if (local22 == 98) {
                this.anIntArray44[local16] = local28 + (this.anIntArray44[local16] & 0x3F80);
            }
            if (local22 == 101) {
                this.anIntArray44[local16] = (this.anIntArray44[local16] & 0x7F) + (local28 << 7) + 16384;
            }
            if (local22 == 100) {
                this.anIntArray44[local16] = (this.anIntArray44[local16] & 0x3F80) + local28 + 16384;
            }
            if (local22 == 120) {
                this.method930(local16);
            }
            if (local22 == 121) {
                this.method919(local16);
            }
            if (local22 == 123) {
                this.method938(local16);
            }
            @Pc(557) int local557;
            if (local22 == 6) {
                local557 = this.anIntArray44[local16];
                if (local557 == 16384) {
                    this.anIntArray52[local16] = (local28 << 7) + (this.anIntArray52[local16] & 0xFFFFC07F);
                }
            }
            if (local22 == 38) {
                local557 = this.anIntArray44[local16];
                if (local557 == 16384) {
                    this.anIntArray52[local16] = (this.anIntArray52[local16] & 0xFFFFFF80) + local28;
                }
            }
            if (local22 == 16) {
                this.anIntArray49[local16] = (local28 << 7) + (this.anIntArray49[local16] & 0xFFFFC07F);
            }
            if (local22 == 48) {
                this.anIntArray49[local16] = local28 + (this.anIntArray49[local16] & 0xFFFFFF80);
            }
            if (local22 == 81) {
                if (local28 >= 64) {
                    this.anIntArray56[local16] |= 0x4;
                } else {
                    this.method917(0, local16);
                    this.anIntArray56[local16] &= 0xFFFFFFFB;
                }
            }
            if (local22 == 17) {
                this.method928((this.anIntArray43[local16] & 0xFFFFC07F) + (local28 << 7), local16);
            }
            if (local22 == 49) {
                this.method928(local28 + (this.anIntArray43[local16] & 0xFFFFFF80), local16);
            }
        } else if (local9 == 192) {
            local16 = arg0 & 0xF;
            local22 = arg0 >> 8 & 0x7F;
            this.method947(local16, local22 + this.anIntArray50[local16]);
        } else if (local9 == 208) {
            local16 = arg0 & 0xF;
            local22 = arg0 >> 8 & 0x7F;
            this.method946(local16, local22);
        } else if (local9 == 224) {
            local16 = arg0 & 0xF;
            local22 = (arg0 >> 8 & 0x7F) + ((arg0 & 0x7F0197) >> 9);
            this.method937(local22, -5807, local16);
        } else {
            local9 = arg0 & 0xFF;
            if (local9 == 255) {
                this.method942(true);
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!bn;ZIJZ)V")
    public synchronized void method925(@OriginalArg(0) MidiSong arg0, @OriginalArg(1) boolean arg1, @OriginalArg(3) long arg2) {
        this.method913(arg1, true, arg0);
        this.method910(arg2 * (long) this.midiSequence.timeDivision);
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(IZI)V")
    public synchronized void method926(@OriginalArg(0) int level, @OriginalArg(2) int channel) {
        if (channel < 0) {
            for (@Pc(12) int c = 0; c < 16; c++) {
                this.anIntArray42[c] = level;
            }
        } else {
            this.anIntArray42[channel] = level;
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(B)V")
    public synchronized void method927() {
        for (@Pc(5) MusicPatch local5 = (MusicPatch) this.aIterableHashTable_7.first(); local5 != null; local5 = (MusicPatch) this.aIterableHashTable_7.next()) {
            local5.unlink();
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(IIB)V")
    public void method928(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        this.anIntArray43[arg1] = arg0;
        this.anIntArray57[arg1] = (int) (Math.pow(2.0D, (double) arg0 * 5.4931640625E-4D) * 2097152.0D + 0.5D);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IIB)V")
    public synchronized void method929() {
        this.method940();
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(II)V")
    public void method930(@OriginalArg(1) int arg0) {
        for (@Pc(14) MusicPatchNode local14 = (MusicPatchNode) this.voiceBuss.voices.first(); local14 != null; local14 = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if (arg0 < 0 || local14.channel == arg0) {
                if (local14.stream != null) {
                    local14.stream.method3320(Audio.sampleRate / 100);
                    if (local14.stream.method3336()) {
                        this.voiceBuss.fadeOutBuss.addFirst(local14.stream);
                    }
                    local14.clear();
                }
                if (local14.releasePhase < 0) {
                    this.keyVoices[local14.channel][local14.key] = null;
                }
                local14.unlink();
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "(B)V")
    public void method932(@OriginalArg(0) byte arg0) {
        @Pc(8) int local8 = this.anInt840;
        @Pc(11) int local11 = this.anInt841;
        @Pc(14) long local14 = this.aLong28;
        if (this.aClass2_Sub8_1 != null && this.anInt842 == local11) {
            this.method913(this.aBoolean65, this.aBoolean66, this.aClass2_Sub8_1);
            this.method932((byte) -98);
            return;
        }
        while (this.anInt841 == local11) {
            while (this.midiSequence.trackDeltas[local8] == local11) {
                this.midiSequence.switchTrack(local8);
                @Pc(50) int local50 = this.midiSequence.nextEvent(local8);
                if (local50 == 1) {
                    this.midiSequence.endTrack();
                    this.midiSequence.updatePosition(local8);
                    if (this.midiSequence.isComplete()) {
                        if (this.aClass2_Sub8_1 != null) {
                            this.method934(this.aClass2_Sub8_1, this.aBoolean65);
                            this.method932((byte) -119);
                            return;
                        }
                        if (!this.aBoolean65 || local11 == 0) {
                            this.method942(true);
                            this.midiSequence.finish();
                            return;
                        }
                        this.midiSequence.reset(local14);
                    }
                    break;
                }
                if ((local50 & 0x80) != 0) {
                    this.method924(local50);
                }
                this.midiSequence.step(local8);
                this.midiSequence.updatePosition(local8);
            }
            local8 = this.midiSequence.activeTrack();
            local11 = this.midiSequence.trackDeltas[local8];
            local14 = this.midiSequence.delayForDelta(local11);
        }
        if (arg0 >= -19) {
            this.anInt841 = -58;
        }
        this.anInt840 = local8;
        this.aLong28 = local14;
        this.anInt841 = local11;
        if (this.aClass2_Sub8_1 != null && local11 > this.anInt842) {
            this.anInt841 = this.anInt842;
            this.anInt840 = -1;
            this.aLong28 = this.midiSequence.delayForDelta(this.anInt841);
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(I)V")
    public synchronized void method933() {
        for (@Pc(7) MusicPatch local7 = (MusicPatch) this.aIterableHashTable_7.first(); local7 != null; local7 = (MusicPatch) this.aIterableHashTable_7.next()) {
            local7.clearSoundIds();
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ILclient!bn;Z)V")
    public synchronized void method934(@OriginalArg(1) MidiSong arg0, @OriginalArg(2) boolean arg1) {
        this.method913(arg1, true, arg0);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IIIZ)V")
    public void method936(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        this.method915(64, arg0, arg1);
        if ((this.anIntArray56[arg0] & 0x2) != 0) {
            for (@Pc(25) MusicPatchNode local25 = (MusicPatchNode) this.voiceBuss.voices.last(); local25 != null; local25 = (MusicPatchNode) this.voiceBuss.voices.previous()) {
                if (arg0 == local25.channel && local25.releasePhase < 0) {
                    this.keyVoices[arg0][local25.key] = null;
                    this.keyVoices[arg0][arg1] = local25;
                    @Pc(72) int local72 = local25.pitch + (local25.portamentoFraction * local25.portamentoOffset >> 12);
                    local25.pitch += arg1 - local25.key << 8;
                    local25.portamentoOffset = local72 - local25.pitch;
                    local25.portamentoFraction = 4096;
                    local25.key = arg1;
                    return;
                }
            }
        }
        @Pc(117) MusicPatch local117 = (MusicPatch) this.aIterableHashTable_7.get(this.anIntArray54[arg0]);
        if (local117 == null) {
            return;
        }
        @Pc(126) VariableRateSoundPacket local126 = local117.sounds[arg1];
        if (local126 == null) {
            return;
        }
        @Pc(142) MusicPatchNode local142 = new MusicPatchNode();
        local142.patch = local117;
        local142.sound = local126;
        local142.channel = arg0;
        local142.envelope = local117.envelopes[arg1];
        local142.exclusiveGroup = local117.exclusiveGroups[arg1];
        local142.key = arg1;
        local142.volume = local117.volumes[arg1] * arg2 * arg2 * local117.volume + 1024 >> 11;
        local142.pan = local117.pans[arg1] & 0xFF;
        local142.pitch = (arg1 << 8) - (local117.pitchOffsets[arg1] & 0x7FFF);
        local142.decayPhase = 0;
        local142.releaseIndex = 0;
        local142.releasePhase = -1;
        local142.envelopePhase = 0;
        local142.envelopeIndex = 0;
        if (this.anIntArray49[arg0] == 0) {
            local142.stream = SoundStream.create(local126, this.sampleRate(local142), this.method920(local142), this.method923(local142));
        } else {
            local142.stream = SoundStream.create(local126, this.sampleRate(local142), 0, this.method923(local142));
            this.method943(local117.pitchOffsets[arg1] < 0, local142);
        }
        if (local117.pitchOffsets[arg1] < 0) {
            local142.stream.setLoops(-1);
        }
        if (local142.exclusiveGroup >= 0) {
            @Pc(297) MusicPatchNode local297 = this.groupVoices[arg0][local142.exclusiveGroup];
            if (local297 != null && local297.releasePhase < 0) {
                this.keyVoices[arg0][local297.key] = null;
                local297.releasePhase = 0;
            }
            this.groupVoices[arg0][local142.exclusiveGroup] = local142;
        }
        this.voiceBuss.voices.addLast(local142);
        this.keyVoices[arg0][arg1] = local142;
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(III)V")
    public void method937(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        if (arg1 != -5807) {
            this.anInt840 = -101;
        }
        this.anIntArray48[arg2] = arg0;
    }

    @OriginalMember(owner = "client!bd", name = "e", descriptor = "(II)V")
    public void method938(@OriginalArg(1) int arg0) {
        for (@Pc(6) MusicPatchNode local6 = (MusicPatchNode) this.voiceBuss.voices.first(); local6 != null; local6 = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if ((arg0 < 0 || arg0 == local6.channel) && local6.releasePhase < 0) {
                this.keyVoices[local6.channel][local6.key] = null;
                local6.releasePhase = 0;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(IIII)V")
    public void method939(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IZI)V")
    public void method940() {
        this.anIntArray46[9] = 128;
        this.anIntArray50[9] = 128;
        this.method947(9, 128);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(BI)V")
    public void method941(@OriginalArg(1) int arg0) {
        if ((this.anIntArray56[arg0] & 0x2) == 0) {
            return;
        }
        for (@Pc(28) MusicPatchNode local28 = (MusicPatchNode) this.voiceBuss.voices.first(); local28 != null; local28 = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if (arg0 == local28.channel && this.keyVoices[arg0][local28.key] == null && local28.releasePhase < 0) {
                local28.releasePhase = 0;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IZ)V")
    public void method942(@OriginalArg(1) boolean arg0) {
        if (arg0) {
            this.method930(-1);
        } else {
            this.method938(-1);
        }
        this.method919(-1);
        for (@Pc(21) int local21 = 0; local21 < 16; local21++) {
            this.anIntArray54[local21] = this.anIntArray46[local21];
        }
        for (@Pc(46) int local46 = 0; local46 < 16; local46++) {
            this.anIntArray50[local46] = this.anIntArray46[local46] & 0xFFFFFF80;
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ZLclient!dha;I)V")
    public void method943(@OriginalArg(0) boolean arg0, @OriginalArg(1) MusicPatchNode arg1) {
        @Pc(13) int local13 = arg1.sound.data.length;
        @Pc(42) int local42;
        if (arg0 && arg1.sound.aBoolean668) {
            @Pc(29) int local29 = local13 + local13 - arg1.sound.nominalBitRate;
            local42 = (int) ((long) local29 * (long) this.anIntArray49[arg1.channel] >> 6);
            local13 <<= 0x8;
            if (local13 <= local42) {
                local42 = local13 + local13 - local42 - 1;
                arg1.stream.method3323();
            }
        } else {
            local42 = (int) ((long) this.anIntArray49[arg1.channel] * (long) local13 >> 6);
        }
        arg1.stream.method3344(local42);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!fca;Lclient!sb;IILclient!bn;)Z")
    public synchronized boolean method944(@OriginalArg(0) SoundCache arg0, @OriginalArg(1) js5 arg1, @OriginalArg(4) MidiSong song) {
        song.computePrograms();

        @Pc(15) boolean ready = true;
        @Pc(29) int[] maxSamples = new int[]{22050};
        for (@Pc(35) MidiProgramNode node = (MidiProgramNode) song.programs.first(); node != null; node = (MidiProgramNode) song.programs.next()) {
            @Pc(40) int program = (int) node.key;
            @Pc(48) MusicPatch local48 = (MusicPatch) this.aIterableHashTable_7.get(program);
            if (local48 == null) {
                local48 = method8917(arg1, program);
                if (local48 == null) {
                    ready = false;
                    continue;
                }
                this.aIterableHashTable_7.put(program, local48);
            }

            if (!local48.loadSounds(arg0, maxSamples, node.notes)) {
                ready = false;
            }
        }

        if (ready) {
            song.resetPrograms();
        }

        return ready;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(I)V")
    @Override
    public synchronized void skip(@OriginalArg(0) int arg0) {
        if (this.midiSequence.isPlaying()) {
            @Pc(18) int local18 = this.midiSequence.timeDivision * this.anInt815 / Audio.sampleRate;
            do {
                @Pc(27) long local27 = (long) arg0 * (long) local18 + this.aLong29;
                if (this.aLong28 - local27 >= 0L) {
                    this.aLong29 = local27;
                    break;
                }
                @Pc(58) int local58 = (int) ((this.aLong28 + (long) local18 - this.aLong29 - 1L) / (long) local18);
                this.aLong29 += (long) local58 * (long) local18;
                this.voiceBuss.skip(local58);
                arg0 -= local58;
                this.method932((byte) -117);
            } while (this.midiSequence.isPlaying());
        }
        this.voiceBuss.skip(arg0);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!dha;I[III)Z")
    public boolean method945(@OriginalArg(0) MusicPatchNode arg0, @OriginalArg(2) int[] arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        arg0.samplesUntilUpdate = Audio.sampleRate / 100;
        if (arg0.releasePhase >= 0 && (arg0.stream == null || arg0.stream.method3311())) {
            arg0.clear();
            arg0.unlink();
            if (arg0.exclusiveGroup > 0 && this.groupVoices[arg0.channel][arg0.exclusiveGroup] == arg0) {
                this.groupVoices[arg0.channel][arg0.exclusiveGroup] = null;
            }
            return true;
        }
        @Pc(70) int local70 = arg0.portamentoFraction;
        if (local70 > 0) {
            local70 -= (int) (Math.pow(2.0D, (double) this.anIntArray47[arg0.channel] * 4.921259842519685E-4D) * 16.0D + 0.5D);
            if (local70 < 0) {
                local70 = 0;
            }
            arg0.portamentoFraction = local70;
        }
        arg0.stream.setRate(this.sampleRate(arg0));
        @Pc(113) MusicPatchEnvelope local113 = arg0.envelope;
        arg0.vibratoPhase += local113.vibratoRate;
        @Pc(122) boolean local122 = false;
        arg0.elapsed++;
        @Pc(147) double local147 = (double) ((arg0.key - 60 << 8) + (arg0.portamentoOffset * arg0.portamentoFraction >> 12)) * 5.086263020833333E-6D;
        if (local113.decay > 0) {
            if (local113.decayKeyScale > 0) {
                arg0.decayPhase += (int) (Math.pow(2.0D, (double) local113.decayKeyScale * local147) * 128.0D + 0.5D);
            } else {
                arg0.decayPhase += 128;
            }
            if (arg0.decayPhase * local113.decay >= 819200) {
                local122 = true;
            }
        }
        if (local113.envelope != null) {
            if (local113.envelopeKeyScale <= 0) {
                arg0.envelopePhase += 128;
            } else {
                arg0.envelopePhase += (int) (Math.pow(2.0D, local147 * (double) local113.envelopeKeyScale) * 128.0D + 0.5D);
            }
            while (arg0.envelopeIndex < local113.envelope.length - 2 && arg0.envelopePhase > (local113.envelope[arg0.envelopeIndex + 2] & 0xFF) << 8) {
                arg0.envelopeIndex += 2;
            }
            if (arg0.envelopeIndex == local113.envelope.length - 2 && local113.envelope[arg0.envelopeIndex + 1] == 0) {
                local122 = true;
            }
        }
        if (arg0.releasePhase >= 0 && local113.release != null && (this.anIntArray56[arg0.channel] & 0x1) == 0 && (arg0.exclusiveGroup < 0 || arg0 != this.groupVoices[arg0.channel][arg0.exclusiveGroup])) {
            if (local113.releaseKeyScale <= 0) {
                arg0.releasePhase += 128;
            } else {
                arg0.releasePhase += (int) (Math.pow(2.0D, local147 * (double) local113.releaseKeyScale) * 128.0D + 0.5D);
            }
            while (local113.release.length - 2 > arg0.releaseIndex && arg0.releasePhase > (local113.release[arg0.releaseIndex + 2] & 0xFF) << 8) {
                arg0.releaseIndex += 2;
            }
            if (local113.release.length - 2 == arg0.releaseIndex) {
                local122 = true;
            }
        }
        if (!local122) {
            arg0.stream.method3338(arg0.samplesUntilUpdate, this.method920(arg0), this.method923(arg0));
            return false;
        }
        arg0.stream.method3320(arg0.samplesUntilUpdate);
        if (arg1 == null) {
            arg0.stream.skip(arg3);
        } else {
            arg0.stream.fill(arg1, arg2, arg3);
        }
        if (arg0.stream.method3336()) {
            this.voiceBuss.fadeOutBuss.addFirst(arg0.stream);
        }
        arg0.clear();
        if (arg0.releasePhase >= 0) {
            arg0.unlink();
            if (arg0.exclusiveGroup > 0 && this.groupVoices[arg0.channel][arg0.exclusiveGroup] == arg0) {
                this.groupVoices[arg0.channel][arg0.exclusiveGroup] = null;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(III)V")
    public void method946(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "(IIB)V")
    public void method947(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        if (this.anIntArray54[arg0] != arg1) {
            this.anIntArray54[arg0] = arg1;
            for (@Pc(16) int local16 = 0; local16 < 128; local16++) {
                this.groupVoices[arg0][local16] = null;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "g", descriptor = "(I)I")
    public int getVolume() {
        return this.volume;
    }
}
