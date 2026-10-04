import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Writes every sprite out of the cache as a PNG atlas and a TexturePacker JSON hash beside it,
 * with an index of them all, and checks each one against the client.
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

    /** The one tool the JSON names as having written it, as TexturePacker names itself. */
    private static final String APP = "runescape-667 export";

    private static final String FORMAT = "RGBA8888";

    private static final String SCALE = "1";

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

        var entries = new ArrayList<Map<String, Object>>();
        var frames = 0;
        var empty = 0;
        for (var id : ids) {
            var read = reader.read(id);
            if (read.isEmpty()) {
                empty++;
            } else {
                var sprite = read.get();
                var atlas = SpriteAtlas.pack(sprite.frames());
                var document = document(sprite, atlas);
                var image = args.out.resolve(imageName(id));
                Files.write(image, png(atlas));
                Files.writeString(args.out.resolve(dataName(id)), Json.write(document), StandardCharsets.UTF_8);
                check.compare(id, document, ImageIO.read(image.toFile()));
                entries.add(entry(sprite));
                frames += sprite.frames().size();
            }
        }

        if (args.sprites.isEmpty()) {
            Files.writeString(args.out.resolve("index.json"), Json.write(Map.of("sprites", entries)), StandardCharsets.UTF_8);
        }

        System.out.println("wrote " + entries.size() + " sprites of " + frames + " frames to " + args.out.toAbsolutePath().normalize()
            + (empty > 0 ? ", and left out " + empty + " groups that hold no sprite" : ""));
        System.out.println(check.report());
        if (!check.passed()) {
            System.exit(1);
        }
    }

    public static String imageName(int id) {
        return id + ".png";
    }

    public static String dataName(int id) {
        return id + ".json";
    }

    public static String frameName(int id, int frame) {
        return id + "_" + frame;
    }

    /** The TexturePacker JSON hash of one sprite's atlas. */
    private static Map<String, Object> document(SpriteArchive sprite, SpriteAtlas atlas) {
        var frames = new LinkedHashMap<String, Object>();
        for (var index = 0; index < sprite.frames().size(); index++) {
            var frame = sprite.frames().get(index);
            var placed = atlas.placements().get(index);
            frames.put(frameName(sprite.id(), index), Map.of(
                "frame", rectangle(placed.x(), placed.y(), frame.width(), frame.height()),
                "rotated", false,
                "trimmed", frame.trimmed(),
                "spriteSourceSize", rectangle(frame.x(), frame.y(), frame.width(), frame.height()),
                "sourceSize", Map.of("w", frame.canvasWidth(), "h", frame.canvasHeight()),
                "alpha", frame.alpha()
            ));
        }

        var meta = new LinkedHashMap<String, Object>();
        meta.put("app", APP);
        meta.put("image", imageName(sprite.id()));
        meta.put("format", FORMAT);
        meta.put("size", Map.of("w", imageWidth(atlas), "h", imageHeight(atlas)));
        meta.put("scale", SCALE);
        meta.put("sprite", sprite.id());
        sprite.name().ifPresent(name -> meta.put("name", name));
        return Map.of("frames", frames, "meta", meta);
    }

    private static Map<String, Object> entry(SpriteArchive sprite) {
        var entry = new LinkedHashMap<String, Object>();
        entry.put("id", sprite.id());
        sprite.name().ifPresent(name -> entry.put("name", name));
        entry.put("frames", sprite.frames().size());
        entry.put("image", imageName(sprite.id()));
        entry.put("data", dataName(sprite.id()));
        return entry;
    }

    private static Map<String, Object> rectangle(int x, int y, int width, int height) {
        return Map.of("x", x, "y", y, "w", width, "h", height);
    }

    /** A PNG must be at least one pixel, so a sprite whose frames are all empty gets one clear pixel. */
    private static int imageWidth(SpriteAtlas atlas) {
        return Math.max(1, atlas.width());
    }

    private static int imageHeight(SpriteAtlas atlas) {
        return Math.max(1, atlas.height());
    }

    private static byte[] png(SpriteAtlas atlas) {
        var image = new BufferedImage(imageWidth(atlas), imageHeight(atlas), BufferedImage.TYPE_INT_ARGB);
        if (atlas.width() > 0 && atlas.height() > 0) {
            image.setRGB(0, 0, atlas.width(), atlas.height(), atlas.pixels(), 0, atlas.width());
        }

        var out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", out);
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
        return out.toByteArray();
    }

    private SpriteExport() {
        /* empty */
    }
}
