/*
 * The ground.
 *
 * The world's terrain is a grid of tiles, each of which the client hands over as a list of
 * triangle corners once and which is then drawn many times as the camera moves. A tile carries its
 * corners' places in its own square, their heights, and the colour and texture each corner takes,
 * and the ground carries the heights of the whole grid so that a corner between two tiles sits at
 * the same height in both.
 *
 * A corner's place is kept relative to its own tile rather than to the world. The client builds
 * tiles once and the world is far larger than a short can hold, so a corner that knew where it was
 * in the world could not be a short, and a tile is drawn by putting its own square in place first.
 */

#include <stdlib.h>
#include <math.h>
#include <string.h>

#include "sw3d.h"

/**
 * What one tile holds. Every one of these runs three to a face, in the order the client gave them.
 */
typedef struct {
    int corners;
    int faces;

    int16_t *across;
    int16_t *up;
    int16_t *along;

    uint32_t *colour;
    int16_t *texture;
    int16_t *size;
    unsigned char *light;

    /** How deep the water over the corner is, or nothing where the tile is not underwater. */
    int16_t *depth;

    /** Whether the tile was handed over as one that casts a shadow of its own. */
    int shadowed;
} Tile;

/** A light the client has put in the world, which brightens the tiles around it. */
typedef struct {
    int which;
    int across;
    int up;
    int along;
    int range;
    int strength;
    float intensity;
} Light;

typedef struct {
    int sizeX;
    int sizeZ;
    int tileSize;
    int tileShift;
    int groundFlags;
    int featureFlags;

    /** The height of every corner of the grid, one more each way than there are tiles. */
    int *heights;

    Tile **tiles;

    Light *lights;
    int lightCount;
    int lightRoom;

    /**
     * How much of the sun each place on the ground is kept out of, one byte per place, counting
     * how many models stand over it.
     *
     * The client adds a model's shadow to this when the model is put down and takes it away again
     * when the model moves, so the ground carries the shadows of everything standing on it and
     * never has to look at the models themselves.
     */
    unsigned char *shade;
    int shadeAcross;
    int shadeDown;

    /** Which tiles have had a shadow move over them since their picture was last worked out. */
    unsigned char *reshade;

    /**
     * The way the ground faces at every corner of the grid, four floats each, and how much of
     * the sun each corner is kept out of.
     */
    float *normals;
    unsigned char *corners;
} Ground;

/** How many floats one corner's direction takes: the three parts and a length. */
enum { NORMAL_PARTS = 4 };

/** How many channels of a colour the sun reaches. */
enum { CHANNELS_LIT = 3 };

static Ground *groundOf(JNIEnv *env, jobject self) {
    return (Ground *) (intptr_t) nativeIdOf(env, self);
}

static Tile **tileAt(Ground *ground, int x, int z) {
    return &ground->tiles[(size_t) x * (size_t) ground->sizeZ + (size_t) z];
}

static int heightAt(const Ground *ground, int x, int z) {
    return ground->heights[(size_t) x * (size_t) (ground->sizeZ + 1) + (size_t) z];
}

/**
 * How high the ground is between the corners of a tile, which is where a corner the client put
 * inside a tile sits.
 */
static int averageHeight(const Ground *ground, int across, int along) {
    int x = across >> ground->tileShift;
    int z = along >> ground->tileShift;

    if (x < 0 || z < 0 || x > ground->sizeX - 1 || z > ground->sizeZ - 1) {
        return 0;
    }

    int intoX = (ground->tileSize - 1) & across;
    int intoZ = (ground->tileSize - 1) & along;

    int near = (intoX * heightAt(ground, x + 1, z)
        + (ground->tileSize - intoX) * heightAt(ground, x, z)) >> ground->tileShift;
    int far = (heightAt(ground, x, z + 1) * (ground->tileSize - intoX)
        + intoX * heightAt(ground, x + 1, z + 1)) >> ground->tileShift;

    return ((ground->tileSize - intoZ) * near + intoZ * far) >> ground->tileShift;
}

