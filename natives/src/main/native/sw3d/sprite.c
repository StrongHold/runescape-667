/*
 * Sprites, and putting them on the back buffer.
 *
 * A sprite owns a copy of its pixels rather than a view onto the array it was made from, because
 * the client is free to reuse that array the moment it returns.
 */

#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

typedef struct {
    int width;
    int height;

    /**
     * How much empty room the client cut off each side when it made the sprite.
     *
     * The client draws a sprite at the corner of the room it once took up rather than at the
     * corner of its ink, and it lays out interfaces by the size including that room. So a sprite
     * answers its own width plus what was cut off either side when it is asked how wide it is.
     */
    int fromLeft;
    int fromTop;
    int fromRight;
    int fromBottom;

    uint32_t *pixels;
} Sprite;

/** Room for a sprite's pixels, replacing whatever it held. */
static int spriteRoom(Sprite *sprite, int width, int height) {
    free(sprite->pixels);

    sprite->width = width;
    sprite->height = height;
    sprite->pixels = calloc((size_t) width * (size_t) height, sizeof(uint32_t));

    if (sprite->pixels == NULL) {
        sprite->width = 0;
        sprite->height = 0;
        return 0;
    }

    allocatedGrew((size_t) width * (size_t) height * sizeof(uint32_t));
    return 1;
}

static void spriteFree(Sprite *sprite) {
    if (sprite != NULL) {
        free(sprite->pixels);
        free(sprite);
    }
}

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

/** The four bytes of a pixel: blue, green, red and alpha, in the order they sit in it. */
enum { PARTS = 4 };

static int partOf(uint32_t pixel, int part) {
    return (int) (pixel >> (part * 8)) & 0xFF;
}

static uint32_t fromParts(const int *parts) {
    uint32_t pixel = 0;
    for (int part = 0; part < PARTS; part++) {
        pixel |= (uint32_t) (parts[part] & 0xFF) << (part * 8);
    }
    return pixel;
}

static int holdToWhite(int value) {
    return value > 0xFF ? 0xFF : value;
}

/**
 * The top sixteen bits of the product of two sixteen bit numbers, read as unsigned.
 *
 * This is one multiply of the toolkit's, and it has to be done in numbers wide enough to hold the
 * product: a colour asking for more than half alpha spreads a multiplier of nearly sixteen bits,
 * and the product of that with a part carried up eight places does not fit in a signed word.
 */
static int highProduct(int left, int right) {
    return (int) (((uint32_t) left & 0xFFFFu) * ((uint32_t) right & 0xFFFFu) >> 16);
}

/**
 * Narrows a sixteen bit number back down to one part of a pixel.
 *
 * The number is read as a **signed** one, so anything from half of sixteen bits upwards comes out
 * as nothing rather than as white. Nothing reaches that from an ordinary colour, but a colour
 * asking for more than half alpha does, and the part of the picture that should have come out
 * brightest comes out black instead. That is what the toolkit does.
 */
static int narrowToPart(int value) {
    int16_t signed16 = (int16_t) value;

    if (signed16 < 0) {
        return 0;
    } else if (signed16 > 0xFF) {
        return 0xFF;
    } else {
        return signed16;
    }
}

/**
 * How much of the sprite a mix keeps, and how much of the colour, held the way the toolkit holds
 * them.
 *
 * The toolkit takes the colour's top byte with a **signed** shift and packs two copies of it into
 * one pair of numbers. A colour asking for more than half alpha therefore spreads a number far
 * larger than a byte, every part of the answer runs past white, and the two halves of the pair
 * stop agreeing with each other. That is what it does, so it is what happens here. Which half of
 * the pair a part of a pixel uses depends on whether the part is an even one or an odd one.
 */
typedef struct {
    int keep[2];
    int lose[2];
} Mixture;

static Mixture mixtureOf(uint32_t colour) {
    int alpha = (int) colour >> 24;
    uint32_t packed = ((uint32_t) alpha << 16) | (uint32_t) alpha;
    uint32_t inverse = (packed ^ 0xFF00FFu) << 8;

    Mixture mixture;
    mixture.keep[0] = (int) (packed & 0xFFFF);
    mixture.keep[1] = (int) (packed >> 16);
    mixture.lose[0] = (int) (inverse & 0xFFFF);
    mixture.lose[1] = (int) (inverse >> 16);
    return mixture;
}

/**
 * One part of a pixel, once the sprite and the colour have been put together.
 *
 * The answer is sixteen bits wide. Everything but the mix leaves a part that already fits in a
 * byte, and the mix does not: when the answer is run into the buffer by its own alpha the whole
 * sixteen bits are what is run, and everywhere else the two halves of the mix are narrowed to
 * bytes first and then added with no room to carry.
 *
 * Each of these carries the sprite's part up eight places before it multiplies, which is why a
 * sprite multiplied by white comes back as it went in rather than one lower.
 */
