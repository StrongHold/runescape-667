/*
 * The worker a thread draws through.
 *
 * The client can be told to use more than one thread to draw with, and it asks the toolkit for one
 * worker per thread. A worker holds the scratch a thread needs while it is drawing, so that two
 * threads drawing at once do not write over one another's working.
 *
 * A worker is linked to whichever thread claims it and unlinked when that thread is done, because
 * the client hands a worker between threads over the life of the toolkit rather than keeping one
 * thread per worker.
 */

#include <stdlib.h>

#include "sw3d.h"

typedef struct {
    int width;
    int height;

    /** Whether a thread has claimed this worker, which the client asks for and gives back. */
    int claimed;
} Worker;

static Worker *workerOf(JNIEnv *env, jobject self) {
    return (Worker *) (intptr_t) nativeIdOf(env, self);
}

JNIEXPORT void JNICALL Java_a_HA(JNIEnv *env, jobject self, jlong held, jobject toolkit,
                                  jint width, jint height) {
    (void) held;
    (void) toolkit;

    free(workerOf(env, self));
    setNativeId(env, self, 0);

    Worker *worker = calloc(1, sizeof(Worker));
    if (worker == NULL) {
        return;
    }

    worker->width = width;
    worker->height = height;
    setNativeId(env, self, (jlong) (intptr_t) worker);
}

/**
 * Claims the worker for the thread that is asking.
 */
JNIEXPORT void JNICALL Java_a_M(JNIEnv *env, jobject self, jlong held) {
    (void) env;
    (void) self;

    Worker *worker = (Worker *) (intptr_t) held;
    if (worker != NULL) {
        worker->claimed = 1;
    }
}

JNIEXPORT void JNICALL Java_a_W(JNIEnv *env, jobject self, jlong held) {
    (void) env;
    (void) self;

    Worker *worker = (Worker *) (intptr_t) held;
    if (worker != NULL) {
        worker->claimed = 0;
    }
}

JNIEXPORT void JNICALL Java_a_E(JNIEnv *env, jobject self, jlong held, jboolean immediate) {
    (void) held;
    (void) immediate;

    free(workerOf(env, self));
    setNativeId(env, self, 0);
}
