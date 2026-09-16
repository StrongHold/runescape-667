/*
 * The renderer the toolkit object stands in front of.
 *
 * There is one of it. The toolkit carries no handle, so the state lives here, and a client that
 * builds several toolkits is driving the same renderer through each of them.
 */

#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

Raster raster;

void rasterResetClip(void) {
    raster.clipLeft = 0;
    raster.clipTop = 0;
    raster.clipRight = raster.width;
    raster.clipBottom = raster.height;
}

void rasterUse(uint32_t *pixels, int width, int height) {
    raster.pixels = pixels;
    raster.width = width;
    raster.height = height;
    rasterResetClip();

    int wanted = width * height;
    if (raster.depthRoom < wanted) {
        float *grown = realloc(raster.ownDepths, (size_t) wanted * sizeof(float));
        if (grown != NULL) {
            allocatedGrew((size_t) (wanted - raster.depthRoom) * sizeof(float));
            raster.ownDepths = grown;
            raster.depthRoom = wanted;
        }
    }

    raster.depths = raster.ownDepths;
    depthClear(0, 0, width, height, FURTHEST);
}

void rasterBorrow(uint32_t *pixels, float *depths, int width, int height) {
    raster.pixels = pixels;
    raster.depths = depths;
    raster.width = width;
    raster.height = height;
    rasterResetClip();
}

float *depthRow(int y) {
    return raster.depths + (size_t) y * (size_t) raster.width;
}

void depthClear(int left, int top, int width, int height, float value) {
    if (raster.depths == NULL) {
        return;
    }

    if (left < raster.clipLeft) {
        width -= raster.clipLeft - left;
        left = raster.clipLeft;
    }
    if (top < raster.clipTop) {
        height -= raster.clipTop - top;
        top = raster.clipTop;
    }
    if (left + width > raster.clipRight) {
        width = raster.clipRight - left;
    }
    if (top + height > raster.clipBottom) {
        height = raster.clipBottom - top;
    }

    for (int y = top; y < top + height; y++) {
        float *row = depthRow(y);
        for (int x = left; x < left + width; x++) {
            row[x] = value;
        }
    }
}

/**
 * How much memory the toolkit is holding.
 *
 * The client watches this and drops what it can when it grows too large, so it is a count of what
 * the toolkit asked the system for rather than of what any one thing needs.
 */
static size_t allocated;

size_t allocatedSize(void) {
    return allocated;
}

void allocatedGrew(size_t bytes) {
    allocated += bytes;
}

void allocatedShrank(size_t bytes) {
    allocated = bytes > allocated ? 0 : allocated - bytes;
}

static int modelsBuilt;

void modelWasBuilt(void) {
    modelsBuilt++;
}

int modelsBuiltSinceAsked(void) {
    int count = modelsBuilt;
    modelsBuilt = 0;
    return count;
}

/**
 * Where the client wants the middle of the picture, and how wide a field it wants through it.
 * Nothing reads these yet; they are kept because every projection the toolkit does is relative
 * to them.
 */
static Projection view;

static float ambient;

static jlong camera;

const Projection *projection(void) {
    return &view;
}

const void *cameraMatrix(void) {
    return (const void *) (intptr_t) camera;
}

/** Where a shadow's detail was last set to. Nothing reads it, because nothing draws a shadow. */
static int shadowResolution;

/** What the client last asked for the recording of distance. Nothing reads it either. */
static int depthWriteAsked;

static Fog fog;

static Pool *modelPool;

static Sun light;

float globalAmbient(void) {
    return ambient;
}

const Sun *sun(void) {
    return &light;
}

/**
 * Sets the light everything is shaded by.
 *
 * The direction is stored with a length of one so that shading is a plain dot product later, and
 * the colour is split into its three bytes because that is how it is used.
 */
JNIEXPORT void JNICALL Java_oa_ZA(JNIEnv *env, jobject self, jint colour, jfloat intensity,
                                   jfloat reverseIntensity, jfloat x, jfloat y, jfloat z) {
    (void) env;
    (void) self;

    light.red = (unsigned char) (colour >> 16);
    light.green = (unsigned char) (colour >> 8);
    light.blue = (unsigned char) colour;

    float length = sqrtf(x * x + y * y + z * z);
    float scale = 1.0f / length;

    light.x = x * scale;
    light.y = y * scale;
    light.z = z * scale;
    light.intensity = intensity;
    light.reverseIntensity = reverseIntensity;
}

