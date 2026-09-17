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
 * Where the client draws when it asks for more than one sample a pixel, and how many.
 *
 * The plain framebuffer above is what the layer shows and what anything reads, and it keeps that
 * job whatever the client asks for. Where the client asks for one sample a pixel it also draws
 * there and this is unused. Where it asks for more it draws here instead, and what it drew is
 * brought down into the plain one before anything shows or reads it.
 *
 * It is this way round on purpose. The layer draws in a context of its own, framebuffers are not
 * shared between contexts, and the plain one is the only framebuffer the layer has ever touched.
 * Handing it a second one to read is refused there, which is a black screen and was twice.
 */
static GLint wantedSamples;
static GLuint drawFramebuffer;
static GLuint drawColour;
static GLuint drawDepth;
static GLint offscreenWidth;
static GLint offscreenHeight;
static BOOL defaultBound;
static uint64_t shownFrames;

/** Set when the client finishes a frame, cleared when the screen takes one. */
static _Atomic bool frameWaiting;

/**
 * Raised each time the screen takes a frame, so that the client can wait for it to.
 *
 * A real swap of buffers waits for the screen before it answers, and that wait is what holds a
 * client to the rate the screen refreshes at. Nothing waited here, so the client finished frames
 * on its own clock and they fell between refreshes wherever they happened to land: shown a
 * hundred out of a hundred, but one eight thousand microseconds after the last and the next thirty
 * three thousand, when every one of them was drawn twenty thousand apart.
 */
static dispatch_semaphore_t frameTaken;

/**
 * How long the client will wait for the screen to take a frame before going on without it.
 *
 * There has to be a limit. A window nobody can see is not refreshed, so its layer is never asked
 * for a frame, and a client waiting for that with no way out would stop for good.
 */
enum { LONGEST_WAIT_IN_MILLISECONDS = 100 };

/* How evenly the screen took them, which is the last thing a count of them cannot say. */
static uint64_t lastShown;
static uint64_t shownGap;
static uint64_t shownGapLeast;
static uint64_t shownGapMost;

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

    JAGGLLOG("%llu frames every %llu us, %llu of that handing over; shown %llu of them, "
             "one every %llu us, the quickest %llu apart and the slowest %llu",
             frames, betweenSwaps / frames, swapping / frames, shownFrames,
             shownFrames > 1 ? shownGap / (shownFrames - 1) : 0, shownGapLeast, shownGapMost);

    frames = 0;
    swapping = 0;
    betweenSwaps = 0;
    shownFrames = 0;
    shownGap = 0;
    shownGapLeast = 0;
    shownGapMost = 0;
    lastReport = now;
}

/**
 * Whether the client's own buffer may be drawn with more than one sample a pixel.
 *
 * Off unless asked for. Everything behind it has been written and checked as far as a harness can
 * check it, and twice it has left the client with nothing on screen, so it waits on a run of the
 * client rather than on another harness.
 */
static BOOL askedForSamples(void) {
    static BOOL cached;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        cached = getenv("JAGGL_SAMPLES") != NULL;
    });
    return cached;
}

/**
 * Throws away whatever OpenGL is already unhappy about.
 *
 * What comes back from asking is the oldest complaint outstanding, not the newest, so anything
 * left lying about by the client is answered in place of the step being watched. Every check of a
 * step here has to start from nothing or it reports another's fault as its own, which is how the
 * showing of a frame came to be blamed for a whole picture that arrived intact.
 */
static void forget(void) {
    if (!verbose()) {
        return;
    }

    for (int left = 0; left < 32 && glGetError() != GL_NO_ERROR; left++) {
        /* empty, the point is the asking */
    }
}

/**
 * Reports what OpenGL made of the step just taken, which is only the step just taken where the
 * complaints outstanding were thrown away before it.
 */
static void complain(const char *what) {
    if (!verbose()) {
        return;
    }

    GLenum trouble = glGetError();
    if (trouble != GL_NO_ERROR) {
        JAGGLLOG("%s failed with 0x%x", what, trouble);
    }
}

@implementation JagGLLayer

- (instancetype)init {
    self = [super init];
    if (self != nil) {
        static dispatch_once_t once;
        dispatch_once(&once, ^{
            frameTaken = dispatch_semaphore_create(0);
        });

        /*
         * Core Animation asks this layer for a frame at the rate the screen refreshes, rather than
         * being told to take one at the rate the client finishes them. Those are not the same rate
         * and never will be, and telling it loses frames: two told between one refresh and the next
         * become one shown, and the other is never seen. Asked instead, it takes the newest whole
         * frame there is every time it refreshes and none is lost.
         *
         * This is only safe because the client draws into a buffer of its own. Asked for a frame at
         * any moment, what this layer reads is the last one finished rather than the one being
         * painted.
         */
        self.asynchronous = YES;
        self.opaque = YES;
        self.needsDisplayOnBoundsChange = YES;
    }
    return self;
}

