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
public record SpriteFrame(int x, int y, int width, int height, int canvasWidth, int canvasHeight, int[] pixels) {

    /**
     * The frame laid on its whole canvas, clear outside its rectangle, a row at a time from the
     * top: what the client draws at a point, with the canvas's top left corner there.
     */
    public int[] canvas() {
        var laid = new int[canvasWidth * canvasHeight];
        for (var line = 0; line < height; line++) {
            System.arraycopy(pixels, line * width, laid, (y + line) * canvasWidth + x, width);
        }
        return laid;
    }
}
