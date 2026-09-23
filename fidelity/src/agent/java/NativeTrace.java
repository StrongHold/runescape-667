import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * An agent that writes down every call the client makes into the native software toolkit.
 *
 * The recompiled client draws gaps into the ground that the jar it came from does not, through the
 * same shipped library. The library cannot be the difference, so what the client hands it must
 * be. The classes the library binds to keep the jar's names in the recompiled client, so this one
 * agent can watch either client, and the two records can be set side by side.
 *
 * Each native method of those classes is renamed with a prefix, which the virtual machine strips
 * again when it links the method to the library. A method of the old name takes its place, calls
 * the renamed one, and hands the arguments and the answer to {@link NativeLog}.
 *
 * Started with {@code -javaagent:native-trace.jar=<file>}. The record is written to that file.
 * Only the ground and the camera's matrix are watched unless other classes are named, as
 * {@code -javaagent:native-trace.jar=<file>,classes=t:ja:oa}.
 *
 * A method written in Java can be watched as well, by its class, name and descriptor, as
 * {@code ,methods=lb.a(FFFFFFFFFI)V;Rasterizer.renderFlatTriangleRgb(FFFFFFFFFI)V}. Its arguments
 * are written down as it is entered. This is how the toolkit written in Java is compared, since it
 * makes no native calls at all, and the jar and the recompiled client name its methods differently.
 */
public final class NativeTrace {

    private static final String PREFIX = "$traced$";

    /**
     * The classes watched unless others are named: the ground and the camera's matrix.
     *
     * The other classes the software toolkit binds to draw models and sprites, many times a
     * frame, and writing every one of those down slows the client until it cannot be played.
     */
    private static final String GROUND_AND_CAMERA = "t:ja";

    public static void premain(String argument, Instrumentation instrumentation) {
        if (!instrumentation.isNativeMethodPrefixSupported()) {
            throw new IllegalStateException("This virtual machine cannot rename native methods.");
        }

        var settings = argument == null ? new String[]{""} : argument.split(",");
        var classes = GROUND_AND_CAMERA;
        var methods = new HashSet<String>();
        for (var at = 1; at < settings.length; at++) {
            if (settings[at].startsWith("classes=")) {
                classes = settings[at].substring("classes=".length());
            } else if (settings[at].startsWith("methods=")) {
                methods.addAll(Set.of(settings[at].substring("methods=".length()).split(";")));
            }
        }

        NativeLog.open(settings[0]);
        var transformer = new Wrapper(Set.of(classes.split(":")), methods);
        instrumentation.addTransformer(transformer);
        instrumentation.setNativeMethodPrefix(transformer, PREFIX);
    }

    private static final class Wrapper implements ClassFileTransformer {

        private final Set<String> watched;
        private final Set<String> methods;
        private final Set<String> owners = new HashSet<>();

        Wrapper(Set<String> watched, Set<String> methods) {
            this.watched = Set.copyOf(watched);
            this.methods = Set.copyOf(methods);
            for (var method : methods) {
                owners.add(method.substring(0, method.indexOf('.')));
            }
        }

        @Override
        public byte[] transform(ClassLoader loader, String name, Class<?> redefined,
                                ProtectionDomain domain, byte[] bytes) {
            if (name == null || !(watched.contains(name) || owners.contains(name))) {
                return null;
            }

            try {
                var reader = new ClassReader(bytes);
                var writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
                reader.accept(new Renaming(writer, name, watched.contains(name), methods), ClassReader.EXPAND_FRAMES);
                return writer.toByteArray();
            } catch (RuntimeException failure) {
                // The virtual machine drops what a transformer throws without a word, which
                // would leave a class quietly unwatched.
                failure.printStackTrace();
                throw failure;
            }
        }
    }

    /**
     * Renames every native method and puts a traced one of the old name in its place.
     */
    private static final class Renaming extends ClassVisitor {

        private final String owner;
        private final boolean natives;
        private final Set<String> methods;

        Renaming(ClassVisitor next, String owner, boolean natives, Set<String> methods) {
            super(Opcodes.ASM9, next);
            this.owner = owner;
            this.natives = natives;
            this.methods = methods;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                         String[] exceptions) {
            var named = owner + "." + name + descriptor;
            if (methods.contains(named)) {
                var next = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new Entering(next, access, name, descriptor, owner + "." + name);
            } else if (!natives || (access & Opcodes.ACC_NATIVE) == 0) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            } else {
                var renamed = (access & ~Opcodes.ACC_PUBLIC & ~Opcodes.ACC_PROTECTED) | Opcodes.ACC_PRIVATE;
                super.visitMethod(renamed, PREFIX + name, descriptor, signature, exceptions);
                traced(access & ~Opcodes.ACC_NATIVE, name, descriptor, exceptions);
                return null;
            }
        }

        private void traced(int access, String name, String descriptor, String[] exceptions) {
            var method = new Method(name, descriptor);
            var real = new Method(PREFIX + name, descriptor);
            var code = new GeneratorAdapter(access, method, null, toTypes(exceptions), cv);
            var instance = (access & Opcodes.ACC_STATIC) == 0;
            var self = Type.getObjectType(owner);
            var returns = method.getReturnType();

            code.visitCode();
            if (instance) {
                code.loadThis();
                code.loadArgs();
                code.visitMethodInsn(Opcodes.INVOKESPECIAL, owner, real.getName(), descriptor, false);
            } else {
                code.loadArgs();
                code.invokeStatic(self, real);
            }

            var answer = -1;
            if (returns.getSort() != Type.VOID) {
                answer = code.newLocal(returns);
                code.storeLocal(answer);
            }

            code.push(owner + "." + name);
            code.loadArgArray();
            if (answer < 0) {
                code.visitInsn(Opcodes.ACONST_NULL);
            } else {
                code.loadLocal(answer);
                code.box(returns);
            }
            code.invokeStatic(Type.getType(NativeLog.class),
                Method.getMethod("void record(String, Object[], Object)"));

            if (answer >= 0) {
                code.loadLocal(answer);
            }
            code.returnValue();
            code.endMethod();
        }

        private static Type[] toTypes(String[] names) {
            if (names == null) {
                return new Type[0];
            } else {
                var types = new Type[names.length];
                for (var at = 0; at < names.length; at++) {
                    types[at] = Type.getObjectType(names[at]);
                }
                return types;
            }
        }
    }

    /**
     * Writes a Java method's arguments down as it is entered.
     */
    private static final class Entering extends AdviceAdapter {

        private final String named;

        Entering(MethodVisitor next, int access, String name, String descriptor, String named) {
            super(Opcodes.ASM9, next, access, name, descriptor);
            this.named = named;
        }

        @Override
        protected void onMethodEnter() {
            push(named);
            loadArgArray();
            visitInsn(Opcodes.ACONST_NULL);
            invokeStatic(Type.getType(NativeLog.class), Method.getMethod("void record(String, Object[], Object)"));
        }
    }

    private NativeTrace() {
        /* empty */
    }
}
