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
} Ground;

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
    (void) shadowed;

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
            tile->colour[corner] = colours[corner] == -1
                ? 0
                : colourOf(colours[corner] & 0xFFFF);
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
