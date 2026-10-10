import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.game.runetek6.config.npctype.NPCType;
import type.entity.BasKind;
import type.entity.NpcKind;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Writes one NPC type out of the cache: every field of the type as JSON, which names the models it
 * is made of and its base animation set, the set into the set library, {@code bas/<id>.json}, and
 * the models and the sequences of that set into the model and sequence libraries, each where its
 * library lacks it, from which an engine builds the NPC as the client does
 * ({@code NPCType.getModel}). With {@code --baked} it also writes the NPC's model baked as the
 * client builds it, with a morph target or a joint pose for every frame of the sequences it stands,
 * turns and moves with, and its head in a file of its own, the reference an engine's own building
 * is checked against.
 */
public final class NpcExport {

    private static final String SHADOW_NODE = "spot shadow";

    /**
     * How far above the NPC's origin the client draws its spot shadow, in its units.
     */
    private static final float SHADOW_ABOVE_NPC_FINE = 15.0F;

    private static final float FINE_PER_METRE = 512.0F;

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @ParametersDelegate
        private final TextureArgs textures = new TextureArgs();

        @Parameter(names = "--npc", description = "Which NPC to write, by its id", required = true)
        private int npc;

        @Parameter(
            names = "--npcs",
            description = "The directory the NPC types are kept in, beside the model and sequence libraries, relative to the export module when not absolute"
        )
        private Path npcs = Path.of("build", "npcs");

        @Parameter(
            names = "--baked",
            description = "A glTF file to also write the NPC's model into, baked and posed as the client builds it, with its head beside it, the reference an engine's own building is checked against"
        )
        private Path baked;

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

    /**
     * Writes the NPC's type data, and the models it names and the sequences its base animation set
     * names into their libraries where they lack them, each sequence's channels worked out on the
     * NPC.
     */
    private static void write(Args args, ClientNpcReader reader, NPCType type) throws Exception {
        var typeFile = args.npcs.resolve(type.id + ".json");
        Files.createDirectories(args.npcs);
        var archives = new CacheArchives(args.where.cache());
        Files.writeString(typeFile, Json.write(TypeFiles.of(args.where.cache(), new NpcKind(archives), type.id)),
            StandardCharsets.UTF_8);
        System.out.println("wrote " + typeFile.toAbsolutePath().normalize());
        var bas = reader.bas(type);
        if (bas.isPresent()) {
            var basFile = args.npcs.resolveSibling("bas").resolve(type.basId + ".json");
            if (!Files.exists(basFile)) {
                Files.createDirectories(basFile.getParent());
                Files.writeString(basFile, Json.write(TypeFiles.of(args.where.cache(), new BasKind(archives), type.basId)),
                    StandardCharsets.UTF_8);
                System.out.println("wrote " + basFile.toAbsolutePath().normalize());
            }
        }

        var textures = args.textures.library(reader.textures());
        var models = new ModelLibrary(reader.models(), textures, args.npcs.resolveSibling("models"));
        for (var model : type.models) {
            if (model != -1) {
                models.file(model);
            }
        }
        if (type.headModels != null) {
            for (var model : type.headModels) {
                models.file(model);
            }
        }
        var poser = reader.poser(type);
        var sequences = new SequenceLibrary(args.npcs.resolveSibling("sequences"));
        for (var movement : reader.movements(type)) {
            sequences.ensure(movement.sequence(), poser);
        }

        if (args.baked != null) {
            writeBaked(args, reader, type, args.baked);
        }
    }

