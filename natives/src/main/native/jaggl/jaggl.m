/*
 * The part of the OpenGL binding that owns the context and the surface.
 *
 * The generated half of the binding passes its arguments straight through to OpenGL. This half is
 * everything that cannot be: creating the context, giving it somewhere to draw, putting finished
 * frames on screen, and the few natives whose arguments need unpacking first.
 *
 * The approach comes from openrs2-natives, which solved the same problem for a later client.
 *
 *   Copyright (c) 2019-2020 OpenRS2 Authors
 *
 *   Permission to use, copy, modify, and/or distribute this software for any
 *   purpose with or without fee is hereby granted, provided that the above
 *   copyright notice and this permission notice appear in all copies.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 *   REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *   FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 *   INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
 *   LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 *   OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *   PERFORMANCE OF THIS SOFTWARE.
 *
 * Why it is shaped this way. The client draws from the thread it ticks on, and AppKit refuses the
 * calls that touch an NSOpenGLContext's drawable from any thread but the main one. Waiting on the
 * main thread from the drawing thread deadlocks, because the client is often holding the AWT tree
 * lock that the main thread needs to finish a resize. Not waiting races the driver.
 *
 * So AppKit is kept out of the drawing path. The context is a CGL context, which carries no such
 * requirement, and frames reach the screen through a CAOpenGLLayer that Core Animation draws on its
 * own terms. A framebuffer object under a lock is the handoff between the two: the client's frame
 * is copied into it when the client swaps buffers, and copied out of it when Core Animation asks
 * the layer to draw.
 */

#import <Cocoa/Cocoa.h>
#import <QuartzCore/CAOpenGLLayer.h>

#include <OpenGL/gl.h>
#include <OpenGL/glext.h>
#include <OpenGL/OpenGL.h>
#include <dlfcn.h>
#include <limits.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

/* The client renders here, off screen, and the layer shows what lands in it. */
@interface JagGLLayer : CAOpenGLLayer

- (void)present;

@end

/*
 * One per canvas the client draws on. The client holds these as opaque longs and hands them back to
 * say which one it means.
 *
 * The layer is held as a plain pointer with its retain taken by hand, because this record is
 * allocated by malloc and ARC cannot manage an object reference in memory it did not allocate.
 */
typedef struct {
    jobject canvas;
    void *layer;
    GLint width;
    GLint height;
} Surface;

static CGLPixelFormatObj pixelFormat;

/* The context Core Animation draws the layer with, and the context the client draws frames with. */
static CGLContextObj layerContext;
static CGLContextObj clientContext;

/*
 * Somewhere for the client's own drawing to land.
 *
 * The context has no drawable at all. A window would have to be made and resized on the main
 * thread, and the client calls this from the thread it draws on while holding the AWT tree lock
 * that the main thread needs, so waiting on it stalls the client until it gives up. A pixel buffer
 * would avoid that, but this machine's OpenGL refuses to make one.
 *
 * So the client's default framebuffer is a framebuffer object of ours. Where the client asks for
 * framebuffer zero it is given this one, and where it names the front or back buffer it is given
 * this one's colour attachment. Everything else about its drawing is unchanged, and the layer
 * shows whatever lands here.
 */
static GLuint defaultFramebuffer;
static GLuint defaultColour;
static GLuint defaultDepth;

/*
 * How many samples a pixel of the client's own drawable is drawn from, and the plain framebuffer
 * that many are brought back down to.
 *
 * The client draws into a framebuffer of this library's own rather than into the one the window
 * carries, so the samples the pixel format was chosen for reach nothing by themselves: how finely
 * a pixel is drawn is decided by the buffers hung on the framebuffer that is bound. Where the
 * client asks for more than one sample the buffers carry that many and are brought back down to
 * one before the picture is shown, because a blit that scales cannot read a multisampled buffer.
 */
