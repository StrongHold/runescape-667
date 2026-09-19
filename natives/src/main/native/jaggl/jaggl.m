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
 * Why it is shaped this way. The client draws into a view inside the window it is shown in, and
 * showing a frame is that view's buffers being swapped. Nothing is copied anywhere.
 *
 * That sounds like the obvious arrangement and it was arrived at the long way round. A modern JDK
 * hands out a layer rather than a view, so there is no view to be had for the asking, and what was
 * built instead drew into something of this library's own and copied the result to the screen:
 * first a layer Core Animation drew on its own terms, then a framebuffer object with no drawable
 * at all. Both work. Both stutter on a machine driving screens the built-in one is not among,
 * badly enough to be unusable, and a bisect put the start of it at the commit where the client
 * began using this binding rather than the one it shipped with. The shipped one draws into a view.
 *
 * So the view is found rather than asked for: the JDK names the window's own layer, and the window
 * whose layer that is holds the view the canvas is in. One of ours goes inside it.
 *
 * The cost is that giving a context a view, and sizing one, are main thread work, and the client
 * draws from the thread it ticks on. Those are waited for. Waiting on the main thread from the
 * drawing thread can deadlock, because the client may hold the AWT tree lock that the main thread
 * wants to finish a resize, and if that is ever seen this is where it will be. Not waiting was
 * tried, and leaves the client drawing into a view that is not yet the right size or not yet on
 * screen.
 */

#import <Cocoa/Cocoa.h>
#import <QuartzCore/CAOpenGLLayer.h>
#import <QuartzCore/CATransaction.h>

#include <OpenGL/gl.h>
#include <OpenGL/glext.h>
#include <OpenGL/OpenGL.h>
#include <dlfcn.h>
#include <limits.h>
#include <sys/time.h>
#include <stdatomic.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

/* The client renders here, off screen, and the layer shows what lands in it. */

/*
 * One per canvas the client draws on. The client holds these as opaque longs and hands them back to
 * say which one it means.
 *
 * The layer is held as a plain pointer with its retain taken by hand, because this record is
 * allocated by malloc and ARC cannot manage an object reference in memory it did not allocate.
 */
/*
 * One per canvas the client draws on. The client holds these as opaque longs and hands them back
 * to say which one it means.
 *
 * The view is one of ours, put inside the window the canvas belongs to. The client's context is
 * attached to it and draws into it, and showing a frame is the context swapping its own buffers.
 * There is no layer, no framebuffer of ours, and no copy.
 */
typedef struct {
    jobject canvas;
    void *view;
    GLint width;
    GLint height;
} Surface;

static CGLPixelFormatObj pixelFormat;

/* The context Core Animation draws the layer with, and the context the client draws frames with. */
static CGLContextObj layerContext;

/*
 * The client's context as Cocoa sees it, so that it can be given a view to draw into and asked to
 * swap that view's buffers.
 */
static NSOpenGLContext *drawingContext;
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
static GLint offscreenWidth;
static GLint offscreenHeight;
static uint64_t shownFrames;




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

/**
 * Whether to report how long the showing of a frame takes.
 *
 * Off unless asked for, because it reads the clock twice a frame and the point of it is to find
 * out where a frame's time goes rather than to add to it.
 */
static BOOL timing(void) {
    static BOOL cached;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        cached = getenv("JAGGL_TIMING") != NULL;
    });
    return cached;
}

static uint64_t nowInMicroseconds(void) {
    struct timeval now;
    gettimeofday(&now, NULL);
    return (uint64_t) now.tv_sec * 1000000u + (uint64_t) now.tv_usec;
}

/**
 * Says how a frame's time was spent, once every hundred frames.
 *
 * Three numbers, and between them they say which part is at fault. How often a frame is handed
 * over says whether the client is making them evenly. How long the handing over takes says what
 * this library costs. How long the layer takes to show one, and how often it is asked to, say
 * whether what the client finishes is what reaches the screen: a client handing over fifty frames
 * a second while the layer shows thirty is a client whose frames are being dropped, and that is
 * choppy however healthy the count looks.
 */
