import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects the parts of a glTF document and the one buffer they all point into.
 *
 * Each part is added once and named by its place in its list, which is how glTF refers to one
 * part from another. Everything written into the buffer starts on a four byte boundary, which is
 * the strictest alignment any accessor here asks for.
 *
 * Primitives are added to the mesh being built, which {@link #mesh} closes. A document of one
 * mesh worn by one node is written by {@link #json(String)}, and a document of many by
 * {@link #json(List)}, from the nodes added with {@link #node}.
 */
public final class GltfBuilder {

    public static final int FLOAT = 5126;
    public static final int UNSIGNED_BYTE = 5121;
    public static final int ARRAY_BUFFER = 34962;
    public static final int ELEMENT_ARRAY_BUFFER = 34963;
    public static final int UNSIGNED_SHORT = 5123;
    public static final int UNSIGNED_INT = 5125;

    /** The most vertices a primitive may number with unsigned shorts. */
    private static final int MAX_SHORT_VERTICES = 0x10000;
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
    private final List<Object> meshes = new ArrayList<>();
    private final List<Object> nodes = new ArrayList<>();
    private final List<Object> animations = new ArrayList<>();
    private final List<Object> skins = new ArrayList<>();
    private final List<Object> punctualLights = new ArrayList<>();
    private List<Object> primitives = new ArrayList<>();
    private List<String> targetNames = List.of();

    /**
     * Adds a vertex attribute of floats, {@code components} to a vertex.
     *
     * @param bounded whether the accessor carries its least and greatest values, which glTF
     *     requires of a position.
     */
    public int attribute(float[] values, int components, String type, boolean bounded) {
        return accessor(values, components, type, bounded, ARRAY_BUFFER);
    }

    /**
     * Adds the times or the values of an animation. A buffer view that holds them names no
     * binding, because they are never handed to the GPU as they are.
     *
     * @param bounded whether the accessor carries its least and greatest values, which glTF
     *     requires of the times.
     */
    public int animationData(float[] values, String type, int components, boolean bounded) {
        return accessor(values, components, type, bounded, 0);
    }

    private int accessor(float[] values, int components, String type, boolean bounded, int target) {
        var bytes = ByteBuffer.allocate(values.length * Float.BYTES).order(ByteOrder.LITTLE_ENDIAN);
        for (var value : values) {
            bytes.putFloat(value);
        }

        var view = bufferView(bytes.array(), target);
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

    /**
     * Adds a vertex attribute of small whole numbers, {@code components} to a vertex, as unsigned
     * bytes where they all fit and unsigned shorts otherwise, which is how glTF asks for the
     * joints of a vertex.
     */
    public int wholeAttribute(int[] values, int components, String type) {
        var greatest = 0;
        for (var value : values) {
            greatest = Math.max(greatest, value);
        }
        var wide = greatest > 0xFF;
        var bytes = ByteBuffer.allocate(values.length * (wide ? Short.BYTES : Byte.BYTES)).order(ByteOrder.LITTLE_ENDIAN);
        for (var value : values) {
            if (wide) {
                bytes.putShort((short) value);
            } else {
                bytes.put((byte) value);
            }
        }

        var accessor = new LinkedHashMap<String, Object>();
        accessor.put("bufferView", bufferView(bytes.array(), ARRAY_BUFFER));
        accessor.put("componentType", wide ? UNSIGNED_SHORT : UNSIGNED_BYTE);
        accessor.put("count", values.length / components);
        accessor.put("type", type);
        accessors.add(accessor);
        return accessors.size() - 1;
    }

    /**
     * Adds a skin: the joints a mesh is bound to, in the order its vertices number them.
     *
     * @param inverseBindMatrices the accessor of one matrix for each joint, which takes the mesh
     *     into the joint's frame at bind time.
     */
    public int skin(List<Integer> joints, int inverseBindMatrices, Map<String, Object> extras) {
        var skin = new LinkedHashMap<String, Object>();
        skin.put("joints", joints);
        skin.put("inverseBindMatrices", inverseBindMatrices);
        skin.put("extras", extras);
        skins.add(skin);
        return skins.size() - 1;
    }

    /**
     * Adds a point light of the KHR_lights_punctual extension, which a node wears through
     * {@link #lightNode}.
     *
     * @param colour the light's colour in linear light.
     * @param range how far it reaches, in metres.
     */
    public int punctualLight(String name, float[] colour, float intensity, float range) {
        var light = new LinkedHashMap<String, Object>();
        light.put("name", name);
        light.put("type", "point");
        light.put("color", List.of(colour[0], colour[1], colour[2]));
        light.put("intensity", intensity);
        light.put("range", range);
        punctualLights.add(light);
        return punctualLights.size() - 1;
    }

    /**
     * Adds a node that wears a point light.
     */
    public int lightNode(String name, int light, List<Float> translation, Map<String, Object> extras) {
        var node = new LinkedHashMap<String, Object>();
        node.put("name", name);
        node.put("translation", translation);
        node.put("extensions", Map.of("KHR_lights_punctual", Map.of("light", light)));
        if (!extras.isEmpty()) {
            node.put("extras", extras);
        }
        return node(node);
    }

    /**
     * Adds an animation of any channels, each driven by one of the samplers.
     */
    public void animation(String name, List<Map<String, Object>> samplers, List<Map<String, Object>> channels,
                          Map<String, Object> extras) {
        var animation = new LinkedHashMap<String, Object>();
        animation.put("name", name);
        animation.put("samplers", samplers);
        animation.put("channels", channels);
        animation.put("extras", extras);
        animations.add(animation);
    }

    /**
     * Adds the vertex numbers of a primitive's triangles, as unsigned shorts where the primitive
     * has few enough vertices.
     */
    public int indices(int[] values, int vertices) {
        var wide = vertices > MAX_SHORT_VERTICES;
        var bytes = ByteBuffer.allocate(values.length * (wide ? Integer.BYTES : Short.BYTES))
            .order(ByteOrder.LITTLE_ENDIAN);
        for (var value : values) {
            if (wide) {
                bytes.putInt(value);
            } else {
                bytes.putShort((short) value);
            }
        }

        var accessor = new LinkedHashMap<String, Object>();
        accessor.put("bufferView", bufferView(bytes.array(), ELEMENT_ARRAY_BUFFER));
        accessor.put("componentType", wide ? UNSIGNED_INT : UNSIGNED_SHORT);
        accessor.put("count", values.length);
        accessor.put("type", "SCALAR");
        accessors.add(accessor);
        return accessors.size() - 1;
    }

    /**
     * Adds an image kept in a file of its own, which the document refers to by a path relative
     * to where it is written.
     */
    public int image(String uri, String name) {
        images.add(Map.of("uri", uri, "mimeType", "image/png", "name", name));
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

    /**
     * @param targets the morph targets of the primitive, which every primitive of the mesh has
     *     the same number of.
     */
    public void primitive(Map<String, Integer> attributes, int indices, int material,
                          List<Map<String, Integer>> targets) {
        var primitive = new LinkedHashMap<String, Object>();
        primitive.put("attributes", attributes);
        primitive.put("indices", indices);
        primitive.put("material", material);
        primitive.put("mode", TRIANGLES);
        if (!targets.isEmpty()) {
            primitive.put("targets", targets);
        }
        primitives.add(primitive);
    }

    /**
     * Names the mesh's morph targets, in the place three.js, Blender and Godot all look for them.
     */
    public void targetNames(List<String> names) {
        targetNames = List.copyOf(names);
    }

    /**
     * Adds an animation that sets the weights of the morph targets of the mesh each node named
     * wears, all from the one sampler.
     *
     * @param times the accessor of the key times, in seconds.
     * @param weights the accessor of every target's weight at each key time.
     */
    public void weightAnimation(String name, int times, int weights, String interpolation, List<Integer> nodes,
                                Map<String, Object> extras) {
        var sampler = Map.of("input", times, "output", weights, "interpolation", interpolation);
        var channels = nodes.stream()
            .map(node -> Map.of("sampler", 0, "target", Map.of("node", node, "path", "weights")))
            .toList();

        var animation = new LinkedHashMap<String, Object>();
        animation.put("name", name);
        animation.put("samplers", List.of(sampler));
        animation.put("channels", channels);
        animation.put("extras", extras);
        animations.add(animation);
    }

    /**
     * Whether the mesh being built has no primitive yet.
     */
    public boolean empty() {
        return primitives.isEmpty();
    }

    /**
     * Closes the mesh being built, with every primitive added since the last one was closed, and
     * starts another.
     *
     * @return the mesh's number, which a node names to wear it.
     */
    public int mesh(String name) {
        var mesh = new LinkedHashMap<String, Object>();
        mesh.put("name", name);
        mesh.put("primitives", primitives);
        if (!targetNames.isEmpty()) {
            mesh.put("weights", Collections.nCopies(targetNames.size(), 0.0F));
            mesh.put("extras", Map.of("targetNames", targetNames));
        }

        meshes.add(mesh);
        primitives = new ArrayList<>();
        targetNames = List.of();
        return meshes.size() - 1;
    }

    /**
     * Adds a node, which may wear a mesh, be moved, and hold other nodes.
     *
     * @return the node's number, which a scene or a parent node names it by.
     */
    public int node(Map<String, Object> node) {
        nodes.add(node);
        return nodes.size() - 1;
    }

    /**
     * The one child of a node added earlier.
     */
    @SuppressWarnings("unchecked")
    public int childOf(int node) {
        var children = (List<Integer>) ((Map<String, Object>) nodes.get(node)).get("children");
        return children.getFirst();
    }

    /**
     * The document, holding one scene of one node that wears the one mesh.
     */
    public String json(String name) {
        return json(name, Map.of());
    }

    /**
     * @param extras what the node carries beyond glTF's own properties.
     */
    public String json(String name, Map<String, Object> extras) {
        var node = new LinkedHashMap<String, Object>();
        node.put("name", name);
        node.put("mesh", mesh(name));
        if (!extras.isEmpty()) {
            node.put("extras", extras);
        }
        return json(List.of(node(node)));
    }

    /**
     * The document, holding one scene of the nodes named, which the other nodes hang from.
     */
    public String json(List<Integer> roots) {
        return json(roots, null, Map.of());
    }

    /**
     * @param name what the scene is called, or null for no name.
     * @param extras what the scene carries beyond glTF's own properties.
     */
    public String json(List<Integer> roots, String name, Map<String, Object> extras) {
        var scene = new LinkedHashMap<String, Object>();
        scene.put("nodes", roots);
        if (name != null) {
            scene.put("name", name);
        }
        if (!extras.isEmpty()) {
            scene.put("extras", extras);
        }

        var document = new LinkedHashMap<String, Object>();
        document.put("asset", Map.of("version", "2.0", "generator", "runescape-667 export"));
        document.put("scene", 0);
        document.put("scenes", List.of(scene));
        document.put("nodes", nodes);
        putIfAny(document, "meshes", meshes);
        putIfAny(document, "animations", animations);
        putIfAny(document, "skins", skins);
        if (!punctualLights.isEmpty()) {
            document.put("extensionsUsed", List.of("KHR_lights_punctual"));
            document.put("extensions", Map.of("KHR_lights_punctual", Map.of("lights", punctualLights)));
        }
        putIfAny(document, "materials", materials);
        putIfAny(document, "textures", textures);
        putIfAny(document, "samplers", samplers);
        putIfAny(document, "images", images);
        putIfAny(document, "accessors", accessors);
        putIfAny(document, "bufferViews", bufferViews);
        if (bin.size() > 0) {
            document.put("buffers", List.of(Map.of("byteLength", bin.size())));
        }
        return Json.write(document);
    }

    public byte[] bin() {
        return bin.toByteArray();
    }

    /**
     * @param target the binding a view of vertex data is meant for, or 0 for anything else.
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
