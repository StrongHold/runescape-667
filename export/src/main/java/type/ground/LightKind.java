package type.ground;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.lighttype.LightType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * How a light that a map square places flickers ({@code LightTypeList}): the pattern of its
 * flicker, how fast and how far it swings, and what it adds to its own brightness.
 */
public final class LightKind implements ConfigKind<LightType> {

    private static final Codes CODES = Codes.of()
        .code(1, "pattern")
        .code(2, "frequency")
        .code(3, "amplitude")
        .code(4, "ambient");

    @Override
    public String directory() {
        return "lights";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.LIGHTTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public LightType create(int id) {
        return new LightType();
    }

    @Override
    public void decode(LightType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<LightType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
