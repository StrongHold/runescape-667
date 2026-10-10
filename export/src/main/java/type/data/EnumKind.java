package type.data;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.enumtype.EnumType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The tables the client's scripts look values up in ({@code EnumTypeList}): the types of the keys
 * and the values, characters of the client's script types, the defaults, and the values. Codes 5
 * and 6 give the values as a table keyed by any number, which the file holds as an object keyed by
 * the keys as text. Codes 7 and 8 give them as an array of {@code count} slots with null where a
 * slot is empty, which the file holds as a list. Codes 5 and 7 give strings, 6 and 8 numbers.
 */
public final class EnumKind implements ConfigKind<EnumType> {

    private static final int FILE_BITS = 8;

    private static final Codes CODES = Codes.of()
        .code(1, "keyType")
        .code(2, "valType")
        .code(3, "defaultStr")
        .code(4, "defaultInt")
        .codes(5, 8, "outputCount", "output");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "reversed", "The client builds a table from each value back to its keys, the first time a script asks."
    );

    @Override
    public String directory() {
        return "enums";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.everyGroup(Js5Archive.CONFIG_ENUM, FILE_BITS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public EnumType create(int id) {
        return new EnumType();
    }

    @Override
    public void decode(EnumType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<EnumType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}
