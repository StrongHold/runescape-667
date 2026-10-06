import com.jagex.graphics.TextureMetrics;
import com.jagex.math.ColourUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * Turns the tiles of one level of a map square, as the client's software toolkit holds them, into
 * glTF primitives.
 *
 * <p>With ground blending on and textures enabled, the toolkit keeps each tile as a list of
 * triangles, three vertices to a face. Each vertex holds where it is within its tile, its height,
 * its colour once the underlays are smoothed and the overlays blended in, and the texture and
 * texture size of what it is made of. That is everything the rasteriser reads to draw the tile, so
 * it is read here as it is.
 *
 * <p>Coordinates are turned as a model's are: x east, y up and north along -z, one tile to a
 * metre. The square's south west corner is the origin, and heights keep the client's level, so a
 * location placed on the ground lands on it.
 *
 * <p>Faces. The rasteriser skips a face whose first vertex has no colour, which is how the toolkit
 * leaves out the part of a tile that nothing covers. A textured face is tinted by its vertex
 * colours, as the rasteriser tints it.
 *
 * <p>Texture coordinates. The client works out a ground texture's coordinates from where each
 * vertex is in the world, divided by the size the floor type gives the texture, so a texture runs
 * on unbroken from tile to tile. They are worked out the same way here, measured from a multiple
 * of the texture's size near the map square, so the numbers stay small and two squares written apart
 * still meet.
 *
 * <p>Normals. The client lights each corner of a tile from the slope across the two tiles either
 * side of it, and lights a point within a tile by blending its four corners. The same slope gives
 * each corner a normal here, and a point within a tile blends the normals of its four corners.
 *
 * <p>Colours. {@code COLOR_0} is the tile's own colour at each vertex, lit and lerped to the
 * water as the software toolkit holds it in {@code vertexColours}. It tints the texture, and it
 * is what the client shows with textures off, when it builds the tiles without textures, so a
 * textured face carries no second colour. The GL toolkit tints with the overlay's blend colour
 * instead, which the tile keeps only per face: taking a face's first corner for all three
 * corners breaks the blend across a face whose corners differ, so that colour is not used. The
 * material says whether its texture is {@code disableable}, and water and the like stay
 * textured.
 */
public final class GroundToGltf {

    private static final float UNITS_PER_METRE = 512.0F;
    private static final int TILE = 512;

    private static final int OPAQUE_ALPHA = 0xFF;
    /**
     * The alpha the software ground gives a vertex of a water texture ({@code JavaGround.U}).
     */
    private static final int WATER_EFFECT_ALPHA = 0x9B;

    private static final float[] ALL_CORNERS = {1.0F, 1.0F, 1.0F};

    private final JavaGround ground;
    private final int origin;
    private final int originX;
    private final int originZ;
    private final RecordingGround colours;
    private final boolean underwater;
    private final GltfBuilder gltf;
    private final GltfMaterials materials;
    private final Map<PrimitiveKey, Primitive> primitives = new LinkedHashMap<>();
    private final Map<String, Integer> skipped = new TreeMap<>();
    private final Map<String, Integer> approximated = new TreeMap<>();
    private final Map<Integer, Integer> layered = new TreeMap<>();

    private GroundToGltf(JavaGround ground, RecordingGround colours, boolean underwater, int origin, int worldX,
                         int worldZ, GltfBuilder gltf, GltfMaterials materials) {
        this.ground = ground;
        this.colours = colours;
        this.underwater = underwater;
        this.origin = origin;
        this.originX = worldX;
        this.originZ = worldZ;
        this.gltf = gltf;
        this.materials = materials;
    }

    /**
     * What one level came to.
     *
     * @param tiles how many tiles of the map square the level has.
     * @param faces how many faces the client draws, each counted once however many layers it is
     *     written as.
     * @param approximated how many faces are written other than as the client draws them, and why.
     * @param layered how many faces are written as layers, by how many layers.
     */
    public record Result(int tiles, int faces, int primitives, Map<String, Integer> skipped,
                         Map<String, Integer> approximated, Map<Integer, Integer> layered) {
    }

    /**
     * Adds the primitives of one level's tiles inside a square to the mesh a document is building.
     * The caller closes the mesh.
     *
     * @param origin where the map square starts in the region the ground covers, in tiles.
     * @param worldX where the map square starts in the world, in tiles, which places its textures.
     */
    public static Result convertInto(GltfBuilder gltf, GltfMaterials materials, JavaGround ground,
                                     RecordingGround colours, boolean underwater, int origin, int worldX, int worldZ) {
        return new GroundToGltf(ground, colours, underwater, origin, worldX, worldZ, gltf, materials).convert();
    }

