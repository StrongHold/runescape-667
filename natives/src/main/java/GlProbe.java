import jaggl.OpenGL;
import rs2.client.loading.library.LibraryManager;

import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Records what an OpenGL binding does, so that ours can be held against the shipped one.
 *
 * A binding is not a renderer. It carries arguments across to a call of the same name and carries
 * an answer back, and both bindings here talk to the same driver on the same machine, so what can
 * go wrong is the carrying: an argument in the wrong place, an array read from the wrong offset, a
 * value widened the wrong way. A picture would find some of that. Reading the state back after
 * setting it finds all of it, and over every call rather than the handful a scene happens to make.
 *
 * So most of this sets a piece of state and then asks for it back. What is drawn at the end is
 * there for the calls that set nothing readable: what a vertex and a colour and a texture do is
 * only visible in the pixels they produce.
 *
 * The library and the file to write are the arguments.
 */
public final class GlProbe {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 256;

    /** What the client asks a context for: eight bits a channel, depth, no stencil, no multisampling. */
    private static final int CHANNEL = 8;
    private static final int DEPTH = 24;
    private static final int STENCIL = 0;
    private static final int SAMPLES = 0;

    private static final int VENDOR = 0x1F00;
    private static final int RENDERER = 0x1F01;
    private static final int VERSION = 0x1F02;

    private static final int SAMPLE_BUFFERS = 0x80A8;
    private static final int SAMPLES_HELD = 0x80A9;
    private static final int RED_BITS = 0x0D52;
    private static final int DEPTH_BITS = 0x0D56;
    private static final int STENCIL_BITS = 0x0D57;

    private static final int DEPTH_TEST = 0x0B71;
    private static final int BLEND = 0x0BE2;
    private static final int CULL_FACE = 0x0B44;
    private static final int ALPHA_TEST = 0x0BC0;
    private static final int STENCIL_TEST = 0x0B90;
    private static final int FOG = 0x0B60;
    private static final int TEXTURE_2D = 0x0DE1;
    private static final int SCISSOR_TEST = 0x0C11;

    private static final int BLEND_SRC = 0x0BE1;
    private static final int BLEND_DST = 0x0BE0;
    private static final int CULL_FACE_MODE = 0x0B45;
    private static final int DEPTH_FUNC = 0x0B74;
    private static final int SHADE_MODEL = 0x0B54;
    private static final int MATRIX_MODE = 0x0BA0;
    private static final int VIEWPORT = 0x0BA2;
    private static final int SCISSOR_BOX = 0x0C10;
    private static final int POLYGON_MODE = 0x0B40;
    private static final int ALPHA_TEST_FUNC = 0x0BC1;
    private static final int DEPTH_WRITEMASK = 0x0B72;
    private static final int COLOR_WRITEMASK = 0x0C23;
    private static final int TEXTURE_BINDING_2D = 0x8069;
    private static final int FOG_MODE = 0x0B65;

    private static final int COLOR_CLEAR_VALUE = 0x0C22;
    private static final int DEPTH_CLEAR_VALUE = 0x0B73;
    private static final int LINE_WIDTH = 0x0B21;
    private static final int ALPHA_TEST_REF = 0x0BC2;
    private static final int FOG_COLOR = 0x0B66;
    private static final int FOG_START = 0x0B63;
    private static final int FOG_END = 0x0B64;
    private static final int MODELVIEW_MATRIX = 0x0BA6;
    private static final int PROJECTION_MATRIX = 0x0BA7;

