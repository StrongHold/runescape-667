import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.js5.Js5Archive;
import type.ConfigKind;
import type.ConfigKinds;
import type.TypeFile;
import type.ui.QuickChatCategoryKind;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Writes every entry of every config type the client decodes, one JSON file an entry, each with
 * every field the client's decoder reads under the client's name for it ({@link ConfigKind}), and
 * reads every entry of the types another export writes into one file of their own, to check them.
 * It stops at a code no field holds, at bytes a decoder leaves unread, and at a group of the config
 * archive that no kind reads and the client does not leave undecoded.
 */
public final class TypeExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The directory to write into, relative to the export module when not absolute"
        )
        private Path out = Path.of("build");

        @Parameter(names = "--kind", description = "The directory of a kind to write; repeat for several, omit for all")
        private List<String> kinds = new ArrayList<>();

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /**
     * The directory the groups of the defaults archive are written to ({@link DefaultsExport}).
     */
    private static final String DEFAULTS = "defaults";

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportTypes", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        var written = new ArrayList<>(ConfigKinds.written(new CacheArchives(cache)));
        written.add(new QuickChatPhraseKind(Js5Archive.QUICKCHAT, 0));
        written.add(new QuickChatPhraseKind(Js5Archive.QUICKCHAT_GLOBAL, QuickChatCategoryKind.GLOBAL));
        var checked = ConfigKinds.checked();

        var all = new ArrayList<ConfigKind<?>>(written);
        all.addAll(checked);
        var unknown = new TreeSet<>(args.kinds);
        all.forEach(kind -> unknown.remove(kind.directory()));
        unknown.remove(DEFAULTS);
        if (!unknown.isEmpty()) {
            throw new IllegalArgumentException("No kind writes " + unknown + ". The kinds are "
                + all.stream().map(ConfigKind::directory).distinct().toList() + ".");
        }

        reportUndecoded(cache, all);
        var counts = new TreeMap<String, Integer>();
        for (var kind : written) {
            if (args.kinds.isEmpty() || args.kinds.contains(kind.directory())) {
                counts.merge(kind.directory(), read(cache, kind, args.out.resolve(kind.directory())), Integer::sum);
            }
        }
        for (var kind : checked) {
            if (args.kinds.isEmpty() || args.kinds.contains(kind.directory())) {
                System.out.println("checked " + read(cache, kind, null) + " " + kind.directory()
                    + ", which another export writes");
            }
        }
        counts.forEach((directory, count) -> System.out.println("wrote " + count + " " + directory + " to "
            + args.out.resolve(directory).toAbsolutePath().normalize()));
        if (args.kinds.isEmpty() || args.kinds.contains(DEFAULTS)) {
            DefaultsExport.write(cache, args.out.resolve(DEFAULTS));
        }
    }

    /**
     * Stops where the config archive holds a group that no kind reads and the client does not leave
     * undecoded, and says how many entries each group the client leaves undecoded holds.
     */
    private static void reportUndecoded(File cache, List<ConfigKind<?>> kinds) throws Exception {
        var index = Cache.index(cache, Js5Archive.CONFIG);
        var unread = new TreeSet<Integer>();
        var undecoded = new TreeMap<Integer, Integer>();
        for (var group : Cache.groupsOf(index)) {
            var read = kinds.stream().anyMatch(kind -> kind.archive().archive() == Js5Archive.CONFIG
                && kind.archive().group() == group);
            if (ConfigKinds.UNDECODED_CONFIG_GROUPS.containsKey(group)) {
                undecoded.put(group, index.fileCounts[group]);
            } else if (!read) {
                unread.add(group);
            }
        }
        if (!unread.isEmpty()) {
            throw new IllegalStateException("The config archive holds groups " + unread + ", which no kind reads.");
        }
        for (Map.Entry<Integer, Integer> entry : undecoded.entrySet()) {
            System.out.println("left out group " + entry.getKey() + " of the config archive, " + entry.getValue()
                + " entries: " + ConfigKinds.UNDECODED_CONFIG_GROUPS.get(entry.getKey()));
        }
    }

    /**
     * Reads every entry of a kind, and writes each into a directory where one is given, and says how
     * many there were.
     */
    private static int read(File cache, ConfigKind<?> kind, Path directory) throws Exception {
        var archive = kind.archive();
        var index = Cache.index(cache, archive.archive());
        var groups = new ArrayList<Integer>();
        for (var group : Cache.groupsOf(index)) {
            if (archive.holdsEveryGroup() || archive.group() == group) {
                groups.add(group);
            }
        }

        if (directory != null) {
            Files.createDirectories(directory);
        }
        var count = 0;
        for (var group : groups) {
            var files = Cache.split(Cache.group(cache, archive.archive(), group), index, group);
            for (var entry : files.entrySet()) {
                var id = archive.id(group, entry.getKey());
                var file = TypeFile.of(kind, id, entry.getValue());
                if (directory != null) {
                    Files.writeString(directory.resolve(id + ".json"), Json.write(file), StandardCharsets.UTF_8);
                }
                count++;
            }
        }
        return count;
    }
}
