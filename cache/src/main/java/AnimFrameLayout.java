import java.util.LinkedHashMap;

/**
 * Walks an animation frame's bytes the way {@code AnimFrame} reads them, keeping only where each
 * section starts and how far it is read.
 *
 * A frame starts with a byte the client skips, the number of the base it moves and how many of
 * that base's transforms it gives flags for. One byte of flags for each of those follows, and then
 * the values, which have no size of their own: a transform with any flags set has a value for each
 * of its three axes that its low three flags name, and none for the rest. The frame's own base is
 * not needed to find where it ends.
 */
public final class AnimFrameLayout {

    private static final int HEADER = 4;

    public static Layout.Walk walk(byte[] data) {
        var layout = new Layout(data);
        var counts = new LinkedHashMap<String, Integer>();

        try {
            var header = layout.section("header", HEADER);
            counts.put("skipped byte", header.g1());
            counts.put("base", header.g2());
            var groupCount = header.g1();
            counts.put("groups", groupCount);

            var flags = layout.section("transform flags", groupCount);
            var values = layout.rest("transform values");

            var transforms = 0;
            for (var group = 0; group < groupCount; group++) {
                var mask = flags.g1();
                if (mask > 0) {
                    transforms++;
                    for (var axis = 0; axis < 3; axis++) {
                        if ((mask & 1 << axis) != 0) {
                            values.gsmarts();
                        }
                    }
                }
            }
            counts.put("transforms", transforms);
        } catch (Layout.Overrun overrun) {
            return new Layout.Walk(counts, overrun.mismatch());
        }

        return new Layout.Walk(counts, layout.check().orElse(null));
    }

    private AnimFrameLayout() {
        /* empty */
    }
}
