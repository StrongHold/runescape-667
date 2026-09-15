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

/** A corner of a triangle, with the colour the light gave it. */
typedef struct {
    int x;
    int y;
    float depth;
    float red;
    float green;
    float blue;
} Corner;

static Corner cornerAt(const Projected *point, uint32_t colour) {
    Corner corner;
    corner.x = point->x;
    corner.y = point->y;
    corner.depth = (float) point->depth;
    corner.red = (float) ((colour >> 16) & 0xFF);
    corner.green = (float) ((colour >> 8) & 0xFF);
    corner.blue = (float) (colour & 0xFF);
    return corner;
}

static Corner between(const Corner *from, const Corner *to, float howfar) {
    Corner corner;
    corner.x = (int) ((float) from->x + ((float) to->x - (float) from->x) * howfar);
    corner.y = 0;
    corner.depth = from->depth + (to->depth - from->depth) * howfar;
    corner.red = from->red + (to->red - from->red) * howfar;
    corner.green = from->green + (to->green - from->green) * howfar;
    corner.blue = from->blue + (to->blue - from->blue) * howfar;
    return corner;
}

/**
 * Fills the triangle between three corners, one row at a time, running the colour from each
 * corner into the next so that a curved surface made of flat faces does not look flat.
 */
static void fillTriangle(Corner top, Corner middle, Corner bottom) {
    Corner swap;

    if (top.y > middle.y) {
        swap = top; top = middle; middle = swap;
    }
    if (middle.y > bottom.y) {
        swap = middle; middle = bottom; bottom = swap;
    }
    if (top.y > middle.y) {
        swap = top; top = middle; middle = swap;
    }

    if (top.y == bottom.y) {
        return;
    }

    int first = top.y < raster.clipTop ? raster.clipTop : top.y;
    int last = bottom.y > raster.clipBottom ? raster.clipBottom : bottom.y;

    for (int y = first; y < last; y++) {
        Corner left = between(&top, &bottom, (float) (y - top.y) / (float) (bottom.y - top.y));
        Corner right;

        if (y < middle.y) {
            if (middle.y == top.y) {
                continue;
            }
            right = between(&top, &middle, (float) (y - top.y) / (float) (middle.y - top.y));
        } else {
            if (bottom.y == middle.y) {
                continue;
            }
            right = between(&middle, &bottom, (float) (y - middle.y) / (float) (bottom.y - middle.y));
        }

        if (left.x > right.x) {
            swap = left; left = right; right = swap;
        }

        int span = right.x - left.x;
        if (span <= 0) {
            continue;
        }

        int from = left.x < raster.clipLeft ? raster.clipLeft : left.x;
        int to = right.x > raster.clipRight ? raster.clipRight : right.x;

        uint32_t *row = raster.pixels + (size_t) y * (size_t) raster.width;
        float *depths = depthRow(y);

        for (int x = from; x < to; x++) {
            float across = (float) (x - left.x) / (float) span;
            float depth = left.depth + (right.depth - left.depth) * across;

            if (depth >= depths[x]) {
                continue;
            }

            depths[x] = depth;
            int red = (int) (left.red + (right.red - left.red) * across);
            int green = (int) (left.green + (right.green - left.green) * across);
            int blue = (int) (left.blue + (right.blue - left.blue) * across);
            row[x] = ((uint32_t) red << 16) | ((uint32_t) green << 8) | (uint32_t) blue;
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

    clearDepths();
    if (depths == NULL) {
        return;
    }

    qsort(order, (size_t) drawn, sizeof(Ordered), compareDepth);

    const Normal *normals = modelNormals(model);
    int ambient = modelAmbient(model);
    float strength = modelContrast(model) == 0 ? 1.0f : 768.0f / (float) modelContrast(model);

    for (int i = 0; i < drawn; i++) {
        int face = order[i].face;
        int hsl = faceColour == NULL ? 0 : faceColour[face] & 0xFFFF;
        uint32_t unlit = unlitColour(hsl, ambient);
        uint32_t shaded;
        uint32_t shadedB;
        uint32_t shadedC;

        if (normals == NULL) {
            shaded = shadedB = shadedC = unlit;
        } else if (modelFaceIsFlat(model, face)) {
            shaded = shadedB = shadedC =
                sunlitColour(unlit, &modelFaceNormals(model)[face], strength);
        } else {
            shaded = sunlitColour(unlit, &normals[faceA[face]], strength);
            shadedB = sunlitColour(unlit, &normals[faceB[face]], strength);
            shadedC = sunlitColour(unlit, &normals[faceC[face]], strength);
        }

        fillTriangle(cornerAt(&projected[faceA[face]], shaded),
                     cornerAt(&projected[faceB[face]], shadedB),
                     cornerAt(&projected[faceC[face]], shadedC));
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
