import com.jagex.core.constants.LocShapes;
import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.Animator;
import com.jagex.game.collision.CollisionMap;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayTypeList;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.game.runetek6.config.vartype.bit.VarBitTypeListClient;
import com.jagex.game.runetek6.config.vartype.player.VarPlayerTypeListClient;
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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Builds one map square the way the client builds the world it is about to draw.
 *
 * The client never builds a square on its own: it reads a region of squares around the player,
 * decodes every tile of each, gives the toolkit a ground for each level, places the locations on
 * them, and only then works out the colour of every tile, smoothing the underlays across their
 * neighbours and blending the overlays into them. A tile at the edge of a square takes its colour
 * and the heights of its corners from the squares beside it, so the square wanted is built here as
 * the middle of a region of three squares by three, and only the middle one is read back.
 *
 * Everything runs on the software toolkit, which needs no window, with the client's own texture
 * source, and with the options set to what a player on high detail would see: ground blending,
 * textures and ground decorations on, and every location placed whatever level the player is on.
 * The lighting the client bakes into the ground is turned off, so that the colours read back are
 * the colours of the ground itself, as the colours of a model are.
 */
public final class ClientSquareReader {

    public static final int TILES_ACROSS = 64;
    public static final int LEVELS = 4;

    /**
     * The region is three squares across, and the square wanted starts one square in.
     */
    private static final int REGION_TILES = TILES_ACROSS * 3;
    public static final int ORIGIN = TILES_ACROSS;

    /**
     * How far the scene keeps track of what is drawn around the camera. Nothing is drawn here, so
     * this only sizes some arrays the scene allocates.
     */
    private static final int RENDER_DISTANCE = 25;

    /**
     * The light a tile is coloured under, as near to full as the ground's light levels go. A light
     * of 128 leaves a colour as it is, and the levels are kept in a signed byte.
     */
    private static final byte FULL_LIGHT = 127;

    /**
     * Asks the toolkit for a model that it has not lit, which keeps each face's colour, shading
     * type and normals for the export to read, as {@link ClientNpcReader} explains. 0x800 is what
     * the client asks of every location it places.
     */
    private static final int UNLIT_LOCATION = 0x800 | 0x10000;

    private static final int LANGUAGE = 0;
    private static final int ON = 1;
    private static final int OFF = 0;
    private static final int HIGH_WATER_DETAIL = 2;

    private static final String NOT_ASKED = "not asked for";

    private final ClientModelReader models;
    private final js5 maps;
    private final Path keys;

    public ClientSquareReader(File cache, Path keys) {
        this.models = new ClientModelReader(cache);
        this.maps = Cache.js5(cache, Js5Archive.MAPS);
        this.keys = keys;

        ClientOptions.instance = highDetailOptions();
        var config = Cache.js5(cache, Js5Archive.CONFIG);
        FloorOverlayTypeList.instance = new FloorOverlayTypeList(ModeGame.RUNESCAPE, LANGUAGE, config);
        FloorUnderlayTypeList.instance = new FloorUnderlayTypeList(ModeGame.RUNESCAPE, LANGUAGE, config);
        LocTypeList.instance = new LocTypeList(ModeGame.RUNESCAPE, LANGUAGE, true,
            Cache.js5(cache, Js5Archive.CONFIG_LOC), Cache.js5(cache, Js5Archive.MODELS));
        VarBitTypeListClient.instance = new VarBitTypeListClient(ModeGame.RUNESCAPE, LANGUAGE,
            Cache.js5(cache, Js5Archive.CONFIG_STRUCT));
        VarPlayerTypeListClient.instance = new VarPlayerTypeListClient(ModeGame.RUNESCAPE, LANGUAGE, config);
        TimedVarDomain.instance = new TimedVarDomain();
        Animator.setSeqTL(new SeqTypeList(ModeGame.RUNESCAPE, LANGUAGE, Cache.js5(cache, Js5Archive.CONFIG_SEQ),
            Cache.js5(cache, Js5Archive.ANIMS), Cache.js5(cache, Js5Archive.BASES)));
    }

    public Js5TextureSource textures() {
        return models.textures();
    }

    /**
     * One square built in the middle of its region.
     *
     * @param grounds the ground the toolkit was given for each level, holding every tile of the
     *     region as the client will draw it.
     * @param underwater the bed under the region's water, where it has one.
     * @param placements every location of the square, on land and under the water, with the model
     *     the client builds for it and where it is drawn.
     * @param locations whether the square's locations on land were placed.
     */
    public record Square(int x, int z, List<JavaGround> grounds, List<Placement> placements, Placing locations,
                         Underwater underwater) {
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
         * @param locations whether the locations on the square's bed were placed.
         */
        record Bed(JavaGround ground, Placing locations) implements Underwater {
        }
    }

