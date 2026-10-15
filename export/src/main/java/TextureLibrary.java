import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.jagex.graphics.EnvironmentLight;

/**
 * The textures every export shares, one PNG for each texture id in one directory.
 *
 * A texture is drawn by the client's own texture source and written once, under its id, and
 * every file that uses it refers to it by a relative path rather than carrying a copy. An engine
 * then loads each texture once, however many files use it, and a texture can be replaced by a
 * better one by replacing one file. For that reason a texture that is already in the library is
 * left as it is, and only a missing one is drawn.
 */
public final class TextureLibrary {

    /**
     * The gamma both toolkits ask the texture source to draw a texture with.
     */
    private static final float TEXTURE_GAMMA = 0.7F;

    private static final String METRICS = "metrics.json";

    private static final int TEXTURE_SIZE_TEXELS = 128;
    private static final int SMALL_TEXTURE_SIZE_TEXELS = 64;

    /**
     * The metrics field no Java code of the client reads: the client hands it only to the native
     * toolkit ({@code oa}), whose use of it is unknown, so it is left out of the metrics file.
     */
    private static final String UNREAD_FIELD = "unusedFlag";

    /**
     * What names a texture's PNG of high dynamic range scales, after its id.
     */
    private static final String SCALES = ".hdr.png";

    /**
     * The value of a texture's high dynamic range op that brightens a texel the most, 32 times
     * ({@code Texture.floatArgbOutput}), which its PNG of scales writes as 255.
     */
    private static final int FULL_SCALE = 4096;
    private static final int GREY_LEVELS = 255;

    /**
     * The argument the client's float output passes its high dynamic range op for a row.
     */
    private static final int SCALES_ROW_ARGUMENT = 115;

    public static final int ALPHA_CUTOUT = 1;
    public static final int ALPHA_BLENDED = 2;

    private final Js5TextureSource source;
    private final Path directory;

    public TextureLibrary(Js5TextureSource source, Path directory) {
        this.source = source;
        this.directory = directory;
    }

    /**
     * The directory exports keep their textures in unless told another: beside the models, NPCs
     * and squares under the export module's build directory.
     */
    public static Path defaultDirectory() {
        return Path.of("build", "textures");
    }

    public Path directory() {
        return directory;
    }

    /**
     * Where a texture's PNG is, written if the library does not hold it yet.
     */
    public Path file(int id) {
        var file = directory.resolve(id + ".png");
        if (!Files.exists(file)) {
            write(id, file);
        }
        writeScales(id);
        return file;
    }

    /**
     * Writes every texture the cache holds that is not in the library yet, with the scales of each
     * whose metrics ask for them where the library lacks them.
     *
     * @return how many textures were written.
     */
    public int writeAll() {
        var written = 0;
        for (var id = 0; id < source.textureCount(); id++) {
            if (source.getMetrics(id) != null && source.textureAvailable(id)) {
                var file = directory.resolve(id + ".png");
                if (!Files.exists(file)) {
                    write(id, file);
                    written++;
                }
                writeScales(id);
            }
        }
        return written;
    }

    /**
     * Writes the metrics of every texture the cache holds into {@code metrics.json}, unless the
     * library holds them already: a list indexed by the texture's id, null where the cache has no
     * texture of that id, each every public field of the client's {@code TextureMetrics} by its
     * name, and whether the texture source can draw it, as {@code available}. An engine that builds
     * a type's model reads from them how each texture blends, tints its faces and whether it skips
     * them, for a texture a type retextures a face to as for any other.
     */
    public void writeMetrics() {
        var file = directory.resolve(METRICS);
        if (!Files.exists(file)) {
            var list = new ArrayList<Object>();
            for (var id = 0; id < source.textureCount(); id++) {
                var metrics = source.getMetrics(id);
                if (metrics == null) {
                    list.add(null);
                } else {
                    var fields = new LinkedHashMap<String, Object>(PublicFields.of(metrics));
                    fields.remove(UNREAD_FIELD);
                    fields.put("available", source.textureAvailable(id));
                    list.add(fields);
                }
            }
            try {
                Files.createDirectories(directory);
                Files.writeString(file, Json.write(list), StandardCharsets.UTF_8);
            } catch (IOException failure) {
                throw new UncheckedIOException("Could not write the texture metrics to " + file, failure);
            }
        }
    }

    /**
     * How many textures the cache holds metrics for, whether or not each can be drawn.
     */
    public int count() {
        return source.textureCount();
    }

