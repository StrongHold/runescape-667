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

/**
 * What is added to a radius before it is cut to a whole number, which takes it to the next whole
 * number for all but the hundredth of values that land just above one.
 */
static const float ROUNDING = 0.99f;

/** What the client passes to leave an axis the size it already is. */
enum { FULL = 128 };

/**
 * What a model has to have been built to allow before the client may ask for it.
 *
 * A model is built with room for only the things the client says it will do to it, so asking for
 * anything else is a mistake in the client rather than something to be quietly allowed. The
 * toolkit throws, and so does this.
 */
enum {
    MAY_CHANGE_X = 0x1,
    MAY_CHANGE_Y = 0x2,
    MAY_CHANGE_Z = 0x4,
    MAY_TURN_NORMALS = 0x8,
    MAY_MIRROR = 0x10,
    MAY_RECOLOUR = 0x4000,
    MAY_RETEXTURE = 0x8000,
    MAY_ANIMATE = 0x10000
};

/**
 * A face that asked to be shaded only while the model is being animated. Once the client gives up
 * the right to animate, such a face is shaded like any other.
 */
enum { SHADED_WHEN_ANIMATED = 2 };

/**
 * What a model has to have been built to draw, as the bits the client passed for its features.
 *
 * Only one of them is read here: a model whose faces are shaded needs the direction each vertex
 * faces, and that has to be worked out again every time a vertex moves.
 */
enum { NEEDS_NORMALS = 0x10 };

/**
 * The three turns about the upright axis that are whole quarters of a circle, which are done by
 * swapping two places over rather than through the table.
 */
enum {
    QUARTER = 0x1000,
    HALF = 0x2000,
    THREE_QUARTERS = 0x3000
};

/** Which of the four floats of a vertex hold which axis. */
enum { ACROSS = 0, UPRIGHT = 1, AWAY = 2 };

static const float OVER_FULL = 1.0f / (float) FULL;

/** How many floats a vertex takes, which is one more than it needs so that four fit a register. */
enum { VERTEX_STRIDE = 4 };

typedef struct {
    int vertexCount;

    /**
     * How many of the vertices have a direction worked out for them.
     *
     * The client counts the vertices its faces are built from separately from the vertices it
     * hands over, because the ones past this point belong to billboards and to particles and are
     * never shaded. The normals are only this long, so anything walking both arrays at once stops
     * here and finishes the vertices on its own.
     */
    int maxVertex;

    int faceCount;

    /**
     * Where every vertex is, four floats apart.
     *
     * The client hands them over as whole numbers and they are kept as floats, because everything
     * the client does to a model afterwards, moving it, resizing it, turning it, is done in floats
     * and leaves them somewhere between two whole numbers.
     */
    float *vertices;

    short *faceA;
    short *faceB;
    short *faceC;
    short *faceColour;
    short *faceTexture;
    signed char *faceAlpha;
    signed char *shadingType;

    /** Which vertices move together, which is what an animation is applied through. */
    int *vertexLabel;

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

    /**
     * What the client said it would do to the model, as the bits it passed when it built one.
     *
     * A model is built with room only for what this asks for, so anything else is refused. This is
     * not the same as the features the client asks for, which say how a model is drawn rather than
     * what may be done to it.
     */
    int functions;

    /**
     * What the client said the model would be drawn with, which is a different set of bits from
     * the functions and says how rather than what.
     */
    int features;

    /** Whether the box and the two radii still describe where the vertices are. */
    int measured;

    /** Whether any face is see-through, which the client asks so that it can draw in two passes. */
    int transparent;

    /** Whether any face wears a texture that moves of its own accord. */
    int movingTextures;

    /** Whether the animation now open has moved anything. */
    int animated;

    /** How far the animation now open has carried the whole model. */
    int carriedX;
    int carriedY;
    int carriedZ;

    Normal *normals;
    Normal *faceNormals;

    /**
     * The colour each corner of each face takes, or null when it has yet to be worked out.
     *
     * It is worked out the first time the model is drawn rather than when it is built, because
     * everything that moves a vertex moves the direction it faces with it and the client moves a
     * model about a great deal between building it and drawing it.
     */
    uint32_t *shade;
} Model;

static Model *modelOf(JNIEnv *env, jobject self) {
    return (Model *) (intptr_t) nativeIdOf(env, self);
}

/**
 * Takes the three arrays the client hands over and lays them out a vertex at a time, as floats.
 */
