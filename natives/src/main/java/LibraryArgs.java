import com.beust.jcommander.Parameter;

import java.io.File;

/**
 * Which native library a tool is to load.
 *
 * Every tool that drives a native takes one, and which one it is decides what is being measured:
 * the shipped library or ours. It is always named rather than found, so that a run says on the
 * command line which of the two it was.
 */
public final class LibraryArgs implements Arguments {

    @Parameter(
        names = "--library",
        description = "The native library to load",
        required = true
    )
    private File library;

    @Parameter(names = "--help", help = true, description = "Print this message")
    private boolean help;

    public File library() {
        return library;
    }

    @Override
    public boolean help() {
        return help;
    }
}
