package com.jagex.sound;

import com.jagex.core.datastruct.key.Deque;
import com.jagex.game.runetek6.sound.Audio;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!uka")
public final class VoiceBuss extends AudioBuss {

    @OriginalMember(owner = "client!uka", name = "t", descriptor = "Lclient!sia;")
    public final Deque voices = new Deque();

    @OriginalMember(owner = "client!uka", name = "A", descriptor = "Lclient!nn;")
    public final AudioBussMixer fadeOutBuss = new AudioBussMixer();

    @OriginalMember(owner = "client!uka", name = "D", descriptor = "Lclient!bd;")
    public final MixBuss mixBuss;

    @OriginalMember(owner = "client!uka", name = "<init>", descriptor = "(Lclient!bd;)V")
    public VoiceBuss(@OriginalArg(0) MixBuss arg0) {
        this.mixBuss = arg0;
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "(I)V")
    @Override
    public void skip(@OriginalArg(0) int arg0) {
        this.fadeOutBuss.skip(arg0);
        for (@Pc(15) MusicPatchNode local15 = (MusicPatchNode) this.voices.first(); local15 != null; local15 = (MusicPatchNode) this.voices.next()) {
            if (!this.mixBuss.method921(local15)) {
                @Pc(27) int local27 = arg0;
                do {
                    if (local27 <= local15.samplesUntilUpdate) {
                        this.skipVoice(local15, local27);
                        local15.samplesUntilUpdate -= local27;
                        break;
                    }
                    this.skipVoice(local15, local15.samplesUntilUpdate);
                    local27 -= local15.samplesUntilUpdate;
                } while (!this.mixBuss.method945(local15, null, 0, local27));
            }
        }
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "(Lclient!dha;II)V")
    public void skipVoice(@OriginalArg(0) MusicPatchNode arg0, @OriginalArg(2) int arg1) {
        if ((this.mixBuss.anIntArray56[arg0.channel] & 0x4) != 0 && arg0.releasePhase < 0) {
            @Pc(28) int local28 = this.mixBuss.anIntArray57[arg0.channel] / Audio.sampleRate;
            @Pc(37) int local37 = (local28 + 1048575 - arg0.retriggerPhase) / local28;
            arg0.retriggerPhase = arg0.retriggerPhase + local28 * arg1 & 0xFFFFF;
            if (local37 <= arg1) {
                if (this.mixBuss.anIntArray49[arg0.channel] == 0) {
                    arg0.stream = SoundStream.create(arg0.sound, arg0.stream.method3340(), arg0.stream.getVolume(), arg0.stream.getRange());
                } else {
                    arg0.stream = SoundStream.create(arg0.sound, arg0.stream.method3340(), 0, arg0.stream.getRange());
                    this.mixBuss.method943(arg0.patch.pitchOffsets[arg0.key] < 0, arg0);
                }
                if (arg0.patch.pitchOffsets[arg0.key] < 0) {
                    arg0.stream.setLoops(-1);
                }
                arg1 = arg0.retriggerPhase / local28;
            }
        }
        arg0.stream.skip(arg1);
    }

    @OriginalMember(owner = "client!uka", name = "b", descriptor = "()I")
    @Override
    public int method9132() {
        return 0;
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss nextSubStream() {
        @Pc(11) MusicPatchNode local11;
        do {
            local11 = (MusicPatchNode) this.voices.next();
            if (local11 == null) {
                return null;
            }
        } while (local11.stream == null);
        return local11.stream;
    }

    @OriginalMember(owner = "client!uka", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss firstSubStream() {
        @Pc(9) MusicPatchNode local9 = (MusicPatchNode) this.voices.first();
        if (local9 == null) {
            return null;
        } else if (local9.stream == null) {
            return this.nextSubStream();
        } else {
            return local9.stream;
        }
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "([ILclient!dha;IIII)V")
    public void fillVoice(@OriginalArg(0) int[] arg0, @OriginalArg(1) MusicPatchNode arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4) {
        if ((this.mixBuss.anIntArray56[arg1.channel] & 0x4) != 0 && arg1.releasePhase < 0) {
            @Pc(34) int local34 = this.mixBuss.anIntArray57[arg1.channel] / Audio.sampleRate;
            while (true) {
                @Pc(44) int local44 = (local34 + 1048575 - arg1.retriggerPhase) / local34;
                if (local44 > arg3) {
                    arg1.retriggerPhase += arg3 * local34;
                    break;
                }
                arg1.stream.fill(arg0, arg4, local44);
                arg1.retriggerPhase += local34 * local44 - 1048576;
                arg3 -= local44;
                arg4 += local44;
                @Pc(75) int local75 = Audio.sampleRate / 100;
                @Pc(79) int local79 = 262144 / local34;
                if (local79 < local75) {
                    local75 = local79;
                }
                @Pc(91) SoundStream local91 = arg1.stream;
                if (this.mixBuss.anIntArray49[arg1.channel] == 0) {
                    arg1.stream = SoundStream.create(arg1.sound, local91.method3340(), local91.getVolume(), local91.getRange());
                } else {
                    arg1.stream = SoundStream.create(arg1.sound, local91.method3340(), 0, local91.getRange());
                    this.mixBuss.method943(arg1.patch.pitchOffsets[arg1.key] < 0, arg1);
                    arg1.stream.method3315(local75, local91.getVolume());
                }
                if (arg1.patch.pitchOffsets[arg1.key] < 0) {
                    arg1.stream.setLoops(-1);
                }
                local91.method3320(local75);
                local91.fill(arg0, arg4, arg2 - arg4);
                if (local91.method3336()) {
                    this.fadeOutBuss.addFirst(local91);
                }
            }
        }
        arg1.stream.fill(arg0, arg4, arg3);
    }

    @OriginalMember(owner = "client!uka", name = "b", descriptor = "([III)V")
    @Override
    public void fill(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        this.fadeOutBuss.fill(arg0, arg1, arg2);
        for (@Pc(17) MusicPatchNode local17 = (MusicPatchNode) this.voices.first(); local17 != null; local17 = (MusicPatchNode) this.voices.next()) {
            if (!this.mixBuss.method921(local17)) {
                @Pc(29) int local29 = arg1;
                @Pc(31) int local31 = arg2;
                do {
                    if (local17.samplesUntilUpdate >= local31) {
                        this.fillVoice(arg0, local17, local29 + local31, local31, local29);
                        local17.samplesUntilUpdate -= local31;
                        break;
                    }
                    this.fillVoice(arg0, local17, local31 + local29, local17.samplesUntilUpdate, local29);
                    local29 += local17.samplesUntilUpdate;
                    local31 -= local17.samplesUntilUpdate;
                } while (!this.mixBuss.method945(local17, arg0, local29, local31));
            }
        }
    }
}
