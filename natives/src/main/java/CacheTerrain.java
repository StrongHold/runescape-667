import com.beust.jcommander.Parameter;
import com.jagex.core.io.BufferedFile;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayType;
import com.jagex.core.io.FileOnDisk;
import com.jagex.core.io.Packet;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5Index;
import com.jagex.js5.js5;

import java.io.File;

/**
 * Says what the map is made of on one tile: how high it stands, what ground it is, what is laid
 * over it and what shape that overlay is cut to.
 *
 * A picture the client draws wrongly names a place. Where nothing stands on that place, what is
 * wrong is the ground itself, and the ground is what this reads back.
 */
public final class CacheTerrain {

    private static final int TILES_ACROSS_A_SQUARE = 64;

    /** How many levels a square of the map holds. */
    private static final int LEVELS = 4;

    public static final class Args implements Helpable {

        @Parameter(names = "--x", description = "The tile's position from west to east", required = true)
        private int x;

        @Parameter(names = "--z", description = "The tile's position from south to north", required = true)
        private int z;

        @Parameter(names = "--level", description = "Which floor of the world the tile is on")
        private int level;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var args = new Args();

        if (!CommandLine.parsed("listTerrain", args, arguments)) {
            return;
        }

        var wantX = args.x;
        var wantZ = args.z;
        var wantLevel = args.level;

        var cache = Cache.standard();
        var name = "m" + (wantX / TILES_ACROSS_A_SQUARE) + "_" + (wantZ / TILES_ACROSS_A_SQUARE);
        var index = readIndex(cache, Js5Archive.MAPS);
        var group = index.groupNameTable.find(StringTools.intHashCp1252(name));

        if (group < 0) {
            System.out.println("the cache holds no " + name);
            return;
        }

        var packed = store(cache).read(index.groupIds[group]);
        if (packed == null) {
            System.out.println(name + " is named but not held");
            return;
        }

        System.out.println(name + " group " + index.groupIds[group]);
        report(new Packet(js5.decodeContainer(packed)), wantX % TILES_ACROSS_A_SQUARE,
            wantZ % TILES_ACROSS_A_SQUARE, wantLevel);
    }

    /**
     * Walks every tile of the square in the order the client walks them, and says what the wanted
     * one is made of.
     *
     * Every tile has to be read whether it is wanted or not, because a tile is a run of numbered
     * things and where the next one begins is only known once this one has ended.
     */
    private static void report(Packet packet, int wantX, int wantZ, int wantLevel)
            throws Exception {
        for (var level = 0; level < LEVELS; level++) {
            for (var x = 0; x < TILES_ACROSS_A_SQUARE; x++) {
                for (var z = 0; z < TILES_ACROSS_A_SQUARE; z++) {
                    var height = -1;
                    var overlay = 0;
                    var shape = 0;
                    var direction = 0;
                    var flags = 0;
                    var underlay = 0;

                    while (true) {
                        var code = packet.g1();
                        if (code == 0) {
                            break;
                        }

                        if (code == 1) {
                            height = packet.g1();
                            break;
                        }

                        if (code <= 49) {
                            overlay = packet.g1b();
                            shape = (code - 2) / 4;
                            direction = code - 2 & 0x3;
                        } else if (code <= 81) {
                            flags = code - 49;
                        } else {
                            underlay = code - 81;
                        }
                    }

                    if (level == wantLevel && x == wantX && z == wantZ) {
                        System.out.println("tile " + x + "," + z + " at level " + level
                            + ": height " + height
                            + ", underlay " + underlay
                            + ", overlay " + overlay
                            + ", shape " + shape
                            + ", turned " + direction
                            + ", flags " + flags);

                        describeUnderlay(underlay);
                        if (overlay != 0) {
                            describeOverlay(overlay);
                        }
                    }
                }
            }
        }
    }

    /** Which group of the configuration each kind of floor is held in. */
    private static final int UNDERLAY_GROUP = 1;
    private static final int OVERLAY_GROUP = 4;

    private static void describeUnderlay(int id) throws Exception {
        var data = configFile(UNDERLAY_GROUP, id);
        if (data == null) {
            System.out.println("  the cache holds no underlay " + id);
            return;
        }

        var type = new FloorUnderlayType();
        type.decode(new Packet(data));
        System.out.println("  underlay " + id + ": colour " + type.colour
            + ", texture " + type.texture + ", size " + type.size
            + ", occludes " + type.occludes + ", allowShadow " + type.allowShadow);
    }

    private static void describeOverlay(int id) throws Exception {
        var data = configFile(OVERLAY_GROUP, id);
        System.out.println("  overlay " + id + ": "
            + (data == null ? "not held" : data.length + " bytes"));
    }

    private static byte[] configFile(int group, int id) throws Exception {
        var cache = Cache.standard();
        var index = CacheLocType.readIndex(cache, Js5Archive.CONFIG);
        var packed = CacheLocType.store(cache, Js5Archive.CONFIG).read(group);

        return packed == null
            ? null
            : CacheLocType.fileFrom(js5.decodeContainer(packed), index, group, id);
    }

    private static FileSystem_Client store(File cache) throws Exception {
        var data = new FileOnDisk(new File(cache, "main_file_cache.dat2"), "r", Long.MAX_VALUE);
        var index = new FileOnDisk(
            new File(cache, "main_file_cache.idx" + Js5Archive.MAPS), "r", Long.MAX_VALUE);

        return new FileSystem_Client(Js5Archive.MAPS,
            new BufferedFile(data, 5200, 0), new BufferedFile(index, 6000, 0), 1 << 22);
    }

    private static Js5Index readIndex(File cache, int archive) throws Exception {
        var data = new FileOnDisk(new File(cache, "main_file_cache.dat2"), "r", Long.MAX_VALUE);
        var master = new FileOnDisk(
            new File(cache, "main_file_cache.idx255"), "r", Long.MAX_VALUE);

        var store = new FileSystem_Client(255,
            new BufferedFile(data, 5200, 0), new BufferedFile(master, 6000, 0), 500000);

        var packed = store.read(archive);
        if (packed == null) {
            throw new IllegalStateException("The cache holds no index for archive " + archive + ".");
        }

        return new Js5Index(packed, Packet.getcrc(packed.length, packed), null);
    }

    private CacheTerrain() {
        /* empty */
    }
}
