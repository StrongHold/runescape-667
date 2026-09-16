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
    MAY_CHANGE_ALPHA = 0x100,
    MAY_TURN_NORMALS_WHILE_ANIMATING = 0x200,
    MAY_RETEXTURE = 0x8000,
    MAY_SHARE_LIGHT = 0x10000
};

/**
 * The functions that let a copy change something the light depends on.
 *
 * A copy given any of them starts without the light its original was wearing, because the first
 * thing the client does with such a copy is the very thing that would have thrown it away.
 */
enum { MAY_CHANGE_THE_LIGHT = 0x17218 };

/**
 * The functions that move the direction a vertex or a face is shaded by, rather than working it
 * out again. A copy given any of them needs directions of its own.
 */
enum { MAY_CHANGE_THE_DIRECTIONS = MAY_TURN_NORMALS | MAY_MIRROR | MAY_TURN_NORMALS_WHILE_ANIMATING };

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

/**
 * What an animation does to the vertices of the group it names.
 *
 * The three that move particles and the one that fades a face are not here. Particles are not
 * built yet, and fading needs the faces gathered by label, which is built from what working out
 * the texture coordinates leaves behind.
 */
enum {
    PIVOT_AT = 0,
    MOVE_BY = 1,
    TURN_BY = 2,
    STRETCH_BY = 3
};

/** What the client counts a full stretch as, where a hundred and twenty eight leaves an axis be. */
static const float OVER_STRETCH = 1.0f / 128.0f;

/**
 * Whether a turn is applied about the across axis before the into-the-picture one, which is the
 * low bit of the number the client passes alongside the three angles.
 */
enum { TURN_ACROSS_FIRST = 0x1 };

/** Where in the group list a label's vertices sit. */
typedef struct {
    int start;
    int count;
} LabelGroup;

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

    /**
     * Which of the pieces the client built the model from each vertex came from, one bit per
     * piece. A player is one model built from a head, a torso and so on, and each piece is moved
     * into place on its own.
     */
    short *vertexPiece;
    signed char *faceAlpha;
    signed char *shadingType;

    /**
     * Which vertices move together, gathered into a list per label.
     *
     * An animation names a label and everything it does applies to the whole group at once. The
     * client hands over a label per vertex; this is that turned inside out, because an animation
     * asks the question the other way round and asks it several hundred times a frame.
     */
    LabelGroup *labelTable;
    int labelGroups;
    unsigned short *labelVertices;

    /**
     * The vertices the client hangs particles off: three for every emitter, then one for every
     * effector, in one flat run.
     */
    int *particleVertices;
    int emitters;
    int effectors;

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

    /** Whether the animation now open has moved anything the light depends on. */
    int animated;

    /**
     * Where the animation now open is turning and stretching the model about.
     *
     * An animation sets this from the average of a group of vertices and then works relative to
     * it, so it is carried from one step of the animation to the next.
     */
    float pivot[3];

    Normal *normals;
    Normal *faceNormals;

    /**
     * The direction a vertex faces once a neighbouring model has had a say, or null while no
     * neighbour has.
     *
     * Two models built side by side meet along an edge, and a vertex on that edge is shaded by
     * the faces of both of them rather than only by its own. This is where the neighbour's
     * directions are gathered, and a vertex with anything here is shaded by this instead of by
     * the direction its own faces gave it.
     */
    Normal *sharedNormals;

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

/**
 * Turns the label the client puts on each vertex into a list of vertices per label.
 *
 * A label of less than nothing means the vertex belongs to no group and is left out. The groups
 * run from nothing to the largest label that appeared, so a model that uses only high labels
 * carries empty groups below them.
 */
