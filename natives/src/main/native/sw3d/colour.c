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

#if defined(__SSE__) || defined(_M_X64)
#include <xmmintrin.h>
#endif

#include "sw3d.h"

/** How many steps the client's packed colour holds for each of its three parts. */
enum {
    HUES = 64,
    SATURATIONS = 8,
    LIGHTNESSES = 128
};

/** How many colours the client can pack, which is every hue against every other part. */
enum {
    COLOURS = HUES * SATURATIONS * LIGHTNESSES
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
        /*
         * A hue and a saturation are read at the middle of the step they name rather than at its
         * near edge, so the steps are spread evenly round the wheel instead of all leaning one
         * way. Half a step is what is added.
         */
        float hue = (float) ((i >> 10) & (HUES - 1)) / (float) HUES + 0.5f / (float) HUES;
        float saturation = (float) ((i >> 7) & (SATURATIONS - 1)) / (float) SATURATIONS
            + 0.5f / (float) SATURATIONS;
        float lightness = (float) (i & (LIGHTNESSES - 1)) / (float) LIGHTNESSES;

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
/**
 * What a texture does to the colour a face is lit from.
 *
 * A textured face is not lit from its own colour alone. The texture says how far that colour is
 * carried towards a grey made from the model's own ambient, and then how much to brighten what is
 * left. Both are the texture's, so two textures that answer differently light the same face
 * differently, which is why swapping one for the other throws the face's light away.
 */
uint32_t texturedUnlitColour(uint32_t unlit, int ambient, int towardsGrey, int brighten) {
    uint32_t colour = unlit;

    if (towardsGrey != 0) {
        uint32_t redBlue = 0xFF00FF;
        uint32_t green = 0xFF00;

        /*
         * The grey is asked about as though it could not be less than nothing, so a light below
         * nothing leaves the colour carried towards white rather than towards black.
         */
        if ((uint32_t) ambient <= 0x7Fu) {
            uint32_t grey = (uint32_t) ambient * 0x20202;
            redBlue = grey & 0xFF00FF;
            green = grey & 0xFF00;
        }

        uint32_t rest = 0x100 - (uint32_t) towardsGrey;
        uint32_t mixedGreen = (green * (uint32_t) towardsGrey + (colour & 0xFF00) * rest)
            & 0xFF0000;
        uint32_t mixedRedBlue = (redBlue * (uint32_t) towardsGrey + (colour & 0xFF00FF) * rest)
            & 0xFF00FF00;
        colour = (mixedGreen + mixedRedBlue) >> 8;
    }

    if (brighten != 0) {
        uint32_t scale = 0x100 + (uint32_t) brighten;
        uint32_t red = ((colour & 0xFF0000) >> 16) * scale;
        uint32_t green = ((colour >> 8) & 0xFF) * scale;
        uint32_t blue = (colour & 0xFF) * scale;

        if (red > 0xFFFF) {
            red = 0xFFFF;
        }
        if (green > 0xFFFF) {
            green = 0xFFFF;
        }
        if (blue > 0xFFFF) {
            blue = 0xFFFF;
        }

        colour = (red << 8 & 0xFF0000) + (green & 0xFF00) + (blue >> 8);
    }

    return colour;
}

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

/**
 * The processor's approximate reciprocal square root.
 *
 * A light falls off by the cube of the distance, and the toolkit reaches that by asking for both
 * approximations and multiplying them rather than by taking a root. Both approximations carry
 * about twelve bits, so the answer differs from a true one often enough to decide a colour.
 */
static float reciprocalRoot(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rsqrt_ss(_mm_set_ss(value)));
#else
    return 1.0f / sqrtf(value);
#endif
}

static float approximateReciprocal(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rcp_ss(_mm_set_ss(value)));
#else
    return 1.0f / value;
#endif
}

/** The four bytes of a colour, in the order they sit in a pixel. */
static void spread(uint32_t colour, float *into) {
    for (int channel = 0; channel < 4; channel++) {
        into[channel] = (float) ((colour >> (channel * 8)) & 0xFF);
    }
}

/**
 * A light brought back into a byte.
 *
 * The toolkit gets there in three steps and each one is visible in the answer: the light is
 * rounded to a whole number, held to a signed short, and then held to an unsigned byte. A light
 * too large for a whole number rounds to the smallest one there is, which the two holds then
 * turn into black rather than white.
 */
static uint32_t squeeze(float value) {
    int whole;
    if (value >= -2147483648.0f && value < 2147483648.0f) {
        whole = (int) nearbyintf(value);
    } else {
        whole = -2147483647 - 1;
    }

    if (whole < -32768) {
        whole = -32768;
    } else if (whole > 32767) {
        whole = 32767;
    }

    if (whole < 0) {
        whole = 0;
    } else if (whole > 255) {
        whole = 255;
    }

    return (uint32_t) whole;
}

uint32_t pointLitColour(uint32_t colour, const float *place, const Normal *normal,
        const float places[][4]) {
    int lights = pointLightCount();
    if (lights == 0) {
        return colour;
    }

    float lit[4];
    spread(colour, lit);

    /*
     * The colour of a light is a fraction of the colour already there rather than an addition to
     * it, so the two are multiplied and brought back down by the largest a pair of bytes can be.
     */
    float share[4];
    for (int channel = 0; channel < 4; channel++) {
        share[channel] = lit[channel] * (1.0f / 65535.0f);
    }

    for (int light = 0; light < lights; light++) {
        /*
         * How much of the light reaches the surface depends only on which way the surface faces,
         * so the direction is taken as a unit one. A vertex facing nowhere has no length to
         * divide by, and the division leaves something that is not a number, which the hold at
         * nothing below turns into no light at all.
         */
        float towardsX = places[light][0] - place[0];
        float towardsY = places[light][1] - place[1];
        float towardsZ = places[light][2] - place[2];

        float away = towardsX * towardsX + towardsY * towardsY + towardsZ * towardsZ;
        float facing = (towardsX * normal->x + towardsY * normal->y + towardsZ * normal->z)
            / normal->magnitude;

        facing = facing > 0.0f ? facing : 0.0f;

        float falls = approximateReciprocal(away) * reciprocalRoot(away);
        float reach = facing * pointLight(light)->reach * falls;

        float tint[4];
        spread(pointLight(light)->colour, tint);

        for (int channel = 0; channel < 4; channel++) {
            lit[channel] += tint[channel] * share[channel] * reach;
        }
    }

    return (squeeze(lit[3]) << 24) | (squeeze(lit[2]) << 16)
        | (squeeze(lit[1]) << 8) | squeeze(lit[0]);
}
