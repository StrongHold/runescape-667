import java.util.List;
import java.util.Map;

/**
 * Writes a tree of maps, lists, strings, numbers and booleans as JSON.
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

    private static void writeObject(Map<?, ?> map, StringBuilder out) {
        out.append('{');
        var first = true;
        for (var entry : map.entrySet()) {
            if (!first) {
                out.append(',');
            }
            writeString((String) entry.getKey(), out);
            out.append(':');
            write(entry.getValue(), out);
            first = false;
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
