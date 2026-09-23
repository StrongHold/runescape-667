import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.FileOnDisk;
import com.jagex.graphics.Mesh;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

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
     * Counts how each model in the cache asks for its textures to be placed, so that the ways worth
     * writing can be told from the ways that are never used.
     */
    public static void surveyMappingTypes() throws Exception {
        var cache = Cache.standard();
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return;
        }

        var held = at(cache);
        var ways = new TreeMap<Integer, Integer>();
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

        var billboards = 0;
        var particles = 0;
        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = held.read(group);
            if (mesh.isEmpty()) {
                continue;
            }
            if (mesh.get().billboards != null && mesh.get().billboards.length > 0) {
                billboards++;
            }
            if (mesh.get().emitters != null && mesh.get().emitters.length > 0) {
                particles++;
            }
        }

        System.out.println("SURVEY " + textured + " textured models, spaces by way " + ways
            + ", models with billboards " + billboards + ", with particles " + particles);
    }

    /**
     * The first model in the cache with at least this many faces and a texture space placed the
     * named way, so that a way can be given a picture of its own to be judged against.
     */
    public static Optional<Mesh> anyPlaced(int way, int faces, int most) throws Exception {
        var cache = Cache.standard();
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

    /**
     * The model in the cache with the most faces placed the named way.
     *
     * The first model that uses a way at all may use it on a handful of faces buried inside
     * itself, which draws a picture that would look the same if the way were never worked out.
     * The most is the one that has something to show.
     */
    public static Optional<Mesh> mostPlaced(int way) throws Exception {
        var cache = Cache.standard();
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return Optional.empty();
        }

        var held = at(cache);
        var best = Optional.<Mesh>empty();
        var most = 0;

        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = held.read(group);
            if (mesh.isEmpty() || mesh.get().texMappingType == null
                || mesh.get().faceTexSpace == null || !plain(mesh.get())) {
                continue;
            }

            var placed = placedFaces(mesh.get(), way);
            if (placed > most) {
                most = placed;
                best = mesh;
                lastPlaced = group;
            }
        }

        return best;
    }

    /** How many faces of the mesh belong to a space placed the named way. */
    public static int placedFaces(Mesh mesh, int way) {
        var ways = mesh.texMappingType;
        var placed = 0;

        for (var face = 0; face < mesh.faceCount; face++) {
            var space = mesh.faceTexSpace[face];
            if (space >= 0 && space < ways.length && ways[space] == way) {
                placed++;
            }
        }

        return placed;
    }


    /**
     * What one model in the cache asks for, named by its group.
     *
     * A picture the client draws wrongly names a model, and this says what that model asks the
     * toolkit to do, so a scene can be pointed at the same thing the client was.
     */
    public static void report(int group) throws Exception {
        var cache = Cache.standard();
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return;
        }

        var mesh = at(cache).read(group);
        if (mesh.isEmpty()) {
            System.out.println("MODEL " + group + " is not in the cache");
            return;
        }

        var held = mesh.get();
        var ways = new TreeMap<Integer, Integer>();
        if (held.texMappingType != null && held.faceTexSpace != null) {
            for (var way : held.texMappingType) {
                ways.merge((int) way, 0, Integer::sum);
            }
            for (var way : ways.keySet()) {
                ways.put(way, placedFaces(held, way));
            }
        }

        System.out.println("MODEL " + group + ": " + held.faceCount + " faces, "
            + (held.faceTexture == null ? "no textures" : "textured")
            + ", faces by way " + ways
            + ", spaces " + held.texSpaceCount
            + ", numbers " + lengths(held)
            + ", billboards " + (held.billboards == null ? 0 : held.billboards.length)
            + ", alphas " + (held.faceAlpha == null ? "none" : "some")
            + ", priorities " + (held.facePriority == null ? "none" : "some")
            + ", globalPriority " + held.globalPriority);

        var alphas = new TreeMap<Integer, Integer>();
        var textures = new TreeMap<Integer, Integer>();
        var colours = new TreeMap<Integer, Integer>();
        for (var face = 0; face < held.faceCount; face++) {
            alphas.merge(held.faceAlpha == null ? 0 : held.faceAlpha[face] & 0xFF, 1, Integer::sum);
            textures.merge(held.faceTexture == null ? -1 : (int) held.faceTexture[face], 1,
                Integer::sum);
            colours.merge((int) held.faceColour[face], 1, Integer::sum);
        }

        System.out.println("  alphas " + alphas);
        System.out.println("  textures " + textures);
        System.out.println("  colours " + colours.size() + " distinct, "
            + colours.entrySet().stream().limit(8).toList());
    }

    /** How many spaces the mesh carries each of the numbers only some ways need. */
    private static String lengths(Mesh mesh) {
        var held = new int[] {
            length(mesh.texSpaceScaleX), length(mesh.texSpaceScaleY), length(mesh.texSpaceScaleZ),
            length(mesh.texOffsetX), length(mesh.texOffsetY), length(mesh.texOffsetZ),
            length(mesh.texRotation), length(mesh.texDirection)
        };

        return Arrays.toString(held);
    }

    private static int length(int[] held) {
        return held == null ? 0 : held.length;
    }

    private static int length(byte[] held) {
        return held == null ? 0 : held.length;
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
    /**
     * Says which groups a search through the cache would settle on, so that the groups the scenes
     * name can be worked out again if a cache brought up to date ever makes them wrong.
     */
    public static void sayWhichAreScanned() throws Exception {
        var cache = Cache.standard();
        var held = at(cache);

        for (var way = 0; way < WAYS_A_TEXTURE_IS_PLACED; way++) {
            if (mostPlaced(way).isPresent()) {
                System.out.println("most faces placed way " + way + ": group " + lastPlaced);
            }
        }

        held.firstTexturedWithFaces(ENOUGH_FACES).ifPresent(mesh ->
            System.out.println("a textured model with " + ENOUGH_FACES + " faces or more is"
                + " group " + held.lastRead));

        for (var group : held.firstUntexturedGroups(ENOUGH_FACES, 2)) {
            System.out.println("an untextured model with " + ENOUGH_FACES + " faces or more is"
                + " group " + group);
        }
    }

    /** How many faces a model must have before a scene finds it worth drawing. */
    private static final int ENOUGH_FACES = 200;

    /** The group the last search read, so that a search can say where it found what it found. */
    private int lastRead = -1;

    /** How many ways the client can ask for a texture to be placed on a face. */
    private static final int WAYS_A_TEXTURE_IS_PLACED = 4;

    /** The group the last search for a way of placing a texture settled on. */
    private static int lastPlaced = -1;

    /**
     * The groups of the first few untextured models with at least this many faces.
     */
    public List<Integer> firstUntexturedGroups(int faces, int wanted) {
        var found = new ArrayList<Integer>();

        for (var group = 0; group < GROUP_LIMIT && found.size() < wanted; group++) {
            var mesh = read(group);
            if (mesh.isPresent() && mesh.get().faceCount >= faces && untextured(mesh.get())
                && plain(mesh.get())) {
                found.add(group);
            }
        }

        return found;
    }

    public Optional<Mesh> firstTexturedWithFaces(int faces) {
        for (var group = 0; group < GROUP_LIMIT; group++) {
            var mesh = read(group);
            if (mesh.isPresent() && mesh.get().faceCount >= faces && !untextured(mesh.get())
                && plain(mesh.get())) {
                lastRead = group;
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

    /**
     * One model out of the cache by the group it is kept in, or nothing where the cache has none.
     */
    public static Mesh group(int group) throws Exception {
        var cache = Cache.standard();
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return null;
        }

        return at(cache).read(group).orElse(null);
    }

    /**
     * One group exactly as the cache packs it, which is what a model kept beside the scenes is.
     */
    public Optional<byte[]> packed(int group) {
        try {
            var held = store.read(group);
            return held == null ? Optional.empty() : Optional.of(held);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    /**
     * One model out of this cache, or nothing where it has none or cannot decode it.
     */
    public Optional<Mesh> read(int group) {
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