    /**
     * One model of a location, and where the client draws it, in the region's units: 512 to a
     * tile, x east, y down and z north.
     *
     * @param part what the client keeps it as: a wall, the second wall of a corner, a wall
     *     decoration, the second decoration of a diagonal wall, a ground decoration, or a location
     *     that stands in the middle of its tiles or across them.
     * @param underwater whether it stands on the bed under the water rather than on land.
     * @param conformed whether its type bends it to fit the ground under it.
     */
    public record Placement(int id, String name, int shape, int rotation, int level, String part,
                            boolean underwater, int x, int y, int z, JavaModel model, boolean conformed) {

        private Placement under(boolean water) {
            return new Placement(id, name, shape, rotation, level, part, water, x, y, z, model, conformed);
        }
    }

    /**
     * Builds the region around a square in the order {@code MapBuilder.build} does, and reads
     * the square back.
     *
     * Where the region has a world under its water, the client reads it as a region of its own of
     * one level, raises it by the heights of the land, and gives the land the heights of the bed
     * to light its water by. The bed is built after the land, against the land's ground, which
     * lifts the bed's overlays to the surface of the water.
     */
    public Square read(int squareX, int squareZ, boolean withLocations) throws IOException {
        var toolkit = models.toolkit();
        var underwater = hasUnderwater(squareX, squareZ);
        var collisionMaps = scene(toolkit, underwater);
        var region = new MapRegion(LEVELS, REGION_TILES, REGION_TILES, false);
        decodeTiles(region, collisionMaps, "m", squareX, squareZ);

        MapRegion bed = null;
        if (underwater) {
            switchScene(true);
            bed = new MapRegion(1, REGION_TILES, REGION_TILES, true);
            decodeTiles(bed, null, "um", squareX, squareZ);
            bed.addHeightOffsets(region.tileHeights[0]);
            bed.createGrounds(null, toolkit, null);
            switchScene(false);
        }

        region.createGrounds(bed == null ? null : bed.tileHeights, toolkit, collisionMaps);
        var locations = withLocations
            ? placeLocations(region, collisionMaps, squareX, squareZ)
            : new Placing.NotPlaced(NOT_ASKED);

        for (var ground : Static706.floor) {
            unlit((JavaGround) ground);
        }
        if (bed != null) {
            unlit((JavaGround) Static693.underwaterGround[0]);
        }
        region.load(toolkit, bed == null ? null : Static693.underwaterGround[0], null);

        var placements = new ArrayList<Placement>();
        if (locations instanceof Placing.Placed) {
            placements.addAll(placements(toolkit, Static478.aTileArrayArrayArray3, false));
        }

        Underwater underwaterWorld = new Underwater.Dry();
        if (bed != null) {
            switchScene(true);
            var bedLocations = withLocations ? placeBedLocations(bed, squareX, squareZ) : new Placing.NotPlaced(NOT_ASKED);
            bed.load(toolkit, null, Static706.floor[0]);
            switchScene(false);

            if (bedLocations instanceof Placing.Placed) {
                placements.addAll(placements(toolkit, Static420.aTileArrayArrayArray2, true));
            }
            underwaterWorld = new Underwater.Bed((JavaGround) Static693.underwaterGround[0], bedLocations);
        }

        var grounds = Arrays.stream(Static706.floor).map(ground -> (JavaGround) ground).toList();
        return new Square(squareX, squareZ, grounds, List.copyOf(placements), locations, underwaterWorld);
    }

    /**
     * Points the scene at the world under the water, or back at the land: which tiles the client
     * keeps locations on, and which grounds it builds and places them against.
     */
    private static void switchScene(boolean underwater) {
        Static379.method5355(underwater);
    }

    /**
     * Whether any square of the region has a world under its water, which the client only builds
     * on high water detail.
     */
    private boolean hasUnderwater(int squareX, int squareZ) {
        var any = false;
        for (var across = -1; across <= 1; across++) {
            for (var up = -1; up <= 1; up++) {
                any |= maps.getgroupid("um" + (squareX + across) + "_" + (squareZ + up)) != -1;
            }
        }
        return any;
    }

