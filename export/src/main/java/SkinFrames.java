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
 * pieces in one place, as an ornate frame does, and they are kept in the order the client draws
 * them. A filled rectangle that grows both ways is a fill of one colour. A component the player can
 * use, one with a hook or an option, is not part of a frame, and a box without a corner at each
 * corner and an edge along each side is not a frame.
 */
final class SkinFrames {

    /**
     * The two sizes a box is laid out at, wide enough that no piece of a frame meets the middle.
     */
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    static final int WIDER = 500;
    static final int HIGHER = 400;

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
    }

    /**
     * The frames found, each once, with where it was found.
     */
    private final Map<String, Map<String, Object>> frames = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> scripts = new LinkedHashMap<>();
    private final Map<String, TreeSet<Integer>> interfaces = new LinkedHashMap<>();

    static SkinFrames ofInterfaces(File cache) throws Exception {
        var found = new SkinFrames();
        var index = Cache.index(cache, Js5Archive.INTERFACES);
        for (var group : Cache.groupsOf(index)) {
            var data = Cache.group(cache, Js5Archive.INTERFACES, group);
            if (data != null) {
                var boxes = new LinkedHashMap<Integer, List<Piece>>();
                for (var file : Cache.split(data, index, group).values()) {
                    var component = new Component();
                    component.decode(new Packet(file));
                    if (isPiece(component)) {
                        boxes.computeIfAbsent(component.layer, layer -> new ArrayList<>()).add(pieceOf(component));
                    }
                }
                for (var box : boxes.values()) {
                    found.add(box, null, group);
                }
            }
        }
        return found;
    }

    /**
     * The hover frames that scripts 4155 and 4158 build, with the sprites each call gives them. A
     * call whose sprites are known only while the game runs builds no frame.
     */
    static SkinFrames hoverFrames(Map<Integer, ClientScript> scripts, SkinExport.CallReader calls) {
        var found = new SkinFrames();
        for (var id : HOVER_FRAME_SCRIPTS) {
            for (var given : calls.argumentsOf(id)) {
                found.add(builtBy(scripts.get(id), Arrays.asList(given)), id, null);
            }
        }
        return found;
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
     * Whether the player can use a component: it has a hook or an option.
     */
    private static boolean isUsable(Component component) throws IllegalAccessException {
        for (var field : Component.class.getFields()) {
            if (field.getType() == Object[].class && field.getName().startsWith("on") && field.get(component) != null) {
                return true;
            }
        }
        return component.ops != null && java.util.Arrays.stream(component.ops).anyMatch(op -> op != null);
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
     * Adds a box's pieces as a frame, where they make one.
     */
    private void add(List<Piece> pieces, Integer script, Integer interfaceId) {
        var parts = partsOf(pieces);
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
     * A piece as the file holds it: its place, its sprite, how it is drawn, and where it stands in the larger
     * box, measured from the sides it keeps to. A corner is measured from its two sides and has its
     * size; an edge runs from a distance after one corner to a distance before the other, a distance
     * in from its side, as thick as it is; the fill keeps a distance from each side.
     */
    private static Map<String, Object> written(Piece piece, String place, Rect rect) {
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
        var right = WIDER - rect.x() - rect.width();
        var bottom = HIGHER - rect.y() - rect.height();
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
     * A box's pieces as the parts of a frame, each in its place, in the order the box holds them,
     * measured in the larger box; null where they make no frame. A piece in none of a frame's
     * places, such as one centred on a side, is passed over.
     */
    static List<Map<String, Object>> partsOf(List<Piece> pieces) {
        var parts = new ArrayList<Map<String, Object>>();
        var places = new java.util.HashSet<String>();
        for (var piece : pieces) {
            var small = rect(piece.layout(), WIDTH, HEIGHT);
            var large = rect(piece.layout(), WIDER, HIGHER);
            var across = side(small.x(), small.width(), WIDTH, large.x(), large.width(), WIDER);
            var down = side(small.y(), small.height(), HEIGHT, large.y(), large.height(), HIGHER);
            var place = across == NEITHER || down == NEITHER ? null : PLACES[across][down];
            if (place != null && (!piece.isFill() || place.equals("centre"))) {
                places.add(place);
                parts.add(written(piece, place, large));
            }
        }
        return places.containsAll(EDGES) ? parts : null;
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
