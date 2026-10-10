package type.entity;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.idktype.IDKType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The identity kits, the parts a player's body is built from where it wears nothing over them
 * ({@code IDKTypeList}): the meshes of the body part, the meshes of the head the client shows in
 * conversation, and the colour and texture swaps. The decoder reads a byte for code 1 and drops
 * it, and reads nothing for code 3, which the file holds as {@code ignored1}, null where the entry
 * gives none, and {@code ignored3}, whether the entry holds code 3.
 */
public final class IdkKind implements ConfigKind<IDKType> {

    private static final Codes CODES = Codes.of()
        .code(1, "ignored1")
        .code(2, "meshes")
        .code(3, "ignored3")
        .code(40, "recol_s", "recol_d")
        .code(41, "retex_s", "retex_d")
        .codes(60, 69, "headMeshes");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "typeList", "The type list the identity kit belongs to."
    );

    @Override
    public String directory() {
        return "idks";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.IDKTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public IDKType create(int id) {
        return new IDKType();
    }

    @Override
    public void decode(IDKType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 1) {
            captured.put("ignored1", payload.g1());
        } else if (code == 3) {
            captured.put("ignored3", true);
        }
    }

    @Override
    public Map<String, Object> json(Decoded<IDKType> type) {
        var written = Fields.written(this, type);
        written.putIfAbsent("ignored1", null);
        written.putIfAbsent("ignored3", false);
        return written;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}