    /**
     * Sets up the scene the way the client does before it builds a region: the size of the map,
     * its tile flags and collision maps, the arrays of tiles that walls and locations are kept in,
     * and the detail settings that decide how the ground is built.
     */
    private static CollisionMap[] scene(JavaToolkit toolkit, boolean underwater) {
        Static720.mapWidth = REGION_TILES;
        Static501.mapLength = REGION_TILES;
        Static708.resetTileFlags(REGION_TILES, REGION_TILES);

        var collisionMaps = new CollisionMap[LEVELS];
        for (var level = 0; level < LEVELS; level++) {
            collisionMaps[level] = CollisionMap.create(REGION_TILES, REGION_TILES);
        }

        Static21.initScene(toolkit, 1, REGION_TILES, REGION_TILES, RENDER_DISTANCE, underwater, false);

        Static439.hardShadows = OFF;
        Static428.highMemory = true;
        Static50.highWaterDetail = true;
        Static305.highLightDetail = false;
        Static404.renderShadows = false;
        AnimatedBackground.level = -1;
        Static718.groundBlending = true;
        Static196.textures = true;
        return collisionMaps;
    }

    /**
     * Reads every tile of the region's squares as {@code Static73.decodeStaticArea} does. A square
     * the cache holds no tiles for is the sea, and is given the flat heights the client gives it.
     *
     * @param prefix what the client names the squares' groups by: {@code m} for the land and
     *     {@code um} for the world under its water.
     */
    private void decodeTiles(MapRegion region, CollisionMap[] collisionMaps, String prefix, int squareX, int squareZ) {
        var baseX = (squareX - 1) * TILES_ACROSS;
        var baseZ = (squareZ - 1) * TILES_ACROSS;
        var missing = new ArrayList<int[]>();

        for (var across = 0; across < 3; across++) {
            for (var up = 0; up < 3; up++) {
                var data = file(prefix + (squareX - 1 + across) + "_" + (squareZ - 1 + up), null);
                var x = across * TILES_ACROSS;
                var z = up * TILES_ACROSS;

                if (data == null) {
                    missing.add(new int[] {x, z});
                } else {
                    region.decodeMapSquare(new Packet(data), collisionMaps, x, z, baseX, baseZ);
                }
            }
        }

        for (var square : missing) {
            region.setMapSquareHeights(square[0], square[1]);
        }
    }

    /**
     * Places the square's locations on land as {@code Static338.loadStaticLocations} does, or
     * says why they could not be. They are locked with the square's key.
     *
     * Only the square wanted is placed: a location belongs to the square its first tile is in, and
     * the export takes the square's locations alone.
     */
    private Placing placeLocations(MapRegion region, CollisionMap[] collisionMaps, int squareX, int squareZ)
            throws IOException {
        var name = "l" + squareX + "_" + squareZ;
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

        region.loadLocations(ORIGIN, ORIGIN, collisionMaps, models.toolkit(), data);
        return new Placing.Placed();
    }

