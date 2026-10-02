import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.AnimBase;
import com.jagex.AnimFrame;
import com.jagex.core.io.Packet;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.graphics.Mesh;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5Index;
import com.jagex.js5.js5;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Checks that every model, animation base, animation frame, map square and the table of texture
 * metrics in the cache is read in full.
 *
 * These formats are not lists of opcodes, so the config census cannot step through them. Each is
 * instead cut into the sections its reader works through, and a walker that reads only what
 * decides where each section ends says whether every section stops where the next one begins.
 * The client's own reader decodes the same bytes, and the counts it comes to are set against the
 * walker's, which shows the walker follows the same structure the client does.
 */
public final class CacheLayoutCensus {

    private static final List<String> FORMATS = List.of("model", "base", "frame", "terrain", "location", "texture");

    /**
     * How far a map square's position can run on either axis: the client keeps each in a byte.
     */
    private static final int SQUARES_ACROSS = 256;

    /**
     * What the client names the groups of the map by, before the square's position: its terrain,
     * its locations, its NPCs, and the terrain and locations of the world under its water.
     */
    private static final List<String> MAP_PREFIXES = List.of("m", "l", "n", "um", "ul");

    /**
     * The most a locked group can claim to unpack to before the claim is taken as a sign of the
     * wrong key, which leaves that field as noise.
     */
    private static final int LARGEST_PLAUSIBLE_GROUP = 1 << 24;

    /**
     * The key the client is handed for a square whose locations are not locked.
     */
    private static final int[] NO_KEY = new int[4];

    /**
     * How long a container's header is when its data is stored as it is, and when it is
     * compressed and so also says how long the data unpacks to.
     */
    private static final int STORED_HEADER = 5;
    private static final int COMPRESSED_HEADER = 9;

    /**
     * The version every stored group ends with, which is not part of its container.
     */
    private static final int VERSION_TRAILER = 2;

    /**
     * How many failing items of one kind are named before the rest are only counted.
     */
    private static final int NAMED_FAILURES = 5;

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs cache = new CacheArgs();

        @Parameter(names = "--format", description = "A format to check: model, base, frame, terrain, location or texture; repeat for several, omit for all")
        private List<String> formats = new ArrayList<>();

        @Parameter(names = "--keys", description = "The directory holding the key each square's locations are locked with, one <name>.txt of four numbers per square")
        private Path keys = LocationKeys.defaultDirectory();

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /**
     * Every item of one format, how many failed, and the ways they failed.
     */
    private static final class Tally {

        private final String format;
        private int items;
        private int failed;
        private final Map<String, List<String>> kinds = new TreeMap<>();
        private final Map<String, Integer> skipped = new TreeMap<>();

        private Tally(String format) {
            this.format = format;
        }

        private void add(String id, List<Layout.Mismatch> mismatches) {
            items++;
            if (!mismatches.isEmpty()) {
                failed++;
            }
            for (var mismatch : mismatches) {
                kinds.computeIfAbsent(mismatch.kind(), unused -> new ArrayList<>())
                    .add(id + " (" + mismatch.detail() + ")");
            }
        }

        /**
         * Notes an item that could not be checked at all, and why, apart from those that could.
         */
        private void skip(String why) {
            skipped.merge(why, 1, Integer::sum);
        }

