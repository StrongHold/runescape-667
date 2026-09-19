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

#if defined(__SSE__) || defined(_M_X64)
#include <xmmintrin.h>
#endif

/** The smallest number of rows or pixels a side is allowed to be divided by. */
static const float LEAST = 1.0e-6f;

/**
 * The reciprocal a side is divided by, taken four at a time.
 *
 * This is the processor's approximation rather than a true division. What it scales is cut to a
 * whole number straight afterwards, so the approximation decides the answer often enough to
 * matter and has to be the same approximation wherever a side is walked. The sides of a shadow
 * are walked by the same rasteriser the picture is, so they are divided the same way.
 */
static inline float reciprocalOfFour(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rcp_ps(_mm_set1_ps(value)));
#else
    return 1.0f / value;
#endif
}

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
     * A distance here runs from nothing at the near plane to one at the far plane, which is what
     * comes out of the picture the model was put through. That matters to anything that reads one
     * back out and does arithmetic on it rather than only comparing it, such as moving everything
     * in a rectangle further away.
     */
    float *depths;

    /**
     * The buffer of distances the toolkit keeps for the window, which is the one it may grow.
     *
     * Drawing into a surface of the client's own borrows that client's buffer instead, and the
     * borrowed one must not be grown or given back, so the two are kept apart.
     */
    float *ownDepths;
    int depthRoom;
} Raster;

extern Raster raster;

/**
 * The distance an untouched pixel is.
 *
 * A distance runs from nothing at the near plane to one at the far plane, so one is as far as
 * anything drawn through the projection ever reaches. It is not merely a large number: a thing
 * handed over at a distance past the far plane is kept out by an untouched pixel, and the
 * particles rely on that without meaning to.
 */
#define FURTHEST 1.0f

/**
 * Points the renderer at a buffer, or at nothing, and opens the clip over all of it.
 */
void rasterUse(uint32_t *pixels, int width, int height);

/**
 * Points the renderer at a surface of the client's own, along with the client's own buffer of
 * distances. Neither is grown and neither is given back here.
 */
void rasterBorrow(uint32_t *pixels, float *depths, int width, int height);

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

/**
 * How the client wants a sprite's own pixels combined with the colour it passes.
 *
 * Every one of these works a byte at a time on all four bytes of a pixel, the top one included, so
 * the colour's own alpha is combined with the sprite's alpha exactly as the other three are.
 */
enum {
    /** Multiply the two, which leaves the sprite as it is when the colour is white. */
    OP_MULTIPLY = 0,
    /** Take the sprite as it stands and ignore the colour. */
    OP_KEEP = 1,
    /** Run between the sprite and the colour, by how much alpha the colour carries. */
    OP_MIX = 2,
    /** Add the colour, holding at white. */
    OP_ADD = 3,
    /** Take the colour away, holding at black. */
    OP_SUBTRACT = 4
};

/** A pixel wholly there, whatever the texture it came from says about its own alpha. */
enum { OPAQUE = 0xFF000000u };

uint32_t blend(uint32_t destination, uint32_t colour, int mode);

/**
 * Fills a rectangle, cut to what may be drawn on.
 */
void fillRect(int x, int y, int width, int height, uint32_t colour, int mode);

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

    /**
     * Where the edges of what may be drawn on sit, counted from the middle of the picture.
     *
     * These are worked out when the client says where the middle is and are not touched again, so
     * narrowing what may be drawn on afterwards does not move them. That is how the toolkit keeps
     * them and it is what anything projecting a point is measured against.
     */
    float leftEdge;
    float rightEdge;
    float topEdge;
    float bottomEdge;
} Projection;

const Projection *projection(void);

/**
 * Puts the middle of the picture at the middle of a surface this size, and works out where its
 * four edges are. The scale is left as it is.
 */
void projectionMiddled(int width, int height);

/**
 * The colour the distance fades everything towards, and how far away the fade is complete.
 */
typedef struct {
    uint32_t colour;
    float range;

    /**
     * How deep a point has to be before the fade begins, and one over what is left of the depth
     * beyond it, both worked out from how far away the fade is complete and where the near and
     * far edges of the world stand.
     *
     * They are kept here rather than worked out as each corner is faded because they change only
     * when the client moves one of the three, and every corner of every face reads them.
     */
    float from;
    float overRest;
} Fog;

const Fog *distanceFog(void);

/**
 * How everything is drawn while the eye is under water.
 *
 * Water fades what is under it towards one colour, and the further below the surface a thing is
 * the more of that colour it takes. Past the depth given nothing shows at all, so a face with a
 * corner that deep is dropped rather than drawn.
 *
 * The fade is worked out per corner of a face and carried across it, which is why the depth is
 * kept as its reciprocal: a corner costs a multiply rather than a divide.
 */
