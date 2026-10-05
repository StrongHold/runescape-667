import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The sliders of the interfaces: a knob the player drags along a box, over the sprites of a track.
 * The client's scripts 1764 and 1215 move a knob whose drag hook runs them: they keep the knob
 * inside the layer it stands in, its box, and take as far along the box as it is, out of the box's
 * width less the knob's, for the value. The knob's sprite is its own, or where the knob is a layer,
 * the one sprite in it. The track is every other sprite of the interface whose place, laid out
 * through its layers, lies within the box's height and crosses it.
 */
final class SkinSliders {

    private static final List<Integer> SLIDER_SCRIPTS = List.of(1764, 1215);

    private static final String DRAG_HOOK = "onDragComplete";

    private static final int INTERFACE_SHIFT = 16;
    private static final int CHILD_MASK = 0xFFFF;

    private final Map<String, Map<String, Object>> sliders = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> interfaces = new LinkedHashMap<>();

    static SkinSliders read(File cache) throws Exception {
        var found = new SkinSliders();
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
                for (var knob : components.entrySet()) {
                    found.addIfSlider(knob.getKey(), knob.getValue(), components, layout, group);
                }
            }
        }
        return found;
    }

    private void addIfSlider(int knobId, Component knob, Map<Integer, Component> components, ComponentLayout layout, int interfaceId) throws Exception {
        var hook = (Object[]) Component.class.getField(DRAG_HOOK).get(knob);
        var drags = hook != null && hook.length > 0 && hook[0] instanceof Integer script && SLIDER_SCRIPTS.contains(script);
        if (!drags) {
            return;
        }

        var knobSpriteId = knob.graphic >= 0 ? Integer.valueOf(knobId) : onlySpriteIn(knobId, components, layout);
        var boxId = layout.parentOf(knobId);
        var box = boxId == -1 ? null : layout.boxOf(boxId);
        var knobBox = layout.boxOf(knobId);
        if (knobSpriteId == null || box == null || knobBox == null) {
            return;
        }

        var track = new ArrayList<Map<String, Object>>();
        var pieces = new ArrayList<Integer>(components.keySet());
        pieces.sort(Comparator.comparingInt(id -> {
            var placed = layout.boxOf(id);
            return placed == null ? 0 : placed.x();
        }));
        for (var id : pieces) {
            var component = components.get(id);
            var placed = layout.boxOf(id);
            var piece = component.type == Component.TYPE_GRAPHIC && component.graphic >= 0 && id != knobSpriteId;
            if (piece && placed != null && liesOn(placed, box) && !isInside(id, knobId, layout)) {
                var written = new LinkedHashMap<String, Object>();
                written.put("sprite", component.graphic);
                if (component.tiling) {
                    written.put("tiled", true);
                }
                written.put("x", placed.x() - box.x());
                written.put("y", placed.y() - box.y());
                written.put("width", placed.width());
                written.put("height", placed.height());
                track.add(written);
            }
        }
        if (track.isEmpty()) {
            return;
        }

        var slider = new LinkedHashMap<String, Object>();
        slider.put("knob", components.get(knobSpriteId).graphic);
        slider.put("knobWidth", knobBox.width());
        slider.put("knobHeight", knobBox.height());
        slider.put("width", box.width());
        slider.put("height", box.height());
        slider.put("track", track);
        var key = Json.write(slider);
        sliders.putIfAbsent(key, slider);
        interfaces.computeIfAbsent(key, k -> new TreeSet<>()).add(interfaceId);
    }

    /**
     * The one sprite component in a layer, or null where it holds none or more than one.
     */
    private static Integer onlySpriteIn(int layerId, Map<Integer, Component> components, ComponentLayout layout) {
        Integer found = null;
        var count = 0;
        for (var entry : components.entrySet()) {
            var component = entry.getValue();
            if (layout.parentOf(entry.getKey()) == layerId && component.type == Component.TYPE_GRAPHIC && component.graphic >= 0) {
                found = entry.getKey();
                count++;
            }
        }
        return count == 1 ? found : null;
    }

    /**
     * Whether a component is the layer or within it, at any depth.
     */
    private static boolean isInside(int id, int layerId, ComponentLayout layout) {
        var at = id;
        while (at != -1) {
            if (at == layerId) {
                return true;
            }
            at = layout.parentOf(at);
        }
        return false;
    }

    /**
     * Whether a place lies within the box's height and crosses it.
     */
    private static boolean liesOn(ComponentLayout.Box placed, ComponentLayout.Box box) {
        var within = placed.y() >= box.y() && placed.y() + placed.height() <= box.y() + box.height();
        var crosses = placed.x() < box.x() + box.width() && placed.x() + placed.width() > box.x();
        return within && crosses;
    }

    int size() {
        return sliders.size();
    }

    List<Map<String, Object>> written() {
        var written = new ArrayList<Map<String, Object>>();
        for (var slider : sliders.entrySet()) {
            var entry = new LinkedHashMap<String, Object>(slider.getValue());
            entry.put("interfaces", new ArrayList<>(interfaces.get(slider.getKey())));
            written.add(entry);
        }
        return written;
    }
}
