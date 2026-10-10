package type;

import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.LongNode;
import com.jagex.core.datastruct.key.StringNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Turns a value a config type holds into the value its JSON file holds: a number, a flag or a
 * string as it is, a character as a string of one character, an array as a list, and a table of
 * the client's keyed by its keys as text, each node as the value it holds.
 *
 * <p>A short or a byte is written as the client holds it, signed.
 */
public final class Values {

    public static Object of(Object value) {
        return switch (value) {
            case null -> null;
            case Character character -> Character.toString(character);
            case Boolean flag -> flag;
            case Number number -> number;
            case String text -> text;
            case IterableHashTable table -> table(table);
            case IntNode node -> node.value;
            case StringNode node -> node.value;
            case LongNode node -> node.value;
            default -> {
                if (value.getClass().isArray()) {
                    yield list(value);
                }
                throw new IllegalArgumentException("A type holds a " + value.getClass() + ", which no file can hold.");
            }
        };
    }

    private static List<Object> list(Object array) {
        var list = new ArrayList<Object>();
        for (var i = 0; i < Array.getLength(array); i++) {
            list.add(of(Array.get(array, i)));
        }
        return list;
    }

    /**
     * A table keyed by its keys as text. Where the table holds a key more than once, the client
     * finds the node it was given first ({@code IterableHashTable.get}), so that is the one kept
     * here, and {@link #shadowed} gives the others.
     */
    private static Map<String, Object> table(IterableHashTable table) {
        var map = new TreeMap<String, Object>();
        for (var node = table.first(); node != null; node = table.next()) {
            map.putIfAbsent(Long.toString(node.key), of(node));
        }
        return map;
    }

    /**
     * The nodes of a table under a key it holds already, which the client never finds, each as its
     * key and its value, in the order the table holds them, or none where the table holds each key
     * once or is null.
     */
    public static List<List<Object>> shadowed(IterableHashTable table) {
        var shadowed = new ArrayList<List<Object>>();
        if (table != null) {
            var seen = new java.util.HashSet<Long>();
            for (var node = table.first(); node != null; node = table.next()) {
                if (!seen.add(node.key)) {
                    shadowed.add(List.of(node.key, of(node)));
                }
            }
        }
        return shadowed;
    }

    private Values() {
        /* empty */
    }
}
