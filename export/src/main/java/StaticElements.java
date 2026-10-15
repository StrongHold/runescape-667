import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The map elements the world map places by itself, apart from the locations: a dungeon's entrance,
 * a guild, a quest's start. The client keeps them by world map area, each area's in the group
 * {@code <file>_staticelements} of the world map's data, and its minimap shows the elements of the
 * area the player is in ({@code MapBuilder}, {@code Minimap.drawLevel}), each on the player's level
 * at the middle of its tile.
 *
 * They are given here by map square, so that a square brings its own: each element of an area that
 * stands in the square, where the area is the one the client finds for the element's own tile
 * ({@code WorldMap.getMap}), the first area it looks in whose chunks hold the tile. The client finds
 * its area by the middle of the region it has built rather than by each tile, so the two differ
 * only by an area's edge. The areas are decoded with the client's own decoder
 * ({@code WorldMapArea.decode}) and the elements read as the client reads them
 * ({@code MapElementList.load}), with whether each is for members alone kept.
 */
public final class StaticElements {

    private static final String DETAILS = "details";
    private static final String GROUP_SUFFIX = "_staticelements";
    private static final int NO_GROUP = -1;

    /**
     * Where a packed tile of the world map keeps its level, its x and its z
     * ({@code Minimap.drawLevel}).
     */
    private static final int LEVEL_SHIFT = 28;
    private static final int X_SHIFT = 14;
    private static final int AXIS_MASK = 0x3FFF;
    private static final int LEVEL_MASK = 0x3;

    private static final int TILES_ACROSS = 64;

    /**
     * An element as an area places it: the area, its level, its world tile, its element and whether
     * it is for members alone.
     */
    private record Placed(WorldMapArea area, int level, int x, int z, int element, boolean members) {
    }

    private final List<WorldMapArea> areas = new ArrayList<>();
    private final List<Placed> placed = new ArrayList<>();

    public StaticElements(File cache) {
        var data = Cache.js5(cache, Js5Archive.WORLDMAPDATA);
        var details = data.getgroupid(DETAILS);
        for (var file : data.fileIds(details)) {
            var area = WorldMapArea.decode(data, details, file);
            areas.add(area);
            read(data, area);
        }
    }

    /**
     * The elements in a map square, its tiles counted from its south west corner: each its level,
     * tile, element and whether it is for members alone, in the order of the areas and their files.
     */
    public List<Map<String, Object>> of(int squareX, int squareZ) {
        var originX = squareX * TILES_ACROSS;
        var originZ = squareZ * TILES_ACROSS;
        var elements = new ArrayList<Map<String, Object>>();
        for (var element : placed) {
            var inSquare = element.x() >= originX && element.x() < originX + TILES_ACROSS
                && element.z() >= originZ && element.z() < originZ + TILES_ACROSS;
            if (inSquare && areaOf(element.x(), element.z()) == element.area()) {
                var fields = new LinkedHashMap<String, Object>();
                fields.put("level", element.level());
                fields.put("x", element.x() - originX);
                fields.put("z", element.z() - originZ);
                fields.put("element", element.element());
                fields.put("members", element.members());
                elements.add(fields);
            }
        }
        return elements;
    }

    /**
     * The area the client finds for a tile: the first it looks in whose chunks hold it
     * ({@code WorldMap.getMap}), or none.
     */
    private WorldMapArea areaOf(int x, int z) {
        for (var area : areas) {
            if (area.aBoolean354 && area.sourceContains(x, z)) {
                return area;
            }
        }
        return null;
    }

    private void read(js5 data, WorldMapArea area) {
        var group = data.getgroupid(area.file + GROUP_SUFFIX);
        if (group == NO_GROUP) {
            return;
        }
        for (var id : data.fileIds(group)) {
            var packet = new Packet(data.getfile(id, group));
            var coord = packet.g4();
            var element = packet.g2();
            var members = packet.g1() == 1;
            placed.add(new Placed(area, coord >>> LEVEL_SHIFT & LEVEL_MASK, coord >> X_SHIFT & AXIS_MASK,
                coord & AXIS_MASK, element, members));
        }
    }
}