static float *copyVertices(JNIEnv *env, jintArray x, jintArray y, jintArray z, int count) {
    if (x == NULL || y == NULL || z == NULL || count <= 0) {
        return NULL;
    }

    int *held = calloc((size_t) count, sizeof(int));
    float *laid = calloc((size_t) count * VERTEX_STRIDE, sizeof(float));

    if (held == NULL || laid == NULL) {
        free(held);
        free(laid);
        return NULL;
    }

    jintArray sources[3] = {x, y, z};
    for (int lane = 0; lane < 3; lane++) {
        (*env)->GetIntArrayRegion(env, sources[lane], 0, count, (jint *) held);
        for (int vertex = 0; vertex < count; vertex++) {
            laid[(size_t) vertex * VERTEX_STRIDE + lane] = (float) held[vertex];
        }
    }

    free(held);
    return laid;
}

/**
 * Whether the model was built to allow all of this, throwing back into the client when it was not.
 */
static int allowed(JNIEnv *env, const Model *model, int functions) {
    if ((model->functions & functions) == functions) {
        return 1;
    }

    jclass complaint = (*env)->FindClass(env, "java/lang/IllegalStateException");
    if (complaint != NULL) {
        (*env)->ThrowNew(env, complaint, NULL);
    }
    return 0;
}

static float *vertexAt(Model *model, int vertex) {
    return model->vertices + (size_t) vertex * VERTEX_STRIDE;
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
 * worth looking at.
 *
 * The cylinder ignores height, because the client turns models about the upright axis and a radius
 * that ignores height does not change when it does.
 *
 * The box starts at the ends of what a short can hold rather than at nothing, so a model that sits
 * entirely to one side of the middle is measured where it is rather than being stretched back to
 * include the middle. Every one of these is kept as a short, and a model larger than a short can
 * hold comes back wrapped.
 */
static void measure(Model *model) {
    float leastX = 32767.0f;
    float mostX = -32768.0f;
    float leastY = 32767.0f;
    float mostY = -32768.0f;
    float leastZ = 32767.0f;
    float mostZ = -32768.0f;
    float widest = 0.0f;
    float furthest = 0.0f;

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        const float *at = vertexAt(model, vertex);
        float x = at[0];
        float y = at[1];
        float z = at[2];

        leastX = fminf(x, leastX);
        mostX = fmaxf(x, mostX);
        leastY = fminf(y, leastY);
        mostY = fmaxf(y, mostY);
        leastZ = fminf(z, leastZ);
        mostZ = fmaxf(z, mostZ);

        float across = x * x + z * z;
        widest = fmaxf(across, widest);
        furthest = fmaxf(y * y + across, furthest);
    }

    model->minX = (short) (int) leastX;
    model->maxX = (short) (int) mostX;
    model->minY = (short) (int) leastY;
    model->maxY = (short) (int) mostY;
    model->minZ = (short) (int) leastZ;
    model->maxZ = (short) (int) mostZ;
    model->radiusCylinder = (short) (int) (sqrtf(widest) + ROUNDING);
    model->radiusSphere = (short) (int) (sqrtf(furthest) + ROUNDING);
    model->measured = 1;
}

/** Measures the model again if anything has moved since it last was. */
static void measureIfNeeded(Model *model) {
    if (!model->measured) {
        measure(model);
    }
}

/**
 * The direction each vertex faces, taken as the average of the unit normals of the faces meeting
 * there.
 *
 * A face may ask for its own direction instead, in which case it keeps one of its own and hands
 * nothing to its corners, and a face may ask for neither and is left without a direction at all.
 *
 * A face with no area contributes nothing but is still counted, which drags the average of every
 * corner it touches towards nothing. That is what the toolkit does and it is kept.
 */
