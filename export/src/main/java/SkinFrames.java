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
 * order the client draws them. A filled rectangle that grows both ways is a fill of one colour. A component the player can
 * use, one with a hook or an option, is not part of a frame, and a box without a corner at each
 * corner and an edge along each side is not a frame.
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
 * at every size, so it is measured at the size its layer has in the client's fixed window.
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
    private static final int PAIR_SLACK = 8;

    /**
     * The resize mode of a piece that takes a share of the box, in 16384ths ({@code
     * InterfaceManager.resize}).
     */
    private static final int SHARE = 2;

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
     * colour and transparency, whether it is drawn mirrored left to right, flipped top to bottom, and
     * tiled rather than stretched, and how it is laid out.
     */
    record Piece(int sprite, int colour, int transparency, boolean mirrored, boolean flipped, boolean tiled, Layout layout) {

        boolean isFill() {
            return sprite < 0;
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

    static SkinFrames ofInterfaces(File cache, SkinEdges edges) throws Exception {
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
                var layers = new LinkedHashMap<Integer, ComponentLayout.Box>();
                for (var component : components.entrySet()) {
                    if (isPiece(component.getValue())) {
                        var layer = component.getValue().layer;
                        boxes.computeIfAbsent(layer, key -> new ArrayList<>()).add(pieceOf(component.getValue()));
                        if (!layers.containsKey(layer)) {
                            layers.put(layer, layout.layerBoxOf(component.getKey()));
                        }
                    }
                }
                for (var box : boxes.entrySet()) {
                    var layer = layers.get(box.getKey());
                    var size = sizeOf(box.getValue(), layer);
                    if (size != null) {
                        var real = layer == null ? null : new Size(layer.width(), layer.height());
                        var parts = partsOf(box.getValue(), size, real, edges);
                        found.add(parts == null ? null : inOrder(inItsBox(parts), size), null, group);
                    }
                }
            }
        }
        return found;
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
                    found.add(partsOf(pieces, size, null, edges), id, null);
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
     * Whether a component may be a piece of a frame: a sprite or a filled rectangle the player
     * cannot use.
     */
    static boolean isPiece(Component component) throws IllegalAccessException {
        var sprite = component.type == Component.TYPE_GRAPHIC && component.graphic >= 0;
        var fill = component.type == Component.TYPE_RECTANGLE && component.filled;
        return (sprite || fill) && !isUsable(component);
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
        var layout = new Layout(
            component.originalWidth,
            component.originalHeight,
            component.resizeModeX,
            component.resizeModeY,
            component.originalX,
            component.originalY,
            component.reposModeX,
            component.reposModeY
        );
        var sprite = component.type == Component.TYPE_GRAPHIC ? component.graphic : -1;
        return new Piece(sprite, component.colour, component.transparency, component.verticalFlip, component.horizontalFlip, component.tiling, layout);
    }

    /**
     * The pieces a script builds, replayed from its instructions: each component it creates, laid
     * out, given a sprite from its arguments, flipped and tiled as the constants it pushes say.
     */
    static List<Piece> builtBy(ClientScript script, List<Integer> given) {
        var pieces = new ArrayList<Piece>();
        var layout = new int[8];
        var sprite = -1;
        var mirrored = false;
        var flipped = false;
        var tiled = false;
        var building = false;
        for (var at = 0; at < script.opcodes.length; at++) {
            var opcode = script.opcodes[at];
            if (opcode == ClientScriptOpCode.CC_CREATE || opcode == ClientScriptOpCode.RETURN) {
                if (building && sprite >= 0) {
                    pieces.add(new Piece(sprite, 0, 0, mirrored, flipped, tiled, new Layout(layout[4], layout[5], layout[6], layout[7], layout[0], layout[1], layout[2], layout[3])));
                }
                building = opcode == ClientScriptOpCode.CC_CREATE;
                sprite = -1;
                mirrored = false;
                flipped = false;
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
     * Adds the parts of a frame, where a box's pieces make one.
     */
    private void add(List<Map<String, Object>> parts, Integer script, Integer interfaceId) {
        if (parts == null) {
            return;
        }

        var frame = new LinkedHashMap<String, Object>();
        frame.put("parts", parts);
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

    private static int length(int mode, int value, int box) {
        return switch (mode) {
            case 0 -> value;
            case 1 -> box - value;
            case 2 -> (value * box) >> 14;
            default -> Integer.MIN_VALUE;
        };
    }

    private static int place(int mode, int value, int length, int box) {
        return switch (mode) {
            case 0 -> value;
            case 1 -> value + (box - length) / 2;
            case 2 -> box - length - value;
            default -> Integer.MIN_VALUE;
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
    static List<Map<String, Object>> partsOf(List<Piece> pieces, Size box, Size real, SkinEdges edges) {
        var placed = new ArrayList<Map<String, Object>>();
        for (var piece : pieces) {
            var small = rect(piece.layout(), box.width(), box.height());
            var large = rect(piece.layout(), box.width() + GROWTH, box.height() + GROWTH);
            var across = side(small.x(), small.width(), box.width(), large.x(), large.width(), box.width() + GROWTH);
            var down = side(small.y(), small.height(), box.height(), large.y(), large.height(), box.height() + GROWTH);
            var place = across == NEITHER || down == NEITHER ? null : PLACES[across][down];
            if (place != null && (!piece.isFill() || place.equals("centre"))) {
                placed.add(written(piece, place, small, box));
            }
        }

        var laidByHand = real == null ? List.<Map<String, Object>>of() : laidByHandIn(pieces, real);
        var standing = placed.stream().filter(part -> !isCorner(part) || !isLaidByHand(part, laidByHand)).toList();
        var corners = standing.stream().filter(part -> isCorner(part) && isPaired(part, standing)).toList();
        var between = placed.stream().filter(part -> isEdge(part) && isBetween(part, corners)).toList();
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
                var small = rect(piece.layout(), real.width(), real.height());
                var large = rect(piece.layout(), real.width() + GROWTH, real.height() + GROWTH);
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
    private static int marginOf(String side, String measure, List<Map<String, Object>> corners) {
        var margin = corners.stream()
            .filter(corner -> ((String) corner.get("place")).startsWith(side) || ((String) corner.get("place")).endsWith(side))
            .mapToInt(corner -> (Integer) corner.get(measure))
            .min()
            .orElse(0);
        return Math.max(0, margin);
    }

    private static boolean isCorner(Map<String, Object> part) {
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
    private static int reachOf(String side, List<Map<String, Object>> corners) {
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
