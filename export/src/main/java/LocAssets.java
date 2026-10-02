import com.jagex.game.runetek6.config.loctype.LocType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The locations every map square shares, one glTF file for each location type in one directory.
 *
 * A location is written once, under its id, and holds a mesh for each shape its type has a model
 * for, which for most types is one, each as a root of the file's one scene, and the scene carries
 * the location's name and extras. A skinned mesh is best left a root, as its own transform is
 * ignored in favour of its joints'. Each mesh is the location's asset as {@link ClientLocReader}
 * builds it, and the file's extras carry what an importer needs to place it as {@link LocPlacing}
 * describes. A wall decoration that animates has a second mesh for a diagonal placement, turned
 * already, as {@link ClientLocReader#poser} explains. Each mesh's extras carry the client's own
 * top and bottom of the model, {@code minY} and {@code maxY}, which the bend measures the model
 * by: the client takes them over every vertex, and the mesh holds only the faces the client draws.
 * A location that the client animates has a morph target for every frame of every sequence it can
 * play and an animation for each sequence, as an NPC has. A location that is already in the
 * library is left as it is, so the library is written once and read by every map square after.
 */
public final class LocAssets {

    private static final int SOLE_WEIGHT = 65535;
    private static final int FULL_SCALE = 128;

    private final ClientLocReader reader;
    private final TextureLibrary textures;
    private final Path directory;

    public LocAssets(ClientLocReader reader, TextureLibrary textures, Path directory) {
        this.reader = reader;
        this.textures = textures;
        this.directory = directory;
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
     * Where a location's file is, written if the library does not hold it yet, or nothing where
     * the type has no model the client can build.
     */
    public Optional<Path> file(int id) {
        var file = directory.resolve(id + ".glb");
        if (Files.exists(file)) {
            return Optional.of(file);
        } else {
            return write(id, file).map(ignored -> file);
        }
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
     * Writes one location's file, or nothing where its type has no model the client can build.
     */
    public Optional<Written> write(int id, Path file) {
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
                node.put("extras", Map.of("shape", shape, "turned", variant.turned(), "minY", still.fa(), "maxY", still.EA()));
                var skin = bones.map(held -> SkinWriter.write(gltf, held.joints()));
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
                                skin.orElseThrow(), number, result.targets() ? poses.size() : 0, extras);
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

        var document = gltf.json(shapeNodes, label(type), extras(type));
        try {
            Glb.write(file, document, gltf.bin());
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
     * {@link LocPlacing} uses them.
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
        return extras;
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
