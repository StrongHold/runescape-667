/*
 * Clipping masks.
 *
 * A mask is a shape kept as one run of pixels per row: where the run starts and how long it is.
 * That is all the client needs to round the corners off an interface, which is what it uses them
 * for, and it is far cheaper to test than a shape kept a pixel at a time.
 *
 * A mask is placed on the buffer when it is used rather than when it is made, so the same mask
 * rounds the corners of every interface that is the same size.
 */

#include <stdlib.h>

#include "sw3d.h"

typedef struct {
    int width;
    int height;
    int rows;
    int *starts;
    int *lengths;
} Mask;

static int *copyRow(JNIEnv *env, jintArray source, int count) {
    int *copy = calloc((size_t) count, sizeof(int));
    if (copy != NULL) {
        (*env)->GetIntArrayRegion(env, source, 0, count, (jint *) copy);
    }
    return copy;
}

JNIEXPORT void JNICALL Java_na_ma(JNIEnv *env, jobject self, jobject toolkit, jobject pool,
                                   jint width, jint height, jintArray starts, jintArray lengths) {
    (void) toolkit;
    (void) pool;

    maskFree((void *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);

    if (starts == NULL || lengths == NULL) {
        return;
    }

    Mask *mask = calloc(1, sizeof(Mask));
    if (mask == NULL) {
        return;
    }

    mask->width = width;
    mask->height = height;
    mask->rows = (int) (*env)->GetArrayLength(env, starts);
    mask->starts = copyRow(env, starts, mask->rows);
    mask->lengths = copyRow(env, lengths, mask->rows);

    if (mask->starts == NULL || mask->lengths == NULL) {
        maskFree(mask);
        return;
    }

    allocatedGrew((size_t) mask->rows * 2 * sizeof(int));
    setNativeId(env, self, (jlong) (intptr_t) mask);
}

void maskFree(void *held) {
    Mask *mask = (Mask *) held;
    if (mask != NULL) {
        free(mask->starts);
        free(mask->lengths);
        free(mask);
    }
}

JNIEXPORT void JNICALL Java_na_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    maskFree((void *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);
}

/**
 * What a mask lets through on one row of the buffer, already brought inside the clip.
 *
 * The row is given in the buffer's own rows, and where the mask sits on the buffer is given
 * alongside, because a mask is placed when it is used.
 */
/**
 * How many rows the shape describes, which is what bounds a line drawn through it.
 */
int maskRows(const void *held) {
    const Mask *mask = (const Mask *) held;
    return mask == NULL ? 0 : mask->rows;
}

/**
 * The run the shape allows on one row, as the client gave it and not narrowed to what may be
 * drawn on. A line is measured against this one and clipped separately.
 */
int maskRowRun(const void *held, int row, int across, int down, int *from, int *count) {
    const Mask *mask = (const Mask *) held;
    if (mask == NULL || row < down || row - down >= mask->rows) {
        return 0;
    }

    *from = across + mask->starts[row - down];
    *count = mask->lengths[row - down];
    return 1;
}

int maskRun(const void *held, int row, int across, int down, int *from, int *count) {
    const Mask *mask = (const Mask *) held;
    if (mask == NULL || row < down || row - down >= mask->rows) {
        return 0;
    }

    int start = across + mask->starts[row - down];
    int length = mask->lengths[row - down];

    if (start < raster.clipLeft) {
        length -= raster.clipLeft - start;
        start = raster.clipLeft;
    }
    if (start + length > raster.clipRight) {
        length = raster.clipRight - start;
    }

    *from = start;
    *count = length;
    return length > 0;
}

/**
 * Fills the shape a mask describes with one colour.
 */
JNIEXPORT void JNICALL Java_oa_A(JNIEnv *env, jobject self, jint colour, jobject held,
                                  jint across, jint down) {
    (void) self;

    const void *mask = (const void *) (intptr_t) nativeIdOf(env, held);
    if (mask == NULL || raster.pixels == NULL) {
        return;
    }

    for (int row = raster.clipTop; row < raster.clipBottom; row++) {
        int from = 0;
        int count = 0;

        if (maskRun(mask, row, across, down, &from, &count)) {
            uint32_t *into = raster.pixels + (size_t) row * (size_t) raster.width + (size_t) from;
            for (int column = 0; column < count; column++) {
                into[column] = (uint32_t) colour;
            }
        }
    }
}
