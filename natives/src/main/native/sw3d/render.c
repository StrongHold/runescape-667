/*
 * Drawing a model.
 *
 * Every vertex is put through the model's matrix and then the camera's, which leaves it in front
 * of the eye, and divided by its distance to land on the buffer. Each face is then filled between
 * the three points its corners landed on.
 *
 * The fill is split the way the toolkit splits it: a triangle is cut into the part above its
 * middle corner and the part below, each part walks its two sides a row at a time, and each row
 * hands a run of pixels to the span fill. Light runs from corner to corner as a whole number with
 * eight places after the point, and the amount it moves by is cut to a whole number before the
 * walk begins, so the light on a face steps rather than slides. Keeping that is the difference
 * between a picture that is nearly right and one that is the same.
 */

#include <math.h>
#include <stdlib.h>
#include <string.h>

#if defined(__SSE__) || defined(_M_X64)
#include <xmmintrin.h>
#endif

#include "sw3d.h"

/** A colour has a blue, a green, a red and an alpha part, in the order they sit in a pixel. */
enum { CHANNELS = 4 };

/** The smallest number of rows or pixels a side is allowed to be divided by. */
static const float LEAST = 1.0e-6f;

/**
 * The reciprocal a side is divided by.
 *
 * This is the processor's approximation rather than a true division. The light it scales is cut
 * to a whole number straight afterwards, so the approximation decides the answer often enough to
 * matter and has to be the same approximation.
 */
static float reciprocal(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rcp_ss(_mm_set_ss(value)));
#else
    return 1.0f / value;
#endif
}

/** Brings a whole number into the range a light step is held in, holding at the ends. */
static int16_t narrow(float value) {
    int whole = (int) value;

    if (whole < -32768) {
        return (int16_t) -32768;
    } else if (whole > 32767) {
        return (int16_t) 32767;
    } else {
        return (int16_t) whole;
    }
}

/** Brings a whole number into the range a light is held in, holding at black and at white. */
static uint16_t hold(float value) {
    int whole = (int) value;

    if (whole < 0) {
        return 0;
    } else if (whole > 65535) {
        return (uint16_t) 65535;
    } else {
        return (uint16_t) whole;
    }
}

/**
 * How far away each pixel drawn so far is, so that a nearer face covers one behind it wherever
 * they overlap rather than only when it happens to be drawn later.
 *
 * Sorting whole faces cannot answer this. Two faces that pass through each other, or three that
 * overlap in a ring, have no order that is right everywhere, and a model made of flat faces has
 * plenty of both.
 */
static float *depths;
static int depthRoom;
static int depthWidth;

static float *depthRow(int y) {
    return depths + (size_t) y * (size_t) depthWidth;
}

static void clearDepths(void) {
    int wanted = raster.width * raster.height;

    if (depthRoom < wanted) {
        float *grown = realloc(depths, (size_t) wanted * sizeof(float));
        if (grown == NULL) {
            return;
        }
        depths = grown;
        depthRoom = wanted;
    }

    depthWidth = raster.width;
    for (int i = 0; i < wanted; i++) {
        depths[i] = 3.4e38f;
    }
}

/** A vertex after it has been projected. Behind the eye it has no place on the buffer. */
typedef struct {
    float x;
    float y;
    int depth;
    int visible;
} Projected;

typedef struct {
    int face;
    int depth;
} Ordered;

static Projected *projected;
static int projectedRoom;

static Ordered *order;
static int orderRoom;

static int room(void **held, int *have, int want, size_t size) {
    if (*have >= want) {
        return 1;
    }

    void *grown = realloc(*held, (size_t) want * size);
    if (grown == NULL) {
        return 0;
    }

    *held = grown;
    *have = want;
    return 1;
}

static int compareDepth(const void *left, const void *right) {
    return ((const Ordered *) right)->depth - ((const Ordered *) left)->depth;
}

/**
 * A corner of a triangle: where it landed, how far away it is, and the light it was given.
 *
 * The light is held as four whole numbers with eight places after the point, one for each part of
 * a pixel, which is the room the walk down a side needs to move a part of a colour by less than a
 * whole step without ever leaving whole numbers.
 */
typedef struct {
    float x;
    float y;
    float depth;
    uint16_t colour[CHANNELS];
} Corner;

/** One side of a triangle, either where it has reached or how far it moves in a row. */
typedef struct {
    float x;
    float depth;
    int16_t colour[CHANNELS];
} Side;

static Corner cornerAt(const Projected *point, uint32_t colour) {
    Corner corner;
    corner.x = point->x;
    corner.y = point->y;
    corner.depth = (float) point->depth;

    for (int part = 0; part < CHANNELS; part++) {
        corner.colour[part] = (uint16_t) ((colour >> (part * 8) & 0xFF) << 8);
    }

    return corner;
}

/**
 * How far a side moves in one row.
 *
 * The rows are counted between the rows the two corners landed on, and the light's part of the
 * answer is cut to a whole number, so a side that climbs by less than one eight-hundredth of a
 * part per row does not climb at all.
 */
