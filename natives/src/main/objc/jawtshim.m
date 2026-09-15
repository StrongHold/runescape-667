/*
 * A drawing surface for the software toolkit on modern macOS.
 *
 * The toolkit asks JavaVM.framework for a JAWT_VERSION_1_3 drawing surface. That framework now
 * serves only JAWT_VERSION_1_7, so the request fails and the toolkit can never obtain a surface.
 * This library answers that request instead. The toolkit imports JAWT_GetAWT and nothing else from
 * the framework, so repointing that one import is enough to reach this code.
 *
 * The toolkit uses very little of the surface. It reads the view out of the platform info, sends it
 * lockFocusIfCanDraw, draws through the current NSGraphicsContext, and sends unlockFocus. Nothing
 * here needs to be a real NSView, so the surface hands back an object that answers those two
 * messages over a bitmap context this library owns.
 */

#import <Cocoa/Cocoa.h>
#import <ImageIO/ImageIO.h>
#import <QuartzCore/QuartzCore.h>
#import <objc/runtime.h>

#include <dlfcn.h>
#include <limits.h>
#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

typedef jboolean (*JawtGetAwt)(JNIEnv *, JAWT *);

/* Newest first. Which of these the JDK serves has changed between releases, so ask for each. */
static const jint PRESENTATION_VERSIONS[] = {
    JAWT_VERSION_9 | JAWT_MACOSX_USE_CALAYER,
    JAWT_VERSION_1_7 | JAWT_MACOSX_USE_CALAYER,
    JAWT_VERSION_1_4 | JAWT_MACOSX_USE_CALAYER,
    JAWT_VERSION_9,
    JAWT_VERSION_1_7,
};

/*
 * The JDK still ships a working JAWT, it just refuses the old version the toolkit asks for. The
 * shim answers the toolkit itself and uses the JDK's own JAWT for presentation.
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

static BOOL verbose(void) {
    static BOOL cached;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        cached = getenv("JAWTSHIM_VERBOSE") != NULL;
    });
    return cached;
}

/*
 * Reports what the surface is doing. Arguments must be free of side effects, because they are only
 * evaluated when reporting is switched on.
 */
#define SHIMLOG(...) do { if (verbose()) { fprintf(stderr, "[jawtshim] " __VA_ARGS__); fputc('\n', stderr); } } while (0)

/*
 * The object the toolkit treats as its view. It answers the two messages the toolkit sends and
 * makes its own bitmap the current drawing destination in between.
 */
@interface ShimSurface : NSObject

@property (nonatomic, assign) CGContextRef bitmap;
@property (nonatomic, assign) int width;
@property (nonatomic, assign) int height;
@property (nonatomic, assign) unsigned long signatureBeforeDraw;
@property (nonatomic, strong) CALayer *presentationLayer;
@property (nonatomic, assign) BOOL presentationUnavailable;
@property (nonatomic, assign) int framesPresented;
@property (nonatomic, strong) NSGraphicsContext *drawingContext;

- (void)attachLayer:(JNIEnv *)env target:(jobject)target;

@end

@implementation ShimSurface

- (instancetype)initWithWidth:(int)width height:(int)height {
    self = [super init];
    if (self != nil) {
        _width = width;
        _height = height;

        CGColorSpaceRef space = CGColorSpaceCreateDeviceRGB();
        _bitmap = CGBitmapContextCreate(NULL, width, height, 8, (size_t) width * BYTES_PER_PIXEL, space,
                                        kCGImageAlphaNoneSkipFirst | kCGBitmapByteOrder32Host);
        CGColorSpaceRelease(space);

        /*
         * The toolkit draws top down and concatenates a vertical flip to suit a context whose
         * origin is at the bottom. The bitmap is read back as an image whose first row is its top,
         * so the context starts flipped and the toolkit's own flip cancels it. Without this the
         * picture is upside down.
         */
        CGContextTranslateCTM(_bitmap, 0, height);
        CGContextScaleCTM(_bitmap, 1, -1);

        SHIMLOG("surface %dx%d bitmap=%p", width, height, _bitmap);
    }
    return self;
}

