package type.data;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.invtype.InvType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The inventories the server keeps objects in ({@code InvTypeList}): how many slots one has.
 */
public final class InvKind implements ConfigKind<InvType> {

    private static final Codes CODES = Codes.of()
        .code(2, "size");

    @Override
    public String directory() {
        return "invs";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.INVTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public InvType create(int id) {
        return new InvType();
    }

    @Override
    public void decode(InvType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<InvType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
