/*
 * The textures the client hands over, and where the toolkit keeps them.
 *
 * A texture arrives in one of two shapes. The client may hand over its pixels along with
 * everything it knows about the texture, or it may hand over only what it knows. The second is
 * what a texture worn by something the client has decided to draw arrives as before anyone has
 * asked for a single pixel of it. The two are kept apart because a texture with pixels costs
 * sixty four kilobytes and one without costs a few dozen bytes, and there is room for two hundred
 * of each.
 *
 * Nothing hands a texture to the renderer. The renderer asks for one by the number the client
 * gave it, and when the toolkit has never been given that number it asks the client for it and
 * looks again. That is why the toolkit keeps the object the client built it through: the ask is a
 * call back into the client, not a return value.
 *
 * The pixels of all two hundred live in one run of memory laid out two textures wide, so a row of
 * one texture is a hundred and twenty eight pixels inside a row of two hundred and fifty six.
 */

#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

/** Every texture is this square, whatever size the client says it drew it at. */
enum { TEXTURE_SIZE = 128 };

/** How many textures may be held at once, with pixels and without. */
enum { TEXTURE_SLOTS = 200 };

/** The client names a texture by an unsigned short, and this one means none. */
enum { NO_TEXTURE = 0xffff };

/** What a slot that holds nothing, and a list that has run out, are marked with. */
enum { NO_SLOT = -1 };

struct Texture {
    TextureMetrics metrics;

    /**
     * How far a texture that moves of its own accord has moved by now, which the renderer adds to
     * every coordinate it reads the texture at.
     */
    float offsetU;
    float offsetV;

    /** Whether this texture is already waiting to be moved on at the next quiet moment. */
    int walking;

    uint32_t *pixels;
};

/**
 * Which of the two hundred slots holds which texture, and in which order they were last wanted.
 *
 * The order is a list rather than a stamp on each slot because the only two questions asked of it
 * are which slot was wanted longest ago, which is the head, and moving a slot to the end, and a
 * list answers both without looking at the other hundred and ninety nine.
 */
typedef struct {
    signed char slotOf[NO_TEXTURE + 1];
    unsigned short textureIn[TEXTURE_SLOTS];
    signed char head;
    signed char tail;
    signed char next[TEXTURE_SLOTS];
    signed char previous[TEXTURE_SLOTS];
} Ordering;

/**
 * A texture that has never been held reads as this rather than as none, because none is a texture
 * number the client may ask for.
 */
enum { UNHELD = 0xff };

static struct {
    uint32_t *store;

    Texture withPixels[TEXTURE_SLOTS];
    TextureMetrics withoutPixels[TEXTURE_SLOTS];

    Ordering pixelOrder;
    Ordering metricsOrder;

    /** The slots wanting to be moved on, gathered as they are asked for and emptied all at once. */
    unsigned char walking[TEXTURE_SLOTS];
    int walkingCount;

    JavaVM *vm;
    jobject client;
    jmethodID askForTexture;
    jmethodID askForMetrics;
} cache;

/**
 * Where a slot's pixels sit in the store the two hundred of them share.
 */
static uint32_t *slotPixels(int slot) {
    return cache.store + ((size_t) ((slot / 2) * TEXTURE_STRIDE + (slot & 1)) * TEXTURE_SIZE);
}

static void orderingReset(Ordering *ordering) {
    memset(ordering->slotOf, NO_SLOT, sizeof(ordering->slotOf));

    for (int slot = 0; slot < TEXTURE_SLOTS; slot++) {
        ordering->textureIn[slot] = UNHELD;
        ordering->next[slot] = (signed char) (slot + 1);
        ordering->previous[slot] = (signed char) (slot - 1);
    }

    ordering->head = 0;
    ordering->previous[0] = NO_SLOT;
    ordering->tail = (signed char) (TEXTURE_SLOTS - 1);
    ordering->next[TEXTURE_SLOTS - 1] = NO_SLOT;
}

/**
 * Moves a slot to the end of the order, so that the head is always the one wanted longest ago.
 */
static void wantedNow(Ordering *ordering, int slot) {
    signed char previous = ordering->previous[slot];
    if (previous == NO_SLOT) {
        ordering->head = ordering->next[slot];
    } else {
        ordering->next[previous] = ordering->next[slot];
    }

    signed char next = ordering->next[slot];
    if (next == NO_SLOT) {
        ordering->tail = ordering->previous[slot];
    } else {
        ordering->previous[next] = ordering->previous[slot];
    }

    signed char tail = ordering->tail;
    ordering->previous[slot] = tail;
    ordering->next[slot] = NO_SLOT;
    ordering->next[tail] = (signed char) slot;
    ordering->tail = (signed char) slot;
}