- (void)dealloc {
    CGContextRelease(_bitmap);
}

/* A cheap signature of the bitmap, enough to tell whether the toolkit wrote anything into it. */
- (unsigned long)bitmapSignature {
    const unsigned char *pixels = CGBitmapContextGetData(self.bitmap);
    size_t length = CGBitmapContextGetBytesPerRow(self.bitmap) * CGBitmapContextGetHeight(self.bitmap);
    unsigned long signature = 1469598103934665603UL;

    for (size_t i = 0; i < length; i++) {
        signature = (signature ^ pixels[i]) * 1099511628211UL;
    }
    return signature;
}

- (BOOL)lockFocusIfCanDraw {
    if (self.bitmap == NULL) {
        return NO;
    }

    if (self.drawingContext == nil) {
        self.drawingContext = [NSGraphicsContext graphicsContextWithCGContext:self.bitmap flipped:NO];
    }

    /*
     * The toolkit concatenates a vertical flip onto the transform of whatever context it is given,
     * so the bitmap has to start every frame in the state it started the last one in. Without this
     * the flips accumulate and the picture turns over on alternate frames.
     */
    CGContextSaveGState(self.bitmap);

    [NSGraphicsContext saveGraphicsState];
    [NSGraphicsContext setCurrentContext:self.drawingContext];
    self.signatureBeforeDraw = [self bitmapSignature];
    SHIMLOG("lockFocusIfCanDraw");
    return YES;
}

- (void)unlockFocus {
    CGContextFlush(self.bitmap);
    [NSGraphicsContext restoreGraphicsState];
    CGContextRestoreGState(self.bitmap);

    unsigned long after = [self bitmapSignature];
    SHIMLOG("unlockFocus, pixels %s", after == self.signatureBeforeDraw ? "UNCHANGED" : "WRITTEN");
    [self present];
}

/*
 * Attaching the layer needs JNI, so it runs on the calling thread. Only the layer itself is handed
 * to the main thread, while the surface info that produced it is still alive.
 */
- (void)attachLayer:(JNIEnv *)env target:(jobject)target {
    if (self.presentationLayer != nil || self.presentationUnavailable) {
        return;
    }
    self.presentationUnavailable = YES;

    JawtGetAwt getAwt = jdkJawtGetAwt(env);
    if (getAwt == NULL) {
        SHIMLOG("no JDK JAWT, nothing will reach the screen");
        return;
    }

    JAWT awt;
    BOOL acquired = NO;
    for (size_t i = 0; i < sizeof(PRESENTATION_VERSIONS) / sizeof(jint); i++) {
        if (!acquired) {
            awt.version = PRESENTATION_VERSIONS[i];
            acquired = getAwt(env, &awt) == JNI_TRUE;
            SHIMLOG("JDK JAWT version 0x%08x %s", (unsigned) awt.version, acquired ? "accepted" : "refused");
        }
    }

    if (!acquired) {
        return;
    }

    JAWT_DrawingSurface *surface = awt.GetDrawingSurface(env, target);
    if (surface == NULL) {
        SHIMLOG("JDK JAWT gave no surface");
        return;
    }

    if ((surface->Lock(surface) & JAWT_LOCK_ERROR) == 0) {
        JAWT_DrawingSurfaceInfo *info = surface->GetDrawingSurfaceInfo(surface);
        if (info != NULL) {
            CALayer *layer = [CALayer layer];
            layer.contentsGravity = kCAGravityResize;
            layer.magnificationFilter = kCAFilterNearest;
            layer.anchorPoint = CGPointZero;
            layer.frame = CGRectMake(0, 0, self.width, self.height);
            layer.autoresizingMask = kCALayerWidthSizable | kCALayerHeightSizable;

            id<JAWT_SurfaceLayers> layers = (__bridge id<JAWT_SurfaceLayers>) info->platformInfo;
            dispatch_sync(dispatch_get_main_queue(), ^{
                layers.layer = layer;
            });

            self.presentationLayer = layer;
            self.presentationUnavailable = NO;
            SHIMLOG("attached layer %p", layer);
            surface->FreeDrawingSurfaceInfo(info);
        }
        surface->Unlock(surface);
    }

    awt.FreeDrawingSurface(surface);
}