/*
 * Shows the finished frame, and shows it now.
 *
 * The frame is already in the buffer this layer reads, because the client draws into one of its
 * own and a finished frame is copied across on the swap. So all that is left is to say that there
 * is one, and to see that it is taken.
 *
 * Saying it is not enough on its own. Marking the layer as wanting to be drawn leaves Core
 * Animation to choose when, and a client handing over fifty frames a second is not the rate it
 * chooses: several frames fall into one drawing and the rest are never shown, so the count stays
 * at fifty while what reaches the screen jerks. Committing a transaction around it hands the frame
 * over there and then, which is what the software toolkit's own surface does and why that one is
 * smooth.
 */
- (void)present {
    glFlush();
    atomic_store(&frameWaiting, true);

    /*
     * Waits for the screen to take it, which is what a swap of buffers does and what paces the
     * client to the screen rather than to its own clock. Giving up after a while costs one frame's
     * smoothness and is the only thing standing between a hidden window and a client that never
     * runs again.
     */
    dispatch_semaphore_wait(frameTaken, dispatch_time(DISPATCH_TIME_NOW,
                                                      LONGEST_WAIT_IN_MILLISECONDS * NSEC_PER_MSEC));
}

/*
 * Answered at every refresh of the screen. Yes only where a frame has been finished since the last
 * one was shown, so a screen that refreshes faster than the client draws does not show the same
 * frame twice over, and one that refreshes slower shows the newest rather than the oldest.
 */
- (BOOL)canDrawInCGLContext:(CGLContextObj)context
                pixelFormat:(CGLPixelFormatObj)format
               forLayerTime:(CFTimeInterval)layerTime
                displayTime:(const CVTimeStamp *)displayTime {
    return defaultFramebuffer != 0 && atomic_load(&frameWaiting);
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
        forget();
        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, defaultFramebuffer);
        glBlitFramebufferEXT(0, 0, offscreenWidth, offscreenHeight,
                             0, 0, (GLint) size.width, (GLint) size.height,
                             GL_COLOR_BUFFER_BIT, GL_NEAREST);
        complain("showing the frame");
        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, 0);
    }

    atomic_store(&frameWaiting, false);
    dispatch_semaphore_signal(frameTaken);
    shownFrames++;

    if (timing()) {
        uint64_t now = nowInMicroseconds();
        if (lastShown != 0) {
            uint64_t gap = now - lastShown;
            shownGap += gap;
            if (shownGapLeast == 0 || gap < shownGapLeast) {
                shownGapLeast = gap;
            }
            if (gap > shownGapMost) {
                shownGapMost = gap;
            }
        }
        lastShown = now;
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
 * Hangs a buffer on the renderbuffer that is bound, carrying as many samples a pixel as the client
 * asked for.
 */
static void drawnStorage(GLenum format, GLint width, GLint height) {
    if (wantedSamples > 0) {
        glRenderbufferStorageMultisampleEXT(GL_RENDERBUFFER_EXT, wantedSamples, format, width, height);
    } else {
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, format, width, height);
    }
}

/**
 * The framebuffer the client's own drawing goes to, which is never the one being shown while there
 * is one to draw into.
 */
