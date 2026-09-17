/*
 * The odds and ends library: a clock, the size of memory, and a ping.
 *
 * Nothing in here belongs with anything else in here. The client wants a monotonic clock to pace
 * its ticks against, the size of physical memory to report in the debug console, and the round
 * trip to a world so that the world list can be ordered by it.
 *
 * It wants all three loosely. Every call is made inside a catch of Throwable, a tick scheduler
 * built on System.nanoTime takes over when the clock cannot be had, and the world list falls back
 * to a flat thousand for a ping that does not come. The game's own file store bears this out: it
 * holds jagmisc for Windows and for nothing else, so the client has always run this way on macOS
 * and on Linux.
 *
 * That means there is no shipped library here to measure against. What the checks can do instead
 * is hold each answer against one the machine can be asked for by another route, which is what
 * `Jagmisc` in the harness does.
 *
 * The clock and the ping are POSIX and should carry to Linux unchanged. The two memory sizes are
 * the part that will need writing again, once per platform.
 */

#include <mach/mach.h>
#include <mach/vm_page_size.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netinet/ip_icmp.h>
#include <stdatomic.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/sysctl.h>
#include <time.h>
#include <unistd.h>

#include "jagex3_jagmisc_jagmisc.h"

enum {
    NANOSECONDS_PER_SECOND = 1000000000,
    NANOSECONDS_PER_MILLISECOND = 1000000,
    MILLISECONDS_PER_SECOND = 1000,
};

/**
 * The monotonic clock, in nanoseconds, or zero where the machine has no such clock.
 *
 * Zero is how a caller is told there is none. The client builds its tick scheduler on the first
 * reading and throws when that reading is zero, which is what puts it back on System.nanoTime.
 * A working clock counts from the last boot and will not be zero again this side of one.
 */
static jlong monotonicNanos(void) {
    struct timespec now;
    if (clock_gettime(CLOCK_MONOTONIC, &now) != 0) {
        return 0;
    }
    return (jlong) now.tv_sec * NANOSECONDS_PER_SECOND + now.tv_nsec;
}

JNIEXPORT jlong JNICALL Java_jagex3_jagmisc_jagmisc_nanoTime(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;

    return monotonicNanos();
}

/**
 * Whether the library can answer at all. The client throws away what this says, but a machine
 * with no monotonic clock has nothing to offer and is better off saying so once than failing
 * every call.
 */
JNIEXPORT jboolean JNICALL Java_jagex3_jagmisc_jagmisc_init(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;

    return monotonicNanos() == 0 ? JNI_FALSE : JNI_TRUE;
}

/**
 * Releases what the library holds, which is nothing. A socket lives no longer than the ping that
 * opened it and the clock is the machine's, so there is nothing left over to give back.
 */
JNIEXPORT void JNICALL Java_jagex3_jagmisc_jagmisc_Quit0(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;
    /* empty */
}

JNIEXPORT jlong JNICALL Java_jagex3_jagmisc_jagmisc_getTotalPhysicalMemory(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;

    int selector[2] = {CTL_HW, HW_MEMSIZE};
    uint64_t memory = 0;
    size_t length = sizeof(memory);
    if (sysctl(selector, 2, &memory, &length, NULL, 0) != 0) {
        return 0;
    }
    return (jlong) memory;
}

/**
 * How much of physical memory could be given to something that asked for it now.
 *
 * Free pages alone are not the answer. This system keeps whatever it has read from disk in pages
 * it marks inactive and hands them back the moment anything wants them, so a machine that has
 * been up an hour reports almost nothing free while most of memory is in fact available. Pages
 * marked purgeable are counted for the same reason: their owner has already said they may be
 * taken away.
 */
JNIEXPORT jlong JNICALL Java_jagex3_jagmisc_jagmisc_getAvailablePhysicalMemory(JNIEnv *env, jclass owner) {
    (void) env;
    (void) owner;

    mach_port_t host = mach_host_self();
    vm_statistics64_data_t statistics;
    mach_msg_type_number_t count = HOST_VM_INFO64_COUNT;
    kern_return_t answered = host_statistics64(host, HOST_VM_INFO64, (host_info64_t) &statistics, &count);
    mach_port_deallocate(mach_task_self(), host);

    if (answered != KERN_SUCCESS) {
        return 0;
    }

    uint64_t pages = (uint64_t) statistics.free_count
            + statistics.inactive_count
            + statistics.purgeable_count;
    return (jlong) (pages * vm_kernel_page_size);
}

/*
 * What a ping answers when it does not answer a time.
 *
 * All the client reads is the sign: anything below zero is thrown as an exception carrying the
 * number, caught, and turned into a flat thousand. The two values are therefore ours to choose,
 * and they are apart only so that a trace can tell a machine that cannot ping from a machine that
 * did not answer.
 */
enum {
    PING_UNAVAILABLE = -1,
    PING_TIMED_OUT = -2,
};

enum {
    ECHO_PAYLOAD = 56,
    ECHO_HEADER = 8,
    ECHO_SEQUENCE_AT = 6,
    REPLY_ROOM = 1500,
    IPV4 = 4,
    PAYLOAD_FILL = 0x42,
};