/*
 * Writes each presented frame to the directory named by JAWTSHIM_DUMP. Useful for comparing what
 * the toolkit rasterises against another implementation of it.
 */
- (void)dumpFrame:(CGImageRef)frame {
    const char *directory = getenv("JAWTSHIM_DUMP");
    if (directory == NULL) {
        return;
    }

    NSString *path = [NSString stringWithFormat:@"%s/frame-%04d.png", directory, self.framesPresented];
    NSURL *url = [NSURL fileURLWithPath:path];
    CGImageDestinationRef destination = CGImageDestinationCreateWithURL((__bridge CFURLRef) url,
                                                                        CFSTR("public.png"), 1, NULL);
    if (destination != NULL) {
        CGImageDestinationAddImage(destination, frame, NULL);
        CGImageDestinationFinalize(destination);
        CFRelease(destination);
        SHIMLOG("wrote %s", path.UTF8String);
    }
}

/*
 * Draws a known pattern straight into the bitmap when JAWTSHIM_TESTPATTERN is set. It separates a
 * fault in this library from an empty frame produced by the toolkit.
 */
- (void)drawTestPattern {
    if (getenv("JAWTSHIM_TESTPATTERN") == NULL) {
        return;
    }

    CGContextSetRGBFillColor(self.bitmap, 0.9, 0.1, 0.1, 1.0);
    CGContextFillRect(self.bitmap, CGRectMake(0, 0, self.width, self.height));
    CGContextSetRGBFillColor(self.bitmap, 0.1, 0.9, 0.2, 1.0);
    CGContextFillRect(self.bitmap, CGRectMake(self.width / 4, self.height / 4, self.width / 2, self.height / 2));
}

- (void)present {
    @autoreleasepool {
        [self presentInPool];
    }
}

- (void)presentInPool {
    [self drawTestPattern];
    CGImageRef frame = CGBitmapContextCreateImage(self.bitmap);
    if (frame == NULL) {
        return;
    }

    [self dumpFrame:frame];
    CALayer *layer = self.presentationLayer;
    if (layer == nil) {
        CGImageRelease(frame);
        self.framesPresented++;
        return;
    }

    self.framesPresented++;

    id contents = (__bridge_transfer id) frame;
    dispatch_async(dispatch_get_main_queue(), ^{
        [CATransaction begin];
        [CATransaction setDisableActions:YES];
        layer.contents = contents;
        [CATransaction commit];
    });
}

@end

/*
 * The hardware toolkit works its OpenGL context from the thread it draws on. AppKit requires the
 * main thread for the calls that touch the context's drawable and stops the process when they come
 * from anywhere else, so those are passed to the main thread on the toolkit's behalf.
 *
 * This replaces a method on a system class, which is worth doing only because the alternative is
 * changing which thread the client builds its toolkit on. It is put in place when the hardware
 * toolkit asks for a surface and not before, and it changes where the call runs rather than what it
 * does.
 */
static IMP contextSetView;
static IMP contextUpdate;
static IMP contextClearDrawable;

static void onMainThread(void (^work)(void)) {
    if ([NSThread isMainThread]) {
        work();
    } else {
        dispatch_sync(dispatch_get_main_queue(), work);
    }
}

static void mainThreadSetView(id context, SEL selector, id view) {
    onMainThread(^{
        ((void (*)(id, SEL, id)) contextSetView)(context, selector, view);
    });
}

