/*
 * The flat shapes the client's interfaces are made of.
 *
 * Every one of them is a run of pixels in a straight line, so they all come down to the same two
 * loops, clipped to the rectangle the client last set.
 */

#include "sw3d.h"

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
 * Walks the longer axis a pixel at a time and carries the error on the shorter one, which is the
 * ordinary way to draw a line and the one the client's own diagonal edges were drawn with.
 */
JNIEXPORT void JNICALL Java_oa_wa(JNIEnv *env, jobject self, jint x1, jint y1, jint x2, jint y2,
                                   jint colour, jint mode) {
    (void) env;
    (void) self;
    if (raster.pixels == NULL) {
        return;
    }

    uint32_t value = (uint32_t) colour;

    int dx = x2 > x1 ? x2 - x1 : x1 - x2;
    int dy = y2 > y1 ? y2 - y1 : y1 - y2;
    int stepX = x1 < x2 ? 1 : -1;
    int stepY = y1 < y2 ? 1 : -1;
    int error = dx - dy;

    int x = x1;
    int y = y1;

    while (x != x2 || y != y2) {
        plot(x, y, value, mode);

        int doubled = error * 2;
        if (doubled > -dy) {
            error -= dy;
            x += stepX;
        }
        if (doubled < dx) {
            error += dx;
            y += stepY;
        }
    }

    plot(x2, y2, value, mode);
}
