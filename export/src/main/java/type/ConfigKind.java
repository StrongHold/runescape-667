package type;

import com.jagex.core.io.Packet;

import java.util.Map;
import java.util.Set;

/**
 * One config type of the client, as the export reads its entries and writes each as a JSON file.
 *
 * <p>An entry is read with the client's own decoder, one code at a time ({@link TypeReader}),
 * and every code must be one the kind lists ({@link #codes}). The file holds every field of the
 * type ({@link TypeFile} checks that none is left out but those {@link #unwritten} names), under
 * the client's name for it, as the decoder leaves it. Where the client's {@code postDecode} works
 * out a field, the file holds the worked out value ({@link #derived}). Where it fills in a default
 * that the entry left unset, the file holds the entry's own value ({@link #settled}) and a reader
 * fills in the default as the client does. What the decoder reads and then drops, keeps outside
 * the type, or folds so that the type cannot give it back is kept by {@link #capture}.
 */
public interface ConfigKind<T> {

    /**
     * The directory of the export the entries are written to, one file an entry, named by id.
     */
    String directory();

    ConfigArchive archive();

    Codes codes();

    /**
     * A type as the client's type list makes one before it decodes the entry into it.
     */
    T create(int id);

    /**
     * The client's decoder of one code.
     */
    void decode(T type, int id, int code, Packet packet);

    /**
     * The client's {@code postDecode}, where the type list runs one.
     */
    default void postDecode(T type, int id) {
        /* empty */
    }

    /**
     * Keeps, by name, what one code gives that the decoded type cannot give back: a value the
     * decoder reads and drops, one it keeps outside the type, or one it folds. The packet holds
     * the code's payload alone.
     */
    default void capture(int code, Packet payload, Map<String, Object> captured) {
        /* empty */
    }

    /**
     * The file of an entry.
     */
    Map<String, Object> json(Decoded<T> type);

    /**
     * The fields of the type that no file holds, each with why: the type list it belongs to, a
     * cache of what the client built from it, or the entry's own id, which the file's name gives.
     */
    Map<String, String> unwritten();

    /**
     * The fields the file holds under another key, by the field.
     */
    default Map<String, String> writtenAs() {
        return Map.of();
    }

    /**
     * The fields that {@code postDecode} works out, which the file holds as worked out.
     */
    default Set<String> derived() {
        return Set.of();
    }

    /**
     * The fields to which {@code postDecode} gives a default where the entry leaves them unset,
     * which the file holds as the entry gives them.
     */
    default Set<String> settled() {
        return Set.of();
    }
}
