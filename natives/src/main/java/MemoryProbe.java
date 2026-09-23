import jaclib.hardware_info.HardwareInfo;
import jaclib.memory.NativeBuffer;
import jaclib.memory.Stream;
import jaclib.memory.heap.NativeHeap;
import jaclib.memory.heap.NativeHeapBuffer;
import rs2.client.loading.library.LibraryManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Records what a memory library answers, so that ours can be held against the shipped one.
 *
 * The heap is the part worth asking about. What it hands back is an address, and two libraries
 * will never malloc at the same one, so an address on its own says nothing. What can be compared
 * is where a buffer sits within its heap: the first buffer of each heap is taken as the mark and
 * every address is written down as the distance from it. That records the size of whatever header
 * a buffer carries, how one is aligned, where the next one starts, and what moves when the holes
 * are closed, none of which depends on where the heap itself landed.
 *
 * The library and the file to write are the arguments.
 */
public final class MemoryProbe {

    private static final int CAPACITY = 4096;
    private static final int BLOCK = 1000;
    private static final int BLOCKS = 4;

    /**
     * Larger than what is left at the top of the heap once a block is freed, and smaller than what
     * closing that hole would leave, so it cannot be satisfied without moving every live buffer.
     */
    private static final int FORCES_COMPACTION = 1024;

