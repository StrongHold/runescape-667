import com.jagex.AnimBase;
import com.jagex.AnimFrame;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The sequences every type shares, one glTF file for each sequence in one directory, which binds
 * to any model of the model library by the names of its labels.
 *
 * <p>The file holds a node for each label the sequence's frames move, named {@code label <n>} as a
 * model's joints are, and one animation. The animation carries the extension
 * {@code RS_client_frames}, which is what the client plays: each frame it shows, for how many
 * cycles, and the frame's transforms as the client reads them, six numbers each (the group, x, y
 * and z, the pivot group it applies first or -1, and its tween bits), with the base that numbers
 * the groups, whether the client tweens the sequence, and how many frames from the end it loops
 * back to. An engine replays those on the model, as the client does.
 *
 * <p>The animation's own channels move each label's node by steps, one key a frame, for a tool
 * that knows nothing of the extension. A frame turns and scales a label about the centre of the
 * vertices it names in the posed model, so no transform of a node is right for every model: the
 * channels are worked out on the model the sequence is first written for, and are exact for that
 * model alone. A sequence that moves no vertex, as one that only fades faces does, has a channel
 * that holds its first node still, as glTF asks every animation for one.
 */
public final class SequenceLibrary {

    /**
     * The version of the format of a sequence file, which its {@code asset} and its extension
     * carry.
     */
    public static final int VERSION = 1;

    public static final String EXTENSION = "RS_client_frames";

    private static final int TRANSFORM_NUMBERS = 6;
    private static final String STEP = "STEP";
    private static final float STILL = 1e-6F;
    private static final float[] IDENTITY_MOVE = {0, 0, 0};
    private static final float[] IDENTITY_TURN = {0, 0, 0, 1};
    private static final float[] IDENTITY_SCALE = {1, 1, 1};

    private final Path directory;

    public SequenceLibrary(Path directory) {
        this.directory = directory;
    }

    public static Path defaultDirectory() {
        return Path.of("build", "sequences");
    }

    /**
     * Writes a sequence's file where the library does not hold it yet, with its channels worked
     * out on the model a poser builds.
     */
    public void ensure(int sequence, Poser poser) {
        var file = directory.resolve(sequence + ".gltf");
        if (!Files.exists(file)) {
            write(sequence, poser, file);
        }
    }

    /**
     * Writes one sequence's file, which holds nothing but its first node where the client shows
     * none of its frames.
     */
    public void write(int sequence, Poser poser, Path file) {
        var baker = new PoseBaker(poser);
        var clip = baker.bake(sequence);
        var frames = baker.frames();
        var framePoses = baker.framePoses();
        var base = clip.keys().isEmpty() ? null : frames.get(clip.keys().getFirst().target()).base;

        var gltf = new GltfBuilder();
        gltf.formatVersion(VERSION);
        var labels = labelsOf(base);
        var nodes = new ArrayList<Integer>();
        for (var label : labels) {
            nodes.add(gltf.node(Map.of("name", "label " + label)));
        }
        if (nodes.isEmpty()) {
            nodes.add(gltf.node(Map.of("name", "still")));
        }

        var input = gltf.animationData(AnimationWriter.keyTimes(clip), "SCALAR", 1, true);
        var samplers = new ArrayList<Map<String, Object>>();
        var channels = new ArrayList<Map<String, Object>>();
        for (var index = 0; index < labels.size(); index++) {
            var label = labels.get(index);
            var count = clip.keys().size() + 1;
            var translations = new float[count * 3];
            var rotations = new float[count * 4];
            var scales = new float[count * 3];
            for (var key = 0; key < count; key++) {
                var target = clip.keys().get(Math.min(key, clip.keys().size() - 1)).target();
                var trs = SkinWriter.Trs.of(framePoses.get(target).of(label));
                System.arraycopy(trs.translation(), 0, translations, key * 3, 3);
                System.arraycopy(trs.rotation(), 0, rotations, key * 4, 4);
                System.arraycopy(trs.scale(), 0, scales, key * 3, 3);
            }
            var node = nodes.get(index);
            track(gltf, samplers, channels, node, "translation", input, translations, IDENTITY_MOVE);
            track(gltf, samplers, channels, node, "rotation", input, rotations, IDENTITY_TURN);
            track(gltf, samplers, channels, node, "scale", input, scales, IDENTITY_SCALE);
        }
        if (channels.isEmpty()) {
            var still = new float[(clip.keys().size() + 1) * 3];
            channel(gltf, samplers, channels, nodes.getFirst(), "translation", input,
                gltf.animationData(still, "VEC3", 3, false));
        }

        gltf.animation("sequence " + sequence, samplers, channels, Map.of(),
            Map.of(EXTENSION, frames(clip, frames, base)));
        var document = gltf.document(nodes, "sequence " + sequence, Map.of());
        try {
            GltfFile.write(file, document, gltf.bin());
        } catch (IOException failure) {
            throw new UncheckedIOException("Could not write sequence " + sequence + " to " + file, failure);
        }
    }

