import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.io.File;
import java.util.List;

/**
 * Which toolkit draws the scenes, and which of the scenes it draws.
 *
 * Every scene is drawn unless some are named. Naming one is what a trace of a single scene wants:
 * the frames come out numbered from nought for that scene alone, and the trace holds nothing any
 * other scene put there.
 */
public final class CaptureArgs implements Helpable {

    @ParametersDelegate
    private final LibraryArgs driving = new LibraryArgs();

    @Parameter(
        names = "--scene",
        splitter = Whole.class,
        description = "A scene to draw, by its title; repeat to draw several, omit to draw them all"
    )
    private List<String> scenes = List.of();

    public File library() {
        return driving.library();
    }

    public List<String> scenes() {
        return List.copyOf(scenes);
    }

    @Override
    public boolean help() {
        return driving.help();
    }
}
