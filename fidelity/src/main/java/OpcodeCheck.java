import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.spi.ToolProvider;

/**
 * Compares recompiled classes against the same classes in the jar this source was recovered from.
 * <p>
 * This answers one question: did a wave of renaming, or the round trip through a decompiler,
 * change what the code computes? Nothing else in the build can answer it. The source no longer
 * resembles the jar, so the two cannot be matched by name, and the obfuscator's dummy parameters
 * were removed, so they cannot be matched by descriptor either. The {@code @OriginalMember}
 * annotations record what every member used to be called, and they are the only thing that still
 * relates the two.
 * <p>
 * Only the opcodes that decide a value are compared. The deobfuscator inverts branches, reorders
 * independent statements, removes dead arithmetic on dummy parameters and unwinds the obfuscator's
 * comparison tricks. None of that changes what a method computes and all of it shows up in a plain
 * diff, which is why a plain diff of these classes says nothing.
 * <p>
 * Two things this cannot tell apart, so read a report with them in mind:
 * <ul>
 *   <li>Independent statements evaluated in a different order look the same as arithmetic that
 *   has been re-associated. The first is harmless, the second is not, and both appear as a hunk
 *   with the same number of opcodes on either side.</li>
 *   <li>A method the annotations cannot match is not compared at all. The count of those is
 *   reported rather than hidden.</li>
 * </ul>
 * The {@code verifyOpcodes} task works out the triples and runs it.
 */
public final class OpcodeCheck {

    /**
     * The opcodes that decide a value. Loads, stores, branches and stack shuffling are all
     * restructured without changing an answer, so they are left out.
     */
    private static final Set<String> VALUE = Set.of(
        "idiv", "ishr", "iushr", "ishl", "irem", "imul", "isub", "iadd", "ineg", "iand", "ior",
        "ixor", "i2s", "i2c", "i2b", "i2l", "i2f", "i2d", "f2i", "f2l", "f2d", "d2i", "d2f", "l2i",
        "fadd", "fsub", "fmul", "fdiv", "fneg", "dadd", "dsub", "dmul", "ddiv",
        "ladd", "lsub", "lmul", "ldiv", "lshl", "lshr", "lushr");

    private static final Pattern MEMBER = Pattern.compile("  \\S.*;\\s*");
    private static final Pattern OPCODE = Pattern.compile("\\s+\\d+: (\\w+)");
    private static final Pattern NAME = Pattern.compile("([\\w$<>]+)\\(");
    private static final Pattern ANNOTATED = Pattern.compile(
        "@OriginalMember\\(owner = \"[^\"]+\", name = \"([^\"]+)\", "
            + "descriptor = \"([^\"]+)\"\\)"
            + "(.*?)\\n\\s*"
            + "(?:public|private|protected|static|final|abstract|native|synchronized|\\w)"
            + "[^\\n(]*?([\\w$<>]+)\\s*\\(",
        Pattern.DOTALL);

    /**
     * How many lines of a method's difference are shown. The rest of a long one says nothing the
     * first few did not.
     */
    private static final int SAMPLE = 30;

    private static final int WRONG_USAGE = 2;

    private static final String USAGE = "Usage: OpcodeCheck [--] <source.java> "
        + "<original.class> <recompiled.class> [...]";

    /**
     * A method, as its javap signature and descriptor and the value opcodes it runs in order.
     */
    private record Method(String signature, String descriptor, List<String> opcodes) {

        Key key() {
            var name = NAME.matcher(signature);
            return new Key(name.find() ? name.group(1) : "?", descriptor);
        }
    }

    private record Key(String name, String descriptor) {
    }

    /**
     * What a method was called in the jar, and the descriptor it had there.
     */
    private record Original(String name, String descriptor) {
    }

    private record Tally(int same, int differ, int unmatched, List<String> lines) {
    }

    public static void main(String[] arguments) throws IOException {
        var given = arguments.length > 0 && arguments[0].equals("--")
            ? Arrays.copyOfRange(arguments, 1, arguments.length)
            : arguments;

        if (given.length == 0 || given.length % 3 != 0) {
            System.out.println(USAGE);
            System.exit(WRONG_USAGE);
        } else {
            report(given);
        }
    }

