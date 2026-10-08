import com.jagex.core.constants.LocShapes;
import com.jagex.core.io.Packet;
import com.jagex.core.constants.ModeGame;
import com.jagex.game.collision.CollisionMap;
import com.jagex.game.runetek6.config.lighttype.LightTypeList;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.Ground;
import com.jagex.graphics.ModelAndShadow;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;
import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Builds one map square the way the client builds the world it is about to draw.
 *
 * The client never builds a square on its own: it reads a region of squares around the player,
 * decodes every tile of each, gives the toolkit a ground for each level, places the locations on
 * them, and only then works out the colour of every tile, smoothing the underlays across their
 * neighbours and blending the overlays into them. A tile at the edge of a square takes its colour
 * and the heights of its corners from the map squares beside it, so the map square wanted is built here as
 * the middle of a region of three squares by three, and only the middle one is read back.
 *
 * Everything runs on the software toolkit, which needs no window, with the client's own texture
 * source, and with the options set to what a player on high detail would see: ground blending,
 * textures and ground decorations on, and every location placed whatever level the player is on.
 * The lighting the client bakes into the ground is turned off, so that the colours read back are
 * the colours of the ground itself, as the colours of a model are.
 */
public final class ClientMapSquareReader {

    public static final int TILES_ACROSS = 64;
    public static final int LEVELS = 4;

    /**
     * The region is three squares across, and the map square wanted starts one square in.
     */
    private static final int REGION_TILES = TILES_ACROSS * 3;
    public static final int ORIGIN_TILES = TILES_ACROSS;

    /**
     * How far the scene keeps track of what is drawn around the camera. Nothing is drawn here, so
     * this only sizes some arrays the scene allocates.
     */
    private static final int RENDER_DISTANCE_TILES = 25;

    /**
     * The light a tile is coloured under, as near to full as the ground's light levels go. A light
     * of 128 leaves a colour as it is, and the levels are kept in a signed byte.
     */
    static final byte FULL_LIGHT = 127;

    /**
     * Asks the toolkit for a model that it has not lit, which keeps each face's colour, shading
     * type and normals for the export to read, as {@link ClientNpcReader} explains. 0x800 is what
     * the client asks of every location it places.
     */
    private static final int UNLIT_LOCATION = 0x800 | 0x10000;

    private static final int ON = 1;
    private static final int OFF = 0;
    private static final int HIGH_WATER_DETAIL = 2;

    private static final String NOT_ASKED = "not asked for";

    /**
     * How many tiles beyond the map square's edge the heights are read for. A location at the
     * edge is bent against the corners of every tile it covers, and a large one reaches several
     * tiles into the neighbour.
     */
    public static final int HEIGHT_MARGIN_TILES = 8;

    private final ClientLocReader locs;
    private final ClientModelReader models;
    private final js5 maps;
    private final Path keys;
    private final LightTypeList lightTypes;

    /**
     * How far a posed vertex may be from the client's, in the client's units. The client turns a
     * frame's angles and offsets to suit a placement's rotation before it poses, and an importer
     * poses the asset and turns the result, and the two round the same arithmetic differently by
     * at most one unit, which is a five hundred and twelfth of a tile.
     */
    private static final int POSED_TOLERANCE_FINE = 1;

    /**
     * The asset of each location and shape placed so far, which every placement of it is
     * checked against.
     */
    private final Map<Long, Optional<JavaModel>> assets = new HashMap<>();

    public ClientMapSquareReader(File cache, Path keys) {
        this.locs = new ClientLocReader(cache);
        this.models = locs.models();
        this.maps = Cache.js5(cache, Js5Archive.MAPS);
        this.keys = keys;
        this.lightTypes = new LightTypeList(ModeGame.RUNESCAPE, 0, Cache.js5(cache, Js5Archive.CONFIG));
        ClientOptions.instance = highDetailOptions();
    }

    public Js5TextureSource textures() {
        return models.textures();
    }

