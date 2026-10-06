import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.constants.ClientScriptOpCode;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.js5.Js5Archive;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Writes the combat styles the combat styles tab (interface 884) offers for each category of weapon:
 * each style's label, icon and tooltip, in the order its tiles stand.
 *
 * <p>Script 1142 runs as the tab loads and as the worn items change. With no weapon worn, or a
 * members' weapon on a free world, it offers the unarmed styles; otherwise it switches on the
 * weapon's category (its param 686), and each case calls script 1143 with four styles, each a
 * label, an icon and a tooltip, as constants, the tooltip joined from its lines. A style with an empty label is a tile the tab hides.
 * The task reads those calls from the script's instructions, and stops where the script no longer
 * has that shape.
 */
public final class CombatStyleExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "combatstyles.json");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    private static final int CHOOSE_STYLES = 1142;
    private static final int SET_STYLES = 1143;

    /**
     * Script 1143 takes four styles, each a label, an icon and a tooltip: two strings and a number.
     */
    private static final int STYLES = 4;
    private static final int STRINGS_A_STYLE = 2;

    /**
     * The instructions that build a call's arguments: a string or a number pushed, and strings joined.
     */
    private static final Set<Integer> PUSHES = Set.of(
        ClientScriptOpCode.PUSH_CONSTANT_STRING,
        ClientScriptOpCode.PUSH_CONSTANT_INT,
        ClientScriptOpCode.JOIN_STRING
    );

    /**
     * The categories the task asks the switch for. The cache's weapons use 0 to 27.
     */
    private static final int CATEGORIES = 256;

    private static final int SCRIPT_FILE = 0;

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportCombatStyles", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var data = Cache.js5(args.where.cache(), Js5Archive.CLIENTSCRIPTS).getfile(SCRIPT_FILE, CHOOSE_STYLES);
        var script = ClientScript.decode(data);

        var switchAt = indexOf(script, ClientScriptOpCode.SWITCH, 0);
        var unarmed = callAfter(script, 0);
        if (switchAt == -1 || unarmed == -1 || unarmed > switchAt) {
            stop("offer the unarmed styles before it switches on the weapon's category");
        }

        var categories = new ArrayList<Map<String, Object>>();
        var table = script.switchTables[script.intOperands[switchAt]];
        for (var category = 0; category < CATEGORIES; category++) {
            var jump = (IntNode) table.get(category);
            if (jump != null) {
                var call = callAfter(script, switchAt + jump.value + 1);
                if (call == -1) {
                    stop("offer styles for category " + category);
                }
                var written = new LinkedHashMap<String, Object>();
                written.put("category", category);
                written.put("styles", stylesOf(script, call));
                categories.add(written);
            }
        }

        var file = new LinkedHashMap<String, Object>();
        file.put("unarmed", stylesOf(script, unarmed));
        file.put("categories", categories);
        if (args.out.getParent() != null) {
            Files.createDirectories(args.out.getParent());
        }
        Files.writeString(args.out, Json.write(file), StandardCharsets.UTF_8);
        System.out.println(categories.size() + " categories of weapon, written to " + args.out.toAbsolutePath().normalize());
    }

    private static int indexOf(ClientScript script, int opcode, int from) {
        for (var at = from; at < script.opcodes.length; at++) {
            if (script.opcodes[at] == opcode) {
                return at;
            }
        }
        return -1;
    }

    /**
     * Where the first call of script 1143 is from an instruction on, or -1 for none.
     */
    private static int callAfter(ClientScript script, int from) {
        for (var at = from; at < script.opcodes.length; at++) {
            if (script.opcodes[at] == ClientScriptOpCode.GOSUB_WITH_PARAMS && script.intOperands[at] == SET_STYLES) {
                return at;
            }
        }
        return -1;
    }

    /**
     * The styles a call gives, each its label, its icon and its tooltip, without the tiles it hides.
     * They are worked out from the constants pushed and joined just before the call: the labels and
     * tooltips in turn on the strings, the icons on the numbers.
     */
    private static List<Map<String, Object>> stylesOf(ClientScript script, int call) {
        var from = call;
        while (from > 0 && PUSHES.contains(script.opcodes[from - 1])) {
            from--;
        }
        var strings = new ArrayList<String>();
        var ints = new ArrayList<Integer>();
        for (var at = from; at < call; at++) {
            switch (script.opcodes[at]) {
                case ClientScriptOpCode.PUSH_CONSTANT_STRING -> strings.add(script.stringOperands[at]);
                case ClientScriptOpCode.PUSH_CONSTANT_INT -> ints.add(script.intOperands[at]);
                default -> {
                    var count = script.intOperands[at];
                    var joined = String.join("", strings.subList(strings.size() - count, strings.size()));
                    strings.subList(strings.size() - count, strings.size()).clear();
                    strings.add(joined);
                }
            }
        }
        if (strings.size() != STYLES * STRINGS_A_STYLE || ints.size() != STYLES) {
            stop("give each style as a label, an icon and a tooltip in constants");
        }

        var styles = new ArrayList<Map<String, Object>>();
        for (var style = 0; style < STYLES; style++) {
            var label = strings.get(style * STRINGS_A_STYLE);
            if (!label.isEmpty()) {
                var written = new LinkedHashMap<String, Object>();
                written.put("label", label);
                written.put("icon", ints.get(style));
                written.put("tooltip", strings.get(style * STRINGS_A_STYLE + 1));
                styles.add(written);
            }
        }
        return styles;
    }

    private static void stop(String shape) {
        System.out.println("script " + CHOOSE_STYLES + " no longer has the shape the export reads: it does not " + shape);
        System.exit(1);
    }
}
