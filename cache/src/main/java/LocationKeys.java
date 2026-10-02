import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The keys that lock the locations of the map's squares.
 *
 * Only the server that serves the world holds them, and it hands the client the key of every
 * square it enters. They are read here from a directory of one file per square, named for the
 * square's group of locations, such as {@code l50_50.txt}, which holds the four numbers of the key
 * one to a line.
 */
public final class LocationKeys {

    /**
     * The variable that can name where the keys are kept, and the server's own copy of them, read
     * when neither it nor an argument names a place.
     */
    private static final String VARIABLE = "SW3D_LOCATION_KEYS";
    private static final Path SERVER_COPY = Path.of(System.getProperty("user.home"),
        "code/stronghold/game/share/location-keys");

    private static final int KEY_PARTS = 4;

    /**
     * Where the keys are when nothing names a place: the directory {@code SW3D_LOCATION_KEYS}
     * names, or else the server's copy beside this checkout.
     */
    public static Path defaultDirectory() {
        var named = System.getenv(VARIABLE);
        return named == null || named.isEmpty() ? SERVER_COPY : Path.of(named);
    }

    /**
     * The key of one square's locations, by the name of their group, or nothing when the
     * directory holds no key for it.
     */
    public static Optional<int[]> read(Path directory, String name) throws IOException {
        var file = directory.resolve(name + ".txt");
        if (!Files.isReadable(file)) {
            return Optional.empty();
        }

        var lines = Files.readAllLines(file);
        var key = new int[KEY_PARTS];
        for (var part = 0; part < key.length; part++) {
            key[part] = Integer.parseInt(lines.get(part).trim());
        }
        return Optional.of(key);
    }

    /**
     * Whether a key is the key of nothing, which the client takes to mean the group is not locked.
     */
    public static boolean isOpen(int[] key) {
        return key[0] == 0 && key[1] == 0 && key[2] == 0 && key[3] == 0;
    }

    private LocationKeys() {
        /* empty */
    }
}
