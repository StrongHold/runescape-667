import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.constants.ModeGame;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkType;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkTypeList;
import com.jagex.js5.Js5Archive;
import type.Lists;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Writes what the client draws hit splats with: every hitmark type, and the graphics defaults that
 * say how many splats an entity shows at once, where each one stands, and how long overhead chat
 * stays up.
 *
 * Each type is decoded with the client's own type list ({@code HitmarkTypeList.list}) and the
 * defaults with the client's own decoder ({@code GraphicsDefaults}). A sprite or a font that a type
 * names must be in the cache, or the export stops, since a splat that draws with it could not be
 * drawn.
 */
public final class HitmarkExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "hitmarks.json");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    private static final int LANGUAGE = 0;

    /**
     * What the client keeps in a field that names nothing: no sprite, no font, no fade, no rule to
     * displace a splat.
     */
    private static final int NONE = -1;

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportHitmarks", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        var config = Cache.js5(cache, Js5Archive.CONFIG);
        var list = new HitmarkTypeList(ModeGame.RUNESCAPE, LANGUAGE, config, Cache.js5(cache, Js5Archive.SPRITES));
        var sprites = Set.of(boxed(Cache.groupsOf(Cache.index(cache, Js5Archive.SPRITES))));
        var fonts = Set.of(boxed(Cache.groupsOf(Cache.index(cache, Js5Archive.FONTMETRICS))));

        var types = new TreeMap<Integer, Object>();
        var missing = new TreeSet<String>();
        var usedFonts = new TreeSet<Integer>();
        var limit = config.fileLimit(Js5ConfigGroup.HITMARKTYPE);
        for (var id = 0; id < limit; id++) {
            if (config.getfile(id, Js5ConfigGroup.HITMARKTYPE) != null) {
                var type = list.list(id);
                types.put(id, type(type));
                for (var sprite : List.of(type.icon, type.left, type.inner, type.right)) {
                    if (sprite != NONE && !sprites.contains(sprite)) {
                        missing.add("type " + id + " names sprite " + sprite);
                    }
                }
                if (type.font != NONE) {
                    usedFonts.add(type.font);
                    if (!fonts.contains(type.font)) {
                        missing.add("type " + id + " names font " + type.font);
                    }
                }
            }
        }

        if (!missing.isEmpty()) {
            System.out.println("A hitmark type names what the cache does not hold: " + String.join(", ", missing));
            System.exit(1);
        }

        var file = new LinkedHashMap<String, Object>();
        file.put("defaults", defaults(new GraphicsDefaults(Cache.js5(cache, Js5Archive.DEFAULTS))));
        var named = new LinkedHashMap<String, Object>();
        types.forEach((id, type) -> named.put(Integer.toString(id), type));
        file.put("types", named);
        if (args.out.getParent() != null) {
            Files.createDirectories(args.out.getParent());
        }
        Files.writeString(args.out, Json.write(file), StandardCharsets.UTF_8);
        System.out.println("wrote " + types.size() + " hitmark types, which draw with fonts " + usedFonts
            + ", and the graphics defaults to " + args.out.toAbsolutePath().normalize());
    }

    /**
     * A type's every field, under the client's names, with null for a field that names nothing.
     */
    private static Map<String, Object> type(HitmarkType type) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("icon", orNull(type.icon));
        fields.put("left", orNull(type.left));
        fields.put("inner", orNull(type.inner));
        fields.put("right", orNull(type.right));
        fields.put("font", orNull(type.font));
        fields.put("textColour", type.textColour);
        fields.put("amountString", type.amountString);
        fields.put("offsetX", type.offsetX);
        fields.put("offsetY", type.offsetY);
        fields.put("textOffsetY", type.textOffsetY);
        fields.put("duration", type.duration);
        fields.put("fadeTime", orNull(type.fadeTime));
        fields.put("comparisonType", orNull(type.comparisonType));
        return fields;
    }

    /**
     * The defaults' every field, under the client's names, with null for a model that names nothing
     * and for recolour tables the cache does not give.
     */
    private static Map<String, Object> defaults(GraphicsDefaults defaults) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("maxhitmarks", defaults.maxhitmarks);
        fields.put("hitmarkpos_x", Lists.ints(defaults.hitmarkpos_x));
        fields.put("hitmarkpos_y", Lists.ints(defaults.hitmarkpos_y));
        fields.put("npcShouldDisplayChat", defaults.npcShouldDisplayChat);
        fields.put("npcChatTimeout", defaults.npcChatTimeout);
        fields.put("playerShouldDisplayChat", defaults.playerShouldDisplayChat);
        fields.put("playerChatTimeout", defaults.playerChatTimeout);
        fields.put("profilingModel", orNull(defaults.profilingModel));
        fields.put("login_interface", defaults.login_interface);
        fields.put("lobby_interface", defaults.lobby_interface);
        fields.put("recol_s", defaults.recol_s == null ? null : shortTable(defaults.recol_s));
        fields.put("recol_d", defaults.recol_d == null ? null : shortTables(defaults.recol_d));
        return fields;
    }

    private static Object orNull(int value) {
        return value == NONE ? null : value;
    }

    private static List<List<Integer>> shortTable(short[][] table) {
        var rows = new ArrayList<List<Integer>>();
        for (var row : table) {
            rows.add(shorts(row));
        }
        return rows;
    }

    private static List<List<List<Integer>>> shortTables(short[][][] tables) {
        var all = new ArrayList<List<List<Integer>>>();
        for (var table : tables) {
            all.add(shortTable(table));
        }
        return all;
    }

    private static List<Integer> shorts(short[] values) {
        var list = new ArrayList<Integer>();
        for (var value : values) {
            list.add((int) value);
        }
        return list;
    }

    private static Integer[] boxed(int[] values) {
        var boxed = new Integer[values.length];
        for (var at = 0; at < values.length; at++) {
            boxed[at] = values[at];
        }
        return boxed;
    }

    private HitmarkExport() {
        /* empty */
    }
}