static void calculateNormals(Model *model) {
    free(model->normals);
    free(model->faceNormals);

    int shaded = model->maxVertex > model->vertexCount ? model->maxVertex : model->vertexCount;
    model->normals = calloc((size_t) shaded, sizeof(Normal));
    model->faceNormals = calloc((size_t) model->faceCount, sizeof(Normal));
    if (model->normals == NULL || model->faceNormals == NULL) {
        return;
    }

    for (int face = 0; face < model->faceCount; face++) {
        int a = model->faceA[face];
        int b = model->faceB[face];
        int c = model->faceC[face];

        const float *from = vertexAt(model, a);
        const float *toB = vertexAt(model, b);
        const float *toC = vertexAt(model, c);

        float abx = toB[0] - from[0];
        float aby = toB[1] - from[1];
        float abz = toB[2] - from[2];
        float acx = toC[0] - from[0];
        float acy = toC[1] - from[1];
        float acz = toC[2] - from[2];

        float nx = aby * acz - abz * acy;
        float ny = abz * acx - abx * acz;
        float nz = abx * acy - aby * acx;

        float length = sqrtf(nx * nx + ny * ny + nz * nz);
        if (length != 0.0f) {
            float scale = 1.0f / length;
            nx *= scale;
            ny *= scale;
            nz *= scale;
        }

        int shading = model->shadingType == NULL ? 0 : model->shadingType[face];

        if (shading == 1) {
            Normal *normal = &model->faceNormals[face];
            normal->x = nx;
            normal->y = ny;
            normal->z = nz;
            normal->magnitude = 1.0f;
        } else if (shading == 0) {
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
}

/**
 * Works out what colour each corner of each face takes, from where the vertices are now.
 *
 * A model that has no directions worked out for it gets them here, because everything that moves
 * a vertex throws them away and this is the first thing afterwards that needs them.
 */
static void lightModel(Model *model) {
    if (model->faceColour == NULL) {
        return;
    }

    if (model->normals == NULL && model->faceA != NULL && model->faceB != NULL
        && model->faceC != NULL) {
        calculateNormals(model);
    }

    if (model->normals == NULL) {
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

/** Throws away the light the model is wearing, so that it is worked out again when it is drawn. */
static void unlight(Model *model) {
    free(model->shade);
    model->shade = NULL;
}

/**
 * What every change to where a vertex sits ends in.
 *
 * The box the model sits in no longer describes it, and the direction each vertex faces has moved
 * with the vertex, so both are thrown away. The light is not: a model already wearing one keeps it
 * until something asks for it to be worked out again.
 *
 * A model the client said would be shaded has its directions worked out again straight away. Any
 * other model waits until the light is asked for, which is where they are worked out from.
 */
static void geometryChanged(Model *model) {
    model->measured = 0;

    free(model->normals);
    free(model->faceNormals);
    model->normals = NULL;
    model->faceNormals = NULL;

    if ((model->features & NEEDS_NORMALS) != 0 && model->faceA != NULL
        && model->faceB != NULL && model->faceC != NULL) {
        calculateNormals(model);
    }
}

/**
 * Turns two of a place's three axes about the third.
 *
 * Which two, and which way round, is all that separates the three turns, so they share this.
 * `first` is the axis the sine is taken from and `second` the one it is taken off.
 */
static void turnPlace(float *place, int first, int second, float sine, float cosine) {
    float a = place[first];
    float b = place[second];

    place[second] = cosine * b - sine * a;
    place[first] = b * sine + a * cosine;
}

/**
 * The same turn for the three angles that are whole quarters of a circle, which are two places
 * swapped over and a sign rather than anything read from a table.
 */
static void quarterTurnPlace(float *place, int angle) {
    float across = place[ACROSS];
    float away = place[AWAY];

    if (angle == QUARTER) {
        place[ACROSS] = away;
        place[AWAY] = -across;
    } else if (angle == HALF) {
        place[ACROSS] = -across;
        place[AWAY] = -away;
    } else {
        place[ACROSS] = -away;
        place[AWAY] = across;
    }
}

/**
 * A direction is laid out as three floats and a fourth, the same as a vertex, so the turns read
 * one through the other.
 */
static float *normalPlace(Normal *normal) {
    return &normal->x;
}

static void turn(Model *model, int angle, int first, int second) {
    float sine = sineOf(angle);
    float cosine = cosineOf(angle);

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        turnPlace(vertexAt(model, vertex), first, second, sine, cosine);
    }
}

/**
 * Turns the model about the upright axis, carrying the direction every vertex and every face
 * looks in round with it rather than working them all out again.
 *
 * Only the vertices the faces are built from have a direction, so the rest are turned on their
 * own afterwards.
 */
static void turnWithNormals(Model *model, int angle) {
    int quarter = angle == QUARTER || angle == HALF || angle == THREE_QUARTERS;
    float sine = quarter ? 0.0f : sineOf(angle);
    float cosine = quarter ? 0.0f : cosineOf(angle);

    for (int vertex = 0; vertex < model->maxVertex; vertex++) {
        float *at = vertexAt(model, vertex);
        float *normal = normalPlace(&model->normals[vertex]);

        if (quarter) {
            quarterTurnPlace(at, angle);
            quarterTurnPlace(normal, angle);
        } else {
            turnPlace(at, ACROSS, AWAY, sine, cosine);
            turnPlace(normal, ACROSS, AWAY, sine, cosine);
        }
    }

    if (model->faceNormals != NULL) {
        for (int face = 0; face < model->faceCount; face++) {
            float *normal = normalPlace(&model->faceNormals[face]);
            if (quarter) {
                quarterTurnPlace(normal, angle);
            } else {
                turnPlace(normal, ACROSS, AWAY, sine, cosine);
            }
        }
    }

    for (int vertex = model->maxVertex; vertex < model->vertexCount; vertex++) {
        float *at = vertexAt(model, vertex);
        if (quarter) {
            quarterTurnPlace(at, angle);
        } else {
            turnPlace(at, ACROSS, AWAY, sine, cosine);
        }
    }

    model->measured = 0;
    unlight(model);
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
    (void) originModels;
    (void) facePriority;
    (void) faceTexSpace;
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
    (void) billboards;

    Model *model = calloc(1, sizeof(Model));
    if (model == NULL) {
        return;
    }

    model->vertexCount = vertexCount;
    model->maxVertex = maxVertex;
    model->faceCount = faceCount;
    model->ambient = ambient;
    model->contrast = contrast;
    model->functions = functions;
    model->features = features;

    model->vertices = copyVertices(env, vertexX, vertexY, vertexZ, vertexCount);

    model->faceA = copyShorts(env, faceA, faceCount);
    model->faceB = copyShorts(env, faceB, faceCount);
    model->faceC = copyShorts(env, faceC, faceCount);
    model->faceColour = copyShorts(env, faceColour, faceCount);
    model->faceTexture = copyShorts(env, faceTexture, faceCount);
    model->faceAlpha = copyBytes(env, faceAlpha, faceCount);
    model->shadingType = copyBytes(env, shadingType, faceCount);
    model->vertexLabel = copyInts(env, vertexLabel, vertexCount);

    if (model->vertices != NULL) {
        measure(model);

        if (model->faceA != NULL && model->faceB != NULL && model->faceC != NULL) {
            calculateNormals(model);
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

    free(model->vertices);
    free(model->faceA);
    free(model->faceB);
    free(model->faceC);
    free(model->faceColour);
    free(model->faceTexture);
    free(model->faceAlpha);
    free(model->shadingType);
    free(model->vertexLabel);
    free(model->normals);
    free(model->faceNormals);
    free(model->shade);
    free(model);

    setNativeId(env, self, 0);
}

JNIEXPORT jint JNICALL Java_i_V(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->minX;
}

JNIEXPORT jint JNICALL Java_i_RA(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->maxX;
}

JNIEXPORT jint JNICALL Java_i_fa(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->minY;
}

JNIEXPORT jint JNICALL Java_i_HA(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->minZ;
}

JNIEXPORT jint JNICALL Java_i_G(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->maxZ;
}

JNIEXPORT jint JNICALL Java_i_na(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->radiusCylinder;
}

JNIEXPORT jint JNICALL Java_i_da(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->contrast;
}

JNIEXPORT jint JNICALL Java_i_WA(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->ambient;
}

/** What the client said it would do to the model when it built one. */
JNIEXPORT jint JNICALL Java_i_ua(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model == NULL ? 0 : model->functions;
}

JNIEXPORT jint JNICALL Java_i_EA(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->maxY;
}

JNIEXPORT jint JNICALL Java_i_ma(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return 0;
    }

    measureIfNeeded(model);
    return model->radiusSphere;
}

/**
 * How much light the model has before the sun reaches it. Changing it does not relight the model:
 * the client sets this before it builds one.
 */
JNIEXPORT void JNICALL Java_i_C(JNIEnv *env, jobject self, jint ambient) {
    Model *model = modelOf(env, self);
    if (model != NULL) {
        model->ambient = ambient;
    }
}

JNIEXPORT void JNICALL Java_i_LA(JNIEnv *env, jobject self, jint contrast) {
    Model *model = modelOf(env, self);
    if (model != NULL) {
        model->contrast = contrast;
    }
}

/**
 * Moves every vertex.
 *
 * The box the model sits in is left alone, so a model that has been moved still answers where it
 * was before the move until something else makes it measure itself again. That is what the toolkit
 * does, and it is kept.
 */
JNIEXPORT void JNICALL Java_i_H(JNIEnv *env, jobject self, jint x, jint y, jint z) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    if ((x != 0 && !allowed(env, model, MAY_CHANGE_X))
        || (y != 0 && !allowed(env, model, MAY_CHANGE_Y))
        || (z != 0 && !allowed(env, model, MAY_CHANGE_Z))) {
        return;
    }

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        float *at = vertexAt(model, vertex);
        at[0] += (float) x;
        at[1] += (float) y;
        at[2] += (float) z;
    }
}

/**
 * Stretches every vertex, where a hundred and twenty eight leaves an axis as it is.
 *
 * Each axis is taken down to the whole number below, so a model stretched and then stretched back
 * does not come out where it started.
 */
JNIEXPORT void JNICALL Java_i_O(JNIEnv *env, jobject self, jint x, jint y, jint z) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    if ((x != FULL && !allowed(env, model, MAY_CHANGE_X))
        || (y != FULL && !allowed(env, model, MAY_CHANGE_Y))
        || (z != FULL && !allowed(env, model, MAY_CHANGE_Z))) {
        return;
    }

    float scale[3] = {
        (float) x * OVER_FULL,
        (float) y * OVER_FULL,
        (float) z * OVER_FULL
    };
    int wanted[3] = {x, y, z};

    for (int lane = 0; lane < 3; lane++) {
        if (wanted[lane] == FULL) {
            continue;
        }

        for (int vertex = 0; vertex < model->vertexCount; vertex++) {
            float *at = vertexAt(model, vertex);
            at[lane] = floorf(at[lane] * scale[lane]);
        }
    }

    geometryChanged(model);
}

/**
 * Gives every face wearing one colour another one, and forgets the light that was worked out for
 * the model so that it is worked out again with the new colours.
 */
JNIEXPORT void JNICALL Java_i_ia(JNIEnv *env, jobject self, jshort from, jshort to) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->faceColour == NULL || !allowed(env, model, MAY_RECOLOUR)) {
        return;
    }

    for (int face = 0; face < model->faceCount; face++) {
        if (model->faceColour[face] == from) {
            model->faceColour[face] = to;
        }
    }

    unlight(model);
}

