package type.entity;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The sequences ({@code SeqTypeList}), each written as JSON beside its glTF file in the sequence
 * library where that has one: its frames, each the frame set's group in the high 16 bits and the
 * frame's file in the low, as the client packs them, with how many cycles each shows, the
 * secondary frames, the labels it blends, its sounds, priorities, loops and the objects it shows
 * in a player's hands. The type list's {@code postDecode} gives {@code walkingPrecedence} and
 * {@code animatingPrecedence} a default where the entry leaves them -1: 2 where the sequence blends
 * labels, else 0. The file holds them as the entry gives them. The decoder reads nothing for code
 * 16, which the file holds as {@code ignored16}, whether the entry holds it.
 */
public final class SeqKind implements ConfigKind<SeqType> {

    private static final int FILE_BITS = 7;

    /**
     * Where a frame's packed sound keeps the sound, its loops and its range (`SoundManager`).
     */
    private static final int SOUND_SHIFT = 8;
    private static final int LOOPS_SHIFT = 5;
    private static final int LOOPS_MASK = 0x7;
    private static final int RANGE_MASK = 0x1F;

    private static final Codes CODES = Codes.of()
        .code(1, "frameDurations", "frames")
        .code(2, "loopOffset")
        .code(3, "blendFlags")
        .code(5, "priority")
        .code(6, "playerLeftHand")
        .code(7, "playerRightHand")
        .code(8, "maxLoops")
        .code(9, "animatingPrecedence")
        .code(10, "walkingPrecedence")
        .code(11, "replayMode")
        .code(12, "secondaryFrames")
        .code(13, "soundInfo")
        .code(14, "rotateNormals")
        .code(15, "tweened")
        .code(16, "ignored16")
        .code(18, "vorbisSound")
        .code(19, "soundVolumes")
        .code(20, "soundRateMin", "soundRateMax");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "id", "The sequence's own id, which the file's name gives."
    );

    @Override
    public String directory() {
        return "sequences";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.everyGroup(Js5Archive.CONFIG_SEQ, FILE_BITS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public SeqType create(int id) {
        var type = new SeqType();
        type.id = id;
        return type;
    }

    @Override
    public void decode(SeqType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public void postDecode(SeqType type, int id) {
        type.postDecode();
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 16) {
            captured.put("ignored16", true);
        }
    }

    @Override
    public Map<String, Object> json(Decoded<SeqType> type) {
        var written = Fields.written(this, type);
        written.put("soundInfo", soundInfo(type.decoded().soundInfo));
        written.putIfAbsent("ignored16", false);
        return written;
    }

    /**
     * The sound each frame plays, none where the sequence plays none, null for a frame that plays none, as the client reads it
     * ({@code SoundManager}): the first number packs the sound in its high bits, how many times it
     * loops in bits 5 to 7, and in its low 5 bits the range in tiles another player hears it at, 0
     * for a sound only the player who plays it hears. The client plays that sound or one of the
     * numbers after it, at random. Each is written as {@code sounds}, the first and the others,
     * {@code loops} and {@code range}.
     */
    private static List<Map<String, Object>> soundInfo(int[][] info) {
        var frames = new ArrayList<Map<String, Object>>();
        for (var frame : info == null ? new int[0][] : info) {
            if (frame == null) {
                frames.add(null);
            } else {
                var sounds = new ArrayList<Integer>();
                sounds.add(frame[0] >> SOUND_SHIFT);
                for (var i = 1; i < frame.length; i++) {
                    sounds.add(frame[i]);
                }
                var sound = new LinkedHashMap<String, Object>();
                sound.put("sounds", sounds);
                sound.put("loops", frame[0] >> LOOPS_SHIFT & LOOPS_MASK);
                sound.put("range", frame[0] & RANGE_MASK);
                frames.add(sound);
            }
        }
        return frames;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }

    @Override
    public Set<String> settled() {
        return Set.of("walkingPrecedence", "animatingPrecedence");
    }
}
