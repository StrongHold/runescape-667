package type;

import com.jagex.core.datastruct.key.IterableHashTable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The fields of a config type, read by reflection, so that a field the client adds or that the
 * export misses is found rather than left out.
 */
public final class Fields {

    /**
     * What the key of a table's shadowed nodes adds to the table's own key.
     */
    private static final String SHADOWED = "Shadowed";

    /**
     * A name the decompiler or the deobfuscator gave a field it knew nothing of, such as
     * {@code anIntArray428}, {@code aBoolean12} or {@code field123}, which no file may use.
     */
    private static final Pattern UNNAMED = Pattern.compile("^(an?[A-Z][A-Za-z]*\\d+|field\\d+|unknown\\d*|var\\d+)$");

    /**
     * Every field an instance of the type holds, by name.
     */
    public static List<Field> of(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
            .peek(field -> field.setAccessible(true))
            .sorted(Comparator.comparing(Field::getName))
            .toList();
    }

    /**
     * Whether a field of a type holds an array of no elements.
     */
    public static boolean emptyArray(Object type, String name) {
        try {
            var field = type.getClass().getDeclaredField(name);
            field.setAccessible(true);
            var value = field.get(type);
            return value != null && value.getClass().isArray() && java.lang.reflect.Array.getLength(value) == 0;
        } catch (ReflectiveOperationException failure) {
            throw new IllegalStateException(failure);
        }
    }

    public static boolean unnamed(String name) {
        return UNNAMED.matcher(name).matches();
    }

    /**
     * The value of one field of a type, as a file holds it ({@link Values}).
     */
    public static Object value(Object type, String name) {
        try {
            var field = type.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return Values.of(field.get(type));
        } catch (ReflectiveOperationException failure) {
            throw new IllegalStateException(failure);
        }
    }

    /**
     * Every field of the type that a file holds, under the client's name: as the decoder left it,
     * or, for a field {@code postDecode} works out, as worked out, followed by what the export kept
     * that the type cannot give back. An array the type leaves unset is an empty list.
     */
    public static <T> Map<String, Object> written(ConfigKind<T> kind, Decoded<T> type) {
        var written = new LinkedHashMap<String, Object>();
        for (var field : of(type.decoded().getClass())) {
            var name = field.getName();
            if (!kind.unwritten().containsKey(name)) {
                var source = kind.derived().contains(name) ? type.held() : type.decoded();
                var key = kind.writtenAs().getOrDefault(name, name);
                written.put(key, field.getType().isArray() ? listValue(source, field) : value(source, name));
                if (field.getType() == IterableHashTable.class) {
                    putShadowed(written, key, (IterableHashTable) raw(source, field));
                }
            }
        }
        written.putAll(type.captured());
        return written;
    }

    /**
     * Writes the nodes a table holds under a key it holds already, which the client never finds, as
     * {@code <key>Shadowed}, where there are any.
     */
    public static void putShadowed(Map<String, Object> written, String key, IterableHashTable table) {
        var shadowed = Values.shadowed(table);
        if (!shadowed.isEmpty()) {
            written.put(key + SHADOWED, shadowed);
        }
    }

    /**
     * An array field as a list, an empty one where the type leaves the array unset, as the files of
     * the NPCs, the locations and the base animation sets write it. {@link TypeFile} stops at an
     * entry that gives an empty array, which a file could not tell from no array.
     */
    private static Object listValue(Object type, Field field) {
        var array = raw(type, field);
        return array == null ? List.of() : Values.of(array);
    }

    private static Object raw(Object type, Field field) {
        try {
            return field.get(type);
        } catch (IllegalAccessException failure) {
            throw new IllegalStateException(failure);
        }
    }

    private Fields() {
        /* empty */
    }
}
