import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The windows of the interfaces: a frame of sprites with a title written across its top and a
 * button that closes it, laid out at a fixed size in a layer.
 *
 * A window's frame is the layer's sprite components the player cannot use, each placed by where it
 * stands in the box they cover: in the left, middle or right third across, and the top, middle or
 * bottom third down, within a few pixels of the sides of its place, or the largest in the middle
 * for the fill. Its title is a centred line of text the layer holds near its top, and its close
 * button a sprite the hover hooks of the layer swap near its top right. The frame is written as
 * the frames are, measured from the sides of the box, so it can take any size.
 */
final class WidgetWindows {

    /**
     * How far from the side of its place a piece of a frame may stand, as the corners of the
     * stone window stand 13 pixels in from its side edges.
     */
    private static final int NEAR_SIDE = 16;

    /**
     * How far down from the top of the frame the title and the close button may stand.
     */
    private static final int TOP_BAND = 32;

    private static final int SWAP_SPRITE = 44;

    private static final int CENTRED = 1;

    private static final String[][] PLACES = {
        {"topLeft", "left", "bottomLeft"},
        {"top", "centre", "bottom"},
        {"topRight", "right", "bottomRight"}
    };

    private static final List<String> EDGES = List.of("topLeft", "top", "topRight", "left", "right", "bottomLeft", "bottom", "bottomRight");

    private final Map<String, Map<String, Object>> windows = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> interfaces = new LinkedHashMap<>();

    private record Box(int x, int y, int width, int height) {

        int right() {
            return x + width;
        }

        int bottom() {
            return y + height;
        }
    }

    static WidgetWindows read(File cache) throws Exception {
        var found = new WidgetWindows();
        var index = Cache.index(cache, Js5Archive.INTERFACES);
        for (var group : Cache.groupsOf(index)) {
            var data = Cache.group(cache, Js5Archive.INTERFACES, group);
            if (data != null) {
                var layers = new LinkedHashMap<Integer, List<Component>>();
                for (var file : Cache.split(data, index, group).values()) {
                    var component = new Component();
                    component.decode(new Packet(file));
                    layers.computeIfAbsent(component.layer, layer -> new ArrayList<>()).add(component);
                }
                for (var layer : layers.values()) {
                    found.addIfWindow(layer, group);
                }
            }
        }
        return found;
    }

    private void addIfWindow(List<Component> layer, int interfaceId) throws Exception {
        var pieces = new ArrayList<Component>();
        for (var component : layer) {
            if (component.type == Component.TYPE_GRAPHIC && component.graphic >= 0 && !isUsable(component) && isFixed(component)) {
                pieces.add(component);
            }
        }
        if (pieces.size() < EDGES.size()) {
            return;
        }

        var box = boxAround(pieces);
        var placed = new LinkedHashMap<String, Component>();
        Component fill = null;
        for (var piece : pieces) {
            var place = placeOf(piece, box);
            if (place != null && placed.containsKey(place)) {
                return;
            } else if (place != null) {
                placed.put(place, piece);
            } else if (isInside(piece, box) && (fill == null || areaOf(piece) > areaOf(fill))) {
                fill = piece;
            }
        }
        var title = titleOf(layer, box);
        var close = closeOf(layer, box);
        if (!placed.keySet().containsAll(EDGES) || title == null || close == null) {
            return;
        }

        var parts = new ArrayList<Map<String, Object>>();
        for (var piece : pieces) {
            if (piece == fill) {
                parts.add(partOf(piece, "centre", box));
            }
            for (var place : placed.entrySet()) {
                if (place.getValue() == piece) {
                    parts.add(partOf(piece, place.getKey(), box));
                }
            }
        }

        var window = new LinkedHashMap<String, Object>();
        window.put("parts", parts);
        window.put("title", title);
        window.put("close", close);
        var key = Json.write(window);
        windows.putIfAbsent(key, window);
        interfaces.computeIfAbsent(key, k -> new TreeSet<>()).add(interfaceId);
    }

    /**
     * The place of a piece of the frame, by the thirds of the box its middle stands in, where it
     * stands within a few pixels of the sides of that place; null for one in the middle or one that
     * stands in from its sides.
     */
    private static String placeOf(Component piece, Box box) {
        var across = third(piece.originalX + piece.originalWidth / 2 - box.x(), box.width());
        var down = third(piece.originalY + piece.originalHeight / 2 - box.y(), box.height());
        var nearAcross = across == 1 || nearSide(across, piece.originalX - box.x(), box.right() - piece.originalX - piece.originalWidth);
        var nearDown = down == 1 || nearSide(down, piece.originalY - box.y(), box.bottom() - piece.originalY - piece.originalHeight);
        var middle = across == 1 && down == 1;
        return !middle && nearAcross && nearDown ? PLACES[across][down] : null;
    }

    private static int third(int at, int length) {
        return at * 3 < length ? 0 : at * 3 > length * 2 ? 2 : 1;
    }

    private static boolean nearSide(int third, int fromStart, int fromEnd) {
        return third == 0 ? fromStart <= NEAR_SIDE : fromEnd <= NEAR_SIDE;
    }

    private static boolean isInside(Component piece, Box box) {
        var x = piece.originalX - box.x();
        var y = piece.originalY - box.y();
        return x > 0 && y > 0 && x + piece.originalWidth < box.width() && y + piece.originalHeight < box.height();
    }

