import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.nio.file.Path;

/**
 * Writes one location type out of the cache as a glTF file with its buffer beside it, into the library that every
 * map square refers to.
 */
public final class LocExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @ParametersDelegate
        private final TextureArgs textures = new TextureArgs();

        @Parameter(names = "--loc", description = "Which location type to write, by its id", required = true)
        private int loc;

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportLoc", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) {
        var reader = new ClientLocReader(args.where.cache());
        var assets = new LocAssets(reader, args.textures.library(reader.textures()), LocAssets.defaultDirectory());
        var out = args.out == null ? assets.directory().resolve(args.loc + ".gltf") : args.out;
        var written = assets.write(args.loc, out)
            .orElseThrow(() -> new IllegalStateException("Location " + args.loc + " has no model the client builds."));

        System.out.println("wrote " + out.toAbsolutePath().normalize());
        System.out.println("  " + LocAssets.label(reader.type(args.loc)) + ", " + written.shapes() + " shapes, "
            + written.faces() + " faces, " + written.targets() + " morph targets");
        for (var animation : written.animations()) {
            System.out.println("  " + animation);
        }
    }

    private LocExport() {
        /* empty */
    }
}
