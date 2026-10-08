import com.jagex.graphics.TextureMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Turns one model of the models archive into the mesh of the model library: the model as the
 * client builds it for every type that names it, before a type merges it with others, mirrors,
 * recolours, retextures, tints, lights or scales it.
 *
 * <p>Coordinates are as {@link ModelToGltf} writes them: the client's frame turned half a turn
 * about x, one tile to the metre, so a position times 512 is the client's whole number. The
 * standard attributes make the model look as the client draws it at the ambient and contrast most
 * models are built with, so that a tool shows it as it is. The attributes whose names start with
 * an underscore are the client's own, which an engine builds a type's model from as the client
 * does ({@code LocType.model}, {@code NPCType.getModel}):
 *
 * <ul>
 *   <li>{@code _HSL}: the face's colour as the client holds it, which a type recolours and tints,
 *       and which the palette turns into a colour under the type's ambient, as four unsigned
 *       bytes, as the ground writes it: the hue of 64, the saturation of 8, the lightness of 128
 *       and a spare.
 *   <li>{@code _ALPHA}: the face's alpha, 0 opaque and 255 invisible.
 *   <li>{@code _SHADING}: how the face is shaded, 0 smooth, 1 flat, 2 hidden, 3 black, or another
 *       the client has no case for.
 *   <li>{@code _FACE_LABEL}: the face's label, which the colour and alpha transforms of a frame
 *       act on, or -1.
 *   <li>{@code _VERTEX}: the client's vertex, which a merge joins with the vertices of the other
 *       models at the same position ({@code Mesh(Mesh[], int)}).
 * </ul>
 *
 * <p>Every one but {@code _HSL} is a float, as glTF asks that each element of a vertex attribute
 * start on a four byte boundary, and a float holds each of these whole numbers exactly.
 *
 * <p>The client works out a model's normals once a type has merged, mirrored, turned and scaled
 * it, from every face, and finds the pivot of a frame from every vertex, drawn or not. So every
 * face and every vertex is written. The faces the client never draws whatever the type, one hidden
 * at a join that no frame can show, one smeared instead of drawn, one of a shading the client has
 * no case for, and one a billboard hides, are in a primitive of their own whose material,
 * {@code hidden}, is wholly see-through, and a vertex no face uses is a point of a primitive of
 * points in that material. A face whose texture skips its faces is drawn by some types, as a type
 * can retexture it, so it is not hidden.
 *
 * <p>Each vertex is bound wholly to the joint of its label, the first joint being for the vertices
 * of no label, so a clip moves the model by its labels in any tool.
 */
public final class LibraryModelToGltf {

    private static final float FINE_PER_METRE = 512.0F;

    private static final int SMOOTH = 0;
    private static final int FLAT = 1;
    private static final int HIDDEN = 2;
    private static final int BLACK = 3;

    private static final int HIDDEN_ALPHA = 255;
    private static final int SMEAR_ALPHA = 254;
    private static final int NO_LABEL = -1;

    private static final int HUE_SHIFT = 10;
    private static final int SATURATION_SHIFT = 7;
    private static final int SATURATION_MASK = 0x7;
    private static final int LIGHTNESS_MASK = 0x7F;

    private final JavaModel model;
    private final Js5TextureSource source;
    private final GltfBuilder gltf;
    private final GltfMaterials materials;
    private final int[] labelOfVertex;
    private final int[] labelOfFace;
    private final List<Integer> labels;
    private final int[] jointOfVertex;
    private final Map<PrimitiveKey, Primitive> primitives = new LinkedHashMap<>();
    private final Primitive hidden = new Primitive(false);
    private final Map<String, Integer> skipped = new TreeMap<>();

    private LibraryModelToGltf(JavaModel model, GltfBuilder gltf, GltfMaterials materials) {
        this.model = model;
        this.source = materials.source();
        this.gltf = gltf;
        this.materials = materials;
        this.labelOfVertex = labelOf(model.vertexLabels, model.vertexCount);
        this.labelOfFace = labelOf(model.faceLabels, model.faceCount);
        this.labels = jointLabels(labelOfVertex);
        this.jointOfVertex = jointsOf(labelOfVertex, labels);
    }

