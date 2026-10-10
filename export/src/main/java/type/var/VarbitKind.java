package type.var;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.vartype.bit.VarBitType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The variables that are a run of bits of a player's variable ({@code VarBitTypeListClient}): the
 * variable, and the first and last bit of the run. The client reads them from the archive that
 * {@code Js5Archive} calls {@code CONFIG_STRUCT}, with an id split into a group and 1,024 files.
 */
public final class VarbitKind implements ConfigKind<VarBitType> {

    private static final int FILE_BITS = 10;

    private static final Codes CODES = Codes.of()
        .code(1, "baseVar", "startBit", "endBit");

    @Override
    public String directory() {
        return "varbits";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.everyGroup(Js5Archive.CONFIG_STRUCT, FILE_BITS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public VarBitType create(int id) {
        return new VarBitType();
    }

    @Override
    public void decode(VarBitType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public Map<String, Object> json(Decoded<VarBitType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
