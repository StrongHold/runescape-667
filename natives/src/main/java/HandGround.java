import com.jagex.graphics.Ground;
import com.jagex.graphics.Toolkit;

/**
 * A patch of terrain built here rather than loaded from a map.
 *
 * Terrain is a grid of tiles whose corners are shared, so a height is a property of the grid and
 * not of one tile. The heights here rise and fall across the patch, because a flat patch cannot
 * show a corner taken from the wrong tile or a height read from the wrong array.
 *
 * Each tile is two triangles with its own colour, and the colours run across the patch so that a
 * tile drawn in a neighbour's colour is visible.
 */
public final class HandGround {

    public static final int TILES = 10;

    /** How wide one tile is, which is the only size the client ever asks for. */
    public static final int TILE = 512;

    private static final int FLAT_FACES = 2;

    /**
     * What each tile is handed over carrying, so that one thing can be changed at a time while
     * the reason the shipped toolkit draws no ground at all is looked for.
     */
    private static final int TEXTURE = number("SW3D_GROUND_TEXTURE", -1);
    private static final boolean OVERLAID = number("SW3D_GROUND_OVERLAY", 0) != 0;
    private static final boolean LEVELLED = number("SW3D_GROUND_LEVELS", 0) != 0;

    /**
     * What the ground itself is built asking for. The client works these out from its settings
     * and never asks for nothing, which is what was asked for here.
     */
    private static final boolean FLAT = number("SW3D_GROUND_FLAT", 0) != 0;
    private static final int GROUND_FLAGS = number("SW3D_GROUND_FLAGS", 0);
    private static final int FEATURE_FLAGS = number("SW3D_GROUND_FEATURES", 0);

    private static int number(String name, int fallback) {
        var held = System.getenv(name);
        return held == null || held.isEmpty() ? fallback : Integer.parseInt(held);
    }

    public static Ground build(Toolkit toolkit) {
        var heights = heights();
        var ground = toolkit.createGround(TILES, TILES, heights, heights,
            GROUND_FLAGS, FEATURE_FLAGS);

        for (var x = 0; x < TILES; x++) {
            for (var z = 0; z < TILES; z++) {
                addTile(ground, x, z);
            }
        }

        ground.YA();
        return ground;
    }

    /** Where the ground is at every corner of the grid, which is one more each way than tiles. */
    private static int[][] heights() {
        var heights = new int[TILES + 1][TILES + 1];
        for (var x = 0; x <= TILES; x++) {
            for (var z = 0; z <= TILES; z++) {
                heights[x][z] = FLAT ? 0 : -(x * 40 + (z % 3) * 90 + (x % 4) * 60);
            }
        }
        return heights;
    }

    private static void addTile(Ground ground, int x, int z) {
        var offsetX = new int[] {0, TILE, TILE, 0};
        var offsetY = new int[] {0, 0, TILE, TILE};
        var faceA = new int[] {0, 0};
        var faceB = new int[] {2, 3};
        var faceC = new int[] {1, 2};

        var colours = new int[FLAT_FACES];
        for (var face = 0; face < FLAT_FACES; face++) {
            colours[face] = hslOf(x, z, face);
        }

        var textures = new int[] {TEXTURE, TEXTURE};
        var sizes = new int[] {0, 0};

        var overlay = OVERLAID ? colours.clone() : null;
        var levels = LEVELLED ? new int[] {0, 0, 0, 0} : null;

        ground.addTile(x, z, offsetX, levels, offsetY, null, faceA, faceB, faceC,
            colours, overlay, textures, sizes, 0, 0, 0);
    }

    /** A colour packed the way the client packs one: hue, then saturation, then lightness. */
    private static final int FORCED_COLOUR = number("SW3D_GROUND_COLOUR", -1);

    private static int hslOf(int x, int z, int face) {
        if (FORCED_COLOUR >= 0) {
            return FORCED_COLOUR;
        }

        var hue = (x * 5 + z * 3) & 0x3F;
        var saturation = (x + face) % 8;
        var lightness = 40 + ((z * 7 + face * 20) % 80);
        return hue << 10 | saturation << 7 | lightness;
    }

    private HandGround() {
        /* empty */
    }
}