    private static final int MODELVIEW = 0x1700;
    private static final int PROJECTION = 0x1701;
    private static final int SRC_ALPHA = 0x0302;
    private static final int ONE_MINUS_SRC_ALPHA = 0x0303;
    private static final int FRONT = 0x0404;
    private static final int FRONT_AND_BACK = 0x0408;
    private static final int LEQUAL = 0x0203;
    private static final int GREATER = 0x0204;
    private static final int FLAT = 0x1D00;
    private static final int LINE = 0x1B01;
    private static final int LINEAR = 0x2601;
    private static final int NEAREST = 0x2600;
    private static final int TEXTURE_MIN_FILTER = 0x2801;
    private static final int TEXTURE_MAG_FILTER = 0x2800;
    private static final int RGBA = 0x1908;
    private static final int UNSIGNED_BYTE = 0x1401;
    private static final int TRIANGLES = 0x0004;
    private static final int COLOR_BUFFER_BIT = 0x4000;
    private static final int DEPTH_BUFFER_BIT = 0x0100;

    /**
     * Every array is read at an offset rather than from its start, because an offset handled
     * wrongly is the one fault a binding can have that reading from zero would never show.
     */
    private static final int OFFSET = 3;

    private static final int TEXTURE_SIZE = 4;

    /**
     * Where the context the answers were given under is written.
     *
     * An OpenGL answer means nothing without the context it came from, and the two are read back
     * separately, so the context goes in its own file beside the answers rather than among them.
     */
    private static Path beside(Path answers) {
        return Path.of(answers.toString().replace(".txt", "-context.txt"));
    }

