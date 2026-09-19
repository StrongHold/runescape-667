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

#include <stdio.h>

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

    /**
     * The colour each corner is drawn in on the map, where the client lays one over the face the
     * corner belongs to. Nothing where it laid none, and the corner's own colour serves instead.
     */
    uint32_t *plan;
    int16_t *texture;
    int16_t *size;
    unsigned char *light;

    /**
     * Whether the client gave the corner no colour at all, which it does where the floor opens
     * onto the one below.
     */
    unsigned char *hollow;

    /**
     * Whether the client gave the corner no colour of either kind, so that there is nothing to
     * draw it in even on the map.
     */
    unsigned char *bare;

    /** How deep the water over the corner is, or nothing where the tile is not underwater. */
    int16_t *depth;


    /** Whether the tile was handed over as one that casts a shadow of its own. */
    int shadowed;

    /**
     * Whether the client handed this tile over with water on it, which it does by giving it a
     * colour for the water and a depth at one or more of its corners, and the colour it gave
     * whether or not any corner had a depth.
     */
    int watered;
    int waterColour;

    /**
     * How far down the water over this tile lets anything be seen.
     *
     * A corner as deep as this shows nothing of the ground under it and is drawn in the water's
     * own colour; one at the surface shows the ground whole. It is also what says whether the
     * tile carries water at all, because the client gives it nothing where there is none.
     */
    int waterReaches;

    /**
     * Where this tile's picture of the shadow over it sits in the run the ground keeps, and how
     * far a place on its texture is shifted down to reach a place in that picture.
     *
     * A tile is given somewhere to keep it the first time it is drawn with the sun kept out of
     * anywhere over it, and keeps the same place afterwards. Nought means it has not been given
     * one, because the first place given out is never nought.
     */
    int shadowAt;
    int shadowShift;
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

    /**
     * The second grid of heights the client hands the ground, which is where the water over it
     * lies.
     *
     * The ground is drawn where the first grid puts it, but it is lit as though it lay where the
     * second does: which way a corner faces is worked out from this grid and not from the other.
     * Where there is no water the client hands the same grid twice and the two are the same
     * thing, which is why nothing else here had ever noticed the difference.
     */
    int *waterHeights;

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
     * A picture of the shadow over every tile that has been drawn, laid out as a texture is: two
     * hundred and fifty six across, so that a place in it is reached the same way a texel is.
     *
     * Tiles are laid side by side across a band as tall as one tile is wide in places. A band
     * that cannot fit another tile is left as it is and the next tile starts a new one, which is
     * why a tile keeps where it was put rather than where it would go now.
     */
    unsigned char *shadowTexture;
    size_t shadowTextureRoom;
    int shadowNext;
    int shadowBand;

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

static int waterHeightAt(const Ground *ground, int x, int z) {
    return ground->waterHeights[(size_t) x * (size_t) (ground->sizeZ + 1) + (size_t) z];
}

/**
 * How high the ground is between the corners of a tile, which is where a corner the client put
 * inside a tile sits.
 */
static int averageOf(const Ground *ground, int across, int along, int water) {
    int x = across >> ground->tileShift;
    int z = along >> ground->tileShift;

    if (x < 0 || z < 0 || x > ground->sizeX - 1 || z > ground->sizeZ - 1) {
        return 0;
    }

    int intoX = (ground->tileSize - 1) & across;
    int intoZ = (ground->tileSize - 1) & along;

    int nearLeft = water ? waterHeightAt(ground, x, z) : heightAt(ground, x, z);
    int nearRight = water ? waterHeightAt(ground, x + 1, z) : heightAt(ground, x + 1, z);
    int farLeft = water ? waterHeightAt(ground, x, z + 1) : heightAt(ground, x, z + 1);
    int farRight = water ? waterHeightAt(ground, x + 1, z + 1) : heightAt(ground, x + 1, z + 1);

    int near = (intoX * nearRight + (ground->tileSize - intoX) * nearLeft) >> ground->tileShift;
    int far = (farLeft * (ground->tileSize - intoX) + intoX * farRight) >> ground->tileShift;

    return ((ground->tileSize - intoZ) * near + intoZ * far) >> ground->tileShift;
}

