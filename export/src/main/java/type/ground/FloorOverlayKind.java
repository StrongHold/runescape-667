package type.ground;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.flotype.FloorOverlayType;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.js5.Js5Archive;
import type.Archives;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;
import java.util.Set;

/**
 * The floor overlays a map square's tiles name ({@code FloorOverlayTypeList}). The decoder turns
 * the colours codes 1 and 7 give from RGB into the client's HSL ({@code FloorOverlayType.colour},
 * magenta as -1), which loses bits, so the file also holds each as given, {@code colourRgb} and
 * {@code blendColourRgb}, null where the entry gives none. Code 8 makes the overlay the type list's
 * default ({@code FloorOverlayTypeList.dflt}), which the file holds as {@code dflt}. The type
 * list's {@code postDecode} puts the id below the blend priority, {@code (blendPriority << 8) | id},
 * which the file holds as worked out.
 */
public final class FloorOverlayKind implements ConfigKind<FloorOverlayType> {

    private static final int LANGUAGE = 0;

    private static final Codes CODES = Codes.of()
        .code(1, "colour", "colourRgb")
        .code(2, "texture")
        .code(3, "texture")
        .code(5, "occludes")
        .code(7, "blendColour", "blendColourRgb")
        .code(8, "dflt")
        .code(9, "size")
        .code(10, "blockShadow")
        .code(11, "blendPriority")
        .code(12, "blendable")
        .code(13, "waterColour")
        .code(14, "waterDepth")
        .code(16, "waterBias");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "myList", "The type list the overlay belongs to.",
        "id", "The overlay's own id, which the file's name gives."
    );

    private final FloorOverlayTypeList list;

    public FloorOverlayKind(Archives archives) {
        this.list = new FloorOverlayTypeList(ModeGame.RUNESCAPE, LANGUAGE, archives.js5(Js5Archive.CONFIG));
    }

    @Override
    public String directory() {
        return "flooroverlays";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.FLOTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public FloorOverlayType create(int id) {
        var type = new FloorOverlayType();
        type.myList = list;
        type.id = id;
        return type;
    }

    @Override
    public void decode(FloorOverlayType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public void postDecode(FloorOverlayType type, int id) {
        type.postDecode();
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 1) {
            captured.put("colourRgb", payload.g3());
        } else if (code == 7) {
            captured.put("blendColourRgb", payload.g3());
        } else if (code == 8) {
            captured.put("dflt", true);
        }
    }

    @Override
    public Map<String, Object> json(Decoded<FloorOverlayType> type) {
        var written = Fields.written(this, type);
        written.putIfAbsent("colourRgb", null);
        written.putIfAbsent("blendColourRgb", null);
        written.putIfAbsent("dflt", false);
        return written;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }

    @Override
    public Set<String> derived() {
        return Set.of("blendPriority");
    }
}
