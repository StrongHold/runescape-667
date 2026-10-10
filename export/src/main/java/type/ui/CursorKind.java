package type.ui;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.cursortype.CursorType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;

/**
 * The mouse cursors a type's options can show ({@code CursorTypeList}): the sprite, and the pixel
 * of it the pointer is at.
 */
public final class CursorKind implements ConfigKind<CursorType> {

    private static final Codes CODES = Codes.of()
        .code(1, "graphic")
        .code(2, "hotspotx", "hotspoty");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "myList", "The type list the cursor belongs to."
    );

    @Override
    public String directory() {
        return "cursors";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.CURSORTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public CursorType create(int id) {
        return new CursorType();
    }

    @Override
    public void decode(CursorType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public Map<String, Object> json(Decoded<CursorType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}
