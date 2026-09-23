import com.beust.jcommander.Parameter;

import java.nio.file.Path;

/**
 * Writes the models the scenes are drawn with beside the scenes, out of the game's cache.
 *
 * Run it when a scene starts to use a model that is not kept yet. Drawing the scenes needs the
 * cache only until then.
 */
public final class KeepModels {

    private static final class Args implements Arguments {

        @Parameter(
            names = "--into",
            description = "The directory to write the models into",
            required = true
        )
        private Path into;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("keepModels", new Args(), arguments);

        if (parsed.isPresent()) {
            SceneModel.keep(parsed.get().into);
        }
    }

    private KeepModels() {
        /* empty */
    }
}
