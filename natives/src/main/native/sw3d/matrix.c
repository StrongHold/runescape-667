/*
 * Matrices.
 *
 * Four rows of four floats. The first three rows are what x, y and z are each multiplied by, and
 * the fourth is added, so projecting a point is three multiplies and three adds of whole rows:
 *
 *     result = x * row[0] + y * row[1] + z * row[2] + row[3]
 *
 * The answer is handed back as integers rounded to nearest, not truncated. The toolkit this
 * replaces converts four floats at once with an instruction that rounds, and a point landing
 * halfway between two pixels therefore goes to the nearer one rather than always downwards.
 *
 * Angles are in sixteen thousand three hundred and eighty four steps of a circle, and the sine
 * of one is taken from a table of floats built with single precision, which is what the toolkit
 * does and what its models were built against.
 */

#include <math.h>
#include <stdlib.h>
#include <string.h>

#include "sw3d.h"

enum {
    TURN = 16384,
    ROWS = 4
};

/**
 * One step of the circle in radians, held at the precision the table is built at rather than at
 * the precision the constant deserves.
 */
static const float STEP = (float) (2.0 * M_PI / TURN);

typedef struct {
    float row[ROWS][ROWS];
} Matrix;

static float sineTable[TURN];
static float cosineTable[TURN];
static int tablesBuilt;

static void buildTables(void) {
    if (tablesBuilt) {
        return;
    }

    sineTable[0] = 0.0f;
    cosineTable[0] = 1.0f;

    for (unsigned int i = 1; i < TURN; i++) {
        float angle = (float) i * STEP;
        sineTable[i] = sinf(angle);
        cosineTable[i] = cosf(angle);
    }

    tablesBuilt = 1;
}

static float sineOf(int angle) {
    buildTables();
    return sineTable[(unsigned int) angle & (TURN - 1)];
}

static float cosineOf(int angle) {
    buildTables();
    return cosineTable[(unsigned int) angle & (TURN - 1)];
}

static Matrix *matrixOf(jlong handle) {
    return (Matrix *) (intptr_t) handle;
}

static void setIdentity(Matrix *matrix) {
    memset(matrix, 0, sizeof(Matrix));
    matrix->row[0][0] = 1.0f;
    matrix->row[1][1] = 1.0f;
    matrix->row[2][2] = 1.0f;
    matrix->row[3][3] = 1.0f;
}

/**
 * Turns a transform into the one that undoes it, for the rotation part only.
 */
static void transpose(Matrix *matrix) {
    for (int row = 0; row < 3; row++) {
        for (int column = row + 1; column < 3; column++) {
            float held = matrix->row[row][column];
            matrix->row[row][column] = matrix->row[column][row];
            matrix->row[column][row] = held;
        }
    }
}

/**
 * Puts one transform after another, which is the only place in here that combines two matrices.
 */
static void compose(Matrix *matrix, const Matrix *after) {
    Matrix result;

    for (int row = 0; row < ROWS; row++) {
        for (int column = 0; column < ROWS; column++) {
            float total = 0.0f;
            for (int term = 0; term < ROWS; term++) {
                total += matrix->row[row][term] * after->row[term][column];
            }
            result.row[row][column] = total;
        }
    }

    *matrix = result;
}

static void rotationX(Matrix *matrix, int angle) {
    float sine = sineOf(angle);
    float cosine = cosineOf(angle);

    setIdentity(matrix);
    matrix->row[1][1] = cosine;
    matrix->row[1][2] = sine;
    matrix->row[2][1] = -sine;
    matrix->row[2][2] = cosine;
}

static void rotationY(Matrix *matrix, int angle) {
    float sine = sineOf(angle);
    float cosine = cosineOf(angle);

    setIdentity(matrix);
    matrix->row[0][0] = cosine;
    matrix->row[0][2] = -sine;
    matrix->row[2][0] = sine;
    matrix->row[2][2] = cosine;
}

static void rotationZ(Matrix *matrix, int angle) {
    float sine = sineOf(angle);
    float cosine = cosineOf(angle);

    setIdentity(matrix);
    matrix->row[0][0] = cosine;
    matrix->row[0][1] = sine;
    matrix->row[1][0] = -sine;
    matrix->row[1][1] = cosine;
}