/**
 * Hands the slot wanted longest ago to a texture, throwing out whatever was in it.
 *
 * The slot is not moved to the end of the order here. The client only ever uploads a texture
 * because the renderer just asked it for one, and the renderer moves it as soon as it looks
 * again, so a texture that nobody wanted is thrown out by the very next upload.
 */
static int slotFor(Ordering *ordering, int texture) {
    int slot = ordering->head;
    unsigned short held = ordering->textureIn[slot];

    if (held != UNHELD) {
        ordering->slotOf[held] = NO_SLOT;
    }

    ordering->textureIn[slot] = (unsigned short) texture;
    ordering->slotOf[texture] = (signed char) slot;
    return slot;
}

/**
 * The five weights a blurred texel is gathered with, a normal curve one texel wide.
 *
 * They do not add up to one. What a texel is divided by is worked out as it is gathered, because
 * a texel the blur was told to leave out takes its weight out of the sum with it.
 */
static const float BLUR[5] = { 0.05399097f, 0.24197072f, 0.39894229f, 0.24197072f, 0.05399097f };

/** Where a tap lands when the texture does not repeat: pinned to the edge it ran off. */
static int pinned(int at) {
    int held = at < TEXTURE_SIZE ? at : TEXTURE_SIZE - 1;
    return held > 0 ? held : 0;
}

/** Where a tap lands when the texture repeats: round the other side. */
static int wrapped(int at) {
    return at & (TEXTURE_SIZE - 1);
}

/**
 * Gathers one texel from five, in whichever direction the caller steps.
 *
 * A texture the client says has no alpha is gathered only from the texels that are not empty, and
 * an empty texel is left empty rather than gathered at all. That keeps the colour of a texture
 * drawn on nothing from bleeding out into the nothing around it.
 */
static uint32_t gathered(const uint32_t *from, int along, int across, int step, int repeats,
                         int keepsEmpty) {
    float alpha = 0.0f;
    float red = 0.0f;
    float green = 0.0f;
    float blue = 0.0f;
    float weight = 0.0f;

    for (int tap = 0; tap < 5; tap++) {
        int at = along + tap - 2;
        int landed = repeats ? wrapped(at) : pinned(at);
        uint32_t texel = from[landed * step + across];

        if (keepsEmpty || texel != 0) {
            float share = BLUR[tap];
            alpha += (float) (texel >> 24) * share;
            red += (float) ((texel >> 16) & 0xff) * share;
            green += (float) ((texel >> 8) & 0xff) * share;
            blue += (float) (texel & 0xff) * share;
            weight += share;
        }
    }

    float scale = 1.0f / weight;
    return (uint32_t) ((int) (alpha * scale) << 24 | (int) (red * scale) << 16
        | (int) (green * scale) << 8 | (int) (blue * scale));
}

/**
 * Lays a texture the client handed over into its slot, gathering it as it goes.
 *
 * Both directions are always gathered. What the client says is whether the texture repeats in
 * each of them, which only decides where a tap that runs off the edge lands.
 */
static void layDown(const uint32_t *from, uint32_t *into, int repeatsU, int repeatsV,
                    int keepsEmpty) {
    static uint32_t rowGathered[TEXTURE_SIZE * TEXTURE_SIZE];

    for (int down = 0; down < TEXTURE_SIZE; down++) {
        const uint32_t *row = from + down * TEXTURE_SIZE;
        uint32_t *into1 = rowGathered + down * TEXTURE_SIZE;

        for (int across = 0; across < TEXTURE_SIZE; across++) {
            if (!keepsEmpty && row[across] == 0) {
                into1[across] = 0;
            } else {
                into1[across] = gathered(row, across, 0, 1, repeatsU, keepsEmpty);
            }
        }
    }

    for (int down = 0; down < TEXTURE_SIZE; down++) {
        uint32_t *row = into + down * TEXTURE_STRIDE;

        for (int across = 0; across < TEXTURE_SIZE; across++) {
            if (!keepsEmpty && rowGathered[down * TEXTURE_SIZE + across] == 0) {
                row[across] = 0;
            } else {
                row[across] = gathered(rowGathered, down, across, TEXTURE_SIZE, repeatsV,
                                       keepsEmpty);
            }
        }
    }
}