    private static void report(String[] triples) throws IOException {
        var same = 0;
        var differ = 0;
        var unmatched = 0;

        for (var at = 0; at < triples.length; at += 3) {
            var source = Path.of(triples[at]);
            var tally = compare(source, Path.of(triples[at + 1]), Path.of(triples[at + 2]));

            same += tally.same();
            differ += tally.differ();
            unmatched += tally.unmatched();

            if (!tally.lines().isEmpty()) {
                System.out.println(source.getFileName() + ":");
                System.out.println(String.join("\n", tally.lines()));
            }
        }

        System.out.printf("%d methods compute what they did, %d differ, %d could not be matched%n",
            same, differ, unmatched);
    }

    /**
     * Reports every method of one class whose arithmetic changed, and counts how many did.
     */
    private static Tally compare(Path source, Path original, Path recompiled) throws IOException {
        var wanted = new TreeMap<String, Original>();
        var annotated = ANNOTATED.matcher(Files.readString(source));
        while (annotated.find()) {
            if (annotated.group(2).contains("(")) {
                var was = new Original(annotated.group(1), annotated.group(2));
                wanted.put(annotated.group(4), was);
            }
        }

        var before = byKey(methods(original));
        var after = byKey(methods(recompiled));

        var same = 0;
        var differ = 0;
        var unmatched = 0;
        var lines = new ArrayList<String>();

        for (var entry : wanted.entrySet()) {
            var ours = entry.getKey();
            var was = entry.getValue();
            var mine = after.entrySet().stream()
                .filter(held -> held.getKey().name().equals(ours))
                .flatMap(held -> held.getValue().stream())
                .toList();
            var theirs = before.getOrDefault(new Key(was.name(), was.descriptor()), List.of());

            if (mine.size() != 1 || theirs.size() != 1) {
                unmatched++;
            } else if (theirs.get(0).opcodes().equals(mine.get(0).opcodes())) {
                same++;
            } else {
                differ++;
                var hunks = Difference.unified(theirs.get(0).opcodes(), mine.get(0).opcodes());
                lines.add("    " + ours + ", was " + was.name() + was.descriptor());
                var shown = hunks.subList(0, Math.min(SAMPLE, hunks.size()));
                lines.add("      " + String.join(" ", shown));
            }
        }

        return new Tally(same, differ, unmatched, List.copyOf(lines));
    }

    private static Map<Key, List<Method>> byKey(List<Method> methods) {
        var held = new LinkedHashMap<Key, List<Method>>();
        for (var method : methods) {
            held.computeIfAbsent(method.key(), ignored -> new ArrayList<>()).add(method);
        }
        return held;
    }

    /**
     * Every method of a class, as the value opcodes it runs, read out of what javap prints.
     */
    private static List<Method> methods(Path path) {
        var lines = javap(path).lines().toList();
        var methods = new ArrayList<Method>();

        String signature = null;
        String descriptor = null;
        var opcodes = new ArrayList<String>();

        for (var line : lines) {
            if (MEMBER.matcher(line).matches()) {
                if (signature != null && signature.contains("(")) {
                    methods.add(new Method(signature, descriptor, List.copyOf(opcodes)));
                }
                signature = line.strip();
                descriptor = null;
                opcodes.clear();
            } else if (signature != null) {
                var trimmed = line.strip();
                if (trimmed.startsWith("descriptor:") && descriptor == null) {
                    descriptor = trimmed.substring("descriptor:".length()).strip();
                } else {
                    var opcode = OPCODE.matcher(line);
                    if (opcode.lookingAt() && VALUE.contains(opcode.group(1))) {
                        opcodes.add(opcode.group(1));
                    }
                }
            }
        }

        if (signature != null && signature.contains("(")) {
            methods.add(new Method(signature, descriptor, List.copyOf(opcodes)));
        }
        return List.copyOf(methods);
    }