static GLint wantedSamples;
static GLuint resolveFramebuffer;
static GLuint resolveColour;
static GLint offscreenWidth;
static GLint offscreenHeight;
static BOOL defaultBound;

static Surface *currentSurface;

/*
 * The drawing surface comes from the JDK rather than from a library linked at build time, because
 * the JDK that runs the client is not the one that built this.
 */
typedef jboolean (*JawtGetAwt)(JNIEnv *, JAWT *);

static JawtGetAwt jawt(JNIEnv *env) {
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

static BOOL verbose(void) {
    static BOOL cached;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        cached = getenv("JAGGL_VERBOSE") != NULL;
    });
    return cached;
}

/* Reports what the binding is doing. Arguments must be free of side effects. */
#define JAGGLLOG(...) do { if (verbose()) { fprintf(stderr, "[jaggl] " __VA_ARGS__); fputc('\n', stderr); } } while (0)

@implementation JagGLLayer

- (instancetype)init {
    self = [super init];
    if (self != nil) {
        self.asynchronous = NO;
        self.opaque = YES;
        self.needsDisplayOnBoundsChange = YES;
    }
    return self;
}

/*
 * Asks for the finished frame to be shown. The frame itself is already where the layer reads it,
 * because the client draws into a framebuffer both contexts share, so there is nothing to copy.
 *
 * Nothing stops the client drawing the next frame into that framebuffer while Core Animation is
 * reading it for the last one. If frames ever tear or flicker, this is why, and the fix is a
 * second framebuffer: on each swap, copy the finished frame into it under a lock, and have the
 * layer read the copy instead. openrs2-natives does exactly that, and keeps the copy the size of
 * the layer so the resize is handled in the same step.
 *
 * It is not done here because nothing torn has been seen, and one framebuffer with one blit is
 * both quicker and easier to follow than two with a lock between them. Add the copy when there is
 * a reason to, not before.
 */
- (void)present {
    glFlush();

    dispatch_async(dispatch_get_main_queue(), ^{
        [self setNeedsDisplay];
    });
}

- (BOOL)canDrawInCGLContext:(CGLContextObj)context
                pixelFormat:(CGLPixelFormatObj)format
               forLayerTime:(CFTimeInterval)layerTime
                displayTime:(const CVTimeStamp *)displayTime {
    return defaultFramebuffer != 0;
}

- (void)drawInCGLContext:(CGLContextObj)context
             pixelFormat:(CGLPixelFormatObj)format
            forLayerTime:(CFTimeInterval)layerTime
             displayTime:(const CVTimeStamp *)displayTime {
    if (CGLSetCurrentContext(context) != kCGLNoError) {
        return;
    }

    glClearColor(0, 0, 0, 1);
    glClear(GL_COLOR_BUFFER_BIT);

    /* Read straight from what the client drew. See -present for what that risks. */
    if (defaultFramebuffer != 0) {
        CGSize size = self.bounds.size;
        GLuint from = defaultFramebuffer;

        /*
         * A blit that scales cannot read a buffer of more than one sample a pixel, so where there
         * is one it is brought down to a plain buffer of its own size first and the picture is
         * taken from that.
         */
        if (resolveFramebuffer != 0) {
            glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, defaultFramebuffer);
            glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, resolveFramebuffer);
            glBlitFramebufferEXT(0, 0, offscreenWidth, offscreenHeight,
                                 0, 0, offscreenWidth, offscreenHeight,
                                 GL_COLOR_BUFFER_BIT, GL_NEAREST);
            glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, 0);
            from = resolveFramebuffer;
        }

        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, from);
        glBlitFramebufferEXT(0, 0, offscreenWidth, offscreenHeight,
                             0, 0, (GLint) size.width, (GLint) size.height,
                             GL_COLOR_BUFFER_BIT, GL_NEAREST);
        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, 0);
    }

    [super drawInCGLContext:context pixelFormat:format forLayerTime:layerTime displayTime:displayTime];
}

