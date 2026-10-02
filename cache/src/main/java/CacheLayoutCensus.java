import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.AnimBase;
import com.jagex.AnimFrame;
import com.jagex.graphics.Mesh;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5Index;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Checks that every model, animation base and animation frame in the cache is read in full.
 *
 * These formats are not lists of opcodes, so the config census cannot step through them. Each is
 * instead cut into the sections its reader works through, and a walker that reads only what
 * decides where each section ends says whether every section stops where the next one begins.
 * The client's own reader decodes the same bytes, and the counts it comes to are set against the
 * walker's, which shows the walker follows the same structure the client does.
 */
public final class CacheLayoutCensus {

    private static final List<String> FORMATS = List.of("model", "base", "frame");

    /**
     * How many failing items of one kind are named before the rest are only counted.
     */
    private static final int NAMED_FAILURES = 5;

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs cache = new CacheArgs();

        @Parameter(names = "--format", description = "A format to check: model, base or frame; repeat for several, omit for all")
        private List<String> formats = new ArrayList<>();

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

        private void report() {
            System.out.println(format + ": " + items + " items, " + (items - failed) + " read in full, "
                + failed + " not");
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
