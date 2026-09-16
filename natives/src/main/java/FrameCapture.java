import com.jagex.graphics.Toolkit;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws every scene through a software toolkit and leaves the frames on disk.
 *
 * The client only reaches the toolkit once the user picks it, which makes it a poor place to
 * check rendering from. This drives the toolkit directly, and it drives the shipped one and ours
 * the same way, so the frames from each can be compared.
 *
 * Each scene is drawn twice. The second copy catches state the toolkit set and did not put back,
 * which shows as a frame that differs from the one before it rather than from the other
 * implementation.
 *
 * The directory the frames go in is named by SW3D_DUMP, or by JAWTSHIM_DUMP when the shipped
 * toolkit is being driven through the shim. A manifest is written beside them naming the scene
 * each frame belongs to.
 */
public final class FrameCapture {

    private static final int REPEATS = 2;

    private static final int POOL_SIZE = 1 << 20;
    private static final int FUNCTIONS = 2048;
    private static final int FEATURES = 64;
    private static final int MODEL_FACES = 200;
    private static final int VISIBLE_FACES = 2;
    private static final int AMBIENT = number("SW3D_AMBIENT", 64);
    private static final int CONTRAST = 768;

    /**
     * The window keeps the virtual machine alive after this method returns, so every path out of
     * here ends in an explicit exit. Without that a failure hangs instead of reporting.
     */
    public static void main(String[] args) {
        try {
            Watchdog.arm("The frame capture", 120);
            capture(args[0]);
            System.out.println("drew " + Scene.ALL.size() + " scenes, " + REPEATS + " times each");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void capture(String library) throws Exception {
        LibraryManager.putLibrary(new File(library), "sw3d");

        var canvas = new Canvas();
        canvas.setSize(Scene.WIDTH, Scene.HEIGHT);

        var window = new Frame("software toolkit frame capture");
        window.add(canvas);
        window.pack();
        window.setVisible(true);
        Thread.sleep(1000);

        var toolkit = oa.create(canvas, new StubTextureSource(), Scene.WIDTH, Scene.HEIGHT);
        var sprite = GradientSprite.INSTANCE;
        var gradient = toolkit.createSprite(
            sprite.width(), sprite.width(), sprite.height(), sprite.pixels());

        toolkit.la();
        toolkit.DA(Scene.WIDTH / 2, Scene.HEIGHT / 2, 512, 512);
        toolkit.xa(1.0F);

        /*
         * The same setup the client's own profiling scene does. A model needs a camera to be
         * projected through and a sun to be shaded by, and it has to be built asking for the
         * features that draw it.
         */
        var camera = toolkit.createMatrix();
        camera.makeIdentity();
        toolkit.setCamera(camera);
        var sun = (float) number("SW3D_SUN_TENTHS", 5) / 10.0F;
        toolkit.ZA(0xFFFFFF, sun, sun, 20.0F, -50.0F, 30.0F);

        toolkit.method7938(toolkit.createHeap(POOL_SIZE));
        var props = new Scene.Props(
            gradient,
            toolkit.createModel(CacheMesh.anyUntextured(MODEL_FACES), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            toolkit.createModel(fewFaces(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            toolkit.createMatrix(),
            toolkit.createFont(HandFont.metrics(), HandFont.letters(), true),
            toolkit.createFont(HandFont.metrics(), HandFont.letters(), false));

        var manifest = new ArrayList<String>();

        for (var scene : Scene.ALL) {
            for (var repeat = 0; repeat < REPEATS; repeat++) {
                drawOnce(toolkit, scene, props);
                manifest.add(scene.written() ? scene.title() : scene.title() + " (outstanding)");
            }
        }

        toolkit.method7950();
        writeManifest(manifest);
        window.dispose();
    }

    private static void drawOnce(Toolkit toolkit, Scene scene, Scene.Props props) throws Exception {
        toolkit.GA(Scene.CLEAR_COLOUR);
        toolkit.ya();
        scene.draw(toolkit, props);
        toolkit.flip(0, 0);
        Thread.sleep(60);
    }

    /**
     * Names the scene each frame belongs to, so that a difference is reported against a scene
     * rather than against a frame number nobody can place.
     */
    private static void writeManifest(List<String> scenes) throws Exception {
        var directory = dumpDirectory();
        if (directory == null) {
            return;
        }

        Files.write(Path.of(directory, "scenes.txt"), scenes);
    }

    /**
     * The same model from the cache with all but a couple of its faces cut off.
     *
     * A model the toolkit accepts is the only kind worth checking against, and three hundred
     * faces say only that something is wrong. Two faces can be worked out by hand.
     */
    private static com.jagex.graphics.Mesh fewFaces() throws Exception {
        var mesh = CacheMesh.anyUntextured(MODEL_FACES);
        mesh.faceCount = Math.min(mesh.faceCount, VISIBLE_FACES);

        /*
         * The colour of every visible face can be forced from outside, so that the colour table
         * can be read out of either toolkit one value at a time.
         */
        var forced = System.getenv("SW3D_FACE_COLOUR");
        if (forced != null && !forced.isEmpty()) {
            for (var face = 0; face < mesh.faceCount; face++) {
                mesh.faceColour[face] = (short) Integer.parseInt(forced);
            }
        }

        return mesh;
    }

    /**
     * A number the environment may override, so that the scenes can be driven to a particular
     * corner without changing what they normally draw.
     */
    private static int number(String name, int fallback) {
        var held = System.getenv(name);
        return held == null || held.isEmpty() ? fallback : Integer.parseInt(held);
    }

    static String dumpDirectory() {
        var shim = System.getenv("JAWTSHIM_DUMP");
        return shim == null ? System.getenv("SW3D_DUMP") : shim;
    }

    private FrameCapture() {
        /* empty */
    }
}
