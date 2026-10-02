import java.util.LinkedHashMap;

/**
 * Walks a map square's locations the way {@code MapRegion.loadLocations} reads them, keeping only
 * how far they are read.
 *
 * The locations are a run of kinds, each named by how far its number is past the last one, and
 * each followed by a run of the places it stands, given as how far each is past the last place
 * plus one and followed by its shape and turn. A step of 0 ends either run. Nothing declares a
 * size, so the step of 0 that ends the run of kinds has to be the last byte of the data.
 */
public final class LocationLayout {

    private static final int END = 0;

    /**
     * Where a place keeps its level: above the six bits of each of its two tile positions.
     */
    private static final int LEVEL_SHIFT = 12;

    public static Layout.Walk walk(byte[] data) {
        var layout = new Layout(data);
        var counts = new LinkedHashMap<String, Integer>();

        try {
            var locations = layout.rest("locations");
            var kinds = 0;
            var placed = 0;
            var highestLevel = 0;
            var id = -1;
            var step = locations.gExtended1or2();

            while (step != END) {
                id += step;
                kinds++;

                var coord = 0;
                var offset = locations.gsmart();
                while (offset != END) {
                    coord += offset - 1;
                    locations.g1();
                    placed++;
                    highestLevel = Math.max(highestLevel, coord >> LEVEL_SHIFT);
                    offset = locations.gsmart();
                }
                step = locations.gExtended1or2();
            }

            counts.put("kinds", kinds);
            counts.put("locations", placed);
            counts.put("highest id", id);
            counts.put("highest level", highestLevel);
            counts.put("bytes read", locations.pos());
        } catch (Layout.Overrun overrun) {
            return new Layout.Walk(counts, overrun.mismatch());
        }

        return new Layout.Walk(counts, layout.check().orElse(null));
    }

    private LocationLayout() {
        /* empty */
    }
}