    public ClientLocReader locs() {
        return locs;
    }

    /**
     * One square built in the middle of its region.
     *
     * @param grounds the ground the toolkit was given for each level, holding every tile of the
     *     region as the client will draw it.
     * @param heights the height of every tile corner of each level, from one tile before the
     *     map square to one tile after, as [level][x][z], in the client's units.
     * @param underwater the bed under the region's water, where it has one.
     * @param flags the client's flags for every tile of each level, as [level][x][z]: 1 blocks
     *     movement, 2 is a bridge, 4 has its roof removed when the player is under it, 8 counts
     *     as level 0 whatever level it is on, 16 is never drawn, 128 is water ({@code TileFlag}).
     * @param floorShadows for every tile of each level, as [level][x][z], 1 where its floor casts a
     *     hard shadow on the levels below and 0 where it does not ({@code Terrain.loadBlended}).
     * @param placements every location of the map square, on land and under the water, and where
     *     the client draws it.
     * @param locations whether the map square's locations on land were placed.
     * @param environment how the map square is lit, its fog and sky, and the lights on it.
     */
    public record MapSquare(int x, int z, List<JavaGround> grounds, List<RecordingGround> colours, int[][][] heights,
                            int[][][] flags, int[][][] floorShadows, List<Placement> placements,
                            Placing locations, Underwater underwater, EnvironmentDecoder.Environment environment) {
    }

    /**
     * Whether a square's locations were placed.
     */
    public sealed interface Placing {

        record Placed() implements Placing {
        }

        /**
         * @param reason why they were not, such as a key that is missing.
         */
        record NotPlaced(String reason) implements Placing {
        }
    }

    /**
     * What lies under a region's water.
     */
    public sealed interface Underwater {

        /**
         * The region has no world under its water, as most have not.
         */
        record Dry() implements Underwater {
        }

        /**
         * @param ground the ground of the bed, holding every tile of the region as the client will
         *     draw it.
         * @param heights the height of every tile corner of the bed, as the land's are read.
         * @param locations whether the locations on the map square's bed were placed.
         */
        record Bed(JavaGround ground, RecordingGround colours, int[][] heights, Placing locations) implements Underwater {
        }
    }

    /**
     * One location, and where the client draws it, in the region's units: 512 to a tile, x east,
     * y down and z north.
     *
     * @param shape the shape the client builds the location's model as, which is one shape for
     *     every wall decoration and a straight centrepiece for a diagonal one.
     * @param rotation the rotation the client builds it with, which is above 3 for a diagonal.
     * @param part what the client keeps it as: a wall, the second wall of a corner, a wall
     *     decoration, the second decoration of a diagonal wall, a ground decoration, or a location
     *     that stands in the middle of its tiles or across them.
     * @param virtualLevel the level whose ground it is bent against and drawn with, which a
     *     bridge makes differ from the level it is kept on.
     * @param underwater whether it stands on the bed under the water rather than on land.
     * @param sequencesOf the location whose sequences it plays, which for a location that takes
     *     the look of another can be the one it was placed as.
     * @param check whether its asset, placed as {@link LocPlacing} describes, is what the client
     *     builds for it.
     */
    public record Placement(int id, String name, int shape, int rotation, int level, int virtualLevel, String part,
                            boolean underwater, int x, int y, int z, int sequencesOf, Check check) {

        private Placement under(boolean water) {
            return new Placement(id, name, shape, rotation, level, virtualLevel, part, water, x, y, z, sequencesOf,
                check);
        }
    }

    /**
     * Whether a placement's asset, placed by the importer's steps, is what the client builds.
     */
    public enum Check {
        /** Every vertex is where the client puts it, still and at the first frame alike. */
        MATCHES,
        /** The still model differs from the client's. */
        DIFFERS,
        /**
         * The still model matches and the first frame of the first sequence is further from the
         * client's than rounding allows.
         */
        DIFFERS_POSED,
        /** The client builds a model and the asset has none, or the other way about. */
        NO_ASSET
    }

