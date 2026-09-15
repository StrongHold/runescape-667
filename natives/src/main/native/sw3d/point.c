/*
 * Where a point in the world lands on the buffer, and what is already on it.
 *
 * The client asks these for the drawing it does itself rather than through a model: where to put
 * the marker over a player's head, whether a thing is on the screen at all, what the picture looks
 * like so far. Everything here answers in the corner of the buffer that may be drawn on, which is
 * where the client draws.
 */

#include <stdlib.h>

#include "sw3d.h"

/** A point's place on the buffer, and how far from the eye it was. */
typedef struct {
    int across;
    int down;
    int away;
} Landing;

/** What a point that is not on the screen answers. */
static const Landing NOWHERE = { -1, -1, -1 };

/**
 * One of the three numbers a point becomes once the camera has had it.
 *
 * The camera's matrix is read a column at a time because the client asks for one point at a time,
 * and the distance is wanted before either of the other two: a point too near or too far is
 * answered without the rest being worked out at all.
 */
static float throughCamera(const float *camera, float x, float y, float z, int column) {
    return x * camera[column] + y * camera[4 + column] + z * camera[8 + column]
        + camera[12 + column];
}

/**
 * Where a point lands, given what it is divided by.
 *
 * Dividing by the distance is what puts a point where the eye sees it. Dividing by a fixed number
 * instead lays it flat, which is how the client places a thing that should not shrink as it goes
 * away.
 */
static Landing landing(float x, float y, float z, float divisor, int bounded) {
    const void *held = cameraMatrix();
    if (held == NULL) {
        return NOWHERE;
    }

    const float *camera = matrixRows(held);
    const Projection *view = projection();
    float away = throughCamera(camera, x, y, z, 2);

    if (bounded && (view->near > away || away > view->far)) {
        return NOWHERE;
    }

    float across = throughCamera(camera, x, y, z, 0) * view->scaleX / divisor;
    float fromLeft = (float) raster.clipLeft - view->centreX;
    float fromRight = (float) raster.clipRight - view->centreX;

    if (bounded && (across < fromLeft || fromRight < across)) {
        return NOWHERE;
    }

    float down = throughCamera(camera, x, y, z, 1) * view->scaleY / divisor;
    float fromTop = (float) raster.clipTop - view->centreY;
    float fromBottom = (float) raster.clipBottom - view->centreY;

    if (bounded && (down < fromTop || fromBottom < down)) {
        return NOWHERE;
    }

    Landing where;
    where.across = (int) (across - fromLeft);
    where.down = (int) (down - fromTop);
    where.away = (int) away;
    return where;
}

static void answer(JNIEnv *env, jintArray destination, Landing where) {
    if (destination == NULL) {
        return;
    }

    jint written[3] = { where.across, where.down, where.away };
    (*env)->SetIntArrayRegion(env, destination, 0, 3, written);
}

/**
 * Where a point lands, or nowhere when it is off the screen or outside the two planes.
 */
JNIEXPORT void JNICALL Java_oa_da(JNIEnv *env, jobject self, jint x, jint y, jint z,
                                   jintArray destination) {
    (void) self;

    float away = 0.0f;
    const void *held = cameraMatrix();
    if (held != NULL) {
        away = throughCamera(matrixRows(held), (float) x, (float) y, (float) z, 2);
    }

    answer(env, destination, landing((float) x, (float) y, (float) z, away, 1));
}

/**
 * Where a point lands, answered even when it is nowhere near the screen.
 */
JNIEXPORT void JNICALL Java_oa_H(JNIEnv *env, jobject self, jint x, jint y, jint z,
                                  jintArray destination) {
    (void) self;

    float away = 0.0f;
    const void *held = cameraMatrix();
    if (held != NULL) {
        away = throughCamera(matrixRows(held), (float) x, (float) y, (float) z, 2);
    }

    answer(env, destination, landing((float) x, (float) y, (float) z, away, 0));
}

/**
 * Where a point lands when it is laid flat rather than seen in perspective.
 */
JNIEXPORT void JNICALL Java_oa_HA(JNIEnv *env, jobject self, jint x, jint y, jint z,
                                   jint spread, jintArray destination) {
    (void) self;

    answer(env, destination, landing((float) x, (float) y, (float) z, (float) spread, 1));
}

/**
 * What may be drawn on, as left, top, right and bottom.
 */
JNIEXPORT void JNICALL Java_oa_K(JNIEnv *env, jobject self, jintArray destination) {
    (void) self;

    if (destination == NULL) {
        return;
    }

    jint written[4] = {
        raster.clipLeft,
        raster.clipTop,
        raster.clipRight,
        raster.clipBottom
    };

    (*env)->SetIntArrayRegion(env, destination, 0, 4, written);
}

/**
 * Where the middle of the picture is and how wide a field runs through it.
 */
JNIEXPORT jintArray JNICALL Java_oa_Y(JNIEnv *env, jobject self) {
    (void) self;

    jintArray answered = (*env)->NewIntArray(env, 4);
    if (answered == NULL) {
        return NULL;
    }

    const Projection *view = projection();
    jint written[4] = {
        (jint) view->centreX,
        (jint) view->centreY,
        (jint) view->scaleX,
        (jint) view->scaleY
    };

    (*env)->SetIntArrayRegion(env, answered, 0, 4, written);
    return answered;
}

/**
 * A rectangle of the buffer as it stands, a row at a time.
 *
 * The rectangle is taken where the client asked for it, in the whole buffer rather than in the
 * part that may be drawn on, and it is not clipped.
 */
JNIEXPORT jintArray JNICALL Java_oa_na(JNIEnv *env, jobject self, jint x, jint y,
                                        jint width, jint height) {
    (void) self;

    if (width <= 0 || height <= 0 || raster.pixels == NULL) {
        return NULL;
    }

    jintArray answered = (*env)->NewIntArray(env, width * height);
    if (answered == NULL) {
        return NULL;
    }

    for (int row = 0; row < height; row++) {
        (*env)->SetIntArrayRegion(env, answered, row * width, width,
                                  (const jint *) (raster.pixels
                                      + (size_t) (y + row) * (size_t) raster.width
                                      + (size_t) x));
    }

    return answered;
}
