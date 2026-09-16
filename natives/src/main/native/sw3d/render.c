/*
 * Drawing a model.
 *
 * Every vertex is put through the model's matrix and then the camera's, which leaves it in front
 * of the eye, and divided by its distance to land on the buffer. Each face is then filled between
 * the three points its corners landed on.
 *
 * The fill is split the way the toolkit splits it: a triangle is cut into the part above its
 * middle corner and the part below, each part walks its two sides a row at a time, and each row
 * hands a run of pixels to the span fill. Light runs from corner to corner as a whole number with
 * eight places after the point, and the amount it moves by is cut to a whole number before the
 * walk begins, so the light on a face steps rather than slides. Keeping that is the difference
 * between a picture that is nearly right and one that is the same.
 */

#include <math.h>
#include <stdlib.h>
#include <string.h>

#if defined(__SSE__) || defined(_M_X64)
#include <xmmintrin.h>
#endif

#include "sw3d.h"

/** A colour has a blue, a green, a red and an alpha part, in the order they sit in a pixel. */
enum { CHANNELS = 4 };

/** The smallest number of rows or pixels a side is allowed to be divided by. */
static const float LEAST = 1.0e-6f;

/**
 * The reciprocal a side is divided by.
 *
 * This is the processor's approximation rather than a true division. The light it scales is cut
 * to a whole number straight afterwards, so the approximation decides the answer often enough to
 * matter and has to be the same approximation.
 */
static float reciprocal(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rcp_ss(_mm_set_ss(value)));
#else
    return 1.0f / value;
#endif
}

/**
 * The same reciprocal, taken four at a time.
 *
 * The toolkit divides a whole side at once and a row one value at a time, and the two ways of
 * asking the processor for an approximate reciprocal need not answer alike, so each is asked the
 * way the toolkit asks it.
 */
static float reciprocalOfFour(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rcp_ps(_mm_set1_ps(value)));
#else
    return 1.0f / value;
#endif
}

/** Brings a whole number into the range a light step is held in, holding at the ends. */
static int16_t narrow(float value) {
    int whole = (int) value;

    if (whole < -32768) {
        return (int16_t) -32768;
    } else if (whole > 32767) {
        return (int16_t) 32767;
    } else {
        return (int16_t) whole;
    }
}

/** Brings a whole number into the range a light is held in, holding at black and at white. */
static uint16_t hold(float value) {
    int whole = (int) value;

    if (whole < 0) {
        return 0;
    } else if (whole > 65535) {
        return (uint16_t) 65535;
    } else {
        return (uint16_t) whole;
    }
}

/** A vertex after it has been projected. Behind the eye it has no place on the buffer. */
typedef struct {
    float x;
    float y;
    float depth;

    /** How far from the eye the point ended up, which a texture is read back through. */
    float away;
    int visible;

    /** How far this point has faded towards the water, where one is wholly water. */
    float fade;
} Projected;

/**
 * How far a point has faded towards the water, from where it stands in the world.
 *
 * The client counts height downwards, so the height of a thing is the other way round from the
 * place it stands at. A point at the surface has none of the water in it and one as deep as the
 * water reaches has nothing else, and everything between is a straight run from one to the other.
 */
static float fadeAt(const float *place, float x, float y, float z) {
    const Underwater *water = underwater();
    if (!water->under) {
        return 0.0f;
    }

    float height = -(x * place[1] + y * place[5] + z * place[9] + place[13]);
    float fade = (height - water->surface) * water->perDepth;

    if (fade < 0.0f) {
        return 0.0f;
    }

    return fade > 1.0f ? 1.0f : fade;
}

static Projected *projected;
static int projectedRoom;

static int room(void **held, int *have, int want, size_t size) {
    if (*have >= want) {
        return 1;
    }

    void *grown = realloc(*held, (size_t) want * size);
    if (grown == NULL) {
        return 0;
    }

    *held = grown;
    *have = want;
    return 1;
}

/**
 * A corner of a triangle: where it landed, how far away it is, and the light it was given.
 *
 * The light is held as four whole numbers with eight places after the point, one for each part of
 * a pixel, which is the room the walk down a side needs to move a part of a colour by less than a
 * whole step without ever leaving whole numbers.
 */
typedef struct {
    float x;
    float y;
    float depth;
    uint16_t colour[CHANNELS];

    /**
     * Where the corner sits on its texture, divided by how far away it is, along with one over
     * that distance.
     *
     * They are kept divided because a texture has to be read in the world's own units rather than
     * the buffer's: stepping the two across a row and dividing one by the other at each pixel is
     * what keeps a texture lying flat on a face that leans away, instead of sliding as the face
     * turns.
     */
    float u;
    float v;
    float w;
} Corner;

/** One side of a triangle, either where it has reached or how far it moves in a row. */
typedef struct {
    float x;
    float depth;
    int16_t colour[CHANNELS];
    float u;
    float v;
    float w;
} Side;

static uint16_t fadedPart(uint16_t held, float fade, int part);

static Corner cornerAt(const Projected *point, uint32_t colour) {
    Corner corner;
    corner.x = point->x;
    corner.y = point->y;
    corner.depth = point->depth;

    for (int part = 0; part < CHANNELS; part++) {
        corner.colour[part] = (uint16_t) ((colour >> (part * 8) & 0xFF) << 8);
    }

    /*
     * A face is laid down solid. The colour a model is shaded with carries nothing in its top
     * byte, and the window pays no attention to that byte, but a surface the client later draws
     * as a sprite does, so what is written there has to say solid rather than say nothing.
     */
    corner.colour[3] = 0xFF00;

    /*
     * The water is put on here rather than at every pixel. A corner is faded and the fade is then
     * carried across the face the same way the light is, which is what the toolkit this replaces
     * comes to, and it costs one pass over three corners instead of one over every pixel.
     */
    for (int part = 0; part < CHANNELS - 1; part++) {
        corner.colour[part] = fadedPart(corner.colour[part], point->fade, part);
    }

    corner.u = 0.0f;
    corner.v = 0.0f;
    corner.w = 0.0f;

    return corner;
}

/**
 * Where a corner sits on its texture, kept divided by how far away the corner is.
 */
/**
 * Where a corner sits on its texture, kept divided by how far away the corner is.
 *
 * What it is divided by is the corner's own distance as the buffer keeps it, running from nothing
 * at the near plane to one at the far plane, rather than one over the distance the camera left
 * behind. The two differ, and reading a texture through the second puts the wrong texel down on
 * two pixels in five.
 */
static Corner onTexture(Corner corner, float u, float v) {
    corner.u = u * corner.depth;
    corner.v = v * corner.depth;
    corner.w = corner.depth;
    return corner;
}

/** The last texel of a texture each way, and the mask that wraps a coordinate back onto it. */
enum { TEXTURE_EDGE = 127 };

/**
 * Where the three corners of a face sit on the texture it wears.
 *
 * Every textured face wears the whole of its texture, laid down the same way round. A model whose
 * mesh carries texture spaces places its textures by those instead, and nothing here reads one
 * yet, so such a model is textured as though it carried none.
 */
static const float FACE_CORNERS[3][2] = {
    { 0.0f, (float) TEXTURE_EDGE },
    { (float) TEXTURE_EDGE, (float) TEXTURE_EDGE },
    { 0.0f, 0.0f }
};

/**
 * How far a side moves in one row.
 *
 * The rows are counted between the rows the two corners landed on, and the light's part of the
 * answer is cut to a whole number, so a side that climbs by less than one eight-hundredth of a
 * part per row does not climb at all.
 */
static Side sideBetween(const Corner *from, const Corner *to, int rows) {
    float over = reciprocalOfFour(fmaxf((float) rows, LEAST));

    Side side;
    side.x = (to->x - from->x) * over;
    side.depth = (to->depth - from->depth) * over;
    side.u = (to->u - from->u) * over;
    side.v = (to->v - from->v) * over;
    side.w = (to->w - from->w) * over;

    for (int part = 0; part < CHANNELS; part++) {
        side.colour[part] = narrow(((float) to->colour[part] - (float) from->colour[part]) * over);
    }

    return side;
}

static Side sideAt(const Corner *corner) {
    Side side;
    side.x = corner->x;
    side.depth = corner->depth;
    side.u = corner->u;
    side.v = corner->v;
    side.w = corner->w;

    for (int part = 0; part < CHANNELS; part++) {
        side.colour[part] = (int16_t) corner->colour[part];
    }

    return side;
}

/** Moves a side on by a whole number of rows at once, which is how a clipped side starts. */
static void carry(Side *side, const Side *step, int rows) {
    side->x += (float) rows * step->x;
    side->depth += (float) rows * step->depth;
    side->u += (float) rows * step->u;
    side->v += (float) rows * step->v;
    side->w += (float) rows * step->w;

    for (int part = 0; part < CHANNELS; part++) {
        side->colour[part] = (int16_t) (side->colour[part] + (int16_t) rows * step->colour[part]);
    }
}

