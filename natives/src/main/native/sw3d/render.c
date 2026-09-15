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

/** A vertex after it has been projected. Behind the eye it has no place on the buffer. */
typedef struct {
    float x;
    float y;
    float depth;
    int visible;
} Projected;

static Projected *projected;
static int projectedRoom;

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
    corner.depth = point->depth;

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
    int width = raster.clipRight - raster.clipLeft;

    if (to > width) {
        to = width;
    }

    int skipped = 0;
    if (from < 0) {
        skipped = -from;
        from = 0;
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

    size_t start = (size_t) (y + raster.clipTop) * (size_t) raster.width
        + (size_t) raster.clipLeft;
    uint32_t *row = raster.pixels + start;
    float *held = raster.depths + start;

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

    int height = raster.clipBottom - raster.clipTop;
    int row = topRow < 0 ? 0 : topRow;
    int skipped = row - topRow;
    int rows = middleRow - topRow - skipped;

    if (height - row < rows) {
        rows = height - row;
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

    if (height - row < rows) {
        rows = height - row;
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

enum { ROWS = 4 };

typedef struct {
    float row[ROWS][ROWS];
} Transform;

/**
 * The matrix a model is put through once the camera has had it.
 *
 * It opens the picture out to the field the client asked for and leaves the distance from the eye
 * in the fourth place, so that dividing a point by that place is what makes a thing further away
 * smaller. The third place is left running from nothing at the near plane to one at the far
 * plane, which is the range distances are kept in.
 *
 * Folding the field into the matrix is not the same as opening the picture out afterwards. The
 * field then multiplies each term of the sum rather than the sum, and the two answers differ by
 * enough to move the edge of a face onto the next pixel.
 */
static Transform projectionMatrix(void) {
    const Projection *view = projection();
    float range = view->far - view->near;

    Transform matrix;
    memset(&matrix, 0, sizeof matrix);
    matrix.row[0][0] = view->scaleX;
    matrix.row[1][1] = view->scaleY;
    matrix.row[2][2] = view->far / range;
    matrix.row[2][3] = 1.0f;
    matrix.row[3][2] = -view->near * view->far / range;
    return matrix;
}

/**
 * Puts one matrix after another, adding the four terms of a row in pairs.
 *
 * Four numbers added in a different order are a different number as soon as they stop fitting
 * exactly, and this answer decides which pixel the edge of a face lands on, so the pairs are kept.
 */
static Transform after(const float *first, const Transform *second) {
    Transform result;

    for (int row = 0; row < ROWS; row++) {
        const float *terms = first + row * ROWS;

        for (int lane = 0; lane < ROWS; lane++) {
            float even = terms[0] * second->row[0][lane] + terms[2] * second->row[2][lane];
            float odd = terms[1] * second->row[1][lane] + terms[3] * second->row[3][lane];
            result.row[row][lane] = even + odd;
        }
    }

    return result;
}

/**
 * Keeps the sign a distance had before it was divided.
 *
 * Dividing by a distance behind the eye turns the sign of the answer round. The toolkit puts the
 * sign back rather than letting a point behind the eye come out in front of one in front of it.
 */
static float signedAs(float value, float before) {
    return before < 0.0f ? -fabsf(value) : value;
}

/**
 * Draws one model through one matrix.
 */
static void renderModel(const void *model, const void *matrix) {
    if (model == NULL || matrix == NULL || raster.pixels == NULL || raster.depths == NULL) {
        return;
    }

    int vertices = modelVertexCount(model);
    int faces = modelFaceCount(model);
    if (vertices <= 0 || faces <= 0) {
        return;
    }

    if (!room((void **) &projected, &projectedRoom, vertices, sizeof(Projected))) {
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

    Transform projector = projectionMatrix();
    Transform onto = after(matrixRows(combined), &projector);
    free(combined);

    /* Where the middle of the picture sits, counted from the corner that may be drawn on. */
    float acrossFromClip = view->centreX - (float) raster.clipLeft;
    float downFromClip = view->centreY - (float) raster.clipTop;

    const int *vertexX = modelVertexX(model);
    const int *vertexY = modelVertexY(model);
    const int *vertexZ = modelVertexZ(model);

    for (int vertex = 0; vertex < vertices; vertex++) {
        float x = (float) vertexX[vertex];
        float y = (float) vertexY[vertex];
        float z = (float) vertexZ[vertex];

        float point[ROWS];
        for (int lane = 0; lane < ROWS; lane++) {
            point[lane] = x * onto.row[0][lane] + y * onto.row[1][lane]
                + z * onto.row[2][lane] + onto.row[3][lane];
        }

        /* The fourth place is how far from the eye the point ended up. */
        float away = point[3];

        Projected *landed = &projected[vertex];
        landed->depth = signedAs(point[2] / away, point[2]);
        landed->visible = away >= view->near && away <= view->far;

        if (landed->visible) {
            landed->x = point[0] / away + acrossFromClip;
            landed->y = point[1] / away + downFromClip;
        }
    }

    const short *faceA = modelFaceA(model);
    const short *faceB = modelFaceB(model);
    const short *faceC = modelFaceC(model);
    const short *faceColour = modelFaceColour(model);

    const uint32_t *shade = modelShade(model);

    /*
     * The faces are drawn in the order the model lists them. Nothing sorts them: what covers what
     * is settled a pixel at a time by how far away each one is, and two faces that meet exactly
     * are settled by which of them the model lists second. Sorting them first would change that
     * answer wherever they meet, which on a model whose faces line up with an axis is a great
     * many pixels.
     */
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

        uint32_t unlit = shade == NULL
            ? unlitColour(faceColour == NULL ? 0 : faceColour[face] & 0xFFFF, modelAmbient(model))
            : 0;

        fillTriangle(
            cornerAt(a, shade == NULL ? unlit : shade[face * 3]),
            cornerAt(b, shade == NULL ? unlit : shade[face * 3 + 1]),
            cornerAt(c, shade == NULL ? unlit : shade[face * 3 + 2]));
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
