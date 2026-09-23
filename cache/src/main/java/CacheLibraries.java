import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5Index;

import java.util.ArrayList;
import java.util.List;

/**
 * Lists the native libraries the cache holds, for every platform rather than this one.
 *
 * The client asks for one library at a time and only for the platform it is running on, so what
 * lands on disk says nothing about what is available. The index for the library archive names
 * every group in it, so reading that answers the question outright.
 *
 * Names are stored as hashes, so a name can be tested but not read back. Every combination the
 * client could ask for is tested instead, which is the same set its own path building produces,
 * widened enough to leave no group unnamed.
 */
public final class CacheLibraries {

    private static final List<String> LIBRARIES =
        List.of("jaclib", "jaggl", "jagdx", "jagmisc", "jagtheora", "theora", "hw3d", "sw3d");

    private static final List<String> ARCHITECTURES =
        List.of("x86", "x86_64", "universal", "ppc", "ppc64", "msjava", "amd64", "i386", "sparc");

    private static final List<Platform> PLATFORMS = List.of(
        new Platform("windows", "", ".dll"),
        new Platform("linux", "lib", ".so"),
        new Platform("macos", "lib", ".dylib"),
        new Platform("macos", "lib", ".jnilib"),
        new Platform("solaris", "lib", ".so")
    );

    private record Platform(String name, String prefix, String extension) {
        /* empty */
    }

    public static final class Args implements Helpable {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        Args args = new Args();

        if (!CommandLine.parsed("listCacheLibraries", args, arguments)) {
            return;
        }

        Js5Index index = Cache.index(args.where.cache(), Js5Archive.DLLS);

        int named = 0;
        for (Platform platform : PLATFORMS) {
            List<String> found = librariesFor(index, platform);
            named += found.size();

            if (!found.isEmpty()) {
                System.out.println();
                found.forEach(System.out::println);
            }
        }

        System.out.println();
        System.out.println("named " + named + " of " + index.groupCount + " groups");
    }

    private static List<String> librariesFor(Js5Index index, Platform platform) {
        List<String> found = new ArrayList<>();

        for (String architecture : ARCHITECTURES) {
            for (String library : LIBRARIES) {
                String name = platform.name() + "/" + architecture + "/"
                    + platform.prefix() + library + platform.extension();

                if (index.groupNameTable.find(StringTools.intHashCp1252(name)) >= 0) {
                    found.add(name);
                }
            }
        }

        return found;
    }

    private CacheLibraries() {
        /* empty */
    }
}
