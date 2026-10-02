import com.beust.jcommander.Parameter;

import java.nio.file.Path;

/**
 * Where the textures every export shares are kept, for any tool that writes or refers to them.
 */
public final class TextureArgs {

    @Parameter(
        names = "--textures",
        description = "The directory the textures every export shares are kept in, relative to the export module when not absolute"
    )
    private Path directory = TextureLibrary.defaultDirectory();

    public TextureLibrary library(Js5TextureSource source) {
        return new TextureLibrary(source, directory);
    }
}
