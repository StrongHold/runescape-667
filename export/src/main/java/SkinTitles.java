import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The title and the close button that an interface places over a frame, found where they stand
 * when the interface is laid out through its layers in the client's fixed window, whatever layer
 * holds them, and measured from the box that the frame's corners stand in, as the frame's parts
 * are. An ornate window holds its frame, its title and its close button each in a layer of its own,
 * and one (1122) holds its frame in a layer beside the other two.
 *
 * A title is a line of text centred across the frame near its top: a text component, shown and
 * centred across, that stands as far in from the one side as from the other, starts no higher than
 * a few pixels above the frame and ends within the reach of the frame's top corners, and stands
 * above every other text over the frame. So the title of 890 in the dark band between its stone
 * border and its divider is a title, and so is the title of 405, which has no divider; a label in a
 * column, a message in the middle of a box, or a line of text in a thin frame that has no room for a
 * title is not.
 *
 * A close button is a sprite near the top right of the frame that the player closes it by: one
 * whose hover hook swaps it for another sprite by script 44, or a stack of sprites at one place,
 * one of them with an option, where the first is the button, another shows under the pointer, as
 * the hover hooks of the ornate windows fade one in (scripts 4209 and 4214), and where it has one, a
 * third shows while the button is held down (script 4207).
 */
final class SkinTitles {

    /**
     * How far down from the top of the frame the close button may stand, as {@code SkinWindows}
     * takes it.
     */
    private static final int TOP_BAND = 32;

    /**
     * The hook a sprite button runs to swap its sprite, with the sprite under the pointer as its
     * third value.
     */
    private static final int SWAP_SPRITE = 44;

    /**
     * The text alignment of a centred line ({@code Component.textAlignX}).
     */
    private static final int CENTRED = 1;

    /**
     * A component shown over the frame and where it stands in the client's window.
     */
    private record Placed(Component component, SkinFrames.Rect rect) {
    }

    private final List<Placed> placed;
    private final SkinFrames.Rect frame;
    private final int topReach;

    private SkinTitles(List<Placed> placed, SkinFrames.Rect frame, int topReach) {
        this.placed = placed;
        this.frame = frame;
        this.topReach = topReach;
    }

    /**
     * The components shown over the frame of these parts, in the order the client draws them and
     * where they stand, with the box that the frame's corners stand in. The frame's box is a layer
     * that stands where it does in the client's window, or the window itself, and what stands over
     * the frame is what the layer around the box holds, the box's own layers among it, or where the
     * box is in no layer, what the box holds. A component is shown where it and every layer it is in
     * within that layer are shown, but for the frame's box, which a script may show with the frame.
     */
    static SkinTitles in(Map<Integer, Component> components, ComponentLayout layout, int box, ComponentLayout.Box layer, List<Map<String, Object>> parts) {
        var corners = parts.stream().filter(SkinFrames::isCorner).toList();
        var left = SkinFrames.marginOf("Left", "x", corners);
        var top = SkinFrames.marginOf("top", "y", corners);
        var right = SkinFrames.marginOf("Right", "x", corners);
        var bottom = SkinFrames.marginOf("bottom", "y", corners);
        var frame = new SkinFrames.Rect(layer.x() + left, layer.y() + top, layer.width() - left - right, layer.height() - top - bottom);
        var topReach = SkinFrames.reachOf("top", corners) - top;

        var around = box == -1 || layout.parentOf(box) == -1 ? box : layout.parentOf(box);
        var placed = new ArrayList<Placed>();
        for (var id : SkinFrames.inDrawingOrder(components, layout)) {
            var at = layout.boxOf(id);
            if (at != null && isShownIn(id, around, box, components, layout)) {
                placed.add(new Placed(components.get(id), new SkinFrames.Rect(at.x(), at.y(), at.width(), at.height())));
            }
        }
        return new SkinTitles(placed, frame, topReach);
    }

    /**
     * Whether a component is in a layer and shown in it: it and every layer it is in within that
     * layer are shown, but for the frame's box.
     */
    private static boolean isShownIn(int id, int layer, int box, Map<Integer, Component> components, ComponentLayout layout) {
        var at = id;
        while (at != layer && at != -1 && components.get(at) != null && (at == box || !components.get(at).hidden)) {
            at = layout.parentOf(at);
        }
        return at == layer && id != layer;
    }

    /**
     * The title of the frame, as a window writes its title: its colour, font, whether it has a
     * shadow, and its place from the sides and the top of the frame; null where it has none.
     */
    Map<String, Object> title() {
        for (var each : placed) {
            var component = each.component();
            if (component.type == Component.TYPE_TEXT && component.textAlignX == CENTRED && isTitle(each)) {
                var title = new LinkedHashMap<String, Object>();
                title.put("colour", component.colour);
                title.put("font", component.fontGraphic);
                if (component.textShadow) {
                    title.put("shadow", true);
                }
                title.put("left", each.rect().x() - frame.x());
                title.put("right", rightOf(frame) - rightOf(each.rect()));
                title.put("top", each.rect().y() - frame.y());
                title.put("height", each.rect().height());
                return title;
            }
        }
        return null;
    }

