/**
 * One image of a sprite, as the client keeps it after reading it from the cache.
 *
 * The client stores only the rectangle of a frame that it draws, and remembers where that
 * rectangle sits on a larger canvas: {@code x} and {@code y} are its left and top margins, and the
 * canvas is {@code canvasWidth} by {@code canvasHeight}. The client draws a frame at a point by
 * putting the canvas's top left corner there, so the rectangle lands at the point plus its margins.
 *
 * The pixels are the rectangle's, a row at a time from the top, as straight (not premultiplied)
 * ARGB.
 */
public record SpriteFrame(int x, int y, int width, int height, int canvasWidth, int canvasHeight, boolean alpha, int[] pixels) {

    /** Whether the client's rectangle is smaller than its canvas, so that the frame has margins. */
    public boolean trimmed() {
        return x != 0 || y != 0 || width != canvasWidth || height != canvasHeight;
    }
}
