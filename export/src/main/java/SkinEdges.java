import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * How far in from its side each edge of a frame is seen: where it stands, and then to the inner
 * side of the part of its sprite the sprite holds pixels for, the canvas less the offsets the cache
 * keeps with it ({@code IndexedImage.offX1}, {@code offX2}, {@code offY1}, {@code offY2}), mirrored,
 * flipped or turned as it is drawn and scaled to how thick it is drawn, or for a tiled edge repeated
 * at its own size and cut to how thick it is drawn. What a frame holds stands inside
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
     * Whether a sprite's canvas in the cache is of a size.
     */
    boolean isSize(int sprite, int width, int height) {
        var canvas = bounds.get(sprite);
        return canvas != null && canvas.width() == width && canvas.height() == height;
    }

    /**
     * Writes into each edge of these parts how far in from its side it is seen, as `seen`.
     */
    @SuppressWarnings("unchecked")
    void addSeen(List<?> parts) {
        for (var each : parts) {
            var part = (Map<String, Object>) each;
            var place = (String) part.get("place");
            if (List.of("top", "bottom", "left", "right").contains(place)) {
                part.put("seen", seenOf(part));
            }
        }
    }

    /**
     * How far in from its side an edge is seen, or how far in it is drawn where it is of one colour
     * or its sprite is not in the cache.
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
        var drawn = drawn(sprite, part);
        var across = place.equals("left") || place.equals("right");
        var size = across ? drawn.width() : drawn.height();
        var start = across ? drawn.left() : drawn.top();
        var length = across ? drawn.inner() : drawn.innerHeight();
        var fromStart = place.equals("left") || place.equals("top");
        var fromNearSide = fromStart ? start + length : size - start;
        var inset = (Integer) part.get("inset");
        var thickness = (Integer) part.get("thickness");
        if (Boolean.TRUE.equals(part.get("tiled"))) {
            return inset + tiledReach(start, length, size, thickness, fromStart);
        } else {
            return inset + Math.round((float) fromNearSide * thickness / size);
        }
    }

    /**
     * How far in from its side a tiled edge holds pixels: the client repeats the sprite at its own
     * size from the top left of the edge ({@code Sprite.renderTiled}) and cuts the last copy to the
     * edge, so an edge thinner than its sprite shows the sprite's first rows or columns whichever side
     * it lies on, and is not scaled as a stretched one is.
     */
    private static int tiledReach(int start, int length, int size, int thickness, boolean fromStart) {
        var nearest = thickness;
        var furthest = 0;
        for (var copy = 0; copy < thickness; copy += size) {
            var from = Math.min(copy + start, thickness);
            var to = Math.min(copy + start + length, thickness);
            if (from < to) {
                nearest = Math.min(nearest, from);
                furthest = Math.max(furthest, to);
            }
        }
        return fromStart ? furthest : thickness - nearest;
    }

    /**
     * A sprite's canvas and the part of it the cache holds pixels for as the part draws it: mirrored,
     * flipped, then turned a quarter turn anticlockwise, which takes its top side to the left.
     */
    private static Bounds drawn(Bounds sprite, Map<String, Object> part) {
        var mirrored = Boolean.TRUE.equals(part.get("mirrored"));
        var flipped = Boolean.TRUE.equals(part.get("flipped"));
        var left = mirrored ? sprite.width() - sprite.left() - sprite.inner() : sprite.left();
        var top = flipped ? sprite.height() - sprite.top() - sprite.innerHeight() : sprite.top();
        if (Boolean.TRUE.equals(part.get("turned"))) {
            return new Bounds(sprite.height(), top, sprite.innerHeight(), sprite.width(), sprite.width() - left - sprite.inner(), sprite.inner());
        } else {
            return new Bounds(sprite.width(), left, sprite.inner(), sprite.height(), top, sprite.innerHeight());
        }
    }
}
