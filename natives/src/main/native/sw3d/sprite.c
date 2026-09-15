/*
 * Sprites, and putting them on the back buffer.
 *
 * A sprite owns a copy of its pixels rather than a view onto the array it was made from, because
 * the client is free to reuse that array the moment it returns.
 */

#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

typedef struct {
    int width;
    int height;
    uint32_t *pixels;
} Sprite;

static void spriteFree(Sprite *sprite) {
    if (sprite != NULL) {
        free(sprite->pixels);
        free(sprite);
    }
}

/**
 * How the client wants a sprite's own pixels combined with the colour it passes.
 */
enum {
    /** Multiply the two, which leaves the sprite almost as it is when the colour is white. */
    OP_MODULATE = 0,
    /** Ignore the sprite's colours and paint its shape in the colour. */
    OP_FLAT = 3
};

/**
 * Multiplies a pixel by a colour.
 *
 * The shift is by eight rather than a divide by 255, so a channel comes back one lower than it
 * went in even when the colour is white. That is what the toolkit this replaces does, and the
 * client's artwork was drawn against it, so it is kept.
 */
static uint32_t modulate(uint32_t pixel, uint32_t colour) {
    uint32_t red = (((pixel >> 16) & 0xFF) * ((colour >> 16) & 0xFF)) >> 8;
    uint32_t green = (((pixel >> 8) & 0xFF) * ((colour >> 8) & 0xFF)) >> 8;
    uint32_t blue = ((pixel & 0xFF) * (colour & 0xFF)) >> 8;

    return (red << 16) | (green << 8) | blue;
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

    Sprite *sprite = calloc(1, sizeof(Sprite));
    if (sprite == NULL) {
        return;
    }

    sprite->width = width;
    sprite->height = height;
    sprite->pixels = calloc((size_t) width * (size_t) height, sizeof(uint32_t));

    if (sprite->pixels == NULL) {
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

/**
 * Draws a sprite at a place on the back buffer, clipped to it.
 */
JNIEXPORT void JNICALL Java_j_W(JNIEnv *env, jobject self, jlong handle, jint x, jint y,
                                 jint op, jint colour, jint mode) {
    (void) env;
    (void) self;
    (void) mode;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    if (sprite == NULL || raster.pixels == NULL) {
        return;
    }

    int firstRow = y < raster.clipTop ? raster.clipTop - y : 0;
    int firstColumn = x < raster.clipLeft ? raster.clipLeft - x : 0;
    int lastRow = y + sprite->height > raster.clipBottom ? raster.clipBottom - y : sprite->height;
    int lastColumn = x + sprite->width > raster.clipRight ? raster.clipRight - x : sprite->width;

    uint32_t tint = (uint32_t) colour & 0xFFFFFF;

    for (int row = firstRow; row < lastRow; row++) {
        const uint32_t *from = sprite->pixels + (size_t) row * (size_t) sprite->width;
        uint32_t *to = raster.pixels + (size_t) (y + row) * (size_t) raster.width + x;

        for (int column = firstColumn; column < lastColumn; column++) {
            to[column] = op == OP_FLAT ? tint : modulate(from[column] & 0xFFFFFF, tint);
        }
    }
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
