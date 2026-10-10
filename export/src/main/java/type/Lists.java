package type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Turns the arrays a config type holds into the lists the files of the NPCs, the locations and the
 * base animation sets hold, which are older than the other kinds' files and keep their form: an
 * empty list for an array the type leaves unset.
 */
public final class Lists {

    public static List<Integer> ints(int[] values) {
        var list = new ArrayList<Integer>();
        if (values != null) {
            for (var value : values) {
                list.add(value);
            }
        }
        return list;
    }

    public static List<Integer> bytes(byte[] values) {
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
    public static List<List<Integer>> slots(int[][] values) {
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
    public static List<List<Integer>> swaps(short[] from, short[] to) {
        var list = new ArrayList<List<Integer>>();
        if (from != null) {
            for (var i = 0; i < from.length; i++) {
                list.add(List.of((int) from[i], (int) to[i]));
            }
        }
        return list;
    }

    /**
     * The options the type offers on the mini menu, in the client's order, with null for a slot
     * the type leaves empty: the slots the type's data can set. The client's type list adds one
     * more, Examine, to every type, which the type's data cannot change, so it is not written.
     */
    public static List<String> options(String[] ops, int slots) {
        return Arrays.asList(ops).subList(0, slots);
    }

    private Lists() {
        /* empty */
    }
}
