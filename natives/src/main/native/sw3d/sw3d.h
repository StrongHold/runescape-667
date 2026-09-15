/*
 * The software toolkit.
 *
 * One renderer serves the whole client. The toolkit object carries no handle of its own, which is
 * why `oa.nativeid` and `xa.nativeid` are declared final zero and never written, so the state the
 * renderer needs lives here rather than behind a handle.
 *
 * Everything the toolkit draws goes into the current surface's back buffer. A surface belongs to
 * one AWT canvas, owns its pixels, and presents them when the client asks it to.
 */

#ifndef SW3D_H
#define SW3D_H

#include <jni.h>
#include <stddef.h>
#include <stdint.h>

typedef struct Surface Surface;
typedef struct Pool Pool;

/**
 * The memory a model's geometry is kept in. Everything taken from a pool is given back at once.
 */
void *poolTake(Pool *pool, size_t size);
void poolRelease(Pool *pool);

/**
 * The back buffer being drawn into, which is the current surface's. Every drawing native writes
 * through this rather than reaching for the surface, because that is the only thing they need
 * from it.
 */
typedef struct {
    uint32_t *pixels;
    int width;
    int height;

    /** What may be drawn on, as a half open rectangle. Resetting it opens the whole buffer. */
    int clipLeft;
    int clipTop;
    int clipRight;
    int clipBottom;
} Raster;

extern Raster raster;

/**
 * Points the renderer at a buffer, or at nothing, and opens the clip over all of it.
 */
void rasterUse(uint32_t *pixels, int width, int height);

void rasterResetClip(void);

/**
 * How the client asks for a colour to be put down. It passes the alpha the blending mode uses in
 * the top byte of the colour itself.
 */
enum {
    BLEND_OPAQUE = 0,
    BLEND_ALPHA = 1,
    BLEND_ADD = 2
};

uint32_t blend(uint32_t destination, uint32_t colour, int mode);

/**
 * The colour a packed hue, saturation and lightness stands for.
 */
uint32_t colourOf(int packed);

/**
 * Where the client wants the middle of the picture, how wide a field it wants through it, and
 * how close and how far a thing may be before it is cut away.
 */
typedef struct {
    float centreX;
    float centreY;
    float scaleX;
    float scaleY;
    int near;
    int far;
} Projection;

const Projection *projection(void);

/**
 * The light everything is shaded by. The direction is kept with a length of one, as the toolkit
 * keeps it, and the two strengths say how much light a face gets when it faces the sun and when
 * it faces away.
 */
typedef struct {
    float x;
    float y;
    float z;
    float intensity;
    float reverseIntensity;
    unsigned char red;
    unsigned char green;
    unsigned char blue;
} Sun;

const Sun *sun(void);

/**
 * The matrix the world is seen through, or null before the client has given one.
 */
const void *cameraMatrix(void);

/**
 * Puts a point through a matrix, answering the four floats before they are rounded.
 */
void matrixTransform(const void *matrix, float x, float y, float z, float *into);

/**
 * Puts one matrix after another, into a third.
 */
void matrixCompose(const void *first, const void *second, void *into);

size_t matrixSize(void);

/** What a model holds, read without knowing how it is laid out. */
int modelVertexCount(const void *handle);
int modelFaceCount(const void *handle);
const int *modelVertexX(const void *handle);
const int *modelVertexY(const void *handle);
const int *modelVertexZ(const void *handle);
const short *modelFaceA(const void *handle);
const short *modelFaceB(const void *handle);
const short *modelFaceC(const void *handle);
const short *modelFaceColour(const void *handle);

Surface *surfaceCreate(JNIEnv *env, jobject canvas, int width, int height);
void surfaceResize(Surface *surface, int width, int height);
void surfacePresent(Surface *surface, int x, int y);
void surfaceFree(Surface *surface);
uint32_t *surfacePixels(Surface *surface);
int surfaceWidth(Surface *surface);
int surfaceHeight(Surface *surface);

/**
 * The handle an object of the toolkit carries, which every class in it names `nativeid`.
 */
jlong nativeIdOf(JNIEnv *env, jobject owner);
void setNativeId(JNIEnv *env, jobject owner, jlong value);

#endif
