import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 * Writes every sprite out of the cache as one PNG for each frame, each frame on its whole canvas,
 * with the names the client asks for sprites by and the number of frames of each sprite with more
 * than one, and checks each frame against the client.
 */
public final class SpriteExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--sprite",
            description = "Write only this sprite, by its group in the sprites archive"
        )
        private List<Integer> sprites = new ArrayList<>();

        @Parameter(
            names = "--out",
            description = "The directory to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "sprites");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    private static final String NAMES = "names.json";

    /**
     * The file that says how many frames each sprite with more than one has, which its files cannot
     * say themselves.
     */
    private static final String FRAMES = "frames.json";

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportSprites", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var reader = new ClientSpriteReader(args.where.cache());
        var ids = args.sprites.isEmpty() ? reader.ids(args.where.cache()) : args.sprites.stream().mapToInt(Integer::intValue).toArray();
        var check = new SpriteCheck(reader.archive());
        Files.createDirectories(args.out);

        var names = new TreeMap<String, Object>();
        var frameCounts = new TreeMap<Integer, Object>();
        var sprites = 0;
        var frames = 0;
        var empty = 0;
        for (var id : ids) {
            var read = reader.read(id);
            if (read.isEmpty()) {
                empty++;
            } else {
                var sprite = read.get();
                var written = write(args.out.resolve(Integer.toString(id)), sprite);
                check.compare(id, written);
                sprite.name().ifPresent(name -> names.put(name, id));
                if (sprite.frames().size() > 1) {
                    frameCounts.put(id, sprite.frames().size());
                }
                sprites++;
                frames += sprite.frames().size();
            }
        }

        if (args.sprites.isEmpty()) {
            Files.writeString(args.out.resolve(NAMES), Json.write(names), StandardCharsets.UTF_8);
            var counts = new TreeMap<String, Object>();
            frameCounts.forEach((id, count) -> counts.put(Integer.toString(id), count));
            Files.writeString(args.out.resolve(FRAMES), Json.write(counts), StandardCharsets.UTF_8);
        }

        System.out.println("wrote " + sprites + " sprites of " + frames + " frames to " + args.out.toAbsolutePath().normalize()
            + (empty > 0 ? ", and left out " + empty + " groups that hold no sprite" : ""));
        System.out.println(check.report());
        if (!check.passed()) {
            System.exit(1);
        }
    }

    /**
     * Writes each frame of a sprite to `<n>.png` in its directory, and reads each back as it was
     * written, for the check.
     */
    private static List<BufferedImage> write(Path directory, SpriteArchive sprite) throws IOException {
        Files.createDirectories(directory);
        var written = new ArrayList<BufferedImage>();
        for (var index = 0; index < sprite.frames().size(); index++) {
            var file = directory.resolve(index + ".png");
            ImageIO.write(image(sprite.frames().get(index)), "png", file.toFile());
            written.add(ImageIO.read(file.toFile()));
        }
        return written;
    }

    /**
     * The frame on its canvas. A PNG cannot be smaller than one pixel, so a frame whose canvas has
     * no pixels is one clear pixel.
     */
    private static BufferedImage image(SpriteFrame frame) {
        var width = Math.max(1, frame.canvasWidth());
        var height = Math.max(1, frame.canvasHeight());
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (frame.canvasWidth() > 0 && frame.canvasHeight() > 0) {
            image.setRGB(0, 0, frame.canvasWidth(), frame.canvasHeight(), frame.canvas(), 0, frame.canvasWidth());
        }
        return image;
    }

    private SpriteExport() {
        /* empty */
    }
}
