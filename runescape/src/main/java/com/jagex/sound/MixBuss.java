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
    public long nextEventTime;

    @OriginalMember(owner = "client!bd", name = "Ib", descriptor = "Z")
    public boolean looping;

    @OriginalMember(owner = "client!bd", name = "gb", descriptor = "I")
    public int track;

    @OriginalMember(owner = "client!bd", name = "J", descriptor = "J")
    public long currentTime;

    @OriginalMember(owner = "client!bd", name = "hb", descriptor = "I")
    public int tick;

    @OriginalMember(owner = "client!bd", name = "cb", descriptor = "I")
    public int switchTick;

    @OriginalMember(owner = "client!bd", name = "jb", descriptor = "Z")
    public boolean killVoices;

    @OriginalMember(owner = "client!bd", name = "Cb", descriptor = "Lclient!bn;")
    public MidiSong nextSong;

    @OriginalMember(owner = "client!bd", name = "D", descriptor = "[I")
    public final int[] levels = new int[16];

    @OriginalMember(owner = "client!bd", name = "I", descriptor = "[I")
    public final int[] expressions = new int[16];

    @OriginalMember(owner = "client!bd", name = "w", descriptor = "[I")
    public final int[] banks = new int[16];

    @OriginalMember(owner = "client!bd", name = "bb", descriptor = "[I")
    public final int[] parameters = new int[16];

    @OriginalMember(owner = "client!bd", name = "mb", descriptor = "[I")
    public final int[] retriggerRates = new int[16];

    @OriginalMember(owner = "client!bd", name = "N", descriptor = "I")
    public final int microsecondsPerSecond = 1000000;

    @OriginalMember(owner = "client!bd", name = "kb", descriptor = "[I")
    public final int[] pitchBends = new int[16];

    @OriginalMember(owner = "client!bd", name = "Gb", descriptor = "[I")
    public final int[] portamentoTimes = new int[16];

    @OriginalMember(owner = "client!bd", name = "o", descriptor = "[I")
    public final int[] pans = new int[16];

    @OriginalMember(owner = "client!bd", name = "E", descriptor = "[I")
    public final int[] volumes = new int[16];

    @OriginalMember(owner = "client!bd", name = "Fb", descriptor = "[[Lclient!dha;")
    public final MusicPatchNode[][] keyVoices = new MusicPatchNode[16][128];

    @OriginalMember(owner = "client!bd", name = "Q", descriptor = "[I")
    public final int[] defaultPrograms = new int[16];

    @OriginalMember(owner = "client!bd", name = "xb", descriptor = "I")
    public int volume = 256;

    @OriginalMember(owner = "client!bd", name = "v", descriptor = "[I")
    public final int[] startOffsets = new int[16];

    @OriginalMember(owner = "client!bd", name = "db", descriptor = "[I")
    public final int[] switches = new int[16];

    @OriginalMember(owner = "client!bd", name = "Z", descriptor = "[I")
    public final int[] bendRanges = new int[16];

    @OriginalMember(owner = "client!bd", name = "Mb", descriptor = "[[Lclient!dha;")
    public final MusicPatchNode[][] groupVoices = new MusicPatchNode[16][128];

    @OriginalMember(owner = "client!bd", name = "nb", descriptor = "[I")
    public final int[] modulations = new int[16];

    @OriginalMember(owner = "client!bd", name = "L", descriptor = "[I")
    public final int[] retriggerSteps = new int[16];

    @OriginalMember(owner = "client!bd", name = "y", descriptor = "[I")
    public final int[] programs = new int[16];

    @OriginalMember(owner = "client!bd", name = "vb", descriptor = "Lclient!bha;")
    public final MidiSequence midiSequence = new MidiSequence();

    @OriginalMember(owner = "client!bd", name = "rb", descriptor = "Lclient!uka;")
    public final VoiceBuss voiceBuss = new VoiceBuss(this);

    @OriginalMember(owner = "client!bd", name = "q", descriptor = "Lclient!av;")
    public final IterableHashTable patches;

    @OriginalMember(owner = "client!bd", name = "<init>", descriptor = "()V")
    public MixBuss() {
        this.patches = new IterableHashTable(128);
        this.method926(256, -1);
        this.reset(true);
    }

    @OriginalMember(owner = "client!bd", name = "<init>", descriptor = "(Lclient!bd;)V")
    public MixBuss(@OriginalArg(0) MixBuss source) {
        this.patches = source.patches;
        this.method926(256, -1);
        this.reset(true);
    }

    @OriginalMember(owner = "client!vja", name = "a", descriptor = "(Lclient!sb;IZ)Lclient!cea;")
    public static MusicPatch readPatch(@OriginalArg(0) js5 js5, @OriginalArg(1) int id) {
        @Pc(8) byte[] data = js5.getfile(id);
        return data == null ? null : new MusicPatch(data);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IJ)V")
    public void fastForward(@OriginalArg(1) long limit) {
        while (this.nextEventTime <= limit) {
            @Pc(14) int track = this.track;
            @Pc(17) int tick = this.tick;
            @Pc(20) long time = this.nextEventTime;
            while (tick == this.tick) {
                while (this.midiSequence.trackDeltas[track] == tick) {
                    this.midiSequence.switchTrack(track);
                    @Pc(33) int event = this.midiSequence.nextEvent(track);
                    if (event == 1) {
                        this.midiSequence.endTrack();
                        this.midiSequence.updatePosition(track);
                        if (this.midiSequence.isComplete()) {
                            if (!this.looping || tick == 0) {
                                this.reset(true);
                                this.midiSequence.finish();
                                return;
                            }
                            this.midiSequence.reset(time);
                        }
                        break;
                    }
                    if ((event & 0x80) != 0 && (event & 0xF0) != 144) {
                        this.handleEvent(event);
                    }
                    this.midiSequence.step(track);
                    this.midiSequence.updatePosition(track);
                }
                this.currentTime = time;
                track = this.midiSequence.activeTrack();
                tick = this.midiSequence.trackDeltas[track];
                time = this.midiSequence.delayForDelta(tick);
            }
            this.tick = tick;
            this.track = track;
            this.nextEventTime = time;
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(BZ)V")
    public synchronized void stop(@OriginalArg(1) boolean killVoices) {
        this.midiSequence.finish();
        this.nextSong = null;
        this.reset(killVoices);
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "([III)V")
    @Override
    public synchronized void fill(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int length) {
        if (this.midiSequence.isPlaying()) {
            @Pc(14) int timePerSample = this.midiSequence.timeDivision * this.microsecondsPerSecond / Audio.sampleRate;
            do {
                @Pc(24) long time = this.currentTime + (long) length * (long) timePerSample;
                if (this.nextEventTime - time >= 0L) {
                    this.currentTime = time;
                    break;
                }
                @Pc(55) int count = (int) ((this.nextEventTime + (long) timePerSample - this.currentTime - 1L) / (long) timePerSample);
                this.currentTime += (long) count * (long) timePerSample;
                this.voiceBuss.fill(mix, offset, count);
                offset += count;
                this.processEvents((byte) -89);
                length -= count;
            } while (this.midiSequence.isPlaying());
        }
        this.voiceBuss.fill(mix, offset, length);
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public synchronized AudioBuss firstSubStream() {
        return this.voiceBuss;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Z)V")
    public synchronized void method912() {
        this.stop(true);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ZZLclient!bn;I)V")
    public synchronized void start(@OriginalArg(0) boolean loop, @OriginalArg(1) boolean killVoices, @OriginalArg(2) MidiSong song) {
        this.stop(killVoices);
        this.midiSequence.decode(song.midiData);
        this.currentTime = 0L;
        this.looping = loop;
        @Pc(24) int count = this.midiSequence.trackCount();
        for (@Pc(26) int i = 0; i < count; i++) {
            this.midiSequence.switchTrack(i);
            this.midiSequence.step(i);
            this.midiSequence.updatePosition(i);
        }
        if (18429 != 18429) {
            this.resetRetrigger(68, 61);
        }
        this.track = this.midiSequence.activeTrack();
        this.tick = this.midiSequence.trackDeltas[this.track];
        this.nextEventTime = this.midiSequence.delayForDelta(this.tick);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ILclient!dha;)I")
    public int sampleRate(@OriginalArg(1) MusicPatchNode voice) {
        @Pc(14) int pitch = voice.pitch + (voice.portamentoFraction * voice.portamentoOffset >> 12);
        pitch += (this.pitchBends[voice.channel] - 8192) * this.bendRanges[voice.channel] >> 12;
        @Pc(35) MusicPatchEnvelope envelope = voice.envelope;
        @Pc(65) int rate;
        if (envelope.vibratoRate > 0 && (envelope.vibratoDepth > 0 || this.modulations[voice.channel] > 0)) {
            rate = envelope.vibratoDepth << 2;
            @Pc(70) int delay = envelope.vibratoDelay << 1;
            if (delay > voice.elapsed) {
                rate = voice.elapsed * rate / delay;
            }
            rate += this.modulations[voice.channel] >> 7;
            @Pc(104) double vibrato = Math.sin((double) (voice.vibratoPhase & 0x1FF) * 0.01227184630308513D);
            pitch += (int) (vibrato * (double) rate);
        }
        rate = (int) ((double) (voice.sound.sampleRate * 256) * Math.pow(2.0D, (double) pitch * 3.255208333333333E-4D) / (double) Audio.sampleRate + 0.5D);
        return rate >= 1 ? rate : 1;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IIII)V")
    public void noteOff(@OriginalArg(0) int velocity, @OriginalArg(2) int channel, @OriginalArg(3) int key) {
        @Pc(12) MusicPatchNode voice = this.keyVoices[channel][key];
        if (voice == null) {
            return;
        }
        this.keyVoices[channel][key] = null;
        if ((this.switches[channel] & 0x2) == 0) {
            voice.releasePhase = 0;
            return;
        }
        for (@Pc(47) MusicPatchNode other = (MusicPatchNode) this.voiceBuss.voices.first(); other != null; other = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if (voice.channel == other.channel && other.releasePhase < 0 && voice != other) {
                voice.releasePhase = 0;
                break;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(II)V")
    public synchronized void setVolume(@OriginalArg(1) int volume) {
        this.volume = volume;
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "(II)V")
    public void resetRetrigger(@OriginalArg(0) int arg0, @OriginalArg(1) int channel) {
        if ((this.switches[channel] & 0x4) != 0) {
            for (@Pc(22) MusicPatchNode voice = (MusicPatchNode) this.voiceBuss.voices.first(); voice != null; voice = (MusicPatchNode) this.voiceBuss.voices.next()) {
                if (channel == voice.channel) {
                    voice.retriggerPhase = 0;
                }
            }
        }
        if (arg0 != 0) {
            this.setPitchBend(61, -58, -70);
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(BI)V")
    public void resetChannel(@OriginalArg(1) int channel) {
        if (channel < 0) {
            for (@Pc(7) int c = 0; c < 16; c++) {
                this.resetChannel(c);
            }
            return;
        }
        this.volumes[channel] = 12800;
        this.pans[channel] = 8192;
        this.expressions[channel] = 16383;
        this.pitchBends[channel] = 8192;
        this.modulations[channel] = 0;
        this.portamentoTimes[channel] = 8192;
        this.releaseUnheldVoices(channel);
        this.resetRetrigger(0, channel);
        this.switches[channel] = 0;
        this.parameters[channel] = 32767;
        this.bendRanges[channel] = 256;
        this.startOffsets[channel] = 0;
        this.setRetriggerRate(8192, channel);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!dha;B)I")
    public int noteVolume(@OriginalArg(0) MusicPatchNode voice) {
        if (this.levels[voice.channel] == 0) {
            return 0;
        }
        @Pc(18) MusicPatchEnvelope envelope = voice.envelope;
        @Pc(34) int gain = this.expressions[voice.channel] * this.volumes[voice.channel] + 4096 >> 13;
        @Pc(42) int scaled = gain * gain + 16384 >> 15;
        @Pc(51) int noteGain = voice.volume * scaled + 16384 >> 15;
        @Pc(60) int mixGain = this.volume * noteGain + 128 >> 8;
        gain = mixGain * this.levels[voice.channel] + 128 >> 8;
        if (envelope.decay > 0) {
            gain = (int) ((double) gain * Math.pow(0.5D, (double) voice.decayPhase * 1.953125E-5D * (double) envelope.decay) + 0.5D);
        }
        @Pc(102) int phase;
        @Pc(110) int amplitude;
        @Pc(132) int start;
        @Pc(144) int end;
        if (envelope.envelope != null) {
            phase = voice.envelopePhase;
            amplitude = envelope.envelope[voice.envelopeIndex + 1];
            if (envelope.envelope.length - 2 > voice.envelopeIndex) {
                start = (envelope.envelope[voice.envelopeIndex] & 0xFF) << 8;
                end = (envelope.envelope[voice.envelopeIndex + 2] & 0xFF) << 8;
                amplitude += (envelope.envelope[voice.envelopeIndex + 3] - amplitude) * (-start + phase) / (end - start);
            }
            gain = amplitude * gain + 32 >> 6;
        }
        if (voice.releasePhase > 0 && envelope.release != null) {
            phase = voice.releasePhase;
            amplitude = envelope.release[voice.releaseIndex + 1];
            if (envelope.release.length - 2 > voice.releaseIndex) {
                start = (envelope.release[voice.releaseIndex] & 0xFF) << 8;
                end = (envelope.release[voice.releaseIndex + 2] & 0xFF) << 8;
                amplitude += (phase - start) * (envelope.release[voice.releaseIndex + 3] - amplitude) / (end - start);
            }
            gain = amplitude * gain + 32 >> 6;
        }
        return gain;
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "()I")
    @Override
    public synchronized int method9132() {
        return 0;
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(Lclient!dha;B)Z")
    public boolean dropSilentVoice(@OriginalArg(0) MusicPatchNode voice) {
        if (voice.stream != null) {
            return false;
        }
        if (voice.releasePhase >= 0) {
            voice.unlink();
            if (voice.exclusiveGroup > 0 && this.groupVoices[voice.channel][voice.exclusiveGroup] == voice) {
                this.groupVoices[voice.channel][voice.exclusiveGroup] = null;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public synchronized AudioBuss nextSubStream() {
        return null;
    }

    @OriginalMember(owner = "client!bd", name = "d", descriptor = "(B)Z")
    public synchronized boolean isPlaying() {
        return this.midiSequence.isPlaying();
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(ILclient!dha;)I")
    public int notePan(@OriginalArg(1) MusicPatchNode voice) {
        @Pc(9) int pan = this.pans[voice.channel];
        return pan < 8192 ? pan * voice.pan + 32 >> 6 : 16384 - ((128 - voice.pan) * (-pan + 16384) + 32 >> 6);
    }

    @OriginalMember(owner = "client!bd", name = "d", descriptor = "(II)V")
    public void handleEvent(@OriginalArg(0) int event) {
        @Pc(9) int status = event & 0xF0;
        @Pc(16) int channel;
        @Pc(22) int data1;
        @Pc(28) int data2;
        if (status == 128) {
            channel = event & 0xF;
            data1 = event >> 8 & 0x7F;
            data2 = event >> 16 & 0x7F;
            this.noteOff(data2, channel, data1);
        } else if (status == 144) {
            channel = event & 0xF;
            data1 = event >> 8 & 0x7F;
            data2 = event >> 16 & 0x7F;
            if (data2 <= 0) {
                this.noteOff(64, channel, data1);
            } else {
                this.noteOn(channel, data1, data2);
            }
        } else if (status == 160) {
            channel = event & 0xF;
            data1 = event >> 8 & 0x7F;
            data2 = event >> 16 & 0x7F;
            this.keyPressure(data2, channel, data1);
        } else if (status == 176) {
            channel = event & 0xF;
            data1 = event >> 8 & 0x7F;
            data2 = event >> 16 & 0x7F;
            if (data1 == 0) {
                this.banks[channel] = (data2 << 14) + (this.banks[channel] & 0xFFE03FFF);
            }
            if (data1 == 32) {
                this.banks[channel] = (data2 << 7) + (this.banks[channel] & 0xFFFFC07F);
            }
            if (data1 == 1) {
                this.modulations[channel] = (this.modulations[channel] & 0xFFFFC07F) + (data2 << 7);
            }
            if (data1 == 33) {
                this.modulations[channel] = data2 + (this.modulations[channel] & 0xFFFFFF80);
            }
            if (data1 == 5) {
                this.portamentoTimes[channel] = (data2 << 7) + (this.portamentoTimes[channel] & 0xFFFFC07F);
            }
            if (data1 == 37) {
                this.portamentoTimes[channel] = (this.portamentoTimes[channel] & 0xFFFFFF80) + data2;
            }
            if (data1 == 7) {
                this.volumes[channel] = (data2 << 7) + (this.volumes[channel] & 0xFFFFC07F);
            }
            if (data1 == 39) {
                this.volumes[channel] = data2 + (this.volumes[channel] & 0xFFFFFF80);
            }
            if (data1 == 10) {
                this.pans[channel] = (data2 << 7) + (this.pans[channel] & 0xFFFFC07F);
            }
            if (data1 == 42) {
                this.pans[channel] = data2 + (this.pans[channel] & 0xFFFFFF80);
            }
            if (data1 == 11) {
                this.expressions[channel] = (this.expressions[channel] & 0xFFFFC07F) + (data2 << 7);
            }
            if (data1 == 43) {
                this.expressions[channel] = (this.expressions[channel] & 0xFFFFFF80) + data2;
            }
            if (data1 == 64) {
                if (data2 < 64) {
                    this.switches[channel] &= 0xFFFFFFFE;
                } else {
                    this.switches[channel] |= 0x1;
                }
            }
            if (data1 == 65) {
                if (data2 >= 64) {
                    this.switches[channel] |= 0x2;
                } else {
                    this.releaseUnheldVoices(channel);
                    this.switches[channel] &= 0xFFFFFFFD;
                }
            }
            if (data1 == 99) {
                this.parameters[channel] = (data2 << 7) + (this.parameters[channel] & 0x7F);
            }
            if (data1 == 98) {
                this.parameters[channel] = data2 + (this.parameters[channel] & 0x3F80);
            }
            if (data1 == 101) {
                this.parameters[channel] = (this.parameters[channel] & 0x7F) + (data2 << 7) + 16384;
            }
            if (data1 == 100) {
                this.parameters[channel] = (this.parameters[channel] & 0x3F80) + data2 + 16384;
            }
            if (data1 == 120) {
                this.stopVoices(channel);
            }
            if (data1 == 121) {
                this.resetChannel(channel);
            }
            if (data1 == 123) {
                this.releaseVoices(channel);
            }
            @Pc(557) int parameter;
            if (data1 == 6) {
                parameter = this.parameters[channel];
                if (parameter == 16384) {
                    this.bendRanges[channel] = (data2 << 7) + (this.bendRanges[channel] & 0xFFFFC07F);
                }
            }
            if (data1 == 38) {
                parameter = this.parameters[channel];
                if (parameter == 16384) {
                    this.bendRanges[channel] = (this.bendRanges[channel] & 0xFFFFFF80) + data2;
                }
            }
            if (data1 == 16) {
                this.startOffsets[channel] = (data2 << 7) + (this.startOffsets[channel] & 0xFFFFC07F);
            }
            if (data1 == 48) {
                this.startOffsets[channel] = data2 + (this.startOffsets[channel] & 0xFFFFFF80);
            }
            if (data1 == 81) {
                if (data2 >= 64) {
                    this.switches[channel] |= 0x4;
                } else {
                    this.resetRetrigger(0, channel);
                    this.switches[channel] &= 0xFFFFFFFB;
                }
            }
            if (data1 == 17) {
                this.setRetriggerRate((this.retriggerRates[channel] & 0xFFFFC07F) + (data2 << 7), channel);
            }
            if (data1 == 49) {
                this.setRetriggerRate(data2 + (this.retriggerRates[channel] & 0xFFFFFF80), channel);
            }
        } else if (status == 192) {
            channel = event & 0xF;
            data1 = event >> 8 & 0x7F;
            this.setProgram(channel, data1 + this.banks[channel]);
        } else if (status == 208) {
            channel = event & 0xF;
            data1 = event >> 8 & 0x7F;
            this.channelPressure(channel, data1);
        } else if (status == 224) {
            channel = event & 0xF;
            data1 = (event >> 8 & 0x7F) + ((event & 0x7F0197) >> 9);
            this.setPitchBend(data1, -5807, channel);
        } else {
            status = event & 0xFF;
            if (status == 255) {
                this.reset(true);
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!bn;ZIJZ)V")
    public synchronized void method925(@OriginalArg(0) MidiSong song, @OriginalArg(1) boolean loop, @OriginalArg(3) long ticks) {
        this.start(loop, true, song);
        this.fastForward(ticks * (long) this.midiSequence.timeDivision);
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(IZI)V")
    public synchronized void method926(@OriginalArg(0) int level, @OriginalArg(2) int channel) {
        if (channel < 0) {
            for (@Pc(12) int c = 0; c < 16; c++) {
                this.levels[c] = level;
            }
        } else {
            this.levels[channel] = level;
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(B)V")
    public synchronized void method927() {
        for (@Pc(5) MusicPatch patch = (MusicPatch) this.patches.first(); patch != null; patch = (MusicPatch) this.patches.next()) {
            patch.unlink();
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(IIB)V")
    public void setRetriggerRate(@OriginalArg(0) int rate, @OriginalArg(1) int channel) {
        this.retriggerRates[channel] = rate;
        this.retriggerSteps[channel] = (int) (Math.pow(2.0D, (double) rate * 5.4931640625E-4D) * 2097152.0D + 0.5D);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IIB)V")
    public synchronized void method929() {
        this.initDrumChannel();
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(II)V")
    public void stopVoices(@OriginalArg(1) int channel) {
        for (@Pc(14) MusicPatchNode voice = (MusicPatchNode) this.voiceBuss.voices.first(); voice != null; voice = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if (channel < 0 || voice.channel == channel) {
                if (voice.stream != null) {
                    voice.stream.fadeOut(Audio.sampleRate / 100);
                    if (voice.stream.isFading()) {
                        this.voiceBuss.fadeOutBuss.addFirst(voice.stream);
                    }
                    voice.clear();
                }
                if (voice.releasePhase < 0) {
                    this.keyVoices[voice.channel][voice.key] = null;
                }
                voice.unlink();
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "(B)V")
    public void processEvents(@OriginalArg(0) byte arg0) {
        @Pc(8) int track = this.track;
        @Pc(11) int tick = this.tick;
        @Pc(14) long time = this.nextEventTime;
        if (this.nextSong != null && this.switchTick == tick) {
            this.start(this.looping, this.killVoices, this.nextSong);
            this.processEvents((byte) -98);
            return;
        }
        while (this.tick == tick) {
            while (this.midiSequence.trackDeltas[track] == tick) {
                this.midiSequence.switchTrack(track);
                @Pc(50) int event = this.midiSequence.nextEvent(track);
                if (event == 1) {
                    this.midiSequence.endTrack();
                    this.midiSequence.updatePosition(track);
                    if (this.midiSequence.isComplete()) {
                        if (this.nextSong != null) {
                            this.method934(this.nextSong, this.looping);
                            this.processEvents((byte) -119);
                            return;
                        }
                        if (!this.looping || tick == 0) {
                            this.reset(true);
                            this.midiSequence.finish();
                            return;
                        }
                        this.midiSequence.reset(time);
                    }
                    break;
                }
                if ((event & 0x80) != 0) {
                    this.handleEvent(event);
                }
                this.midiSequence.step(track);
                this.midiSequence.updatePosition(track);
            }
            track = this.midiSequence.activeTrack();
            tick = this.midiSequence.trackDeltas[track];
            time = this.midiSequence.delayForDelta(tick);
        }
        if (arg0 >= -19) {
            this.tick = -58;
        }
        this.track = track;
        this.nextEventTime = time;
        this.tick = tick;
        if (this.nextSong != null && tick > this.switchTick) {
            this.tick = this.switchTick;
            this.track = -1;
            this.nextEventTime = this.midiSequence.delayForDelta(this.tick);
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(I)V")
    public synchronized void method933() {
        for (@Pc(7) MusicPatch patch = (MusicPatch) this.patches.first(); patch != null; patch = (MusicPatch) this.patches.next()) {
            patch.clearSoundIds();
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ILclient!bn;Z)V")
    public synchronized void method934(@OriginalArg(1) MidiSong song, @OriginalArg(2) boolean loop) {
        this.start(loop, true, song);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IIIZ)V")
    public void noteOn(@OriginalArg(0) int channel, @OriginalArg(1) int key, @OriginalArg(2) int velocity) {
        this.noteOff(64, channel, key);
        if ((this.switches[channel] & 0x2) != 0) {
            for (@Pc(25) MusicPatchNode held = (MusicPatchNode) this.voiceBuss.voices.last(); held != null; held = (MusicPatchNode) this.voiceBuss.voices.previous()) {
                if (channel == held.channel && held.releasePhase < 0) {
                    this.keyVoices[channel][held.key] = null;
                    this.keyVoices[channel][key] = held;
                    @Pc(72) int pitch = held.pitch + (held.portamentoFraction * held.portamentoOffset >> 12);
                    held.pitch += key - held.key << 8;
                    held.portamentoOffset = pitch - held.pitch;
                    held.portamentoFraction = 4096;
                    held.key = key;
                    return;
                }
            }
        }
        @Pc(117) MusicPatch patch = (MusicPatch) this.patches.get(this.programs[channel]);
        if (patch == null) {
            return;
        }
        @Pc(126) VariableRateSoundPacket sound = patch.sounds[key];
        if (sound == null) {
            return;
        }
        @Pc(142) MusicPatchNode voice = new MusicPatchNode();
        voice.patch = patch;
        voice.sound = sound;
        voice.channel = channel;
        voice.envelope = patch.envelopes[key];
        voice.exclusiveGroup = patch.exclusiveGroups[key];
        voice.key = key;
        voice.volume = patch.volumes[key] * velocity * velocity * patch.volume + 1024 >> 11;
        voice.pan = patch.pans[key] & 0xFF;
        voice.pitch = (key << 8) - (patch.pitchOffsets[key] & 0x7FFF);
        voice.decayPhase = 0;
        voice.releaseIndex = 0;
        voice.releasePhase = -1;
        voice.envelopePhase = 0;
        voice.envelopeIndex = 0;
        if (this.startOffsets[channel] == 0) {
            voice.stream = SoundStream.create(sound, this.sampleRate(voice), this.noteVolume(voice), this.notePan(voice));
        } else {
            voice.stream = SoundStream.create(sound, this.sampleRate(voice), 0, this.notePan(voice));
            this.applyStartOffset(patch.pitchOffsets[key] < 0, voice);
        }
        if (patch.pitchOffsets[key] < 0) {
            voice.stream.setLoops(-1);
        }
        if (voice.exclusiveGroup >= 0) {
            @Pc(297) MusicPatchNode previous = this.groupVoices[channel][voice.exclusiveGroup];
            if (previous != null && previous.releasePhase < 0) {
                this.keyVoices[channel][previous.key] = null;
                previous.releasePhase = 0;
            }
            this.groupVoices[channel][voice.exclusiveGroup] = voice;
        }
        this.voiceBuss.voices.addLast(voice);
        this.keyVoices[channel][key] = voice;
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(III)V")
    public void setPitchBend(@OriginalArg(0) int bend, @OriginalArg(1) int arg1, @OriginalArg(2) int channel) {
        if (arg1 != -5807) {
            this.track = -101;
        }
        this.pitchBends[channel] = bend;
    }

    @OriginalMember(owner = "client!bd", name = "e", descriptor = "(II)V")
    public void releaseVoices(@OriginalArg(1) int channel) {
        for (@Pc(6) MusicPatchNode voice = (MusicPatchNode) this.voiceBuss.voices.first(); voice != null; voice = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if ((channel < 0 || channel == voice.channel) && voice.releasePhase < 0) {
                this.keyVoices[voice.channel][voice.key] = null;
                voice.releasePhase = 0;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "b", descriptor = "(IIII)V")
    public void keyPressure(@OriginalArg(0) int pressure, @OriginalArg(1) int channel, @OriginalArg(2) int key) {
        /* empty */
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IZI)V")
    public void initDrumChannel() {
        this.defaultPrograms[9] = 128;
        this.banks[9] = 128;
        this.setProgram(9, 128);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(BI)V")
    public void releaseUnheldVoices(@OriginalArg(1) int channel) {
        if ((this.switches[channel] & 0x2) == 0) {
            return;
        }
        for (@Pc(28) MusicPatchNode voice = (MusicPatchNode) this.voiceBuss.voices.first(); voice != null; voice = (MusicPatchNode) this.voiceBuss.voices.next()) {
            if (channel == voice.channel && this.keyVoices[channel][voice.key] == null && voice.releasePhase < 0) {
                voice.releasePhase = 0;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(IZ)V")
    public void reset(@OriginalArg(1) boolean killVoices) {
        if (killVoices) {
            this.stopVoices(-1);
        } else {
            this.releaseVoices(-1);
        }
        this.resetChannel(-1);
        for (@Pc(21) int channel = 0; channel < 16; channel++) {
            this.programs[channel] = this.defaultPrograms[channel];
        }
        for (@Pc(46) int channel = 0; channel < 16; channel++) {
            this.banks[channel] = this.defaultPrograms[channel] & 0xFFFFFF80;
        }
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(ZLclient!dha;I)V")
    public void applyStartOffset(@OriginalArg(0) boolean looping, @OriginalArg(1) MusicPatchNode voice) {
        @Pc(13) int length = voice.sound.data.length;
        @Pc(42) int offset;
        if (looping && voice.sound.pingPong) {
            @Pc(29) int span = length + length - voice.sound.nominalBitRate;
            offset = (int) ((long) span * (long) this.startOffsets[voice.channel] >> 6);
            length <<= 0x8;
            if (length <= offset) {
                offset = length + length - offset - 1;
                voice.stream.playBackward();
            }
        } else {
            offset = (int) ((long) this.startOffsets[voice.channel] * (long) length >> 6);
        }
        voice.stream.setPosition(offset);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!fca;Lclient!sb;IILclient!bn;)Z")
    public synchronized boolean method944(@OriginalArg(0) SoundCache cache, @OriginalArg(1) js5 js5, @OriginalArg(4) MidiSong song) {
        song.computePrograms();

        @Pc(15) boolean ready = true;
        @Pc(29) int[] maxSamples = new int[]{22050};
        for (@Pc(35) MidiProgramNode node = (MidiProgramNode) song.programs.first(); node != null; node = (MidiProgramNode) song.programs.next()) {
            @Pc(40) int program = (int) node.key;
            @Pc(48) MusicPatch patch = (MusicPatch) this.patches.get(program);
            if (patch == null) {
                patch = readPatch(js5, program);
                if (patch == null) {
                    ready = false;
                    continue;
                }
                this.patches.put(program, patch);
            }

            if (!patch.loadSounds(cache, maxSamples, node.notes)) {
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
    public synchronized void skip(@OriginalArg(0) int length) {
        if (this.midiSequence.isPlaying()) {
            @Pc(18) int timePerSample = this.midiSequence.timeDivision * this.microsecondsPerSecond / Audio.sampleRate;
            do {
                @Pc(27) long time = (long) length * (long) timePerSample + this.currentTime;
                if (this.nextEventTime - time >= 0L) {
                    this.currentTime = time;
                    break;
                }
                @Pc(58) int count = (int) ((this.nextEventTime + (long) timePerSample - this.currentTime - 1L) / (long) timePerSample);
                this.currentTime += (long) count * (long) timePerSample;
                this.voiceBuss.skip(count);
                length -= count;
                this.processEvents((byte) -117);
            } while (this.midiSequence.isPlaying());
        }
        this.voiceBuss.skip(length);
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(Lclient!dha;I[III)Z")
    public boolean updateVoice(@OriginalArg(0) MusicPatchNode voice, @OriginalArg(2) int[] mix, @OriginalArg(3) int offset, @OriginalArg(4) int length) {
        voice.samplesUntilUpdate = Audio.sampleRate / 100;
        if (voice.releasePhase >= 0 && (voice.stream == null || voice.stream.isFinished())) {
            voice.clear();
            voice.unlink();
            if (voice.exclusiveGroup > 0 && this.groupVoices[voice.channel][voice.exclusiveGroup] == voice) {
                this.groupVoices[voice.channel][voice.exclusiveGroup] = null;
            }
            return true;
        }
        @Pc(70) int fraction = voice.portamentoFraction;
        if (fraction > 0) {
            fraction -= (int) (Math.pow(2.0D, (double) this.portamentoTimes[voice.channel] * 4.921259842519685E-4D) * 16.0D + 0.5D);
            if (fraction < 0) {
                fraction = 0;
            }
            voice.portamentoFraction = fraction;
        }
        voice.stream.setRate(this.sampleRate(voice));
        @Pc(113) MusicPatchEnvelope envelope = voice.envelope;
        voice.vibratoPhase += envelope.vibratoRate;
        @Pc(122) boolean finished = false;
        voice.elapsed++;
        @Pc(147) double keyScale = (double) ((voice.key - 60 << 8) + (voice.portamentoOffset * voice.portamentoFraction >> 12)) * 5.086263020833333E-6D;
        if (envelope.decay > 0) {
            if (envelope.decayKeyScale > 0) {
                voice.decayPhase += (int) (Math.pow(2.0D, (double) envelope.decayKeyScale * keyScale) * 128.0D + 0.5D);
            } else {
                voice.decayPhase += 128;
            }
            if (voice.decayPhase * envelope.decay >= 819200) {
                finished = true;
            }
        }
        if (envelope.envelope != null) {
            if (envelope.envelopeKeyScale <= 0) {
                voice.envelopePhase += 128;
            } else {
                voice.envelopePhase += (int) (Math.pow(2.0D, keyScale * (double) envelope.envelopeKeyScale) * 128.0D + 0.5D);
            }
            while (voice.envelopeIndex < envelope.envelope.length - 2 && voice.envelopePhase > (envelope.envelope[voice.envelopeIndex + 2] & 0xFF) << 8) {
                voice.envelopeIndex += 2;
            }
            if (voice.envelopeIndex == envelope.envelope.length - 2 && envelope.envelope[voice.envelopeIndex + 1] == 0) {
                finished = true;
            }
        }
        if (voice.releasePhase >= 0 && envelope.release != null && (this.switches[voice.channel] & 0x1) == 0 && (voice.exclusiveGroup < 0 || voice != this.groupVoices[voice.channel][voice.exclusiveGroup])) {
            if (envelope.releaseKeyScale <= 0) {
                voice.releasePhase += 128;
            } else {
                voice.releasePhase += (int) (Math.pow(2.0D, keyScale * (double) envelope.releaseKeyScale) * 128.0D + 0.5D);
            }
            while (envelope.release.length - 2 > voice.releaseIndex && voice.releasePhase > (envelope.release[voice.releaseIndex + 2] & 0xFF) << 8) {
                voice.releaseIndex += 2;
            }
            if (envelope.release.length - 2 == voice.releaseIndex) {
                finished = true;
            }
        }
        if (!finished) {
            voice.stream.fadeTo(voice.samplesUntilUpdate, this.noteVolume(voice), this.notePan(voice));
            return false;
        }
        voice.stream.fadeOut(voice.samplesUntilUpdate);
        if (mix == null) {
            voice.stream.skip(length);
        } else {
            voice.stream.fill(mix, offset, length);
        }
        if (voice.stream.isFading()) {
            this.voiceBuss.fadeOutBuss.addFirst(voice.stream);
        }
        voice.clear();
        if (voice.releasePhase >= 0) {
            voice.unlink();
            if (voice.exclusiveGroup > 0 && this.groupVoices[voice.channel][voice.exclusiveGroup] == voice) {
                this.groupVoices[voice.channel][voice.exclusiveGroup] = null;
            }
        }
        return true;
    }

    @OriginalMember(owner = "client!bd", name = "a", descriptor = "(III)V")
    public void channelPressure(@OriginalArg(0) int channel, @OriginalArg(1) int pressure) {
        /* empty */
    }

    @OriginalMember(owner = "client!bd", name = "c", descriptor = "(IIB)V")
    public void setProgram(@OriginalArg(0) int channel, @OriginalArg(1) int program) {
        if (this.programs[channel] != program) {
            this.programs[channel] = program;
            for (@Pc(16) int group = 0; group < 128; group++) {
                this.groupVoices[channel][group] = null;
            }
        }
    }

    @OriginalMember(owner = "client!bd", name = "g", descriptor = "(I)I")
    public int getVolume() {
        return this.volume;
    }
}