static Side sideBetween(const Corner *from, const Corner *to, int rows) {
    float over = reciprocal(fmaxf((float) rows, LEAST));

    Side side;
    side.x = (to->x - from->x) * over;
    side.depth = (to->depth - from->depth) * over;

    for (int part = 0; part < CHANNELS; part++) {
        side.colour[part] = narrow(((float) to->colour[part] - (float) from->colour[part]) * over);
    }

    return side;
}

static Side sideAt(const Corner *corner) {
    Side side;
    side.x = corner->x;
    side.depth = corner->depth;

    for (int part = 0; part < CHANNELS; part++) {
        side.colour[part] = (int16_t) corner->colour[part];
    }

    return side;
}

/** Moves a side on by a whole number of rows at once, which is how a clipped side starts. */
static void carry(Side *side, const Side *step, int rows) {
    side->x += (float) rows * step->x;
    side->depth += (float) rows * step->depth;

    for (int part = 0; part < CHANNELS; part++) {
        side->colour[part] = (int16_t) (side->colour[part] + (int16_t) rows * step->colour[part]);
    }
}

static void advance(Side *side, const Side *step) {
    side->x += step->x;
    side->depth += step->depth;

    for (int part = 0; part < CHANNELS; part++) {
        side->colour[part] = (int16_t) (side->colour[part] + step->colour[part]);
    }
}

/**
 * Fills one run of pixels between two sides.
 *
 * A pixel is covered when the left and right ends are put on the nearest whole pixel and it falls
 * between them, and it is drawn when nothing already drawn there is nearer. The light moves by a
 * whole number of eight-hundredths per pixel, worked out from the ends before either was brought
 * inside the buffer, so clipping a run does not change the light along the part that is left.
 */
static void fillSpan(int y, const Side *left, const Side *right) {
    int from = (int) lrintf(left->x);
    int to = (int) lrintf(right->x);
    float over = reciprocal((float) (to - from));

    if (to > raster.clipRight) {
        to = raster.clipRight;
    }

    int skipped = 0;
    if (from < raster.clipLeft) {
        skipped = raster.clipLeft - from;
        from = raster.clipLeft;
    }

    if (to <= from) {
        return;
    }

    float depthStep = (right->depth - left->depth) * over;
    float depth = left->depth + (float) skipped * depthStep;

    uint16_t colour[CHANNELS];
    int16_t colourStep[CHANNELS];

    for (int part = 0; part < CHANNELS; part++) {
        float each = ((float) (uint16_t) right->colour[part]
                      - (float) (uint16_t) left->colour[part]) * over;
        colour[part] = hold((float) (uint16_t) left->colour[part] + (float) skipped * each);
        colourStep[part] = narrow(each);
    }

    uint32_t *row = raster.pixels + (size_t) y * (size_t) raster.width;
    float *held = depthRow(y);

    for (int x = from; x < to; x++) {
        if (depth <= held[x]) {
            held[x] = depth;
            row[x] = (uint32_t) (colour[0] >> 8)
                | (uint32_t) (colour[1] >> 8) << 8
                | (uint32_t) (colour[2] >> 8) << 16
                | (uint32_t) (colour[3] >> 8) << 24;
        }

        depth += depthStep;
        for (int part = 0; part < CHANNELS; part++) {
            colour[part] = (uint16_t) (colour[part] + colourStep[part]);
        }
    }
}

/** Fills the rows between two sides, which is half a triangle. */
static void fillHalf(int row, int rows, Side left, Side right, const Side *leftStep,
                     const Side *rightStep) {
    for (int done = 0; done < rows; done++) {
        fillSpan(row + done, &left, &right);
        advance(&left, leftStep);
        advance(&right, rightStep);
    }
}

/**
 * Fills the triangle between three corners.
 *
 * The corners are put in order of the row they landed on, and the triangle is cut in two at the
 * middle one. The side that runs the whole height is one edge of both halves, and which of the two
 * is on the left is settled once for each half by which of them moves further to the right in a
 * row, rather than by comparing where they are on every row.
 */