    /**
     * The path a file at {@code from} refers to a texture by, relative to the directory the file
     * is in, as glTF asks a URI to be.
     */
    public static String relativeUri(Path from, Path texture) {
        var fromDirectory = Optional.ofNullable(from.toAbsolutePath().normalize().getParent())
            .orElseThrow(() -> new IllegalArgumentException(from + " is in no directory"));
        var relative = fromDirectory.relativize(texture.toAbsolutePath().normalize());
        return relative.toString().replace(relative.getFileSystem().getSeparator(), "/");
    }

    private void write(int id, Path file) {
        var metrics = source.getMetrics(id);
        var size = metrics.small ? SMALL_TEXTURE_SIZE_TEXELS : TEXTURE_SIZE_TEXELS;
        var pixels = source.argbOutput(TEXTURE_GAMMA, id, size, size);
        try {
            Files.createDirectories(directory);
            Files.write(file, png(pixels, size, metrics.alphaBlendMode));
        } catch (IOException failure) {
            throw new UncheckedIOException("Could not write texture " + id + " to " + file, failure);
        }
    }

    /**
     * Writes a texture's high dynamic range scales as {@code <id>.hdr.png}, where its metrics ask
     * for them ({@code hdr}) and the library lacks them: the client draws such a texture in floating
     * point where bloom is on, each texel brightened by its own scale from 1 to 32 times, {@code 1 +
     * 31 * value / 4096}, the value of the texture's high dynamic range op
     * ({@code Texture.floatArgbOutput}). The PNG is grey, a texel's value out of 4096 as a byte out of
     * 255, held between them, laid out texel for texel as the texture's own PNG.
     */
    private void writeScales(int id) {
        var metrics = source.getMetrics(id);
        var file = directory.resolve(id + SCALES);
        if (metrics != null && metrics.hdr && !Files.exists(file)) {
            var size = metrics.small ? SMALL_TEXTURE_SIZE_TEXELS : TEXTURE_SIZE_TEXELS;
            var scales = scalesOf(id, size, metrics.transposed);
            try {
                Files.createDirectories(directory);
                Files.write(file, greyPng(scales, size));
            } catch (IOException failure) {
                throw new UncheckedIOException("Could not write the scales of texture " + id + " to " + file, failure);
            }
        }
    }

    /**
     * A texture's high dynamic range op drawn as the client's float output draws it, each row from
     * its last texel to its first and transposed where the texture is, as its colour is drawn: the
     * op's value at each texel, held between 0 and 4096, as a byte out of 255.
     */
    private int[] scalesOf(int id, int size, boolean transposed) {
        var texture = source.list(id);
        Static582.aJs5_108 = source.sprites;
        Static677.anTextureSource_11 = source;
        for (var op : texture.ops) {
            op.initCache(size, size);
        }
        EnvironmentLight.setTextureOpSize(size, size);
        var scales = new int[size * size];
        var index = 0;
        for (var y = 0; y < size; y++) {
            var row = texture.hdrOp.monochrome
                ? texture.hdrOp.monochromeOutput(SCALES_ROW_ARGUMENT, y)
                : texture.hdrOp.method9414(y)[0];
            if (transposed) {
                index = y;
            }
            for (var x = size - 1; x >= 0; x--) {
                var value = Math.clamp(row[x], 0, FULL_SCALE);
                scales[index++] = Math.round((float) value * GREY_LEVELS / FULL_SCALE);
                if (transposed) {
                    index += size - 1;
                }
            }
        }
        for (var op : texture.ops) {
            op.cacheReset();
        }
        return scales;
    }

    private static byte[] greyPng(int[] levels, int size) {
        var image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
        var raster = image.getRaster();
        for (var i = 0; i < levels.length; i++) {
            raster.setSample(i % size, i / size, 0, levels[i]);
        }

        var out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", out);
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
        return out.toByteArray();
    }

    /**
     * The texture as a PNG, with the alpha the client's rasteriser reads from it: its own alpha
     * where the texture blends, none at all where it is cut out except that a texel of zero is a
     * hole, and fully opaque otherwise. The texels are stored a row at a time from the top, which
     * is also how glTF lays out texture coordinates.
     */
    private static byte[] png(int[] pixels, int size, int blendMode) {
        var image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for (var i = 0; i < pixels.length; i++) {
            var texel = pixels[i];
            var alpha = switch (blendMode) {
                case ALPHA_BLENDED -> texel >>> 24;
                case ALPHA_CUTOUT -> texel == 0 ? 0 : 0xFF;
                default -> 0xFF;
            };
            image.setRGB(i % size, i / size, alpha << 24 | texel & 0xFFFFFF);
        }

        var out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", out);
        } catch (IOException failure) {
            throw new UncheckedIOException(failure);
        }
        return out.toByteArray();
    }
}
