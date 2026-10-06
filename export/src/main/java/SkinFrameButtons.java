import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The buttons whose scripts build them as a frame: sprites at the corners and along the sides,
 * over a fill of one colour where they have one, built afresh under the pointer by another script.
 * Each is read by playing its two scripts back ({@code ScriptReplay}) for a button of two sizes and
 * laying out what they create as the frames are ({@code SkinFrames.partsOf}).
 */
final class SkinFrameButtons {

    /**
     * A button's scripts: the one that builds it, and the one that builds it under the pointer.
     */
    private record Pair(int plain, int hover) {
    }

    /**
     * The stone button that 151 components of 51 interfaces have, such as the squad reset question
     * of interface 292: script 92 builds it on load and when the pointer leaves (proc 679), and
     * script 94 under the pointer (proc 1360, with a dark see-through fill). And the bevelled
     * button of the question that accepts a graphics setting, interface 883: script 2701 builds it
     * (proc 1151) and script 2702 builds it under the pointer (proc 1166).
     */
    private static final List<Pair> PAIRS = List.of(new Pair(679, 1360), new Pair(1151, 1166));

    private SkinFrameButtons() {
    }

    static List<Map<String, Object>> read(Map<Integer, ClientScript> scripts) {
        var written = new ArrayList<Map<String, Object>>();
        for (var pair : PAIRS) {
            var button = new LinkedHashMap<String, Object>();
            button.put("frame", frameOf(scripts, pair.plain()));
            button.put("hoverFrame", frameOf(scripts, pair.hover()));
            button.put("scripts", List.of(pair.plain(), pair.hover()));
            written.add(button);
        }
        return written;
    }

    private static Map<String, Object> frameOf(Map<Integer, ClientScript> scripts, int script) {
        var small = ScriptReplay.run(scripts, script, SkinFrames.WIDTH, SkinFrames.HEIGHT);
        var large = ScriptReplay.run(scripts, script, SkinFrames.WIDER, SkinFrames.HIGHER);
        var pieces = new ArrayList<SkinFrames.Piece>();
        for (var at = 0; at < small.size() && at < large.size(); at++) {
            var piece = pieceOf(small.get(at), large.get(at));
            if (piece != null) {
                pieces.add(piece);
            }
        }
        var parts = SkinFrames.partsOf(pieces);
        if (small.size() != large.size() || parts == null) {
            System.out.println("script " + script + " no longer builds a frame");
            System.exit(1);
        }
        var frame = new LinkedHashMap<String, Object>();
        frame.put("parts", parts);
        return frame;
    }

    /**
     * A created component as a piece of a frame, its place and size given the modes that make it
     * stand where it stood in both sizes of button; none for a component that is neither a sprite
     * nor a filled rectangle, or that follows the button in no way the client has.
     */
    private static SkinFrames.Piece pieceOf(ScriptReplay.Created small, ScriptReplay.Created large) {
        var sprite = small.type == Component.TYPE_GRAPHIC && small.graphic >= 0;
        var fill = small.type == Component.TYPE_RECTANGLE && small.filled;
        if (!sprite && !fill) {
            return null;
        }

        var inSmall = SkinFrames.rect(layoutOf(small), SkinFrames.WIDTH, SkinFrames.HEIGHT);
        var inLarge = SkinFrames.rect(layoutOf(large), SkinFrames.WIDER, SkinFrames.HIGHER);
        var across = axisOf(inSmall.x(), inSmall.width(), SkinFrames.WIDTH, inLarge.x(), inLarge.width(), SkinFrames.WIDER);
        var down = axisOf(inSmall.y(), inSmall.height(), SkinFrames.HEIGHT, inLarge.y(), inLarge.height(), SkinFrames.HIGHER);
        if (across == null || down == null) {
            return null;
        }

        var layout = new SkinFrames.Layout(across[1], down[1], across[0], down[0], across[3], down[3], across[2], down[2]);
        return new SkinFrames.Piece(
            sprite ? small.graphic : -1,
            small.colour,
            small.transparency,
            small.verticalFlip,
            small.horizontalFlip,
            small.tiled,
            layout
        );
    }

    private static SkinFrames.Layout layoutOf(ScriptReplay.Created created) {
        return new SkinFrames.Layout(
            created.width,
            created.height,
            created.resizeX,
            created.resizeY,
            created.x,
            created.y,
            created.reposX,
            created.reposY
        );
    }

    /**
     * How a component follows the button along one side, from where it stands in the two sizes: its
     * size mode and size, kept or less than the button by a number, and its place mode and place,
     * from the start, centred or from the end; none where it follows in no such way.
     */
    private static int[] axisOf(int place, int length, int box, int widerPlace, int widerLength, int widerBox) {
        int[] size;
        if (length == widerLength) {
            size = new int[] {0, length};
        } else if (box - length == widerBox - widerLength) {
            size = new int[] {1, box - length};
        } else {
            return null;
        }

        var fromEnd = box - place - length;
        var centredBy = place - (box - length) / 2;
        if (place == widerPlace) {
            return new int[] {size[0], size[1], 0, place};
        } else if (fromEnd == widerBox - widerPlace - widerLength) {
            return new int[] {size[0], size[1], 2, fromEnd};
        } else if (centredBy == widerPlace - (widerBox - widerLength) / 2) {
            return new int[] {size[0], size[1], 1, centredBy};
        } else {
            return null;
        }
    }
}
