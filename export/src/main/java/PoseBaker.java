import com.jagex.AnimFrame;
import com.jagex.game.runetek6.config.seqtype.SeqType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final List<Skinning.FramePose> framePoses = new ArrayList<>();
    private final List<AnimFrame> frames = new ArrayList<>();
    private final List<String> names = new ArrayList<>();
    private Skinning skinning;
    private double worstDeviationFine;

    public PoseBaker(Poser poser) {
        this.poser = poser;
    }

    /**
     * The transform of each label in each pose kept so far, in the same order as the poses, or
     * nothing where no pose has been kept.
     */
    public Optional<Skinning> skinning() {
        return Optional.ofNullable(skinning);
    }

    public List<Skinning.FramePose> framePoses() {
        return List.copyOf(framePoses);
    }

    /** The frame of the sequence each pose was made from, as the client reads it, in the same order. */
    public List<AnimFrame> frames() {
        return List.copyOf(frames);
    }

    /**
     * How far, at most, a vertex moved by its label's transform lands from where the client put
     * it, over every pose kept, in the client's units.
     */
    public double worstDeviationFine() {
        return worstDeviationFine;
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
            var posed = poser.posed(animator);
            var pose = Pose.of(posed);
            if (skinning == null) {
                skinning = new Skinning(poser.unscaledStill(), poser.scale(),
                    posed.vertexLabels == null ? new int[0][] : posed.vertexLabels);
            }
            var read = animator.frame();
            var framePose = skinning.pose(read);
            var deviationFine = skinning.deviationFine(framePose, pose);
            worstDeviationFine = Math.max(worstDeviationFine, deviationFine);
            poses.add(pose);
            framePoses.add(framePose);
            frames.add(read);
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