    /**
     * What was written: how many faces some type draws, how many faces no type draws for each
     * reason, how many vertices no face uses, how many primitives, and the label each joint
     * carries, -1 for the first.
     */
    public record Result(int faces, Map<String, Integer> hidden, int unused, int primitives, List<Integer> labels) {
    }

    /**
     * Adds the model's primitives to the mesh the document is building. The caller closes the
     * mesh and writes the skin of the labels the result names.
     */
    public static Result convertInto(GltfBuilder gltf, GltfMaterials materials, JavaModel model) {
        return new LibraryModelToGltf(model, gltf, materials).convert();
    }

    private Result convert() {
        model.calculateNormals();
        var billboarded = billboardHiddenFaces();
        var drawn = 0;
        var used = new boolean[model.vertexCount];

        for (var face = 0; face < model.faceCount; face++) {
            var reason = billboarded.contains(face) ? "hidden by a billboard" : hiddenReason(face);
            if (reason == null) {
                addFace(face, primitiveOf(face));
                drawn++;
            } else {
                addFace(face, hidden);
                skipped.merge(reason, 1, Integer::sum);
            }
            used[model.faceA[face]] = true;
            used[model.faceB[face]] = true;
            used[model.faceC[face]] = true;
        }

        for (var entry : primitives.entrySet()) {
            writeTriangles(entry.getValue(), materials.material(entry.getKey().texture(), entry.getKey().mode()));
        }
        var points = unusedPoints(used);
        if (!hidden.indices.isEmpty() || !points.indices.isEmpty()) {
            var hiddenMaterial = hiddenMaterial();
            if (!hidden.indices.isEmpty()) {
                writeTriangles(hidden, hiddenMaterial);
            }
            if (!points.indices.isEmpty()) {
                writePoints(points, hiddenMaterial);
            }
        }
        var unused = points.indices.size();
        var count = primitives.size() + (hidden.indices.isEmpty() ? 0 : 1) + (unused == 0 ? 0 : 1);
        return new Result(drawn, Collections.unmodifiableMap(new TreeMap<>(skipped)), unused, count, labels);
    }

    /**
     * Why the client never draws a face whatever type names the model, or null where some type
     * may draw it. A face hidden at a join is drawn once a frame fades it in, which only a face
     * with a label can be.
     */
    private String hiddenReason(int face) {
        var alpha = alpha(face);
        var shading = shading(face);
        var texture = texture(face);

        if (alpha == HIDDEN_ALPHA && labelOfFace[face] == NO_LABEL || shading == HIDDEN) {
            return "hidden at a join";
        } else if (alpha == SMEAR_ALPHA) {
            return "smeared instead of drawn";
        } else if (texture != -1 && shading != SMOOTH && shading != FLAT) {
            return "textured with no shading the client draws";
        } else if (texture == -1 && shading != SMOOTH && shading != FLAT && shading != BLACK) {
            return "no shading the client draws";
        } else {
            return null;
        }
    }

    private Set<Integer> billboardHiddenFaces() {
        var faces = new HashSet<Integer>();
        if (model.billboardFaces != null) {
            for (var billboard : model.billboardFaces) {
                if (billboard.hideFace) {
                    faces.add(billboard.face);
                }
            }
        }
        return faces;
    }

    private Primitive primitiveOf(int face) {
        var texture = drawableTexture(face);
        var metrics = texture == -1 ? null : source.getMetrics(texture);
        var key = new PrimitiveKey(texture, alphaMode(face, metrics));
        return primitives.computeIfAbsent(key, ignored -> new Primitive(key.texture() != -1));
    }

    private void addFace(int face, Primitive primitive) {
        var texture = drawableTexture(face);
        var hsl = model.faceColour[face] & 0xFFFF;
        var rgb = rgb(face, texture, hsl);
        var opacity = primitive == hidden ? 0.0F : opacity(alpha(face));
        var us = primitive.textured ? drawnCoordinates(model.texCoordU[face]) : null;
        var vs = primitive.textured ? drawnCoordinates(model.texCoordV[face]) : null;
        var corners = new int[] {model.faceA[face], model.faceB[face], model.faceC[face]};

        for (var corner = 0; corner < corners.length; corner++) {
            var vertex = corners[corner];
            var normal = unitNormal(face, vertex);
            var u = primitive.textured ? us[corner] : 0.0F;
            var v = primitive.textured ? vs[corner] : 0.0F;
            var described = new Corner(vertex, hsl, alpha(face), shading(face), labelOfFace[face], rgb, opacity,
                normal[0], normal[1], normal[2], u, v);
            var known = primitive.numbers.get(described);
            if (known != null) {
                primitive.indices.add(known);
            } else {
                primitive.numbers.put(described, primitive.numbers.size());
                primitive.indices.add(primitive.numbers.size() - 1);
                addCorner(primitive, described);
            }
        }
    }