    /**
     * Places the locations under the square's water, which the client reads without a key.
     */
    private Placing placeBedLocations(MapRegion bed, int squareX, int squareZ) {
        var name = "ul" + squareX + "_" + squareZ;
        var data = file(name, null);
        if (data == null) {
            return new Placing.NotPlaced("the cache holds no " + name);
        }

        bed.loadLocations(ORIGIN, ORIGIN, null, models.toolkit(), data);
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
     * Every location of the square, read from the tiles the client keeps them on. A location that
     * covers several tiles is held by each, and is read once.
     */
    private static List<Placement> placements(JavaToolkit toolkit, Tile[][][] tiles, boolean underwater) {
        var placements = new ArrayList<Placement>();
        Set<Object> seen = Collections.newSetFromMap(new IdentityHashMap<>());

        for (var level = 0; level < tiles.length; level++) {
            for (var x = ORIGIN; x < ORIGIN + TILES_ACROSS; x++) {
                for (var z = ORIGIN; z < ORIGIN + TILES_ACROSS; z++) {
                    var tile = tiles[level][x][z];
                    if (tile != null) {
                        placementsOn(tile, toolkit, seen).forEach(placement -> placements.add(placement.under(underwater)));
                    }
                }
            }
        }

        return List.copyOf(placements);
    }

    private static List<Placement> placementsOn(Tile tile, JavaToolkit toolkit, Set<Object> seen) {
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

    private static Optional<Placement> wall(Wall wall, String part, JavaToolkit toolkit) {
        return switch (wall) {
            case null -> Optional.empty();
            case StaticWall held -> placement(held.id, held.shape, held.rotation, held.level, part,
                held.x, held.y, held.z, model(held.modelAndShadow(toolkit, UNLIT_LOCATION, false)));
            case DynamicWall moving -> dynamic(moving.entity, part, moving.x, moving.y, moving.z, toolkit);
            default -> throw new IllegalStateException("A wall of a kind the client never places: " + wall.getClass());
        };
    }

    private static Optional<Placement> wallDecor(WallDecor decor, String part, JavaToolkit toolkit) {
        return switch (decor) {
            case null -> Optional.empty();
            case StaticWallDecor held -> placement(held.id, held.shape, held.rotation, held.level, part,
                held.x + held.aShort101, held.y, held.z + held.aShort102,
                model(held.modelAndShadow(toolkit, UNLIT_LOCATION, false)));
            case DynamicWallDecor moving -> dynamic(moving.entity, part,
                moving.x + moving.aShort101, moving.y, moving.z + moving.aShort102, toolkit);
            default -> throw new IllegalStateException("A wall decoration of a kind the client never places: "
                + decor.getClass());
        };
    }

    private static Optional<Placement> groundDecor(GroundDecor decor, JavaToolkit toolkit) {
        return switch (decor) {
            case null -> Optional.empty();
            case StaticGroundDecor held -> placement(held.id, LocShapes.GROUNDDECOR, held.rotation, held.level,
                "ground decoration", held.x, held.y, held.z, model(held.modelAndShadow(UNLIT_LOCATION, toolkit, false)));
            case DynamicGroundDecor moving -> dynamic(moving.entity, "ground decoration", moving.x, moving.y,
                moving.z, toolkit);
            default -> throw new IllegalStateException("A ground decoration of a kind the client never places: "
                + decor.getClass());
        };
    }

    private static Optional<Placement> standing(PositionEntity entity, JavaToolkit toolkit) {
        return switch (entity) {
            case StaticLocation held -> placement(held.id, held.shape, held.rotation, held.level, "location",
                held.x, held.y, held.z, model(held.modelAndShadow(toolkit, false, UNLIT_LOCATION)));
            case DynamicLocation moving -> dynamic(moving.entity, "location", moving.x, moving.y, moving.z, toolkit);
            default -> Optional.empty();
        };
    }

    /**
     * A location that the client animates, or that takes the look of another by a variable.
     *
     * The client asks its entity for a model every frame, which poses it at whatever frame its
     * animation has reached, and the animation of many starts at a random frame. So the model is
     * built here as {@code LocEntity.model} builds it while nothing is playing, through
     * {@code LocType.modelAndShadow}, which bends it to the ground under it as for any other
     * location. A location that takes the look of another does so with every variable at 0, which
     * is how the client stands before the server sends any.
     */
    private static Optional<Placement> dynamic(LocEntity entity, String part, int x, int y, int z, JavaToolkit toolkit) {
        var type = LocTypeList.instance.list(entity.id);
        if (type.multiloc != null) {
            type = type.getMultiLoc(TimedVarDomain.instance);
        }
        if (type == null) {
            return Optional.empty();
        }

        var floor = LocGround.floor(entity.underwater, entity.virtualLevel);
        var ceiling = LocGround.ceiling(entity.underwater, entity.virtualLevel);
        var diagonal = entity.shape == LocShapes.CENTREPIECE_DIAGONAL;
        var built = type.modelAndShadow(diagonal ? entity.rotation + 4 : entity.rotation, entity.entity.z,
            entity.entity.x, floor, false, floor.averageHeight(entity.entity.x, entity.entity.z),
            diagonal ? LocShapes.CENTREPIECE_STRAIGHT : entity.shape, toolkit, null, UNLIT_LOCATION, ceiling);
        return placement(type.id, entity.shape, entity.rotation, entity.level, part, x, y, z, model(built));
    }

    private static JavaModel model(ModelAndShadow built) {
        return built == null ? null : (JavaModel) built.model;
    }

    private static Optional<Placement> placement(int id, int shape, int rotation, int level, String part,
                                                 int x, int y, int z, JavaModel model) {
        if (model == null) {
            return Optional.empty();
        } else {
            var type = LocTypeList.instance.list(id & 0xFFFF);
            return Optional.of(new Placement(id & 0xFFFF, type.name, shape, rotation, level, part, false, x, y, z, model,
                type.hillchange != 0));
        }
    }

    /**
     * Options as a player on high detail has them. They are made without their constructor, which
     * asks the machine the client runs on about itself, and only the options that building a
     * region reads are set.
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