static void tileFree(Tile *tile) {
    if (tile == NULL) {
        return;
    }

    free(tile->across);
    free(tile->up);
    free(tile->along);
    free(tile->colour);
    free(tile->texture);
    free(tile->size);
    free(tile->light);
    free(tile->depth);
    free(tile);
}

static void groundFree(Ground *ground) {
    if (ground == NULL) {
        return;
    }

    if (ground->tiles != NULL) {
        for (int at = 0; at < ground->sizeX * ground->sizeZ; at++) {
            tileFree(ground->tiles[at]);
        }
        free(ground->tiles);
    }

    free(ground->heights);
    free(ground->lights);
    free(ground->shade);
    free(ground->reshade);
    free(ground->normals);
    free(ground->corners);
    free(ground);
}

/**
 * Lays the heights of the grid out one row after another.
 *
 * The client keeps them as an array of arrays, one per row across, and hands the whole thing over.
 */
static int *flattenHeights(JNIEnv *env, jobjectArray rows, int sizeX, int sizeZ) {
    int *heights = calloc((size_t) (sizeX + 1) * (size_t) (sizeZ + 1), sizeof(int));
    if (heights == NULL || rows == NULL) {
        return heights;
    }

    int given = (int) (*env)->GetArrayLength(env, rows);

    for (int x = 0; x <= sizeX && x < given; x++) {
        jintArray row = (jintArray) (*env)->GetObjectArrayElement(env, rows, x);
        if (row == NULL) {
            continue;
        }

        int wanted = (int) (*env)->GetArrayLength(env, row);
        if (wanted > sizeZ + 1) {
            wanted = sizeZ + 1;
        }

        (*env)->GetIntArrayRegion(env, row, 0, wanted,
                                  (jint *) &heights[(size_t) x * (size_t) (sizeZ + 1)]);
        (*env)->DeleteLocalRef(env, row);
    }

    return heights;
}

/**
 * How many places the tile size is shifted by, which the client gives as the size itself.
 */
static int shiftOf(int size) {
    int shift = 0;
    while ((1 << shift) < size && shift < 31) {
        shift++;
    }
    return shift;
}

/**
 * Works out the way the ground faces at every corner of the grid, which is what the sun is
 * weighed against when a tile is lit.
 */
static void measureCorners(Ground *ground);

/**
 * The feature that says the ground takes the shadows of what stands on it, and the one that says
 * it does not after all. The client passes both for ground it means to draw without shadows, and
 * the second wins.
 */
enum { TAKES_SHADOWS = 0x10, TAKES_NO_SHADOWS = 0x20 };

/**
 * How many places wider than the ground the shadow map is: one spare at each end, so that a
 * shadow thrown by a model at the edge of the world still has somewhere to land.
 */
enum { SHADE_MARGIN = 2 };

static void takeShadows(Ground *ground) {
    if ((ground->featureFlags & TAKES_NO_SHADOWS) != 0
        || (ground->featureFlags & TAKES_SHADOWS) == 0) {
        return;
    }

    int shift = shadowShift();
    ground->shadeAcross = ((ground->tileSize * ground->sizeX) >> shift) + SHADE_MARGIN;
    ground->shadeDown = ((ground->tileSize * ground->sizeZ) >> shift) + SHADE_MARGIN;

    ground->shade = calloc((size_t) ground->shadeAcross * (size_t) ground->shadeDown,
                           sizeof(unsigned char));
    ground->reshade = calloc((size_t) ground->sizeX * (size_t) ground->sizeZ,
                             sizeof(unsigned char));
}

