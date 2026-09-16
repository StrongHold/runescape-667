/*
 * Sprites, and putting them on the back buffer.
 *
 * A sprite owns a copy of its pixels rather than a view onto the array it was made from, because
 * the client is free to reuse that array the moment it returns.
 */

#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

typedef struct {
    int width;
    int height;

    /**
     * How much empty room the client cut off each side when it made the sprite.
     *
     * The client draws a sprite at the corner of the room it once took up rather than at the
     * corner of its ink, and it lays out interfaces by the size including that room. So a sprite
     * answers its own width plus what was cut off either side when it is asked how wide it is.
     */
    int fromLeft;
    int fromTop;
    int fromRight;
    int fromBottom;

    uint32_t *pixels;
} Sprite;

/** Room for a sprite's pixels, replacing whatever it held. */
static int spriteRoom(Sprite *sprite, int width, int height) {
    free(sprite->pixels);

    sprite->width = width;
    sprite->height = height;
    sprite->pixels = calloc((size_t) width * (size_t) height, sizeof(uint32_t));

    if (sprite->pixels == NULL) {
        sprite->width = 0;
        sprite->height = 0;
        return 0;
    }

    allocatedGrew((size_t) width * (size_t) height * sizeof(uint32_t));
    return 1;
}

static void spriteFree(Sprite *sprite) {
    if (sprite != NULL) {
        free(sprite->pixels);
        free(sprite);
    }
}

/**
 * How the client wants a sprite's own pixels combined with the colour it passes.
 *
 * Every one of these works a byte at a time on all four bytes of a pixel, the top one included, so
 * the colour's own alpha is combined with the sprite's alpha exactly as the other three are.
 */
enum {
    /** Multiply the two, which leaves the sprite as it is when the colour is white. */
    OP_MULTIPLY = 0,
    /** Take the sprite as it stands and ignore the colour. */
    OP_KEEP = 1,
    /** Run between the sprite and the colour, by how much alpha the colour carries. */
    OP_MIX = 2,
    /** Add the colour, holding at white. */
    OP_ADD = 3,
    /** Take the colour away, holding at black. */
    OP_SUBTRACT = 4
};

/** The four bytes of a pixel: blue, green, red and alpha, in the order they sit in it. */
enum { PARTS = 4 };

static int partOf(uint32_t pixel, int part) {
    return (int) (pixel >> (part * 8)) & 0xFF;
}

static uint32_t fromParts(const int *parts) {
    uint32_t pixel = 0;
    for (int part = 0; part < PARTS; part++) {
        pixel |= (uint32_t) (parts[part] & 0xFF) << (part * 8);
    }
    return pixel;
}

static int holdToWhite(int value) {
    return value > 0xFF ? 0xFF : value;
}

/**
 * The top sixteen bits of the product of two sixteen bit numbers, read as unsigned.
 *
 * This is one multiply of the toolkit's, and it has to be done in numbers wide enough to hold the
 * product: a colour asking for more than half alpha spreads a multiplier of nearly sixteen bits,
 * and the product of that with a part carried up eight places does not fit in a signed word.
 */
static int highProduct(int left, int right) {
    return (int) (((uint32_t) left & 0xFFFFu) * ((uint32_t) right & 0xFFFFu) >> 16);
}

/**
 * Narrows a sixteen bit number back down to one part of a pixel.
 *
 * The number is read as a **signed** one, so anything from half of sixteen bits upwards comes out
 * as nothing rather than as white. Nothing reaches that from an ordinary colour, but a colour
 * asking for more than half alpha does, and the part of the picture that should have come out
 * brightest comes out black instead. That is what the toolkit does.
 */
static int narrowToPart(int value) {
    int16_t signed16 = (int16_t) value;

    if (signed16 < 0) {
        return 0;
    } else if (signed16 > 0xFF) {
        return 0xFF;
    } else {
        return signed16;
    }
}

/**
 * How much of the sprite a mix keeps, and how much of the colour, held the way the toolkit holds
 * them.
 *
 * The toolkit takes the colour's top byte with a **signed** shift and packs two copies of it into
 * one pair of numbers. A colour asking for more than half alpha therefore spreads a number far
 * larger than a byte, every part of the answer runs past white, and the two halves of the pair
 * stop agreeing with each other. That is what it does, so it is what happens here. Which half of
 * the pair a part of a pixel uses depends on whether the part is an even one or an odd one.
 */