- (CGLPixelFormatObj)copyCGLPixelFormatForDisplayMask:(uint32_t)mask {
    return pixelFormat;
}

- (void)releaseCGLPixelFormat:(CGLPixelFormatObj)format {
    /* empty, the format outlives every layer */
}

- (CGLContextObj)copyCGLContextForPixelFormat:(CGLPixelFormatObj)format {
    return layerContext;
}

- (void)releaseCGLContext:(CGLContextObj)context {
    /* empty, the context outlives every layer */
}

@end

/*
 * Reads where a component sits and how big it is, and attaches the layer that shows it.
 *
 * The bounds come from the JDK's own drawing surface rather than from the component, because the
 * layer is placed in the window's coordinates, which count up from the bottom where AWT counts down
 * from the top.
 */
static BOOL attach(JNIEnv *env, jobject canvas, Surface *surface) {
    JawtGetAwt getAwt = jawt(env);
    if (getAwt == NULL) {
        JAGGLLOG("no drawing surface in the running JDK");
        return NO;
    }

    JAWT awt;
    awt.version = JAWT_VERSION_1_4 | JAWT_MACOSX_USE_CALAYER;
    if (!getAwt(env, &awt)) {
        JAGGLLOG("the JDK refused a layer surface");
        return NO;
    }

    JAWT_DrawingSurface *drawing = awt.GetDrawingSurface(env, canvas);
    if (drawing == NULL) {
        return NO;
    }

    __block BOOL attached = NO;
    if ((drawing->Lock(drawing) & JAWT_LOCK_ERROR) == 0) {
        JAWT_DrawingSurfaceInfo *info = drawing->GetDrawingSurfaceInfo(drawing);
        if (info != NULL) {
            id<JAWT_SurfaceLayers> layers = (__bridge id<JAWT_SurfaceLayers>) info->platformInfo;
            JagGLLayer *layer = (__bridge JagGLLayer *) surface->layer;
            CGRect frame = CGRectMake(info->bounds.x, 0, info->bounds.width, info->bounds.height);

            surface->width = info->bounds.width;
            surface->height = info->bounds.height;
            jint top = info->bounds.y;
            attached = YES;

            dispatch_async(dispatch_get_main_queue(), ^{
                CGFloat window = layers.windowLayer.bounds.size.height;
                layer.frame = CGRectMake(frame.origin.x, window - top - frame.size.height,
                                         frame.size.width, frame.size.height);
                if (layer.superlayer == nil) {
                    layers.layer = layer;
                }
            });

            drawing->FreeDrawingSurfaceInfo(info);
        }
        drawing->Unlock(drawing);
    }

    awt.FreeDrawingSurface(drawing);
    JAGGLLOG("attached %dx%d", surface->width, surface->height);
    return attached;
}

/*
 * Sizes the drawable the client draws into. It is never shown, so its only job is to be as large as
 * the surface being drawn for.
 */
/**
 * Hangs a buffer on the renderbuffer that is bound, with as many samples a pixel as the client
 * asked the context for.
 */
static void storage(GLenum format, GLint width, GLint height) {
    if (wantedSamples > 0) {
        glRenderbufferStorageMultisampleEXT(GL_RENDERBUFFER_EXT, wantedSamples, format, width, height);
    } else {
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, format, width, height);
    }
}