static void writePoint(JNIEnv *env, jintArray destination, const float *point) {
    if (destination == NULL) {
        return;
    }

    jint written[3] = {
        (jint) lrintf(point[0]),
        (jint) lrintf(point[1]),
        (jint) lrintf(point[2])
    };

    (*env)->SetIntArrayRegion(env, destination, 0, 3, written);
}

/**
 * Takes the move off a point and then turns it back, which is the matrix transposed.
 */
static void relative(const Matrix *matrix, float *point, float *into) {
    for (int lane = 0; lane < ROWS; lane++) {
        point[lane] -= matrix->row[3][lane];
    }

    for (int lane = 0; lane < ROWS; lane++) {
        into[lane] = 0.0f;
        for (int term = 0; term < ROWS; term++) {
            into[lane] += point[term] * matrix->row[lane][term];
        }
    }
}

static void transform(const Matrix *matrix, float x, float y, float z, float *into) {
    for (int lane = 0; lane < ROWS; lane++) {
        into[lane] = x * matrix->row[0][lane]
            + y * matrix->row[1][lane]
            + z * matrix->row[2][lane]
            + matrix->row[3][lane];
    }
}

JNIEXPORT void JNICALL Java_ja_la(JNIEnv *env, jobject self) {
    Matrix *matrix = calloc(1, sizeof(Matrix));
    if (matrix != NULL) {
        setIdentity(matrix);
    }

    setNativeId(env, self, (jlong) (intptr_t) matrix);
}

JNIEXPORT void JNICALL Java_ja_AA(JNIEnv *env, jobject self, jlong handle, jboolean immediate) {
    (void) env;
    (void) self;
    (void) immediate;

    free(matrixOf(handle));
}

JNIEXPORT void JNICALL Java_ja_u(JNIEnv *env, jobject self, jlong handle) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix != NULL) {
        setIdentity(matrix);
    }
}

/**
 * Replaces the matrix with a move and nothing else.
 */
JNIEXPORT void JNICALL Java_ja_FA(JNIEnv *env, jobject self, jlong handle, jint x, jint y, jint z) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix == NULL) {
        return;
    }

    setIdentity(matrix);
    matrix->row[3][0] = (float) x;
    matrix->row[3][1] = (float) y;
    matrix->row[3][2] = (float) z;
    matrix->row[3][3] = 1.0f;
}

/**
 * Moves whatever the matrix already held.
 */
JNIEXPORT void JNICALL Java_ja_a(JNIEnv *env, jobject self, jlong handle, jint x, jint y, jint z) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix == NULL) {
        return;
    }

    matrix->row[3][0] += (float) x;
    matrix->row[3][1] += (float) y;
    matrix->row[3][2] += (float) z;
}

/**
 * Replaces the matrix with a single turn. The client uses these to start a transform.
 */
JNIEXPORT void JNICALL Java_ja_VA(JNIEnv *env, jobject self, jlong handle, jint angle) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix != NULL) {
        rotationX(matrix, angle);
    }
}

JNIEXPORT void JNICALL Java_ja_NA(JNIEnv *env, jobject self, jlong handle, jint angle) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix != NULL) {
        rotationZ(matrix, angle);
    }
}

/**
 * Turns whatever the matrix already held, so that turns can be chained. The turn goes after what
 * is already there rather than before it.
 */
JNIEXPORT void JNICALL Java_ja_J(JNIEnv *env, jobject self, jlong handle, jint angle) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix != NULL) {
        Matrix rotation;
        rotationX(&rotation, angle);
        compose(matrix, &rotation);
    }
}

JNIEXPORT void JNICALL Java_ja_m(JNIEnv *env, jobject self, jlong handle, jint angle) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix != NULL) {
        Matrix rotation;
        rotationY(&rotation, angle);
        compose(matrix, &rotation);
    }
}

JNIEXPORT void JNICALL Java_ja_za(JNIEnv *env, jobject self, jlong handle, jint angle) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix != NULL) {
        Matrix rotation;
        rotationZ(&rotation, angle);
        compose(matrix, &rotation);
    }
}

/**
 * Turns about the upright axis, which is what the client means when it asks a matrix to rotate
 * without saying about what. This one replaces the matrix, where turning about a named axis
 * chains onto it.
 */
JNIEXPORT void JNICALL Java_ja_t(JNIEnv *env, jobject self, jlong handle, jint angle) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix != NULL) {
        rotationY(matrix, angle);
    }
}