JNIEXPORT void JNICALL Java_t_ga(JNIEnv *env, jobject self, jobject toolkit, jobject pool,
                                  jint sizeX, jint sizeZ, jobjectArray heights,
                                  jobjectArray levels, jint tileSize,
                                  jint groundFlags, jint featureFlags) {
    (void) toolkit;
    (void) pool;
    (void) levels;

    groundFree(groundOf(env, self));
    setNativeId(env, self, 0);

    if (sizeX <= 0 || sizeZ <= 0) {
        return;
    }

    Ground *ground = calloc(1, sizeof(Ground));
    if (ground == NULL) {
        return;
    }

    ground->sizeX = sizeX;
    ground->sizeZ = sizeZ;
    ground->tileSize = tileSize;
    ground->tileShift = shiftOf(tileSize);
    ground->groundFlags = groundFlags;
    ground->featureFlags = featureFlags;
    ground->heights = flattenHeights(env, heights, sizeX, sizeZ);
    ground->tiles = calloc((size_t) sizeX * (size_t) sizeZ, sizeof(Tile *));

    if (ground->heights == NULL || ground->tiles == NULL) {
        groundFree(ground);
        return;
    }

    allocatedGrew((size_t) (sizeX + 1) * (size_t) (sizeZ + 1) * sizeof(int));
    takeShadows(ground);
    measureCorners(ground);
    setNativeId(env, self, (jlong) (intptr_t) ground);
}

JNIEXPORT void JNICALL Java_t_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    groundFree(groundOf(env, self));
    setNativeId(env, self, 0);
}

/**
 * Nothing is left to do once every tile has been given. The tiles are built as they arrive.
 */
JNIEXPORT void JNICALL Java_t_YA(JNIEnv *env, jobject self) {
    (void) env;
    (void) self;
}

/**
 * How strongly each light the client has put in the world is shining.
 *
 * The client sends all of them at once, in the order it added them, before every frame it draws.
 */
JNIEXPORT void JNICALL Java_t_q(JNIEnv *env, jobject self, jfloatArray strengths) {
    Ground *ground = groundOf(env, self);
    if (ground == NULL || strengths == NULL) {
        return;
    }

    int given = (int) (*env)->GetArrayLength(env, strengths);
    if (given > ground->lightCount) {
        given = ground->lightCount;
    }

    for (int light = 0; light < given; light++) {
        jfloat held = 0.0f;
        (*env)->GetFloatArrayRegion(env, strengths, light, 1, &held);
        ground->lights[light].intensity = held;
    }
}

/**
 * Puts a light in the world. The array the client passes says which tiles it reaches, and the
 * tiles work that out for themselves from where it is and how far it reaches.
 */
JNIEXPORT void JNICALL Java_t_V(JNIEnv *env, jobject self, jint which, jint across, jint up,
                                 jint along, jint range, jint strength, jintArray reaches) {
    (void) reaches;

    Ground *ground = groundOf(env, self);
    if (ground == NULL) {
        return;
    }

    if (ground->lightCount == ground->lightRoom) {
        int wanted = ground->lightRoom == 0 ? 8 : ground->lightRoom * 2;
        Light *grown = realloc(ground->lights, (size_t) wanted * sizeof(Light));
        if (grown == NULL) {
            return;
        }
        ground->lights = grown;
        ground->lightRoom = wanted;
    }

    Light *light = &ground->lights[ground->lightCount++];
    light->which = which;
    light->across = across;
    light->up = up;
    light->along = along;
    light->range = range;
    light->strength = strength;
    light->intensity = 0.0f;
}

/**
 * Forgets a tile, which the client asks for when the world beneath it changes.
 */
JNIEXPORT void JNICALL Java_t_ka(JNIEnv *env, jobject self, jint x, jint z, jint level) {
    (void) level;

    Ground *ground = groundOf(env, self);
    if (ground == NULL || x < 0 || z < 0 || x >= ground->sizeX || z >= ground->sizeZ) {
        return;
    }

    tileFree(*tileAt(ground, x, z));
    *tileAt(ground, x, z) = NULL;
}

static int16_t *shortsFrom(JNIEnv *env, jintArray source, int count) {
    if (source == NULL) {
        return NULL;
    }

    int16_t *held = calloc((size_t) count, sizeof(int16_t));
    int *read = calloc((size_t) count, sizeof(int));

    if (held != NULL && read != NULL) {
        (*env)->GetIntArrayRegion(env, source, 0, count, (jint *) read);
        for (int at = 0; at < count; at++) {
            held[at] = (int16_t) read[at];
        }
    }

    free(read);
    return held;
}