    private void addCorner(Primitive primitive, Corner corner) {
        addVertex(primitive, corner.vertex());
        primitive.normals.add(corner.normalX());
        primitive.normals.add(-corner.normalY());
        primitive.normals.add(-corner.normalZ());

        primitive.colours.add(Srgb.toLinear(corner.rgb() >> 16 & 0xFF));
        primitive.colours.add(Srgb.toLinear(corner.rgb() >> 8 & 0xFF));
        primitive.colours.add(Srgb.toLinear(corner.rgb() & 0xFF));
        primitive.colours.add(corner.opacity());

        if (primitive.textured) {
            primitive.uvs.add(corner.u());
            primitive.uvs.add(corner.v());
        }

        primitive.hsls.add(corner.hsl());
        primitive.alphas.add(corner.alpha());
        primitive.shadings.add(corner.shading());
        primitive.faceLabels.add(corner.faceLabel());
    }

    /**
     * Writes where a client vertex is, its joint and its number, which every vertex of every
     * primitive carries.
     */
    private void addVertex(Primitive primitive, int vertex) {
        primitive.positions.add(model.vertexX[vertex] / FINE_PER_METRE);
        primitive.positions.add(-model.vertexY[vertex] / FINE_PER_METRE);
        primitive.positions.add(-model.vertexZ[vertex] / FINE_PER_METRE);
        primitive.joints.add(jointOfVertex[vertex]);
        primitive.joints.add(0);
        primitive.joints.add(0);
        primitive.joints.add(0);
        primitive.weights.add(1.0F);
        primitive.weights.add(0.0F);
        primitive.weights.add(0.0F);
        primitive.weights.add(0.0F);
        primitive.vertices.add(vertex);
    }

    /**
     * A unit normal for one corner of a face in the client's frame, for a tool to light the model
     * by: the vertex's sum for a smooth face, the face's own for a flat one, else the face's plane,
     * else up.
     */
    private float[] unitNormal(int face, int vertex) {
        var summed = model.vertexNormals[vertex];
        var flat = model.faceNormals == null ? null : model.faceNormals[face];
        if (shading(face) == SMOOTH && summed != null && hasLength(summed.x, summed.y, summed.z)) {
            return unit(summed.x, summed.y, summed.z);
        } else if (shading(face) == FLAT && flat != null && hasLength(flat.x, flat.y, flat.z)) {
            return unit(flat.x, flat.y, flat.z);
        } else {
            return planeNormal(face);
        }
    }

    private float[] planeNormal(int face) {
        var a = model.faceA[face];
        var b = model.faceB[face];
        var c = model.faceC[face];
        float abX = model.vertexX[b] - model.vertexX[a];
        float abY = model.vertexY[b] - model.vertexY[a];
        float abZ = model.vertexZ[b] - model.vertexZ[a];
        float acX = model.vertexX[c] - model.vertexX[a];
        float acY = model.vertexY[c] - model.vertexY[a];
        float acZ = model.vertexZ[c] - model.vertexZ[a];
        var x = abY * acZ - abZ * acY;
        var y = abZ * acX - abX * acZ;
        var z = abX * acY - abY * acX;
        if (hasLength(x, y, z)) {
            return unit(x, y, z);
        } else {
            return new float[] {0.0F, -1.0F, 0.0F};
        }
    }

    private static boolean hasLength(float x, float y, float z) {
        return x != 0.0F || y != 0.0F || z != 0.0F;
    }

    private static float[] unit(float x, float y, float z) {
        var length = (float) Math.sqrt(x * x + y * y + z * z);
        return new float[] {x / length, y / length, z / length};
    }

    /**
     * One texture coordinate at each corner of a face, as the rasteriser ends up using them: a
     * corner at NaN makes the whole face read the texture at 0 along that axis.
     */
    private static float[] drawnCoordinates(float[] corners) {
        var finite = Float.isFinite(corners[0]) && Float.isFinite(corners[1]) && Float.isFinite(corners[2]);
        return finite ? corners : new float[corners.length];
    }

