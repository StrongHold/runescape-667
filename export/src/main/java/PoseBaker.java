import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.seqtype.SeqType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Poses an NPC at every frame of the sequences it moves with, and keeps each distinct pose once.
 *
 * A sequence names each of its frames by the frameset that holds it and its place there, and
 * several sequences often share frames, so a pose is kept for each frame named rather than for
 * each frame of each sequence.
 */
final class PoseBaker {

    private final ClientNpcReader reader;
    private final NPCType type;
    private final Map<Integer, Integer> targets = new LinkedHashMap<>();
    private final List<Pose> poses = new ArrayList<>();
    private final List<String> names = new ArrayList<>();

    PoseBaker(ClientNpcReader reader, NPCType type) {
        this.reader = reader;
        this.type = type;
    }

    /**
     * Every frame of a sequence that the client shows for at least one cycle, each with the pose
     * it is kept as.
     */
    Clip bake(ClientNpcReader.Movement movement) {
        var animator = new SequenceAnimator(movement.sequence());
        var sequence = animator.sequence();
        var keys = new ArrayList<Key>();

        if (sequence.frames != null) {
            for (var frame = 0; frame < sequence.frames.length; frame++) {
                var cycles = sequence.frameDurations[frame];
                if (cycles > 0) {
                    keys.add(new Key(target(animator, frame), cycles));
                }
            }
        }

        return new Clip(movement, sequence, List.copyOf(keys));
    }

    /**
     * Each pose kept so far, in the order its morph target is numbered.
     */
    List<Pose> poses() {
        return List.copyOf(poses);
    }

    List<String> names() {
        return List.copyOf(names);
    }

    private int target(SequenceAnimator animator, int frame) {
        var named = animator.sequence().frames[frame];
        var held = targets.get(named);

        if (held != null) {
            return held;
        } else if (!animator.show(frame)) {
            throw new IllegalStateException("Frame " + frame + " of sequence " + animator.sequence().id
                + " is frame " + (named & 0xFFFF) + " of frameset " + (named >>> 16)
                + ", which the cache does not hold.");
        } else {
            poses.add(Pose.of(reader.posed(type, animator)));
            names.add("frameset " + (named >>> 16) + " frame " + (named & 0xFFFF));
            targets.put(named, poses.size() - 1);
            return poses.size() - 1;
        }
    }

    /**
     * The frames of one sequence that are shown, in order.
     */
    record Clip(ClientNpcReader.Movement movement, SeqType sequence, List<Key> keys) {
    }

    /**
     * One frame of a sequence: the morph target that holds its pose, and how many of the client's
     * cycles it is shown for.
     */
    record Key(int target, int cycles) {
    }
}
