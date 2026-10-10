package type.ui;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.List;
import java.util.Map;

/**
 * The hit splat types ({@code HitmarkTypeList}), which {@code exportHitmarks} writes into one
 * file with the graphics defaults: every field under the client's name, with null for a field that
 * names nothing, no sprite, no font, no fade or no rule to displace a splat. Code 11 sets
 * {@code fadeTime} to 0 and code 14 to the number it gives.
 */
public final class HitmarkKind implements ConfigKind<HitmarkType> {

    /**
     * What the client keeps in a field that names nothing.
     */
    private static final int NONE = -1;

    /**
     * The fields that hold null where they name nothing.
     */
    private static final List<String> NULLABLE = List.of("icon", "left", "inner", "right", "font", "fadeTime",
        "comparisonType");

    private static final Codes CODES = Codes.of()
        .code(1, "font")
        .code(2, "textColour")
        .code(3, "icon")
        .code(4, "left")
        .code(5, "inner")
        .code(6, "right")
        .code(7, "offsetX")
        .code(8, "amountString")
        .code(9, "duration")
        .code(10, "offsetY")
        .code(11, "fadeTime")
        .code(12, "comparisonType")
        .code(13, "textOffsetY")
        .code(14, "fadeTime");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "myList", "The type list the hitmark belongs to."
    );

    @Override
    public String directory() {
        return "hitmarks";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.HITMARKTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public HitmarkType create(int id) {
        return new HitmarkType();
    }

    @Override
    public void decode(HitmarkType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public Map<String, Object> json(Decoded<HitmarkType> type) {
        var written = Fields.written(this, type);
        for (var field : NULLABLE) {
            if (Integer.valueOf(NONE).equals(written.get(field))) {
                written.put(field, null);
            }
        }
        return written;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}