    private static String javap(Path path) {
        var tool = ToolProvider.findFirst("javap")
            .orElseThrow(() -> new IllegalStateException("This JDK has no javap."));
        var printed = new ByteArrayOutputStream();
        var failed = new ByteArrayOutputStream();

        int status;
        try (var out = new PrintStream(printed, true, StandardCharsets.UTF_8);
             var err = new PrintStream(failed, true, StandardCharsets.UTF_8)) {
            status = tool.run(out, err, "-p", "-c", "-s", path.toString());
        }

        if (status != 0) {
            throw new IllegalStateException("javap could not read " + path + ": "
                + failed.toString(StandardCharsets.UTF_8));
        }
        return printed.toString(StandardCharsets.UTF_8);
    }

    /**
     * The difference between two runs of opcodes, as a unified diff with no lines of context.
     * <p>
     * This is Python's difflib, the matcher and the diff both, carried across line for line. The
     * reports this writes have been read and kept for as long as the script it replaces existed, so
     * a difference found here has to come out in the same hunks as it did there. A different way of
     * finding the longest common run would change which opcodes are paired, and the hunks with it.
     */
    private static final class Difference {

        /**
         * A sequence this long or longer has any element that appears more often than one in a
         * hundred times treated as noise rather than matched on.
         */
        private static final int AUTOJUNK_FROM = 200;

        private record Block(int a, int b, int size) {
        }

        private record Code(String tag, int a1, int a2, int b1, int b2) {

            /**
             * A run of nothing that matches, which is what a hunk with no context starts from.
             */
            static Code empty(int a, int b) {
                return new Code("equal", a, a, b, b);
            }
        }

        private final List<String> a;
        private final List<String> b;
        private final Map<String, List<Integer>> where = new HashMap<>();

        private Difference(List<String> a, List<String> b) {
            this.a = a;
            this.b = b;

            for (var at = 0; at < b.size(); at++) {
                where.computeIfAbsent(b.get(at), ignored -> new ArrayList<>()).add(at);
            }

            if (b.size() >= AUTOJUNK_FROM) {
                var most = b.size() / 100 + 1;
                where.values().removeIf(found -> found.size() > most);
            }
        }

        /**
         * The lines of a unified diff with no context, without the two lines that name the files.
         */
        static List<String> unified(List<String> a, List<String> b) {
            var difference = new Difference(a, b);
            var lines = new ArrayList<String>();

            for (var group : difference.grouped()) {
                var first = group.get(0);
                var last = group.get(group.size() - 1);
                var before = range(first.a1(), last.a2());
                var after = range(first.b1(), last.b2());
                lines.add("@@ -" + before + " +" + after + " @@");

                for (var code : group) {
                    if (code.tag().equals("equal")) {
                        for (var line : a.subList(code.a1(), code.a2())) {
                            lines.add(" " + line);
                        }
                    } else {
                        if (code.tag().equals("replace") || code.tag().equals("delete")) {
                            for (var line : a.subList(code.a1(), code.a2())) {
                                lines.add("-" + line);
                            }
                        }
                        if (code.tag().equals("replace") || code.tag().equals("insert")) {
                            for (var line : b.subList(code.b1(), code.b2())) {
                                lines.add("+" + line);
                            }
                        }
                    }
                }
            }
            return List.copyOf(lines);
        }

        private static String range(int start, int stop) {
            var beginning = start + 1;
            var length = stop - start;

            if (length == 1) {
                return Integer.toString(beginning);
            } else if (length == 0) {
                return (beginning - 1) + "," + length;
            } else {
                return beginning + "," + length;
            }
        }

        private Block longestMatch(int aLow, int aHigh, int bLow, int bHigh) {
            var bestA = aLow;
            var bestB = bLow;
            var bestSize = 0;
            var lengths = new HashMap<Integer, Integer>();

            for (var i = aLow; i < aHigh; i++) {
                var next = new HashMap<Integer, Integer>();
                for (var j : where.getOrDefault(a.get(i), List.of())) {
                    if (j >= bLow && j < bHigh) {
                        var k = lengths.getOrDefault(j - 1, 0) + 1;
                        next.put(j, k);
                        if (k > bestSize) {
                            bestA = i - k + 1;
                            bestB = j - k + 1;
                            bestSize = k;
                        }
                    }
                }
                lengths = next;
            }

            while (bestA > aLow && bestB > bLow && a.get(bestA - 1).equals(b.get(bestB - 1))) {
                bestA--;
                bestB--;
                bestSize++;
            }
            while (bestA + bestSize < aHigh && bestB + bestSize < bHigh
                && a.get(bestA + bestSize).equals(b.get(bestB + bestSize))) {
                bestSize++;
            }

            return new Block(bestA, bestB, bestSize);
        }

