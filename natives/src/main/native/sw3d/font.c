/*
 * Fonts, and putting a letter on the buffer.
 *
 * There are two kinds and the client picks between them by whether a font is monospaced. A mono
 * font carries one byte per pixel saying only whether there is ink there, and the client gives the
 * colour every time it draws. A paletted font carries a byte per pixel that stands for one of the
 * colours the font came with, and the colour the client gives is used only for the shadow behind
 * the letter.
 *
 * A letter's own pixels are not stretched or blended into shape here. The client has already laid
 * out where every letter goes, so each one is put down where it is asked for and clipped there.
 */

#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

/** One letter: where its ink is, how big it is, and where it sits against the line. */
typedef struct {
    unsigned char *ink;
    int width;
    int height;
    int acrossFromPen;
    int downFromPen;
} Letter;

typedef struct {
    int count;
    Letter *letters;

    /** The colours a paletted font came with, and nothing for a mono one. */
    uint32_t *palette;
    int paletteSize;
} Font;

static Font *fontOf(JNIEnv *env, jobject self) {
    return (Font *) (intptr_t) nativeIdOf(env, self);
}

static void fontFree(Font *font) {
    if (font == NULL) {
        return;
    }

    for (int letter = 0; letter < font->count; letter++) {
        free(font->letters[letter].ink);
    }

    free(font->letters);
    free(font->palette);
    free(font);
}

/**
 * Reads the letters of a font, however its ink is meant to be read.
 *
 * The four numbers a letter carries arrive as four whole arrays rather than as one array of
 * letters, which is how the client holds them.
 */
static Font *readFont(JNIEnv *env, jobjectArray ink, jintArray width, jintArray height,
                      jintArray across, jintArray down) {
    if (ink == NULL || width == NULL || height == NULL || across == NULL || down == NULL) {
        return NULL;
    }

    int count = (int) (*env)->GetArrayLength(env, ink);
    Font *font = calloc(1, sizeof(Font));
    if (font == NULL) {
        return NULL;
    }

    font->count = count;
    font->letters = calloc((size_t) count, sizeof(Letter));
    if (font->letters == NULL) {
        free(font);
        return NULL;
    }

    int *numbers = calloc((size_t) count, sizeof(int));
    if (numbers == NULL) {
        fontFree(font);
        return NULL;
    }

    jintArray sources[4] = {width, height, across, down};
    for (int which = 0; which < 4; which++) {
        (*env)->GetIntArrayRegion(env, sources[which], 0, count, (jint *) numbers);

        for (int letter = 0; letter < count; letter++) {
            int *into[4] = {
                &font->letters[letter].width,
                &font->letters[letter].height,
                &font->letters[letter].acrossFromPen,
                &font->letters[letter].downFromPen
            };
            *into[which] = numbers[letter];
        }
    }

    free(numbers);

    for (int letter = 0; letter < count; letter++) {
        jbyteArray held = (jbyteArray) (*env)->GetObjectArrayElement(env, ink, letter);
        if (held == NULL) {
            continue;
        }

        int size = (int) (*env)->GetArrayLength(env, held);
        font->letters[letter].ink = calloc((size_t) size + 1, 1);
        if (font->letters[letter].ink != NULL) {
            (*env)->GetByteArrayRegion(env, held, 0, size, (jbyte *) font->letters[letter].ink);
        }

        (*env)->DeleteLocalRef(env, held);
    }

    allocatedGrew((size_t) count * sizeof(Letter));
    return font;
}

/**
 * Scales the three parts of a colour at once.
 *
 * The green is multiplied apart from the red and the blue so that all three fit one multiply
 * each without running into one another, which is how the toolkit does it. Nothing comes back in
 * the top byte.
 */
static uint32_t scaled(uint32_t colour, int by) {
    uint32_t green = ((colour & 0xFF00u) * (uint32_t) by) & 0xFF0000u;
    uint32_t redAndBlue = ((colour & 0xFF00FFu) * (uint32_t) by) & 0xFF00FF00u;
    return (green + redAndBlue) >> 8;
}

/** Where a letter is put down, after the clip has taken what it will. */
typedef struct {
    int left;
    int top;
    int width;
    int height;
    int fromLeft;
    int fromTop;
} Placed;

static int place(const Letter *letter, int x, int y, Placed *placed) {
    placed->left = x + letter->acrossFromPen;
    placed->top = y + letter->downFromPen;
    placed->width = letter->width;
    placed->height = letter->height;
    placed->fromLeft = 0;
    placed->fromTop = 0;

    if (placed->top < raster.clipTop) {
        placed->fromTop = raster.clipTop - placed->top;
        placed->height -= placed->fromTop;
        placed->top = raster.clipTop;
    }
    if (placed->top + placed->height > raster.clipBottom) {
        placed->height = raster.clipBottom - placed->top;
    }
    if (placed->left < raster.clipLeft) {
        placed->fromLeft = raster.clipLeft - placed->left;
        placed->width -= placed->fromLeft;
        placed->left = raster.clipLeft;
    }
    if (placed->left + placed->width > raster.clipRight) {
        placed->width = raster.clipRight - placed->left;
    }

    return placed->width > 0 && placed->height > 0;
}