static void advance(Side *side, const Side *step) {
    side->x += step->x;
    side->depth += step->depth;
    side->u += step->u;
    side->v += step->v;
    side->w += step->w;

    for (int part = 0; part < CHANNELS; part++) {
        side->colour[part] = (int16_t) (side->colour[part] + step->colour[part]);
    }
}

/**
 * Fills one run of pixels between two sides.
 *
 * A pixel is covered when the left and right ends are put on the nearest whole pixel and it falls
 * between them, and it is drawn when nothing already drawn there is nearer. The light moves by a
 * whole number of eight-hundredths per pixel, worked out from the ends before either was brought
 * inside the buffer, so clipping a run does not change the light along the part that is left.
 */
/**
 * Whether how far away a pixel is decides anything.
 *
 * Most of what the toolkit fills is part of the world and is settled a pixel at a time by how far
 * away it is. A thick line is not: it is two triangles laid flat on the picture, and the toolkit
 * fills those without reading or writing a distance at all.
 */
static int distanceDecides = 1;

/**
 * The texture the faces being drawn wear, and how a coordinate that runs off it is brought back.
 *
 * A texture belongs to the face rather than to the span, so it is put here once before a face is
 * filled rather than carried down through every side and every row.
 */
static const uint32_t *texels;
static int texelsRepeat;

/**
 * How much of the face being drawn shows, out of two hundred and fifty five, or nothing at all
 * when the face is drawn solid.
 *
 * The client hands over an alpha per face and counts it the other way round from how much shows,
 * so a face with no alpha is solid and one with the most is not there at all. Like the texture,
 * it belongs to the face rather than to the span, so it is put here once before the face is
 * filled.
 */
static int faceShows;

/** What a face with no alpha of its own is drawn as. */
enum { WHOLLY_SOLID = 0 };

/**
 * Where on its texture one pixel of a span reads from.
 *
 * The two coordinates arrive divided by how far away the pixel is, so each is brought back by
 * multiplying by that distance again. The distance is the processor's approximate reciprocal of
 * what the span carries, not a true division, because that is what the toolkit asks for and how
 * close the approximation comes belongs to the instruction set.
 */
static uint32_t texelAt(float u, float v, float w) {
    float away = reciprocalOfFour(w);
    int across = (int) (u * away);
    int down = (int) (v * away);

    if (texelsRepeat) {
        across &= TEXTURE_EDGE;
        down &= TEXTURE_EDGE;
    } else {
        across = across < 0 ? 0 : (across > TEXTURE_EDGE ? TEXTURE_EDGE : across);
        down = down < 0 ? 0 : (down > TEXTURE_EDGE ? TEXTURE_EDGE : down);
    }

    return texels[(down << 8) | across];
}

/**
 * Fades one part of a pixel towards the water it is seen through.
 *
 * The fade is worked out before the part is cut back down to a byte, in the same eight places
 * after the point the light is carried in, because cutting first and fading afterwards loses the
 * places that decide which way the answer rounds.
 */
static uint16_t fadedPart(uint16_t held, float fade, int part) {
    if (fade <= 0.0f) {
        return held;
    }

    const Underwater *water = underwater();
    return (uint16_t) ((float) held + (water->towards[part] - (float) held) * fade);
}

/**
 * Puts one pixel of a face over what is already there.
 *
 * A solid face replaces what it covers. One the client gave an alpha to is mixed with it: each
 * side is cut down on its own before they are added, so the answer is up to one lower than
 * cutting the sum once would give, and the part of the face being drawn keeps the places after
 * the point that the walk across the row carries it in.
 *
 * How much the face hides is one less than the whole rather than one more than how much it
 * shows, so a face drawn at its most solid still lets a little of what is behind it through.
 */
static uint32_t laidOver(uint32_t there, const uint16_t *colour) {
    uint32_t packed = (uint32_t) (colour[3] >> 8) << 24;

    if (faceShows == WHOLLY_SOLID) {
        for (int part = 0; part < CHANNELS - 1; part++) {
            packed |= (uint32_t) (colour[part] >> 8) << (part * 8);
        }

        return packed;
    }

    uint32_t shows = (uint32_t) faceShows;
    uint32_t hides = 0xFFu - shows;

    for (int part = 0; part < CHANNELS - 1; part++) {
        uint32_t mine = ((uint32_t) colour[part] * shows) >> 16;
        uint32_t held = (((there >> (part * 8)) & 0xFFu) * hides) >> 8;
        uint32_t both = mine + held;

        packed |= (both > 0xFFu ? 0xFFu : both) << (part * 8);
    }

    return packed;
}

static void fillSpan(int y, const Side *left, const Side *right) {
    int from = (int) lrintf(left->x);
    int to = (int) lrintf(right->x);
    float over = reciprocal((float) (to - from));
    int width = raster.clipRight - raster.clipLeft;

    if (to > width) {
        to = width;
    }

    int skipped = 0;
    if (from < 0) {
        skipped = -from;
        from = 0;
    }

    if (to <= from) {
        return;
    }

    float depthStep = (right->depth - left->depth) * over;
    float depth = left->depth + (float) skipped * depthStep;

    float uStep = (right->u - left->u) * over;
    float vStep = (right->v - left->v) * over;
    float wStep = (right->w - left->w) * over;

    /*
     * A texture is read four pixels at a time, and the four are worked out from where the group of
     * four starts rather than one from the last. The groups line up with the buffer rather than
     * with the run, so the first of them begins before the run does. Stepping one pixel at a time
     * instead gathers a little more error with every pixel, and a texture read at a coarse enough
     * angle turns that into the wrong texel.
     */
    int group = from & ~3;
    float uBase = left->u + (float) skipped * uStep - (float) (from - group) * uStep;
    float vBase = left->v + (float) skipped * vStep - (float) (from - group) * vStep;
    float wBase = left->w + (float) skipped * wStep - (float) (from - group) * wStep;

    uint16_t colour[CHANNELS];
    int16_t colourStep[CHANNELS];

    for (int part = 0; part < CHANNELS; part++) {
        float each = ((float) (uint16_t) right->colour[part]
                      - (float) (uint16_t) left->colour[part]) * over;
        colour[part] = hold((float) (uint16_t) left->colour[part] + (float) skipped * each);
        colourStep[part] = narrow(each);
    }

    size_t start = (size_t) (y + raster.clipTop) * (size_t) raster.width
        + (size_t) raster.clipLeft;
    uint32_t *row = raster.pixels + start;
    float *held = raster.depths + start;

    for (int x = from; x < to; x++) {
        if (!distanceDecides || depth <= held[x]) {
            /*
             * A face drawn through what is behind it does not record how far away it is. What it
             * covers stays as near as whatever was there, so a second blended face over the same
             * place is drawn through both rather than hidden by the first.
             */
            if (distanceDecides && faceShows == WHOLLY_SOLID) {
                held[x] = depth;
            }

            if (texels == NULL) {
                row[x] = laidOver(row[x], colour);
            } else {
                /*
                 * A texel is shaded by the light the span has reached rather than replacing it,
                 * and the product keeps its top half, which is what turns a byte times a
                 * sixteenth part back into a byte.
                 */
                int lane = x - group;
                uint32_t texel = texelAt(uBase + (float) lane * uStep,
                                         vBase + (float) lane * vStep,
                                         wBase + (float) lane * wStep);
                uint32_t written = 0;

                for (int part = 0; part < CHANNELS; part++) {
                    uint32_t channel = texel >> (part * 8) & 0xFF;
                    written |= (channel * colour[part] >> 16) << (part * 8);
                }

                uint16_t lifted[CHANNELS] = {
                (uint16_t) ((written & 0xFF) << 8),
                (uint16_t) (((written >> 8) & 0xFF) << 8),
                (uint16_t) (((written >> 16) & 0xFF) << 8),
                (uint16_t) (((written >> 24) & 0xFF) << 8)
            };
            row[x] = laidOver(row[x], lifted);
            }
        }

        depth += depthStep;

        if (x - group == 3) {
            group += 4;
            uBase += 4.0f * uStep;
            vBase += 4.0f * vStep;
            wBase += 4.0f * wStep;
        }
        for (int part = 0; part < CHANNELS; part++) {
            colour[part] = (uint16_t) (colour[part] + colourStep[part]);
        }
    }
}

/**
 * Fills the rows between two sides, which is half a triangle.
 *
 * The sides are left where the walk ended rather than being put back, because the second half of
 * a triangle carries on down one of them. Where it ended is not where multiplying the step by the
 * number of rows would put it, and the second half starts from where the walk ended.
 */
static void fillHalf(int row, int rows, Side *left, Side *right, const Side *leftStep,
                     const Side *rightStep) {
    for (int done = 0; done < rows; done++) {
        fillSpan(row + done, left, right);
        advance(left, leftStep);
        advance(right, rightStep);
    }
}

