import com.jagex.graphics.Model;
import com.jagex.graphics.PickingCylinder;
import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Asks a software toolkit where a model is, moves it, and asks again.
 *
 * Where a model is decides whether the client bothers drawing it, how far the camera may be pushed
 * before it disappears, and where its name plate goes. None of that reaches the screen through the
 * model itself, so no picture can check it.
 *
 * Each measurement is taken before and after the model is changed, because the toolkit measures a
 * model once and then again only when something has moved it, and a measurement that is never
 * taken again is right the first time and wrong for the rest of the model's life.
 */
public final class ModelProbe {

    private static final int POOL_SIZE = 1 << 20;
    /**
     * Every thing a model can be asked to do, so that every operation is allowed. One model is
     * built asking for nothing, to check that asking anyway is refused.
     */
    private static final int FUNCTIONS = 0xFFFF;

    private static final int NO_FUNCTIONS = 0;
    private static final int FEATURES = 64;
    private static final int AMBIENT = 64;
    private static final int CONTRAST = 768;
    private static final int MODEL_FACES = 200;

    /** How many vertices beyond the ones its faces use a model is asked to carry. */
    private static final int[] SPARES = {0, 1, 4};

    /** Two emitters of three vertices each, and three effectors of one. */
    private static final int PARTICLE_PLACES = 2 * 3 + 3;

    /** How far in front of the camera the model stands when it is asked whether it was picked. */
    private static final int DEPTH = 900;

    private static final int[][] MOVES = {
        {0, 0, 0}, {10, 0, 0}, {0, -40, 0}, {0, 0, 300}, {-7, 13, -29}, {-32000, 0, 0}
    };

    private static final int[][] SIZES = {
        {128, 128, 128}, {256, 128, 128}, {128, 64, 128}, {64, 64, 64}, {1, 1, 1}, {1000, 1, 300}
    };

    /**
     * Angles to turn through, in the sixteen thousand three hundred and eighty four steps of a
     * circle the client counts in. The three whole quarters are taken the short way round by the
     * toolkit and the rest through a table, so both paths are covered.
     */
    private static final int[] ANGLES = {0, 1, 0x1000, 0x2000, 0x3000, 0x0400, 0x2ABC, 0x3FFF};