typedef struct {
    int keep[2];
    int lose[2];
} Mixture;

static Mixture mixtureOf(uint32_t colour) {
    int alpha = (int) colour >> 24;
    uint32_t packed = ((uint32_t) alpha << 16) | (uint32_t) alpha;
    uint32_t inverse = (packed ^ 0xFF00FFu) << 8;

    Mixture mixture;
    mixture.keep[0] = (int) (packed & 0xFFFF);
    mixture.keep[1] = (int) (packed >> 16);
    mixture.lose[0] = (int) (inverse & 0xFFFF);
    mixture.lose[1] = (int) (inverse >> 16);
    return mixture;
}

/**
 * One part of a pixel, once the sprite and the colour have been put together.
 *
 * The answer is sixteen bits wide. Everything but the mix leaves a part that already fits in a
 * byte, and the mix does not: when the answer is run into the buffer by its own alpha the whole
 * sixteen bits are what is run, and everywhere else the two halves of the mix are narrowed to
 * bytes first and then added with no room to carry.
 *
 * Each of these carries the sprite's part up eight places before it multiplies, which is why a
 * sprite multiplied by white comes back as it went in rather than one lower.
 */
static int combine(int op, int sprite, int colour, const Mixture *mixture, int part, int wide) {
    int half = part & 1;

    switch (op) {
        case OP_MULTIPLY:
            return highProduct(sprite << 8, colour);
        case OP_KEEP:
            return sprite;
        case OP_MIX: {
            int kept = highProduct(sprite << 8, mixture->keep[half]);
            int lost = highProduct(colour, mixture->lose[half]);
            return wide
                ? (kept + lost) & 0xFFFF
                : (narrowToPart(kept) + narrowToPart(lost)) & 0xFF;
        }
        case OP_ADD:
            return holdToWhite(sprite + colour);
        case OP_SUBTRACT:
            return sprite - colour < 0 ? 0 : sprite - colour;
        default:
            return sprite;
    }
}

/**
 * One part of a pixel, once the result has met what the buffer already held.
 */
static int lay(int mode, int put, int held, int alpha) {
    switch (mode) {
        case BLEND_ALPHA:
            return narrowToPart((highProduct((alpha << 8) & 0xFFFF, put)
                + highProduct(held << 8, alpha ^ 0xFF)) & 0xFFFF);
        case BLEND_ADD:
            return holdToWhite(held + put);
        default:
            return put;
    }
}

/**
 * Reads the pixels into a sprite.
 *
 * The rows of the source are `stride` apart, which is not always the sprite's own width: the
 * client cuts sprites out of larger sheets and hands over the whole sheet with an offset to the
 * corner it wants.
 */
