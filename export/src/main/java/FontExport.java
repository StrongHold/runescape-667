import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.IndexedImage;
import com.jagex.graphics.FontMetrics;
import com.jagex.js5.Js5Archive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes every font out of the cache as a BDF file, and checks that each draws as the client draws it.
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
        var written = 0;
        var differ = 0;

        for (var id : Cache.groupsOf(Cache.index(cache, Js5Archive.FONTMETRICS))) {
            var metrics = FontMetrics.loadFile(metricsArchive, id);
            var glyphs = IndexedImage.load(spritesArchive, id);
            if (metrics == null || glyphs == null) {
                System.out.println("font " + id + " has no " + (metrics == null ? "metrics" : "glyphs") + ", left out");
            } else {
                var name = FontNames.name(spritesIndex, id);
                var file = args.out.resolve(id + ".bdf");
                Files.writeString(file, BdfFont.of(name.orElse(Integer.toString(id)), metrics, glyphs));

                var differences = check.differences(file, metrics, glyphs);
                differ += differences > 0 ? 1 : 0;
                written++;

                System.out.println("font " + id + " " + name.orElse("(unnamed)") + ": line height " + metrics.verticalSpacing
                    + ", ascent " + metrics.paddingTop + ", descent " + metrics.paddingBottom
                    + (differences > 0 ? ", " + differences + " texels differ from the client" : ", drawn as the client draws it"));
            }
        }

        System.out.println("wrote " + written + " fonts to " + args.out.toAbsolutePath().normalize());

        if (differ > 0) {
            throw new IllegalStateException(differ + " fonts draw differently from the client");
        }
    }

    private FontExport() {
        /* empty */
    }
}