    /**
     * Builds the region around a map square in the order {@code MapBuilder.build} does, and reads
     * the map square back.
     *
     * Where the region has a world under its water, the client reads it as a region of its own of
     * one level, raises it by the heights of the land, and gives the land the heights of the bed
     * to light its water by. The bed is built after the land, against the land's ground, which
     * lifts the bed's overlays to the surface of the water.
     *
     * The ground is built blended, as the client builds it with ground blending on, or with each
     * tile in a colour of its own ({@code Terrain.loadUnblended}), as with it off.
     */
    public MapSquare read(int mapSquareX, int mapSquareZ, boolean withLocations, boolean groundBlending) throws IOException {
        var toolkit = models.toolkit();
        var underwater = hasUnderwater(mapSquareX, mapSquareZ);
        var collisionMaps = scene(toolkit, underwater, groundBlending);
        var region = new MapRegion(LEVELS, REGION_TILES, REGION_TILES, false);
        var environment = decodeTiles(region, collisionMaps, "m", mapSquareX, mapSquareZ);

        MapRegion bed = null;
        if (underwater) {
            switchScene(true);
            bed = new MapRegion(1, REGION_TILES, REGION_TILES, true);
            decodeTiles(bed, null, "um", mapSquareX, mapSquareZ);
            bed.addHeightOffsets(region.tileHeights[0]);
            bed.createGrounds(null, toolkit, null);
            switchScene(false);
        }

        region.createGrounds(bed == null ? null : bed.tileHeights, toolkit, collisionMaps);
        var locations = withLocations
            ? placeLocations(region, collisionMaps, mapSquareX, mapSquareZ)
            : new Placing.NotPlaced(NOT_ASKED);

        for (var ground : Static706.floor) {
            unlit((JavaGround) ground);
        }
        if (bed != null) {
            unlit((JavaGround) Static693.underwaterGround[0]);
        }
        var colours = recordColours(Static706.floor);
        region.load(toolkit, bed == null ? null : Static693.underwaterGround[0], null);
        restoreGrounds(Static706.floor, colours);

        var placements = new ArrayList<Placement>();
        if (locations instanceof Placing.Placed) {
            placements.addAll(placements(toolkit, Static478.floorTiles, false));
        }

        Underwater underwaterWorld = new Underwater.Dry();
        if (bed != null) {
            switchScene(true);
            var bedLocations = withLocations ? placeBedLocations(bed, mapSquareX, mapSquareZ) : new Placing.NotPlaced(NOT_ASKED);
            var bedColours = recordColours(Static693.underwaterGround);
            bed.load(toolkit, null, Static706.floor[0]);
            restoreGrounds(Static693.underwaterGround, bedColours);
            switchScene(false);

            if (bedLocations instanceof Placing.Placed) {
                placements.addAll(placements(toolkit, Static420.underwaterTiles, true));
            }
            underwaterWorld = new Underwater.Bed((JavaGround) Static693.underwaterGround[0], bedColours.getFirst(),
                heights(Static693.underwaterGround[0]), bedLocations);
        }

        var grounds = new ArrayList<JavaGround>();
        var heights = new int[LEVELS][][];
        var flags = new int[LEVELS][][];
        var floorShadows = new int[LEVELS][][];
        for (var level = 0; level < LEVELS; level++) {
            grounds.add((JavaGround) Static706.floor[level]);
            heights[level] = heights(Static706.floor[level]);
            flags[level] = flags(level);
            floorShadows[level] = floorShadows(colours.get(level));
        }
        return new MapSquare(mapSquareX, mapSquareZ, List.copyOf(grounds), colours, heights, flags, floorShadows,
            List.copyOf(placements), locations, underwaterWorld, environment);
    }

