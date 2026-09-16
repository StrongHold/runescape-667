/*
 * A buffer of how far away each pixel is, kept apart from the picture it belongs to.
 *
 * The client makes one of these when it wants to draw somewhere other than the window: a surface
 * holds the colours and one of these holds the distances, and the two are handed over together.
 */

#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

typedef struct {
    int width;
    int height;
    float *distances;
} Distances;

int distanceBufferWidth(const void *handle) {
    return ((const Distances *) handle)->width;
}

int distanceBufferHeight(const void *handle) {
    return ((const Distances *) handle)->height;
}

float *distanceBufferRows(void *handle) {
    return ((Distances *) handle)->distances;
}

/**
 * Makes a buffer of distances the given size.
 *
 * Nothing is written into it. What it holds before anything has been drawn is whatever the
 * memory held, and the client clears it itself.
 */
JNIEXPORT void JNICALL Java_xa_r(JNIEnv *env, jobject self, jint width, jint height) {
    Distances *buffer = calloc(1, sizeof(Distances));
    if (buffer == NULL) {
        return;
    }

    buffer->width = width;
    buffer->height = height;

    /*
     * The toolkit asks for this on a sixty four byte boundary and is given plain memory: what it
     * calls the aligned allocator hands the size straight to malloc and drops the boundary. So
     * the rows start wherever they start, and the memory is given back with a plain free.
     */
    buffer->distances = malloc((size_t) (width * height) * sizeof(float));

    setNativeId(env, self, (jlong) (intptr_t) buffer);
}

/**
 * Gives a buffer of distances back. The client also passes whether it is being given back from
 * the collector, which the toolkit does not look at.
 */
JNIEXPORT void JNICALL Java_xa_va(JNIEnv *env, jobject self, jlong handle, jboolean collected) {
    (void) collected;

    Distances *buffer = (Distances *) (intptr_t) handle;
    if (buffer != NULL) {
        free(buffer->distances);
        free(buffer);
    }

    setNativeId(env, self, 0);
}
