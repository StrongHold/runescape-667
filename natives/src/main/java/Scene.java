import com.jagex.graphics.Font;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;

import java.util.List;

/**
 * A picture the toolkit is asked to draw, named so that a difference can be reported against it.
 *
 * Each scene is drawn on its own, into its own frame, by the shipped toolkit and by ours. Keeping
 * them apart means a failure names the thing that broke rather than a coordinate in one crowded
 * picture, and it means a scene can cover a native thoroughly without having to find room for
 * itself beside every other scene.
 *
 * Add a scene for each native as it is written. A native with no scene is a native nothing checks.
 */
public sealed interface Scene {

    int WIDTH = 512;
    int HEIGHT = 384;
    int CLEAR_COLOUR = 0x202080;

    /** The toolkit turns through this many steps of a circle. */
    int TURN = 16384;

    /** How close to the camera a model may come before it is cut away. */
    int NEAR = 50;

    /** How far in front of the camera the model in the geometry scene sits. */
    int DEPTH = 900;

    /** How far apart the copies in the geometry scene stand. */
    int SPREAD = 420;

    List<Scene> ALL = List.of(
        new Sprites(),
        new AlphaSweep(),
        new BlendMatrix(),
        new BlendMode(),
        new OutlineAndLine(),
        new Clip(),
        new Geometry(),
        new FewFaces(),
        new DepthOrder(),
        new SubClip(),
        new ReadBack(),
        new SpriteMatrix(),
        new IndexedSprites(),
        new Text(),
        new StretchedAndTiled(),
        new Masked(),
        new Turned(),
        new Copied(),
        new Animated(),
        new Terrain()
    );

    /**
     * What every scene is given. A scene uses what it needs and ignores the rest, which keeps one
     * scene from having to know what another one wanted.
     */
    record Props(Sprite gradient, Model model, Model simple, Matrix matrix,
                 Font mono, Font proportional, Ground ground, Mesh mesh) {
        /* empty */
    }

    void draw(Toolkit toolkit, Props props);

    /**
     * Whether our toolkit is expected to draw this scene yet.
     *
     * A scene for a native that is not written is still worth drawing, because the shipped
     * toolkit's frame is the thing the implementation will be written against. It is reported as
     * outstanding rather than as a failure, so that it cannot hide a real difference in a scene
     * that is finished.
     */
    default boolean written() {
        return true;
    }

    /**
     * What a difference in this scene is reported against.
     */
    default String title() {
        return getClass().getSimpleName();
    }

    private static int grey(int value) {
        return (value << 16) | (value << 8) | value;
    }

