import com.jagex.math.ColourUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Where a model draws its billboards, for a shape's {@code extras}: for each, the point it is
 * drawn at, which is the middle of its face in the client's units and frame before the file's
 * turn, how far it is pulled towards the camera, its half width and half height in the client's
 * units, its texture, how it blends (1 by its alpha, 2 added, 128 multiplied in), the face's
 * colour as the palette gives it, and its alpha out of 255. These are what the GL toolkit draws
 * with ({@code Model_Sub2.method4984}); the face itself is left out of the mesh where the type
 * hides it.
 */
public final class BillboardSources {

    private static final int WHOLE_ALPHA = 255;

    public static List<Map<String, Object>> billboards(JavaModel model) {
        var written = new ArrayList<Map<String, Object>>();
        for (var billboard : model.billboardFaces) {
            var face = billboard.anInt6139;
            var a = model.faceA[face];
            var b = model.faceB[face];
            var c = model.faceC[face];
            var entry = new LinkedHashMap<String, Object>();
            entry.put("centre", List.of(
                middle(model.vertexX[a], model.vertexX[b], model.vertexX[c]),
                middle(model.vertexY[a], model.vertexY[b], model.vertexY[c]),
                middle(model.vertexZ[a], model.vertexZ[b], model.vertexZ[c])));
            entry.put("distance", billboard.anInt6140);
            entry.put("width", (int) billboard.aShort71);
            entry.put("height", (int) billboard.aShort73);
            entry.put("texture", (int) billboard.aShort72);
            entry.put("blendMode", (int) billboard.aByte97);
            entry.put("colour", ColourUtils.HSL_TO_RGB[model.faceColour[face] & 0xFFFF] & 0xFFFFFF);
            entry.put("alpha", WHOLE_ALPHA - (model.faceAlpha == null ? 0 : model.faceAlpha[face] & 0xFF));
            written.add(entry);
        }
        return written;
    }

    /** The middle of three corners, as the GL toolkit averages them in floats. */
    private static float middle(int a, int b, int c) {
        return (a + b + c) * 0.3333333F;
    }

    private BillboardSources() {
        /* empty */
    }
}
