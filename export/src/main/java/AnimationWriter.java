import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes a baked sequence as a glTF animation of the weights of a mesh's morph targets.
 *
 * <p>The animation shows one frame at a time, each from the moment the client moves on to it, and
 * then jumps to the next. A last key at the end of the sequence holds the last frame until the
 * animation loops, so the animation lasts as long as the sequence does. The client also tweens
 * from one frame towards the next while it shows a frame, when the sequence asks for that, and
 * that is not written.
 */
public final class AnimationWriter {

    /**
     * How long one of the client's cycles is. The client runs its logic every
     * {@code GameShell.logicUpdateInterval}, which is 20 ms, and every animator the client draws
     * by is moved on by one cycle each time.
     */
    public static final float SECONDS_PER_CYCLE = 0.02F;

    private static final String STEP = "STEP";

    /**
     * Adds an animation that plays a clip on every node named, each of which wears a mesh with
     * the clip's morph targets.
     *
     * @param targets how many morph targets the mesh has, which every key sets the weight of.
     * @param extras what the animation carries beyond its keys, to which the sequence's own
     *     details are added.
     */
    public static void write(GltfBuilder gltf, String name, PoseBaker.Clip clip, int targets, List<Integer> nodes,
                             Map<String, Object> extras) {
        var sampler = weightsSampler(gltf, clip, targets);
        gltf.weightAnimation(name, sampler.input(), sampler.output(), STEP, nodes, sequenceExtras(clip, extras));
    }

    /**
     * The accessors of a sampler that sets the weight of one target at a time through a clip.
     *
     * @param input the key times.
     * @param output every target's weight at each key time.
     */
    public record Sampler(int input, int output) {
    }

    public static Sampler weightsSampler(GltfBuilder gltf, PoseBaker.Clip clip, int targets) {
        var keys = clip.keys();
        var times = keyTimes(clip);
        var weights = new float[times.length * targets];
        for (var key = 0; key < keys.size(); key++) {
            weights[key * targets + keys.get(key).target()] = 1.0F;
        }
        weights[keys.size() * targets + keys.getLast().target()] = 1.0F;
        return new Sampler(gltf.animationData(times, "SCALAR", 1, true), gltf.animationData(weights, "SCALAR", 1, false));
    }

    /**
     * When each key of a clip starts, in seconds, with a last key at the end of the clip that
     * holds the last frame until the clip loops.
     */
    public static float[] keyTimes(PoseBaker.Clip clip) {
        var keys = clip.keys();
        var times = new float[keys.size() + 1];
        var cycles = 0;
        for (var key = 0; key < keys.size(); key++) {
            times[key] = cycles * SECONDS_PER_CYCLE;
            cycles += keys.get(key).cycles();
        }
        times[keys.size()] = cycles * SECONDS_PER_CYCLE;
        return times;
    }

    /**
     * What an engine needs to play the sequence the way the client does, beyond its frames. A
     * sequence that loops over only its last few frames plays the ones before them once.
     */
    public static Map<String, Object> sequenceExtras(PoseBaker.Clip clip, Map<String, Object> given) {
        var sequence = clip.sequence();
        var extras = new LinkedHashMap<String, Object>(given);
        extras.put("sequence", sequence.id);
        extras.put("tweened", sequence.tweened);
        extras.put("loopOffset", sequence.loopOffset);

        if (sequence.loopOffset > 0 && sequence.loopOffset <= sequence.frames.length) {
            var loopStartFrame = sequence.frames.length - sequence.loopOffset;
            var cycles = 0;
            for (var frame = 0; frame < loopStartFrame; frame++) {
                cycles += sequence.frameDurations[frame];
            }
            extras.put("loopStartSeconds", cycles * SECONDS_PER_CYCLE);
        }

        return extras;
    }

    /**
     * How a clip is described in the report: its frames, its length, and how the client plays it.
     */
    public static String describe(String name, PoseBaker.Clip clip) {
        var sequence = clip.sequence();

        if (clip.keys().isEmpty()) {
            return name + ": no frame the client shows, left out";
        } else {
            var durations = clip.keys().stream().map(PoseBaker.Key::cycles).toList();
            var cycles = durations.stream().mapToInt(Integer::intValue).sum();
            return name + ": " + clip.keys().size() + " frames, " + cycles + " cycles ("
                + Math.round(cycles * SECONDS_PER_CYCLE * 1000) + " ms), " + frameCycles(durations)
                + (sequence.tweened ? ", tweened in the client" : "");
        }
    }

    /**
     * How long each frame is shown, as one number where every frame is shown for the same time.
     */
    private static String frameCycles(List<Integer> durations) {
        var distinct = durations.stream().distinct().count();
        return distinct == 1 ? durations.getFirst() + " cycles a frame" : "frame cycles " + durations;
    }

    private AnimationWriter() {
        /* empty */
    }
}