JNIEXPORT void JNICALL Java_oa_MA(JNIEnv *env, jobject self, jobject textures,
                                   jint a2, jint a3) {
    (void) env;
    (void) self;
    (void) textures;
    (void) a2;
    (void) a3;

    rasterUse(NULL, 0, 0);
}

JNIEXPORT void JNICALL Java_oa_ma(JNIEnv *env, jobject self, jlong matrix) {
    (void) env;
    (void) self;

    camera = matrix;
}

/**
 * Points the renderer at the surface it should draw into, or at nothing when the client takes
 * its canvas away.
 */
JNIEXPORT void JNICALL Java_oa_t(JNIEnv *env, jobject self, jobject canvas) {
    (void) self;

    Surface *surface = (Surface *) (intptr_t) nativeIdOf(env, canvas);
    if (surface == NULL) {
        rasterUse(NULL, 0, 0);
    } else {
        rasterUse(surfacePixels(surface), surfaceWidth(surface), surfaceHeight(surface));
    }
}

/**
 * Opens the clip over the whole buffer again.
 */
JNIEXPORT void JNICALL Java_oa_la(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    rasterResetClip();
}

JNIEXPORT void JNICALL Java_oa_DA(JNIEnv *env, jobject self, jint x, jint y,
                                   jint width, jint height) {
    (void) env;
    (void) self;

    view.centreX = (float) x;
    view.centreY = (float) y;
    view.scaleX = (float) width;
    view.scaleY = (float) height;

    view.leftEdge = (float) raster.clipLeft - view.centreX;
    view.rightEdge = (float) raster.clipRight - view.centreX;
    view.topEdge = (float) raster.clipTop - view.centreY;
    view.bottomEdge = (float) raster.clipBottom - view.centreY;
}

/**
 * How close and how far a thing may be before it is cut away. Nothing is drawn until the client
 * has said, and it does not complain when it has not.
 */
void projectionMiddled(int width, int height) {
    view.centreX = (float) width * 0.5f;
    view.centreY = (float) height * 0.5f;

    view.leftEdge = 0.0f - view.centreX;
    view.rightEdge = (float) width - view.centreX;
    view.topEdge = 0.0f - view.centreY;
    view.bottomEdge = (float) height - view.centreY;
}

JNIEXPORT void JNICALL Java_oa_f(JNIEnv *env, jobject self, jint near, jint far) {
    (void) env;
    (void) self;

    view.near = (float) near;
    view.far = (float) far;
}

/** How close a thing may come before it is cut away, back as the whole number it started as. */
JNIEXPORT jint JNICALL Java_oa_i(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    return (jint) view.near;
}

JNIEXPORT jint JNICALL Java_oa_XA(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    return (jint) view.far;
}

JNIEXPORT jint JNICALL Java_oa_E(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    return (jint) allocatedSize();
}

JNIEXPORT jint JNICALL Java_oa_M(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    return modelsBuiltSinceAsked();
}

/**
 * How many faces were drawn, which the client shows in its own count of what a frame cost.
 *
 * The toolkit this replaces never counted them and answers nothing, so the client's count has
 * always read zero on this renderer. That is kept.
 */
JNIEXPORT jint JNICALL Java_oa_I(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    return 0;
}

/**
 * Moves everything already drawn, and how far away it all is, by a whole number of pixels.
 *
 * Both buffers are moved together and nothing is put in the room left behind, so the client
 * draws over that itself. A move of one row plus a few columns is one move of the whole buffer
 * rather than a move per row, which is why this takes a distance rather than a rectangle.
 */
JNIEXPORT void JNICALL Java_oa_F(JNIEnv *env, jobject self, jint x, jint y) {
    (void) env;
    (void) self;

    if (raster.pixels == NULL) {
        return;
    }

    int shift = y * raster.width + x;
    int held = raster.width * raster.height;

    if (shift > 0) {
        int count = held - shift;
        for (int at = count - 1; at >= 0; at--) {
            raster.pixels[at + shift] = raster.pixels[at];
            if (raster.depths != NULL) {
                raster.depths[at + shift] = raster.depths[at];
            }
        }
    } else if (shift < 0) {
        int count = held + shift;
        for (int at = 0; at < count; at++) {
            raster.pixels[at] = raster.pixels[at - shift];
            if (raster.depths != NULL) {
                raster.depths[at] = raster.depths[at - shift];
            }
        }
    }
}

