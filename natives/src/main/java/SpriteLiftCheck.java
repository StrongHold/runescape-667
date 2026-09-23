import java.awt.Canvas;
import java.awt.Frame;
import java.io.File;
import rs2.client.loading.library.LibraryManager;

/**
 * Checks the sprite lifted straight out of the buffer against the same thing done in two steps.
 *
 * The toolkit this is all checked against cannot be driven through the one-step native at all:
 * it exports it under a C++ name that no virtual machine will find. So the only check available
 * is that the one step and the two steps agree with each other.
 */
public final class SpriteLiftCheck {
    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("verifySpriteLift", new LibraryArgs(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(LibraryArgs args) throws Exception  {
        Watchdog.arm("The sprite lift check", 60);
        LibraryManager.putLibrary(args.library(), "sw3d");

        var canvas = new Canvas();
        canvas.setSize(512, 384);
        var window = new Frame("sprite lift check");
        window.add(canvas);
        window.pack();
        window.setVisible(true);
        Thread.sleep(800);

        var toolkit = (oa) oa.create(canvas, new StubTextureSource(), 512, 384);
        toolkit.la();
        toolkit.DA(256, 192, 512, 512);

        toolkit.GA(0);
        for (var i = 0; i < 40; i++) {
            toolkit.aa(i * 11, i * 7, 30 + i, 20 + i, 0xFF000000 | (i * 0x0A1B3C), 0);
        }

        var lifted = toolkit.createSprite(20, 15, 200, 150, false);
        var staged = toolkit.createSprite(200, 150, true);
        staged.copyRect(0, 0, 200, 150, 20, 15);

        var one = new int[200 * 150];
        var two = new int[200 * 150];
        ((j) lifted).CA(((j) lifted).nativeid, one);
        ((j) staged).CA(((j) staged).nativeid, two);

        var differ = 0;
        for (var i = 0; i < one.length; i++) {
            if (one[i] != two[i]) {
                differ++;
            }
        }

        System.out.println("lifted " + lifted.getWidth() + "x" + lifted.getHeight()
            + ", " + differ + " of " + one.length + " pixels differ");
        System.exit(differ == 0 ? 0 : 1);
    }
}