    private static int areaOf(Component piece) {
        return piece.originalWidth * piece.originalHeight;
    }

    private static Box boxAround(List<Component> pieces) {
        var left = pieces.stream().mapToInt(piece -> piece.originalX).min().orElse(0);
        var top = pieces.stream().mapToInt(piece -> piece.originalY).min().orElse(0);
        var right = pieces.stream().mapToInt(piece -> piece.originalX + piece.originalWidth).max().orElse(0);
        var bottom = pieces.stream().mapToInt(piece -> piece.originalY + piece.originalHeight).max().orElse(0);
        return new Box(left, top, right - left, bottom - top);
    }

    /**
     * The title of a window: a centred line of text near the top of the frame, as its colour, font,
     * whether it has a shadow, and its place from the sides and the top of the box.
     */
    private static Map<String, Object> titleOf(List<Component> layer, Box box) {
        for (var component : layer) {
            var nearTop = component.originalY >= box.y() && component.originalY < box.y() + TOP_BAND;
            if (component.type == Component.TYPE_TEXT && isFixed(component) && component.textAlignX == CENTRED && nearTop) {
                var title = new LinkedHashMap<String, Object>();
                title.put("colour", component.colour);
                title.put("font", component.fontGraphic);
                if (component.textShadow) {
                    title.put("shadow", true);
                }
                title.put("left", component.originalX - box.x());
                title.put("right", box.right() - component.originalX - component.originalWidth);
                title.put("top", component.originalY - box.y());
                title.put("height", component.originalHeight);
                return title;
            }
        }
        return null;
    }

    /**
     * The close button of a window: a sprite near the top right of the frame that hooks of script 44
     * swap under the pointer, as its sprite, the sprite under the pointer, and its place from the
     * right and the top of the box.
     */
    private static Map<String, Object> closeOf(List<Component> layer, Box box) throws Exception {
        for (var component : layer) {
            var over = (Object[]) Component.class.getField("onMouseOver").get(component);
            var swaps = over != null && over.length > 2 && Integer.valueOf(SWAP_SPRITE).equals(over[0]) && over[2] instanceof Integer;
            var nearTop = component.originalY >= box.y() && component.originalY < box.y() + TOP_BAND;
            var nearRight = component.originalX > box.x() + box.width() / 2;
            if (component.type == Component.TYPE_GRAPHIC && component.graphic >= 0 && swaps && nearTop && nearRight && isFixed(component)) {
                var close = new LinkedHashMap<String, Object>();
                close.put("sprite", component.graphic);
                close.put("hover", over[2]);
                close.put("right", box.right() - component.originalX - component.originalWidth);
                close.put("top", component.originalY - box.y());
                close.put("width", component.originalWidth);
                close.put("height", component.originalHeight);
                return close;
            }
        }
        return null;
    }

    /**
     * A part of the frame as the frames write theirs, measured from the sides of the box.
     */
    private static Map<String, Object> partOf(Component piece, String place, Box box) {
        var part = new LinkedHashMap<String, Object>();
        part.put("place", place);
        part.put("sprite", piece.graphic);
        if (piece.verticalFlip) {
            part.put("mirrored", true);
        }
        if (piece.horizontalFlip) {
            part.put("flipped", true);
        }
        if (piece.tiling) {
            part.put("tiled", true);
        }
        var left = piece.originalX - box.x();
        var top = piece.originalY - box.y();
        var right = box.right() - piece.originalX - piece.originalWidth;
        var bottom = box.bottom() - piece.originalY - piece.originalHeight;
        switch (place) {
            case "top", "bottom" -> {
                part.put("start", left);
                part.put("end", right);
                part.put("inset", place.equals("top") ? top : bottom);
                part.put("thickness", piece.originalHeight);
            }
            case "left", "right" -> {
                part.put("start", top);
                part.put("end", bottom);
                part.put("inset", place.equals("left") ? left : right);
                part.put("thickness", piece.originalWidth);
            }
            case "centre" -> {
                part.put("left", left);
                part.put("top", top);
                part.put("right", right);
                part.put("bottom", bottom);
            }
            default -> {
                part.put("x", place.endsWith("Left") ? left : right);
                part.put("y", place.startsWith("top") ? top : bottom);
                part.put("width", piece.originalWidth);
                part.put("height", piece.originalHeight);
            }
        }
        return part;
    }

    private static boolean isUsable(Component component) throws IllegalAccessException {
        for (var field : Component.class.getFields()) {
            if (field.getType() == Object[].class && field.getName().startsWith("on") && field.get(component) != null) {
                return true;
            }
        }
        return component.ops != null && java.util.Arrays.stream(component.ops).anyMatch(op -> op != null);
    }

    private static boolean isFixed(Component component) {
        return component.reposModeX == 0 && component.reposModeY == 0 && component.resizeModeX == 0 && component.resizeModeY == 0;
    }

    int size() {
        return windows.size();
    }

    List<Map<String, Object>> written() {
        var written = new ArrayList<Map<String, Object>>();
        for (var window : windows.entrySet()) {
            var entry = new LinkedHashMap<String, Object>(window.getValue());
            entry.put("interfaces", new ArrayList<>(interfaces.get(window.getKey())));
            written.add(entry);
        }
        written.sort((a, b) -> ((List<?>) b.get("interfaces")).size() - ((List<?>) a.get("interfaces")).size());
        return written;
    }
}