static GLuint whereTheClientDraws(void) {
    return drawFramebuffer != 0 ? drawFramebuffer : defaultFramebuffer;
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

    if (drawFramebuffer != 0) {
        glDeleteRenderbuffersEXT(1, &drawDepth);
        glDeleteRenderbuffersEXT(1, &drawColour);
        glDeleteFramebuffersEXT(1, &drawFramebuffer);
        drawFramebuffer = 0;
        drawColour = 0;
        drawDepth = 0;
    }

    glGenFramebuffersEXT(1, &defaultFramebuffer);
    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, defaultFramebuffer);

    glGenRenderbuffersEXT(1, &defaultColour);
    glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, defaultColour);
    glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGBA8, width, height);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_RENDERBUFFER_EXT, defaultColour);

    glGenRenderbuffersEXT(1, &defaultDepth);
    glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, defaultDepth);
    glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH24_STENCIL8_EXT, width, height);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, defaultDepth);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_STENCIL_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, defaultDepth);

    GLenum status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
    if (status != GL_FRAMEBUFFER_COMPLETE_EXT) {
        JAGGLLOG("the default framebuffer is not complete at %dx%d, status 0x%x", width, height, status);
        return NO;
    }

    glDrawBuffer(GL_COLOR_ATTACHMENT0_EXT);
    glReadBuffer(GL_COLOR_ATTACHMENT0_EXT);

    glGenFramebuffersEXT(1, &drawFramebuffer);
    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, drawFramebuffer);

    glGenRenderbuffersEXT(1, &drawColour);
    glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, drawColour);
    drawnStorage(GL_RGBA8, width, height);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
                                 GL_RENDERBUFFER_EXT, drawColour);

    glGenRenderbuffersEXT(1, &drawDepth);
    glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, drawDepth);
    drawnStorage(GL_DEPTH24_STENCIL8_EXT, width, height);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT,
                                 GL_RENDERBUFFER_EXT, drawDepth);
    glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_STENCIL_ATTACHMENT_EXT,
                                 GL_RENDERBUFFER_EXT, drawDepth);

    forget();
    GLenum drawn = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
    JAGGLLOG("shown buffer %u, drawn buffer %u at %d samples, status 0x%x",
             defaultFramebuffer, drawFramebuffer, wantedSamples, drawn);
    complain("making the buffer the client draws into");

    if (drawn != GL_FRAMEBUFFER_COMPLETE_EXT) {
        JAGGLLOG("no buffer of %d samples to draw into", wantedSamples);
        glDeleteRenderbuffersEXT(1, &drawDepth);
        glDeleteRenderbuffersEXT(1, &drawColour);
        glDeleteFramebuffersEXT(1, &drawFramebuffer);
        drawFramebuffer = 0;
        drawColour = 0;
        drawDepth = 0;
    }

    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, whereTheClientDraws());
    glDrawBuffer(GL_COLOR_ATTACHMENT0_EXT);
    glReadBuffer(GL_COLOR_ATTACHMENT0_EXT);

    defaultBound = YES;
    offscreenWidth = width;
    offscreenHeight = height;
    JAGGLLOG("drawable %dx%d", width, height);
    return YES;
}

/**
 * Brings a piece of what the client drew down into the plain buffer, where it can be read.
 *
 * Nothing may be read out of a buffer of many samples a pixel, so where the client has been drawing
 * into one the piece wanted is brought down first. Only the piece, never the whole picture: the
 * client reads its buffer back one row at a time, and bringing the whole down for each row would
 * cost the picture over for every row of it.
 *
 * Where the client draws into the plain buffer already there is nothing to do, because that is the
 * buffer everything reads.
 */
static void bringDown(const char *who, GLint x, GLint y, GLint width, GLint height) {
    if (drawFramebuffer == 0 || !defaultBound || wantedSamples <= 0) {
        return;
    }

    /*
     * Held to what the buffers actually cover. A piece reaching past the edge is not refused by a
     * blit, but a piece that is wholly outside leaves nothing to copy and asking for it is how one
     * of these came to be refused.
     */
    GLint left = x < 0 ? 0 : x;
    GLint bottom = y < 0 ? 0 : y;
    GLint right = x + width > offscreenWidth ? offscreenWidth : x + width;
    GLint top = y + height > offscreenHeight ? offscreenHeight : y + height;

    if (right <= left || top <= bottom) {
        return;
    }

    forget();

    glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, drawFramebuffer);
    glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, defaultFramebuffer);
    glBlitFramebufferEXT(left, bottom, right, top, left, bottom, right, top,
                         GL_COLOR_BUFFER_BIT, GL_NEAREST);

    if (verbose()) {
        GLenum trouble = glGetError();
        if (trouble != GL_NO_ERROR) {
            JAGGLLOG("%s: bringing %d,%d to %d,%d down failed with 0x%x, drawable %dx%d",
                     who, left, bottom, right, top, trouble, offscreenWidth, offscreenHeight);
        }
    }

    glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, defaultFramebuffer);
    glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, drawFramebuffer);
}

/**
 * Puts the read back where the client left it, which is wherever it draws.
 */
