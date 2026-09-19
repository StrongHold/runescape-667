import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.io.File;
import java.nio.file.Path;

/**
 * What a probe is asked, and where its answers go.
 *
 * A probe drives one library through a fixed set of questions and writes every answer down. The
 * same probe is run twice, once against each library, and the two files are then compared, so the
 * answers have to be written somewhere the comparison can find them rather than printed.
 */
public final class ProbeArgs implements Helpable {

    @ParametersDelegate
    private final LibraryArgs driving = new LibraryArgs();

    @Parameter(
        names = "--answers",
        description = "The file to write the answers to",
        required = true
    )
    private Path answers;

    public File library() {
        return driving.library();
    }

    public Path answers() {
        return answers;
    }

    @Override
    public boolean help() {
        return driving.help();
    }
}
