import com.jagex.core.constants.ClientScriptOpCode;
import com.jagex.core.io.Packet;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The frames of the interfaces: boxes drawn as corners, edges along the sides and fills, each a
 * sprite component that stays at its corner or stretches with the box. A box's pieces are found by
 * laying its components out at two sizes with the client's own rules ({@code
 * InterfaceManager.resize}, {@code reposition}): a piece that keeps its size is a corner, one that
 * grows along one side is an edge, and one that grows both ways is a fill. A frame may have several
 * pieces in one place, as an ornate frame does, and pieces that cover each other are kept in the
 * order the client draws them. A filled rectangle that grows both ways is a fill of one colour, and
 * one that grows along a side is an edge of one colour. A component the player can use, one with a
 * hook or an option, is not part of a frame, nor is one hidden until a script shows it, as a
 * highlight under the pointer is, and a box without a corner at each corner and an edge along each
 * side is not a frame. A box is a layer with the layers in it that follow its size, as the client
 * draws them over it, so a background or an inner frame in a layer of its own is part of the frame.
 * A sprite is drawn mirrored and flipped as its component says, then turned as its angle says
 * ({@code Component.angle2d}), as the right corners of the stone panels are a left corner turned.
 *
 * A box holds more than its frame: icons, rows of slots, banners and pictures stay at a corner as
 * a frame's corners do, and dividers and rules stretch along a side as its edges do. A frame's
 * corners are drawn in pairs, the two corners of a side alike, so a piece at a corner is taken for
 * a corner only where the corner across from it holds a piece of its size near the same place, and
 * not where it is one end of a drawing that the box holds at one size only. An edge runs along the
 * side between its corners and lies against the side or goes round the box, so it is taken only
 * where it stands no further in than they reach, meets them at both ends, and lies against the
 * edges outside it or has an edge as far in on every side; a fill is taken only where it fills
 * what the corners and the edges hold between them. A frame of an interface is measured from the
 * box its corners stand in.
 *
 * A box whose pieces take a share of its size ({@code Component.resizeModeX} 2) has no one shape
 * at every size, so it is measured at the size its layer has in the client's fixed window. Where
 * that size is known, a grid of one sprite laid by hand over the middle is a tiled fill, and a piece
 * centred on a side and longer than it, which the client cuts to the layer, runs along the side.
 *
 * Where the interface writes a title over a frame, the frame has a heading where the title stands,
 * and where it has a close button over the frame, the frame is a window ({@code SkinTitles}).
 */
final class SkinFrames {

    /**
     * The two sizes a box is laid out at, wide enough that no piece of a frame meets the middle.
     */
    static final int WIDTH = 400;
    static final int HEIGHT = 300;
    private static final int GROWTH = 100;
    static final int WIDER = WIDTH + GROWTH;
    static final int HIGHER = HEIGHT + GROWTH;

    /**
     * The size of box a frame is measured in where its pieces take no share of its size.
     */
    private static final Size MEASURING = new Size(WIDTH, HEIGHT);

    /**
     * How far apart the two corners of a pair may stand, each measured from its own sides, as the
     * corners of the stone frame stand 13 and 12 pixels in from theirs.
     */
    static final int PAIR_SLACK = 8;

    /**
     * The resize mode of a piece that takes a share of the box, in 16384ths ({@code
     * InterfaceManager.resize}).
     */
    private static final int SHARE = 2;

    /**
     * The resize mode of a component that is the size of its layer less a fixed amount ({@code
     * InterfaceManager.resize}).
     */
    private static final int FOLLOWS = 1;

    /**
     * The reposition mode of a component centred in its layer ({@code InterfaceManager.reposition}).
     */
    private static final int CENTRED = 1;

    /**
     * The client turns a sprite component by its angle in 65536ths of a whole turn ({@code
     * Component.angle2d}, {@code Sprite.renderRotated}).
     */
    private static final int WHOLE_TURN = 65536;
    private static final int QUARTER_TURN = WHOLE_TURN / 4;
    private static final int HALF_TURN = WHOLE_TURN / 2;

    /**
     * The components of an interface stand under their interface's number in the high half of
     * their id, and their own number in the low half ({@code Component.id}).
     */
    private static final int INTERFACE_SHIFT = 16;

    /**
     * Where a piece stands along one side of the box: at its start, across it, at its end, or none
     * of these, which no frame has.
     */
    private static final int START = 0;
    private static final int ACROSS = 1;
    private static final int END = 2;
    private static final int NEITHER = -1;

    /**
     * The places of a frame's pieces, by where each stands across and down.
     */
    private static final String[][] PLACES = {
        {"topLeft", "left", "bottomLeft"},
        {"top", "centre", "bottom"},
        {"topRight", "right", "bottomRight"}
    };

    private static final List<String> EDGES = List.of("topLeft", "top", "topRight", "left", "right", "bottomLeft", "bottom", "bottomRight");

    /**
     * The corner across the box from each corner, the other corner of its side.
     */
    private static final Map<String, String> ACROSS_FROM = Map.of(
        "topLeft", "topRight",
        "topRight", "topLeft",
        "bottomLeft", "bottomRight",
        "bottomRight", "bottomLeft"
    );

    /**
     * Scripts 4155 and 4158 build a frame over the component their hooks run on, its pieces clear
     * until the pointer moves over it.
     */
    private static final List<Integer> HOVER_FRAME_SCRIPTS = List.of(4155, 4158);

    /**
     * How the client lays out a component: its size and position, each with the mode that says how
     * it follows the box ({@code Component.resizeModeX}, {@code reposModeX}).
     */
    record Layout(int width, int height, int resizeX, int resizeY, int x, int y, int reposX, int reposY) {
    }

    /**
     * A sprite component of a box, or a filled rectangle: its sprite, or none and the rectangle's
     * colour and transparency, whether it is drawn mirrored left to right, flipped top to bottom,
     * then turned a quarter turn anticlockwise, and tiled rather than stretched, how it is laid out,
     * and the layers it is laid out in within the box, the outermost first.
     */
    record Piece(int sprite, int colour, int transparency, boolean mirrored, boolean flipped, boolean turned, boolean tiled, Layout layout, List<Layout> layers) {

