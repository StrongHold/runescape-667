import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * Reads a tool's arguments, and says whether it should go on to do its work.
 *
 * Every tool in this build is a main method run from a Gradle task, and each one wants the same
 * three things: the arguments read into an object, a usage message when they are wrong or when
 * help is asked for, and a failing exit status when they are wrong. Doing that here leaves each
 * tool holding only what it is actually for.
 */
public final class CommandLine {

    /**
     * Reads the arguments into the given object.
     *
     * @return whether the tool should run. Help was asked for, or the arguments were wrong, when
     *         this is false, and either way the reason has already been printed.
     */
    public static boolean parsed(String tool, Object into, String[] arguments) {
        var commander = JCommander.newBuilder()
            .programName(tool)
            .addObject(into)
            .build();

        try {
            commander.parse(arguments);
        } catch (ParameterException wrong) {
            System.out.println(wrong.getMessage());
            System.out.println();
            commander.usage();
            System.exit(1);
            return false;
        }

        if (helpWanted(into)) {
            commander.usage();
            return false;
        }

        return true;
    }

    /**
     * Whether the arguments carry a help flag that was given.
     *
     * The flag is optional: a tool small enough to have no arguments of its own has nothing to
     * print help about, so it is not made to declare one.
     */
    private static boolean helpWanted(Object into) {
        if (into instanceof Helpable helpable) {
            return helpable.help();
        } else {
            return false;
        }
    }

    private CommandLine() {
        /* empty */
    }
}
