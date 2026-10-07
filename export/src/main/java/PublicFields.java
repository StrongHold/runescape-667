import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The public instance fields of one of the client's types by name, in the order of their names:
 * numbers, flags and lists of numbers, which is what a type holds once it is decoded.
 */
public final class PublicFields {

    public static Map<String, Object> of(Object type) {
        var values = new LinkedHashMap<String, Object>();
        var fields = type.getClass().getFields();
        Arrays.sort(fields, (a, b) -> a.getName().compareTo(b.getName()));
        for (var field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    var value = field.get(type);
                    if (value instanceof int[] numbers) {
                        values.put(field.getName(), Arrays.stream(numbers).boxed().toList());
                    } else if (value instanceof Number || value instanceof Boolean) {
                        values.put(field.getName(), value);
                    }
                } catch (IllegalAccessException failure) {
                    throw new IllegalStateException(failure);
                }
            }
        }
        return values;
    }

    private PublicFields() {
        /* empty */
    }
}
