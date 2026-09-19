/*
 * The flat shapes the client's interfaces are made of.
 *
 * Every one of them is a run of pixels in a straight line, so they all come down to the same two
 * loops, clipped to the rectangle the client last set.
 */

#include <math.h>
#include <stdint.h>

#include "sw3d.h"

enum {
    FRACTION = 16,
    HALF = 1 << (FRACTION - 1)
};

static void horizontal(int x, int y, int length, uint32_t colour, int mode) {
    if (raster.pixels == NULL || y < raster.clipTop || y >= raster.clipBottom) {
        return;
    }

    int from = x < raster.clipLeft ? raster.clipLeft : x;
    int to = x + length > raster.clipRight ? raster.clipRight : x + length;
    uint32_t *row = raster.pixels + (size_t) y * (size_t) raster.width;

    for (int column = from; column < to; column++) {
        row[column] = blend(row[column], colour, mode);
    }
}

static void vertical(int x, int y, int length, uint32_t colour, int mode) {
    if (raster.pixels == NULL || x < raster.clipLeft || x >= raster.clipRight) {
        return;
    }

    int from = y < raster.clipTop ? raster.clipTop : y;
    int to = y + length > raster.clipBottom ? raster.clipBottom : y + length;

    for (int row = from; row < to; row++) {
        uint32_t *pixel = raster.pixels + (size_t) row * (size_t) raster.width + x;
        *pixel = blend(*pixel, colour, mode);
    }
}

/**
 * Narrows what may be drawn on to a rectangle, given by two corners.
 */
JNIEXPORT void JNICALL Java_oa_KA(JNIEnv *env, jobject self, jint x1, jint y1, jint x2, jint y2) {
    (void) env;
    (void) self;

    raster.clipLeft = x1 < 0 ? 0 : x1;
    raster.clipTop = y1 < 0 ? 0 : y1;
    raster.clipRight = x2 > raster.width ? raster.width : x2;
    raster.clipBottom = y2 > raster.height ? raster.height : y2;
}

/**
 * Fills a rectangle.
 *
 * The rectangle is cut to what may be drawn on before anything is drawn rather than a row at a
 * time. That is how the toolkit does it, and it matters: the client is free to ask for a
 * rectangle a billion rows tall, and cutting each row as it comes still walks a billion of them.
 */
JNIEXPORT void JNICALL Java_oa_aa(JNIEnv *env, jobject self, jint x, jint y, jint width,
                                   jint height, jint colour, jint mode) {
    (void) env;
    (void) self;

    fillRect(x, y, width, height, (uint32_t) colour, mode);
}

void fillRect(int x, int y, int width, int height, uint32_t colour, int mode) {
    if (raster.clipLeft > x) {
        width -= raster.clipLeft - x;
        x = raster.clipLeft;
    }
    if (raster.clipTop > y) {
        height -= raster.clipTop - y;
        y = raster.clipTop;
    }
    if (x + width > raster.clipRight) {
        width = raster.clipRight - x;
    }
    if (y + height > raster.clipBottom) {
        height = raster.clipBottom - y;
    }

    for (int row = 0; row < height; row++) {
        horizontal(x, y + row, width, colour, mode);
    }
}

JNIEXPORT void JNICALL Java_oa_U(JNIEnv *env, jobject self, jint x, jint y, jint length,
                                  jint colour, jint mode) {
    (void) env;
    (void) self;
    horizontal(x, y, length, (uint32_t) colour, mode);
}

JNIEXPORT void JNICALL Java_oa_P(JNIEnv *env, jobject self, jint x, jint y, jint length,
                                  jint colour, jint mode) {
    (void) env;
    (void) self;
    vertical(x, y, length, (uint32_t) colour, mode);
}

/*
 * Lines.
 *
 * A line is walked along whichever axis it covers more of, a whole pixel at a time, carrying the
 * other axis as a fraction of a pixel. Three things about that are worth keeping in mind:
 *
 * The walk is cut to what may be drawn on **before** it starts rather than a pixel at a time.
 * The client is free to ask for a line between two points far outside the buffer, and walking
 * every step of one only to throw each away costs as much as drawing it.
 *
 * The step of the shorter axis is rounded rather than truncated, and the fraction starts half a
 * pixel in. Without either, a long line lands a pixel to one side of where the toolkit puts it.
 *
 * A line along a single row or column is drawn as that run instead, but only when the client
 * gives no shape to cut it to.
 */