    /**
     * Puts a recording ground in front of each of the client's grounds, so that the terrain's
     * colours are kept as it hands them over.
     */
    private static List<RecordingGround> recordColours(Ground[] grounds) {
        var recordings = new ArrayList<RecordingGround>();
        for (var level = 0; level < grounds.length; level++) {
            var recording = new RecordingGround(grounds[level]);
            recordings.add(recording);
            grounds[level] = recording;
        }
        return List.copyOf(recordings);
    }

    /** Puts the client's own grounds back, so that everything after the load sees them. */
    private static void restoreGrounds(Ground[] grounds, List<RecordingGround> recordings) {
        for (var level = 0; level < grounds.length; level++) {
            grounds[level] = recordings.get(level).real();
        }
    }

    /**
     * The client's flags for every tile of the map square on one level, as {@code Terrain.decodeMapSquare}
     * read them into {@code Static280.tileFlags}, which the scene reads to hide levels and roofs.
     */
    private static int[][] flags(int level) {
        var flags = new int[TILES_ACROSS][TILES_ACROSS];
        for (var x = 0; x < TILES_ACROSS; x++) {
            for (var z = 0; z < TILES_ACROSS; z++) {
                flags[x][z] = Static280.tileFlags[level][ORIGIN_TILES + x][ORIGIN_TILES + z] & 0xFF;
            }
        }
        return flags;
    }

    /**
     * For every tile of the map square on one level, 1 where its floor casts a hard shadow and 0
     * where it does not, as the blended terrain handed it to the ground.
     */
    private static int[][] floorShadows(RecordingGround ground) {
        var shadows = new int[TILES_ACROSS][TILES_ACROSS];
        for (var x = 0; x < TILES_ACROSS; x++) {
            for (var z = 0; z < TILES_ACROSS; z++) {
                shadows[x][z] = ground.castsFloorShadow(ORIGIN_TILES + x, ORIGIN_TILES + z) ? 1 : 0;
            }
        }
        return shadows;
    }

    /**
     * The height of every tile corner the map square's locations can be bent against.
     */
    private static int[][] heights(Ground ground) {
        var across = TILES_ACROSS + 2 * HEIGHT_MARGIN_TILES + 1;
        var heights = new int[across][across];
        for (var x = 0; x < across; x++) {
            for (var z = 0; z < across; z++) {
                heights[x][z] = ground.tileHeights[ORIGIN_TILES - HEIGHT_MARGIN_TILES + x][ORIGIN_TILES - HEIGHT_MARGIN_TILES + z];
            }
        }
        return heights;
    }

    /**
     * Points the scene at the world under the water, or back at the land: which tiles the client
     * keeps locations on, and which grounds it builds and places them against.
     */
    private static void switchScene(boolean underwater) {
        Static379.setUnderwater(underwater);
    }

    /**
     * Whether any square of the region has a world under its water, which the client only builds
     * on high water detail.
     */
    private boolean hasUnderwater(int mapSquareX, int mapSquareZ) {
        var any = false;
        for (var across = -1; across <= 1; across++) {
            for (var up = -1; up <= 1; up++) {
                any |= maps.getgroupid("um" + (mapSquareX + across) + "_" + (mapSquareZ + up)) != -1;
            }
        }
        return any;
    }

    /**
     * Sets up the scene the way the client does before it builds a region: the size of the map,
     * its tile flags and collision maps, the arrays of tiles that walls and locations are kept in,
     * and the detail settings that decide how the ground is built, its tiles blended into each
     * other or each in a colour of its own, as the player's ground blending option says.
     */
    private static CollisionMap[] scene(JavaToolkit toolkit, boolean underwater, boolean groundBlending) {
        Static720.mapWidth = REGION_TILES;
        Static501.mapLength = REGION_TILES;
        Static708.resetTileFlags(REGION_TILES, REGION_TILES);

        var collisionMaps = new CollisionMap[LEVELS];
        for (var level = 0; level < LEVELS; level++) {
            collisionMaps[level] = CollisionMap.create(REGION_TILES, REGION_TILES);
        }

        Static21.initScene(toolkit, 1, REGION_TILES, REGION_TILES, RENDER_DISTANCE_TILES, underwater, false);

        Static439.hardShadows = OFF;
        Static428.highMemory = true;
        Static50.highWaterDetail = true;
        Static305.highLightDetail = false;
        Static404.renderShadows = false;
        AnimatedBackground.level = -1;
        Static718.groundBlending = groundBlending;
        Static196.textures = true;
        return collisionMaps;
    }