JNIEXPORT void JNICALL Java_j_ua(JNIEnv *env, jobject self, jobject toolkit, jintArray source,
                                  jint offset, jint stride, jint width, jint height,
                                  jboolean opaque) {
    (void) toolkit;
    (void) opaque;

    if (source == NULL || width <= 0 || height <= 0) {
        return;
    }

    spriteFree((Sprite *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);

    Sprite *sprite = calloc(1, sizeof(Sprite));
    if (sprite == NULL) {
        return;
    }

    if (!spriteRoom(sprite, width, height)) {
        free(sprite);
        return;
    }

    jint *pixels = (*env)->GetPrimitiveArrayCritical(env, source, NULL);
    if (pixels != NULL) {
        for (int row = 0; row < height; row++) {
            const jint *from = pixels + offset + (jlong) row * stride;
            uint32_t *to = sprite->pixels + (size_t) row * (size_t) width;

            for (int column = 0; column < width; column++) {
                to[column] = (uint32_t) from[column];
            }
        }
        (*env)->ReleasePrimitiveArrayCritical(env, source, pixels, JNI_ABORT);
    }

    setNativeId(env, self, (jlong) (intptr_t) sprite);
}

/** One pixel of the sprite, put together with the colour and laid onto what was there. */
static uint32_t putPixel(int op, int mode, uint32_t from, uint32_t held, int colour,
                         const Mixture *mixture) {
    int put[PARTS];
    for (int part = 0; part < PARTS; part++) {
        put[part] = combine(op, partOf(from, part), partOf((uint32_t) colour, part),
                            mixture, part, mode == BLEND_ALPHA);
    }

    int alpha = put[PARTS - 1];
    int laid[PARTS];
    for (int part = 0; part < PARTS; part++) {
        laid[part] = lay(mode, put[part], partOf(held, part), alpha);
    }

    return fromParts(laid);
}

/** The size a sprite takes up including the empty room that was cut off its sides. */
static int fullWidth(const Sprite *sprite) {
    return sprite->width + sprite->fromLeft + sprite->fromRight;
}

static int fullHeight(const Sprite *sprite) {
    return sprite->height + sprite->fromTop + sprite->fromBottom;
}

/**
 * Draws a sprite at a place on the back buffer, clipped to it.
 */
static void drawSprite(const Sprite *sprite, int x, int y, int op, int colour, int mode) {
    if (sprite == NULL || raster.pixels == NULL) {
        return;
    }

    if (op < OP_MULTIPLY || op > OP_SUBTRACT || mode < BLEND_OPAQUE || mode > BLEND_ADD) {
        return;
    }

    /*
     * The client asks for the corner of the room the sprite once took up, not the corner of its
     * ink, so what was cut off the top and the left is added back here.
     */
    x += sprite->fromLeft;
    y += sprite->fromTop;

    int firstRow = y < raster.clipTop ? raster.clipTop - y : 0;
    int firstColumn = x < raster.clipLeft ? raster.clipLeft - x : 0;
    int lastRow = y + sprite->height > raster.clipBottom ? raster.clipBottom - y : sprite->height;
    int lastColumn = x + sprite->width > raster.clipRight ? raster.clipRight - x : sprite->width;

    Mixture mixture = mixtureOf((uint32_t) colour);

    for (int row = firstRow; row < lastRow; row++) {
        const uint32_t *from = sprite->pixels + (size_t) row * (size_t) sprite->width;
        uint32_t *to = raster.pixels + (size_t) (y + row) * (size_t) raster.width + x;

        for (int column = firstColumn; column < lastColumn; column++) {
            to[column] = putPixel(op, mode, from[column], to[column], colour, &mixture);
        }
    }
}

/** How many pixels of the answer are lost off the near side of the clip. */
static int lostBefore(int at, int edge) {
    return at < edge ? edge - at : 0;
}

/**
 * Draws a sprite stretched to fill a rectangle.
 *
 * Nothing is mixed between one source pixel and the next: each pixel of the answer is whichever
 * source pixel it landed on. The empty room cut off the sprite's sides is stretched by the same
 * amount as the ink, so a frame whose corners were cut away keeps its proportions.
 *
 * How far along the source a row has got is added up four pixels at a time for as long as four
 * fit, and then divided out for the few left over. Those are not the same answer once the numbers
 * stop fitting exactly, and the toolkit answers both ways in the one row.
 */
static void drawStretched(const Sprite *sprite, int x, int y, int wantedWidth, int wantedHeight,
                          int op, int colour, int mode) {
    if (sprite == NULL || raster.pixels == NULL || sprite->width <= 0 || sprite->height <= 0) {
        return;
    }

    if (op < OP_MULTIPLY || op > OP_SUBTRACT || mode < BLEND_OPAQUE || mode > BLEND_ADD) {
        return;
    }

    float acrossBy = (float) wantedWidth / (float) fullWidth(sprite);
    float downBy = (float) wantedHeight / (float) fullHeight(sprite);

    x += (int) ((float) sprite->fromLeft * acrossBy);
    y += (int) ((float) sprite->fromTop * downBy);

    int inkWidth = wantedWidth - (int) ((float) sprite->fromLeft * acrossBy)
        - (int) ((float) sprite->fromRight * acrossBy);
    int inkHeight = wantedHeight - (int) ((float) sprite->fromTop * downBy)
        - (int) ((float) sprite->fromBottom * downBy);

    float perSource = (float) inkWidth / (float) sprite->width;
    float perSourceRow = (float) inkHeight / (float) sprite->height;
    if (perSource == 0.0f || perSourceRow == 0.0f) {
        return;
    }

    int lostLeft = lostBefore(x, raster.clipLeft);
    int lostTop = lostBefore(y, raster.clipTop);
    int left = x < raster.clipLeft ? raster.clipLeft : x;
    int top = y < raster.clipTop ? raster.clipTop : y;

    int width = inkWidth - lostLeft;
    if (width > raster.clipRight - left) {
        width = raster.clipRight - left;
    }

    int height = inkHeight - lostTop;
    if (height > raster.clipBottom - top) {
        height = raster.clipBottom - top;
    }

    if (width <= 0 || height <= 0) {
        return;
    }

    int firstColumn = (int) ((float) lostLeft / perSource);
    int firstRow = (int) ((float) lostTop / perSourceRow);

    Mixture mixture = mixtureOf((uint32_t) colour);
    int blocks = width >> 2;
    float overSource = 1.0f / perSource;
    float sourceRow = 0.0f;

    for (int row = 0; row < height; row++) {
        const uint32_t *from = sprite->pixels
            + ((size_t) firstRow + (size_t) (int) sourceRow) * (size_t) sprite->width
            + (size_t) firstColumn;
        uint32_t *to = raster.pixels
            + (size_t) (top + row) * (size_t) raster.width + (size_t) left;

        float held[4] = {0.0f, overSource, overSource * 2.0f, overSource * 3.0f};

        for (int block = 0; block < blocks; block++) {
            for (int lane = 0; lane < 4; lane++) {
                int column = block * 4 + lane;
                to[column] = putPixel(op, mode, from[(int) held[lane]], to[column],
                                      colour, &mixture);
            }

            for (int lane = 0; lane < 4; lane++) {
                held[lane] += overSource * 4.0f;
            }
        }

        for (int column = blocks * 4; column < width; column++) {
            int taken = (int) ((float) column / perSource);
            to[column] = putPixel(op, mode, from[taken], to[column], colour, &mixture);
        }

        sourceRow += 1.0f / perSourceRow;
    }
}

/**
 * Draws a sprite, but only where a mask lets it through.
 *
 * Each row of the sprite is narrowed to the run the mask allows on that row of the buffer, which
 * is how the client rounds the corners off an interface.
 */
static void drawMasked(const Sprite *sprite, int x, int y, int op, int colour, int mode,
                       const void *mask, int across, int down) {
    if (sprite == NULL || raster.pixels == NULL || mask == NULL) {
        return;
    }

    if (op < OP_MULTIPLY || op > OP_SUBTRACT || mode < BLEND_OPAQUE || mode > BLEND_ADD) {
        return;
    }

    x += sprite->fromLeft;
    y += sprite->fromTop;

    Mixture mixture = mixtureOf((uint32_t) colour);

    for (int row = 0; row < sprite->height; row++) {
        int at = y + row;
        if (at < raster.clipTop || at >= raster.clipBottom) {
            continue;
        }

        int from = 0;
        int count = 0;
        if (!maskRun(mask, at, across, down, &from, &count)) {
            continue;
        }

        int left = from > x ? from : x;
        int right = from + count;
        if (right > x + sprite->width) {
            right = x + sprite->width;
        }

        const uint32_t *ink = sprite->pixels + (size_t) row * (size_t) sprite->width;
        uint32_t *into = raster.pixels + (size_t) at * (size_t) raster.width;

        for (int column = left; column < right; column++) {
            into[column] = putPixel(op, mode, ink[column - x], into[column], colour, &mixture);
        }
    }
}

/*
 * Drawing a sprite into a parallelogram.
 *
 * The client turns a sprite by working out where its three corners land and handing them over. It
 * is how the compass turns, how the minimap turns under it, and how any interface picture with an
 * angle on it is drawn.
 *
 * Walking the answer rather than the sprite is what makes a turned sprite leave no holes: every
 * pixel of the answer asks which pixel of the sprite it came from, in sixteen bit fractions of
 * one, and the two run along the row and down the column by a fixed step each.
 */

/** How many fraction bits are kept in the place a pixel is read from. */
enum { PLACE_BITS = 12 };

/** One whole pixel of the sprite, in the units the place is kept in. */
static const float WHOLE_PLACE = 4096.0f;

/**
 * What a corner is measured in while the first place is worked out, which is sixteenths of a
 * pixel. Half of one is added so that the place lands in the middle of the pixel rather than at
 * its corner.
 */
static const float SIXTEENTHS = 16.0f;
static const float HALF_A_PIXEL = 8.0f;

/** A whole number multiply that is allowed to run over, because the toolkit's does. */
static int overrunning(int left, int right) {
    return (int) ((uint32_t) left * (uint32_t) right);
}

/** The corners of the parallelogram, in the order the client hands them over. */
typedef struct {
    float acrossFirst;
    float acrossSecond;
    float acrossThird;
    float downFirst;
    float downSecond;
    float downThird;
} Corners;

/**
 * Moves the corners in by the empty room the client cut off the sprite's sides.
 *
 * The corners describe where the sprite would be if it still had that room, so the ink inside
 * them starts a little way in from each edge.
 */
static void insetByMargins(const Sprite *sprite, Corners *corner) {
    float acrossStep = (corner->acrossSecond - corner->acrossFirst) / (float) fullWidth(sprite);
    float downStep = (corner->downSecond - corner->downFirst) / (float) fullWidth(sprite);
    float acrossDrop = (corner->acrossThird - corner->acrossFirst) / (float) fullHeight(sprite);
    float downDrop = (corner->downThird - corner->downFirst) / (float) fullHeight(sprite);

    float fromTop = (float) sprite->fromTop;
    float fromLeft = (float) sprite->fromLeft;
    float fromRight = (float) sprite->fromRight;
    float fromBottom = (float) sprite->fromBottom;

    corner->acrossFirst += acrossStep * fromLeft + acrossDrop * fromTop;
    corner->downFirst += downStep * fromLeft + downDrop * fromTop;
    corner->acrossSecond += acrossDrop * fromTop - acrossStep * fromRight;
    corner->downSecond += downDrop * fromTop - downStep * fromRight;
    corner->acrossThird += acrossStep * fromLeft - acrossDrop * fromBottom;
    corner->downThird += downStep * fromLeft - downDrop * fromBottom;
}

static float leastOf(float first, float second, float third, float fourth) {
    float pair = first < second ? first : second;
    return fminf(fourth, fminf(third, pair));
}

static float mostOf(float first, float second, float third, float fourth) {
    float pair = first < second ? second : first;
    return fmaxf(fourth, fmaxf(third, pair));
}

/**
 * Where in the sprite the answer's pixels come from, and how that place moves.
 *
 * The four steps are the inverse of the parallelogram, so the whole of it can be walked by
 * adding rather than by working anything out per pixel.
 */
typedef struct {
    int place[2];
    int alongRow[2];
    int downColumn[2];
} Walk;

static Walk walkOf(const Sprite *sprite, const Corners *corner, float left, float top) {
    float acrossSpan = corner->acrossSecond - corner->acrossFirst;
    float downSpan = corner->downSecond - corner->downFirst;
    float acrossDrop = corner->acrossThird - corner->acrossFirst;
    float downDrop = corner->downThird - corner->downFirst;

    float area = acrossSpan * downDrop - downSpan * acrossDrop;
    float against = downSpan * acrossDrop - acrossSpan * downDrop;

    float wide = (float) sprite->width;
    float tall = (float) sprite->height;

    Walk walk;
    walk.alongRow[0] = (int) (downDrop * WHOLE_PLACE * wide / area);
    walk.alongRow[1] = (int) (downSpan * WHOLE_PLACE * tall / against);
    walk.downColumn[0] = (int) (acrossDrop * WHOLE_PLACE * wide / against);
    walk.downColumn[1] = (int) (acrossSpan * WHOLE_PLACE * tall / area);

    /*
     * Where the middle of the first pixel sits, counted from the middle of the parallelogram in
     * sixteenths of a pixel. The eight is half a pixel, which puts it at the middle rather than
     * at the corner.
     */
    float fourthAcross = acrossSpan + corner->acrossThird;
    float fourthDown = downDrop + corner->downSecond;
    int fromMiddleAcross = (int) (left * SIXTEENTHS + HALF_A_PIXEL
        + (corner->acrossFirst + corner->acrossSecond + corner->acrossThird + fourthAcross)
            * 0.25f * -SIXTEENTHS);
    int fromMiddleDown = (int) (top * SIXTEENTHS + HALF_A_PIXEL
        + (corner->downFirst + corner->downSecond + corner->downThird + fourthDown)
            * 0.25f * -SIXTEENTHS);

    walk.place[0] = (overrunning(walk.downColumn[0], fromMiddleDown) >> 4)
        + ((sprite->width >> 1) << PLACE_BITS)
        + (overrunning(walk.alongRow[0], fromMiddleAcross) >> 4);
    walk.place[1] = (overrunning(walk.downColumn[1], fromMiddleDown) >> 4)
        + ((sprite->height >> 1) << PLACE_BITS)
        + (overrunning(walk.alongRow[1], fromMiddleAcross) >> 4);

    return walk;
}

/**
 * Draws a sprite into the parallelogram the client's three corners describe, clipped to the
 * buffer and, when the client gives one, to a shape.
 */
static void drawParallelogram(const Sprite *sprite, Corners corner, int op, int colour, int mode,
                              const void *mask, int across, int down) {
    if (sprite == NULL || raster.pixels == NULL) {
        return;
    }

    if (op < OP_MULTIPLY || op > OP_SUBTRACT || mode < BLEND_OPAQUE || mode > BLEND_ADD) {
        return;
    }

    if (sprite->width != fullWidth(sprite) || sprite->height != fullHeight(sprite)) {
        insetByMargins(sprite, &corner);
    }

    float fourthAcross = corner.acrossSecond - corner.acrossFirst + corner.acrossThird;
    float fourthDown = corner.downThird - corner.downFirst + corner.downSecond;

    float left = fmaxf((float) raster.clipLeft, leastOf(corner.acrossFirst, corner.acrossSecond,
        corner.acrossThird, fourthAcross));
    float right = fminf((float) raster.clipRight, mostOf(corner.acrossFirst, corner.acrossSecond,
        corner.acrossThird, fourthAcross));
    float top = fmaxf((float) raster.clipTop, leastOf(corner.downFirst, corner.downSecond,
        corner.downThird, fourthDown));
    float bottom = fminf((float) raster.clipBottom, mostOf(corner.downFirst, corner.downSecond,
        corner.downThird, fourthDown));

    if (!(left - right < 0.0f) || !(top - bottom < 0.0f)) {
        return;
    }

    int columns = -(int) (left - right);
    int rows = -(int) (top - bottom);
    int start = (int) ((float) ((int) top * raster.width) + left);

    Walk walk = walkOf(sprite, &corner, left, top);

    int highest = sprite->width << PLACE_BITS;
    int lowest = sprite->height << PLACE_BITS;
    Mixture mixture = mixtureOf((uint32_t) colour);

    int rowPlace[2] = {walk.place[0], walk.place[1]};
    int at = start;

    for (int row = 0; row < rows; row++) {
        int firstColumn = 0;
        int lastColumn = columns;

        if (mask != NULL) {
            int from = 0;
            int count = 0;
            int buffer = (int) top + row;

            if (!maskRun(mask, buffer, across, down, &from, &count)) {
                firstColumn = lastColumn;
            } else {
                int leftEdge = from - (int) left;
                int rightEdge = leftEdge + count;
                firstColumn = leftEdge > 0 ? leftEdge : 0;
                lastColumn = rightEdge < columns ? rightEdge : columns;
            }
        }

        int place[2] = {
            rowPlace[0] + overrunning(walk.alongRow[0], firstColumn),
            rowPlace[1] + overrunning(walk.alongRow[1], firstColumn)
        };
        uint32_t *into = raster.pixels + at;

        for (int column = firstColumn; column < lastColumn; column++) {
            if (place[0] >= 0 && place[0] < highest && place[1] >= 0 && place[1] < lowest) {
                uint32_t from = sprite->pixels[(size_t) (place[1] >> PLACE_BITS)
                    * (size_t) sprite->width + (size_t) (place[0] >> PLACE_BITS)];
                into[column] = putPixel(op, mode, from, into[column], colour, &mixture);
            }

            place[0] += walk.alongRow[0];
            place[1] += walk.alongRow[1];
        }

        rowPlace[0] += walk.downColumn[0];
        rowPlace[1] += walk.downColumn[1];
        at += raster.width;
    }
}

/**
 * Draws a sprite turned, which the client asks for by where the corners land rather than by an
 * angle.
 */
JNIEXPORT void JNICALL Java_j_b(JNIEnv *env, jobject self, jlong handle,
                                 jfloat acrossFirst, jfloat downFirst,
                                 jfloat acrossSecond, jfloat downSecond,
                                 jfloat acrossThird, jfloat downThird,
                                 jint op, jint colour, jint mode, jint unused) {
    (void) env;
    (void) self;
    (void) unused;

    Corners corner = {acrossFirst, acrossSecond, acrossThird, downFirst, downSecond, downThird};
    drawParallelogram((const Sprite *) (intptr_t) handle, corner, op, colour, mode, NULL, 0, 0);
}

/**
 * Draws a sprite turned and cut to a shape, which is how the minimap turns inside its round
 * window.
 */
JNIEXPORT void JNICALL Java_j_UA(JNIEnv *env, jobject self, jlong handle,
                                  jfloat acrossFirst, jfloat downFirst,
                                  jfloat acrossSecond, jfloat downSecond,
                                  jfloat acrossThird, jfloat downThird,
                                  jint op, jlong mask, jint across, jint down) {
    (void) env;
    (void) self;

    Corners corner = {acrossFirst, acrossSecond, acrossThird, downFirst, downSecond, downThird};
    drawParallelogram((const Sprite *) (intptr_t) handle, corner, op, 0, BLEND_ALPHA,
        (const void *) (intptr_t) mask, across, down);
}

JNIEXPORT void JNICALL Java_j_W(JNIEnv *env, jobject self, jlong handle, jint x, jint y,
                                 jint op, jint colour, jint mode) {
    (void) env;
    (void) self;

    drawSprite((const Sprite *) (intptr_t) handle, x, y, op, colour, mode);
}

/**
 * Draws a sprite as it is, where a mask lets it through.
 */
JNIEXPORT void JNICALL Java_j_V(JNIEnv *env, jobject self, jlong handle, jint x, jint y,
                                 jlong mask, jint across, jint down) {
    (void) env;
    (void) self;

    drawMasked((const Sprite *) (intptr_t) handle, x, y, OP_KEEP, 0, BLEND_ALPHA,
               (const void *) (intptr_t) mask, across, down);
}

/**
 * Draws a sprite stretched to fill a rectangle. The last number the client passes says whether the
 * stretch should be smoothed, and this toolkit never smooths it.
 */
JNIEXPORT void JNICALL Java_j_RA(JNIEnv *env, jobject self, jlong handle, jint x, jint y,
                                  jint width, jint height, jint op, jint colour, jint mode,
                                  jint smooth) {
    (void) env;
    (void) self;
    (void) smooth;

    drawStretched((const Sprite *) (intptr_t) handle, x, y, width, height, op, colour, mode);
}

/**
 * Fills a rectangle with copies of the sprite, side by side and row below row.
 *
 * The copies run from the corner the client asked for, so a copy that would overhang the far side
 * is drawn and cut off there rather than being left out. Only the far side and the bottom are cut:
 * the near side and the top are wherever the clip already was, so a rectangle that starts left of
 * the clip still draws its copies from where it says it starts.
 */
JNIEXPORT void JNICALL Java_j_P(JNIEnv *env, jobject self, jlong handle, jint x, jint y,
                                 jint width, jint height, jint op, jint colour, jint mode) {
    (void) env;
    (void) self;

    const Sprite *sprite = (const Sprite *) (intptr_t) handle;
    if (sprite == NULL) {
        return;
    }

    int across = fullWidth(sprite);
    int down = fullHeight(sprite);
    if (across <= 0 || down <= 0) {
        return;
    }

    int wasRight = raster.clipRight;
    int wasBottom = raster.clipBottom;

    if (raster.clipRight > x + width) {
        raster.clipRight = x + width;
    }
    if (raster.clipBottom > y + height) {
        raster.clipBottom = y + height;
    }

    int columns = (width - 1 + across) / across;
    int rows = (height - 1 + down) / down;

    for (int row = 0; row < rows; row++) {
        for (int column = 0; column < columns; column++) {
            drawSprite(sprite, x + column * across, y + row * down, op, colour, mode);
        }
    }

    raster.clipRight = wasRight;
    raster.clipBottom = wasBottom;
}

JNIEXPORT void JNICALL Java_j_R(JNIEnv *env, jobject self, jlong handle, jboolean immediate) {
    (void) self;
    (void) immediate;

    spriteFree((Sprite *) (intptr_t) handle);
}

JNIEXPORT jint JNICALL Java_j_M(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->width;
}

JNIEXPORT jint JNICALL Java_j_I(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->height;
}

/**
 * How much empty room the client cut off each side, in the order left, top, right, bottom.
 */
JNIEXPORT void JNICALL Java_j_A(JNIEnv *env, jobject self, jlong handle, jint left, jint top,
                                 jint right, jint bottom) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    if (sprite == NULL) {
        return;
    }

    sprite->fromLeft = left;
    sprite->fromTop = top;
    sprite->fromRight = right;
    sprite->fromBottom = bottom;
}

