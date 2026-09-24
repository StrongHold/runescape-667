import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * What every class and member of the recompiled client was called in the jar it came from.
 *
 * The {@code @OriginalClass}, {@code @OriginalMember} and {@code @OriginalArg} annotations are kept
 * in the class files, so the whole of the renaming can be read back without the source. A member
 * is named here by its original owner, name and descriptor, on either side, which is what lets a
 * field read in the jar be recognised as the same field read in the recompiled class.
 *
 * A member the annotations do not cover keeps its recompiled name, and a member of a class
 * neither side declares, such as one of the JDK's, keeps the name both sides give it.
 */
public final class Naming {

    private static final String ORIGINAL_CLASS = "Lorg/openrs2/deob/annotation/OriginalClass;";
    private static final String ORIGINAL_MEMBER = "Lorg/openrs2/deob/annotation/OriginalMember;";
    private static final String ORIGINAL_ARG = "Lorg/openrs2/deob/annotation/OriginalArg;";

    /**
     * The annotations write a class as the jar it came from, a mark, then its name. A jar is named
     * in lower case, which keeps the L that opens a class in a descriptor out of the match.
     */
    private static final Pattern JAR_MARK = Pattern.compile("[a-z]+!");

    private static final Pattern CLASS_IN_DESCRIPTOR = Pattern.compile("L([^;]+);");

    /**
     * A member of a class, by the class it is declared in, its name and its descriptor.
     */
    public record Member(String owner, String name, String descriptor) {

        @Override
        public String toString() {
            return owner + "." + name + descriptor;
        }
    }

    private final Map<String, ClassNode> recompiled;
    private final Map<String, ClassNode> original;
    private final Map<String, String> classes = new HashMap<>();
    private final Map<Member, Member> members = new HashMap<>();
    private final Map<String, String> readable = new HashMap<>();
    private final Map<String, MethodNode> recompiledMethods = new HashMap<>();

    private Naming(Map<String, ClassNode> recompiled, Map<String, ClassNode> original) {
        this.recompiled = recompiled;
        this.original = original;

        for (var node : recompiled.values()) {
            annotation(node.invisibleAnnotations, ORIGINAL_CLASS)
                .ifPresent(found -> classes.put(node.name, unmark((String) found.values.get(1))));

            for (var field : node.fields) {
                annotation(field.invisibleAnnotations, ORIGINAL_MEMBER).ifPresent(found ->
                    remember(new Member(node.name, field.name, field.desc), found));
            }

            for (var method : node.methods) {
                annotation(method.invisibleAnnotations, ORIGINAL_MEMBER).ifPresent(found -> {
                    var member = new Member(node.name, method.name, method.desc);
                    remember(member, found);
                    recompiledMethods.put(members.get(member).toString(), method);
                });
            }
        }
    }

    /**
     * Reads every class of both sides, the recompiled ones from a directory of class files and the
     * original ones from the unpacked jar.
     */
    public static Naming read(Path recompiled, Path original) {
        return new Naming(load(recompiled), load(original));
    }

    private static Map<String, ClassNode> load(Path root) {
        var loaded = new HashMap<String, ClassNode>();

        try (Stream<Path> files = Files.walk(root)) {
            for (var file : (Iterable<Path>) files.filter(path -> path.toString().endsWith(".class"))::iterator) {
                var node = new ClassNode();
                new ClassReader(Files.readAllBytes(file)).accept(node, ClassReader.SKIP_FRAMES);
                loaded.put(node.name, node);
            }
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }

        return loaded;
    }

    private void remember(Member member, AnnotationNode found) {
        var was = new Member(
            unmark(value(found, "owner")),
            value(found, "name"),
            unmark(value(found, "descriptor")));
        members.put(member, was);
        readable.put(was.toString(), member.owner() + "." + member.name());
    }

    private static String value(AnnotationNode node, String key) {
        for (var at = 0; at < node.values.size(); at += 2) {
            if (node.values.get(at).equals(key)) {
                return (String) node.values.get(at + 1);
            }
        }
        throw new IllegalStateException(node.desc + " has no " + key + ".");
    }

    private static String unmark(String named) {
        return JAR_MARK.matcher(named).replaceAll("");
    }

    private static Optional<AnnotationNode> annotation(List<AnnotationNode> nodes, String desc) {
        if (nodes == null) {
            return Optional.empty();
        } else {
            return nodes.stream().filter(node -> node.desc.equals(desc)).findFirst();
        }
    }

    public Optional<ClassNode> recompiledClass(String name) {
        return Optional.ofNullable(recompiled.get(name));
    }

    public Optional<ClassNode> originalClass(String name) {
        return Optional.ofNullable(original.get(name));
    }

    /**
     * The original a recompiled method was, when the annotations say.
     */
    public Optional<Member> originalOf(String owner, MethodNode method) {
        return Optional.ofNullable(members.get(new Member(owner, method.name, method.desc)));
    }