static TextureMetrics metricsFrom(jshort size, jint alphaBlendMode, jbyte effectType,
                                  jbyte effectParam1, jint effectParam2, jboolean small,
                                  jbyte alpha, jbyte aByte57, jbyte speedU, jbyte speedV,
                                  jboolean disableable, jboolean aBoolean234, jboolean aBoolean239,
                                  jboolean repeatsU, jboolean repeatsV, jbyte aByte53,
                                  jboolean aBoolean237, jboolean aBoolean238, jint colourOp) {
    TextureMetrics metrics;

    metrics.size = (unsigned short) size;
    metrics.alphaBlendMode = alphaBlendMode;
    metrics.effectType = (unsigned char) effectType;
    metrics.effectParam1 = (unsigned char) effectParam1;
    metrics.effectParam2 = effectParam2;
    metrics.small = small != JNI_FALSE;
    metrics.alpha = (unsigned char) alpha;
    metrics.aByte57 = (unsigned char) aByte57;
    metrics.speedU = (signed char) speedU;
    metrics.speedV = (signed char) speedV;
    metrics.disableable = disableable != JNI_FALSE;
    metrics.aBoolean234 = aBoolean234 != JNI_FALSE;
    metrics.aBoolean239 = aBoolean239 != JNI_FALSE;
    metrics.repeatsU = repeatsU != JNI_FALSE;
    metrics.repeatsV = repeatsV != JNI_FALSE;
    metrics.aByte53 = (unsigned char) aByte53;
    metrics.aBoolean237 = aBoolean237 != JNI_FALSE;
    metrics.aBoolean238 = aBoolean238 != JNI_FALSE;
    metrics.colourOp = colourOp;
    return metrics;
}

/**
 * Whether a texture wearing these moves of its own accord, and so has to be moved on every frame.
 */
static int walks(const TextureMetrics *metrics) {
    return metrics->speedU != 0 || metrics->speedV != 0;
}

static void walkLater(int slot) {
    if (!cache.withPixels[slot].walking) {
        cache.withPixels[slot].walking = 1;
        cache.walking[cache.walkingCount++] = (unsigned char) slot;
    }
}

/**
 * Asks the client for a texture the toolkit has never been given, which it answers by handing one
 * over through the uploads below.
 *
 * The renderer reaches this from whichever thread is drawing, so the thread is joined to the
 * virtual machine first. A thread already joined is handed the same environment back.
 */
static int askClient(jmethodID method, int texture) {
    if (cache.vm == NULL || cache.client == NULL || method == NULL) {
        return 0;
    }

    JNIEnv *env = NULL;
    if ((*cache.vm)->GetEnv(cache.vm, (void **) &env, JNI_VERSION_1_1) != JNI_OK
        && (*cache.vm)->AttachCurrentThread(cache.vm, (void **) &env, NULL) != JNI_OK) {
        return 0;
    }

    return (*env)->CallBooleanMethod(env, cache.client, method, (jshort) texture) != JNI_FALSE;
}

const Texture *textureFor(int texture) {
    if (texture == NO_TEXTURE || cache.store == NULL) {
        return NULL;
    }

    signed char slot = cache.pixelOrder.slotOf[texture];
    if (slot == NO_SLOT) {
        if (!askClient(cache.askForTexture, texture)) {
            return NULL;
        }

        slot = cache.pixelOrder.slotOf[texture];
        if (slot == NO_SLOT) {
            return NULL;
        }
    }

    wantedNow(&cache.pixelOrder, slot);

    if (walks(&cache.withPixels[slot].metrics)) {
        walkLater(slot);
    }

    return &cache.withPixels[slot];
}

const TextureMetrics *textureMetricsFor(int texture) {
    if (texture == NO_TEXTURE || cache.store == NULL) {
        return NULL;
    }

    signed char slot = cache.pixelOrder.slotOf[texture];
    if (slot != NO_SLOT) {
        wantedNow(&cache.pixelOrder, slot);

        if (walks(&cache.withPixels[slot].metrics)) {
            walkLater(slot);
        }

        return &cache.withPixels[slot].metrics;
    }

    slot = cache.metricsOrder.slotOf[texture];
    if (slot == NO_SLOT) {
        if (!askClient(cache.askForMetrics, texture)) {
            return NULL;
        }

        slot = cache.metricsOrder.slotOf[texture];
        if (slot == NO_SLOT) {
            return NULL;
        }
    }

    wantedNow(&cache.metricsOrder, slot);
    return &cache.withoutPixels[slot];
}

const uint32_t *texturePixels(const Texture *texture) {
    return texture->pixels;
}

const TextureMetrics *textureMetrics(const Texture *texture) {
    return &texture->metrics;
}

void textureOffsets(const Texture *texture, float *u, float *v) {
    *u = texture->offsetU;
    *v = texture->offsetV;
}

/**
 * Makes room for every texture the toolkit may be given, once, however many toolkits the client
 * builds on top of it.
 *
 * The store is not counted against what the toolkit reports holding. The toolkit this replaces
 * takes it from the system directly rather than through anything that counts.
 */
