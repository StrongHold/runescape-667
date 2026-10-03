import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Writes one map square: its ground as a binary glTF file, and its locations as a description of
 * where each stands, which refers to the location library by id.
 *
 * <p>The ground file has one root node for the map square, named for it, whose south west corner
 * is the origin. Under it hangs a node for the ground of each level that has tiles, and a node
 * for the bed under the map square's water where it has one. The description is a JSON file
 * beside it, as the export module's README lays out, and every location it names is written to
 * the library if it is not there yet.
 *
 * <p>Every placement is checked: the location's asset, placed as an importer would place it, is
 * compared vertex for vertex with the model the client builds for the placement, and a map
 * square with any that differ is reported and fails.
 */
public final class MapSquareExport {

    /**
     * Where the map square starts in the region it is built in, in the client's units, which
     * every placement is measured from so that it stands on the ground as written.
     */
    private static final int MAP_SQUARE_UNITS = ClientMapSquareReader.ORIGIN * 512;
    private static final float UNITS_PER_METRE = 512.0F;

    /**
     * The strength a map light is written with, in candela, which is what the client gives every
     * light before its flicker. The client's lights reach a radius of whole tiles and half a tile
     * more.
     */
    private static final float LIGHT_INTENSITY = 1.0F;
    private static final float HALF_TILE = 0.5F;

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @ParametersDelegate
        private final TextureArgs textures = new TextureArgs();

        @Parameter(names = "--x", description = "The map square's position from west to east, in map squares of 64 tiles", required = true)
        private int x;

        @Parameter(names = "--z", description = "The map square's position from south to north, in map squares of 64 tiles", required = true)
        private int z;

        @Parameter(
            names = "--out",
            description = "The ground file to write, relative to the export module when not absolute; the description is written beside it"
        )
        private Path out;

        @Parameter(names = "--locs", description = "The directory the locations every map square shares are kept in, relative to the export module when not absolute")
        private Path locs = LocAssets.defaultDirectory();

        @Parameter(names = "--keys", description = "The directory holding the key each map square's locations are locked with, one <name>.txt of four numbers per map square")
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
        var parsed = CommandLine.parse("exportMapSquare", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var reader = new ClientMapSquareReader(args.where.cache(), args.keys);
        var square = reader.read(args.x, args.z, !args.noLocations);
        var name = args.x + "_" + args.z;
        var out = args.out == null ? Path.of("build", "mapsquares", name + ".glb") : args.out;
        var textures = args.textures.library(reader.textures());
        var assets = new LocAssets(reader.locs(), textures, args.locs);
        var gltf = new GltfBuilder();
        var materials = new GltfMaterials(gltf, reader.textures(), textures, out);

        System.out.println("mapsquare " + name + ", tiles " + args.x * ClientMapSquareReader.TILES_ACROSS + ","
            + args.z * ClientMapSquareReader.TILES_ACROSS + " to " + ((args.x + 1) * ClientMapSquareReader.TILES_ACROSS - 1)
            + "," + ((args.z + 1) * ClientMapSquareReader.TILES_ACROSS - 1));

        var children = new ArrayList<Integer>();
        for (var level = 0; level < ClientMapSquareReader.LEVELS; level++) {
            terrain(gltf, materials, square, square.grounds().get(level), square.colours().get(level),
                "terrain level " + level, level).ifPresent(children::add);
        }
        if (square.underwater() instanceof ClientMapSquareReader.Underwater.Bed bed) {
            terrain(gltf, materials, square, bed.ground(), bed.colours(), "underwater bed", 0).ifPresent(children::add);
            report("locations under the water", bed.locations());
        }
        report("locations", square.locations());
        children.addAll(lights(gltf, square.environment()));

        var root = new LinkedHashMap<String, Object>();
        root.put("name", "mapsquare " + name);
        root.put("children", children);
        root.put("extras", Map.of("mapSquareX", args.x, "mapSquareZ", args.z,
            "tileX", args.x * ClientMapSquareReader.TILES_ACROSS, "tileZ", args.z * ClientMapSquareReader.TILES_ACROSS));
        Glb.write(out, gltf.json(List.of(gltf.node(root))), gltf.bin());
        System.out.println("wrote " + out.toAbsolutePath().normalize());

        var description = describe(args, square, out, assets);
        var descriptionFile = out.resolveSibling(name + ".json");
        Files.writeString(descriptionFile, Json.write(description), StandardCharsets.UTF_8);
        System.out.println("wrote " + descriptionFile.toAbsolutePath().normalize());

        var checks = checks(square.placements());
        if (checks.differ() > 0) {
            throw new IllegalStateException(checks.differ() + " placements differ from what the client builds");
        }
    }

    private static void report(String what, ClientMapSquareReader.Placing placing) {
        switch (placing) {
            case ClientMapSquareReader.Placing.Placed placed -> {
                /* empty */
            }
            case ClientMapSquareReader.Placing.NotPlaced not -> System.out.println("  no " + what + ": " + not.reason());
        }
    }

