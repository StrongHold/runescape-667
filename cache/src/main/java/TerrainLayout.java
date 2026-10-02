import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Walks a map square's terrain the way {@code Terrain.decodeMapSquare} and then
 * {@code MapRegion.decodeStaticEnvironment} read it, keeping only where each section starts and
 * how far it is read.
 *
 * The tiles come first, every level from west to east and south to north, each a run of codes
 * that ends at a code of 0, or at a code of 1 and the height after it. A code up to 49 is followed
 * by an overlay and says its shape and turn, a code up to 81 is the tile's settings, and any code
 * above that is the underlay. The tiles declare no size, so only reading every one of them says
 * where they end.
 *
 * What follows the tiles is read until the data runs out, as records that each start with a
 * code: the sun, ambient light and fog of the square, the lights placed on it, how much it blooms,
 * its sky box, and how high the camera keeps above it. The client stops on any code it does not
 * know, so there is no terminator and the last record has to end exactly at the end of the data.
 */
public final class TerrainLayout {

    public static final int LEVELS = 4;
    public static final int TILES_ACROSS = 64;

    private static final int CODE_END = 0;
    private static final int CODE_HEIGHT = 1;
    private static final int LAST_OVERLAY_CODE = 49;
    private static final int LAST_SETTINGS_CODE = 81;

    private static final int RECORD_LIGHTING = 0;
    private static final int RECORD_LIGHTS = 1;
    private static final int RECORD_BLOOM = 2;
    private static final int RECORD_SKY_BOX = 128;
    private static final int RECORD_CAMERA_HEIGHTS = 129;

    /**
     * The light preset that is not built in, whose parameters a light type supplies instead.
     */
    private static final int PRESET_FROM_LIGHT_TYPE = 31;

    private static final int CAMERA_HEIGHTS_SET = 1;
    private static final int CAMERA_HEIGHTS_COPIED = 2;

    /**
     * Camera heights are given for blocks of four by four tiles, so a square holds sixteen by
     * sixteen of them.
     */
    private static final int CAMERA_HEIGHT_BLOCKS = (TILES_ACROSS / 4) * (TILES_ACROSS / 4);

    public static Layout.Walk walk(byte[] data) {
        var layout = new Layout(data);
        var counts = new LinkedHashMap<String, Integer>();

        try {
            var tiles = layout.open("tiles");
            walkTiles(tiles, counts);
            layout.close(tiles);
            counts.put("tile bytes", tiles.pos());

            var environment = layout.rest("environment");
            var unknown = walkEnvironment(environment, counts);
            if (unknown != null) {
                return new Layout.Walk(counts, unknown);
            }
        } catch (Layout.Overrun overrun) {
            return new Layout.Walk(counts, overrun.mismatch());
        }

        return new Layout.Walk(counts, layout.check().orElse(null));
    }

    /**
     * Reads every tile, and sums what each tile is left holding once its codes are read, which is
     * what the client keeps of it: a later code of the same kind replaces an earlier one.
     */
    private static void walkTiles(Layout.Cursor tiles, Map<String, Integer> counts) {
        var heights = 0;
        var overlaySum = 0;
        var shapeSum = 0;
        var turnSum = 0;
        var underlaySum = 0;

        for (var level = 0; level < LEVELS; level++) {
            for (var x = 0; x < TILES_ACROSS; x++) {
                for (var z = 0; z < TILES_ACROSS; z++) {
                    var overlay = 0;
                    var shape = 0;
                    var turn = 0;
                    var underlay = 0;
                    var ended = false;

                    while (!ended) {
                        var code = tiles.g1();
                        if (code == CODE_END) {
                            ended = true;
                        } else if (code == CODE_HEIGHT) {
                            tiles.g1();
                            heights++;
                            ended = true;
                        } else if (code <= LAST_OVERLAY_CODE) {
                            overlay = tiles.g1();
                            shape = (code - 2) / 4;
                            turn = code - 2 & 0x3;
                        } else if (code > LAST_SETTINGS_CODE) {
                            underlay = (code - LAST_SETTINGS_CODE) & 0xFF;
                        }
                    }

                    overlaySum += overlay;
                    shapeSum += shape;
                    turnSum += turn;
                    underlaySum += underlay;
                }
            }
        }

        counts.put("heights", heights);
        counts.put("overlay sum", overlaySum);
        counts.put("shape sum", shapeSum);
        counts.put("turn sum", turnSum);
        counts.put("underlay sum", underlaySum);
    }

