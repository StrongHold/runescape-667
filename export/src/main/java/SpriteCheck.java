import com.jagex.IndexedImage;
import com.jagex.graphics.Sprite;
import com.jagex.js5.js5;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Checks each written sprite against the client, frame by frame.
 *
 * Each frame's PNG, read back as it was written, is compared with two things. The first is the
 * frame on its whole canvas as the client lays it out ({@code IndexedImage.method9383}), pixel by
 * pixel. The second is the sprite the software toolkit builds from the same image
 * ({@code JavaToolkit.createSprite}), laid on a clear canvas of the toolkit's size at the toolkit's
 * margins: that must be the same picture, so the margins and the canvas are checked too.
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

    public void compare(int id, List<BufferedImage> written) {
        var images = IndexedImage.load(archive, id, SPRITE_FILE);
        if (written.size() != images.length) {
            failures.add("sprite " + id + " has " + images.length + " frames in the client and " + written.size() + " written");
        } else {
            for (var index = 0; index < images.length; index++) {
                var before = failures.size();
                compare("sprite " + id + " frame " + index, images[index], written.get(index));
                frames++;
                differing += failures.size() > before ? 1 : 0;
            }
        }
    }

    private void compare(String name, IndexedImage image, BufferedImage png) {
        var pixels = png.getRGB(0, 0, png.getWidth(), png.getHeight(), null, 0, png.getWidth());
        if (!Arrays.equals(pixels, onCanvas(image.offsetX(), image.offsetY(), image.method9383()))) {
            failures.add(name + " differs from the client's canvas");
        }

        var sprite = toolkit.createSprite(image, false);
        var margins = new int[4];
        sprite.projectOffsets(margins);
        var width = sprite.scaleWidth() - margins[0] - margins[2];
        var height = sprite.scaleHeight() - margins[1] - margins[3];
        var frame = new SpriteFrame(margins[0], margins[1], width, height, sprite.scaleWidth(), sprite.scaleHeight(), pixels(sprite));
        if (!Arrays.equals(pixels, onCanvas(frame.canvasWidth(), frame.canvasHeight(), frame.canvas()))) {
            failures.add(name + " differs from the toolkit's sprite on its canvas");
        }
    }

    /**
     * A canvas as the PNG holds it, which is one clear pixel where the canvas has none.
     */
    private static int[] onCanvas(int width, int height, int[] canvas) {
        return width > 0 && height > 0 ? canvas : new int[1];
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
