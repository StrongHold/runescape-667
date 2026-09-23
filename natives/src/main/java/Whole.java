import com.beust.jcommander.converters.IParameterSplitter;

import java.util.List;

/**
 * Keeps a repeated option's value whole.
 *
 * JCommander splits the value of an option that collects a list at every comma, but some of the
 * values given here carry commas of their own, a routine's parameters and a pixel's two
 * coordinates among them. Each is given as a repeated option instead, one value to each.
 */
public final class Whole implements IParameterSplitter {

    @Override
    public List<String> split(String value) {
        return List.of(value);
    }
}
