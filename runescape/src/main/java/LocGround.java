import com.jagex.graphics.Ground;

/**
 * Picks the ground meshes a static loc is built against: the one it stands on, and the one directly above it, which
 * roofs and upper storeys are clipped to. The top level has nothing above it.
 */
public final class LocGround {

    private static final int TOP_LEVEL = 3;

    private LocGround() {
    }

    public static Ground floor(boolean underwater, int level) {
        if (underwater) {
            return Static693.underwaterGround[level];
        } else {
            return Static706.floor[level];
        }
    }

    public static Ground ceiling(boolean underwater, int level) {
        if (underwater) {
            return Static706.floor[0];
        } else if (level < TOP_LEVEL) {
            return Static706.floor[level + 1];
        } else {
            return null;
        }
    }
}
