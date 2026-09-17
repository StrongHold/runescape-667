import com.jagex.core.io.BufferedFile;
import com.jagex.core.io.FileOnDisk;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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

    /**
     * The group holding a piece of scenery the client drew with its textures in the wrong places.
     *
     * It carries nine texture spaces but the numbers for only seven of them, and three ways of
     * placing a texture at once, which is why it showed what a model built by hand here did not.
     */
    public static final int ROCK = 64785;

    /**
     * The group holding a piece of scenery a hundred and twenty four of whose faces stand on the
     * same three corners as another face. Two faces in the same place are settled by how far away
     * each is reckoned to be, and turning one about is the only way to ask what happens when the
     * two answers are a hair apart.
     */
    public static final int DOUBLED_FACES = 32421;

    /**
     * The group holding the step at the head of a flight of stairs, which the client draws
     * nothing for, so that the hole beneath the stairs shows through where the step should be.
     *
     * The flight itself is 26910 and stands on the tile beside it. Both were put in front of both
     * toolkits; the flight came out identical.
     */
    public static final int STAIRS = 32419;

    /**
     * A model the client draws with a black ground behind it where there should be none.
     */
    public static final int BLACK_BACKED = 2030;

    /**
     * The groups the scenes that want any model at all are given.
     *
     * They were once found by looking through the cache for the first model with enough faces of
     * the kind wanted, which made every scene drawn with one depend on what the cache happened to
     * hold and in what order. A cache brought up to date could hand a scene a different model and
     * move every number recorded against it, without anything saying why. These are the groups
     * that search settled on, written down so that it cannot settle on others.
     *
     * CacheModel says which groups the search would find today, so these can be worked out again
     * if they ever have to be.
     */
    public static final int TEXTURED = 1;
    public static final int UNTEXTURED = 122;
    public static final int UNTEXTURED_BESIDE = 336;

    /**
     * The model with more faces placed round a point than any other, which is the only way of
     * placing a texture that no model built here can ask for.
     */
    public static final int ROUND_A_POINT = 3105;

    private final FileSystem_Client store;

    private CacheMesh(FileSystem_Client store) {
        this.store = store;
    }

    /**
     * A model out of the cache that wears at least one texture, or nothing when the cache holds
     * none, because a mesh built here carries no texture space for a texture to sit in.
     */
    public static Optional<Mesh> anyTextured() throws Exception {
        return numbered(TEXTURED);
    }

    /**
     * A model out of the cache where there is one, and one built here where there is not, so a
     * check still runs on a machine with no cache.
     *
     * Both sides of every check are given the same mesh, so which one it is does not decide
     * whether they agree. It decides only how much of the toolkit the check reaches, and a model
     * the client itself drew reaches far more of it than one built by hand.
     */
    public static Mesh anyUntextured() throws Exception {
        var found = numbered(UNTEXTURED);
        if (found.isEmpty()) {
            System.out.println("no model " + UNTEXTURED + " to be had, using the mesh built here");
            return FlatMesh.INSTANCE.build();
        }

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
    public static Mesh twoUntexturedJoined() throws Exception {
        var found = new ArrayList<Mesh>();
        numbered(UNTEXTURED).ifPresent(found::add);
        numbered(UNTEXTURED_BESIDE).ifPresent(found::add);

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

    /**
     * The model in the cache with the most faces placed the named way.
     *
     * The first model that uses a way at all may use it on a handful of faces buried inside
     * itself, which draws a picture that would look the same if the way were never worked out.
     * The most is the one that has something to show.
     */
    public static Optional<Mesh> mostPlaced(int way) throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
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
     * One model out of the cache, named by its group.
     *
     * A model the client draws wrongly is named by its group and nothing else, so a scene that
     * has to draw the same thing the client drew asks for it by that number.
     */
    /**
     * The models the scenes are drawn with, kept beside the source rather than taken from the
     * game's cache.
     *
     * A cache is a quarter of a gigabyte, belongs to whoever ran the client, and changes when the
     * game does. The scenes want seven models and ten kilobytes of it, so those are kept here
     * instead: a scene then draws the same model on any machine, and a cache brought up to date
     * cannot move a number recorded against a scene without anyone noticing.
     *
     * What is kept is exactly what the cache holds for the group, packed the way the cache packs
     * it, so it is read back through the same decoder and nothing else has to know where it came
     * from.
     */
    private static final String KEPT = "models";

    /**
     * Where the kept models are, looked for beside the working directory and then beside the
     * natives, so that it is found whether a task runs from the natives or from the root.
     */
    private static Optional<Path> keptModel(int group) {
        for (var root : new String[] {KEPT, "natives/" + KEPT}) {
            var held = Path.of(root, group + ".dat");
            if (Files.isRegularFile(held)) {
                return Optional.of(held);
            }
        }

        return Optional.empty();
    }

    /**
     * One model, from what is kept beside the source where it is there and from the cache where
     * it is not.
     */
    public static Optional<Mesh> numbered(int group) throws Exception {
        var kept = keptModel(group);
        if (kept.isPresent()) {
            return Optional.of(new Mesh(js5.decodeContainer(Files.readAllBytes(kept.get()))));
        }

        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return Optional.empty();
        }

        return at(cache).read(group);
    }

    /**
     * Writes the models the scenes are drawn with beside the source, so that they need the cache
     * once rather than every time.
     */
    public static void keep(Path into) throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            System.out.println("no cache at " + cache + ", so there is nothing to keep");
            return;
        }

        var held = at(cache);
        Files.createDirectories(into);

        for (var group : DRAWN_WITH) {
            var packed = held.packed(group);
            if (packed.isEmpty()) {
                System.out.println("group " + group + " is not in the cache");
                continue;
            }

            var file = into.resolve(group + ".dat");
            Files.write(file, packed.get());
            System.out.println("kept group " + group + " as " + file + ", "
                + packed.get().length + " bytes");
        }
    }

    /** Every model any scene or probe is drawn with. */
    private static final int[] DRAWN_WITH = {
        TEXTURED, UNTEXTURED, UNTEXTURED_BESIDE, ROUND_A_POINT, BLACK_BACKED, STAIRS,
        DOUBLED_FACES, ROCK
    };


    /**
     * What one model in the cache asks for, named by its group.
     *
     * A picture the client draws wrongly names a model, and this says what that model asks the
     * toolkit to do, so a scene can be pointed at the same thing the client was.
     */
    public static void report(int group) throws Exception {
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
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
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
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
        var cache = new File(System.getProperty("user.home"), ".jagex_cache_32/runescape");
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return null;
        }

        return at(cache).read(group).orElse(null);
    }

    private Optional<byte[]> packed(int group) {
        try {
            var held = store.read(group);
            return held == null ? Optional.empty() : Optional.of(held);
        } catch (Exception ignored) {
            return Optional.empty();
        }
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