static void fillTriangle(Corner a, Corner b, Corner c) {
    const Corner *top;
    const Corner *middle;
    const Corner *bottom;

    if (b.y > a.y) {
        if (c.y > b.y) {
            top = &a; middle = &b; bottom = &c;
        } else if (c.y > a.y) {
            top = &a; middle = &c; bottom = &b;
        } else {
            top = &c; middle = &a; bottom = &b;
        }
    } else if (c.y > a.y) {
        top = &b; middle = &a; bottom = &c;
    } else if (c.y > b.y) {
        top = &b; middle = &c; bottom = &a;
    } else {
        top = &c; middle = &b; bottom = &a;
    }

    int topRow = (int) top->y;
    int middleRow = (int) middle->y;
    int bottomRow = (int) bottom->y;

    Side toMiddle = sideBetween(top, middle, middleRow - topRow);
    Side toBottom = sideBetween(top, bottom, bottomRow - topRow);
    Side acrossBottom = sideBetween(middle, bottom, bottomRow - middleRow);

    int row = topRow < raster.clipTop ? raster.clipTop : topRow;
    int skipped = row - topRow;
    int rows = middleRow - topRow - skipped;

    if (raster.clipBottom - row < rows) {
        rows = raster.clipBottom - row;
    }

    Side leftStep = toMiddle.x > toBottom.x ? toBottom : toMiddle;
    Side rightStep = toMiddle.x > toBottom.x ? toMiddle : toBottom;
    Side left = sideAt(top);
    Side right = left;

    carry(&left, &leftStep, skipped);
    carry(&right, &rightStep, skipped);

    if (rows > 0) {
        fillHalf(row, rows, left, right, &leftStep, &rightStep);
        carry(&left, &leftStep, rows);
        carry(&right, &rightStep, rows);
        row += rows;
        skipped = 0;
        rows = bottomRow - middleRow;
    } else {
        skipped = -rows;
        rows = bottomRow - middleRow + rows;
    }

    if (raster.clipBottom - row < rows) {
        rows = raster.clipBottom - row;
    }

    if (rows > 0) {
        if (toBottom.x > acrossBottom.x) {
            rightStep = acrossBottom;
            right = sideAt(middle);
            carry(&right, &rightStep, skipped);
        } else {
            leftStep = acrossBottom;
            left = sideAt(middle);
            carry(&left, &leftStep, skipped);
        }

        fillHalf(row, rows, left, right, &leftStep, &rightStep);
    }
}

/**
 * Draws one model through one matrix.
 */
static void renderModel(const void *model, const void *matrix) {
    if (model == NULL || matrix == NULL || raster.pixels == NULL) {
        return;
    }

    int vertices = modelVertexCount(model);
    int faces = modelFaceCount(model);
    if (vertices <= 0 || faces <= 0) {
        return;
    }

    if (!room((void **) &projected, &projectedRoom, vertices, sizeof(Projected))
        || !room((void **) &order, &orderRoom, faces, sizeof(Ordered))) {
        return;
    }

    const Projection *view = projection();
    const void *camera = cameraMatrix();

    void *combined = malloc(matrixSize());
    if (combined == NULL) {
        return;
    }

    if (camera == NULL) {
        memcpy(combined, matrix, matrixSize());
    } else {
        matrixCompose(matrix, camera, combined);
    }

    const int *vertexX = modelVertexX(model);
    const int *vertexY = modelVertexY(model);
    const int *vertexZ = modelVertexZ(model);

    for (int vertex = 0; vertex < vertices; vertex++) {
        float point[4];
        matrixTransform(combined, (float) vertexX[vertex], (float) vertexY[vertex],
                        (float) vertexZ[vertex], point);

        Projected *landed = &projected[vertex];
        landed->depth = (int) point[2];
        landed->visible = point[2] >= (float) view->near && point[2] <= (float) view->far;

        if (landed->visible) {
            landed->x = view->centreX + point[0] * view->scaleX / point[2];
            landed->y = view->centreY + point[1] * view->scaleY / point[2];
        }
    }

    free(combined);

    const short *faceA = modelFaceA(model);
    const short *faceB = modelFaceB(model);
    const short *faceC = modelFaceC(model);
    const short *faceColour = modelFaceColour(model);

    int drawn = 0;
    for (int face = 0; face < faces; face++) {
        const Projected *a = &projected[faceA[face]];
        const Projected *b = &projected[faceB[face]];
        const Projected *c = &projected[faceC[face]];

        if (!a->visible || !b->visible || !c->visible) {
            continue;
        }

        /*
         * A face turned away from the eye is inside the model and is not drawn. Which way round
         * that is comes from the order its corners were given in, so the test is the sign of the
         * area the three landed points enclose.
         */
        float area = (b->x - a->x) * (c->y - a->y) - (c->x - a->x) * (b->y - a->y);
        if (area >= 0.0f) {
            continue;
        }

        order[drawn].face = face;
        order[drawn].depth = a->depth + b->depth + c->depth;
        drawn++;
    }

    clearDepths();
    if (depths == NULL) {
        return;
    }

    qsort(order, (size_t) drawn, sizeof(Ordered), compareDepth);

    const uint32_t *shade = modelShade(model);

    for (int i = 0; i < drawn; i++) {
        int face = order[i].face;
        uint32_t unlit = shade == NULL
            ? unlitColour(faceColour == NULL ? 0 : faceColour[face] & 0xFFFF, modelAmbient(model))
            : 0;

        fillTriangle(
            cornerAt(&projected[faceA[face]], shade == NULL ? unlit : shade[face * 3]),
            cornerAt(&projected[faceB[face]], shade == NULL ? unlit : shade[face * 3 + 1]),
            cornerAt(&projected[faceC[face]], shade == NULL ? unlit : shade[face * 3 + 2]));
    }
}

JNIEXPORT void JNICALL Java_a_UA(JNIEnv *env, jobject self, jlong worker, jlong model,
                                  jlong matrix, jintArray cylinder, jint flags) {
    (void) env;
    (void) self;
    (void) worker;
    (void) cylinder;
    (void) flags;

    renderModel((const void *) (intptr_t) model, (const void *) (intptr_t) matrix);
}