    /**
     * Writes the NPC's model baked as the client builds it, posed by every sequence it moves with,
     * and its head beside it.
     */
    private static void writeBaked(Args args, ClientNpcReader reader, NPCType type, Path out) throws Exception {
        var poser = reader.poser(type);
        var baker = new PoseBaker(poser);
        var baked = new ArrayList<Baked>();
        for (var movement : reader.movements(type)) {
            baked.add(new Baked(movement, baker.bake(movement.sequence())));
        }

        var base = poser.still();
        var poses = baker.poses();
        var gltf = new GltfBuilder();
        var materials = new GltfMaterials(gltf, reader.textures(), args.textures.library(reader.textures()), out);
        var bones = Bones.of(baker);
        var result = bones.isPresent()
            ? ModelToGltf.convertSkinned(base, gltf, materials, poses, bones.get().joints().ofVertex())
            : ModelToGltf.convert(base, gltf, materials, poses);
        if (gltf.empty()) {
            throw new IllegalStateException("NPC " + args.npc + " has no face the client draws.");
        }

        if (result.targets()) {
            gltf.targetNames(baker.names());
        }
        var name = type.name + " (npc " + args.npc + ")";
        var node = new LinkedHashMap<String, Object>();
        node.put("name", name);
        node.put("mesh", gltf.mesh(name));
        var skin = bones.map(held -> SkinWriter.write(gltf, held.joints(), baker.skinning().orElseThrow()));
        skin.ifPresent(held -> {
            node.put("skin", held.number());
            node.put("children", held.childNodes());
        });
        var nodeNumber = gltf.node(node);
        var roots = new ArrayList<Integer>(List.of(nodeNumber));
        var shadow = SpotShadow.of(type, base, reader.toolkit());
        if (shadow.isPresent()) {
            roots.add(shadowNode(gltf, materials, shadow.get()));
        }

        for (var animation : baked) {
            if (!animation.clip().keys().isEmpty()) {
                var extras = Map.<String, Object>of("role", animation.movement().role());
                if (bones.isPresent()) {
                    SkinWriter.writeClip(gltf, animation.name(), animation.clip(), bones.get().joints(),
                        baker.framePoses(), baker.frames(), skin.orElseThrow(), nodeNumber,
                        result.targets() ? poses.size() : 0, extras);
                } else {
                    AnimationWriter.write(gltf, animation.name(), animation.clip(), poses.size(), List.of(nodeNumber),
                        extras);
                }
            }
        }

        GltfFile.write(out, gltf.document(roots), gltf.bin());

        System.out.println("wrote " + out.toAbsolutePath().normalize());
        System.out.println("  " + name + ", " + base.vertexCount + " vertices, " + result.faces() + " faces in "
            + result.primitives() + " primitives, " + (result.targets() ? poses.size() : 0) + " morph targets");
        System.out.println("  " + Bones.describe(baker, bones));
        for (var skip : result.skipped().entrySet()) {
            System.out.println("  " + skip.getValue() + " faces left out, " + skip.getKey());
        }
        for (var animation : baked) {
            System.out.println("  " + AnimationWriter.describe(animation.name(), animation.clip()));
        }

        var head = reader.head(type);
        if (head.isPresent()) {
            writeHead(args, reader, type, head.get(), out);
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

    /**
     * Adds the spot shadow's mesh and its node, which stands where the client draws the shadow
     * against the NPC: 20 units above the ground, where the NPC itself is drawn 5 units above it
     * (`NPCEntity.render`), so 15 units above the NPC's origin.
     */
    private static int shadowNode(GltfBuilder gltf, GltfMaterials materials, JavaModel shadow) {
        ModelToGltf.convertInto(gltf, materials, shadow);
        var node = new LinkedHashMap<String, Object>();
        node.put("name", SHADOW_NODE);
        node.put("mesh", gltf.mesh(SHADOW_NODE));
        node.put("translation", List.of(0.0F, SHADOW_ABOVE_NPC_FINE / FINE_PER_METRE, 0.0F));
        node.put("extras", Map.of("spotShadow", true));
        return gltf.node(node);
    }

    /**
     * Writes the head model to `<npc>.head.gltf` beside the NPC's own file, as one mesh that is not
     * posed.
     */
    private static void writeHead(Args args, ClientNpcReader reader, NPCType type, JavaModel head, Path out)
        throws Exception {
        var file = GltfFile.sibling(out, ".head.gltf");
        var gltf = new GltfBuilder();
        var materials = new GltfMaterials(gltf, reader.textures(), args.textures.library(reader.textures()), file);
        var result = ModelToGltf.convert(head, gltf, materials, List.of());
        var name = type.name + " head (npc " + type.id + ")";
        GltfFile.write(file, gltf.document(name), gltf.bin());
        System.out.println("wrote " + file.toAbsolutePath().normalize());
        System.out.println("  " + name + ", " + head.vertexCount + " vertices, " + result.faces() + " faces");
    }

    private NpcExport() {
        /* empty */
    }
}
