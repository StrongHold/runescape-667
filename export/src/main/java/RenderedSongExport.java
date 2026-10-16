import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.sound.Audio;
import com.jagex.js5.Js5Archive;
import com.jagex.sound.MixBuss;
import com.jagex.sound.QueueBuss;
import com.jagex.sound.SoundCache;
import com.jagex.sound.midi.MidiSong;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes the start of a song as the client's own synthesiser plays it ({@code MixBuss}), in
 * stereo at the client's rate of 22050 samples a second, at the mixer's whole volume: a WAV file
 * of sixteen bit PCM, each sample the mix less its lowest eight bits and held to sixteen, as the
 * client writes it to the sound card ({@code PcmPlayer_Sub1}). It is the reference another
 * synthesiser of the export's songs and instruments is compared with.
 */
public final class RenderedSongExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(names = "--song", description = "The song's id, its group in the songs archive", required = true)
        private int song;

        @Parameter(names = "--seconds", description = "How many seconds of the song to write")
        private int seconds = 20;

        @Parameter(
            names = "--out",
            description = "The directory to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "rendered-songs");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /**
     * The archive of the music's instruments ({@code MixBuss.readPatch}).
     */
    private static final int MIDI_INSTRUMENTS = 15;

    private static final int SAMPLE_RATE = 22050;

    /**
     * How many samples the mixer fills at a time, as the client's player does ({@code PcmPlayer.fill}).
     */
    private static final int BLOCK = 256;

    private RenderedSongExport() {
        /* empty */
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportRenderedSong", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        Audio.sampleRate = SAMPLE_RATE;
        QueueBuss.stereo = true;
        var song = new MidiSong(new Packet(Cache.js5(cache, Js5Archive.MIDI_SONGS).getfile(args.song)));
        var sounds = new SoundCache(Cache.js5(cache, Js5Archive.SYNTH_SOUNDS), Cache.js5(cache, Js5Archive.VORBIS));
        var instruments = Cache.js5(cache, MIDI_INSTRUMENTS);
        var mixer = new MixBuss();
        mixer.method929();
        while (!mixer.method944(sounds, instruments, song)) {
            Thread.onSpinWait();
        }
        mixer.method934(song, false);

        var total = args.seconds * SAMPLE_RATE;
        var pcm = ByteBuffer.allocate(total * 4).order(ByteOrder.LITTLE_ENDIAN);
        var mix = new int[BLOCK * 2];
        for (var at = 0; at < total; at += BLOCK) {
            var length = Math.min(BLOCK, total - at);
            java.util.Arrays.fill(mix, 0);
            mixer.fill(mix, 0, length);
            for (var i = 0; i < length * 2; i++) {
                var sample = mix[i] >> 8;
                pcm.putShort((short) Math.max(-32768, Math.min(32767, sample)));
            }
        }

        Files.createDirectories(args.out);
        var file = args.out.resolve(args.song + ".wav");
        Files.write(file, wav(pcm.array()));
        System.out.println("wrote " + args.seconds + " seconds of song " + args.song + " to " + file.toAbsolutePath().normalize());
    }

    private static byte[] wav(byte[] pcm) {
        var header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN);
        header.put("RIFF".getBytes(StandardCharsets.US_ASCII)).putInt(36 + pcm.length);
        header.put("WAVE".getBytes(StandardCharsets.US_ASCII));
        header.put("fmt ".getBytes(StandardCharsets.US_ASCII)).putInt(16)
            .putShort((short) 1).putShort((short) 2).putInt(SAMPLE_RATE).putInt(SAMPLE_RATE * 4)
            .putShort((short) 4).putShort((short) 16);
        header.put("data".getBytes(StandardCharsets.US_ASCII)).putInt(pcm.length);
        var out = new ByteArrayOutputStream();
        out.writeBytes(header.array());
        out.writeBytes(pcm);
        return out.toByteArray();
    }
}
