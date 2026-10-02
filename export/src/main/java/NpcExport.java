import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.game.runetek6.config.npctype.NPCType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes one NPC out of the cache as a binary glTF file: its model as the client builds it, with
 * a morph target for every frame of the sequences it stands, turns and moves with, and one
 * animation for each of those sequences, as {@link AnimationWriter} writes them.
 */
public final class NpcExport {

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
        var poser = reader.poser(type);
        var baker = new PoseBaker(poser);
        var baked = new ArrayList<Baked>();
        for (var movement : reader.movements(type)) {
            baked.add(new Baked(movement, baker.bake(movement.sequence())));
        }

        var base = poser.still();
        var poses = baker.poses();
        var result = ModelToGltf.convert(base, reader.textures(), poses);
        if (result.gltf().empty()) {
            throw new IllegalStateException("NPC " + args.npc + " has no face the client draws.");
        }

        var gltf = result.gltf();
        gltf.targetNames(baker.names());
        var node = List.of(0);
        for (var animation : baked) {
            if (!animation.clip().keys().isEmpty()) {
                AnimationWriter.write(gltf, animation.name(), animation.clip(), poses.size(), node,
                    Map.of("role", animation.movement().role()));
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
        for (var animation : baked) {
            System.out.println("  " + AnimationWriter.describe(animation.name(), animation.clip()));
        }
    }

    /**
     * One sequence of the NPC's base animation set, baked, and named after what the set uses it
     * for and the sequence's id.
     */
    private record Baked(ClientNpcReader.Movement movement, PoseBaker.Clip clip) {

        private String name() {
            return movement.role() + " " + movement.sequence();
        }
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

    private NpcExport() {
        /* empty */
    }
}
