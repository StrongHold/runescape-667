import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.util.Optional;

/**
 * Reads a tool's arguments.
 *
 * Every tool in this build is a main method run from a Gradle task, and each one wants the same
 * three things: its arguments read, a usage message when they are wrong or when help is asked for,
 * and a failing exit status when they are wrong. Doing that here leaves each tool holding only
 * what it is actually for.
 */
public final class CommandLine {

    private static final int WRONG_USAGE = 1;

    /**
     * Reads the arguments into a fresh instance of the tool's own arguments.
     *
     * Arguments that cannot be read are reported with the usage, and the process exits with a
     * failing status, because nothing a tool could do with them would be right.
     *
     * @return the arguments, or nothing when help was asked for and has been printed.
     */
    public static <T extends Arguments> Optional<T> parse(String tool, T fresh, String[] given) {
        var commander = JCommander.newBuilder()
            .programName(tool)
            .addObject(fresh)
            .build();

        try {
            commander.parse(given);
        } catch (ParameterException wrong) {
            System.out.println(wrong.getMessage());
            System.out.println();
            commander.usage();
            System.exit(WRONG_USAGE);
        }

        if (fresh.help()) {
            commander.usage();
            return Optional.empty();
        } else {
            return Optional.of(fresh);
        }
    }

    private CommandLine() {
        /* empty */
    }
}
