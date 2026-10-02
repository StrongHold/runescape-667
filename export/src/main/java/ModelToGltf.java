import com.jagex.graphics.TextureMetrics;
import com.jagex.math.ColourUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Turns a model the client has built into a glTF mesh.
 *
 * <p>Coordinates. The client's world is x east, y down and z north, measured in 1/512ths of a
 * tile. That is a right handed frame, as glTF's is, so it is turned half a turn about x rather
 * than mirrored: y and z are both negated, which puts y up and north along -z, the way Godot
 * faces. A tile becomes one metre. A turn does not change which way round a triangle's corners
 * go, so each face keeps the order A, B, C. The client draws a face whose corners go
 * anticlockwise on screen and culls the rest, and glTF treats an anticlockwise face as the front,
 * so the front faces stay the front faces.
 *
 * <p>Faces. Every corner of every face is written as a vertex of its own, so that each face keeps
 * its own colour, alpha and texture coordinates exactly. Faces are grouped into one primitive for
 * each texture and way of blending.
 *
 * <p>Poses. Each pose the model is given becomes a morph target of every primitive, which holds
 * how far each corner has moved from where the model holds it, turned the same way as the model.
 * Where a pose also changes the colour or the alpha of a face, as the frames of a flame do, the
 * target holds how far each corner's colour has moved as well.
 */
public final class ModelToGltf {

    /**
     * How many of the client's units make one tile, which becomes one metre.
     */
    private static final float UNITS_PER_METRE = 512.0F;

    private static final int SMOOTH = 0;
    private static final int FLAT = 1;
    private static final int BLACK = 3;

    /**
     * A face alpha of 255 makes the client treat the face as hidden at a join.
     */
    private static final int HIDDEN_ALPHA = 255;

    /**
     * A face alpha of 254 makes the client shade the face black, and its rasteriser then draws
     * nothing of the face's own: it smears the pixels already on screen one to the side.
     */
    private static final int SMEAR_ALPHA = 254;

    /**
     * The light a face is coloured under before the toolkit lights it, as near to full as the
     * client's lightness goes without carrying into the next channel.
     */
    private static final int FULL_LIGHT = 127;

    private final JavaModel model;
    private final Js5TextureSource source;
    private final List<Pose> poses;
    private final GltfBuilder gltf;
    private final GltfMaterials materials;
    private final Map<PrimitiveKey, Primitive> primitives = new LinkedHashMap<>();
    private final Map<String, Integer> skipped = new TreeMap<>();
    private final boolean colourPosed;

    private ModelToGltf(JavaModel model, List<Pose> poses, GltfBuilder gltf, GltfMaterials materials) {
        this.model = model;
        this.source = materials.source();
        this.poses = poses;
        this.gltf = gltf;
        this.materials = materials;
        this.colourPosed = poses.stream().anyMatch(this::changesColour);
    }

