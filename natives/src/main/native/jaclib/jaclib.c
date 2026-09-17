/*
 * The native memory library the hardware toolkits allocate their vertex and index data from.
 *
 * A heap is one malloc'd block that buffers are cut from in order. Nothing is reused where it
 * stands: a freed buffer leaves a hole, and the holes are closed only when the next allocation
 * does not fit, by moving every live buffer down to close the gaps. A buffer therefore changes
 * address over its life, so its address is asked for again on every use rather than kept.
 *
 * A buffer is named by a handle rather than by its address, for the same reason. Handles come
 * from one counter shared by every heap and are looked up in a bucket table.
 */

#include <stdatomic.h>
#include <stdlib.h>
#include <string.h>
#include <sys/sysctl.h>

#include "jaclib_hardware_info_HardwareInfo.h"
#include "jaclib_memory_NativeBuffer.h"
#include "jaclib_memory_Stream.h"
#include "jaclib_memory_heap_NativeHeap.h"
#include "jaclib_peer_Peer.h"

#define BUCKETS 128

typedef struct Buffer {
    struct Buffer *next;
    struct Buffer *previous;
    struct Buffer *nextInBucket;
    struct Buffer *previousInBucket;
    jint handle;
    jint size;
    unsigned char *address;
} Buffer;

typedef struct {
    unsigned char *base;
    unsigned char *top;
    jint capacity;
    Buffer *first;
    Buffer *last;
    Buffer *buckets[BUCKETS];
} Heap;

static atomic_int allocationCount;

static void throwByName(JNIEnv *env, const char *name, const char *message) {
    jclass clazz = (*env)->FindClass(env, name);
    if (clazz != NULL) {
        (*env)->ThrowNew(env, clazz, message);
    }
}

/**
 * The heap the object's peer field names, or null where the heap has already been deallocated.
 */
static Heap *heapOf(JNIEnv *env, jobject owner) {
    jclass clazz = (*env)->GetObjectClass(env, owner);
    jfieldID field = (*env)->GetFieldID(env, clazz, "peer", "J");
    if (field == NULL) {
        return NULL;
    }
    return (Heap *) (intptr_t) (*env)->GetLongField(env, owner, field);
}

static void setHeap(JNIEnv *env, jobject owner, Heap *heap) {
    jclass clazz = (*env)->GetObjectClass(env, owner);
    jfieldID field = (*env)->GetFieldID(env, clazz, "peer", "J");
    if (field != NULL) {
        (*env)->SetLongField(env, owner, field, (jlong) (intptr_t) heap);
    }
}

static Buffer *bufferOf(Heap *heap, jint handle) {
    Buffer *buffer = heap->buckets[(unsigned int) handle % BUCKETS];
    while (buffer != NULL && buffer->handle != handle) {
        buffer = buffer->nextInBucket;
    }
    return buffer;
}

static jlong used(Heap *heap) {
    return (jlong) (heap->top - heap->base);
}

/**
 * Moves every live buffer down to close the gaps the freed ones left, in the order they were
 * allocated, so that the free space is one run at the top.
 */
static void compact(Heap *heap) {
    heap->top = heap->base;

    Buffer *buffer = heap->first;
    while (buffer != NULL) {
        if (buffer->address > heap->top) {
            memmove(heap->top, buffer->address, (size_t) buffer->size);
            buffer->address = heap->top;
        }
        heap->top += buffer->size;
        buffer = buffer->next;
    }
}

static void unlink(Heap *heap, Buffer *buffer) {
    if (buffer->previous == NULL) {
        heap->first = buffer->next;
    } else {
        buffer->previous->next = buffer->next;
    }

    if (buffer->next == NULL) {
        heap->last = buffer->previous;
    } else {
        buffer->next->previous = buffer->previous;
    }

    unsigned int bucket = (unsigned int) buffer->handle % BUCKETS;
    if (buffer->previousInBucket == NULL) {
        heap->buckets[bucket] = buffer->nextInBucket;
    } else {
        buffer->previousInBucket->nextInBucket = buffer->nextInBucket;
    }

    if (buffer->nextInBucket != NULL) {
        buffer->nextInBucket->previousInBucket = buffer->previousInBucket;
    }
}

