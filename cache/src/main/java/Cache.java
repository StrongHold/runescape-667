import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.FileOnDisk;
import com.jagex.core.io.Packet;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Index;
import com.jagex.js5.js5;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * The cache on disk, read the way the client reads it but without a client.
 *
 * The client reaches its cache through a provider that talks to a server for anything it is
 * missing. Nothing here is missing, so the files are opened directly and the server half is left
 * out.
 */
public final class Cache {

    /** What the client opens its own master index with. */
    private static final int MASTER_ARCHIVE = 255;

    private static final String WHERE_THE_CLIENT_KEEPS_IT = ".jagex_cache_32/runescape";

    private static final int DATA_BUFFER = 5200;
    private static final int INDEX_BUFFER = 6000;
    private static final int GROUP_LIMIT = 500000;

    /**
     * Where the client keeps its cache, which is the same place on every machine it runs on.
     */
    public static File standard() {
        return new File(System.getProperty("user.home"), WHERE_THE_CLIENT_KEEPS_IT);
    }

    /** Reads one archive's index, which the master index holds under the archive's own number. */
    public static Js5Index index(File cache, int archive) throws Exception {
        byte[] packed = open(cache, MASTER_ARCHIVE).read(archive);
        if (packed == null) {
            throw new IllegalStateException("The cache holds no index for archive " + archive + ".");
        }
        return new Js5Index(packed, Packet.getcrc(packed.length, packed), null);
    }

    /** Reads one group out of an archive, undoing the compression it is held under. */
    public static byte[] group(File cache, int archive, int group) throws Exception {
        byte[] packed = open(cache, archive).read(group);
        if (packed == null) {
            throw new IllegalStateException(
                "Archive " + archive + " of the cache holds no group " + group + ".");
        }
        return js5.decodeContainer(packed);
    }

    /**
     * Every file of a group, by its number.
     *
     * The sizes of the files are written after them rather than before, each as how much longer
     * it is than the one before, and how many times over that is written is the very last byte.
     */
    public static Map<Integer, byte[]> split(byte[] data, Js5Index index, int group) {
        var count = index.fileCounts[group];
        var ids = index.fileIds[group];

        if (count <= 1) {
            return Map.of(ids == null ? 0 : ids[0], data);
        }

        var at = data.length - 1;
        var blocks = data[at] & 0xFF;
        at -= blocks * count * 4;

        var sizes = new int[count];
        var packet = new Packet(data);
        packet.pos = at;

        for (var block = 0; block < blocks; block++) {
            var size = 0;
            for (var file = 0; file < count; file++) {
                size += packet.g4();
                sizes[file] += size;
            }
        }

        var held = new byte[count][];
        for (var file = 0; file < count; file++) {
            held[file] = new byte[sizes[file]];
        }

        packet.pos = at;
        var into = new int[count];
        var from = 0;

        for (var block = 0; block < blocks; block++) {
            var size = 0;
            for (var file = 0; file < count; file++) {
                size += packet.g4();
                System.arraycopy(data, from, held[file], into[file], size);
                into[file] += size;
                from += size;
            }
        }

        var files = new LinkedHashMap<Integer, byte[]>();
        for (var file = 0; file < count; file++) {
            files.put(ids == null ? file : ids[file], held[file]);
        }
        return files;
    }

    /** The number of every group an archive holds. */
    public static int[] groupsOf(Js5Index index) {
        return index.groupIds != null ? index.groupIds : IntStream.range(0, index.groupCount).toArray();
    }

    /**
     * The stores opened so far, one for each archive of each cache. Opening a store opens its
     * files, so a tool that reads many groups reads them all through one.
     */
    private static final Map<String, FileSystem_Client> OPENED = new ConcurrentHashMap<>();

    private static FileSystem_Client open(File cache, int archive) throws Exception {
        var key = cache.getAbsolutePath() + "#" + archive;
        var opened = OPENED.get(key);
        if (opened == null) {
            opened = openFresh(cache, archive);
            OPENED.put(key, opened);
        }
        return opened;
    }

    private static FileSystem_Client openFresh(File cache, int archive) throws Exception {
        var data = new FileOnDisk(new File(cache, "main_file_cache.dat2"), "r", Long.MAX_VALUE);
        var table = new FileOnDisk(new File(cache, "main_file_cache.idx" + archive), "r", Long.MAX_VALUE);

        return new FileSystem_Client(
            archive,
            new BufferedFile(data, DATA_BUFFER, 0),
            new BufferedFile(table, INDEX_BUFFER, 0),
            GROUP_LIMIT
        );
    }

    private Cache() {
        /* empty */
    }
}