/**
 * Narrows what may be drawn on. It only ever narrows: a rectangle wider than the one already set
 * leaves that one alone. Opening it again is what resetting the clip is for.
 */
JNIEXPORT void JNICALL Java_oa_T(JNIEnv *env, jobject self, jint left, jint top,
                                  jint right, jint bottom) {
    (void) env;
    (void) self;

    if (raster.clipLeft < left) {
        raster.clipLeft = left;
    }
    if (raster.clipTop < top) {
        raster.clipTop = top;
    }
    if (raster.clipRight > right) {
        raster.clipRight = right;
    }
    if (raster.clipBottom > bottom) {
        raster.clipBottom = bottom;
    }
}

/**
 * Whether a face that is drawn records how far away it is.
 *
 * The client asks for this and the software renderer does not answer: it records the distance of
 * everything it draws, whichever way the flag is set. The flag is kept because the client can ask
 * for it back, and for no other reason.
 */
JNIEXPORT void JNICALL Java_oa_C(JNIEnv *env, jobject self, jboolean write) {
    (void) env;
    (void) self;

    depthWriteAsked = write == JNI_TRUE;
}

/**
 * Forgets how far away everything is, over the whole buffer.
 */
JNIEXPORT void JNICALL Java_oa_ya(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    depthClear(0, 0, raster.width, raster.height, FURTHEST);
}

/**
 * Moves everything already drawn in a rectangle further away, by a distance the client gives.
 *
 * The client draws the world from above by scrolling what it drew last frame and filling in only
 * the strip that came into view. When the camera rises or falls, everything it kept was recorded
 * against a camera at the old height, so rather than draw it all again the client says how far
 * the camera moved and every distance in the rectangle moves with it.
 *
 * The rectangle is walked with its own width taken as the width of the buffer, so the first
 * distance touched is not the one at the corner the client named unless the rectangle is the full
 * width. The client only ever asks for the full width. This is kept on purpose, do not correct
 * it.
 */
JNIEXPORT void JNICALL Java_oa_b(JNIEnv *env, jobject self, jint x, jint y, jint width,
                                  jint height, jdouble away) {
    (void) env;
    (void) self;

    if (raster.depths == NULL) {
        return;
    }

    const Projection *view = projection();
    float step = (float) away / (view->far - view->near);

    float *at = raster.depths + (size_t) (y * width + x);

    for (int row = 0; row < height; row++) {
        for (int column = 0; column < width; column++) {
            at[column] += step;
        }
        at += raster.width;
    }
}

/**
 * The colour the distance fades everything towards, and how far away the fade is complete. The
 * client offers a third number that the toolkit has never read.
 */
JNIEXPORT void JNICALL Java_oa_L(JNIEnv *env, jobject self, jint colour, jint range, jint offset) {
    (void) env;
    (void) self;
    (void) offset;

    fog.colour = (uint32_t) colour;
    fog.range = range < 0 ? 0.0f : (float) range;
}

const Fog *distanceFog(void) {
    return &fog;
}

/**
 * How finely a shadow is drawn. This toolkit draws none, so the number is only remembered.
 */
JNIEXPORT void JNICALL Java_oa_X(JNIEnv *env, jobject self, jint resolution) {
    (void) env;
    (void) self;

    shadowResolution = resolution;
}

/**
 * Lets the toolkit do whatever it keeps for quiet moments. There is nothing it keeps.
 */
JNIEXPORT void JNICALL Java_oa_d(JNIEnv *env, jobject self, jint budget) {
    (void) env;
    (void) self;
    (void) budget;
}

/**
 * Stops drawing everything as though it were seen through water. Nothing here ever started.
 */
JNIEXPORT void JNICALL Java_oa_pa(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;
}

/**
 * Draws a piece of ground. The toolkit this replaces does nothing here, and the client draws its
 * ground through the ground natives instead.
 */
