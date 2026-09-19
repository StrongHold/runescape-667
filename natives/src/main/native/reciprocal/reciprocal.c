/*
 * Writes down what this processor's approximate reciprocal answers.
 *
 * The rasteriser divides by rcpss and rcpps rather than by a true division, and what it scales is
 * cut to a whole number straight afterwards, so the approximation decides pixels. The instruction
 * set says only how far the answer may be from the truth, not what the answer is, and two
 * processors are free to disagree inside that. A processor that disagrees with the one a picture
 * was drawn on cannot redraw that picture, so what each one answers has to be written down before
 * either is trusted to check the other.
 */

#include <math.h>
#include <stdint.h>
#include <stdio.h>
#include <xmmintrin.h>

/** The range of sizes a side or a depth is ever divided by, as powers of two. */
enum { SMALLEST_POWER = -30, LARGEST_POWER = 30 };

/** How many steps each power is walked in, which is the top of the mantissa. */
enum { STEPS_PER_POWER = 256 };

static uint32_t bits(float value) {
    uint32_t held;
    __builtin_memcpy(&held, &value, sizeof held);
    return held;
}

static uint32_t reciprocalOfOne(float value) {
    return bits(_mm_cvtss_f32(_mm_rcp_ss(_mm_set_ss(value))));
}

static uint32_t reciprocalOfFour(float value) {
    return bits(_mm_cvtss_f32(_mm_rcp_ps(_mm_set1_ps(value))));
}

static uint32_t inverseRootOfOne(float value) {
    return bits(_mm_cvtss_f32(_mm_rsqrt_ss(_mm_set_ss(value))));
}

static void say(float value) {
    printf("%08x %08x %08x %08x\n",
           bits(value), reciprocalOfOne(value), reciprocalOfFour(value), inverseRootOfOne(value));
}

int main(void) {
    for (int power = SMALLEST_POWER; power <= LARGEST_POWER; power++) {
        for (int step = 0; step < STEPS_PER_POWER; step++) {
            float value = ldexpf(1.0f + (float) step / (float) STEPS_PER_POWER, power);
            say(value);
            say(-value);
        }
    }

    return 0;
}
