import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The frames of one sprite packed into one image.
 *
 * Frames are laid in rows, tallest first, in a width that makes the image roughly square, with
 * one transparent pixel between neighbours so that a sampler that filters never reads one frame's
 * edge into the next. A frame of no pixels takes no room and is placed at the origin. Each frame
 * keeps the pixels the client keeps, untrimmed and unrotated.
 */
public record SpriteAtlas(int width, int height, List<Placement> placements, int[] pixels) {

    /** Where one frame of the sprite, by its index, lies in the atlas. */
    public record Placement(int x, int y) {
    }

    private static final int GAP = 1;

    public static SpriteAtlas pack(List<SpriteFrame> frames) {
        var order = IntStream.range(0, frames.size()).boxed()
            .sorted(Comparator.comparingInt((Integer i) -> -frames.get(i).height()).thenComparingInt(i -> i))
            .toList();

        var area = frames.stream().mapToLong(frame -> (long) (frame.width() + GAP) * (frame.height() + GAP)).sum();
        var widest = frames.stream().mapToInt(SpriteFrame::width).max().orElse(0);
        var limit = Math.max(widest, (int) Math.ceil(Math.sqrt(area)));

        var placed = new Placement[frames.size()];
        var x = 0;
        var y = 0;
        var row = 0;
        var width = 0;
        for (var index : order) {
            var frame = frames.get(index);
            if (frame.width() == 0 || frame.height() == 0) {
                placed[index] = new Placement(0, 0);
            } else {
                if (x > 0 && x + frame.width() > limit) {
                    x = 0;
                    y += row + GAP;
                    row = 0;
                }
                placed[index] = new Placement(x, y);
                width = Math.max(width, x + frame.width());
                row = Math.max(row, frame.height());
                x += frame.width() + GAP;
            }
        }
        var height = y + row;

        var pixels = new int[width * height];
        for (var index = 0; index < frames.size(); index++) {
            var frame = frames.get(index);
            for (var line = 0; line < frame.height(); line++) {
                System.arraycopy(frame.pixels(), line * frame.width(), pixels, (placed[index].y() + line) * width + placed[index].x(), frame.width());
            }
        }
        return new SpriteAtlas(width, height, List.of(placed), pixels);
    }
}
