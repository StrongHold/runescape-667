package type.ground;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The floor underlays a map square's tiles name ({@code FloorUnderlayTypeList}): the colour as
 * RGB, with the hue, its weight, the saturation and the lightness the decoder works out of it
 * ({@code FloorUnderlayType.computeHsl}), the texture, its size, and whether the tile takes shadows
 * and hides what is below it.
 */
public final class FloorUnderlayKind implements ConfigKind<FloorUnderlayType> {

    private static final Codes CODES = Codes.of()
        .code(1, "colour", "hue", "hueWeight", "saturation", "lightness")
        .code(2, "texture")
        .code(3, "size")
        .code(4, "allowShadow")
        .code(5, "occludes");

    @Override
    public String directory() {
        return "floorunderlays";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.FLUTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public FloorUnderlayType create(int id) {
        return new FloorUnderlayType();
    }

    @Override
    public void decode(FloorUnderlayType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<FloorUnderlayType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return Map.of();
    }
}