static void mainThreadUpdate(id context, SEL selector) {
    onMainThread(^{
        ((void (*)(id, SEL)) contextUpdate)(context, selector);
    });
}

static void mainThreadClearDrawable(id context, SEL selector) {
    onMainThread(^{
        ((void (*)(id, SEL)) contextClearDrawable)(context, selector);
    });
}

static BOOL replace(Class owner, SEL selector, IMP replacement, IMP *original) {
    Method method = class_getInstanceMethod(owner, selector);
    if (method == NULL) {
        return NO;
    }

    *original = method_getImplementation(method);
    method_setImplementation(method, replacement);
    return YES;
}

/*
 * Only the calls that touch the drawable are moved. makeCurrentContext and flushBuffer are left
 * where they are on purpose: they act on the thread that calls them, so running them on the main
 * thread would make the context current on the wrong one.
 */
static void passDrawableCallsToTheMainThread(void) {
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        Class context = NSClassFromString(@"NSOpenGLContext");
        BOOL view = replace(context, @selector(setView:), (IMP) mainThreadSetView, &contextSetView);
        BOOL update = replace(context, @selector(update), (IMP) mainThreadUpdate, &contextUpdate);
        BOOL clear = replace(context, @selector(clearDrawable), (IMP) mainThreadClearDrawable, &contextClearDrawable);
        SHIMLOG("moved to the main thread: setView: %d, update %d, clearDrawable %d", view, update, clear);
    });
}

/*
 * The hardware toolkit wants a real NSView, because it hands whatever it finds to
 * [NSOpenGLContext setView:]. A Canvas has no view of its own, so one is made and placed in the
 * window's own view.
 *
 * The window is found by identity rather than by name or order: the layer the JDK reports as the
 * surface's window layer is the same layer as the content view's, so the window whose content view
 * carries that layer is the right one even when the client has several.
 */
static NSView *hostView(JNIEnv *env, jobject target) {
    JawtGetAwt getAwt = jdkJawtGetAwt(env);
    if (getAwt == NULL) {
        return nil;
    }

    JAWT awt;
    awt.version = JAWT_VERSION_1_4 | JAWT_MACOSX_USE_CALAYER;
    if (!getAwt(env, &awt)) {
        SHIMLOG("JDK JAWT refused a layer surface for the hardware toolkit");
        return nil;
    }

    __block NSView *found = nil;
    JAWT_DrawingSurface *surface = awt.GetDrawingSurface(env, target);
    if (surface == NULL) {
        return nil;
    }

    if ((surface->Lock(surface) & JAWT_LOCK_ERROR) == 0) {
        JAWT_DrawingSurfaceInfo *info = surface->GetDrawingSurfaceInfo(surface);
        if (info != NULL) {
            id<JAWT_SurfaceLayers> layers = (__bridge id<JAWT_SurfaceLayers>) info->platformInfo;
            CALayer *windowLayer = layers.windowLayer;

            dispatch_sync(dispatch_get_main_queue(), ^{
                for (NSWindow *window in [NSApp windows]) {
                    NSView *content = [window contentView];
                    if (found == nil && content != nil && content.layer == windowLayer) {
                        found = content;
                    }
                }
            });

            surface->FreeDrawingSurfaceInfo(info);
        }
        surface->Unlock(surface);
    }
    awt.FreeDrawingSurface(surface);

    SHIMLOG("host view = %s", found == nil ? "(not found)" : class_getName([found class]));
    return found;
}

/*
 * The view handed to the hardware toolkit. It is kept for as long as the client runs, because the
 * toolkit holds the context that draws into it.
 */
static NSView *liveGlView;