/**
 * How dark the ground is where nothing stands over it, out of the hundred and twenty eight the
 * lightness of a colour is held in.
 *
 * A corner kept from the sun by what stands on it is darker again, and the amount it is kept out
 * of is taken off this before the lightness is scaled.
 */
enum { GROUND_LIGHTNESS = 74 };

/** What the lightness of a colour is held out of, and the ends it is kept away from. */
enum { LIGHTNESS_WHOLE = 128, LIGHTNESS_LEAST = 2, LIGHTNESS_MOST = 126 };

/** The ends a lit channel is kept away from, so that no part of the ground is wholly one thing. */
enum { CHANNEL_LEAST = 4, CHANNEL_MOST = 252 };

/** What a light level is held out of once it is a whole number. */
enum { LIGHT_WHOLE = 256 };

/**
 * What one corner of a tile is painted.
 *
 * The lightness the client gave is brought down by how much of the sun the corner is kept out of,
 * and the colour that makes is then lit by the sun according to which way the ground faces there.
 * A corner facing away from the sun takes only what the world gives it.
 */
static uint32_t litCorner(const Ground *ground, int packed, int shade, int x, int z) {
    int lightness = ((packed & (LIGHTNESS_WHOLE - 1)) * (GROUND_LIGHTNESS - shade))
        / LIGHTNESS_WHOLE;

    if (lightness < LIGHTNESS_LEAST) {
        lightness = LIGHTNESS_LEAST;
    } else if (lightness > LIGHTNESS_MOST) {
        lightness = LIGHTNESS_MOST;
    }

    uint32_t colour = colourOf((packed & ~(LIGHTNESS_WHOLE - 1)) | lightness);

    const float *normal = groundCornerNormal(ground, x, z);
    const Sun *light = sun();

    /*
     * A corner on the rim of the grid faces nowhere, and the toolkit this replaces divides by
     * that direction's length of nothing. What comes back is not a number, and turning it into a
     * whole one lands below anything, so every channel of such a corner ends at the floor. The
     * same answer is reached here by saying so, because what a machine makes of a number that is
     * not one differs between the two this is built for.
     */
    if (normal == NULL || normal[3] == 0.0f) {
        return ((uint32_t) CHANNEL_LEAST << 16)
            | ((uint32_t) CHANNEL_LEAST << 8)
            | (uint32_t) CHANNEL_LEAST;
    }

    float towards = (light->x * normal[0] + light->y * normal[1] + light->z * normal[2])
        / normal[3];

    /*
     * A corner turned away from the sun is not merely left in the shade: the light it faces away
     * from is taken off what the world gives it, by the second of the two strengths the client
     * sets the sun with.
     */
    float reach = towards > 0.0f ? light->intensity : light->reverseIntensity;
    int strength = (int) ((globalAmbient() + reach * towards) * LIGHT_WHOLE);
    unsigned char sunColour[CHANNELS_LIT] = {light->red, light->green, light->blue};

    uint32_t lit = 0;
    for (int part = 0; part < CHANNELS_LIT; part++) {
        int channel = (int) ((colour >> ((2 - part) * 8)) & 0xff);
        channel = (((channel * sunColour[part]) >> 8) * strength) >> 8;

        if (channel < CHANNEL_LEAST) {
            channel = CHANNEL_LEAST;
        } else if (channel > CHANNEL_MOST) {
            channel = CHANNEL_MOST;
        }

        lit |= (uint32_t) channel << ((2 - part) * 8);
    }

    return lit;
}

/**
 * Builds one tile out of the corners the client hands over.
 *
 * Every corner arrives three to a face, already spread out of the indexed list the client keeps,
 * so nothing here has to know which corners a face shares with its neighbours.
 */
