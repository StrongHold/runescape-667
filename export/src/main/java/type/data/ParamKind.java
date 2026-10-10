package type.data;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.paramtype.ParamType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The parameters a type can carry ({@code ParamTypeList}): the type of value one holds, a
 * character of the client's script types, its default as a number or as a string, and whether the
 * object list drops it from a members' object in a free world.
 */
public final class ParamKind implements ConfigKind<ParamType> {

    private static final Codes CODES = Codes.of()
        .code(1, "type")
        .code(2, "defaultint")
        .code(4, "autodisable")
        .code(5, "defaultstr");

    @Override
    public String directory() {
        return "params";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.PARAMTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public ParamType create(int id) {
        return new ParamType();
    }

    @Override
    public void decode(ParamType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<ParamType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
