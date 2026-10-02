import com.jagex.js5.Js5Index;
import com.jagex.js5.ResourceProvider;

import java.io.File;

/**
 * Hands the client's {@code js5} one archive of the cache on disk.
 *
 * The client's own provider asks a server for any group its cache is missing and checks every
 * group it reads against the index. Here every group is on disk already, so each one is handed
 * over as it is stored and the server is never asked.
 */
public final class CacheResourceProvider extends ResourceProvider {

    private final File cache;
    private final int archive;
    private Js5Index index;

    public CacheResourceProvider(File cache, int archive) {
        this.cache = cache;
        this.archive = archive;
    }

    @Override
    public void requestGroup(int groupId) {
        /* empty */
    }

    @Override
    public int completePercentage(int groupId) {
        return 100;
    }

    @Override
    public byte[] fetchgroup(int groupId) {
        try {
            return Cache.packed(cache, archive, groupId);
        } catch (Exception failure) {
            throw new IllegalStateException("Archive " + archive + " group " + groupId + " cannot be read.", failure);
        }
    }

    @Override
    public Js5Index index() {
        if (index == null) {
            try {
                index = Cache.index(cache, archive);
            } catch (Exception failure) {
                throw new IllegalStateException("The index of archive " + archive + " cannot be read.", failure);
            }
        }
        return index;
    }
}
