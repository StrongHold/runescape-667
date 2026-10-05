import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.constants.ClientScriptOpCode;
import com.jagex.js5.Js5Archive;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Writes the sprites the client's scripts build their widgets from: the scrollbar, the button and
 * the checkbox, each a script that other scripts call with the sprites to draw it in.
 *
 * Each script in the cache is decoded with the client's own decoder, and each call of a widget's
 * script is read back from the instructions before it, which push its arguments. Where a caller
 * passes on an argument it was given, the calls of that caller are read in turn. A call whose
 * sprites are known only while the game runs is counted and not written. A sprite that a widget
 * names must be in the cache, or the export stops, since the widget could not be drawn.
 */
public final class WidgetExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "widgets.json");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /**
     * A widget's script, the argument its sprites start at, and the names of its sprites in the
     * order the script takes them.
     */
    private record Widget(String name, int script, int firstSprite, List<String> fields) {
    }

    /**
     * The scrollbar takes the scrollbar's layer and the scrolled layer, then the track, the top,
     * middle and bottom of the dragger, and the up and down arrows. The button takes its layer,
     * then the edge and middle of its plate, and the edge and middle under the pointer. The
     * checkbox takes its layer, then the box, the box under the pointer, and the box held down.
     */
    private static final List<Widget> WIDGETS = List.of(
        new Widget("scrollbars", 31, 2, List.of("track", "draggerTop", "draggerMiddle", "draggerBottom", "upArrow", "downArrow")),
        new Widget("buttons", 3077, 1, List.of("edge", "middle", "hoverEdge", "hoverMiddle")),
        new Widget("checkboxes", 4521, 1, List.of("box", "hoverBox", "pressedBox"))
    );

    /**
     * The file of a script in its group, which holds that script alone ({@code ClientScriptList.list}).
     */
    private static final int SCRIPT_FILE = 0;

    /**
     * An argument of a call: a constant, an argument of the calling script passed on, or neither.
     */
    private record Pushed(Integer constant, Integer local) {
    }

    /**
     * One call of a script: the script that makes it, and each argument, null where it is not known.
     */
    private record Call(int caller, Integer[] arguments) {
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportWidgets", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        var scripts = new TreeMap<Integer, ClientScript>();
        var js5 = Cache.js5(cache, Js5Archive.CLIENTSCRIPTS);
        for (var id : Cache.groupsOf(Cache.index(cache, Js5Archive.CLIENTSCRIPTS))) {
            var data = js5.getfile(SCRIPT_FILE, id);
            if (data != null && data.length > 1) {
                scripts.put(id, ClientScript.decode(data));
            }
        }

        var sprites = new HashSet<Integer>();
        for (var id : Cache.groupsOf(Cache.index(cache, Js5Archive.SPRITES))) {
            sprites.add(id);
        }

        var calls = new Calls(scripts);
        var file = new LinkedHashMap<String, Object>();
        var report = new ArrayList<String>();
        for (var widget : WIDGETS) {
            var skins = new LinkedHashMap<List<Integer>, TreeSet<Integer>>();
            var unknown = 0;
            for (var call : calls.of(widget.script())) {
                var named = Arrays.asList(call.arguments()).subList(widget.firstSprite(), widget.firstSprite() + widget.fields().size());
                if (named.contains(null)) {
                    unknown++;
                } else {
                    skins.computeIfAbsent(named, key -> new TreeSet<>()).add(call.caller());
                }
            }

            var written = new ArrayList<Map<String, Object>>();
            for (var skin : skins.entrySet()) {
                var entry = new LinkedHashMap<String, Object>();
                for (var at = 0; at < widget.fields().size(); at++) {
                    var sprite = skin.getKey().get(at);
                    if (!sprites.contains(sprite)) {
                        System.out.println(widget.name() + " name sprite " + sprite + ", which is not in the cache");
                        System.exit(1);
                    }
                    entry.put(widget.fields().get(at), sprite);
                }
                entry.put("scripts", new ArrayList<>(skin.getValue()));
                written.add(entry);
            }
            file.put(widget.name(), written);
            report.add(widget.name() + ": " + written.size() + " (" + unknown + " calls known only while the game runs)");
        }

        if (args.out.getParent() != null) {
            Files.createDirectories(args.out.getParent());
        }
        Files.writeString(args.out, Json.write(file), StandardCharsets.UTF_8);
        System.out.println("read " + scripts.size() + " scripts: " + String.join(", ", report) + ", written to "
            + args.out.toAbsolutePath().normalize());
    }

    /**
     * The calls of each script in the cache, with the arguments a caller passes on read from the
     * calls of that caller, kept once found.
     */
    private static final class Calls {

        private final Map<Integer, ClientScript> scripts;

        private final Map<Integer, List<Call>> found = new HashMap<>();

        Calls(Map<Integer, ClientScript> scripts) {
            this.scripts = scripts;
        }

        List<Call> of(int callee) {
            return of(callee, new HashSet<>());
        }

        private List<Call> of(int callee, Set<Integer> reading) {
            var known = found.get(callee);
            if (known != null) {
                return known;
            }

            var script = scripts.get(callee);
            var result = new ArrayList<Call>();
            if (script != null && script.stringArgCount == 0 && script.longArgCount == 0 && reading.add(callee)) {
                for (var caller : scripts.entrySet()) {
                    var instructions = caller.getValue();
                    for (var at = 0; at < instructions.opcodes.length; at++) {
                        if (instructions.opcodes[at] == ClientScriptOpCode.GOSUB_WITH_PARAMS && instructions.intOperands[at] == callee) {
                            result.addAll(callsAt(caller.getKey(), instructions, at, script.intArgCount, reading));
                        }
                    }
                }
                reading.remove(callee);
                found.put(callee, result);
            }
            return result;
        }

        /**
         * The calls an instruction makes: one where every argument is pushed as a constant, or one
         * for each call of the caller where it passes on arguments it was given.
         */
        private List<Call> callsAt(int callerId, ClientScript caller, int call, int count, Set<Integer> reading) {
            var pushed = new Pushed[count];
            for (var argument = 0; argument < count; argument++) {
                pushed[argument] = pushedAt(caller, call - count + argument);
            }

            var passesOn = Arrays.stream(pushed).anyMatch(argument -> argument.local() != null);
            if (!passesOn) {
                return List.of(new Call(callerId, resolved(pushed, null)));
            }

            var outer = of(callerId, reading);
            var result = new ArrayList<Call>();
            for (var given : outer) {
                result.add(new Call(callerId, resolved(pushed, given.arguments())));
            }
            if (outer.isEmpty()) {
                result.add(new Call(callerId, resolved(pushed, null)));
            }
            return result;
        }

        private static Pushed pushedAt(ClientScript script, int at) {
            if (at < 0) {
                return new Pushed(null, null);
            } else if (script.opcodes[at] == ClientScriptOpCode.PUSH_CONSTANT_INT) {
                return new Pushed(script.intOperands[at], null);
            } else if (script.opcodes[at] == ClientScriptOpCode.PUSH_INT_LOCAL && script.intOperands[at] < script.intArgCount) {
                return new Pushed(null, script.intOperands[at]);
            } else {
                return new Pushed(null, null);
            }
        }

        private static Integer[] resolved(Pushed[] pushed, Integer[] given) {
            var arguments = new Integer[pushed.length];
            for (var at = 0; at < pushed.length; at++) {
                var argument = pushed[at];
                if (argument.constant() != null) {
                    arguments[at] = argument.constant();
                } else if (argument.local() != null && given != null && argument.local() < given.length) {
                    arguments[at] = given[argument.local()];
                }
            }
            return arguments;
        }
    }

    private WidgetExport() {
        /* empty */
    }
}
