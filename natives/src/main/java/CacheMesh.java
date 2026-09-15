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

    /**
     * A model out of the cache where there is one, and one built here where there is not, so a
     * check still runs on a machine with no cache.
     *
     * Both sides of every check are given the same mesh, so which one it is does not decide
     * whether they agree. It decides only how much of the toolkit the check reaches, and a model
     * the client itself drew reaches far more of it than one built by hand.
     */
    public static Mesh anyUntextured(int faces) throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            System.out.println("no cache at " + cache + ", using the mesh built here");
            return FlatMesh.INSTANCE.build();
        }

        var found = at(cache).firstUntexturedWithFaces(faces);
        if (found.isEmpty()) {
            return FlatMesh.INSTANCE.build();
        }

        System.out.println("model from the cache: " + found.get().faceCount + " faces, "
            + found.get().vertexCount + " vertices");
        return found.get();
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
     * The first model in the cache with at least this many faces and none of them textured.
     *
     * A textured face sends the toolkit to its texture cache, which answers nothing while the
     * scenes hand it a texture source that holds nothing, and it reads the answer without
     * checking it. An untextured model keeps that out of the way until textures are written.
     */
    public Optional<Mesh> firstUntexturedWithFaces(int faces) {
        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = read(group);
            if (mesh.isPresent() && mesh.get().faceCount >= faces && untextured(mesh.get())) {
                return mesh;
            }
        }

        return Optional.empty();
    }

    private static boolean untextured(Mesh mesh) {
        if (mesh.faceTexture == null) {
            return true;
        }

        for (var face = 0; face < mesh.faceCount; face++) {
            if (mesh.faceTexture[face] != -1) {
                return false;
            }
        }

        return true;
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
