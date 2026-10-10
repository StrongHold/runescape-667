package type.var;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.vartype.VarcType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The client's own variables, which its scripts set and read ({@code VarcTypeList}): the type of
 * value one holds, and whether the client forgets it at log out.
 */
public final class VarcKind implements ConfigKind<VarcType> {

    private static final Codes CODES = Codes.of()
        .code(1, "dataType")
        .code(2, "temporary");

    @Override
    public String directory() {
        return "varcs";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.VARC);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public VarcType create(int id) {
        return new VarcType();
    }

    @Override
    public void decode(VarcType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<VarcType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
