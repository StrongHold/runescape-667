/*
 * The flat shapes the client's interfaces are made of.
 *
 * Every one of them is a run of pixels in a straight line, so they all come down to the same two
 * loops, clipped to the rectangle the client last set.
 */

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

static void plot(int x, int y, uint32_t colour, int mode) {
    if (x >= raster.clipLeft && x < raster.clipRight && y >= raster.clipTop && y < raster.clipBottom) {
        uint32_t *pixel = raster.pixels + (size_t) y * (size_t) raster.width + x;
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

JNIEXPORT void JNICALL Java_oa_aa(JNIEnv *env, jobject self, jint x, jint y, jint width,
                                   jint height, jint colour, jint mode) {
    (void) env;
    (void) self;
    for (int row = 0; row < height; row++) {
        horizontal(x, y + row, width, (uint32_t) colour, mode);
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

/**
 * Walks the longer axis a pixel at a time and carries the other one as a fraction.
 *
 * The fraction starts half a pixel in, so a point that falls exactly between two pixels is put on
 * the further one. Without that bias a steep line lands a pixel to one side of where the toolkit
 * this replaces puts it, which is invisible on a short line and obvious on a long one.
 */
JNIEXPORT void JNICALL Java_oa_wa(JNIEnv *env, jobject self, jint x1, jint y1, jint x2, jint y2,
                                   jint colour, jint mode) {
    (void) env;
    (void) self;

    if (raster.pixels == NULL) {
        return;
    }

    uint32_t value = (uint32_t) colour;

    int dx = x2 - x1;
    int dy = y2 - y1;
    int alongX = dx < 0 ? -dx : dx;
    int alongY = dy < 0 ? -dy : dy;

    if (alongX == 0 && alongY == 0) {
        plot(x1, y1, value, mode);
        return;
    }

    int steps = alongX > alongY ? alongX : alongY;
    int64_t x = ((int64_t) x1 << FRACTION) + HALF;
    int64_t y = ((int64_t) y1 << FRACTION) + HALF;
    int64_t stepX = ((int64_t) dx << FRACTION) / steps;
    int64_t stepY = ((int64_t) dy << FRACTION) / steps;

    for (int i = 0; i <= steps; i++) {
        plot((int) (x >> FRACTION), (int) (y >> FRACTION), value, mode);
        x += stepX;
        y += stepY;
    }
}
