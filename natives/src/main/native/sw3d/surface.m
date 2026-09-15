/*
 * The back buffer for one AWT canvas, and how it reaches the screen.
 *
 * The toolkit rasterises into memory this owns and then asks for it to be shown. macOS shows it
 * through a layer hung off the canvas, which is what AWT offers and what the window server
 * composites.
 *
 * The version asked of JAWT is the modern one. The toolkit this replaces asked for 1.3, which
 * macOS stopped serving, and that single fact is why it could not draw here at all.
 */

#import <Cocoa/Cocoa.h>
#import <ImageIO/ImageIO.h>
#import <QuartzCore/QuartzCore.h>

#include <dlfcn.h>
#include <jawt.h>
#include <jawt_md.h>
#include <limits.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

#include "sw3d.h"

typedef jboolean (JNICALL *JawtGetAwt)(JNIEnv *, JAWT *);

/**
 * The JDK's own JAWT, found through the JDK this client is running on rather than linked against
 * one, so the library does not carry a path to whichever JDK happened to build it.
 */
static JawtGetAwt jdkJawtGetAwt(JNIEnv *env) {
    static JawtGetAwt cached;
    static dispatch_once_t once;

    dispatch_once(&once, ^{
        jclass system = (*env)->FindClass(env, "java/lang/System");
        jmethodID getProperty = (*env)->GetStaticMethodID(env, system, "getProperty",
                                                          "(Ljava/lang/String;)Ljava/lang/String;");
        jstring name = (*env)->NewStringUTF(env, "java.home");
        jstring home = (*env)->CallStaticObjectMethod(env, system, getProperty, name);
        const char *chars = (*env)->GetStringUTFChars(env, home, NULL);

        char path[PATH_MAX];
        snprintf(path, sizeof(path), "%s/lib/libjawt.dylib", chars);
        (*env)->ReleaseStringUTFChars(env, home, chars);

        void *handle = dlopen(path, RTLD_NOW);
        cached = handle == NULL ? NULL : (JawtGetAwt) dlsym(handle, "JAWT_GetAWT");
    });

    return cached;
}

static const int BYTES_PER_PIXEL = 4;

__attribute__((format(printf, 1, 2)))
static void trace(const char *format, ...) {
    if (getenv("SW3D_VERBOSE") == NULL) {
        return;
    }

    va_list arguments;
    va_start(arguments, format);
    fprintf(stderr, "[sw3d] ");
    vfprintf(stderr, format, arguments);
    fprintf(stderr, "\n");
    va_end(arguments);
}

struct Surface {
    uint32_t *pixels;
    int width;
    int height;
    CGContextRef bitmap;
    void *layer;
};

/**
 * The layer a surface presents through, held outside ARC because the surface is malloc'd and ARC
 * cannot manage an object pointer that lives there.
 */
static CALayer *layerOf(Surface *surface) {
    return (__bridge CALayer *) surface->layer;
}

static void makeBitmap(Surface *surface) {
    size_t stride = (size_t) surface->width * BYTES_PER_PIXEL;
    surface->pixels = calloc(1, stride * (size_t) surface->height);

    CGColorSpaceRef space = CGColorSpaceCreateDeviceRGB();
    surface->bitmap = CGBitmapContextCreate(surface->pixels, (size_t) surface->width,
                                            (size_t) surface->height, 8, stride, space,
                                            kCGImageAlphaNoneSkipFirst | kCGBitmapByteOrder32Host);
    CGColorSpaceRelease(space);
}

static void freeBitmap(Surface *surface) {
    CGContextRelease(surface->bitmap);
    surface->bitmap = NULL;
    free(surface->pixels);
    surface->pixels = NULL;
}

/**
 * Hangs a layer off the canvas and answers it.
 *
 * The layer is handed over on the main thread because AppKit owns the view hierarchy, and the
 * handover is waited on because the layer has to exist before the first frame is presented.
 */