    private Result convert() {
        var tiles = 0;
        var faces = 0;

        for (var x = origin; x < origin + ClientMapSquareReader.TILES_ACROSS; x++) {
            for (var z = origin; z < origin + ClientMapSquareReader.TILES_ACROSS; z++) {
                var blended = ground.genericBlendedTiles == null ? null : ground.genericBlendedTiles[x][z];
                var unblended = colours.unblended(x, z);
                if (blended != null) {
                    tiles++;
                    faces += addTile(x, z, blended);
                } else if (unblended != null) {
                    var written = addUnblendedTile(x, z, unblended);
                    tiles += written > 0 ? 1 : 0;
                    faces += written;
                }
            }
        }

        for (var entry : primitives.entrySet()) {
            writePrimitive(entry.getKey(), entry.getValue());
        }
        return new Result(tiles, faces, primitives.size(), Collections.unmodifiableMap(skipped),
            Collections.unmodifiableMap(approximated), Collections.unmodifiableMap(layered));
    }

    private int addTile(int x, int z, JavaGenericBlendedTile tile) {
        var written = 0;

        for (var face = 0; face < tile.faceCount; face++) {
            var a = face * 3;
            if ((tile.vertexColours[a] & 0xFFFFFF) == 0) {
                skipped.merge("no colour at its first corner", 1, Integer::sum);
            } else {
                addFace(x, z, tile, a);
                written++;
            }
        }

        return written;
    }

    /**
     * Writes the faces of a tile built with ground blending off, each in its one colour: the
     * floor's colour, or where it has none, its blend colour, as the software ground keeps a face
     * that has either. A face with neither is left out, as the ground leaves it out.
     */
    private int addUnblendedTile(int x, int z, RecordingGround.UnblendedTile tile) {
        var written = 0;
        for (var face = 0; face < tile.colours().length; face++) {
            var hsl = tile.colours()[face] >= 0 ? tile.colours()[face]
                : tile.blendedColours() == null ? -1 : tile.blendedColours()[face];
            if (hsl < 0) {
                skipped.merge("no colour of its own or to blend", 1, Integer::sum);
            } else {
                addUnblendedFace(x, z, tile, face, hsl);
                written++;
            }
        }
        return written;
    }

    /**
     * Writes one face of an unblended tile with its texture, or in its colour alone where the
     * toolkit holds no such texture. Each corner's colour is the face's, made RGB as the software
     * ground makes a blended vertex's ({@code JavaGround.U}): through the palette, at the full light
     * the export builds every ground with, and towards the water's colour as deep as the corner
     * lies under it.
     */
    private void addUnblendedFace(int x, int z, RecordingGround.UnblendedTile tile, int face, int hsl) {
        var texture = drawable(tile.textures()[face]) ? tile.textures()[face] : -1;
        var size = tile.sizes()[face];
        var metrics = metrics(texture);
        var waterEffect = metrics != null && ground.isWaterEffect(metrics.effectType);
        var alpha = waterEffect ? WATER_EFFECT_ALPHA : OPAQUE_ALPHA;
        var mode = alphaMode(metrics, alpha);
        var water = underwater ? tile.water() : RecordingGround.Water.NONE;
        var primitive = primitives.computeIfAbsent(new PrimitiveKey(texture, mode, water), ignored -> new Primitive(texture != -1));
        var opacity = opacity(metrics, mode, alpha);
        var paletteRgb = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(hsl) & 0xFFFF];

