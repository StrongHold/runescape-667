/**
 * Arguments that carry a help flag.
 *
 * JCommander sets the flag while it reads the arguments, so the tool only learns that help was
 * asked for once everything else has been read. This is how {@link CommandLine} asks.
 */
public interface Helpable {

    boolean help();
}
