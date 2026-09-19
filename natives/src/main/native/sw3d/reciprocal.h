#ifndef SW3D_RECIPROCAL_H
#define SW3D_RECIPROCAL_H

#include <stdint.h>
#include <string.h>

extern const uint32_t RECIPROCAL_ANSWERS[2048];
extern const uint32_t ROOT_ANSWERS_EVEN[1024];
extern const uint32_t ROOT_ANSWERS_ODD[1024];

/*
 * The approximations the rasteriser divides by, answered from a table rather than by the
 * processor.
 *
 * What these scale is cut to a whole number straight afterwards, so the approximation decides
 * pixels. The instruction set fixes only how far the answer may be from the truth and not what
 * the answer is, so two processors may disagree inside that bound and both be right. Apple's
 * translation and real x86 hardware do disagree, on nearly half of all inputs, which would mean a
 * picture drawn on one machine could not be checked on another.
 *
 * Reading the answers out of a table instead makes the picture the same everywhere, this
 * machine's own architecture included. The table is what one processor answered, and every answer
 * below reproduces that processor exactly, for every float there is.
 */

enum {
    /** How many bits of the mantissa are below the part the reciprocal reads. */
    RECIPROCAL_DROPPED_BITS = 12,

    /** How many bits of the mantissa are below the part the inverse root reads. */
    ROOT_DROPPED_BITS = 13,

    /** Where a float keeps its exponent, and how wide it is. */
    EXPONENT_AT = 23,
    EXPONENT_BIAS = 127,
    EXPONENT_FULL = 0xFF,

    SIGN_BIT = (int) 0x80000000,
    MANTISSA_MASK = 0x7FFFFF,
    QUIET_NAN_BIT = 0x400000
};

static inline uint32_t reciprocalBitsOf(float value) {
    uint32_t held;
    memcpy(&held, &value, sizeof held);
    return held;
}

static inline float reciprocalFloatOf(uint32_t held) {
    float value;
    memcpy(&value, &held, sizeof value);
    return value;
}

/**
 * The processor's approximate reciprocal, as the table remembers it.
 *
 * Infinity gives zero, zero gives infinity, and anything too small to hold gives zero, each
 * keeping the sign it came in with. A value that is not a number stays one.
 */
static inline float approximateReciprocal(float value) {
    uint32_t held = reciprocalBitsOf(value);
    uint32_t sign = held & (uint32_t) SIGN_BIT;
    uint32_t exponent = (held >> EXPONENT_AT) & EXPONENT_FULL;

    if (exponent == EXPONENT_FULL) {
        return reciprocalFloatOf((held & MANTISSA_MASK) ? (held | QUIET_NAN_BIT) : sign);
    }

    if (exponent == 0) {
        return reciprocalFloatOf(sign | ((uint32_t) EXPONENT_FULL << EXPONENT_AT));
    }

    uint32_t whole = RECIPROCAL_ANSWERS[(held >> RECIPROCAL_DROPPED_BITS) & (2048 - 1)];
    int32_t moved = (int32_t) ((whole >> EXPONENT_AT) & EXPONENT_FULL)
        - ((int32_t) exponent - EXPONENT_BIAS);

    if (moved <= 0) {
        return reciprocalFloatOf(sign);
    }

    return reciprocalFloatOf(sign | ((uint32_t) moved << EXPONENT_AT) | (whole & MANTISSA_MASK));
}

/**
 * The processor's approximate inverse square root, as the table remembers it.
 *
 * A square root halves the exponent, so an odd exponent and an even one are answered from
 * different halves of the table. Nothing negative has a square root, so anything below zero is
 * not a number.
 */
static inline float approximateInverseRoot(float value) {
    uint32_t held = reciprocalBitsOf(value);
    uint32_t sign = held & (uint32_t) SIGN_BIT;
    uint32_t exponent = (held >> EXPONENT_AT) & EXPONENT_FULL;

    if (exponent == EXPONENT_FULL) {
        if (held & MANTISSA_MASK) {
            return reciprocalFloatOf(held | QUIET_NAN_BIT);
        }
        return reciprocalFloatOf(sign ? 0xFFC00000u : 0u);
    }

    if (exponent == 0) {
        return reciprocalFloatOf(sign | ((uint32_t) EXPONENT_FULL << EXPONENT_AT));
    }

    if (sign) {
        return reciprocalFloatOf(0xFFC00000u);
    }

    uint32_t parity = exponent & 1u;
    uint32_t index = (held >> ROOT_DROPPED_BITS) & (1024 - 1);
    uint32_t whole = parity ? ROOT_ANSWERS_ODD[index] : ROOT_ANSWERS_EVEN[index];
    int32_t moved = (int32_t) ((whole >> EXPONENT_AT) & EXPONENT_FULL)
        - (((int32_t) exponent - EXPONENT_BIAS + 1 - (int32_t) parity) >> 1);

    if (moved <= 0) {
        return 0.0f;
    }

    if (moved >= EXPONENT_FULL) {
        return reciprocalFloatOf((uint32_t) EXPONENT_FULL << EXPONENT_AT);
    }

    return reciprocalFloatOf(((uint32_t) moved << EXPONENT_AT) | (whole & MANTISSA_MASK));
}

#endif
