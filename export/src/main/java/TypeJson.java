import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.StringNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Turns the arrays and tables a config type holds into the values {@link Json} writes, so that a
 * type's JSON file holds every field the client decoded, with an empty list or object for one the
 * type leaves unset.
 */
final class TypeJson {

    static List<Integer> ints(int[] values) {
        var list = new ArrayList<Integer>();
        if (values != null) {
            for (var value : values) {
                list.add(value);
            }
        }
        return list;
    }

    static List<Integer> bytes(byte[] values) {
        var list = new ArrayList<Integer>();
        if (values != null) {
            for (var value : values) {
                list.add((int) value);
            }
        }
        return list;
    }

    /**
     * A list for each slot of a table the type fills slot by slot, with an empty list for a slot
     * it leaves empty, and no slots where it fills none.
     */
    static List<List<Integer>> slots(int[][] values) {
        var list = new ArrayList<List<Integer>>();
        if (values != null) {
            for (var slot : values) {
                list.add(ints(slot));
            }
        }
        return list;
    }

    /**
     * Each value the type swaps, as a pair of the value in the mesh and the value it becomes, as
     * the client's `recol_s` and `recol_d` or `retex_s` and `retex_d` hold them.
     */
    static List<List<Integer>> swaps(short[] from, short[] to) {
        var list = new ArrayList<List<Integer>>();
        if (from != null) {
            for (var i = 0; i < from.length; i++) {
                list.add(List.of((int) from[i], (int) to[i]));
            }
        }
        return list;
    }

    /**
     * The type's parameters, keyed by their id as text, since a JSON object's keys are text, each
     * an integer or a string as the client decoded it.
     */
    static Map<String, Object> params(IterableHashTable params) {
        var map = new LinkedHashMap<String, Object>();
        if (params != null) {
            for (var node = params.first(); node != null; node = params.next()) {
                switch (node) {
                    case IntNode number -> map.put(Long.toString(node.key), number.value);
                    case StringNode text -> map.put(Long.toString(node.key), text.value);
                    default -> throw new IllegalStateException("A parameter holds a " + node.getClass());
                }
            }
        }
        return map;
    }

    /**
     * The options the type offers on the mini menu, in the client's order, with null for a slot
     * the type leaves empty: the slots the type's data can set. The client's type list adds one
     * more, Examine, to every type, which the type's data cannot change, so it is not written.
     */
    static List<String> options(String[] ops, int slots) {
        return Arrays.asList(ops).subList(0, slots);
    }

    private TypeJson() {
        /* empty */
    }
}
