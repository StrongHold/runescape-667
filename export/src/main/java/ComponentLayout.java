import java.util.Map;

/**
 * Where the components of an interface stand, laid out through each layer they are in with the
 * client's rules ({@code InterfaceManager.resize}, {@code reposition}) in the client's fixed window.
 * A component names its layer by the layer's number in the low half of its id as decoded; the
 * client fills the interface's number in when it loads the interface.
 */
final class ComponentLayout {

    /**
     * The size of the game's window the components are laid out in, the client's fixed window.
     */
    private static final int SCREEN_WIDTH = 765;
    private static final int SCREEN_HEIGHT = 503;

    private static final int CHILD_MASK = 0xFFFF;

    /**
     * Where a component stands in the window, and its size.
     */
    record Box(int x, int y, int width, int height) {
    }

    private final Map<Integer, Component> components;

    /**
     * The layout of an interface's components, by their ids.
     */
    ComponentLayout(Map<Integer, Component> components) {
        this.components = components;
    }

    /**
     * Where a component stands and its size, or null where a layer it is in is not of its interface
     * or a rule is one the export does not take.
     */
    Box boxOf(int id) {
        var component = components.get(id);
        if (component == null) {
            return null;
        }
        var parent = layerBoxOf(id);
        if (parent == null) {
            return null;
        }
        var width = length(component.resizeModeX, component.originalWidth, parent.width());
        var height = length(component.resizeModeY, component.originalHeight, parent.height());
        var x = place(component.reposModeX, component.originalX, width, parent.width());
        var y = place(component.reposModeY, component.originalY, height, parent.height());
        if (width == Integer.MIN_VALUE || height == Integer.MIN_VALUE || x == Integer.MIN_VALUE || y == Integer.MIN_VALUE) {
            return null;
        }
        return new Box(parent.x() + x, parent.y() + y, width, height);
    }

    /**
     * Where the layer a component is in stands and its size, the whole window for a component in no
     * layer, or null where {@link #boxOf} gives none for the layer.
     */
    Box layerBoxOf(int id) {
        var component = components.get(id);
        if (component == null) {
            return null;
        }
        return component.layer == -1 ? new Box(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT) : boxOf(parentOf(id));
    }

    /**
     * The id of the layer a component is in, or -1 for none.
     */
    int parentOf(int id) {
        var component = components.get(id);
        return component == null || component.layer == -1 ? -1 : (id & ~CHILD_MASK) | (component.layer & CHILD_MASK);
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
            case 3 -> (value * box) >> 14;
            default -> Integer.MIN_VALUE;
        };
    }
}