static CALayer *attachLayer(JNIEnv *env, jobject canvas, int width, int height) {
    JawtGetAwt getAwt = jdkJawtGetAwt(env);
    if (getAwt == NULL) {
        trace("no JAWT in this JDK");
        return nil;
    }

    /* Newest first. Which of these a JDK serves has changed between releases, so ask for each. */
    static const jint versions[] = {
        JAWT_VERSION_9 | JAWT_MACOSX_USE_CALAYER,
        JAWT_VERSION_1_7 | JAWT_MACOSX_USE_CALAYER,
        JAWT_VERSION_1_4 | JAWT_MACOSX_USE_CALAYER,
    };

    JAWT awt;
    jboolean acquired = JNI_FALSE;
    for (size_t i = 0; i < sizeof(versions) / sizeof(jint) && !acquired; i++) {
        awt.version = versions[i];
        acquired = getAwt(env, &awt);
        trace("JAWT 0x%08x %s", (unsigned) versions[i], acquired ? "accepted" : "refused");
    }

    if (!acquired) {
        return nil;
    }

    JAWT_DrawingSurface *drawing = awt.GetDrawingSurface(env, canvas);
    if (drawing == NULL) {
        trace("JAWT gave no surface");
        return nil;
    }

    CALayer *attached = nil;
    jint lock = drawing->Lock(drawing);
    if ((lock & JAWT_LOCK_ERROR) == 0) {
        JAWT_DrawingSurfaceInfo *info = drawing->GetDrawingSurfaceInfo(drawing);
        trace("surface info %s", info == NULL ? "refused" : "given");
        if (info != NULL) {
            CALayer *layer = [CALayer layer];
            layer.contentsGravity = kCAGravityResize;
            layer.magnificationFilter = kCAFilterNearest;
            layer.anchorPoint = CGPointZero;
            layer.frame = CGRectMake(0, 0, width, height);
            layer.autoresizingMask = kCALayerWidthSizable | kCALayerHeightSizable;

            id<JAWT_SurfaceLayers> layers = (__bridge id<JAWT_SurfaceLayers>) info->platformInfo;
            dispatch_sync(dispatch_get_main_queue(), ^{
                layers.layer = layer;
            });

            attached = layer;
            drawing->FreeDrawingSurfaceInfo(info);
        }
        drawing->Unlock(drawing);
    } else {
        trace("surface lock refused, 0x%x", (unsigned) lock);
    }

    awt.FreeDrawingSurface(drawing);
    return attached;
}

Surface *surfaceCreate(JNIEnv *env, jobject canvas, int width, int height) {
    Surface *surface = calloc(1, sizeof(Surface));
    if (surface == NULL) {
        return NULL;
    }

    surface->width = width;
    surface->height = height;
    makeBitmap(surface);

    if (surface->pixels == NULL || surface->bitmap == NULL) {
        freeBitmap(surface);
        free(surface);
        return NULL;
    }

    @autoreleasepool {
        CALayer *layer = attachLayer(env, canvas, width, height);
        surface->layer = (__bridge_retained void *) layer;

        trace("surface %dx%d layer=%p", width, height, (__bridge void *) layer);
    }

    return surface;
}

void surfaceResize(Surface *surface, int width, int height) {
    if (surface->width == width && surface->height == height) {
        return;
    }

    freeBitmap(surface);
    surface->width = width;
    surface->height = height;
    makeBitmap(surface);
}

/**
 * Writes each presented frame to the directory named by SW3D_DUMP.
 *
 * The toolkit this replaces is driven through the same harness and dumps the same way, so the two
 * can be compared frame by frame. That comparison is the only thing that can tell us whether this
 * draws what the client expects.
 */