    public static void main(String[] args) {
        try {
            Watchdog.arm("The model probe", 120);
            LibraryManager.putLibrary(new File(args[0]), "sw3d");

            /*
             * A canvas, because the cylinder a model may be clicked on is only worked out on the
             * way to drawing it and the toolkit stops before that when it has nowhere to draw.
             */
            var canvas = new Canvas();
            canvas.setSize(Scene.WIDTH, Scene.HEIGHT);

            var window = new Frame("model probe");
            window.add(canvas);
            window.pack();
            window.setVisible(true);
            Thread.sleep(1000);

            var toolkit = oa.create(canvas, new HandTextureSource(), Scene.WIDTH, Scene.HEIGHT);
            toolkit.xa(1.0F);
            toolkit.ZA(0xFFFFFF, 0.5F, 0.5F, 20.0F, -50.0F, 30.0F);
            toolkit.method7938(toolkit.createHeap(POOL_SIZE));
            toolkit.allocateThreads(1);
            toolkit.linkThreads(0);
            toolkit.DA(Scene.WIDTH / 2, Scene.HEIGHT / 2, 512, 512);
            toolkit.f(Scene.NEAR, Integer.MAX_VALUE);

            var camera = toolkit.createMatrix();
            camera.makeIdentity();
            toolkit.setCamera(camera);

            var lines = new ArrayList<String>();
            refusals(toolkit, lines);
            moves(toolkit, lines);
            sizes(toolkit, lines);
            turns(toolkit, lines);
            mirrors(toolkit, lines);
            masks(toolkit, lines);
            copies(toolkit, lines);
            picks(toolkit, lines);
            pieces(toolkit, lines);
            cylinders(toolkit, lines);
            animations(toolkit, lines);
            textures(toolkit, lines);
            lights(toolkit, lines);
            particles(toolkit, lines);
            spareVertices(toolkit, lines);

            Files.write(Path.of(args[1]), lines);
            System.out.println("recorded " + lines.size() + " model answers");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Asking a model to do something it was not built for, which is refused rather than allowed.
     */
    private static void refusals(Toolkit toolkit, List<String> lines) throws Exception {
        var model = toolkit.createModel(
            CacheMesh.anyUntextured(MODEL_FACES), NO_FUNCTIONS, FEATURES, AMBIENT, CONTRAST);

        lines.add("refused move " + refused(() -> model.H(1, 0, 0)));
        lines.add("refused size " + refused(() -> model.O(64, 128, 128)));
        lines.add("refused recolour " + refused(() -> model.ia((short) 1, (short) 2)));
        lines.add("refused turn " + refused(() -> model.a(0x1000)));
        lines.add("refused turn with normals " + refused(() -> model.k(0x1000)));
        lines.add("refused tip " + refused(() -> model.FA(0x1000)));
        lines.add("refused roll " + refused(() -> model.VA(0x1000)));
        lines.add("refused mirror " + refused(model::v));
        lines.add("refused retexture " + refused(() -> model.aa((short) 1, (short) 2)));
        lines.add("allowed nothing " + measure(model));
    }

    private static String refused(Runnable asked) {
        try {
            asked.run();
            return "no";
        } catch (RuntimeException complaint) {
            return complaint.getClass().getName();
        }
    }

    private static void moves(Toolkit toolkit, List<String> lines) throws Exception {
        for (var move : MOVES) {
            var model = build(toolkit);
            lines.add("before " + measure(model));
            model.H(move[0], move[1], move[2]);
            lines.add("moved " + move[0] + " " + move[1] + " " + move[2] + " " + measure(model));
            model.H(-move[0], -move[1], -move[2]);
            lines.add("back " + measure(model));
        }
    }

    private static void sizes(Toolkit toolkit, List<String> lines) throws Exception {
        for (var size : SIZES) {
            var model = build(toolkit);
            model.O(size[0], size[1], size[2]);
            lines.add("sized " + size[0] + " " + size[1] + " " + size[2] + " " + measure(model));
            model.O(size[0], size[1], size[2]);
            lines.add("again " + measure(model));
        }
    }

    /**
     * Turning a model, both the way that leaves the directions it is shaded by to be worked out
     * again and the way that carries them round with it.
     */
    private static void turns(Toolkit toolkit, List<String> lines) throws Exception {
        for (var angle : ANGLES) {
            var turned = build(toolkit);
            turned.a(angle);
            lines.add("turned " + angle + " " + measure(turned));
            turned.a(angle);
            lines.add("turned twice " + angle + " " + measure(turned));

            var carried = build(toolkit);
            carried.k(angle);
            lines.add("carried " + angle + " " + measure(carried));

            var tipped = build(toolkit);
            tipped.FA(angle);
            lines.add("tipped " + angle + " " + measure(tipped));

            var rolled = build(toolkit);
            rolled.VA(angle);
            lines.add("rolled " + angle + " " + measure(rolled));
        }
    }

    /**
     * Turning a model back to front, which also turns every face the other way round.
     */
    private static void mirrors(Toolkit toolkit, List<String> lines) throws Exception {
        var model = build(toolkit);
        model.v();
        lines.add("mirrored " + measure(model));
        model.v();
        lines.add("mirrored back " + measure(model));
    }

    /**
     * Narrowing what may be done to a model.
     *
     * Asking for something back that was given up is not probed. The toolkit this is checked
     * against throws that out of the native call without catching it, which ends the process
     * rather than reaching the client, so there is no answer to compare against.
     */
    private static void masks(Toolkit toolkit, List<String> lines) throws Exception {
        var model = build(toolkit);
        lines.add("mask " + model.ua());

        model.s(0x00FF);
        lines.add("narrowed " + model.ua());

        model.s(0x000F);
        lines.add("narrowed again " + model.ua());
        lines.add("refused after narrowing " + refused(() -> model.ia((short) 1, (short) 2)));
    }

    /**
     * Copying a model, which hands the copy only the right to do what the mask allows.
     *
     * A copy is measured before anything is done to it, because the measurement is carried over
     * rather than taken again, and then changed in a way the mask allows so that the copy is
     * known to have its own arrays rather than the original's.
     */
    private static void copies(Toolkit toolkit, List<String> lines) throws Exception {
        int[] masks = {0, 0x1, 0xF, 0x4000, 0x801F, 0xFFFF};

        for (var mask : masks) {
            var model = build(toolkit);
            lines.add("original " + measure(model));

            var copy = model.copy((byte) 0, mask, true);
            lines.add("copied " + mask + " " + copy.ua() + " " + measure(copy)
                + " " + copy.WA() + " " + copy.da() + " " + copy.F());

            if ((mask & 0x1) != 0) {
                copy.H(300, 0, 0);
                lines.add("copy moved " + measure(copy) + " original " + measure(model));
            }

            if ((mask & 0x5) == 0x5) {
                copy.a(0x1000);
                lines.add("copy turned " + measure(copy) + " original " + measure(model));
            }

            if ((mask & 0x4000) != 0) {
                copy.ia((short) 0, (short) 40);
                lines.add("copy recoloured " + measure(copy) + " original " + measure(model));
            }
        }
    }

    /**
     * Whether a point on the screen lands on a model.
     *
     * The client asks this of everything under the mouse, so an answer that is wrong anywhere
     * makes something in the world unclickable or makes the wrong thing answer. The grid is
     * walked coarsely across the whole picture and then finely across one edge of the model,
     * because the answer only changes at an edge.
     */
    private static void picks(Toolkit toolkit, List<String> lines) throws Exception {
        var model = build(toolkit);

        var matrix = toolkit.createMatrix();
        matrix.makeRotationZ(0);
        matrix.translate(0, 0, DEPTH);

        var coarse = new StringBuilder();
        for (var y = 0; y < Scene.HEIGHT; y += 24) {
            for (var x = 0; x < Scene.WIDTH; x += 24) {
                coarse.append(model.picked(x, y, matrix, false, 0) ? '#' : '.');
            }
            lines.add("picked " + y + " " + coarse);
            coarse.setLength(0);
        }

        for (var x = 150; x < 370; x++) {
            lines.add("picked edge " + x
                + " " + model.picked(x, 192, matrix, false, 0)
                + " " + model.picked(x, 192, matrix, true, 0)
                + " " + model.pickedOrtho(x, 192, matrix, false, 0, 128));
        }

        var behind = toolkit.createMatrix();
        behind.makeRotationZ(0);
        behind.translate(0, 0, -DEPTH);
        lines.add("picked behind " + model.picked(Scene.WIDTH / 2, Scene.HEIGHT / 2, behind, false, 0));
    }

    /**
     * Animating only some of the pieces a model was built from.
     *
     * A player is one model built from a head, a torso and so on, and the client animates it
     * several times over naming a different set of those pieces each time. The model here is two
     * models from the cache joined, which is the only kind that records which piece each vertex
     * came from.
     */
    private static void pieces(Toolkit toolkit, List<String> lines) throws Exception {
        int[] masks = {0, 0x1, 0x2, 0x3, 0xFFFF};
        int[] labels = {0, 1, 2};

        for (var mask : masks) {
            var moved = joined(toolkit);
            moved.NA();
            moved.I(1, labels, 4000, -3000, 2500, false, mask, null);
            moved.wa();
            lines.add("piece moved " + mask + " " + measure(moved));

            var turned = joined(toolkit);
            turned.NA();
            turned.I(0, labels, 2000, 2000, 2000, false, mask, null);
            turned.I(2, labels, 1024, 2748, 8192, false, mask, null);
            turned.wa();
            lines.add("piece turned " + mask + " " + measure(turned));

            var stretched = joined(toolkit);
            stretched.NA();
            stretched.I(0, labels, 3, -4, 5, false, mask, null);
            stretched.I(3, labels, 256, 64, 300, false, mask, null);
            stretched.wa();
            lines.add("piece stretched " + mask + " " + measure(stretched));
        }
    }

    private static i joined(Toolkit toolkit) throws Exception {
        return (i) toolkit.createModel(
            CacheMesh.twoUntexturedJoined(MODEL_FACES), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
    }

    /**
     * The cylinder the client tests the mouse against before it asks the model itself.
     *
     * A model with no cylinder is unclickable however well it answers being picked, so this is
     * asked at several distances including two that put an end of the model behind the eye,
     * where the cylinder is pulled along to the near plane rather than dropped, and one that puts
     * the whole of it behind, where nothing is written at all.
     */
    private static void cylinders(Toolkit toolkit, List<String> lines) throws Exception {
        int[] depths = {DEPTH, 200, 60, 0, -100, -DEPTH};

        var model = build(toolkit);
        var matrix = toolkit.createMatrix();

        for (var depth : depths) {
            var cylinder = new PickingCylinder();

            matrix.makeRotationZ(0);
            matrix.translate(0, 0, depth);
            model.render(matrix, cylinder, 1);

            lines.add("cylinder " + depth + " " + cylinder.aBoolean352
                + " " + cylinder.anInt4504 + " " + cylinder.anInt4505
                + " " + cylinder.anInt4501 + " " + cylinder.anInt4503
                + " " + cylinder.anInt4502);
        }
    }

    /**
     * Opening and closing an animation, and what the model says about itself between them.
     */
    private static void animations(Toolkit toolkit, List<String> lines) throws Exception {
        var model = (i) build(toolkit);
        lines.add("see through " + model.F() + " moving textures " + model.r());

        lines.add("opened " + model.NA());
        model.wa();
        lines.add("closed " + measure(model));

        steps(toolkit, lines);

        model.aa((short) 0, (short) 1);
        lines.add("retextured " + measure(model) + " moving textures " + model.r());
    }

    /**
     * Handing textures to the toolkit, and what a model swapped onto one says about itself.
     *
     * A model is the only thing that reads a texture back out at present, and the only two
     * questions it answers about one are whether the texture slides and whether its light had to
     * be thrown away. Nothing here looks at a single pixel of a texture, so a texture handed over
     * with its pixels is checked no further than the numbers that came with it.
     *
     * The model has to come out of the cache. A mesh built here carries no texture space for a
     * texture to sit in, and a model built from one is not carried onto a new texture at all.
     */
    private static void textures(Toolkit toolkit, List<String> lines) throws Exception {
        var held = (oa) toolkit;

        for (var id = 0; id < 5; id++) {
            lines.add("metrics " + id + " taken " + held.c((short) id));
        }

        for (var id = 0; id < 5; id++) {
            lines.add("texture " + id + " taken " + held.WA((short) id));
        }

        lines.add("metrics beyond the source " + held.c((short) HandTextureSource.COUNT));
        lines.add("texture beyond the source " + held.WA((short) HandTextureSource.COUNT));

        var mesh = CacheMesh.anyTextured(MODEL_FACES);
        if (mesh.isEmpty()) {
            lines.add("no textured model in the cache");
            return;
        }

        /*
         * Texture one slides across, and the toolkit marks a model as wearing a moving texture
         * while it builds it, so a model every face of which has been put on texture one says
         * whether a change made to the mesh reaches the toolkit at all.
         */
        var forced = CacheMesh.anyTextured(MODEL_FACES).get();
        for (var face = 0; face < forced.faceCount; face++) {
            forced.faceTexture[face] = 1;
        }
        var marked = (i) toolkit.createModel(forced, FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
        lines.add("every face on a sliding texture " + marked.r());

        for (var id = 0; id < 5; id++) {
            var model = (i) toolkit.createModel(
                mesh.get(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
            model.aa((short) 0, (short) id);
            lines.add("swapped onto " + id + " moving textures " + model.r()
                + " " + measure(model));
        }

        /*
         * More textures than there is room for, so that the ones handed over first are thrown out.
         * The toolkit asks the source for a texture it has thrown out rather than answering that
         * it has none, so what this checks is that both toolkits throw out the same one.
         */
        for (var id = 0; id < HandTextureSource.COUNT; id++) {
            held.WA((short) id);
        }

        var model = (i) toolkit.createModel(mesh.get(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
        model.aa((short) 0, (short) 1);
        lines.add("swapped onto one after a flood " + model.r() + " " + measure(model));
    }

    /**
     * The steps an animation is made of, each applied to a fresh model so that one step cannot
     * hide behind another.
     *
     * The point a step turns about is set by the step before it, so each of them is preceded by
     * the step that sets it. Two of the labels name groups the model has and one names a group
     * it does not, because an animation names labels from a frame that may have been built
     * against a different model.
     *
     * No step here asks for the directions to be turned as well. The toolkit this is checked
     * against reads them without looking, and a model that has not been drawn yet has none, so
     * asking ends the process rather than answering.
     */
    private static void steps(Toolkit toolkit, List<String> lines) throws Exception {
        int[][] named = {{0}, {1}, {0, 1, 2}, {999}, {}};

        for (var labels : named) {
            var pivoted = (i) build(toolkit);
            pivoted.NA();
            pivoted.l(pivoted.nativeid, 0, labels, 5, -9, 17, 0, false);
            pivoted.wa();
            lines.add("pivoted " + labels.length + " " + measure(pivoted));

            var moved = (i) build(toolkit);
            moved.NA();
            moved.l(moved.nativeid, 1, labels, 40, -12, 7, 0, false);
            moved.wa();
            lines.add("stepped " + labels.length + " " + measure(moved));

            for (var order = 0; order < 2; order++) {
                var turned = (i) build(toolkit);
                turned.NA();
                turned.l(turned.nativeid, 0, labels, 0, 0, 0, 0, false);
                turned.l(turned.nativeid, 2, labels, 1024, 2748, 8192, order, false);
                turned.wa();
                lines.add("swung " + labels.length + " " + order + " " + measure(turned));
            }

            var stretched = (i) build(toolkit);
            stretched.NA();
            stretched.l(stretched.nativeid, 0, labels, 0, 0, 0, 0, false);
            stretched.l(stretched.nativeid, 3, labels, 256, 64, 300, 0, false);
            stretched.wa();
            lines.add("stretched " + labels.length + " " + measure(stretched));
        }
    }

    /**
     * The light a model carries, and what changing its colours does to it.
     */
    private static void lights(Toolkit toolkit, List<String> lines) throws Exception {
        var model = build(toolkit);
        lines.add("light " + model.WA() + " " + model.da() + " " + model.ua());

        model.C(20);
        model.LA(400);
        lines.add("light set " + model.WA() + " " + model.da());

        for (var colour = 0; colour < 8; colour++) {
            model.ia((short) colour, (short) (colour + 100));
            lines.add("recoloured " + colour + " " + measure(model));
        }
    }

    private static String measure(Model model) {
        return "%d %d %d %d %d %d %d %d".formatted(
            model.V(), model.RA(), model.fa(), model.EA(),
            model.HA(), model.G(), model.na(), model.ma());
    }

    /**
     * Where the vertices the client hangs particles off end up.
     *
     * Three whole numbers come back for each one, the three corners of every emitter first and
     * then the single vertex of every effector, so reading the run in the wrong order shows up
     * as well as putting a vertex in the wrong place. The model is asked again after it has been
     * turned and moved, because the answer is worked out from where its vertices are now.
     */
    private static void particles(Toolkit toolkit, List<String> lines) throws Exception {
        var model = (i) toolkit.createModel(CacheMesh.withParticles(
            CacheMesh.anyUntextured(MODEL_FACES)), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);

        var matrix = toolkit.createMatrix();
        var places = new int[PARTICLE_PLACES * 3];

        matrix.makeIdentity();
        model.method3688(places, matrix);
        lines.add("particles flat " + Arrays.toString(places));

        matrix.makeRotationZ(0x1400);
        matrix.rotateAxisY(0x0900);
        matrix.translate(-40, 700, 250);
        model.method3688(places, matrix);
        lines.add("particles turned " + Arrays.toString(places));

        model.a(0x0800);
        model.H(13, -21, 34);
        model.method3688(places, matrix);
        lines.add("particles after the model moved " + Arrays.toString(places));

        var plain = (i) build(toolkit);
        Arrays.fill(places, -1);
        plain.method3688(places, matrix);
        lines.add("particles on a model with none " + Arrays.toString(places));
    }

    /**
     * A model that carries vertices no face is built from.
     *
     * Those vertices hang billboards and particles rather than being part of the shape, and the
     * box the toolkit measures leaves them out. They stand a long way outside the shape here, so
     * a box drawn around every vertex the model holds is nothing like the right one.
     */
    private static void spareVertices(Toolkit toolkit, List<String> lines) {
        for (var spare : SPARES) {
            var model = toolkit.createModel(
                new FlatMesh(160, spare).build(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);

            lines.add("spare " + spare + " " + measure(model));
        }
    }

    private static Model build(Toolkit toolkit) throws Exception {
        return toolkit.createModel(CacheMesh.anyUntextured(MODEL_FACES), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
    }

    private ModelProbe() {
        /* empty */
    }
}