/**
 * Fills the triangle between three corners.
 *
 * The corners are put in order of the row they landed on, and the triangle is cut in two at the
 * middle one. The side that runs the whole height is one edge of both halves, and which of the two
 * is on the left is settled once for each half by which of them moves further to the right in a
 * row, rather than by comparing where they are on every row.
 */
static void fillTriangle(Corner a, Corner b, Corner c) {
    const Corner *top;
    const Corner *middle;
    const Corner *bottom;

    if (b.y > a.y) {
        if (c.y > b.y) {
            top = &a; middle = &b; bottom = &c;
        } else if (c.y > a.y) {
            top = &a; middle = &c; bottom = &b;
        } else {
            top = &c; middle = &a; bottom = &b;
        }
    } else if (c.y > a.y) {
        top = &b; middle = &a; bottom = &c;
    } else if (c.y > b.y) {
        top = &b; middle = &c; bottom = &a;
    } else {
        top = &c; middle = &b; bottom = &a;
    }

    int topRow = (int) top->y;
    int middleRow = (int) middle->y;
    int bottomRow = (int) bottom->y;

    Side toMiddle = sideBetween(top, middle, middleRow - topRow);
    Side toBottom = sideBetween(top, bottom, bottomRow - topRow);
    Side acrossBottom = sideBetween(middle, bottom, bottomRow - middleRow);

    int height = raster.clipBottom - raster.clipTop;
    int row = topRow < 0 ? 0 : topRow;
    int skipped = row - topRow;
    int rows = middleRow - topRow - skipped;

    if (height - row < rows) {
        rows = height - row;
    }

    Side leftStep = toMiddle.x > toBottom.x ? toBottom : toMiddle;
    Side rightStep = toMiddle.x > toBottom.x ? toMiddle : toBottom;
    Side left = sideAt(top);
    Side right = left;

    carry(&left, &leftStep, skipped);
    carry(&right, &rightStep, skipped);

    if (rows > 0) {
        fillHalf(row, rows, &left, &right, &leftStep, &rightStep);
        row += rows;
        skipped = 0;
        rows = bottomRow - middleRow;
    } else {
        skipped = -rows;
        rows = bottomRow - middleRow + rows;
    }

    if (height - row < rows) {
        rows = height - row;
    }

    if (rows > 0) {
        if (toBottom.x > acrossBottom.x) {
            rightStep = acrossBottom;
            right = sideAt(middle);
            carry(&right, &rightStep, skipped);
        } else {
            leftStep = acrossBottom;
            left = sideAt(middle);
            carry(&left, &leftStep, skipped);
        }

        fillHalf(row, rows, &left, &right, &leftStep, &rightStep);
    }
}

enum { ROWS = 4 };

/**
 * How many numbers the client reads back about where a model may be clicked: two points and a
 * radius, and a sixth saying whether the rest were written.
 */
enum { CYLINDER_PARTS = 6 };

typedef struct {
    float row[ROWS][ROWS];
} Transform;

/**
 * The matrix a model is put through once the camera has had it.
 *
 * It opens the picture out to the field the client asked for and leaves the distance from the eye
 * in the fourth place, so that dividing a point by that place is what makes a thing further away
 * smaller. The third place is left running from nothing at the near plane to one at the far
 * plane, which is the range distances are kept in.
 *
 * Folding the field into the matrix is not the same as opening the picture out afterwards. The
 * field then multiplies each term of the sum rather than the sum, and the two answers differ by
 * enough to move the edge of a face onto the next pixel.
 */
static Transform projectionMatrix(void) {
    const Projection *view = projection();
    float range = view->far - view->near;

    Transform matrix;
    memset(&matrix, 0, sizeof matrix);
    matrix.row[0][0] = view->scaleX;
    matrix.row[1][1] = view->scaleY;
    matrix.row[2][2] = view->far / range;
    matrix.row[2][3] = 1.0f;
    matrix.row[3][2] = -view->near * view->far / range;
    return matrix;
}

/**
 * The picture a model is put through when the client wants it seen from no particular place.
 *
 * Nothing in it carries a distance into the fourth place, so every point comes back with a
 * fourth place of one and nothing shrinks with distance. The picture is that much smaller than
 * the one the eye sees, which is what the client asks for when it is drawing a model into a
 * corner of the screen rather than into the world.
 */
static Transform flatProjection(int smaller) {
    const Projection *view = projection();
    float range = view->far - view->near;
    float shrink = (float) smaller;

    Transform matrix;
    memset(&matrix, 0, sizeof matrix);
    matrix.row[0][0] = view->scaleX / shrink;
    matrix.row[1][1] = view->scaleY / shrink;
    matrix.row[2][2] = 1.0f / range;
    matrix.row[3][2] = -view->near / range;
    matrix.row[3][3] = 1.0f;
    return matrix;
}

/**
 * How the picture is taken, which the client chooses each time it asks for a model to be drawn.
 * Anything below nothing means the eye's own picture.
 */
enum { THROUGH_THE_EYE = -1 };

static Transform pictureOf(int smaller) {
    if (smaller < 0) {
        return projectionMatrix();
    } else {
        return flatProjection(smaller);
    }
}

/**
 * Puts one matrix after another, adding the four terms of a row in pairs.
 *
 * Four numbers added in a different order are a different number as soon as they stop fitting
 * exactly, and this answer decides which pixel the edge of a face lands on, so the pairs are kept.
 */
static Transform after(const float *first, const Transform *second) {
    Transform result;

    for (int row = 0; row < ROWS; row++) {
        const float *terms = first + row * ROWS;

        for (int lane = 0; lane < ROWS; lane++) {
            float even = terms[0] * second->row[0][lane] + terms[2] * second->row[2][lane];
            float odd = terms[1] * second->row[1][lane] + terms[3] * second->row[3][lane];
            result.row[row][lane] = even + odd;
        }
    }

    return result;
}

/**
 * Keeps the sign a distance had before it was divided.
 *
 * Dividing by a distance behind the eye turns the sign of the answer round. The toolkit puts the
 * sign back rather than letting a point behind the eye come out in front of one in front of it.
 */
static float signedAs(float value, float before) {
    return before < 0.0f ? -fabsf(value) : value;
}

/**
 * Puts a point through the sixteen floats of a matrix, leaving all four places as they came out.
 */
static void throughRows(const float *rows, float x, float y, float z, float *into) {
    for (int lane = 0; lane < ROWS; lane++) {
        into[lane] = x * rows[lane] + y * rows[ROWS + lane] + z * rows[2 * ROWS + lane]
            + rows[3 * ROWS + lane];
    }
}

/**
 * The upright cylinder a model fits inside, as the client sees it.
 *
 * This is what makes a thing in the world clickable. The client tests the mouse against the
 * cylinder first and only asks the model itself when the cylinder says yes, so a cylinder that
 * is never worked out leaves everything in the world unclickable however well the model answers.
 *
 * It is two points and a radius: the middle of the model at its lowest and at its highest, each
 * put through the camera and divided by how far away it ended up, and how far the radius reaches
 * across the screen beside whichever of the two is nearer. An end behind the near plane is pulled
 * along the line to the plane rather than dropped, and a model with both ends behind it is left
 * alone, which is how the client is told it cannot be clicked.
 *
 * The last of the six the client reads is what says an answer was written at all.
 */
static void pickingCylinder(void *model, const float *seen, int zoom, jint *into) {
    const Projection *view = projection();

    int bounds[6];
    modelBounds(model, bounds);

    float middle[2] = {
        (float) ((bounds[0] + bounds[1]) >> 1),
        (float) ((bounds[4] + bounds[5]) >> 1)
    };
    float radius = (float) modelRadius(model);

    float low[ROWS];
    float high[ROWS];
    throughRows(seen, middle[0], (float) bounds[2], middle[1], low);
    throughRows(seen, middle[0], (float) bounds[3], middle[1], high);

    float near = view->near;
    float bottomAcross = 0.0f;
    float bottomDown = 0.0f;
    float topAcross = 0.0f;
    float topDown = 0.0f;
    int bottomBehind = 1;

    if (low[2] >= near) {
        float away = zoom < 0 ? low[2] : (float) zoom;
        bottomAcross = view->scaleX * low[0] / away - view->leftEdge;
        bottomDown = view->scaleY * low[1] / away - view->topEdge;
        bottomBehind = 0;
    }

    int settled = 0;

    if (high[2] >= near) {
        float away = zoom < 0 ? high[2] : (float) zoom;
        topAcross = view->scaleX * high[0] / away - view->leftEdge;
        topDown = view->scaleY * high[1] / away - view->topEdge;
        settled = !bottomBehind;
    }

    if (!settled && near > low[2]) {
        if (near > high[2]) {
            return;
        }

        float along = (high[2] - near) / (high[2] - low[2]);
        float away = zoom < 0 ? near : (float) zoom;
        bottomAcross = ((high[0] - low[0]) * along + high[0]) * view->scaleX / away
            - view->leftEdge;
        bottomDown = ((high[1] - low[1]) * along + high[1]) * view->scaleY / away
            - view->topEdge;
    } else if (!settled && near > high[2]) {
        float along = (low[2] - near) / (low[2] - high[2]);
        float away = zoom < 0 ? near : (float) zoom;
        topAcross = ((low[0] - high[0]) * along + low[0]) * view->scaleX / away - view->leftEdge;
        topDown = ((low[1] - high[1]) * along + low[1]) * view->scaleY / away - view->topEdge;
    }

    float across;
    if (low[2] > high[2]) {
        float away = zoom < 0 ? low[2] : (float) zoom;
        across = (radius + low[0]) * view->scaleX / away - view->leftEdge - bottomAcross;
    } else {
        float away = zoom < 0 ? high[2] : (float) zoom;
        across = (radius + high[0]) * view->scaleX / away - view->leftEdge - topAcross;
    }

    into[0] = (jint) bottomAcross;
    into[1] = (jint) bottomDown;
    into[2] = (jint) topAcross;
    into[3] = (jint) topDown;
    into[4] = (jint) across;
    into[5] = 1;
}

