import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

            var toolkit = oa.create(null, new StubTextureSource(), Scene.WIDTH, Scene.HEIGHT);
            toolkit.xa(1.0F);
            toolkit.ZA(0xFFFFFF, 0.5F, 0.5F, 20.0F, -50.0F, 30.0F);
            toolkit.method7938(toolkit.createHeap(POOL_SIZE));

            var lines = new ArrayList<String>();
            refusals(toolkit, lines);
            moves(toolkit, lines);
            sizes(toolkit, lines);
            turns(toolkit, lines);
            mirrors(toolkit, lines);
            masks(toolkit, lines);
            animations(toolkit, lines);
            lights(toolkit, lines);

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
     * Opening and closing an animation, and what the model says about itself between them.
     */
    private static void animations(Toolkit toolkit, List<String> lines) throws Exception {
        var model = (i) build(toolkit);
        lines.add("see through " + model.F() + " moving textures " + model.r());

        lines.add("opened " + model.NA());
        model.wa();
        lines.add("closed " + measure(model));

        model.aa((short) 0, (short) 1);
        lines.add("retextured " + measure(model) + " moving textures " + model.r());
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

    private static Model build(Toolkit toolkit) throws Exception {
        return toolkit.createModel(CacheMesh.anyUntextured(MODEL_FACES), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
    }

    private ModelProbe() {
        /* empty */
    }
}
