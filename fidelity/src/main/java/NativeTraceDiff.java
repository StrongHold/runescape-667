import com.beust.jcommander.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Sets two records written by {@link NativeTrace} side by side, method by method.
 *
 * The calls that build the ground are made in the same order by both clients, one tile after
 * another, so the calls of each method are compared in the order they were made. The first calls
 * that differ are shown with the arguments that differ picked out, because the first difference is
 * nearly always the cause of the rest.
 */
public final class NativeTraceDiff {

    public static final class Args implements Arguments {

        @Parameter(names = "--jar", required = true, description = "The record the jar's client wrote")
        private File jar;

        @Parameter(names = "--ours", required = true, description = "The record the recompiled client wrote")
        private File ours;

        @Parameter(names = "--shown", description = "How many differing calls of each method to show")
        private int shown = 3;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws IOException {
        var parsed = CommandLine.parse("diffNativeTraces", new Args(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(Args args) throws IOException {
        var jar = byMethod(args.jar);
        var ours = byMethod(args.ours);
        var methods = new LinkedHashSet<String>(jar.keySet());
        methods.addAll(ours.keySet());

        for (var method : methods) {
            var theirs = jar.getOrDefault(method, List.of());
            var mine = ours.getOrDefault(method, List.of());
            var differing = 0;
            var first = new ArrayList<String>();

            for (var at = 0; at < Math.min(theirs.size(), mine.size()); at++) {
                if (!theirs.get(at).equals(mine.get(at))) {
                    differing++;
                    if (first.size() < args.shown * 3) {
                        first.add("    call " + at + ", arguments " + differingArguments(theirs.get(at), mine.get(at)));
                        first.add("      jar  " + clip(theirs.get(at)));
                        first.add("      ours " + clip(mine.get(at)));
                    }
                }
            }

            if (differing > 0 || theirs.size() != mine.size()) {
                System.out.println(method + ": " + theirs.size() + " calls in the jar, " + mine.size()
                    + " in ours, " + differing + " of those in both differ");
                for (var line : first) {
                    System.out.println(line);
                }
            }
        }
    }

    private static Map<String, List<String>> byMethod(File file) throws IOException {
        var calls = new LinkedHashMap<String, List<String>>();
        for (var line : Files.readAllLines(file.toPath())) {
            var open = line.indexOf('(');
            if (open > 0) {
                calls.computeIfAbsent(line.substring(0, open), ignored -> new ArrayList<>())
                    .add(line.substring(open));
            }
        }
        return calls;
    }

    /**
     * Which arguments differ, counted from nought, splitting only on commas outside an array.
     */
    private static List<Integer> differingArguments(String theirs, String mine) {
        var a = split(theirs);
        var b = split(mine);
        var differ = new ArrayList<Integer>();
        for (var at = 0; at < Math.max(a.size(), b.size()); at++) {
            if (at >= a.size() || at >= b.size() || !a.get(at).equals(b.get(at))) {
                differ.add(at);
            }
        }
        return differ;
    }

    private static List<String> split(String call) {
        var parts = new ArrayList<String>();
        var depth = 0;
        var start = 1;
        for (var at = 1; at < call.length(); at++) {
            var c = call.charAt(at);
            if (c == '[') {
                depth++;
            } else if (c == ']') {
                depth--;
            } else if ((c == ',' && depth == 0) || (c == ')' && depth == 0)) {
                parts.add(call.substring(start, at).trim());
                start = at + 1;
            }
        }
        return parts;
    }

    private static String clip(String call) {
        return call.length() > 400 ? call.substring(0, 400) + "..." : call;
    }

    private NativeTraceDiff() {
        /* empty */
    }
}