static const Letter *letterOf(const Font *font, int which) {
    if (font == NULL || which < 0 || which >= font->count) {
        return NULL;
    }

    return font->letters[which].ink == NULL ? NULL : &font->letters[which];
}

/**
 * Puts one letter of a mono font down.
 *
 * The client's flag saying whether this is a shadow is not read. A letter with one byte per pixel
 * saying only whether there is ink there comes out the same either way.
 */
JNIEXPORT void JNICALL Java_h_fa(JNIEnv *env, jobject self, jchar which, jint x, jint y,
                                  jint colour, jboolean shadow) {
    (void) shadow;

    Font *font = fontOf(env, self);
    const Letter *letter = letterOf(font, which);
    Placed placed;

    if (letter == NULL || raster.pixels == NULL || !place(letter, x, y, &placed)) {
        return;
    }

    int alpha = (int) ((uint32_t) colour >> 24);
    if (alpha == 0) {
        return;
    }

    uint32_t ink = alpha == 0xFF ? (uint32_t) colour : scaled((uint32_t) colour, alpha);
    int rest = 0xFF - alpha;

    for (int row = 0; row < placed.height; row++) {
        const unsigned char *from = letter->ink
            + (size_t) (placed.fromTop + row) * (size_t) letter->width + (size_t) placed.fromLeft;
        uint32_t *to = raster.pixels
            + (size_t) (placed.top + row) * (size_t) raster.width + (size_t) placed.left;

        for (int column = 0; column < placed.width; column++) {
            if (from[column] == 0) {
                continue;
            }

            to[column] = alpha == 0xFF ? ink : scaled(to[column], rest) + ink;
        }
    }
}

/**
 * Puts one letter of a paletted font down.
 *
 * The letter's own colours are used for the letter itself, so the colour the client gives is
 * ignored there. It is used for the shadow, which is the same shape filled flat.
 */
JNIEXPORT void JNICALL Java_n_fa(JNIEnv *env, jobject self, jchar which, jint x, jint y,
                                  jint colour, jboolean shadow) {
    Font *font = fontOf(env, self);
    const Letter *letter = letterOf(font, which);
    Placed placed;

    if (letter == NULL || raster.pixels == NULL || !place(letter, x, y, &placed)) {
        return;
    }

    if (shadow != JNI_TRUE && font->palette == NULL) {
        return;
    }

    for (int row = 0; row < placed.height; row++) {
        const unsigned char *from = letter->ink
            + (size_t) (placed.fromTop + row) * (size_t) letter->width + (size_t) placed.fromLeft;
        uint32_t *to = raster.pixels
            + (size_t) (placed.top + row) * (size_t) raster.width + (size_t) placed.left;

        for (int column = 0; column < placed.width; column++) {
            int held = from[column];
            if (held == 0) {
                continue;
            }

            if (shadow == JNI_TRUE) {
                to[column] = (uint32_t) colour;
            } else if (held < font->paletteSize) {
                to[column] = font->palette[held];
            }
        }
    }
}

JNIEXPORT void JNICALL Java_h_JA(JNIEnv *env, jobject self, jobject toolkit, jobject pool,
                                  jobjectArray ink, jintArray width, jintArray height,
                                  jintArray across, jintArray down) {
    (void) toolkit;
    (void) pool;

    fontFree(fontOf(env, self));
    setNativeId(env, self, (jlong) (intptr_t) readFont(env, ink, width, height, across, down));
}

JNIEXPORT void JNICALL Java_n_S(JNIEnv *env, jobject self, jobject toolkit, jobject pool,
                                 jobjectArray ink, jintArray palette, jintArray width,
                                 jintArray height, jintArray across, jintArray down) {
    (void) toolkit;
    (void) pool;

    fontFree(fontOf(env, self));

    Font *font = readFont(env, ink, width, height, across, down);
    if (font != NULL && palette != NULL) {
        font->paletteSize = (int) (*env)->GetArrayLength(env, palette);
        font->palette = calloc((size_t) font->paletteSize, sizeof(uint32_t));
        if (font->palette != NULL) {
            (*env)->GetIntArrayRegion(env, palette, 0, font->paletteSize, (jint *) font->palette);
        }
    }

    setNativeId(env, self, (jlong) (intptr_t) font);
}

JNIEXPORT void JNICALL Java_h_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    fontFree(fontOf(env, self));
    setNativeId(env, self, 0);
}

JNIEXPORT void JNICALL Java_n_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    fontFree(fontOf(env, self));
    setNativeId(env, self, 0);
}