JNIEXPORT void JNICALL Java_j_CA(JNIEnv *env, jobject self, jlong handle, jintArray destination) {
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    if (sprite == NULL || destination == NULL) {
        return;
    }

    jint written[4] = {
        sprite->fromLeft,
        sprite->fromTop,
        sprite->fromRight,
        sprite->fromBottom
    };

    (*env)->SetIntArrayRegion(env, destination, 0, 4, written);
}

/** How wide the sprite was before the empty room either side of it was cut off. */
JNIEXPORT jint JNICALL Java_j_wa(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->width + sprite->fromLeft + sprite->fromRight;
}

JNIEXPORT jint JNICALL Java_j_JA(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->height + sprite->fromTop + sprite->fromBottom;
}

/**
 * An empty sprite of a given size, for the client to draw into.
 */
JNIEXPORT void JNICALL Java_j_EA(JNIEnv *env, jobject self, jobject toolkit,
                                  jint width, jint height) {
    (void) toolkit;

    spriteFree((Sprite *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);

    if (width <= 0 || height <= 0) {
        return;
    }

    Sprite *sprite = calloc(1, sizeof(Sprite));
    if (sprite == NULL) {
        return;
    }

    if (!spriteRoom(sprite, width, height)) {
        free(sprite);
        return;
    }

    setNativeId(env, self, (jlong) (intptr_t) sprite);
}

/**
 * A sprite from a picture the client holds as one byte per pixel and a table of colours.
 *
 * Where the client supplies no alpha, the byte nought means nothing is there at all and every
 * other byte is opaque. Where it does supply one, the byte is looked up whatever it is and the
 * alpha decides on its own, so nought is a colour like any other.
 */
JNIEXPORT void JNICALL Java_j_ma(JNIEnv *env, jobject self, jobject toolkit, jintArray palette,
                                  jbyteArray ink, jbyteArray alpha, jint offset, jint stride,
                                  jint width, jint height) {
    (void) toolkit;

    spriteFree((Sprite *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);

    if (palette == NULL || ink == NULL || width <= 0 || height <= 0) {
        return;
    }

    Sprite *sprite = calloc(1, sizeof(Sprite));
    if (sprite == NULL) {
        return;
    }

    if (!spriteRoom(sprite, width, height)) {
        free(sprite);
        return;
    }

    int colours = (int) (*env)->GetArrayLength(env, palette);
    int taken = (int) (*env)->GetArrayLength(env, ink);

    uint32_t *table = calloc((size_t) colours, sizeof(uint32_t));
    signed char *held = calloc((size_t) taken, 1);
    signed char *clear = alpha == NULL
        ? NULL
        : calloc((size_t) (*env)->GetArrayLength(env, alpha), 1);

    if (table != NULL && held != NULL && (alpha == NULL || clear != NULL)) {
        (*env)->GetIntArrayRegion(env, palette, 0, colours, (jint *) table);
        (*env)->GetByteArrayRegion(env, ink, 0, taken, (jbyte *) held);
        if (clear != NULL) {
            (*env)->GetByteArrayRegion(env, alpha, 0,
                                       (*env)->GetArrayLength(env, alpha), (jbyte *) clear);
        }

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                size_t at = (size_t) offset + (size_t) row * (size_t) stride + (size_t) column;
                if (at >= (size_t) taken) {
                    continue;
                }

                int which = held[at] & 0xFF;
                uint32_t colour = which < colours ? table[which] : 0;
                uint32_t *into = sprite->pixels + (size_t) row * (size_t) width + (size_t) column;

                if (clear == NULL) {
                    *into = which == 0 ? 0 : colour | 0xFF000000u;
                } else {
                    *into = (colour & 0xFFFFFFu) | ((uint32_t) (clear[at] & 0xFF) << 24);
                }
            }
        }
    }

    free(table);
    free(held);
    free(clear);

    setNativeId(env, self, (jlong) (intptr_t) sprite);
}
