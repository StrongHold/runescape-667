import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.util.Printer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Runs a method on names rather than numbers, so that each value it computes comes out as the
 * expression that made it.
 *
 * ASM's analyser walks every path through a method and asks this what each instruction makes of
 * the values it is handed. A value that reaches an instruction along more than one path, which is
 * what a loop variable is, is not followed further: it becomes a single leaf that stands for
 * whatever arrived. Everything computed from it afterwards is still written out in full.
 */
public final class Symbolic extends Interpreter<Expression> {

    /**
     * What a value that arrived by more than one path is written as.
     */
    static final String MERGED = "merged";

    /**
     * How one side of the comparison names what a method refers to, so that the same field or
     * parameter is written the same way on both sides.
     */
    public interface Side {

        String member(String owner, String name, String descriptor, boolean field);

        String type(String name);

        /**
         * The arguments of a call as the recompiled method takes them.
         *
         * A call in the jar passes the obfuscator's extra parameters as well, and those are
         * dropped, and the rest are put in the order the recompiled method declares them.
         */
        List<Expression> arguments(MethodInsnNode call, List<Expression> given);

        /**
         * The one instruction the method a call names does, when that is all it does.
         */
        java.util.Optional<String> helperOperation(String member);

        /**
         * The parameter at a position of the method, counted from nought without the receiver.
         */
        String parameter(int position);
    }

    private final Side side;
    private final int[] positions;

    /**
     * @param side      how names are written
     * @param instance  whether the method has a receiver in local nought
     * @param descriptor the method's descriptor, which says which local holds which parameter
     */
    public Symbolic(Side side, boolean instance, String descriptor) {
        super(Opcodes.ASM9);
        this.side = side;
        this.positions = localPositions(instance, descriptor);
    }

    /**
     * Which parameter each local holds when the method starts, or minus one for a local that holds
     * the receiver or the second half of a long or a double.
     */
    private static int[] localPositions(boolean instance, String descriptor) {
        var arguments = Type.getArgumentTypes(descriptor);
        var size = (instance ? 1 : 0) + Type.getArgumentsAndReturnSizes(descriptor) / 4;
        var positions = new int[Math.max(size, 1) + 2];
        Arrays.fill(positions, -1);

        var local = instance ? 1 : 0;
        for (var at = 0; at < arguments.length; at++) {
            positions[local] = at;
            local += arguments[at].getSize();
        }
        return positions;
    }

    @Override
    public Expression newValue(Type type) {
        if (type == Type.VOID_TYPE) {
            return null;
        } else if (type == null) {
            return Expression.leaf("unset", 1);
        } else {
            return Expression.leaf("unknown " + type.getDescriptor(), type.getSize());
        }
    }

    @Override
    public Expression newParameterValue(boolean instance, int local, Type type) {
        if (instance && local == 0) {
            return Expression.leaf("this", 1);
        } else if (local < positions.length && positions[local] >= 0) {
            return Expression.leaf(side.parameter(positions[local]), type.getSize());
        } else {
            return newValue(type);
        }
    }

    @Override
    public Expression newExceptionValue(TryCatchBlockNode block, Frame<Expression> frame,
                                        Type type) {
        return Expression.leaf("caught", 1);
    }

    @Override
    public Expression newOperation(AbstractInsnNode insn) {
        return switch (insn.getOpcode()) {
            case Opcodes.ACONST_NULL -> Expression.leaf("null", 1);
            case Opcodes.ICONST_M1, Opcodes.ICONST_0, Opcodes.ICONST_1, Opcodes.ICONST_2,
                 Opcodes.ICONST_3, Opcodes.ICONST_4, Opcodes.ICONST_5 ->
                Expression.leaf("int " + (insn.getOpcode() - Opcodes.ICONST_0), 1);
            case Opcodes.LCONST_0, Opcodes.LCONST_1 ->
                Expression.leaf("long " + (insn.getOpcode() - Opcodes.LCONST_0), 2);
            case Opcodes.FCONST_0, Opcodes.FCONST_1, Opcodes.FCONST_2 ->
                Expression.leaf("float " + (float) (insn.getOpcode() - Opcodes.FCONST_0), 1);
            case Opcodes.DCONST_0, Opcodes.DCONST_1 ->
                Expression.leaf("double " + (double) (insn.getOpcode() - Opcodes.DCONST_0), 2);
            case Opcodes.BIPUSH, Opcodes.SIPUSH ->
                Expression.leaf("int " + ((IntInsnNode) insn).operand, 1);
            case Opcodes.LDC -> constant(((LdcInsnNode) insn).cst);
            case Opcodes.GETSTATIC -> {
                var field = (FieldInsnNode) insn;
                yield Expression.leaf("get " + side.member(field.owner, field.name, field.desc, true),
                    Type.getType(field.desc).getSize());
            }
            case Opcodes.NEW -> Expression.leaf("new " + side.type(((TypeInsnNode) insn).desc), 1);
            default -> Expression.leaf(name(insn), 1);
        };
    }

    private static Expression constant(Object value) {
        return switch (value) {
            case Integer number -> Expression.leaf("int " + number, 1);
            case Float number -> Expression.leaf("float " + number, 1);
            case Long number -> Expression.leaf("long " + number, 2);
            case Double number -> Expression.leaf("double " + number, 2);
            case String text -> Expression.leaf("string " + text, 1);
            default -> Expression.leaf("constant " + value, 1);
        };
    }

    @Override
    public Expression copyOperation(AbstractInsnNode insn, Expression value) {
        return value;
    }

