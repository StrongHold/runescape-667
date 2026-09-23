import com.beust.jcommander.Parameter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Compares what each recompiled method computes with what it computed in the jar, as trees of
 * arithmetic.
 *
 * The opcode comparison in {@code tools/Fidelity.java} cannot tell a harmless reordering of
 * statements from a re-association of floating point arithmetic, and only the second changes an
 * answer. Here every value a method lets out is written as the tree that made it, on both sides,
 * with every field, call and parameter named the way the jar named it. A tree found on one side
 * and not the other is a change in what the method computes.
 *
 * By default only trees that round to floating point are reported, because integer arithmetic
 * that has been rearranged still gives the same answer and is put into one form before the two
 * sides are compared. Ask for every tree to see the rest.
 */
public final class ExpressionCheck {

    /**
     * How deep a tree is written out. Deeper than this, a difference is still reported, but the
     * rest of the tree is left as an ellipsis.
     */
    private static final int DEPTH = 7;

    /**
     * How deep a place where the two sides part is written out, which needs only enough to find it
     * in the source.
     */
    private static final int DIVERGENCE_DEPTH = 4;

    private static final Pattern ORIGINAL_PARAMETER = Pattern.compile("param (\\d+)");

    public static final class Args implements Arguments {

        @Parameter(names = "--recompiled", required = true,
            description = "The directory of recompiled class files")
        private File recompiled;

        @Parameter(names = "--original", required = true,
            description = "The directory the original jar is unpacked in")
        private File original;

        @Parameter(names = "--class",
            description = "A recompiled class to check; repeat for more, omit to check them all")
        private List<String> classes = List.of();

        @Parameter(names = "--every",
            description = "With --whole, report integer arithmetic as well as floating point")
        private boolean every;

