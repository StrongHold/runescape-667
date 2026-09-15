/*
 * The software toolkit.
 *
 * One renderer serves the whole client. The toolkit object carries no handle of its own, which is
 * why `oa.nativeid` and `xa.nativeid` are declared final zero and never written, so the state the
 * renderer needs lives here rather than behind a handle.
 *
 * Everything the toolkit draws goes into the current surface's back buffer. A surface belongs to
 * one AWT canvas, owns its pixels, and presents them when the client asks it to.
 */

#ifndef SW3D_H
#define SW3D_H

#include <jni.h>
#include <stdint.h>

typedef struct Surface Surface;

/**
 * The back buffer being drawn into, which is the current surface's. Every drawing native writes
 * through this rather than reaching for the surface, because that is the only thing they need
 * from it.
 */
typedef struct {
    uint32_t *pixels;
    int width;
    int height;
} Raster;

extern Raster raster;

Surface *surfaceCreate(JNIEnv *env, jobject canvas, int width, int height);
void surfaceResize(Surface *surface, int width, int height);
void surfacePresent(Surface *surface, int x, int y);
void surfaceFree(Surface *surface);
uint32_t *surfacePixels(Surface *surface);
int surfaceWidth(Surface *surface);
int surfaceHeight(Surface *surface);

/**
 * The handle an object of the toolkit carries, which every class in it names `nativeid`.
 */
jlong nativeIdOf(JNIEnv *env, jobject owner);
void setNativeId(JNIEnv *env, jobject owner, jlong value);

#endif
