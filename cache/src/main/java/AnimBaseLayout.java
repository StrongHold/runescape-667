import java.util.LinkedHashMap;

/**
 * Walks an animation base's bytes the way {@code AnimBase} reads them, keeping only where each
 * section starts and how far it is read.
 *
 * A base is its count of transforms followed by four tables of one entry per transform: the
 * type, whether it is shadowed, its origin mask, and how many labels it moves. The labels follow
 * those tables with no size of their own, so only the label counts say where the base ends.
 */
public final class AnimBaseLayout {

    public static Layout.Walk walk(byte[] data) {
        var layout = new Layout(data);
        var counts = new LinkedHashMap<String, Integer>();

        try {
            var transformCount = layout.section("transform count", 1).g1();
            counts.put("transforms", transformCount);

            var types = layout.section("transform types", transformCount);
            var shadowed = layout.section("shadow flags", transformCount);
            var masks = layout.section("origin masks", transformCount * 2);
            var labelCounts = layout.section("label counts", transformCount);
            var labels = layout.rest("labels");

            for (var transform = 0; transform < transformCount; transform++) {
                types.g1();
                shadowed.g1();
                masks.g2();
            }

            var labelTotal = 0;
            for (var transform = 0; transform < transformCount; transform++) {
                var moved = labelCounts.g1();
                for (var label = 0; label < moved; label++) {
                    labels.g1();
                }
                labelTotal += moved;
            }
            counts.put("labels", labelTotal);
        } catch (Layout.Overrun overrun) {
            return new Layout.Walk(counts, overrun.mismatch());
        }

        return new Layout.Walk(counts, layout.check().orElse(null));
    }

    private AnimBaseLayout() {
        /* empty */
    }
}
