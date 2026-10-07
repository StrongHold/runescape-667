import com.jagex.game.runetek6.config.loctype.LocInteractivity;
import com.jagex.game.runetek6.config.loctype.LocType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The location types every map square shares: one JSON file for each type in one directory, which
 * names the models of each shape and the sequences the type plays, and the models and sequences
 * themselves in the model and sequence libraries beside it ({@link ModelLibrary},
 * {@link SequenceLibrary}). An engine builds each shape's meshes from them, as the client builds a
 * location's model ({@code LocType.model}), and places them as {@link LocPlacing} describes.
 *
 * <p>A type is written once, under its id, and one that is already in the library is left as it
 * is, so the library is written once and read by every map square after. The meshes as the client
 * builds them can also be baked into a glTF file of their own ({@link #writeBaked}), a mesh for
 * each shape, each pose of an animated one a morph target, which is the reference an engine's own
 * building is checked against.
 */
public final class LocAssets {

    /**
     * How many of the options on the mini menu a type's data can set (`LocType.decode`, opcodes 30
     * to 34 and 150 to 154).
     */
    private static final int OPTION_SLOTS = 5;

    private static final int SOLE_WEIGHT = 65535;
    private static final int FULL_SCALE = 128;

    private final ClientLocReader reader;
    private final TextureLibrary textures;
    private final Path directory;
    private final ModelLibrary models;
    private final SequenceLibrary sequences;

    /**
     * @param directory where the locations are kept, beside which the models and sequences they
     *     name are kept, in {@code models} and {@code sequences}.
     */
    public LocAssets(ClientLocReader reader, TextureLibrary textures, Path directory) {
        this.reader = reader;
        this.textures = textures;
        this.directory = directory;
        this.models = new ModelLibrary(reader.models(), textures, directory.resolveSibling("models"));
        this.sequences = new SequenceLibrary(directory.resolveSibling("sequences"));
    }

    /**
     * The directory map squares keep their locations in unless told another: beside the models,
     * NPCs and map squares under the export module's build directory.
     */
    public static Path defaultDirectory() {
        return Path.of("build", "locs");
    }

    public Path directory() {
        return directory;
    }

    /**
     * Where a location type's data is, written with the models it names and the sequences it plays
     * where the libraries lack them, or nothing where the type has no model the client can build.
     */
    public Optional<Path> file(int id) {
        var file = directory.resolve(id + ".json");
        var type = reader.type(id);
        if (Files.exists(file)) {
            writeShared(type);
            return Optional.of(file);
        } else if (buildable(type)) {
            try {
                Files.createDirectories(directory);
                Files.writeString(file, Json.write(extras(type)), StandardCharsets.UTF_8);
            } catch (IOException failure) {
                throw new UncheckedIOException("Could not write location " + id + " to " + file, failure);
            }
            writeShared(type);
            return Optional.of(file);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Whether the client builds a model for every shape of a type, as it builds none for a type
     * that names a mesh the cache does not hold.
     */
    private boolean buildable(LocType type) {
        var shapes = ClientLocReader.shapes(type);
        return !shapes.isEmpty() && shapes.stream().allMatch(shape -> reader.poser(type, shape, false).still() != null);
    }

    /**
     * Writes the models a type names and the sequences it plays into their libraries, where they
     * lack them. Each sequence's channels are worked out on the type's first shape.
     */
    private void writeShared(LocType type) {
        if (type.models != null) {
            for (var shape : type.models) {
                for (var model : shape) {
                    models.file(model & 0xFFFF);
                }
            }
        }
        if (type.hasAnimations()) {
            var poser = reader.poser(type, ClientLocReader.shapes(type).getFirst(), false);
            for (var sequence : type.anim) {
                if (sequence != -1) {
                    sequences.ensure(sequence, poser);
                }
            }
        }
    }

    /**
     * The directory of the models the locations name.
     */
    public Path modelDirectory() {
        return directory.resolveSibling("models");
    }

    /**
     * The directory of the sequences the locations play.
     */
    public Path sequenceDirectory() {
        return directory.resolveSibling("sequences");
    }

    /**
     * What was written for one location.
     *
     * @param shapes how many shapes have a mesh.
     * @param targets how many morph targets each mesh has, which is 0 for a location that never
     *     moves.
     */
    public record Written(int shapes, int faces, int targets, List<String> animations) {
    }

    /**
     * Writes one location's meshes as the client builds them, baked into a glTF file of their own
     * with every pose, or nothing where its type has no model the client can build. An engine builds
     * them from the model and sequence libraries instead; this is the reference it is checked
     * against.
     */
    public Optional<Written> writeBaked(int id, Path file) {
        var type = reader.type(id);
        var gltf = new GltfBuilder();
        var materials = new GltfMaterials(gltf, reader.textures(), textures, file);
        var shapeNodes = new ArrayList<Integer>();
        var animations = new ArrayList<String>();
        var faces = 0;
        var targets = 0;

        var variants = new ArrayList<Variant>();
        for (var shape : ClientLocReader.shapes(type)) {
            variants.add(new Variant(shape, false));
            if (ClientLocReader.needsTurnedAsset(type, shape)) {
                variants.add(new Variant(shape, true));
            }
        }

        for (var variant : variants) {
            var shape = variant.shape();
            var poser = reader.poser(type, shape, variant.turned());
            var still = poser.still();
            if (still == null) {
                return Optional.empty();
            }

            var baker = new PoseBaker(poser);
            var clips = new ArrayList<Clip>();
            if (type.hasAnimations()) {
                for (var i = 0; i < type.anim.length; i++) {
                    if (type.anim[i] != -1) {
                        clips.add(new Clip(type.anim[i], baker.bake(type.anim[i])));
                    }
                }
            }

            var poses = baker.poses();
            var bones = Bones.of(baker);
            var result = bones.isPresent()
                ? ModelToGltf.convertSkinnedInto(gltf, materials, still, poses, bones.get().joints().ofVertex())
                : ModelToGltf.convertInto(gltf, materials, still, poses);
            if (gltf.empty()) {
                /* empty */
            } else {
                if (result.targets()) {
                    gltf.targetNames(baker.names());
                }
                var mesh = gltf.mesh(variant.name());
                var node = new LinkedHashMap<String, Object>();
                node.put("name", variant.name());
                node.put("mesh", mesh);
                var shapeExtras = new java.util.LinkedHashMap<String, Object>();
                shapeExtras.put("shape", shape);
                shapeExtras.put("turned", variant.turned());
                shapeExtras.put("minY", still.fa());
                shapeExtras.put("maxY", still.EA());
                if (still.emitters != null) {
                    shapeExtras.put("emitters", ParticleSources.emitters(still));
                }
                if (still.effectors != null) {
                    shapeExtras.put("effectors", ParticleSources.effectors(still));
                }
                if (still.billboardFaces != null) {
                    shapeExtras.put("billboards", BillboardSources.billboards(still));
                }
                node.put("extras", shapeExtras);
                var skin = bones.map(held -> SkinWriter.write(gltf, held.joints(), baker.skinning().orElseThrow()));
                skin.ifPresent(held -> {
                    node.put("skin", held.number());
                    node.put("children", held.childNodes());
                });
                var number = gltf.node(node);
                shapeNodes.add(number);
                faces += result.faces();
                targets = result.targets() ? poses.size() : 0;
                if (!poses.isEmpty()) {
                    animations.add(variant.name() + " " + Bones.describe(baker, bones));
                }

                for (var clip : clips) {
                    if (!clip.baked().keys().isEmpty()) {
                        var name = variant.name() + " sequence " + clip.sequence();
                        var extras = Map.<String, Object>of("shape", shape);
                        if (bones.isPresent()) {
                            SkinWriter.writeClip(gltf, name, clip.baked(), bones.get().joints(), baker.framePoses(),
                                baker.frames(), skin.orElseThrow(), number, result.targets() ? poses.size() : 0, extras);
                        } else {
                            AnimationWriter.write(gltf, name, clip.baked(), poses.size(), List.of(number), extras);
                        }
                        animations.add(name);
                    }
                }
            }
        }

        if (shapeNodes.isEmpty()) {
            return Optional.empty();
        }

        var document = gltf.document(shapeNodes, label(type), Map.of());
        try {
            GltfFile.write(file, document, gltf.bin());
        } catch (IOException failure) {
            throw new UncheckedIOException("Could not write location " + id + " to " + file, failure);
        }
        return Optional.of(new Written(shapeNodes.size(), faces, targets, List.copyOf(animations)));
    }

    public static String label(LocType type) {
        var name = type.name == null || type.name.equals("null") ? "location" : type.name;
        return name + " " + type.id;
    }

    /**
     * What an importer needs to place the location, in the client's units, as
     * {@link LocPlacing} uses them, followed by every other field of the type under the name the
     * client gives it. The models, colour and texture swaps, lighting and tint are already applied
     * to the meshes, and are listed so that a reader can see what the meshes are made of.
     */
    private static Map<String, Object> extras(LocType type) {
        var extras = new LinkedHashMap<String, Object>();
        extras.put("loc", type.id);
        extras.put("name", type.name);
        extras.put("shapes", ClientLocReader.shapes(type));
        extras.put("mirrored", type.mirror);
        if (ClientLocReader.scaledInAsset(type)) {
            extras.put("resize", List.of(FULL_SCALE, FULL_SCALE, FULL_SCALE));
            extras.put("scaledInAsset", List.of(type.resizex, type.resizey, type.resizez));
        } else {
            extras.put("resize", List.of(type.resizex, type.resizey, type.resizez));
        }
        extras.put("offset", List.of(type.xoff, type.yoff, type.zoff));
        extras.put("translate", List.of(type.translateX, type.translateY, type.translateZ));
        extras.put("hillchange", (int) type.hillchange);
        extras.put("hillskew", type.hillskew);
        extras.put("size", List.of(type.width, type.length));
        extras.put("shadow", type.shadow);
        extras.put("hardShadow", type.hardshadow);
        extras.put("interactive", type.active != LocInteractivity.NONINTERACTIVE);
        extras.put("ops", TypeJson.options(type.ops, OPTION_SLOTS));
        if (type.hasAnimations()) {
            var sequences = new ArrayList<Integer>();
            var weights = new ArrayList<Integer>();
            for (var i = 0; i < type.anim.length; i++) {
                if (type.anim[i] != -1) {
                    sequences.add(type.anim[i]);
                    weights.add(type.anim.length > 1 ? type.anim_weight[i] : SOLE_WEIGHT);
                }
            }
            extras.put("sequences", sequences);
            if (sequences.size() > 1) {
                extras.put("sequenceWeights", weights);
            }
            extras.put("randomStartFrame", type.randomanimframe);
        }
        extras.put("models", models(type));
        extras.put("modelShapes", TypeJson.bytes(type.modelShapes));
        extras.put("recolours", TypeJson.swaps(type.recol_s, type.recol_d));
        extras.put("recolourPalette", TypeJson.bytes(type.recol_d_palette));
        extras.put("retextures", TypeJson.swaps(type.retex_s, type.retex_d));
        extras.put("ambient", type.ambient);
        extras.put("contrast", type.contrast);
        extras.put("tint", List.of((int) type.targetHue, (int) type.targetSaturation, (int) type.targetLightness,
            (int) type.colourShiftPercentage));
        extras.put("sharelight", type.sharelight);
        extras.put("offsetY", type.offsetY);
        extras.put("walloff", type.walloff);
        extras.put("blockwalk", type.blockwalk);
        extras.put("blockrange", type.blockrange);
        extras.put("breakroutefinding", type.breakroutefinding);
        extras.put("forceapproach", type.forceapproach);
        extras.put("forcedecor", type.forcedecor);
        extras.put("raiseobject", type.raiseobject);
        extras.put("occlude", type.occlude);
        extras.put("occlusionHeight", type.occlusionHeight);
        extras.put("occlusionOffset", type.occlusionOffset);
        extras.put("istexture", type.istexture);
        extras.put("dynamic", type.dynamic);
        extras.put("animated", type.animated);
        extras.put("members", type.members);
        extras.put("mapelement", type.mapelement);
        extras.put("msi", type.msi);
        extras.put("msiflip", type.msiflip);
        extras.put("msirotate", type.msirotate);
        extras.put("msiRotateOffset", type.msiRotateOffset);
        extras.put("cursor1Op", type.cursor1Op);
        extras.put("cursor1", type.cursor1);
        extras.put("cursor2Op", type.cursor2Op);
        extras.put("cursor2", type.cursor2);
        extras.put("sound", type.sound);
        extras.put("soundRange", type.soundRange);
        extras.put("soundSize", type.soundSize);
        extras.put("soundVolume", type.soundVolume);
        extras.put("soundDelayMin", type.soundDelayMin);
        extras.put("soundDelayMax", type.soundDelayMax);
        extras.put("soundRateMin", type.soundRateMin);
        extras.put("soundRateMax", type.soundRateMax);
        extras.put("randomsound", type.randomsound);
        extras.put("randomSoundIds", TypeJson.ints(type.randomSoundIds));
        extras.put("vorbis", type.vorbis);
        extras.put("multivarbit", type.multivarbit);
        extras.put("multivarp", type.multivarp);
        extras.put("multiloc", TypeJson.ints(type.multiloc));
        extras.put("quests", TypeJson.ints(type.quests));
        extras.put("params", TypeJson.params(type.params));
        return extras;
    }

    /**
     * The meshes of each shape the type lists in `modelShapes`, by id, as the client's `models`
     * holds them.
     */
    private static List<List<Integer>> models(LocType type) {
        var list = new ArrayList<List<Integer>>();
        if (type.models != null) {
            for (var shape : type.models) {
                list.add(TypeJson.ints(shape));
            }
        }
        return list;
    }

    private record Clip(int sequence, PoseBaker.Clip baked) {
    }

    /**
     * One mesh of the file: a shape, and whether it is the one turned for a diagonal placement.
     */
    private record Variant(int shape, boolean turned) {

        private String name() {
            return "shape " + shape + (turned ? " turned" : "");
        }
    }
}
