import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.js5.Js5Archive;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * Writes the hash of the name of every group of the interfaces, client scripts, songs and jingles
 * archives, which the archives' indexes keep in place of the names, so that a name from another
 * source can be checked against a group.
 */
public final class NameHashExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The directory to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "hashes");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    /** The archives whose names are written, by the file each is written to. */
    private static final Map<String, Integer> ARCHIVES = Map.of(
        "interfaces.json", Js5Archive.INTERFACES,
        "scripts.json", Js5Archive.CLIENTSCRIPTS,
        "songs.json", Js5Archive.MIDI_SONGS,
        "jingles.json", Js5Archive.MIDI_JINGLES
    );

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportNameHashes", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        Files.createDirectories(args.out);
        for (var entry : new TreeMap<>(ARCHIVES).entrySet()) {
            var index = Cache.index(args.where.cache(), entry.getValue());
            var hashes = new TreeMap<String, Object>();
            if (index.groupNames != null) {
                for (var group : Cache.groupsOf(index)) {
                    hashes.put(Integer.toString(group), index.groupNames[group]);
                }
            }
            var file = args.out.resolve(entry.getKey());
            Files.writeString(file, Json.write(hashes), StandardCharsets.UTF_8);
            System.out.println("wrote the name hashes of " + hashes.size() + " groups of archive " + entry.getValue()
                + " to " + file.toAbsolutePath().normalize());
        }
    }
}
