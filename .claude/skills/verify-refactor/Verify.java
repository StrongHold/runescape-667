import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Proves that a deobfuscation wave changed only names, by comparing the compiled bytecode of every
 * changed class against the bytecode that same class had at a baseline commit.
 * <p>
 * A rename cannot change what the JVM does, so a renamed class must compile to semantically
 * identical bytecode. Four kinds of difference do follow from a rename, and are normalised away
 * before comparing: identifier text, constant pool indices and the instruction width that follows
 * from them, absolute bytecode offsets, and local variable slot numbers.
 * <p>
 * Run with the JDK source launcher, which needs no build step and no dependency beyond the JDK:
 * <pre>java .claude/skills/verify-refactor/Verify.java [baseline-ref]</pre>
 */
public final class Verify {

    private static final String CLASSES = "runescape/build/classes/java/main";

    /** Instructions that carry a branch target, which is an absolute offset into the method. */
    private static final Pattern BRANCH =
        Pattern.compile("^(goto|goto_w|jsr|jsr_w|if[a-z_]+|tableswitch|lookupswitch)\\b.*");

    /** The three instruction shapes that name a local variable by its slot number. */
    private static final Pattern SLOT =
        Pattern.compile("^([adfil](?:load|store))(?:_(\\d)\\b|\\s+(\\d+))\\s*$");
    private static final Pattern INCREMENT = Pattern.compile("^iinc\\s+(\\d+),\\s*(-?\\d+)\\s*$");
    private static final Pattern SUBROUTINE = Pattern.compile("^ret\\s+(\\d+)\\s*$");

    private static final Pattern CODE = Pattern.compile("^\\s{4,}(\\d+): (.+)$");
    private static final Pattern POOL = Pattern.compile("#\\d+,?\\s*");
    private static final Pattern NUMBER = Pattern.compile("\\b\\d+\\b");

    /**
     * Wide instruction forms differ from their narrow ones only by how large a constant pool index
     * they can reach, which a rename changes whenever it changes the size of the pool.
     */
    private static final Map<String, String> NARROW = Map.of(
        "ldc_w", "ldc",
        "ldc2_w", "ldc",
        "goto_w", "goto",
        "jsr_w", "jsr"
    );

    private record Instruction(int offset, String text) { /* a single line of javap output */ }

    private record Body(String signature, List<Instruction> instructions) { /* one method */ }

    private record Result(String name, List<String> problems) {

        boolean identical() {
            return this.problems.isEmpty();
        }
    }

    private Verify() {
        /* empty */
    }

    public static void main(String[] args) throws Exception {
        @SuppressWarnings("resource")
        var baseline = args.length > 0 ? args[0] : "HEAD~1";
        var repo = Path.of(run(Path.of("."), "git", "rev-parse", "--show-toplevel").strip());
        var worktree = Files.createTempDirectory("verify-refactor");

        System.out.printf("baseline %s   current %s%n",
            run(repo, "git", "rev-parse", "--short", baseline).strip(),
            run(repo, "git", "rev-parse", "--short", "HEAD").strip());

        var changed = changedSources(repo, baseline);
        if (changed.isEmpty()) {
            System.out.println("no java files changed since " + baseline);
        } else {
            System.out.println(changed.size() + " changed java files");
            report(repo, worktree, baseline, changed);
        }
    }

    private static void report(Path repo, Path worktree, String baseline, List<String> changed)
        throws Exception {

        run(repo, "git", "worktree", "add", "-q", "--detach", worktree.toString(), baseline);
        try {
            System.out.println("building baseline...");
            compile(worktree);
            System.out.println("building current...");
            compile(repo);

            var results = new ArrayList<Result>();
            for (var source : changed) {
                results.addAll(compare(repo, worktree, source));
            }
            print(results);
        } finally {
            run(repo, "git", "worktree", "remove", "--force", worktree.toString());
        }
    }

    private static void print(List<Result> results) {
        var identical = results.stream().filter(Result::identical).count();
        results.stream().filter(result -> !result.identical()).forEach(result -> {
            System.out.println("CHANGED " + result.name());
            result.problems().forEach(problem -> System.out.println("  " + problem));
        });

        System.out.printf("%nidentical %d   changed %d%n", identical, results.size() - identical);

        if (identical != results.size()) {
            System.out.println("""

                A changed body is not proof of a bug. It means the edit was more than a rename, so a
                person has to read it. The usual innocent cause is a declaration moved into a
                narrower scope, which renumbers the slots and moves the loop setup. Read the source
                diff for each class above and confirm that no value carried across the old scope.""");
        }
    }

