import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Shadow;
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

    /** How finely a shadow is drawn, which is what the client asks the toolkit for. */
    private static final int SHADOW_DETAIL = 32;

    /** What the distance fades towards, and how far away it has finished fading. */
    private static final int FOG_COLOUR = 0x8090C0;
    private static final int FOG_RANGE = 3000;
    private static final int FEATURES = 64;
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

        /*
         * How finely a shadow is drawn. The client asks for this once and nothing here ever did,
         * which left every shadow drawn at one place to the world unit.
         */
        toolkit.X(SHADOW_DETAIL);

        /*
         * The colour the distance fades everything towards, and how far away the fade is
         * complete. The client asks for this whenever the player has fog turned on, and nothing
         * here had ever asked for it at all.
         */
        toolkit.L(FOG_COLOUR, FOG_RANGE, 0);

        toolkit.method7938(toolkit.createHeap(POOL_SIZE));

        /*
         * The ground is drawn through a worker, one per thread, which the client asks the toolkit
         * to make and then claims for whichever thread is drawing.
         */
        toolkit.allocateThreads(1);
        toolkit.linkThreads(0);

        /*
         * Read once and shared by both textured models. Reading the cache a second time hands
         * back nothing, and a scene handed nothing quietly draws nothing.
         */
        var textured = texturedMesh();
        
        var props = new Scene.Props(
            gradient,
            toolkit.createModel(CacheMesh.anyUntextured(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            toolkit.createModel(fewFaces(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            toolkit.createMatrix(),
            toolkit.createFont(HandFont.metrics(), HandFont.letters(), true),
            toolkit.createFont(HandFont.metrics(), HandFont.letters(), false),
            HandGround.build(toolkit),
            CacheMesh.anyUntextured(),
            texturedModel(toolkit, textured, FEATURES, FORCED_TEXTURE),
            toolkit.createModel(fadedMesh(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            texturedModel(toolkit, textured, FEATURES | TEXTURES_OFF, TEXTURE_THAT_MAY_GO),
            HandGround.buildTextured(toolkit),
            HandGround.buildShaped(toolkit),
            HandGround.buildSmooth(toolkit),
            roundPointModel(toolkit),
            rockModel(toolkit),
            seenThroughModel(toolkit, textured),
            HandGround.buildOverlaid(toolkit),
            HandGround.buildHollow(toolkit),
            HandGround.buildShadowed(toolkit, shadowOf(toolkit)),
            HandGround.buildBlended(toolkit),
            namedModel(toolkit, CacheMesh.STAIRS, false, EVERY_FUNCTION),
            toolkit.createModel(PriorityMesh.INSTANCE.build(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            toolkit.createModel(BillboardMesh.INSTANCE.build(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST),
            CacheMesh.numbered(CacheMesh.STAIRS).orElse(null),
            CacheMesh.numbered(CacheMesh.BLACK_BACKED).orElse(null),
            toolkit.createModel(DoubledMesh.INSTANCE.build(), FUNCTIONS, FEATURES,
                AMBIENT, CONTRAST));

        var manifest = new ArrayList<String>();

        for (var scene : Scene.ALL) {
            for (var repeat = 0; repeat < REPEATS; repeat++) {
                drawOnce(toolkit, scene, props, camera);
                manifest.add(scene.title()
                    + (scene.written() ? "" : " (outstanding)")
                    + (scene.drawsNothing() ? " (empty on purpose)" : ""));
            }
        }

        toolkit.method7950();
        writeManifest(manifest);
        window.dispose();
    }

    private static void drawOnce(Toolkit toolkit, Scene scene, Scene.Props props,
                                 Matrix camera) throws Exception {
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
    private static Mesh fewFaces() throws Exception {
        var mesh = CacheMesh.anyUntextured();
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
     * A texture the player is allowed to turn off, which the one above is not. A model built
     * asking for textures off keeps the first and loses this one.
     */
    private static final short TEXTURE_THAT_MAY_GO = 7;

    /**
     * A texture whose blend mode is the one that reads an alpha out of the texture itself rather
     * than off the face, which is every third number.
     *
     * It also has to be one the player is not allowed to turn off, because the scenes are built
     * asking for textures off and a texture that may go would go.
     */
    private static final short TEXTURE_SEEN_THROUGH = 8;

    /**
     * A texture whose blend mode says it carries no alpha at all, and says where it is not there
     * by leaving a texel empty. It is even for the same reason as the one above.
     */
    private static final short TEXTURE_EMPTY_WHERE_BARE = 4;

    /**
     * The feature the client asks for when the player has turned textures off. It is one of the
     * features a model is built with rather than something the toolkit is told once, so a model
     * built before the player changed their mind keeps the textures it was built with.
     */
    private static final int TEXTURES_OFF = 0x40;

    /** The way of placing a texture that wraps it round a point rather than laying it flat. */
    private static final int ROUND_A_POINT = 3;

    /**
     * The mesh both textured models are built from.
     *
     * It is read once and shared. Reading the cache a second time hands back nothing, and a
     * scene handed nothing quietly draws nothing, which is a check that cannot fail.
     */
    private static Mesh texturedMesh() throws Exception {
        var mesh = CacheMesh.anyTextured();
        if (mesh.isEmpty()) {
            System.out.println("no textured model in the cache");
            return null;
        }

        return mesh.get();
    }

    /**
     * A model out of the cache that asks for a texture to be wrapped round a point.
     *
     * Only the faces that ask for it are given a texture, so that a difference in the picture is
     * a difference in that one way of placing a texture and not in the two the rest of the model
     * would have used.
     */
    private static Model roundPointModel(Toolkit toolkit) throws Exception {
        var mesh = CacheMesh.numbered(CacheMesh.ROUND_A_POINT);
        if (mesh.isEmpty()) {
            System.out.println("no model in the cache places a texture round a point");
            return null;
        }

        var held = mesh.get();
        var ways = held.texMappingType;
        var placed = 0;

        for (var face = 0; face < held.faceCount; face++) {
            var space = held.faceTexSpace[face];
            if (space >= 0 && space < ways.length && ways[space] == ROUND_A_POINT) {
                held.faceTexture[face] = FORCED_TEXTURE;
                placed++;
            } else {
                held.faceTexture[face] = -1;
            }
        }

        System.out.println("round a point: " + placed + " of " + held.faceCount + " faces");
        return toolkit.createModel(held, FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
    }

    /**
     * A reported piece of scenery, with every face given a texture so that where the texture
     * lands is what the picture shows.
     */
    private static Model rockModel(Toolkit toolkit) throws Exception {
        return namedModel(toolkit, CacheMesh.ROCK, true);
    }

    /**
     * A model out of the cache, wearing whatever it says it wears.
     *
     * Putting one texture on every face says where the texture lands but nothing about which
     * texture a face asked for, and which one a face asks for decides how it is drawn: whether it
     * is seen through, whether it is left off where it is bare, and whether the player is allowed
     * to turn it off at all.
     */
    private static Model namedModel(Toolkit toolkit, int group) throws Exception {
        return namedModel(toolkit, group, false);
    }

    /** Everything a model may be asked to do, so that a scene may turn one about. */
    private static final int EVERY_FUNCTION = 0xFFFF;

    private static Model namedModel(Toolkit toolkit, int group, boolean forced) throws Exception {
        return namedModel(toolkit, group, forced, FUNCTIONS);
    }

    private static Model namedModel(Toolkit toolkit, int group, boolean forced, int functions)
            throws Exception {
        var mesh = CacheMesh.numbered(group);
        if (mesh.isEmpty()) {
            System.out.println("model " + group + " is not in the cache");
            return null;
        }

        var held = mesh.get();
        if (forced) {
            for (var face = 0; face < held.faceCount; face++) {
                held.faceTexture[face] = FORCED_TEXTURE;
            }
        }

        return toolkit.createModel(held, FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
    }

    /** What a model has to be built asking for before it will hand over a shadow. */
    private static final int MAY_CAST_A_SHADOW = 0x40000;

    /**
     * The shadow of a model out of the cache, which is the only way to come by one.
     *
     * A model only carries a shadow when it was built asking for one, and none of the models the
     * other scenes use was.
     */
    private static Shadow shadowOf(Toolkit toolkit) throws Exception {
        var model = toolkit.createModel(CacheMesh.anyUntextured(),
            FUNCTIONS | MAY_CAST_A_SHADOW, FEATURES, AMBIENT, CONTRAST);
        var shadow = model.ba(null);

        if (shadow == null) {
            System.out.println("the model handed over no shadow");
        }

        return shadow;
    }

    /**
     * The same model with every other face wearing a texture that carries an alpha of its own and
     * the rest wearing none.
     *
     * Every third face wears each of the three: one seen through its own alpha, one that says
     * where it is not there by leaving a texel empty, and one wearing none at all. A model
     * wearing one everywhere says nothing about the order faces are drawn in, and a tree is not
     * like that: its leaves are seen through and its trunk is not, and which is drawn first
     * decides which is in front.
     */
    private static Model seenThroughModel(Toolkit toolkit, Mesh held) {
        if (held == null) {
            return null;
        }

        for (var face = 0; face < held.faceCount; face++) {
            held.faceTexture[face] = switch (face % 3) {
                case 0 -> TEXTURE_SEEN_THROUGH;
                case 1 -> TEXTURE_EMPTY_WHERE_BARE;
                default -> (short) -1;
            };
        }

        return toolkit.createModel(held, FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
    }

    private static Model texturedModel(Toolkit toolkit, Mesh held, int features, short texture)
            throws Exception {
        if (held == null) {
            return null;
        }

        /*
         * Every face is put on the same texture so that a texture either shows across the whole
         * model or does not show at all. A handful of textured faces among three hundred says
         * nothing either way. Which space a face belongs to is left as the mesh had it, and the
         * two the model carries are placed different ways, so the scene draws both.
         */
        for (var face = 0; face < held.faceCount; face++) {
            held.faceTexture[face] = texture;
        }

        return toolkit.createModel(held, FUNCTIONS, features, AMBIENT, CONTRAST);
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
    private static Mesh fadedMesh() throws Exception {
        var mesh = CacheMesh.anyUntextured();
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