    /**
     * Whether a text stands where a frame's title does: centred across the frame, from no more than
     * a few pixels above its top to within the reach of its top corners, and above every other text
     * over the frame.
     */
    private boolean isTitle(Placed text) {
        var rect = text.rect();
        var left = rect.x() - frame.x();
        var right = rightOf(frame) - rightOf(rect);
        var top = rect.y() - frame.y();
        var centred = Math.abs(left - right) <= SkinFrames.PAIR_SLACK;
        var atTop = top >= -SkinFrames.PAIR_SLACK && top + rect.height() <= topReach;
        var aboveTheRest = placed.stream()
            .filter(other -> other != text && other.component().type == Component.TYPE_TEXT && other.rect().overlaps(frame))
            .allMatch(other -> other.rect().y() >= rect.y() + rect.height());
        return centred && atTop && aboveTheRest;
    }

    /**
     * The area of a frame's heading, where its title stands: its place from the sides and the top
     * of the frame, and its height.
     */
    static Map<String, Object> headingOf(Map<String, Object> title) {
        var heading = new LinkedHashMap<String, Object>();
        heading.put("left", title.get("left"));
        heading.put("top", title.get("top"));
        heading.put("right", title.get("right"));
        heading.put("height", title.get("height"));
        return heading;
    }

    /**
     * The close button of the frame, as a window writes its close button: its sprite, the sprite
     * under the pointer, the sprite while it is held where it has one, and its place from the right
     * and the top of the frame; null where it has none. Of the buttons in the frame's top border, it
     * is the one nearest its top right corner, as an ornate window has buttons to zoom and to pan
     * below its title bar (1111).
     */
    Map<String, Object> close() throws Exception {
        Placed nearest = null;
        Integer nearestHover = null;
        for (var each : placed) {
            var hover = isInTopRight(each) && isSprite(each) ? hoverOf(each) : null;
            if (hover != null && (nearest == null || fromTopRight(each) < fromTopRight(nearest))) {
                nearest = each;
                nearestHover = hover;
            }
        }
        if (nearest == null) {
            return null;
        }

        var close = new LinkedHashMap<String, Object>();
        close.put("sprite", nearest.component().graphic);
        close.put("hover", nearestHover);
        var pressed = stackedOf(nearest, "onClick");
        if (pressed != null && isStack(nearest)) {
            close.put("pressed", pressed);
        }
        close.put("right", rightOf(frame) - rightOf(nearest.rect()));
        close.put("top", nearest.rect().y() - frame.y());
        close.put("width", nearest.rect().width());
        close.put("height", nearest.rect().height());
        return close;
    }

    /**
     * Whether a component stands in the right half of the frame's top border: inside the frame,
     * starting near its top, and ending within the reach of its top corners, as a close button does
     * and a button over what the frame holds does not.
     */
    private boolean isInTopRight(Placed each) {
        var rect = each.rect();
        var top = rect.y() - frame.y();
        var inside = top >= 0 && rightOf(rect) <= rightOf(frame);
        var nearTop = top < TOP_BAND && top + rect.height() <= topReach;
        var nearRight = rect.x() > frame.x() + frame.width() / 2;
        return inside && nearTop && nearRight;
    }

    private int fromTopRight(Placed each) {
        return rightOf(frame) - rightOf(each.rect()) + each.rect().y() - frame.y();
    }

    /**
     * The sprite a button shows under the pointer: the one its hover hook swaps it for by script
     * 44, or where it is the first of a stack of sprites at one place, one of them with an option,
     * the sprite of the stack whose hover hook shows it; null where it has neither.
     */
    private Integer hoverOf(Placed button) throws Exception {
        var over = (Object[]) Component.class.getField("onMouseOver").get(button.component());
        if (over != null && over.length > 2 && Integer.valueOf(SWAP_SPRITE).equals(over[0]) && over[2] instanceof Integer swapped) {
            return swapped;
        } else if (isStack(button)) {
            return stackedOf(button, "onMouseOver");
        } else {
            return null;
        }
    }

    /**
     * Whether a sprite is the first of a stack of sprites at one place, one of which has an option.
     */
    private boolean isStack(Placed button) {
        var stack = placed.stream().filter(other -> isSprite(other) && other.rect().equals(button.rect())).toList();
        return stack.size() > 1 && stack.getFirst() == button && stack.stream().anyMatch(other -> hasOption(other.component()));
    }

    private static boolean isSprite(Placed placed) {
        return placed.component().type == Component.TYPE_GRAPHIC && placed.component().graphic >= 0;
    }

    private static boolean hasOption(Component component) {
        return component.ops != null && java.util.Arrays.stream(component.ops).anyMatch(op -> op != null && !op.isBlank());
    }

    /**
     * The sprite of another sprite at a button's very place that has a hook, or null where there is
     * none.
     */
    private Integer stackedOf(Placed button, String hook) throws Exception {
        for (var other : placed) {
            var hooked = Component.class.getField(hook).get(other.component()) != null;
            if (other != button && isSprite(other) && hooked && other.rect().equals(button.rect())) {
                return other.component().graphic;
            }
        }
        return null;
    }

    private static int rightOf(SkinFrames.Rect rect) {
        return rect.x() + rect.width();
    }
}