JNIEXPORT void JNICALL Java_t_U(JNIEnv *env, jobject self, jint x, jint z,
                                 jintArray across, jintArray level, jintArray along,
                                 jintArray depth, jintArray colour, jintArray overlay,
                                 jintArray texture, jintArray size,
                                 jint waterColour, jint waterDepth, jint waterBias,
                                 jboolean shadowed) {
    (void) overlay;
    (void) waterColour;
    (void) waterDepth;
    (void) waterBias;

    Ground *ground = groundOf(env, self);
    if (ground == NULL || across == NULL || x < 0 || z < 0
        || x >= ground->sizeX || z >= ground->sizeZ) {
        return;
    }

    int corners = (int) (*env)->GetArrayLength(env, across);
    if (corners < 3) {
        return;
    }

    Tile *tile = calloc(1, sizeof(Tile));
    if (tile == NULL) {
        return;
    }

    tile->corners = corners;
    tile->faces = corners / 3;
    tile->shadowed = shadowed == JNI_TRUE;
    tile->across = shortsFrom(env, across, corners);
    tile->along = shortsFrom(env, along, corners);
    tile->texture = shortsFrom(env, texture, corners);
    tile->size = shortsFrom(env, size, corners);
    tile->depth = shortsFrom(env, depth, corners);
    tile->up = calloc((size_t) corners, sizeof(int16_t));
    tile->colour = calloc((size_t) corners, sizeof(uint32_t));
    tile->light = calloc((size_t) corners, 1);

    if (tile->across == NULL || tile->along == NULL || tile->up == NULL
        || tile->colour == NULL || tile->light == NULL) {
        tileFree(tile);
        return;
    }

    int *levels = calloc((size_t) corners, sizeof(int));
    int *colours = calloc((size_t) corners, sizeof(int));

    if (levels != NULL && colours != NULL) {
        if (level != NULL) {
            (*env)->GetIntArrayRegion(env, level, 0, corners, (jint *) levels);
        }
        if (colour != NULL) {
            (*env)->GetIntArrayRegion(env, colour, 0, corners, (jint *) colours);
        }

        for (int corner = 0; corner < corners; corner++) {
            int worldX = (x << ground->tileShift) + tile->across[corner];
            int worldZ = (z << ground->tileShift) + tile->along[corner];

            tile->up[corner] = (int16_t) (averageHeight(ground, worldX, worldZ) + levels[corner]);
            tile->light[corner] = (unsigned char) groundCornerShade(ground,
                worldX >> ground->tileShift, worldZ >> ground->tileShift);
            tile->colour[corner] = colours[corner] == -1
                ? 0
                : litCorner(ground, colours[corner] & 0xFFFF, tile->light[corner],
                    worldX >> ground->tileShift, worldZ >> ground->tileShift);
        }
    }

    free(levels);
    free(colours);

    tileFree(*tileAt(ground, x, z));
    *tileAt(ground, x, z) = tile;
    allocatedGrew((size_t) corners * sizeof(uint32_t));
}

const void *groundTile(const void *held, int x, int z, int *corners) {
    const Ground *ground = (const Ground *) held;
    if (ground == NULL || x < 0 || z < 0 || x >= ground->sizeX || z >= ground->sizeZ) {
        return NULL;
    }

    const Tile *tile = ground->tiles[(size_t) x * (size_t) ground->sizeZ + (size_t) z];
    if (tile == NULL) {
        return NULL;
    }

    *corners = tile->corners;
    return tile;
}

void groundTileCorner(const void *held, const void *at, int corner, int tileSize,
                      int x, int z, int *into, uint32_t *colour) {
    const Tile *tile = (const Tile *) at;
    (void) held;

    into[0] = x * tileSize + tile->across[corner];
    into[1] = tile->up[corner];
    into[2] = z * tileSize + tile->along[corner];
    *colour = tile->colour[corner];
}

int groundTileSize(const void *held) {
    return ((const Ground *) held)->tileSize;
}

/**
 * Marks the places one tile stands over, which is the tile flattened straight down.
 *
 * A tile is already given in its own square, so nothing has to be taken off before its corners
 * are brought down to the detail a shadow is drawn at.
 */
