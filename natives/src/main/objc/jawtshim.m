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

    [NSGraphicsContext saveGraphicsState];
    NSGraphicsContext *context = [NSGraphicsContext graphicsContextWithCGContext:self.bitmap flipped:NO];
    [NSGraphicsContext setCurrentContext:context];
    self.signatureBeforeDraw = [self bitmapSignature];
    SHIMLOG("lockFocusIfCanDraw");
    return YES;
}

- (void)unlockFocus {
    CGContextFlush(self.bitmap);
    [NSGraphicsContext restoreGraphicsState];

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

typedef struct {
    JAWT_DrawingSurface surface;
    ShimSurface *shim;
} ShimDrawingSurface;

static jint JNICALL shimLock(JAWT_DrawingSurface *surface) {
    return 0;
}

static void JNICALL shimUnlock(JAWT_DrawingSurface *surface) {
    /* empty */
}

static JAWT_DrawingSurfaceInfo *JNICALL shimGetDrawingSurfaceInfo(JAWT_DrawingSurface *surface) {
    ShimDrawingSurface *owner = (ShimDrawingSurface *) surface;
    ShimDrawingSurfaceInfo *info = calloc(1, sizeof(ShimDrawingSurfaceInfo));

    info->platform.view = (__bridge void *) owner->shim;
    info->info.platformInfo = &info->platform;
    info->info.ds = surface;
    info->info.bounds.x = 0;
    info->info.bounds.y = 0;
    info->info.bounds.width = owner->shim.width;
    info->info.bounds.height = owner->shim.height;
    info->info.clipSize = 1;
    info->info.clip = &info->info.bounds;

    SHIMLOG("getDrawingSurfaceInfo %dx%d", owner->shim.width, owner->shim.height);
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

static JAWT_DrawingSurface *JNICALL shimGetDrawingSurface(JNIEnv *env, jobject target) {
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

    ShimDrawingSurface *surface = calloc(1, sizeof(ShimDrawingSurface));
    surface->shim = liveSurface;
    surface->surface.env = env;
    surface->surface.target = (*env)->NewGlobalRef(env, target);
    surface->surface.Lock = shimLock;
    surface->surface.Unlock = shimUnlock;
    surface->surface.GetDrawingSurfaceInfo = shimGetDrawingSurfaceInfo;
    surface->surface.FreeDrawingSurfaceInfo = shimFreeDrawingSurfaceInfo;

    SHIMLOG("getDrawingSurface %dx%d", width, height);
    return &surface->surface;
}

static void JNICALL shimFreeDrawingSurface(JAWT_DrawingSurface *surface) {
    ShimDrawingSurface *owner = (ShimDrawingSurface *) surface;
    (*owner->surface.env)->DeleteGlobalRef(owner->surface.env, owner->surface.target);
    owner->shim = nil;
    free(owner);
}

/*
 * The caller sizes the JAWT structure for the version it asks for, so only the fields that version
 * defines may be written. The software toolkit puts a JAWT_VERSION_1_3 structure on its stack, and
 * writing the fields added by later versions would overwrite its locals.
 */
JNIEXPORT jboolean JNICALL JAWT_GetAWT(JNIEnv *env, JAWT *awt) {
    if (awt->version != JAWT_VERSION_1_3) {
        SHIMLOG("refusing JAWT version 0x%08x", (unsigned) awt->version);
        return JNI_FALSE;
    }

    awt->GetDrawingSurface = shimGetDrawingSurface;
    awt->FreeDrawingSurface = shimFreeDrawingSurface;
    SHIMLOG("JAWT_GetAWT 0x%08x", (unsigned) awt->version);
    return JNI_TRUE;
}
