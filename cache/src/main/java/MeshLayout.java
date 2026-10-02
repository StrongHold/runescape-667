import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Walks a model's bytes the way {@code Mesh} reads them, keeping only where each section starts
 * and how far it is read, and none of what the values mean.
 *
 * A model comes in one of two formats, told apart by its last two bytes. Both keep their header at
 * the end and lay their sections out one after another from the first byte, in an order the
 * header's counts and flags decide. Some sections have a size that follows from the counts alone,
 * such as one byte of flags for each vertex. The rest are given a size in the header, and only
 * reading them says whether that size is right: how many of the vertex deltas a section holds
 * depends on the vertex flags, how many face indices depends on the face types, and how many
 * texture space bytes depends on which faces are textured.
 */
public final class MeshLayout {

    private static final int NEW_FOOTER = 23;
    private static final int OLD_FOOTER = 18;

    /**
     * What a face's priority flag is when every face has its own priority rather than all of
     * them sharing one.
     */
    private static final int PRIORITY_PER_FACE = 255;

    private static final int PLANAR = 0;
    private static final int CYLINDRICAL = 1;
    private static final int CUBE = 2;
    private static final int SPHERICAL = 3;

    /**
     * Walks one model, and answers what it holds by count and the first section that does not
     * end where the next begins.
     */
    public static Layout.Walk walk(byte[] data) {
        var layout = new Layout(data);
        var counts = new LinkedHashMap<String, Integer>();

        try {
            if (isNew(data)) {
                walkNew(layout, counts);
            } else {
                walkOld(layout, counts);
            }
        } catch (Layout.Overrun overrun) {
            return new Layout.Walk(counts, overrun.mismatch());
        }

        return new Layout.Walk(counts, layout.check().orElse(null));
    }

    /**
     * Whether a model is in the newer format, which the client tells by its last two bytes both
     * being 0xFF.
     */
    public static boolean isNew(byte[] data) {
        return data.length >= 2 && data[data.length - 1] == -1 && data[data.length - 2] == -1;
    }