static BOOL resizeOffscreen(GLint width, GLint height) {
    if (width <= 0 || height <= 0) {
        return NO;
    }

    if (defaultFramebuffer != 0 && width == offscreenWidth && height == offscreenHeight) {
        return YES;
    }

    if (defaultFramebuffer != 0) {
        glDeleteRenderbuffersEXT(1, &defaultDepth);
        glDeleteRenderbuffersEXT(1, &defaultColour);
        glDeleteFramebuffersEXT(1, &defaultFramebuffer);
    }

    if (resolveFramebuffer != 0) {
        glDeleteRenderbuffersEXT(1, &resolveColour);
        glDeleteFramebuffersEXT(1, &resolveFramebuffer);
        resolveFramebuffer = 0;
        resolveColour = 0;
    }

    glGenFramebuffersEXT(1, &defaultFramebuffer);
    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, defaultFramebuffer);

    glGenRenderbuffersEXT(1, &defaultColour);
    glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, defaultColour);
    storage(GL_RGBA8, width, height);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_RENDERBUFFER_EXT, defaultColour);

    glGenRenderbuffersEXT(1, &defaultDepth);
    glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, defaultDepth);
    storage(GL_DEPTH24_STENCIL8_EXT, width, height);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, defaultDepth);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_STENCIL_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, defaultDepth);

    GLenum status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
    if (status != GL_FRAMEBUFFER_COMPLETE_EXT) {
        JAGGLLOG("the default framebuffer is not complete at %dx%d, status 0x%x", width, height, status);
        return NO;
    }

    if (wantedSamples > 0) {
        glGenFramebuffersEXT(1, &resolveFramebuffer);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, resolveFramebuffer);
        glGenRenderbuffersEXT(1, &resolveColour);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, resolveColour);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGBA8, width, height);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
                                     GL_RENDERBUFFER_EXT, resolveColour);

        if (glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) != GL_FRAMEBUFFER_COMPLETE_EXT) {
            JAGGLLOG("no plain framebuffer to bring %d samples down to", wantedSamples);
            glDeleteRenderbuffersEXT(1, &resolveColour);
            glDeleteFramebuffersEXT(1, &resolveFramebuffer);
            resolveFramebuffer = 0;
            resolveColour = 0;
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, defaultFramebuffer);
    }

    glDrawBuffer(GL_COLOR_ATTACHMENT0_EXT);
    glReadBuffer(GL_COLOR_ATTACHMENT0_EXT);

    defaultBound = YES;
    offscreenWidth = width;
    offscreenHeight = height;
    JAGGLLOG("drawable %dx%d", width, height);
    return YES;
}

/**
 * Makes a piece of what the client drew readable, and answers what was bound to read it before.
 *
 * Nothing may be read out of a buffer of more than one sample a pixel, so where the client's own
 * buffer carries several the piece wanted is brought down to one in the plain buffer beside it and
 * that is what is read instead. Only the piece is brought down rather than the whole picture,
 * because the client reads its buffer back a row at a time and bringing the whole down for each
 * row would cost the picture over for every row of it.
 *
 * Where there is one sample a pixel, or where the client is reading a buffer of its own, nothing
 * is done and nothing needs putting back.
 */
static GLuint readableRegion(GLint x, GLint y, GLint width, GLint height) {
    if (resolveFramebuffer == 0 || !defaultBound || width <= 0 || height <= 0) {
        return 0;
    }

    GLint bound = 0;
    glGetIntegerv(GL_READ_FRAMEBUFFER_BINDING_EXT, &bound);

    glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, defaultFramebuffer);
    glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, resolveFramebuffer);
    glBlitFramebufferEXT(x, y, x + width, y + height, x, y, x + width, y + height,
                         GL_COLOR_BUFFER_BIT, GL_NEAREST);
    glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, defaultFramebuffer);
    glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, resolveFramebuffer);

    return (GLuint) bound;
}

/**
 * Puts back whatever was bound to read before a piece was made readable.
 */
static void doneReading(GLuint bound) {
    if (resolveFramebuffer != 0 && defaultBound) {
        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, bound);
    }
}

