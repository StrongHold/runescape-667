import com.jagex.graphics.Ground;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.Toolkit;

import java.util.Arrays;

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
        return build(toolkit, TEXTURE, 0);
    }

    /**
     * A patch whose tiles wear a texture, which the plain one does not.
     *
     * The size a tile gives its texture decides how much of the texture one tile covers, so a
     * patch built here gives the near half of it one size and the far half another, and a strip
     * down the middle none at all. One picture then shows a tile wearing a texture, a tile
     * wearing the same texture at another size, and a tile wearing none.
     */
    public static Ground buildTextured(Toolkit toolkit) {
        return build(toolkit, TEXTURED_WITH, TILE, FEATURE_FLAGS | TEXTURES_TURNED_OFF);
    }

    /**
     * The texture a textured patch wears on its near half, which the player is not allowed to
     * turn off, and the one it wears on its far half, which they are.
     */
    private static final int TEXTURED_WITH = 6;

    /** A second texture the player may not turn off either, so a corner can differ from its own. */
    private static final int TEXTURED_BESIDE = 4;

    /**
     * A texture that says it carries on round in neither direction, which a model would hold at
     * its edge. The ground carries every texture round whatever the texture says.
     */
    private static final int TEXTURED_ROUND = 12;

    /** What the client hands over for a corner of the ground that has no colour of its own. */
    private static final int NO_COLOUR = -1;

    /** The most the client ever asks a corner of the ground to be darkened by. */
    private static final int DARKENED_MOST = 30;

    /**
     * The colour laid over a whole face, which is a red the corner colours never reach so that a
     * face drawn with it cannot be mistaken for one drawn without.
     */
    private static final int OVERLAID_WITH = (0 << 10) | (7 << 7) | 60;
    private static final int TEXTURED_WITH_ONE_THAT_MAY_GO = 7;

    /** What the client asks the ground for when the player has turned textures off. */
    private static final int TEXTURES_TURNED_OFF = 0x20;

    private static Ground build(Toolkit toolkit, int texture, int size) {
        return build(toolkit, texture, size, FEATURE_FLAGS);
    }

    /**
     * A patch whose tiles are cut into four about a corner in the middle of each.
     *
     * The client cuts a tile up wherever one kind of ground meets another, and a corner of a face
     * is then not a corner of the grid. How the ground faces there has to be worked out from the
     * four corners around it; a patch whose corners all sit on the grid never asks the question.
     */
    public static Ground buildShaped(Toolkit toolkit) {
        var heights = heights();
        var ground = toolkit.createGround(TILES, TILES, heights, heights,
            GROUND_FLAGS, FEATURE_FLAGS);

        for (var x = 0; x < TILES; x++) {
            for (var z = 0; z < TILES; z++) {
                addCutTile(ground, x, z);
            }
        }

        ground.YA();
        return ground;
    }

    /**
     * A patch whose corners each carry a colour of their own.
     *
     * The client hands its tiles over one corner at a time rather than one face at a time, and
     * gives every corner the colour the ground is at that corner, so the colour runs smoothly from
     * one tile into the next. A patch handed over face by face gives all three corners of a face
     * the same colour, and no amount of that ever asks whether a face is shaded across.
     */
    public static Ground buildSmooth(Toolkit toolkit) {
        return buildCornerLit(toolkit, false, FEATURE_FLAGS, false);
    }

    /**
     * The same patch with a colour laid over each face as well as a colour at every corner.
     *
     * The client hands over both for every tile of its terrain. One is blended across the corners
     * and the other belongs to the face, and where a face carries one it is what the face is
     * drawn as. Nothing else here hands over the second, so this is the only scene that says what
     * becomes of it.
     */
    /**
     * The same patch with the corners of a tile naming different textures from one another.
     *
     * The client gives every corner of the ground the texture of whatever it stands nearest, so
     * the corners of a face disagree wherever one kind of ground meets another, and a face whose
     * corners disagree is blended from all three rather than drawn with one of them.
     */
    public static Ground buildBlended(Toolkit toolkit) {
        return buildCornerLit(toolkit, false, FEATURE_FLAGS, true);
    }

    public static Ground buildOverlaid(Toolkit toolkit) {
        return buildCornerLit(toolkit, true, FEATURE_FLAGS, false);
    }

    /**
     * The same patch with something standing on it throwing a shadow across the middle.
     *
     * The client puts the shadow of everything that stands on the ground onto the ground itself,
     * and the ground carries the sum of them and darkens what it draws by it. Nothing else here
     * puts one down, so the whole of that was drawn by neither toolkit until now.
     *
     * One is put down on every other tile, at three heights between them, because how far a
     * shadow slides depends on how far above the ground the thing throwing it stands.
     */
    public static Ground buildShadowed(Toolkit toolkit, Shadow shadow) {
        var ground = buildCornerLit(toolkit, false, FEATURE_FLAGS | TAKES_SHADOWS, false);

        if (shadow != null) {
            for (var x = 1; x < TILES; x += 2) {
                for (var z = 1; z < TILES; z += 2) {
                    /*
                     * Every third one is thrown from higher up, because how far a shadow slides
                     * depends on how far above the ground the thing throwing it stands.
                     */
                    var height = (x + z) % 3 * HIGH_UP;
                    ground.CA(shadow, x * TILE, height, z * TILE, 0, false);
                }
            }
        }

        return ground;
    }

    /** How far above the ground the second and third shadows are thrown from. */
    private static final int HIGH_UP = 256;

    /**
     * The feature the client asks the ground for when it wants what stands on it to throw a
     * shadow onto it. Ground not asked for this way keeps no shadow at all.
     */
    private static final int TAKES_SHADOWS = 0x10;

    private static Ground buildCornerLit(Toolkit toolkit, boolean overlaid, int features,
            boolean blended) {
        var heights = heights();
        var ground = toolkit.createGround(TILES, TILES, heights, heights,
            GROUND_FLAGS, features);

        /*
         * The client darkens the ground under and around everything that stands on it and casts a
         * shadow, corner by corner, before it hands any tile over. A block of corners here is
         * darkened by as much as the client ever asks for and a second by half of it.
         */
        for (var x = 2; x < 6; x++) {
            for (var z = 2; z < 6; z++) {
                ground.ka(x, z, x < 4 ? DARKENED_MOST : DARKENED_MOST / 2);
            }
        }

        for (var x = 0; x < TILES; x++) {
            for (var z = 0; z < TILES; z++) {
                addCornerLitTile(ground, x, z, overlaid, blended);
            }
        }

        ground.YA();
        return ground;
    }

    /** Which corner of the tile each of the six slots of its two faces stands at. */
    private static final int[] SLOT_ACROSS = {0, TILE, TILE, 0, 0, TILE};
    private static final int[] SLOT_ALONG = {0, TILE, 0, 0, TILE, TILE};

    private static void addCornerLitTile(Ground ground, int x, int z, boolean overlaid,
            boolean blended) {
        var slots = SLOT_ACROSS.length;
        var across = new int[slots];
        var along = new int[slots];
        var colours = new int[slots];
        var textures = new int[slots];
        var sizes = new int[slots];
        var overlay = overlaid ? new int[slots] : null;

        for (var slot = 0; slot < slots; slot++) {
            across[slot] = SLOT_ACROSS[slot];
            along[slot] = SLOT_ALONG[slot];
            /*
             * Every tile but one column wears a texture, so that a smoothly coloured patch is
             * covered the way the ground the client bands light and dark across is covered. The
             * bare column leaves the same patch drawn both ways in one picture.
             */
            /*
             * The client picks a corner's texture from the ground the corner stands nearest, so
             * the corners of one tile disagree wherever one kind of ground meets another. A tile
             * whose corners all agree says nothing about which of them the face takes its own
             * from.
             */
            textures[slot] = x == TILES / 2 ? -1
                : !blended ? TEXTURED_WITH
                : x == TILES / 4 ? TEXTURED_ROUND
                : (x + z + SLOT_ACROSS[slot] / TILE + SLOT_ALONG[slot] / TILE) % 2 == 0
                    ? TEXTURED_WITH : TEXTURED_BESIDE;
            /*
             * A tile naming a size smaller than itself is covered by its texture more than once,
             * which is the only way a place on the ground runs off the far edge of one.
             */
            /*
             * A corner names how wide its texture is laid as well as which one it is, and the
             * client lets the corners of a face disagree about both at once.
             */
            sizes[slot] = !blended ? TILE
                : x == TILES / 4 ? TILE / 2
                : (SLOT_ACROSS[slot] + SLOT_ALONG[slot]) % (TILE * 2) == 0 ? TILE / 4 : TILE;
            /*
             * A corner the client gives no colour to is a corner with no ground under it, which
             * is what it hands over at the mouth of a stairwell and anywhere else the floor opens
             * onto the one below. One tile in nine here has one, and a whole row of them has
             * nothing but, because a tile with no ground anywhere on it is what the client hands
             * over for the hole itself.
             */
            colours[slot] = (x + z) % 3 == 1 && slot % 2 == 0 || z == TILES - 2
                ? NO_COLOUR
                : cornerHsl(x + SLOT_ACROSS[slot] / TILE, z + SLOT_ALONG[slot] / TILE);

            if (overlay != null) {
                /*
                 * The near half of the patch carries a colour of its own over every face and the
                 * far half carries none, so one picture holds a face drawn each way.
                 */
                overlay[slot] = z < TILES / 2 ? OVERLAID_WITH : -1;
            }
        }

        ground.U(x, z, across, null, along, null, colours, overlay, textures, sizes,
            0, 0, 0, false);
    }

    /** A colour that belongs to a corner of the grid rather than to a face, so it is shared. */
    private static int cornerHsl(int x, int z) {
        var hue = (x * 3 + z * 5) & 0x3F;
        var saturation = (x + z) % 8;
        var lightness = 30 + ((x * 9 + z * 11) % 90);
        return hue << 10 | saturation << 7 | lightness;
    }

    /** How many faces a tile cut about its middle has. */
    private static final int CUT_FACES = 4;

    /** A texture apiece saying the face wears none. */
    private static int[] bare() {
        var none = new int[CUT_FACES];
        Arrays.fill(none, -1);
        return none;
    }

    private static void addCutTile(Ground ground, int x, int z) {
        var offsetX = new int[] {0, TILE, TILE, 0, TILE / 2};
        var offsetY = new int[] {0, 0, TILE, TILE, TILE / 2};

        var faceA = new int[] {0, 1, 2, 3};
        var faceB = new int[] {4, 4, 4, 4};
        var faceC = new int[] {1, 2, 3, 0};

        var colours = new int[CUT_FACES];
        for (var face = 0; face < CUT_FACES; face++) {
            colours[face] = hslOf(x, z, face);
        }

        ground.addTile(x, z, offsetX, null, offsetY, null, faceA, faceB, faceC,
            colours, null, bare(), new int[CUT_FACES], 0, 0, 0);
    }

    private static Ground build(Toolkit toolkit, int texture, int size, int features) {
        var heights = heights();
        var ground = toolkit.createGround(TILES, TILES, heights, heights,
            GROUND_FLAGS, features);

        for (var x = 0; x < TILES; x++) {
            for (var z = 0; z < TILES; z++) {
                addTile(ground, x, z, texture, size);
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

    private static void addTile(Ground ground, int x, int z, int texture, int size) {
        var wears = texture == -1 || x == TILES / 2 ? -1
            : z < TILES / 2 ? texture : TEXTURED_WITH_ONE_THAT_MAY_GO;

        /*
         * The two halves either side of the bare strip give their texture different sizes, so one
         * picture holds a tile covered by the whole of a texture and one covered by four of it.
         */
        var across = x < TILES / 2 ? size : size / 2;

        var offsetX = new int[] {0, TILE, TILE, 0};
        var offsetY = new int[] {0, 0, TILE, TILE};
        var faceA = new int[] {0, 0};
        var faceB = new int[] {2, 3};
        var faceC = new int[] {1, 2};

        var colours = new int[FLAT_FACES];
        for (var face = 0; face < FLAT_FACES; face++) {
            colours[face] = hslOf(x, z, face);
        }

        var textures = new int[] {wears, wears};
        var sizes = new int[] {across, across};

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
