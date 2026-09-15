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

#include <jni.h>
#include <jawt.h>

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

static JAWT_DrawingSurface *JNICALL shimGetDrawingSurface(JNIEnv *env, jobject target) {
    int width = 0;
    int height = 0;
    componentSize(env, target, &width, &height);

    if (width <= 0 || height <= 0) {
        SHIMLOG("getDrawingSurface refused, component is %dx%d", width, height);
        return NULL;
    }

    ShimDrawingSurface *surface = calloc(1, sizeof(ShimDrawingSurface));
    surface->shim = [[ShimSurface alloc] initWithWidth:width height:height];
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