static void reportTiming(uint64_t spentSwapping) {
    static uint64_t frames;
    static uint64_t swapping;
    static uint64_t lastReport;
    static uint64_t lastSwap;
    static uint64_t betweenSwaps;

    if (!timing()) {
        return;
    }

    uint64_t now = nowInMicroseconds();
    swapping += spentSwapping;
    if (lastSwap != 0) {
        betweenSwaps += now - lastSwap;
    }
    lastSwap = now;
    frames++;

    if (lastReport == 0) {
        lastReport = now;
        return;
    }

    if (frames < 100) {
        return;
    }

    JAGGLLOG("%llu frames every %llu us, %llu of that handing over; the layer showed %llu",
             frames, betweenSwaps / frames, swapping / frames, shownFrames);

    frames = 0;
    swapping = 0;
    betweenSwaps = 0;
    shownFrames = 0;
    lastReport = now;
}


/*
 * Reads where a component sits and how big it is, and attaches the layer that shows it.
 *
 * The bounds come from the JDK's own drawing surface rather than from the component, because the
 * layer is placed in the window's coordinates, which count up from the bottom where AWT counts down
 * from the top.
 */
/**
 * Runs the work on the main thread and waits for it.
 *
 * Attaching a layer and sizing a drawable were both handed over without waiting once, to keep the
 * drawing thread off the main one. They are waited for again because the binding was smooth when
 * they were and has not been since: what the client draws into and what shows it are settled
 * before the client is told it may draw, rather than a frame or two afterwards.
 */
static void onMainThreadAndWait(void (^work)(void)) {
    if ([NSThread isMainThread]) {
        work();
    } else {
        dispatch_sync(dispatch_get_main_queue(), work);
    }
}

/**
 * Finds the view the canvas's window draws through.
 *
 * A modern JDK hands out a layer rather than a view, so the view has to be found by the layer: the
 * window whose own layer is the one named is the window the canvas is in.
 */
static NSView *windowView(id<JAWT_SurfaceLayers> layers) {
    __block NSView *found = nil;
    CALayer *windowLayer = layers.windowLayer;

    onMainThreadAndWait(^{
        for (NSWindow *window in [NSApp windows]) {
            NSView *content = [window contentView];
            if (found == nil && content != nil && content.layer == windowLayer) {
                found = content;
            }
        }
    });

    return found;
}

/**
 * Puts a view of our own where the canvas is, for the client's context to draw into.
 *
 * The client draws into a view in the window it is shown in, which is what the shipped binding
 * does and what this machine is quick at. Drawing into anything else and copying the result to the
 * screen is what made this slow: it was tried through a layer of its own and through a framebuffer
 * object, and both stutter on a machine driving screens the built-in one is not among.
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
            NSView *host = windowView(layers);

            surface->width = info->bounds.width;
            surface->height = info->bounds.height;

            if (host != nil) {
                jint left = info->bounds.x;
                jint top = info->bounds.y;
                GLint wide = info->bounds.width;
                GLint high = info->bounds.height;
                __block NSView *ours = (__bridge NSView *) surface->view;

                onMainThreadAndWait(^{
                    CGFloat window = host.bounds.size.height;
                    NSRect frame = NSMakeRect(left, window - top - high, wide, high);

                    if (ours == nil) {
                        ours = [[NSView alloc] initWithFrame:frame];

                        /*
                         * The client draws at the size it was given and knows nothing of the
                         * backing store. Left to itself the surface is sized in backing store
                         * pixels, and on a screen with more than one pixel to a point the picture
                         * comes out at a fraction of the size in the bottom left corner, which is
                         * where the OpenGL origin is.
                         */
                        ours.wantsBestResolutionOpenGLSurface = NO;
                        [host addSubview:ours];
                        surface->view = (__bridge_retained void *) ours;
                    } else {
                        ours.frame = frame;
                    }

                    attached = YES;
                });
            }

            drawing->FreeDrawingSurfaceInfo(info);
        }
        drawing->Unlock(drawing);
    }

    awt.FreeDrawingSurface(drawing);
    JAGGLLOG("attached %dx%d", surface->width, surface->height);
    return attached;
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
    /*
     * The samples the client asks for are asked of the format, which is where they belong now that
     * the client draws into a drawable of its own rather than into a framebuffer object, and is
     * how the shipped library asks for them.
     */
    if (samples > 0) {
        attributes[n++] = kCGLPFAMultisample;
        attributes[n++] = kCGLPFASampleBuffers;
        attributes[n++] = (CGLPixelFormatAttribute) 1;
        attributes[n++] = kCGLPFASamples;
        attributes[n++] = (CGLPixelFormatAttribute) samples;
    }

    attributes[n] = (CGLPixelFormatAttribute) 0;


    JAGGLLOG("asked for %d samples a pixel", samples);

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

    drawingContext = [[NSOpenGLContext alloc] initWithCGLContextObj:clientContext];

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

    if (!attach(env, canvas, surface)) {
        (*env)->DeleteGlobalRef(env, surface->canvas);
        free(surface);
        return 0;
    }

    JAGGLLOG("surface %p prepared, view %p", (void *) surface, surface->view);
    return (jlong) (intptr_t) surface;
}