    /**
     * Uncommitted work counts as changed, so that a wave can be checked before it is committed
     * rather than only afterwards.
     */
    private static List<String> changedSources(Path repo, String baseline) throws Exception {
        return Stream.of(
                run(repo, "git", "diff", "--name-only", baseline, "--", "*.java"),
                run(repo, "git", "diff", "--name-only", "--", "*.java"))
            .flatMap(String::lines)
            .map(String::strip)
            .filter(line -> !line.isEmpty())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    /**
     * Compares one source file, and every nested and anonymous class it compiles to, against the
     * baseline build.
     */
    private static List<Result> compare(Path repo, Path worktree, String source) throws Exception {
        var stem = source.replaceFirst("^runescape/src/main/java/", "").replaceFirst("\\.java$", "");
        var simple = Path.of(stem).getFileName().toString();
        var directory = repo.resolve(CLASSES).resolve(stem).getParent();

        if (!Files.isDirectory(directory)) {
            return List.of();
        } else {
            try (var files = Files.list(directory)) {
                return files
                    .map(file -> file.getFileName().toString())
                    .filter(file -> file.equals(simple + ".class") || file.startsWith(simple + "$"))
                    .filter(file -> file.endsWith(".class"))
                    .sorted()
                    .map(file -> compareClass(repo, worktree, directory, file))
                    .collect(Collectors.toList());
            }
        }
    }

    private static Result compareClass(Path repo, Path worktree, Path directory, String file) {
        var relative = repo.resolve(CLASSES).relativize(directory.resolve(file));
        var name = relative.toString().replaceFirst("\\.class$", "");
        var before = worktree.resolve(CLASSES).resolve(relative);
        var after = directory.resolve(file);

        if (!Files.exists(before)) {
            return new Result(name, List.of("the baseline build has no such class"));
        } else {
            return new Result(name, differences(bodies(before), bodies(after)));
        }
    }

    /**
     * Reports every method body that no baseline body matches. Methods are paired by shape and then
     * by content rather than by position, so that a reordered source file is not a difference.
     */
    private static List<String> differences(Map<String, List<List<String>>> before,
                                            Map<String, List<List<String>>> after) {
        var problems = new ArrayList<String>();
        var signatures = Stream.concat(before.keySet().stream(), after.keySet().stream())
            .distinct().sorted().toList();

        for (var signature : signatures) {
            var left = new ArrayList<>(before.getOrDefault(signature, List.of()));
            var right = after.getOrDefault(signature, List.of());

            if (left.size() != right.size()) {
                problems.add("%s: %d methods before, %d after"
                    .formatted(signature, left.size(), right.size()));
            } else {
                right.stream()
                    .filter(body -> !left.remove(body))
                    .forEach(body -> problems.add("%s: a body changed, %d instructions"
                        .formatted(signature, body.size())));
            }
        }
        return problems;
    }

    private static Map<String, List<List<String>>> bodies(Path classFile) {
        var grouped = new LinkedHashMap<String, List<List<String>>>();
        for (var body : parse(classFile)) {
            grouped.computeIfAbsent(body.signature(), key -> new ArrayList<>())
                .add(normalise(body.instructions()));
        }
        return grouped;
    }

    private static List<Body> parse(Path classFile) {
        var output = disassemble(classFile);
        var bodies = new ArrayList<Body>();
        var instructions = new ArrayList<Instruction>();
        String signature = null;

        for (var line : output.split("\n", -1)) {
            var code = CODE.matcher(line);
            if (code.matches()) {
                instructions.add(new Instruction(
                    Integer.parseInt(code.group(1)), code.group(2).strip()));
            } else {
                var declaration = line.strip();
                if (declaration.endsWith(";") && declaration.contains("(")) {
                    if (signature != null) {
                        bodies.add(new Body(signature, List.copyOf(instructions)));
                    }
                    signature = shape(declaration);
                    instructions = new ArrayList<>();
                }
            }
        }

        if (signature != null) {
            bodies.add(new Body(signature, List.copyOf(instructions)));
        }
        return bodies;
    }

    /**
     * Reduces a method declaration to its arity and modifiers. The method name and its parameter
     * type names are exactly what a rename is allowed to change, so neither can take part in
     * matching one method to another.
     */
    private static String shape(String declaration) {
        var modifiers = Stream.of("static", "abstract", "native", "synchronized")
            .filter(word -> declaration.matches(".*\\b" + word + "\\b.*"))
            .collect(Collectors.joining(" "));
        var arguments = declaration.substring(
            declaration.indexOf('(') + 1, declaration.lastIndexOf(')')).strip();
        var arity = arguments.isEmpty() ? 0 : arguments.split(",").length;
        return modifiers + "/" + arity;
    }

    /** Rewrites one method body so that only what the JVM does with it remains. */
    private static List<String> normalise(List<Instruction> instructions) {
        var positions = new HashMap<Integer, Integer>();
        for (var i = 0; i < instructions.size(); i++) {
            positions.put(instructions.get(i).offset(), i);
        }

        var slots = new HashMap<String, Integer>();
        var normalised = new ArrayList<String>(instructions.size());

        for (var position = 0; position < instructions.size(); position++) {
            var text = instructions.get(position).text();
            var opcode = text.split("\\s+")[0];
            text = NARROW.getOrDefault(opcode, opcode) + text.substring(opcode.length());

            // Everything after the comment marker is javap resolving a pool entry back to a name,
            // and a name is what a rename changes.
            text = POOL.matcher(text.split("//")[0]).replaceAll("").strip();

            if (BRANCH.matcher(text).matches()) {
                text = relative(text, positions, position);
            }
            normalised.add(local(text, slots));
        }
        return List.copyOf(normalised);
    }

    /**
     * Restates a branch target as a distance in instructions. The target javap prints is an absolute
     * offset, so it moves whenever any instruction above it changes width.
     */
    private static String relative(String text, Map<Integer, Integer> positions, int position) {
        var matcher = NUMBER.matcher(text);
        var rewritten = new StringBuilder();
        while (matcher.find()) {
            var target = positions.get(Integer.parseInt(matcher.group()));
            var distance = target == null ? "?" : String.valueOf(target - position);
            matcher.appendReplacement(rewritten, Matcher.quoteReplacement("@" + distance));
        }
        matcher.appendTail(rewritten);
        return rewritten.toString();
    }

    /**
     * Renumbers a local by when the method first uses it, rather than by the slot javac gave it.
     * Erasing slot numbers outright would hide a rename that swapped two variables. Numbering them
     * by first use keeps that visible, because two instructions that shared a slot before must still
     * share one as each other afterwards.
     */
    private static String local(String text, Map<String, Integer> slots) {
        var slot = SLOT.matcher(text);
        var increment = INCREMENT.matcher(text);
        var subroutine = SUBROUTINE.matcher(text);

        if (slot.matches()) {
            var number = Optional.ofNullable(slot.group(2)).orElseGet(() -> slot.group(3));
            return slot.group(1) + " %" + canonical(slots, number);
        } else if (increment.matches()) {
            return "iinc %" + canonical(slots, increment.group(1)) + ", " + increment.group(2);
        } else if (subroutine.matches()) {
            return "ret %" + canonical(slots, subroutine.group(1));
        } else {
            return text;
        }
    }

    private static int canonical(Map<String, Integer> slots, String number) {
        return slots.computeIfAbsent(number, key -> slots.size());
    }

    /**
     * The JDK that runs this program is the one mise pinned, because the source launcher was
     * started from an activated shell. Resolving the tools against {@code java.home} rather than
     * against the path therefore applies the pinned toolchain without naming mise or an install
     * location, and it guarantees that the disassembler matches the compiler.
     */
    private static Path tool(String name) {
        return Path.of(System.getProperty("java.home"), "bin", name);
    }

    private static String disassemble(Path classFile) {
        try {
            return run(classFile.getParent(),
                tool("javap").toString(), "-p", "-c", classFile.toString());
        } catch (Exception e) {
            throw new IllegalStateException("cannot disassemble " + classFile, e);
        }
    }

    private static void compile(Path directory) throws Exception {
        var gradle = new ProcessBuilder(
            directory.resolve("gradlew").toString(),
            "runescape:compileJava", "--console=plain", "-q")
            .directory(directory.toFile())
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .redirectError(ProcessBuilder.Redirect.DISCARD);

        gradle.environment().put("JAVA_HOME", System.getProperty("java.home"));

        if (gradle.start().waitFor() != 0) {
            throw new IllegalStateException(directory + " does not compile");
        }
    }

    private static String run(Path directory, String... command) throws IOException,
        InterruptedException {

        var process = new ProcessBuilder(command)
            .directory(directory.toFile())
            .redirectErrorStream(true)
            .start();
        var output = new String(process.getInputStream().readAllBytes());
        process.waitFor();
        return output;
    }
}