/** Where a step of the shorter axis is held, as a fraction of a pixel. */
enum { LINE_FRACTION = 16 };

static void plotAt(int x, int y, uint32_t colour, int mode) {
    uint32_t *pixel = raster.pixels + (size_t) y * (size_t) raster.width + x;
    *pixel = blend(*pixel, colour, mode);
}

/** How far the shorter axis moves for each whole pixel of the longer one. */
static int slopeOf(int shorter, int longer) {
    if (longer == 0) {
        return 0;
    }

    return (int) floor((double) (shorter << LINE_FRACTION) / (double) longer + 0.5);
}

/**
 * Draws a line, cut to a shape when the client gives one.
 *
 * A shape holds one run per row and also says which rows it covers at all, so a line given one
 * is bounded twice over.
 */
static void drawLine(int x1, int y1, int x2, int y2, uint32_t colour, int mode,
                     const void *mask, int across, int down) {
    if (raster.pixels == NULL) {
        return;
    }

    int firstRow = raster.clipTop;
    int lastRow = raster.clipBottom;

    if (mask != NULL) {
        if (firstRow < down) {
            firstRow = down;
        }
        if (lastRow >= down + maskRows(mask)) {
            lastRow = down + maskRows(mask);
        }
    }

    int alongX = x2 - x1;
    int alongY = y2 - y1;

    /*
     * The line is always walked in the direction that leaves both steps positive, so one end is
     * taken as the start and the other worked out from it rather than used directly.
     */
    if (alongX + alongY < 0) {
        x1 += alongX;
        alongX = -alongX;
        y1 += alongY;
        alongY = -alongY;
    }

    int from = 0;
    int count = 0;

    if (alongX > alongY) {
        int held = (y1 << LINE_FRACTION) + (1 << (LINE_FRACTION - 1));
        int slope = slopeOf(alongY, alongX);
        int last = x1 + alongX;

        if (x1 < raster.clipLeft) {
            held += (raster.clipLeft - x1) * slope;
            x1 = raster.clipLeft;
        }
        if (last >= raster.clipRight) {
            last = raster.clipRight - 1;
        }

        for (int x = x1; x <= last; x++, held += slope) {
            int row = held >> LINE_FRACTION;
            if (row < firstRow || row >= lastRow) {
                continue;
            }

            if (mask != NULL) {
                maskRowRun(mask, row, across, down, &from, &count);
                if (x < from || x >= from + count) {
                    continue;
                }
            }

            plotAt(x, row, colour, mode);
        }
    } else {
        int held = (x1 << LINE_FRACTION) + (1 << (LINE_FRACTION - 1));
        int slope = slopeOf(alongX, alongY);
        int last = y1 + alongY;

        if (y1 < firstRow) {
            held += (firstRow - y1) * slope;
            y1 = firstRow;
        }
        if (last >= lastRow) {
            last = lastRow - 1;
        }

        for (int y = y1; y <= last; y++, held += slope) {
            int x = held >> LINE_FRACTION;
            if (x < raster.clipLeft || x >= raster.clipRight) {
                continue;
            }

            if (mask != NULL) {
                maskRowRun(mask, y, across, down, &from, &count);
                if (x < from || x >= from + count) {
                    continue;
                }
            }

            plotAt(x, y, colour, mode);
        }
    }
}

JNIEXPORT void JNICALL Java_oa_wa(JNIEnv *env, jobject self, jint x1, jint y1, jint x2, jint y2,
                                   jint colour, jint mode) {
    (void) env;
    (void) self;

    if (y2 - y1 == 0) {
        int left = x2 < x1 ? x2 : x1;
        int span = x2 < x1 ? x1 - x2 : x2 - x1;
        horizontal(left, y1, span + 1, (uint32_t) colour, mode);
    } else if (x2 - x1 == 0) {
        int top = y2 < y1 ? y2 : y1;
        int span = y2 < y1 ? y1 - y2 : y2 - y1;
        vertical(x1, top, span + 1, (uint32_t) colour, mode);
    } else {
        drawLine(x1, y1, x2, y2, (uint32_t) colour, mode, NULL, 0, 0);
    }
}

/**
 * A line cut to a shape, which is how the minimap draws the outline of every landmark.
 */
