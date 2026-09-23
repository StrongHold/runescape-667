import com.beust.jcommander.Parameter;

import java.io.File;

/**
 * Where the game's cache is.
 *
 * The client keeps its cache in the same place on every machine, so the default is nearly always
 * right. It is still worth naming, because a second cache is how a tool is pointed at a different
 * build of the game.
 */
public final class CacheArgs {

    @Parameter(
        names = "--cache",
        description = "The directory the game's cache is in"
    )
    private File cache = Cache.standard();

    public File cache() {
        return cache;
    }
}
