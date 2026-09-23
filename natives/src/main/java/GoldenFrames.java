import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps one frame per scene in the repository, so that a machine without the shipped library can
 * still say whether our toolkit draws what it is meant to.
 *
 * The shipped library comes out of the game's cache and is not ours to publish, so it cannot reach
 * a build running anywhere but a developer's own machine. What it drew can be kept instead. Each
 * frame is filed under its scene rather than under the number it was drawn in, because a change to
 * one is read by people, and a number says nothing about what changed.
 */
public final class GoldenFrames {

    public static final class Args implements Arguments {

        @Parameter(names = "--frames", description = "The frames the shipped toolkit drew", required = true)
        private Path frames;

        @Parameter(names = "--into", description = "The directory to keep one frame per scene in", required = true)
        private Path into;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("updateGoldens", new Args(), arguments);

        if (parsed.isPresent()) {
            keep(parsed.get());
        }
    }

    private static void keep(Args args) throws IOException {
        var scenes = Files.readAllLines(args.frames.resolve("scenes.txt"));
        if (scenes.isEmpty()) {
            throw new IllegalStateException("No scenes were drawn into " + args.frames + ".");
        }

        /*
         * Anything already kept that no scene asks for any more is taken away, so that a scene
         * which has been renamed or removed does not leave a frame behind that nothing checks.
         */
        if (Files.isDirectory(args.into)) {
            try (var held = Files.list(args.into)) {
                for (var path : held.toList()) {
                    Files.delete(path);
                }
            }
        } else {
            Files.createDirectories(args.into);
        }

        var kept = new ArrayList<String>();

        for (var index = 0; index < scenes.size(); index++) {
            var name = named(scenes.get(index));
            if (kept.contains(name)) {
                continue;
            }

            var drawn = args.frames.resolve(String.format("frame-%04d.png", index));
            if (!Files.isRegularFile(drawn)) {
                throw new IllegalStateException("The scene " + name + " drew no frame " + index + ".");
            }

            Files.copy(drawn, args.into.resolve(name + ".png"));
            kept.add(name);
        }

        System.out.println("kept " + kept.size() + " scenes in " + args.into);
    }

    /**
     * A scene's name without what is said about it. A scene carries notes such as "(outstanding)"
     * beside its name, and those say how it is judged rather than which scene it is.
     */
    private static String named(String scene) {
        var said = scene.indexOf(" (");
        return said < 0 ? scene : scene.substring(0, said);
    }

    private GoldenFrames() {
        /* empty */
    }
}
