/*
 * Drawing a model.
 *
 * Every vertex is put through the model's matrix and then the camera's, which leaves it in front
 * of the eye, and divided by its distance to land on the buffer. Each face is then filled between
 * the three points its corners landed on.
 *
 * Faces are drawn furthest first, so a nearer face covers one behind it. The toolkit this
 * replaces sorts them more carefully than this, and this is where that difference will show.
 */

#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

/** A vertex after it has been projected. Behind the eye it has no place on the buffer. */
typedef struct {
    int x;
    int y;
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
 * Fills the triangle between three points, one row at a time, by walking each edge down.
 */
static void fillTriangle(const Projected *a, const Projected *b, const Projected *c,
                         uint32_t colour) {
    const Projected *top = a;
    const Projected *middle = b;
    const Projected *bottom = c;
    const Projected *swap;

    if (top->y > middle->y) {
        swap = top; top = middle; middle = swap;
    }
    if (middle->y > bottom->y) {
        swap = middle; middle = bottom; bottom = swap;
    }
    if (top->y > middle->y) {
        swap = top; top = middle; middle = swap;
    }

    if (top->y == bottom->y) {
        return;
    }

    int first = top->y < raster.clipTop ? raster.clipTop : top->y;
    int last = bottom->y > raster.clipBottom ? raster.clipBottom : bottom->y;

    for (int y = first; y < last; y++) {
        float longSide = (float) (y - top->y) / (float) (bottom->y - top->y);
        float left = (float) top->x + ((float) bottom->x - (float) top->x) * longSide;
        float right;

        if (y < middle->y) {
            if (middle->y == top->y) {
                continue;
            }
            float shortSide = (float) (y - top->y) / (float) (middle->y - top->y);
            right = (float) top->x + ((float) middle->x - (float) top->x) * shortSide;
        } else {
            if (bottom->y == middle->y) {
                continue;
            }
            float shortSide = (float) (y - middle->y) / (float) (bottom->y - middle->y);
            right = (float) middle->x + ((float) bottom->x - (float) middle->x) * shortSide;
        }

        int from = (int) (left < right ? left : right);
        int to = (int) (left < right ? right : left);

        if (from < raster.clipLeft) {
            from = raster.clipLeft;
        }
        if (to > raster.clipRight) {
            to = raster.clipRight;
        }

        uint32_t *row = raster.pixels + (size_t) y * (size_t) raster.width;
        for (int x = from; x < to; x++) {
            row[x] = colour;
        }
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
            landed->x = (int) (view->centreX + point[0] * view->scaleX / point[2]);
            landed->y = (int) (view->centreY + point[1] * view->scaleY / point[2]);
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

        if (a->visible && b->visible && c->visible) {
            order[drawn].face = face;
            order[drawn].depth = a->depth + b->depth + c->depth;
            drawn++;
        }
    }

    qsort(order, (size_t) drawn, sizeof(Ordered), compareDepth);

    for (int i = 0; i < drawn; i++) {
        int face = order[i].face;
        fillTriangle(&projected[faceA[face]], &projected[faceB[face]], &projected[faceC[face]],
                     colourOf(faceColour == NULL ? 0 : faceColour[face]));
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
