import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.npctype.NPCType;

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

    /**
     * How many of the options on the mini menu a type's data can set (`NPCType.decode`, opcodes 30
     * to 34 and 150 to 154).
     */
    private static final int OPTION_SLOTS = 5;

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
        Files.writeString(typeFile, Json.write(typeData(type)), StandardCharsets.UTF_8);
        System.out.println("wrote " + typeFile.toAbsolutePath().normalize());
        var bas = reader.bas(type);
        if (bas.isPresent()) {
            var basFile = args.npcs.resolveSibling("bas").resolve(type.basId + ".json");
            if (!Files.exists(basFile)) {
                Files.createDirectories(basFile.getParent());
                Files.writeString(basFile, Json.write(basData(bas.get())), StandardCharsets.UTF_8);
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

    /**
     * Every field of the NPC type, under the name the client gives it: the models it names, the
     * colour and texture swaps, the translations, the scales, the lighting and the tint its model is
     * built with, and its base animation set by id, {@code bas}, which the set library holds.
     */
    private static Map<String, Object> typeData(NPCType type) {
        var data = new LinkedHashMap<String, Object>();
        data.put("npc", type.id);
        data.put("name", type.name);
        data.put("size", type.size);
        if (type.basId != -1) {
            data.put("bas", type.basId);
        }
        data.put("interactive", type.interactive);
        data.put("ops", TypeJson.options(type.op, OPTION_SLOTS));
        data.put("pickSizeShift", type.pickSizeShift);
        data.put("quickPick", type.quickPick);
        data.put("models", TypeJson.ints(type.models));
        data.put("headModels", TypeJson.ints(type.headModels));
        data.put("recolours", TypeJson.swaps(type.recol_s, type.recol_d));
        data.put("recolourPalette", TypeJson.bytes(type.recol_d_palette));
        data.put("retextures", TypeJson.swaps(type.retex_s, type.retex_d));
        data.put("translations", translations(type));
        data.put("scaleH", type.scaleH);
        data.put("scaleV", type.scaleV);
        data.put("ambient", type.ambient);
        data.put("diffusion", type.diffusion);
        data.put("tint", List.of((int) type.colourHue, (int) type.colourSaturation, (int) type.colourLightness,
            type.colourScale & 0xFF));
        data.put("hasShadow", type.hasShadow);
        data.put("shadowInnerColour", type.shadowInnerColour & 0xFFFF);
        data.put("shadowOuterColour", type.shadowOuterColour & 0xFFFF);
        data.put("shadowInnerAlpha", (int) type.shadowInnerAlpha);
        data.put("shadowOuterAlpha", (int) type.shadowOuterAlpha);
        data.put("height", type.height);
        data.put("spawnDirection", (int) type.spawnDirection);
        data.put("yawSpeed", type.yawSpeed);
        data.put("crawl", type.crawl);
        data.put("movementCapabilities", (int) type.movementCapabilities);
        data.put("combatLevel", type.combatLevel);
        data.put("displayOnMiniMap", type.displayOnMiniMap);
        data.put("mapElement", type.mapElement);
        data.put("headIcon", type.headIcon);
        data.put("healthBarSprite", type.healthBarSprite);
        data.put("timerbarSprite", type.timerbarSprite);
        data.put("mobilisingArmiesIcon", type.mobilisingArmiesIcon);
        data.put("renderHighPriority", type.renderHighPriority);
        data.put("renderLowPriority", type.renderLowPriority);
        data.put("lowPriorityAttackOps", (int) type.lowPriorityAttackOps);
        data.put("isFollower", type.isFollower);
        data.put("cursor1Op", type.cursor1Op);
        data.put("cursor1", type.cursor1);
        data.put("cursor2Op", type.cursor2Op);
        data.put("cursor2", type.cursor2);
        data.put("attackCursor", type.attackCursor);
        data.put("readySound", type.readySound);
        data.put("crawlSound", type.crawlSound);
        data.put("walkSound", type.walkSound);
        data.put("runSound", type.runSound);
        data.put("soundRangeMin", type.soundRangeMin);
        data.put("soundRangeMax", type.soundRangeMax);
        data.put("soundVolume", type.soundVolume);
        data.put("soundRateMin", type.soundRateMin);
        data.put("soundRateMax", type.soundRateMax);
        data.put("vorbis", type.vorbis);
        data.put("multinpcVarbit", type.multinpcVarbit);
        data.put("multinpcVarp", type.multinpcVarp);
        data.put("multinpcs", TypeJson.ints(type.multinpcs));
        data.put("quests", TypeJson.ints(type.quests));
        data.put("params", TypeJson.params(type.params));
        return data;
    }

    /**
     * Every field of a base animation set, under the name the client gives it, as the set library
     * holds it, one file for each set however many NPCs share it: the sequences it names, which are
     * in the sequence library, and {@code animateShadow}, whether the client draws the shadow under
     * the NPC at all.
     */
    private static Map<String, Object> basData(BASType set) {
        var data = new LinkedHashMap<String, Object>();
        data.put("ready", set.ready);
        data.put("readyTurnCw", set.readyTurnCw);
        data.put("readyTurnCcw", set.readyTurnCcw);
        data.put("readyAnimations", TypeJson.ints(set.readyAnimations));
        data.put("readyAnimationWeights", TypeJson.ints(set.readyAnimationWeights));
        data.put("walk", set.walk);
        data.put("walkTurnCw", set.walkTurnCw);
        data.put("walkTurnCcw", set.walkTurnCcw);
        data.put("walkFollowTurn180", set.walkFollowTurn180);
        data.put("walkFollowTurnCw", set.walkFollowTurnCw);
        data.put("walkFollowTurnCcw", set.walkFollowTurnCcw);
        data.put("run", set.run);
        data.put("runTurnCw", set.runTurnCw);
        data.put("runTurnCcw", set.runTurnCcw);
        data.put("runFollowTurn180", set.runFollowTurn180);
        data.put("runFollowTurnCw", set.runFollowTurnCw);
        data.put("runFollowTurnCcw", set.runFollowTurnCcw);
        data.put("crawl", set.crawl);
        data.put("crawlTurnCw", set.crawlTurnCw);
        data.put("crawlTurnCcw", set.crawlTurnCcw);
        data.put("crawlFollowTurn180", set.crawlFollowTurn180);
        data.put("crawlFollowTurnCw", set.crawlFollowTurnCw);
        data.put("crawlFollowTurnCcw", set.crawlFollowTurnCcw);
        data.put("animateShadow", set.animateShadow);
        data.put("hillWidth", set.hillWidth);
        data.put("hillHeight", set.hillHeight);
        data.put("hillMaxAngleX", set.hillMaxAngleX);
        data.put("hillMaxAngleY", set.hillMaxAngleY);
        data.put("yawAcceleration", set.yawAcceleration);
        data.put("yawMaxSpeed", set.yawMaxSpeed);
        data.put("rollAcceleration", set.rollAcceleration);
        data.put("rollMaxSpeed", set.rollMaxSpeed);
        data.put("rollTargetAngle", set.rollTargetAngle);
        data.put("pitchAcceleration", set.pitchAcceleration);
        data.put("pitchMaxSpeed", set.pitchMaxSpeed);
        data.put("pitchTargetAngle", set.pitchTargetAngle);
        data.put("movementAcceleration", set.movementAcceleration);
        data.put("characterHeight", set.characterHeight);
        data.put("hitbarSprite", set.hitbarSprite);
        data.put("timerbarSprite", set.timerbarSprite);
        data.put("wornTransformations", TypeJson.slots(set.wornTransformations));
        data.put("maxWornRotation", TypeJson.ints(set.maxWornRotation));
        data.put("graphicOffsets", TypeJson.slots(set.graphicOffsets));
        data.put("invObjSlots", TypeJson.ints(set.invObjSlots));
        return data;
    }

    /**
     * How far the type moves each of its models before it merges them, one `[x, y, z]` for each
     * model, `[0, 0, 0]` for a model it leaves in place.
     */
    private static List<List<Integer>> translations(NPCType type) {
        var list = new ArrayList<List<Integer>>();
        for (var model = 0; model < type.models.length; model++) {
            var moved = type.translations == null ? null : type.translations[model];
            list.add(moved == null ? List.of(0, 0, 0) : TypeJson.ints(moved));
        }
        return list;
    }

    private NpcExport() {
        /* empty */
    }
}
