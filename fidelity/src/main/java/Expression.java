import org.objectweb.asm.tree.analysis.Value;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A value a method computes, written as the operation that made it and the values it was made
 * from.
 *
 * Every expression is interned, so two expressions are equal exactly when they are the same
 * object. A value that is used many times is one node many times over rather than a copy each
 * time, which keeps a long method from growing a tree that doubles in size with every use.
 *
 * Expressions are put into one form as they are made, so that two ways of writing the same sum
 * come out as the same node. Integer arithmetic is exact, so a chain of additions, of
 * multiplications or of one bitwise operation is flattened and put in order. Floating point
 * addition and multiplication are commutative but not associative, so the two operands of one
 * are put in order and the chain is left alone. That is the whole point: a re-association of
 * floating point arithmetic is a change in what is computed, and it shows up as a different node.
 */
public final class Expression implements Value {

    /**
     * The integer operations whose chains may be flattened and put in any order.
     */
    private static final Set<String> EXACT = Set.of(
        "iadd", "imul", "iand", "ior", "ixor", "ladd", "lmul", "land", "lor", "lxor");

    /**
     * The floating point operations whose two operands may be swapped.
     */
    private static final Set<String> COMMUTATIVE = Set.of("fadd", "fmul", "dadd", "dmul");

    /**
     * The operations that round to floating point, which are what can lose a pixel.
     */
    private static final Set<String> FLOATING = Set.of(
        "fadd", "fsub", "fmul", "fdiv", "frem", "fneg", "dadd", "dsub", "dmul", "ddiv", "drem",
        "dneg", "i2f", "i2d", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f");

    private static final Map<Key, Expression> INTERNED = new HashMap<>();

    private record Key(String operator, List<Integer> operands, int size) {
        /* empty */
    }

    private final int id;
    private final String operator;
    private final List<Expression> operands;
    private final int size;
    private final boolean floating;

    private Expression(int id, String operator, List<Expression> operands, int size) {
        this.id = id;
        this.operator = operator;
        this.operands = operands;
        this.size = size;
        this.floating = FLOATING.contains(operator)
            || operands.stream().anyMatch(Expression::floating);
    }

    /**
     * A value with nothing under it: a constant, a parameter, a field, or a value that arrived
     * from more than one place.
     */
    public static Expression leaf(String name, int size) {
        return of(name, List.of(), size);
    }

    /**
     * The value an operation makes from others, put into the one form.
     */
    public static Expression of(String operator, List<Expression> operands, int size) {
        var whole = wholeForm(operator, operands, size);
        if (whole != null) {
            return whole;
        }

        var signed = signless(operator, operands, size);
        if (signed != null) {
            return signed;
        }

        var formed = new ArrayList<Expression>();

        if (EXACT.contains(operator)) {
            for (var operand : operands) {
                if (operand.operator.equals(operator)) {
                    formed.addAll(operand.operands);
                } else {
                    formed.add(operand);
                }
            }
            formed.sort(Comparator.comparingInt(Expression::id));
        } else if (COMMUTATIVE.contains(operator)) {
            formed.addAll(operands);
            formed.sort(Comparator.comparingInt(Expression::id));
        } else {
            formed.addAll(operands);
        }

        var key = new Key(operator, formed.stream().map(Expression::id).toList(), size);
        return INTERNED.computeIfAbsent(key,
            ignored -> new Expression(INTERNED.size(), operator, List.copyOf(formed), size));
    }

    /**
     * The same integer value in one form, or nothing when it is in that form already.
     *
     * Arithmetic on an int or a long wraps round, so it is exact in any order: a subtraction is
     * the addition of a negation, a negation of a sum is the sum of the negations, two negations
     * cancel, and the constants of a sum can be added up. A shift uses only the low five bits of
     * its distance, or six for a long, and the obfuscator wrote many distances far past that.
     * Floating point constants are exact to negate, so a subtraction of one is the addition of
     * its negation. The decompiler and the obfuscator each wrote all of these either way round.
     */
    private static Expression wholeForm(String operator, List<Expression> operands, int size) {
        return switch (operator) {
            case "isub", "lsub" -> of(operator.charAt(0) + "add",
                List.of(operands.get(0), of(operator.charAt(0) + "neg", List.of(operands.get(1)), size)), size);
            case "ineg", "lneg" -> negated(operator, operands.get(0), size);
            case "iadd", "ladd" -> foldedSum(operator, operands, size);
            case "ishr", "iushr" -> {
                var masked = maskedShift(operator, operands, size);
                yield masked != null ? masked : shifted(operator, operands, size, 31);
            }
            case "ishl" -> shifted(operator, operands, size, 31);
            case "lshl", "lshr", "lushr" -> shifted(operator, operands, size, 63);
            case "fsub", "dsub" -> constantOf(operands.get(1)) == null ? null
                : of(operator.charAt(0) + "add", List.of(operands.get(0), constant(operands.get(1), -constantOf(operands.get(1)))), size);
            default -> null;
        };
    }

    private static Expression negated(String operator, Expression operand, int size) {
        var kind = operator.charAt(0);
        var value = wholeOf(operand);
        if (value != null) {
            return whole(kind, kind == 'i' ? (long) -(int) (long) value : -value);
        } else if (operand.operator.equals(operator)) {
            return operand.operands.get(0);
        } else if (operand.operator.equals(kind + "add")) {
            var parts = new ArrayList<Expression>();
            for (var part : operand.operands) {
                parts.add(of(operator, List.of(part), size));
            }
            return of(kind + "add", parts, size);
        } else {
            return null;
        }
    }

    /**
     * A sum with its operands flattened and its constants added into one, or nothing when there
     * is nothing to fold.
     */
    private static Expression foldedSum(String operator, List<Expression> operands, int size) {
        var kind = operator.charAt(0);
        var flat = new ArrayList<Expression>();
        for (var operand : operands) {
            if (operand.operator.equals(operator)) {
                flat.addAll(operand.operands);
            } else {
                flat.add(operand);
            }
        }

        long constant = 0;
        var constants = 0;
        var rest = new ArrayList<Expression>();
        for (var operand : flat) {
            var value = wholeOf(operand);
            if (value == null) {
                rest.add(operand);
            } else {
                constant += value;
                constants++;
            }
        }
        if (kind == 'i') {
            constant = (int) constant;
        }

        if (constants == 0 || (constants == 1 && constant != 0)) {
            return null;
        } else if (constant != 0) {
            rest.add(whole(kind, constant));
        }

        if (rest.isEmpty()) {
            return whole(kind, 0);
        } else if (rest.size() == 1) {
            return rest.get(0);
        } else {
            return of(operator, rest, size);
        }
    }

    /**
     * A shift of a masked value written as a mask of the shifted value, when the two are the same.
     *
     * The obfuscator writes {@code (x & m) >> 16} with a mask whose low bits are noise, where the
     * source says {@code (x >> 16) & 255}. With a mask that has no sign bit the masked value is
     * never negative, so shifting it and then masking by the shifted mask gives the same bits.
     */
    private static Expression maskedShift(String operator, List<Expression> operands, int size) {
        var distance = wholeOf(operands.get(1));
        var value = operands.get(0);
        if (distance == null || !value.operator.equals("iand") || value.operands.size() != 2) {
            return null;
        }

        Long mask = null;
        Expression masked = null;
        for (var operand : value.operands) {
            var constant = wholeOf(operand);
            if (constant != null && mask == null) {
                mask = constant;
            } else {
                masked = operand;
            }
        }
        if (mask == null || masked == null || mask < 0) {
            return null;
        }

        var shift = (int) (distance & 31);
        var shifted = of(operator, List.of(masked, whole('i', shift)), size);
        return of("iand", List.of(shifted, whole('i', (int) mask.longValue() >>> shift)), size);
    }

    private static Expression shifted(String operator, List<Expression> operands, int size, int bits) {
        var distance = wholeOf(operands.get(1));
        if (distance == null || (distance & bits) == distance) {
            return null;
        } else {
            return of(operator, List.of(operands.get(0), whole('i', distance & bits)), size);
        }
    }

    private static Long wholeOf(Expression value) {
        if (value.operator.startsWith("int ") || value.operator.startsWith("long ")) {
            try {
                return Long.parseLong(value.operator.substring(value.operator.indexOf(' ') + 1));
            } catch (NumberFormatException ignored) {
                return null;
            }
        } else {
            return null;
        }
    }

    private static Expression whole(char kind, long value) {
        return kind == 'i' ? leaf("int " + (int) value, 1) : leaf("long " + value, 2);
    }

    private static Double constantOf(Expression value) {
        if (value.operator.startsWith("float ") || value.operator.startsWith("double ")) {
            try {
                return Double.parseDouble(value.operator.substring(value.operator.indexOf(' ') + 1));
            } catch (NumberFormatException ignored) {
                return null;
            }
        } else {
            return null;
        }
    }

    private static Expression constant(Expression like, double value) {
        if (like.operator.startsWith("float ")) {
            return leaf("float " + (float) value, 1);
        } else {
            return leaf("double " + value, 2);
        }
    }

    /**
     * The same value with its negations moved to one place, or nothing when there are none to
     * move.
     *
     * Negating a floating point number only flips its sign, so it is exact, and IEEE 754 defines
     * subtraction as the addition of a negated number. Adding a negated number is therefore the
     * same as subtracting it, to the bit, and a negated operand of a product or a quotient can
     * be taken outside it. Rounding to nearest is the same either side of nought, which is what
     * makes the last of these exact. The decompiler writes each of these either way round.
     */
    private static Expression signless(String operator, List<Expression> operands, int size) {
        var kind = operator.isEmpty() ? ' ' : operator.charAt(0);
        var negate = kind + "neg";
        var add = kind + "add";
        var subtract = kind + "sub";

        if (kind != 'f' && kind != 'd') {
            return null;
        } else if (operator.equals(negate) && operands.get(0).operator.equals(negate)) {
            return operands.get(0).operands.get(0);
        } else if (operator.equals(add) && operands.get(1).operator.equals(negate)) {
            return of(subtract, List.of(operands.get(0), operands.get(1).operands.get(0)), size);
        } else if (operator.equals(add) && operands.get(0).operator.equals(negate)) {
            return of(subtract, List.of(operands.get(1), operands.get(0).operands.get(0)), size);
        } else if (operator.equals(subtract) && operands.get(1).operator.equals(negate)) {
            return of(add, List.of(operands.get(0), operands.get(1).operands.get(0)), size);
        } else if (operator.equals(subtract) && operands.get(0).operator.equals(negate)) {
            return of(negate, List.of(of(add, List.of(operands.get(0).operands.get(0), operands.get(1)), size)), size);
        } else if ((operator.equals(kind + "mul") || operator.equals(kind + "div"))
            && (operands.get(0).operator.equals(negate) || operands.get(1).operator.equals(negate))) {
            return negatedProduct(operator, operands, negate, size);
        } else {
            return null;
        }
    }

    private static Expression negatedProduct(String operator, List<Expression> operands, String negate,
                                             int size) {
        var first = operands.get(0);
        var second = operands.get(1);
        var flips = 0;

        if (first.operator.equals(negate)) {
            first = first.operands.get(0);
            flips++;
        }
        if (second.operator.equals(negate)) {
            second = second.operands.get(0);
            flips++;
        }

        var product = of(operator, List.of(first, second), size);
        if (flips == 1) {
            return of(negate, List.of(product), size);
        } else {
            return product;
        }
    }

    /**
     * Whether this value and another could be the same value, taking a value that arrived by more
     * than one path, on either side, to stand for any value at all.
     *
     * The jar reuses a local where the source has a new one, or computes a value before a branch
     * that the source computes inside it, and the value then reaches its use by more than one path
     * on one side only. What it is made from is the same, so it is not a difference.
     *
     * Such a value may stand only for one that rounds nothing, such as a parameter, a field, a
     * constant or integer arithmetic. A loop carries a floating point value round by more than
     * one path, and letting it stand for any sum would hide the very regrouping this looks for.
     */
    public boolean couldBe(Expression other) {
        if (this == other) {
            return true;
        } else if (operator.equals("merged")) {
            return !other.floating;
        } else if (other.operator.equals("merged")) {
            return !floating;
        } else if (!operator.equals(other.operator) || operands.size() != other.operands.size()) {
            return false;
        } else if (EXACT.contains(operator) || COMMUTATIVE.contains(operator)) {
            return pairUp(operands, new ArrayList<>(other.operands));
        } else {
            for (var at = 0; at < operands.size(); at++) {
                if (!operands.get(at).couldBe(other.operands.get(at))) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Whether each of one set of operands could be one of the other set, each used once.
     */
    private static boolean pairUp(List<Expression> left, List<Expression> right) {
        if (left.isEmpty()) {
            return true;
        } else {
            var first = left.get(0);
            var remaining = left.subList(1, left.size());
            for (var at = 0; at < right.size(); at++) {
                if (first.couldBe(right.get(at))) {
                    var rest = new ArrayList<>(right);
                    rest.remove(at);
                    if (pairUp(remaining, rest)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public int id() {
        return id;
    }

    public String operator() {
        return operator;
    }

    public List<Expression> operands() {
        return operands;
    }

    /**
     * How many slots of a frame the value takes, two for a long or a double and one otherwise.
     */
    public int size() {
        return size;
    }

    @Override
    public int getSize() {
        return size;
    }

    /**
     * Whether any part of the value is rounded to floating point.
     */
    public boolean floating() {
        return floating;
    }

    /**
     * Whether this node itself rounds to floating point, whatever it is made from.
     */
    public boolean rounds() {
        return FLOATING.contains(operator);
    }

    /**
     * Every node of the value that rounds to floating point, the value itself included.
     */
    public Set<Expression> rounding() {
        var seen = new HashSet<Expression>();
        var waiting = new ArrayDeque<Expression>(List.of(this));
        var found = new HashSet<Expression>();

        while (!waiting.isEmpty()) {
            var next = waiting.pop();

            if (!seen.add(next)) {
                /* empty */
            } else {
                if (next.rounds()) {
                    found.add(next);
                }
                waiting.addAll(next.operands);
            }
        }

        return Set.copyOf(found);
    }

    /**
     * Every leaf the value is made from.
     *
     * A shared part of the value is looked at once, however many times it is used, because a
     * value built from its own parts again and again would otherwise take exponential time.
     */
    public Set<String> leaves() {
        var seen = new HashSet<Expression>();
        var waiting = new ArrayDeque<Expression>(List.of(this));
        var found = new HashSet<String>();

        while (!waiting.isEmpty()) {
            var next = waiting.pop();

            if (!seen.add(next)) {
                /* empty */
            } else if (next.operands.isEmpty()) {
                found.add(next.operator);
            } else {
                waiting.addAll(next.operands);
            }
        }

        return Set.copyOf(found);
    }

    /**
     * The expression written out, no deeper than the given depth, with each member it names
     * written the way the recompiled client names it.
     */
    public String render(int depth, Naming naming) {
        if (operands.isEmpty()) {
            return naming.readable(operator);
        } else if (depth == 0) {
            return naming.readable(operator) + "(...)";
        } else {
            return naming.readable(operator) + "(" + operands.stream()
                .map(operand -> operand.render(depth - 1, naming))
                .collect(Collectors.joining(", ")) + ")";
        }
    }
}