JNIEXPORT void JNICALL Java_jaclib_memory_heap_NativeHeap_allocateHeap(JNIEnv *env, jobject owner,
                                                                       jint capacity) {
    Heap *heap = calloc(1, sizeof(Heap));
    if (heap == NULL) {
        throwByName(env, "java/lang/OutOfMemoryError", NULL);
        return;
    }

    heap->base = malloc(capacity <= 0 ? 1 : (size_t) capacity);
    if (heap->base == NULL) {
        free(heap);
        throwByName(env, "java/lang/OutOfMemoryError", NULL);
        return;
    }

    heap->top = heap->base;
    heap->capacity = capacity;
    setHeap(env, owner, heap);
}

JNIEXPORT void JNICALL Java_jaclib_memory_heap_NativeHeap_deallocateHeap(JNIEnv *env, jobject owner) {
    Heap *heap = heapOf(env, owner);
    if (heap == NULL) {
        return;
    }

    Buffer *buffer = heap->first;
    while (buffer != NULL) {
        Buffer *next = buffer->next;
        free(buffer);
        buffer = next;
    }

    free(heap->base);
    free(heap);
    setHeap(env, owner, NULL);
}

JNIEXPORT jint JNICALL Java_jaclib_memory_heap_NativeHeap_allocateBuffer(JNIEnv *env, jobject owner,
                                                                         jint size, jboolean zero) {
    Heap *heap = heapOf(env, owner);
    if (heap == NULL) {
        throwByName(env, "java/lang/RuntimeException", "System exception occurred!");
        return 0;
    }

    /*
     * A size below zero would otherwise pass the test below, because it makes the heap look
     * emptier rather than fuller, and would then be handed back as a buffer nothing can hold.
     */
    if (size < 0) {
        throwByName(env, "java/lang/OutOfMemoryError", NULL);
        return 0;
    }

    if (used(heap) + size > heap->capacity) {
        compact(heap);

        if (used(heap) + size > heap->capacity) {
            throwByName(env, "java/lang/OutOfMemoryError", NULL);
            return 0;
        }
    }

    Buffer *buffer = calloc(1, sizeof(Buffer));
    if (buffer == NULL) {
        throwByName(env, "java/lang/OutOfMemoryError", NULL);
        return 0;
    }

    buffer->handle = atomic_fetch_add(&allocationCount, 1);
    buffer->size = size;
    buffer->address = heap->top;

    buffer->previous = heap->last;
    if (heap->last == NULL) {
        heap->first = buffer;
    } else {
        heap->last->next = buffer;
    }
    heap->last = buffer;

    unsigned int bucket = (unsigned int) buffer->handle % BUCKETS;
    buffer->nextInBucket = heap->buckets[bucket];
    if (heap->buckets[bucket] != NULL) {
        heap->buckets[bucket]->previousInBucket = buffer;
    }
    heap->buckets[bucket] = buffer;

    if (zero) {
        memset(heap->top, 0, (size_t) size);
    }

    heap->top += size;
    return buffer->handle;
}

JNIEXPORT void JNICALL Java_jaclib_memory_heap_NativeHeap_deallocateBuffer(JNIEnv *env, jobject owner,
                                                                           jint handle) {
    Heap *heap = heapOf(env, owner);
    if (heap == NULL) {
        return;
    }

    Buffer *buffer = bufferOf(heap, handle);
    if (buffer != NULL) {
        unlink(heap, buffer);
        free(buffer);
    }
}

JNIEXPORT jlong JNICALL Java_jaclib_memory_heap_NativeHeap_getBufferAddress(JNIEnv *env, jobject owner,
                                                                            jint handle) {
    Heap *heap = heapOf(env, owner);
    if (heap == NULL) {
        return 0;
    }

    Buffer *buffer = bufferOf(heap, handle);
    return buffer == NULL ? 0 : (jlong) (intptr_t) buffer->address;
}

JNIEXPORT void JNICALL Java_jaclib_memory_heap_NativeHeap_put(JNIEnv *env, jobject owner, jint handle,
                                                              jbyteArray source, jint sourceOffset,
                                                              jint bufferOffset, jint length) {
    Heap *heap = heapOf(env, owner);
    if (heap == NULL) {
        return;
    }

    Buffer *buffer = bufferOf(heap, handle);
    if (buffer != NULL) {
        (*env)->GetByteArrayRegion(env, source, sourceOffset, length,
                                   (jbyte *) (buffer->address + bufferOffset));
    }
}

JNIEXPORT void JNICALL Java_jaclib_memory_heap_NativeHeap_get(JNIEnv *env, jobject owner, jint handle,
                                                              jbyteArray destination, jint destinationOffset,
                                                              jint bufferOffset, jint length) {
    Heap *heap = heapOf(env, owner);
    if (heap == NULL) {
        return;
    }

    Buffer *buffer = bufferOf(heap, handle);
    if (buffer != NULL) {
        (*env)->SetByteArrayRegion(env, destination, destinationOffset, length,
                                   (const jbyte *) (buffer->address + bufferOffset));
    }
}

