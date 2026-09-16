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

        var toolkit = oa.create(canvas, new HandTextureSource(), Scene.WIDTH, Scene.HEIGHT);
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

        /*
         * The ground is drawn through a worker, one per thread, which the client asks the toolkit
         * to make and then claims for whichever thread is drawing.
         */
        toolkit.allocateThreads(1);
        toolkit.linkThreads(0);
        var props = new Scene.Props(
            gradient,
            toolkit.createModel(CacheMesh.anyUntextured(MODEL_FACES), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            toolkit.createModel(fewFaces(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            toolkit.createMatrix(),
            toolkit.createFont(HandFont.metrics(), HandFont.letters(), true),
            toolkit.createFont(HandFont.metrics(), HandFont.letters(), false),
            HandGround.build(toolkit),
            CacheMesh.anyUntextured(MODEL_FACES),
            texturedModel(toolkit),
            toolkit.createModel(fadedMesh(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST));

        var manifest = new ArrayList<String>();

        for (var scene : Scene.ALL) {
            for (var repeat = 0; repeat < REPEATS; repeat++) {
                drawOnce(toolkit, scene, props, camera);
                manifest.add(scene.written() ? scene.title() : scene.title() + " (outstanding)");
            }
        }

        toolkit.method7950();
        writeManifest(manifest);
        window.dispose();
    }

    private static void drawOnce(Toolkit toolkit, Scene scene, Scene.Props props,
                                 com.jagex.graphics.Matrix camera) throws Exception {
        toolkit.GA(Scene.CLEAR_COLOUR);
        toolkit.ya();

        /*
         * A scene that wants its own camera puts one in, and this puts the plain one back
         * afterwards, so that a scene is drawn the same whatever was drawn before it.
         */
        toolkit.setCamera(camera);
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
     * A model out of the cache that wears a texture, or nothing when the cache holds none.
     *
     * A mesh built here cannot stand in. A texture sits on a face through a texture space, and
     * nothing built by hand carries one, so a model built here would be textured nowhere.
     */
    /**
     * The texture every face of the textured model is put on. Its blend mode has to be one that
     * leaves the face in the ordinary pass, which rules out every third number.
     */
    private static final short FORCED_TEXTURE = 6;

    /**
     * The way of placing a texture the model is picked for.
     *
     * A model that wraps a texture round an axis happens also to carry spaces placed both other
     * written ways, and faces belonging to no space at all, so naming this one reaches all four.
     */
    private static final int WRAPPED_AROUND = 1;

    private static com.jagex.graphics.Model texturedModel(Toolkit toolkit) throws Exception {
        var mesh = CacheMesh.anyPlaced(WRAPPED_AROUND, MODEL_FACES, MODEL_FACES * 2);
        if (mesh.isEmpty()) {
            mesh = CacheMesh.anyTextured(MODEL_FACES);
        }
        if (mesh.isEmpty()) {
            System.out.println("no textured model in the cache");
            return null;
        }

        var held = mesh.get();
        /*
         * Every face is put on the same texture so that a texture either shows across the whole
         * model or does not show at all. A handful of textured faces among three hundred says
         * nothing either way. Which space a face belongs to is left as the mesh had it, and the
         * two the model carries are placed different ways, so the scene draws both.
         */
        for (var face = 0; face < held.faceCount; face++) {
            held.faceTexture[face] = FORCED_TEXTURE;
        }

        return toolkit.createModel(held, FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
    }

    /**
     * The same model from the cache with every face given an alpha of its own.
     *
     * A face the client hands over with an alpha is drawn through what is already there rather
     * than over it, and the client leans on that for a shadow under a player, for a window, and
     * for anything that fades as it appears. A model out of the cache carries no alpha at all, so
     * the alphas are put on here, running from solid at one end of the model to almost clear at
     * the other.
     */
    private static com.jagex.graphics.Mesh fadedMesh() throws Exception {
        var mesh = CacheMesh.anyUntextured(MODEL_FACES);
        mesh.faceAlpha = new byte[mesh.faceCount];

        for (var face = 0; face < mesh.faceCount; face++) {
            mesh.faceAlpha[face] = (byte) (face * 0xFF / mesh.faceCount);
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
