import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects the parts of a glTF document and the one buffer they all point into.
 *
 * Each part is added once and named by its place in its list, which is how glTF refers to one
 * part from another. Everything written into the buffer starts on a four byte boundary, which is
 * the strictest alignment any accessor here asks for.
 */
public final class GltfBuilder {

    public static final int FLOAT = 5126;
    public static final int ARRAY_BUFFER = 34962;
    public static final int TRIANGLES = 4;

    public static final int LINEAR = 9729;
    public static final int LINEAR_MIPMAP_LINEAR = 9987;
    public static final int REPEAT = 10497;
    public static final int CLAMP_TO_EDGE = 33071;

    private final ByteArrayOutputStream bin = new ByteArrayOutputStream();
    private final List<Object> bufferViews = new ArrayList<>();
    private final List<Object> accessors = new ArrayList<>();
    private final List<Object> images = new ArrayList<>();
    private final List<Object> samplers = new ArrayList<>();
    private final List<Object> textures = new ArrayList<>();
    private final List<Object> materials = new ArrayList<>();
    private final List<Object> primitives = new ArrayList<>();

    /**
     * Adds a vertex attribute of floats, {@code components} to a vertex.
     *
     * @param bounded whether the accessor carries its least and greatest values, which glTF
     *     requires of a position.
     */
    public int attribute(float[] values, int components, String type, boolean bounded) {
        var bytes = ByteBuffer.allocate(values.length * Float.BYTES).order(ByteOrder.LITTLE_ENDIAN);
        for (var value : values) {
            bytes.putFloat(value);
        }

        var view = bufferView(bytes.array(), ARRAY_BUFFER);
        var accessor = new LinkedHashMap<String, Object>();
        accessor.put("bufferView", view);
        accessor.put("componentType", FLOAT);
        accessor.put("count", values.length / components);
        accessor.put("type", type);

        if (bounded) {
            var bounds = bounds(values, components);
            accessor.put("min", bounds.least());
            accessor.put("max", bounds.greatest());
        }

        accessors.add(accessor);
        return accessors.size() - 1;
    }

    public int image(byte[] png, String name) {
        images.add(Map.of("bufferView", bufferView(png, 0), "mimeType", "image/png", "name", name));
        return images.size() - 1;
    }

    public int texture(int image, int wrapS, int wrapT, int minFilter) {
        samplers.add(Map.of("magFilter", LINEAR, "minFilter", minFilter, "wrapS", wrapS, "wrapT", wrapT));
        textures.add(Map.of("sampler", samplers.size() - 1, "source", image));
        return textures.size() - 1;
    }

    public int material(Map<String, Object> material) {
        materials.add(material);
        return materials.size() - 1;
    }

    public void primitive(Map<String, Integer> attributes, int material) {
        primitives.add(Map.of("attributes", attributes, "material", material, "mode", TRIANGLES));
    }

    public boolean empty() {
        return primitives.isEmpty();
    }

    /**
     * The document, holding one scene of one node that wears the one mesh.
     */
    public String json(String name) {
        var document = new LinkedHashMap<String, Object>();
        document.put("asset", Map.of("version", "2.0", "generator", "runescape-667 export"));
        document.put("scene", 0);
        document.put("scenes", List.of(Map.of("nodes", List.of(0))));
        document.put("nodes", List.of(Map.of("name", name, "mesh", 0)));
        document.put("meshes", List.of(Map.of("name", name, "primitives", primitives)));
        document.put("materials", materials);
        putIfAny(document, "textures", textures);
        putIfAny(document, "samplers", samplers);
        putIfAny(document, "images", images);
        document.put("accessors", accessors);
        document.put("bufferViews", bufferViews);
        document.put("buffers", List.of(Map.of("byteLength", bin.size())));
        return Json.write(document);
    }

    public byte[] bin() {
        return bin.toByteArray();
    }

    /**
     * @param target the binding a view of vertex data is meant for, or 0 for an image.
     */
    private int bufferView(byte[] bytes, int target) {
        var offset = bin.size();
        bin.writeBytes(Glb.pad(bytes, (byte) 0));

        var view = new LinkedHashMap<String, Object>();
        view.put("buffer", 0);
        view.put("byteOffset", offset);
        view.put("byteLength", bytes.length);
        if (target != 0) {
            view.put("target", target);
        }

        bufferViews.add(view);
        return bufferViews.size() - 1;
    }

    private static Bounds bounds(float[] values, int components) {
        var least = new ArrayList<Float>();
        var greatest = new ArrayList<Float>();
        for (var component = 0; component < components; component++) {
            var low = values[component];
            var high = values[component];
            for (var i = component; i < values.length; i += components) {
                low = Math.min(low, values[i]);
                high = Math.max(high, values[i]);
            }
            least.add(low);
            greatest.add(high);
        }
        return new Bounds(List.copyOf(least), List.copyOf(greatest));
    }

    private static void putIfAny(Map<String, Object> document, String key, List<Object> parts) {
        if (!parts.isEmpty()) {
            document.put(key, parts);
        }
    }

    private record Bounds(List<Float> least, List<Float> greatest) {
    }
}
