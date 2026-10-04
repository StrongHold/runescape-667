import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.IndexedImage;
import com.jagex.graphics.FontMetrics;
import com.jagex.js5.Js5Archive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;

/**
 * Writes every font out of the cache as an AngelCode BMFont, with an index of them all.
 */
public final class FontExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The directory to write the fonts to, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "fonts");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportFonts", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        var metricsArchive = Cache.js5(cache, Js5Archive.FONTMETRICS);
        var spritesArchive = Cache.js5(cache, Js5Archive.SPRITES);
        var spritesIndex = Cache.index(cache, Js5Archive.SPRITES);
        var check = new FontCheck();

        Files.createDirectories(args.out);
        var index = new ArrayList<Object>();
        var differ = 0;

        for (var id : Cache.groupsOf(Cache.index(cache, Js5Archive.FONTMETRICS))) {
            var metrics = FontMetrics.loadFile(metricsArchive, id);
            var glyphs = IndexedImage.load(spritesArchive, id);
            if (metrics == null || glyphs == null) {
                System.out.println("font " + id + " has no " + (metrics == null ? "metrics" : "glyphs") + ", left out");
            } else {
                var name = FontNames.name(spritesIndex, id);
                var base = name.orElse(Integer.toString(id));
                var font = BmFont.of(base, base + ".png", metrics, glyphs);
                var descriptor = args.out.resolve(base + ".fnt");
                var page = args.out.resolve(base + ".png");
                Files.writeString(descriptor, font.descriptor());
                ImageIO.write(font.page(), "png", page.toFile());

                var differences = check.differences(descriptor, metrics, glyphs);
                differ += differences > 0 ? 1 : 0;

                var antialiased = font.antialiased();
                var kerned = metrics.glyphSpacing != null;
                var entry = new LinkedHashMap<String, Object>();
                entry.put("id", id);
                name.ifPresent(known -> entry.put("name", known));
                entry.put("fnt", base + ".fnt");
                entry.put("png", base + ".png");
                entry.put("lineHeight", metrics.verticalSpacing);
                entry.put("ascent", metrics.paddingTop);
                entry.put("descent", metrics.paddingBottom);
                entry.put("antialiased", antialiased);
                entry.put("kerned", kerned);
                index.add(entry);

                System.out.println("font " + id + " " + name.orElse("(unnamed)") + ": line height " + metrics.verticalSpacing
                    + ", ascent " + metrics.paddingTop + ", descent " + metrics.paddingBottom
                    + (antialiased ? ", antialiased" : "") + (kerned ? ", kerned" : "")
                    + (differences > 0 ? ", " + differences + " texels differ from the client" : ", drawn as the client draws it"));
            }
        }

        Files.writeString(args.out.resolve("fonts.json"), Json.write(index));
        System.out.println("wrote " + index.size() + " fonts to " + args.out.toAbsolutePath().normalize());

        if (differ > 0) {
            throw new IllegalStateException(differ + " fonts draw differently from the client");
        }
    }

    private FontExport() {
        /* empty */
    }
}
