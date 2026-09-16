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

    public static Ground build(Toolkit toolkit) {
        var heights = heights();
        var ground = toolkit.createGround(TILES, TILES, heights, heights, 0, 0);

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
                heights[x][z] = -(x * 40 + (z % 3) * 90 + (x % 4) * 60);
            }
        }
        return heights;
    }

    private static void addTile(Ground ground, int x, int z) {
        var offsetX = new int[] {0, TILE, TILE, 0};
        var offsetY = new int[] {0, 0, TILE, TILE};
        var faceA = new int[] {0, 0};
        var faceB = new int[] {1, 2};
        var faceC = new int[] {2, 3};

        var colours = new int[FLAT_FACES];
        for (var face = 0; face < FLAT_FACES; face++) {
            colours[face] = hslOf(x, z, face);
        }

        var textures = new int[] {-1, -1};
        var sizes = new int[] {0, 0};

        ground.addTile(x, z, offsetX, null, offsetY, null, faceA, faceB, faceC,
            colours, null, textures, sizes, 0, 0, 0);
    }

    /** A colour packed the way the client packs one: hue, then saturation, then lightness. */
    private static int hslOf(int x, int z, int face) {
        var hue = (x * 5 + z * 3) & 0x3F;
        var saturation = (x + face) % 8;
        var lightness = 40 + ((z * 7 + face * 20) % 80);
        return hue << 10 | saturation << 7 | lightness;
    }

    private HandGround() {
        /* empty */
    }
}
