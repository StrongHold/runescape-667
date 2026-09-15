import com.jagex.graphics.Matrix;
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
        new SpriteMatrix()
    );

    /**
     * What every scene is given. A scene uses what it needs and ignores the rest, which keeps one
     * scene from having to know what another one wanted.
     */
    record Props(Sprite gradient, Model model, Model simple, Matrix matrix) {
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
