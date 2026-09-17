/*
 * The shadow a model casts on the ground.
 *
 * A shadow is the model seen from straight above and flattened: one byte per place, counting how
 * many of the model's faces stand over it. The client asks a model for a shadow, adds it to the
 * ground under the model, and takes it away again before the model moves, so the same shadow is
 * carried between the two sides rather than being worked out afresh every frame.
 *
 * The places are coarser than the world. How much coarser the client sets, and a world distance
 * is brought down to a shadow distance by a shift rather than a divide.
 */

#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

struct Shadow {
    /** Where the shadow sits, in shadow places, around the model's own middle. */
    int left;
    int top;
    int right;
    int bottom;

    /** How far the shadow reaches, which is one short of how many places a row holds. */
    int across;
    int down;

    unsigned char *places;
    int room;
};

Shadow *shadowNew(int width, int height) {
    Shadow *shadow = calloc(1, sizeof(Shadow));
    if (shadow == NULL) {
        return NULL;
    }

    int wanted = width * height;
    if (wanted < 0) {
        wanted = 0;
    }

    shadow->places = calloc((size_t) wanted + 1, sizeof(unsigned char));
    if (shadow->places == NULL) {
        free(shadow);
        return NULL;
    }

    allocatedGrew((size_t) wanted);
    shadow->room = wanted;
    return shadow;
}

void shadowRelease(Shadow *shadow) {
    if (shadow != NULL) {
        allocatedShrank((size_t) shadow->room);
        free(shadow->places);
        free(shadow);
    }
}

/**
 * Whether a shadow already in hand has room for one this size, which is the only thing that
 * decides whether it is used again. A shadow that is the wrong shape but large enough is reused,
 * and the bounds put in afterwards say what shape it now is.
 */
int shadowCanHold(const Shadow *shadow, int width, int height) {
    return shadow != NULL && shadow->room >= width * height;
}

void shadowClear(Shadow *shadow) {
    if (shadow != NULL) {
        memset(shadow->places, 0, (size_t) shadow->room);
    }
}

void shadowSetBounds(Shadow *shadow, int left, int top, int right, int bottom) {
    shadow->left = left;
    shadow->top = top;
    shadow->right = right;
    shadow->bottom = bottom;
    shadow->across = right - left;
    shadow->down = bottom - top;
}

int shadowLeft(const Shadow *shadow) {
    return shadow->left;
}

int shadowTop(const Shadow *shadow) {
    return shadow->top;
}

int shadowAcross(const Shadow *shadow) {
    return shadow->across;
}

int shadowDown(const Shadow *shadow) {
    return shadow->down;
}

const unsigned char *shadowPlaces(const Shadow *shadow) {
    return shadow->places;
}

int shadowRoom(const Shadow *shadow) {
    return shadow->room;
}

/**
 * One side of a triangle being walked down the rows, as a place and how far it moves each row.
 */
typedef struct {
    float x;
    float step;
} Edge;

static Edge edgeBetween(float from, float to, int rows) {
    float over = reciprocalOfFour(fmaxf((float) rows, LEAST));

    Edge edge;
    edge.x = from;
    edge.step = (to - from) * over;
    return edge;
}

/**
 * Marks one row of places between two sides.
 *
 * Where a run starts and ends is cut back to the place it is standing in rather than rounded to
 * the nearest, which is not what the toolkit does with a run of the picture. A shadow is filled
 * by a routine of its own that writes a byte a place and cuts back, and rounding it to the
 * nearest instead puts the edge of a shadow a place out.
 *
 * The run is brought inside the shadow before it is written. The toolkit this replaces leaves it
 * where it fell, which holds together only while the bounds the shadow was given cover every
 * corner of every face, and writes past the end of the shadow when they do not.
 */
static void markRow(Shadow *shadow, int row, const Edge *left, const Edge *right) {
    int from = (int) left->x;
    int to = (int) right->x;

    if (row < 0 || row > shadow->down || to <= from) {
        return;
    }

    if (from < 0) {
        from = 0;
    }
    if (to > shadow->across) {
        to = shadow->across;
    }

    if (to <= from) {
        return;
    }

    int at = row * shadow->across + from;
    if (at < 0 || at + (to - from) > shadow->room) {
        return;
    }

    memset(shadow->places + at, 1, (size_t) (to - from));
}

static void markHalf(Shadow *shadow, int row, int rows, Edge *left, Edge *right) {
    for (int done = 0; done < rows; done++) {
        markRow(shadow, row + done, left, right);
        left->x += left->step;
        right->x += right->step;
    }
}

/**
 * Marks the places a triangle of the flattened model stands over.
 *
 * The corners are put in order of the row they landed on and the triangle is cut in two at the
 * middle one, which is how every other triangle in this toolkit is filled. Which side is on the
 * left is settled by which of the two moves further right in a row.
 */
void shadowMarkTriangle(Shadow *shadow, int downA, int downB, int downC,
                        int acrossA, int acrossB, int acrossC) {
    float down[3] = {(float) downA, (float) downB, (float) downC};
    float across[3] = {(float) acrossA, (float) acrossB, (float) acrossC};

    int top;
    int middle;
    int bottom;

    if (down[1] > down[0]) {
        if (down[2] > down[1]) {
            top = 0; middle = 1; bottom = 2;
        } else if (down[2] > down[0]) {
            top = 0; middle = 2; bottom = 1;
        } else {
            top = 2; middle = 0; bottom = 1;
        }
    } else if (down[2] > down[0]) {
        top = 1; middle = 0; bottom = 2;
    } else if (down[2] > down[1]) {
        top = 1; middle = 2; bottom = 0;
    } else {
        top = 2; middle = 1; bottom = 0;
    }

    int topRow = (int) down[top];
    int middleRow = (int) down[middle];
    int bottomRow = (int) down[bottom];

    Edge toMiddle = edgeBetween(across[top], across[middle], middleRow - topRow);
    Edge toBottom = edgeBetween(across[top], across[bottom], bottomRow - topRow);
    Edge acrossBottom = edgeBetween(across[middle], across[bottom], bottomRow - middleRow);

    int row = topRow < 0 ? 0 : topRow;
    int skipped = row - topRow;
    int rows = middleRow - topRow - skipped;

    Edge left = toMiddle.step > toBottom.step ? toBottom : toMiddle;
    Edge right = toMiddle.step > toBottom.step ? toMiddle : toBottom;

    left.x += left.step * (float) skipped;
    right.x += right.step * (float) skipped;

    if (rows > 0) {
        markHalf(shadow, row, rows, &left, &right);
        row += rows;
        skipped = 0;
        rows = bottomRow - middleRow;
    } else {
        skipped = -rows;
        rows = bottomRow - middleRow + rows;
    }

    if (rows > 0) {
        if (toBottom.step > acrossBottom.step) {
            right = acrossBottom;
            right.x = across[middle] + right.step * (float) skipped;
        } else {
            left = acrossBottom;
            left.x = across[middle] + left.step * (float) skipped;
        }

        markHalf(shadow, row, rows, &left, &right);
    }
}

/**
 * Releases the shadow the client is holding.
 */
JNIEXPORT void JNICALL Java_ba_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    shadowRelease((Shadow *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);
}
