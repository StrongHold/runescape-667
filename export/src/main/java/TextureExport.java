import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

/**
 * Writes every texture out of the cache into the library that the other exports refer to.
 */
public final class TextureExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @ParametersDelegate
        private final TextureArgs textures = new TextureArgs();

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportTextures", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws java.io.IOException {
        var reader = new ClientModelReader(args.where.cache());
        var library = args.textures.library(reader.textures());
        var written = library.writeAll();
        System.out.println("wrote " + written + " textures to " + library.directory().toAbsolutePath().normalize()
            + ", of " + library.count() + " the cache holds");
        WaterTextures.write(library.directory());
        System.out.println("wrote the water's ripple frames and turbulence to " + library.directory().resolve("water").toAbsolutePath().normalize());
    }

    private TextureExport() {
        /* empty */
    }
}