static int combine(int op, int sprite, int colour, const Mixture *mixture, int part, int wide) {
    int half = part & 1;

    switch (op) {
        case OP_MULTIPLY:
            return highProduct(sprite << 8, colour);
        case OP_KEEP:
            return sprite;
        case OP_MIX: {
            int kept = highProduct(sprite << 8, mixture->keep[half]);
            int lost = highProduct(colour, mixture->lose[half]);
            return wide
                ? (kept + lost) & 0xFFFF
                : (narrowToPart(kept) + narrowToPart(lost)) & 0xFF;
        }
        case OP_ADD:
            return holdToWhite(sprite + colour);
        case OP_SUBTRACT:
            return sprite - colour < 0 ? 0 : sprite - colour;
        default:
            return sprite;
    }
}

/**
 * One part of a pixel, once the result has met what the buffer already held.
 */
static int lay(int mode, int put, int held, int alpha) {
    switch (mode) {
        case BLEND_ALPHA:
            return narrowToPart((highProduct((alpha << 8) & 0xFFFF, put)
                + highProduct(held << 8, alpha ^ 0xFF)) & 0xFFFF);
        case BLEND_ADD:
            return holdToWhite(held + put);
        default:
            return put;
    }
}

/**
 * Reads the pixels into a sprite.
 *
 * The rows of the source are `stride` apart, which is not always the sprite's own width: the
 * client cuts sprites out of larger sheets and hands over the whole sheet with an offset to the
 * corner it wants.
 */
JNIEXPORT void JNICALL Java_j_ua(JNIEnv *env, jobject self, jobject toolkit, jintArray source,
                                  jint offset, jint stride, jint width, jint height,
                                  jboolean opaque) {
    (void) toolkit;
    (void) opaque;

    if (source == NULL || width <= 0 || height <= 0) {
        return;
    }

    spriteFree((Sprite *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);

    Sprite *sprite = calloc(1, sizeof(Sprite));
    if (sprite == NULL) {
        return;
    }

    if (!spriteRoom(sprite, width, height)) {
        free(sprite);
        return;
    }

    jint *pixels = (*env)->GetPrimitiveArrayCritical(env, source, NULL);
    if (pixels != NULL) {
        for (int row = 0; row < height; row++) {
            const jint *from = pixels + offset + (jlong) row * stride;
            uint32_t *to = sprite->pixels + (size_t) row * (size_t) width;

            for (int column = 0; column < width; column++) {
                to[column] = (uint32_t) from[column];
            }
        }
        (*env)->ReleasePrimitiveArrayCritical(env, source, pixels, JNI_ABORT);
    }

    setNativeId(env, self, (jlong) (intptr_t) sprite);
}

/**
 * Draws a sprite at a place on the back buffer, clipped to it.
 */
JNIEXPORT void JNICALL Java_j_W(JNIEnv *env, jobject self, jlong handle, jint x, jint y,
                                 jint op, jint colour, jint mode) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    if (sprite == NULL || raster.pixels == NULL) {
        return;
    }

    if (op < OP_MULTIPLY || op > OP_SUBTRACT || mode < BLEND_OPAQUE || mode > BLEND_ADD) {
        return;
    }

    /*
     * The client asks for the corner of the room the sprite once took up, not the corner of its
     * ink, so what was cut off the top and the left is added back here.
     */
    x += sprite->fromLeft;
    y += sprite->fromTop;

    int firstRow = y < raster.clipTop ? raster.clipTop - y : 0;
    int firstColumn = x < raster.clipLeft ? raster.clipLeft - x : 0;
    int lastRow = y + sprite->height > raster.clipBottom ? raster.clipBottom - y : sprite->height;
    int lastColumn = x + sprite->width > raster.clipRight ? raster.clipRight - x : sprite->width;

    Mixture mixture = mixtureOf((uint32_t) colour);

    for (int row = firstRow; row < lastRow; row++) {
        const uint32_t *from = sprite->pixels + (size_t) row * (size_t) sprite->width;
        uint32_t *to = raster.pixels + (size_t) (y + row) * (size_t) raster.width + x;

        for (int column = firstColumn; column < lastColumn; column++) {
            int put[PARTS];

            for (int part = 0; part < PARTS; part++) {
                put[part] = combine(op, partOf(from[column], part),
                                    partOf((uint32_t) colour, part), &mixture, part,
                                    mode == BLEND_ALPHA);
            }

            int alpha = put[PARTS - 1];
            int laid[PARTS];

            for (int part = 0; part < PARTS; part++) {
                laid[part] = lay(mode, put[part], partOf(to[column], part), alpha);
            }

            to[column] = fromParts(laid);
        }
    }
}

