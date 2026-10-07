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
 * A window's frame is the layer's sprite components the player cannot use. A window drawn at one
 * size has each sprite read as one that keeps to the sides it is nearest, by the third of the box
 * it stands in, and the frame's rules ({@code SkinFrames}) then read them at that size, so its
 * dividers and the caps at their ends are found as a frame's are. Its title is a centred line of
 * text the layer holds near its top, and its close button the sprite nearest its top right that
 * the hover hooks of the layer swap. The frame is written as the frames are, measured from the
 * sides of the box, so it can take any size.
 *
 * A window whose pieces follow the size of its layer, as the graphics options window's do, is laid
 * out as the frames are, at two sizes with the client's own rules ({@code SkinFrames}), so that a
 * place may hold several pieces, and the rules that need the layer's real size read it where the
 * layer is laid out in the client's window; its title and close button are found where they stand
 * in the larger of the two.
 *
 * A frame that {@code SkinFrames} finds with a close button over it is a window as well, with its
 * title where the interface writes one, unless these rules found a window in a layer that holds its
 * pieces.
 */
final class SkinWindows {

    /**
     * How far in from a side the outermost corners of a window drawn at one size stand, as the
     * corners of the stone window stand 13 pixels in from its side edges.
     */
    private static final int NEAR_SIDE = 16;

    /**
     * How far apart the two corners of a pair may stand in a window drawn by hand at one size, each
     * measured from its own sides, as the top corners of 327 stand 13 and 3 pixels in from the sides
     * of a box whose right edge is 9 pixels thinner than its left.
     */
    private static final int HAND_PAIR_SLACK = 16;

    /**
     * The thirds of a box a piece's middle stands in, across or down.
     */
    private static final int MIDDLE = 1;
    private static final int LAST = 2;

    /**
     * The resize mode of a piece the size of its box less a fixed amount, and the reposition modes
     * of one centred in its box and of one that keeps its distance from the end of its side ({@code
     * InterfaceManager.resize}, {@code reposition}).
     */
    private static final int FOLLOWS = 1;
    private static final int CENTRED_IN_BOX = 1;
    private static final int FROM_END = 2;

    /**
     * The components of an interface stand under their interface's number in the high half of their
     * id, and their own number in the low half, which names the layer a component is in ({@code
     * Component.id}).
     */
    private static final int INTERFACE_SHIFT = 16;
    private static final int CHILD_MASK = 0xFFFF;

    /**
     * How far down from the top of the frame the title and the close button may stand.
     */
    private static final int TOP_BAND = 32;

    private static final int SWAP_SPRITE = 44;

