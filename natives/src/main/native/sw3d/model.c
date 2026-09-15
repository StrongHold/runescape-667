/*
 * Models, and putting their faces on the buffer.
 *
 * A model is uploaded once, as a copy of every array the client's mesh holds, and then drawn many
 * times through whatever matrix it is handed. The upload works out the bounds as it goes, because
 * the client asks for them and because the renderer needs the cylinder radius to decide whether a
 * model is worth projecting at all.
 */

#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

typedef struct {
    int vertexCount;
    int faceCount;

    int *vertexX;
    int *vertexY;
    int *vertexZ;

    short *faceA;
    short *faceB;
    short *faceC;
    short *faceColour;
    signed char *faceAlpha;
    signed char *shadingType;

    int minX;
    int maxX;
    int minY;
    int maxY;
    int minZ;
    int maxZ;
    int radiusCylinder;
    int radiusSphere;

    int ambient;
    int contrast;

    Normal *normals;
    Normal *faceNormals;

    /** The colour each corner of each face takes, worked out once when the model is built. */
    uint32_t *shade;
} Model;

static Model *modelOf(JNIEnv *env, jobject self) {
    return (Model *) (intptr_t) nativeIdOf(env, self);
}

static int *copyInts(JNIEnv *env, jintArray source, int count) {
    if (source == NULL || count <= 0) {
        return NULL;
    }

    int *copy = calloc((size_t) count, sizeof(int));
    if (copy != NULL) {
        (*env)->GetIntArrayRegion(env, source, 0, count, (jint *) copy);
    }
    return copy;
}

static short *copyShorts(JNIEnv *env, jshortArray source, int count) {
    if (source == NULL || count <= 0) {
        return NULL;
    }

    short *copy = calloc((size_t) count, sizeof(short));
    if (copy != NULL) {
        (*env)->GetShortArrayRegion(env, source, 0, count, (jshort *) copy);
    }
    return copy;
}

static signed char *copyBytes(JNIEnv *env, jbyteArray source, int count) {
    if (source == NULL || count <= 0) {
        return NULL;
    }

    signed char *copy = calloc((size_t) count, sizeof(signed char));
    if (copy != NULL) {
        (*env)->GetByteArrayRegion(env, source, 0, count, (jbyte *) copy);
    }
    return copy;
}

/**
 * The smallest box the model sits in, and the two radii the renderer uses to decide whether it is
 * worth looking at. The cylinder ignores height, because the client turns models about the
 * upright axis and a radius that ignores height does not change when it does.
 */
static void measure(Model *model) {
    model->minX = model->minY = model->minZ = 0;
    model->maxX = model->maxY = model->maxZ = 0;

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        int x = model->vertexX[vertex];
        int y = model->vertexY[vertex];
        int z = model->vertexZ[vertex];

        if (x < model->minX) {
            model->minX = x;
        }
        if (x > model->maxX) {
            model->maxX = x;
        }
        if (y < model->minY) {
            model->minY = y;
        }
        if (y > model->maxY) {
            model->maxY = y;
        }
        if (z < model->minZ) {
            model->minZ = z;
        }
        if (z > model->maxZ) {
            model->maxZ = z;
        }
    }

    double flat = 0.0;
    double solid = 0.0;

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        double x = model->vertexX[vertex];
        double y = model->vertexY[vertex];
        double z = model->vertexZ[vertex];

        double across = x * x + z * z;
        if (across > flat) {
            flat = across;
        }
        if (across + y * y > solid) {
            solid = across + y * y;
        }
    }

    model->radiusCylinder = (int) ceil(sqrt(flat));
    model->radiusSphere = (int) ceil(sqrt(solid));
}

/**
 * The direction each vertex faces, taken as the sum of the unit normals of the faces meeting
 * there. The count is kept alongside, because the sum is divided by it rather than normalised
 * again.
 */