JNIEXPORT void JNICALL Java_oa_Z(JNIEnv *env, jobject self, jint x1, jint y1, jint x2, jint y2,
                                  jint colour, jint mode, jobject mask, jint across, jint down) {
    (void) self;

    const void *shape = (const void *) (intptr_t) nativeIdOf(env, mask);
    if (shape == NULL) {
        return;
    }

    drawLine(x1, y1, x2, y2, (uint32_t) colour, mode, shape, across, down);
}

/*
 * A filled circle.
 *
 * Each row is a run, and how long the run is comes from an addition rather than from a square
 * root: the two halves of the circle walk outwards and inwards a step at a time, and every step
 * adds a number that itself grows by two. Doing it by the root instead lands on a different
 * pixel wherever the answer sits close to a whole number.
 *
 * The two halves do not agree about their edges. The upper one runs from one past the left of
 * the run up to but not including the right of it; the lower one runs from the left up to and
 * including the right, and stops one short of what may be drawn on. That is what the toolkit
 * does.
 */

/** How far away a pixel already is before this one is allowed over it. */
static int nearerThan(int x, int y, float depth) {
    if (raster.depths == NULL) {
        return 1;
    }

    return raster.depths[(size_t) y * (size_t) raster.width + x] > depth;
}

static void runOfCircle(int y, int from, int to, uint32_t colour, int mode, float depth) {
    uint32_t *row = raster.pixels + (size_t) y * (size_t) raster.width;

    for (int column = from; column < to; column++) {
        if (nearerThan(column, y, depth)) {
            row[column] = blend(row[column], colour, mode);
        }
    }
}

void fillCircle(int x, int y, float depth, int radius, uint32_t colour, int mode) {
    if (raster.pixels == NULL) {
        return;
    }

    int reach = radius < 0 ? -radius : radius;
    int firstRow = y - reach < raster.clipTop ? raster.clipTop : y - reach;
    int lastRow = y + reach + 1 > raster.clipBottom ? raster.clipBottom : y + reach + 1;
    int middleRow = lastRow < y ? lastRow : y;
    int square = reach * reach;

    int down = y - firstRow;
    int outer = down * down;
    int inner = outer - down;
    int step = -(down + down);
    int across = 0;

    for (int row = firstRow; row < middleRow; row++) {
        while (square >= inner || square >= outer) {
            outer += across + across;
            inner += across + across;
            across++;
        }

        int from = x + 1 - across < raster.clipLeft ? raster.clipLeft : x + 1 - across;
        int to = x + across > raster.clipRight ? raster.clipRight : x + across;
        runOfCircle(row, from, to, colour, mode, depth);

        step += 2;
        outer += step - 2;
        inner += step;
    }

    /*
     * The lower half starts where the upper one left off, or at the first row that may be drawn
     * on when the middle of the circle sits above that. Without the second of those a circle
     * centred above what may be drawn on is drawn above it.
     */
    int start = middleRow < firstRow ? firstRow : middleRow;
    int below = start - y;
    int reached = below * below + square;
    int lowOuter = reached - below;
    int lowInner = reached - reach;
    int lowStep = below + below;
    int lowAcross = reach;

    for (int row = start; row < lastRow; row++) {
        while (square < lowOuter && square < lowInner) {
            lowOuter -= lowAcross + lowAcross;
            lowAcross--;
            lowInner -= lowAcross + lowAcross;
        }

        int from = x - lowAcross < raster.clipLeft ? raster.clipLeft : x - lowAcross;
        int to = x + lowAcross > raster.clipRight - 1 ? raster.clipRight - 1 : x + lowAcross;
        runOfCircle(row, from, to + 1, colour, mode, depth);

        lowOuter += lowStep;
        lowInner += lowStep;
        lowStep += 2;
    }
}

/**
 * Fills a circle. The client only ever asks for the blending mode, and a mode the toolkit does
 * not know is a mistake in the client rather than something to be quietly ignored.
 */
JNIEXPORT void JNICALL Java_oa_za(JNIEnv *env, jobject self, jint x, jint y, jint radius,
                                   jint colour, jint mode) {
    (void) self;

    if (mode < BLEND_OPAQUE || mode > BLEND_ADD) {
        jclass complaint = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
        if (complaint != NULL) {
            (*env)->ThrowNew(env, complaint, NULL);
        }
        return;
    }

    fillCircle(x, y, 0.0f, radius, (uint32_t) colour, mode);
}
