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
 * the PNG of a sprite of one frame named by the sprite alone and each of several by its number too,
 * with the names the client asks for sprites by, and checks each frame against the client.
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

    private static final String PNG = ".png";

    /** What comes between a sprite's name and the number of a frame, where it has several. */
    private static final String FRAME_SEPARATOR = "_";

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
        var sprites = 0;
        var frames = 0;
        var empty = 0;
        for (var id : ids) {
            var read = reader.read(id);
            if (read.isEmpty()) {
                empty++;
            } else {
                var sprite = read.get();
                var written = write(args.out, Integer.toString(id), sprite);
                check.compare(id, written);
                sprite.name().ifPresent(name -> names.put(name, id));
                sprites++;
                frames += sprite.frames().size();
            }
        }

        if (args.sprites.isEmpty()) {
            Files.writeString(args.out.resolve(NAMES), Json.write(names), StandardCharsets.UTF_8);
        }

        System.out.println("wrote " + sprites + " sprites of " + frames + " frames to " + args.out.toAbsolutePath().normalize()
            + (empty > 0 ? ", and left out " + empty + " groups that hold no sprite" : ""));
        System.out.println(check.report());
        if (!check.passed()) {
            System.exit(1);
        }
    }

    /**
     * Writes a sprite of one frame to `<name>.png` and a sprite of several to `<name>_<n>.png` for
     * each frame, after it removes what an earlier export left for the same sprite, and reads each
     * frame back as it was written, for the check.
     */
    private static List<BufferedImage> write(Path out, String name, SpriteArchive sprite) throws IOException {
        removeEarlier(out, name);
        var frames = sprite.frames();
        var written = new ArrayList<BufferedImage>();
        for (var index = 0; index < frames.size(); index++) {
            var file = out.resolve(frames.size() == 1 ? name + PNG : name + FRAME_SEPARATOR + index + PNG);
            ImageIO.write(image(frames.get(index)), "png", file.toFile());
            written.add(ImageIO.read(file.toFile()));
        }
        return written;
    }

    /**
     * Removes the files of a sprite that an earlier export wrote into the same directory: its one
     * file, the files of its frames, and `<name>/<n>.png`, the older layout of the export's sprites.
     * The sprite can have a different number of frames in another cache, so each form is removed.
     */
    private static void removeEarlier(Path out, String name) throws IOException {
        Files.deleteIfExists(out.resolve(name + PNG));
        try (var files = Files.newDirectoryStream(out, name + FRAME_SEPARATOR + "*" + PNG)) {
            for (var file : files) {
                if (isFrameOf(name, file.getFileName().toString())) {
                    Files.delete(file);
                }
            }
        }

        var directory = out.resolve(name);
        if (Files.isDirectory(directory)) {
            try (var files = Files.newDirectoryStream(directory, "*" + PNG)) {
                for (var file : files) {
                    Files.delete(file);
                }
            }
            Files.delete(directory);
        }
    }

    /**
     * Whether a file is a frame of the sprite of a name, `<name>_<n>.png`, and not the file of
     * another sprite whose name starts the same way.
     */
    private static boolean isFrameOf(String name, String file) {
        var index = file.substring(name.length() + FRAME_SEPARATOR.length(), file.length() - PNG.length());
        return !index.isEmpty() && index.chars().allMatch(Character::isDigit);
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
