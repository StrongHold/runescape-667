import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;
import com.jagex.sound.MusicPatch;
import com.jagex.sound.MusicPatchEnvelope;
import com.jagex.sound.SynthSound;
import com.jagex.sound.VariableRateSoundPacket;
import com.jagex.sound.midi.MidiSong;
import com.jagex.sound.vorbis.VorbisSound;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes every sound of the cache in a standard format: each synthesised sound and each Vorbis
 * sound as the samples the client plays, a WAV file of its rate, mono, eight bits, with its loop in
 * the file's sample chunk; each song and each jingle as the standard MIDI file the client builds
 * from its own packed form ({@code MidiSong.midiData}); and each instrument of the music, which no
 * standard format holds as the client plays it, as JSON of the fields the client decodes.
 *
 * <p>A sound is rendered by the client's own code: {@code SynthSound.sample}, without the delay
 * the client moves out of its start ({@code SynthSound.delay}), as the file keeps the silence the
 * delay stands for; and {@code VorbisSound.method8502}, which decodes every packet and keeps each
 * sample as a signed byte. Every sound the client plays by id is the one file of the group of that
 * id ({@code js5.getfile(int)}), and so is every instrument.
 */
public final class SoundExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The directory to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /**
     * The archive of the music's instruments, one group for each bank and program
     * ({@code MixBuss.readPatch}).
     */
    private static final int MIDI_INSTRUMENTS = 15;

    /**
     * The group of the Vorbis archive whose first file is the setup every Vorbis sound shares
     * ({@code VorbisSound.decodeHeader}); it is no sound.
     */
    private static final int VORBIS_SETUP_GROUP = 0;

    /**
     * What a WAV file's sample chunk says of a loop: played forwards, or forwards and back.
     */
    private static final int LOOP_FORWARD = 0;
    private static final int LOOP_ALTERNATING = 1;

    private SoundExport() {
        /* empty */
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportSounds", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        writeSynths(cache, args.out.resolve("synths"));
        writeVorbis(cache, args.out.resolve("vorbis"));
        writeSongs(cache, Js5Archive.MIDI_SONGS, args.out.resolve("songs"));
        writeSongs(cache, Js5Archive.MIDI_JINGLES, args.out.resolve("jingles"));
        writePatches(cache, args.out.resolve("patches"));
    }

    private static void writeSynths(File cache, Path directory) throws Exception {
        Files.createDirectories(directory);
        var archive = Cache.js5(cache, Js5Archive.SYNTH_SOUNDS);
        var written = 0;
        for (var group : Cache.groupsOf(Cache.index(cache, Js5Archive.SYNTH_SOUNDS))) {
            var data = archive.getfile(group);
            if (data != null) {
                var packet = new SynthSound(new Packet(data)).sample();
                Files.write(directory.resolve(group + ".wav"), wav(packet));
                written++;
            }
        }
        System.out.println("wrote " + written + " synthesised sounds to " + directory.toAbsolutePath().normalize());
    }

    private static void writeVorbis(File cache, Path directory) throws Exception {
        Files.createDirectories(directory);
        var archive = Cache.js5(cache, Js5Archive.VORBIS);
        VorbisSound.decodeHeader(archive.getfile(0, VORBIS_SETUP_GROUP));
        var written = 0;
        for (var group : Cache.groupsOf(Cache.index(cache, Js5Archive.VORBIS))) {
            if (group != VORBIS_SETUP_GROUP) {
                var data = archive.getfile(group);
                if (data != null) {
                    var packet = new VorbisSound(data).method8502(null);
                    Files.write(directory.resolve(group + ".wav"), wav(packet));
                    written++;
                }
            }
        }
        System.out.println("wrote " + written + " Vorbis sounds to " + directory.toAbsolutePath().normalize());
    }

    private static void writeSongs(File cache, int archiveId, Path directory) throws Exception {
        Files.createDirectories(directory);
        var archive = Cache.js5(cache, archiveId);
        var written = 0;
        for (var group : Cache.groupsOf(Cache.index(cache, archiveId))) {
            var data = archive.getfile(group);
            if (data != null) {
                var song = new MidiSong(new Packet(data));
                Files.write(directory.resolve(group + ".mid"), song.midiData);
                written++;
            }
        }
        System.out.println("wrote " + written + " MIDI files to " + directory.toAbsolutePath().normalize());
    }

    private static void writePatches(File cache, Path directory) throws Exception {
        Files.createDirectories(directory);
        var archive = Cache.js5(cache, MIDI_INSTRUMENTS);
        var written = 0;
        for (var group : Cache.groupsOf(Cache.index(cache, MIDI_INSTRUMENTS))) {
            var data = archive.getfile(group);
            if (data != null) {
                var json = Json.write(patch(new MusicPatch(data)));
                Files.writeString(directory.resolve(group + ".json"), json, StandardCharsets.UTF_8);
                written++;
            }
        }
        System.out.println("wrote " + written + " instruments to " + directory.toAbsolutePath().normalize());
    }

    /**
     * An instrument as the client holds it ({@code MusicPatch}): its volume, and each of the 128
     * keys a note can strike, none where the key plays no sound, with the sound it plays by id and
     * whether that is a Vorbis or a synthesised sound (bit 0 of {@code soundIds}, less one, the id
     * above its two flags), its pitch offset, which is negative where the sound loops, its volume,
     * pan and exclusive group as the client keeps them, and the envelope it shares with other keys,
     * by its place in {@code envelopes}.
     */
    private static Map<String, Object> patch(MusicPatch patch) {
        var envelopes = new ArrayList<MusicPatchEnvelope>();
        var keys = new ArrayList<Object>();
        for (var key = 0; key < patch.soundIds.length; key++) {
            var packed = patch.soundIds[key];
            if (packed == 0) {
                keys.add(null);
            } else {
                var envelope = patch.envelopes[key];
                var at = envelopes.indexOf(envelope);
                if (at < 0) {
                    at = envelopes.size();
                    envelopes.add(envelope);
                }
                var fields = new LinkedHashMap<String, Object>();
                fields.put("sound", (packed - 1) >> 2);
                fields.put("vorbis", ((packed - 1) & 1) != 0);
                fields.put("pitchOffset", (int) patch.pitchOffsets[key]);
                fields.put("volume", (int) patch.volumes[key]);
                fields.put("pan", (int) patch.pans[key]);
                fields.put("exclusiveGroup", (int) patch.exclusiveGroups[key]);
                fields.put("envelope", at);
                keys.add(fields);
            }
        }
        var fields = new LinkedHashMap<String, Object>();
        fields.put("volume", patch.volume);
        fields.put("keys", keys);
        fields.put("envelopes", envelopes.stream().map(SoundExport::envelope).toList());
        return fields;
    }

    /**
     * A patch's envelope as the client holds it ({@code MusicPatchEnvelope}): its points as pairs of
     * signed bytes, none where it has none, and its decay, scaling and vibrato.
     */
    private static Map<String, Object> envelope(MusicPatchEnvelope envelope) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("envelope", bytes(envelope.envelope));
        fields.put("release", bytes(envelope.release));
        fields.put("decay", envelope.decay);
        fields.put("decayKeyScale", envelope.decayKeyScale);
        fields.put("envelopeKeyScale", envelope.envelopeKeyScale);
        fields.put("releaseKeyScale", envelope.releaseKeyScale);
        fields.put("vibratoRate", envelope.vibratoRate);
        fields.put("vibratoDepth", envelope.vibratoDepth);
        fields.put("vibratoDelay", envelope.vibratoDelay);
        return fields;
    }

    private static List<Integer> bytes(byte[] values) {
        if (values == null) {
            return null;
        }
        var list = new ArrayList<Integer>(values.length);
        for (var value : values) {
            list.add((int) value);
        }
        return list;
    }

    /**
     * A sound's samples as a WAV file: unsigned eight bit PCM, mono, at the sound's rate, with a
     * sample chunk of its loop where it has one, from its first sample to its last, forwards or,
     * where the client plays it so, forwards and back.
     */
    private static byte[] wav(VariableRateSoundPacket packet) {
        var samples = packet.data;
        var loopStart = packet.nominalBitRate;
        var loopEnd = packet.minBitRate;
        var looped = loopStart < loopEnd;
        var out = new ByteArrayOutputStream();
        var format = chunk("fmt ", 16)
            .putShort((short) 1)
            .putShort((short) 1)
            .putInt(packet.sampleRate)
            .putInt(packet.sampleRate)
            .putShort((short) 1)
            .putShort((short) 8);
        var data = new byte[samples.length];
        for (var i = 0; i < samples.length; i++) {
            data[i] = (byte) (samples[i] + 128);
        }
        var body = new ByteArrayOutputStream();
        body.writeBytes("WAVE".getBytes(StandardCharsets.US_ASCII));
        body.writeBytes(format.array());
        body.writeBytes(chunk("data", 0).putInt(4, data.length).array());
        body.writeBytes(data);
        if (data.length % 2 != 0) {
            body.write(0);
        }
        if (looped) {
            var nanosPerSample = 1_000_000_000 / packet.sampleRate;
            var sampler = chunk("smpl", 36 + 24)
                .putInt(0)
                .putInt(0)
                .putInt(nanosPerSample)
                .putInt(60)
                .putInt(0)
                .putInt(0)
                .putInt(0)
                .putInt(1)
                .putInt(0)
                .putInt(0)
                .putInt(packet.pingPong ? LOOP_ALTERNATING : LOOP_FORWARD)
                .putInt(loopStart)
                .putInt(loopEnd - 1)
                .putInt(0)
                .putInt(0);
            body.writeBytes(sampler.array());
        }
        var riff = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        riff.put("RIFF".getBytes(StandardCharsets.US_ASCII)).putInt(body.size());
        out.writeBytes(riff.array());
        out.writeBytes(body.toByteArray());
        return out.toByteArray();
    }

    /**
     * A chunk's header and room for its body, little-endian, the buffer placed after the header.
     */
    private static ByteBuffer chunk(String id, int size) {
        var buffer = ByteBuffer.allocate(8 + size).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(id.getBytes(StandardCharsets.US_ASCII)).putInt(size);
        return buffer;
    }
}
