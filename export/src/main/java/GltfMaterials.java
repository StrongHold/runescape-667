import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The materials of one glTF document, and the textures they draw with.
 *
 * Each texture the document draws with is taken from the {@link TextureLibrary} and referred to
 * by its path from the file being written, and each pairing of a texture and a way of blending
 * becomes one material, however many meshes use it. A document that holds the ground of a map
 * square and every location on it draws the same few textures over and over, so this is what
 * keeps them from being named more than once.
 */
public final class GltfMaterials {

    /**
     * How a face is laid over what is behind it: not at all, cut out where its texture has holes,
     * or blended by its alpha.
     */
    public enum AlphaMode {
        OPAQUE, MASK, BLEND
    }

    public static final int ALPHA_CUTOUT = TextureLibrary.ALPHA_CUTOUT;
    public static final int ALPHA_BLENDED = TextureLibrary.ALPHA_BLENDED;

    private static final float MASK_CUTOFF = 0.5F;

    private final GltfBuilder gltf;
    private final Js5TextureSource source;
    private final TextureLibrary library;
    private final Path file;
    private final Map<Integer, Integer> textures = new HashMap<>();
    private final Map<MaterialKey, Integer> materials = new HashMap<>();

    /**
     * @param file the file the document is written to, which its textures are referred to from.
     */
    public GltfMaterials(GltfBuilder gltf, Js5TextureSource source, TextureLibrary library, Path file) {
        this.gltf = gltf;
        this.source = source;
        this.library = library;
        this.file = file;
    }

    public Js5TextureSource source() {
        return source;
    }

    /**
     * The material for faces of a texture, or of colour alone where the texture is -1, laid over
     * what is behind them in one way. A textured material says in its {@code extras} whether the
     * texture is {@code disableable}: one the client leaves off when the player turns textures
     * off, drawing the face in its {@code COLOR_1} instead. Water and the like are not. It also
     * says the texture's {@code colourOp}, which is how the GL toolkit combines a texel with the
     * lit vertex colour: 0 multiplies them, 1 shows the texel alone, 2 interpolates, 3 adds them,
     * and 4 takes a dot product ({@code GlToolkit.method6991}).
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
        if (texture != -1) {
            var metrics = source.getMetrics(texture);
            var extras = new LinkedHashMap<String, Object>();
            extras.put("disableable", metrics.disableable);
            extras.put("colourOp", metrics.colorOp);
            extras.put("alpha", metrics.alpha & 0xFF);
            extras.put("brightness", metrics.brightness & 0xFF);
            extras.put("effectType", (int) metrics.effectType);
            extras.put("effectParam1", metrics.effectParam1 & 0xFF);
            extras.put("effectParam2", metrics.effectParam2);
            material.put("extras", extras);
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
        var image = gltf.image(TextureLibrary.relativeUri(file, library.file(id)), "texture " + id);
        var wrapS = metrics.repeatsU ? GltfBuilder.REPEAT : GltfBuilder.CLAMP_TO_EDGE;
        var wrapT = metrics.repeatsV ? GltfBuilder.REPEAT : GltfBuilder.CLAMP_TO_EDGE;
        var minFilter = metrics.mipmap != 0 ? GltfBuilder.LINEAR_MIPMAP_LINEAR : GltfBuilder.LINEAR;

        var texture = gltf.texture(image, wrapS, wrapT, minFilter);
        textures.put(id, texture);
        return texture;
    }

    private record MaterialKey(int texture, AlphaMode mode) {
    }
}
