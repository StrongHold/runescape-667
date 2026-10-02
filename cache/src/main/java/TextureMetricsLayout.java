import java.util.LinkedHashMap;
import java.util.List;

/**
 * Walks the table of texture metrics the way {@code Js5TextureSource} reads it, keeping only
 * where each section starts and how far it is read.
 *
 * The table starts with how many textures there are and a byte for each saying whether it is
 * held. Every metric then follows as a column of its own, with one value for each held texture
 * and nothing for the rest, so the number held is the only thing that says how long each column
 * is. The last column has to end at the end of the data.
 */
public final class TextureMetricsLayout {

    private static final int HELD = 1;

    /**
     * A column of the table: its name and how many bytes each held texture has in it, in the
     * order the client reads them.
     */
    private record Column(String name, int width) {
        /* empty */
    }

    private static final List<Column> COLUMNS = List.of(
        new Column("disableable", 1),
        new Column("small", 1),
        new Column("skip faces", 1),
        new Column("brightness", 1),
        new Column("alpha", 1),
        new Column("effect type", 1),
        new Column("effect param 1", 1),
        new Column("average colour", 2),
        new Column("speed u", 1),
        new Column("speed v", 1),
        new Column("unused flag", 1),
        new Column("transposed", 1),
        new Column("mipmap", 1),
        new Column("repeats u", 1),
        new Column("repeats v", 1),
        new Column("hdr", 1),
        new Column("colour op", 1),
        new Column("effect param 2", 4),
        new Column("alpha blend mode", 1)
    );

    /**
     * The columns whose values are summed, for setting against what the client reads into each
     * texture's metrics.
     */
    private static final List<String> SUMMED = List.of("brightness", "average colour", "effect param 2",
        "alpha blend mode");

    public static Layout.Walk walk(byte[] data) {
        var layout = new Layout(data);
        var counts = new LinkedHashMap<String, Integer>();

        try {
            var textureCount = layout.section("texture count", 2).g2();
            counts.put("textures", textureCount);

            var presence = layout.section("held flags", textureCount);
            var held = 0;
            var otherFlags = 0;
            for (var texture = 0; texture < textureCount; texture++) {
                var flag = presence.g1();
                if (flag == HELD) {
                    held++;
                } else if (flag != 0) {
                    otherFlags++;
                }
            }
            counts.put("held", held);
            counts.put("held flags neither 0 nor 1", otherFlags);

            for (var column : COLUMNS) {
                var values = column == COLUMNS.getLast()
                    ? layout.rest(column.name())
                    : layout.section(column.name(), held * column.width());
                var sum = 0;
                for (var texture = 0; texture < held; texture++) {
                    sum += read(values, column.width());
                }
                if (SUMMED.contains(column.name())) {
                    counts.put(column.name() + " sum", sum);
                }
            }
        } catch (Layout.Overrun overrun) {
            return new Layout.Walk(counts, overrun.mismatch());
        }

        return new Layout.Walk(counts, layout.check().orElse(null));
    }

    /**
     * One value, unsigned whatever the client makes of it.
     */
    private static int read(Layout.Cursor values, int width) {
        return switch (width) {
            case 1 -> values.g1();
            case 2 -> values.g2();
            case 4 -> values.g4();
            default -> throw new IllegalArgumentException("No metric is " + width + " bytes wide.");
        };
    }

    private TextureMetricsLayout() {
        /* empty */
    }
}