/**
 * Draws one model through one matrix.
 */
/**
 * Where each light sits in the model's own frame.
 *
 * A model is drawn from the vertices it keeps rather than from where they end up, so the lights
 * come to the model instead. The matrix is turned about by reading it down its columns, which is
 * the undoing of it for as long as the client only ever turns and moves a model.
 */
static void bringLightsIn(const float *rows, int lights, float nearby[][4]) {
    for (int light = 0; light < lights; light++) {
        const PointLight *lit = pointLight(light);

        float acrossFrom = lit->place[0] - rows[12];
        float downFrom = lit->place[1] - rows[13];
        float awayFrom = lit->place[2] - rows[14];

        for (int lane = 0; lane < 4; lane++) {
            nearby[light][lane] = acrossFrom * rows[lane * 4]
                + downFrom * rows[lane * 4 + 1]
                + awayFrom * rows[lane * 4 + 2];
        }
    }
}

/**
 * One corner's colour once the lights near it are added.
 *
 * A face the model said to shade flat faces the way the face does, and every other corner faces
 * the way its vertex does, which is the same choice the sun is worked out with.
 */
static uint32_t litByNearby(void *model, int face, int vertex, uint32_t colour,
        const float nearby[][4]) {
    const Normal *normals = modelFaceIsFlat(model, face)
        ? modelFaceNormals(model)
        : modelNormals(model);

    if (normals == NULL) {
        return colour;
    }

    const float *held = modelVertices(model);
    const float *place = &held[(size_t) vertex * MODEL_VERTEX_STRIDE];
    const Normal *normal = modelFaceIsFlat(model, face) ? &normals[face] : &normals[vertex];

    return pointLitColour(colour, place, normal, nearby);
}

/**
 * Whether the whole model can be thrown away without looking at a single face.
 *
 * The model is stood in for by a cylinder: the two ends of its upright reach, and one radius
 * around them. Both ends go through the matrix, and the cylinder is then measured against the
 * near and far planes and against the four edges of the picture.
 *
 * The one radius is the reach across, and it is used for the upright measurement as well as for
 * the two across it. That makes the cylinder taller than the model, which only ever keeps a
 * model that could have been thrown away.
 *
 * What the two across are divided by is the whole of the difference between the two pictures.
 * The eye's picture divides by the far end of the cylinder, so that a model standing further
 * back is measured smaller. A picture taken from no particular place divides by how much smaller
 * it was asked to be, and distance does not come into it.
 */
static int wholeModelIsOut(void *model, const float *rows, int smaller) {
    const Projection *view = projection();

    int reach[6];
    modelBounds(model, reach);
    float radius = (float) modelRadius(model);

    float low[ROWS];
    float high[ROWS];
    for (int lane = 0; lane < ROWS; lane++) {
        low[lane] = (float) reach[2] * rows[4 + lane] + rows[12 + lane];
        high[lane] = (float) reach[3] * rows[4 + lane] + rows[12 + lane];
    }

    float nearest = fminf(low[2], high[2]) - radius;
    float furthest = fmaxf(low[2], high[2]) + radius;

    if (nearest >= view->far || view->near >= furthest) {
        return 1;
    }

    float divideBy = smaller < 0 ? furthest : (float) smaller;

    float leftmost = (fminf(low[0], high[0]) - radius) * view->scaleX / divideBy;
    float rightmost = (fmaxf(low[0], high[0]) + radius) * view->scaleX / divideBy;

    if (leftmost >= view->rightEdge || view->leftEdge >= rightmost) {
        return 1;
    }

    float topmost = (fminf(low[1], high[1]) - radius) * view->scaleY / divideBy;
    float bottommost = (fmaxf(low[1], high[1]) + radius) * view->scaleY / divideBy;

    return topmost >= view->bottomEdge || view->topEdge >= bottommost;
}

static void renderModel(void *model, const void *matrix, jint *cylinder, int smaller) {
    if (model == NULL || matrix == NULL || raster.pixels == NULL || raster.depths == NULL) {
        return;
    }

    int vertices = modelVertexCount(model);
    int faces = modelFaceCount(model);
    if (vertices <= 0 || faces <= 0) {
        return;
    }

    const Projection *view = projection();
    const void *camera = cameraMatrix();

    void *combined = malloc(matrixSize());
    if (combined == NULL) {
        return;
    }

    if (camera == NULL) {
        memcpy(combined, matrix, matrixSize());
    } else {
        matrixCompose(matrix, camera, combined);
    }

    if (wholeModelIsOut(model, matrixRows(combined), smaller)) {
        free(combined);
        return;
    }

    Transform projector = pictureOf(smaller);
    Transform onto = after(matrixRows(combined), &projector);

    if (cylinder != NULL) {
        pickingCylinder(model, matrixRows(combined), -1, cylinder);
    }

    free(combined);

    /*
     * How far below the surface of the water every vertex sits, worked out from where the model
     * stands in the world rather than from where it landed on the picture. Nothing is worked out
     * at all while the eye is above water, which is almost always.
     */
    const float *place = matrixRows(matrix);

    float nearby[POINT_LIGHTS][4];
    int lights = modelNeedsNormals(model) ? pointLightCount() : 0;
    bringLightsIn(matrixRows(matrix), lights, nearby);

    if (raster.pixels == NULL || raster.depths == NULL) {
        return;
    }

    if (!room((void **) &projected, &projectedRoom, vertices, sizeof(Projected))) {
        return;
    }

    /* Where the middle of the picture sits, counted from the corner that may be drawn on. */
    float acrossFromClip = view->centreX - (float) raster.clipLeft;
    float downFromClip = view->centreY - (float) raster.clipTop;

    const float *held = modelVertices(model);

    for (int vertex = 0; vertex < vertices; vertex++) {
        float x = held[(size_t) vertex * MODEL_VERTEX_STRIDE];
        float y = held[(size_t) vertex * MODEL_VERTEX_STRIDE + 1];
        float z = held[(size_t) vertex * MODEL_VERTEX_STRIDE + 2];

        float point[ROWS];
        for (int lane = 0; lane < ROWS; lane++) {
            point[lane] = x * onto.row[0][lane] + y * onto.row[1][lane]
                + z * onto.row[2][lane] + onto.row[3][lane];
        }

        /* The fourth place is how far from the eye the point ended up. */
        float away = point[3];

        Projected *landed = &projected[vertex];
        landed->away = away;
        landed->depth = signedAs(point[2] / away, point[2]);
        landed->fade = fadeAt(place, x, y, z);

        /* A picture taken from no particular place has nothing behind it and nothing beyond. */
        landed->visible = smaller >= 0 || (away >= view->near && away <= view->far);

        if (landed->visible) {
            landed->x = point[0] / away + acrossFromClip;
            landed->y = point[1] / away + downFromClip;
        }
    }

    const short *faceA = modelFaceA(model);
    const short *faceB = modelFaceB(model);
    const short *faceC = modelFaceC(model);
    const short *faceColour = modelFaceColour(model);
    const short *faceTexture = modelFaceTexture(model);

    const uint32_t *shade = modelShade(model);

    /*
     * The faces are drawn in the order the model lists them. Nothing sorts them: what covers what
     * is settled a pixel at a time by how far away each one is, and two faces that meet exactly
     * are settled by which of them the model lists second. Sorting them first would change that
     * answer wherever they meet, which on a model whose faces line up with an axis is a great
     * many pixels.
     */
    for (int face = 0; face < faces; face++) {
        const Projected *a = &projected[faceA[face]];
        const Projected *b = &projected[faceB[face]];
        const Projected *c = &projected[faceC[face]];

        if (!a->visible || !b->visible || !c->visible) {
            continue;
        }

        /*
         * A face with any corner as deep as the water reaches is dropped rather than drawn in the
         * water's own colour. One corner is enough: the face is on its way out of sight and the
         * toolkit this replaces gives up on the whole of it.
         */
        if (a->fade >= 1.0f || b->fade >= 1.0f || c->fade >= 1.0f) {
            continue;
        }

        /*
         * A face turned away from the eye is inside the model and is not drawn. Which way round
         * that is comes from the order its corners were given in, so the test is the sign of the
         * area the three landed points enclose.
         */
        float area = (b->x - a->x) * (c->y - a->y) - (c->x - a->x) * (b->y - a->y);
        if (area >= 0.0f) {
            continue;
        }

        uint32_t unlit = 0;
        if (shade == NULL) {
            unlit = unlitColour(faceColour == NULL ? 0 : faceColour[face] & 0xFFFF,
                                modelAmbient(model));

            if (faceTexture != NULL && faceTexture[face] != -1) {
                const TextureMetrics *metrics =
                    textureMetricsFor((unsigned short) faceTexture[face]);
                if (metrics != NULL) {
                    unlit = texturedUnlitColour(unlit, modelAmbient(model), metrics->alpha,
                                                metrics->aByte57);
                }
            }
        }

        uint32_t colours[3];
        for (int corner = 0; corner < 3; corner++) {
            colours[corner] = shade == NULL ? unlit : shade[face * 3 + corner];
        }

        if (lights > 0) {
            const short *corners[3] = {faceA, faceB, faceC};

            for (int corner = 0; corner < 3; corner++) {
                colours[corner] = litByNearby(model, face, corners[corner][face],
                    colours[corner], nearby);
            }
        }

        Corner walked[3] = {
            cornerAt(a, colours[0]),
            cornerAt(b, colours[1]),
            cornerAt(c, colours[2])
        };

        const Texture *texture = faceTexture == NULL || faceTexture[face] == -1
            ? NULL
            : textureFor((unsigned short) faceTexture[face]);


        /*
         * A face the client gave no alpha to is drawn solid, and so is one whose alpha says it is
         * wholly there. The client counts an alpha the other way round from how much shows.
         */
        int alpha = modelFaceAlpha(model, face);
        faceShows = alpha == 0 ? WHOLLY_SOLID : 0xFF - alpha;

        texels = NULL;
        if (texture != NULL) {
            const TextureMetrics *metrics = textureMetrics(texture);
            float slidU = 0.0f;
            float slidV = 0.0f;
            textureOffsets(texture, &slidU, &slidV);

            texels = texturePixels(texture);
            texelsRepeat = metrics->repeatsU || metrics->repeatsV;

            const float *placed = modelFaceUV(model);

            for (int corner = 0; corner < 3; corner++) {
                float acrossTexture = placed == NULL
                    ? FACE_CORNERS[corner][0]
                    : placed[(face * 3 + corner) * 2];
                float downTexture = placed == NULL
                    ? FACE_CORNERS[corner][1]
                    : placed[(face * 3 + corner) * 2 + 1];

                walked[corner] = onTexture(walked[corner],
                    acrossTexture + slidU, downTexture + slidV);
            }
        }

        fillTriangle(walked[0], walked[1], walked[2]);
    }

    texels = NULL;
    faceShows = WHOLLY_SOLID;
}

