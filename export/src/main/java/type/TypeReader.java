package type;

import com.jagex.core.io.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Reads one entry of a config type with the client's own decoder, one code at a time, as the
 * type's own loop does, and stops at a code the kind does not list or at bytes the decoder leaves
 * unread, so that nothing the entry holds is lost unseen.
 *
 * <p>The entry is decoded twice, into a type that stays as the decoder leaves it and into one that
 * {@code postDecode} then runs over, as the type list does.
 */
public final class TypeReader {

    public static <T> Decoded<T> read(ConfigKind<T> kind, int id, byte[] data) {
        var decoded = kind.create(id);
        var held = kind.create(id);
        var captured = new LinkedHashMap<String, Object>();
        var codes = new ArrayList<Integer>();
        var packet = new Packet(data);
        var again = new Packet(data);

        for (var code = packet.g1(); code != 0; code = packet.g1()) {
            if (kind.codes().fieldsOf(code).isEmpty()) {
                throw new IllegalStateException(kind.directory() + " " + id + " holds code " + code + " at byte "
                    + (packet.pos - 1) + ", which no field of the export holds. The entry is "
                    + java.util.HexFormat.of().formatHex(data) + ".");
            }
            codes.add(code);
            var from = packet.pos;
            kind.decode(decoded, id, code, packet);
            kind.capture(code, new Packet(Arrays.copyOfRange(data, from, packet.pos)), captured);
            again.g1();
            kind.decode(held, id, code, again);
        }

        if (packet.pos != data.length) {
            throw new IllegalStateException(kind.directory() + " " + id + " ends at byte " + packet.pos + " of "
                + data.length + ", so the decoder leaves bytes unread.");
        }

        kind.postDecode(held, id);
        return new Decoded<>(id, decoded, held, captured, codes);
    }

    private TypeReader() {
        /* empty */
    }
}
