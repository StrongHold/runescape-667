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
 * inside the box it stands in, and take as far along the box as it is, out of the box's width less
 * the knob's, for the value. The track is the sprite components beside that box, of the same
 * layer, that lie within its height and cross it.
 */
final class WidgetSliders {

    private static final List<Integer> SLIDER_SCRIPTS = List.of(1764, 1215);

    private static final String DRAG_HOOK = "onDragComplete";

    /**
     * The components of an interface stand under their interface's number in the high half of
     * their id, and their own number in the low half ({@code Component.id}).
     */
    private static final int CHILD_MASK = 0xFFFF;

    private final Map<String, Map<String, Object>> sliders = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> interfaces = new LinkedHashMap<>();

    static WidgetSliders read(File cache) throws Exception {
        var found = new WidgetSliders();
        var index = Cache.index(cache, Js5Archive.INTERFACES);
        for (var group : Cache.groupsOf(index)) {
            var data = Cache.group(cache, Js5Archive.INTERFACES, group);
            if (data != null) {
                var components = new LinkedHashMap<Integer, Component>();
                for (var file : Cache.split(data, index, group).entrySet()) {
                    var component = new Component();
                    component.decode(new Packet(file.getValue()));
                    components.put(file.getKey(), component);
                }
                for (var knob : components.values()) {
                    found.addIfSlider(knob, components, group);
                }
            }
        }
        return found;
    }

    private void addIfSlider(Component knob, Map<Integer, Component> components, int interfaceId) throws Exception {
        var hook = (Object[]) Component.class.getField(DRAG_HOOK).get(knob);
        var drags = hook != null && hook.length > 0 && hook[0] instanceof Integer script && SLIDER_SCRIPTS.contains(script);
        var box = knob.layer == -1 ? null : components.get(knob.layer & CHILD_MASK);
        if (!drags || knob.graphic < 0 || box == null || !isFixed(box)) {
            return;
        }

        var track = new ArrayList<Component>();
        for (var component : components.values()) {
            var beside = component.layer == box.layer && component != box && component.type == Component.TYPE_GRAPHIC;
            if (beside && component.graphic >= 0 && isFixed(component) && liesOn(component, box)) {
                track.add(component);
            }
        }
        if (track.isEmpty()) {
            return;
        }
        track.sort(Comparator.comparingInt(component -> component.originalX));

        var pieces = new ArrayList<Map<String, Object>>();
        for (var piece : track) {
            var written = new LinkedHashMap<String, Object>();
            written.put("sprite", piece.graphic);
            if (piece.tiling) {
                written.put("tiled", true);
            }
            written.put("x", piece.originalX - box.originalX);
            written.put("y", piece.originalY - box.originalY);
            written.put("width", piece.originalWidth);
            written.put("height", piece.originalHeight);
            pieces.add(written);
        }

        var slider = new LinkedHashMap<String, Object>();
        slider.put("knob", knob.graphic);
        slider.put("knobWidth", knob.originalWidth);
        slider.put("knobHeight", knob.originalHeight);
        slider.put("width", box.originalWidth);
        slider.put("height", box.originalHeight);
        slider.put("track", pieces);
        var key = Json.write(slider);
        sliders.putIfAbsent(key, slider);
        interfaces.computeIfAbsent(key, k -> new TreeSet<>()).add(interfaceId);
    }

    /**
     * Whether a component stands where its numbers say and is the size they say, as most do.
     */
    private static boolean isFixed(Component component) {
        return component.reposModeX == 0 && component.reposModeY == 0 && component.resizeModeX == 0 && component.resizeModeY == 0;
    }

    /**
     * Whether a component lies within the box's height and crosses it.
     */
    private static boolean liesOn(Component component, Component box) {
        var within = component.originalY >= box.originalY
            && component.originalY + component.originalHeight <= box.originalY + box.originalHeight;
        var crosses = component.originalX < box.originalX + box.originalWidth
            && component.originalX + component.originalWidth > box.originalX;
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