        for (var vertex : new int[] {tile.faceA()[face], tile.faceB()[face], tile.faceC()[face]}) {
            var withinX = tile.offsetX()[vertex];
            var withinZ = tile.offsetY()[vertex];
            var localX = (x - origin) * TILE + withinX;
            var localZ = (z - origin) * TILE + withinZ;
            var lift = tile.offsetLevel() == null ? 0 : tile.offsetLevel()[vertex];
            var height = ground.averageHeight((x << ground.tileSizeShift) + withinX, (z << ground.tileSizeShift) + withinZ) + lift;
            var depth = tile.depths() == null ? 0 : tile.depths()[vertex];
            var towardsWater = tile.water().depth() == 0 ? 0 : Math.clamp(depth * 255L / tile.water().depth(), 0, 255);
            var rgb = Static572.lerpRgb(tile.water().colour(), Static732.scaleRgb(paletteRgb, ClientMapSquareReader.FULL_LIGHT), towardsWater);
            var normal = normal(x, z, withinX, withinZ);
            var u = texture == -1 ? 0.0F : textureCoordinate(originX, localX, size);
            var v = texture == -1 ? 0.0F : textureCoordinate(originZ, localZ, size);
            addCorner(primitive, new Corner(localX, height, localZ, normal[0], normal[1], normal[2], rgb & 0xFFFFFF,
                hsl & 0xFFFF, depth, opacity, u, v));
        }
    }

    /**
     * The texture and size a vertex names. A texture of size 0 is drawn by neither toolkit: the
     * software one divides the vertex's place by the size and the GL one scales the texture by its
     * inverse ({@code JavaGround.renderTile}, {@code Node_Sub39}), each infinite, so the vertex
     * is written untextured, in its colour alone.
     */
    private static Surface surface(JavaGenericBlendedTile tile, int vertex) {
        var texture = tile.vertexTextures[vertex];
        var size = tile.vertexSizes[vertex];
        return texture != -1 && size == 0 ? new Surface(-1, size) : new Surface(texture, size);
    }

    /**
     * Writes one face as the rasteriser draws it.
     *
     * <p>A face whose corners agree on a texture and its size is drawn with it, or in colour alone
     * where the toolkit holds no such texture. A face whose corners disagree is drawn by the
     * rasteriser weighting each corner's texture by how near the corner each pixel is, unless one
     * of them is a texture the toolkit does not hold, and then in colour alone.
     *
     * <p>glTF has no way to weight textures across a face, so such a face is written as layers:
     * the first corner's texture, and over it each other texture, blended in by an alpha that is 1
     * at the corners that name it and 0 at the others. Where two textures meet on a face, that
     * comes to exactly the rasteriser's weighting. Where three do, the last layer also covers part
     * of the second, which is close but not exact.
     */
    private void addFace(int x, int z, JavaGenericBlendedTile tile, int a) {
        var corners = List.of(surface(tile, a), surface(tile, a + 1), surface(tile, a + 2));
        if (hasSizelessTexture(tile, a)) {
            approximated.merge("a corner names a texture of size 0, drawn untextured there", 1, Integer::sum);
        }
        var first = corners.getFirst();
        var alpha = tile.vertexColours[a] >>> 24;
        var distinct = corners.stream().distinct().toList();

        if (distinct.size() == 1) {
            var texture = drawable(first.texture()) ? first.texture() : -1;
            addLayer(x, z, tile, a, texture, first.size(), alphaMode(metrics(texture), alpha), ALL_CORNERS, alpha);
        } else if (!corners.stream().allMatch(surface -> drawable(surface.texture()))) {
            addLayer(x, z, tile, a, -1, first.size(), GltfMaterials.AlphaMode.OPAQUE, ALL_CORNERS, alpha);
        } else if (!layerable(distinct, alpha)) {
            approximated.merge("corners name textures that blend by their own alpha, drawn with the first corner's",
                1, Integer::sum);
            addLayer(x, z, tile, a, first.texture(), first.size(), alphaMode(metrics(first.texture()), alpha),
                ALL_CORNERS, alpha);
        } else {
            addLayer(x, z, tile, a, first.texture(), first.size(), GltfMaterials.AlphaMode.OPAQUE, ALL_CORNERS, alpha);
            for (var overlay : distinct.subList(1, distinct.size())) {
                var weights = new float[corners.size()];
                for (var corner = 0; corner < weights.length; corner++) {
                    weights[corner] = corners.get(corner).equals(overlay) ? 1.0F : 0.0F;
                }
                addLayer(x, z, tile, a, overlay.texture(), overlay.size(), GltfMaterials.AlphaMode.BLEND, weights,
                    alpha);
            }
            layered.merge(distinct.size(), 1, Integer::sum);
        }
    }

    /**
     * Whether a face of several textures can be written as layers, which needs every one of them
     * to be opaque, as every texture of the ground is but water's.
     */
    private boolean layerable(List<Surface> surfaces, int alpha) {
        return alpha == OPAQUE_ALPHA
            && surfaces.stream().allMatch(surface -> metrics(surface.texture()).alphaBlendMode == 0);
    }

    private boolean drawable(int texture) {
        return texture != -1 && materials.source().textureAvailable(texture);
    }

    private TextureMetrics metrics(int texture) {
        return texture == -1 ? null : materials.source().getMetrics(texture);
    }

    /**
     * Writes one face with one texture, or with none where it is -1.
     *
     * @param weights how much of the texture each corner shows, which is the corner's alpha where
     *     the face is a layer over another of the same face.
     * @param alpha the alpha of the face's first vertex, which the toolkit lowers on water.
     */
    private void addLayer(int x, int z, JavaGenericBlendedTile tile, int a, int texture, int size,
                          GltfMaterials.AlphaMode mode, float[] weights, int alpha) {
        var water = underwater ? colours.water(x, z) : RecordingGround.Water.NONE;
        var key = new PrimitiveKey(texture, mode, water);
        var primitive = primitives.computeIfAbsent(key, ignored -> new Primitive(texture != -1));
        var opacity = opacity(metrics(texture), mode, alpha);
        var depths = colours.waterDepths(x, z);
        var hsls = colours.hsl(x, z);
        if (hsls == null || hsls.length != tile.vertexCount) {
            throw new IllegalStateException("Tile " + x + "," + z + " has no HSL colour for each of its " + tile.vertexCount
                + " vertices.");
        }

        for (var corner = 0; corner < weights.length; corner++) {
            var vertex = a + corner;
            var localX = (x - origin) * TILE + tile.verticesX[vertex];
            var localZ = (z - origin) * TILE + tile.verticesZ[vertex];
            var normal = normal(x, z, tile.verticesX[vertex], tile.verticesZ[vertex]);
            var u = texture == -1 ? 0.0F : textureCoordinate(originX, localX, size);
            var v = texture == -1 ? 0.0F : textureCoordinate(originZ, localZ, size);

            var depth = depths == null ? 0 : depths[vertex];
            var described = new Corner(localX, tile.verticesY[vertex], localZ, normal[0], normal[1], normal[2],
                tile.vertexColours[vertex] & 0xFFFFFF, hsls[vertex] & 0xFFFF, depth, opacity * weights[corner], u, v);
            addCorner(primitive, described);
        }
    }

    /**
     * Adds a corner to a primitive, sharing the vertex of a corner already written that agrees
     * with it on everything.
     */
    private static void addCorner(Primitive primitive, Corner corner) {
        var known = primitive.numbers.get(corner);
        if (known != null) {
            primitive.indices.add(known);
        } else {
            primitive.numbers.put(corner, primitive.numbers.size());
            primitive.indices.add(primitive.numbers.size() - 1);
            primitive.add(corner);
        }
    }

    /**
     * How the rasteriser lays a face over what is behind it. A texture that blends or cuts out by
     * its own alpha decides, and otherwise a textured face is blended by the alpha of its first
     * vertex, which the toolkit lowers on water.
     */
    private static GltfMaterials.AlphaMode alphaMode(TextureMetrics metrics, int alpha) {
        if (metrics != null && metrics.alphaBlendMode == GltfMaterials.ALPHA_BLENDED) {
            return GltfMaterials.AlphaMode.BLEND;
        } else if (metrics != null && metrics.alphaBlendMode == GltfMaterials.ALPHA_CUTOUT) {
            return GltfMaterials.AlphaMode.MASK;
        } else if (metrics != null && alpha != OPAQUE_ALPHA) {
            return GltfMaterials.AlphaMode.BLEND;
        } else {
            return GltfMaterials.AlphaMode.OPAQUE;
        }
    }

    /**
     * How opaque a face's vertices make it. Only a face blended by the alpha the toolkit gives its
     * vertices, which it does on water, is less than opaque: a texture that blends or cuts out by
     * its own alpha takes the place of the vertices'.
     */
    private static float opacity(TextureMetrics metrics, GltfMaterials.AlphaMode mode, int alpha) {
        if (mode == GltfMaterials.AlphaMode.BLEND && metrics != null
            && metrics.alphaBlendMode != GltfMaterials.ALPHA_BLENDED && alpha != OPAQUE_ALPHA) {
            return alpha / 255.0F;
        } else {
            return 1.0F;
        }
    }

    /**
     * A texture coordinate along one axis: where the vertex is in the world over the texture's
     * size, measured from the last multiple of that size before the map square starts.
     */
    private static float textureCoordinate(int squareStart, int local, int size) {
        var start = (long) squareStart * TILE;
        var from = Math.floorDiv(start, size) * (long) size;
        return (float) (start - from + local) / size;
    }

    /**
     * The normal at a point within a tile, in the client's frame, blended from the normals of the
     * tile's four corners as the client blends their light.
     */
    private float[] normal(int x, int z, int withinX, int withinZ) {
        var sw = cornerNormal(x, z);
        var se = cornerNormal(x + 1, z);
        var nw = cornerNormal(x, z + 1);
        var ne = cornerNormal(x + 1, z + 1);
        var east = withinX / (float) TILE;
        var north = withinZ / (float) TILE;

        var blended = new float[3];
        for (var axis = 0; axis < blended.length; axis++) {
            var south = sw[axis] * (1 - east) + se[axis] * east;
            var northern = nw[axis] * (1 - east) + ne[axis] * east;
            blended[axis] = south * (1 - north) + northern * north;
        }
        return unit(blended);
    }

    /**
     * The normal at a corner from the slope between the corners either side of it, as
     * {@code JavaGround} works out the light that falls on it. y is down, so the normal of level
     * ground points along -y.
     */
    private float[] cornerNormal(int x, int z) {
        var heights = ground.tileHeights;
        var west = heights[Math.max(x - 1, 0)][z];
        var east = heights[Math.min(x + 1, heights.length - 1)][z];
        var south = heights[x][Math.max(z - 1, 0)];
        var north = heights[x][Math.min(z + 1, heights[x].length - 1)];
        return unit(new float[] {east - west, -2.0F * TILE, north - south});
    }

    private static float[] unit(float[] vector) {
        var length = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        return new float[] {vector[0] / length, vector[1] / length, vector[2] / length};
    }

    private void writePrimitive(PrimitiveKey key, Primitive primitive) {
        var attributes = new LinkedHashMap<String, Integer>();
        attributes.put("POSITION", gltf.attribute(primitive.positions.toArray(), 3, "VEC3", true));
        attributes.put("NORMAL", gltf.attribute(primitive.normals.toArray(), 3, "VEC3", false));
        attributes.put("COLOR_0", gltf.attribute(primitive.colours.toArray(), 4, "VEC4", false));
        attributes.put("_HSL", gltf.wholeAttribute(primitive.hsls.stream().mapToInt(Integer::intValue).toArray(), 4, "VEC4"));
        attributes.put("_WATER", gltf.attribute(primitive.waterDepths.toArray(), 1, "SCALAR", false));
        if (primitive.textured) {
            attributes.put("TEXCOORD_0", gltf.attribute(primitive.uvs.toArray(), 2, "VEC2", false));
        }

        var indices = primitive.indices.stream().mapToInt(Integer::intValue).toArray();
        gltf.primitive(attributes, gltf.indices(indices, primitive.numbers.size()),
            materials.material(key.texture(), key.mode(), key.water()), List.of());
    }

    private record PrimitiveKey(int texture, GltfMaterials.AlphaMode mode, RecordingGround.Water water) {
    }

    /**
     * What a corner of a face is made of: a texture, and the size the floor type gives it.
     */
    private static boolean hasSizelessTexture(JavaGenericBlendedTile tile, int a) {
        return IntStream.range(a, a + 3).anyMatch(vertex -> tile.vertexTextures[vertex] != -1 && tile.vertexSizes[vertex] == 0);
    }

    private record Surface(int texture, int size) {
    }

    /**
     * Everything one corner of a face gives its vertex, in the client's units and frame. Two
     * corners that agree on all of it share a vertex, which is what joins the tiles into one
     * surface.
     */
    private record Corner(int x, int y, int z, float normalX, float normalY, float normalZ, int rgb, int hsl,
                          int waterDepth, float opacity, float u, float v) {
    }

    private static final class Primitive {

        private final boolean textured;
        private final FloatList positions = new FloatList();
        private final FloatList normals = new FloatList();
        private final FloatList colours = new FloatList();
        private final FloatList uvs = new FloatList();
        private final List<Integer> hsls = new ArrayList<>();
        private final FloatList waterDepths = new FloatList();
        private final Map<Corner, Integer> numbers = new HashMap<>();
        private final List<Integer> indices = new ArrayList<>();

        private Primitive(boolean textured) {
            this.textured = textured;
        }

        /**
         * Writes one vertex, turned half a turn about x as a model's are.
         */
        private void add(Corner corner) {
            positions.add(corner.x() / UNITS_PER_METRE);
            positions.add(-corner.y() / UNITS_PER_METRE);
            positions.add(-corner.z() / UNITS_PER_METRE);

            normals.add(corner.normalX());
            normals.add(-corner.normalY());
            normals.add(-corner.normalZ());

            hsls.add(corner.hsl() >> 10 & 0x3F);
            hsls.add(corner.hsl() >> 7 & 0x7);
            hsls.add(corner.hsl() & 0x7F);
            hsls.add(0);
            waterDepths.add(corner.waterDepth());

            colours.add(Srgb.toLinear(corner.rgb() >> 16 & 0xFF));
            colours.add(Srgb.toLinear(corner.rgb() >> 8 & 0xFF));
            colours.add(Srgb.toLinear(corner.rgb() & 0xFF));
            colours.add(corner.opacity());

            if (textured) {
                uvs.add(corner.u());
                uvs.add(corner.v());
            }
        }
    }
}
