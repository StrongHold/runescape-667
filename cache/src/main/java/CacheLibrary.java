import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.nio.file.Files;

/**
 * Writes one of the cache's native libraries out to a file.
 *
 * The client only ever asks for the library its own platform needs, so the toolkits built for
 * every other platform sit in the cache unread. Naming one writes it out, which is what puts
 * another platform's shipped toolkit in front of the harness.
 *
 * Names are the ones {@link CacheLibraries} lists, such as linux/x86_64/libsw3d.so.
 */
public final class CacheLibrary {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--library",
            description = "The library to write out, as listCacheLibraries names it",
            required = true
        )
        private String library;

        @Parameter(
            names = "--into",
            description = "The directory to write it under, keeping the name the cache holds it under"
        )
        private File into = new File("build/cache-libraries");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("extractCacheLibrary", new Args(), arguments);

        if (parsed.isPresent()) {
            write(parsed.get());
        }
    }

    private static void write(Args args) throws Exception {
        var index = Cache.index(args.where.cache(), Js5Archive.DLLS);
        var group = index.groupNameTable.find(StringTools.intHashCp1252(args.library));

        if (group < 0) {
            throw new IllegalStateException("The cache holds no library named " + args.library + ".");
        }

        /*
         * A group holding several files keeps them end to end behind a table of their lengths. No
         * library is held that way, so rather than read the table this refuses the group and says
         * why, which is more use than a library that is silently the wrong length.
         */
        if (index.fileCounts[group] > 1) {
            throw new IllegalStateException(
                args.library + " is held as " + index.fileCounts[group] + " files rather than one.");
        }

        /*
         * Written under the name the cache holds it under, platform and all. Several platforms
         * name their toolkit the same thing, so writing it under the last part alone would leave
         * one platform's library standing where another's was asked for.
         */
        var target = new File(args.into, args.library);
        target.getParentFile().mkdirs();

        var library = Cache.group(args.where.cache(), Js5Archive.DLLS, group);
        Files.write(target.toPath(), library);

        System.out.println("wrote " + library.length + " bytes of " + args.library + " to " + target);
    }

    private CacheLibrary() {
        /* empty */
    }
}
