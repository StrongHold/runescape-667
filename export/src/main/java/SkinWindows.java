import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 *
 * A window whose pieces follow the size of its layer, as the graphics options window's do, is laid
 * out as the frames are, at two sizes with the client's own rules ({@code SkinFrames}), so that a
 * place may hold several pieces; its title and close button are found where they stand in the
 * larger of the two.
 *
 * A frame that {@code SkinFrames} finds with a close button over it is a window as well, with its
 * title where the interface writes one, unless these rules found a window in a layer that holds its
 * pieces.
 */
final class SkinWindows {

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
    private final Map<Integer, Set<Integer>> layersWithWindows = new HashMap<>();
    private final SkinEdges edges;

    private SkinWindows(SkinEdges edges) {
        this.edges = edges;
    }

    private record Box(int x, int y, int width, int height) {

        int right() {
            return x + width;
        }

        int bottom() {
            return y + height;
        }
    }

    static SkinWindows read(File cache, SkinEdges edges) throws Exception {
        var found = new SkinWindows(edges);
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
                for (var layer : layers.entrySet()) {
                    found.addIfWindow(layer.getValue(), group, layer.getKey());
                }
            }
        }
        return found;
    }

    private void addIfWindow(List<Component> layer, int interfaceId, int layerId) throws Exception {
        var graphics = new ArrayList<Component>();
        for (var component : layer) {
            if (component.type == Component.TYPE_GRAPHIC && SkinFrames.isPiece(component)) {
                graphics.add(component);
            }
        }
        if (graphics.stream().allMatch(SkinWindows::isFixed)) {
            addIfFixedWindow(layer, graphics, interfaceId, layerId);
        } else {
            addIfResizingWindow(layer, interfaceId, layerId);
        }
    }

    /**
     * Adds a layer whose pieces follow its size as a window, where they make a frame and the layer
     * holds a title and a close button, each measured where it stands in the larger box.
     */
    private void addIfResizingWindow(List<Component> layer, int interfaceId, int layerId) throws Exception {
        var pieces = new ArrayList<SkinFrames.Piece>();
        for (var component : layer) {
            if (SkinFrames.isPiece(component)) {
                pieces.add(SkinFrames.pieceOf(component));
            }
        }
        var parts = SkinFrames.partsOf(pieces, edges);
        if (parts == null) {
            return;
        }

        var box = new Box(0, 0, SkinFrames.WIDER, SkinFrames.HIGHER);
        var laidOut = new LinkedHashMap<Component, Box>();
        for (var component : layer) {
            var rect = SkinFrames.rect(SkinFrames.pieceOf(component).layout(), box.width(), box.height());
            laidOut.put(component, new Box(rect.x(), rect.y(), rect.width(), rect.height()));
        }
        add(parts, titleOf(laidOut, box), closeOf(laidOut, box), interfaceId, layerId);
    }

    private void addIfFixedWindow(List<Component> layer, List<Component> graphics, int interfaceId, int layerId) throws Exception {
        var pieces = graphics;
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
        if (!placed.keySet().containsAll(EDGES)) {
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

        var fixed = new LinkedHashMap<Component, Box>();
        for (var component : layer) {
            if (isFixed(component)) {
                fixed.put(component, new Box(component.originalX, component.originalY, component.originalWidth, component.originalHeight));
            }
        }
        add(parts, titleOf(fixed, box), closeOf(fixed, box), interfaceId, layerId);
    }

    /**
     * Adds the window of a frame that has a close button over it, and where it has one a title, in
     * whatever layers hold them, as the ornate windows hold their title in a layer of its own and
     * their close button in another ({@code SkinTitles}). A window whose interface writes no title
     * over its frame, as 1111 writes none in its title bar, has none.
     */
    void addFramed(List<Map<String, Object>> parts, Map<String, Object> title, Map<String, Object> close, int interfaceId) {
        var window = new LinkedHashMap<String, Object>();
        window.put("parts", parts);
        if (title != null) {
            window.put("title", title);
        }
        window.put("close", close);
        add(window, interfaceId);
    }

    /**
     * Whether a window was found in one of these layers of an interface, by the layer's number in
     * the interface, so that the frame of the same window is not found again from the box it is in.
     */
    boolean hasWindowIn(int interfaceId, Set<Integer> layers) {
        var found = layersWithWindows.getOrDefault(interfaceId, Set.of());
        return layers.stream().anyMatch(found::contains);
    }

    /**
     * Adds a window of these parts, title and close button, where it has a title and a close button.
     */
    private void add(List<Map<String, Object>> parts, Map<String, Object> title, Map<String, Object> close, int interfaceId, int layerId) {
        if (title == null || close == null) {
            return;
        }

        layersWithWindows.computeIfAbsent(interfaceId, id -> new HashSet<>()).add(layerId);

        var window = new LinkedHashMap<String, Object>();
        window.put("parts", parts);
        window.put("title", title);
        window.put("close", close);
        add(window, interfaceId);
    }

    private void add(Map<String, Object> window, int interfaceId) {
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
    private static Map<String, Object> titleOf(Map<Component, Box> laidOut, Box box) {
        for (var entry : laidOut.entrySet()) {
            var component = entry.getKey();
            var at = entry.getValue();
            var nearTop = at.y() >= box.y() && at.y() < box.y() + TOP_BAND;
            if (component.type == Component.TYPE_TEXT && component.textAlignX == CENTRED && nearTop) {
                var title = new LinkedHashMap<String, Object>();
                title.put("colour", component.colour);
                title.put("font", component.fontGraphic);
                if (component.textShadow) {
                    title.put("shadow", true);
                }
                title.put("left", at.x() - box.x());
                title.put("right", box.right() - at.right());
                title.put("top", at.y() - box.y());
                title.put("height", at.height());
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
    private static Map<String, Object> closeOf(Map<Component, Box> laidOut, Box box) throws Exception {
        for (var entry : laidOut.entrySet()) {
            var component = entry.getKey();
            var at = entry.getValue();
            var over = (Object[]) Component.class.getField("onMouseOver").get(component);
            var swaps = over != null && over.length > 2 && Integer.valueOf(SWAP_SPRITE).equals(over[0]) && over[2] instanceof Integer;
            var nearTop = at.y() >= box.y() && at.y() < box.y() + TOP_BAND;
            var nearRight = at.x() > box.x() + box.width() / 2;
            if (component.type == Component.TYPE_GRAPHIC && component.graphic >= 0 && swaps && nearTop && nearRight) {
                var close = new LinkedHashMap<String, Object>();
                close.put("sprite", component.graphic);
                close.put("hover", over[2]);
                close.put("right", box.right() - at.right());
                close.put("top", at.y() - box.y());
                close.put("width", at.width());
                close.put("height", at.height());
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
        var drawn = SkinFrames.pieceOf(piece);
        if (drawn.mirrored()) {
            part.put("mirrored", true);
        }
        if (drawn.flipped()) {
            part.put("flipped", true);
        }
        if (drawn.turned()) {
            part.put("turned", true);
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
