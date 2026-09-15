import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.FileOnDisk;
import com.jagex.graphics.Mesh;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import java.io.File;
import java.util.Optional;

/**
 * Reads a model straight out of the cache on disk.
 *
 * A mesh built by hand is a poor thing to check a rasteriser against, because a scene that draws
 * nothing says only that something is wrong and not which of the mesh or the setup is at fault.
 * A model the client itself drew is known to be good, so it takes the mesh out of the question.
 *
 * Only the store is read here. Nothing is downloaded and no index is needed, because a group
 * holds a whole model and its number is enough to find it.
 */
public final class CacheMesh {

    private static final int GROUP_LIMIT = 4096;

    private final FileSystem_Client store;

    private CacheMesh(FileSystem_Client store) {
        this.store = store;
    }

    public static CacheMesh at(File cache) throws Exception {
        var data = new FileOnDisk(new File(cache, "main_file_cache.dat2"), "r", Long.MAX_VALUE);
        var index = new FileOnDisk(
            new File(cache, "main_file_cache.idx" + Js5Archive.MODELS), "r", Long.MAX_VALUE);

        return new CacheMesh(new FileSystem_Client(
            Js5Archive.MODELS,
            new BufferedFile(data, 5200, 0),
            new BufferedFile(index, 6000, 0),
            1 << 22
        ));
    }

    /**
     * The first model in the cache with at least this many faces, which keeps the scene away from
     * the many tiny models the cache begins with.
     */
    public Optional<Mesh> firstWithFaces(int faces) {
        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = read(group);
            if (mesh.isPresent() && mesh.get().faceCount >= faces) {
                return mesh;
            }
        }

        return Optional.empty();
    }

    private Optional<Mesh> read(int group) {
        try {
            var packed = store.read(group);
            if (packed == null) {
                return Optional.empty();
            }
            return Optional.of(new Mesh(js5.decodeContainer(packed)));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}