    /**
     * Reads every tile of the region's map squares as {@code Static73.decodeStaticArea} does. A
     * map square the cache holds no tiles for is the sea, and is given the flat heights the client
     * gives it. The environment that follows the middle map square's tiles in its file is read as
     * well, and a map square the cache holds nothing for gets the client's default.
     *
     * @param prefix what the client names the map squares' groups by: {@code m} for the land and
     *     {@code um} for the world under its water.
     */
    private EnvironmentDecoder.Environment decodeTiles(MapRegion region, CollisionMap[] collisionMaps, String prefix,
                                                       int mapSquareX, int mapSquareZ) {
        var baseX = (mapSquareX - 1) * TILES_ACROSS;
        var baseZ = (mapSquareZ - 1) * TILES_ACROSS;
        var missing = new ArrayList<int[]>();
        var tail = new Packet(new byte[0]);

        for (var across = 0; across < 3; across++) {
            for (var up = 0; up < 3; up++) {
                var data = file(prefix + (mapSquareX - 1 + across) + "_" + (mapSquareZ - 1 + up), null);
                var x = across * TILES_ACROSS;
                var z = up * TILES_ACROSS;

                if (data == null) {
                    missing.add(new int[] {x, z});
                } else {
                    var packet = new Packet(data);
                    region.decodeMapSquare(packet, collisionMaps, x, z, baseX, baseZ);
                    if (across == 1 && up == 1) {
                        tail = packet;
                    }
                }
            }
        }

        for (var square : missing) {
            region.setMapSquareHeights(square[0], square[1]);
        }
        return EnvironmentDecoder.decode(tail, lightTypes, region.tileHeights, ORIGIN_TILES, ORIGIN_TILES);
    }

    /**
     * Places the map square's locations on land as {@code Static338.loadStaticLocations} does, or
     * says why they could not be. They are locked with the map square's key.
     *
     * Only the map square wanted is placed: a location belongs to the map square its first tile is in, and
     * the export takes the map square's locations alone.
     */
    private Placing placeLocations(MapRegion region, CollisionMap[] collisionMaps, int mapSquareX, int mapSquareZ)
            throws IOException {
        var name = "l" + mapSquareX + "_" + mapSquareZ;
        var key = LocationKeys.read(keys, name);
        if (maps.getgroupid(name) == -1) {
            return new Placing.NotPlaced("the cache holds no " + name);
        }

        var data = file(name, key.orElse(null));
        if (data == null) {
            return new Placing.NotPlaced(key.isPresent()
                ? "the key for " + name + " under " + keys + " does not unlock it"
                : "there is no key for " + name + " under " + keys + ", and it is not stored open");
        }

        region.loadLocations(ORIGIN_TILES, ORIGIN_TILES, collisionMaps, models.toolkit(), data);
        return new Placing.Placed();
    }

    /**
     * Places the locations under the map square's water, which the client reads without a key.
     */
    private Placing placeBedLocations(MapRegion bed, int mapSquareX, int mapSquareZ) {
        var name = "ul" + mapSquareX + "_" + mapSquareZ;
        var data = file(name, null);
        if (data == null) {
            return new Placing.NotPlaced("the cache holds no " + name);
        }

        bed.loadLocations(ORIGIN_TILES, ORIGIN_TILES, null, models.toolkit(), data);
        return new Placing.Placed();
    }

    /**
     * File 0 of a group of the maps, unlocked with a key as {@code js5.getfile} does, or null
     * where the cache holds no such group or the key does not fit it.
     */
    private byte[] file(String name, int[] key) {
        var group = maps.getgroupid(name);
        if (group == -1) {
            return null;
        }

        try {
            return maps.getfile(key, 0, group);
        } catch (RuntimeException wrongKey) {
            return null;
        }
    }