    @Override
    public Expression unaryOperation(AbstractInsnNode insn, Expression value) {
        var opcode = insn.getOpcode();

        return switch (opcode) {
            case Opcodes.IINC -> Expression.of("iadd",
                List.of(value, Expression.leaf("int " + ((IincInsnNode) insn).incr, 1)), 1);
            case Opcodes.GETFIELD -> {
                var field = (FieldInsnNode) insn;
                yield Expression.of("get " + side.member(field.owner, field.name, field.desc, true),
                    List.of(value), Type.getType(field.desc).getSize());
            }
            case Opcodes.CHECKCAST -> value;
            case Opcodes.NEWARRAY, Opcodes.ARRAYLENGTH, Opcodes.INSTANCEOF ->
                Expression.of(name(insn), List.of(value), 1);
            case Opcodes.ANEWARRAY ->
                Expression.of("anewarray " + side.type(((TypeInsnNode) insn).desc), List.of(value), 1);
            case Opcodes.I2L, Opcodes.F2L, Opcodes.I2D, Opcodes.F2D, Opcodes.LNEG, Opcodes.DNEG,
                 Opcodes.L2D, Opcodes.D2L -> Expression.of(name(insn), List.of(value), 2);
            case Opcodes.INEG, Opcodes.FNEG, Opcodes.L2I, Opcodes.L2F, Opcodes.F2I, Opcodes.I2F,
                 Opcodes.D2I, Opcodes.D2F, Opcodes.I2B, Opcodes.I2C, Opcodes.I2S ->
                Expression.of(name(insn), List.of(value), 1);
            default -> null;
        };
    }

    @Override
    public Expression binaryOperation(AbstractInsnNode insn, Expression first, Expression second) {
        var opcode = insn.getOpcode();

        return switch (opcode) {
            case Opcodes.IALOAD, Opcodes.FALOAD, Opcodes.AALOAD, Opcodes.BALOAD, Opcodes.CALOAD,
                 Opcodes.SALOAD -> Expression.of("load", List.of(first, second), 1);
            case Opcodes.LALOAD, Opcodes.DALOAD -> Expression.of("load", List.of(first, second), 2);
            case Opcodes.LADD, Opcodes.LSUB, Opcodes.LMUL, Opcodes.LDIV, Opcodes.LREM,
                 Opcodes.LSHL, Opcodes.LSHR, Opcodes.LUSHR, Opcodes.LAND, Opcodes.LOR, Opcodes.LXOR,
                 Opcodes.DADD, Opcodes.DSUB, Opcodes.DMUL, Opcodes.DDIV, Opcodes.DREM ->
                Expression.of(name(insn), List.of(first, second), 2);
            case Opcodes.IADD, Opcodes.ISUB, Opcodes.IMUL, Opcodes.IDIV, Opcodes.IREM,
                 Opcodes.ISHL, Opcodes.ISHR, Opcodes.IUSHR, Opcodes.IAND, Opcodes.IOR, Opcodes.IXOR,
                 Opcodes.FADD, Opcodes.FSUB, Opcodes.FMUL, Opcodes.FDIV, Opcodes.FREM,
                 Opcodes.LCMP -> Expression.of(name(insn), List.of(first, second), 1);
            case Opcodes.FCMPL, Opcodes.FCMPG -> Expression.of("fcmp", List.of(first, second), 1);
            case Opcodes.DCMPL, Opcodes.DCMPG -> Expression.of("dcmp", List.of(first, second), 1);
            default -> null;
        };
    }

    @Override
    public Expression ternaryOperation(AbstractInsnNode insn, Expression first, Expression second,
                                       Expression third) {
        return null;
    }

    @Override
    public Expression naryOperation(AbstractInsnNode insn, List<? extends Expression> values) {
        return switch (insn) {
            case MethodInsnNode call -> call(side, call, List.copyOf(values));
            case InvokeDynamicInsnNode dynamic -> Expression.of(
                "dynamic " + dynamic.name + " " + bootstrap(dynamic.bsm),
                List.copyOf(values),
                Math.max(Type.getReturnType(dynamic.desc).getSize(), 1));
            case MultiANewArrayInsnNode array -> Expression.of(
                "multianewarray " + side.type(array.desc), List.copyOf(values), 1);
            default -> Expression.of(name(insn), List.copyOf(values), 1);
        };
    }

    /**
     * What a call makes, named and with its arguments lined up the same way on both sides.
     */
    static Expression call(Side side, MethodInsnNode call, List<Expression> given) {
        var member = side.member(call.owner, call.name, call.desc, false);
        var helper = side.helperOperation(member);
        if (helper.isPresent() && given.size() == 2) {
            return Expression.of(helper.get(), given, 1);
        }
        return Expression.of(
            "call " + member,
            side.arguments(call, given),
            Math.max(Type.getReturnType(call.desc).getSize(), 1));
    }

    private static String bootstrap(Handle handle) {
        return handle.getOwner() + "." + handle.getName();
    }

    @Override
    public void returnOperation(AbstractInsnNode insn, Expression value, Expression expected) {
        /* empty */
    }

    @Override
    public Expression merge(Expression first, Expression second) {
        if (first == second || first.operator().equals(MERGED)) {
            return first;
        } else {
            return Expression.leaf(MERGED, first.size());
        }
    }

    static String name(AbstractInsnNode insn) {
        return Printer.OPCODES[insn.getOpcode()].toLowerCase(Locale.ROOT);
    }
}
