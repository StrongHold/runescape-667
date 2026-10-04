import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.constants.ClientScriptOpCode;
import com.jagex.js5.Js5Archive;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Writes how the client's scripts style the mini menu: the colours and the sprites that the script
 * command {@code FORMATMINIMENU} gives it, which make the client draw the menu with sprites, and
 * the scripts that switch it back to the plain menu with {@code DEFAULTMINIMENU}.
 *
 * Each script in the cache is decoded with the client's own decoder, and each call of the command
 * is read back from the instructions before it, which push its eleven arguments. An argument the
 * script does not push as a constant is not written, and the export stops, since the menu's style
 * would then be known only while the game runs.
 */
public final class MiniMenuExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "minimenu.json");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /**
     * The arguments of {@code FORMATMINIMENU} in the order the script pushes them, under the names
     * of the client's fields they are stored in ({@code ScriptRunner}).
     */
    private static final List<String> FIELDS = List.of(
        "topColour",
        "topOpacity",
        "spriteBodyColour",
        "spriteBodyOpacity",
        "separatorSpriteId",
        "topCornerSpriteId",
        "horizontalBorderSpriteId",
        "verticalBorderSpriteId",
        "bottomCornerSpriteId",
        "textColour",
        "spriteHighlightColour"
    );

    /**
     * The file of a script in its group, which holds that script alone ({@code ClientScriptList.list}).
     */
    private static final int SCRIPT_FILE = 0;

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportMiniMenu", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        var scripts = Cache.js5(cache, Js5Archive.CLIENTSCRIPTS);
        var formats = new TreeMap<String, Object>();
        var defaults = new ArrayList<Integer>();
        var unread = new ArrayList<String>();
        var read = 0;
        for (var id : Cache.groupsOf(Cache.index(cache, Js5Archive.CLIENTSCRIPTS))) {
            var data = scripts.getfile(SCRIPT_FILE, id);
            if (data != null && data.length > 1) {
                var script = ClientScript.decode(data);
                read++;
                for (var at = 0; at < script.opcodes.length; at++) {
                    if (script.opcodes[at] == ClientScriptOpCode.FORMATMINIMENU) {
                        var format = format(script, at);
                        if (format == null) {
                            unread.add("script " + id + " at instruction " + at);
                        } else {
                            formats.put(Integer.toString(id), format);
                        }
                    } else if (script.opcodes[at] == ClientScriptOpCode.DEFAULTMINIMENU && !defaults.contains(id)) {
                        defaults.add(id);
                    }
                }
            }
        }

        if (!unread.isEmpty()) {
            System.out.println("FORMATMINIMENU is called with an argument that is not a constant in " + String.join(", ", unread));
            System.exit(1);
        }

        var file = new LinkedHashMap<String, Object>();
        file.put("formats", formats);
        file.put("defaults", defaults);
        if (args.out.getParent() != null) {
            Files.createDirectories(args.out.getParent());
        }
        Files.writeString(args.out, Json.write(file), StandardCharsets.UTF_8);
        System.out.println("read " + read + " scripts: " + formats.size() + " style the mini menu with sprites and "
            + defaults.size() + " switch it back, written to " + args.out.toAbsolutePath().normalize());
    }

    /**
     * The arguments of the call at an instruction, from the eleven instructions before it, or
     * null where any of them does not push a constant.
     */
    private static Map<String, Object> format(ClientScript script, int call) {
        var first = call - FIELDS.size();
        if (first < 0) {
            return null;
        }
        var format = new LinkedHashMap<String, Object>();
        for (var field = 0; field < FIELDS.size(); field++) {
            var at = first + field;
            if (script.opcodes[at] != ClientScriptOpCode.PUSH_CONSTANT_INT) {
                return null;
            }
            format.put(FIELDS.get(field), script.intOperands[at]);
        }
        return format;
    }

    private MiniMenuExport() {
        /* empty */
    }
}
