/*
 * The renderer the toolkit object stands in front of.
 *
 * There is one of it. The toolkit carries no handle, so the state lives here, and a client that
 * builds several toolkits is driving the same renderer through each of them.
 */

#include <string.h>

#include "sw3d.h"

Raster raster;

/**
 * Where the client wants the middle of the picture, and how wide a field it wants through it.
 * Nothing reads these yet; they are kept because every projection the toolkit does is relative
 * to them.
 */
static int centreX;
static int centreY;
static int fieldWidth;
static int fieldHeight;

static float ambient;

static jlong camera;

JNIEXPORT void JNICALL Java_oa_MA(JNIEnv *env, jobject self, jobject textures,
                                   jint a2, jint a3) {
    (void) env;
    (void) self;
    (void) textures;
    (void) a2;
    (void) a3;

    raster.pixels = NULL;
    raster.width = 0;
    raster.height = 0;
}

JNIEXPORT void JNICALL Java_oa_ma(JNIEnv *env, jobject self, jlong matrix) {
    (void) env;
    (void) self;

    camera = matrix;
}

/**
 * Points the renderer at the surface it should draw into, or at nothing when the client takes
 * its canvas away.
 */
JNIEXPORT void JNICALL Java_oa_t(JNIEnv *env, jobject self, jobject canvas) {
    (void) self;

    Surface *surface = (Surface *) (intptr_t) nativeIdOf(env, canvas);
    if (surface == NULL) {
        raster.pixels = NULL;
        raster.width = 0;
        raster.height = 0;
    } else {
        raster.pixels = surfacePixels(surface);
        raster.width = surfaceWidth(surface);
        raster.height = surfaceHeight(surface);
    }
}

JNIEXPORT void JNICALL Java_oa_la(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;
}

JNIEXPORT void JNICALL Java_oa_DA(JNIEnv *env, jobject self, jint x, jint y,
                                   jint width, jint height) {
    (void) env;
    (void) self;

    centreX = x;
    centreY = y;
    fieldWidth = width;
    fieldHeight = height;
}

JNIEXPORT void JNICALL Java_oa_xa(JNIEnv *env, jobject self, jfloat globalAmbient) {
    (void) env;
    (void) self;

    ambient = globalAmbient;
}

/**
 * Fills the whole back buffer. The client passes a colour with no alpha in it, and the surface
 * ignores the top byte, so it is written straight through.
 */
JNIEXPORT void JNICALL Java_oa_GA(JNIEnv *env, jobject self, jint colour) {
    (void) env;
    (void) self;

    if (raster.pixels == NULL) {
        return;
    }

    size_t count = (size_t) raster.width * (size_t) raster.height;
    uint32_t value = (uint32_t) colour;

    for (size_t i = 0; i < count; i++) {
        raster.pixels[i] = value;
    }
}
