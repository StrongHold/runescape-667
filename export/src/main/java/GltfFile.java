import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes a glTF document as text, `<name>.gltf`, with the buffer it points into beside it as
 * `<name>.bin`, which the document names by a relative URI. A document with no buffer has no
 * `.bin`.
 */
public final class GltfFile {

    private static final String GLTF = ".gltf";
    private static final String BIN = ".bin";

    public static void write(Path file, Map<String, Object> document, byte[] bin) throws IOException {
        var parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        var written = new LinkedHashMap<>(document);
        if (bin.length > 0) {
            var binName = binName(file);
            written.put("buffers", List.of(Map.of("byteLength", bin.length, "uri", binName)));
            Files.write(file.resolveSibling(binName), bin);
        }
        Files.writeString(file, Json.write(written), StandardCharsets.UTF_8);
    }

    /**
     * The file beside a glTF file that has the same name with another ending, such as the JSON of a
     * type's data.
     */
    public static Path sibling(Path file, String ending) {
        return file.resolveSibling(baseName(file) + ending);
    }

    private static String binName(Path file) {
        return baseName(file) + BIN;
    }

    private static String baseName(Path file) {
        var name = file.getFileName().toString();
        return name.endsWith(GLTF) ? name.substring(0, name.length() - GLTF.length()) : name;
    }

    /**
     * Pads to the next multiple of four bytes, which every buffer view starts on.
     */
    public static byte[] pad(byte[] bytes, byte filler) {
        var padded = new byte[aligned(bytes.length)];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        for (var i = bytes.length; i < padded.length; i++) {
            padded[i] = filler;
        }
        return padded;
    }

    public static int aligned(int length) {
        return (length + 3) & ~3;
    }

    private GltfFile() {
        /* empty */
    }
}