static void calculateNormals(Model *model) {
    model->normals = calloc((size_t) model->vertexCount, sizeof(Normal));
    model->faceNormals = calloc((size_t) model->faceCount, sizeof(Normal));
    if (model->normals == NULL || model->faceNormals == NULL) {
        return;
    }

    for (int face = 0; face < model->faceCount; face++) {
        int a = model->faceA[face];
        int b = model->faceB[face];
        int c = model->faceC[face];

        float abx = (float) (model->vertexX[b] - model->vertexX[a]);
        float aby = (float) (model->vertexY[b] - model->vertexY[a]);
        float abz = (float) (model->vertexZ[b] - model->vertexZ[a]);
        float acx = (float) (model->vertexX[c] - model->vertexX[a]);
        float acy = (float) (model->vertexY[c] - model->vertexY[a]);
        float acz = (float) (model->vertexZ[c] - model->vertexZ[a]);

        float nx = aby * acz - abz * acy;
        float ny = abz * acx - abx * acz;
        float nz = abx * acy - aby * acx;

        float length = sqrtf(nx * nx + ny * ny + nz * nz);
        if (length == 0.0f) {
            continue;
        }

        float scale = 1.0f / length;
        nx *= scale;
        ny *= scale;
        nz *= scale;

        /*
         * A face that asked to be flat keeps its own direction. Only the faces that asked to be
         * smoothed hand their direction to the corners they meet at, and a face given a corner's
         * averaged direction where it wanted its own comes out with almost no shading at all.
         */
        if (model->shadingType != NULL && model->shadingType[face] != 0) {
            Normal *normal = &model->faceNormals[face];
            normal->x = nx;
            normal->y = ny;
            normal->z = nz;
            normal->magnitude = 1.0f;
            continue;
        }

        int corners[3] = {a, b, c};
        for (int corner = 0; corner < 3; corner++) {
            Normal *normal = &model->normals[corners[corner]];
            normal->x += nx;
            normal->y += ny;
            normal->z += nz;
            normal->magnitude += 1.0f;
        }
    }
}

/**
 * Works out what colour each corner of each face takes.
 *
 * This happens when the model is built rather than when it is drawn, which is what the toolkit
 * does. A model carries the light that was on it when it was made, so moving the sun afterwards
 * does not change a model that already exists.
 */
static void lightModel(Model *model) {
    if (model->faceColour == NULL || model->normals == NULL) {
        return;
    }

    model->shade = calloc((size_t) model->faceCount * 3, sizeof(uint32_t));
    if (model->shade == NULL) {
        return;
    }

    float strength = model->contrast == 0 ? 1.0f : 768.0f / (float) model->contrast;

    for (int face = 0; face < model->faceCount; face++) {
        uint32_t unlit = unlitColour(model->faceColour[face] & 0xFFFF, model->ambient);
        const short *corners[3] = {model->faceA, model->faceB, model->faceC};

        int flat = model->shadingType != NULL && model->shadingType[face] != 0;

        for (int corner = 0; corner < 3; corner++) {
            const Normal *normal = flat
                ? &model->faceNormals[face]
                : &model->normals[corners[corner][face]];

            model->shade[face * 3 + corner] = normal->magnitude == 0.0f
                ? unlit
                : sunlitColour(unlit, normal, strength);
        }
    }
}

JNIEXPORT void JNICALL Java_i_oa(JNIEnv *env, jobject self, jobject toolkit) {
    (void) toolkit;

    setNativeId(env, self, (jlong) (intptr_t) calloc(1, sizeof(Model)));
}

/**
 * Takes a copy of the mesh.
 *
 * The client is free to reuse every array it hands over the moment this returns, and it does, so
 * nothing here may be a view onto one of them.
 */
