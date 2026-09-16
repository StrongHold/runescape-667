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

/**
 * The same reciprocal, taken four at a time.
 *
 * The toolkit divides a whole side at once and a row one value at a time, and the two ways of
 * asking the processor for an approximate reciprocal need not answer alike, so each is asked the
 * way the toolkit asks it.
 */
static float reciprocalOfFour(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rcp_ps(_mm_set1_ps(value)));
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
    float over = reciprocalOfFour(fmaxf((float) rows, LEAST));

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

/**
 * Fills the rows between two sides, which is half a triangle.
 *
 * The sides are left where the walk ended rather than being put back, because the second half of
 * a triangle carries on down one of them. Where it ended is not where multiplying the step by the
 * number of rows would put it, and the second half starts from where the walk ended.
 */
static void fillHalf(int row, int rows, Side *left, Side *right, const Side *leftStep,
                     const Side *rightStep) {
    for (int done = 0; done < rows; done++) {
        fillSpan(row + done, left, right);
        advance(left, leftStep);
        advance(right, rightStep);
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
        fillHalf(row, rows, &left, &right, &leftStep, &rightStep);
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

        fillHalf(row, rows, &left, &right, &leftStep, &rightStep);
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
static void renderModel(void *model, const void *matrix) {
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

    const float *held = modelVertices(model);

    for (int vertex = 0; vertex < vertices; vertex++) {
        float x = held[(size_t) vertex * MODEL_VERTEX_STRIDE];
        float y = held[(size_t) vertex * MODEL_VERTEX_STRIDE + 1];
        float z = held[(size_t) vertex * MODEL_VERTEX_STRIDE + 2];

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

/**
 * Draws one tile of the ground.
 *
 * A tile's corners are already where they belong in the world, so they go through the camera and
 * the projection and nothing else. Each face is filled between its three corners the same way a
 * model's face is, because that is what the toolkit fills it with.
 */
void renderGroundTile(const void *ground, int x, int z) {
    int corners = 0;
    const void *tile = groundTile(ground, x, z, &corners);
    const void *camera = cameraMatrix();

    if (tile == NULL || camera == NULL || raster.pixels == NULL || raster.depths == NULL) {
        return;
    }

    if (!room((void **) &projected, &projectedRoom, corners, sizeof(Projected))) {
        return;
    }

    Transform projector = projectionMatrix();
    Transform onto = after(matrixRows(camera), &projector);

    const Projection *view = projection();
    float acrossFromClip = view->centreX - (float) raster.clipLeft;
    float downFromClip = view->centreY - (float) raster.clipTop;
    int tileSize = groundTileSize(ground);

    uint32_t *shade = calloc((size_t) corners, sizeof(uint32_t));
    if (shade == NULL) {
        return;
    }

    for (int corner = 0; corner < corners; corner++) {
        int where[3];
        groundTileCorner(ground, tile, corner, tileSize, x, z, where, &shade[corner]);

        float point[ROWS];
        for (int lane = 0; lane < ROWS; lane++) {
            point[lane] = (float) where[0] * onto.row[0][lane]
                + (float) where[1] * onto.row[1][lane]
                + (float) where[2] * onto.row[2][lane] + onto.row[3][lane];
        }

        float away = point[3];
        Projected *landed = &projected[corner];
        landed->depth = signedAs(point[2] / away, point[2]);
        landed->visible = away >= view->near && away <= view->far;

        if (landed->visible) {
            landed->x = point[0] / away + acrossFromClip;
            landed->y = point[1] / away + downFromClip;
        }
    }

    for (int face = 0; face * 3 + 2 < corners; face++) {
        const Projected *a = &projected[face * 3];
        const Projected *b = &projected[face * 3 + 1];
        const Projected *c = &projected[face * 3 + 2];

        if (!a->visible || !b->visible || !c->visible) {
            continue;
        }

        fillTriangle(cornerAt(a, shade[face * 3]),
                     cornerAt(b, shade[face * 3 + 1]),
                     cornerAt(c, shade[face * 3 + 2]));
    }

    free(shade);
}

JNIEXPORT void JNICALL Java_a_UA(JNIEnv *env, jobject self, jlong worker, jlong model,
                                  jlong matrix, jintArray cylinder, jint flags) {
    (void) env;
    (void) self;
    (void) worker;
    (void) cylinder;
    (void) flags;

    renderModel((void *) (intptr_t) model, (const void *) (intptr_t) matrix);
}

/**
 * Draws one tile of the ground, every depth of it.
 */
JNIEXPORT void JNICALL Java_a_H(JNIEnv *env, jobject self, jlong worker, jlong ground,
                                 jint x, jint z) {
    (void) env;
    (void) self;
    (void) worker;

    renderGroundTile((const void *) (intptr_t) ground, x, z);
}

/**
 * Draws one depth of one tile of the ground. Nothing here keeps its faces apart by depth yet, so
 * this draws the whole tile.
 */
JNIEXPORT void JNICALL Java_a_Z(JNIEnv *env, jobject self, jlong worker, jlong ground,
                                 jint x, jint z, jint depth) {
    (void) env;
    (void) self;
    (void) worker;
    (void) depth;

    renderGroundTile((const void *) (intptr_t) ground, x, z);
}

/*
 * Finding out whether a point on the screen lands on a model.
 *
 * The client asks this of every thing in the world under the mouse, several times a frame, so it
 * is two tests rather than one. The first projects the eight corners of the box the model sits in
 * and asks whether the point is inside what they cover. Only if it is, and only if the client
 * asked for more than a guess, is every face projected and asked in turn.
 *
 * A face is asked the same crude question as the box: whether the point is inside the rectangle
 * the face's three corners cover. That is not the same as being inside the face, so a point in
 * the corner of a long thin triangle picks it. That is what the toolkit does.
 */

/** How far out the box starts before any corner has been looked at. */
static const float OUTSIDE_EVERYTHING = 100000.0f;

enum { BOX_CORNERS = 8 };

/**
 * The matrix a model is put through when the client wants it flat rather than in perspective.
 *
 * Distance no longer makes a thing smaller, so the field is divided by the zoom the client asks
 * for instead, and the fourth place comes out as one for every point.
 */
static Transform flatMatrix(int zoom) {
    const Projection *view = projection();
    float range = view->far - view->near;

    Transform matrix;
    memset(&matrix, 0, sizeof matrix);
    matrix.row[0][0] = view->scaleX / (float) zoom;
    matrix.row[1][1] = view->scaleY / (float) zoom;
    matrix.row[2][2] = 1.0f / range;
    matrix.row[3][2] = -view->near / range;
    matrix.row[3][3] = 1.0f;
    return matrix;
}

/**
 * Where one point of the model lands, as three places across, down and away from the eye.
 */
static void placeOnScreen(const Transform *onto, const Projection *view,
                          float x, float y, float z, float *into) {
    float point[ROWS];
    for (int lane = 0; lane < ROWS; lane++) {
        point[lane] = x * onto->row[0][lane] + y * onto->row[1][lane]
            + z * onto->row[2][lane] + onto->row[3][lane];
    }

    float away = point[3];
    into[0] = point[0] / away + view->centreX;
    into[1] = point[1] / away + view->centreY;
    into[2] = signedAs(point[2] / away, point[2]);
}

/** Whether all three of a face's corners sit past the point along one axis. */
static int allPast(float first, float second, float third, float mark) {
    return first > mark && second > mark && third > mark;
}

/** Whether all three of a face's corners sit short of the point along one axis. */
static int allShort(float first, float second, float third, float mark) {
    return mark > first && mark > second && mark > third;
}

/**
 * The matrix everything in the model is put through, which is the one the client gave, then the
 * camera, then the opening out of the picture.
 */
static int lookThrough(const void *matrix, int zoom, Transform *into) {
    const void *camera = cameraMatrix();

    void *combined = malloc(matrixSize());
    if (combined == NULL) {
        return 0;
    }

    if (camera == NULL) {
        memcpy(combined, matrix, matrixSize());
    } else {
        matrixCompose(matrix, camera, combined);
    }

    Transform projector = zoom < 0 ? projectionMatrix() : flatMatrix(zoom);
    *into = after(matrixRows(combined), &projector);
    free(combined);
    return 1;
}

/**
 * Whether the point is inside what the eight corners of the model's box cover.
 *
 * A corner behind the near plane is left out. If every one of them is, the model is behind the
 * eye and nothing is picked.
 */
static int insideTheBox(void *model, const Transform *onto, const Projection *view,
                        int x, int y) {
    int bounds[6];
    modelBounds(model, bounds);

    float least[3] = {(float) bounds[0], (float) bounds[2], (float) bounds[4]};
    float most[3] = {(float) bounds[1], (float) bounds[3], (float) bounds[5]};

    float leastAcross = OUTSIDE_EVERYTHING;
    float mostAcross = -OUTSIDE_EVERYTHING;
    float leastDown = OUTSIDE_EVERYTHING;
    float mostDown = -OUTSIDE_EVERYTHING;
    int anyInFront = 0;

    for (int corner = 0; corner < BOX_CORNERS; corner++) {
        float landed[3];
        placeOnScreen(onto, view,
            (corner & 0x1) == 0 ? least[0] : most[0],
            (corner & 0x2) == 0 ? least[1] : most[1],
            (corner & 0x4) == 0 ? least[2] : most[2],
            landed);

        if (landed[2] < 0.0f) {
            continue;
        }

        anyInFront = 1;
        leastAcross = fminf(landed[0], leastAcross);
        mostAcross = fmaxf(landed[0], mostAcross);
        leastDown = fminf(landed[1], leastDown);
        mostDown = fmaxf(landed[1], mostDown);
    }

    if (!anyInFront) {
        return 0;
    }

    float across = (float) x;
    float down = (float) y;

    return across > leastAcross && mostAcross > across
        && down > leastDown && mostDown > down;
}

/**
 * Whether any face of the model covers the point.
 */
static int onAnyFace(void *model, const Transform *onto, const Projection *view, int x, int y) {
    int vertices = modelVertexCount(model);
    int faces = modelFaceCount(model);

    float *landed = calloc((size_t) vertices * 3, sizeof(float));
    if (landed == NULL) {
        return 0;
    }

    const float *held = modelVertices(model);
    for (int vertex = 0; vertex < vertices; vertex++) {
        placeOnScreen(onto, view,
            held[(size_t) vertex * MODEL_VERTEX_STRIDE],
            held[(size_t) vertex * MODEL_VERTEX_STRIDE + 1],
            held[(size_t) vertex * MODEL_VERTEX_STRIDE + 2],
            landed + (size_t) vertex * 3);
    }

    const short *faceA = modelFaceA(model);
    const short *faceB = modelFaceB(model);
    const short *faceC = modelFaceC(model);

    float across = (float) x;
    float down = (float) y;
    int found = 0;

    for (int face = 0; face < faces && !found; face++) {
        const float *a = landed + (size_t) faceA[face] * 3;
        const float *b = landed + (size_t) faceB[face] * 3;
        const float *c = landed + (size_t) faceC[face] * 3;

        /*
         * The third corner is not asked whether it is in front of the eye. Only the first two
         * are, which is what the toolkit does.
         */
        if (a[2] < 0.0f || b[2] < 0.0f) {
            continue;
        }

        found = !allPast(a[1], b[1], c[1], down)
            && !allShort(a[1], b[1], c[1], down)
            && !allPast(a[0], b[0], c[0], across)
            && !allShort(a[0], b[0], c[0], across);
    }

    free(landed);
    return found;
}

/**
 * Whether a point on the screen lands on the model.
 */
static int pointOnModel(void *model, const void *matrix, int x, int y, int quick, int zoom) {
    if (model == NULL || matrix == NULL || modelFaceCount(model) == 0) {
        return 0;
    }

    Transform onto;
    if (!lookThrough(matrix, zoom, &onto)) {
        return 0;
    }

    const Projection *view = projection();
    if (!insideTheBox(model, &onto, view, x, y)) {
        return 0;
    }

    return quick || onAnyFace(model, &onto, view, x, y);
}

/**
 * Whether a point on the screen lands on the model, seen in perspective.
 */
JNIEXPORT jboolean JNICALL Java_a_R(JNIEnv *env, jobject self, jlong worker, jlong model,
                                     jint x, jint y, jlong matrix, jboolean quick) {
    (void) env;
    (void) self;
    (void) worker;

    return pointOnModel((void *) (intptr_t) model, (const void *) (intptr_t) matrix,
        x, y, quick == JNI_TRUE, -1) ? JNI_TRUE : JNI_FALSE;
}

/**
 * Whether a point on the screen lands on the model, seen flat at the zoom the client gives.
 */
JNIEXPORT jboolean JNICALL Java_a_n(JNIEnv *env, jobject self, jlong worker, jlong model,
                                     jint x, jint y, jlong matrix, jboolean quick, jint zoom) {
    (void) env;
    (void) self;
    (void) worker;

    return pointOnModel((void *) (intptr_t) model, (const void *) (intptr_t) matrix,
        x, y, quick == JNI_TRUE, zoom) ? JNI_TRUE : JNI_FALSE;
}
