import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.game.runetek6.config.npctype.NPCType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Writes one NPC out of the cache as a binary glTF file: its model as the client builds it, with
 * a morph target for every frame of the sequences it stands, turns and moves with, and one
 * animation for each of those sequences.
 *
 * <p>An animation shows each frame for as long as the client does and then jumps to the next. The
 * client also tweens from one frame towards the next while it shows a frame, when the sequence
 * asks for that, and that is not written.
 */
public final class NpcExport {

    /**
     * How long one of the client's cycles is. The client runs its logic every
     * {@code GameShell.logicUpdateInterval}, which is 20 ms, and {@code Static50.animationTick}
     * moves an NPC's movement animator on by one cycle each time.
     */
    private static final float SECONDS_PER_CYCLE = 0.02F;

    private static final String STEP = "STEP";

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(names = "--npc", description = "Which NPC to write, by its id", required = true)
        private int npc;

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportNpc", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var reader = new ClientNpcReader(args.where.cache());
        var type = reader.type(args.npc);

        if (type.multinpcs != null) {
            throw new IllegalStateException("NPC " + args.npc + " looks like one of the NPCs "
                + Arrays.toString(type.multinpcs) + " as a variable chooses. Write one of those instead.");
        } else if (type.models.length == 0) {
            throw new IllegalStateException("NPC " + args.npc + " has no model.");
        } else {
            write(args, reader, type);
        }
    }

    private static void write(Args args, ClientNpcReader reader, NPCType type) throws Exception {
        var baker = new PoseBaker(reader, type);
        var clips = new ArrayList<PoseBaker.Clip>();
        for (var movement : reader.movements(type)) {
            clips.add(baker.bake(movement));
        }

        var base = reader.unanimated(type);
        var poses = baker.poses();
        var result = ModelToGltf.convert(base, reader.textures(), poses);
        if (result.gltf().empty()) {
            throw new IllegalStateException("NPC " + args.npc + " has no face the client draws.");
        }

        var gltf = result.gltf();
        gltf.targetNames(baker.names());
        for (var clip : clips) {
            if (!clip.keys().isEmpty()) {
                addAnimation(gltf, clip, poses.size());
            }
        }

        var name = type.name + " (npc " + args.npc + ")";
        var out = args.out == null ? Path.of("build", "npcs", args.npc + ".glb") : args.out;
        Glb.write(out, gltf.json(name, extras(args.npc, type)), gltf.bin());

        System.out.println("wrote " + out.toAbsolutePath().normalize());
        System.out.println("  " + name + ", " + base.vertexCount + " vertices, " + result.faces() + " faces in "
            + result.primitives() + " primitives, " + poses.size() + " morph targets");
        for (var skip : result.skipped().entrySet()) {
            System.out.println("  " + skip.getValue() + " faces left out, " + skip.getKey());
        }
        for (var clip : clips) {
            System.out.println("  " + describe(clip));
        }
    }

    /**
     * Adds an animation that shows one frame of a sequence at a time, each from the moment the
     * client moves on to it. A last key at the end of the sequence holds the last frame until the
     * animation loops, so the animation lasts as long as the sequence does.
     */
    private static void addAnimation(GltfBuilder gltf, PoseBaker.Clip clip, int targets) {
        var keys = clip.keys();
        var times = new float[keys.size() + 1];
        var weights = new float[times.length * targets];
        var cycles = 0;

        for (var key = 0; key < keys.size(); key++) {
            times[key] = cycles * SECONDS_PER_CYCLE;
            weights[key * targets + keys.get(key).target()] = 1.0F;
            cycles += keys.get(key).cycles();
        }
        times[keys.size()] = cycles * SECONDS_PER_CYCLE;
        weights[keys.size() * targets + keys.getLast().target()] = 1.0F;

        var input = gltf.animationData(times, "SCALAR", 1, true);
        var output = gltf.animationData(weights, "SCALAR", 1, false);
        gltf.weightAnimation(animationName(clip), input, output, STEP, animationExtras(clip));
    }

    private static String animationName(PoseBaker.Clip clip) {
        return clip.movement().role() + " " + clip.movement().sequence();
    }

    /**
     * What an engine needs to play the sequence the way the client does, beyond its frames. A
     * sequence that loops over only its last few frames plays the ones before them once.
     */
    private static Map<String, Object> animationExtras(PoseBaker.Clip clip) {
        var sequence = clip.sequence();
        var extras = new LinkedHashMap<String, Object>();
        extras.put("role", clip.movement().role());
        extras.put("sequence", sequence.id);
        extras.put("tweened", sequence.tweened);

        if (sequence.loopOffset > 0 && sequence.loopOffset <= sequence.frames.length) {
            var loopStart = sequence.frames.length - sequence.loopOffset;
            var cycles = 0;
            for (var frame = 0; frame < loopStart; frame++) {
                cycles += sequence.frameDurations[frame];
            }
            extras.put("loopStart", cycles * SECONDS_PER_CYCLE);
        }

        return extras;
    }

    private static Map<String, Object> extras(int id, NPCType type) {
        var extras = new LinkedHashMap<String, Object>();
        extras.put("npc", id);
        extras.put("name", type.name);
        extras.put("size", type.size);
        if (type.basId != -1) {
            extras.put("bas", type.basId);
        }
        return extras;
    }

    private static String describe(PoseBaker.Clip clip) {
        var name = animationName(clip);
        var sequence = clip.sequence();

        if (clip.keys().isEmpty()) {
            return name + ": no frame the client shows, left out";
        } else {
            var durations = clip.keys().stream().map(PoseBaker.Key::cycles).toList();
            var cycles = durations.stream().mapToInt(Integer::intValue).sum();
            return name + ": " + clip.keys().size() + " frames, " + cycles + " cycles ("
                + Math.round(cycles * SECONDS_PER_CYCLE * 1000) + " ms), frame cycles " + durations
                + (sequence.tweened ? ", tweened in the client" : "");
        }
    }

    private NpcExport() {
        /* empty */
    }
}
