package type.var;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.vartype.clan.VarClanSettingType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The variables of a clan's settings ({@code VarClanSettingTypeList}): the type of value one
 * holds, and where it is a run of bits of another setting, that setting by {@code id} and the
 * first and last bit of the run.
 */
public final class VarClanSettingKind implements ConfigKind<VarClanSettingType> {

    private static final Codes CODES = Codes.of()
        .code(1, "dataType")
        .code(3, "id", "start", "end");

    @Override
    public String directory() {
        return "varclansettings";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.VAR_CLAN_SETTING);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public VarClanSettingType create(int id) {
        return new VarClanSettingType();
    }

    @Override
    public void decode(VarClanSettingType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<VarClanSettingType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
