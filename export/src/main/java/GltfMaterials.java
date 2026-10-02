import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * The materials of one glTF document, and the textures they draw with.
 *
 * Each texture is drawn by the client's own texture source and kept once as a PNG, and each
 * pairing of a texture and a way of blending becomes one material, however many meshes use it.
 * A document that holds the ground of a map square and every location on it draws the same few
 * textures over and over, so this is what keeps them from being written more than once.
 */
public final class GltfMaterials {

    /**
     * How a face is laid over what is behind it: not at all, cut out where its texture has holes,
     * or blended by its alpha.
     */
    public enum AlphaMode {
        OPAQUE, MASK, BLEND
    }

    /**
     * The gamma both toolkits ask the texture source to draw a texture with.
     */
    private static final float TEXTURE_GAMMA = 0.7F;

    private static final int TEXTURE_SIZE = 128;
    private static final int SMALL_TEXTURE_SIZE = 64;

    public static final int ALPHA_CUTOUT = 1;
    public static final int ALPHA_BLENDED = 2;

    private static final float MASK_CUTOFF = 0.5F;

    private final GltfBuilder gltf;
    private final Js5TextureSource source;
    private final Map<Integer, Integer> textures = new HashMap<>();
    private final Map<MaterialKey, Integer> materials = new HashMap<>();

    public GltfMaterials(GltfBuilder gltf, Js5TextureSource source) {
        this.gltf = gltf;
        this.source = source;
    }

    public Js5TextureSource source() {
        return source;
    }

    /**
     * The material for faces of a texture, or of colour alone where the texture is -1, laid over
     * what is behind them in one way.
     */
    public int material(int texture, AlphaMode mode) {
        var key = new MaterialKey(texture, mode);
        var held = materials.get(key);
        if (held != null) {
            return held;
        }

        var pbr = new LinkedHashMap<String, Object>();
        pbr.put("baseColorFactor", List.of(1.0F, 1.0F, 1.0F, 1.0F));
        pbr.put("metallicFactor", 0.0F);
        pbr.put("roughnessFactor", 1.0F);
        if (texture != -1) {
            pbr.put("baseColorTexture", Map.of("index", gltfTexture(texture)));
        }

        var material = new LinkedHashMap<String, Object>();
        material.put("name", (texture == -1 ? "colour" : "texture " + texture) + " " + mode.name().toLowerCase());
        material.put("pbrMetallicRoughness", pbr);
        material.put("alphaMode", mode.name());
        if (mode == AlphaMode.MASK) {
            material.put("alphaCutoff", MASK_CUTOFF);
        }

        var number = gltf.material(material);
        materials.put(key, number);
        return number;
    }

    private int gltfTexture(int id) {
        var held = textures.get(id);
        if (held != null) {
            return held;
        }

        var metrics = source.getMetrics(id);
        var size = metrics.small ? SMALL_TEXTURE_SIZE : TEXTURE_SIZE;
        var pixels = source.argbOutput(TEXTURE_GAMMA, id, size, size);
        var image = gltf.image(png(pixels, size, metrics.alphaBlendMode), "texture " + id);
        var wrapS = metrics.repeatsU ? GltfBuilder.REPEAT : GltfBuilder.CLAMP_TO_EDGE;
        var wrapT = metrics.repeatsV ? GltfBuilder.REPEAT : GltfBuilder.CLAMP_TO_EDGE;
        var minFilter = metrics.mipmap != 0 ? GltfBuilder.LINEAR_MIPMAP_LINEAR : GltfBuilder.LINEAR;

        var texture = gltf.texture(image, wrapS, wrapT, minFilter);
        textures.put(id, texture);
        return texture;
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

    private record MaterialKey(int texture, AlphaMode mode) {
    }
}