JNIEXPORT void JNICALL Java_oa_Q(JNIEnv *env, jobject self, jint x, jint y, jint width,
                                  jint height, jint overlay, jint underlay, jbyteArray shape,
                                  jint size, jint mode) {
    (void) env;
    (void) self;
    (void) x;
    (void) y;
    (void) width;
    (void) height;
    (void) overlay;
    (void) underlay;
    (void) shape;
    (void) size;
    (void) mode;
}

/**
 * The pool a model's geometry is taken from.
 */
JNIEXPORT void JNICALL Java_oa_va(JNIEnv *env, jobject self, jobject pool) {
    (void) self;

    modelPool = (Pool *) (intptr_t) nativeIdOf(env, pool);
}

Pool *modelPoolInUse(void) {
    return modelPool;
}

/**
 * Lets go of everything the toolkit holds. The client calls this when it is shutting down and
 * when it is changing which toolkit it draws through.
 */
static void releaseEverything(void) {
    free(raster.ownDepths);
    raster.ownDepths = NULL;
    raster.depths = NULL;
    raster.depthRoom = 0;
    rasterUse(NULL, 0, 0);
    camera = 0;
    modelPool = NULL;
}

JNIEXPORT void JNICALL Java_oa_FA(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;

    releaseEverything();
}

JNIEXPORT void JNICALL Java_oa_w(JNIEnv *env, jobject self, jboolean unused) {
    (void) env;
    (void) self;
    (void) unused;

    releaseEverything();
}

JNIEXPORT void JNICALL Java_oa_xa(JNIEnv *env, jobject self, jfloat globalAmbient) {
    (void) env;
    (void) self;

    ambient = globalAmbient;
}

/**
 * Fills the whole back buffer. The client passes a colour with no alpha in it, and the surface
 * ignores the top byte, so it is written straight through.
 */
JNIEXPORT void JNICALL Java_oa_GA(JNIEnv *env, jobject self, jint colour) {
    (void) env;
    (void) self;

    if (raster.pixels == NULL) {
        return;
    }

    size_t count = (size_t) raster.width * (size_t) raster.height;
    uint32_t value = (uint32_t) colour;

    for (size_t i = 0; i < count; i++) {
        raster.pixels[i] = value;
    }
}

static PointLight lights[POINT_LIGHTS];
static int litCount;

int pointLightCount(void) {
    return litCount;
}

const PointLight *pointLight(int which) {
    return &lights[which];
}

/**
 * Gives the toolkit the lights that have a place in the world.
 *
 * Five whole numbers describe each light: where it is, how far it reaches, and what colour it
 * is. The strengths come separately as floats, one per light, and a strength above one is taken
 * as one.
 *
 * Only the first four are kept. A count above four is cut to four, and the strength array is
 * read for as many lights as are kept.
 */
JNIEXPORT void JNICALL Java_oa_N(JNIEnv *env, jobject self, jint count, jintArray described,
        jfloatArray strengths) {
    (void) self;

    int kept = count < POINT_LIGHTS ? count : POINT_LIGHTS;
    litCount = kept;

    if (kept <= 0 || described == NULL || strengths == NULL) {
        return;
    }

    jint *places = (*env)->GetIntArrayElements(env, described, NULL);
    jfloat *strong = (*env)->GetFloatArrayElements(env, strengths, NULL);

    if (places != NULL && strong != NULL) {
        for (int light = 0; light < kept; light++) {
            const jint *described5 = &places[(size_t) light * 5];
            PointLight *into = &lights[light];

            into->place[0] = (float) described5[0];
            into->place[1] = (float) described5[1];
            into->place[2] = (float) described5[2];
            into->place[3] = 0.0f;

            /* One is the most a strength counts for, and a strength that is not a number is kept. */
            float held = 1.0f < strong[light] ? 1.0f : strong[light];
            uint32_t range = (uint32_t) described5[3];

            into->reach = (float) (int32_t) (range * range) * held * 256.0f;
            into->colour = (uint32_t) described5[4];
        }
    }

    if (places != NULL) {
        (*env)->ReleaseIntArrayElements(env, described, places, JNI_ABORT);
    }

    if (strong != NULL) {
        (*env)->ReleaseFloatArrayElements(env, strengths, strong, JNI_ABORT);
    }
}