    /**
     * Every label a group of the base names, in order.
     */
    private static List<Integer> labelsOf(AnimBase base) {
        var labels = new TreeSet<Integer>();
        if (base != null) {
            for (var group : Skinning.groupLabels(base)) {
                labels.addAll(group);
            }
        }
        return List.copyOf(labels);
    }

    /**
     * Adds a channel of a node, unless every key of it is what the node holds anyway.
     */
    private static void track(GltfBuilder gltf, List<Map<String, Object>> samplers,
                              List<Map<String, Object>> channels, int node, String path, int input, float[] values,
                              float[] identity) {
        var still = true;
        for (var i = 0; i < values.length; i++) {
            still &= Math.abs(values[i] - identity[i % identity.length]) < STILL;
        }
        if (!still) {
            var type = identity.length == 4 ? "VEC4" : "VEC3";
            channel(gltf, samplers, channels, node, path, input, gltf.animationData(values, type, identity.length, false));
        }
    }

    private static void channel(GltfBuilder gltf, List<Map<String, Object>> samplers,
                                List<Map<String, Object>> channels, int node, String path, int input, int output) {
        samplers.add(Map.of("input", input, "output", output, "interpolation", STEP));
        channels.add(Map.of("sampler", samplers.size() - 1, "target", Map.of("node", node, "path", path)));
    }

    /**
     * The extension's content: what the client plays, as it reads it from the cache. Every frame
     * of a sequence shares one base, as {@link SkinWriter} checks.
     */
    private static Map<String, Object> frames(PoseBaker.Clip clip, List<AnimFrame> frames, AnimBase base) {
        var sequence = clip.sequence();
        var keys = new ArrayList<Map<String, Object>>();
        for (var key : clip.keys()) {
            var frame = frames.get(key.target());
            if (frame.base != base) {
                throw new IllegalStateException("Sequence " + sequence.id + " has frames of two bases.");
            }
            var raw = Skinning.rawTransforms(frame);
            var transforms = new ArrayList<List<Integer>>();
            for (var start = 0; start < raw.size(); start += TRANSFORM_NUMBERS) {
                transforms.add(raw.subList(start, start + TRANSFORM_NUMBERS));
            }
            var entry = new LinkedHashMap<String, Object>();
            entry.put("cycles", key.cycles());
            entry.put("transforms", transforms);
            keys.add(entry);
        }

        var described = new LinkedHashMap<String, Object>();
        described.put("version", VERSION);
        described.put("sequence", sequence.id);
        described.put("tweened", sequence.tweened);
        described.put("loopOffset", sequence.loopOffset);
        var groups = new LinkedHashMap<String, Object>();
        groups.put("types", base == null ? List.of() : Skinning.groupTypes(base));
        groups.put("labels", base == null ? List.of() : Skinning.groupLabels(base));
        groups.put("shadowed", base == null ? List.of() : Skinning.groupShadowed(base));
        described.put("base", groups);
        described.put("keys", keys);
        return described;
    }
}