/**
 * Turns the model about the upright axis.
 *
 * A quarter, a half and three quarters of a circle are done by swapping two places over, because
 * a sine read from the table is not exactly one and a model turned four times through the table
 * does not come back where it started.
 */
JNIEXPORT void JNICALL Java_i_a(JNIEnv *env, jobject self, jint angle) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    if (!allowed(env, model, MAY_CHANGE_X | MAY_CHANGE_Z)) {
        return;
    }

    if (angle == QUARTER || angle == HALF || angle == THREE_QUARTERS) {
        for (int vertex = 0; vertex < model->vertexCount; vertex++) {
            quarterTurnPlace(vertexAt(model, vertex), angle);
        }
    } else {
        turn(model, angle, ACROSS, AWAY);
    }

    geometryChanged(model);
}

/**
 * Turns the model about the upright axis, carrying the directions round with it.
 *
 * A model with no directions worked out for it is turned the ordinary way, which works them out
 * again from where the vertices ended up.
 */
JNIEXPORT void JNICALL Java_i_k(JNIEnv *env, jobject self, jint angle) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    if (!allowed(env, model, MAY_CHANGE_X | MAY_TURN_NORMALS | MAY_CHANGE_Z)) {
        return;
    }

    if (model->normals == NULL) {
        Java_i_a(env, self, angle);
    } else {
        turnWithNormals(model, angle);
    }
}

