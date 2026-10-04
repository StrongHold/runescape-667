import java.util.List;
import java.util.Map;

/**
 * Writes a tree of maps, lists, strings, numbers, booleans and nulls as JSON.
 *
 * A glTF file needs only this much JSON, and none of it is ever read back, so a library for it
 * would be more than the job asks for.
 */
public final class Json {

    public static String write(Object value) {
        var out = new StringBuilder();
        write(value, out);
        return out.toString();
    }

    private static void write(Object value, StringBuilder out) {
        switch (value) {
            case null -> out.append("null");
            case Map<?, ?> map -> writeObject(map, out);
            case List<?> list -> writeArray(list, out);
            case String text -> writeString(text, out);
            case Float number -> out.append(finite(number));
            case Double number -> out.append(finite(number.floatValue()));
            case Number number -> out.append(number.longValue());
            case Boolean bool -> out.append(bool);
            default -> throw new IllegalArgumentException("JSON has no way to hold a " + value.getClass());
        }
    }

    /**
     * Writes an object's keys in alphabetical order. The order a map hands them out in is not
     * always the same from one run to the next, and the file written must be the same bytes
     * every time it is written from the same cache.
     */
    private static void writeObject(Map<?, ?> map, StringBuilder out) {
        out.append('{');
        var keys = map.keySet().stream().map(String.class::cast).sorted().toList();
        for (var key : keys) {
            if (!key.equals(keys.getFirst())) {
                out.append(',');
            }
            writeString(key, out);
            out.append(':');
            write(map.get(key), out);
        }
        out.append('}');
    }

    private static void writeArray(List<?> list, StringBuilder out) {
        out.append('[');
        for (var i = 0; i < list.size(); i++) {
            if (i > 0) {
                out.append(',');
            }
            write(list.get(i), out);
        }
        out.append(']');
    }

    private static void writeString(String text, StringBuilder out) {
        out.append('"');
        for (var c : text.toCharArray()) {
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        out.append('"');
    }

    private static String finite(float number) {
        if (!Float.isFinite(number)) {
            throw new IllegalArgumentException("JSON has no way to hold " + number);
        }
        return Float.toString(number);
    }

    private Json() {
        /* empty */
    }
}
