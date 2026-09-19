import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeap;
import jaclib.memory.heap.NativeHeapBuffer;
import rs2.client.loading.library.LibraryManager;

import java.io.File;
import java.util.Arrays;

/**
 * Exercises the native memory library the hardware toolkits allocate from.
 *
 * The part worth checking is what happens when a heap runs out of room. A freed buffer leaves a
 * hole, and the holes are closed only when the next allocation does not fit, by moving every live
 * buffer down. A buffer that moves has to keep its contents and report its new address, and this
 * forces that to happen and then reads everything back.
 */
public final class MemoryHeap {

    private static final int CAPACITY = 4096;
    private static final int BLOCK = 1024;

    public static void main(String[] arguments) {
        var args = new LibraryArgs();

        if (!CommandLine.parsed("verifyMemoryLibrary", args, arguments)) {
            return;
        }

        try {
            Watchdog.arm("The memory heap check", 120);
            LibraryManager.putLibrary(args.library(), "jaclib");
            LibraryManager.loadNative(MemoryHeap.class, "jaclib");

            checkEndianness();
            checkFloatBits();
            checkZeroFill();
            checkCompaction();
            checkExhaustion();

            System.out.println("the memory heap survived compaction on " + System.getProperty("os.arch"));
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The client reads the first byte of an int to decide which way round this machine stores one.
     */
    private static void checkEndianness() {
        byte first = Stream.getLSB(0xFFFF0000);
        if (first != 0) {
            throw new IllegalStateException("expected a little endian machine, first byte was " + first);
        }
    }

    private static void checkFloatBits() {
        int bits = Stream.floatToRawIntBits(1.5F);
        if (bits != Float.floatToRawIntBits(1.5F)) {
            throw new IllegalStateException("float bits were " + Integer.toHexString(bits));
        }
    }

    private static void checkZeroFill() {
        NativeHeap heap = new NativeHeap(CAPACITY);
        NativeHeapBuffer dirty = heap.a(BLOCK, false);
        dirty.a(pattern(BLOCK, (byte) 0x5A), 0, 0, BLOCK);
        dirty.b();

        NativeHeapBuffer clean = heap.a(BLOCK, true);
        byte[] read = read(heap, clean);
        for (byte value : read) {
            if (value != 0) {
                throw new IllegalStateException("a zeroed buffer held " + value);
            }
        }
        heap.b();
    }

    /**
     * Fills a heap, frees a buffer in the middle of it, and then asks for one that fits only once
     * the hole is closed.
     */
    private static void checkCompaction() {
        NativeHeap heap = new NativeHeap(CAPACITY);

        NativeHeapBuffer first = heap.a(BLOCK, false);
        NativeHeapBuffer middle = heap.a(BLOCK, false);
        NativeHeapBuffer last = heap.a(BLOCK, false);

        first.a(pattern(BLOCK, (byte) 0x11), 0, 0, BLOCK);
        middle.a(pattern(BLOCK, (byte) 0x22), 0, 0, BLOCK);
        last.a(pattern(BLOCK, (byte) 0x33), 0, 0, BLOCK);

        long before = last.getAddress();
        middle.b();

        NativeHeapBuffer added = heap.a(BLOCK * 2, false);
        added.a(pattern(BLOCK * 2, (byte) 0x44), 0, 0, BLOCK * 2);

        if (last.getAddress() == before) {
            throw new IllegalStateException("the last buffer did not move, so nothing was compacted");
        }

        expect(read(heap, first), pattern(BLOCK, (byte) 0x11), "the first buffer");
        expect(read(heap, last), pattern(BLOCK, (byte) 0x33), "the last buffer");
        expect(read(heap, added), pattern(BLOCK * 2, (byte) 0x44), "the added buffer");

        heap.b();
    }

    private static void checkExhaustion() {
        NativeHeap heap = new NativeHeap(CAPACITY);
        try {
            heap.a(CAPACITY * 2, false);
        } catch (Throwable expected) {
            heap.b();
            return;
        }
        heap.b();
        throw new IllegalStateException("a heap gave out more than it holds");
    }

    private static byte[] read(NativeHeap heap, NativeHeapBuffer buffer) {
        byte[] read = new byte[buffer.getSize()];
        heap.get(handleOf(buffer), read, 0, 0, read.length);
        return read;
    }

    /**
     * The handle the heap knows this buffer by, which the buffer keeps but does not offer.
     */
    private static int handleOf(NativeHeapBuffer buffer) {
        return buffer.c;
    }

    private static byte[] pattern(int length, byte value) {
        byte[] pattern = new byte[length];
        Arrays.fill(pattern, value);
        return pattern;
    }

    private static void expect(byte[] actual, byte[] wanted, String what) {
        if (!Arrays.equals(actual, wanted)) {
            throw new IllegalStateException(what + " did not survive being moved");
        }
    }

    private MemoryHeap() {
        /* empty */
    }
}