    /**
     * Lights every corner of a ground fully and casts no shadow on it. The ground works out how
     * much light falls on each corner from the slope and the toolkit's sun when it is made, and
     * multiplies every colour it is given by that. The export lights the ground where it is shown,
     * from its normals, as it does a model.
     */
    private static void unlit(JavaGround ground) {
        for (var column : ground.lightLevels) {
            Arrays.fill(column, FULL_LIGHT);
        }
        for (var column : ground.shadowLevels) {
            Arrays.fill(column, (byte) 0);
        }
    }

    /**
     * Every location of the map square, read from the tiles the client keeps them on. A location that
     * covers several tiles is held by each, and is read once.
     */
    private List<Placement> placements(JavaToolkit toolkit, Tile[][][] tiles, boolean underwater) {
        var placements = new ArrayList<Placement>();
        Set<Object> seen = Collections.newSetFromMap(new IdentityHashMap<>());

        for (var level = 0; level < tiles.length; level++) {
            for (var x = ORIGIN_TILES; x < ORIGIN_TILES + TILES_ACROSS; x++) {
                for (var z = ORIGIN_TILES; z < ORIGIN_TILES + TILES_ACROSS; z++) {
                    var tile = tiles[level][x][z];
                    if (tile != null) {
                        placementsOn(tile, toolkit, seen).forEach(placement -> placements.add(placement.under(underwater)));
                    }
                }
            }
        }

        return List.copyOf(placements);
    }

    private List<Placement> placementsOn(Tile tile, JavaToolkit toolkit, Set<Object> seen) {
        var placements = new ArrayList<Placement>();

        wall(tile.wall, "wall", toolkit).ifPresent(placements::add);
        wall(tile.adjacentWall, "corner wall", toolkit).ifPresent(placements::add);
        wallDecor(tile.wallDecor, "wall decoration", toolkit).ifPresent(placements::add);
        wallDecor(tile.wallDecor2, "second wall decoration", toolkit).ifPresent(placements::add);
        groundDecor(tile.groundDecor, toolkit).ifPresent(placements::add);

        for (var node = tile.head; node != null; node = node.node) {
            if (seen.add(node.entity)) {
                standing(node.entity, toolkit).ifPresent(placements::add);
            }
        }

        return placements;
    }

    private Optional<Placement> wall(Wall wall, String part, JavaToolkit toolkit) {
        return switch (wall) {
            case null -> Optional.empty();
            case StaticWall held -> placement(held.id, held.shape, held.rotation, held.level, held.virtualLevel, part,
                held.x, held.y, held.z, held.underwater, model(held.modelAndShadow(toolkit, UNLIT_LOCATION, false)));
            case DynamicWall moving -> dynamic(moving.entity, part, moving.x, moving.y, moving.z, toolkit);
            default -> throw new IllegalStateException("A wall of a kind the client never places: " + wall.getClass());
        };
    }

    private Optional<Placement> wallDecor(WallDecor decor, String part, JavaToolkit toolkit) {
        return switch (decor) {
            case null -> Optional.empty();
            case StaticWallDecor held -> placement(held.id, held.shape, held.rotation, held.level, held.virtualLevel,
                part, held.x + held.offsetX, held.y, held.z + held.offsetZ, held.underwater,
                model(held.modelAndShadow(toolkit, UNLIT_LOCATION, false)));
            case DynamicWallDecor moving -> dynamic(moving.entity, part,
                moving.x + moving.offsetX, moving.y, moving.z + moving.offsetZ, toolkit);
            default -> throw new IllegalStateException("A wall decoration of a kind the client never places: "
                + decor.getClass());
        };
    }

