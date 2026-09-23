import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Everything a method lets out of itself, each as the expression that made it.
 *
 * A value a method computes only matters where it goes: into a field, an array, a local, a call,
 * a comparison, or back to the caller. Those are gathered here, and nothing else. The order they
 * happen in is left out, because the decompiler moves independent statements past each other
 * without changing what any of them computes. How many times the same value goes out is left out
 * as well, because a value kept in a local and a value worked out again where it is used are the
 * same value.
 *
 * Comparisons are put into one form. The decompiler turns a branch round and writes a comparison
 * the other way about, so every ordering becomes "less than" with its operands in the order that
 * makes it true, and every equality has its operands in order. The obfuscator's habit of comparing
 * two complements is undone the same way.
 */
public final class Outcome {

    private static final Expression MINUS_ONE = Expression.leaf("int -1", 1);
    private static final Expression ZERO = Expression.leaf("int 0", 1);

    private Outcome() {
        /* empty */
    }

    /**
     * What a method lets out, read by running it on names.
     *
     * Each instruction is run once, on the values the one path into it brings. Where more than one
     * path meets, every value is taken to have arrived from more than one place, so nothing needs
     * to be run again until it settles. ASM's own analyser runs a loop until its values settle and
     * merges at every instruction on the way, which would turn everything worked out inside a loop
     * into a value that merely arrived by more than one path.
     */
    public static Set<Expression> of(String owner, MethodNode method, Symbolic.Side side)
            throws AnalyzerException {
        if (method.instructions.size() == 0) {
            return Set.of();
        }

        var instance = (method.access & Opcodes.ACC_STATIC) == 0;
        var symbolic = new Symbolic(side, instance, method.desc);
        var flow = new Flow();
        var shapes = flow.analyze(owner, method);
        var frames = new ArrayList<Frame<Expression>>();
        for (var at = 0; at < method.instructions.size(); at++) {
            frames.add(null);
        }

        frames.set(0, entry(method, symbolic, instance));
        var waiting = new ArrayDeque<Integer>(List.of(0));
        var outcome = new HashSet<Expression>();

        while (!waiting.isEmpty()) {
            var at = waiting.pop();
            var insn = method.instructions.get(at);
            var frame = frames.get(at);
            outcome.addAll(outOf(insn, frame, side));

            var after = new Frame<Expression>(frame);
            if (insn.getOpcode() >= 0) {
                after.execute(insn, symbolic);
            }

            for (var next : flow.successors(at)) {
                if (frames.get(next) == null) {
                    frames.set(next, flow.joins(next) ? joined(shapes[next], symbolic, method) : after);
                    waiting.push(next);
                }
            }
        }

        return Set.copyOf(outcome);
    }

    /**
     * The values a method starts with: the receiver, the parameters, and nothing in the rest.
     */
    private static Frame<Expression> entry(MethodNode method, Symbolic symbolic, boolean instance) {
        var frame = new Frame<Expression>(method.maxLocals, method.maxStack);
        frame.setReturn(symbolic.newReturnTypeValue(Type.getReturnType(method.desc)));

        var local = 0;
        if (instance) {
            frame.setLocal(local, symbolic.newParameterValue(true, local, Type.getObjectType("java/lang/Object")));
            local++;
        }

        for (var argument : Type.getArgumentTypes(method.desc)) {
            frame.setLocal(local, symbolic.newParameterValue(instance, local, argument));
            local++;
            if (argument.getSize() == 2) {
                frame.setLocal(local, symbolic.newEmptyValue(local));
                local++;
            }
        }

        while (local < method.maxLocals) {
            frame.setLocal(local, symbolic.newEmptyValue(local));
            local++;
        }

        return frame;
    }

    /**
     * The values at a place more than one path reaches, each of which stands for whatever
     * arrived. A handler's caught exception is the one value that is known.
     */
    private static Frame<Expression> joined(Frame<BasicValue> shape, Symbolic symbolic, MethodNode method) {
        var frame = new Frame<Expression>(shape.getLocals(), shape.getMaxStackSize());
        frame.setReturn(symbolic.newReturnTypeValue(Type.getReturnType(method.desc)));

        for (var local = 0; local < shape.getLocals(); local++) {
            frame.setLocal(local, arrived(shape.getLocal(local)));
        }

        for (var place = 0; place < shape.getStackSize(); place++) {
            frame.push(arrived(shape.getStack(place)));
        }

        return frame;
    }

    private static Expression arrived(BasicValue value) {
        if (value == null || value == BasicValue.UNINITIALIZED_VALUE) {
            return Expression.leaf("unset", 1);
        } else {
            return Expression.leaf(Symbolic.MERGED, value.getSize());
        }
    }

    /**
     * Which instruction leads to which, read off ASM's analyser as it walks the method.
     */
    private static final class Flow extends Analyzer<BasicValue> {

        private final Map<Integer, Set<Integer>> successors = new HashMap<>();
        private final Map<Integer, Set<Integer>> predecessors = new HashMap<>();
        private final Set<Integer> handlers = new HashSet<>();

        Flow() {
            super(new BasicInterpreter());
        }

        @Override
        protected void newControlFlowEdge(int from, int to) {
            successors.computeIfAbsent(from, ignored -> new HashSet<>()).add(to);
            predecessors.computeIfAbsent(to, ignored -> new HashSet<>()).add(from);
        }

        @Override
        protected boolean newControlFlowExceptionEdge(int from, int to) {
            successors.computeIfAbsent(from, ignored -> new HashSet<>()).add(to);
            predecessors.computeIfAbsent(to, ignored -> new HashSet<>()).add(from);
            handlers.add(to);
            return true;
        }

        Set<Integer> successors(int from) {
            return successors.getOrDefault(from, Set.of());
        }