    /**
     * The original method a member names, declared in the jar.
     */
    public Optional<MethodNode> originalMethod(Member member) {
        return originalClass(member.owner()).flatMap(node -> node.methods.stream()
            .filter(method -> method.name.equals(member.name()) && method.desc.equals(member.descriptor()))
            .findFirst());
    }

    /**
     * Which of the original method's parameters each parameter of the recompiled one was, or
     * nothing for one the annotations do not place.
     */
    public List<Optional<Integer>> argumentsOf(MethodNode method) {
        var count = Type.getArgumentTypes(method.desc).length;
        return IntStream.range(0, count)
            .mapToObj(at -> {
                var annotations = method.invisibleParameterAnnotations == null
                    ? null
                    : method.invisibleParameterAnnotations[at];
                return annotation(annotations, ORIGINAL_ARG)
                    .map(found -> (Integer) found.values.get(1));
            })
            .toList();
    }

    /**
     * The name a field or method referred to from recompiled code goes by on both sides.
     *
     * The reference names the class it was compiled against, which may inherit the member rather
     * than declare it, so the class that declares it is looked for first.
     */
    public String recompiledMember(String owner, String name, String descriptor, boolean field) {
        var declared = declaring(recompiled, owner, name, descriptor, field);
        var member = new Member(declared, name, descriptor);
        var was = members.get(member);

        if (was != null) {
            return was.toString();
        } else if (recompiled.containsKey(declared)) {
            return "recompiled " + member;
        } else {
            return member.toString();
        }
    }

    /**
     * The name a field or method referred to from original code goes by on both sides.
     */
    public String originalMember(String owner, String name, String descriptor, boolean field) {
        return new Member(declaring(original, owner, name, descriptor, field), name, descriptor)
            .toString();
    }

    /**
     * The one instruction a method of the jar does, when all it does is load its two parameters,
     * apply one instruction to them and return the answer.
     *
     * The obfuscator moved some single operations, such as an and, into a static method of their
     * own, and the deobfuscator put most of them back. A call to such a method is written as the
     * operation it does, on both sides, so that it matches where it was put back.
     */
    public Optional<String> helperOperation(String member) {
        var dot = member.indexOf('.');
        var paren = member.indexOf('(');
        var found = originalMethod(new Member(member.substring(0, dot), member.substring(dot + 1, paren),
            member.substring(paren)));
        if (found.isEmpty() || (found.get().access & org.objectweb.asm.Opcodes.ACC_STATIC) == 0) {
            return Optional.empty();
        }

        // The obfuscator wrapped many methods in an exception handler, which follows the return.
        var instructions = new java.util.ArrayList<org.objectweb.asm.tree.AbstractInsnNode>();
        for (var insn : found.get().instructions) {
            if (insn.getOpcode() >= 0 && instructions.size() < 4) {
                instructions.add(insn);
            }
        }
        if (instructions.size() != 4
            || !(instructions.get(0) instanceof org.objectweb.asm.tree.VarInsnNode first) || first.var != 0
            || !(instructions.get(1) instanceof org.objectweb.asm.tree.VarInsnNode second) || second.var != 1
            || instructions.get(3).getOpcode() != org.objectweb.asm.Opcodes.IRETURN) {
            return Optional.empty();
        }
        return Optional.of(Symbolic.name(instructions.get(2)));
    }

    /**
     * The recompiled method an original one became, when the annotations say.
     */
    public Optional<MethodNode> recompiledMethod(String original) {
        return Optional.ofNullable(recompiledMethods.get(original));
    }

    /**
     * The name a class referred to from recompiled code goes by on both sides.
     *
     * An array is referred to by its descriptor rather than by a name, so every class named inside
     * one is looked up.
     */
    public String recompiledClassName(String name) {
        if (name.startsWith("[")) {
            return CLASS_IN_DESCRIPTOR.matcher(name)
                .replaceAll(found -> "L" + classes.getOrDefault(found.group(1), found.group(1)) + ";");
        } else {
            return classes.getOrDefault(name, name);
        }
    }

    /**
     * Every recompiled class that says which class of the jar it was.
     */
    public List<String> recompiledNames() {
        return List.copyOf(classes.keySet());
    }

    /**
     * The recompiled name of a member written by its original name, for a person to read.
     */
    public String readable(String written) {
        var space = written.indexOf(' ');

        if (space < 0) {
            return written;
        } else {
            var named = readable.get(written.substring(space + 1));
            return named == null ? written : written.substring(0, space + 1) + named;
        }
    }

    private static String declaring(Map<String, ClassNode> side, String owner, String name,
                                    String descriptor, boolean field) {
        var at = side.get(owner);

        while (at != null) {
            var declares = field
                ? at.fields.stream().anyMatch(it -> it.name.equals(name) && it.desc.equals(descriptor))
                : at.methods.stream().anyMatch(it -> it.name.equals(name) && it.desc.equals(descriptor));

            if (declares) {
                return at.name;
            } else {
                at = side.get(at.superName);
            }
        }

        return owner;
    }
}