/** Turns the model about the axis that runs across the picture. */
JNIEXPORT void JNICALL Java_i_FA(JNIEnv *env, jobject self, jint angle) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    if (!allowed(env, model, MAY_CHANGE_Y | MAY_CHANGE_Z)) {
        return;
    }

    turn(model, angle, AWAY, UPRIGHT);
    geometryChanged(model);
}

/** Turns the model about the axis that runs into the picture. */
JNIEXPORT void JNICALL Java_i_VA(JNIEnv *env, jobject self, jint angle) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    if (!allowed(env, model, MAY_CHANGE_X | MAY_CHANGE_Y)) {
        return;
    }

    turn(model, angle, ACROSS, UPRIGHT);
    geometryChanged(model);
}

/**
 * Turns the model back to front.
 *
 * Every face is wound the other way round once its vertices have moved, so the two corners that
 * are not the first are swapped over. The directions are turned rather than worked out again,
 * and the light is thrown away because the corners a face is lit at have changed places.
 */
JNIEXPORT void JNICALL Java_i_v(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL || !allowed(env, model, MAY_MIRROR)) {
        return;
    }

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        vertexAt(model, vertex)[AWAY] = -vertexAt(model, vertex)[AWAY];
    }

    if (model->normals != NULL) {
        for (int vertex = 0; vertex < model->maxVertex; vertex++) {
            model->normals[vertex].z = -model->normals[vertex].z;
        }
    }

    if (model->faceNormals != NULL) {
        for (int face = 0; face < model->faceCount; face++) {
            model->faceNormals[face].z = -model->faceNormals[face].z;
        }
    }

    short *wasB = model->faceB;
    model->faceB = model->faceC;
    model->faceC = wasB;

    model->measured = 0;
    unlight(model);
}

