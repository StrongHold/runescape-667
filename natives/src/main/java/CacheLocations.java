import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.FileOnDisk;
import com.jagex.core.io.Packet;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5Index;
import com.jagex.js5.js5;

import java.io.File;

/**
 * Lists what the map says stands on one tile of the world.
 *
 * A picture the client draws wrongly names a place rather than a model, and the map square that
 * place falls in says which locations stand there. Each of them names a model, and a model can be
 * put in front of both toolkits and judged.
 *
 * The map holds one group of locations per square of sixty four tiles, named for the square.
 */
public final class CacheLocations {

    private static final int TILES_ACROSS_A_SQUARE = 64;

    public static void main(String[] args) throws Exception {
        var x = Integer.parseInt(args[0]);
        var z = Integer.parseInt(args[1]);
        var level = args.length > 2 ? Integer.parseInt(args[2]) : 0;

        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        var name = "l" + (x / TILES_ACROSS_A_SQUARE) + "_" + (z / TILES_ACROSS_A_SQUARE);
        var index = readIndex(cache, Js5Archive.MAPS);
        var group = index.groupNameTable.find(StringTools.intHashCp1252(name));

        if (group < 0) {
            System.out.println("the cache holds no " + name);
            return;
        }

        var packed = mapStore(cache).read(index.groupIds[group]);
        if (packed == null) {
            System.out.println(name + " is named but not held");
            return;
        }

        System.out.println(name + " group " + index.groupIds[group]);

        var data = unlocked(packed);
        if (data == null) {
            System.out.println("the group is locked with a key this cache does not hold");
            return;
        }

        report(data, x % TILES_ACROSS_A_SQUARE, z % TILES_ACROSS_A_SQUARE, level);
    }

    /**
     * The group as it stands, or unlocked with a key of nothing.
     *
     * The map locks its locations with a key the server hands the client at the door, so a cache
     * on its own cannot always read them back. A server that never bothered leaves the key as
     * nothing, which is still a key and still has to be turned.
     */
    private static byte[] unlocked(byte[] packed) {
        try {
            return js5.decodeContainer(packed);
        } catch (RuntimeException plain) {
            var packet = new Packet(packed);
            packet.tinydec(new int[4], packed.length);

            try {
                return js5.decodeContainer(packet.data);
            } catch (RuntimeException locked) {
                return null;
            }
        }
    }

    /**
     * Walks the group the way the client walks it: a run of locations, each holding a run of the
     * places it stands, both counted from the last rather than given outright.
     */
    private static void report(byte[] data, int wantX, int wantZ, int wantLevel) {
        var packet = new Packet(data);
        var id = -1;
        var found = 0;

        while (true) {
            var idOffset = packet.gExtended1or2();
            if (idOffset == 0) {
                System.out.println(found + " locations stand on " + wantX + "," + wantZ
                    + " at level " + wantLevel);
                return;
            }

            id += idOffset;
            var coord = 0;

            while (true) {
                var coordOffset = packet.gsmart();
                if (coordOffset == 0) {
                    break;
                }

                coord += coordOffset - 1;
                var shapeAndRotation = packet.g1();

                if ((coord & 0x3F) == wantZ && (coord >> 6 & 0x3F) == wantX
                    && coord >> 12 == wantLevel) {
                    found++;
                    System.out.println("  location " + id
                        + " shape " + (shapeAndRotation >> 2)
                        + " rotation " + (shapeAndRotation & 0x3));
                }
            }
        }
    }

    private static FileSystem_Client mapStore(File cache) throws Exception {
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

    private CacheLocations() {
        /* empty */
    }
}
