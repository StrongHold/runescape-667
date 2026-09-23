/**
 * What a tool is given on its command line.
 *
 * Every tool reads its arguments into a class of its own that implements this, and JCommander
 * fills in that class's annotated fields. Help is one of those fields, so a tool only learns that
 * help was asked for once everything else has been read. This is how {@link CommandLine} asks.
 */
public interface Arguments {

    boolean help();
}