typedef struct {
    /** Whether the eye is under water at all. */
    int under;

    /** Where the surface of the water is, as the client counts height. */
    float surface;

    /** Minus one over the depth, which turns a height below the surface into a fade. */
    float perDepth;

    /** What the water fades everything towards, one channel per lane, each times 256. */
    float towards[4];
} Underwater;

const Underwater *underwater(void);

/**
 * How many triangles have been filled since the eye was last told it is looking through water.
 *
 * Kept only so that the pass the client draws through water can be counted from the client
 * itself, where the harness cannot reach.
 */
long throughWaterFilled(void);

long throughWaterFilledStanding(void);

int wateredHundredths(int most);

long wateredTilesDried(void);

long wateredTilesAbove(void);

int groundTileCarriesDepths(const void *tile);

void throughWaterReset(void);

/** How many tiles the client gave water to have been drawn since the count was last cleared. */
long wateredTilesPainted(void);

void wateredTilesReset(void);

/**
 * Fills a circle, keeping whatever is already nearer than the distance given.
 */
void fillCircle(int x, int y, float depth, int radius, uint32_t colour, int mode);

/**
 * A shape the client draws through, kept as one run of pixels per row.
 *
 * Nothing outside the mask file knows how a mask is laid out. A caller asks what a mask lets
 * through on one row of the buffer and is told where that run starts and how long it is, already
 * brought inside the clip.
 */
int maskRun(const void *held, int row, int across, int down, int *from, int *count);
/** How many rows the shape describes. */
int maskRows(const void *held);
/** The run one row allows, as the client gave it rather than narrowed to what may be drawn on. */
int maskRowRun(const void *held, int row, int across, int down, int *from, int *count);

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

int groundTileSize(const void *ground);
int groundTileShift(const void *ground);
int groundSizeX(const void *ground);
int groundSizeZ(const void *ground);

/** How high the ground is at one corner of the grid. */
int groundHeightAt(const void *ground, int x, int z);

/** How high the ground is at a place between its corners. */
int groundHeightBetween(const void *ground, int x, int z);

/**
 * The way the ground faces at one corner of the grid, worked out from how the height changes
 * either side of it. Four floats: the three parts of the direction and a length of one.
 */
const float *groundCornerNormal(const void *ground, int x, int z);

/** How much of the sun one corner of the grid is kept out of by what stands on it. */
int groundCornerShade(const void *ground, int x, int z);

/** How many faces one tile of the ground is drawn as. */
int groundTileFaces(const void *tile);

/** Which texture a face of the tile wears, or nothing where it wears none. */
int groundTileFaceTexture(const void *tile, int face);

/**
 * How much of a face of this ground wearing this texture shows, or nothing where it is solid.
 *
 * Only the ground drawn above the water is ever seen through, and only where it wears one of the
 * textures that stand for water.
 */
int groundDrawsThrough(const void *ground, int texture);

/** Whether the client handed a tile over with water on it. */
int groundTileWatered(const void *tile);

/** The colour the client gave the water on a tile, whether or not the water reaches it. */
int groundTileWaterColour(const void *tile);

/** How much of the water stands over a corner, out of the whole. */
float groundTileCornerUnder(const void *tile, int corner);
int groundTileCornerDepth(const void *tile, int corner);
int groundTileFaceHollow(const void *tile, int face);

int groundTileFaceBare(const void *tile, int face);
int groundTilePlanColour(const void *tile, int corner, uint32_t *colour);

/** How much of the world one whole width of a face's texture covers. */
int groundTileFaceSize(const void *tile, int face);
int groundTileCornerTexture(const void *tile, int corner);
int groundTileCornerSize(const void *tile, int corner);

/**
 * The picture of the shadow over one tile, worked out again where a shadow has moved over it.
 *
 * Nothing comes back for ground that keeps no shadows. What does is read the way a texture is,
 * two hundred and fifty six places across, at the place a corner sits on its texture shifted
 * down by however much is handed back.
 */
const unsigned char *groundTileShadow(const void *ground, void *tile, int x, int z, int *shift);

/** Where one corner of the tile sits in its own square, and what colour it is. */
void groundTilePlanCorner(const void *tile, int corner, int *across, int *along,
                          uint32_t *colour);

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
 * How far the sun leans over one unit of height, in two hundred and fifty sixths.
 *
 * A shadow is the model flattened straight down and then slid by however far the sun leans, so
 * these two numbers are all the shadow work needs of the sun's direction.
 */