    private static void walkNew(Layout layout, Map<String, Integer> counts) {
        var footer = layout.footer("footer", NEW_FOOTER);
        var vertexCount = footer.g2();
        var faceCount = footer.g2();
        var texSpaceCount = footer.g1();
        var flags = footer.g1();
        var priorityFlag = footer.g1();
        var faceAlphaFlag = footer.g1();
        var faceLabelFlag = footer.g1();
        var faceTextureFlag = footer.g1();
        var vertexLabelFlag = footer.g1();
        var lengthX = footer.g2();
        var lengthY = footer.g2();
        var lengthZ = footer.g2();
        var faceDataSize = footer.g2();
        var texSpaceSize = footer.g2();
        footer.g2();

        var hasFlatShading = (flags & 0x1) != 0;
        var hasParticles = (flags & 0x2) != 0;
        var hasBillboards = (flags & 0x4) != 0;
        var hasVersion = (flags & 0x8) != 0;

        var version = hasVersion ? layout.footer("version", 1).g1() : 12;

        counts.put("vertices", vertexCount);
        counts.put("faces", faceCount);
        counts.put("texture spaces", texSpaceCount);
        counts.put("version", version);

        var mappingTypes = layout.section("texture mapping types", texSpaceCount);
        var types = new int[texSpaceCount];
        var planar = 0;
        var complex = 0;
        var cube = 0;
        for (var space = 0; space < texSpaceCount; space++) {
            types[space] = mappingTypes.g1b();
            planar += types[space] == PLANAR ? 1 : 0;
            cube += types[space] == CUBE ? 1 : 0;
            complex += types[space] >= CYLINDRICAL && types[space] <= SPHERICAL ? 1 : 0;
        }
        counts.put("complex texture spaces", complex);

        var vertexFlags = layout.section("vertex flags", vertexCount);
        var shading = layout.section("face shading", hasFlatShading ? faceCount : 0);
        var faceTypes = layout.section("face types", faceCount);
        var priorities = layout.section("face priorities", priorityFlag == PRIORITY_PER_FACE ? faceCount : 0);
        var faceLabels = layout.section("face labels", faceLabelFlag == 1 ? faceCount : 0);
        var vertexLabels = layout.section("vertex labels", vertexLabelFlag == 1 ? vertexCount : 0);
        var alphas = layout.section("face alphas", faceAlphaFlag == 1 ? faceCount : 0);
        var indices = layout.section("face indices", faceDataSize);
        var textures = layout.section("face textures", faceTextureFlag == 1 ? faceCount * 2 : 0);
        var faceSpaces = layout.section("face texture spaces", texSpaceSize);
        var colours = layout.section("face colours", faceCount * 2);
        var x = layout.section("vertex x", lengthX);
        var y = layout.section("vertex y", lengthY);
        var z = layout.section("vertex z", lengthZ);
        var planarSpaces = layout.section("planar texture spaces", planar * 6);
        var complexSpaces = layout.section("complex texture spaces", complex * 6);
        var scales = layout.section("texture space scales", complex * scaleSize(version));
        var rotations = layout.section("texture space rotations", complex);
        var directions = layout.section("texture space directions", complex);
        var offsets = layout.section("texture space offsets", complex + cube * 2);

        walkVertices(vertexCount, vertexFlags, x, y, z, vertexLabelFlag == 1 ? vertexLabels : null);

        var hasFaceSpaces = faceTextureFlag == 1 && texSpaceCount > 0;
        for (var face = 0; face < faceCount; face++) {
            colours.g2();
            if (hasFlatShading) {
                shading.g1b();
            }
            if (priorityFlag == PRIORITY_PER_FACE) {
                priorities.g1b();
            }
            if (faceAlphaFlag == 1) {
                alphas.g1b();
            }
            if (faceLabelFlag == 1) {
                faceLabels.g1();
            }
            var untextured = true;
            if (faceTextureFlag == 1) {
                untextured = textures.g2() == 0;
            }
            if (hasFaceSpaces && !untextured) {
                faceSpaces.g1();
            }
        }

        walkIndices(faceCount, faceTypes, indices);

        for (var space = 0; space < texSpaceCount; space++) {
            var type = types[space] & 0xFF;
            if (type == PLANAR) {
                skip(planarSpaces, 6);
            } else if (type == CYLINDRICAL || type == CUBE || type == SPHERICAL) {
                skip(complexSpaces, 6);
                skip(scales, scaleSize(version));
                rotations.g1b();
                directions.g1b();
                skip(offsets, type == CUBE ? 3 : 1);
            } else {
                /* empty */
            }
        }

        var trailer = layout.rest(trailerName(hasParticles, hasBillboards));
        var emitters = 0;
        var effectors = 0;
        var billboards = 0;
        if (hasParticles) {
            emitters = trailer.g1();
            skip(trailer, emitters * 4);
            effectors = trailer.g1();
            skip(trailer, effectors * 4);
        }
        if (hasBillboards) {
            billboards = trailer.g1();
            skip(trailer, billboards * 6);
        }
        counts.put("emitters", emitters);
        counts.put("effectors", effectors);
        counts.put("billboards", billboards);
    }

