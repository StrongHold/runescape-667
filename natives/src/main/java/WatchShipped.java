import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Draws scenes through the shipped toolkit with some of its routines watched.
 *
 * The watcher is a library inserted into this virtual machine before it starts, and it reads which
 * routines to watch once the toolkit is opened. This writes that list and then draws, so the list
 * is in place by the time the watcher looks for it. What each watched call was handed ends up in
 * the trace, under the scene that caused it.
 *
 * The watcher cannot tell the shipped toolkit apart from anything else loaded after it, so it is
 * only ever inserted by the task that runs this.
 */
public final class WatchShipped {

    private static final class Args implements Helpable {

        @ParametersDelegate
        private final CaptureArgs capture = new CaptureArgs();

        @Parameter(
            names = "--watch",
            splitter = Whole.class,
            description = "A routine to watch, as family:parameters, for example "
                + "body:2,0,0,false,true,3,false,false; repeat to watch several",
            required = true
        )
        private List<String> routines = List.of();

        @Parameter(
            names = "--routines",
            description = "Where to write the list the watcher reads",
            required = true
        )
        private Path list;

        @Override
        public boolean help() {
            return capture.help();
        }
    }

    public static void main(String[] arguments) throws Exception {
        var args = new Args();

        if (!CommandLine.parsed("watchShipped", args, arguments)) {
            return;
        }

        var routines = args.routines.stream().map(ShippedRoutine::parse).toList();
        Files.writeString(args.list, routines.stream()
            .map(ShippedRoutine::watchLine)
            .collect(Collectors.joining("\n", "", "\n")));
        for (var routine : routines) {
            System.out.println("watching " + routine);
        }

        var capture = new ArrayList<String>();
        capture.add("--library");
        capture.add(args.capture.library().getAbsolutePath());
        for (var scene : args.capture.scenes()) {
            capture.add("--scene");
            capture.add(scene);
        }
        FrameCapture.main(capture.toArray(String[]::new));
    }

    private WatchShipped() {
        /* empty */
    }
}