    public static void main(String[] arguments) {
        var parsed = CommandLine.parse("glProbe", new ProbeArgs(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(ProbeArgs args) {
        try {
            Watchdog.arm("The OpenGL binding probe", 120);
            LibraryManager.putLibrary(args.library(), "jaggl");
            LibraryManager.loadNative(GlProbe.class, "jaggl");

            var answers = new ArrayList<String>();
            var context = new ArrayList<String>();
            probe(answers, context);

            Files.write(args.answers(), answers);
            Files.write(beside(args.answers()), context);
            System.out.println("the binding carried " + answers.size() + " answers back unchanged");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void probe(List<String> answers, List<String> context) throws Exception {
        var canvas = new Canvas();
        canvas.setSize(WIDTH, HEIGHT);

        var window = new Frame("opengl binding probe");
        window.add(canvas);
        window.pack();
        window.setVisible(true);
        Thread.sleep(1000);

        var binding = new OpenGL();
        long surface = binding.init(canvas, CHANNEL, CHANNEL, CHANNEL, DEPTH, STENCIL, SAMPLES);
        if (surface == 0L) {
            throw new IllegalStateException("the binding gave no surface");
        }

        /*
         * The client makes the surface current in a separate step and so does this, because only
         * one of the two bindings does it as part of building the context and the sequence has to
         * be the client's rather than either library's.
         */
        if (!binding.setSurface(surface)) {
            throw new IllegalStateException("the binding would not make its surface current");
        }

        askWhoIsDrawing(answers, context);
        askSwitches(answers);
        askWholeNumbers(answers);
        askFractions(answers);
        askOffsets(answers);
        askMatrices(answers);
        askTexture(answers);
        askPixels(answers);
        context.add("pixels drawn = " + drawnPixels);

        binding.releaseSurface(canvas, surface);
        binding.release();
    }

    /**
     * Both bindings reach the same driver, so this says the context is real rather than that the
     * binding is right. A probe that quietly ran without one would compare two sets of nothing.
     */
    /**
     * What the context is, kept apart from what the binding carries.
     *
     * These describe the context a binding built rather than anything it carried afterwards, and
     * the two bindings do not build the same one. They go in their own file so that the carrying
     * can be held to being identical while the building is reported instead.
     */
    private static void askWhoIsDrawing(List<String> answers, List<String> context) {
        String version = OpenGL.glGetString(VERSION);
        if (version == null) {
            throw new IllegalStateException("the binding made no context current, so nothing below means anything");
        }
        context.add("vendor = " + OpenGL.glGetString(VENDOR));
        context.add("renderer = " + OpenGL.glGetString(RENDERER));
        context.add("version = " + version);
        context.add("sample buffers = " + whole(SAMPLE_BUFFERS));
        context.add("samples = " + whole(SAMPLES_HELD));
        context.add("red bits = " + whole(RED_BITS));
        context.add("depth bits = " + whole(DEPTH_BITS));
        context.add("stencil bits = " + whole(STENCIL_BITS));
    }

    private static void askSwitches(List<String> answers) {
        int[] switches = {DEPTH_TEST, BLEND, CULL_FACE, ALPHA_TEST, STENCIL_TEST, FOG, TEXTURE_2D, SCISSOR_TEST};
        for (int which : switches) {
            OpenGL.glEnable(which);
            agrees(answers, "enabled " + which, 1, whole(which));
            OpenGL.glDisable(which);
            agrees(answers, "disabled " + which, 0, whole(which));
        }
        OpenGL.glEnable(DEPTH_TEST);
        OpenGL.glEnable(BLEND);
        OpenGL.glEnable(CULL_FACE);
    }

    private static void askWholeNumbers(List<String> answers) {
        OpenGL.glBlendFunc(SRC_ALPHA, ONE_MINUS_SRC_ALPHA);
        agrees(answers, "blend source", SRC_ALPHA, whole(BLEND_SRC));
        agrees(answers, "blend destination", ONE_MINUS_SRC_ALPHA, whole(BLEND_DST));

        OpenGL.glCullFace(FRONT);
        agrees(answers, "cull face", FRONT, whole(CULL_FACE_MODE));

        OpenGL.glDepthFunc(LEQUAL);
        agrees(answers, "depth function", LEQUAL, whole(DEPTH_FUNC));

        OpenGL.glShadeModel(FLAT);
        agrees(answers, "shade model", FLAT, whole(SHADE_MODEL));

        OpenGL.glMatrixMode(PROJECTION);
        agrees(answers, "matrix mode", PROJECTION, whole(MATRIX_MODE));

        OpenGL.glViewport(11, 22, 100, 50);
        agrees(answers, "viewport", "[11, 22, 100, 50]", wholes(VIEWPORT, 4));

        OpenGL.glScissor(3, 5, 70, 90);
        agrees(answers, "scissor box", "[3, 5, 70, 90]", wholes(SCISSOR_BOX, 4));

        OpenGL.glPolygonMode(FRONT_AND_BACK, LINE);
        agrees(answers, "polygon mode", "[" + LINE + ", " + LINE + "]", wholes(POLYGON_MODE, 2));

        OpenGL.glAlphaFunc(GREATER, 0.25F);
        agrees(answers, "alpha function", GREATER, whole(ALPHA_TEST_FUNC));

        OpenGL.glDepthMask(false);
        agrees(answers, "depth writable", 0, whole(DEPTH_WRITEMASK));
        OpenGL.glDepthMask(true);

        OpenGL.glColorMask(true, false, true, false);
        agrees(answers, "colour writable", "[1, 0, 1, 0]", wholes(COLOR_WRITEMASK, 4));
        OpenGL.glColorMask(true, true, true, true);

        OpenGL.glFogi(FOG_MODE, LINEAR);
        agrees(answers, "fog mode", LINEAR, whole(FOG_MODE));
    }

    private static void askFractions(List<String> answers) {
        OpenGL.glClearColor(0.25F, 0.5F, 0.75F, 1F);
        agrees(answers, "clear colour", "[0.25, 0.5, 0.75, 1.0]", fractions(COLOR_CLEAR_VALUE, 4));

        OpenGL.glClearDepth(0.5F);
        agrees(answers, "clear depth", "[0.5]", fractions(DEPTH_CLEAR_VALUE, 1));

        OpenGL.glLineWidth(1F);
        agrees(answers, "line width", "[1.0]", fractions(LINE_WIDTH, 1));

        agrees(answers, "alpha reference", "[0.25]", fractions(ALPHA_TEST_REF, 1));

        OpenGL.glFogf(FOG_START, 10F);
        OpenGL.glFogf(FOG_END, 200F);
        agrees(answers, "fog start", "[10.0]", fractions(FOG_START, 1));
        agrees(answers, "fog end", "[200.0]", fractions(FOG_END, 1));

        float[] colour = new float[OFFSET + 4];
        colour[OFFSET] = 0.1F;
        colour[OFFSET + 1] = 0.2F;
        colour[OFFSET + 2] = 0.3F;
        colour[OFFSET + 3] = 0.4F;
        OpenGL.glFogfv(FOG_COLOR, colour, OFFSET);
        agrees(answers, "fog colour", "[0.1, 0.2, 0.3, 0.4]", fractions(FOG_COLOR, 4));
    }

    /**
     * An answer written into the middle of an array. Everything outside the part written has to
     * come back untouched, which is what catches a binding that ignores the offset it was given.
     */
    private static void askOffsets(List<String> answers) {
        int[] room = new int[OFFSET + 4 + OFFSET];
        java.util.Arrays.fill(room, -1);
        OpenGL.glGetIntegerv(VIEWPORT, room, OFFSET);
        agrees(answers, "viewport at an offset", "[-1, -1, -1, 11, 22, 100, 50, -1, -1, -1]", java.util.Arrays.toString(room));

        float[] fractions = new float[OFFSET + 4 + OFFSET];
        java.util.Arrays.fill(fractions, -1F);
        OpenGL.glGetFloatv(COLOR_CLEAR_VALUE, fractions, OFFSET);
        agrees(answers, "clear colour at an offset", "[-1.0, -1.0, -1.0, 0.25, 0.5, 0.75, 1.0, -1.0, -1.0, -1.0]", java.util.Arrays.toString(fractions));
    }

    private static void askMatrices(List<String> answers) {
        OpenGL.glMatrixMode(PROJECTION);
        OpenGL.glLoadIdentity();
        OpenGL.glOrtho(0D, WIDTH, HEIGHT, 0D, -1D, 1D);
        answers.add("projection = " + fractions(PROJECTION_MATRIX, 16));

        OpenGL.glMatrixMode(MODELVIEW);
        OpenGL.glLoadIdentity();
        OpenGL.glTranslatef(1F, 2F, 3F);
        OpenGL.glRotatef(30F, 0F, 1F, 0F);
        answers.add("modelview = " + fractions(MODELVIEW_MATRIX, 16));

        float[] held = new float[OFFSET + 16];
        for (int at = 0; at < 16; at++) {
            held[OFFSET + at] = at % 5 == 0 ? 1F : at / 100F;
        }
        OpenGL.glLoadMatrixf(held, OFFSET);
        answers.add("loaded at an offset = " + fractions(MODELVIEW_MATRIX, 16));

        OpenGL.glLoadIdentity();
    }

    private static void askTexture(List<String> answers) {
        int[] names = new int[OFFSET + 1];
        OpenGL.glGenTextures(1, names, OFFSET);
        int name = names[OFFSET];
        agrees(answers, "texture named", true, name > 0);

        OpenGL.glBindTexture(TEXTURE_2D, name);
        agrees(answers, "texture bound", true, whole(TEXTURE_BINDING_2D) == name);

        int[] texels = new int[OFFSET + TEXTURE_SIZE * TEXTURE_SIZE];
        for (int at = 0; at < TEXTURE_SIZE * TEXTURE_SIZE; at++) {
            texels[OFFSET + at] = 0xFF000000 | (at * 0x111111);
        }
        OpenGL.glTexImage2Di(TEXTURE_2D, 0, RGBA, TEXTURE_SIZE, TEXTURE_SIZE, 0,
                RGBA, UNSIGNED_BYTE, texels, OFFSET);
        OpenGL.glTexParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST);
        OpenGL.glTexParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);

        agrees(answers, "texture error", 0, OpenGL.glGetError());
    }

    /**
     * What a vertex, a colour, a clear and a texture actually put on the screen. These set nothing
     * that can be read back, so the pixels are the only place a fault in them can show.
     *
     * The texture is read here rather than through glGetTexImage because the shipped library does
     * not have that call. Drawing with it tests the same upload by a path both libraries have.
     */
    private static long drawnPixels;

    private static void askPixels(List<String> answers) {
        OpenGL.glDisable(TEXTURE_2D);
        OpenGL.glDisable(CULL_FACE);
        OpenGL.glDisable(BLEND);
        OpenGL.glDisable(DEPTH_TEST);
        OpenGL.glPolygonMode(FRONT_AND_BACK, 0x1B02);
        OpenGL.glViewport(0, 0, WIDTH, HEIGHT);

        OpenGL.glMatrixMode(PROJECTION);
        OpenGL.glLoadIdentity();
        OpenGL.glOrtho(0D, WIDTH, HEIGHT, 0D, -1D, 1D);
        OpenGL.glMatrixMode(MODELVIEW);
        OpenGL.glLoadIdentity();

        OpenGL.glClearColor(0.125F, 0.25F, 0.375F, 1F);
        OpenGL.glClear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT);

        OpenGL.glShadeModel(0x1D01);
        OpenGL.glBegin(TRIANGLES);
        OpenGL.glColor3f(1F, 0F, 0F);
        OpenGL.glVertex3f(16F, 16F, 0F);
        OpenGL.glColor3f(0F, 1F, 0F);
        OpenGL.glVertex3f(200F, 40F, 0F);
        OpenGL.glColor3f(0F, 0F, 1F);
        OpenGL.glVertex3f(60F, 220F, 0F);
        OpenGL.glEnd();

        OpenGL.glEnable(TEXTURE_2D);
        OpenGL.glColor3f(1F, 1F, 1F);
        OpenGL.glBegin(TRIANGLES);
        OpenGL.glTexCoord2f(0F, 0F);
        OpenGL.glVertex3f(140F, 140F, 0F);
        OpenGL.glTexCoord2f(1F, 0F);
        OpenGL.glVertex3f(240F, 140F, 0F);
        OpenGL.glTexCoord2f(1F, 1F);
        OpenGL.glVertex3f(240F, 240F, 0F);
        OpenGL.glEnd();
        OpenGL.glDisable(TEXTURE_2D);
        OpenGL.glFinish();

        int[] pixels = new int[OFFSET + WIDTH * HEIGHT];
        OpenGL.glReadPixelsi(0, 0, WIDTH, HEIGHT, RGBA, UNSIGNED_BYTE, pixels, OFFSET);

        agrees(answers, "untouched before the pixels", "0 0 0", pixels[0] + " " + pixels[1] + " " + pixels[2]);
        drawnPixels = sumOf(pixels, OFFSET);
        long drawn = drawnPixels;
        if (drawn == 0L) {
            throw new IllegalStateException("nothing reached the pixels");
        }
        agrees(answers, "error", 0, OpenGL.glGetError());
    }

    /**
     * Records an answer and holds it to what was set.
     *
     * A binding carries an argument across and carries an answer back, so what it is for is that
     * the two agree. Nothing else here can tell whether they do: the driver on the far side is the
     * same driver either way, and a value that survives the journey is the whole of what is being
     * asked.
     */
    private static void agrees(List<String> answers, String what, Object asked, Object answered) {
        String said = String.valueOf(answered);
        answers.add(what + " = " + said);
        if (!String.valueOf(asked).equals(said)) {
            throw new IllegalStateException(what + " was set to " + asked + " and came back " + said);
        }
    }

    private static int whole(int name) {
        int[] held = new int[1];
        OpenGL.glGetIntegerv(name, held, 0);
        return held[0];
    }

    private static String wholes(int name, int count) {
        int[] held = new int[count];
        OpenGL.glGetIntegerv(name, held, 0);
        return java.util.Arrays.toString(held);
    }

    private static String fractions(int name, int count) {
        float[] held = new float[count];
        OpenGL.glGetFloatv(name, held, 0);
        return java.util.Arrays.toString(held);
    }

    private static long sumOf(int[] values, int from) {
        long total = 0;
        for (int at = from; at < values.length; at++) {
            total = total * 31 + values[at];
        }
        return total;
    }

    private GlProbe() {
        /* empty */
    }
}