    private static final int CENTRED = 1;

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
                var components = new LinkedHashMap<Integer, Component>();
                for (var file : Cache.split(data, index, group).entrySet()) {
                    var component = new Component();
                    component.decode(new Packet(file.getValue()));
                    components.put((group << INTERFACE_SHIFT) | file.getKey(), component);
                }
                var layout = new ComponentLayout(components);
                var layers = new LinkedHashMap<Integer, List<Component>>();
                for (var component : components.values()) {
                    layers.computeIfAbsent(component.layer, layer -> new ArrayList<>()).add(component);
                }
                for (var layer : layers.entrySet()) {
                    var box = layer.getKey() == -1 ? ComponentLayout.WINDOW : layout.boxOf((group << INTERFACE_SHIFT) | (layer.getKey() & CHILD_MASK));
                    var real = box == null ? null : new SkinFrames.Size(box.width(), box.height());
                    found.addIfWindow(layer.getValue(), real, group, layer.getKey());
                }
            }
        }
        return found;
    }

    private void addIfWindow(List<Component> layer, SkinFrames.Size real, int interfaceId, int layerId) throws Exception {
        var graphics = new ArrayList<Component>();
        for (var component : layer) {
            if (component.type == Component.TYPE_GRAPHIC && SkinFrames.isPiece(component)) {
                graphics.add(component);
            }
        }
        if (graphics.stream().allMatch(SkinWindows::isFixed)) {
            addIfFixedWindow(layer, graphics, interfaceId, layerId);
        } else {
            addIfResizingWindow(layer, real, interfaceId, layerId);
        }
    }

    /**
     * Adds a layer whose pieces follow its size as a window, where they make a frame and the layer
     * holds a title and a close button, each measured where it stands in the larger box. The frame's
     * rules that need the layer's real size read it where the layer is laid out in the client's
     * window.
     */
    private void addIfResizingWindow(List<Component> layer, SkinFrames.Size real, int interfaceId, int layerId) throws Exception {
        var pieces = new ArrayList<SkinFrames.Piece>();
        for (var component : layer) {
            if (SkinFrames.isPiece(component)) {
                pieces.add(SkinFrames.pieceOf(component));
            }
        }
        var parts = SkinFrames.partsOf(pieces, real, edges);
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
        var real = new SkinFrames.Size(box.width(), box.height());
        var following = pieces.stream().map(piece -> followingIn(piece, box)).toList();
        var parts = SkinFrames.partsOf(following, real, real, HAND_PAIR_SLACK, edges);
        if (parts == null || !isAtItsSides(parts)) {
            return;
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

    private static int third(int at, int length) {
        return at * 3 < length ? 0 : at * 3 > length * 2 ? 2 : 1;
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
        Map<String, Object> nearest = null;
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
                if (nearest == null || fromTopRight(close) < fromTopRight(nearest)) {
                    nearest = close;
                }
            }
        }
        return nearest;
    }

    /**
     * Whether the outermost corners of a window drawn at one size stand near each side of the box its
     * sprites cover, as the frame of a window is the outside of what it draws; a layer whose sprites
     * reach further out than its frame on a side, as a picture below the frame of 267 does, holds
     * more than a window.
     */
    private static boolean isAtItsSides(List<Map<String, Object>> parts) {
        var corners = parts.stream().filter(SkinFrames::isCorner).toList();
        return List.of("Left", "Right").stream().allMatch(side -> SkinFrames.marginOf(side, "x", corners) <= NEAR_SIDE)
            && List.of("top", "bottom").stream().allMatch(side -> SkinFrames.marginOf(side, "y", corners) <= NEAR_SIDE);
    }

    /**
     * How far a button stands from the top right corner of the box, along the top and down the right
     * side together.
     */
    private static int fromTopRight(Map<String, Object> close) {
        return (Integer) close.get("right") + (Integer) close.get("top");
    }

    /**
     * A sprite of a window laid out at one size, as a piece that keeps to the sides of the box it is
     * nearest and grows with the box where its middle stands in the middle third: in the left third
     * across it keeps its distance from the left, in the right third from the right, and in the
     * middle it keeps its distance from both and grows; down likewise. So the frame's rules read a
     * window drawn at one size as they read one that follows its layer. The corners of a frame and
     * the caps of its dividers keep to its left and right sides, so a piece in the left or right
     * third that stands further in from that side, as an icon beside the title of 327 or a joint of
     * the rules of a table does, is laid out as one centred across the box: it is no part of the
     * frame, but it still covers what it covers at the size the window is drawn at.
     */
    private static SkinFrames.Piece followingIn(Component component, Box box) {
        var drawn = SkinFrames.pieceOf(component);
        var x = component.originalX - box.x();
        var y = component.originalY - box.y();
        var width = component.originalWidth;
        var height = component.originalHeight;
        var across = third(x + width / 2, box.width());
        var down = third(y + height / 2, box.height());
        var fromSide = across == LAST ? box.width() - x - width : x;
        var acrossLayout = across != MIDDLE && fromSide > NEAR_SIDE ? centredAcross(x, width, box.width()) : keptAcross(x, width, box.width(), across);
        var layout = new SkinFrames.Layout(
            acrossLayout.width(),
            down == MIDDLE ? box.height() - height : height,
            acrossLayout.resizeX(),
            down == MIDDLE ? FOLLOWS : 0,
            acrossLayout.x(),
            down == LAST ? box.height() - y - height : y,
            acrossLayout.reposX(),
            down == LAST ? FROM_END : 0
        );
        return new SkinFrames.Piece(drawn.sprite(), drawn.colour(), drawn.transparency(), drawn.mirrored(), drawn.flipped(), drawn.turned(), drawn.tiled(), layout, List.of());
    }

    /**
     * How a piece is laid out across its box: its width, its resize mode, its place and its
     * reposition mode.
     */
    private record Across(int width, int resizeX, int x, int reposX) {
    }

    private static Across centredAcross(int x, int width, int boxWidth) {
        return new Across(width, 0, x - (boxWidth - width) / 2, CENTRED_IN_BOX);
    }

    private static Across keptAcross(int x, int width, int boxWidth, int across) {
        return switch (across) {
            case MIDDLE -> new Across(boxWidth - width, FOLLOWS, x, 0);
            case LAST -> new Across(width, 0, boxWidth - x - width, FROM_END);
            default -> new Across(width, 0, x, 0);
        };
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
