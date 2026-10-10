package type;

import java.util.List;
import java.util.Map;

/**
 * One entry of a config type, read: the type as the client's decoder leaves it, the same type
 * once the client's {@code postDecode} has run over it, and what the export kept of the entry
 * that the type itself cannot give back, by name.
 *
 * @param id the entry's id
 * @param decoded the type after the decoder alone
 * @param held the type as the client holds it after {@code postDecode}
 * @param captured what the decoder drops, keeps outside the type or folds, by the field that holds it
 * @param codes every code the entry holds, in its order
 */
public record Decoded<T>(int id, T decoded, T held, Map<String, Object> captured, List<Integer> codes) {

    /**
     * Whether the entry holds a code.
     */
    public boolean holds(int code) {
        return codes.contains(code);
    }
}