    private static void walkOld(Layout layout, Map<String, Integer> counts) {
        var footer = layout.footer("footer", OLD_FOOTER);
        var vertexCount = footer.g2();
        var faceCount = footer.g2();
        var texSpaceCount = footer.g1();
        var shadingFlag = footer.g1();
        var priorityFlag = footer.g1();
        var alphaFlag = footer.g1();
        var faceLabelFlag = footer.g1();
        var vertexLabelFlag = footer.g1();
        var lengthX = footer.g2();
        var lengthY = footer.g2();
        var lengthZ = footer.g2();
        var faceDataSize = footer.g2();

        counts.put("vertices", vertexCount);
        counts.put("faces", faceCount);
        counts.put("texture spaces", texSpaceCount);
        counts.put("version", 12);
        counts.put("complex texture spaces", 0);
        counts.put("emitters", 0);
        counts.put("effectors", 0);
        counts.put("billboards", 0);

        var vertexFlags = layout.section("vertex flags", vertexCount);
        var faceTypes = layout.section("face types", faceCount);
        var priorities = layout.section("face priorities", priorityFlag == PRIORITY_PER_FACE ? faceCount : 0);
        var faceLabels = layout.section("face labels", faceLabelFlag == 1 ? faceCount : 0);
        var shading = layout.section("face shading and textures", shadingFlag == 1 ? faceCount : 0);
        var vertexLabels = layout.section("vertex labels", vertexLabelFlag == 1 ? vertexCount : 0);
        var alphas = layout.section("face alphas", alphaFlag == 1 ? faceCount : 0);
        var indices = layout.section("face indices", faceDataSize);
        var colours = layout.section("face colours", faceCount * 2);
        var spaces = layout.section("texture spaces", texSpaceCount * 6);
        var x = layout.section("vertex x", lengthX);
        var y = layout.section("vertex y", lengthY);
        var z = layout.section("vertex z", lengthZ);

        walkVertices(vertexCount, vertexFlags, x, y, z, vertexLabelFlag == 1 ? vertexLabels : null);

        for (var face = 0; face < faceCount; face++) {
            colours.g2();
            if (shadingFlag == 1) {
                shading.g1();
            }
            if (priorityFlag == PRIORITY_PER_FACE) {
                priorities.g1b();
            }
            if (alphaFlag == 1) {
                alphas.g1b();
            }
            if (faceLabelFlag == 1) {
                faceLabels.g1();
            }
        }

        walkIndices(faceCount, faceTypes, indices);

        skip(spaces, texSpaceCount * 6);
    }

    /**
     * Each vertex is held as how far it is from the one before, and its flags say which of the
     * three axes it moves along at all, so an axis section holds a delta only for those.
     */
    private static void walkVertices(int vertexCount, Layout.Cursor flags, Layout.Cursor x, Layout.Cursor y,
                                     Layout.Cursor z, Layout.Cursor labels) {
        for (var vertex = 0; vertex < vertexCount; vertex++) {
            var moves = flags.g1();
            if ((moves & 0x1) != 0) {
                x.gsmarts();
            }
            if ((moves & 0x2) != 0) {
                y.gsmarts();
            }
            if ((moves & 0x4) != 0) {
                z.gsmarts();
            }
            if (labels != null) {
                labels.g1();
            }
        }
    }

    /**
     * A face is held either as three new vertices or, sharing an edge with the face before, as
     * one, and its type says which. A type the client does not know reads nothing.
     */
    private static void walkIndices(int faceCount, Layout.Cursor types, Layout.Cursor indices) {
        for (var face = 0; face < faceCount; face++) {
            var type = types.g1();
            if (type == 1) {
                indices.gsmarts();
                indices.gsmarts();
                indices.gsmarts();
            } else if (type >= 2 && type <= 4) {
                indices.gsmarts();
            } else {
                /* empty */
            }
        }
    }

    /**
     * How many bytes the three scales of one texture space take, which grew as the format did.
     */
    private static int scaleSize(int version) {
        if (version >= 15) {
            return 9;
        } else if (version == 14) {
            return 7;
        } else {
            return 6;
        }
    }

    private static String trailerName(boolean hasParticles, boolean hasBillboards) {
        if (hasParticles && hasBillboards) {
            return "particles and billboards";
        } else if (hasParticles) {
            return "particles";
        } else if (hasBillboards) {
            return "billboards";
        } else {
            return "nothing after the texture spaces";
        }
    }

    private static void skip(Layout.Cursor cursor, int bytes) {
        for (var at = 0; at < bytes; at++) {
            cursor.g1();
        }
    }

    private MeshLayout() {
        /* empty */
    }
}
