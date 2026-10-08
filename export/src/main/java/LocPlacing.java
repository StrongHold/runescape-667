import com.jagex.core.constants.LocShapes;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.graphics.Ground;

/**
 * What an importer does to a location's asset to place it as the client does, written with the
 * client's own model operations so that it can be checked against the client for every
 * placement of a map square.
 *
 * <p>The steps, in order, in the client's units (512 to a tile, x east, y down, z north):
 * <ol>
 * <li>An L-shaped wall placed with a rotation above 3 is mirrored along z.</li>
 * <li>A wall decoration placed with a rotation above 3 is turned 45 degrees about y and moved by
 *     (180, 0, -180). A wall decoration that animates has a second asset for that, turned already,
 *     because the client turns it before the frames of its sequence move it, and the frames are
 *     not turned with it. That asset is only moved.</li>
 * <li>The model is turned about y by a quarter turn for each of the rotation's low two bits.</li>
 * <li>It is scaled by the type's resize, over 128, along each axis of the world. An animated
 *     location is scaled in its asset already, and its extras say so.</li>
 * <li>It is moved by the type's offsets.</li>
 * <li>A centrepiece placed with a rotation above 3, which the map placed as a diagonal, is turned
 *     45 degrees about y.</li>
 * <li>Where the type says so, it is bent to the ground under it, as {@code JavaModel.p} does from
 *     the heights of the tile corners it covers.</li>
 * <li>It is moved by the type's translation.</li>
 * <li>It stands at the placement's position.</li>
 * </ol>
 *
 * <p>A turn about y by a quarter turn takes (x, z) to (z, -x) in the client's frame, and a turn
 * of 45 degrees is the same with the client's sine and cosine of 2048 out of 16384.
 */
public final class LocPlacing {

    private static final int FULL_SCALE = 128;
    public static final int EIGHTH_TURN = 2048;
    private static final int QUARTER_TURN = 4096;
    private static final int WALL_DECORATION_TURNED_X_FINE = 180;
    private static final int WALL_DECORATION_TURNED_Z_FINE = -180;

    /**
     * Places a copy of an asset as the client places it, by the client's own operations.
     *
     * @param shape the shape the client builds the model as, after it has mapped every wall
     *     decoration shape to one and a diagonal centrepiece to a straight one.
     * @param rotation the rotation the client builds it with, which is above 3 for a diagonal.
     * @param y the height of the ground at the placement, which the bend measures from.
     * @param turned whether the asset is the one turned 45 degrees already.
     * @param scaled whether the asset is scaled already, as an animated location's is.
     */
    public static JavaModel place(JavaModel asset, LocType type, int shape, int rotation, Ground floor, Ground ceiling,
                                  int x, int y, int z, boolean turned, boolean scaled) {
        var model = (JavaModel) asset.copy((byte) 0, ClientLocReader.EVERY_FUNCTION, true);

        if (shape == LocShapes.WALL_L && rotation > 3) {
            model.v();
        }
        if (shape == LocShapes.WALLDECOR_STRAIGHT_NOOFFSET && rotation > 3) {
            if (!turned) {
                model.k(EIGHTH_TURN);
            }
            model.H(WALL_DECORATION_TURNED_X_FINE, 0, WALL_DECORATION_TURNED_Z_FINE);
        }
        if ((rotation & 3) != 0) {
            model.k((rotation & 3) * QUARTER_TURN);
        }
        if (!scaled && (type.resizex != FULL_SCALE || type.resizey != FULL_SCALE || type.resizez != FULL_SCALE)) {
            model.O(type.resizex, type.resizey, type.resizez);
        }
        if (type.xoff != 0 || type.yoff != 0 || type.zoff != 0) {
            model.H(type.xoff, type.yoff, type.zoff);
        }
        if (shape == LocShapes.CENTREPIECE_STRAIGHT && rotation > 3) {
            model.a(EIGHTH_TURN);
        }
        if (type.hillchange != 0 && (floor != null || ceiling != null)) {
            model.p(type.hillchange, type.hillskew, floor, ceiling, x, y, z);
        }
        if (type.translateX != 0 || type.translateY != 0 || type.translateZ != 0) {
            model.H(type.translateX, type.translateY, type.translateZ);
        }
        return model;
    }

    /**
     * Whether two models hold every vertex in the same place, to within a tolerance along each
     * axis.
     */
    public static boolean sameVertices(JavaModel a, JavaModel b, int tolerance) {
        if (a.vertexCount != b.vertexCount) {
            return false;
        }
        for (var i = 0; i < a.vertexCount; i++) {
            if (Math.abs(a.vertexX[i] - b.vertexX[i]) > tolerance || Math.abs(a.vertexY[i] - b.vertexY[i]) > tolerance
                    || Math.abs(a.vertexZ[i] - b.vertexZ[i]) > tolerance) {
                return false;
            }
        }
        return true;
    }

    private LocPlacing() {
        /* empty */
    }
}