/**
 * Draws one tile of the ground.
 *
 * A tile's corners are already where they belong in the world, so they go through the camera and
 * the projection and nothing else. Each face is filled between its three corners the same way a
 * model's face is, because that is what the toolkit fills it with.
 */
/**
 * Whether a face of the ground is turned towards the eye once it has landed on the picture.
 *
 * The ground is drawn from one side only. A face wound the other way round is the underside of
 * the world, and drawing it would let the ground show through itself wherever the eye gets below
 * it. Which way a face is wound is the sign of the area it covers once it has been laid down.
 *
 * A model is not treated this way. The client hands a model over already knowing which of its
 * faces are worth drawing, and the ground it works out here.
 */
static int facesTheEye(const Projected *a, const Projected *b, const Projected *c) {
    float acrossA = a->x - b->x;
    float downA = a->y - b->y;
    float acrossC = c->x - b->x;
    float downC = c->y - b->y;

    return acrossA * downC > downA * acrossC;
}

void renderGroundTile(const void *ground, int x, int z) {
    int corners = 0;
    const void *tile = groundTile(ground, x, z, &corners);
    const void *camera = cameraMatrix();

    if (tile == NULL || camera == NULL || raster.pixels == NULL || raster.depths == NULL) {
        return;
    }

    if (!room((void **) &projected, &projectedRoom, corners, sizeof(Projected))) {
        return;
    }

    Transform projector = projectionMatrix();
    Transform onto = after(matrixRows(camera), &projector);

    const Projection *view = projection();
    float acrossFromClip = view->centreX - (float) raster.clipLeft;
    float downFromClip = view->centreY - (float) raster.clipTop;
    int tileSize = groundTileSize(ground);

    uint32_t *shade = calloc((size_t) corners, sizeof(uint32_t));
    if (shade == NULL) {
        return;
    }

    for (int corner = 0; corner < corners; corner++) {
        int where[3];
        groundTileCorner(ground, tile, corner, tileSize, x, z, where, &shade[corner]);

        float point[ROWS];
        for (int lane = 0; lane < ROWS; lane++) {
            point[lane] = (float) where[0] * onto.row[0][lane]
                + (float) where[1] * onto.row[1][lane]
                + (float) where[2] * onto.row[2][lane] + onto.row[3][lane];
        }

        float away = point[3];
        Projected *landed = &projected[corner];
        landed->depth = signedAs(point[2] / away, point[2]);
        landed->visible = away >= view->near && away <= view->far;

        if (landed->visible) {
            landed->x = point[0] / away + acrossFromClip;
            landed->y = point[1] / away + downFromClip;
        }
    }

    for (int face = 0; face * 3 + 2 < corners; face++) {
        const Projected *a = &projected[face * 3];
        const Projected *b = &projected[face * 3 + 1];
        const Projected *c = &projected[face * 3 + 2];

        if (!a->visible || !b->visible || !c->visible || !facesTheEye(a, b, c)) {
            continue;
        }

        fillTriangle(cornerAt(a, shade[face * 3]),
                     cornerAt(b, shade[face * 3 + 1]),
                     cornerAt(c, shade[face * 3 + 2]));
    }

    free(shade);
}

/**
 * Draws one model, and answers where the client may click on it.
 *
 * The six numbers the client reads back are only written when the model has somewhere on the
 * screen to be clicked. The client clears the last of them before asking, so leaving them alone
 * is how it is told the model cannot be clicked at all.
 */
static void drawModelFor(JNIEnv *env, jlong model, jlong matrix, jintArray cylinder,
        int smaller) {
    jint answer[CYLINDER_PARTS];
    int wanted = cylinder != NULL && (*env)->GetArrayLength(env, cylinder) >= CYLINDER_PARTS;
    if (wanted) {
        (*env)->GetIntArrayRegion(env, cylinder, 0, CYLINDER_PARTS, answer);
    }

    renderModel((void *) (intptr_t) model, (const void *) (intptr_t) matrix,
        wanted ? answer : NULL, smaller);

    if (wanted) {
        (*env)->SetIntArrayRegion(env, cylinder, 0, CYLINDER_PARTS, answer);
    }
}

JNIEXPORT void JNICALL Java_a_UA(JNIEnv *env, jobject self, jlong worker, jlong model,
                                  jlong matrix, jintArray cylinder, jint flags) {
    (void) self;
    (void) worker;
    (void) flags;

    drawModelFor(env, model, matrix, cylinder, THROUGH_THE_EYE);
}

/**
 * Draws one model through a picture taken from no particular place, that much smaller than the
 * one the eye sees.
 *
 * The client asks for this when a model belongs to the screen rather than to the world, and it
 * hands over a second number that the toolkit has never looked at.
 */
JNIEXPORT void JNICALL Java_a_f(JNIEnv *env, jobject self, jlong worker, jlong model,
                                 jlong matrix, jintArray cylinder, jint smaller, jint unused) {
    (void) self;
    (void) worker;
    (void) unused;

    drawModelFor(env, model, matrix, cylinder, smaller);
}

/**
 * Draws one tile of the ground, every depth of it.
 */
JNIEXPORT void JNICALL Java_a_H(JNIEnv *env, jobject self, jlong worker, jlong ground,
                                 jint x, jint z) {
    (void) env;
    (void) self;
    (void) worker;

    renderGroundTile((const void *) (intptr_t) ground, x, z);
}

/**
 * Draws one depth of one tile of the ground. Nothing here keeps its faces apart by depth yet, so
 * this draws the whole tile.
 */
JNIEXPORT void JNICALL Java_a_Z(JNIEnv *env, jobject self, jlong worker, jlong ground,
                                 jint x, jint z, jint depth) {
    (void) env;
    (void) self;
    (void) worker;
    (void) depth;

    renderGroundTile((const void *) (intptr_t) ground, x, z);
}

