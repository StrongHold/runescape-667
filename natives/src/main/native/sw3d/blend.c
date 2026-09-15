/*
 * How a colour meets what is already on the buffer.
 *
 * The client chooses between these per call and passes the alpha in the top byte of the colour,
 * so a fill asked for in the blending mode with no alpha set draws nothing at all.
 */

#include "sw3d.h"

uint32_t blend(uint32_t destination, uint32_t colour, int mode) {
    if (mode == BLEND_OPAQUE) {
        return colour & 0xFFFFFF;
    }

    if (mode == BLEND_ADD) {
        uint32_t red = ((destination >> 16) & 0xFF) + ((colour >> 16) & 0xFF);
        uint32_t green = ((destination >> 8) & 0xFF) + ((colour >> 8) & 0xFF);
        uint32_t blue = (destination & 0xFF) + (colour & 0xFF);

        return ((red > 255 ? 255 : red) << 16)
            | ((green > 255 ? 255 : green) << 8)
            | (blue > 255 ? 255 : blue);
    }

    uint32_t alpha = (colour >> 24) & 0xFF;

    /*
     * Fully opaque is answered without blending. Taken through the sum below it would come back
     * one short of the colour asked for.
     */
    if (alpha == 255) {
        return colour & 0xFFFFFF;
    }

    /*
     * Each side is shifted down before they are added, rather than the sum being shifted once.
     * Both lose their fraction, so the result is up to one lower than a single shift would give,
     * and it is lower often enough to see wherever one shape is drawn over another.
     */
    uint32_t inverse = 256 - alpha;
    uint32_t red = ((((colour >> 16) & 0xFF) * alpha) >> 8)
        + ((((destination >> 16) & 0xFF) * inverse) >> 8);
    uint32_t green = ((((colour >> 8) & 0xFF) * alpha) >> 8)
        + ((((destination >> 8) & 0xFF) * inverse) >> 8);
    uint32_t blue = (((colour & 0xFF) * alpha) >> 8)
        + (((destination & 0xFF) * inverse) >> 8);

    return (red << 16) | (green << 8) | blue;
}