        Piece(int sprite, int colour, int transparency, boolean mirrored, boolean flipped, boolean tiled, Layout layout) {
            this(sprite, colour, transparency, mirrored, flipped, false, tiled, layout, List.of());
        }

        boolean isFill() {
            return sprite < 0;
        }

        /**
         * The piece turned by an angle the client turns a sprite by, a whole number of quarter
         * turns: a half turn is a mirror and a flip, and what is left a quarter turn.
         */
        Piece turnedBy(int angle) {
            var halfTurned = angle >= HALF_TURN;
            return new Piece(sprite, colour, transparency, mirrored != halfTurned, flipped != halfTurned, angle % HALF_TURN != 0, tiled, layout, layers);
        }

        Piece within(List<Layout> outer) {
            var all = new ArrayList<>(outer);
            all.addAll(layers);
            return new Piece(sprite, colour, transparency, mirrored, flipped, turned, tiled, layout, List.copyOf(all));
        }
    }

    record Rect(int x, int y, int width, int height) {

        boolean overlaps(Rect other) {
            return x < other.x + other.width && other.x < x + width && y < other.y + other.height && other.y < y + height;
        }
    }

    record Size(int width, int height) {
    }

    /**
     * The frames found, each once, with where it was found.
     */
    private final Map<String, Map<String, Object>> frames = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> scripts = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> interfaces = new LinkedHashMap<>();

