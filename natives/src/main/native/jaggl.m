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
@interface JagGLLayer : CAOpenGLLayer {
    @private
    GLuint framebuffer;
    GLuint colour;
    GLuint depth;
    GLint width;
    GLint height;
    NSLock *lock;
}

- (void)blit;

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
 * Somewhere for the client's own drawing to land. The client draws to the default framebuffer, so
 * the context it draws with needs a real drawable of its own, and that drawable is never shown.
 */
static NSWindow *offscreenWindow;
static NSView *offscreenView;
static NSOpenGLContext *offscreenContext;

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
        lock = [[NSLock alloc] init];
    }
    return self;
}

/*
 * Copies the frame the client has just finished into the framebuffer the layer draws from.
 *
 * This runs on the client's thread with the client's context current, which is why the framebuffer
 * is shared between the two contexts rather than handed over as a drawable.
 */
- (void)blit {
    CGSize size = self.bounds.size;
    GLint wanted = (GLint) size.width;
    GLint tall = (GLint) size.height;
    if (wanted <= 0 || tall <= 0) {
        return;
    }

    [lock lock];

    if (framebuffer == 0 || wanted != width || tall != height) {
        if (framebuffer != 0) {
            glDeleteRenderbuffersEXT(1, &depth);
            glDeleteRenderbuffersEXT(1, &colour);
            glDeleteFramebuffersEXT(1, &framebuffer);
        }

        glGenFramebuffersEXT(1, &framebuffer);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebuffer);

        glGenRenderbuffersEXT(1, &colour);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, colour);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGB, wanted, tall);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_RENDERBUFFER_EXT, colour);

        glGenRenderbuffersEXT(1, &depth);
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depth);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT24, wanted, tall);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depth);

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        width = wanted;
        height = tall;
        JAGGLLOG("framebuffer %dx%d", width, height);
    }

    glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, 0);
    glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, framebuffer);
    glBlitFramebufferEXT(0, 0, width, height, 0, 0, width, height, GL_COLOR_BUFFER_BIT, GL_NEAREST);
    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    glFlush();

    [lock unlock];

    dispatch_async(dispatch_get_main_queue(), ^{
        [self setNeedsDisplay];
    });
}

- (BOOL)canDrawInCGLContext:(CGLContextObj)context
                pixelFormat:(CGLPixelFormatObj)format
               forLayerTime:(CFTimeInterval)layerTime
                displayTime:(const CVTimeStamp *)displayTime {
    return framebuffer != 0;
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

    [lock lock];
    if (framebuffer != 0) {
        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, framebuffer);
        glBlitFramebufferEXT(0, 0, width, height, 0, 0, width, height, GL_COLOR_BUFFER_BIT, GL_NEAREST);
        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, 0);
    }
    [lock unlock];

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

            dispatch_sync(dispatch_get_main_queue(), ^{
                CGFloat top = layers.windowLayer.bounds.size.height;
                layer.frame = CGRectMake(frame.origin.x, top - info->bounds.y - frame.size.height,
                                         frame.size.width, frame.size.height);
                if (layer.superlayer == nil) {
                    layers.layer = layer;
                }
                attached = YES;
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
 * Sizes the drawable the client draws into. It is never shown, so its only job is to be at least as
 * large as the surface being drawn for.
 */
static void resizeOffscreen(GLint width, GLint height) {
    dispatch_sync(dispatch_get_main_queue(), ^{
        NSRect frame = NSMakeRect(0, 0, width, height);
        offscreenView.frame = frame;
        [offscreenWindow setFrame:[offscreenWindow frameRectForContentRect:frame] display:NO];
        [offscreenContext update];
    });
}

JNIEXPORT jlong JNICALL Java_jaggl_OpenGL_prepareSurface(JNIEnv *env, jclass owner, jobject canvas);

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

    dispatch_sync(dispatch_get_main_queue(), ^{
        offscreenWindow = [[NSWindow alloc] initWithContentRect:NSMakeRect(0, 0, 16, 16)
                                                      styleMask:NSWindowStyleMaskBorderless
                                                        backing:NSBackingStoreBuffered
                                                          defer:NO];
        offscreenWindow.releasedWhenClosed = NO;
        offscreenView = [[NSView alloc] initWithFrame:NSMakeRect(0, 0, 16, 16)];
        offscreenView.wantsBestResolutionOpenGLSurface = NO;
        offscreenWindow.contentView = offscreenView;

        offscreenContext = [[NSOpenGLContext alloc] initWithCGLContextObj:clientContext];
        offscreenContext.view = offscreenView;
    });

    JAGGLLOG("context ready");
    return Java_jaggl_OpenGL_prepareSurface(env, owner, canvas);
}

JNIEXPORT jlong JNICALL Java_jaggl_OpenGL_prepareSurface(JNIEnv *env, jclass owner, jobject canvas) {
    Surface *surface = calloc(1, sizeof(Surface));
    surface->canvas = (*env)->NewGlobalRef(env, canvas);

    __block JagGLLayer *layer = nil;
    dispatch_sync(dispatch_get_main_queue(), ^{
        layer = [[JagGLLayer alloc] init];
    });
    surface->layer = (__bridge_retained void *) layer;

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

    currentSurface = surface;
    resizeOffscreen(surface->width, surface->height);
    return CGLSetCurrentContext(clientContext) == kCGLNoError;
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
    dispatch_sync(dispatch_get_main_queue(), ^{
        [layer removeFromSuperlayer];
    });

    (*env)->DeleteGlobalRef(env, surface->canvas);
    CFBridgingRelease(surface->layer);
    free(surface);
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_swapBuffers(JNIEnv *env, jclass owner) {
    if (currentSurface != NULL) {
        [(__bridge JagGLLayer *) currentSurface->layer blit];
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

    dispatch_sync(dispatch_get_main_queue(), ^{
        offscreenContext = nil;
        offscreenView = nil;
        [offscreenWindow orderOut:nil];
        offscreenWindow = nil;
    });
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
