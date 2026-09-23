import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
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
 */
public final class NativeTrace {

    private static final String PREFIX = "$traced$";

    /**
     * The classes the software toolkit binds to, the ground and the camera's matrix among them.
     */
    private static final Set<String> WATCHED = Set.of("a", "h", "i", "j", "ja", "n", "oa", "t", "wa", "xa");

    public static void premain(String argument, Instrumentation instrumentation) {
        if (!instrumentation.isNativeMethodPrefixSupported()) {
            throw new IllegalStateException("This virtual machine cannot rename native methods.");
        }

        NativeLog.open(argument);
        var transformer = new Wrapper();
        instrumentation.addTransformer(transformer);
        instrumentation.setNativeMethodPrefix(transformer, PREFIX);
    }

    private static final class Wrapper implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String name, Class<?> redefined,
                                ProtectionDomain domain, byte[] bytes) {
            if (name == null || !WATCHED.contains(name)) {
                return null;
            }

            var reader = new ClassReader(bytes);
            var writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            reader.accept(new Renaming(writer, name), 0);
            return writer.toByteArray();
        }
    }

    /**
     * Renames every native method and puts a traced one of the old name in its place.
     */
    private static final class Renaming extends ClassVisitor {

        private final String owner;

        Renaming(ClassVisitor next, String owner) {
            super(Opcodes.ASM9, next);
            this.owner = owner;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                         String[] exceptions) {
            if ((access & Opcodes.ACC_NATIVE) == 0) {
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

    private NativeTrace() {
        /* empty */
    }
}