int sunLeanAcross(void);
int sunLeanAlong(void);

/**
 * What the sun's lean is held out of, which is how far it leans over one unit of height.
 *
 * A lean is taken back out by a shift rather than a divide. A model below the height its shadow
 * is cast from counts as a negative height, and the two round such a number different ways.
 */
enum { SUN_LEAN_SHIFT = 8, SUN_LEAN_WHOLE = 1 << SUN_LEAN_SHIFT };

/**
 * How coarsely a shadow is drawn, as the number of places a world distance is shifted down by.
 *
 * The client asks for a resolution and the toolkit keeps the shift that reaches it, so that
 * turning a world distance into a shadow distance is a shift rather than a divide.
 */
int shadowShift(void);

/**
 * A shadow the client hands between a model and the ground.
 *
 * A shadow is the model seen from straight above: one byte per place, holding how many of the
 * model's faces stand over it. The client asks a model for one, adds it to the ground under the
 * model, and takes it away again when the model moves.
 */
typedef struct Shadow Shadow;

/**
 * A shadow of the model, using the one given where it is large enough and building one where it
 * is not. The one given is returned when it was reused.
 */
Shadow *shadowOfModel(void *model, Shadow *reuse);

/** Releases a shadow. */
void shadowRelease(Shadow *shadow);

/** Where the shadow sits, in shadow places, relative to the model's own middle. */
int shadowLeft(const Shadow *shadow);
int shadowTop(const Shadow *shadow);

/** How far the shadow reaches, which is one short of how many places it holds. */
int shadowAcross(const Shadow *shadow);
int shadowDown(const Shadow *shadow);

const unsigned char *shadowPlaces(const Shadow *shadow);

/** How many places the shadow has room for, which reuse is judged against. */
int shadowRoom(const Shadow *shadow);

/**
 * Remembers the toolkit object the client is driving, so that a native can ask it to build an
 * object the client owns the class of.
 */
void toolkitReady(JNIEnv *env, jobject self);

/** An empty shadow object for a native to fill in and hand back to the client. */
jobject toolkitShadowObject(JNIEnv *env);

Shadow *shadowNew(int width, int height);
int shadowCanHold(const Shadow *shadow, int width, int height);
void shadowClear(Shadow *shadow);
void shadowSetBounds(Shadow *shadow, int left, int top, int right, int bottom);

/** Marks the places one triangle of the flattened model stands over. */
void shadowMarkTriangle(Shadow *shadow, int downA, int downB, int downC,
                        int acrossA, int acrossB, int acrossC);

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
 * The same colour once the texture the face wears has had its say, which is how far the colour is
 * carried towards a grey and how much what is left is brightened.
 */
uint32_t texturedUnlitColour(uint32_t unlit, int ambient, int towardsGrey, int brighten);

/**
 * The colour a surface facing this way takes, given what it looks like unlit.
 */
uint32_t sunlitColour(uint32_t unlit, const Normal *normal, float strength);

/**
 * How many lights the toolkit may be given a place for. Asking for more keeps the first four.
 */
enum { POINT_LIGHTS = 4 };

/**
 * A light with a place in the world, as opposed to the sun, which only has a direction.
 *
 * The place is kept as four floats so that it can be read a whole register at a time, and the
 * fourth is never looked at. The reach is the range squared, scaled, which is the only form the
 * light is ever used in.
 */
typedef struct {
    float place[4];
    float reach;
    uint32_t colour;
} PointLight;

/**
 * How many lights the client has given places for, between none and four.
 */
int pointLightCount(void);

const PointLight *pointLight(int which);

/**
 * The colour a corner takes once the lights near it are added to the colour it already has.
 *
 * The places are the lights brought into the model's own frame, one per light, because a model
 * is drawn with its vertices where the model keeps them rather than where they end up.
 */
uint32_t pointLitColour(uint32_t colour, const float *place, const Normal *normal,
    const float places[][4]);

/**
 * What the client knows about a texture, apart from its pixels.
 *
 * The client hands these over whole, and the toolkit keeps them under the names the client gave
 * them. Three have a settled meaning: `alphaBlendMode` of one says the texture has no alpha, so
 * an empty texel of it stays empty rather than being gathered into its neighbours; and the two
 * repeat flags say which way round a texture carries on past its own edge.
 */
