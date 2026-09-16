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
        new ThickLines(),
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
        new Rotated(),
        new LiftedOut(),
        new Scrolled(),
        new Circles(),
        new Turned(),
        new Copied(),
        new Animated(),
        new PointLit(),
        new Flattened(),
        new Offscreen(),
        new SharedLight(),
        new DepthShifted(),
        new Particles(),
        new Textured(),
        new Terrain()
    );

    /**
     * What every scene is given. A scene uses what it needs and ignores the rest, which keeps one
     * scene from having to know what another one wanted.
     */
    record Props(Sprite gradient, Model model, Model simple, Matrix matrix,
                 Font mono, Font proportional, Ground ground, Mesh mesh, Model textured) {
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
     * A model whose faces wear a texture, which is the only thing that reads a texture back out.
     *
     * Two copies side by side at different distances, because a texture is read through the
     * distance of the pixel it lands on and a face square to the eye would not show that.
     */
    record Textured() implements Scene {

        private static final int LEAN = 0x600;

        @Override
        public boolean written() {
            return false;
        }

        @Override
        public void draw(Toolkit toolkit, Props props) {
            if (props.textured() == null) {
                return;
            }


            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            for (var step = 0; step < 2; step++) {
                props.matrix().makeRotationZ(0);
                props.matrix().rotateAxisX(step == 0 ? 0 : LEAN);
                props.matrix().translate((step * 2 - 1) * SPREAD / 2, 0, DEPTH);
                props.textured().render(props.matrix(), null, 1);
            }
        }
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

            /*
             * Lines whose ends are a long way outside the buffer. The toolkit cuts the walk to
             * what may be drawn on before it starts, so where a line lands depends on what the
             * cut end carries with it rather than only on the two points given.
             */
            toolkit.line(-9000, 100, 9000, 300, 0xFFFF8800, 0);
            toolkit.line(9000, 340, -9000, 90, 0xFF88FF00, 0);
            toolkit.line(120, -9000, 400, 9000, 0xFF0088FF, 0);
            toolkit.line(-4000, -4000, 4000, 4000, 0xFFFFFFFF, 0);
            toolkit.line(4000, -4000, -4000, 4000, 0xFFFF4444, 0);

            /*
             * Slopes whose step does not divide evenly, where rounding the step and truncating
             * it part company.
             */
            for (var step = 1; step <= 7; step++) {
                toolkit.line(40, 260 + step * 14, 470, 262 + step * 14 + step * 3,
                    0xFFCCCCCC, 0);
            }
        }
    }

    /**
     * Filled circles, which the client draws the world map's markers out of.
     *
     * The two halves of a circle do not agree about their edges in the toolkit, so radii are
     * drawn both small and large and against every edge of what may be drawn on, where the
     * disagreement shows as a row that is one pixel wider on one side than the other.
     *
     * The client only ever asks for the blending mode. The other two are asked for here through
     * the toolkit itself, because nothing else can reach them.
     */
    record Circles() implements Scene {

        private static final int[] RADII = {0, 1, 2, 3, 7, 20, 41};

        @Override
        public void draw(Toolkit toolkit, Props props) {
            var at = (oa) toolkit;

            var x = 30;
            for (var radius : RADII) {
                at.za(x, 60, radius, 0xFF20C080, 1);
                at.za(x, 150, radius, 0x80C02040, 1);
                at.za(x, 240, radius, 0xFF3060C0, 0);
                at.za(x, 330, radius, 0x40808080, 2);
                x += 70;
            }

            at.za(0, 0, 30, 0xFFFFCC00, 1);
            at.za(WIDTH, 0, 30, 0xFFFFCC00, 1);
            at.za(0, HEIGHT, 30, 0xFFCC00FF, 1);
            at.za(WIDTH, HEIGHT, 30, 0xFFCC00FF, 1);
            at.za(WIDTH / 2, -10, 40, 0xFF00CCCC, 1);

            /*
             * Circles whose middle sits outside what may be drawn on. The lower half of a circle
             * starts at the middle rather than at the first row that may be drawn on, so one
             * centred above the clip is drawn above it. That is what the toolkit does.
             */
            toolkit.T(140, 300, 400, 384);
            at.za(200, 270, 45, 0xFFFFFFFF, 1);
            at.za(340, 500, 45, 0xFF884400, 1);
            toolkit.la();
        }
    }

    /**
     * Everything already drawn moved by a whole number of pixels.
     *
     * The client moves the buffer rather than drawing it again when the view shifts by a little,
     * and draws only the strip left behind. Nothing fills that strip here, so what the move left
     * of the old picture is part of what is checked.
     */
    record Scrolled() implements Scene {

        @Override
        public void draw(Toolkit toolkit, Props props) {
            props.gradient().render(30, 30, 0, 0xFFFFFF, 0);
            toolkit.aa(200, 40, 90, 60, 0xFF20C080, 0);
            toolkit.aa(60, 150, 140, 40, 0xFFC02040, 0);

            toolkit.F(40, 25);
            toolkit.aa(0, 0, WIDTH, 20, 0xFF2040A0, 0);

            toolkit.F(-15, -8);
            props.gradient().render(300, 240, 1, 0, 1);

            toolkit.F(0, 60);
            toolkit.F(70, 0);
            toolkit.F(-90, -120);
        }
    }

    /**
     * Lifting what has been drawn back out into a sprite.
     *
     * This is how the minimap is built: the client draws into the buffer and then takes it out a
     * square at a time. A pixel left at nothing stays clear and every other one becomes solid,
     * so the picture is drawn on a background of nothing rather than on the usual one.
     */
    record LiftedOut() implements Scene {

        private static final int SIZE = 100;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.GA(0);

            props.gradient().render(10, 10, 0, 0xFFFFFF, 0);
            toolkit.aa(40, 40, 60, 40, 0xFF20C080, 0);
            toolkit.aa(120, 20, 50, 90, 0xFFC02040, 0);
            props.mono().setTextColours(0xFFFFFF, 0x000000);
            props.mono().render("lift", 20, 100, 0xFFFFFF, 0x000000, null, null);

            var lifted = toolkit.createSprite(SIZE, SIZE, true);
            lifted.copyRect(0, 0, SIZE, SIZE, 20, 20);

            var cut = toolkit.createSprite(SIZE, SIZE, true);
            cut.copyRect(0, 0, SIZE, SIZE, 0, 0);
            cut.copyAlpha(40, 40, 3);

            toolkit.aa(0, 140, WIDTH, HEIGHT - 140, 0xFF303060, 0);

            lifted.render(20, 160, 1, 0, 0);
            lifted.render(140, 160, 1, 0, 1);
            cut.render(260, 160, 1, 0, 1);
            cut.render(380, 160, 0, 0xFF8040, 1);

            lifted.render(20, 270, 1, 0, 2);
            cut.render(140, 270, 2, 0x80FFFFFF, 1);
        }
    }

    /**
     * A sprite turned, which the client asks for by where the corners land rather than by an
     * angle.
     *
     * Every angle here is off the whole quarters, because a turn that lands on a quarter walks
     * the sprite a pixel at a time and says nothing about the fractions the rest of them read
     * from. The last row is drawn through a shape, which is how the minimap turns inside its
     * round window.
     */
    record Rotated() implements Scene {

        private static final int SIZE = 96;

        /** How far the client counts a sprite as being at its own size. */
        private static final int SAME_SIZE = 4096;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            var angles = new int[] {0, 2731, 8000, 13000, 20000, 41000};

            for (var step = 0; step < angles.length; step++) {
                props.gradient().renderRotated(48.0F + step * 80.0F, 60.0F,
                    SAME_SIZE, angles[step]);
            }

            for (var step = 0; step < angles.length; step++) {
                props.gradient().renderRotated(48.0F + step * 80.0F, 170.0F,
                    SAME_SIZE / 2 + step * 900, angles[step], 0xFF4488CC);
            }



            var starts = new int[SIZE];
            var lengths = new int[SIZE];
            for (var row = 0; row < SIZE; row++) {
                var half = SIZE / 2;
                var reach = (int) Math.sqrt(half * half - (row - half) * (row - half));
                starts[row] = half - reach;
                lengths[row] = reach * 2;
            }

            var mask = toolkit.createMask(SIZE, SIZE, starts, lengths);

            for (var step = 0; step < angles.length; step++) {
                var x = 20 + step * 80;
                props.gradient().renderRotated(x + SIZE / 2.0F, 280.0F + SIZE / 2.0F,
                    24.0F, 24.0F, SAME_SIZE, angles[step], mask, x, 280);
            }
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
     * Models standing among lights that have a place in the world rather than only a direction.
     *
     * The client never asks for these, so the only way they are ever drawn is to ask for them
     * here. Five are handed over and only four may be kept, one strength is above the one that
     * counts as full, and one light sits behind the models so that the side facing away from it
     * takes nothing from it.
     */
    record PointLit() implements Scene {

        /** Where the models stand, and how far apart. */
        private static final int APART = 260;

        private static final int[] LIGHTS = {
            -140, -80, DEPTH - 150, 90, 0xFF4040,
            160, 60, DEPTH - 120, 110, 0x40FF60,
            0, -200, DEPTH, 140, 0x8080FF,
            -60, 120, DEPTH + 260, 120, 0xFFC020,
            400, 400, DEPTH, 200, 0xFFFFFF
        };

        private static final float[] STRENGTHS = {1.0F, 0.45F, 2.5F, 0.8F, 1.0F};

        /**
         * A model only meets these lights if it was built asking for the directions its vertices
         * face, which is a feature the scenes drawn by a lit model do not otherwise need.
         */
        private static final int FACING = 0x10;

        private static final int FUNCTIONS = 2048;

        private static final int FEATURES = 64;

        private static final int AMBIENT = 64;

        private static final int CONTRAST = 768;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            var facing = toolkit.createModel(props.mesh(), FUNCTIONS, FEATURES | FACING,
                AMBIENT, CONTRAST);

            var software = (oa) toolkit;
            software.N(LIGHTS.length / 5, LIGHTS, STRENGTHS);

            for (var step = 0; step < 3; step++) {
                props.matrix().makeRotationZ(0);
                props.matrix().rotateAxisY(step * TURN / 3);
                props.matrix().translate((step - 1) * APART, 0, DEPTH);
                facing.render(props.matrix(), null, 1);
            }

            /* The model that did not ask to face anywhere takes nothing from the lights. */
            props.matrix().makeRotationZ(0);
            props.matrix().applyTranslation(0, -190, DEPTH);
            props.simple().render(props.matrix(), null, 1);

            software.N(0, LIGHTS, STRENGTHS);
        }
    }

    /**
     * The same model drawn through a picture taken from no particular place.
     *
     * Nothing shrinks with distance in that picture, and the client chooses how much smaller it
     * is than the one the eye sees. Three sizes are drawn side by side, and a fourth stands far
     * enough back that the eye's own picture would have shrunk it to nothing.
     */
    record Flattened() implements Scene {

        /** How much smaller than the eye's picture each of the four is asked to be. */
        private static final int[] SIZES = {900, 1400, 2200, 1200};

        private static final int APART = 128;

        /**
         * How far away each one stands. A picture taken from no particular place puts them all
         * the same size, so the only thing distance settles is what covers what.
         */
        private static final int[] DEPTHS = {DEPTH, DEPTH * 2, DEPTH * 3, DEPTH * 4};

        /**
         * A far plane the client has picked with the scene in mind.
         *
         * A model drawn this way is dropped whole once its distance is a small enough part of
         * the way to the far plane, somewhere under a twenty thousandth of it. The eye's own
         * picture never comes near that, because what it records grows towards one as a model
         * goes back rather than towards nothing.
         */
        private static final int FAR = DEPTH * 8;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, FAR);

            for (var step = 0; step < SIZES.length; step++) {
                props.matrix().makeRotationZ(0);
                props.matrix().rotateAxisY(step * TURN / 4);
                props.matrix().translate((step - 1) * APART - APART / 2, 0, DEPTHS[step]);
                props.model().renderOrtho(props.matrix(), null, SIZES[step], 0);
            }
        }
    }

    /**
     * A cloud of particles, each one a point the client has already worked out where to put.
     *
     * The client hands the whole cloud over at once, three whole numbers for where each one is,
     * one for its colour, one for how big it is, and the texture it wears. A particle wearing no
     * texture is a filled circle laid over whatever is already there, sized by how far away it
     * ended up. A model stands among them, so a particle behind it is hidden by it.
     *
     * The last few stand somewhere a particle cannot be drawn: off each side, behind the eye,
     * and so far away that nothing is left of it.
     *
     * This is not finished. The shipped toolkit draws no particle at all when it is driven this
     * way, and why it does not is still to find, so the scene reports how far apart the two
     * pictures are rather than passing or failing.
     */
    record Particles() implements Scene {

        @Override
        public boolean written() {
            return false;
        }


        /** How many places of a fraction the client keeps a place and a size in. */
        private static final int PLACE_PLACES = 12;
        private static final int SIZE_PLACES = 11;

        private static final int ACROSS = 6;
        private static final int DOWN = 4;

        /** Where each one of the grid stands, and how far apart. */
        private static final int SPREAD_X = 120;
        private static final int SPREAD_Y = 110;

        /** Every one of these stands somewhere nothing is drawn. */
        private static final int[][] NOWHERE = {
            {-4000, 0, DEPTH, 60, 0xFFFFFFFF},
            {4000, 0, DEPTH, 60, 0xFFFFFFFF},
            {0, -4000, DEPTH, 60, 0xFFFFFFFF},
            {0, 0, -DEPTH, 60, 0xFFFFFFFF},
            {0, 0, DEPTH * 400, 60, 0xFFFFFFFF}
        };

        /** A particle wearing no texture, which is the only kind drawn yet. */
        private static final short BARE = -1;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            props.matrix().makeRotationZ(0);
            props.matrix().applyTranslation(0, 0, DEPTH);
            props.model().render(props.matrix(), null, 1);

            var count = ACROSS * DOWN + NOWHERE.length;
            var places = new int[count * 3];
            var colours = new int[count];
            var sizes = new int[count];
            var textures = new short[count];

            var which = 0;
            for (var column = 0; column < ACROSS; column++) {
                for (var row = 0; row < DOWN; row++) {
                    var x = (column - (ACROSS - 1) / 2) * SPREAD_X;
                    var y = (row - (DOWN - 1) / 2) * SPREAD_Y;
                    var z = DEPTH + (column - 2) * 160;

                    put(places, colours, sizes, textures, which,
                        x, y, z, 24 + row * 14, shade(column, row));
                    which++;
                }
            }

            for (var beyond : NOWHERE) {
                put(places, colours, sizes, textures, which,
                    beyond[0], beyond[1], beyond[2], beyond[3], beyond[4]);
                which++;
            }

            ((oa) toolkit).method6087().method4(toolkit, places, colours, sizes, textures, count);
        }

        /** Solid down one side of the grid and half see-through down the other. */
        private static int shade(int column, int row) {
            var alpha = column < ACROSS / 2 ? 0xFF : 0x60;
            var red = 40 + column * 34;
            var green = 220 - row * 40;
            var blue = 90 + row * 50;
            return alpha << 24 | red << 16 | green << 8 | blue;
        }

        private static void put(int[] places, int[] colours, int[] sizes, short[] textures,
                                int which, int x, int y, int z, int size, int colour) {
            places[which * 3] = x << PLACE_PLACES;
            places[which * 3 + 1] = y << PLACE_PLACES;
            places[which * 3 + 2] = z << PLACE_PLACES;
            colours[which] = colour;
            sizes[which] = size << SIZE_PLACES;
            textures[which] = BARE;
        }
    }

    /**
     * Moving what has already been drawn further away without drawing it again.
     *
     * The client draws the world from above by scrolling what it drew last frame and filling in
     * only the strip that came into view. A camera that rose or fell leaves everything it kept
     * recorded at the wrong distance, so the client names a rectangle and says how far the
     * camera moved, and every distance in it moves with it.
     *
     * A model is drawn, three rectangles are moved by different amounts, and a second model is
     * drawn behind the first. It wins wherever the first was pushed back past it. The third
     * rectangle is narrower than the buffer, which is where the walk goes wrong, and it is drawn
     * to show what the client would get if it ever asked for one.
     */
    record DepthShifted() implements Scene {

        /** A near enough far plane that a few hundred moved is a few hundred seen. */
        private static final int FAR = DEPTH * 4;

        /** How far behind the first model the second one stands. */
        private static final int BEHIND = 120;

        /** Each row is a rectangle and how far the camera is said to have moved over it. */
        private static final int[][] MOVED = {
            {0, 40, WIDTH, 60, 600},
            {0, 160, WIDTH, 60, -600},
            {30, 120, 100, 40, 900}
        };

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, FAR);

            props.matrix().makeRotationZ(0);
            props.matrix().applyTranslation(0, 0, DEPTH);
            props.model().render(props.matrix(), null, 1);

            for (var rectangle : MOVED) {
                toolkit.b(rectangle[0], rectangle[1], rectangle[2], rectangle[3], rectangle[4]);
            }

            props.matrix().makeRotationZ(0);
            props.matrix().rotateAxisY(TURN / 4);
            props.matrix().applyTranslation(0, 0, DEPTH + BEHIND);
            props.model().render(props.matrix(), null, 1);
        }
    }

    /**
     * Two models standing against one another, shaded as though they were one.
     *
     * The client builds a wall as a model per panel. Each panel is shaded only by its own faces,
     * so the upright edge where two of them meet shows as a hard line between two flat shades.
     * Telling the pair that they meet hands each vertex of the join the other panel's direction
     * as well, and the corner shades as a curve.
     *
     * Three corners are drawn. The first is left alone, the second is told that its panels meet
     * where they stand, and the third has its second panel built somewhere else and is told how
     * far along that is, which is the offset the client passes for a panel that was not built
     * about the same middle.
     */
    record SharedLight() implements Scene {

        /** How far from the middle the outer edge of a panel stands. */
        private static final int REACH = 90;

        /** How far back an outer edge leans, which is what makes the two panels differ. */
        private static final int LEAN = 150;

        private static final int TALL = 110;

        private static final int APART = 170;

        /** How far along the third corner's second panel is built. */
        private static final int ELSEWHERE = 4000;

        /**
         * A model may only be told that it meets another while it still says its light can
         * change. The client drops the right once the wall is built and the light is settled.
         */
        private static final int MAY_SHARE_LIGHT = 0x10000;

        private static final int FEATURES = 64;

        private static final int AMBIENT = 64;

        private static final int CONTRAST = 768;

        private static final short COLOUR = (short) ((10 << 10) | (7 << 7) | 70);

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, Integer.MAX_VALUE);

            draw(toolkit, props, -APART, 0, false);
            draw(toolkit, props, 0, 0, true);
            draw(toolkit, props, APART, ELSEWHERE, true);
        }

        /**
         * One corner: a panel leaning away to the left and another leaning away to the right,
         * meeting along the upright edge between them.
         */
        private void draw(Toolkit toolkit, Props props, int across, int built, boolean meeting) {
            var left = panel(toolkit, -REACH, 0);
            var right = panel(toolkit, REACH, built);

            if (meeting) {
                /* The last thing the client passes is the one the toolkit never reads. */
                left.method7481(right, -built, 0, 0, true);
            }

            props.matrix().makeRotationZ(0);
            props.matrix().applyTranslation(across, 0, DEPTH);
            left.render(props.matrix(), null, 1);

            props.matrix().makeRotationZ(0);
            props.matrix().applyTranslation(across - built, 0, DEPTH);
            right.render(props.matrix(), null, 1);
        }

        private Model panel(Toolkit toolkit, int reach, int shift) {
            var mesh = new PanelMesh(reach, LEAN, TALL, shift, COLOUR).build();
            return toolkit.createModel(mesh, MAY_SHARE_LIGHT, FEATURES, AMBIENT, CONTRAST);
        }
    }

    /**
     * Drawing somewhere other than the window, and bringing the result back.
     *
     * The client pairs a sprite with a buffer of distances and hands the pair over as a surface.
     * Everything drawn afterwards lands on the sprite instead, with the middle of the picture
     * moved to the middle of it, until the canvas is named again.
     *
     * A rectangle is copied out of the window first and back again afterwards, colours and
     * distances together, so a surface that carried the distances across covers what it covered
     * before rather than only what was drawn over it.
     */
    record Offscreen() implements Scene {

        private static final int SIDE = 192;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            toolkit.DA(WIDTH / 2, HEIGHT / 2, 512, 512);
            toolkit.f(NEAR, DEPTH * 8);

            var sprite = toolkit.createSprite(SIDE, SIDE, false);
            var surface = toolkit.createOffscreenSurface(sprite,
                toolkit.method7986(SIDE, SIDE));

            toolkit.swapSurface(surface);

            /* The buffer of distances comes as it was found, so it is cleared before use. */
            toolkit.ya();
            toolkit.fillRect(0, 0, SIDE, SIDE, 0xFF203060);
            toolkit.fillRect(10, 10, 80, 60, 0xFF44CC44);

            props.matrix().makeRotationZ(0);
            props.matrix().rotateAxisY(TURN / 8);
            props.matrix().applyTranslation(0, 40, DEPTH);
            props.model().render(props.matrix(), null, 1);

            toolkit.restoreSurface();

            /*
             * The window is painted again from here, because what a surface swap leaves behind
             * in it is not settled.
             */
            toolkit.fillRect(0, 0, WIDTH, HEIGHT, CLEAR_COLOUR | 0xFF000000);
            toolkit.fillRect(20, 20, 300, 120, 0xFF2266AA);

            surface.method9040(0, 0, SIDE, SIDE, 280, 150);
            sprite.render(20, 250);
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

            /*
             * Lines cut to the same shape, at every slope, so that the row a line is on and the
             * run the shape allows on that row are matched against one another rather than by
             * chance.
             */
            for (var step = 0; step < 8; step++) {
                toolkit.line(300, 40 + step * 8, 460, 110 - step * 8, 0xFFFFCC00, 1,
                    mask, 300, 20);
                toolkit.line(300 + step * 20, 20, 460 - step * 20, 130, 0xFF00CCFF, 1,
                    mask, 300, 20);
            }

            toolkit.line(40, 260, 40, 380, 0xFFFF6600, 1, mask, 20, 260);
            toolkit.line(20, 300, 160, 300, 0xFFFF6600, 1, mask, 20, 260);
            toolkit.line(90, 320, 90, 320, 0xFFFFFFFF, 1, mask, 20, 260);
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
    /**
     * Lines with a width to them, which the toolkit draws as two triangles rather than as a walk.
     *
     * A width that does not halve evenly puts the extra pixel on one side, and which side that
     * is depends on which way the line leans, so the fan below turns the whole way round. The
     * last few run off the edges and lie exactly along them.
     */
    record ThickLines() implements Scene {

        private static final int MIDDLE_X = 170;

        private static final int MIDDLE_Y = 180;

        private static final int REACH = 140;

        private static final int SPOKES = 12;

        @Override
        public void draw(Toolkit toolkit, Props props) {
            for (var spoke = 0; spoke < SPOKES; spoke++) {
                var angle = spoke * 2.0 * Math.PI / SPOKES;
                var endX = MIDDLE_X + (int) (Math.cos(angle) * REACH);
                var endY = MIDDLE_Y + (int) (Math.sin(angle) * REACH);
                toolkit.strongLine(MIDDLE_X, MIDDLE_Y, endX, endY,
                    0xFF000000 | (spoke * 0x2010 + 0x40C080), spoke + 1, 0);
            }

            /* Odd and even widths side by side, on a line that leans the other way. */
            for (var width = 1; width <= 8; width++) {
                toolkit.strongLine(360, 40 + width * 20, 470, 60 + width * 20,
                    0xFFCCCC22, width, 0);
            }

            /* Upright, flat, and no length at all. */
            toolkit.strongLine(330, 210, 330, 360, 0xFF22CCCC, 7, 0);
            toolkit.strongLine(350, 210, 490, 210, 0xFF22CC22, 6, 0);
            toolkit.strongLine(400, 280, 400, 280, 0xFFFFFFFF, 9, 0);

            /* Off every edge, including one that lies along the top row. */
            toolkit.strongLine(-200, 330, 700, 350, 0xFFFF6600, 11, 0);
            toolkit.strongLine(60, -200, 90, 700, 0xFF6600FF, 5, 0);
            toolkit.strongLine(-40, 0, 552, 0, 0xFFFFFF00, 3, 0);
        }
    }

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
