import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The tiles of a radio group, such as the combat styles (interface 884): a plate the player picks
 * one of, with an icon over a label, the plate red where it is the one picked and grey where it is
 * not. Script 1134 sets the plate it is given to one sprite where a variable holds the value it is
 * given and to another where it does not, and each plate runs it as it loads and as the variable
 * changes. The tile is the layer the plate stands in: the plate fills it, and the one other sprite
 * and the one text in it are the icon and the label, which scripts fill in as the game runs.
 *
 * <p>The icon is written without its place across: 884 places its four icons 18, 19 and 20 pixels
 * in by hand, each near the middle of its 70 pixel tile, so the tile centres it.
 */
final class SkinTiles {

    private static final int SELECT_TILE = 1134;

    private static final List<String> HOOKS = List.of("onLoad", "onVarTransmit");

    private static final int INTERFACE_SHIFT = 16;

    private final Map<String, Map<String, Object>> tiles = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> interfaces = new LinkedHashMap<>();

    /**
     * The tile of each layout the cache holds, once, with the interfaces that show it. The sprites
     * are the two the script sets, the plain one and the one picked.
     */
    static List<Map<String, Object>> read(File cache, Map<Integer, ClientScript> scripts) throws Exception {
        var graphics = SkinReaders.graphicsOf(scripts.get(SELECT_TILE));
        var shaped = graphics.size() == 2 && graphics.get(0)[0] == 0 && graphics.get(1)[0] == 0
            && !graphics.get(0)[1].equals(graphics.get(1)[1]);
        if (!shaped) {
            SkinReaders.stop(SELECT_TILE, "set the component it is given to the picked sprite and else to the plain one");
        }
        var selected = graphics.get(0)[1];
        var plain = graphics.get(1)[1];

        var found = new SkinTiles();
        var index = Cache.index(cache, Js5Archive.INTERFACES);
        for (var group : Cache.groupsOf(index)) {
            var data = Cache.group(cache, Js5Archive.INTERFACES, group);
            if (data != null) {
                var components = new LinkedHashMap<Integer, Component>();
                for (var file : Cache.split(data, index, group).entrySet()) {
                    var component = new Component();
                    component.decode(new Packet(file.getValue()));
                    components.put((group << INTERFACE_SHIFT) | file.getKey(), component);
                }
                var layout = new ComponentLayout(components);
                for (var plate : components.entrySet()) {
                    if (selects(plate.getValue())) {
                        found.add(plate.getKey(), plain, selected, components, layout, group);
                    }
                }
            }
        }
        return found.written();
    }

    private static boolean selects(Component component) throws Exception {
        for (var name : HOOKS) {
            var hook = (Object[]) Component.class.getField(name).get(component);
            if (hook != null && hook.length > 0 && hook[0] instanceof Integer script && script == SELECT_TILE) {
                return true;
            }
        }
        return false;
    }

    private void add(int plateId, int plain, int selected, Map<Integer, Component> components, ComponentLayout layout,
                     int interfaceId) {
        var tileId = layout.parentOf(plateId);
        var tile = tileId == -1 ? null : layout.boxOf(tileId);
        Integer iconId = null;
        Integer labelId = null;
        var icons = 0;
        var labels = 0;
        for (var entry : components.entrySet()) {
            var component = entry.getValue();
            if (entry.getKey() != plateId && layout.parentOf(entry.getKey()) == tileId) {
                if (component.type == Component.TYPE_GRAPHIC) {
                    iconId = entry.getKey();
                    icons++;
                } else if (component.type == Component.TYPE_TEXT) {
                    labelId = entry.getKey();
                    labels++;
                }
            }
        }
        if (tile == null || icons != 1 || labels != 1) {
            return;
        }
        var icon = layout.boxOf(iconId);
        var label = layout.boxOf(labelId);
        var text = components.get(labelId);
        if (icon == null || label == null) {
            return;
        }

        var iconBox = new LinkedHashMap<String, Object>();
        iconBox.put("y", icon.y() - tile.y());
        iconBox.put("width", icon.width());
        iconBox.put("height", icon.height());

        var labelBox = new LinkedHashMap<String, Object>();
        labelBox.put("x", label.x() - tile.x());
        labelBox.put("y", label.y() - tile.y());
        labelBox.put("width", label.width());
        labelBox.put("height", label.height());
        labelBox.put("font", text.fontGraphic);
        labelBox.put("colour", text.colour);
        labelBox.put("shadow", text.textShadow);

        var written = new LinkedHashMap<String, Object>();
        written.put("sprite", plain);
        written.put("selected", selected);
        written.put("width", tile.width());
        written.put("height", tile.height());
        written.put("icon", iconBox);
        written.put("label", labelBox);
        var key = Json.write(written);
        tiles.putIfAbsent(key, written);
        interfaces.computeIfAbsent(key, k -> new TreeSet<>()).add(interfaceId);
    }

    private List<Map<String, Object>> written() {
        var written = new ArrayList<Map<String, Object>>();
        for (var entry : tiles.entrySet()) {
            var tile = new LinkedHashMap<String, Object>(entry.getValue());
            tile.put("scripts", List.of(SELECT_TILE));
            tile.put("interfaces", List.copyOf(interfaces.get(entry.getKey())));
            written.add(tile);
        }
        return written;
    }
}
