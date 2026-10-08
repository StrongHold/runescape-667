import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Toolkit;
import com.jagex.math.Trig1;

import java.util.Optional;

/**
 * Builds the shadow the client draws under an NPC when spot shadows are on, as
 * {@code ShadowList.model} builds it: a flat disc of three rings about a middle vertex, each ring
 * one colour and one alpha between the type's inner and outer ones, stretched to the NPC's
 * bounds across and along, and moved to their middle.
 *
 * The client asks the toolkit for a lit copy, and the software toolkit drops a face's colour when
 * it lights it, so the disc is built here through the same calls with a function mask that keeps
 * it unlit. The geometry, the colours and the alphas are the client's.
 */
final class SpotShadow {

    /**
     * Each ring's radius before the disc is stretched, of which the outer one is the scale the
     * client stretches by: 128 is the NPC's half width.
     */
    private static final int[] RING_RADII_FINE = {64, 96, 128};

    private static final int FULL_SCALE = 128;

    /**
     * How many sides the rings have for an NPC one tile across, and how many more for each tile
     * more, up to five tiles.
     */
    private static final int LEAST_SIDES = 9;
    private static final int SIDES_A_TILE = 3;
    private static final int LARGEST_SIZE_TILES = 5;

    /**
     * A turn in the client's angle units, which its sine and cosine tables are indexed by.
     */
    private static final int TURN = 16384;
    private static final int TRIG_SHIFT = 14;

    /**
     * The parts of the client's 16 bit colour, hue, saturation and lightness, which are blended
     * one by one.
     */
    private static final int HUE = 0xFC00;
    private static final int SATURATION = 0x380;
    private static final int LIGHTNESS = 0x7F;

    /**
     * A face the client shades flat, with no texture and no texture space.
     */
    private static final byte FLAT = 1;
    private static final short NO_TEXTURE = -1;
    private static final byte NO_SPACE = -1;

    /**
     * The functions the client builds the disc with (2055: moving and scaling its vertices), and
     * the one that keeps it unlit.
     */
    private static final int FUNCTIONS = 2055 | 0x10000;
    private static final int TEXTURES_ON = 0;
    private static final int AMBIENT = 64;
    private static final int CONTRAST = 768;

    /**
     * The disc under an NPC whose still model is given, or nothing where its type casts none.
     */
    static Optional<JavaModel> of(NPCType type, JavaModel body, Toolkit toolkit) {
        if (!type.hasShadow) {
            return Optional.empty();
        } else {
            return Optional.of(disc(type, body, toolkit));
        }
    }

    private static JavaModel disc(NPCType type, JavaModel body, Toolkit toolkit) {
        var sides = LEAST_SIDES + SIDES_A_TILE * (Math.min(Math.max(type.size, 1), LARGEST_SIZE_TILES) - 1);
        var mesh = new Mesh(sides * RING_RADII_FINE.length + 1, sides * (RING_RADII_FINE.length * 2 - 1), 0);
        var middle = mesh.addVertex(0, 0, 0);
        var rings = new int[RING_RADII_FINE.length][sides];
        for (var ring = 0; ring < RING_RADII_FINE.length; ring++) {
            for (var side = 0; side < sides; side++) {
                var angle = side * TURN / sides;
                var across = Trig1.COS[angle] * RING_RADII_FINE[ring] >> TRIG_SHIFT;
                var along = Trig1.SIN[angle] * RING_RADII_FINE[ring] >> TRIG_SHIFT;
                rings[ring][side] = mesh.addVertex(across, 0, along);
            }
        }

        var innerAlpha = type.shadowInnerAlpha & 0xFF;
        var outerAlpha = type.shadowOuterAlpha & 0xFF;
        var innerColour = type.shadowInnerColour & 0xFFFF;
        var outerColour = type.shadowOuterColour & 0xFFFF;
        for (var ring = 0; ring < RING_RADII_FINE.length; ring++) {
            var inner = (ring * 256 + 128) / RING_RADII_FINE.length;
            var outer = 256 - inner;
            var alpha = (byte) (innerAlpha * inner + outerAlpha * outer >> 8);
            var colour = (short) (blend(innerColour, outerColour, inner, outer, LIGHTNESS)
                | blend(innerColour, outerColour, inner, outer, SATURATION)
                | blend(innerColour, outerColour, inner, outer, HUE));
            for (var side = 0; side < sides; side++) {
                var next = (side + 1) % sides;
                if (ring == 0) {
                    mesh.addFace(middle, rings[0][side], rings[0][next], colour, NO_TEXTURE, alpha, FLAT, NO_SPACE);
                } else {
                    var within = rings[ring - 1];
                    var without = rings[ring];
                    mesh.addFace(within[side], without[next], within[next], colour, NO_TEXTURE, alpha, FLAT, NO_SPACE);
                    mesh.addFace(within[side], without[side], without[next], colour, NO_TEXTURE, alpha, FLAT, NO_SPACE);
                }
            }
        }

        var disc = (JavaModel) toolkit.createModel(mesh, FUNCTIONS, TEXTURES_ON, AMBIENT, CONTRAST);
        disc.O(body.RA() - body.V() >> 1, FULL_SCALE, body.G() - body.HA() >> 1);
        disc.H(body.RA() + body.V() >> 1, 0, body.G() + body.HA() >> 1);
        return disc;
    }

    /**
     * One part of the two colours, weighed by how much of each a ring takes out of 256.
     */
    private static int blend(int inner, int outer, int innerWeight, int outerWeight, int part) {
        return ((inner & part) * innerWeight + (outer & part) * outerWeight & part << 8) >> 8;
    }

    private SpotShadow() {
        /* empty */
    }
}
