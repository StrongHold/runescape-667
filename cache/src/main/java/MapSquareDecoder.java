import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.lighttype.LightTypeList;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereTypeList;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxTypeList;
import com.jagex.graphics.TextureSource;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.Js5Archive;
import sun.misc.Unsafe;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads a map square with the client's own readers, and says what they came to.
 *
 * The client reads a square into a region it is about to draw, so its readers lean on a toolkit,
 * the player's options and the config types a square names. The least of each that lets them run
 * is set up here once: the software toolkit, which needs no window and takes no lights, options
 * that turn the lighting detail down, and the light, sky box and sky box sphere types read from
 * the cache.
 */
public final class MapSquareDecoder {

    private static final int LEVELS = TerrainLayout.LEVELS;
    private static final int TILES_ACROSS = TerrainLayout.TILES_ACROSS;
    private static final int ZONES_ACROSS = TILES_ACROSS / 8;

    /**
     * The size of a region that no location falls inside, so that the client reads every one and
     * places none.
     */
    private static final int NO_TILES = 0;

    private final Toolkit toolkit;

    public MapSquareDecoder(File cache) throws Exception {
        this.toolkit = new JavaToolkit((TextureSource) null);
        Static425.toolkit = toolkit;
        ClientOptions.instance = lowDetailOptions();

        var config = Cache.js5(cache, Js5Archive.CONFIG);
        LightTypeList.instance = new LightTypeList(ModeGame.RUNESCAPE, 0, config);
        SkyBoxTypeList.instance = new SkyBoxTypeList(ModeGame.RUNESCAPE, 0, config);
        SkyBoxSphereTypeList.instance = new SkyBoxSphereTypeList(ModeGame.RUNESCAPE, 0, config);
    }

    /**
     * Reads a square's tiles as {@code Terrain.decodeMapSquare} does and then the records after
     * them as {@code MapRegion.decodeStaticEnvironment} does, both on the one packet, as the
     * client reads a square it builds the world from.
     *
     * The tiles are read into an underwater region, which keeps what each tile is made of without
     * touching the collision maps or the flags of the world being built. An underwater region
     * skips the records after the tiles, so those are read into an ordinary one.
     */
    public Map<String, Integer> terrain(byte[] data) {
        var counts = new LinkedHashMap<String, Integer>();
        var packet = new Packet(data);

        var tiles = new MapRegion(LEVELS, TILES_ACROSS, TILES_ACROSS, true);
        tiles.decodeMapSquare(packet, null, 0, 0, 0, 0);
        counts.put("tile bytes", packet.pos);
        counts.putAll(tileSums(tiles));

        Static665.zoneEnvironments = new Environment[ZONES_ACROSS][ZONES_ACROSS];
        var environment = new MapRegion(LEVELS, TILES_ACROSS, TILES_ACROSS, false);
        environment.decodeStaticEnvironment(0, packet, 0, toolkit);
        counts.put("environment", Static665.zoneEnvironments[0][0] == null ? 0 : 1);
        counts.put("camera height levels", cameraHeightLevels(environment));
        return counts;
    }

    /**
     * Reads a square's locations as {@code MapRegion.loadLocations} does, into a region with no
     * tiles, so that none of them is placed and nothing beyond the reader itself runs.
     */
    public static void locations(byte[] data) {
        new MapRegion(LEVELS, NO_TILES, NO_TILES, true).loadLocations(0, 0, null, null, data);
    }

    private static Map<String, Integer> tileSums(MapRegion region) {
        var overlaySum = 0;
        var shapeSum = 0;
        var turnSum = 0;
        var underlaySum = 0;

        for (var level = 0; level < LEVELS; level++) {
            for (var x = 0; x < TILES_ACROSS; x++) {
                for (var z = 0; z < TILES_ACROSS; z++) {
                    overlaySum += region.overlay[level][x][z] & 0xFF;
                    shapeSum += region.tileShapes[level][x][z];
                    turnSum += region.tileDirections[level][x][z];
                    underlaySum += region.underlay[level][x][z] & 0xFF;
                }
            }
        }

        return Map.of("overlay sum", overlaySum, "shape sum", shapeSum, "turn sum", turnSum,
            "underlay sum", underlaySum);
    }

    private static int cameraHeightLevels(MapRegion region) {
        var levels = 0;
        if (region.cameraHeights != null) {
            for (var heights : region.cameraHeights) {
                levels += heights == null ? 0 : 1;
            }
        }
        return levels;
    }

    /**
     * Options with the lighting detail turned down, which is all a square's lighting asks of
     * them. The options are made without their constructor, which asks the machine the client
     * runs on about itself.
     */
    private static ClientOptions lowDetailOptions() {
        var options = allocated(ClientOptions.class);
        options.lightDetail = new LightDetailOption(0, options);
        return options;
    }

    private static <T> T allocated(Class<T> type) {
        try {
            var field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return type.cast(((Unsafe) field.get(null)).allocateInstance(type));
        } catch (ReflectiveOperationException failure) {
            throw new IllegalStateException("Could not allocate a " + type.getSimpleName(), failure);
        }
    }
}
