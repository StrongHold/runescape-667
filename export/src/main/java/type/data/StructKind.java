package type.data;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.structtype.StructType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The structures the client's scripts read parameters from ({@code StructTypeList}): parameters
 * alone, keyed by parameter.
 */
public final class StructKind implements ConfigKind<StructType> {

    private static final Codes CODES = Codes.of()
        .code(249, "params");

    @Override
    public String directory() {
        return "structs";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.STRUCTTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public StructType create(int id) {
        return new StructType();
    }

    @Override
    public void decode(StructType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<StructType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
