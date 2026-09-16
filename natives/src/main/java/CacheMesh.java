import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.FileOnDisk;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
     * A model out of the cache that wears at least one texture, or nothing when the cache holds
     * none, because a mesh built here carries no texture space for a texture to sit in.
     */
    public static Optional<Mesh> anyTextured(int faces) throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return Optional.empty();
        }

        return at(cache).firstTexturedWithFaces(faces);
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

    /**
     * Hangs particles off a mesh, which no model out of the cache here happens to carry.
     *
     * An emitter names three vertices and an effector names one, and the client flattens both
     * into a single run of vertex numbers before the toolkit ever sees them. The numbers chosen
     * here are spread across the mesh rather than bunched, so a run read in the wrong order
     * comes back wrong rather than merely shifted.
     */
    public static Mesh withParticles(Mesh mesh) {
        var reach = mesh.vertexCount;
        mesh.emitters = new ModelParticleEmitter[] {
            new ModelParticleEmitter(0, 0, reach / 3, reach - 1, (byte) 0),
            new ModelParticleEmitter(1, reach / 2, 1, reach / 4, (byte) 0)
        };
        mesh.effectors = new ModelParticleEffector[] {
            new ModelParticleEffector(0, reach - 2),
            new ModelParticleEffector(1, 2),
            new ModelParticleEffector(2, reach / 5)
        };
        return mesh;
    }

    /**
     * Two models out of the cache joined into one.
     *
     * A model built from several pieces is the only kind that records which piece each vertex
     * came from, and that is what the client names when it animates one part of a player and
     * leaves the rest standing still.
     */
    public static Mesh twoUntexturedJoined(int faces) throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return FlatMesh.INSTANCE.build();
        }

        var found = at(cache).firstUntexturedWithFaces(faces, 2);
        if (found.size() < 2) {
            return FlatMesh.INSTANCE.build();
        }

        return new Mesh(found.toArray(new Mesh[0]), found.size());
    }

    /**
     * Counts how each model in the cache asks for its textures to be placed, so that the ways worth
     * writing can be told from the ways that are never used.
     */
    public static void surveyMappingTypes() throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return;
        }

        var held = at(cache);
        var ways = new java.util.TreeMap<Integer, Integer>();
        var textured = 0;

        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = held.read(group);
            if (mesh.isEmpty() || mesh.get().texMappingType == null) {
                continue;
            }

            textured++;
            for (var way : mesh.get().texMappingType) {
                ways.merge((int) way, 1, Integer::sum);
            }
        }

        System.out.println("SURVEY " + textured + " textured models, spaces by way " + ways);
    }

    /**
     * The first model in the cache with at least this many faces and a texture space placed the
     * named way, so that a way can be given a picture of its own to be judged against.
     */
    public static Optional<Mesh> anyPlaced(int way, int faces, int most) throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return Optional.empty();
        }

        var held = at(cache);
        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = held.read(group);
            if (mesh.isEmpty() || mesh.get().faceCount < faces || mesh.get().faceCount > most
                || !plain(mesh.get())
                || mesh.get().texMappingType == null || mesh.get().faceTexSpace == null) {
                continue;
            }

            for (var space = 0; space < mesh.get().texMappingType.length; space++) {
                if (mesh.get().texMappingType[space] == way && wears(mesh.get(), space)) {
                    return mesh;
                }
            }
        }

        return Optional.empty();
    }

    /** Whether any face of the mesh belongs to a space, which an unused one does not. */
    private static boolean wears(Mesh mesh, int space) {
        for (var face = 0; face < mesh.faceCount; face++) {
            if (mesh.faceTexSpace[face] == space) {
                return true;
            }
        }

        return false;
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
     * checking it. An untextured model keeps that out of the way.
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

    /**
     * The first model in the cache with at least this many faces and one of them textured.
     */
    public Optional<Mesh> firstTexturedWithFaces(int faces) {
        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = read(group);
            if (mesh.isPresent() && mesh.get().faceCount >= faces && !untextured(mesh.get())
                && plain(mesh.get())) {
                return mesh;
            }
        }

        return Optional.empty();
    }

    /**
     * The first few models in the cache with at least this many faces and none of them textured.
     */
    public List<Mesh> firstUntexturedWithFaces(int faces, int wanted) {
        var found = new ArrayList<Mesh>();

        for (var group = 0; group < GROUP_LIMIT && found.size() < wanted; group++) {
            var mesh = read(group);
            if (mesh.isPresent() && mesh.get().faceCount >= faces && untextured(mesh.get())
                && plain(mesh.get())) {
                found.add(mesh.get());
            }
        }

        return found;
    }

    /**
     * Whether the model carries nothing but geometry.
     *
     * A billboard is described by a type read from the configuration, which needs a client
     * connected to a server to read, and particles need a toolkit built to carry them. A model
     * with neither can be built from the cache alone.
     */
    private static boolean plain(Mesh mesh) {
        return mesh.billboards == null && mesh.emitters == null && mesh.effectors == null;
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