static void castTileShadow(const Tile *tile, Shadow *shadow) {
    int shift = shadowShift();

    for (int face = 0; face < tile->faces; face++) {
        int first = face * 3;

        int across[3];
        int down[3];
        for (int corner = 0; corner < 3; corner++) {
            across[corner] = tile->across[first + corner] >> shift;
            down[corner] = tile->along[first + corner] >> shift;
        }

        int turned = (down[1] - down[2]) * (across[0] - across[1])
            - (across[2] - across[1]) * (down[1] - down[0]);

        if (turned > 0) {
            shadowMarkTriangle(shadow, down[0], down[1], down[2],
                across[0], across[1], across[2]);
        }
    }
}

/**
 * Where a shadow lands, in shadow places, for a model standing this high above the ground.
 *
 * The sun leans, so a model standing above the ground throws its shadow to one side by however
 * far the sun leans over the height it stands at.
 */
static int shadowLands(int place, int height, int lean) {
    return (place - ((height * lean) >> SUN_LEAN_SHIFT)) >> shadowShift();
}

/**
 * Notes that the picture of these tiles has to be worked out again, because a shadow moved over
 * them. Nothing draws a shadow yet, so nothing reads this back.
 */
static void reshadeTiles(Ground *ground, int left, int top, int right, int bottom) {
    for (int x = left; x <= right; x++) {
        for (int z = top; z <= bottom; z++) {
            if (x >= 0 && z >= 0 && x < ground->sizeX && z < ground->sizeZ) {
                ground->reshade[(size_t) x * (size_t) ground->sizeZ + (size_t) z] = 1;
            }
        }
    }
}

/**
 * How many places of the shadow map fall on one tile, which turns the run of places a shadow
 * moved over back into the tiles whose picture has to be worked out again.
 *
 * Sixteen is not worked out from the tile size and the detail shadows are drawn at, though it is
 * what those two come to here: a tile is five hundred and twelve across and shadows are asked
 * for at thirty two, which is five shifts, and five hundred and twelve shifted down five times
 * is sixteen. The toolkit this stands in for holds the sixteen rather than the sum, so a tile of
 * another size or a shadow asked for at another detail would not change it. Kept as it is.
 */
enum { PLACES_PER_TILE_SHIFT = 4 };

/**
 * Adds a shadow to the ground beneath it, or takes it away again.
 *
 * The shadow is a run of places counting how much of the sun each one is kept out of, and the
 * ground carries the sum over everything standing on it, so putting one down adds its places and
 * taking it away subtracts them. The run is brought inside the map first, because a model at the
 * edge of the world throws its shadow past it.
 */
static void moveShadow(Ground *ground, const Shadow *shadow, int x, int height, int z, int adding) {
    if (ground == NULL || ground->shade == NULL || shadow == NULL) {
        return;
    }

    int left = shadowLeft(shadow) + 1 + shadowLands(x, height, sunLeanAcross());
    int top = shadowTop(shadow) + 1 + shadowLands(z, height, sunLeanAlong());
    int across = shadowAcross(shadow);
    int down = shadowDown(shadow);

    int from = 0;
    int skipped = 0;

    if (top <= 0) {
        int above = 1 - top;
        down -= above;
        from += across * above;
        top = 1;
    }

    if (top + down >= ground->shadeDown) {
        down -= top + down - ground->shadeDown + 1;
    }

    if (left <= 0) {
        int beside = 1 - left;
        across -= beside;
        from += beside;
        skipped = beside;
        left = 1;
    }

    if (left + across >= ground->shadeAcross) {
        int beyond = left + across - ground->shadeAcross + 1;
        across -= beyond;
        skipped += beyond;
    }

    if (across <= 0 || down <= 0) {
        return;
    }

    const unsigned char *places = shadowPlaces(shadow);
    int room = shadowRoom(shadow);
    int at = top * ground->shadeAcross + left;

    for (int row = 0; row < down; row++) {
        for (int column = 0; column < across; column++) {
            if (from >= 0 && from < room) {
                if (adding) {
                    ground->shade[at] = (unsigned char) (ground->shade[at] + places[from]);
                } else {
                    ground->shade[at] = (unsigned char) (ground->shade[at] - places[from]);
                }
            }
            at++;
            from++;
        }
        at += ground->shadeAcross - across;
        from += skipped;
    }

    reshadeTiles(ground,
        (left - 1) >> PLACES_PER_TILE_SHIFT,
        (top - 1) >> PLACES_PER_TILE_SHIFT,
        (left + across - 2) >> PLACES_PER_TILE_SHIFT,
        (top + down - 2) >> PLACES_PER_TILE_SHIFT);
}

