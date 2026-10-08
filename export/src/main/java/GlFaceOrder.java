import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * The order the GL toolkit draws a model's faces in. It sorts them once, as it builds the model
 * ({@code GlModel(GlToolkit, Mesh, int, int, int, int)}, the key it hands {@code Quicksort.sort}),
 * and draws them in that order, with depth writes on, every frame.
 *
 * <p>The key has two halves. The greater half holds, for a see-through face, its priority and a
 * flag that it is see-through, then for every face the effect type and the first effect parameter
 * of its texture. A face is see-through where it has an alpha or its texture blends or cuts out.
 * So the opaque faces come first, and then the see-through faces by priority. The lesser half holds
 * the texture number and the face's own number, and is a signed int, so a face of no texture,
 * whose number is 0xFFFF, comes before the textured faces of the same greater half. Where the
 * model's function mask asks for 0x100, the opaque faces are sorted by priority too.
 */
public final class GlFaceOrder {

    private static final int PRIORITY_SHIFT = 17;
    private static final int SEEN_THROUGH = 1 << 16;
    private static final int EFFECT_TYPE_SHIFT = 8;
    private static final int TEXTURE_SHIFT = 16;
    private static final int BYTE = 0xFF;
    private static final int SHORT = 0xFFFF;
    private static final int OPAQUE_BY_PRIORITY = 0x100;
    private static final int HALF_BITS = 32;

    private GlFaceOrder() {
        /* empty */
    }

    /**
     * The model's faces in the order the GL toolkit draws them.
     */
    public static int[] of(JavaModel model, Js5TextureSource source) {
        var keys = new long[model.faceCount];
        for (var face = 0; face < model.faceCount; face++) {
            keys[face] = key(model, source, face);
        }
        return IntStream.range(0, model.faceCount)
            .boxed()
            .sorted(Comparator.comparingLong(face -> keys[face]))
            .mapToInt(Integer::intValue)
            .toArray();
    }

    private static long key(JavaModel model, Js5TextureSource source, int face) {
        var texture = model.faceTextures == null ? -1 : model.faceTextures[face];
        var metrics = texture == -1 ? null : source.getMetrics(texture & SHORT);
        var alpha = model.faceAlpha == null ? 0 : model.faceAlpha[face];
        var seenThrough = alpha != 0 || metrics != null && metrics.alphaBlendMode != 0;
        var byPriority = seenThrough || (model.functionMask & OPAQUE_BY_PRIORITY) != 0;

        var greater = 0;
        if (byPriority && model.facePriority != null) {
            greater += model.facePriority[face] << PRIORITY_SHIFT;
        }
        if (seenThrough) {
            greater += SEEN_THROUGH;
        }
        if (metrics != null) {
            greater += (metrics.effectType & BYTE) << EFFECT_TYPE_SHIFT;
            greater += metrics.effectParam1 & BYTE;
        }
        var lesser = ((texture & SHORT) << TEXTURE_SHIFT) + (face & SHORT);
        return ((long) greater << HALF_BITS) + lesser;
    }
}