    private int rgb(int face, int texture, int hsl) {
        var untextured = shading(face) == BLACK ? 0 : GlTint.untextured(hsl, model.WA());
        if (texture == -1) {
            return untextured;
        } else {
            return GlTint.textured(untextured, model.WA(), source.getMetrics(texture));
        }
    }

    /**
     * How opaque a face is: its alpha taken from whole, for every face, textured or not, as the GL
     * toolkit writes it into the vertex colour (GlModel) and multiplies the texture's own alpha by
     * it (GlToolkit.setColourOp, GL_MODULATE).
     */
    private static float opacity(int alpha) {
        return (HIDDEN_ALPHA - alpha) / (float) HIDDEN_ALPHA;
    }

    private GltfMaterials.AlphaMode alphaMode(int face, TextureMetrics metrics) {
        if (metrics != null && metrics.alphaBlendMode == GltfMaterials.ALPHA_BLENDED) {
            return GltfMaterials.AlphaMode.BLEND;
        } else if (metrics != null && metrics.alphaBlendMode == GltfMaterials.ALPHA_CUTOUT) {
            return GltfMaterials.AlphaMode.MASK;
        } else if (alpha(face) != 0) {
            return GltfMaterials.AlphaMode.BLEND;
        } else {
            return GltfMaterials.AlphaMode.OPAQUE;
        }
    }

    /**
     * The material of what the client never draws: wholly see-through, so a tool shows nothing.
     */
    private int hiddenMaterial() {
        var pbr = new LinkedHashMap<String, Object>();
        pbr.put("baseColorFactor", List.of(1.0F, 1.0F, 1.0F, 0.0F));
        pbr.put("metallicFactor", 0.0F);
        pbr.put("roughnessFactor", 1.0F);
        var material = new LinkedHashMap<String, Object>();
        material.put("name", "hidden");
        material.put("pbrMetallicRoughness", pbr);
        material.put("alphaMode", GltfMaterials.AlphaMode.BLEND.name());
        return gltf.material(material);
    }

    private void writeTriangles(Primitive primitive, int material) {
        var attributes = new LinkedHashMap<String, Integer>();
        attributes.put("POSITION", gltf.attribute(primitive.positions.toArray(), 3, "VEC3", true));
        attributes.put("NORMAL", gltf.attribute(primitive.normals.toArray(), 3, "VEC3", false));
        attributes.put("COLOR_0", gltf.attribute(primitive.colours.toArray(), 4, "VEC4", false));
        if (primitive.textured) {
            attributes.put("TEXCOORD_0", gltf.attribute(primitive.uvs.toArray(), 2, "VEC2", false));
        }
        putVertexAttributes(attributes, primitive);
        attributes.put("_HSL", gltf.wholeAttribute(hslBytes(primitive.hsls), 4, "VEC4"));
        attributes.put("_ALPHA", scalar(primitive.alphas));
        attributes.put("_SHADING", scalar(primitive.shadings));
        attributes.put("_FACE_LABEL", scalar(primitive.faceLabels));

        var indices = primitive.indices.stream().mapToInt(Integer::intValue).toArray();
        gltf.primitive(attributes, gltf.indices(indices, primitive.numbers.size()), material, List.of());
    }

    /**
     * Every vertex no face uses, as a point.
     */
    private Primitive unusedPoints(boolean[] used) {
        var points = new Primitive(false);
        for (var vertex = 0; vertex < used.length; vertex++) {
            if (!used[vertex]) {
                addVertex(points, vertex);
                points.indices.add(points.indices.size());
            }
        }
        return points;
    }

    private void writePoints(Primitive points, int material) {
        var attributes = new LinkedHashMap<String, Integer>();
        attributes.put("POSITION", gltf.attribute(points.positions.toArray(), 3, "VEC3", true));
        putVertexAttributes(attributes, points);
        var indices = points.indices.stream().mapToInt(Integer::intValue).toArray();
        gltf.points(attributes, gltf.indices(indices, indices.length), material);
    }

