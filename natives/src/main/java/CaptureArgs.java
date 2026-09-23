import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.io.File;
import java.util.List;

/**
 * Which toolkit draws the scenes, and which of the scenes it draws.
 *
 * The native software toolkit is the one everything is measured with. The toolkit written in Java
 * is there to be looked at: it draws the same scenes into a buffer of its own, and its frames are
 * written out from that buffer, since no native surface is involved.
 *
 * Every scene is drawn unless some are named. Naming one is what a trace of a single scene wants:
 * the frames come out numbered from nought for that scene alone, and the trace holds nothing any
 * other scene put there.
 */
public final class CaptureArgs implements Arguments {

    @ParametersDelegate
    private final LibraryArgs driving = new LibraryArgs();

    @Parameter(
        names = "--scene",
        splitter = Whole.class,
        description = "A scene to draw, by its title; repeat to draw several, omit to draw them all"
    )
    private List<String> scenes = List.of();

    @Parameter(
        names = "--toolkit",
        description = "Which toolkit draws the scenes: the native software one, or the one written in Java"
    )
    private CaptureToolkit toolkit = CaptureToolkit.SW3D;

    public File library() {
        return driving.library();
    }

    public CaptureToolkit toolkit() {
        return toolkit;
    }

    public List<String> scenes() {
        return List.copyOf(scenes);
    }

    @Override
    public boolean help() {
        return driving.help();
    }
}