static NSView *glView(JNIEnv *env, jobject target) {
    if (liveGlView != nil) {
        return liveGlView;
    }

    NSView *host = hostView(env, target);
    if (host == nil) {
        return nil;
    }

    dispatch_sync(dispatch_get_main_queue(), ^{
        liveGlView = [[NSView alloc] initWithFrame:host.bounds];
        liveGlView.autoresizingMask = NSViewWidthSizable | NSViewHeightSizable;
        [host addSubview:liveGlView];
    });

    SHIMLOG("made a %.0fx%.0f view for the hardware toolkit", host.bounds.size.width, host.bounds.size.height);
    return liveGlView;
}

/*
 * JAWT_MacOSXDrawingSurfaceInfo as the toolkit expects it: the view is the first field, and the
 * toolkit reads it through two levels of indirection from the drawing surface info.
 */
typedef struct {
    void *view;
} ShimPlatformInfo;

typedef struct {
    JAWT_DrawingSurfaceInfo info;
    ShimPlatformInfo platform;
} ShimDrawingSurfaceInfo;

/*
 * The surface is owned by liveSurface, so this holds an unowned pointer. ARC cannot manage an
 * object reference inside memory it did not allocate.
 *
 * The JavaVM is kept rather than the JNIEnv. A JNIEnv belongs to one thread, and the toolkit
 * destroys its canvas from the finalizer thread, not the thread that created it.
 */
typedef struct {
    JAWT_DrawingSurface surface;
    void *view;
    void *shim;
    JavaVM *vm;
} ShimDrawingSurface;

static jint JNICALL shimLock(JAWT_DrawingSurface *surface) {
    return 0;
}

static void JNICALL shimUnlock(JAWT_DrawingSurface *surface) {
    /* empty */
}

static JAWT_DrawingSurfaceInfo *JNICALL shimGetDrawingSurfaceInfo(JAWT_DrawingSurface *surface) {
    ShimDrawingSurface *owner = (ShimDrawingSurface *) surface;
    ShimSurface *shim = (__bridge ShimSurface *) owner->shim;
    ShimDrawingSurfaceInfo *info = calloc(1, sizeof(ShimDrawingSurfaceInfo));

    NSSize size = shim != nil
        ? NSMakeSize(shim.width, shim.height)
        : ((__bridge NSView *) owner->view).bounds.size;

    info->platform.view = owner->view;
    info->info.platformInfo = &info->platform;
    info->info.ds = surface;
    info->info.bounds.x = 0;
    info->info.bounds.y = 0;
    info->info.bounds.width = (jint) size.width;
    info->info.bounds.height = (jint) size.height;
    info->info.clipSize = 1;
    info->info.clip = &info->info.bounds;

    SHIMLOG("getDrawingSurfaceInfo %.0fx%.0f", size.width, size.height);
    return &info->info;
}

static void JNICALL shimFreeDrawingSurfaceInfo(JAWT_DrawingSurfaceInfo *info) {
    free(info);
}

static void componentSize(JNIEnv *env, jobject target, int *width, int *height) {
    jclass clazz = (*env)->GetObjectClass(env, target);
    jmethodID getWidth = (*env)->GetMethodID(env, clazz, "getWidth", "()I");
    jmethodID getHeight = (*env)->GetMethodID(env, clazz, "getHeight", "()I");
    *width = (*env)->CallIntMethod(env, target, getWidth);
    *height = (*env)->CallIntMethod(env, target, getHeight);
}

/*
 * The toolkit asks for a surface on every flip. The bitmap and the presentation layer outlive a
 * single flip, so they are kept and reused until the component changes size.
 */
static ShimSurface *liveSurface;

static ShimDrawingSurface *newSurface(JNIEnv *env, jobject target) {
    ShimDrawingSurface *surface = calloc(1, sizeof(ShimDrawingSurface));
    (*env)->GetJavaVM(env, &surface->vm);
    surface->surface.env = env;
    surface->surface.target = (*env)->NewGlobalRef(env, target);
    surface->surface.Lock = shimLock;
    surface->surface.Unlock = shimUnlock;
    surface->surface.GetDrawingSurfaceInfo = shimGetDrawingSurfaceInfo;
    surface->surface.FreeDrawingSurfaceInfo = shimFreeDrawingSurfaceInfo;
    return surface;
}