static void gatherLabels(JNIEnv *env, Model *model, jintArray vertexLabel) {
    if (vertexLabel == NULL || (*env)->GetArrayLength(env, vertexLabel) == 0) {
        return;
    }

    int given = (*env)->GetArrayLength(env, vertexLabel);
    int room = given > model->vertexCount ? given : model->vertexCount;
    int *labels = calloc((size_t) room, sizeof(int));
    if (labels == NULL) {
        return;
    }
    (*env)->GetIntArrayRegion(env, vertexLabel, 0, given, (jint *) labels);

    int groups = 1;
    int largest = 0;
    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        if (labels[vertex] > largest) {
            largest = labels[vertex];
        }
    }
    if (model->vertexCount > 0) {
        groups = largest + 1;
    }

    model->labelTable = calloc((size_t) groups, sizeof(LabelGroup));
    if (model->labelTable == NULL) {
        free(labels);
        return;
    }
    model->labelGroups = groups;

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        if (labels[vertex] >= 0) {
            model->labelTable[labels[vertex]].count++;
        }
    }

    int running = 0;
    for (int group = 0; group < groups; group++) {
        model->labelTable[group].start = running;
        running += model->labelTable[group].count;
    }

    model->labelVertices = calloc((size_t) (running == 0 ? 1 : running), sizeof(unsigned short));
    if (model->labelVertices == NULL) {
        free(labels);
        return;
    }

    int *placed = calloc((size_t) groups, sizeof(int));
    if (placed == NULL) {
        free(labels);
        return;
    }

    for (int vertex = 0; vertex < model->vertexCount; vertex++) {
        int label = labels[vertex];
        if (label >= 0) {
            model->labelVertices[model->labelTable[label].start + placed[label]] =
                (unsigned short) vertex;
            placed[label]++;
        }
    }

    free(placed);
    free(labels);
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
 * Only the vertices the faces are built from are measured. A model carries more than those: the
 * ones past that point hold billboards and particles, and a box drawn around those would not be
 * the box the faces fill.
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

    for (int vertex = 0; vertex < model->maxVertex; vertex++) {
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
 * The direction a vertex is shaded by, which is the one a neighbouring model gave it if any
 * neighbour has, and the one its own faces gave it otherwise.
 */
static const Normal *cornerNormal(const Model *model, int vertex) {
    if (model->sharedNormals != NULL && model->sharedNormals[vertex].magnitude != 0.0f) {
        return &model->sharedNormals[vertex];
    } else {
        return &model->normals[vertex];
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
                : cornerNormal(model, corners[corner][face]);

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
    free(model->sharedNormals);
    model->normals = NULL;
    model->faceNormals = NULL;
    model->sharedNormals = NULL;

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
/**
 * Asks for every texture the model wears while it is being built, and keeps what the answers say
 * about the model as a whole.
 *
 * The asking is the point as much as the answers are. A texture the client has not handed over yet
 * is fetched here rather than in the middle of drawing, so the first frame a model appears in is
 * not the one that goes back to the client for its pixels.
 *
 * A face whose texture is blended keeps the whole model marked as see through, and a texture that
 * slides marks the model as wearing one. A blended texture is not looked at for sliding, because
 * the two are decided in that order and the first ends the matter.
 */
static void takeTextures(Model *model) {
    if (model->faceTexture == NULL) {
        return;
    }

    for (int face = 0; face < model->faceCount; face++) {
        if (model->faceTexture[face] == -1) {
            continue;
        }

        const Texture *texture = textureFor(model->faceTexture[face]);
        if (texture == NULL) {
            continue;
        }

        const TextureMetrics *metrics = textureMetrics(texture);
        if (metrics->alphaBlendMode == 2) {
            model->transparent = 1;
        } else if (metrics->speedU != 0 || metrics->speedV != 0) {
            model->movingTextures = 1;
        }
    }
}

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
    model->vertexPiece = copyShorts(env, originModels, vertexCount);
    model->emitters = emitterCount;
    model->effectors = effectorCount;
    model->particleVertices = copyInts(env, particles, emitterCount * 3 + effectorCount);
    model->faceAlpha = copyBytes(env, faceAlpha, faceCount);
    model->shadingType = copyBytes(env, shadingType, faceCount);
    gatherLabels(env, model, vertexLabel);

    takeTextures(model);

    if (model->vertices != NULL) {
        measure(model);

        if (model->faceA != NULL && model->faceB != NULL && model->faceC != NULL) {
            calculateNormals(model);
        }
    }

    setNativeId(env, self, (jlong) (intptr_t) model);
}

/**
 * Lets go of everything the model holds, leaving it empty rather than freeing it.
 */
static void emptyModel(Model *model) {
    free(model->vertices);
    free(model->faceA);
    free(model->faceB);
    free(model->faceC);
    free(model->faceColour);
    free(model->faceTexture);
    free(model->vertexPiece);
    free(model->particleVertices);
    free(model->faceAlpha);
    free(model->shadingType);
    free(model->labelTable);
    free(model->labelVertices);
    free(model->normals);
    free(model->faceNormals);
    free(model->sharedNormals);
    free(model->shade);

    memset(model, 0, sizeof *model);
}

JNIEXPORT void JNICALL Java_i_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    Model *model = modelOf(env, self);
    if (model == NULL) {
        return;
    }

    emptyModel(model);
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

    if ((model->functions & MAY_SHARE_LIGHT) != 0 && (functions & MAY_SHARE_LIGHT) == 0) {
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
 * The light a model has already been given is kept unless the two textures are shaded through
 * differently, because working a model's light out again is far dearer than comparing two numbers
 * and most swaps are between textures that agree on both. A model swapped onto a texture that
 * slides either way is marked as wearing one.
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

    unsigned char fromAlpha = 0;
    unsigned char fromByte57 = 0;
    if (from != -1) {
        const TextureMetrics *metrics = textureMetricsFor((unsigned short) from);
        if (metrics != NULL) {
            fromAlpha = metrics->alpha;
            fromByte57 = metrics->aByte57;
        }
    }

    unsigned char toAlpha = 0;
    unsigned char toByte57 = 0;
    if (to != -1) {
        const TextureMetrics *metrics = textureMetricsFor((unsigned short) to);
        if (metrics != NULL) {
            toAlpha = metrics->alpha;
            toByte57 = metrics->aByte57;

            if (metrics->speedU != 0 || metrics->speedV != 0) {
                model->movingTextures = 1;
            }
        }
    }

    if (toAlpha != fromAlpha || fromByte57 != toByte57) {
        unlight(model);
    }
}

/**
 * Which vertices a step of an animation moves.
 *
 * The labels name groups. A step may also be told to move only the pieces of the model named by
 * a mask, which is how a player's arms animate while the rest of it stands still, and a vertex is
 * then moved only if the piece it came from is one of them. A model built from a single piece
 * carries no piece to name, and is moved whatever the mask says.
 */
typedef struct {
    const int *labels;
    int count;
    int pieces;
    int byPiece;
} Chosen;

/**
 * The vertices a label names, or nothing when the label names no group.
 */
static const LabelGroup *groupOf(const Model *model, int label) {
    if (model->labelTable == NULL || label < 0 || label >= model->labelGroups) {
        return NULL;
    }

    const LabelGroup *group = &model->labelTable[label];
    return group->count <= 0 ? NULL : group;
}

static int groupVertex(const Model *model, const LabelGroup *group, int within) {
    return model->labelVertices[group->start + within];
}

/**
 * Sets the point the rest of the animation turns and stretches about.
 *
 * It is the average of where the named groups are, moved by the amount given. A step naming no
 * group at all, or naming only groups with nothing in them, puts it at the amount given on its
 * own.
 */
static int moves(const Model *model, const Chosen *chosen, int vertex) {
    return !chosen->byPiece || model->vertexPiece == NULL
        || (model->vertexPiece[vertex] & chosen->pieces) != 0;
}

static void pivotAt(Model *model, const Chosen *chosen, int x, int y, int z) {
    model->pivot[0] = 0.0f;
    model->pivot[1] = 0.0f;
    model->pivot[2] = 0.0f;

    int gathered = 0;

    for (int named = 0; named < chosen->count; named++) {
        const LabelGroup *group = groupOf(model, chosen->labels[named]);
        if (group == NULL) {
            continue;
        }

        for (int within = 0; within < group->count; within++) {
            int vertex = groupVertex(model, group, within);
            if (!moves(model, chosen, vertex)) {
                continue;
            }

            const float *at = vertexAt(model, vertex);
            model->pivot[0] += at[0];
            model->pivot[1] += at[1];
            model->pivot[2] += at[2];
            gathered++;
        }
    }

    if (gathered == 0) {
        model->pivot[0] = (float) x;
        model->pivot[1] = (float) y;
        model->pivot[2] = (float) z;
    } else {
        model->pivot[0] = model->pivot[0] / (float) gathered + (float) x;
        model->pivot[1] = model->pivot[1] / (float) gathered + (float) y;
        model->pivot[2] = model->pivot[2] / (float) gathered + (float) z;
    }
}

static void moveBy(Model *model, const Chosen *chosen, int x, int y, int z) {
    for (int named = 0; named < chosen->count; named++) {
        const LabelGroup *group = groupOf(model, chosen->labels[named]);
        if (group == NULL) {
            continue;
        }

        for (int within = 0; within < group->count; within++) {
            int vertex = groupVertex(model, group, within);
            if (!moves(model, chosen, vertex)) {
                continue;
            }

            float *at = vertexAt(model, vertex);
            at[0] += (float) x;
            at[1] += (float) y;
            at[2] += (float) z;
        }
    }
}

/**
 * Turns a place about the pivot by three angles in turn.
 *
 * Which of the three goes first is the caller's, because the client asks for the across axis
 * first for some steps and the into-the-picture one first for others, and the two orders do not
 * give the same answer. An angle of nothing is skipped rather than turned through, which matters
 * because the table's sine of nothing is exact and its cosine of nothing is one.
 */
static void turnAboutPivot(float *place, int across, int upright, int away, int acrossFirst) {
    int order[3] = {away, across, upright};
    int axes[3][2] = {{ACROSS, UPRIGHT}, {AWAY, UPRIGHT}, {ACROSS, AWAY}};

    if (acrossFirst) {
        order[0] = across;
        order[1] = away;
        axes[0][0] = AWAY;
        axes[0][1] = UPRIGHT;
        axes[1][0] = ACROSS;
        axes[1][1] = UPRIGHT;
    }

    for (int step = 0; step < 3; step++) {
        if (order[step] != 0) {
            turnPlace(place, axes[step][0], axes[step][1],
                sineOf(order[step]), cosineOf(order[step]));
        }
    }
}

/**
 * Turns the named groups about the pivot, and their directions with them when the model was
 * built to allow it and the caller asked.
 */
static void turnBy(Model *model, const Chosen *chosen, int across, int upright, int away,
                   int order, int alsoNormals) {
    int acrossFirst = (order & TURN_ACROSS_FIRST) != 0;

    for (int named = 0; named < chosen->count; named++) {
        const LabelGroup *group = groupOf(model, chosen->labels[named]);
        if (group == NULL) {
            continue;
        }

        for (int within = 0; within < group->count; within++) {
            int vertex = groupVertex(model, group, within);
            if (!moves(model, chosen, vertex)) {
                continue;
            }

            float *at = vertexAt(model, vertex);

            at[0] -= model->pivot[0];
            at[1] -= model->pivot[1];
            at[2] -= model->pivot[2];

            turnAboutPivot(at, across, upright, away, acrossFirst);

            at[0] += model->pivot[0];
            at[1] += model->pivot[1];
            at[2] += model->pivot[2];
        }
    }

    if (!alsoNormals || model->normals == NULL
        || (model->functions & MAY_TURN_NORMALS_WHILE_ANIMATING) == 0) {
        return;
    }

    for (int named = 0; named < chosen->count; named++) {
        const LabelGroup *group = groupOf(model, chosen->labels[named]);
        if (group == NULL) {
            continue;
        }

        for (int within = 0; within < group->count; within++) {
            int vertex = groupVertex(model, group, within);
            if (!moves(model, chosen, vertex)) {
                continue;
            }

            turnAboutPivot(normalPlace(&model->normals[vertex]),
                across, upright, away, acrossFirst);
        }
    }
}

/**
 * Stretches the named groups away from the pivot, where a hundred and twenty eight leaves an
 * axis the length it already is.
 */
static void stretchBy(Model *model, const Chosen *chosen, int x, int y, int z) {
    float scale[3] = {(float) x, (float) y, (float) z};

    for (int named = 0; named < chosen->count; named++) {
        const LabelGroup *group = groupOf(model, chosen->labels[named]);
        if (group == NULL) {
            continue;
        }

        for (int within = 0; within < group->count; within++) {
            int vertex = groupVertex(model, group, within);
            if (!moves(model, chosen, vertex)) {
                continue;
            }

            float *at = vertexAt(model, vertex);

            for (int lane = 0; lane < 3; lane++) {
                at[lane] -= model->pivot[lane];
            }
            for (int lane = 0; lane < 3; lane++) {
                at[lane] = scale[lane] * at[lane] * OVER_STRETCH;
            }
            for (int lane = 0; lane < 3; lane++) {
                at[lane] += model->pivot[lane];
            }
        }
    }
}

/**
 * One step of an animation, applied to every vertex carrying one of the labels named.
 *
 * The model is named by the handle rather than by the object, because the client has it to hand
 * and calls this several hundred times a frame.
 *
 * The steps that move a particle, fade a face or turn a billboard are not here. Particles are not
 * built yet, and fading needs the faces gathered by label, which the toolkit works out while it
 * works out where a texture sits.
 */
/**
 * One step of an animation, applied to whichever vertices the step names.
 */
static void animationStep(Model *model, const Chosen *chosen, int step,
                          int x, int y, int z, int order, int alsoNormals) {
    if (step == PIVOT_AT) {
        pivotAt(model, chosen, x, y, z);
    } else if (step == MOVE_BY) {
        moveBy(model, chosen, x, y, z);
    } else if (step == TURN_BY) {
        turnBy(model, chosen, x, y, z, order, alsoNormals);
    } else if (step == STRETCH_BY) {
        stretchBy(model, chosen, x, y, z);
    }
}

/**
 * Reads the labels a step names out of the array the client handed over.
 */
static int *labelsOf(JNIEnv *env, jintArray named, int *count) {
    *count = named == NULL ? 0 : (*env)->GetArrayLength(env, named);
    if (*count <= 0) {
        return NULL;
    }

    int *labels = calloc((size_t) *count, sizeof(int));
    if (labels == NULL) {
        *count = 0;
        return NULL;
    }

    (*env)->GetIntArrayRegion(env, named, 0, *count, (jint *) labels);
    return labels;
}

JNIEXPORT void JNICALL Java_i_l(JNIEnv *env, jobject self, jlong handle, jint step,
                                 jintArray named, jint x, jint y, jint z, jint order,
                                 jboolean alsoNormals) {
    (void) self;

    Model *model = (Model *) (intptr_t) handle;
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    Chosen chosen;
    chosen.labels = labelsOf(env, named, &chosen.count);
    chosen.pieces = 0;
    chosen.byPiece = 0;

    animationStep(model, &chosen, step, x, y, z, order, alsoNormals == JNI_TRUE);

    free((void *) chosen.labels);
}

/**
 * One step of an animation, applied only to the pieces of the model the mask names.
 *
 * This is how a player waves an arm while the rest of it stands still: the client animates the
 * same model several times over, naming a different set of the pieces it was built from each
 * time.
 *
 * The client also has room to weigh each step by a matrix handed over alongside, and never uses
 * it: every call site passes nothing. The toolkit this replaces has a second copy of every step
 * for that case, and none of it is written here.
 */
JNIEXPORT void JNICALL Java_i_I(JNIEnv *env, jobject self, jint step, jintArray named,
                                 jint x, jint y, jint z, jboolean alsoNormals,
                                 jint pieces, jintArray weights) {
    (void) weights;

    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL) {
        return;
    }

    Chosen chosen;
    chosen.labels = labelsOf(env, named, &chosen.count);
    chosen.pieces = pieces;
    chosen.byPiece = 1;

    animationStep(model, &chosen, step, x, y, z, 0, alsoNormals == JNI_TRUE);

    free((void *) chosen.labels);
}

/**
 * A copy of an array of whatever size, or nothing when there was nothing to copy.
 */
static void *duplicate(const void *source, size_t bytes) {
    if (source == NULL || bytes == 0) {
        return NULL;
    }

    void *copy = malloc(bytes);
    if (copy != NULL) {
        memcpy(copy, source, bytes);
    }
    return copy;
}

/**
 * Makes one model into a copy of another, keeping only the right to do what the mask allows.
 *
 * The toolkit this replaces shares an array between the two models wherever the mask says the
 * copy will never change it, and takes a copy only of the rest. Here every array is copied. The
 * two behave the same, because the arrays that would have been shared are the ones nothing is
 * allowed to touch; the difference is that this asks the system for more memory.
 *
 * The client hands over a second model to take the copies from, so that a copy made every frame
 * reuses the same memory. That is the same saving by another route and is not taken here either.
 */
JNIEXPORT void JNICALL Java_i_ZA(JNIEnv *env, jobject self, jobject into, jobject scratch,
                                  jint functions, jboolean reused, jboolean deep) {
    (void) scratch;
    (void) reused;

    Model *source = modelOf(env, self);
    Model *copy = modelOf(env, into);
    if (source == NULL || copy == NULL) {
        return;
    }

    if ((functions & source->functions) != functions) {
        jclass complaint = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
        if (complaint != NULL) {
            (*env)->ThrowNew(env, complaint,
                "Can't re-enable previously disabled functions");
        }
        return;
    }

    /*
     * A copy that will keep the light works the model it came from out first, and wears the
     * answer. That matters because the client turns such a copy immediately afterwards, and a
     * turn does not throw the light away: the copy is meant to be lit as the model it came from
     * stood, not as it ends up.
     */
    if (deep == JNI_TRUE && (functions & MAY_CHANGE_THE_LIGHT) == 0 && source->shade == NULL) {
        lightModel(source);
    }

    if (deep == JNI_TRUE && (functions & MAY_CHANGE_THE_DIRECTIONS) == 0
        && source->normals == NULL) {
        calculateNormals(source);
    }

    emptyModel(copy);

    size_t vertices = (size_t) source->vertexCount;
    size_t faces = (size_t) source->faceCount;
    size_t shaded = (size_t) (source->maxVertex > source->vertexCount
        ? source->maxVertex : source->vertexCount);

    copy->vertexCount = source->vertexCount;
    copy->maxVertex = source->maxVertex;
    copy->faceCount = source->faceCount;
    copy->ambient = source->ambient;
    copy->contrast = source->contrast;
    copy->features = source->features;
    copy->functions = functions;
    copy->transparent = source->transparent;
    copy->movingTextures = source->movingTextures;

    copy->measured = source->measured;
    if (source->measured) {
        copy->minX = source->minX;
        copy->maxX = source->maxX;
        copy->minY = source->minY;
        copy->maxY = source->maxY;
        copy->minZ = source->minZ;
        copy->maxZ = source->maxZ;
        copy->radiusCylinder = source->radiusCylinder;
        copy->radiusSphere = source->radiusSphere;
    }

    copy->vertices = duplicate(source->vertices, vertices * VERTEX_STRIDE * sizeof(float));
    copy->faceA = duplicate(source->faceA, faces * sizeof(short));
    copy->faceB = duplicate(source->faceB, faces * sizeof(short));
    copy->faceC = duplicate(source->faceC, faces * sizeof(short));
    copy->faceColour = duplicate(source->faceColour, faces * sizeof(short));
    copy->faceTexture = duplicate(source->faceTexture, faces * sizeof(short));
    copy->faceAlpha = duplicate(source->faceAlpha, faces * sizeof(signed char));
    copy->shadingType = duplicate(source->shadingType, faces * sizeof(signed char));
    copy->vertexPiece = duplicate(source->vertexPiece, vertices * sizeof(short));
    copy->normals = duplicate(source->normals, shaded * sizeof(Normal));
    copy->faceNormals = duplicate(source->faceNormals, faces * sizeof(Normal));

    if ((functions & MAY_CHANGE_THE_LIGHT) == 0) {
        copy->shade = duplicate(source->shade, faces * 3 * sizeof(uint32_t));
    }

    /*
     * A copy allowed to change how see-through a face is gets somewhere to keep that, whether or
     * not the model it came from had one. That is why such a copy answers that it can be seen
     * through when the original says it cannot.
     */
    if ((functions & MAY_CHANGE_ALPHA) != 0 && copy->faceAlpha == NULL && faces > 0) {
        copy->faceAlpha = calloc(faces, sizeof(signed char));
    }

    if (source->labelTable != NULL) {
        int held = 0;
        for (int group = 0; group < source->labelGroups; group++) {
            held += source->labelTable[group].count;
        }

        copy->labelGroups = source->labelGroups;
        copy->labelTable = duplicate(source->labelTable,
            (size_t) source->labelGroups * sizeof(LabelGroup));
        copy->labelVertices = duplicate(source->labelVertices,
            (size_t) (held == 0 ? 1 : held) * sizeof(unsigned short));
    }
}

/**
 * Puts the vertices of some of the pieces the model was built from through a matrix.
 *
 * The pieces are named by a bit each, and a vertex belongs to the pieces its own word names. A
 * player is one model built from a head, a torso, a pair of legs and so on, and each of them is
 * carried to where its bone is on its own.
 *
 * Undoing a matrix takes the translation off first and then puts the point through the matrix the
 * other way round, which is its inverse as long as the rest of it only turns. Applying one is the
 * ordinary way round.
 *
 * Nothing here says the model has moved. The client puts a piece into place and takes it out
 * again either side of an animation, and measuring it in between would measure it where no frame
 * ever sees it.
 */
JNIEXPORT void JNICALL Java_i_J(JNIEnv *env, jobject self, jlong matrix, jint pieces,
                                 jboolean undo) {
    Model *model = modelOf(env, self);
    if (model == NULL || model->vertices == NULL || model->vertexPiece == NULL) {
        return;
    }

    const float *rows = matrixRows((const void *) (intptr_t) matrix);
    if (rows == NULL) {
        return;
    }

    for (int vertex = 0; vertex < model->maxVertex; vertex++) {
        if ((model->vertexPiece[vertex] & pieces) == 0) {
            continue;
        }

        float *at = vertexAt(model, vertex);
        float was[VERTEX_STRIDE];
        memcpy(was, at, sizeof was);

        if (undo == JNI_TRUE) {
            for (int lane = 0; lane < VERTEX_STRIDE; lane++) {
                was[lane] -= rows[12 + lane];
            }

            for (int lane = 0; lane < VERTEX_STRIDE; lane++) {
                at[lane] = was[0] * rows[lane * 4]
                    + was[1] * rows[lane * 4 + 1]
                    + was[2] * rows[lane * 4 + 2];
            }
        } else {
            for (int lane = 0; lane < VERTEX_STRIDE; lane++) {
                at[lane] = was[0] * rows[lane]
                    + was[1] * rows[4 + lane]
                    + was[2] * rows[8 + lane]
                    + rows[12 + lane];
            }
        }
    }
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
    if (model == NULL || model->labelTable == NULL) {
        return JNI_FALSE;
    }

    model->pivot[0] = 0.0f;
    model->pivot[1] = 0.0f;
    model->pivot[2] = 0.0f;
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

/**
 * How far apart two vertices may be and still count as the same place.
 *
 * The client hands the two models over in whole numbers, but a model that has been resized or
 * turned keeps its vertices between whole numbers, so the meeting is looked for by nearness
 * rather than by equality.
 */
static const float SAME_PLACE = 0.01f;

/** Somewhere for a neighbour's directions to be gathered, made the first time one is offered. */
static Normal *sharedNormalsOf(Model *model) {
    if (model->sharedNormals == NULL && model->maxVertex > 0) {
        model->sharedNormals = calloc((size_t) model->maxVertex, sizeof(Normal));
    }
    return model->sharedNormals;
}

/**
 * Lets two models that meet shade the edge they meet along as one surface.
 *
 * The client builds a wall as several models and stands them next to one another, and a corner
 * between two of them would otherwise show as a hard line, because each vertex of the join is
 * shaded only by the faces of the model it belongs to. Every vertex of one model that sits where
 * a vertex of the other does hands its direction over and takes the other's, and both are shaded
 * as though the two models were one.
 *
 * The second model is given where it stands relative to the first, because the two are built
 * about their own middles and only the client knows how far apart they are put.
 *
 * Both models have to have been built saying their light may still change, or neither has a light
 * left to change by the time this is asked for.
 *
 * The last thing the client passes asks for the faces of the join to be marked as shaded while
 * animating. The toolkit has never read it.
 */
JNIEXPORT void JNICALL Java_a_r(JNIEnv *env, jobject self, jlong worker, jlong first,
                                 jlong second, jint x, jint y, jint z, jboolean marked) {
    (void) self;
    (void) worker;
    (void) marked;

    Model *model = (Model *) (intptr_t) first;
    Model *neighbour = (Model *) (intptr_t) second;
    if (model == NULL || neighbour == NULL) {
        return;
    }

    if (!allowed(env, model, MAY_SHARE_LIGHT) || !allowed(env, neighbour, MAY_SHARE_LIGHT)) {
        return;
    }

    measure(model);
    calculateNormals(model);
    measure(neighbour);
    calculateNormals(neighbour);

    if (model->normals == NULL || neighbour->normals == NULL) {
        return;
    }

    float awayX = (float) x;
    float awayY = (float) y;
    float awayZ = (float) z;

    for (int vertex = 0; vertex < model->maxVertex; vertex++) {
        Normal here = model->normals[vertex];
        if (here.magnitude == 0.0f) {
            continue;
        }

        const float *at = vertexAt(model, vertex);
        float upright = at[1] - awayY;
        if ((float) neighbour->minY > upright || upright > (float) neighbour->maxY) {
            continue;
        }

        float across = at[0] - awayX;
        if ((float) neighbour->minX > across || across > (float) neighbour->maxX) {
            continue;
        }

        float depth = at[2] - awayZ;
        if ((float) neighbour->minZ > depth || depth > (float) neighbour->maxZ) {
            continue;
        }

        for (int other = 0; other < neighbour->maxVertex; other++) {
            Normal there = neighbour->normals[other];
            const float *meets = vertexAt(neighbour, other);

            if (fabsf(across - meets[0]) >= SAME_PLACE
                || fabsf(depth - meets[2]) >= SAME_PLACE
                || fabsf(upright - meets[1]) >= SAME_PLACE
                || there.magnitude == 0.0f) {
                continue;
            }

            Normal *mine = sharedNormalsOf(model);
            Normal *theirs = sharedNormalsOf(neighbour);
            if (mine == NULL || theirs == NULL) {
                return;
            }

            mine[vertex].x += there.x;
            mine[vertex].y += there.y;
            mine[vertex].z += there.z;
            mine[vertex].magnitude += there.magnitude;

            theirs[other].x += here.x;
            theirs[other].y += here.y;
            theirs[other].z += here.z;
            theirs[other].magnitude += here.magnitude;
        }
    }
}

/**
 * The box the model sits in, measuring it again first when something has moved.
 *
 * The six are the least and most of each axis in turn, which is the order a caller wanting the
 * eight corners of the box wants them in.
 */
void modelBounds(void *handle, int *into) {
    Model *model = handle;

    measureIfNeeded(model);

    into[0] = model->minX;
    into[1] = model->maxX;
    into[2] = model->minY;
    into[3] = model->maxY;
    into[4] = model->minZ;
    into[5] = model->maxZ;
}

/** How wide the model is about its upright axis, measuring it again first if anything has moved. */
int modelRadius(void *handle) {
    Model *model = handle;

    measureIfNeeded(model);
    return model->radiusCylinder;
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

/**
 * How many vertices the client has hung particles off: three for every emitter and one for every
 * effector.
 */
int modelParticleCount(const void *handle) {
    const Model *model = handle;
    return model->emitters * 3 + model->effectors;
}

const int *modelParticleVertices(const void *handle) {
    return ((const Model *) handle)->particleVertices;
}

int modelNeedsNormals(const void *handle) {
    return (((const Model *) handle)->features & NEEDS_NORMALS) != 0;
}

int modelAmbient(const void *handle) {
    return ((const Model *) handle)->ambient;
}

int modelContrast(const void *handle) {
    return ((const Model *) handle)->contrast;
}
