import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Writes one map square, its ground and the locations standing on it, as a binary glTF file.
 *
 * <p>The scene has one root node for the square, named for it, whose south west corner is the
 * origin. Under it hangs a node for the ground of each level that has tiles, a node for the bed
 * under the square's water where it has one, and a node for the locations of each level and for
 * those under the water, which holds a node for each location placed. A location's node is named
 * after the location and its id, moved to where the client draws it, and wears the mesh of its
 * model. Each distinct model is written once, so the same tree planted many times on level ground
 * is one mesh.
 */
public final class SquareExport {

    private static final float UNITS_PER_METRE = 512.0F;
    private static final int SQUARE_UNITS = ClientSquareReader.ORIGIN * 512;

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(names = "--x", description = "The square's position from west to east, in squares of 64 tiles", required = true)
        private int x;

        @Parameter(names = "--z", description = "The square's position from south to north, in squares of 64 tiles", required = true)
        private int z;

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out;

        @Parameter(names = "--keys", description = "The directory holding the key each square's locations are locked with, one <name>.txt of four numbers per square")
        private Path keys = LocationKeys.defaultDirectory();

        @Parameter(names = "--no-locations", description = "Write the ground alone")
        private boolean noLocations;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportSquare", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var reader = new ClientSquareReader(args.where.cache(), args.keys);
        var square = reader.read(args.x, args.z, !args.noLocations);
        var gltf = new GltfBuilder();
        var materials = new GltfMaterials(gltf, reader.textures());
        var name = args.x + "_" + args.z;

        System.out.println("square " + name + ", tiles " + args.x * ClientSquareReader.TILES_ACROSS + ","
            + args.z * ClientSquareReader.TILES_ACROSS + " to " + ((args.x + 1) * ClientSquareReader.TILES_ACROSS - 1)
            + "," + ((args.z + 1) * ClientSquareReader.TILES_ACROSS - 1));

        var children = new ArrayList<Integer>();
        for (var level = 0; level < ClientSquareReader.LEVELS; level++) {
            terrain(gltf, materials, square, square.grounds().get(level), "terrain level " + level, level)
                .ifPresent(children::add);
        }
        if (square.underwater() instanceof ClientSquareReader.Underwater.Bed bed) {
            terrain(gltf, materials, square, bed.ground(), "underwater bed", 0).ifPresent(children::add);
            report("locations under the water", bed.locations());
        }

        report("locations", square.locations());
        children.addAll(locations(gltf, materials, square.placements()));

        var root = new LinkedHashMap<String, Object>();
        root.put("name", "square " + name);
        root.put("children", children);
        root.put("extras", Map.of("squareX", args.x, "squareZ", args.z,
            "tileX", args.x * ClientSquareReader.TILES_ACROSS, "tileZ", args.z * ClientSquareReader.TILES_ACROSS));
        var document = gltf.json(List.of(gltf.node(root)));

