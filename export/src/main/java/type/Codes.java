package type;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Every code a type's decoder reads, with the fields of the type's file that hold what each code
 * gives. A code that is not here stops the export, as it would be data the file could not hold.
 *
 * <p>The fields are the keys of the written file: the client's names for the fields the code
 * sets, or the names of the values the export keeps where the client drops what a code gives.
 */
public final class Codes {

    private final Map<Integer, List<String>> fields = new TreeMap<>();

    public static Codes of() {
        return new Codes();
    }

    /**
     * One code, and the fields that hold what it gives.
     */
    public Codes code(int code, String... named) {
        if (fields.put(code, List.of(named)) != null) {
            throw new IllegalArgumentException("Code " + code + " is listed twice.");
        }
        return this;
    }

    /**
     * Every code from one to another, both included, each held by the same fields.
     */
    public Codes codes(int from, int to, String... named) {
        for (var code = from; code <= to; code++) {
            code(code, named);
        }
        return this;
    }

    public Optional<List<String>> fieldsOf(int code) {
        return Optional.ofNullable(fields.get(code));
    }

    public Map<Integer, List<String>> all() {
        return Map.copyOf(fields);
    }
}