    /**
     * A sprite drawn as it is, drawn again as a flat shape, and drawn off each edge.
     */
    record Sprites() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            props.gradient().render(10, 10, 0, 0xFFFFFF, 0);
            props.gradient().render(10, 120, 3, 0xFFFFFF, 0);
            props.gradient().render(-20, 240, 0, 0xFFFFFF, 0);
            props.gradient().render(WIDTH - 40, 300, 0, 0xFFFFFF, 0);
        }
    }

    /**
     * The same fill at every alpha the client can ask for, over a background that is not flat.
     */
    record AlphaSweep() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.aa(0, 100, WIDTH, 80, 0xFF806040, 0);

            for (var step = 0; step <= 16; step++) {
                var alpha = step == 16 ? 0xFF : step * 16;
                toolkit.aa(step * 30, 20, 28, 240, (alpha << 24) | 0xCC3311, 1);
            }
        }
    }

    /**
     * Every source value blended over a spread of destination values, one alpha per band.
     *
     * A blend can be wrong at a handful of inputs and right everywhere else, and two formulas
     * that differ only in where they lose their fraction agree almost everywhere. This leaves
     * nowhere for that to hide: each column is one source value and each band is one destination
     * and alpha, so the frame carries the whole function rather than samples of it.
     */
    record BlendMatrix() implements Scene {

        private static final int[] ALPHAS = {1, 63, 64, 65, 127, 128, 129, 191, 192, 253, 254, 255};

        @Override
        public void draw(Toolkit toolkit, Props props) {
            for (var band = 0; band < ALPHAS.length; band++) {
                var y = band * 32;

                toolkit.aa(0, y, 256, 32, 0xFF000000 | grey(band * 23), 0);

                for (var source = 0; source < 256; source++) {
                    toolkit.aa(source, y, 1, 32, (ALPHAS[band] << 24) | grey(source), 1);
                }
            }
        }
    }

    /**
     * Each way of putting a colour down, over the background and over an already drawn shape, so
     * a blend that only looks right against the clear colour still fails.
     */
    record BlendMode() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.aa(20, 20, 470, 60, 0xFF806040, 0);

            toolkit.aa(40, 40, 80, 20, 0xFFCC3311, 0);
            toolkit.aa(160, 40, 80, 20, 0x80CC3311, 1);
            toolkit.aa(280, 40, 80, 20, 0xFFCC3311, 2);
            toolkit.aa(400, 40, 80, 20, 0xFFF0F0F0, 2);

            toolkit.aa(40, 120, 80, 40, 0x8033CC11, 1);
            toolkit.aa(160, 120, 80, 40, 0x8033CC11, 1);
            toolkit.aa(160, 120, 80, 40, 0x8033CC11, 1);
        }
    }

    /**
     * Rectangles and lines in all three modes, including lines that run off every edge.
     */
    record OutlineAndLine() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.outlineRect(20, 20, 120, 60, 0xFF33CC11, 0);
            toolkit.outlineRect(160, 20, 120, 60, 0x8033CC11, 1);
            toolkit.outlineRect(300, 20, 120, 60, 0xFF33CC11, 2);
            toolkit.outlineRect(440, 20, 1, 60, 0xFF33CC11, 0);

            toolkit.line(20, 120, 490, 200, 0xFF11CCCC, 0);
            toolkit.line(490, 130, 20, 210, 0x8011CCCC, 1);
            toolkit.line(20, 220, 30, 370, 0xFFCCCC11, 0);
            toolkit.line(60, 370, 50, 220, 0xFFCCCC11, 0);
            toolkit.line(100, 300, 100, 300, 0xFFFFFFFF, 0);
            toolkit.line(-50, 250, 560, 260, 0xFFFF00FF, 0);
            toolkit.line(200, -50, 260, 430, 0xFF00FFFF, 0);
        }
    }

    /**
     * A model drawn beside copies of itself.
     *
     * A copy is handed only the right to do what its mask allows, and the light it was wearing is
     * taken off it whenever the mask lets it change something the light depends on. Drawing the
     * original after the copies have been changed also shows whether a copy shares an array with
     * the model it came from, which it must not.
     */
    record Copied() implements Scene {

        private static final int FUNCTIONS = 0xFFFF;
        private static final int FEATURES = 64;
        private static final int AMBIENT = 64;
        private static final int CONTRAST = 768;

        /** Enough to turn a model, enough to recolour one, and everything. */
        private static final int[] MASKS = {0x5, 0x4000, 0xFFFF};

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            var model = toolkit.createModel(props.mesh(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);

            for (var step = 0; step < MASKS.length; step++) {
                var copy = model.copy((byte) 0, MASKS[step], true);

                if ((MASKS[step] & 0x5) == 0x5) {
                    copy.a(TURN / 4);
                }
                if ((MASKS[step] & 0x4000) != 0) {
                    copy.ia((short) 0, (short) 40);
                }

                place(props, step - 1, copy);
            }

            place(props, 2, model);
        }

        private static void place(Props props, int step, Model model) {
            props.matrix().makeRotationZ(0);
            props.matrix().translate(step * SPREAD, 0, DEPTH);
            model.render(props.matrix(), null, 1);
        }
    }

    /**
     * A model with an animation applied to it, one step at a time.
     *
     * Each copy is drawn once before it is animated and once after, side by side, so the frame
     * shows what the step did as well as where it left the model. Drawing it first also settles
     * the direction each vertex faces, which the step that turns a group needs and which a model
     * that has never been drawn does not have.
     */
    record Animated() implements Scene {

        /** Everything a model may be asked to do, including turning its directions mid animation. */
        private static final int FUNCTIONS = 0xFFFF;

        private static final int FEATURES = 64;
        private static final int AMBIENT = 64;
        private static final int CONTRAST = 768;

        /** The groups the step names, which are the first few labels the model carries. */
        private static final int[] NAMED = {0, 1, 2};

        private static final int MOVE = 1;
        private static final int TURN = 2;
        private static final int STRETCH = 3;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            step(toolkit, props, -2, MOVE, 60, -30, 20, false);
            step(toolkit, props, -1, TURN, 1024, 2748, 0, false);
            step(toolkit, props, 0, TURN, 1024, 2748, 0, true);
            step(toolkit, props, 1, STRETCH, 200, 64, 128, false);
        }

        /**
         * Draws one copy where it started, applies one step to it, and draws it again beside it.
         */
        private static void step(Toolkit toolkit, Props props, int column, int kind,
                                 int x, int y, int z, boolean alsoNormals) {
            var model = (i) toolkit.createModel(props.mesh(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
            place(props, column * 2, model);

            model.NA();
            model.l(model.nativeid, 0, NAMED, 0, 0, 0, 0, false);
            model.l(model.nativeid, kind, NAMED, x, y, z, 0, alsoNormals);
            model.wa();

            place(props, column * 2 + 1, model);
        }

        private static void place(Props props, int step, Model model) {
            props.matrix().makeRotationZ(0);
            props.matrix().translate(step * (SPREAD / 2), 0, DEPTH);
            model.render(props.matrix(), null, 1);
        }
    }

    /**
     * A model turned by the model rather than by the matrix it is drawn through.
     *
     * Turning a model moves its vertices, so the same picture could be had from a matrix and this
     * would say nothing. What it covers is the rest of what a turn does: the direction each face
     * is shaded by, which either moves with the vertices or is worked out again, and the order a
     * face's corners are listed in, which a mirror reverses.
     *
     * Each copy is built from the mesh here rather than taken from the props, because turning a
     * model is a change to the model and a model shared with another scene would arrive at it
     * already turned.
     */
    record Turned() implements Scene {

        /** Everything a model may be asked to do, so that every turn here is allowed. */
        private static final int FUNCTIONS = 0xFFFF;

        private static final int FEATURES = 64;
        private static final int AMBIENT = 64;
        private static final int CONTRAST = 768;

        /** An angle that is not a whole quarter, so the turn goes through the table. */
        private static final int ODD_ANGLE = 2748;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            var upright = build(toolkit, props);
            upright.a(ODD_ANGLE);
            place(props, -2, upright);

            var carried = build(toolkit, props);
            carried.k(TURN / 4);
            place(props, -1, carried);

            var tipped = build(toolkit, props);
            tipped.FA(ODD_ANGLE);
            place(props, 0, tipped);

            var rolled = build(toolkit, props);
            rolled.VA(TURN / 2);
            place(props, 1, rolled);

            var mirrored = build(toolkit, props);
            mirrored.v();
            place(props, 2, mirrored);
        }

        private static Model build(Toolkit toolkit, Props props) {
            return toolkit.createModel(props.mesh(), FUNCTIONS, FEATURES, AMBIENT, CONTRAST);
        }

        private static void place(Props props, int step, Model model) {
            props.matrix().makeRotationZ(0);
            props.matrix().translate(step * SPREAD, 0, DEPTH);
            model.render(props.matrix(), null, 1);
        }
    }

    /**
     * A model turned to several angles, which is the first thing to need vertices projected and
     * faces filled rather than rectangles.
     */
    record Geometry() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            /*
             * Four copies side by side rather than on top of one another. Stacking them put four
             * near identical surfaces at almost the same distance and made the scene a test of
             * how ties are broken rather than of how a model is drawn.
             */
            for (var step = 0; step < 4; step++) {
                props.matrix().makeRotationZ(0);
                props.matrix().rotateAxisY(step * TURN / 4);
                props.matrix().translate((step - 2) * SPREAD, 0, DEPTH);
                props.model().render(props.matrix(), null, 1);
            }
        }
    }

    /**
     * The same model with all but a couple of its faces cut off, so that a difference can be
     * worked out by hand. Three hundred faces say only that something is wrong.
     */
    record FewFaces() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            props.matrix().applyTranslation(0, 0, DEPTH);
            props.simple().render(props.matrix(), null, 1);
        }
    }

    /**
     * Two models overlapping, drawn near first and then far, so that what covers what is decided
     * by how far away a pixel is rather than by which model was drawn last. Forgetting how far
     * away everything is between the two lets the far one cover the near one.
     *
     * The last model is drawn with the recording of distance turned off. The toolkit ignores that
     * and records it anyway, which is worth drawing so that answering the client here would show
     * up as a difference.
     */
    record DepthOrder() implements Scene {

        /** How much nearer the front model stands than the one behind it. */
        private static final int CLOSER = 300;

        /** How far the models are nudged apart, so that only part of each is covered. */
        private static final int NUDGE = 90;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            place(props, -NUDGE, DEPTH - CLOSER);
            place(props, NUDGE, DEPTH);

            toolkit.ya();
            place(props, 0, DEPTH + CLOSER);

            toolkit.C(false);
            place(props, NUDGE * 2, DEPTH - CLOSER);
            toolkit.C(true);
        }

        private static void place(Props props, int across, int away) {
            props.matrix().makeRotationZ(0);
            props.matrix().translate(across, 0, away);
            props.model().render(props.matrix(), null, 1);
        }
    }

    /**
     * The ground, seen from above and to one side.
     *
     * Terrain is a grid of tiles whose corners are shared, and a tile is handed over once and
     * drawn every frame afterwards. This is not finished: the light a tile takes, the water over
     * it, its textures and the order its faces are drawn in are all still to write, so the scene
     * reports how far apart the two pictures are rather than passing or failing.
     */
    record Terrain() implements Scene {

        /** How far back and up the eye stands from the corner of the patch. */
        private static final int BACK = 2600;

        private static final int UP = 1500;

        @Override
        public boolean written() {
            return false;
        }

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            var camera = toolkit.createMatrix();
            camera.createCamera(HandGround.TILES * HandGround.TILE / 2, UP, -BACK,
                TURN / 8, 0, 0);
            toolkit.setCamera(camera);

            var visible = new boolean[HandGround.TILES * 2][HandGround.TILES * 2];
            for (var across = 0; across < visible.length; across++) {
                for (var along = 0; along < visible.length; along++) {
                    visible[across][along] = true;
                }
            }

            props.ground().renderTiles(HandGround.TILES / 2, HandGround.TILES / 2,
                HandGround.TILES, visible, false, 0);
        }
    }

    /**
     * A shape drawn through, which is how the client rounds the corners off an interface.
     *
     * A mask is one run of pixels per row, and it is placed on the buffer when it is used rather
     * than when it is made. So the same mask is drawn through several times here at several
     * places, including places where part of it falls outside what may be drawn on.
     */
    record Masked() implements Scene {

        private static final int SIZE = 120;

        /** How far in the corners are rounded, which is what the client uses a mask for. */
        private static final int CORNER = 30;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.aa(0, 0, WIDTH, HEIGHT, 0xFF303048, 0);
            toolkit.aa(0, 150, WIDTH, 80, 0xFF906030, 0);

            var starts = new int[SIZE];
            var lengths = new int[SIZE];
            for (var row = 0; row < SIZE; row++) {
                var inset = inset(row);
                starts[row] = inset;
                lengths[row] = SIZE - inset * 2;
            }

            var mask = toolkit.createMask(SIZE, SIZE, starts, lengths);
            var picture = toolkit.createSprite(IndexedGlyph.solid(), true);

            toolkit.A(0xFF33CC11, mask, 20, 20);
            toolkit.A(0xFF3311CC, mask, 160, 130);
            toolkit.A(0xFFCC1133, mask, WIDTH - 60, 240);
            toolkit.A(0xFFCCCC11, mask, -60, 240);

            for (var step = 0; step < 6; step++) {
                picture.render(24 + step * 16, 26 + step * 14, mask, 20, 20);
            }

            props.mono().setTextColours(0xFFFFFF, 0x000000);
            props.mono().render("Masked", 170, 170, 160, 130, mask, null, null);
            props.proportional().setTextColours(0xFFFFFF, -1);
            props.proportional().render("Masked", 170, 200, 160, 130, mask, null, null);
        }

        /** How far into the row the shape starts, which rounds the corners and waists the middle. */
        private static int inset(int row) {
            if (row < CORNER) {
                return CORNER - row;
            } else if (row >= SIZE - CORNER) {
                return CORNER - (SIZE - 1 - row);
            } else {
                return row % 7;
            }
        }
    }

    /**
     * Sprites stretched to fill a rectangle and sprites tiled to fill one, at sizes that divide
     * evenly and sizes that do not.
     *
     * This is how the client draws the frame around every interface: the corners go down as they
     * are and the runs between them are stretched or repeated. A rectangle that is not a whole
     * number of copies across, or that starts outside what may be drawn on, is where both go
     * wrong, so both are asked for here.
     */
    record StretchedAndTiled() implements Scene {

        private static final int[][] SIZES = {
            {96, 48}, {24, 12}, {200, 17}, {13, 90}, {1, 40}, {70, 1}
        };

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.aa(0, 0, WIDTH, HEIGHT, 0xFF303048, 0);
            toolkit.aa(0, 120, WIDTH, 60, 0xFF906030, 0);

            var picture = toolkit.createSprite(IndexedGlyph.solid(), true);

            for (var size = 0; size < SIZES.length; size++) {
                var x = 20 + size * 80;
                picture.render(x, 20, SIZES[size][0], SIZES[size][1]);
                picture.renderTiled(x, 130, SIZES[size][0], SIZES[size][1]);
            }

            /*
             * Starting outside what may be drawn on, on every side, because how much of a stretch
             * was cut off decides where the rest of it reads from.
             */
            toolkit.T(60, 230, 460, 350);
            picture.render(-30, 210, 180, 90);
            picture.render(430, 250, 180, 90);
            picture.renderTiled(-40, 300, 200, 80);
            picture.renderTiled(420, 300, 200, 80);
            toolkit.la();

            props.gradient().render(30, HEIGHT - 40, 200, 30, 2, 0x80CC3311, 1);
            props.gradient().renderTiled(260, HEIGHT - 40, 200, 30, 3, 0xFF204080, 2);
        }
    }

    /**
     * Text, in both kinds of font, in several colours, with and without a shadow behind it.
     *
     * A monospaced font is drawn in the colour the client gives and a proportional one in the
     * colours the font came with, so a colour that reaches the wrong one of those shows here. The
     * last line of each runs off the right edge and the first sits above the top, because a letter
     * is clipped a letter at a time and the edges are where that goes wrong.
     */
    record Text() implements Scene {

        private static final String LINE = "Wg,ij AZ 019 {}[]|/\\ mmm iii";

        private static final int[] COLOURS = {0xFFFFFF, 0xFF3311, 0x33CC11, 0x000000, 0x8040FF};

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.aa(0, 0, WIDTH, HEIGHT, 0xFF303048, 0);
            toolkit.aa(0, 90, WIDTH, 60, 0xFF906030, 0);

            for (var line = 0; line < COLOURS.length; line++) {
                var y = line * 26 - 4;
                props.mono().render(LINE, 10, y, COLOURS[line], -1);
                props.mono().render(LINE, 300, y + 12, COLOURS[line], 0x000000);
            }

            for (var line = 0; line < COLOURS.length; line++) {
                var y = 150 + line * 26;
                props.proportional().render(LINE, 10, y, COLOURS[line], -1);
                props.proportional().render(LINE, 300, y + 12, COLOURS[line], 0x000000);
            }

            props.mono().render(LINE, WIDTH - 40, HEIGHT - 20, 0xFFFFFF, 0x000000);
            props.proportional().render(LINE, -60, HEIGHT - 8, 0xFFFFFF, -1);
        }
    }

    /**
     * Sprites made from the client's own kind of picture, drawn over a background that is not flat.
     *
     * The client makes most of its artwork this way, and the size a sprite answers when it is asked
     * how wide it is includes the empty room that was cut off its sides, which is what interfaces
     * are laid out by. Both are drawn here, and both sizes are drawn as a rectangle so that a
     * wrong answer shows as a rectangle of the wrong size.
     */
    record IndexedSprites() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.aa(0, 0, WIDTH, 200, 0xFF404060, 0);
            toolkit.aa(0, 60, WIDTH, 60, 0xFF906030, 0);

            var solid = toolkit.createSprite(IndexedGlyph.solid(), true);
            var translucent = toolkit.createSprite(IndexedGlyph.translucent(), true);

            for (var mode = 0; mode < 3; mode++) {
                solid.render(30 + mode * 90, 40, 0, 0xFFFFFFFF, mode);
                translucent.render(30 + mode * 90, 100, 0, 0xFFFFFFFF, mode);
                solid.render(30 + mode * 90, 160, 1, 0xFF33CC11, mode);
            }

            toolkit.outlineRect(320, 40, solid.getWidth(), solid.getHeight(), 0xFF00FF00, 0);
            toolkit.outlineRect(400, 40, translucent.getWidth(), translucent.getHeight(),
                0xFFFF00FF, 0);

            /*
             * An empty sprite is only measured, never drawn. The toolkit asks the system for its
             * pixels and does not clear them, so what it holds is whatever was there before and
             * two runs of the same scene do not agree with each other.
             */
            var empty = toolkit.createSprite(40, 24, true);
            toolkit.outlineRect(400, 120, empty.getWidth(), empty.getHeight(), 0xFF00FFFF, 0);
        }
    }

    /**
     * Every way of putting a sprite down, against every way of combining it with the colour it is
     * given, over backgrounds that are not flat.
     *
     * Five ways of combining and three ways of laying down make fifteen, and several of them agree
     * with each other whenever the colour is white or the background is empty. Each row here is
     * one way of combining and each column one colour, and every one of them is drawn over a band
     * that is already painted, so nothing can pass by agreeing only where it does not matter.
     */
    record SpriteMatrix() implements Scene {

        private static final int[] COLOURS = {
            0xFFFFFFFF, 0xFF00FF00, 0x80CC3311, 0x40FFFFFF, 0xC0336699, 0x00FFAA22
        };

        private static final int STEP = 84;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            for (var mode = 0; mode < 3; mode++) {
                var y = mode * 128;
                toolkit.aa(0, y, WIDTH, 120, 0xFF404060, 0);
                toolkit.aa(0, y + 40, WIDTH, 40, 0xFF906030, 0);

                for (var op = 0; op < 5; op++) {
                    for (var colour = 0; colour < COLOURS.length; colour++) {
                        props.gradient().render(
                            colour * STEP + op * 14, y + op * 6, op, COLOURS[colour], mode);
                    }
                }
            }
        }
    }

    /**
     * Shapes drawn, read back off the buffer, and drawn again beside themselves.
     *
     * Reading a rectangle back answers nothing a picture can check on its own. Making a sprite of
     * what came back and drawing it gives the same picture twice, so a read that takes the wrong
     * rows, or the wrong corner, or stops one short, shows as a copy that does not match what it
     * was copied from.
     */
    record ReadBack() implements Scene {

        private static final int PATCH = 120;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.aa(20, 20, PATCH, PATCH, 0xFF806040, 0);
            toolkit.outlineRect(30, 30, 60, 40, 0xFF33CC11, 0);
            toolkit.line(20, 140, 140, 20, 0xFF11CCCC, 0);
            props.gradient().render(60, 60, 0, 0xFFFFFF, 0);

            var read = toolkit.na(20, 20, PATCH, PATCH);
            toolkit.createSprite(PATCH, PATCH, PATCH, read).render(200, 20, 0, 0xFFFFFF, 0);
            toolkit.createSprite(PATCH, PATCH, PATCH, read).render(200, 180, 3, 0xFF00FF, 0);
        }
    }

    /**
     * A model inside a clip narrowed twice, which only ever narrows, and then drawn again once the
     * clip has been opened. A second call that asks for a wider rectangle has to leave the first
     * one alone.
     */
    record SubClip() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);


            props.matrix().makeRotationZ(0);
            props.matrix().translate(0, 0, DEPTH);
            props.model().render(props.matrix(), null, 1);

            toolkit.la();
            toolkit.aa(0, 366, WIDTH, 14, 0xFF00FF00, 0);
        }
    }

    /**
     * The same shapes inside a clip that cuts each of them, so a clip applied to the wrong edge,
     * or not at all, shows up rather than passing unnoticed. The last fill is drawn after the
     * clip is opened again, so a clip left closed shows up too.
     */
    record Clip() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.KA(120, 100, 380, 280);

            toolkit.aa(20, 20, 470, 340, 0xFF5D5447, 0);
            toolkit.outlineRect(60, 60, 390, 260, 0xFFCC11CC, 0);
            toolkit.line(20, 20, 490, 360, 0xFFFFFFFF, 0);
            props.gradient().render(90, 80, 0, 0xFFFFFF, 0);
            props.gradient().render(340, 240, 0, 0xFFFFFF, 0);

            toolkit.la();
            toolkit.aa(0, 370, WIDTH, 10, 0xFF00FF00, 0);
        }
    }
}