    /**
     * Reads every record after the tiles, and answers the first code the client does not know,
     * or null when there is none.
     */
    private static Layout.Mismatch walkEnvironment(Layout.Cursor environment, Map<String, Integer> counts) {
        var lights = 0;
        var lightTypes = 0;
        var settings = 0;
        var cameraLevels = new TreeSet<Integer>();
        Layout.Mismatch unknown = null;

        while (unknown == null && environment.more()) {
            var at = environment.pos();
            var code = environment.g1();
            if (code == RECORD_LIGHTING) {
                lighting(environment);
                settings++;
            } else if (code == RECORD_LIGHTS) {
                var count = environment.g1();
                for (var light = 0; light < count; light++) {
                    if (light(environment)) {
                        environment.g2();
                        lightTypes++;
                    }
                }
                lights += count;
            } else if (code == RECORD_BLOOM) {
                environment.g1();
                environment.g1();
                environment.g1();
                settings++;
            } else if (code == RECORD_SKY_BOX) {
                for (var value = 0; value < 5; value++) {
                    environment.g2();
                }
                settings++;
            } else if (code == RECORD_CAMERA_HEIGHTS) {
                cameraHeights(environment, cameraLevels);
            } else {
                unknown = new Layout.Mismatch("environment holds a code the client does not know",
                    "code " + code + " at " + at);
            }
        }

        counts.put("lights", lights);
        counts.put("lights from a light type", lightTypes);
        counts.put("environment", settings > 0 ? 1 : 0);
        counts.put("camera height levels", cameraLevels.size());
        return unknown;
    }

    /**
     * The sun, ambient light and fog, each part present only when its flag is set.
     */
    private static void lighting(Layout.Cursor environment) {
        var flags = environment.g1();
        if ((flags & 0x1) != 0) {
            environment.g4();
        }
        if ((flags & 0x2) != 0) {
            environment.g2();
        }
        if ((flags & 0x4) != 0) {
            environment.g2();
        }
        if ((flags & 0x8) != 0) {
            environment.g2();
        }
        if ((flags & 0x10) != 0) {
            environment.g2();
            environment.g2();
            environment.g2();
        }
        if ((flags & 0x20) != 0) {
            environment.g4();
        }
        if ((flags & 0x40) != 0) {
            environment.g2();
        }
        if ((flags & 0x80) != 0) {
            for (var face = 0; face < 6; face++) {
                environment.g2();
            }
        }
    }

    /**
     * One light, as {@code EnvironmentLight} reads it: one span for each row of the square of
     * tiles its radius covers. Answers whether a light type follows it.
     */
    private static boolean light(Layout.Cursor environment) {
        environment.g1();
        environment.g2();
        environment.g2();
        environment.g2();
        var radius = environment.g1();
        for (var row = 0; row < radius * 2 + 1; row++) {
            environment.g2();
        }
        environment.g2();
        var packed = environment.g1();
        return (packed & 0x1F) == PRESET_FROM_LIGHT_TYPE;
    }

    /**
     * A mode for each level. Only a level whose heights are set carries them, and a level set or
     * copied from the one below is one the client keeps heights for.
     */
    private static void cameraHeights(Layout.Cursor environment, TreeSet<Integer> levels) {
        for (var level = 0; level < LEVELS; level++) {
            var mode = environment.g1b();
            if (mode == CAMERA_HEIGHTS_SET) {
                for (var block = 0; block < CAMERA_HEIGHT_BLOCKS; block++) {
                    environment.g1b();
                }
                levels.add(level);
            } else if (mode == CAMERA_HEIGHTS_COPIED) {
                levels.add(level);
            }
        }
    }

    private TerrainLayout() {
        /* empty */
    }
}
