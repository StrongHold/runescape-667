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
 * colour as the palette gives it, its alpha out of 255, and its group, by which the frames of a
 * sequence scale, turn and move it (transforms 10, 9 and 8, {@code Mesh.getBillboardGroups}), -1
 * for none. These are what the GL toolkit draws with ({@code GlModel.renderBillboards}); the face
 * itself is left out of the mesh where the type hides it.
 */
public final class BillboardSources {

    private static final int WHOLE_ALPHA = 255;

    private static final int NO_GROUP = -1;

    public static List<Map<String, Object>> billboards(JavaModel model) {
        var written = new ArrayList<Map<String, Object>>();
        for (var index = 0; index < model.billboardFaces.length; index++) {
            var billboard = model.billboardFaces[index];
            var face = billboard.face;
            var a = model.faceA[face];
            var b = model.faceB[face];
            var c = model.faceC[face];
            var entry = new LinkedHashMap<String, Object>();
            entry.put("centre", List.of(
                middle(model.vertexX[a], model.vertexX[b], model.vertexX[c]),
                middle(model.vertexY[a], model.vertexY[b], model.vertexY[c]),
                middle(model.vertexZ[a], model.vertexZ[b], model.vertexZ[c])));
            entry.put("distance", billboard.distance);
            entry.put("halfWidth", (int) billboard.width);
            entry.put("halfHeight", (int) billboard.height);
            entry.put("texture", (int) billboard.texture);
            entry.put("blendMode", (int) billboard.blendMode);
            entry.put("colour", ColourUtils.HSL_TO_RGB[model.faceColour[face] & 0xFFFF] & 0xFFFFFF);
            entry.put("alpha", WHOLE_ALPHA - (model.faceAlpha == null ? 0 : model.faceAlpha[face] & 0xFF));
            entry.put("group", groupOf(model, index));
            written.add(entry);
        }
        return written;
    }

    /**
     * The group of a billboard, from the billboards of each group the model keeps where it was
     * built with their labels, or none.
     */
    private static int groupOf(JavaModel model, int billboard) {
        if (model.billboardLabels == null) {
            return NO_GROUP;
        }
        for (var group = 0; group < model.billboardLabels.length; group++) {
            for (var member : model.billboardLabels[group]) {
                if (member == billboard) {
                    return group;
                }
            }
        }
        return NO_GROUP;
    }

    /** The middle of three corners, as the GL toolkit averages them in floats. */
    private static float middle(int a, int b, int c) {
        return (a + b + c) * 0.3333333F;
    }

    private BillboardSources() {
        /* empty */
    }
}
