/*
 * Drawing somewhere other than the window.
 *
 * The client pairs a sprite with a buffer of distances and hands the pair over as a surface. The
 * toolkit then draws into the sprite's pixels instead of into the window's, and a rectangle can
 * be copied between the two in either direction.
 */

#include <string.h>

#include "sw3d.h"

/**
 * Points the toolkit at a surface of the client's own.
 *
 * Everything the toolkit knows about where the picture is goes with it: what may be drawn on
 * opens over the whole surface, and the middle of the picture moves to the middle of it. The
 * scale is not touched, so a surface drawn into keeps whatever scale the window was given.
 *
 * Nothing here remembers the window. The client puts the toolkit back by naming the canvas
 * again.
 */
JNIEXPORT void JNICALL Java_oa_n(JNIEnv *env, jobject self, jlong sprite, jlong distances) {
    (void) env;
    (void) self;

    void *surface = (void *) (intptr_t) sprite;
    void *held = (void *) (intptr_t) distances;
    if (surface == NULL) {
        return;
    }

    int width = spriteWidthOf(surface);
    int height = spriteHeightOf(surface);

    rasterBorrow(spritePixelsOf(surface), held == NULL ? NULL : distanceBufferRows(held),
        width, height);
    projectionMiddled(width, height);
}

/**
 * One rectangle copied a row at a time between the picture being drawn and a surface.
 *
 * The colours and the distances are copied by the same walk, so a surface taken away and put
 * back covers what it covered before rather than only what was drawn over it.
 *
 * The distances have a stride of their own when they travel alone. Copied alongside the colours
 * they follow the sprite's width, and copied on their own they follow the width of the buffer
 * they were made with. Those two are the same size wherever the client makes them together, so
 * the difference only shows if it ever makes them apart.
 */
static void blit(void *sprite, void *distances, int pictureX, int pictureY, int wide, int tall,
        int surfaceX, int surfaceY, int colours, int depths, int intoSurface) {
    if (sprite == NULL) {
        return;
    }

    int surfaceWidth = spriteWidthOf(sprite);
    uint32_t *surfacePixels = spritePixelsOf(sprite);
    float *surfaceDepths = distances == NULL ? NULL : distanceBufferRows(distances);

    if (tall <= 0 || wide <= 0) {
        return;
    }

    if (colours && surfacePixels != NULL && raster.pixels != NULL) {
        int pictureStep = raster.width;
        uint32_t *picture = raster.pixels;

        for (int row = 0; row < tall; row++) {
            uint32_t *inPicture = picture
                + (size_t) (pictureY + row) * (size_t) pictureStep + pictureX;
            uint32_t *onSurface = surfacePixels
                + (size_t) (surfaceY + row) * (size_t) surfaceWidth + surfaceX;

            if (intoSurface) {
                memmove(onSurface, inPicture, (size_t) wide * sizeof(uint32_t));
            } else {
                memmove(inPicture, onSurface, (size_t) wide * sizeof(uint32_t));
            }
        }
    }

    if (depths && surfaceDepths != NULL && raster.depths != NULL) {
        int surfaceStep = colours ? surfaceWidth : distanceBufferWidth(distances);

        for (int row = 0; row < tall; row++) {
            float *inPicture = raster.depths
                + (size_t) (pictureY + row) * (size_t) raster.width + pictureX;
            float *onSurface = surfaceDepths
                + (size_t) (surfaceY + row) * (size_t) surfaceStep + surfaceX;

            if (intoSurface) {
                memmove(onSurface, inPicture, (size_t) wide * sizeof(float));
            } else {
                memmove(inPicture, onSurface, (size_t) wide * sizeof(float));
            }
        }
    }
}

/**
 * Copies a rectangle of the picture being drawn onto the surface.
 *
 * The first pair of places is read in the picture and the second is written on the surface.
 * The copy the other way names them the other way round.
 */
JNIEXPORT void JNICALL Java_wa_X(JNIEnv *env, jobject self, jlong toolkit, jlong sprite,
                                  jlong distances, jint pictureX, jint pictureY, jint wide,
                                  jint tall, jint surfaceX, jint surfaceY,
                                  jboolean colours, jboolean depths) {
    (void) env;
    (void) self;
    (void) toolkit;

    blit((void *) (intptr_t) sprite, (void *) (intptr_t) distances,
        pictureX, pictureY, wide, tall, surfaceX, surfaceY, colours, depths, 1);
}

/**
 * Copies a rectangle of the surface back onto the picture being drawn.
 *
 * Here the first pair of places is read on the surface and the second is written in the picture.
 */
JNIEXPORT void JNICALL Java_wa_Z(JNIEnv *env, jobject self, jlong toolkit, jlong sprite,
                                  jlong distances, jint surfaceX, jint surfaceY, jint wide,
                                  jint tall, jint pictureX, jint pictureY,
                                  jboolean colours, jboolean depths) {
    (void) env;
    (void) self;
    (void) toolkit;

    blit((void *) (intptr_t) sprite, (void *) (intptr_t) distances,
        pictureX, pictureY, wide, tall, surfaceX, surfaceY, colours, depths, 0);
}