        var out = args.out == null ? Path.of("build", "squares", name + ".glb") : args.out;
        Glb.write(out, document, gltf.bin());
        System.out.println("wrote " + out.toAbsolutePath().normalize());
    }

    private static void report(String what, ClientSquareReader.Placing placing) {
        switch (placing) {
            case ClientSquareReader.Placing.Placed placed -> {
                /* empty */
            }
            case ClientSquareReader.Placing.NotPlaced not -> System.out.println("  no " + what + ": " + not.reason());
        }
    }

    /**
     * One node wearing the ground of one level, or nothing where the square has no tile on it.
     */
    private static Optional<Integer> terrain(GltfBuilder gltf, GltfMaterials materials,
                                             ClientSquareReader.Square square, JavaGround ground, String name,
                                             int level) {
        var result = GroundToGltf.convertInto(gltf, materials, ground, ClientSquareReader.ORIGIN,
            square.x() * ClientSquareReader.TILES_ACROSS, square.z() * ClientSquareReader.TILES_ACROSS);
        if (gltf.empty()) {
            return Optional.empty();
        }

        var node = new LinkedHashMap<String, Object>();
        node.put("name", name);
        node.put("mesh", gltf.mesh(name));
        node.put("extras", Map.of("level", level, "tiles", result.tiles()));

        System.out.println("  " + name + ": " + result.tiles() + " tiles, " + result.faces() + " faces in "
            + result.primitives() + " primitives");
        result.layered().forEach((layers, count) -> System.out.println("    " + count + " faces of " + layers
            + " textures written as " + layers + " layers"));
        result.skipped().forEach((reason, count) -> System.out.println("    " + count + " faces left out, " + reason));
        result.approximated().forEach((reason, count) -> System.out.println("    " + count + " faces approximated, " + reason));
        return Optional.of(gltf.node(node));
    }

    /**
     * One node of locations for each level, holding a node for each location placed on it. Each
     * distinct model is written as one mesh, which every placement of it wears, and a location
     * the client animates gets a morph target for every frame it can show and an animation for
     * every sequence it can play, which every node of that location plays.
     */
    private static List<Integer> locations(GltfBuilder gltf, GltfMaterials materials,
                                           List<ClientSquareReader.Placement> placements) {
        if (placements.isEmpty()) {
            return List.of();
        }

        var meshes = new HashMap<MeshKey, Integer>();
        var byLevel = new TreeMap<String, List<Integer>>();
        var animations = new LinkedHashMap<AnimationKey, Animation>();
        var parts = new TreeMap<String, Integer>();
        var faces = 0;
        var empty = 0;
        var animated = 0;

        for (var placement : placements) {
            var key = MeshKey.of(placement);
            var mesh = meshes.get(key);
            if (mesh == null && !meshes.containsKey(key)) {
                var result = ModelToGltf.convertInto(gltf, materials, placement.model(), poses(placement));
                if (gltf.empty()) {
                    mesh = null;
                } else {
                    if (placement.motion() instanceof ClientSquareReader.Motion.Animated moving) {
                        gltf.targetNames(moving.targetNames());
                    }
                    mesh = gltf.mesh(meshName(placement));
                    faces += result.faces();
                }
                meshes.put(key, mesh);
            }

            if (mesh == null) {
                empty++;
            } else {
                var node = gltf.node(placementNode(placement, mesh));
                byLevel.computeIfAbsent(groupName(placement), ignored -> new ArrayList<>()).add(node);
                parts.merge(placement.part(), 1, Integer::sum);
                if (placement.motion() instanceof ClientSquareReader.Motion.Animated moving) {
                    animated++;
                    for (var clip : moving.clips()) {
                        animations.computeIfAbsent(new AnimationKey(placement.id(), clip.sequence()),
                                ignored -> new Animation(label(placement), clip.clip(), moving.poses().size()))
                            .play(node, moving.poses().size());
                    }
                }
            }
        }

        for (var animation : animations.values()) {
            animation.write(gltf);
        }

        var roots = new ArrayList<Integer>();
        for (var group : byLevel.entrySet()) {
            var node = new LinkedHashMap<String, Object>();
            node.put("name", group.getKey());
            node.put("children", group.getValue());
            roots.add(gltf.node(node));
        }

        var placed = byLevel.values().stream().mapToInt(List::size).sum();
        var written = meshes.values().stream().filter(Objects::nonNull).count();
        System.out.println("  " + placed + " locations placed, wearing " + written + " distinct meshes of "
            + faces + " faces");
        parts.forEach((part, count) -> System.out.println("    " + count + " " + part));
        if (animated > 0) {
            System.out.println("    " + animated + " animated, playing " + animations.size() + " animations");
            animations.values().forEach(animation -> System.out.println("      " + animation.describe()));
        }
        if (empty > 0) {
            System.out.println("    " + empty + " placements left out, as their model has no face the client draws");
        }
        return roots;
    }

    private static List<Pose> poses(ClientSquareReader.Placement placement) {
        return switch (placement.motion()) {
            case ClientSquareReader.Motion.Still still -> List.of();
            case ClientSquareReader.Motion.Animated moving -> moving.poses();
        };
    }

    private static String groupName(ClientSquareReader.Placement placement) {
        return placement.underwater() ? "underwater locations" : "locations level " + placement.level();
    }

    private static String meshName(ClientSquareReader.Placement placement) {
        return label(placement) + " shape " + placement.shape() + " rotation " + placement.rotation();
    }

    private static String label(ClientSquareReader.Placement placement) {
        var name = placement.name() == null || placement.name().equals("null") ? "location" : placement.name();
        return name + " " + placement.id();
    }

    private static Map<String, Object> placementNode(ClientSquareReader.Placement placement, int mesh) {
        var node = new LinkedHashMap<String, Object>();
        node.put("name", label(placement));
        node.put("mesh", mesh);
        node.put("translation", List.of(
            (placement.x() - SQUARE_UNITS) / UNITS_PER_METRE,
            -placement.y() / UNITS_PER_METRE,
            -(placement.z() - SQUARE_UNITS) / UNITS_PER_METRE));

        var extras = new LinkedHashMap<String, Object>();
        extras.put("loc", placement.id());
        extras.put("shape", placement.shape());
        extras.put("rotation", placement.rotation());
        extras.put("level", placement.level());
        extras.put("part", placement.part());
        extras.put("underwater", placement.underwater());
        if (placement.motion() instanceof ClientSquareReader.Motion.Animated moving) {
            extras.put("sequences", moving.clips().stream().map(ClientSquareReader.LocClip::sequence).toList());
            if (moving.clips().size() > 1) {
                extras.put("sequenceWeights", moving.clips().stream().map(ClientSquareReader.LocClip::weight).toList());
            }
            extras.put("randomStartFrame", moving.randomStartFrame());
        }
        node.put("extras", extras);
        return node;
    }

    /**
     * One sequence of one location, which every node of that location on the square plays.
     */
    private record AnimationKey(int loc, int sequence) {
    }

    /**
     * The nodes that play one sequence of one location. Every node of a location wears a mesh of
     * the same morph targets, in the same order, however the ground under each bends it, so one
     * sampler of weights serves them all.
     */
    private static final class Animation {

        private final String name;
        private final PoseBaker.Clip clip;
        private final int targets;
        private final List<Integer> nodes = new ArrayList<>();

        private Animation(String label, PoseBaker.Clip clip, int targets) {
            this.name = label + " sequence " + clip.sequence().id;
            this.clip = clip;
            this.targets = targets;
        }

        private void play(int node, int nodeTargets) {
            if (nodeTargets != targets) {
                throw new IllegalStateException(name + " is played on a node of " + nodeTargets
                    + " morph targets, where the first node of it has " + targets);
            }
            nodes.add(node);
        }

        private void write(GltfBuilder gltf) {
            if (!clip.keys().isEmpty()) {
                AnimationWriter.write(gltf, name, clip, targets, List.copyOf(nodes), Map.of("nodes", nodes.size()));
            }
        }

        private String describe() {
            return AnimationWriter.describe(name, clip) + ", on " + nodes.size() + " nodes";
        }
    }

    /**
     * What makes two placements wear the same mesh: the same location, put down as the same shape
     * and turned the same way. A location that is bent to fit the ground under it is only the same
     * where the ground bends it the same way, so its heights are part of what it is. A location
     * that is animated is bent again at every frame, so where its frames put every vertex is part
     * of it too.
     */
    private record MeshKey(int id, int shape, int rotation, List<Integer> heights, List<Integer> frames) {

        private static MeshKey of(ClientSquareReader.Placement placement) {
            var model = placement.model();
            var heights = placement.conformed()
                ? Arrays.stream(Arrays.copyOf(model.vertexY, model.vertexCount)).boxed().toList()
                : List.<Integer>of();
            var frames = new ArrayList<Integer>();
            for (var pose : poses(placement)) {
                for (var vertex = 0; vertex < model.vertexCount; vertex++) {
                    frames.add(pose.x()[vertex]);
                    frames.add(pose.y()[vertex]);
                    frames.add(pose.z()[vertex]);
                }
            }
            return new MeshKey(placement.id(), placement.shape(), placement.rotation(), heights, List.copyOf(frames));
        }
    }

    private SquareExport() {
        /* empty */
    }
}
