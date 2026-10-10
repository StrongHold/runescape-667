package type.var;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.vartype.clan.VarClanType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The variables of a clan's channel ({@code VarClanTypeList}): the type of value one holds, and
 * where it is a run of bits of another, that variable and the first and last bit of the run.
 */
public final class VarClanKind implements ConfigKind<VarClanType> {

    private static final Codes CODES = Codes.of()
        .code(1, "dataType")
        .code(2, "baseVar", "startBit", "endBit");

    @Override
    public String directory() {
        return "varclans";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.VAR_CLAN);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public VarClanType create(int id) {
        return new VarClanType();
    }

    @Override
    public void decode(VarClanType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<VarClanType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