typedef struct {
    uint8_t type;
    uint8_t code;
    uint16_t checksum;
    uint16_t identifier;
    uint16_t sequence;
    uint8_t payload[ECHO_PAYLOAD];
} Echo;

_Static_assert(sizeof(Echo) == ECHO_HEADER + ECHO_PAYLOAD, "an echo packet cannot carry padding");

static atomic_ushort lastSequence;

static uint16_t checksumOf(const void *from, size_t length) {
    const uint8_t *bytes = from;
    uint32_t total = 0;

    while (length > 1) {
        uint16_t word;
        memcpy(&word, bytes, sizeof(word));
        total += word;
        bytes += sizeof(word);
        length -= sizeof(word);
    }
    if (length == 1) {
        total += *bytes;
    }

    total = (total >> 16) + (total & 0xFFFF);
    total += total >> 16;
    return (uint16_t) ~total;
}

static int setReceiveTimeout(int socketHandle, jlong nanos) {
    struct timeval limit = {
        .tv_sec = (time_t) (nanos / NANOSECONDS_PER_SECOND),
        .tv_usec = (suseconds_t) (nanos % NANOSECONDS_PER_SECOND / 1000),
    };
    return setsockopt(socketHandle, SOL_SOCKET, SO_RCVTIMEO, &limit, sizeof(limit)) == 0;
}

/**
 * Whether a datagram is the reply to the echo that was just sent.
 *
 * A datagram socket leaves the IPv4 header on the front of a reply on some systems and strips it
 * on others, so a header is skipped where one is there rather than assumed either way. An echo
 * reply is type zero, so its first byte can never be mistaken for the first byte of a header.
 *
 * The sequence number is all there is to match on. The kernel puts its own identifier into an
 * echo sent over a datagram socket, because that is how it knows which socket a reply belongs to,
 * so the one that went out is not the one that comes back.
 */
static int isOurReply(const uint8_t *reply, size_t length, uint16_t sequence) {
    size_t offset = 0;
    if (length > 0 && (reply[0] >> 4) == IPV4) {
        offset = (size_t) (reply[0] & 0x0F) * 4;
    }

    int ours = 0;
    if (length >= offset + ECHO_HEADER && reply[offset] == ICMP_ECHOREPLY) {
        uint16_t answered;
        memcpy(&answered, reply + offset + ECHO_SEQUENCE_AT, sizeof(answered));
        ours = answered == sequence;
    }
    return ours;
}

/**
 * The round trip to an address in milliseconds, or a value below zero where there was none.
 *
 * The socket is a datagram one rather than a raw one so that this needs no privilege. The kernel
 * fills in the identifier and the checksum of anything sent over one, but both are set here
 * anyway so that the packet is a correct one however it goes out.
 */
JNIEXPORT jint JNICALL Java_jagex3_jagmisc_jagmisc_ping0(JNIEnv *env, jclass owner,
        jbyte first, jbyte second, jbyte third, jbyte fourth, jlong timeout) {
    (void) env;
    (void) owner;

    if (timeout <= 0) {
        return PING_TIMED_OUT;
    }

    int socketHandle = socket(AF_INET, SOCK_DGRAM, IPPROTO_ICMP);
    if (socketHandle < 0) {
        return PING_UNAVAILABLE;
    }

    uint8_t octets[4] = {(uint8_t) first, (uint8_t) second, (uint8_t) third, (uint8_t) fourth};
    struct sockaddr_in target = {.sin_family = AF_INET};
    memcpy(&target.sin_addr.s_addr, octets, sizeof(octets));

    uint16_t sequence = htons(atomic_fetch_add(&lastSequence, 1));
    Echo sent = {
        .type = ICMP_ECHO,
        .identifier = htons((uint16_t) getpid()),
        .sequence = sequence,
    };
    memset(sent.payload, PAYLOAD_FILL, sizeof(sent.payload));
    sent.checksum = checksumOf(&sent, sizeof(sent));

    jint result = PING_UNAVAILABLE;
    jlong startedAt = monotonicNanos();
    ssize_t written = sendto(socketHandle, &sent, sizeof(sent), 0,
            (struct sockaddr *) &target, sizeof(target));

    if (written == (ssize_t) sizeof(sent)) {
        jlong deadline = startedAt + timeout * NANOSECONDS_PER_MILLISECOND;
        result = PING_TIMED_OUT;

        int waiting = 1;
        while (waiting) {
            jlong remaining = deadline - monotonicNanos();
            if (remaining <= 0 || !setReceiveTimeout(socketHandle, remaining)) {
                waiting = 0;
            } else {
                uint8_t reply[REPLY_ROOM];
                ssize_t read = recv(socketHandle, reply, sizeof(reply), 0);
                jlong arrived = monotonicNanos();
                if (read < 0) {
                    waiting = 0;
                } else if (isOurReply(reply, (size_t) read, sequence)) {
                    result = (jint) ((arrived - startedAt) / NANOSECONDS_PER_MILLISECOND);
                    waiting = 0;
                }
            }
        }
    }

    close(socketHandle);
    return result;
}