/**
 * Takes the other matrix over wholesale.
 *
 * Nothing is combined here despite the name: the matrix is replaced by the one it was given. The
 * client copies a matrix by making an empty one and applying the original to it, which only works
 * because of that.
 */
/**
 * Builds a camera: the world moved so the camera sits at the origin, then turned to face where
 * the camera faces.
 */
JNIEXPORT void JNICALL Java_ja_P(JNIEnv *env, jobject self, jlong handle, jint x, jint y, jint z,
                                  jint aboutX, jint aboutY, jint aboutZ) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix == NULL) {
        return;
    }

    setIdentity(matrix);
    matrix->row[3][0] = (float) -x;
    matrix->row[3][1] = (float) -y;
    matrix->row[3][2] = (float) -z;

    /*
     * A camera undoes what it sees rather than doing it, so each turn goes the other way than the
     * same turn would on a model, and they are applied upright first and leaning last.
     */
    Matrix rotation;

    rotationZ(&rotation, aboutZ);
    transpose(&rotation);
    compose(matrix, &rotation);

    rotationY(&rotation, aboutY);
    transpose(&rotation);
    compose(matrix, &rotation);

    rotationX(&rotation, aboutX);
    transpose(&rotation);
    compose(matrix, &rotation);
}

JNIEXPORT void JNICALL Java_ja_l(JNIEnv *env, jobject self, jlong handle, jlong other) {
    (void) env;
    (void) self;

    Matrix *matrix = matrixOf(handle);
    Matrix *applied = matrixOf(other);

    if (matrix != NULL && applied != NULL) {
        *matrix = *applied;
    }
}

JNIEXPORT void JNICALL Java_ja_b(JNIEnv *env, jobject self, jlong handle, jint x, jint y, jint z,
                                  jintArray destination) {
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix == NULL) {
        return;
    }

    float point[ROWS];
    transform(matrix, (float) x, (float) y, (float) z, point);
    writePoint(env, destination, point);
}

/**
 * Undoes the matrix on the point the array already holds, in place.
 *
 * The fourth number in the array takes part, unlike the three argument form which supplies its
 * own.
 */
JNIEXPORT void JNICALL Java_ja_w(JNIEnv *env, jobject self, jlong handle, jintArray destination) {
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix == NULL || destination == NULL) {
        return;
    }

    jint held[ROWS];
    (*env)->GetIntArrayRegion(env, destination, 0, ROWS, held);

    float point[ROWS];
    float moved[ROWS] = {
        (float) held[0], (float) held[1], (float) held[2], (float) held[3]
    };

    relative(matrix, moved, point);
    writePoint(env, destination, point);
}

/**
 * Undoes the matrix. The move is taken off first and then the turn, which is the matrix
 * transposed, so the fourth row and column take part even though only three numbers come back.
 */
JNIEXPORT void JNICALL Java_ja_va(JNIEnv *env, jobject self, jlong handle, jint x, jint y, jint z,
                                   jintArray destination) {
    (void) self;

    Matrix *matrix = matrixOf(handle);
    if (matrix == NULL) {
        return;
    }

    float moved[ROWS] = {(float) x, (float) y, (float) z, 1.0f};
    float point[ROWS];
    relative(matrix, moved, point);
    writePoint(env, destination, point);
}

/**
 * Turns whatever the array already holds, without moving it.
 *
 * The point handed in is not used. The toolkit reads the three numbers out of the array it was
 * given, transforms those, and writes them back, so a caller that passes a point and an empty
 * array gets nothing it asked for. This is how it behaves and the client was written against it.
 */
JNIEXPORT void JNICALL Java_ja_XA(JNIEnv *env, jobject self, jlong handle, jint x, jint y, jint z,
                                   jintArray destination) {
    (void) self;
    (void) x;
    (void) y;
    (void) z;

    Matrix *matrix = matrixOf(handle);
    if (matrix == NULL || destination == NULL) {
        return;
    }

    jint held[3];
    (*env)->GetIntArrayRegion(env, destination, 0, 3, held);

    float point[ROWS];
    for (int lane = 0; lane < ROWS; lane++) {
        point[lane] = (float) held[0] * matrix->row[0][lane]
            + (float) held[1] * matrix->row[1][lane]
            + (float) held[2] * matrix->row[2][lane];
    }

    writePoint(env, destination, point);
}