        @Parameter(names = "--whole",
            description = "Write out every tree that differs, rather than where the two sides part")
        private boolean whole;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    private record Difference(String method, List<String> lost, List<String> gained) {
        /* empty */
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("verifyExpressions", new Args(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(Args args) {
        var naming = Naming.read(args.recompiled.toPath(), args.original.toPath());
        var names = args.classes.isEmpty() ? naming.recompiledNames() : args.classes;
        var compared = 0;
        var unmatched = 0;
        var differing = 0;

        for (var name : names.stream().sorted().toList()) {
            var node = naming.recompiledClass(name)
                .orElseThrow(() -> new IllegalArgumentException("No recompiled class is called " + name + "."));
            var differences = new ArrayList<Difference>();

            for (var method : node.methods) {
                var was = naming.originalOf(node.name, method).flatMap(member ->
                    naming.originalMethod(member).map(found -> new Original(member, found)));

                if (was.isEmpty()) {
                    unmatched++;
                } else {
                    compared++;
                    difference(naming, node, method, was.get(), args).ifPresent(differences::add);
                }
            }

            if (!differences.isEmpty()) {
                differing += differences.size();
                System.out.println(name + ":");
                for (var difference : differences) {
                    System.out.println("    " + difference.method());
                    for (var line : difference.lost()) {
                        System.out.println("      - " + line);
                    }
                    for (var line : difference.gained()) {
                        System.out.println("      + " + line);
                    }
                }
            }
        }

        System.out.println(compared + " methods compared, " + differing + " compute something different, "
            + unmatched + " could not be matched to the jar");
    }

    private record Original(Naming.Member member, MethodNode method) {
        /* empty */
    }

    private static Optional<Difference> difference(Naming naming, ClassNode owner, MethodNode method,
                                                   Original was, Args args) {
        var placed = naming.argumentsOf(method);
        var parameters = new HashSet<String>();
        for (var place : placed) {
            place.ifPresent(at -> parameters.add("param " + at));
        }

        Set<Expression> recompiled;
        Set<Expression> original;
        try {
            recompiled = Outcome.of(owner.name, method, new RecompiledSide(naming, placed));
            original = Outcome.of(was.member().owner(), was.method(), new OriginalSide(naming)).stream()
                .filter(tree -> keepsOnlyRealParameters(tree, parameters))
                .collect(Collectors.toUnmodifiableSet());
        } catch (AnalyzerException failure) {
            return Optional.of(new Difference(describe(method, was),
                List.of("could not be run: " + failure.getMessage()), List.of()));
        }

        var lost = args.whole
            ? wholeTrees(original, recompiled, args.every, naming)
            : divergences(original, recompiled, naming);
        var gained = args.whole
            ? wholeTrees(recompiled, original, args.every, naming)
            : divergences(recompiled, original, naming);

        if (lost.isEmpty() && gained.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new Difference(describe(method, was), lost, gained));
        }
    }

    /**
     * Every tree one side lets out that the other does not, written out whole.
     */
    private static List<String> wholeTrees(Set<Expression> side, Set<Expression> other, boolean every,
                                           Naming naming) {
        return side.stream()
            .filter(tree -> !other.contains(tree))
            .filter(tree -> every || tree.floating())
            .sorted(Comparator.comparingInt(Expression::id))
            .map(tree -> tree.render(DEPTH, naming))
            .toList();
    }

    /**
     * The places where one side's floating point arithmetic stops matching the other's.
     *
     * A change deep in a tree changes every tree above it, so reporting every tree that differs
     * buries the change under its consequences. A rounding node is reported here only when each
     * rounding node it is made from is found on both sides, which is the lowest point at which
     * the two sides part.
     */
    private static List<String> divergences(Set<Expression> side, Set<Expression> other, Naming naming) {
        var ours = rounding(side);
        var theirs = rounding(other);

        return ours.stream()
            .filter(node -> !theirs.contains(node))
            .filter(node -> node.operands().stream()
                .filter(Expression::rounds)
                .allMatch(theirs::contains))
            .sorted(Comparator.comparingInt(Expression::id))
            .map(node -> node.render(DIVERGENCE_DEPTH, naming))
            .toList();
    }

    private static Set<Expression> rounding(Set<Expression> trees) {
        var found = new HashSet<Expression>();
        for (var tree : trees) {
            found.addAll(tree.rounding());
        }
        return Set.copyOf(found);
    }

    /**
     * Whether a tree from the jar reads only parameters the recompiled method still has.
     *
     * The obfuscator gave many methods a parameter that does nothing but guard a branch no call
     * takes, and those parameters were removed. What the jar did with one is not a difference.
     */
    private static boolean keepsOnlyRealParameters(Expression tree, Set<String> kept) {
        return tree.leaves().stream()
            .filter(leaf -> ORIGINAL_PARAMETER.matcher(leaf).matches())
            .allMatch(kept::contains);
    }

    private static String describe(MethodNode method, Original was) {
        return method.name + ", was " + was.member().name() + was.member().descriptor();
    }

    /**
     * Names what a recompiled method refers to by what it was called in the jar.
     */
    private record RecompiledSide(Naming naming, List<Optional<Integer>> placed) implements Symbolic.Side {

        @Override
        public String member(String owner, String name, String descriptor, boolean field) {
            return naming.recompiledMember(owner, name, descriptor, field);
        }

        @Override
        public String type(String name) {
            return naming.recompiledClassName(name);
        }

        @Override
        public String parameter(int position) {
            return placed.get(position)
                .map(at -> "param " + at)
                .orElse("recompiled param " + position);
        }

        @Override
        public List<Expression> arguments(MethodInsnNode call, List<Expression> given) {
            return given;
        }
    }

    /**
     * Names what an original method refers to as the jar does.
     */
    private record OriginalSide(Naming naming) implements Symbolic.Side {

        @Override
        public String member(String owner, String name, String descriptor, boolean field) {
            return naming.originalMember(owner, name, descriptor, field);
        }

        @Override
        public String type(String name) {
            return name;
        }

        @Override
        public String parameter(int position) {
            return "param " + position;
        }

        @Override
        public List<Expression> arguments(MethodInsnNode call, List<Expression> given) {
            var became = naming.recompiledMethod(member(call.owner, call.name, call.desc, false));

            if (became.isEmpty()) {
                return given;
            } else {
                var receiver = call.getOpcode() == Opcodes.INVOKESTATIC ? 0 : 1;
                var lined = new ArrayList<Expression>(given.subList(0, receiver));
                for (var place : naming.argumentsOf(became.get())) {
                    place.ifPresent(at -> lined.add(given.get(receiver + at)));
                }
                return lined;
            }
        }
    }
}