    /**
     * Whether a pose gives any face a colour or an alpha other than the model's own.
     */
    private boolean changesColour(Pose pose) {
        for (var face = 0; face < model.faceCount; face++) {
            if (pose.colour()[face] != model.faceColour[face] || (pose.alpha()[face] & 0xFF) != alpha(face)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Turns the model into the one mesh of a new document, drawing on the materials given, which
     * belong to that document.
     *
     * @param poses where the model's vertices are in each pose that is written as a morph target,
     *     in the order the targets are numbered.
     */
    public static Result convert(JavaModel model, GltfBuilder gltf, GltfMaterials materials, List<Pose> poses) {
        return new ModelToGltf(model, poses, gltf, materials).convert();
    }

    /**
     * Adds the model's primitives to the mesh a document is building, drawing on the materials the
     * rest of the document shares. The caller closes the mesh.
     */
    public static Result convertInto(GltfBuilder gltf, GltfMaterials materials, JavaModel model) {
        return convertInto(gltf, materials, model, List.of());
    }

    /**
     * @param poses where the model's vertices are in each pose that is written as a morph target
     *     of every primitive, in the order the targets are numbered.
     */
    public static Result convertInto(GltfBuilder gltf, GltfMaterials materials, JavaModel model, List<Pose> poses) {
        return new ModelToGltf(model, poses, gltf, materials).convert();
    }

    /**
     * The finished document, and how many faces were left out for each reason.
     */
    public record Result(GltfBuilder gltf, int faces, Map<String, Integer> skipped, int primitives) {
    }

    private Result convert() {
        model.calculateNormals();
        var hidden = billboardHiddenFaces();
        var written = 0;

        for (var face = 0; face < model.faceCount; face++) {
            var reason = hidden.contains(face) ? "hidden by a billboard" : skipReason(face);
            if (reason == null) {
                addFace(face);
                written++;
            } else {
                skipped.merge(reason, 1, Integer::sum);
            }
        }

        for (var entry : primitives.entrySet()) {
            writePrimitive(entry.getKey(), entry.getValue());
        }
        return new Result(gltf, written, Collections.unmodifiableMap(new TreeMap<>(skipped)), primitives.size());
    }

    /**
     * Why the client never draws a face, or null when it does.
     *
     * <p>The software toolkit decides in {@code JavaModel.calculateLighting}: a face alpha of 255
     * becomes shading 2, and a face of shading 2, or of a shading it has no case for, is marked
     * with a colour of -2 that {@code drawFaceArgb} returns on. A textured face only has cases
     * for smooth and flat shading.
     *
     * <p>The hardware toolkits also leave out every face whose texture's metrics say
     * {@code skipFaces}, and so does this, since what is drawn there is drawn by something
     * other than the face.
     *
     * <p>A face that is invisible in the model itself but that a pose fades in is kept, as the
     * client draws it once the pose has.
     */
    private String skipReason(int face) {
        var alpha = alpha(face);
        var shading = shading(face);
        var texture = texture(face);

        if (alpha == HIDDEN_ALPHA && invisibleInEveryPose(face) || shading == 2) {
            return "hidden at a join";
        } else if (alpha == SMEAR_ALPHA) {
            return "smeared instead of drawn";
        } else if (texture != -1 && shading != SMOOTH && shading != FLAT) {
            return "textured with no shading the client draws";
        } else if (texture == -1 && shading != SMOOTH && shading != FLAT && shading != BLACK) {
            return "no shading the client draws";
        } else if (texture != -1 && source.getMetrics(texture).skipFaces) {
            return "texture skips its faces";
        } else {
            return null;
        }
    }

    private boolean invisibleInEveryPose(int face) {
        return poses.stream().allMatch(pose -> (pose.alpha()[face] & 0xFF) == HIDDEN_ALPHA);
    }

    /**
     * The faces that a billboard is drawn in place of. The software toolkit keeps, for each
     * billboard, the face it sits on and whether its type hides that face.
     */
    private Set<Integer> billboardHiddenFaces() {
        var hidden = new HashSet<Integer>();

        if (model.billboardFaces != null) {
            for (var billboard : model.billboardFaces) {
                if (billboard.aBoolean464) {
                    hidden.add(billboard.anInt6139);
                }
            }
        }

        return hidden;
    }

    private void addFace(int face) {
        var texture = texture(face);
        var drawable = texture != -1 && source.textureAvailable(texture);
        var metrics = texture == -1 ? null : source.getMetrics(texture);
        var mode = alphaMode(face, drawable ? metrics : null);
        var key = new PrimitiveKey(drawable ? texture : -1, mode);
        var primitive = primitives.computeIfAbsent(key, ignored -> new Primitive(key.texture() != -1, poses.size()));

        var rgb = rgb(face, texture, colour(face));
        var opacity = opacity(drawable ? metrics : null, alpha(face));

        var us = primitive.textured ? drawnCoordinates(model.texCoordU[face]) : null;
        var vs = primitive.textured ? drawnCoordinates(model.texCoordV[face]) : null;
        var corners = new int[] {model.faceA[face], model.faceB[face], model.faceC[face]};
        for (var corner = 0; corner < corners.length; corner++) {
            var vertex = corners[corner];
            var normal = normal(face, vertex);
            var u = primitive.textured ? us[corner] : 0.0F;
            var v = primitive.textured ? vs[corner] : 0.0F;
            var described = new Corner(vertex, normal[0], normal[1], normal[2], rgb, opacity, u, v);

            var known = primitive.numbers.get(described);
            if (known != null) {
                primitive.indices.add(known);
            } else {
                primitive.numbers.put(described, primitive.numbers.size());
                primitive.indices.add(primitive.numbers.size() - 1);
                addCorner(primitive, face, vertex, normal, rgb, opacity, u, v);
            }
        }
    }

    /**
     * Writes one vertex of a primitive: where it is and how it is coloured, in the base pose and
     * in every pose.
     */
    private void addCorner(Primitive primitive, int face, int vertex, float[] normal, int rgb, float opacity,
                           float u, float v) {
        primitive.positions.add(model.vertexX[vertex] / UNITS_PER_METRE);
        primitive.positions.add(-model.vertexY[vertex] / UNITS_PER_METRE);
        primitive.positions.add(-model.vertexZ[vertex] / UNITS_PER_METRE);

        for (var target = 0; target < poses.size(); target++) {
            var pose = poses.get(target);
            var moved = primitive.targets.get(target);
            moved.add((pose.x()[vertex] - model.vertexX[vertex]) / UNITS_PER_METRE);
            moved.add(-(pose.y()[vertex] - model.vertexY[vertex]) / UNITS_PER_METRE);
            moved.add(-(pose.z()[vertex] - model.vertexZ[vertex]) / UNITS_PER_METRE);
        }

        primitive.normals.add(normal[0]);
        primitive.normals.add(-normal[1]);
        primitive.normals.add(-normal[2]);

        addColour(primitive.colours, rgb, opacity);
        if (colourPosed) {
            var texture = texture(face);
            var metrics = primitive.textured ? source.getMetrics(texture) : null;
            for (var target = 0; target < poses.size(); target++) {
                var pose = poses.get(target);
                var posedRgb = rgb(face, texture, pose.colour()[face] & 0xFFFF);
                var posedOpacity = opacity(metrics, pose.alpha()[face] & 0xFF);
                addColourChange(primitive.colourTargets.get(target), rgb, opacity, posedRgb, posedOpacity);
            }
        }

        if (primitive.textured) {
            primitive.uvs.add(u);
            primitive.uvs.add(v);
        }
    }

    private static void addColour(FloatList colours, int rgb, float opacity) {
        colours.add(Srgb.toLinear(rgb >> 16 & 0xFF));
        colours.add(Srgb.toLinear(rgb >> 8 & 0xFF));
        colours.add(Srgb.toLinear(rgb & 0xFF));
        colours.add(opacity);
    }

    private static void addColourChange(FloatList changes, int rgb, float opacity, int posedRgb, float posedOpacity) {
        changes.add(Srgb.toLinear(posedRgb >> 16 & 0xFF) - Srgb.toLinear(rgb >> 16 & 0xFF));
        changes.add(Srgb.toLinear(posedRgb >> 8 & 0xFF) - Srgb.toLinear(rgb >> 8 & 0xFF));
        changes.add(Srgb.toLinear(posedRgb & 0xFF) - Srgb.toLinear(rgb & 0xFF));
        changes.add(posedOpacity - opacity);
    }

    /**
     * One texture coordinate at each corner of a face, as the rasteriser ends up using them.
     *
     * <p>Some texture spaces place a corner at NaN, and the rasteriser carries that across the
     * whole face as it steps from corner to corner. Turning NaN into an int gives 0, so every
     * pixel of the face reads the texture at 0 along that axis.
     */
    private static float[] drawnCoordinates(float[] corners) {
        var finite = Float.isFinite(corners[0]) && Float.isFinite(corners[1]) && Float.isFinite(corners[2]);
        return finite ? corners : new float[corners.length];
    }

    /**
     * A face's colour at full light, given the client's HSL colour it has in some pose. A
     * textured face is tinted by the colour the client multiplies its texels by, which the
     * texture's metrics can lighten or turn grey.
     */
    private int rgb(int face, int texture, int hsl) {
        if (texture == -1) {
            return untexturedRgb(face, hsl);
        } else {
            return model.shadeTexturedRgb(hsl, (short) texture, FULL_LIGHT);
        }
    }

    /**
     * How opaque a face is, given the alpha it has in some pose. A face alpha of 0 is opaque and
     * 255 is invisible. A texture that blends or cuts out by its own alpha takes the place of the
     * face's.
     */
    private static float opacity(TextureMetrics metrics, int alpha) {
        if (metrics != null && metrics.alphaBlendMode != 0) {
            return 1.0F;
        } else {
            return (255 - alpha) / 255.0F;
        }
    }

    /**
     * The colour an untextured face has at full light, before any light falls on it. The palette
     * the client draws through is gamma corrected for the screen, so this is an sRGB colour.
     *
     * <p>A black face is one the software toolkit gives the palette entry 128, which is black.
     */
    private int untexturedRgb(int face, int hsl) {
        if (shading(face) == BLACK) {
            return 0;
        } else {
            return ColourUtils.HSL_TO_RGB[hsl];
        }
    }

    /**
     * How the client blends a face. The software rasteriser blends a textured face by its
     * texture's alpha blend mode when it has one, and only falls back to the face's own alpha
     * when it has none. A face that any pose makes see-through is blended, so that the pose can.
     */
    private GltfMaterials.AlphaMode alphaMode(int face, TextureMetrics metrics) {
        if (metrics != null && metrics.alphaBlendMode == GltfMaterials.ALPHA_BLENDED) {
            return GltfMaterials.AlphaMode.BLEND;
        } else if (metrics != null && metrics.alphaBlendMode == GltfMaterials.ALPHA_CUTOUT) {
            return GltfMaterials.AlphaMode.MASK;
        } else if (alpha(face) != 0 || seeThroughInSomePose(face)) {
            return GltfMaterials.AlphaMode.BLEND;
        } else {
            return GltfMaterials.AlphaMode.OPAQUE;
        }
    }

    private boolean seeThroughInSomePose(int face) {
        return poses.stream().anyMatch(pose -> pose.alpha()[face] != 0);
    }

    /**
     * A unit normal for one corner of a face, in the client's frame.
     *
     * <p>{@code calculateNormals} sums the normals of every smooth face that meets at a vertex,
     * and gives a flat face a normal of its own. Its normals point out of the front of a face. A
     * black face gets neither, and nor does a face of no area, so they are worked out here, and a
     * face with no area at all is given straight up.
     */
    private float[] normal(int face, int vertex) {
        var shading = shading(face);

        if (shading == SMOOTH && hasSummedNormal(vertex)) {
            var summed = model.vertexNormals[vertex];
            return unit(summed.x, summed.y, summed.z);
        } else if (shading == FLAT && hasFlatNormal(face)) {
            var flat = model.faceNormals[face];
            return unit(flat.x, flat.y, flat.z);
        } else {
            return planeNormal(face);
        }
    }

    private boolean hasSummedNormal(int vertex) {
        var summed = model.vertexNormals[vertex];
        return summed.magnitude > 0 && hasLength(summed.x, summed.y, summed.z);
    }

    private boolean hasFlatNormal(int face) {
        var flat = model.faceNormals == null ? null : model.faceNormals[face];
        return flat != null && hasLength(flat.x, flat.y, flat.z);
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

    private void writePrimitive(PrimitiveKey key, Primitive primitive) {
        var attributes = new LinkedHashMap<String, Integer>();
        attributes.put("POSITION", gltf.attribute(primitive.positions.toArray(), 3, "VEC3", true));
        attributes.put("NORMAL", gltf.attribute(primitive.normals.toArray(), 3, "VEC3", false));
        attributes.put("COLOR_0", gltf.attribute(primitive.colours.toArray(), 4, "VEC4", false));
        if (primitive.textured) {
            attributes.put("TEXCOORD_0", gltf.attribute(primitive.uvs.toArray(), 2, "VEC2", false));
        }

        var targets = new ArrayList<Map<String, Integer>>();
        for (var target = 0; target < primitive.targets.size(); target++) {
            var moved = new LinkedHashMap<String, Integer>();
            moved.put("POSITION", gltf.attribute(primitive.targets.get(target).toArray(), 3, "VEC3", true));
            if (colourPosed) {
                moved.put("COLOR_0", gltf.attribute(primitive.colourTargets.get(target).toArray(), 4, "VEC4", false));
            }
            targets.add(moved);
        }

        var indices = primitive.indices.stream().mapToInt(Integer::intValue).toArray();
        gltf.primitive(attributes, gltf.indices(indices, primitive.numbers.size()), material(key), targets);
    }

    private int material(PrimitiveKey key) {
        return materials.material(key.texture(), key.mode());
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

    private int colour(int face) {
        return model.faceColour[face] & 0xFFFF;
    }

    private record PrimitiveKey(int texture, GltfMaterials.AlphaMode mode) {
    }

    /**
     * Everything one corner of a face gives its vertex. Two corners that agree on all of it share
     * a vertex. The client's own vertex number is part of it, because two vertices in one place
     * can move apart when the model is posed.
     */
    private record Corner(int vertex, float normalX, float normalY, float normalZ, int rgb,
                          float opacity, float u, float v) {
    }

    /**
     * The corners of every face that one primitive holds, a component at a time.
     */
    private static final class Primitive {

        private final boolean textured;
        private final FloatList positions = new FloatList();
        private final FloatList normals = new FloatList();
        private final FloatList colours = new FloatList();
        private final FloatList uvs = new FloatList();
        private final List<FloatList> targets = new ArrayList<>();
        private final List<FloatList> colourTargets = new ArrayList<>();
        private final Map<Corner, Integer> numbers = new HashMap<>();
        private final List<Integer> indices = new ArrayList<>();

        private Primitive(boolean textured, int poses) {
            this.textured = textured;
            for (var pose = 0; pose < poses; pose++) {
                targets.add(new FloatList());
                colourTargets.add(new FloatList());
            }
        }
    }
}