JNIEXPORT void JNICALL Java_i_R(JNIEnv *env, jobject self, jobject toolkit, jobject pool,
                                 jint vertexCount, jint maxVertex,
                                 jintArray vertexX, jintArray vertexY, jintArray vertexZ,
                                 jintArray vertexLabel, jshortArray originModels,
                                 jint faceCount,
                                 jshortArray faceA, jshortArray faceB, jshortArray faceC,
                                 jbyteArray shadingType, jbyteArray facePriority,
                                 jbyteArray faceAlpha, jbyteArray faceTexSpace,
                                 jshortArray faceColour, jshortArray faceTexture,
                                 jintArray faceLabel, jbyte globalPriority,
                                 jshortArray unknown, jint texSpaceCount,
                                 jbyteArray texMappingType,
                                 jshortArray texSpaceDefA, jshortArray texSpaceDefB,
                                 jshortArray texSpaceDefC,
                                 jintArray texSpaceScaleX, jintArray texSpaceScaleY,
                                 jintArray texSpaceScaleZ,
                                 jbyteArray texRotation, jbyteArray texDirection,
                                 jintArray texOffsetX, jintArray texOffsetY, jintArray texOffsetZ,
                                 jintArray particles, jint emitterCount, jint effectorCount,
                                 jint functions, jint features, jint ambient, jint contrast,
                                 jintArray billboards) {
    (void) toolkit;
    (void) pool;
    (void) maxVertex;
    (void) vertexLabel;
    (void) originModels;
    (void) facePriority;
    (void) faceTexSpace;
    (void) faceTexture;
    (void) faceLabel;
    (void) globalPriority;
    (void) unknown;
    (void) texSpaceCount;
    (void) texMappingType;
    (void) texSpaceDefA;
    (void) texSpaceDefB;
    (void) texSpaceDefC;
    (void) texSpaceScaleX;
    (void) texSpaceScaleY;
    (void) texSpaceScaleZ;
    (void) texRotation;
    (void) texDirection;
    (void) texOffsetX;
    (void) texOffsetY;
    (void) texOffsetZ;
    (void) particles;
    (void) emitterCount;
    (void) effectorCount;
    (void) functions;
    (void) features;
    (void) billboards;

    Model *model = calloc(1, sizeof(Model));
    if (model == NULL) {
        return;
    }

    model->vertexCount = vertexCount;
    model->faceCount = faceCount;
    model->ambient = ambient;
    model->contrast = contrast;

    model->vertexX = copyInts(env, vertexX, vertexCount);
    model->vertexY = copyInts(env, vertexY, vertexCount);
    model->vertexZ = copyInts(env, vertexZ, vertexCount);

    model->faceA = copyShorts(env, faceA, faceCount);
    model->faceB = copyShorts(env, faceB, faceCount);
    model->faceC = copyShorts(env, faceC, faceCount);
    model->faceColour = copyShorts(env, faceColour, faceCount);
    model->faceAlpha = copyBytes(env, faceAlpha, faceCount);
    model->shadingType = copyBytes(env, shadingType, faceCount);

    if (model->vertexX != NULL && model->vertexY != NULL && model->vertexZ != NULL) {
        measure(model);

        if (model->faceA != NULL && model->faceB != NULL && model->faceC != NULL) {
            calculateNormals(model);
            lightModel(model);
        }
    }

    setNativeId(env, self, (jlong) (intptr_t) model);
}

JNIEXPORT void JNICALL Java_i_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    Model *model = modelOf(env, self);
    if (model == NULL) {
        return;
    }

    free(model->vertexX);
    free(model->vertexY);
    free(model->vertexZ);
    free(model->faceA);
    free(model->faceB);
    free(model->faceC);
    free(model->faceColour);
    free(model->faceAlpha);
    free(model->shadingType);
    free(model->normals);
    free(model->faceNormals);
    free(model->shade);
    free(model);

    setNativeId(env, self, 0);
}

JNIEXPORT jint JNICALL Java_i_V(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->minX;
}

JNIEXPORT jint JNICALL Java_i_RA(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->maxX;
}

JNIEXPORT jint JNICALL Java_i_fa(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->minY;
}

JNIEXPORT jint JNICALL Java_i_HA(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->minZ;
}

JNIEXPORT jint JNICALL Java_i_G(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->maxZ;
}

JNIEXPORT jint JNICALL Java_i_na(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->radiusCylinder;
}

JNIEXPORT jint JNICALL Java_i_da(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->contrast;
}

int modelVertexCount(const void *handle) {
    const Model *model = handle;
    return model == NULL ? 0 : model->vertexCount;
}

int modelFaceCount(const void *handle) {
    const Model *model = handle;
    return model == NULL ? 0 : model->faceCount;
}

const int *modelVertexX(const void *handle) {
    return ((const Model *) handle)->vertexX;
}

const int *modelVertexY(const void *handle) {
    return ((const Model *) handle)->vertexY;
}

const int *modelVertexZ(const void *handle) {
    return ((const Model *) handle)->vertexZ;
}

const short *modelFaceA(const void *handle) {
    return ((const Model *) handle)->faceA;
}

const short *modelFaceB(const void *handle) {
    return ((const Model *) handle)->faceB;
}

const short *modelFaceC(const void *handle) {
    return ((const Model *) handle)->faceC;
}

const short *modelFaceColour(const void *handle) {
    return ((const Model *) handle)->faceColour;
}

const Normal *modelNormals(const void *handle) {
    return ((const Model *) handle)->normals;
}

const Normal *modelFaceNormals(const void *handle) {
    return ((const Model *) handle)->faceNormals;
}

const uint32_t *modelShade(const void *handle) {
    return ((const Model *) handle)->shade;
}

/**
 * Whether this face wanted its own direction rather than the averaged one at its corners.
 */
int modelFaceIsFlat(const void *handle, int face) {
    const Model *model = handle;
    return model->shadingType != NULL && model->shadingType[face] != 0;
}

int modelAmbient(const void *handle) {
    return ((const Model *) handle)->ambient;
}

int modelContrast(const void *handle) {
    return ((const Model *) handle)->contrast;
}