    /**
     * One node wearing the ground of one level, or nothing where the map square has no tile on it.
     */
    private static Optional<Integer> terrain(GltfBuilder gltf, GltfMaterials materials,
                                             ClientMapSquareReader.MapSquare square, JavaGround ground,
                                             RecordingGround colours, String name, int level) {
        var result = GroundToGltf.convertInto(gltf, materials, ground, colours, ClientMapSquareReader.ORIGIN,
            square.x() * ClientMapSquareReader.TILES_ACROSS, square.z() * ClientMapSquareReader.TILES_ACROSS);
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
     * The description of the map square: where its files are, the heights its locations are bent
     * against, and every placement. Every location named is written to the library first, so the
     * description never names one the library lacks.
     */
    private static Map<String, Object> describe(Args args, ClientMapSquareReader.MapSquare square, Path ground,
                                                LocAssets assets) {
        var placements = new ArrayList<Map<String, Object>>();
        var parts = new TreeMap<String, Integer>();
        var written = new TreeSet<Integer>();
        var missing = new TreeSet<Integer>();

        for (var placement : square.placements()) {
            var file = assets.file(placement.id());
            if (file.isPresent()) {
                placements.add(placementDescription(placement));
                parts.merge(placement.part(), 1, Integer::sum);
                written.add(placement.id());
            } else {
                missing.add(placement.id());
            }
        }

        var heights = new LinkedHashMap<String, Object>();
        heights.put("firstTile", -ClientMapSquareReader.HEIGHT_MARGIN);
        heights.put("levels", nested(square.heights()));
        if (square.underwater() instanceof ClientMapSquareReader.Underwater.Bed bed) {
            heights.put("underwater", nested(bed.heights()));
        }

        var description = new LinkedHashMap<String, Object>();
        description.put("mapSquareX", args.x);
        description.put("mapSquareZ", args.z);
        description.put("tileX", args.x * ClientMapSquareReader.TILES_ACROSS);
        description.put("tileZ", args.z * ClientMapSquareReader.TILES_ACROSS);
        description.put("tilesAcross", ClientMapSquareReader.TILES_ACROSS);
        description.put("unitsPerTile", 512);
        description.put("ground", ground.getFileName().toString());
        description.put("locs", TextureLibrary.relativeUri(ground, assets.directory()));
        description.put("textures", TextureLibrary.relativeUri(ground, args.textures.library(null).directory()));
        description.put("heights", heights);
        description.put("flags", nested(square.flags()));
        description.put("environment", environment(square.environment()));
        description.put("lights", square.environment().lights().stream().map(MapSquareExport::light).toList());
        description.put("placements", placements);

        System.out.println("  " + describe(square.environment()));
        System.out.println("  " + placements.size() + " locations placed, of " + written.size() + " kinds in "
            + assets.directory().toAbsolutePath().normalize());
        parts.forEach((part, count) -> System.out.println("    " + count + " " + part));
        if (!missing.isEmpty()) {
            System.out.println("    " + missing.size() + " kinds have no model the client builds, and their "
                + "placements are left out: " + missing);
        }
        return description;
    }

    /**
     * How the map square is lit, as the file gives it, in the client's frame and units. The sun's
     * direction is where its light comes from, with y down.
     */
    private static Map<String, Object> environment(EnvironmentDecoder.Environment environment) {
        var description = new LinkedHashMap<String, Object>();
        description.put("sun", List.of(environment.sun()[0], environment.sun()[1], environment.sun()[2]));
        description.put("sunColour", environment.sunColour());
        description.put("sunIntensity", environment.sunIntensity());
        description.put("reverseSunIntensity", environment.reverseSunIntensity());
        description.put("ambient", environment.ambient());
        description.put("fogColour", environment.fogColour());
        description.put("fogRange", environment.fogRange());
        description.put("bloom", List.of(environment.bloom()[0], environment.bloom()[1], environment.bloom()[2]));
        environment.skyBox().ifPresent(skyBox -> description.put("skyBox", Map.of(
            "id", skyBox.id(), "sphereOffset", List.of(skyBox.sphereOffsetX(), skyBox.sphereOffsetY(), skyBox.sphereOffsetZ()),
            "rotation", skyBox.rotation())));
        environment.cubeMap().ifPresent(textures -> description.put("cubeMap",
            java.util.Arrays.stream(textures).boxed().toList()));
        return description;
    }

    private static Map<String, Object> light(EnvironmentDecoder.Light light) {
        var description = new LinkedHashMap<String, Object>();
        description.put("level", light.level());
        description.put("spansLevelsAbove", light.spansLevelsAbove());
        description.put("spansLevelsBelow", light.spansLevelsBelow());
        description.put("x", light.x());
        description.put("y", light.y());
        description.put("z", light.z());
        description.put("radius", light.radius());
        description.put("rowSpans", java.util.Arrays.stream(light.rowSpans()).boxed().toList());
        description.put("colour", light.colour());
        description.put("phase", light.phase());
        description.put("preset", light.preset());
        if (light.lightType() != -1) {
            description.put("lightType", light.lightType());
        }
        description.put("flickerAmbient", light.ambient());
        description.put("flickerPattern", light.pattern());
        description.put("flickerAmplitude", light.amplitude());
        description.put("flickerFrequency", light.frequency());
        return description;
    }

    /**
     * The map square's lights as nodes of the ground file, through KHR_lights_punctual, so that an
     * engine that reads the extension places them without reading the description. Each reaches
     * as far as its radius of tiles, and its node's extras carry the level and the flicker.
     */
    private static List<Integer> lights(GltfBuilder gltf, EnvironmentDecoder.Environment environment) {
        var nodes = new ArrayList<Integer>();
        var number = 0;
        for (var light : environment.lights()) {
            var colour = new float[] {Srgb.toLinear(light.colour() >> 16 & 0xFF), Srgb.toLinear(light.colour() >> 8 & 0xFF),
                Srgb.toLinear(light.colour() & 0xFF)};
            var name = "light " + number++;
            var punctual = gltf.punctualLight(name, colour, LIGHT_INTENSITY, light.radius() + HALF_TILE);
            var extras = new LinkedHashMap<String, Object>();
            extras.put("level", light.level());
            extras.put("flickerAmbient", light.ambient());
            extras.put("flickerPattern", light.pattern());
            extras.put("flickerAmplitude", light.amplitude());
            extras.put("flickerFrequency", light.frequency());
            extras.put("phase", light.phase());
            nodes.add(gltf.lightNode(name, punctual, List.of(light.x() / UNITS_PER_METRE, -light.y() / UNITS_PER_METRE,
                -light.z() / UNITS_PER_METRE), extras));
        }
        return nodes;
    }

    private static String describe(EnvironmentDecoder.Environment environment) {
        return "sun from " + java.util.Arrays.toString(environment.sun()) + " colour " + Integer.toHexString(environment.sunColour())
            + " intensity " + environment.sunIntensity() + "/" + environment.reverseSunIntensity() + ", ambient "
            + environment.ambient() + ", fog " + Integer.toHexString(environment.fogColour()) + " range " + environment.fogRange()
            + ", sky box " + environment.skyBox().map(EnvironmentDecoder.SkyBox::id).map(String::valueOf).orElse("none")
            + ", " + environment.lights().size() + " lights";
    }

    private static Map<String, Object> placementDescription(ClientMapSquareReader.Placement placement) {
        var description = new LinkedHashMap<String, Object>();
        description.put("loc", placement.id());
        description.put("shape", placement.shape());
        description.put("rotation", placement.rotation());
        description.put("level", placement.level());
        description.put("virtualLevel", placement.virtualLevel());
        description.put("underwater", placement.underwater());
        description.put("x", placement.x() - MAP_SQUARE_UNITS);
        description.put("y", placement.y());
        description.put("z", placement.z() - MAP_SQUARE_UNITS);
        description.put("part", placement.part());
        if (placement.sequencesOf() != placement.id()) {
            description.put("sequencesOf", placement.sequencesOf());
        }
        return description;
    }

    private static List<List<Integer>> nested(int[][] heights) {
        var rows = new ArrayList<List<Integer>>();
        for (var row : heights) {
            var values = new ArrayList<Integer>();
            for (var value : row) {
                values.add(value);
            }
            rows.add(values);
        }
        return rows;
    }

    private static List<List<List<Integer>>> nested(int[][][] heights) {
        var levels = new ArrayList<List<List<Integer>>>();
        for (var level : heights) {
            levels.add(nested(level));
        }
        return levels;
    }

    /**
     * How many placements the importer's steps reproduce, and how many they do not.
     */
    private record Checks(int match, int differ) {
    }

    private static Checks checks(List<ClientMapSquareReader.Placement> placements) {
        var counts = new TreeMap<ClientMapSquareReader.Check, Integer>();
        var examples = new TreeMap<ClientMapSquareReader.Check, String>();
        for (var placement : placements) {
            counts.merge(placement.check(), 1, Integer::sum);
            examples.putIfAbsent(placement.check(), placement.name() + " " + placement.id() + " shape "
                + placement.shape() + " rotation " + placement.rotation() + " at " + placement.x() + "," + placement.z());
        }

        var match = counts.getOrDefault(ClientMapSquareReader.Check.MATCHES, 0);
        System.out.println("  " + match + " of " + placements.size()
            + " placements are what the client builds when their asset is placed by the importer's steps");
        counts.forEach((check, count) -> {
            if (check != ClientMapSquareReader.Check.MATCHES) {
                System.out.println("    " + count + " " + check.name().toLowerCase().replace('_', ' ')
                    + ", such as " + examples.get(check));
            }
        });
        return new Checks(match, placements.size() - match);
    }

    private MapSquareExport() {
        /* empty */
    }
}