    private Optional<Placement> groundDecor(GroundDecor decor, JavaToolkit toolkit) {
        return switch (decor) {
            case null -> Optional.empty();
            case StaticGroundDecor held -> placement(held.id, LocShapes.GROUNDDECOR, held.rotation, held.level,
                held.virtualLevel, "ground decoration", held.x, held.y, held.z, held.underwater,
                model(held.modelAndShadow(UNLIT_LOCATION, toolkit, false)));
            case DynamicGroundDecor moving -> dynamic(moving.entity, "ground decoration", moving.x, moving.y,
                moving.z, toolkit);
            default -> throw new IllegalStateException("A ground decoration of a kind the client never places: "
                + decor.getClass());
        };
    }

    private Optional<Placement> standing(PositionEntity entity, JavaToolkit toolkit) {
        return switch (entity) {
            case StaticLocation held -> placement(held.id, held.shape, held.rotation, held.level, held.virtualLevel,
                "location", held.x, held.y, held.z, held.underwater,
                model(held.modelAndShadow(toolkit, false, UNLIT_LOCATION)));
            case DynamicLocation moving -> dynamic(moving.entity, "location", moving.x, moving.y, moving.z, toolkit);
            default -> Optional.empty();
        };
    }

    /**
     * A location that the client animates, or that takes the look of another by a variable.
     *
     * A location that takes the look of another does so with every variable at 0, which is how
     * the client stands before the server sends any. The sequences it plays are those of the look
     * it has taken, or its own where that look has none, as {@code LocEntity.animate} picks them.
     * It is built as {@code LocEntity.model} builds it while nothing is playing, through
     * {@code LocType.modelAndShadow}, which bends it to the ground under it as for any other
     * location.
     */
    private Optional<Placement> dynamic(LocEntity entity, String part, int x, int y, int z, JavaToolkit toolkit) {
        var own = LocTypeList.instance.list(entity.id);
        var type = own.multiloc == null ? own : own.getMultiLoc(TimedVarDomain.instance);
        if (type == null) {
            return Optional.empty();
        }

        var floor = LocGround.floor(entity.underwater, entity.virtualLevel);
        var ceiling = LocGround.ceiling(entity.underwater, entity.virtualLevel);
        var diagonal = entity.shape == LocShapes.CENTREPIECE_DIAGONAL;
        var groundY = floor.averageHeight(entity.entity.x, entity.entity.z);
        var built = type.modelAndShadow(diagonal ? entity.rotation + 4 : entity.rotation, entity.entity.z,
            entity.entity.x, floor, false, groundY, diagonal ? LocShapes.CENTREPIECE_STRAIGHT : entity.shape, toolkit,
            null, UNLIT_LOCATION, ceiling);
        var sequencesOf = type.hasAnimations() ? type : own.hasAnimations() ? own : type;
        return placement(type.id, entity.shape, entity.rotation, entity.level, entity.virtualLevel, part, x, y, z,
            entity.underwater, model(built), sequencesOf.id, groundY, entity.entity.x, entity.entity.z);
    }

    private static JavaModel model(ModelAndShadow built) {
        return built == null ? null : (JavaModel) built.model;
    }

    private Optional<Placement> placement(int id, int shape, int rotation, int level, int virtualLevel, String part,
                                          int x, int y, int z, boolean underwater, JavaModel model) {
        return placement(id & 0xFFFF, shape, rotation, level, virtualLevel, part, x, y, z, underwater, model,
            id & 0xFFFF, y, x, z);
    }