/*
 * Finding out whether a point on the screen lands on a model.
 *
 * The client asks this of every thing in the world under the mouse, several times a frame, so it
 * is two tests rather than one. The first projects the eight corners of the box the model sits in
 * and asks whether the point is inside what they cover. Only if it is, and only if the client
 * asked for more than a guess, is every face projected and asked in turn.
 *
 * A face is asked the same crude question as the box: whether the point is inside the rectangle
 * the face's three corners cover. That is not the same as being inside the face, so a point in
 * the corner of a long thin triangle picks it. That is what the toolkit does.
 */

/** How far out the box starts before any corner has been looked at. */
static const float OUTSIDE_EVERYTHING = 100000.0f;

enum { BOX_CORNERS = 8 };

/**
 * The matrix a model is put through when the client wants it flat rather than in perspective.
 *
 * Distance no longer makes a thing smaller, so the field is divided by the zoom the client asks
 * for instead, and the fourth place comes out as one for every point.
 */
static Transform flatMatrix(int zoom) {
    const Projection *view = projection();
    float range = view->far - view->near;

    Transform matrix;
    memset(&matrix, 0, sizeof matrix);
    matrix.row[0][0] = view->scaleX / (float) zoom;
    matrix.row[1][1] = view->scaleY / (float) zoom;
    matrix.row[2][2] = 1.0f / range;
    matrix.row[3][2] = -view->near / range;
    matrix.row[3][3] = 1.0f;
    return matrix;
}

/**
 * Where one point of the model lands, as three places across, down and away from the eye.
 */
static void placeOnScreen(const Transform *onto, const Projection *view,
                          float x, float y, float z, float *into) {
    float point[ROWS];
    for (int lane = 0; lane < ROWS; lane++) {
        point[lane] = x * onto->row[0][lane] + y * onto->row[1][lane]
            + z * onto->row[2][lane] + onto->row[3][lane];
    }

    float away = point[3];
    into[0] = point[0] / away + view->centreX;
    into[1] = point[1] / away + view->centreY;
    into[2] = signedAs(point[2] / away, point[2]);
}

/** Whether all three of a face's corners sit past the point along one axis. */
static int allPast(float first, float second, float third, float mark) {
    return first > mark && second > mark && third > mark;
}

/** Whether all three of a face's corners sit short of the point along one axis. */
static int allShort(float first, float second, float third, float mark) {
    return mark > first && mark > second && mark > third;
}

/**
 * The matrix everything in the model is put through, which is the one the client gave, then the
 * camera, then the opening out of the picture.
 */
static int lookThrough(const void *matrix, int zoom, Transform *into) {
    const void *camera = cameraMatrix();

    void *combined = malloc(matrixSize());
    if (combined == NULL) {
        return 0;
    }

    if (camera == NULL) {
        memcpy(combined, matrix, matrixSize());
    } else {
        matrixCompose(matrix, camera, combined);
    }

    Transform projector = zoom < 0 ? projectionMatrix() : flatMatrix(zoom);
    *into = after(matrixRows(combined), &projector);
    free(combined);
    return 1;
}

/**
 * Whether the point is inside what the eight corners of the model's box cover.
 *
 * A corner behind the near plane is left out. If every one of them is, the model is behind the
 * eye and nothing is picked.
 */
static int insideTheBox(void *model, const Transform *onto, const Projection *view,
                        int x, int y) {
    int bounds[6];
    modelBounds(model, bounds);

    float least[3] = {(float) bounds[0], (float) bounds[2], (float) bounds[4]};
    float most[3] = {(float) bounds[1], (float) bounds[3], (float) bounds[5]};

    float leastAcross = OUTSIDE_EVERYTHING;
    float mostAcross = -OUTSIDE_EVERYTHING;
    float leastDown = OUTSIDE_EVERYTHING;
    float mostDown = -OUTSIDE_EVERYTHING;
    int anyInFront = 0;

    for (int corner = 0; corner < BOX_CORNERS; corner++) {
        float landed[3];
        placeOnScreen(onto, view,
            (corner & 0x1) == 0 ? least[0] : most[0],
            (corner & 0x2) == 0 ? least[1] : most[1],
            (corner & 0x4) == 0 ? least[2] : most[2],
            landed);

        if (landed[2] < 0.0f) {
            continue;
        }

        anyInFront = 1;
        leastAcross = fminf(landed[0], leastAcross);
        mostAcross = fmaxf(landed[0], mostAcross);
        leastDown = fminf(landed[1], leastDown);
        mostDown = fmaxf(landed[1], mostDown);
    }

    if (!anyInFront) {
        return 0;
    }

    float across = (float) x;
    float down = (float) y;

    return across > leastAcross && mostAcross > across
        && down > leastDown && mostDown > down;
}

/**
 * Whether any face of the model covers the point.
 */
static int onAnyFace(void *model, const Transform *onto, const Projection *view, int x, int y) {
    int vertices = modelVertexCount(model);
    int faces = modelFaceCount(model);

    float *landed = calloc((size_t) vertices * 3, sizeof(float));
    if (landed == NULL) {
        return 0;
    }

    const float *held = modelVertices(model);
    for (int vertex = 0; vertex < vertices; vertex++) {
        placeOnScreen(onto, view,
            held[(size_t) vertex * MODEL_VERTEX_STRIDE],
            held[(size_t) vertex * MODEL_VERTEX_STRIDE + 1],
            held[(size_t) vertex * MODEL_VERTEX_STRIDE + 2],
            landed + (size_t) vertex * 3);
    }

    const short *faceA = modelFaceA(model);
    const short *faceB = modelFaceB(model);
    const short *faceC = modelFaceC(model);

    float across = (float) x;
    float down = (float) y;
    int found = 0;

    for (int face = 0; face < faces && !found; face++) {
        const float *a = landed + (size_t) faceA[face] * 3;
        const float *b = landed + (size_t) faceB[face] * 3;
        const float *c = landed + (size_t) faceC[face] * 3;

        /*
         * The third corner is not asked whether it is in front of the eye. Only the first two
         * are, which is what the toolkit does.
         */
        if (a[2] < 0.0f || b[2] < 0.0f) {
            continue;
        }

        found = !allPast(a[1], b[1], c[1], down)
            && !allShort(a[1], b[1], c[1], down)
            && !allPast(a[0], b[0], c[0], across)
            && !allShort(a[0], b[0], c[0], across);
    }

    free(landed);
    return found;
}

/**
 * Whether a point on the screen lands on the model.
 */
static int pointOnModel(void *model, const void *matrix, int x, int y, int quick, int zoom) {
    if (model == NULL || matrix == NULL || modelFaceCount(model) == 0) {
        return 0;
    }

    Transform onto;
    if (!lookThrough(matrix, zoom, &onto)) {
        return 0;
    }

    const Projection *view = projection();
    if (!insideTheBox(model, &onto, view, x, y)) {
        return 0;
    }

    return quick || onAnyFace(model, &onto, view, x, y);
}

/**
 * Whether a point on the screen lands on the model, seen in perspective.
 */
JNIEXPORT jboolean JNICALL Java_a_R(JNIEnv *env, jobject self, jlong worker, jlong model,
                                     jint x, jint y, jlong matrix, jboolean quick) {
    (void) env;
    (void) self;
    (void) worker;

    return pointOnModel((void *) (intptr_t) model, (const void *) (intptr_t) matrix,
        x, y, quick == JNI_TRUE, -1) ? JNI_TRUE : JNI_FALSE;
}

/**
 * Whether a point on the screen lands on the model, seen flat at the zoom the client gives.
 */
JNIEXPORT jboolean JNICALL Java_a_n(JNIEnv *env, jobject self, jlong worker, jlong model,
                                     jint x, jint y, jlong matrix, jboolean quick, jint zoom) {
    (void) env;
    (void) self;
    (void) worker;

    return pointOnModel((void *) (intptr_t) model, (const void *) (intptr_t) matrix,
        x, y, quick == JNI_TRUE, zoom) ? JNI_TRUE : JNI_FALSE;
}

/*
 * Whether a line in the world is worth drawing.
 *
 * The client asks this of an upright line at every corner of the ground it is about to draw, and
 * from the answers works out which tiles can be left out altogether. The answer is not yes or no
 * but which edge of the picture both ends of the line fell outside, so that a tile is only left
 * out when all four of its corners fell outside the same one.
 */

enum {
    PAST_THE_LEFT = 0x1,
    PAST_THE_RIGHT = 0x2,
    ABOVE_THE_TOP = 0x4,
    BELOW_THE_BOTTOM = 0x8,
    NEARER_THAN_THE_NEAR = 0x10,
    FURTHER_THAN_THE_FAR = 0x20
};

/** How near the eye a point is allowed to come before it is held there. */
static const float NEAREST = 1.0f;

/**
 * One place of a point put through the camera, which is the only matrix this is measured in.
 */
static float throughCamera(const float *camera, int lane, float x, float y, float z) {
    return x * camera[lane] + y * camera[ROWS + lane] + z * camera[2 * ROWS + lane]
        + camera[3 * ROWS + lane];
}

/**
 * Which edges of the picture both ends of the line fell outside.
 *
 * A zoom of less than nothing asks for the line in perspective, where how far away a point is
 * decides how far from the middle it lands. Anything else divides by the zoom instead.
 */