static void dumpFrame(CGImageRef frame) {
    const char *directory = getenv("SW3D_DUMP");
    if (directory == NULL) {
        return;
    }

    static int counter;
    char path[PATH_MAX];
    snprintf(path, sizeof(path), "%s/frame-%04d.png", directory, counter++);

    NSURL *url = [NSURL fileURLWithPath:@(path)];
    CGImageDestinationRef destination =
        CGImageDestinationCreateWithURL((__bridge CFURLRef) url, kUTTypePNG, 1, NULL);

    if (destination != NULL) {
        CGImageDestinationAddImage(destination, frame, NULL);
        CGImageDestinationFinalize(destination);
        CFRelease(destination);
    }
}

/**
 * Shows what has been drawn.
 *
 * The image is a copy, so the next frame may be rasterised while this one is still on its way to
 * the screen. The handover is not waited on: the drawing thread holds the canvas tree lock here,
 * and the main thread wants that lock, so waiting deadlocks both.
 */
void surfacePresent(Surface *surface, int x, int y) {
    @autoreleasepool {
        CGImageRef frame = CGBitmapContextCreateImage(surface->bitmap);
        if (frame == NULL) {
            return;
        }

        dumpFrame(frame);

        CALayer *layer = layerOf(surface);
        if (layer == nil) {
            CGImageRelease(frame);
            return;
        }

        id contents = (__bridge_transfer id) frame;
        CGRect bounds = CGRectMake(x, y, surface->width, surface->height);

        dispatch_async(dispatch_get_main_queue(), ^{
            [CATransaction begin];
            [CATransaction setDisableActions:YES];
            if (!CGRectEqualToRect(layer.frame, bounds)) {
                layer.frame = bounds;
            }
            layer.contents = contents;
            [CATransaction commit];
        });
    }
}

void surfaceFree(Surface *surface) {
    if (surface == NULL) {
        return;
    }

    CALayer *layer = (__bridge_transfer CALayer *) surface->layer;
    surface->layer = NULL;
    (void) layer;

    freeBitmap(surface);
    free(surface);
}

uint32_t *surfacePixels(Surface *surface) {
    return surface->pixels;
}

int surfaceWidth(Surface *surface) {
    return surface->width;
}

int surfaceHeight(Surface *surface) {
    return surface->height;
}

JNIEXPORT void JNICALL Java_p_sa(JNIEnv *env, jobject self, jobject toolkit, jobject canvas,
                                  jint width, jint height) {
    (void) toolkit;
    setNativeId(env, self, (jlong) (intptr_t) surfaceCreate(env, canvas, width, height));
}

JNIEXPORT void JNICALL Java_p_oa(JNIEnv *env, jobject self, jobject canvas, jint width, jint height) {
    (void) canvas;

    Surface *surface = (Surface *) (intptr_t) nativeIdOf(env, self);
    if (surface != NULL) {
        surfaceResize(surface, width, height);

        if (raster.pixels != NULL) {
            raster.pixels = surface->pixels;
            raster.width = surface->width;
            raster.height = surface->height;
        }
    }
}

/**
 * The canvas size is handed in and ignored. The layer is told to fill the canvas when it is
 * attached, so the window server scales the frame and the toolkit never has to.
 */
JNIEXPORT void JNICALL Java_p_H(JNIEnv *env, jobject self, jint x, jint y,
                                 jint canvasWidth, jint canvasHeight) {
    (void) canvasWidth;
    (void) canvasHeight;

    Surface *surface = (Surface *) (intptr_t) nativeIdOf(env, self);
    if (surface != NULL) {
        surfacePresent(surface, x, y);
    }
}

JNIEXPORT void JNICALL Java_p_w(JNIEnv *env, jobject self, jboolean immediate) {
    (void) immediate;

    Surface *surface = (Surface *) (intptr_t) nativeIdOf(env, self);
    if (surface != NULL) {
        if (raster.pixels == surface->pixels) {
            raster.pixels = NULL;
            raster.width = 0;
            raster.height = 0;
        }

        surfaceFree(surface);
        setNativeId(env, self, 0);
    }
}
