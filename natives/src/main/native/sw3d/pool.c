/*
 * The memory a model's geometry is kept in.
 *
 * A pool is asked for with a size and then handed out in pieces, and everything taken from it is
 * given back at once rather than a piece at a time. The client makes one per scene and empties it
 * when it changes scene.
 *
 * Nothing on the Java side can see inside a pool. It has no way to ask how much is left or where
 * anything sits, so what matters here is that a pool hands out memory, that emptying it releases
 * every piece, and that destroying it releases the pool as well.
 */

#include <stdlib.h>

#include "sw3d.h"

typedef struct Piece {
    struct Piece *next;
} Piece;

struct Pool {
    unsigned int limit;
    size_t taken;
    Piece *pieces;
};

/**
 * Hands out a piece of a pool, or nothing when the pool has already given out as much as it was
 * asked to hold.
 */
void *poolTake(Pool *pool, size_t size) {
    if (pool == NULL || pool->taken + size > pool->limit) {
        return NULL;
    }

    Piece *piece = calloc(1, sizeof(Piece) + size);
    if (piece == NULL) {
        return NULL;
    }

    piece->next = pool->pieces;
    pool->pieces = piece;
    pool->taken += size;

    return piece + 1;
}

void poolRelease(Pool *pool) {
    if (pool == NULL) {
        return;
    }

    Piece *piece = pool->pieces;
    while (piece != NULL) {
        Piece *next = piece->next;
        free(piece);
        piece = next;
    }

    pool->pieces = NULL;
    pool->taken = 0;
}

JNIEXPORT void JNICALL Java_ya_aa(JNIEnv *env, jobject self, jobject toolkit, jint size) {
    (void) toolkit;

    Pool *pool = calloc(1, sizeof(Pool));
    if (pool != NULL) {
        pool->limit = (unsigned int) size;
    }

    setNativeId(env, self, (jlong) (intptr_t) pool);
}

/**
 * Gives back everything taken from the pool, leaving the pool itself.
 */
JNIEXPORT void JNICALL Java_ya_ga(JNIEnv *env, jobject self) {
    poolRelease((Pool *) (intptr_t) nativeIdOf(env, self));
}

/**
 * Answers nothing, as the toolkit this replaces does.
 */
JNIEXPORT void JNICALL Java_ya_r(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;
    /* empty */
}

JNIEXPORT void JNICALL Java_ya_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    Pool *pool = (Pool *) (intptr_t) nativeIdOf(env, self);
    if (pool != NULL) {
        poolRelease(pool);
        free(pool);
        setNativeId(env, self, 0);
    }
}
