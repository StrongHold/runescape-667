import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.FileOnDisk;
import com.jagex.core.io.Packet;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Index;
import com.jagex.js5.js5;

import java.io.File;

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

    private static FileSystem_Client open(File cache, int archive) throws Exception {
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