        boolean joins(int at) {
            return handlers.contains(at) || predecessors.getOrDefault(at, Set.of()).size() > 1;
        }
    }

    private static List<Expression> outOf(AbstractInsnNode insn, Frame<Expression> frame,
                                          Symbolic.Side side) {
        var opcode = insn.getOpcode();

        return switch (opcode) {
            case Opcodes.PUTFIELD -> {
                var field = (FieldInsnNode) insn;
                yield List.of(Expression.of(
                    "put " + side.member(field.owner, field.name, field.desc, true),
                    List.of(top(frame, 1), top(frame, 0)), 1));
            }
            case Opcodes.PUTSTATIC -> {
                var field = (FieldInsnNode) insn;
                yield List.of(Expression.of(
                    "put " + side.member(field.owner, field.name, field.desc, true),
                    List.of(top(frame, 0)), 1));
            }
            case Opcodes.IASTORE, Opcodes.LASTORE, Opcodes.FASTORE, Opcodes.DASTORE,
                 Opcodes.AASTORE, Opcodes.BASTORE, Opcodes.CASTORE, Opcodes.SASTORE ->
                List.of(Expression.of("store",
                    List.of(top(frame, 2), top(frame, 1), top(frame, 0)), 1));
            case Opcodes.INVOKEVIRTUAL, Opcodes.INVOKESPECIAL, Opcodes.INVOKESTATIC,
                 Opcodes.INVOKEINTERFACE -> List.of(call((MethodInsnNode) insn, frame, side));
            case Opcodes.IRETURN, Opcodes.LRETURN, Opcodes.FRETURN, Opcodes.DRETURN,
                 Opcodes.ARETURN -> List.of(Expression.of("return", List.of(top(frame, 0)), 1));
            case Opcodes.ISTORE, Opcodes.LSTORE, Opcodes.FSTORE, Opcodes.DSTORE, Opcodes.ASTORE ->
                List.of(Expression.of("local", List.of(top(frame, 0)), 1));
            case Opcodes.ATHROW -> List.of(Expression.of("throw", List.of(top(frame, 0)), 1));
            case Opcodes.TABLESWITCH, Opcodes.LOOKUPSWITCH ->
                List.of(Expression.of("switch", List.of(top(frame, 0)), 1));
            case Opcodes.IFEQ, Opcodes.IFNE -> List.of(equal(top(frame, 0), ZERO));
            case Opcodes.IFLT, Opcodes.IFGE -> List.of(less(top(frame, 0), ZERO));
            case Opcodes.IFGT, Opcodes.IFLE -> List.of(less(ZERO, top(frame, 0)));
            case Opcodes.IF_ICMPEQ, Opcodes.IF_ICMPNE, Opcodes.IF_ACMPEQ, Opcodes.IF_ACMPNE ->
                List.of(equal(top(frame, 1), top(frame, 0)));
            case Opcodes.IF_ICMPLT, Opcodes.IF_ICMPGE -> List.of(less(top(frame, 1), top(frame, 0)));
            case Opcodes.IF_ICMPGT, Opcodes.IF_ICMPLE -> List.of(less(top(frame, 0), top(frame, 1)));
            case Opcodes.IFNULL, Opcodes.IFNONNULL ->
                List.of(Expression.of("null", List.of(top(frame, 0)), 1));
            default -> List.of();
        };
    }

    private static Expression call(MethodInsnNode call, Frame<Expression> frame, Symbolic.Side side) {
        var count = Type.getArgumentTypes(call.desc).length
            + (call.getOpcode() == Opcodes.INVOKESTATIC ? 0 : 1);
        var given = new ArrayList<Expression>();
        for (var at = count - 1; at >= 0; at--) {
            given.add(top(frame, at));
        }
        return Symbolic.call(side, call, given);
    }

    private static Expression top(Frame<Expression> frame, int down) {
        return frame.getStack(frame.getStackSize() - 1 - down);
    }

    /**
     * An equality, with its operands in order and a comparison of two numbers unwrapped.
     */
    private static Expression equal(Expression first, Expression second) {
        if (second == ZERO && compared(first)) {
            return Expression.of("equal", first.operands(), 1);
        } else {
            return Expression.of("equal", ordered(first, second), 1);
        }
    }

    /**
     * An ordering, written as the smaller operand first.
     *
     * A comparison of two longs or two floating point numbers arrives as the sign of the pair
     * compared with nought, and is unwrapped into the pair. Two complements compared the one way
     * are the two values compared the other way, which is what the obfuscator wrote.
     */
    private static Expression less(Expression smaller, Expression larger) {
        if (larger == ZERO && compared(smaller)) {
            return less(smaller.operands().get(0), smaller.operands().get(1));
        } else if (smaller == ZERO && compared(larger)) {
            return less(larger.operands().get(1), larger.operands().get(0));
        } else if (complement(smaller) && complement(larger)) {
            return less(uncomplemented(larger), uncomplemented(smaller));
        } else {
            return Expression.of("less", List.of(smaller, larger), 1);
        }
    }

    private static boolean compared(Expression value) {
        return switch (value.operator()) {
            case "lcmp", "fcmp", "dcmp" -> true;
            default -> false;
        };
    }

    private static boolean complement(Expression value) {
        return value.operator().equals("ixor") && value.operands().contains(MINUS_ONE)
            && value.operands().size() == 2;
    }

    private static Expression uncomplemented(Expression value) {
        return value.operands().get(0) == MINUS_ONE ? value.operands().get(1) : value.operands().get(0);
    }

    private static List<Expression> ordered(Expression first, Expression second) {
        if (first.id() <= second.id()) {
            return List.of(first, second);
        } else {
            return List.of(second, first);
        }
    }
}