/**
 * Puts a model's shadow on the ground under it.
 */
JNIEXPORT void JNICALL Java_t_CA(JNIEnv *env, jobject self, jobject shadow, jint x, jint height,
                                  jint z, jint unused, jboolean immediate) {
    (void) unused;
    (void) immediate;

    moveShadow(groundOf(env, self), (const Shadow *) (intptr_t) nativeIdOf(env, shadow),
        x, height, z, 1);
}

/**
 * Takes a model's shadow off the ground under it, which the client does before the model moves.
 */
JNIEXPORT void JNICALL Java_t_wa(JNIEnv *env, jobject self, jobject shadow, jint x, jint height,
                                  jint z, jint unused, jboolean immediate) {
    (void) unused;
    (void) immediate;

    moveShadow(groundOf(env, self), (const Shadow *) (intptr_t) nativeIdOf(env, shadow),
        x, height, z, 0);
}

/**
 * The shadow one tile casts, which is the tile flattened the same way a model is.
 *
 * A tile only casts a shadow when it was handed over as one that does, and this client hands
 * every tile over as one that does not, so nothing comes back.
 */
JNIEXPORT jobject JNICALL Java_t_fa(JNIEnv *env, jobject self, jint x, jint z, jobject held) {
    Ground *ground = groundOf(env, self);
    if (ground == NULL || x < 0 || z < 0 || x >= ground->sizeX || z >= ground->sizeZ) {
        return NULL;
    }

    Tile *tile = *tileAt(ground, x, z);
    if (tile == NULL || !tile->shadowed) {
        return NULL;
    }

    int side = ground->tileSize >> shadowShift();

    Shadow *reuse = held == NULL ? NULL : (Shadow *) (intptr_t) nativeIdOf(env, held);
    Shadow *shadow;

    if (shadowCanHold(reuse, side, side)) {
        shadowClear(reuse);
        shadow = reuse;
    } else {
        shadow = shadowNew(side, side);
        if (shadow == NULL) {
            return NULL;
        }
    }

    shadowSetBounds(shadow, 0, 0, side, side);
    castTileShadow(tile, shadow);

    if (shadow == reuse) {
        return held;
    }

    jobject fresh = toolkitShadowObject(env);
    if (fresh != NULL) {
        setNativeId(env, fresh, (jlong) (intptr_t) shadow);
    }

    return fresh;
}

int groundTileShift(const void *handle) {
    return ((const Ground *) handle)->tileShift;
}

int groundSizeX(const void *handle) {
    return ((const Ground *) handle)->sizeX;
}

int groundSizeZ(const void *handle) {
    return ((const Ground *) handle)->sizeZ;
}

int groundHeightAt(const void *handle, int x, int z) {
    return heightAt((const Ground *) handle, x, z);
}

/**
 * How high the ground is at a place between its corners, which is the four corners of the tile it
 * falls in weighed by how near it is to each.
 */
int groundHeightBetween(const void *handle, int x, int z) {
    const Ground *ground = handle;
    int tileX = x >> ground->tileShift;
    int tileZ = z >> ground->tileShift;

    if (tileX < 0 || tileZ < 0 || tileX > ground->sizeX - 1 || tileZ > ground->sizeZ - 1) {
        return 0;
    }

    int acrossTile = (ground->tileSize - 1) & x;
    int alongTile = (ground->tileSize - 1) & z;

    int nearer = (acrossTile * heightAt(ground, tileX + 1, tileZ)
        + (ground->tileSize - acrossTile) * heightAt(ground, tileX, tileZ)) >> ground->tileShift;
    int further = (heightAt(ground, tileX, tileZ + 1) * (ground->tileSize - acrossTile)
        + acrossTile * heightAt(ground, tileX + 1, tileZ + 1)) >> ground->tileShift;

    return ((ground->tileSize - alongTile) * nearer + alongTile * further) >> ground->tileShift;
}