JNIEXPORT void JNICALL Java_j_R(JNIEnv *env, jobject self, jlong handle, jboolean immediate) {
    (void) self;
    (void) immediate;

    spriteFree((Sprite *) (intptr_t) handle);
}

JNIEXPORT jint JNICALL Java_j_M(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->width;
}

JNIEXPORT jint JNICALL Java_j_I(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->height;
}

/**
 * How much empty room the client cut off each side, in the order left, top, right, bottom.
 */
JNIEXPORT void JNICALL Java_j_A(JNIEnv *env, jobject self, jlong handle, jint left, jint top,
                                 jint right, jint bottom) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    if (sprite == NULL) {
        return;
    }

    sprite->fromLeft = left;
    sprite->fromTop = top;
    sprite->fromRight = right;
    sprite->fromBottom = bottom;
}

JNIEXPORT void JNICALL Java_j_CA(JNIEnv *env, jobject self, jlong handle, jintArray destination) {
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    if (sprite == NULL || destination == NULL) {
        return;
    }

    jint written[4] = {
        sprite->fromLeft,
        sprite->fromTop,
        sprite->fromRight,
        sprite->fromBottom
    };

    (*env)->SetIntArrayRegion(env, destination, 0, 4, written);
}

/** How wide the sprite was before the empty room either side of it was cut off. */
JNIEXPORT jint JNICALL Java_j_wa(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->width + sprite->fromLeft + sprite->fromRight;
}

JNIEXPORT jint JNICALL Java_j_JA(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Sprite *sprite = (Sprite *) (intptr_t) handle;
    return sprite == NULL ? 0 : sprite->height + sprite->fromTop + sprite->fromBottom;
}

/**
 * An empty sprite of a given size, for the client to draw into.
 */
JNIEXPORT void JNICALL Java_j_EA(JNIEnv *env, jobject self, jobject toolkit,
                                  jint width, jint height) {
    (void) toolkit;

    spriteFree((Sprite *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);

    if (width <= 0 || height <= 0) {
        return;
    }

    Sprite *sprite = calloc(1, sizeof(Sprite));
    if (sprite == NULL) {
        return;
    }

    if (!spriteRoom(sprite, width, height)) {
        free(sprite);
        return;
    }

    setNativeId(env, self, (jlong) (intptr_t) sprite);
}

/**
 * A sprite from a picture the client holds as one byte per pixel and a table of colours.
 *
 * Where the client supplies no alpha, the byte nought means nothing is there at all and every
 * other byte is opaque. Where it does supply one, the byte is looked up whatever it is and the
 * alpha decides on its own, so nought is a colour like any other.
 */
JNIEXPORT void JNICALL Java_j_ma(JNIEnv *env, jobject self, jobject toolkit, jintArray palette,
                                  jbyteArray ink, jbyteArray alpha, jint offset, jint stride,
                                  jint width, jint height) {
    (void) toolkit;

    spriteFree((Sprite *) (intptr_t) nativeIdOf(env, self));
    setNativeId(env, self, 0);

    if (palette == NULL || ink == NULL || width <= 0 || height <= 0) {
        return;
    }

    Sprite *sprite = calloc(1, sizeof(Sprite));
    if (sprite == NULL) {
        return;
    }

    if (!spriteRoom(sprite, width, height)) {
        free(sprite);
        return;
    }

    int colours = (int) (*env)->GetArrayLength(env, palette);
    int taken = (int) (*env)->GetArrayLength(env, ink);

    uint32_t *table = calloc((size_t) colours, sizeof(uint32_t));
    signed char *held = calloc((size_t) taken, 1);
    signed char *clear = alpha == NULL
        ? NULL
        : calloc((size_t) (*env)->GetArrayLength(env, alpha), 1);

    if (table != NULL && held != NULL && (alpha == NULL || clear != NULL)) {
        (*env)->GetIntArrayRegion(env, palette, 0, colours, (jint *) table);
        (*env)->GetByteArrayRegion(env, ink, 0, taken, (jbyte *) held);
        if (clear != NULL) {
            (*env)->GetByteArrayRegion(env, alpha, 0,
                                       (*env)->GetArrayLength(env, alpha), (jbyte *) clear);
        }

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                size_t at = (size_t) offset + (size_t) row * (size_t) stride + (size_t) column;
                if (at >= (size_t) taken) {
                    continue;
                }

                int which = held[at] & 0xFF;
                uint32_t colour = which < colours ? table[which] : 0;
                uint32_t *into = sprite->pixels + (size_t) row * (size_t) width + (size_t) column;

                if (clear == NULL) {
                    *into = which == 0 ? 0 : colour | 0xFF000000u;
                } else {
                    *into = (colour & 0xFFFFFFu) | ((uint32_t) (clear[at] & 0xFF) << 24);
                }
            }
        }
    }

    free(table);
    free(held);
    free(clear);

    setNativeId(env, self, (jlong) (intptr_t) sprite);
}
