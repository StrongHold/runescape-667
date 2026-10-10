package type;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * The JSON file of one entry of a config type, checked: it holds every field of the type that the
 * kind does not say it leaves out, under no name the client has not given, and every field that a
 * code the decoder reads is held by. Also, {@code postDecode} changes no field the kind does not say
 * it works out or settles.
 */
public final class TypeFile {

    public static <T> Map<String, Object> of(ConfigKind<T> kind, int id, byte[] data) {
        var type = TypeReader.read(kind, id, data);
        Map<String, Object> file;
        try {
            file = kind.json(type);
        } catch (RuntimeException failure) {
            throw new IllegalStateException(kind.directory() + " " + id + ": " + failure.getMessage(), failure);
        }
        check(kind, type, file);
        return file;
    }

    private static <T> void check(ConfigKind<T> kind, Decoded<T> type, Map<String, Object> file) {
        var where = kind.directory() + " " + type.id();
        var names = new HashSet<String>();
        for (var field : Fields.of(type.decoded().getClass())) {
            var name = field.getName();
            names.add(name);
            if (!kind.unwritten().containsKey(name)) {
                checkField(kind, type, file, where, name);
            }
        }

        var declared = new TreeSet<String>();
        declared.addAll(kind.unwritten().keySet());
        declared.addAll(kind.writtenAs().keySet());
        declared.addAll(kind.derived());
        declared.addAll(kind.settled());
        declared.removeAll(names);
        if (!declared.isEmpty()) {
            throw new IllegalStateException(kind.directory() + " names fields its type does not hold: " + declared);
        }

        Set<String> missing = new TreeSet<>();
        kind.codes().all().values().forEach(fields -> fields.stream()
            .map(name -> kind.writtenAs().getOrDefault(name, name))
            .filter(key -> !file.containsKey(key))
            .forEach(missing::add));
        if (!missing.isEmpty()) {
            throw new IllegalStateException(where + ": the file leaves out what codes give: " + missing);
        }
    }

    private static <T> void checkField(ConfigKind<T> kind, Decoded<T> type, Map<String, Object> file, String where,
            String name) {
        var key = kind.writtenAs().getOrDefault(name, name);
        var empty = Fields.emptyArray(type.decoded(), name);
        var changed = !Objects.equals(Fields.value(type.decoded(), name), Fields.value(type.held(), name));
        if (Fields.unnamed(name) && !kind.writtenAs().containsKey(name)) {
            throw new IllegalStateException(where + ": the field " + name + " has no name the client gives it.");
        } else if (empty) {
            throw new IllegalStateException(where + ": the entry gives an empty " + name
                + ", which the file could not tell from none.");
        } else if (!file.containsKey(key)) {
            throw new IllegalStateException(where + ": the file leaves out the field " + name + ".");
        } else if (changed && !kind.derived().contains(name) && !kind.settled().contains(name)) {
            throw new IllegalStateException(where + ": postDecode changes the field " + name
                + ", which the kind neither works out nor settles.");
        }
    }

    private TypeFile() {
        /* empty */
    }
}
