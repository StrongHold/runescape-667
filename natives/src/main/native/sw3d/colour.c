/*
 * The colours the client's artwork is kept in.
 *
 * A face carries six bits of hue, three of saturation and seven of lightness packed into a short,
 * and every one of the 65536 of them is turned into a colour once and looked up thereafter.
 *
 * The client's own Java version of this picks its gamma at random on each run, between 0.685 and
 * 0.715. The toolkit does not: it uses seven tenths every time, which is why two runs of it draw
 * a model identically and why matching it is possible at all.
 */

#include <math.h>

#include "sw3d.h"

enum {
    COLOURS = 65536
};

static const float GAMMA = 0.7f;
static const float THIRD = 1.0f / 3.0f;

static uint32_t table[COLOURS];
static int built;

/**
 * One of the three channels, given the two bounds the lightness and saturation put on it and
 * where round the wheel this channel sits.
 */
static float channel(float low, float high, float position) {
    if (position < 0.0f) {
        position += 1.0f;
    } else if (position > 1.0f) {
        position -= 1.0f;
    }

    if (position * 6.0f < 1.0f) {
        return low + (high - low) * 6.0f * position;
    }

    if (position * 2.0f < 1.0f) {
        return high;
    }

    if (position * 3.0f < 2.0f) {
        return low + (high - low) * (2.0f / 3.0f - position) * 6.0f;
    }

    return low;
}

static void build(void) {
    if (built) {
        return;
    }

    for (int i = 0; i < COLOURS; i++) {
        float hue = (float) ((i >> 10) & 0x3F) / 64.0f + 0.0078125f;
        float saturation = (float) ((i >> 7) & 0x7) / 8.0f + 0.0625f;
        float lightness = (float) (i & 0x7F) / 128.0f;

        float red = lightness;
        float green = lightness;
        float blue = lightness;

        if (saturation != 0.0f) {
            float high = lightness < 0.5f
                ? (saturation + 1.0f) * lightness
                : lightness + saturation - saturation * lightness;
            float low = lightness * 2.0f - high;

            red = channel(low, high, hue + THIRD);
            green = channel(low, high, hue);
            blue = channel(low, high, hue - THIRD);
        }

        table[i] = ((uint32_t) (powf(red, GAMMA) * 256.0f) << 16)
            | ((uint32_t) (powf(green, GAMMA) * 256.0f) << 8)
            | (uint32_t) (powf(blue, GAMMA) * 256.0f);
    }

    built = 1;
}

uint32_t colourOf(int packed) {
    build();
    return table[packed & (COLOURS - 1)];
}

/**
 * The colour a face is before any light reaches it.
 *
 * The model's own ambient scales the lightness, and the result is held away from both ends of the
 * range: a face is never quite black and never quite white, whatever it asked for.
 */
uint32_t unlitColour(int hsl, int ambient) {
    int lightness = ((hsl & 0x7F) * ambient) >> 7;

    if (lightness <= 1) {
        lightness = 2;
    } else if (lightness > 0x7E) {
        lightness = 0x7E;
    }

    return colourOf((hsl & 0xFF80) | lightness);
}

/**
 * The ends of the range a lit channel is held between. A lit surface is never quite black and
 * never quite white, whichever way it is turned and whatever light reaches it.
 */
enum {
    DARKEST = 4,
    BRIGHTEST = 0xFC
};

static int held(int value) {
    if (value < DARKEST) {
        return DARKEST;
    } else if (value > BRIGHTEST) {
        return BRIGHTEST;
    } else {
        return value;
    }
}

/**
 * The colour a surface facing this way takes.
 *
 * The light is the ambient plus the sun, and how much sun depends on whether the surface faces it
 * at all. Each channel is tinted by the sun's own colour before it is scaled.
 */
uint32_t sunlitColour(uint32_t unlit, const Normal *normal, float strength) {
    const Sun *light = sun();

    float towards = (light->x * normal->x + light->y * normal->y + light->z * normal->z)
        / normal->magnitude;
    float reach = towards > 0.0f ? light->intensity : light->reverseIntensity;
    int scale = (int) ((globalAmbient() + reach * towards * strength) * 256.0f);

    int red = (scale * (int) ((((unlit >> 16) & 0xFF) * light->red) >> 8)) >> 8;
    int green = (scale * (int) ((((unlit >> 8) & 0xFF) * light->green) >> 8)) >> 8;
    int blue = (scale * (int) (((unlit & 0xFF) * light->blue) >> 8)) >> 8;

    return ((uint32_t) held(red) << 16) | ((uint32_t) held(green) << 8) | (uint32_t) held(blue);
}
