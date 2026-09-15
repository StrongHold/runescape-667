/*
 * The renderer the toolkit object stands in front of.
 *
 * There is one of it. The toolkit carries no handle, so the state lives here, and a client that
 * builds several toolkits is driving the same renderer through each of them.
 */

#include <string.h>

#include "sw3d.h"

Raster raster;

void rasterResetClip(void) {
    raster.clipLeft = 0;
    raster.clipTop = 0;
    raster.clipRight = raster.width;
    raster.clipBottom = raster.height;
}

void rasterUse(uint32_t *pixels, int width, int height) {
    raster.pixels = pixels;
    raster.width = width;
    raster.height = height;
    rasterResetClip();
}

/**
 * Where the client wants the middle of the picture, and how wide a field it wants through it.
 * Nothing reads these yet; they are kept because every projection the toolkit does is relative
 * to them.
 */
static Projection view;

static float ambient;

static jlong camera;

const Projection *projection(void) {
    return &view;
}

const void *cameraMatrix(void) {
    return (const void *) (intptr_t) camera;
}

JNIEXPORT void JNICALL Java_oa_MA(JNIEnv *env, jobject self, jobject textures,
                                   jint a2, jint a3) {
    (void) env;
    (void) self;
    (void) textures;
    (void) a2;
    (void) a3;

    rasterUse(NULL, 0, 0);
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
        rasterUse(NULL, 0, 0);
    } else {
        rasterUse(surfacePixels(surface), surfaceWidth(surface), surfaceHeight(surface));
    }
}

/**
 * Opens the clip over the whole buffer again.
 */
JNIEXPORT void JNICALL Java_oa_la(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    rasterResetClip();
}

JNIEXPORT void JNICALL Java_oa_DA(JNIEnv *env, jobject self, jint x, jint y,
                                   jint width, jint height) {
    (void) env;
    (void) self;

    view.centreX = (float) x;
    view.centreY = (float) y;
    view.scaleX = (float) width;
    view.scaleY = (float) height;
}

/**
 * How close and how far a thing may be before it is cut away. Nothing is drawn until the client
 * has said, and it does not complain when it has not.
 */
JNIEXPORT void JNICALL Java_oa_f(JNIEnv *env, jobject self, jint near, jint far) {
    (void) env;
    (void) self;

    view.near = near;
    view.far = far;
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
