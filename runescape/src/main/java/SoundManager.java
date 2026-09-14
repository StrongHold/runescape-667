import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.js5.js5;
import com.jagex.sound.MixBuss;
import com.jagex.sound.AudioBussMixer;
import com.jagex.sound.Sound;
import com.jagex.sound.SoundStream;
import com.jagex.sound.SoundType;
import com.jagex.sound.SynthSound;
import com.jagex.sound.VariableRateSoundPacket;
import com.jagex.sound.vorbis.VorbisSound;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class SoundManager {

    @OriginalMember(owner = "client!oj", name = "n", descriptor = "Lclient!sia;")
    public static final Deque locSounds = new Deque();

    @OriginalMember(owner = "client!dja", name = "h", descriptor = "Lclient!av;")
    public static final IterableHashTable playerSounds = new IterableHashTable(16);

    @OriginalMember(owner = "client!wn", name = "f", descriptor = "Lclient!sia;")
    public static final Deque npcSounds = new Deque();

    @OriginalMember(owner = "client!mt", name = "L", descriptor = "[Lclient!eka;")
    public static Sound[] sounds = new Sound[50];

    @OriginalMember(owner = "client!bc", name = "c", descriptor = "I")
    public static int count = 0;

    @OriginalMember(owner = "client!kka", name = "d", descriptor = "Lclient!nn;")
    public static AudioBussMixer activeStreams;

    @OriginalMember(owner = "client!lka", name = "b", descriptor = "(B)V")
    public static void reset() {
        count = 0;
        sounds = new Sound[50];
    }

    @OriginalMember(owner = "client!dca", name = "a", descriptor = "(I)V")
    public static void method2000() {
        for (@Pc(8) PositionedSound sound = (PositionedSound) locSounds.first(); sound != null; sound = (PositionedSound) locSounds.next()) {
            if (sound.multi) {
                sound.update();
            }
        }
        for (@Pc(31) PositionedSound sound = (PositionedSound) npcSounds.first(); sound != null; sound = (PositionedSound) npcSounds.next()) {
            if (sound.multi) {
                sound.update();
            }
        }
    }

    @OriginalMember(owner = "client!pv", name = "a", descriptor = "(IIILclient!c;I)V")
    public static void method8312(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int level, @OriginalArg(3) LocType locType) {
        for (@Pc(18) PositionedSound sound = (PositionedSound) locSounds.first(); sound != null; sound = (PositionedSound) locSounds.next()) {
            if (sound.level == level && sound.x1 == x << 9 && sound.z1 == y << 9 && locType.id == sound.locType.id) {
                if (sound.stream != null) {
                    activeStreams.remove(sound.stream);
                    sound.stream = null;
                }
                if (sound.randomStream != null) {
                    activeStreams.remove(sound.randomStream);
                    sound.randomStream = null;
                }
                sound.unlink();
                return;
            }
        }
    }

    @OriginalMember(owner = "client!tea", name = "a", descriptor = "(ZI)V")
    public static void removeActiveStreams(@OriginalArg(0) boolean entities) {
        for (@Pc(8) PositionedSound sound = (PositionedSound) locSounds.first(); sound != null; sound = (PositionedSound) locSounds.next()) {
            if (sound.stream != null) {
                activeStreams.remove(sound.stream);
                sound.stream = null;
            }
            if (sound.randomStream != null) {
                activeStreams.remove(sound.randomStream);
                sound.randomStream = null;
            }
            sound.unlink();
        }

        if (entities) {
            for (@Pc(57) PositionedSound sound = (PositionedSound) npcSounds.first(); sound != null; sound = (PositionedSound) npcSounds.next()) {
                if (sound.stream != null) {
                    activeStreams.remove(sound.stream);
                    sound.stream = null;
                }
                sound.unlink();
            }

            for (@Pc(85) PositionedSound sound = (PositionedSound) playerSounds.first(); sound != null; sound = (PositionedSound) playerSounds.next()) {
                if (sound.stream != null) {
                    activeStreams.remove(sound.stream);
                    sound.stream = null;
                }
                sound.unlink();
            }
        } else {
        }
    }

    @OriginalMember(owner = "client!u", name = "a", descriptor = "(ILclient!tg;IIBI)V")
    public static void method8366(@OriginalArg(0) int clock, @OriginalArg(1) PositionedSound sound, @OriginalArg(2) int level, @OriginalArg(3) int x, @OriginalArg(5) int z) {
        if (sound.id == -1 && sound.randomIds == null) {
            return;
        }

        @Pc(23) int range = 0;
        @Pc(33) int volume = sound.volume * ClientOptions.instance.backgroundSoundVolume.getValue() >> 8;
        if (sound.x2 < x) {
            range = x - sound.x2;
        } else if (sound.x1 > x) {
            range = sound.x1 - x;
        }
        if (sound.z2 < z) {
            range += z - sound.z2;
        } else if (sound.z1 > z) {
            range += sound.z1 - z;
        }

        if (sound.rangeMax == 0 || range - 256 > sound.rangeMax || ClientOptions.instance.backgroundSoundVolume.getValue() == 0 || level != sound.level) {
            if (sound.stream != null) {
                activeStreams.remove(sound.stream);
                sound.stream = null;
                sound.vorbisSound = null;
                sound.packet = null;
            }
            if (sound.randomStream != null) {
                activeStreams.remove(sound.randomStream);
                sound.randomVorbisSound = null;
                sound.randomStream = null;
                sound.randomPacket = null;
            }
            return;
        }

        range -= 256;

        if (range < 0) {
            range = 0;
        }

        @Pc(172) int rangeDelta = sound.rangeMax - sound.minRange;
        if (rangeDelta < 0) {
            rangeDelta = sound.rangeMax;
        }

        @Pc(182) int soundRange = volume;
        @Pc(187) int local187 = range - sound.minRange;
        if (local187 > 0 && rangeDelta > 0) {
            soundRange = (rangeDelta - local187) * volume / rangeDelta;
        }

        PlayerEntity.self.getSize();
        @Pc(214) int soundRangeY = 8192;
        @Pc(225) int soundX = ((sound.x2 + sound.x1) / 2) - x;
        @Pc(236) int soundZ = ((sound.z2 + sound.z1) / 2) - z;
        if (soundX != 0 || soundZ != 0) {
            @Pc(264) int yaw = (-Camera.yaw - (int) (Math.atan2(soundX, soundZ) * 2607.5945876176133D) - 4096) & 0x3FFF;
            if (yaw > 8192) {
                yaw = 16384 - yaw;
            }

            @Pc(275) int soundY;
            if (range <= 0) {
                soundY = 8192;
            } else if (range >= 4096) {
                soundY = 16384;
            } else {
                soundY = ((range * 8192) / 4096) + 8192;
            }

            soundRangeY = ((16384 - soundY) >> 1) + ((yaw * soundY) / 8192);
        }

        if (sound.stream != null) {
            sound.stream.setVolume(soundRange);
            sound.stream.setRange(soundRangeY);
        } else if (sound.id >= 0) {
            @Pc(264) int rate = sound.rateMax == 256 && sound.rateMin == 256 ? 256 : method2572(sound.rateMin, sound.rateMax);

            if (sound.vorbis) {
                if (sound.vorbisSound == null) {
                    sound.vorbisSound = VorbisSound.create(js5.VORBIS, sound.id);
                }

                if (sound.vorbisSound != null) {
                    if (sound.packet == null) {
                        sound.packet = sound.vorbisSound.method8502(new int[]{22050});
                    }

                    if (sound.packet != null) {
                        @Pc(391) SoundStream stream = SoundStream.create(sound.packet, rate, soundRange << 6, soundRangeY);
                        stream.setLoops(-1);
                        activeStreams.addFirst(stream);
                        sound.stream = stream;
                    }
                }
            } else {
                @Pc(408) SynthSound synth = SynthSound.get(js5.SYNTH_SOUNDS, sound.id, 0);

                if (synth != null) {
                    @Pc(415) VariableRateSoundPacket packet = synth.sample().resample(Static681.aSampleRateConverter_2);
                    @Pc(423) SoundStream stream = SoundStream.create(packet, rate, soundRange << 6, soundRangeY);
                    stream.setLoops(-1);
                    activeStreams.addFirst(stream);
                    sound.stream = stream;
                }
            }
        }

        if (sound.randomStream != null) {
            sound.randomStream.setVolume(soundRange);
            sound.randomStream.setRange(soundRangeY);

            if (!sound.randomStream.isLinked()) {
                sound.randomStream = null;
                sound.randomVorbisSound = null;
                sound.randomPacket = null;
            }
        } else if (sound.randomIds != null && (sound.randomDelay -= clock) <= 0) {
            @Pc(264) int rate = sound.rateMax == 256 && sound.rateMin == 256 ? 256 : sound.rateMin + (int) ((double) (sound.rateMax - sound.rateMin) * Math.random());

            if (sound.random) {
                if (sound.randomVorbisSound == null) {
                    @Pc(275) int randomId = (int) (Math.random() * (double) sound.randomIds.length);
                    sound.randomVorbisSound = VorbisSound.create(js5.VORBIS, sound.randomIds[randomId]);
                }

                if (sound.randomVorbisSound != null) {
                    if (sound.randomPacket == null) {
                        sound.randomPacket = sound.randomVorbisSound.method8502(new int[]{22050});
                    }

                    if (sound.randomPacket != null) {
                        @Pc(391) SoundStream stream = SoundStream.create(sound.randomPacket, rate, soundRange << 6, soundRangeY);
                        stream.setLoops(0);
                        activeStreams.addFirst(stream);
                        sound.randomDelay = (int) ((double) (sound.delayMax - sound.delayMin) * Math.random()) + sound.delayMin;
                        sound.randomStream = stream;
                    }
                }
            } else {
                @Pc(275) int randomId = (int) ((double) sound.randomIds.length * Math.random());
                @Pc(536) SynthSound synth = SynthSound.get(js5.SYNTH_SOUNDS, sound.randomIds[randomId], 0);

                if (synth != null) {
                    @Pc(543) VariableRateSoundPacket packet = synth.sample().resample(Static681.aSampleRateConverter_2);
                    @Pc(551) SoundStream stream = SoundStream.create(packet, rate, soundRange << 6, soundRangeY);
                    stream.setLoops(0);
                    activeStreams.addFirst(stream);
                    sound.randomDelay = (int) (Math.random() * (double) (sound.delayMax - sound.delayMin)) + sound.delayMin;
                    sound.randomStream = stream;
                }
            }
        }
    }

    @OriginalMember(owner = "client!ew", name = "a", descriptor = "(III)I")
    public static int method2572(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        @Pc(17) double max = Math.log(arg1) / Math.log(2.0D);
        @Pc(24) double min = Math.log(arg0) / Math.log(2.0D);
        @Pc(33) double local33 = min + Math.random() * (max - min);
        return (int) (Math.pow(2.0D, local33) + 0.5D);
    }

    @OriginalMember(owner = "client!k", name = "a", descriptor = "(IILclient!cka;)V")
    public static void method4577(@OriginalArg(0) int arg0, @OriginalArg(2) SeqType arg1) {
        if (count >= 50 || (arg1 == null || arg1.soundInfo == null || arg1.soundInfo.length <= arg0 || arg1.soundInfo[arg0] == null)) {
            return;
        }
        @Pc(42) int local42 = arg1.soundInfo[arg0][0];
        @Pc(46) int local46 = local42 >> 8;
        @Pc(63) int local63;
        if (arg1.soundInfo[arg0].length > 1) {
            local63 = (int) (Math.random() * (double) arg1.soundInfo[arg0].length);
            if (local63 > 0) {
                local46 = arg1.soundInfo[arg0][local63];
            }
        }
        @Pc(81) int local81 = local42 >> 5 & 0x7;
        local63 = 256;
        if (arg1.anIntArray154 != null && arg1.anIntArray155 != null) {
            local63 = method2572(arg1.anIntArray154[arg0], arg1.anIntArray155[arg0]);
        }
        if (arg1.vorbisSound) {
            playVorbisSound(local46, local81, 0, 255, local63, false);
        } else {
            playSynthSound(local46, local81, 0, 255, local63);
        }
    }

    @OriginalMember(owner = "client!fo", name = "a", descriptor = "(IIIBIIZ)V")
    public static void playVorbisSound(@OriginalArg(0) int id, @OriginalArg(1) int loops, @OriginalArg(4) int delay, @OriginalArg(5) int volume, @OriginalArg(2) int rate, @OriginalArg(6) boolean speech) {
        if ((speech ? ClientOptions.instance.speechVolume.getValue() : ClientOptions.instance.soundVolume.getValue()) != 0 && loops != 0 && count < 50 && id != -1) {
            sounds[count++] = new Sound((byte) (speech ? SoundType.VORBIS_VOICE_OVER : SoundType.VORBIS), id, loops, delay, volume, rate, 0, null);
        }
    }

    @OriginalMember(owner = "client!w", name = "a", descriptor = "(B)V")
    public static void stopVorbisSpeech() {
        for (@Pc(1) int i = 0; i < count; i++) {
            @Pc(6) Sound sound = sounds[i];

            if (sound.type == SoundType.VORBIS_VOICE_OVER) {
                if (sound.stream == null) {
                    sound.delay = Integer.MIN_VALUE;
                } else {
                    activeStreams.remove(sound.stream);
                }
            }
        }
    }

    @OriginalMember(owner = "client!caa", name = "a", descriptor = "(IIII)V")
    public static void playMidiSong(@OriginalArg(2) int id, @OriginalArg(0) int volume, @OriginalArg(3) int delay) {
        @Pc(12) int local12 = volume * ClientOptions.instance.musicVolume.getValue() >> 8;
        if (id == -1 && !Static501.aBoolean575) {
            Static100.method1988();
        } else if (id != -1 && (SongManager.playing != id || !Static52.method1157(-122)) && local12 != 0 && !Static501.aBoolean575) {
            Static618.method8318(js5.MIDI_SONGS, local12, id, delay);
            AudioRenderer.mixBussReset();
        }
        if (SongManager.playing != id) {
            Static8.aClass2_Sub6_Sub1_1 = null;
        }
        SongManager.playing = id;
    }

    @OriginalMember(owner = "client!pda", name = "a", descriptor = "(IIIB)V")
    public static void playMidiJingle(@OriginalArg(1) int id, @OriginalArg(2) int delay, @OriginalArg(0) int volume) {
        @Pc(12) int local12 = ClientOptions.instance.musicVolume.getValue() * volume >> 8;
        if (local12 == 0 || id == -1) {
            return;
        }
        if (!Static501.aBoolean575 && SongManager.playing != -1 && Static52.method1157(0x6E ^ 0x11) && !Static203.method3070()) {
            Static8.aClass2_Sub6_Sub1_1 = Static426.method1018();
            @Pc(52) MixBuss local52 = Static48.method1100(Static8.aClass2_Sub6_Sub1_1);
            Static697.method9120(local52);
        }
        SongManager.method8229(id, local12, js5.MIDI_JINGLES);
        AudioRenderer.mixBussSetLevel(255, -1);
        Static501.aBoolean575 = true;
    }

    @OriginalMember(owner = "client!fa", name = "a", descriptor = "(IIIIII)V")
    public static void playSynthSound(@OriginalArg(2) int id, @OriginalArg(3) int loops, @OriginalArg(1) int delay, @OriginalArg(4) int volume, @OriginalArg(0) int rate) {
        if (ClientOptions.instance.soundVolume.getValue() != 0 && loops != 0 && count < 50 && id != -1) {
            sounds[count++] = new Sound((byte) SoundType.SYNTH, id, loops, delay, volume, rate, 0, null);
        }
    }

    @OriginalMember(owner = "client!fc", name = "a", descriptor = "(IIBIIII)V")
    public static void playSynthSoundArea(@OriginalArg(3) int id, @OriginalArg(4) int loops, @OriginalArg(1) int delay, @OriginalArg(0) int volume, @OriginalArg(5) int rate, @OriginalArg(6) int coord) {
        if (ClientOptions.instance.soundVolume.getValue() != 0 && loops != 0 && count < 50 && id != -1) {
            sounds[count++] = new Sound((byte) SoundType.SYNTH, id, loops, delay, volume, rate, coord, null);
        }
    }

    @OriginalMember(owner = "client!fk", name = "a", descriptor = "(IIIIZIII)V")
    public static void playVorbisSoundArea(@OriginalArg(7) int id, @OriginalArg(0) int loops, @OriginalArg(1) int delay, @OriginalArg(6) int volume, @OriginalArg(5) int rate, @OriginalArg(2) int coord) {
        if (ClientOptions.instance.soundVolume.getValue() != 0 && loops != 0 && count < 50 && id != -1) {
            sounds[count++] = new Sound((byte) SoundType.VORBIS, id, loops, delay, volume, rate, coord, null);
        }
    }

    @OriginalMember(owner = "client!bu", name = "a", descriptor = "(ILclient!wj;)V")
    public static void removeSounds(@OriginalArg(1) NPCEntity npc) {
        for (@Pc(17) PositionedSound sound = (PositionedSound) npcSounds.first(); sound != null; sound = (PositionedSound) npcSounds.next()) {
            if (sound.npc == npc) {
                if (sound.stream != null) {
                    activeStreams.remove(sound.stream);
                    sound.stream = null;
                }
                sound.unlink();
                return;
            }
        }
    }

    @OriginalMember(owner = "client!cp", name = "a", descriptor = "(ILclient!ca;IILclient!wj;Lclient!c;BI)V")
    public static void addSounds(@OriginalArg(0) int level, @OriginalArg(2) int x, @OriginalArg(3) int z, @OriginalArg(1) PlayerEntity player, @OriginalArg(4) NPCEntity npc, @OriginalArg(5) LocType locType, @OriginalArg(7) int rotation) {
        @Pc(7) PositionedSound sound = new PositionedSound();
        sound.level = level;
        sound.x1 = x << 9;
        sound.z1 = z << 9;

        if (locType != null) {
            sound.locType = locType;

            @Pc(173) int width = locType.width;
            @Pc(176) int length = locType.length;
            if (rotation == 1 || rotation == 3) {
                length = locType.width;
                width = locType.length;
            }

            sound.volume = locType.soundVolume;
            sound.rangeMax = locType.soundRange << 9;
            sound.id = locType.sound;
            sound.delayMax = locType.soundDelayMax;
            sound.z2 = z + length << 9;
            sound.rateMax = locType.soundRateMax;
            sound.vorbis = locType.vorbis;
            sound.random = locType.randomsound;
            sound.minRange = locType.soundSize << 9;
            sound.delayMin = locType.soundDelayMin;
            sound.rateMin = locType.soundRateMin;
            sound.randomIds = locType.randomSoundIds;
            sound.x2 = x + width << 9;

            if (locType.multiloc != null) {
                sound.multi = true;
                sound.update();
            }

            if (sound.randomIds != null) {
                sound.randomDelay = (int) ((double) (sound.delayMax - sound.delayMin) * Math.random()) + sound.delayMin;
            }

            locSounds.addLast(sound);
        } else if (npc != null) {
            sound.npc = npc;

            @Pc(37) NPCType type = npc.type;
            if (type.multinpcs != null) {
                sound.multi = true;
                type = type.getMultiNPC(TimedVarDomain.instance);
            }

            if (type != null) {
                sound.x2 = (x + type.size) << 9;
                sound.z2 = (z + type.size) << 9;
                sound.id = NPCEntity.currentSound(npc);
                sound.minRange = type.soundRangeMin << 9;
                sound.volume = type.soundVolume;
                sound.rateMin = type.soundRateMin;
                sound.vorbis = type.vorbis;
                sound.rangeMax = type.soundRangeMax << 9;
                sound.rateMax = type.soundRateMax;
            }

            npcSounds.addLast(sound);
        } else if (player != null) {
            sound.player = player;
            sound.x2 = (x + player.getSize()) << 9;
            sound.z2 = (z + player.getSize()) << 9;
            sound.id = PlayerEntity.currentSound(player);
            sound.rangeMax = player.soundRange << 9;
            sound.rateMax = 256;
            sound.rateMin = 256;
            sound.volume = player.soundVolume;
            sound.minRange = 0;
            sound.vorbis = player.vorbis;

            playerSounds.put(player.slot, sound);
        }
    }
}
