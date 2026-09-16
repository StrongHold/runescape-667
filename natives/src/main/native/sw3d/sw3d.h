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

    /**
     * How far away each pixel drawn so far is, so that a nearer face covers one behind it wherever
     * they overlap rather than only when it happens to be drawn later.
     *
     * Sorting whole faces cannot answer this. Two faces that pass through each other, or three
     * that overlap in a ring, have no order that is right everywhere, and a model made of flat
     * faces has plenty of both. The buffer belongs to the picture rather than to one model, so
     * everything drawn between two clears is measured against everything else.
     *
     * This holds the distance from the eye rather than the distance the toolkit holds, which runs
     * from nothing at the near plane to one at the far plane. The two put the same pixel in front
     * of the same pixel, so what is drawn is the same, but anything that reads a distance back out
     * and does arithmetic on it, such as fog, needs the toolkit's.
     */
    float *depths;
    int depthRoom;
} Raster;

extern Raster raster;

/** The distance an untouched pixel is, which is further than anything can be drawn. */
#define FURTHEST 3.4e38f

/**
 * Points the renderer at a buffer, or at nothing, and opens the clip over all of it.
 */
void rasterUse(uint32_t *pixels, int width, int height);

void rasterResetClip(void);

/**
 * Sets every distance in a rectangle, clipped, to one value.
 */
void depthClear(int left, int top, int width, int height, float value);

float *depthRow(int y);

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

    /**
     * The client hands these over as whole numbers and the toolkit keeps them as floats, so a far
     * plane the client set to the largest whole number there is comes back as the smallest. That
     * is kept.
     */
    float near;
    float far;
} Projection;

const Projection *projection(void);

/**
 * The colour the distance fades everything towards, and how far away the fade is complete.
 */
typedef struct {
    uint32_t colour;
    float range;
} Fog;

const Fog *distanceFog(void);

/**
 * A shape the client draws through, kept as one run of pixels per row.
 *
 * Nothing outside the mask file knows how a mask is laid out. A caller asks what a mask lets
 * through on one row of the buffer and is told where that run starts and how long it is, already
 * brought inside the clip.
 */
int maskRun(const void *held, int row, int across, int down, int *from, int *count);

void maskFree(void *held);

/**
 * One tile of the ground, and where its corners sit in the world.
 *
 * A tile keeps its corners relative to its own square, so the tile it belongs to has to be named
 * when a corner is asked for. Nothing outside the ground file knows how a tile is laid out.
 */
const void *groundTile(const void *held, int x, int z, int *corners);
void groundTileCorner(const void *held, const void *at, int corner, int tileSize,
                      int x, int z, int *into, uint32_t *colour);
int groundTileSize(const void *held);

/** Draws one tile of the ground, or one depth of it. */
void renderGroundTile(const void *ground, int x, int z);

/** The pool a model's geometry is taken from, or null before the client has given one. */
Pool *modelPoolInUse(void);

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
 * How much light everything gets before the sun is taken into account.
 */
float globalAmbient(void);

/** A vertex normal, kept as the sum of the unit normals of the faces meeting there. */
typedef struct {
    float x;
    float y;
    float z;
    float magnitude;
} Normal;

/**
 * The colour a face is before any light reaches it: its lightness scaled by the model's own
 * ambient and held away from both ends of the range.
 */
uint32_t unlitColour(int hsl, int ambient);

/**
 * The colour a surface facing this way takes, given what it looks like unlit.
 */
uint32_t sunlitColour(uint32_t unlit, const Normal *normal, float strength);

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

/** The sixteen floats a matrix holds, a row at a time. */
const float *matrixRows(const void *handle);

/** What a model holds, read without knowing how it is laid out. */
int modelVertexCount(const void *handle);
int modelFaceCount(const void *handle);
/** Where every vertex is, four floats apart, the fourth of them unused. */
const float *modelVertices(const void *handle);
enum { MODEL_VERTEX_STRIDE = 4 };
const short *modelFaceA(const void *handle);
const short *modelFaceB(const void *handle);
const short *modelFaceC(const void *handle);
const short *modelFaceColour(const void *handle);
const Normal *modelNormals(const void *handle);
const Normal *modelFaceNormals(const void *handle);
const uint32_t *modelShade(const void *handle);
int modelFaceIsFlat(const void *handle, int face);
int modelAmbient(const void *handle);
int modelContrast(const void *handle);

/** How much memory the toolkit is holding, which the client watches and reports. */
size_t allocatedSize(void);
void allocatedGrew(size_t bytes);
void allocatedShrank(size_t bytes);

/** How many models have been built since the client last asked. */
int modelsBuiltSinceAsked(void);
void modelWasBuilt(void);

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
