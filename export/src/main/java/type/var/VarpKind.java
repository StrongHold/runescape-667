package type.var;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.vartype.player.VarPlayerType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The player's variables, which the server sets and the client's scripts read
 * ({@code VarPlayerTypeListClient}): only the code of the client's own behaviour a variable
 * drives, where it drives one.
 */
public final class VarpKind implements ConfigKind<VarPlayerType> {

    private static final Codes CODES = Codes.of()
        .code(5, "clientCode");

    @Override
    public String directory() {
        return "varps";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.VARP);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public VarPlayerType create(int id) {
        return new VarPlayerType();
    }

    @Override
    public void decode(VarPlayerType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public Map<String, Object> json(Decoded<VarPlayerType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
