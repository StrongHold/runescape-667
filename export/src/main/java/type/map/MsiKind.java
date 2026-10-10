package type.map;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.msitype.MSIType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The map scene icons, the small pictures the minimap draws for a location ({@code MSITypeList}):
 * the sprite, a colour, and whether the client draws it larger. Code 4 clears the sprite.
 */
public final class MsiKind implements ConfigKind<MSIType> {

    private static final Codes CODES = Codes.of()
        .code(1, "image")
        .code(2, "colour")
        .code(3, "enlarge")
        .code(4, "image");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "myList", "The type list the icon belongs to."
    );

    @Override
    public String directory() {
        return "msis";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.MSITYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public MSIType create(int id) {
        return new MSIType();
    }

    @Override
    public void decode(MSIType type, int id, int code, Packet packet) {
        type.decode(code, packet);
    }

    @Override
    public Map<String, Object> json(Decoded<MSIType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}