void textureCacheReady(JNIEnv *env, jobject client) {
    if (cache.store == NULL) {
        cache.store = calloc((size_t) TEXTURE_SLOTS * TEXTURE_SIZE * TEXTURE_SIZE,
                             sizeof(uint32_t));
        if (cache.store == NULL) {
            return;
        }

        orderingReset(&cache.pixelOrder);
        orderingReset(&cache.metricsOrder);

        for (int slot = 0; slot < TEXTURE_SLOTS; slot++) {
            cache.withPixels[slot].pixels = slotPixels(slot);
        }
    }

    (*env)->GetJavaVM(env, &cache.vm);

    if (cache.client != NULL) {
        (*env)->DeleteGlobalRef(env, cache.client);
    }
    cache.client = (*env)->NewGlobalRef(env, client);

    jclass owner = (*env)->GetObjectClass(env, client);
    cache.askForTexture = (*env)->GetMethodID(env, owner, "WA", "(S)Z");
    cache.askForMetrics = (*env)->GetMethodID(env, owner, "c", "(S)Z");
    (*env)->DeleteLocalRef(env, owner);
}

/**
 * Moves every texture that moves of its own accord on to where it stands at this moment.
 *
 * The moment is counted in hundredths rather than in milliseconds, and it comes back round every
 * hundred and twenty eight of them, so a texture that scrolls is back where it started about
 * every second and a quarter.
 */
void textureCacheService(int time) {
    if (cache.walkingCount == 0) {
        return;
    }

    int step = (time / 10) % TEXTURE_SIZE;

    for (int which = 0; which < cache.walkingCount; which++) {
        Texture *texture = &cache.withPixels[cache.walking[which]];

        texture->offsetU = (float) (texture->metrics.speedU * step);
        texture->offsetV = (float) (texture->metrics.speedV * step);
        texture->walking = 0;
    }

    cache.walkingCount = 0;
}

/**
 * Takes what the client knows about a texture without taking any of its pixels.
 */
JNIEXPORT void JNICALL Java_oa_AA(JNIEnv *env, jobject self, jshort texture, jshort size,
                                   jint alphaBlendMode, jbyte effectType, jbyte effectParam1,
                                   jint effectParam2, jboolean small, jbyte alpha, jbyte aByte57,
                                   jbyte speedU, jbyte speedV, jboolean disableable,
                                   jboolean aBoolean234, jboolean aBoolean239, jboolean repeatsU,
                                   jboolean repeatsV, jbyte aByte53, jboolean aBoolean237,
                                   jboolean aBoolean238, jint colourOp) {
    (void) env;
    (void) self;

    if (cache.store == NULL) {
        return;
    }

    int slot = slotFor(&cache.metricsOrder, (unsigned short) texture);
    cache.withoutPixels[slot] = metricsFrom(size, alphaBlendMode, effectType, effectParam1,
        effectParam2, small, alpha, aByte57, speedU, speedV, disableable, aBoolean234,
        aBoolean239, repeatsU, repeatsV, aByte53, aBoolean237, aBoolean238, colourOp);
}

/**
 * Takes a texture's pixels along with everything the client knows about it.
 */
JNIEXPORT void JNICALL Java_oa_CA(JNIEnv *env, jobject self, jshort texture, jintArray given,
                                   jshort size, jint alphaBlendMode, jbyte effectType,
                                   jbyte effectParam1, jint effectParam2, jboolean small,
                                   jbyte alpha, jbyte aByte57, jbyte speedU, jbyte speedV,
                                   jboolean disableable, jboolean aBoolean234,
                                   jboolean aBoolean239, jboolean repeatsU, jboolean repeatsV,
                                   jbyte aByte53, jboolean aBoolean237, jboolean aBoolean238,
                                   jint colourOp) {
    (void) self;

    if (cache.store == NULL || given == NULL) {
        return;
    }

    int slot = slotFor(&cache.pixelOrder, (unsigned short) texture);
    Texture *held = &cache.withPixels[slot];

    held->metrics = metricsFrom(size, alphaBlendMode, effectType, effectParam1, effectParam2,
        small, alpha, aByte57, speedU, speedV, disableable, aBoolean234, aBoolean239, repeatsU,
        repeatsV, aByte53, aBoolean237, aBoolean238, colourOp);
    held->offsetU = 0.0f;
    held->offsetV = 0.0f;

    jint *from = (*env)->GetPrimitiveArrayCritical(env, given, NULL);
    if (from != NULL) {
        layDown((const uint32_t *) from, held->pixels, held->metrics.repeatsU,
                held->metrics.repeatsV, alphaBlendMode != 1);
        (*env)->ReleasePrimitiveArrayCritical(env, given, from, JNI_ABORT);
    }
}