        private void report() {
            System.out.println(format + ": " + items + " items, " + (items - failed) + " read in full, "
                + failed + " not");
            skipped.forEach((why, count) -> System.out.println("  not checked, " + why + ": " + count));
            kinds.forEach((kind, examples) -> {
                System.out.println("  " + kind + ": " + examples.size() + " items");
                examples.stream().limit(NAMED_FAILURES).forEach(example -> System.out.println("    " + example));
            });
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("censusLayouts", new Args(), arguments);
        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(Args args) throws Exception {
        var unknown = args.formats.stream().filter(name -> !FORMATS.contains(name)).toList();
        if (!unknown.isEmpty()) {
            throw new IllegalArgumentException("No format is called " + unknown + ". The formats are " + FORMATS + ".");
        }

        var cache = args.cache.cache();
        for (var format : FORMATS) {
            if (args.formats.isEmpty() || args.formats.contains(format)) {
                switch (format) {
                    case "model" -> models(cache);
                    case "base" -> bases(cache);
                    case "frame" -> frames(cache);
                    case "terrain" -> terrain(cache);
                    case "location" -> locations(cache, args.keys);
                    case "texture" -> textures(cache);
                    default -> throw new IllegalStateException(format);
                }
                System.out.println();
            }
        }
    }

    /**
     * Every model, which the client keeps as file 0 of a group of its own.
     */
    private static void models(File cache) throws Exception {
        var tally = new Tally("model");
        var index = Cache.index(cache, Js5Archive.MODELS);
        var older = 0;

        for (var group : Cache.groupsOf(index)) {
            var data = Cache.split(Cache.group(cache, Js5Archive.MODELS, group), index, group).get(0);
            if (data == null) {
                tally.add(Integer.toString(group), List.of(new Layout.Mismatch("group holds no file 0", "")));
            } else {
                older += MeshLayout.isNew(data) ? 0 : 1;
                tally.add(Integer.toString(group), checkModel(data));
            }
        }

        tally.report();
        System.out.println("  in the older format: " + older);
    }

    private static List<Layout.Mismatch> checkModel(byte[] data) {
        var mismatches = new ArrayList<Layout.Mismatch>();
        var walk = MeshLayout.walk(data);
        if (!walk.whole()) {
            mismatches.add(walk.mismatch());
        }

        try {
            var mesh = new Mesh(data);
            var client = new LinkedHashMap<String, Integer>();
            client.put("vertices", mesh.vertexCount);
            client.put("faces", mesh.faceCount);
            client.put("texture spaces", mesh.texSpaceCount);
            client.put("version", mesh.version);
            client.put("complex texture spaces", lengthOf(mesh.texSpaceScaleX));
            client.put("emitters", lengthOf(mesh.emitters));
            client.put("effectors", lengthOf(mesh.effectors));
            client.put("billboards", lengthOf(mesh.billboards));
            compare(walk, client, mismatches);
        } catch (RuntimeException failure) {
            mismatches.add(clientThrows(failure));
        }

        return mismatches;
    }

    private static void bases(File cache) throws Exception {
        var tally = new Tally("base");

        for (var base : basesOf(cache).entrySet()) {
            tally.add(Integer.toString(base.getKey()), checkBase(base.getKey(), base.getValue()));
        }

        tally.report();
    }

    private static List<Layout.Mismatch> checkBase(int id, byte[] data) {
        var mismatches = new ArrayList<Layout.Mismatch>();
        var walk = AnimBaseLayout.walk(data);
        if (!walk.whole()) {
            mismatches.add(walk.mismatch());
        }

        try {
            var base = new AnimBase(id, data);
            compare(walk, Map.of(
                "transforms", base.transformCount,
                "labels", Arrays.stream(base.transformLabels).mapToInt(labels -> labels.length).sum()
            ), mismatches);
        } catch (RuntimeException failure) {
            mismatches.add(clientThrows(failure));
        }

        return mismatches;
    }

    /**
     * Every frame, which the client keeps as the files of one group for each set of frames. The
     * base a frame moves is read once and shared by every frame that names it, as the client
     * shares it within a set.
     */
    private static void frames(File cache) throws Exception {
        var tally = new Tally("frame");
        var skipped = new TreeMap<Integer, Integer>();
        var held = basesOf(cache);
        var bases = new HashMap<Integer, AnimBase>();
        var index = Cache.index(cache, Js5Archive.ANIMS);

        for (var group : Cache.groupsOf(index)) {
            var files = Cache.split(Cache.group(cache, Js5Archive.ANIMS, group), index, group);
            for (var file : files.entrySet()) {
                var walk = AnimFrameLayout.walk(file.getValue());
                var first = walk.counts().get("skipped byte");
                if (first != null) {
                    skipped.merge(first, 1, Integer::sum);
                }
                tally.add(group + "/" + file.getKey(), checkFrame(file.getValue(), walk, held, bases));
            }
        }

        tally.report();
        System.out.println("  the first byte, which the client skips, holds " + skipped);
    }

    private static List<Layout.Mismatch> checkFrame(byte[] data, Layout.Walk walk, Map<Integer, byte[]> held,
                                                    Map<Integer, AnimBase> bases) {
        var mismatches = new ArrayList<Layout.Mismatch>();
        if (!walk.whole()) {
            mismatches.add(walk.mismatch());
        }

        var id = walk.counts().get("base");
        if (id == null) {
            mismatches.add(new Layout.Mismatch("frame too short to name its base", data.length + " bytes"));
        } else if (!held.containsKey(id)) {
            mismatches.add(new Layout.Mismatch("frame names a base the cache does not hold", "base " + id));
        } else {
            try {
                var base = bases.computeIfAbsent(id, unused -> new AnimBase(id, held.get(id)));
                checkAgainstBase(data, walk, base, mismatches);
            } catch (RuntimeException failure) {
                mismatches.add(clientThrows(failure));
            }
        }

        return mismatches;
    }

    /**
     * Decodes a frame with the client's reader, which gives up on any frame it cannot read in
     * full and leaves it with no transforms and no tables, so a frame without its tables is one
     * it refused.
     */
    private static void checkAgainstBase(byte[] data, Layout.Walk walk, AnimBase base, List<Layout.Mismatch> mismatches) {
        var groups = walk.counts().get("groups");
        if (groups != null && groups > base.transformCount) {
            mismatches.add(new Layout.Mismatch("frame flags more transforms than its base has",
                groups + " flagged, base " + base.id + " has " + base.transformCount));
        }

        var frame = new AnimFrame(data, base);
        if (frame.groups == null) {
            mismatches.add(new Layout.Mismatch("client gives up on the frame", "base " + base.id));
        } else {
            compare(walk, Map.of("transforms", frame.transformCount), mismatches);
        }
    }

    /**
     * Every base, by the number a frame names it with. The client takes that number as a file of
     * the archive's only group when it has one group, and as a group of one file otherwise.
     */
    private static Map<Integer, byte[]> basesOf(File cache) throws Exception {
        var index = Cache.index(cache, Js5Archive.BASES);
        var bases = new TreeMap<Integer, byte[]>();

        if (index.fileLimits.length == 1) {
            bases.putAll(Cache.split(Cache.group(cache, Js5Archive.BASES, 0), index, 0));
        } else {
            for (var group : Cache.groupsOf(index)) {
                if (isSingleFile(index, group)) {
                    bases.put(group, Cache.split(Cache.group(cache, Js5Archive.BASES, group), index, group).get(0));
                } else {
                    System.out.println("base group " + group + " holds " + index.fileLimits[group]
                        + " files, which the client cannot read as one base");
                }
            }
        }

        return bases;
    }

    private static boolean isSingleFile(Js5Index index, int group) {
        return index.fileLimits[group] == 1;
    }

    /**
     * Every map square's terrain, which the client keeps as file 0 of a group named for the
     * square.
     */
    private static void terrain(File cache) throws Exception {
        var tally = new Tally("terrain");
        var maps = Cache.js5(cache, Js5Archive.MAPS);
        var index = Cache.index(cache, Js5Archive.MAPS);
        var decoder = new MapSquareDecoder(cache);
        var totals = new TreeMap<String, Integer>();

        for (var square : squaresNamed(index, "m").entrySet()) {
            var group = square.getValue();
            if (index.fileCounts[group] != 1) {
                tally.skip("group holds other than one file");
            } else {
                var mismatches = new ArrayList<>(checkContainer(Cache.packed(cache, Js5Archive.MAPS, group)));
                mismatches.addAll(checkTerrain(maps.getfile(0, group), decoder, totals));
                tally.add(square.getKey(), mismatches);
            }
        }

        tally.report();
        System.out.println("  across every square " + totals);
        reportUnnamedGroups(index);
    }

    private static List<Layout.Mismatch> checkTerrain(byte[] data, MapSquareDecoder decoder, Map<String, Integer> totals) {
        var mismatches = new ArrayList<Layout.Mismatch>();
        var walk = TerrainLayout.walk(data);
        if (!walk.whole()) {
            mismatches.add(walk.mismatch());
        }

        var counts = walk.counts();
        totals.merge("lights", counts.getOrDefault("lights", 0), Integer::sum);
        totals.merge("lights from a light type", counts.getOrDefault("lights from a light type", 0), Integer::sum);
        totals.merge("squares with an environment", counts.getOrDefault("environment", 0), Integer::sum);
        totals.merge("squares with camera heights", Math.min(1, counts.getOrDefault("camera height levels", 0)),
            Integer::sum);

        try {
            compare(walk, decoder.terrain(data), mismatches);
        } catch (RuntimeException failure) {
            mismatches.add(clientThrows(failure));
        }

        return mismatches;
    }

    /**
     * Every map square's locations, which the client keeps as file 0 of a group named for the
     * square and locked with a key of its own.
     *
     * The server hands the client the key of every square it enters, and a key of nothing for a
     * square whose locations are stored open. A square with no key on file is read as the client
     * reads one given a key of nothing, and is only counted, not failed, when that does not open
     * it, because then its key is the thing that is missing.
     */
    private static void locations(File cache, Path keys) throws Exception {
        var tally = new Tally("location");
        var index = Cache.index(cache, Js5Archive.MAPS);
        var locked = 0;
        var open = 0;

        for (var square : squaresNamed(index, "l").entrySet()) {
            var name = square.getKey();
            var group = square.getValue();
            var key = LocationKeys.read(keys, name).orElse(null);
            var packed = Cache.packed(cache, Js5Archive.MAPS, group);
            var data = unlock(packed, key == null ? NO_KEY : key);

            if (index.fileCounts[group] != 1) {
                tally.skip("group holds other than one file");
            } else if (key == null && data == null) {
                tally.skip("no key for the square, and it is not stored open");
            } else if (data == null) {
                tally.add(name, List.of(new Layout.Mismatch("the key does not unlock the group", "group " + group)));
            } else {
                if (key == null || LocationKeys.isOpen(key)) {
                    open++;
                } else {
                    locked++;
                }
                var mismatches = new ArrayList<>(checkContainer(packed));
                mismatches.addAll(checkLocations(data));
                tally.add(name, mismatches);
            }
        }

        tally.report();
        System.out.println("  unlocked with a key: " + locked + ", stored open: " + open);
        System.out.println("  keys read from " + keys);
    }

    private static List<Layout.Mismatch> checkLocations(byte[] data) {
        var mismatches = new ArrayList<Layout.Mismatch>();
        var walk = LocationLayout.walk(data);
        if (!walk.whole()) {
            mismatches.add(walk.mismatch());
        }

        try {
            MapSquareDecoder.locations(data);
            var read = walk.counts().get("bytes read");
            if (read != null) {
                checkClientStopsAt(data, read, mismatches);
            }
        } catch (RuntimeException failure) {
            mismatches.add(clientThrows(failure));
        }

        return mismatches;
    }

    /**
     * Shows that the client's reader stops exactly where the walker did. The reader keeps its
     * position to itself, but it reads past the end of data that is cut short of where it stops
     * and fails, and it does not of data that is not, so cutting the data at the walker's stop
     * and one byte before it shows where the reader stops.
     */
    private static void checkClientStopsAt(byte[] data, int read, List<Layout.Mismatch> mismatches) {
        if (!readsWhole(Arrays.copyOf(data, read))) {
            mismatches.add(new Layout.Mismatch("client reads on past where the walker stops", "walker stops at " + read));
        } else if (read > 0 && readsWhole(Arrays.copyOf(data, read - 1))) {
            mismatches.add(new Layout.Mismatch("client stops before the walker does", "walker stops at " + read));
        }
    }

    private static boolean readsWhole(byte[] data) {
        try {
            MapSquareDecoder.locations(data);
            return true;
        } catch (ArrayIndexOutOfBoundsException overrun) {
            return false;
        }
    }

    /**
     * Unlocks a group and undoes its compression as {@code js5.unpackFile} does, or answers null
     * when the key does not fit it.
     *
     * A stored group is a container: a byte for how it is compressed, four for how long the
     * compressed data is, four more for how long it unpacks to when it is compressed at all, the
     * data, and then the group's two byte version, which is not part of the container. The key
     * locks every whole block of eight bytes from the fifth byte to the end of all of that, so
     * the first five bytes are always open and a tail of up to seven bytes is never locked. The
     * container says how long its data is, and nothing past that is ever unpacked, so the open
     * tail and the version are not part of the file and never come into what is walked.
     *
     * With the wrong key, how long the data unpacks to is noise, and the client would try to
     * make room for it before finding out, so a length past anything a square could hold is taken
     * as the wrong key.
     */
    private static byte[] unlock(byte[] packed, int[] key) {
        var copy = packed.clone();
        if (!LocationKeys.isOpen(key)) {
            new Packet(copy).tinydec(key, copy.length);
        }

        var header = new Packet(copy);
        var compression = header.g1();
        var length = header.g4();
        var unpacked = compression == 0 ? length : header.g4();

        if (length < 0 || unpacked < 0 || unpacked > LARGEST_PLAUSIBLE_GROUP) {
            return null;
        }
        try {
            return js5.decodeContainer(copy);
        } catch (RuntimeException wrongKey) {
            return null;
        }
    }

    /**
     * Checks that a stored group is its container and its version and nothing more. The header
     * that says how long the container is lies outside what any key locks, so this holds whether
     * the group is locked or not.
     */
    private static List<Layout.Mismatch> checkContainer(byte[] packed) {
        var header = new Packet(packed);
        var compression = header.g1();
        var length = header.g4();
        var expected = (compression == 0 ? STORED_HEADER : COMPRESSED_HEADER) + length + VERSION_TRAILER;
        if (packed.length == expected) {
            return List.of();
        } else {
            return List.of(new Layout.Mismatch("stored group is not its container and version",
                packed.length + " bytes stored, container and version need " + expected));
        }
    }

    /**
     * The group of the map each square holds one of, by the name the client asks for it by: the
     * prefix and the square's position across and up. The client asks for a square only by name,
     * so every name a square could have is tried.
     */
    private static Map<String, Integer> squaresNamed(Js5Index index, String prefix) {
        var squares = new TreeMap<String, Integer>();
        for (var x = 0; x < SQUARES_ACROSS; x++) {
            for (var z = 0; z < SQUARES_ACROSS; z++) {
                var name = prefix + x + "_" + z;
                var group = groupNamed(index, name);
                if (group >= 0) {
                    squares.put(name, group);
                }
            }
        }
        return squares;
    }

    /**
     * The group the client finds by a name, as {@code js5.getgroupid} finds it, or -1.
     */
    private static int groupNamed(Js5Index index, String name) {
        var group = index.groupNameTable.find(StringTools.intHashCp1252(name.toLowerCase()));
        if (group < 0 || group >= index.fileCounts.length || index.fileCounts[group] == 0) {
            return -1;
        }
        return group;
    }

    /**
     * Says how many groups of the map no name the client asks for leads to, which no census of
     * the map can reach.
     */
    private static void reportUnnamedGroups(Js5Index index) {
        var named = new HashSet<Integer>();
        var counts = new TreeMap<String, Integer>();
        for (var prefix : MAP_PREFIXES) {
            var squares = squaresNamed(index, prefix);
            named.addAll(squares.values());
            counts.put(prefix, squares.size());
        }
        var unnamed = Arrays.stream(Cache.groupsOf(index)).filter(group -> !named.contains(group)).count();
        System.out.println("  map groups by name " + counts + ", " + unnamed + " groups named by no square");
    }

    /**
     * The table of texture metrics, which the client keeps as file 0 of group 0 of the materials.
     */
    private static void textures(File cache) throws Exception {
        var tally = new Tally("texture");
        var index = Cache.index(cache, Js5Archive.MATERIALS);
        var files = Cache.split(Cache.group(cache, Js5Archive.MATERIALS, 0), index, 0);

        var table = files.get(0);
        tally.add("materials 0/0", checkTextures(table, cache));
        tally.report();
        var counts = TextureMetricsLayout.walk(table).counts();
        System.out.println("  " + counts.get("textures") + " textures, " + counts.get("held") + " held");
        System.out.println("  the materials hold " + index.groupCount + " groups, and group 0 holds files "
            + files.keySet());
    }

    /**
     * Walks the table and sets it against the client's texture source, which reads the same
     * file for itself out of the materials.
     */
    private static List<Layout.Mismatch> checkTextures(byte[] data, File cache) {
        var mismatches = new ArrayList<Layout.Mismatch>();
        var walk = TextureMetricsLayout.walk(data);
        if (!walk.whole()) {
            mismatches.add(walk.mismatch());
        }

        try {
            var source = new Js5TextureSource(Cache.js5(cache, Js5Archive.MATERIALS), null, null);
            compare(walk, textureSums(source), mismatches);
        } catch (RuntimeException failure) {
            mismatches.add(clientThrows(failure));
        }

        return mismatches;
    }

    private static Map<String, Integer> textureSums(Js5TextureSource source) {
        var held = Arrays.stream(source.textureMetrics).filter(Objects::nonNull).toList();
        return Map.of(
            "textures", source.textureCount,
            "held", held.size(),
            "brightness sum", held.stream().mapToInt(metrics -> metrics.brightness & 0xFF).sum(),
            "average colour sum", held.stream().mapToInt(metrics -> metrics.averageColour & 0xFFFF).sum(),
            "effect param 2 sum", held.stream().mapToInt(metrics -> metrics.effectParam2).sum(),
            "alpha blend mode sum", held.stream().mapToInt(metrics -> metrics.alphaBlendMode).sum()
        );
    }

    /**
     * Notes every count the walker reached that the client's reader came to differently.
     */
    private static void compare(Layout.Walk walk, Map<String, Integer> client, List<Layout.Mismatch> mismatches) {
        for (var count : client.entrySet()) {
            var walked = walk.counts().get(count.getKey());
            if (walked != null && !walked.equals(count.getValue())) {
                mismatches.add(new Layout.Mismatch("walker and client disagree on " + count.getKey(),
                    "walker " + walked + ", client " + count.getValue()));
            }
        }
    }

    private static Layout.Mismatch clientThrows(RuntimeException failure) {
        return new Layout.Mismatch("client throws " + failure.getClass().getSimpleName(), failure.toString());
    }

    private static int lengthOf(Object[] array) {
        return array == null ? 0 : array.length;
    }

    private static int lengthOf(int[] array) {
        return array == null ? 0 : array.length;
    }

    private CacheLayoutCensus() {
        /* empty */
    }
}
