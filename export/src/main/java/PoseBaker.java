import com.jagex.game.runetek6.config.seqtype.SeqType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Poses a model at every frame of the sequences it plays, and keeps each distinct pose once.
 *
 * A sequence names each of its frames by the frameset that holds it and its place there, and
 * several sequences often share frames, so a pose is kept for each frame named rather than for
 * each frame of each sequence.
 */
public final class PoseBaker {

    private final Poser poser;
    private final Map<Integer, Integer> targets = new LinkedHashMap<>();
    private final List<Pose> poses = new ArrayList<>();
    private final List<String> names = new ArrayList<>();

    public PoseBaker(Poser poser) {
        this.poser = poser;
    }

    /**
     * Every frame of a sequence that the client shows for at least one cycle, each with the pose
     * it is kept as.
     */
    public Clip bake(int sequenceId) {
        var animator = new SequenceAnimator(sequenceId);
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

        return new Clip(sequence, List.copyOf(keys));
    }

    /**
     * Each pose kept so far, in the order its morph target is numbered.
     */
    public List<Pose> poses() {
        return List.copyOf(poses);
    }

    /**
     * The name of each pose kept so far, in the same order.
     */
    public List<String> names() {
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
            poses.add(Pose.of(poser.posed(animator)));
            names.add("frameset " + (named >>> 16) + " frame " + (named & 0xFFFF));
            targets.put(named, poses.size() - 1);
            return poses.size() - 1;
        }
    }

    /**
     * The frames of one sequence that are shown, in order.
     */
    public record Clip(SeqType sequence, List<Key> keys) {
    }

    /**
     * One frame of a sequence: the morph target that holds its pose, and how many of the client's
     * cycles it is shown for.
     */
    public record Key(int target, int cycles) {
    }
}