    private void putVertexAttributes(Map<String, Integer> attributes, Primitive primitive) {
        var joints = primitive.joints.stream().mapToInt(Integer::intValue).toArray();
        attributes.put("JOINTS_0", gltf.wholeAttribute(joints, 4, "VEC4"));
        attributes.put("WEIGHTS_0", gltf.attribute(primitive.weights.toArray(), 4, "VEC4", false));
        attributes.put("_VERTEX", scalar(primitive.vertices));
    }

    /**
     * Each colour as the ground writes its own: the hue of 64, the saturation of 8 and the
     * lightness of 128 the client packs into 16 bits, and a spare byte.
     */
    private static int[] hslBytes(List<Integer> hsls) {
        var bytes = new int[hsls.size() * 4];
        for (var i = 0; i < hsls.size(); i++) {
            var hsl = hsls.get(i);
            bytes[i * 4] = hsl >> HUE_SHIFT;
            bytes[i * 4 + 1] = hsl >> SATURATION_SHIFT & SATURATION_MASK;
            bytes[i * 4 + 2] = hsl & LIGHTNESS_MASK;
        }
        return bytes;
    }

    private int scalar(List<Integer> values) {
        var floats = new float[values.size()];
        for (var i = 0; i < floats.length; i++) {
            floats[i] = values.get(i);
        }
        return gltf.attribute(floats, 1, "SCALAR", false);
    }

    private int alpha(int face) {
        return model.faceAlpha == null ? 0 : model.faceAlpha[face] & 0xFF;
    }

    private int shading(int face) {
        return model.shadingType == null ? SMOOTH : model.shadingType[face];
    }

    private int texture(int face) {
        if (model.faceTextures == null || model.faceTextures[face] == -1) {
            return -1;
        } else {
            return model.faceTextures[face] & 0xFFFF;
        }
    }

    /**
     * The face's texture where the texture source can draw it, else -1.
     */
    private int drawableTexture(int face) {
        var texture = texture(face);
        return texture != -1 && source.textureAvailable(texture) ? texture : -1;
    }

    /**
     * The label of each vertex or face, from the client's lists of the vertices or faces of each
     * label, -1 for one of none.
     */
    private static int[] labelOf(int[][] groups, int count) {
        var labels = new int[count];
        Arrays.fill(labels, NO_LABEL);
        if (groups != null) {
            for (var label = 0; label < groups.length; label++) {
                for (var member : groups[label]) {
                    labels[member] = label;
                }
            }
        }
        return labels;
    }

    /**
     * The label of each joint: -1 for the first, then every label a vertex carries, in order.
     */
    private static List<Integer> jointLabels(int[] labelOfVertex) {
        var carried = new TreeSet<Integer>();
        for (var label : labelOfVertex) {
            if (label != NO_LABEL) {
                carried.add(label);
            }
        }
        var labels = new ArrayList<Integer>();
        labels.add(NO_LABEL);
        labels.addAll(carried);
        return List.copyOf(labels);
    }

    private static int[] jointsOf(int[] labelOfVertex, List<Integer> labels) {
        var joints = new int[labelOfVertex.length];
        for (var vertex = 0; vertex < joints.length; vertex++) {
            joints[vertex] = labels.indexOf(labelOfVertex[vertex]);
        }
        return joints;
    }

    private record PrimitiveKey(int texture, GltfMaterials.AlphaMode mode) {
    }

    /**
     * Everything one corner of a face gives its vertex. Two corners that agree on all of it share
     * a vertex.
     */
    private record Corner(int vertex, int hsl, int alpha, int shading, int faceLabel, int rgb, float opacity,
                          float normalX, float normalY, float normalZ, float u, float v) {
    }

    private static final class Primitive {

        private final boolean textured;
        private final FloatList positions = new FloatList();
        private final FloatList normals = new FloatList();
        private final FloatList colours = new FloatList();
        private final FloatList uvs = new FloatList();
        private final List<Integer> joints = new ArrayList<>();
        private final FloatList weights = new FloatList();
        private final List<Integer> vertices = new ArrayList<>();
        private final List<Integer> hsls = new ArrayList<>();
        private final List<Integer> alphas = new ArrayList<>();
        private final List<Integer> shadings = new ArrayList<>();
        private final List<Integer> faceLabels = new ArrayList<>();
        private final Map<Corner, Integer> numbers = new HashMap<>();
        private final List<Integer> indices = new ArrayList<>();

        private Primitive(boolean textured) {
            this.textured = textured;
        }
    }
}