    static SkinFrames ofInterfaces(File cache, SkinEdges edges, SkinWindows windows) throws Exception {
        var found = new SkinFrames();
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
                var boxes = new LinkedHashMap<Integer, List<Piece>>();
                var holders = new LinkedHashMap<Integer, java.util.Set<Integer>>();
                for (var id : inDrawingOrder(components, layout)) {
                    var component = components.get(id);
                    if (isPiece(component)) {
                        var layers = new ArrayList<Layout>();
                        var box = layout.parentOf(id);
                        while (isJoined(components.get(box))) {
                            layers.add(0, layoutOf(components.get(box)));
                            box = layout.parentOf(box);
                        }
                        boxes.computeIfAbsent(box, key -> new ArrayList<>()).add(pieceOf(component).within(layers));
                        holders.computeIfAbsent(box, key -> new java.util.HashSet<>()).add(component.layer);
                    }
                }
                for (var box : boxes.entrySet()) {
                    var layer = box.getKey() == -1 ? ComponentLayout.WINDOW : layout.boxOf(box.getKey());
                    var size = sizeOf(box.getValue(), layer);
                    if (size != null) {
                        var real = layer == null ? null : new Size(layer.width(), layer.height());
                        var pieces = real == null ? box.getValue() : withGridsTiled(box.getValue(), real, edges);
                        var parts = partsOf(pieces, size, real, edges);
                        var titles = parts == null || real == null ? null : SkinTitles.in(components, layout, box.getKey(), layer, parts);
                        var title = titles == null ? null : titles.title();
                        var close = titles == null ? null : titles.close();
                        var framed = parts == null ? null : inOrder(inItsBox(parts), size);
                        if (close != null && !windows.hasWindowIn(group, holders.get(box.getKey()))) {
                            windows.addFramed(framed, title, close, group);
                        } else {
                            found.add(framed, title == null ? null : SkinTitles.headingOf(title), null, group);
                        }
                    }
                }
            }
        }
        return found;
    }

    /**
     * The ids of an interface's components in the order the client draws them ({@code
     * InterfaceManager.draw}): the components of a layer in the order the interface holds them, each
     * layer followed at once by what it holds.
     */
    static List<Integer> inDrawingOrder(Map<Integer, Component> components, ComponentLayout layout) {
        var held = new LinkedHashMap<Integer, List<Integer>>();
        for (var id : components.keySet()) {
            held.computeIfAbsent(layout.parentOf(id), parent -> new ArrayList<>()).add(id);
        }
        var ordered = new ArrayList<Integer>();
        var next = new java.util.ArrayDeque<Integer>(held.getOrDefault(-1, List.of()).reversed());
        while (!next.isEmpty()) {
            var id = next.pop();
            ordered.add(id);
            held.getOrDefault(id, List.of()).reversed().forEach(next::push);
        }
        return ordered;
    }

    /**
     * Whether a component is a layer that the layer it is in draws as part of itself: one the player
     * cannot use, shown, and the size of the layer it is in less a fixed amount each way ({@code
     * Component.resizeModeX} and {@code resizeModeY} 1), so that what it holds keeps its place in
     * the outer layer at every size, as the background of 975 and the inner frame of 1099 lie in
     * layers of their own over the box of their frame.
     */
    private static boolean isJoined(Component layer) throws IllegalAccessException {
        return layer != null
            && layer.type == Component.TYPE_LAYER
            && !layer.hidden
            && layer.resizeModeX == FOLLOWS
            && layer.resizeModeY == FOLLOWS
            && !isUsable(layer);
    }

    /**
     * The hover frames that scripts 4155 and 4158 build, with the sprites each call gives them. A
     * call whose sprites are known only while the game runs builds no frame.
     */
    static SkinFrames hoverFrames(Map<Integer, ClientScript> scripts, SkinExport.CallReader calls, SkinEdges edges) {
        var found = new SkinFrames();
        for (var id : HOVER_FRAME_SCRIPTS) {
            for (var given : calls.argumentsOf(id)) {
                var pieces = builtBy(scripts.get(id), Arrays.asList(given));
                var size = sizeOf(pieces, null);
                if (size != null) {
                    found.add(partsOf(pieces, size, null, edges), null, id, null);
                }
            }
        }
        return found;
    }

    /**
     * The size of box a box's pieces are measured in: any size where none takes a share of the box,
     * or the size of their layer, or null where that size is not known.
     */
    private static Size sizeOf(List<Piece> pieces, ComponentLayout.Box layer) {
        var shared = pieces.stream().anyMatch(piece -> piece.layout().resizeX() == SHARE || piece.layout().resizeY() == SHARE);
        if (!shared) {
            return MEASURING;
        } else if (layer == null) {
            return null;
        } else {
            return new Size(layer.width(), layer.height());
        }
    }

    /**
     * Whether a component may be a piece of a frame: a sprite or a filled rectangle that is shown and
     * that the player cannot use. A sprite must be drawn square to the box: turned by no angle but
     * a whole number of quarter turns, and only when it is drawn whole, as the client turns each
     * tile of a tiled sprite about its own middle.
     */
    static boolean isPiece(Component component) throws IllegalAccessException {
        var sprite = component.type == Component.TYPE_GRAPHIC && component.graphic >= 0 && isSquare(component);
        var fill = component.type == Component.TYPE_RECTANGLE && component.filled;
        return (sprite || fill) && !component.hidden && !isUsable(component);
    }

    private static boolean isSquare(Component sprite) {
        return isSquare(sprite.angle2d & (WHOLE_TURN - 1), sprite.tiling);
    }

    private static boolean isSquare(int angle, boolean tiled) {
        return angle % QUARTER_TURN == 0 && (!tiled || angle % HALF_TURN == 0);
    }

    /**
     * Whether the player can use a component: it has a hook or an option with a name, as one without
     * is not offered (`InterfaceManager`, the trimmed name of an option).
     */
    private static boolean isUsable(Component component) throws IllegalAccessException {
        for (var field : Component.class.getFields()) {
            if (field.getType() == Object[].class && field.getName().startsWith("on") && field.get(component) != null) {
                return true;
            }
        }
        return component.ops != null && java.util.Arrays.stream(component.ops).anyMatch(op -> op != null && !op.isBlank());
    }

    static Piece pieceOf(Component component) {
        var sprite = component.type == Component.TYPE_GRAPHIC ? component.graphic : -1;
        var angle = sprite < 0 ? 0 : component.angle2d & (WHOLE_TURN - 1);
        var piece = new Piece(sprite, component.colour, component.transparency, component.verticalFlip, component.horizontalFlip, component.tiling, layoutOf(component));
        return piece.turnedBy(angle);
    }

    /**
     * How the client lays a component out in its layer.
     */
    private static Layout layoutOf(Component component) {
        return new Layout(
            component.originalWidth,
            component.originalHeight,
            component.resizeModeX,
            component.resizeModeY,
            component.originalX,
            component.originalY,
            component.reposModeX,
            component.reposModeY
        );
    }

    /**
     * The pieces a script builds, replayed from its instructions: each component it creates, laid
     * out, given a sprite from its arguments, flipped, turned and tiled as the constants it pushes
     * say.
     */
    static List<Piece> builtBy(ClientScript script, List<Integer> given) {
        var pieces = new ArrayList<Piece>();
        var layout = new int[8];
        var sprite = -1;
        var mirrored = false;
        var flipped = false;
        var angle = 0;
        var tiled = false;
        var building = false;
        for (var at = 0; at < script.opcodes.length; at++) {
            var opcode = script.opcodes[at];
            if (opcode == ClientScriptOpCode.CC_CREATE || opcode == ClientScriptOpCode.RETURN) {
                if (building && sprite >= 0 && isSquare(angle, tiled)) {
                    var drawnLayout = new Layout(layout[4], layout[5], layout[6], layout[7], layout[0], layout[1], layout[2], layout[3]);
                    pieces.add(new Piece(sprite, 0, 0, mirrored, flipped, tiled, drawnLayout).turnedBy(angle));
                }
                building = opcode == ClientScriptOpCode.CC_CREATE;
                sprite = -1;
                mirrored = false;
                flipped = false;
                angle = 0;
                tiled = false;
            } else if (opcode == ClientScriptOpCode.CC_IF_SETPOSITION) {
                System.arraycopy(constantsBefore(script, at, 4), 0, layout, 0, 4);
            } else if (opcode == ClientScriptOpCode.CC_IF_SETSIZE) {
                System.arraycopy(constantsBefore(script, at, 4), 0, layout, 4, 4);
            } else if (opcode == ClientScriptOpCode.CC_IF_SETGRAPHIC && script.opcodes[at - 1] == ClientScriptOpCode.PUSH_INT_LOCAL) {
                var argument = given.get(script.intOperands[at - 1]);
                sprite = argument == null ? -1 : argument;
            } else if (opcode == ClientScriptOpCode.CC_IF_SETVFLIP) {
                mirrored = constantsBefore(script, at, 1)[0] == 1;
            } else if (opcode == ClientScriptOpCode.CC_IF_SETHFLIP) {
                flipped = constantsBefore(script, at, 1)[0] == 1;
            } else if (opcode == ClientScriptOpCode.CC_IF_SET2DANGLE) {
                angle = constantsBefore(script, at, 1)[0] & (WHOLE_TURN - 1);
            } else if (opcode == ClientScriptOpCode.CC_IF_SETTILING) {
                tiled = constantsBefore(script, at, 1)[0] == 1;
            }
        }
        return pieces;
    }

    private static int[] constantsBefore(ClientScript script, int at, int count) {
        var constants = new int[count];
        for (var argument = 0; argument < count; argument++) {
            var instruction = at - count + argument;
            if (script.opcodes[instruction] != ClientScriptOpCode.PUSH_CONSTANT_INT) {
                System.out.println("a frame's script pushes a value that is not a constant at instruction " + instruction);
                System.exit(1);
            }
            constants[argument] = script.intOperands[instruction];
        }
        return constants;
    }

    /**
     * Adds the parts of a frame, where a box's pieces make one, with its heading where it has one.
     * Frames are one frame only where their headings are the same as well.
     */
    private void add(List<Map<String, Object>> parts, Map<String, Object> heading, Integer script, Integer interfaceId) {
        if (parts == null) {
            return;
        }

        var frame = new LinkedHashMap<String, Object>();
        frame.put("parts", parts);
        if (heading != null) {
            frame.put("heading", heading);
        }
        var key = Json.write(frame);
        frames.putIfAbsent(key, frame);
        if (script != null) {
            scripts.computeIfAbsent(key, k -> new TreeSet<>()).add(script);
        }
        if (interfaceId != null) {
            interfaces.computeIfAbsent(key, k -> new TreeSet<>()).add(interfaceId);
        }
    }

    /**
     * A component's size and position in a box of a size, as the client lays it out.
     */
    static Rect rect(Layout layout, int boxWidth, int boxHeight) {
        var width = length(layout.resizeX(), layout.width(), boxWidth);
        var height = length(layout.resizeY(), layout.height(), boxHeight);
        var x = place(layout.reposX(), layout.x(), width, boxWidth);
        var y = place(layout.reposY(), layout.y(), height, boxHeight);
        return new Rect(x, y, width, height);
    }

    /**
     * A piece's size and position in a box of a size, laid out through the layers it is in within
     * the box.
     */
    static Rect rectOf(Piece piece, int boxWidth, int boxHeight) {
        var box = new Rect(0, 0, boxWidth, boxHeight);
        for (var layer : piece.layers()) {
            box = within(box, rect(layer, box.width(), box.height()));
        }
        return within(box, rect(piece.layout(), box.width(), box.height()));
    }

    /**
     * A rectangle laid out in a box, moved to where the box stands, or one of no place where either
     * has none.
     */
    private static Rect within(Rect box, Rect rect) {
        var placed = List.of(box.x(), box.y(), box.width(), box.height(), rect.x(), rect.y(), rect.width(), rect.height());
        if (placed.contains(Integer.MIN_VALUE)) {
            return new Rect(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
        return new Rect(box.x() + rect.x(), box.y() + rect.y(), rect.width(), rect.height());
    }

    /**
     * The pieces of a box with each grid of one sprite laid by hand made one tiled piece: copies of a
     * sprite at its own size, drawn alike, kept to the top left of their layer, that stand edge to
     * edge in full rows and columns. The client draws such a grid at the box's real size only, as
     * 890 covers its middle with fifteen 100 pixel squares of sprite 4079; the tiled piece keeps the
     * grid's distance from each side of the layer at that size, so it fills the box at any size as
     * the grid fills it at that one. It takes the place of the first copy the client draws.
     */
    private static List<Piece> withGridsTiled(List<Piece> pieces, Size real, SkinEdges edges) {
        var grids = new LinkedHashMap<Piece, List<Piece>>();
        for (var piece : pieces) {
            if (isLaidAtItsSize(piece, edges)) {
                grids.computeIfAbsent(gridKeyOf(piece), key -> new ArrayList<>()).add(piece);
            }
        }
        var tiled = new java.util.IdentityHashMap<Piece, Piece>();
        var merged = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<Piece, Boolean>());
        for (var grid : grids.values()) {
            var whole = tiledOver(grid, real);
            if (whole != null) {
                tiled.put(grid.getFirst(), whole);
                merged.addAll(grid);
            }
        }
        var result = new ArrayList<Piece>();
        for (var piece : pieces) {
            if (tiled.containsKey(piece)) {
                result.add(tiled.get(piece));
            } else if (!merged.contains(piece)) {
                result.add(piece);
            }
        }
        return result;
    }

    /**
     * Whether a piece is a sprite drawn at its own size, kept to the top left of its layer.
     */
    private static boolean isLaidAtItsSize(Piece piece, SkinEdges edges) {
        var layout = piece.layout();
        var fixed = layout.resizeX() == 0 && layout.resizeY() == 0 && layout.reposX() == 0 && layout.reposY() == 0;
        return !piece.isFill() && fixed && edges.isSize(piece.sprite(), layout.width(), layout.height());
    }

    /**
     * A piece with its place taken away, the same for every copy in one grid.
     */
    private static Piece gridKeyOf(Piece piece) {
        var layout = piece.layout();
        var size = new Layout(layout.width(), layout.height(), 0, 0, 0, 0, 0, 0);
        return new Piece(piece.sprite(), 0, 0, piece.mirrored(), piece.flipped(), piece.turned(), piece.tiled(), size, piece.layers());
    }

    /**
     * One piece tiled over a grid of copies, or null where they are not one grid of two or more
     * that stand edge to edge in full rows and columns.
     */
    private static Piece tiledOver(List<Piece> grid, Size real) {
        var first = grid.getFirst();
        var width = first.layout().width();
        var height = first.layout().height();
        var left = grid.stream().mapToInt(piece -> piece.layout().x()).min().orElseThrow();
        var top = grid.stream().mapToInt(piece -> piece.layout().y()).min().orElseThrow();
        var right = grid.stream().mapToInt(piece -> piece.layout().x()).max().orElseThrow();
        var bottom = grid.stream().mapToInt(piece -> piece.layout().y()).max().orElseThrow();
        var cells = new java.util.HashSet<List<Integer>>();
        for (var piece : grid) {
            var x = piece.layout().x() - left;
            var y = piece.layout().y() - top;
            if (width <= 0 || height <= 0 || x % width != 0 || y % height != 0) {
                return null;
            }
            cells.add(List.of(x / width, y / height));
        }
        var across = (right - left) / width + 1;
        var down = (bottom - top) / height + 1;
        var layer = new Rect(0, 0, real.width(), real.height());
        for (var outer : first.layers()) {
            layer = within(layer, rect(outer, layer.width(), layer.height()));
        }
        if (grid.size() < 2 || cells.size() != grid.size() || cells.size() != across * down || layer.width() == Integer.MIN_VALUE) {
            return null;
        }
        var layout = new Layout(layer.width() - across * width, layer.height() - down * height, FOLLOWS, FOLLOWS, left, top, 0, 0);
        return new Piece(first.sprite(), first.colour(), first.transparency(), first.mirrored(), first.flipped(), first.turned(), true, layout, first.layers());
    }

    /**
     * A piece that is centred on a side of the layer it is in and, at the box's real size, at least
     * as long as that side, laid out as one that runs the length of the side and past both ends by
     * as much. The client cuts it to the layer, so it covers the side at that size, as the bottom
     * edge of 924 does, 428 pixels long in a box 334 wide.
     */
    private static Piece spanning(Piece piece, Size real) {
        var layer = new Rect(0, 0, real.width(), real.height());
        for (var outer : piece.layers()) {
            layer = within(layer, rect(outer, layer.width(), layer.height()));
        }
        var layout = piece.layout();
        var acrossBy = overrun(layout.resizeX(), layout.reposX(), layout.width(), layer.width());
        var downBy = overrun(layout.resizeY(), layout.reposY(), layout.height(), layer.height());
        var spanned = new Layout(
            acrossBy == null ? layout.width() : -acrossBy,
            downBy == null ? layout.height() : -downBy,
            acrossBy == null ? layout.resizeX() : FOLLOWS,
            downBy == null ? layout.resizeY() : FOLLOWS,
            layout.x(),
            layout.y(),
            layout.reposX(),
            layout.reposY()
        );
        return new Piece(piece.sprite(), piece.colour(), piece.transparency(), piece.mirrored(), piece.flipped(), piece.turned(), piece.tiled(), spanned, piece.layers());
    }

    /**
     * How much longer than its side a piece of a fixed length centred on the side is, or null where
     * it is not such a piece or is shorter than the side.
     */
    private static Integer overrun(int resizeMode, int reposMode, int length, int side) {
        var fixed = resizeMode == 0 && reposMode == CENTRED;
        return fixed && side != Integer.MIN_VALUE && length >= side ? length - side : null;
    }

    private static int length(int mode, int value, int box) {
        return switch (mode) {
            case 0 -> value;
            case 1 -> box - value;
            case 2 -> (value * box) >> 14;
            default -> Integer.MIN_VALUE;
        };
    }

    private static int place(int mode, int value, int length, int box) {
        if (length == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return switch (mode) {
            case 0 -> value;
            case 1 -> value + (box - length) / 2;
            case 2 -> box - length - value;
            case 3 -> (value * box) >> 14;
            case 4 -> (box - length) / 2 + ((box * value) >> 14);
            default -> box - ((value * box) >> 14) - length;
        };
    }

    /**
     * Where a piece stands along a side, from where it is and how long it is in the two boxes: across
     * the side where it grows with the box, at the start where it stays put in the first half, and at
     * the end where it keeps its distance from the end in the second half.
     */
    private static int side(int place, int length, int box, int widerPlace, int widerLength, int widerBox) {
        if (length == Integer.MIN_VALUE || place == Integer.MIN_VALUE) {
            return NEITHER;
        } else if (widerLength > length) {
            return ACROSS;
        } else if (widerLength == length && place == widerPlace && place < box / 2) {
            return START;
        } else if (widerLength == length && box - place - length == widerBox - widerPlace - widerLength && place > box / 2) {
            return END;
        } else {
            return NEITHER;
        }
    }

    /**
     * A piece as the file holds it: its place, its sprite, how it is drawn, and where it stands in a
     * box of a size, measured from the sides it keeps to. A corner is measured from its two sides and has its
     * size; an edge runs from a distance after one corner to a distance before the other, a distance
     * in from its side, as thick as it is; the fill keeps a distance from each side.
     */
    private static Map<String, Object> written(Piece piece, String place, Rect rect, Size box) {
        var written = new LinkedHashMap<String, Object>();
        written.put("place", place);
        if (piece.isFill()) {
            written.put("colour", piece.colour());
            if (piece.transparency() > 0) {
                written.put("transparency", piece.transparency());
            }
        } else {
            written.put("sprite", piece.sprite());
        }
        if (piece.mirrored()) {
            written.put("mirrored", true);
        }
        if (piece.flipped()) {
            written.put("flipped", true);
        }
        if (piece.turned()) {
            written.put("turned", true);
        }
        if (piece.tiled()) {
            written.put("tiled", true);
        }
        var left = rect.x();
        var top = rect.y();
        var right = box.width() - rect.x() - rect.width();
        var bottom = box.height() - rect.y() - rect.height();
        switch (place) {
            case "top", "bottom" -> {
                written.put("start", left);
                written.put("end", right);
                written.put("inset", place.equals("top") ? top : bottom);
                written.put("thickness", rect.height());
            }
            case "left", "right" -> {
                written.put("start", top);
                written.put("end", bottom);
                written.put("inset", place.equals("left") ? left : right);
                written.put("thickness", rect.width());
            }
            case "centre" -> {
                written.put("left", left);
                written.put("top", top);
                written.put("right", right);
                written.put("bottom", bottom);
            }
            default -> {
                written.put("x", place.endsWith("Left") ? left : right);
                written.put("y", place.startsWith("top") ? top : bottom);
                written.put("width", rect.width());
                written.put("height", rect.height());
            }
        }
        return written;
    }

    /**
     * A box's pieces as the parts of a frame, each in its place, measured in a box of the size the
     * frames are measured in; null where they make no frame.
     */
    static List<Map<String, Object>> partsOf(List<Piece> pieces, SkinEdges edges) {
        return partsOf(pieces, MEASURING, null, edges);
    }

    /**
     * A box's pieces as the parts of a frame, each in its place, measured in a box of a size, in the
     * order {@link #inOrder} gives; null where they make no frame. The pieces are laid out in that
     * box and in one larger each way to tell where each stands. A piece in none of a frame's places,
     * such as one centred on a side, is passed over, and so is a corner without its pair, a corner of
     * a drawing laid out by hand at the box's real size where that size is known, an edge further in
     * than the corners of its side reach, one that leaves a gap between itself and them along its
     * side, one that does not lie against its side, and a fill that leaves a gap between itself and
     * what the corners and the edges reach.
     */
    static List<Map<String, Object>> partsOf(List<Piece> given, Size box, Size real, SkinEdges edges) {
        var pieces = real == null ? given : given.stream().map(piece -> spanning(piece, real)).toList();
        var placed = new ArrayList<Map<String, Object>>();
        for (var piece : pieces) {
            var small = rectOf(piece, box.width(), box.height());
            var large = rectOf(piece, box.width() + GROWTH, box.height() + GROWTH);
            var across = side(small.x(), small.width(), box.width(), large.x(), large.width(), box.width() + GROWTH);
            var down = side(small.y(), small.height(), box.height(), large.y(), large.height(), box.height() + GROWTH);
            var place = across == NEITHER || down == NEITHER ? null : PLACES[across][down];
            if (place != null && (!piece.isFill() || !ACROSS_FROM.containsKey(place))) {
                placed.add(written(piece, place, small, box));
            }
        }

        var laidByHand = real == null ? List.<Map<String, Object>>of() : laidByHandIn(pieces, real);
        var standing = placed.stream().filter(part -> !isCorner(part) || !isLaidByHand(part, laidByHand)).toList();
        var corners = standing.stream().filter(part -> isCorner(part) && isPaired(part, standing)).toList();
        var between = placed.stream().filter(part -> isEdge(part) && isBetween(part, corners) && (part.containsKey("sprite") || isWithin(part, corners))).toList();
        var joined = between.stream().filter(part -> isJoined(part, corners)).toList();
        var edgesKept = againstTheirSides(joined, between, edges);
        var parts = new ArrayList<Map<String, Object>>();
        var places = new java.util.HashSet<String>();
        for (var part : placed) {
            var kept = holds(corners, part) || holds(edgesKept, part) || part.get("place").equals("centre") && isFilling(part, corners, edgesKept);
            if (kept) {
                parts.add(part);
                places.add((String) part.get("place"));
            }
        }
        return places.containsAll(EDGES) ? inOrder(parts, box) : null;
    }

    private static boolean isEdge(Map<String, Object> part) {
        return !isCorner(part) && !part.get("place").equals("centre");
    }

    private static boolean holds(List<Map<String, Object>> parts, Map<String, Object> part) {
        return parts.stream().anyMatch(each -> each == part);
    }

    /**
     * The sprites of a box that keep their size and their place against the start of a side while
     * they stand in its far half at the box's real size, or against its end while they stand in its
     * near half, and lie inside it, each written as the corner where it stands. A box holds such a
     * piece where it was drawn by hand at that one size, as the client never resizes it.
     */
    private static List<Map<String, Object>> laidByHandIn(List<Piece> pieces, Size real) {
        var found = new ArrayList<Map<String, Object>>();
        for (var piece : pieces) {
            if (!piece.isFill()) {
                var small = rectOf(piece, real.width(), real.height());
                var large = rectOf(piece, real.width() + GROWTH, real.height() + GROWTH);
                var across = side(small.x(), small.width(), real.width(), large.x(), large.width(), real.width() + GROWTH);
                var down = side(small.y(), small.height(), real.height(), large.y(), large.height(), real.height() + GROWTH);
                var stuckAcross = farSide(small.x(), small.width(), real.width(), large.x(), large.width(), real.width() + GROWTH);
                var stuckDown = farSide(small.y(), small.height(), real.height(), large.y(), large.height(), real.height() + GROWTH);
                var standsAcross = stuckAcross != NEITHER ? stuckAcross : atAnEnd(across);
                var standsDown = stuckDown != NEITHER ? stuckDown : atAnEnd(down);
                var stuck = stuckAcross != NEITHER || stuckDown != NEITHER;
                var inside = small.x() >= 0 && small.y() >= 0 && small.x() + small.width() <= real.width() && small.y() + small.height() <= real.height();
                if (stuck && inside && standsAcross != NEITHER && standsDown != NEITHER) {
                    found.add(written(piece, PLACES[standsAcross][standsDown], small, real));
                }
            }
        }
        return found;
    }

    private static int atAnEnd(int side) {
        return side == START || side == END ? side : NEITHER;
    }

    /**
     * Whether a piece keeps its size and its place against the start of a side while it stands in
     * the far half of the side, or against the end while it stands in the near half: the end of the
     * side it stands at, or none.
     */
    private static int farSide(int place, int length, int box, int widerPlace, int widerLength, int widerBox) {
        if (length == Integer.MIN_VALUE || place == Integer.MIN_VALUE || widerLength != length) {
            return NEITHER;
        } else if (place == widerPlace && place > box / 2) {
            return END;
        } else if (box - place - length == widerBox - widerPlace - widerLength && place + length < box / 2) {
            return START;
        } else {
            return NEITHER;
        }
    }

    /**
     * Whether a corner is the near end of a drawing laid out by hand: a piece of its size laid out by
     * hand stands at the other end of one of its sides, the same distance in from that side, as the
     * bottom corners of a book or the lower half of a pillar keep to the top of a box the client
     * never resizes. Such a corner does not stay at its corner as the box grows.
     */
    private static boolean isLaidByHand(Map<String, Object> corner, List<Map<String, Object>> laidByHand) {
        var place = (String) corner.get("place");
        return laidByHand.stream().anyMatch(other -> {
            var otherPlace = (String) other.get("place");
            var sameSize = other.get("width").equals(corner.get("width")) && other.get("height").equals(corner.get("height"));
            var sideAcross = place.endsWith("Left") == otherPlace.endsWith("Left") ? "x" : null;
            var sideDown = place.startsWith("top") == otherPlace.startsWith("top") ? "y" : null;
            var shared = sideAcross != null ? sideAcross : sideDown;
            return sameSize && !otherPlace.equals(place) && shared != null && Math.abs((Integer) other.get(shared) - (Integer) corner.get(shared)) <= PAIR_SLACK;
        });
    }

    /**
     * Whether an edge starts and ends at the corners of its side, no more than a few pixels past the
     * furthest that the corners at each end reach along the side, as an edge that leaves a gap runs
     * along something inside the frame.
     */
    private static boolean isJoined(Map<String, Object> edge, List<Map<String, Object>> corners) {
        var place = (String) edge.get("place");
        var down = place.equals("left") || place.equals("right");
        var first = down ? "top" + capitalised(place) : place + "Left";
        var last = down ? "bottom" + capitalised(place) : place + "Right";
        var along = down ? "y" : "x";
        var length = down ? "height" : "width";
        return (Integer) edge.get("start") <= alongReachOf(first, along, length, corners) + PAIR_SLACK
            && (Integer) edge.get("end") <= alongReachOf(last, along, length, corners) + PAIR_SLACK;
    }

    private static String capitalised(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    /**
     * How far along a side from its end the corners at one place reach, the furthest of them, or 0
     * where there is none.
     */
    private static int alongReachOf(String place, String along, String length, List<Map<String, Object>> corners) {
        return corners.stream()
            .filter(corner -> corner.get("place").equals(place))
            .mapToInt(corner -> (Integer) corner.get(along) + (Integer) corner.get(length))
            .max()
            .orElse(0);
    }

    /**
     * The edges that lie against their side or go round the box: on each side, from the outermost
     * in, each edge that stands no further in than the edges outside it are seen ({@link
     * SkinEdges#seenOf}), and each edge that every other side has an edge as far in as, to within a
     * few pixels. A frame's edges lie one against the next, as the thin line inside the stone frame
     * does, or make a ring of their own inside it, as a gold line does, so an edge with a gap outside
     * it on one side alone is a rule or a divider of what the box holds, as the lines under the
     * heading of a table are.
     */
    private static List<Map<String, Object>> againstTheirSides(List<Map<String, Object>> edges, List<Map<String, Object>> rings, SkinEdges seen) {
        var sides = List.of("top", "bottom", "left", "right");
        var kept = new ArrayList<Map<String, Object>>();
        for (var side : sides) {
            var onSide = edges.stream()
                .filter(edge -> edge.get("place").equals(side))
                .sorted(java.util.Comparator.comparingInt(edge -> (Integer) edge.get("inset")))
                .toList();
            Integer reach = null;
            for (var edge : onSide) {
                if (reach == null || (Integer) edge.get("inset") <= reach || isInRing(edge, rings, sides)) {
                    kept.add(edge);
                    reach = reach == null ? seen.seenOf(edge) : Math.max(reach, seen.seenOf(edge));
                }
            }
        }
        return kept;
    }

    private static boolean isInRing(Map<String, Object> edge, List<Map<String, Object>> edges, List<String> sides) {
        var inset = (Integer) edge.get("inset");
        return sides.stream().allMatch(side -> edges.stream().anyMatch(other -> other.get("place").equals(side) && Math.abs((Integer) other.get("inset") - inset) <= PAIR_SLACK));
    }

    /**
     * The parts measured from the box the frame's corners stand in, where they stand in from the
     * sides of the box they were found in: on each side, the outermost corner on that side stands
     * at it, as a frame drawn in the middle of a larger box is a frame of the box it marks out.
     */
    static List<Map<String, Object>> inItsBox(List<Map<String, Object>> parts) {
        var corners = parts.stream().filter(SkinFrames::isCorner).toList();
        var left = marginOf("Left", "x", corners);
        var right = marginOf("Right", "x", corners);
        var top = marginOf("top", "y", corners);
        var bottom = marginOf("bottom", "y", corners);
        var moved = new ArrayList<Map<String, Object>>();
        for (var part : parts) {
            var place = (String) part.get("place");
            var copy = new LinkedHashMap<>(part);
            switch (place) {
                case "top", "bottom" -> {
                    copy.put("start", (Integer) part.get("start") - left);
                    copy.put("end", (Integer) part.get("end") - right);
                    copy.put("inset", (Integer) part.get("inset") - (place.equals("top") ? top : bottom));
                }
                case "left", "right" -> {
                    copy.put("start", (Integer) part.get("start") - top);
                    copy.put("end", (Integer) part.get("end") - bottom);
                    copy.put("inset", (Integer) part.get("inset") - (place.equals("left") ? left : right));
                }
                case "centre" -> {
                    copy.put("left", (Integer) part.get("left") - left);
                    copy.put("top", (Integer) part.get("top") - top);
                    copy.put("right", (Integer) part.get("right") - right);
                    copy.put("bottom", (Integer) part.get("bottom") - bottom);
                }
                default -> {
                    copy.put("x", (Integer) part.get("x") - (place.endsWith("Left") ? left : right));
                    copy.put("y", (Integer) part.get("y") - (place.startsWith("top") ? top : bottom));
                }
            }
            moved.add(copy);
        }
        return moved;
    }

    /**
     * How far in from a side the outermost corner on that side stands, or 0 where one stands at the
     * side or past it.
     */
    static int marginOf(String side, String measure, List<Map<String, Object>> corners) {
        var margin = corners.stream()
            .filter(corner -> ((String) corner.get("place")).startsWith(side) || ((String) corner.get("place")).endsWith(side))
            .mapToInt(corner -> (Integer) corner.get(measure))
            .min()
            .orElse(0);
        return Math.max(0, margin);
    }

    static boolean isCorner(Map<String, Object> part) {
        return ACROSS_FROM.containsKey((String) part.get("place"));
    }

    /**
     * Whether the corner across the box from a corner holds a part of its size within a few pixels
     * of its place, each measured from its own sides, as the corners of a frame are drawn in pairs
     * and an icon or a picture at one corner has none.
     */
    private static boolean isPaired(Map<String, Object> corner, List<Map<String, Object>> parts) {
        var across = ACROSS_FROM.get((String) corner.get("place"));
        return parts.stream().anyMatch(other -> other.get("place").equals(across)
            && other.get("width").equals(corner.get("width"))
            && other.get("height").equals(corner.get("height"))
            && Math.abs((Integer) other.get("x") - (Integer) corner.get("x")) <= PAIR_SLACK
            && Math.abs((Integer) other.get("y") - (Integer) corner.get("y")) <= PAIR_SLACK);
    }

    /**
     * Whether an edge stands between these corners: where it stands less far in from its side than
     * the corners of that side reach, as a divider or a rule further in is not part of the frame.
     */
    private static boolean isBetween(Map<String, Object> edge, List<Map<String, Object>> corners) {
        return (Integer) edge.get("inset") < reachOf((String) edge.get("place"), corners);
    }

    /**
     * Whether an edge reaches in from its side no further than the corners of that side reach, as a
     * band of one colour along a side does under a title (890) and as one that reaches further is a
     * panel over what the box holds (902).
     */
    private static boolean isWithin(Map<String, Object> edge, List<Map<String, Object>> corners) {
        return (Integer) edge.get("inset") + (Integer) edge.get("thickness") <= reachOf((String) edge.get("place"), corners);
    }

    /**
     * Whether a fill fills the frame: where it comes out on each side to within a few pixels of the
     * furthest in that the corners or the edges of that side reach, as a fill that leaves a gap
     * fills a panel or a picture inside the box, not the frame.
     */
    private static boolean isFilling(Map<String, Object> fill, List<Map<String, Object>> corners, List<Map<String, Object>> edges) {
        return List.of("left", "top", "right", "bottom").stream().allMatch(side -> {
            var reach = Math.max(reachOf(side, corners), edgeReachOf(side, edges));
            return (Integer) fill.get(side) <= reach + PAIR_SLACK;
        });
    }

    /**
     * How far in from a side the edges along it reach, the furthest of them, or 0 where it has none.
     */
    private static int edgeReachOf(String side, List<Map<String, Object>> edges) {
        return edges.stream()
            .filter(edge -> edge.get("place").equals(side))
            .mapToInt(edge -> (Integer) edge.get("inset") + (Integer) edge.get("thickness"))
            .max()
            .orElse(0);
    }

    /**
     * How far in from a side the corners on that side reach, the furthest of them, or 0 where it
     * has none.
     */
    static int reachOf(String side, List<Map<String, Object>> corners) {
        var reach = 0;
        for (var corner : corners) {
            var place = (String) corner.get("place");
            var onSide = switch (side) {
                case "top", "bottom" -> place.startsWith(side);
                default -> place.toLowerCase().endsWith(side);
            };
            var down = side.equals("top") || side.equals("bottom");
            if (onSide) {
                reach = Math.max(reach, down ? (Integer) corner.get("y") + (Integer) corner.get("height") : (Integer) corner.get("x") + (Integer) corner.get("width"));
            }
        }
        return reach;
    }

    /**
     * Parts of a frame in one order whatever order the box holds them in, so that a frame is found
     * once: the parts that cover each other keep the order the client draws them in, and the others
     * are put in the order of how the file writes them. Each time, the first part by how it is
     * written that no part before it in the box covers is taken next.
     */
    static List<Map<String, Object>> inOrder(List<Map<String, Object>> parts, Size box) {
        var left = new ArrayList<>(parts);
        var ordered = new ArrayList<Map<String, Object>>();
        while (!left.isEmpty()) {
            Map<String, Object> next = null;
            for (var at = 0; at < left.size(); at++) {
                var part = left.get(at);
                var area = areaOf(part, box);
                var free = left.subList(0, at).stream().noneMatch(before -> areaOf(before, box).overlaps(area));
                if (free && (next == null || Json.write(part).compareTo(Json.write(next)) < 0)) {
                    next = part;
                }
            }
            ordered.add(next);
            left.remove(next);
        }
        return ordered;
    }

    /**
     * Where a part as the file holds it stands in a box of a size.
     */
    private static Rect areaOf(Map<String, Object> part, Size box) {
        var place = (String) part.get("place");
        return switch (place) {
            case "top", "bottom" -> {
                var thickness = (Integer) part.get("thickness");
                var inset = (Integer) part.get("inset");
                var start = (Integer) part.get("start");
                var y = place.equals("top") ? inset : box.height() - inset - thickness;
                yield new Rect(start, y, box.width() - start - (Integer) part.get("end"), thickness);
            }
            case "left", "right" -> {
                var thickness = (Integer) part.get("thickness");
                var inset = (Integer) part.get("inset");
                var start = (Integer) part.get("start");
                var x = place.equals("left") ? inset : box.width() - inset - thickness;
                yield new Rect(x, start, thickness, box.height() - start - (Integer) part.get("end"));
            }
            case "centre" -> {
                var left = (Integer) part.get("left");
                var top = (Integer) part.get("top");
                yield new Rect(left, top, box.width() - left - (Integer) part.get("right"), box.height() - top - (Integer) part.get("bottom"));
            }
            default -> {
                var width = (Integer) part.get("width");
                var height = (Integer) part.get("height");
                var x = place.endsWith("Left") ? (Integer) part.get("x") : box.width() - (Integer) part.get("x") - width;
                var y = place.startsWith("top") ? (Integer) part.get("y") : box.height() - (Integer) part.get("y") - height;
                yield new Rect(x, y, width, height);
            }
        };
    }

    /**
     * Leaves out each frame that is the frame of one of these windows, as the window holds it,
     * measured from the box its corners stand in.
     */
    @SuppressWarnings("unchecked")
    void leaveOut(List<Map<String, Object>> windows) {
        var framesOfWindows = new java.util.HashSet<String>();
        for (var window : windows) {
            framesOfWindows.add(Json.write(inOrder(inItsBox((List<Map<String, Object>>) window.get("parts")), MEASURING)));
        }
        frames.entrySet().removeIf(frame -> framesOfWindows.contains(Json.write(inOrder(inItsBox((List<Map<String, Object>>) frame.getValue().get("parts")), MEASURING))));
    }

    int size() {
        return frames.size();
    }

    /**
     * The frames as the file holds them, each with the scripts and the interfaces it is found in.
     */
    List<Map<String, Object>> written() {
        var written = new ArrayList<Map<String, Object>>();
        for (var frame : frames.entrySet()) {
            var entry = new LinkedHashMap<String, Object>(frame.getValue());
            var foundInScripts = scripts.get(frame.getKey());
            var foundInInterfaces = interfaces.get(frame.getKey());
            if (foundInScripts != null) {
                entry.put("scripts", new ArrayList<>(foundInScripts));
            }
            if (foundInInterfaces != null) {
                entry.put("interfaces", new ArrayList<>(foundInInterfaces));
            }
            written.add(entry);
        }
        return written;
    }
}