/*
 * Points the client's context at the view it is to draw into, and makes it current.
 *
 * Giving a context a view has to happen on the main thread, and is waited for, because the client
 * is told it may draw the moment this returns.
 */
JNIEXPORT jboolean JNICALL Java_jaggl_OpenGL_setSurface(JNIEnv *env, jclass owner, jlong handle) {
    Surface *surface = (Surface *) (intptr_t) handle;
    if (surface == NULL || surface->view == NULL) {
        return JNI_FALSE;
    }

    NSView *view = (__bridge NSView *) surface->view;
    onMainThreadAndWait(^{
        if (drawingContext.view != view) {
            drawingContext.view = view;
        }
        [drawingContext update];
    });

    currentSurface = surface;
    return CGLSetCurrentContext(clientContext) == kCGLNoError ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_surfaceResized(JNIEnv *env, jclass owner, jlong handle) {
    Surface *surface = (Surface *) (intptr_t) handle;
    if (surface == NULL) {
        return;
    }

    attach(env, surface->canvas, surface);

    if (surface == currentSurface) {
        onMainThreadAndWait(^{
            [drawingContext update];
        });
    }
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_releaseSurface(JNIEnv *env, jclass owner, jobject canvas,
                                                        jlong handle) {
    Surface *surface = (Surface *) (intptr_t) handle;
    if (surface == NULL) {
        return;
    }

    if (surface == currentSurface) {
        currentSurface = NULL;
    }

    NSView *view = (__bridge_transfer NSView *) surface->view;
    surface->view = NULL;
    onMainThreadAndWait(^{
        if (drawingContext.view == view) {
            [drawingContext clearDrawable];
        }
        [view removeFromSuperview];
    });

    JAGGLLOG("surface %p released, view %p taken off the window", (void *) surface,
            (__bridge void *) view);

    (*env)->DeleteGlobalRef(env, surface->canvas);
    free(surface);
}

/*
 * Shows the frame the client has just finished, by swapping the buffers of the view it drew into.
 *
 * This is what the shipped binding does and it is the whole of why it is smooth here. Everything
 * else tried in its place drew somewhere of ours and copied the result to the screen, and copying
 * is what stutters on a machine driving screens the built-in one is not among.
 */
JNIEXPORT void JNICALL Java_jaggl_OpenGL_swapBuffers(JNIEnv *env, jclass owner) {
    uint64_t began = timing() ? nowInMicroseconds() : 0;

    [drawingContext flushBuffer];
    shownFrames++;

    reportTiming(timing() ? nowInMicroseconds() - began : 0);
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

    drawingContext = nil;

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