static int averageHeight(const Ground *ground, int across, int along) {
    return averageOf(ground, across, along, 0);
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
    free(tile->hollow);
    free(tile->bare);
    free(tile->plan);
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
    free(ground->waterHeights);
    free(ground->lights);
    free(ground->shade);
    free(ground->shadowTexture);
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
 * The feature that says the ground takes the shadows of what stands on it.
 */
enum { TAKES_SHADOWS = 0x10 };

/**
 * The feature the client asks for when the player has turned textures off.
 *
 * A tile then loses any texture the artwork says may be turned off. Ground asked for this way
 * takes no shadows either: the client passes both features and this one puts the other out.
 */
enum { TEXTURES_TURNED_OFF = 0x20 };

/**
 * How many places wider than the ground the shadow map is: one spare at each end, so that a
 * shadow thrown by a model at the edge of the world still has somewhere to land.
 */
enum { SHADE_MARGIN = 2 };

static void takeShadows(Ground *ground) {
    if ((ground->featureFlags & TEXTURES_TURNED_OFF) != 0
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
    ground->waterHeights = levels == NULL ? NULL : flattenHeights(env, levels, sizeX, sizeZ);
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
/** A running count of what becomes of the tiles the client hands over. */
static struct {
    int handed;
    int noGround;
    int outside;
    int tooFewCorners;
    int noRoom;
    int kept;
} handing;

static void handed(const char *why) {
    static int listening = -1;
    if (listening == -1) {
        listening = getenv("SW3D_GROUND_TALLY") != NULL;
    }

    if (!listening) {
        return;
    }

    if (why != NULL && handing.handed > 0) {
        fprintf(stderr, "sw3d handed: %d handed over, %d no ground, %d outside, %d too few"
                " corners, %d no room, %d kept\n",
                handing.handed, handing.noGround, handing.outside, handing.tooFewCorners,
                handing.noRoom, handing.kept);
        fflush(stderr);
    }
}

JNIEXPORT void JNICALL Java_t_YA(JNIEnv *env, jobject self) {
    Ground *ground = groundOf(env, self);

    if (ground != NULL && getenv("SW3D_GROUND_TALLY") != NULL) {
        int held = 0;
        for (int at = 0; at < ground->sizeX * ground->sizeZ; at++) {
            if (ground->tiles[at] != NULL) {
                held++;
            }
        }

        fprintf(stderr, "sw3d finished a ground %p %dx%d: %d tiles held\n", (void *) ground,
                ground->sizeX, ground->sizeZ, held);

        /*
         * Where the tiles a ground holds lie, a row of the grid to a row of text. A ground with
         * holes in it draws nothing where they are, and no count of how many it holds says
         * whether they fall where the water is or where the land is.
         */
        enum { ROWS_SHOWN = 26 };
        int step = ground->sizeX / ROWS_SHOWN;
        if (step < 1) {
            step = 1;
        }

        for (int x = 0; x < ground->sizeX; x += step) {
            char row[128];
            int at = 0;

            for (int z = 0; z < ground->sizeZ && at < (int) sizeof(row) - 1; z += step) {
                row[at++] = ground->tiles[(size_t) x * (size_t) ground->sizeZ + (size_t) z] == NULL
                    ? '.' : '#';
            }

            row[at] = '\0';
            fprintf(stderr, "sw3d held %3d %s\n", x, row);
        }
    }

    handed("");
    handing = (typeof(handing)) {0};
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
/**
 * Darkens one corner of the grid, which is how the client puts the shadow of a thing standing on
 * the ground onto the ground itself.
 *
 * The client walks the corners under and around everything that casts one and asks for each to be
 * darkened by as much as that thing darkens it. A corner already darker than that is left as it
 * is, so a corner under two things takes the darker of the two rather than both.
 *
 * This is asked for before any tile is handed over, and it is what a tile's corners are lit by.
 */
JNIEXPORT void JNICALL Java_t_ka(JNIEnv *env, jobject self, jint x, jint z, jint darker) {
    Ground *ground = groundOf(env, self);
    if (switchedOff("SW3D_NO_GROUND_SHADOW") || ground == NULL || ground->corners == NULL
        || x < 0 || z < 0 || x > ground->sizeX || z > ground->sizeZ) {
        return;
    }

    size_t at = (size_t) x * (size_t) (ground->sizeZ + 1) + (size_t) z;
    if (ground->corners[at] < darker) {
        ground->corners[at] = (unsigned char) darker;
    }
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

/** What the client hands over for a corner of the ground with no colour of its own. */
enum { NO_COLOUR = -1 };

/** The colour such a corner is lit as instead, which carries no hue and no lightness at all. */
enum { BLACK = 0 };


/** What the lightness of a colour is held out of, and the ends it is kept away from. */
enum { LIGHTNESS_WHOLE = 128, LIGHTNESS_SHIFT = 7, LIGHTNESS_LEAST = 2, LIGHTNESS_MOST = 126 };

/** The ends a lit channel is kept away from, so that no part of the ground is wholly one thing. */
enum { CHANNEL_LEAST = 4, CHANNEL_MOST = 252 };

/** What a light level is held out of once it is a whole number. */
enum { LIGHT_WHOLE = 256 };

/**
 * What the light on a corner of the ground is held out of when the map is drawn.
 *
 * The map lights a corner from the sun and the world's own ambient alone, out of half of what a
 * lightness is held out of, and takes the shadow over the corner off afterwards. How strongly the
 * sun is set to shine does not come into it.
 */
enum { GROUND_LIGHT_WHOLE = LIGHTNESS_WHOLE / 2 };

/**
 * What one corner of a tile is painted.
 *
 * The lightness the client gave is brought down by how much of the sun the corner is kept out of,
 * and the colour that makes is then lit by the sun according to which way the ground faces there.
 * A corner facing away from the sun takes only what the world gives it.
 */
/**
 * The way the ground faces at a place inside a tile.
 *
 * A corner of a face is not always a corner of the grid. The client cuts a tile into shapes and
 * puts corners part way along its edges, and the way the ground faces there is worked out from
 * the four corners of the tile around it rather than snapped to the nearest one. Snapping gives
 * every face of such a tile the light of whichever corner it rounded to, which shows as a patch
 * of ground lighter or darker than the one beside it along the line the tile was cut on.
 */
/**
 * How much of the sun is kept out of a place inside a tile.
 *
 * The client darkens the corners of the grid and nothing between them, so a corner it puts inside
 * a tile takes its share of the four corners around it. A tile cut into more than two faces has
 * corners like that all over it, and one of them darkened by the corner of the grid below it
 * rather than by all four is a shade out from its neighbours.
 */
/**
 * The shade at a point inside a tile, shared out from the four corners around it.
 *
 * Each pair of corners is brought down to a shade of its own before the two are mixed, rather than
 * the whole being brought down once at the end, and each step is the distance from one corner to
 * the other taken a share of the way and added back on rather than the two corners weighed against
 * each other. Both lose a fraction, and they do not lose the same one: a share of a distance that
 * runs downhill is brought down away from nothing rather than towards it, and weighing the corners
 * never is.
 */
static int shadeInside(const Ground *ground, int x, int z, int across, int along) {
    int shift = ground->tileShift;

    int nearLeft = groundCornerShade(ground, x, z);
    int nearRight = groundCornerShade(ground, x + 1, z);
    int farLeft = groundCornerShade(ground, x, z + 1);
    int farRight = groundCornerShade(ground, x + 1, z + 1);

    int near = nearLeft + (((nearRight - nearLeft) * across) >> shift);
    int far = farLeft + (((farRight - farLeft) * across) >> shift);

    return near + (((far - near) * along) >> shift);
}

/**
 * Which way the ground faces at a point inside a tile, shared out from the four corners around it.
 *
 * The toolkit hands the four over as a ring rather than as two rows: the corner the tile starts
 * at, the one a step along, the one across from that, and the one a step across. They are paired
 * here as two rows all the same, because pairing them as they are handed over puts every scene
 * made of plain tiles out and this one further out still.
 *
 * The fourth part of each direction is not shared out. A plain one is written into it and the sun
 * is divided by that, so a direction blended from four of length one is left shorter than one and
 * is meant to be.
 */
static void facingInside(const Ground *ground, int x, int z, int across, int along, float *into) {
    const float *near = groundCornerNormal(ground, x, z);
    const float *far = groundCornerNormal(ground, x + 1, z);
    const float *nearAlong = groundCornerNormal(ground, x, z + 1);
    const float *farAlong = groundCornerNormal(ground, x + 1, z + 1);

    if (near == NULL || far == NULL || nearAlong == NULL || farAlong == NULL) {
        into[3] = 0.0f;
        return;
    }

    float partAcross = (float) across / (float) ground->tileSize;
    float partAlong = (float) along / (float) ground->tileSize;

    for (int lane = 0; lane < NORMAL_PARTS; lane++) {
        float nearer = near[lane] + (far[lane] - near[lane]) * partAcross;
        float further = nearAlong[lane] + (farAlong[lane] - nearAlong[lane]) * partAcross;
        into[lane] = nearer + (further - nearer) * partAlong;
    }
}

/**
 * The effect that leaves a face's colour alone. Every other one carries it towards a grey.
 */
enum { LEAVES_THE_COLOUR_BE = 4 };

/**
 * A colour carrying however much of the sun reaches the corner and nothing else.
 *
 * This is the whole of what the map is drawn in. What the world is drawn in goes on from here,
 * through what the texture over the corner does to its colour and through which way the ground
 * faces the sun, and neither belongs on a map: a texture carries every colour towards the same
 * grey, so a map drawn that way comes out in one colour however many kinds of ground it covers.
 */
static int shadedLightness(int packed, int shade) {
    int reaching = GROUND_LIGHTNESS - shade;

    /*
     * How much of the sun the corner gets, brought down by seven places rather than divided, so
     * that a corner darkened past what the ground is lit at comes out one short of nothing rather
     * than at nothing.
     */
    int sunlit = ((packed & (LIGHTNESS_WHOLE - 1)) * reaching) >> LIGHTNESS_SHIFT;

    /*
     * The two ends it is held between are asked about as though it could not be less than
     * nothing, so a corner darkened past what the ground is lit at comes out at the brighter end
     * rather than the darker one. That is what the toolkit this stands in for does, and it is
     * what keeps such a corner from being drawn black. Kept as it is.
     */
    unsigned int held = (unsigned int) sunlit;
    int lightness = LIGHTNESS_LEAST;

    if (held > 1u) {
        lightness = held <= (unsigned int) LIGHTNESS_MOST ? (int) held : LIGHTNESS_MOST;
    }

    return lightness;
}

static uint32_t shadedColour(int packed, int shade) {
    return colourOf((packed & ~(LIGHTNESS_WHOLE - 1)) | shadedLightness(packed, shade));
}

/**
 * A colour held between the two ends a lit corner is kept within.
 */
static int heldLightness(int lightness) {
    if (lightness < LIGHTNESS_LEAST) {
        return LIGHTNESS_LEAST;
    }

    return lightness > LIGHTNESS_MOST ? LIGHTNESS_MOST : lightness;
}

/**
 * Whether the map is drawn the way the client's own renderer draws it, keeping each colour's hue
 * and taking only how strongly the sun reaches a corner.
 *
 * Kept only to measure against. The shipped toolkit draws the map in the colour the ground is
 * drawn in, and that is what is drawn.
 */
static int planTheOldWay(void) {
    return switchedOff("SW3D_PLAN_OLD");
}

static uint32_t litCorner(const Ground *ground, int packed, int shade, int x, int z,
                          int across, int along, int texture, int wearsIts) {
    int reaching = GROUND_LIGHTNESS - shade;
    uint32_t colour = shadedColour(packed, shade);

    /*
     * A tile wearing a texture is not lit from its own colour alone. The texture says how far that
     * colour is carried towards a grey made from the lightness the tile ended up with, and then
     * how much to brighten what is left, which is the same thing a texture does to a face of a
     * model. One effect leaves the colour where it is, and leaves only that: such a tile is still
     * brightened by however much the texture asks for.
     */
    const TextureMetrics *worn = texture == -1 || !wearsIts ? NULL : textureMetricsFor(texture);
    if (worn != NULL) {
        int towardsGrey = worn->effectType == LEAVES_THE_COLOUR_BE ? 0 : worn->alpha;
        colour = texturedUnlitColour(colour, reaching, towardsGrey, worn->aByte57);
    }

    float facing[NORMAL_PARTS];
    facingInside(ground, x, z, across, along, facing);

    const float *normal = facing;
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

    if (!wearsIts && planTheOldWay()) {
        int level = heldLightness((int) ((globalAmbient() + towards) * GROUND_LIGHT_WHOLE));
        int lightness = ((packed & (LIGHTNESS_WHOLE - 1)) * (level - shade)) >> LIGHTNESS_SHIFT;

        return colourOf((packed & ~(LIGHTNESS_WHOLE - 1)) | heldLightness(lightness));
    }

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
 * Whether a ground carries water the player can see through.
 *
 * The client turns this on for the ground above the water and never for the bed beneath it, and
 * only when the player has asked for the better water.
 */
enum { WATER_SEEN_THROUGH = 0x8 };

/** The effects a texture names that stand for water, of which there are three. */
enum { WATER_STILL = 4, WATER_MOVING = 8, WATER_DEEP = 9 };

/** How much of a face of the ground drawn through shows, out of two hundred and fifty five. */
enum { WATER_SHOWS = 0x9B };

/**
 * Says once for each texture the ground is asked about what it was told and what it answered,
 * when it is started with SW3D_WATER set.
 */
static void drawnThroughSaid(const Ground *ground, int texture, int effect, int shows) {
    static int listening = -1;
    if (listening == -1) {
        listening = switchedOff("SW3D_WATER");
    }

    enum { KINDS = 16 };
    static int seen[KINDS];
    static int count;

    if (!listening) {
        return;
    }

    for (int at = 0; at < count; at++) {
        if (seen[at] == texture) {
            return;
        }
    }

    if (count < KINDS) {
        seen[count++] = texture;
    }

    fprintf(stderr, "sw3d water: texture %d effect %d on a ground of %#x shows %d\n",
            texture, effect, (unsigned) ground->featureFlags, shows);
}

int groundDrawsThrough(const void *held, int texture) {
    const Ground *ground = held;

    if (ground == NULL || texture == -1) {
        return 0;
    }

    const TextureMetrics *metrics = textureMetricsFor(texture);
    int effect = metrics == NULL ? -1 : (int) metrics->effectType;

    int shows = 0;
    if ((ground->featureFlags & WATER_SEEN_THROUGH) != 0
        && (effect == WATER_STILL || effect == WATER_MOVING || effect == WATER_DEEP)) {
        shows = WATER_SHOWS;
    }

    drawnThroughSaid(ground, texture, effect, shows);
    return shows;
}

/**
 * Whether the player has turned this texture off.
 *
 * A player who turns textures off keeps the ones the artwork says may not be turned off, and the
 * ground is told which way round it is through a feature of its own rather than through the one
 * a model is built with.
 */
static int wearsNothing(const Ground *ground, int texture) {
    if (texture == -1 || (ground->featureFlags & TEXTURES_TURNED_OFF) == 0) {
        return 0;
    }

    const TextureMetrics *metrics = textureMetricsFor(texture);
    return metrics != NULL && metrics->disableable;
}

/**
 * Says once for each set of water numbers what the client hands a tile over carrying, when it is
 * started with SW3D_WATER set.
 */
static void wateredTileSeen(int colour, int reaches, int bias, int carriesDepths) {
    static int listening = -1;
    if (listening == -1) {
        listening = getenv("SW3D_WATER") != NULL;
    }

    if (!listening) {
        return;
    }

    enum { KEPT = 8 };
    static int seen[KEPT][4];
    static int count;

    for (int at = 0; at < count; at++) {
        if (seen[at][0] == colour && seen[at][1] == reaches
            && seen[at][2] == bias && seen[at][3] == carriesDepths) {
            return;
        }
    }

    if (count < KEPT) {
        seen[count][0] = colour;
        seen[count][1] = reaches;
        seen[count][2] = bias;
        seen[count][3] = carriesDepths;
        count++;
    }

    fprintf(stderr, "sw3d water: a tile carrying colour %06x, reaching %d, bias %d, %s\n",
            (unsigned) colour & 0xFFFFFF, reaches, bias,
            carriesDepths ? "with depths" : "with no depths");
}

/**
 * Says what depths a tile given a colour for the water on it was actually handed, when the client
 * is started with SW3D_WATER set.
 *
 * A colour says the client meant water somewhere near, and only the depths say whether the water
 * reaches this tile. What they run between is the thing to know and cannot be guessed.
 */
/**
 * Says once what the client hands over for a tile it gave water to, whole rather than masked.
 *
 * A corner's colour is read as sixteen bits and the rest thrown away, because that is all a
 * colour is. Anything else the client puts in the top of one goes unseen, and the surface of
 * water covers everything drawn under it, so whether it carries something that says to draw it
 * through is worth knowing.
 */
static void wateredTileHandedOver(int waterColour, const int *colours, const int *overlays,
                                  const int16_t *texture, int corners) {
    static int listening = -1;
    if (listening == -1) {
        listening = switchedOff("SW3D_WATER");
    }

    static int said;
    enum { SAY_AT_MOST = 4 };

    enum { KINDS = 10 };
    static int seen[KINDS];
    static int count;

    if (!listening || corners < 3 || colours == NULL) {
        return;
    }

    /*
     * One line for each texture the ground wears rather than for the first few tiles, because the
     * first few are all the same piece of ground and say nothing about how one kind of tile
     * differs from another.
     */
    int worn = texture == NULL ? -1 : (int) texture[0];
    for (int at = 0; at < count; at++) {
        if (seen[at] == worn) {
            return;
        }
    }

    if (count >= KINDS) {
        return;
    }

    seen[count] = worn;
    count++;
    (void) said;
    (void) SAY_AT_MOST;

    fprintf(stderr, "sw3d water: a %s tile is handed colours %08x %08x %08x,"
            " laid over %08x %08x %08x, wearing %d\n",
            waterColour == 0 ? "dry" : "watered",
            (unsigned) colours[0], (unsigned) colours[1], (unsigned) colours[2],
            (unsigned) (overlays == NULL ? 0 : overlays[0]),
            (unsigned) (overlays == NULL ? 0 : overlays[1]),
            (unsigned) (overlays == NULL ? 0 : overlays[2]),
            texture == NULL ? -1 : (int) texture[0]);
}

/**
 * Says once for each ground where it is drawn and where its second grid of heights is.
 *
 * The client keeps two grounds over water, the bed and the floor above it, and hands each of them
 * two grids of heights. Which grid a ground is drawn at decides what can be seen in front of what,
 * and from outside the two grounds are indistinguishable.
 */
static void groundHeightsSeen(const Ground *ground, int x, int z, int drawnAt, int secondAt) {
    static int listening = -1;
    if (listening == -1) {
        listening = switchedOff("SW3D_WATER");
    }

    enum { KEPT = 4 };
    static const void *seen[KEPT];
    static int count;

    if (!listening) {
        return;
    }

    for (int at = 0; at < count; at++) {
        if (seen[at] == ground) {
            return;
        }
    }

    if (count >= KEPT) {
        return;
    }

    seen[count] = ground;
    count++;

    fprintf(stderr, "sw3d water: a ground %p at %d,%d is drawn at height %d,"
            " its second grid says %d, %d tiles across\n",
            (const void *) ground, x, z, drawnAt, secondAt, ground->sizeX);
}

static void wateredDepthsSeen(int colour, const int16_t *depth, int corners) {
    static int listening = -1;
    if (listening == -1) {
        listening = getenv("SW3D_WATER") != NULL;
    }

    if (!listening || colour == 0 || depth == NULL) {
        return;
    }

    static int least = 0x7FFFFFFF;
    static int most = -0x7FFFFFFF;
    static int said;

    int moved = 0;
    for (int corner = 0; corner < corners; corner++) {
        if (depth[corner] < least) {
            least = depth[corner];
            moved = 1;
        }
        if (depth[corner] > most) {
            most = depth[corner];
            moved = 1;
        }
    }

    enum { SAY_AT_MOST = 12 };
    if (moved && said < SAY_AT_MOST) {
        said++;
        fprintf(stderr, "sw3d water: depths on a watered tile run %d to %d\n", least, most);
    }
}

/**
 * Whether any corner of a tile stands under any depth of water at all.
 *
 * A tile is water to the toolkit when it has been told how far down its water lets anything be
 * seen and one of its corners has some water over it. The colour the client gives the water is
 * not what says so: the colour is never asked about, only used, and the client gives it to whole
 * stretches of ground that the water only reaches part of.
 */
/**
 * Says once for each texture the ground wears what the map has to go on, when the client is
 * started with SW3D_GROUND_BARE set: the colour the whole texture comes to, and whether the
 * player is allowed to turn it off.
 */
/**
 * Says once for each set of them what the three things a corner could stand for on the map are,
 * and which of them it was given, when the client is started with SW3D_PLAN_PICK set.
 *
 * A corner can stand for the colour laid over its face, for the colour its texture comes to, or
 * for the colour of the ground it is on. Which of the three it is given is the whole of what the
 * map draws, and reading it off the client is the only way to know which one it should be.
 */
static void planPicked(int named, int laid, int worn, const TextureMetrics *metrics, int stands) {
    static int listening = -1;
    if (listening == -1) {
        listening = switchedOff("SW3D_PLAN_PICK");
    }

    if (!listening) {
        return;
    }

    enum { KEPT = 40 };
    static int seen[KEPT];
    static int count;
    static int overflowed;

    int comes = metrics == NULL ? -1 : (int) metrics->averageColour;

    /*
     * One line for each texture rather than for each set of colours. The ground's own colour
     * changes from corner to corner and would otherwise fill this up long before the textures
     * worth seeing came round.
     */
    for (int at = 0; at < count; at++) {
        if (seen[at] == worn) {
            return;
        }
    }

    if (count >= KEPT) {
        if (!overflowed) {
            overflowed = 1;
            fprintf(stderr, "sw3d plan: and more besides\n");
        }
        return;
    }

    seen[count] = worn;
    count++;

    fprintf(stderr, "sw3d plan: texture %d comes to %04x%s, ground %04x, laid over %04x,"
            " stands for %04x\n",
            worn,
            (unsigned) comes & 0xFFFF,
            metrics == NULL ? " (nothing known of it)"
                : (metrics->disableable ? " and may go" : " and may not go"),
            (unsigned) named & 0xFFFF,
            (unsigned) laid & 0xFFFF,
            (unsigned) stands & 0xFFFF);
}

static void textureOnTheMap(int worn, const TextureMetrics *metrics) {
    static int listening = -1;
    if (listening == -1) {
        listening = switchedOff("SW3D_GROUND_BARE");
    }

    if (!listening || worn == -1) {
        return;
    }

    enum { KEPT = 24 };
    static int seen[KEPT];
    static int count;

    for (int at = 0; at < count; at++) {
        if (seen[at] == worn) {
            return;
        }
    }

    if (count < KEPT) {
        seen[count] = worn;
        count++;
    }

    if (metrics == NULL) {
        fprintf(stderr, "sw3d ground: texture %d has nothing to say about itself\n", worn);
    } else {
        fprintf(stderr, "sw3d ground: texture %d comes to %04x, %s\n", worn,
                metrics->averageColour,
                metrics->disableable ? "may be turned off" : "may not be turned off");
    }
}

/** How many parts a packed colour carries, which is a byte each for red, green and blue. */
enum { COLOUR_PARTS = 3 };

/** How much of the water over a tile stands over one of its corners, out of the whole. */
/**
 * How much of the water is carried in a corner's own colour, which is one byte of it.
 *
 * The amount is kept beside the colour rather than worked out again where the corner is drawn,
 * so it is only ever as fine as a byte and a corner a shade deeper than its neighbour is carried
 * no further until it is a whole part of two hundred and fifty five deeper.
 */
enum { WHOLE_OF_THE_WATER = 255 };

static float cornerUnder(const Tile *tile, int corner) {
    if (tile == NULL || !tile->watered || tile->depth == NULL || corner >= tile->corners
        || tile->waterReaches <= 0) {
        return 0.0f;
    }

    float under = (float) tile->depth[corner] / (float) (tile->waterReaches / 2);
    if (under < 0.0f) {
        return 0.0f;
    }

    if (under > 1.0f) {
        under = 1.0f;
    }

    return (float) (int) (under * (float) WHOLE_OF_THE_WATER) / (float) WHOLE_OF_THE_WATER;
}

/**
 * Carries one colour towards the colour of the water over it: nothing where no water stands over
 * it, and nothing but the water where the whole of it does.
 */
static uint32_t carriedUnderWater(uint32_t colour, uint32_t water, float under) {
    if (under <= 0.0f) {
        return colour;
    }

    uint32_t carried = 0;
    for (int part = 0; part < COLOUR_PARTS; part++) {
        int shift = part * 8;
        float held = (float) ((colour >> shift) & 0xFF);
        float towards = (float) ((water >> shift) & 0xFF);

        carried |= (uint32_t) (held + (towards - held) * under) << shift;
    }

    return carried;
}

static int anyDepth(const int16_t *depth, int corners) {
    if (depth == NULL) {
        return 0;
    }

    for (int corner = 0; corner < corners; corner++) {
        if (depth[corner] > 0) {
            return 1;
        }
    }

    return 0;
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
    wateredTileSeen(waterColour, waterDepth, waterBias, depth != NULL);

    (void) waterBias;

    handing.handed++;

    Ground *ground = groundOf(env, self);
    if (ground == NULL) {
        handing.noGround++;
        handed("");
        return;
    }

    if (across == NULL || x < 0 || z < 0 || x >= ground->sizeX || z >= ground->sizeZ) {
        handing.outside++;
        handed("");
        return;
    }

    int corners = (int) (*env)->GetArrayLength(env, across);
    if (corners < 3) {
        handing.tooFewCorners++;
        handed("");
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
    tile->waterColour = waterColour;
    tile->waterReaches = waterDepth;
    tile->watered = waterDepth != 0 && anyDepth(tile->depth, corners);
    wateredDepthsSeen(waterColour, tile->depth, corners);
    tile->up = calloc((size_t) corners, sizeof(int16_t));
    tile->colour = calloc((size_t) corners, sizeof(uint32_t));
    tile->light = calloc((size_t) corners, 1);
    tile->hollow = calloc((size_t) corners, 1);
    tile->bare = calloc((size_t) corners, 1);

    if (tile->across == NULL || tile->along == NULL || tile->up == NULL
        || tile->colour == NULL || tile->light == NULL || tile->hollow == NULL || tile->bare == NULL) {
        tileFree(tile);
        return;
    }

    int *levels = calloc((size_t) corners, sizeof(int));
    int *colours = calloc((size_t) corners, sizeof(int));
    int *overlays = overlay == NULL ? NULL : calloc((size_t) corners, sizeof(int));

    if (overlays != NULL) {
        (*env)->GetIntArrayRegion(env, overlay, 0, corners, (jint *) overlays);
    }

    tile->plan = calloc((size_t) corners, sizeof(uint32_t));

    if (levels != NULL && colours != NULL) {
        if (level != NULL) {
            (*env)->GetIntArrayRegion(env, level, 0, corners, (jint *) levels);
        }
        if (colour != NULL) {
            (*env)->GetIntArrayRegion(env, colour, 0, corners, (jint *) colours);
        }

        wateredTileHandedOver(waterColour, colours, overlays, tile->texture, corners);

        for (int corner = 0; corner < corners; corner++) {
            int worldX = (x << ground->tileShift) + tile->across[corner];
            int worldZ = (z << ground->tileShift) + tile->along[corner];

            if (wearsNothing(ground, tile->texture == NULL ? -1 : tile->texture[corner])) {
                tile->texture[corner] = -1;
            }

            tile->up[corner] = (int16_t) (averageHeight(ground, worldX, worldZ) + levels[corner]);

            if (corner == 0) {
                groundHeightsSeen(ground, x, z, tile->up[0],
                        ground->waterHeights == NULL ? -99999 : heightAt(ground, x, z));
            }
            tile->light[corner] = (unsigned char) shadeInside(ground, x, z,
                tile->across[corner], tile->along[corner]);
            /*
             * A corner the client gives no colour of its own to stands where the floor opens onto
             * the one below. Nothing of it is drawn as the world is seen, so what is under the
             * floor shows through, but the map still shows the floor it stands in: the client
             * lays a colour over such a corner for exactly that.
             */
            tile->hollow[corner] = colours[corner] == NO_COLOUR;
            tile->bare[corner] = tile->hollow[corner]
                    && (overlays == NULL || overlays[corner] == NO_COLOUR);

            int named = colours[corner] == NO_COLOUR ? BLACK : colours[corner] & 0xFFFF;

            tile->colour[corner] = litCorner(ground, named, tile->light[corner],
                    x, z, tile->across[corner], tile->along[corner],
                    tile->texture == NULL ? -1 : tile->texture[corner], 1);

            textureOnTheMap(tile->texture == NULL ? -1 : tile->texture[corner],
                    tile->texture == NULL || tile->texture[corner] == -1
                        ? NULL : textureMetricsFor(tile->texture[corner]));

            if (tile->plan != NULL) {
                /*
                 * What a corner stands for on the map.
                 *
                 * A face wearing a texture the player cannot turn off stands for that texture's
                 * own colour and nothing else: a tile on the map is a handful of pixels across
                 * and a texture drawn that small says nothing, so the texture is worth more than
                 * anything laid over it. Such a corner takes no light and no water either. It is
                 * the same colour wherever the face lies and whatever time of day it is, which is
                 * what makes a map of one colour per texture readable at all.
                 *
                 * A corner wearing anything else stands for the colour the client laid over the
                 * face, and failing that for the colour of the ground it is on, and either way it
                 * is drawn the way the world is: lit, and carried through whatever water covers
                 * it.
                 *
                 * The texture is the face's own rather than the corner's, because a face is what
                 * wears one.
                 */
                int worn = tile->texture == NULL ? -1 : tile->texture[corner - corner % 3];
                const TextureMetrics *metrics = worn == -1 ? NULL : textureMetricsFor(worn);
                int laid = overlays == NULL ? NO_COLOUR : overlays[corner];

                if (metrics != NULL && !metrics->disableable) {
                    planPicked(named, laid, worn, metrics, metrics->averageColour);
                    tile->plan[corner] = colourOf(metrics->averageColour);
                } else {
                    int stands = laid == NO_COLOUR ? named : laid & 0xFFFF;
                    planPicked(named, laid, worn, metrics, stands);

                    tile->plan[corner] = litCorner(ground, stands, tile->light[corner],
                            x, z, tile->across[corner], tile->along[corner],
                            tile->texture == NULL ? -1 : tile->texture[corner],
                            !planTheOldWay());

                    tile->plan[corner] = carriedUnderWater(tile->plan[corner],
                            (uint32_t) tile->waterColour, cornerUnder(tile, corner));
                }
            }
        }
    }

    free(levels);
    free(colours);
    free(overlays);

    tileFree(*tileAt(ground, x, z));
    *tileAt(ground, x, z) = tile;
    handing.kept++;
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
 * them. The tile reads the mark back when it is next drawn and builds its picture afresh.
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
 * How much of the light one place of the shadow map takes away, out of the whole of it.
 *
 * Five places decide a place of the picture, so a place nothing stands over keeps the whole of
 * the light and one everything stands over keeps a hundred and eighty five parts of it.
 */
enum { SHADOW_PER_PLACE = 9 };

/** How far a place on a texture is held, which is what a place in the picture is reached from. */
enum { TEXTURE_PLACE_SHIFT = 7 };

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

    if (!switchedOff("SW3D_NO_GROUND_SHADOW")) {
        moveShadow(groundOf(env, self), (const Shadow *) (intptr_t) nativeIdOf(env, shadow),
            x, height, z, 1);
    }
}

/**
 * Takes a model's shadow off the ground under it, which the client does before the model moves.
 */
JNIEXPORT void JNICALL Java_t_wa(JNIEnv *env, jobject self, jobject shadow, jint x, jint height,
                                  jint z, jint unused, jboolean immediate) {
    (void) unused;
    (void) immediate;

    if (!switchedOff("SW3D_NO_GROUND_SHADOW")) {
        moveShadow(groundOf(env, self), (const Shadow *) (intptr_t) nativeIdOf(env, shadow),
            x, height, z, 0);
    }
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
/**
 * How wide a picture of the shadow over one tile is, in places.
 *
 * It is how many places of the shadow map fall across one tile, which is the tile's own width
 * shifted down by however finely the client asked for shadows to be drawn.
 */
static int shadowPlacesAcross(const Ground *ground) {
    return ground->tileSize >> shadowShift();
}

/** How wide the run the pictures are kept in is, which is what a place in one is reached by. */
enum { SHADOW_TEXTURE_ACROSS = 256 };

/**
 * The first place in a band given out to a tile.
 *
 * Nothing is kept in the places before it. A tile that has been given nowhere reads as nought,
 * and nought has to mean nowhere rather than the start of the first band.
 */
enum { SHADOW_FIRST = 4 };

/**
 * Finds this tile somewhere to keep its picture of the shadow over it.
 *
 * Tiles are laid side by side across a band as tall as a tile is wide. When what is left of a
 * band is narrower than a tile the band is abandoned where it is, and the next tile asking opens
 * a new one.
 */
static int shadowRoomFor(Ground *ground, int across) {
    if (ground->shadowTexture == NULL || ground->shadowNext == 0) {
        size_t band = (size_t) across * SHADOW_TEXTURE_ACROSS;
        size_t wanted = ground->shadowBand + band;
        unsigned char *grown = realloc(ground->shadowTexture, wanted);

        if (grown == NULL) {
            return 0;
        }

        memset(grown + ground->shadowBand, 0, band);
        ground->shadowTexture = grown;
        ground->shadowTextureRoom = wanted;
        ground->shadowNext = SHADOW_FIRST;
    }

    int at = ground->shadowBand + ground->shadowNext;
    ground->shadowNext += across;

    if (ground->shadowNext - 1 >= SHADOW_TEXTURE_ACROSS - across) {
        ground->shadowBand = (int) ground->shadowTextureRoom;
        ground->shadowNext = 0;
    }

    return at;
}

/**
 * Works out the picture of the shadow over one tile, if a shadow has moved over it since the last
 * time it was worked out.
 *
 * Each place of the picture counts how many of the five places of the shadow map around it, being
 * the place itself and the four beside it, anything is standing over. The count is turned into
 * how much of the light gets through: none at all leaves the light whole, and each one takes a
 * ninth of a tenth of it away.
 */
const unsigned char *groundTileShadow(const void *held, void *at, int x, int z, int *shift) {
    Ground *ground = (Ground *) held;
    Tile *tile = at;

    *shift = 0;

    if (ground == NULL || tile == NULL || ground->shade == NULL || ground->reshade == NULL) {
        return NULL;
    }

    int across = shadowPlacesAcross(ground);
    if (across <= 0) {
        return NULL;
    }

    *shift = TEXTURE_PLACE_SHIFT - shiftOf(across);

    size_t which = (size_t) x * (size_t) ground->sizeZ + (size_t) z;
    if (!ground->reshade[which]) {
        return tile->shadowAt == 0 ? NULL : ground->shadowTexture + tile->shadowAt;
    }

    ground->reshade[which] = 0;

    if (tile->shadowAt == 0) {
        tile->shadowAt = shadowRoomFor(ground, across);

        if (tile->shadowAt == 0) {
            return NULL;
        }
    }

    tile->shadowShift = *shift;

    /* One place in and one place down, which is the margin the shadow map is given. */
    int from = (z * ground->shadeAcross + x) * across + ground->shadeAcross + 1;
    int into = tile->shadowAt;

    for (int down = 0; down < across; down++) {
        for (int column = 0; column < across; column++) {
            int over = ground->shade[from] != 0;
            over += ground->shade[from - 1] != 0;
            over += ground->shade[from + 1] != 0;
            over += ground->shade[from - ground->shadeAcross] != 0;
            over += ground->shade[from + ground->shadeAcross] != 0;

            ground->shadowTexture[into] = (unsigned char) ~(over * SHADOW_PER_PLACE);
            from++;
            into++;
        }

        from += ground->shadeAcross - across;
        into += SHADOW_TEXTURE_ACROSS - across;
    }

    return ground->shadowTexture + tile->shadowAt;
}

int groundTileWatered(const void *at) {
    const Tile *tile = at;
    return tile != NULL && tile->watered;
}

int groundTileWaterColour(const void *at) {
    const Tile *tile = at;
    return tile == NULL ? 0 : tile->waterColour;
}

/**
 * How much of the water stands over one corner of a tile, out of the whole.
 *
 * Nothing at the surface, and the whole of the water once the corner is half as far down as the
 * client says the water reaches. The client counts the reach the long way and the toolkit only
 * ever uses half of it, so a corner as deep as the reach is well past showing anything.
 */
float groundTileCornerUnder(const void *at, int corner) {
    return cornerUnder(at, corner);
}

/**
 * How deep the water over one corner of a tile is, as the client handed it over, and minus one
 * where the client gave the tile no depths at all.
 */
int groundTileCornerDepth(const void *at, int corner) {
    const Tile *tile = at;
    if (tile == NULL || tile->depth == NULL || corner >= tile->corners) {
        return -1;
    }

    return (uint16_t) tile->depth[corner];
}

/** Whether the client handed this tile a depth for its corners, whatever else it handed with it. */
int groundTileCarriesDepths(const void *at) {
    const Tile *tile = at;
    return tile != NULL && tile->depth != NULL && anyDepth(tile->depth, tile->corners);
}

int groundTileFaceTexture(const void *at, int face) {
    const Tile *tile = at;
    if (tile->texture == NULL || face * 3 >= tile->corners) {
        return -1;
    }

    return tile->texture[face * 3];
}

/**
 * The colour one corner is drawn in on the map, which is the colour the client laid over the face
 * it belongs to. Nothing comes back where the client laid none, and the corner's own colour is
 * what the map is drawn in instead.
 */
int groundTilePlanColour(const void *at, int corner, uint32_t *colour) {
    const Tile *tile = at;
    if (tile->plan == NULL || corner >= tile->corners) {
        return 0;
    }

    *colour = tile->plan[corner];
    return 1;
}

/**
 * Whether the client gave every corner of a face no colour, which is how it says the floor opens
 * onto the one below rather than how it says the floor is black.
 */
int groundTileFaceHollow(const void *at, int face) {
    const Tile *tile = at;
    if (tile->hollow == NULL || face * 3 + 2 >= tile->corners) {
        return 0;
    }

    return tile->hollow[face * 3] && tile->hollow[face * 3 + 1] && tile->hollow[face * 3 + 2];
}

/**
 * Whether the client gave every corner of a face no colour of either kind, so that the face has
 * nothing to be drawn in even on the map.
 */
int groundTileFaceBare(const void *at, int face) {
    const Tile *tile = at;
    if (tile->bare == NULL || face * 3 + 2 >= tile->corners) {
        return 0;
    }

    return tile->bare[face * 3] && tile->bare[face * 3 + 1] && tile->bare[face * 3 + 2];
}

/** The texture one corner of a tile names, which its neighbours in the same face may not share. */
int groundTileCornerTexture(const void *at, int corner) {
    const Tile *tile = at;
    if (tile->texture == NULL || corner >= tile->corners) {
        return -1;
    }

    return tile->texture[corner];
}

/** How wide a texture one corner of a tile names is laid, in the world's own units. */
int groundTileCornerSize(const void *at, int corner) {
    const Tile *tile = at;
    if (tile->size == NULL || corner >= tile->corners) {
        return 0;
    }

    return tile->size[corner];
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

            if (x == 0 || z == 0) {
                continue;
            }

            /*
             * A corner on the far side of the grid faces nowhere in particular rather than
             * nowhere at all: it has a length, so it takes the light the world gives everything
             * and none of the sun. A corner on the near side has no length either, and is left
             * darker still. Both were measured from the frames the shipped toolkit draws; why the
             * two sides are not alike has not been read out of it.
             */
            if (x == across - 1 || z == along - 1) {
                normal[3] = 1.0f;
                continue;
            }

            int stepX = ground->waterHeights == NULL
                ? heightAt(ground, x + 1, z) - heightAt(ground, x - 1, z)
                : waterHeightAt(ground, x + 1, z) - waterHeightAt(ground, x - 1, z);
            int stepZ = ground->waterHeights == NULL
                ? heightAt(ground, x, z + 1) - heightAt(ground, x, z - 1)
                : waterHeightAt(ground, x, z + 1) - waterHeightAt(ground, x, z - 1);

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

/**
 * How much of the world one whole width of a face's texture covers.
 *
 * A tile whose texture is given the size of a tile is covered by exactly one of it; one given
 * half that is covered by four.
 */
int groundTileFaceSize(const void *at, int face) {
    const Tile *tile = at;
    if (tile->size == NULL || face * 3 >= tile->corners) {
        return 0;
    }

    return tile->size[face * 3];
}