/**
 * Narrows what the client may do to the model from here on.
 *
 * Only narrowing is allowed: a mask asking for anything the model was not already built for is a
 * mistake in the client. Giving up the right to animate settles the shading of every face that
 * was waiting on an animation to decide it, so the model is lit again.
 */
JNIEXPORT void JNICALL Java_i_s(JNIEnv *env, jobject self, jint functions) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return;
    }

    if ((functions & model->functions) != functions) {
        jclass complaint = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
        if (complaint != NULL) {
            (*env)->ThrowNew(env, complaint,
                "Can't re-enable previously disabled functions");
        }
        return;
    }

    if ((model->functions & MAY_ANIMATE) != 0 && (functions & MAY_ANIMATE) == 0) {
        if (model->shadingType != NULL) {
            for (int face = 0; face < model->faceCount; face++) {
                if (model->shadingType[face] == SHADED_WHEN_ANIMATED) {
                    model->shadingType[face] = 0;
                }
            }
        }

        unlight(model);
    }

    model->functions = functions;
}

/**
 * Whether anything about the model can be seen through.
 */
JNIEXPORT jboolean JNICALL Java_i_F(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return JNI_FALSE;
    }

    return model->transparent || model->faceAlpha != NULL ? JNI_TRUE : JNI_FALSE;
}

/** Whether any face wears a texture that moves of its own accord. */
JNIEXPORT jboolean JNICALL Java_i_r(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    return model != NULL && model->movingTextures ? JNI_TRUE : JNI_FALSE;
}

/**
 * Gives every face wearing one texture another one.
 *
 * The toolkit this replaces also asks the texture cache how large each of the two is and throws
 * the light away when they differ, because the colour a textured face is lit to depends on it.
 * Nothing here has a texture cache yet, so the light is thrown away either way.
 */
JNIEXPORT void JNICALL Java_i_aa(JNIEnv *env, jobject self, jshort from, jshort to) {
    Model *model = modelOf(env, self);
    if (model == NULL || !allowed(env, model, MAY_RETEXTURE)) {
        return;
    }

    if (model->faceTexture == NULL) {
        return;
    }

    for (int face = 0; face < model->faceCount; face++) {
        if (model->faceTexture[face] == from) {
            model->faceTexture[face] = to;
        }
    }

    unlight(model);
}

/**
 * Opens an animation, answering whether the model can be animated at all.
 *
 * A model with no labels on its vertices has nothing for an animation to move, so the client is
 * told to leave it alone. The distance the animation has carried the model so far starts at
 * nothing.
 */
JNIEXPORT jboolean JNICALL Java_i_NA(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertexLabel == NULL) {
        return JNI_FALSE;
    }

    model->carriedX = 0;
    model->carriedY = 0;
    model->carriedZ = 0;
    return JNI_TRUE;
}

/**
 * Closes an animation. The vertices have moved, so the box the model sits in no longer describes
 * it, and the light is thrown away only if the animation moved anything.
 */
JNIEXPORT void JNICALL Java_i_wa(JNIEnv *env, jobject self) {
    Model *model = modelOf(env, self);
    if (model == NULL) {
        return;
    }

    if (model->animated) {
        unlight(model);
        model->animated = 0;
    }

    model->measured = 0;
}

int modelVertexCount(const void *handle) {
    const Model *model = handle;
    return model == NULL ? 0 : model->vertexCount;
}

int modelFaceCount(const void *handle) {
    const Model *model = handle;
    return model == NULL ? 0 : model->faceCount;
}

const float *modelVertices(const void *handle) {
    return ((const Model *) handle)->vertices;
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

const uint32_t *modelShade(void *handle) {
    Model *model = handle;
    if (model->shade == NULL) {
        lightModel(model);
    }
    return model->shade;
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