/*
 * The natives that take a picture out of whatever is bound to read. Each is written by hand rather
 * than passed straight on, because each has to say which piece it is about to read so that the
 * piece can be brought down to one sample a pixel first.
 */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_glReadPixelsi(JNIEnv *env, jclass owner, jint x, jint y,
                                                        jint width, jint height, jint format,
                                                        jint type, jintArray pixels, jint offset) {
    GLuint bound = readableRegion(x, y, width, height);
    jint *address = pixels == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, pixels, NULL);
    glReadPixels(x, y, width, height, (GLenum) format, (GLenum) type,
                 address == NULL ? NULL : (void *) (address + offset));
    if (address != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, pixels, address, 0);
    }
    doneReading(bound);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glReadPixelsub(JNIEnv *env, jclass owner, jint x, jint y,
                                                         jint width, jint height, jint format,
                                                         jint type, jbyteArray pixels, jint offset) {
    GLuint bound = readableRegion(x, y, width, height);
    jbyte *address = pixels == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, pixels, NULL);
    glReadPixels(x, y, width, height, (GLenum) format, (GLenum) type,
                 address == NULL ? NULL : (void *) (address + offset));
    if (address != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, pixels, address, 0);
    }
    doneReading(bound);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glCopyTexImage2D(JNIEnv *env, jclass owner, jint target,
                                                            jint level, jint format, jint x, jint y,
                                                            jint width, jint height, jint border) {
    GLuint bound = readableRegion(x, y, width, height);
    glCopyTexImage2D((GLenum) target, level, (GLenum) format, x, y, width, height, border);
    doneReading(bound);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glCopyTexSubImage2D(JNIEnv *env, jclass owner, jint target,
                                                               jint level, jint intoX, jint intoY,
                                                               jint x, jint y, jint width,
                                                               jint height) {
    GLuint bound = readableRegion(x, y, width, height);
    glCopyTexSubImage2D((GLenum) target, level, intoX, intoY, x, y, width, height);
    doneReading(bound);
}

/**
 * The client's own blit. Where it reads the client's buffer the piece it reads is brought down
 * first, unless it is asking for the whole of it unscaled, which is a resolve already.
 */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_glBlitFramebufferEXT(JNIEnv *env, jclass owner,
                                                               jint fromX0, jint fromY0,
                                                               jint fromX1, jint fromY1,
                                                               jint toX0, jint toY0, jint toX1,
                                                               jint toY1, jint mask, jint filter) {
    GLint left = fromX0 < fromX1 ? fromX0 : fromX1;
    GLint bottom = fromY0 < fromY1 ? fromY0 : fromY1;
    GLuint bound = readableRegion(left, bottom, (fromX1 > fromX0 ? fromX1 - fromX0 : fromX0 - fromX1),
                                  (fromY1 > fromY0 ? fromY1 - fromY0 : fromY0 - fromY1));
    glBlitFramebufferEXT(fromX0, fromY0, fromX1, fromY1, toX0, toY0, toX1, toY1,
                         (GLbitfield) mask, (GLenum) filter);
    doneReading(bound);
}

JNIEXPORT jlong JNICALL Java_jaggl_OpenGL_prepareSurface(JNIEnv *env, jclass owner, jobject canvas);
JNIEXPORT jboolean JNICALL Java_jaggl_OpenGL_setSurface(JNIEnv *env, jclass owner, jlong handle);

JNIEXPORT jlong JNICALL Java_jaggl_OpenGL_init(JNIEnv *env, jclass owner, jobject canvas,
                                               jint red, jint green, jint blue, jint depth,
                                               jint stencil, jint samples) {
    CGLPixelFormatAttribute attributes[32];
    int n = 0;
    attributes[n++] = kCGLPFAAccelerated;
    attributes[n++] = kCGLPFADoubleBuffer;
    attributes[n++] = kCGLPFAColorSize;
    attributes[n++] = (CGLPixelFormatAttribute) (red + green + blue);
    attributes[n++] = kCGLPFADepthSize;
    attributes[n++] = (CGLPixelFormatAttribute) depth;
    if (stencil > 0) {
        attributes[n++] = kCGLPFAStencilSize;
        attributes[n++] = (CGLPixelFormatAttribute) stencil;
    }
    if (samples > 0) {
        attributes[n++] = kCGLPFAMultisample;
        attributes[n++] = kCGLPFASampleBuffers;
        attributes[n++] = (CGLPixelFormatAttribute) 1;
        attributes[n++] = kCGLPFASamples;
        attributes[n++] = (CGLPixelFormatAttribute) samples;
    }
    attributes[n] = (CGLPixelFormatAttribute) 0;

    wantedSamples = samples;

    GLint formats = 0;
    if (CGLChoosePixelFormat(attributes, &pixelFormat, &formats) != kCGLNoError || pixelFormat == NULL) {
        JAGGLLOG("no pixel format for %d bits of colour and %d of depth", red + green + blue, depth);
        return 0;
    }

    if (CGLCreateContext(pixelFormat, NULL, &layerContext) != kCGLNoError) {
        JAGGLLOG("no context for the layer");
        return 0;
    }

    if (CGLCreateContext(pixelFormat, layerContext, &clientContext) != kCGLNoError) {
        JAGGLLOG("no context for the client");
        return 0;
    }

    JAGGLLOG("context ready");

    jlong handle = Java_jaggl_OpenGL_prepareSurface(env, owner, canvas);
    if (handle == 0) {
        return 0;
    }

    if (!Java_jaggl_OpenGL_setSurface(env, owner, handle)) {
        return 0;
    }

    return handle;
}

JNIEXPORT jlong JNICALL Java_jaggl_OpenGL_prepareSurface(JNIEnv *env, jclass owner, jobject canvas) {
    Surface *surface = calloc(1, sizeof(Surface));
    surface->canvas = (*env)->NewGlobalRef(env, canvas);

    surface->layer = (__bridge_retained void *) [[JagGLLayer alloc] init];

    if (!attach(env, canvas, surface)) {
        (*env)->DeleteGlobalRef(env, surface->canvas);
        CFBridgingRelease(surface->layer);
        free(surface);
        return 0;
    }

    return (jlong) (intptr_t) surface;
}

JNIEXPORT jboolean JNICALL Java_jaggl_OpenGL_setSurface(JNIEnv *env, jclass owner, jlong handle) {
    Surface *surface = (Surface *) (intptr_t) handle;
    if (surface == NULL) {
        return JNI_FALSE;
    }

    if (CGLSetCurrentContext(clientContext) != kCGLNoError) {
        return JNI_FALSE;
    }

    currentSurface = surface;
    return resizeOffscreen(surface->width, surface->height) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_surfaceResized(JNIEnv *env, jclass owner, jlong handle) {
    Surface *surface = (Surface *) (intptr_t) handle;
    if (surface == NULL) {
        return;
    }

    attach(env, surface->canvas, surface);
    if (surface == currentSurface) {
        resizeOffscreen(surface->width, surface->height);
    }
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_releaseSurface(JNIEnv *env, jclass owner, jobject canvas, jlong handle) {
    Surface *surface = (Surface *) (intptr_t) handle;
    if (surface == NULL) {
        return;
    }

    if (surface == currentSurface) {
        currentSurface = NULL;
    }

    JagGLLayer *layer = (__bridge JagGLLayer *) surface->layer;
    dispatch_async(dispatch_get_main_queue(), ^{
        [layer removeFromSuperlayer];
    });

    (*env)->DeleteGlobalRef(env, surface->canvas);
    CFBridgingRelease(surface->layer);
    free(surface);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_swapBuffers(JNIEnv *env, jclass owner) {
    if (currentSurface != NULL) {
        [(__bridge JagGLLayer *) currentSurface->layer present];
    }
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_setSwapInterval(JNIEnv *env, jclass owner, jint interval) {
    GLint value = interval;
    CGLSetParameter(clientContext, kCGLCPSwapInterval, &value);
}

JNIEXPORT jboolean JNICALL Java_jaggl_OpenGL_attachPeer(JNIEnv *env, jclass owner) {
    return CGLSetCurrentContext(clientContext) == kCGLNoError;
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_detachPeer(JNIEnv *env, jclass owner) {
    CGLSetCurrentContext(NULL);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_release(JNIEnv *env, jclass owner) {
    CGLSetCurrentContext(NULL);

    if (clientContext != NULL) {
        CGLDestroyContext(clientContext);
        clientContext = NULL;
    }
    if (layerContext != NULL) {
        CGLDestroyContext(layerContext);
        layerContext = NULL;
    }
    if (pixelFormat != NULL) {
        CGLDestroyPixelFormat(pixelFormat);
        pixelFormat = NULL;
    }

    defaultFramebuffer = 0;
    defaultColour = 0;
    defaultDepth = 0;
    offscreenWidth = 0;
    offscreenHeight = 0;
}

/*
 * The client asks whether pixel buffers are available and prefers framebuffer objects when they
 * are, which this machine's OpenGL has, so it never takes the pixel buffer path. Saying no keeps
 * the three that serve it unreachable rather than wrong.
 */
JNIEXPORT jboolean JNICALL Java_jaggl_OpenGL_arePbuffersAvailable(JNIEnv *env, jclass owner) {
    return JNI_FALSE;
}

JNIEXPORT jlong JNICALL Java_jaggl_OpenGL_createPbuffer(JNIEnv *env, jclass owner, jint width, jint height) {
    return 0;
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_setPbuffer(JNIEnv *env, jclass owner, jlong handle) {
    /* empty, unreachable while pixel buffers are reported unavailable */
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_releasePbuffer(JNIEnv *env, jclass owner, jlong handle) {
    /* empty, unreachable while pixel buffers are reported unavailable */
}

JNIEXPORT jstring JNICALL Java_jaggl_OpenGL_glGetString(JNIEnv *env, jclass owner, jint name) {
    const GLubyte *value = glGetString((GLenum) name);
    if (value == NULL) {
        return NULL;
    }
    return (*env)->NewStringUTF(env, (const char *) value);
}

JNIEXPORT jint JNICALL Java_jaggl_OpenGL_glGetUniformLocationARB(JNIEnv *env, jclass owner,
                                                                 jlong program, jstring name) {
    const char *chars = (*env)->GetStringUTFChars(env, name, NULL);
    GLint location = glGetUniformLocationARB((GLhandleARB) program, chars);
    (*env)->ReleaseStringUTFChars(env, name, chars);
    return location;
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glShaderSourceARB(JNIEnv *env, jclass owner,
                                                           jlong shader, jstring source) {
    const char *chars = (*env)->GetStringUTFChars(env, source, NULL);
    glShaderSourceARB((GLhandleARB) shader, 1, &chars, NULL);
    (*env)->ReleaseStringUTFChars(env, source, chars);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glProgramStringARB(JNIEnv *env, jclass owner,
                                                            jint target, jint format, jstring program) {
    const char *chars = (*env)->GetStringUTFChars(env, program, NULL);
    glProgramStringARB((GLenum) target, (GLenum) format, (GLsizei) strlen(chars), chars);
    (*env)->ReleaseStringUTFChars(env, program, chars);
}

/*
 * The raw pair take the bytes the client already holds rather than a string, so the length comes
 * from the array and the bytes are not required to end in a zero.
 */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_glShaderSourceRawARB(JNIEnv *env, jclass owner,
                                                              jlong shader, jbyteArray source) {
    jsize length = (*env)->GetArrayLength(env, source);
    jbyte *bytes = (*env)->GetPrimitiveArrayCritical(env, source, NULL);
    const char *chars = (const char *) bytes;
    GLint count = length;
    glShaderSourceARB((GLhandleARB) shader, 1, &chars, &count);
    (*env)->ReleasePrimitiveArrayCritical(env, source, bytes, JNI_ABORT);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glProgramRawARB(JNIEnv *env, jclass owner,
                                                          jint target, jint format, jbyteArray program) {
    jsize length = (*env)->GetArrayLength(env, program);
    jbyte *bytes = (*env)->GetPrimitiveArrayCritical(env, program, NULL);
    glProgramStringARB((GLenum) target, (GLenum) format, (GLsizei) length, bytes);
    (*env)->ReleasePrimitiveArrayCritical(env, program, bytes, JNI_ABORT);
}

/* Named in the singular by the client where OpenGL takes a count and an address. */
JNIEXPORT jint JNICALL Java_jaggl_OpenGL_glGenProgramARB(JNIEnv *env, jclass owner) {
    GLuint program = 0;
    glGenProgramsARB(1, &program);
    return (jint) program;
}

/*
 * Named in the singular by the client where OpenGL takes a count and a list, so this needs a body
 * rather than a pass-through.
 *
 * The Mac build of the shipped library exports this as glDeleteProgram, which is not the name the
 * Java declares, so GlToolkit and GlxToolkit both fail against it the first time they tear a
 * shader down. The name here is the one the Java declares.
 */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_glDeleteProgramARB(JNIEnv *env, jclass owner, jint program) {
    GLuint name = (GLuint) program;
    glDeleteProgramsARB(1, &name);
}

/* An extra length follows the array here, where every other native ends with the offset. */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_glBufferDataARBub(JNIEnv *env, jclass owner, jint target,
                                                            jint size, jbyteArray data, jint offset,
                                                            jint usage) {
    jbyte *bytes = data == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, data, NULL);
    glBufferDataARB((GLenum) target, (GLsizeiptrARB) size,
                    bytes == NULL ? NULL : bytes + offset, (GLenum) usage);
    if (bytes != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, data, bytes, JNI_ABORT);
    }
}

/* Two arrays, one for the length written and one for the text. */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_glGetInfoLogARB(JNIEnv *env, jclass owner, jlong object,
                                                          jint limit, jintArray written,
                                                          jint writtenOffset, jbyteArray log,
                                                          jint logOffset) {
    jint *counts = written == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, written, NULL);
    jbyte *text = log == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, log, NULL);
    glGetInfoLogARB((GLhandleARB) object, (GLsizei) limit,
                    counts == NULL ? NULL : (GLsizei *) (counts + writtenOffset),
                    text == NULL ? NULL : (GLcharARB *) (text + logOffset));
    if (text != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, log, text, 0);
    }
    if (counts != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, written, counts, 0);
    }
}

/*
 * The client's default framebuffer is one of ours, so the three natives that name it are answered
 * rather than passed through.
 */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_glBindFramebufferEXT(JNIEnv *env, jclass owner,
                                                              jint target, jint framebuffer) {
    GLuint wanted = framebuffer == 0 ? defaultFramebuffer : (GLuint) framebuffer;
    glBindFramebufferEXT((GLenum) target, wanted);

    if (target == GL_FRAMEBUFFER_EXT || target == GL_DRAW_FRAMEBUFFER_EXT) {
        defaultBound = framebuffer == 0;
    }
}

static GLenum attachmentFor(jint buffer) {
    if (buffer == GL_BACK || buffer == GL_FRONT || buffer == GL_FRONT_AND_BACK || buffer == GL_NONE) {
        return GL_COLOR_ATTACHMENT0_EXT;
    }
    return (GLenum) buffer;
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glDrawBuffer(JNIEnv *env, jclass owner, jint buffer) {
    glDrawBuffer(defaultBound ? attachmentFor(buffer) : (GLenum) buffer);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glReadBuffer(JNIEnv *env, jclass owner, jint buffer) {
    glReadBuffer(defaultBound ? attachmentFor(buffer) : (GLenum) buffer);
}
