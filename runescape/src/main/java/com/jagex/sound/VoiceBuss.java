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
    public VoiceBuss(@OriginalArg(0) MixBuss mixBuss) {
        this.mixBuss = mixBuss;
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "(I)V")
    @Override
    public void skip(@OriginalArg(0) int length) {
        this.fadeOutBuss.skip(length);
        for (@Pc(15) MusicPatchNode voice = (MusicPatchNode) this.voices.first(); voice != null; voice = (MusicPatchNode) this.voices.next()) {
            if (!this.mixBuss.dropSilentVoice(voice)) {
                @Pc(27) int remaining = length;
                do {
                    if (remaining <= voice.samplesUntilUpdate) {
                        this.skipVoice(voice, remaining);
                        voice.samplesUntilUpdate -= remaining;
                        break;
                    }
                    this.skipVoice(voice, voice.samplesUntilUpdate);
                    remaining -= voice.samplesUntilUpdate;
                } while (!this.mixBuss.updateVoice(voice, null, 0, remaining));
            }
        }
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "(Lclient!dha;II)V")
    public void skipVoice(@OriginalArg(0) MusicPatchNode voice, @OriginalArg(2) int samples) {
        if ((this.mixBuss.switches[voice.channel] & 0x4) != 0 && voice.releasePhase < 0) {
            @Pc(28) int step = this.mixBuss.retriggerSteps[voice.channel] / Audio.sampleRate;
            @Pc(37) int count = (step + 1048575 - voice.retriggerPhase) / step;
            voice.retriggerPhase = voice.retriggerPhase + step * samples & 0xFFFFF;
            if (count <= samples) {
                if (this.mixBuss.startOffsets[voice.channel] == 0) {
                    voice.stream = SoundStream.create(voice.sound, voice.stream.getRate(), voice.stream.getVolume(), voice.stream.getRange());
                } else {
                    voice.stream = SoundStream.create(voice.sound, voice.stream.getRate(), 0, voice.stream.getRange());
                    this.mixBuss.applyStartOffset(voice.patch.pitchOffsets[voice.key] < 0, voice);
                }
                if (voice.patch.pitchOffsets[voice.key] < 0) {
                    voice.stream.setLoops(-1);
                }
                samples = voice.retriggerPhase / step;
            }
        }
        voice.stream.skip(samples);
    }

    @OriginalMember(owner = "client!uka", name = "b", descriptor = "()I")
    @Override
    public int method9132() {
        return 0;
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss nextSubStream() {
        @Pc(11) MusicPatchNode voice;
        do {
            voice = (MusicPatchNode) this.voices.next();
            if (voice == null) {
                return null;
            }
        } while (voice.stream == null);
        return voice.stream;
    }

    @OriginalMember(owner = "client!uka", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss firstSubStream() {
        @Pc(9) MusicPatchNode voice = (MusicPatchNode) this.voices.first();
        if (voice == null) {
            return null;
        } else if (voice.stream == null) {
            return this.nextSubStream();
        } else {
            return voice.stream;
        }
    }

    @OriginalMember(owner = "client!uka", name = "a", descriptor = "([ILclient!dha;IIII)V")
    public void fillVoice(@OriginalArg(0) int[] mix, @OriginalArg(1) MusicPatchNode voice, @OriginalArg(2) int end, @OriginalArg(3) int length, @OriginalArg(4) int offset) {
        if ((this.mixBuss.switches[voice.channel] & 0x4) != 0 && voice.releasePhase < 0) {
            @Pc(34) int step = this.mixBuss.retriggerSteps[voice.channel] / Audio.sampleRate;
            while (true) {
                @Pc(44) int count = (step + 1048575 - voice.retriggerPhase) / step;
                if (count > length) {
                    voice.retriggerPhase += length * step;
                    break;
                }
                voice.stream.fill(mix, offset, count);
                voice.retriggerPhase += step * count - 1048576;
                length -= count;
                offset += count;
                @Pc(75) int fade = Audio.sampleRate / 100;
                @Pc(79) int limit = 262144 / step;
                if (limit < fade) {
                    fade = limit;
                }
                @Pc(91) SoundStream stream = voice.stream;
                if (this.mixBuss.startOffsets[voice.channel] == 0) {
                    voice.stream = SoundStream.create(voice.sound, stream.getRate(), stream.getVolume(), stream.getRange());
                } else {
                    voice.stream = SoundStream.create(voice.sound, stream.getRate(), 0, stream.getRange());
                    this.mixBuss.applyStartOffset(voice.patch.pitchOffsets[voice.key] < 0, voice);
                    voice.stream.fadeToVolume(fade, stream.getVolume());
                }
                if (voice.patch.pitchOffsets[voice.key] < 0) {
                    voice.stream.setLoops(-1);
                }
                stream.fadeOut(fade);
                stream.fill(mix, offset, end - offset);
                if (stream.isFading()) {
                    this.fadeOutBuss.addFirst(stream);
                }
            }
        }
        voice.stream.fill(mix, offset, length);
    }

    @OriginalMember(owner = "client!uka", name = "b", descriptor = "([III)V")
    @Override
    public void fill(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int length) {
        this.fadeOutBuss.fill(mix, offset, length);
        for (@Pc(17) MusicPatchNode voice = (MusicPatchNode) this.voices.first(); voice != null; voice = (MusicPatchNode) this.voices.next()) {
            if (!this.mixBuss.dropSilentVoice(voice)) {
                @Pc(29) int cursor = offset;
                @Pc(31) int remaining = length;
                do {
                    if (voice.samplesUntilUpdate >= remaining) {
                        this.fillVoice(mix, voice, cursor + remaining, remaining, cursor);
                        voice.samplesUntilUpdate -= remaining;
                        break;
                    }
                    this.fillVoice(mix, voice, remaining + cursor, voice.samplesUntilUpdate, cursor);
                    cursor += voice.samplesUntilUpdate;
                    remaining -= voice.samplesUntilUpdate;
                } while (!this.mixBuss.updateVoice(voice, mix, cursor, remaining));
            }
        }
    }
}