JNIEXPORT void JNICALL Java_jaclib_memory_NativeBuffer_put(JNIEnv *env, jobject owner, jlong address,
                                                           jbyteArray source, jint sourceOffset,
                                                           jint bufferOffset, jint length) {
    (void) owner;
    (*env)->GetByteArrayRegion(env, source, sourceOffset, length,
                               (jbyte *) (intptr_t) (address + bufferOffset));
}

JNIEXPORT void JNICALL Java_jaclib_memory_NativeBuffer_get(JNIEnv *env, jobject owner, jlong address,
                                                           jbyteArray destination, jint destinationOffset,
                                                           jint bufferOffset, jint length) {
    (void) owner;
    (*env)->SetByteArrayRegion(env, destination, destinationOffset, length,
                               (const jbyte *) (intptr_t) (address + bufferOffset));
}

/**
 * The byte an int starts with in memory, which tells the caller which way round this machine
 * stores one.
 */
JNIEXPORT jbyte JNICALL Java_jaclib_memory_Stream_getLSB(JNIEnv *env, jclass owner, jint value) {
    (void) env;
    (void) owner;

    jbyte bytes[sizeof(jint)];
    memcpy(bytes, &value, sizeof(jint));
    return bytes[0];
}

JNIEXPORT jint JNICALL Java_jaclib_memory_Stream_floatToRawIntBits(JNIEnv *env, jclass owner,
                                                                    jfloat value) {
    (void) env;
    (void) owner;

    jint bits;
    memcpy(&bits, &value, sizeof(bits));
    return bits;
}

static jfieldID referenceField;
static jfieldID peerField;
static jmethodID setPeerMethod;

/**
 * Records how a peer reference is reached and how its handle is read and written. The reference
 * field is declared by the caller, and the other two by the class it passes.
 */
JNIEXPORT void JNICALL Java_jaclib_peer_Peer_init(JNIEnv *env, jclass owner, jclass reference) {
    referenceField = (*env)->GetFieldID(env, owner, "reference", "Ljaclib/peer/PeerReference;");
    setPeerMethod = (*env)->GetMethodID(env, reference, "setPeer", "(J)V");
    peerField = (*env)->GetFieldID(env, reference, "peer", "J");
}

/**
 * The seven fields of the hardware survey the client sends at login. Only the memory size is
 * available on every machine this runs on, and the rest describe an x86 processor, so they are
 * left at zero where there is none.
 *
 * The shipped library reports a different size here, and it is the one place its answer cannot be
 * copied, because there is no answer to copy. It asks for HW_MEMSIZE with an output buffer it
 * declares to be two bytes rather than eight, so the call fails and writes nothing, and it then
 * shifts and returns whatever the stack happened to hold. The number it gives changes on every
 * run and has no relation to how much memory the machine has. The value below is the size the
 * kernel reports. Do not make this agree with the shipped library: nothing would be agreed with.
 */
JNIEXPORT jintArray JNICALL Java_jaclib_hardware_1info_HardwareInfo_getCPUInfo(JNIEnv *env, jclass owner) {
    (void) owner;

    jint info[7] = {0, 0, 0, 0, 0, 0, 0};

    int selector[2] = {CTL_HW, HW_MEMSIZE};
    uint64_t memory = 0;
    size_t length = sizeof(memory);
    if (sysctl(selector, 2, &memory, &length, NULL, 0) == 0) {
        info[6] = (jint) (memory >> 20);
    }

    jintArray array = (*env)->NewIntArray(env, 7);
    if (array != NULL) {
        (*env)->SetIntArrayRegion(env, array, 0, 7, info);
    }
    return array;
}

/**
 * Answers nothing, which is what the shipped library answers here and is not the same as an empty
 * list. The properties these report come from DXDiag and from a Direct3D device, neither of which
 * exists on this system, and nothing in the client reads any of the three.
 */
JNIEXPORT jobjectArray JNICALL Java_jaclib_hardware_1info_HardwareInfo_getOpenGLProps(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;
    return NULL;
}

JNIEXPORT jobjectArray JNICALL Java_jaclib_hardware_1info_HardwareInfo_getDXDiagSystemProps(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;
    return NULL;
}

JNIEXPORT jobjectArray JNICALL Java_jaclib_hardware_1info_HardwareInfo_getDXDiagDisplayDevicesProps(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;
    return NULL;
}