/**
 * Which texture a face of the tile wears, or nothing where it wears none. The client gives one
 * texture per face and it is kept beside each of the face's three corners.
 */
int groundTileFaceTexture(const void *at, int face) {
    const Tile *tile = at;
    if (tile->texture == NULL || face * 3 >= tile->corners) {
        return -1;
    }

    return tile->texture[face * 3];
}

int groundTileFaces(const void *at) {
    return ((const Tile *) at)->faces;
}

/**
 * Where one corner of the tile sits in its own square, and what colour it is.
 */
void groundTilePlanCorner(const void *at, int corner, int *across, int *along, uint32_t *colour) {
    const Tile *tile = at;
    *across = tile->across[corner];
    *along = tile->along[corner];
    *colour = tile->colour[corner];
}

/**
 * How far apart in height two places one tile apart have to be for the ground between them to
 * stand at forty five degrees.
 *
 * The client counts height in the same units it counts distance, and the ground is measured
 * across two tiles at once, so half a tile is what one step of height is weighed against.
 */
enum { UPRIGHT_PER_STEP = 256 };

/**
 * Works out the way the ground faces at every corner of the grid.
 *
 * A corner's direction comes from how the height changes either side of it, one step each way.
 * The upright part is the same everywhere and points the way the client counts up, which is the
 * way heights grow more negative, so the whole of it leans away from a rise.
 *
 * A corner on the edge of the grid is left facing nowhere at all, because the step either side of
 * it would run off the heights. Such a corner takes no sun and is left with only the light the
 * world gives everything, which is what darkens the rim of a patch of ground.
 */
static void measureCorners(Ground *ground) {
    int across = ground->sizeX + 1;
    int along = ground->sizeZ + 1;

    ground->normals = calloc((size_t) across * (size_t) along * NORMAL_PARTS, sizeof(float));
    ground->corners = calloc((size_t) across * (size_t) along, sizeof(unsigned char));
    if (ground->normals == NULL || ground->corners == NULL) {
        return;
    }

    for (int x = 0; x < across; x++) {
        for (int z = 0; z < along; z++) {
            float *normal = &ground->normals[((size_t) x * (size_t) along + (size_t) z)
                * NORMAL_PARTS];

            if (x == 0 || z == 0 || x == across - 1 || z == along - 1) {
                continue;
            }

            int stepX = heightAt(ground, x + 1, z) - heightAt(ground, x - 1, z);
            int stepZ = heightAt(ground, x, z + 1) - heightAt(ground, x, z - 1);

            float length = sqrtf((float) (stepX * stepX)
                + (float) (UPRIGHT_PER_STEP * UPRIGHT_PER_STEP)
                + (float) (stepZ * stepZ));
            float over = 1.0f / length;

            normal[0] = (float) stepX * over;
            normal[1] = (float) -UPRIGHT_PER_STEP * over;
            normal[2] = (float) stepZ * over;
            normal[3] = 1.0f;
        }
    }
}

const float *groundCornerNormal(const void *handle, int x, int z) {
    const Ground *ground = handle;
    if (ground->normals == NULL || x < 0 || z < 0 || x > ground->sizeX || z > ground->sizeZ) {
        return NULL;
    }

    return &ground->normals[((size_t) x * (size_t) (ground->sizeZ + 1) + (size_t) z)
        * NORMAL_PARTS];
}

int groundCornerShade(const void *handle, int x, int z) {
    const Ground *ground = handle;
    if (ground->corners == NULL || x < 0 || z < 0 || x > ground->sizeX || z > ground->sizeZ) {
        return 0;
    }

    return ground->corners[(size_t) x * (size_t) (ground->sizeZ + 1) + (size_t) z];
}