/*
 * The software toolkit draws into a bitmap this library owns, so what it is handed only has to
 * answer the two messages it sends.
 */
static JAWT_DrawingSurface *JNICALL softwareSurface(JNIEnv *env, jobject target) {
    int width = 0;
    int height = 0;
    componentSize(env, target, &width, &height);

    if (width <= 0 || height <= 0) {
        SHIMLOG("getDrawingSurface refused, component is %dx%d", width, height);
        return NULL;
    }

    if (liveSurface == nil || liveSurface.width != width || liveSurface.height != height) {
        liveSurface = [[ShimSurface alloc] initWithWidth:width height:height];
    }
    [liveSurface attachLayer:env target:target];

    ShimDrawingSurface *surface = newSurface(env, target);
    surface->shim = (__bridge void *) liveSurface;
    surface->view = surface->shim;

    SHIMLOG("software surface %dx%d", width, height);
    return &surface->surface;
}

/*
 * The hardware toolkit attaches an OpenGL context to what it is handed, so it gets a real view.
 */
static JAWT_DrawingSurface *JNICALL hardwareSurface(JNIEnv *env, jobject target) {
    passDrawableCallsToTheMainThread();

    NSView *view = glView(env, target);
    if (view == nil) {
        SHIMLOG("no view for the hardware toolkit");
        return NULL;
    }

    ShimDrawingSurface *surface = newSurface(env, target);
    surface->view = (__bridge void *) view;

    SHIMLOG("hardware surface");
    return &surface->surface;
}

/*
 * The toolkit destroys its canvas from the finalizer thread, so the JNIEnv captured when the
 * surface was created belongs to a different thread and must not be used. The reference is dropped
 * only if this thread has an environment of its own, and leaked otherwise, because one stale
 * reference costs far less than using the wrong environment.
 */
static void JNICALL shimFreeDrawingSurface(JAWT_DrawingSurface *surface) {
    ShimDrawingSurface *owner = (ShimDrawingSurface *) surface;
    JNIEnv *env = NULL;

    if (owner->vm != NULL && (*owner->vm)->GetEnv(owner->vm, (void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        (*env)->DeleteGlobalRef(env, owner->surface.target);
    } else {
        SHIMLOG("freeDrawingSurface on a thread with no environment, reference kept");
    }

    free(owner);
}

/*
 * The caller sizes the JAWT structure for the version it asks for, so only the fields that version
 * defines may be written. The software toolkit puts a JAWT_VERSION_1_3 structure on its stack, and
 * writing the fields added by later versions would overwrite its locals.
 */
static void JNICALL shimAwtLock(JNIEnv *env) {
    /* empty */
}

static void JNICALL shimAwtUnlock(JNIEnv *env) {
    /* empty */
}

JNIEXPORT jboolean JNICALL JAWT_GetAWT(JNIEnv *env, JAWT *awt) {
    if (awt->version == JAWT_VERSION_1_3) {
        awt->GetDrawingSurface = softwareSurface;
        awt->FreeDrawingSurface = shimFreeDrawingSurface;
    } else if (awt->version == JAWT_VERSION_1_4) {
        awt->GetDrawingSurface = hardwareSurface;
        awt->FreeDrawingSurface = shimFreeDrawingSurface;
        awt->Lock = shimAwtLock;
        awt->Unlock = shimAwtUnlock;
        awt->GetComponent = NULL;
    } else {
        SHIMLOG("refusing JAWT version 0x%08x", (unsigned) awt->version);
        return JNI_FALSE;
    }

    SHIMLOG("JAWT_GetAWT 0x%08x", (unsigned) awt->version);
    return JNI_TRUE;
}