    public static void main(String[] arguments) {
        var parsed = CommandLine.parse("memoryProbe", new ProbeArgs(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(ProbeArgs args) {
        try {
            Watchdog.arm("The memory library probe", 120);
            LibraryManager.putLibrary(args.library(), "jaclib");
            LibraryManager.loadNative(MemoryProbe.class, "jaclib");

            var answers = new ArrayList<String>();
            askStream(answers);
            askHardware(answers);
            askHeap(answers);
            askBuffer(answers);
            askRefusals(answers);

            Files.write(args.answers(), answers);
            System.out.println("the memory library answered " + answers.size() + " times");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private static void askStream(List<String> answers) {
        for (int shift = 0; shift < Integer.SIZE; shift++) {
            int value = 1 << shift;
            answers.add("lsb " + value + " = " + Stream.getLSB(value));
        }
        for (float value : new float[] {0F, 1F, -1F, 1.5F, -0.125F, Float.MIN_VALUE, Float.MAX_VALUE, Float.NaN}) {
            answers.add("bits " + value + " = " + Stream.floatToRawIntBits(value));
        }
    }

    /**
     * The survey the client sends at login, and the three property lists.
     *
     * Only how many fields the survey has is written down, never what is in them. The survey
     * describes the machine, and the shipped library is measured here under an emulator on a
     * processor it was never built for, so its six processor fields describe the emulator rather
     * than anything real. Its seventh is worse: it asks the kernel for the size of memory with a
     * buffer it declares too small, so the call writes nothing and it returns whatever the stack
     * held, which is a different number on every run. There is nothing there to hold an answer
     * against, and a check that demanded equality would be demanding it of uninitialised memory.
     */
    private static void askHardware(List<String> answers) {
        answers.add("survey fields = " + HardwareInfo.getCPUInfo().length);
        answers.add("opengl properties = " + countOf(HardwareInfo.getOpenGLProps()));
        answers.add("dxdiag system properties = " + countOf(HardwareInfo.getDXDiagSystemProps()));
        answers.add("dxdiag display devices = " + countOf(HardwareInfo.getDXDiagDisplayDevicesProps()));
    }

    /**
     * Nothing and none are different answers here, and the client tells them apart: it reads a
     * property list only after finding it is not null.
     */
    private static String countOf(Object[] properties) {
        return properties == null ? "none" : String.valueOf(properties.length);
    }

    private static void askHeap(List<String> answers) {
        NativeHeap heap = new NativeHeap(CAPACITY);
        var blocks = new ArrayList<NativeHeapBuffer>();
        for (int block = 0; block < BLOCKS; block++) {
            blocks.add(heap.a(BLOCK, block % 2 == 0));
        }

        long mark = blocks.get(0).getAddress();
        describe(answers, "fresh", blocks, mark);
        for (int block = 0; block < blocks.size(); block++) {
            blocks.get(block).a(pattern(block, BLOCK), 0, 0, BLOCK);
        }
        readBack(answers, "fresh", blocks);

        blocks.remove(1).b();
        describe(answers, "holed", blocks, mark);

        blocks.add(heap.a(FORCES_COMPACTION, true));
        describe(answers, "closed", blocks, mark);
        readBack(answers, "closed", blocks);

        answers.add("second heap first handle = " + handleOf(new NativeHeap(CAPACITY).a(BLOCK, true)));
        heap.b();
    }

    /**
     * A buffer put and got by its address rather than by its handle. The address is a heap
     * buffer's, because the client only ever has one of those to give it.
     */
    private static void askBuffer(List<String> answers) {
        NativeHeap heap = new NativeHeap(CAPACITY);
        NativeHeapBuffer block = heap.a(BLOCK, true);
        var buffer = new NativeBuffer();

        buffer.put(block.getAddress(), pattern(7, BLOCK), 0, 0, BLOCK);
        byte[] read = new byte[BLOCK];
        buffer.get(block.getAddress(), read, 0, 0, BLOCK);
        answers.add("buffer round trip = " + sumOf(read));

        buffer.put(block.getAddress(), pattern(9, BLOCK), 100, 200, 300);
        buffer.get(block.getAddress(), read, 0, 0, BLOCK);
        answers.add("buffer part = " + sumOf(read));

        heap.b();
    }

    /**
     * What the library does when it is asked for something it cannot give. A refusal is part of
     * the contract: the client catches an out of memory from a heap and asks for a smaller one.
     */
    private static void askRefusals(List<String> answers) {
        NativeHeap heap = new NativeHeap(CAPACITY);
        heap.a(BLOCK, true);

        answers.add("over capacity: " + refusalOf(() -> heap.a(CAPACITY, true)));
        answers.add("negative size: " + refusalOf(() -> heap.a(-1, true)));
        answers.add("no such handle: " + refusalOf(() -> heap.getBufferAddress(9999)));
        answers.add("free twice: " + refusalOf(() -> heap.deallocateBuffer(9999)));

        heap.b();
        answers.add("after the heap went: " + refusalOf(() -> heap.a(1, true)));
    }

    private static void describe(List<String> answers, String when, List<NativeHeapBuffer> blocks, long mark) {
        for (int block = 0; block < blocks.size(); block++) {
            NativeHeapBuffer held = blocks.get(block);
            answers.add(when + " " + block
                    + ": handle " + handleOf(held)
                    + ", size " + held.getSize()
                    + ", at " + (held.getAddress() - mark));
        }
    }

    private static void readBack(List<String> answers, String when, List<NativeHeapBuffer> blocks) {
        for (int block = 0; block < blocks.size(); block++) {
            NativeHeapBuffer held = blocks.get(block);
            byte[] read = new byte[held.getSize()];
            held.a.get(handleOf(held), read, 0, 0, read.length);
            answers.add(when + " " + block + " holds " + sumOf(read));
        }
    }

    private static int handleOf(NativeHeapBuffer buffer) {
        return buffer.c;
    }

    private static byte[] pattern(int block, int length) {
        byte[] bytes = new byte[length];
        for (int at = 0; at < length; at++) {
            bytes[at] = (byte) (block * 31 + at);
        }
        return bytes;
    }

    private static long sumOf(byte[] bytes) {
        long total = 0;
        for (byte value : bytes) {
            total = total * 31 + (value & 0xFF);
        }
        return total;
    }

    private static String refusalOf(Runnable work) {
        try {
            work.run();
            return "allowed";
        } catch (Throwable refused) {
            return refused.getClass().getName() + " " + refused.getMessage();
        }
    }

    private MemoryProbe() {
        /* empty */
    }
}