static void doneReading(void) {
    if (drawFramebuffer != 0 && defaultBound) {
        glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, drawFramebuffer);
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
    bringDown("reading pixels", x, y, width, height);
    jint *address = pixels == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, pixels, NULL);
    glReadPixels(x, y, width, height, (GLenum) format, (GLenum) type,
                 address == NULL ? NULL : (void *) (address + offset));
    if (address != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, pixels, address, 0);
    }
    doneReading();
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glReadPixelsub(JNIEnv *env, jclass owner, jint x, jint y,
                                                         jint width, jint height, jint format,
                                                         jint type, jbyteArray pixels, jint offset) {
    bringDown("reading pixels as bytes", x, y, width, height);
    jbyte *address = pixels == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, pixels, NULL);
    glReadPixels(x, y, width, height, (GLenum) format, (GLenum) type,
                 address == NULL ? NULL : (void *) (address + offset));
    if (address != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, pixels, address, 0);
    }
    doneReading();
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glCopyTexImage2D(JNIEnv *env, jclass owner, jint target,
                                                            jint level, jint format, jint x, jint y,
                                                            jint width, jint height, jint border) {
    bringDown("copying into a texture", x, y, width, height);
    glCopyTexImage2D((GLenum) target, level, (GLenum) format, x, y, width, height, border);
    doneReading();
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_glCopyTexSubImage2D(JNIEnv *env, jclass owner, jint target,
                                                               jint level, jint intoX, jint intoY,
                                                               jint x, jint y, jint width,
                                                               jint height) {
    bringDown("copying into part of a texture", x, y, width, height);
    glCopyTexSubImage2D((GLenum) target, level, intoX, intoY, x, y, width, height);
    doneReading();
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
    bringDown("the client's own blit", left, bottom,
              fromX1 > fromX0 ? fromX1 - fromX0 : fromX0 - fromX1,
              fromY1 > fromY0 ? fromY1 - fromY0 : fromY0 - fromY1);
    glBlitFramebufferEXT(fromX0, fromY0, fromX1, fromY1, toX0, toY0, toX1, toY1,
                         (GLbitfield) mask, (GLenum) filter);
    doneReading();
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
    attributes[n] = (CGLPixelFormatAttribute) 0;

    /*
     * The samples the client asks for are not asked of the format. The format decides what the
     * window's own drawable carries, and the client never draws into that: it draws into a buffer
     * of this library's, and the samples belong on the buffers hung there instead.
     *
     * Asking for them here as well is worse than useless. The layer shows a frame by blitting into
     * the window's drawable, that blit scales where the layer is not the size the client drew at,
     * and a blit that scales is refused where either side carries more than one sample a pixel. It
     * was asked for here once and every frame came back as an invalid framebuffer operation.
     */

    /*
     * Drawing with more than one sample a pixel is not switched on yet. It works as far as anything
     * here can be made to check it, and it put the client's screen out twice, both times in the
     * showing of a frame rather than the drawing of one, which nothing here drives. Until a run of
     * the client says otherwise it is off, and JAGGL_SAMPLES asks for it.
     */
    wantedSamples = askedForSamples() ? samples : 0;
    JAGGLLOG("asked for %d samples a pixel, taking %d", samples, wantedSamples);

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

/**
 * Copies the finished frame into the buffer the layer shows.
 *
 * The client draws into one buffer and the layer shows another, and a frame crosses from one to
 * the other here and nowhere else. Before this the client drew straight into the buffer being
 * shown, so Core Animation could read a frame that was still being painted, and what reached the
 * screen stuttered however many frames were finished.
 *
 * The copy is made before the layer is told there is anything to show, and showing flushes, so
 * the frame the layer reads is a whole one.
 */
static void copyForShowing(void) {
    if (drawFramebuffer == 0 || offscreenWidth <= 0 || offscreenHeight <= 0) {
        return;
    }

    forget();

    glBindFramebufferEXT(GL_READ_FRAMEBUFFER_EXT, drawFramebuffer);
    glBindFramebufferEXT(GL_DRAW_FRAMEBUFFER_EXT, defaultFramebuffer);
    glBlitFramebufferEXT(0, 0, offscreenWidth, offscreenHeight,
                         0, 0, offscreenWidth, offscreenHeight,
                         GL_COLOR_BUFFER_BIT, GL_NEAREST);
    complain("copying the finished frame across");

    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, whereTheClientDraws());
}

JNIEXPORT void JNICALL Java_jaggl_OpenGL_swapBuffers(JNIEnv *env, jclass owner) {
    uint64_t began = timing() ? nowInMicroseconds() : 0;

    copyForShowing();

    if (currentSurface != NULL) {
        [(__bridge JagGLLayer *) currentSurface->layer present];
    }

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
    drawFramebuffer = 0;
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
    GLuint wanted = framebuffer == 0 ? whereTheClientDraws() : (GLuint) framebuffer;
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
