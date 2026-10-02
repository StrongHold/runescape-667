import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes a binary glTF file: a header, then the JSON document as one chunk and the buffer it
 * points into as another.
 *
 * Every chunk has to start on a four byte boundary, so the JSON is padded with spaces and the
 * buffer with zeros, which is what the format asks each to be padded with.
 */
public final class Glb {

    private static final int MAGIC = 0x46546C67;
    private static final int VERSION = 2;
    private static final int JSON_CHUNK = 0x4E4F534A;
    private static final int BIN_CHUNK = 0x004E4942;
    private static final int HEADER_BYTES = 12;
    private static final int CHUNK_HEADER_BYTES = 8;

    public static void write(Path file, String json, byte[] bin) throws IOException {
        var text = pad(json.getBytes(StandardCharsets.UTF_8), (byte) ' ');
        var data = pad(bin, (byte) 0);
        var length = HEADER_BYTES + CHUNK_HEADER_BYTES + text.length
            + (data.length == 0 ? 0 : CHUNK_HEADER_BYTES + data.length);

        var out = ByteBuffer.allocate(length).order(ByteOrder.LITTLE_ENDIAN);
        out.putInt(MAGIC).putInt(VERSION).putInt(length);
        out.putInt(text.length).putInt(JSON_CHUNK).put(text);
        if (data.length > 0) {
            out.putInt(data.length).putInt(BIN_CHUNK).put(data);
        }

        var parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(file, out.array());
    }

    /**
     * Pads to the next multiple of four bytes, which every chunk and buffer view starts on.
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

    private Glb() {
        /* empty */
    }
}