static int lineOffScreen(int x1, int y1, int z1, int x2, int y2, int z2, int zoom) {
    const void *held = cameraMatrix();
    if (held == NULL) {
        return 0;
    }

    const float *camera = matrixRows(held);
    const Projection *view = projection();

    float first[3] = {(float) x1, (float) y1, (float) z1};
    float second[3] = {(float) x2, (float) y2, (float) z2};

    float awayFirst = fmaxf(NEAREST, throughCamera(camera, 2, first[0], first[1], first[2]));
    float awaySecond = fmaxf(NEAREST, throughCamera(camera, 2, second[0], second[1], second[2]));

    int outside = 0;
    if (view->near > awayFirst && view->near > awaySecond) {
        outside = NEARER_THAN_THE_NEAR;
    } else if (awayFirst > view->far && awaySecond > view->far) {
        outside = FURTHER_THAN_THE_FAR;
    }

    float overFirst = zoom < 0 ? awayFirst : (float) zoom;
    float overSecond = zoom < 0 ? awaySecond : (float) zoom;

    float acrossFirst = throughCamera(camera, 0, first[0], first[1], first[2])
        * view->scaleX / overFirst;
    float acrossSecond = throughCamera(camera, 0, second[0], second[1], second[2])
        * view->scaleX / overSecond;

    if (view->leftEdge > acrossFirst && view->leftEdge > acrossSecond) {
        outside |= PAST_THE_LEFT;
    } else if (acrossFirst > view->rightEdge && acrossSecond > view->rightEdge) {
        outside |= PAST_THE_RIGHT;
    }

    float downFirst = throughCamera(camera, 1, first[0], first[1], first[2])
        * view->scaleY / overFirst;
    float downSecond = throughCamera(camera, 1, second[0], second[1], second[2])
        * view->scaleY / overSecond;

    if (view->topEdge > downFirst && view->topEdge > downSecond) {
        return outside | ABOVE_THE_TOP;
    }

    if (downFirst > view->bottomEdge && downSecond > view->bottomEdge) {
        outside |= BELOW_THE_BOTTOM;
    }

    return outside;
}

JNIEXPORT jint JNICALL Java_oa_JA(JNIEnv *env, jobject self, jint x1, jint y1, jint z1,
                                   jint x2, jint y2, jint z2) {
    (void) env;
    (void) self;

    return lineOffScreen(x1, y1, z1, x2, y2, z2, -1);
}

JNIEXPORT jint JNICALL Java_oa_r(JNIEnv *env, jobject self, jint x1, jint y1, jint z1,
                                  jint x2, jint y2, jint z2, jint zoom) {
    (void) env;
    (void) self;

    return lineOffScreen(x1, y1, z1, x2, y2, z2, zoom);
}

/**
 * How far a step along a line is kept: sixteen places, and one more for the halving.
 */
enum { LINE_PLACES = 16, LINE_HALF_PLACES = 17 };

/** A corner of a shape laid flat on the picture, which nothing behind it can cover. */
static Corner flatCorner(int x, int y, uint32_t colour) {
    Projected landed;
    landed.x = (float) x;
    landed.y = (float) y;
    landed.depth = 0.0f;
    landed.visible = 1;

    return cornerAt(&landed, colour);
}

/**
 * Draws a line with a width to it, as two triangles rather than as a walk.
 *
 * The line is turned into a four cornered shape by stepping away from it to either side. The
 * step is the line's own step with the two parts swapped and one of them turned round, which is
 * the direction across the line, and which of the two is turned round depends on which way the
 * line leans. It is then taken out to half the width that was asked for.
 *
 * The two sides are not the same. One is half the width rounded down and the other is half the
 * width with one added first, so a width that does not halve evenly puts the extra pixel on the
 * same side every time.
 *
 * Nothing about this reads or writes how far away a pixel is, so a thick line covers whatever it
 * is drawn over and does not stop anything drawn afterwards.
 */
JNIEXPORT void JNICALL Java_a_na(JNIEnv *env, jobject self, jlong worker, jobject surface,
                                  jint x1, jint y1, jint x2, jint y2, jint colour, jint width,
                                  jint mode) {
    (void) env;
    (void) self;
    (void) worker;
    (void) surface;
    (void) mode;

    if (raster.pixels == NULL) {
        return;
    }

    int across = x2 - x1;
    int down = y2 - y1;

    int longest = abs(down);
    if (abs(across) >= abs(down)) {
        if (across == 0) {
            return;
        }

        longest = abs(across);
    }

    int alongX = (int) ((uint32_t) across << LINE_PLACES) / longest;
    int alongY = (int) ((uint32_t) down << LINE_PLACES) / longest;

    int outX;
    int outY;
    if (alongX >= alongY) {
        outX = alongY;
        outY = -alongX;
    } else {
        outX = -alongY;
        outY = alongX;
    }

    int wideX = (int) ((uint32_t) outX * (uint32_t) width);
    int wideY = (int) ((uint32_t) outY * (uint32_t) width);

    int oneSideX = wideX >> LINE_HALF_PLACES;
    int oneSideY = wideY >> LINE_HALF_PLACES;
    int otherSideX = (wideX + 1) >> LINE_HALF_PLACES;
    int otherSideY = (wideY + 1) >> LINE_HALF_PLACES;

    int left = x1 - raster.clipLeft;
    int top = y1 - raster.clipTop;
    int right = left + across;
    int bottom = top + down;

    uint32_t paint = (uint32_t) colour;
    Corner start = flatCorner(left + oneSideX, top + oneSideY, paint);
    Corner behind = flatCorner(left - otherSideX, top - otherSideY, paint);
    Corner ahead = flatCorner(right - otherSideX, bottom - otherSideY, paint);
    Corner end = flatCorner(right + oneSideX, bottom + oneSideY, paint);

    distanceDecides = 0;
    fillTriangle(start, behind, ahead);
    fillTriangle(start, ahead, end);
    distanceDecides = 1;
}

/**
 * Answers where every vertex the client hangs a particle off has ended up.
 *
 * Three whole numbers come back for each one, in the order the model keeps them: the three
 * corners of every emitter first and then the single vertex of every effector. Each goes through
 * the matrix as it stands and is cut to a whole number rather than rounded, so a particle sits
 * where its vertex sits and not half a unit past it.
 *
 * The client gives an array it has already made. Nothing here checks that it is long enough.
 */
JNIEXPORT void JNICALL Java_a_e(JNIEnv *env, jobject self, jlong worker, jlong model,
                                 jintArray into, jlong matrix) {
    (void) self;
    (void) worker;

    void *held = (void *) (intptr_t) model;
    const void *rows = matrix == 0 ? NULL : matrixRows((const void *) (intptr_t) matrix);
    if (held == NULL || rows == NULL || into == NULL) {
        return;
    }

    int wanted = modelParticleCount(held);
    const int *vertices = modelParticleVertices(held);
    if (wanted <= 0 || vertices == NULL) {
        return;
    }

    const float *places = modelVertices(held);
    const float *rowsOf = rows;

    for (int which = 0; which < wanted; which++) {
        const float *at = &places[(size_t) vertices[which] * MODEL_VERTEX_STRIDE];

        jint landed[3];
        for (int lane = 0; lane < 3; lane++) {
            landed[lane] = (jint) (at[0] * rowsOf[lane]
                + at[1] * rowsOf[4 + lane]
                + at[2] * rowsOf[8 + lane]
                + rowsOf[12 + lane]);
        }

        (*env)->SetIntArrayRegion(env, into, which * 3, 3, landed);
    }
}

/*
 * Particles.
 *
 * The client hands the whole cloud over at once: three whole numbers for where each one is, one
 * for its colour, one for how big it is, and the texture it wears. Each is a single point put
 * through the camera and the picture, and what is drawn at that point is a filled circle when it
 * wears no texture and a square of that texture when it does.
 */

/**
 * How many places of a fraction the client keeps a particle's position and size in. The two are
 * not the same: a position carries twelve and a size eleven.
 */
enum { PLACE_PLACES = 12, SIZE_PLACES = 11 };

/** How a particle is laid over what is already there, which is the only way the toolkit puts one. */
enum { PARTICLE_BLEND = BLEND_ALPHA };

/**
 * The processor's approximate reciprocal, which is what the toolkit divides a particle by rather
 * than taking a true reciprocal. It carries about twelve bits, so a particle lands a pixel to one
 * side of where an exact divide would put it often enough to matter.
 */
static float roughReciprocal(float value) {
#if defined(__SSE__) || defined(_M_X64)
    return _mm_cvtss_f32(_mm_rcp_ss(_mm_set_ss(value)));
#else
    return 1.0f / value;
#endif
}

/** A float cut to a whole number the way the processor does it, to the nearest and ties to even. */
static int nearestWhole(float value) {
    return (int) nearbyintf(value);
}

