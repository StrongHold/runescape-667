package type.entity;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The spot animations, the effects the client plays on an entity or a tile, such as a spell's
 * splash ({@code SpotAnimationTypeList}): the model, the sequence it plays and whether it loops it,
 * its scale, turn, lighting and swaps, and how it follows the ground. Codes 9 and 11 to 16 each set
 * {@code hillType}, some with a {@code hillValue}.
 */
public final class SpotAnimKind implements ConfigKind<SpotAnimationType> {

    private static final int FILE_BITS = 8;

    private static final Codes CODES = Codes.of()
        .code(1, "model")
        .code(2, "seq")
        .code(4, "scaleXZ")
        .code(5, "scaleY")
        .code(6, "rotation")
        .code(7, "ambient")
        .code(8, "contrast")
        .code(9, "hillType", "hillValue")
        .code(10, "loopSeq")
        .codes(11, 13, "hillType")
        .codes(14, 16, "hillType", "hillValue")
        .code(40, "recol_s", "recol_d")
        .code(41, "retex_s", "retex_d");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "myList", "The type list the spot animation belongs to.",
        "id", "The spot animation's own id, which the file's name gives."
    );

    @Override
    public String directory() {
        return "spotanims";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.everyGroup(Js5Archive.CONFIG_SPOT, FILE_BITS);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public SpotAnimationType create(int id) {
        var type = new SpotAnimationType();
        type.id = id;
        return type;
    }

    @Override
    public void decode(SpotAnimationType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<SpotAnimationType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}
