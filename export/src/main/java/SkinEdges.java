import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * How far in from its side each edge of a frame is seen: where it stands, and then to the inner
 * side of the part of its sprite the sprite holds pixels for, the canvas less the offsets the cache
 * keeps with it ({@code IndexedImage.offX1}, {@code offX2}, {@code offY1}, {@code offY2}), mirrored
 * or flipped as it is drawn and scaled to how thick it is drawn. What a frame holds stands inside
 * this, so the transparent margin of a sprite holds nothing back.
 */
final class SkinEdges {

    /**
     * A sprite's canvas and the part of it the cache holds pixels for, across and down.
     */
    private record Bounds(int width, int left, int inner, int height, int top, int innerHeight) {
    }

    private final Map<Integer, Bounds> bounds;

    private SkinEdges(Map<Integer, Bounds> bounds) {
        this.bounds = bounds;
    }

    static SkinEdges read(File cache) throws Exception {
        var sprites = Cache.js5(cache, Js5Archive.SPRITES);
        var bounds = new HashMap<Integer, Bounds>();
        for (var id : Cache.groupsOf(Cache.index(cache, Js5Archive.SPRITES))) {
            var images = com.jagex.IndexedImage.load(sprites, id, 0);
            if (images != null && images.length > 0) {
                var image = images[0];
                var width = image.offX1 + image.width + image.offX2;
                var height = image.offY1 + image.height + image.offY2;
                bounds.put(id, new Bounds(width, image.offX1, image.width, height, image.offY1, image.height));
            }
        }
        return new SkinEdges(bounds);
    }

    /**
     * Writes into each edge of these parts how far in from its side it is seen, as `seen`.
     */
    @SuppressWarnings("unchecked")
    void addSeen(List<?> parts) {
        for (var each : parts) {
            var part = (Map<String, Object>) each;
            var place = (String) part.get("place");
            var sprite = part.get("sprite");
            var edge = List.of("top", "bottom", "left", "right").contains(place);
            if (edge && sprite instanceof Integer id && bounds.containsKey(id)) {
                part.put("seen", seenOf(part, place, bounds.get(id)));
            }
        }
    }

    /**
     * How far in from its side an edge is seen, or how far in it is drawn where its sprite is not
     * in the cache.
     */
    int seenOf(Map<String, Object> edge) {
        var sprite = edge.get("sprite");
        if (sprite instanceof Integer id && bounds.containsKey(id)) {
            return seenOf(edge, (String) edge.get("place"), bounds.get(id));
        } else {
            return (Integer) edge.get("inset") + (Integer) edge.get("thickness");
        }
    }

    private static int seenOf(Map<String, Object> part, String place, Bounds sprite) {
        var across = place.equals("left") || place.equals("right");
        var size = across ? sprite.width() : sprite.height();
        var start = across ? sprite.left() : sprite.top();
        var length = across ? sprite.inner() : sprite.innerHeight();
        var turned = Boolean.TRUE.equals(part.get(across ? "mirrored" : "flipped"));
        var fromStart = place.equals("left") || place.equals("top");
        var fromNearSide = fromStart == turned ? size - start : start + length;
        var inset = (Integer) part.get("inset");
        var thickness = (Integer) part.get("thickness");
        return inset + Math.round((float) fromNearSide * thickness / size);
    }
}