/**
 * Whether a particle landed somewhere worth drawing.
 *
 * The three that matter are cut to whole numbers first, and to what a short holds, so a particle
 * a long way off the side is measured at the end of the range rather than wherever the arithmetic
 * took it. The edges are the whole buffer rather than what may be drawn on, and both ends count
 * as inside.
 */
static int particleIsOn(float across, float down, float depth) {
    int x = nearestWhole(across);
    int y = nearestWhole(down);
    int z = nearestWhole(depth);

    x = x > 32767 ? 32767 : (x < -32768 ? -32768 : x);
    y = y > 32767 ? 32767 : (y < -32768 ? -32768 : y);
    z = z > 32767 ? 32767 : (z < -32768 ? -32768 : z);

    return x >= 0 && x <= raster.width
        && y >= 0 && y <= raster.height
        && z >= 0;
}

/**
 * How far away a particle is, as the toolkit this replaces hands it to the circle it draws.
 *
 * The circle takes a whole number and turns it into a distance. The particles hand it the bits of
 * the distance they worked out instead of the number, so what the circle makes of it is a distance
 * far beyond anything the scene holds, and the particle is kept out by whatever was drawn first.
 *
 * That is a mistake in the toolkit, and it is what the toolkit does, so it is what this does. A
 * particle drawn any other way would be a particle the client never sees.
 */
static float depthAsTheToolkitPassesIt(float depth) {
    int32_t bits;
    memcpy(&bits, &depth, sizeof(bits));
    return (float) bits;
}

/**
 * Draws a cloud of particles, each one a point the client has already worked out where to put.
 *
 * The toolkit the client passes alongside the worker is not looked at, because there is only ever
 * the one and the toolkit reaches it without being told.
 */
JNIEXPORT void JNICALL Java_a_O(JNIEnv *env, jobject self, jlong worker, jobject toolkit,
                                 jintArray placesArray, jintArray coloursArray,
                                 jintArray sizesArray, jshortArray texturesArray, jint count) {
    (void) self;
    (void) worker;
    (void) toolkit;

    if (placesArray == NULL || coloursArray == NULL || sizesArray == NULL
        || texturesArray == NULL || count <= 0 || raster.pixels == NULL) {
        return;
    }

    int *places = calloc((size_t) count * 3, sizeof(int));
    int *colours = calloc((size_t) count, sizeof(int));
    int *sizes = calloc((size_t) count, sizeof(int));
    short *textures = calloc((size_t) count, sizeof(short));

    if (places != NULL && colours != NULL && sizes != NULL && textures != NULL) {
        (*env)->GetIntArrayRegion(env, placesArray, 0, count * 3, (jint *) places);
        (*env)->GetIntArrayRegion(env, coloursArray, 0, count, (jint *) colours);
        (*env)->GetIntArrayRegion(env, sizesArray, 0, count, (jint *) sizes);
        (*env)->GetShortArrayRegion(env, texturesArray, 0, count, (jshort *) textures);

        const Projection *view = projection();
        const void *camera = cameraMatrix();

        Transform projector = projectionMatrix();
        Transform onto = camera == NULL ? projector : after(matrixRows(camera), &projector);

        for (int which = 0; which < count; which++) {
            float x = (float) (places[which * 3] >> PLACE_PLACES);
            float y = (float) (places[which * 3 + 1] >> PLACE_PLACES);
            float z = (float) (places[which * 3 + 2] >> PLACE_PLACES);

            float point[ROWS];
            for (int lane = 0; lane < ROWS; lane++) {
                point[lane] = x * onto.row[0][lane] + y * onto.row[1][lane]
                    + z * onto.row[2][lane] + onto.row[3][lane];
            }

            float nearness = roughReciprocal(point[3]);
            float across = point[0] * nearness + view->centreX;
            float down = point[1] * nearness + view->centreY;
            float depth = signedAs(point[2] * nearness, point[2]);

            if (!particleIsOn(across, down, depth)) {
                continue;
            }

            int wide = nearestWhole((float) (sizes[which] >> SIZE_PLACES)
                * view->scaleX * nearness);
            if (wide <= 0) {
                continue;
            }

            /* A texture is not written yet, so a particle wearing one is left out. */
            if (textures[which] != -1) {
                continue;
            }

            fillCircle((int) across, (int) down, depthAsTheToolkitPassesIt(depth), wide >> 1,
                (uint32_t) colours[which], PARTICLE_BLEND);
        }
    }

    free(places);
    free(colours);
    free(sizes);
    free(textures);
}

/**
 * What the size of a tile on the plan is held out of.
 *
 * The client asks for the plan at a size it gives in two hundred and fifty sixths of a pixel, so
 * that a map can be drawn at less than one pixel to the tile without the size being nothing.
 */
enum { PLAN_SHIFT = 8, PLAN_WHOLE = 1 << PLAN_SHIFT };

/**
 * Stands for a face whose corners each keep the colour the client gave them, which is every face
 * that wears no texture.
 */
static const uint32_t NO_PAINT = 0xffffffffu;

/**
 * What one corner of a tile looks like from straight above.
 */
static Corner planCorner(const void *tile, int corner, float across, float down, float width,
                         int size, uint32_t paint) {
    int alongX;
    int alongZ;
    uint32_t colour;
    groundTilePlanCorner(tile, corner, &alongX, &alongZ, &colour);

    if (paint != NO_PAINT) {
        colour = paint;
    }

    float x = across + (float) alongX * width / (float) size;
    float y = down - (float) alongZ * width / (float) size;

    return flatCorner((int) x - raster.clipLeft, (int) y - raster.clipTop, colour);
}

/**
 * The colour a whole face is painted in the plan, or nothing where each corner keeps its own.
 *
 * A textured face shows as the one colour that stands for its texture rather than as the texture
 * itself, because a tile on the plan is a handful of pixels across and a texture drawn that small
 * says nothing.
 */
static uint32_t planPaint(const void *tile, int face) {
    int texture = groundTileFaceTexture(tile, face);
    if (texture == -1) {
        return NO_PAINT;
    }

    const TextureMetrics *metrics = textureMetricsFor(texture);
    if (metrics == NULL || metrics->disableable) {
        return NO_PAINT;
    }

    return colourOf(metrics->averageColour);
}

/**
 * Draws one tile of the ground as it looks from straight above.
 */
static void renderTilePlan(const void *ground, const void *tile, float across, float down,
                           float width) {
    int size = groundTileSize(ground);

    for (int face = 0; face < groundTileFaces(tile); face++) {
        uint32_t paint = planPaint(tile, face);

        fillTriangle(
            planCorner(tile, face * 3, across, down, width, size, paint),
            planCorner(tile, face * 3 + 1, across, down, width, size, paint),
            planCorner(tile, face * 3 + 2, across, down, width, size, paint));
    }
}

/**
 * Draws a patch of the ground as it looks from straight above, which is the map.
 *
 * The ground runs left to right across the picture and bottom to top up it, so the first row is
 * drawn at the bottom and each one after it a tile higher. The client says which tiles are worth
 * drawing, one row of flags per row of tiles, because most of the world is behind something.
 *
 * Nothing here reads or writes how far away a pixel is. A tile drawn later covers one drawn
 * earlier, which is what putting the rows in this order is for.
 */
JNIEXPORT void JNICALL Java_a_ta(JNIEnv *env, jobject self, jlong worker, jlong ground,
                                  jint originAcross, jint originDown, jint size,
                                  jint fromX, jint fromZ, jint toX, jint toZ,
                                  jobjectArray visible) {
    (void) self;
    (void) worker;

    const void *held = (const void *) (intptr_t) ground;
    if (held == NULL || raster.pixels == NULL) {
        return;
    }

    float width = (float) (size / PLAN_WHOLE);

    /*
     * Where the bottom row of the plan sits, which is as far below the top as the rows reach.
     * It is rounded towards nothing rather than downwards, so a patch drawn back to front sits
     * where the same patch drawn the right way round does.
     */
    float bottom = (float) originDown + (float) ((toZ - fromZ) * size / PLAN_WHOLE);

    distanceDecides = 0;

    for (int x = fromX; x < toX; x++) {
        float across = (float) originAcross + width * (float) (x - fromX);

        jbooleanArray row = visible == NULL
            ? NULL
            : (jbooleanArray) (*env)->GetObjectArrayElement(env, visible, x - fromX);

        if (row != NULL) {
            jboolean *flags = (*env)->GetBooleanArrayElements(env, row, NULL);
            float down = bottom;

            for (int z = fromZ; z < toZ; z++) {
                int corners;
                const void *tile = groundTile(held, x, z, &corners);

                if (flags != NULL && flags[z - fromZ] && tile != NULL) {
                    renderTilePlan(held, tile, across, down, width);
                }

                down -= width;
            }

            if (flags != NULL) {
                (*env)->ReleaseBooleanArrayElements(env, row, flags, JNI_ABORT);
            }
            (*env)->DeleteLocalRef(env, row);
        }
    }

    distanceDecides = 1;
}
