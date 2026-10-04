import com.jagex.IndexedImage;
import com.jagex.graphics.Sprite;
import com.jagex.js5.js5;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Checks each written sprite against the client, frame by frame.
 *
 * Two things are compared with what the files say. The first is the frame on its whole canvas as
 * the client lays it out ({@code IndexedImage.method9383}): the written frame is cut from the
 * written PNG by its {@code frame}, put on a clear canvas of its {@code sourceSize} at its
 * {@code spriteSourceSize}, and every pixel of the canvas must match. The second is the sprite the
 * software toolkit builds from the same image ({@code JavaToolkit.createSprite}): its pixels must
 * match the frame's, its margins must be the ones the frame's position and canvas leave, and its
 * canvas must be the frame's.
 */
public final class SpriteCheck {

    private static final int SPRITE_FILE = 0;

    private static final int REPORTED = 10;

    private final js5 archive;
    private final JavaToolkit toolkit;
    private final List<String> failures = new ArrayList<>();
    private int frames;
    private int differing;

    public SpriteCheck(js5 archive) {
        this.archive = archive;
        this.toolkit = new JavaToolkit(null);
    }

    @SuppressWarnings("unchecked")
    public void compare(int id, Map<String, Object> document, BufferedImage png) {
        var images = IndexedImage.load(archive, id, SPRITE_FILE);
        var written = (Map<String, Object>) document.get("frames");
        if (written.size() != images.length) {
            failures.add("sprite " + id + " has " + images.length + " frames in the client and " + written.size() + " written");
        } else {
            for (var index = 0; index < images.length; index++) {
                var frame = (Map<String, Object>) written.get(SpriteExport.frameName(id, index));
                var before = failures.size();
                compare(id, index, images[index], frame, png);
                frames++;
                differing += failures.size() > before ? 1 : 0;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void compare(int id, int index, IndexedImage image, Map<String, Object> frame, BufferedImage png) {
        var where = (Map<String, Integer>) frame.get("frame");
        var placed = (Map<String, Integer>) frame.get("spriteSourceSize");
        var canvas = (Map<String, Integer>) frame.get("sourceSize");
        var width = where.get("w");
        var height = where.get("h");
        var rectangle = new int[width * height];
        if (width > 0 && height > 0) {
            png.getRGB(where.get("x"), where.get("y"), width, height, rectangle, 0, width);
        }

        var laid = new int[canvas.get("w") * canvas.get("h")];
        for (var y = 0; y < height; y++) {
            System.arraycopy(rectangle, y * width, laid, (placed.get("y") + y) * canvas.get("w") + placed.get("x"), width);
        }
        var name = "sprite " + id + " frame " + index;
        if (!Arrays.equals(laid, image.method9383())) {
            failures.add(name + " differs from the client's canvas");
        }

        var sprite = toolkit.createSprite(image, false);
        var margins = new int[4];
        sprite.projectOffsets(margins);
        var expected = new int[] {placed.get("x"), placed.get("y"), canvas.get("w") - width - placed.get("x"), canvas.get("h") - height - placed.get("y")};
        if (!Arrays.equals(margins, expected)) {
            failures.add(name + " has margins " + Arrays.toString(margins) + " in the toolkit and " + Arrays.toString(expected) + " written");
        }
        if (sprite.scaleWidth() != canvas.get("w") || sprite.scaleHeight() != canvas.get("h")) {
            failures.add(name + " has a canvas of " + sprite.scaleWidth() + " by " + sprite.scaleHeight() + " in the toolkit");
        }
        if (!Arrays.equals(pixels(sprite), rectangle)) {
            failures.add(name + " differs from the toolkit's sprite");
        }
    }

    private static int[] pixels(Sprite sprite) {
        return switch (sprite) {
            case JavaRgbSprite rgb -> rgb.pixels;
            case JavaArgbSprite argb -> argb.pixels;
            default -> throw new IllegalStateException("The toolkit built a " + sprite.getClass() + " where it builds a sprite of colours");
        };
    }

    public boolean passed() {
        return failures.isEmpty();
    }

    public String report() {
        if (passed()) {
            return "every one of " + frames + " frames matches the client's canvas and the software toolkit's sprite";
        } else {
            var shown = String.join("\n  ", failures.subList(0, Math.min(REPORTED, failures.size())));
            return differing + " of " + frames + " frames differ from the client, such as:\n  " + shown;
        }
    }
}