typedef struct {
    /**
     * The one colour that stands for the whole texture, packed the way the client packs a colour.
     * It is what a textured face is painted in where the texture itself is not drawn, which is
     * what the plan view of the ground does.
     */
    unsigned short averageColour;
    int alphaBlendMode;
    unsigned char effectType;
    unsigned char effectParam1;
    int effectParam2;
    int small;
    unsigned char alpha;
    unsigned char aByte57;

    /** How far the texture slides each way every hundredth of a second, or nought for still. */
    signed char speedU;
    signed char speedV;

    int disableable;
    int aBoolean234;
    int aBoolean239;
    int repeatsU;
    int repeatsV;
    unsigned char aByte53;
    int aBoolean237;
    int aBoolean238;
    int colourOp;
} TextureMetrics;

typedef struct Texture Texture;

/**
 * The texture the client gave this number, asking the client for it if the toolkit has never
 * been given it, or null when the client has none to give.
 *
 * The client names a texture by an unsigned short, and the largest of those means none, so a
 * number outside that range is answered with nothing rather than looked for.
 */
const Texture *textureFor(int texture);

/**
 * What the client knows about a texture, whether or not its pixels have ever been handed over.
 */
const TextureMetrics *textureMetricsFor(int texture);

const uint32_t *texturePixels(const Texture *texture);
const TextureMetrics *textureMetrics(const Texture *texture);
/** How far a texture that slides has slid by now, which the renderer reads it through. */
void textureOffsets(const Texture *texture, float *u, float *v);
/** How many pixels apart two rows of one texture are, which is not how wide a texture is. */
enum { TEXTURE_STRIDE = 256 };

/** How wide one texture is, and how far one row of the run they are kept in is from the next. */
enum { TEXTURE_SIDE = 128, TEXTURE_SHIFT = 8 };

/**
 * Draws a texture stretched over a rectangle, over whatever is further from the eye than it.
 */
void drawTextureOverRect(const uint32_t *from, int x, int y, int wide, int high, float depth,
                         int op, int colour, int mode, int carriesItsOwnAlpha);

/**
 * Makes room for every texture the client may hand over, and remembers the object to ask when it
 * has handed over none.
 */
void textureCacheReady(JNIEnv *env, jobject client);

/** Moves every texture that slides on to where it stands at this moment. */
void textureCacheService(int time);

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

/**
 * The sine and cosine of an angle measured in sixteen thousand three hundred and eighty four
 * steps of a circle, read from the same single precision table every angle in the toolkit is
 * taken from.
 */
float sineOf(int angle);
float cosineOf(int angle);

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
/** The texture each face wears, or null when no face wears one. */
const short *modelFaceTexture(const void *handle);
/**
 * Where each corner of each face sits on the texture it wears, six floats to a face, or null when
 * the mesh named no texture space and every face is given the whole of its texture instead.
 */
const float *modelFaceUV(const void *handle);
/**
 * Whether the client built this model to be drawn with the directions its vertices face.
 *
 * A model built without that is never reached by a light with a place, however many places the
 * client has given the toolkit.
 */
/**
 * The vertices the client hangs particles off, three for every emitter and then one for every
 * effector, in one flat run.
 */
int modelParticleCount(const void *handle);
const int *modelParticleVertices(const void *handle);

int modelNeedsNormals(const void *handle);

const Normal *modelNormals(const void *handle);
const Normal *modelFaceNormals(const void *handle);
const uint32_t *modelShade(void *handle);
/** The least and most of each axis in turn, measured again first if anything has moved. */
void modelBounds(void *handle, int *into);
/** How wide the model is about its upright axis. */
int modelRadius(void *handle);
/**
 * How much of a face the client asked to be drawn through what is behind it, counted the other
 * way round: nothing means solid.
 */
int modelFaceAlpha(const void *model, int face);

/**
 * The square the client hangs off a face and keeps turned towards the eye, or nothing where the
 * face has none.
 */
int modelFaceBillboard(const void *handle, int face);
void modelBillboard(const void *handle, int which, int *face, int *wide, int *high,
                    int *texture, int *colourOp, int *blendMode, int *insteadOfTheFace);

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
/**
 * A buffer of how far away each pixel is, belonging to a surface the client draws into rather
 * than to the window.
 */
int spriteWidthOf(const void *handle);
int spriteHeightOf(const void *handle);
uint32_t *spritePixelsOf(void *handle);

int distanceBufferWidth(const void *handle);
int distanceBufferHeight(const void *handle);
float *distanceBufferRows(void *handle);

/**
 * Whether a named part of the renderer has been switched off from outside, so that a picture the
 * client draws can be taken apart a layer at a time. Each is asked about once and remembered.
 */
int switchedOff(const char *name);

jlong nativeIdOf(JNIEnv *env, jobject owner);
void setNativeId(JNIEnv *env, jobject owner, jlong value);

#endif
