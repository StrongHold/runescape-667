import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.nio.file.Path;

/**
 * Writes one location type out of the cache into the library that every map square refers to: its data as JSON,
 * and the models it names and the sequences it plays into their libraries.
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
            names = "--baked",
            description = "A glTF file to also write the location's meshes into, baked as the client builds them, relative to the export module when not absolute"
        )
        private Path baked;

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
        var file = assets.file(args.loc)
            .orElseThrow(() -> new IllegalStateException("Location " + args.loc + " has no model the client builds."));
        System.out.println("wrote " + file.toAbsolutePath().normalize());
        System.out.println("  " + LocAssets.label(reader.type(args.loc)));

        if (args.baked != null) {
            var baked = assets.writeBaked(args.loc, args.baked).orElseThrow();
            System.out.println("wrote " + args.baked.toAbsolutePath().normalize());
            System.out.println("  " + baked.shapes() + " shapes, " + baked.faces() + " faces, " + baked.targets()
                + " morph targets");
            for (var animation : baked.animations()) {
                System.out.println("  " + animation);
            }
        }
    }

    private LocExport() {
        /* empty */
    }
}