        private List<Block> matchingBlocks() {
            var found = new ArrayList<Block>();
            var queue = new ArrayList<int[]>();
            queue.add(new int[] {0, a.size(), 0, b.size()});

            while (!queue.isEmpty()) {
                var span = queue.remove(queue.size() - 1);
                var match = longestMatch(span[0], span[1], span[2], span[3]);

                if (match.size() > 0) {
                    found.add(match);
                    if (span[0] < match.a() && span[2] < match.b()) {
                        queue.add(new int[] {span[0], match.a(), span[2], match.b()});
                    }
                    if (match.a() + match.size() < span[1] && match.b() + match.size() < span[3]) {
                        queue.add(new int[] {match.a() + match.size(), span[1],
                            match.b() + match.size(), span[3]});
                    }
                }
            }

            found.sort(Comparator.comparingInt(Block::a).thenComparingInt(Block::b)
                .thenComparingInt(Block::size));

            var joined = new ArrayList<Block>();
            var held = new Block(0, 0, 0);
            for (var block : found) {
                if (held.a() + held.size() == block.a() && held.b() + held.size() == block.b()) {
                    held = new Block(held.a(), held.b(), held.size() + block.size());
                } else {
                    if (held.size() > 0) {
                        joined.add(held);
                    }
                    held = block;
                }
            }
            if (held.size() > 0) {
                joined.add(held);
            }
            joined.add(new Block(a.size(), b.size(), 0));
            return joined;
        }

        private List<Code> codes() {
            var codes = new ArrayList<Code>();
            var i = 0;
            var j = 0;

            for (var block : matchingBlocks()) {
                var tag = tagBefore(block, i, j);
                if (tag != null) {
                    codes.add(new Code(tag, i, block.a(), j, block.b()));
                }
                i = block.a() + block.size();
                j = block.b() + block.size();
                if (block.size() > 0) {
                    codes.add(new Code("equal", block.a(), i, block.b(), j));
                }
            }
            return codes;
        }

        /**
         * What happened between where the last matching block ended and where this one starts, or
         * null when this one starts right where the last ended.
         */
        private static String tagBefore(Block block, int i, int j) {
            if (i < block.a() && j < block.b()) {
                return "replace";
            } else if (i < block.a()) {
                return "delete";
            } else if (j < block.b()) {
                return "insert";
            } else {
                return null;
            }
        }

        /**
         * The codes cut into hunks, with no context either side of a change.
         */
        private List<List<Code>> grouped() {
            var codes = codes();
            if (codes.isEmpty()) {
                codes.add(new Code("equal", 0, 1, 0, 1));
            }

            var head = codes.get(0);
            if (head.tag().equals("equal")) {
                codes.set(0, Code.empty(head.a2(), head.b2()));
            }
            var tail = codes.get(codes.size() - 1);
            if (tail.tag().equals("equal")) {
                codes.set(codes.size() - 1, Code.empty(tail.a1(), tail.b1()));
            }

            var groups = new ArrayList<List<Code>>();
            var group = new ArrayList<Code>();
            for (var code : codes) {
                var current = code;
                if (current.tag().equals("equal") && current.a2() - current.a1() > 0) {
                    group.add(Code.empty(current.a1(), current.b1()));
                    groups.add(group);
                    group = new ArrayList<>();
                    current = Code.empty(current.a2(), current.b2());
                }
                group.add(current);
            }

            var onlyEqual = group.size() == 1 && group.get(0).tag().equals("equal");
            if (!group.isEmpty() && !onlyEqual) {
                groups.add(group);
            }
            return groups;
        }
    }

    private OpcodeCheck() {
        /* empty */
    }
}
