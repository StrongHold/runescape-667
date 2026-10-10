package type.map;

import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.meltype.MapElementType;
import com.jagex.js5.Js5Archive;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Fields;

import java.util.Map;
import java.util.Set;

/**
 * The map elements, the icons, labels and landmark shapes of the world map and the minimap
 * ({@code MapElementTypeList}). The type list's {@code postDecode} works out the bounds of the
 * landmark's polygons, {@code minX}, {@code maxX}, {@code minZ} and {@code maxZ}, which the file
 * holds as worked out. Code 7 gives two flags in a byte, and a byte with another bit set stops the
 * export, as the decoder would drop it.
 */
public final class MapElementKind implements ConfigKind<MapElementType> {

    /**
     * The bits of code 7's byte the decoder reads: whether the element is enabled, and whether
     * the minimap shows it.
     */
    private static final int FLAGS_READ = 0x3;

    private static final Codes CODES = Codes.of()
        .code(1, "sprite")
        .code(2, "hoverSprite")
        .code(3, "text")
        .code(4, "textColour")
        .code(5, "hoverTextColour")
        .code(6, "textSize")
        .code(7, "enabled", "showOnMinimap")
        .code(8, "randomise")
        .code(9, "multiVarp", "multiVarBit", "landmarkVarStart", "landmarkVarEnd")
        .codes(10, 14, "ops")
        .code(15, "landmarkPolygons", "landmarkBackground", "landmarkPalette", "landmarkColorIndices")
        .code(16, "showOnWorldMap")
        .code(17, "opBase")
        .code(18, "worldMapSprite")
        .code(19, "category")
        .code(20, "secondaryVarBit", "secondaryVarp", "secondaryVarStart", "secondaryVarEnd")
        .code(21, "outlineColour")
        .code(22, "fillColour")
        .code(23, "dashLength", "gapLength", "dashPhase")
        .code(24, "textOffsetX", "textOffsetY")
        .code(249, "params");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "myList", "The type list the element belongs to.",
        "id", "The element's own id, which the file's name gives."
    );

    @Override
    public String directory() {
        return "mapelements";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.MELTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public MapElementType create(int id) {
        var type = new MapElementType();
        type.id = id;
        return type;
    }

    @Override
    public void decode(MapElementType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public void postDecode(MapElementType type, int id) {
        type.postDecode();
    }

    @Override
    public void capture(int code, Packet payload, Map<String, Object> captured) {
        if (code == 7 && (payload.g1() & ~FLAGS_READ) != 0) {
            throw new IllegalStateException("A map element sets a bit of code 7 that the client does not read.");
        }
    }

    @Override
    public Map<String, Object> json(Decoded<MapElementType> type) {
        return Fields.written(this, type);
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }

    @Override
    public Set<String> derived() {
        return Set.of("minX", "maxX", "minZ", "maxZ");
    }
}
