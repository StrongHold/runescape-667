import com.jagex.graphics.Mesh;
import com.jagex.graphics.particles.ModelParticleEffector;
import com.jagex.graphics.particles.ModelParticleEmitter;
import com.jagex.js5.js5;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The models the scenes and the probes are drawn with.
 *
 * A mesh built by hand is a poor thing to check a rasteriser against, because a scene that draws
 * nothing says only that something is wrong and not which of the mesh or the setup is at fault.
 * A model the client itself drew is known to be good, so it takes the mesh out of the question.
 * These are read out of the copies kept beside the scenes, or out of the cache by {@link CacheMesh}
 * where no copy is kept.
 */
public final class SceneModel {

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
     * {@code ./gradlew :cache:describeModel --args=--scanned} says which groups the search would
     * find today, so these can be worked out again if they ever have to be.
     */
    public static final int TEXTURED = 1;
    public static final int UNTEXTURED = 122;
    public static final int UNTEXTURED_BESIDE = 336;

    /**
     * The model with more faces placed round a point than any other, which is the only way of
     * placing a texture that no model built here can ask for.
     */
    public static final int ROUND_A_POINT = 3105;

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

        var cache = Cache.standard();
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            return Optional.empty();
        }

        return CacheMesh.at(cache).read(group);
    }

    /**
     * Writes the models the scenes are drawn with beside the source, so that they need the cache
     * once rather than every time.
     */
    public static void keep(Path into) throws Exception {
        var cache = Cache.standard();
        if (!new File(cache, "main_file_cache.dat2").isFile()) {
            System.out.println("no cache at " + cache + ", so there is nothing to keep");
            return;
        }

        var held = CacheMesh.at(cache);
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

    private SceneModel() {
        /* empty */
    }
}