    /**
     * @param model the model the client builds for the placement, or null where it builds none.
     * @param groundY the height the client bends the model against, which an animated location
     *     measures at its own tile rather than where it is drawn.
     * @param groundX where the client bends the model at, which an animated wall decoration
     *     measures at the wall rather than where it is drawn.
     */
    private Optional<Placement> placement(int id, int shape, int rotation, int level, int virtualLevel, String part,
                                          int x, int y, int z, boolean underwater, JavaModel model, int sequencesOf,
                                          int groundY, int groundX, int groundZ) {
        var type = LocTypeList.instance.list(id);
        var builtShape = LocShapes.isWallDecor(shape) ? LocShapes.WALLDECOR_STRAIGHT_NOOFFSET
            : shape == LocShapes.CENTREPIECE_DIAGONAL ? LocShapes.CENTREPIECE_STRAIGHT : shape;
        var builtRotation = shape == LocShapes.CENTREPIECE_DIAGONAL ? rotation + 4 : rotation;
        var check = check(type, builtShape, builtRotation, underwater, virtualLevel, groundX, groundY, groundZ, model);

        if (model == null) {
            return Optional.empty();
        } else {
            return Optional.of(new Placement(id, type.name, builtShape, builtRotation, level, virtualLevel, part,
                underwater, x, y, z, sequencesOf, check));
        }
    }

    /**
     * Places the location's asset as an importer would and compares it with what the client
     * built, still and, for a location that animates, at the first frame of its first sequence.
     *
     * The client hands out a posed model from a pool that the next pose fills again, so the
     * client's is copied out before the asset is posed.
     */
    private Check check(LocType type, int shape, int rotation, boolean underwater, int virtualLevel, int x, int y,
                        int z, JavaModel built) {
        var turned = rotation > 3 && ClientLocReader.needsTurnedAsset(type, shape);
        var asset = assets.computeIfAbsent((long) type.id << 9 | shape << 1 | (turned ? 1 : 0),
            ignored -> Optional.ofNullable(locs.poser(type, shape, turned).still()));
        if (asset.isEmpty() || built == null) {
            return asset.isEmpty() && built == null ? Check.MATCHES : Check.NO_ASSET;
        }

        var floor = LocGround.floor(underwater, virtualLevel);
        var ceiling = LocGround.ceiling(underwater, virtualLevel);
        var scaled = ClientLocReader.scaledInAsset(type);
        var placed = LocPlacing.place(asset.get(), type, shape, rotation, floor, ceiling, x, y, z, turned, scaled);
        if (!LocPlacing.sameVertices(placed, built, 0)) {
            return Check.DIFFERS;
        }

        if (type.hasAnimations() && type.anim[0] != -1) {
            var animator = new SequenceAnimator(type.anim[0]);
            if (animator.show(0)) {
                var pooled = (JavaModel) type.wallModel(rotation, z, shape, x, ceiling, animator, locs.toolkit(),
                    floor, null, UNLIT_LOCATION, y);
                var clientPosed = pooled == null ? null : (JavaModel) pooled.copy((byte) 0, ClientLocReader.EVERY_FUNCTION, true);
                animator.show(0);
                var assetPosed = locs.poser(type, shape, turned).posed(animator);
                var placedPosed = LocPlacing.place(assetPosed, type, shape, rotation, floor, ceiling, x, y, z, turned,
                    scaled);
                if (clientPosed == null || !LocPlacing.sameVertices(placedPosed, clientPosed, POSED_TOLERANCE_FINE)) {
                    return Check.DIFFERS_POSED;
                }
            }
        }
        return Check.MATCHES;
    }

    /**
     * Options as a player on high detail has them. They are made without their constructor, which
     * asks the machine the client runs on about itself, and only the options that building a
     * region reads are set. Placing a location starts its animation at a random frame, and a frame
     * that plays a sound asks how loud sounds are, so they are turned off.
     */
    private static ClientOptions highDetailOptions() {
        var options = allocated(ClientOptions.class);
        options.animateBackground = new AnimateBackgroundOption(ON, options);
        options.textures = new TextureQuality(ON, options);
        options.groundDecor = new GroundDecorOption(ON, options);
        options.groundBlending = new GroundBlendingOption(ON, options);
        options.hardShadows = new HardShadowsOption(OFF, options);
        options.lightDetail = new LightDetailOption(OFF, options);
        options.waterDetail = new WaterDetailOption(HIGH_WATER_DETAIL, options);
        options.backgroundSoundVolume = new VolumeOption(OFF, options);
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
