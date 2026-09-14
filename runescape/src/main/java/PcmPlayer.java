import com.jagex.core.util.Arrays;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.runetek6.sound.Audio;
import com.jagex.sound.AudioBuss;
import com.jagex.sound.QueueBuss;
import com.jagex.sound.SoundPacket;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Component;

@OriginalClass("client!cd")
public class PcmPlayer {

    private static boolean debug;

    @OriginalMember(owner = "client!cd", name = "t", descriptor = "[I")
    public int[] anIntArray315;

    @OriginalMember(owner = "client!cd", name = "A", descriptor = "Lclient!dea;")
    public AudioBuss mixBuss;

    @OriginalMember(owner = "client!cd", name = "B", descriptor = "I")
    public int anInt4097;

    @OriginalMember(owner = "client!cd", name = "k", descriptor = "I")
    public int anInt4098;

    @OriginalMember(owner = "client!cd", name = "v", descriptor = "I")
    public int consumeMargin;

    @OriginalMember(owner = "client!cd", name = "b", descriptor = "I")
    public final int mixBudget = 32;

    @OriginalMember(owner = "client!cd", name = "w", descriptor = "Z")
    public boolean closed = false;

    @OriginalMember(owner = "client!cd", name = "e", descriptor = "J")
    public long lastFillTime = SystemTimer.safetime();

    @OriginalMember(owner = "client!cd", name = "h", descriptor = "J")
    public long retryTime = 0L;

    @OriginalMember(owner = "client!cd", name = "f", descriptor = "I")
    public int resortCountdown = 0;

    @OriginalMember(owner = "client!cd", name = "p", descriptor = "[Lclient!dea;")
    public final AudioBuss[] bucketTails = new AudioBuss[8];

    @OriginalMember(owner = "client!cd", name = "z", descriptor = "I")
    public int maxConsumed = 0;

    @OriginalMember(owner = "client!cd", name = "o", descriptor = "I")
    public int prevPosition = 0;

    @OriginalMember(owner = "client!cd", name = "m", descriptor = "J")
    public long nextCheckTime = 0L;

    @OriginalMember(owner = "client!cd", name = "y", descriptor = "Z")
    public boolean restarted = true;

    @OriginalMember(owner = "client!cd", name = "u", descriptor = "[Lclient!dea;")
    public final AudioBuss[] bucketHeads = new AudioBuss[8];

    @OriginalMember(owner = "client!cd", name = "G", descriptor = "I")
    public int lastMaxConsumed = 0;

    @OriginalMember(owner = "client!cd", name = "a", descriptor = "(ILclient!dea;)V")
    public final synchronized void method3582(@OriginalArg(1) AudioBuss buss) {
        this.mixBuss = buss;
    }

    @OriginalMember(owner = "client!cd", name = "a", descriptor = "()V")
    protected void discardBuffer() throws Exception {
        /* empty */
    }

    @OriginalMember(owner = "client!cd", name = "a", descriptor = "(II)V")
    public void skipBlock() {
        this.resortCountdown -= 256;
        if (this.resortCountdown < 0) {
            this.resortCountdown = 0;
        }
        if (this.mixBuss != null) {
            this.mixBuss.skip(256);
        }
    }

    @OriginalMember(owner = "client!cd", name = "c", descriptor = "(I)V")
    public final synchronized void method3586() {
        if (Static232.pcmPlayerThread != null) {
            @Pc(11) boolean allStopped = true;
            for (@Pc(13) int index = 0; index < 2; index++) {
                if (Static232.pcmPlayerThread.players[index] == this) {
                    Static232.pcmPlayerThread.players[index] = null;
                }
                if (Static232.pcmPlayerThread.players[index] != null) {
                    allStopped = false;
                }
            }
            if (allStopped) {
                Static232.pcmPlayerThread.stopping = true;
                while (Static232.pcmPlayerThread.running) {
                    TimeUtils.sleep(50L);
                }
                Static232.pcmPlayerThread = null;
            }
        }
        this.closeDevice();
        this.anIntArray315 = null;
        this.closed = true;
    }

    @OriginalMember(owner = "client!cd", name = "d", descriptor = "()I")
    protected int position() throws Exception {
        return this.anInt4097;
    }

    @OriginalMember(owner = "client!cd", name = "b", descriptor = "(I)V")
    public void method3588(@OriginalArg(0) int capacity) throws Exception {
        /* empty */
    }

    @OriginalMember(owner = "client!cd", name = "b", descriptor = "()V")
    protected void write() throws Exception {
        /* empty */
    }

    @OriginalMember(owner = "client!cd", name = "a", descriptor = "(ZILclient!dea;)V")
    public void addToBucket(@OriginalArg(1) int priority, @OriginalArg(2) AudioBuss buss) {
        @Pc(7) int bucket = priority >> 5;
        @Pc(12) AudioBuss tail = this.bucketTails[bucket];
        if (tail == null) {
            this.bucketHeads[bucket] = buss;
        } else {
            tail.nextInBucket = buss;
        }
        this.bucketTails[bucket] = buss;
        buss.priority = priority;
    }

    @OriginalMember(owner = "client!cd", name = "a", descriptor = "(B)V")
    public final synchronized void method3592() {
        this.restarted = true;
        try {
            this.discardBuffer();
        } catch (@Pc(19) Exception exception) {
            System.out.println("pcm_player - discardbuffer error: " + exception.getMessage());
            this.closeDevice();
            this.retryTime = SystemTimer.safetime() + 2000L;
        }
    }

    @OriginalMember(owner = "client!cd", name = "a", descriptor = "(Ljava/awt/Component;)V")
    public void method3593(@OriginalArg(0) Component component) throws Exception {
        /* empty */
    }

    @OriginalMember(owner = "client!cd", name = "b", descriptor = "(B)V")
    public final synchronized void method3594() {
        if (this.closed) {
            return;
        }
        @Pc(11) long time = SystemTimer.safetime();
        try {
            if (this.lastFillTime + 6000L < time) {
                this.lastFillTime = time - 6000L;
            }
            while (time > this.lastFillTime + 5000L) {
                this.skipBlock();
                this.lastFillTime += 256000 / Audio.sampleRate;
                time = SystemTimer.safetime();
            }
        } catch (@Pc(54) Exception exception) {
            System.out.println("pcm_player - stalldetect error: " + exception.getMessage());
            this.lastFillTime = time;
        }

        if (this.anIntArray315 == null) {
            return;
        }
        try {
            if (this.retryTime != 0L) {
                if (this.retryTime > time) {
                    return;
                }
                this.method3588(this.anInt4097);
                this.retryTime = 0L;
                this.restarted = true;
            }
            @Pc(95) int buffered = this.position();
            if (this.prevPosition - buffered > this.maxConsumed) {
                this.maxConsumed = this.prevPosition - buffered;
            }
            @Pc(118) int target = this.anInt4098 + this.consumeMargin;
            if (target + 256 > 16384) {
                target = 16128;
            }
            if (this.anInt4097 < target + 256) {
                this.anInt4097 += 1024;
                if (this.anInt4097 > 16384) {
                    this.anInt4097 = 16384;
                }
                this.closeDevice();
                this.method3588(this.anInt4097);
                buffered = 0;
                if (this.anInt4097 < target + 256) {
                    target = this.anInt4097 - 256;
                    this.consumeMargin = target - this.anInt4098;
                }
                this.restarted = true;
            }
            while (target > buffered) {
                this.fill(this.anIntArray315);
                buffered += 256;
                this.write();
            }
            if (time > this.nextCheckTime) {
                if (this.restarted) {
                    this.restarted = false;
                } else if (this.maxConsumed == 0 && this.lastMaxConsumed == 0) {
                    System.out.println("pcm_player - soundcard has stopped consuming!");
                    this.closeDevice();
                    this.retryTime = time + 2000L;
                    return;
                } else {
                    this.consumeMargin = Math.min(this.lastMaxConsumed, this.maxConsumed);
                    this.lastMaxConsumed = this.maxConsumed;
                }
                this.nextCheckTime = time + 2000L;
                this.maxConsumed = 0;
            }
            this.prevPosition = buffered;
        } catch (@Pc(268) Exception exception) {
            System.out.println("pcm_player - error: " + exception.getMessage());
            this.closeDevice();
            this.retryTime = time + 2000L;
        }
    }

    @OriginalMember(owner = "client!cd", name = "a", descriptor = "([II)V")
    public void fill(@OriginalArg(0) int[] samples) {
        @Pc(1) short count = 256;
        if (QueueBuss.stereo) {
            count = 512;
        }
        Arrays.clear(samples, 0, count);
        this.resortCountdown -= 256;
        if (this.mixBuss != null && this.resortCountdown <= 0) {
            this.resortCountdown += Audio.sampleRate >> 4;
            Static440.method5964(this.mixBuss);
            this.addToBucket(this.mixBuss.method9136(), this.mixBuss);
            @Pc(47) int totalCost = 0;
            @Pc(49) int pending = 255;
            @Pc(51) int pass = 7;
            @Pc(58) int bucket;
            label103:
            while (pending != 0) {
                @Pc(63) int round;
                if (pass < 0) {
                    bucket = pass & 0x3;
                    round = -(pass >> 2);
                } else {
                    bucket = pass;
                    round = 0;
                }
                for (@Pc(74) int bits = pending >>> bucket & 0x11111111; bits != 0; bits >>>= 0x4) {
                    if ((bits & 0x1) != 0) {
                        pending &= ~(0x1 << bucket);
                        @Pc(92) AudioBuss prev = null;
                        @Pc(97) AudioBuss buss = this.bucketHeads[bucket];
                        label97:
                        while (true) {
                            while (true) {
                                if (buss == null) {
                                    break label97;
                                }
                                @Pc(101) SoundPacket packet = buss.aClass2_Sub49_6;
                                if (packet == null || packet.anInt8817 <= round) {
                                    buss.active = true;
                                    @Pc(127) int cost = buss.method9132();
                                    totalCost += cost;
                                    if (packet != null) {
                                        packet.anInt8817 += cost;
                                    }
                                    if (totalCost >= this.mixBudget) {
                                        break label103;
                                    }
                                    @Pc(148) AudioBuss subBuss = buss.firstSubStream();
                                    if (subBuss != null) {
                                        if (debug && packet != null && buss.firstSubStream() != null) {
                                            System.out.println("Warning: a pcm_stream with substreams has set its \'w\' - this can cause");
                                            System.out.println("         parent duplicate demotion, and high-pri substreams will be lost!");
                                            System.out.println("         Guilty class name: " + buss.getClass().getName());
                                            debug = true;
                                        }

                                        @Pc(153) int parentPriority = buss.priority;
                                        while (subBuss != null) {
                                            this.addToBucket(parentPriority * subBuss.method9136() >> 8, subBuss);
                                            subBuss = buss.nextSubStream();
                                        }
                                    }
                                    @Pc(172) AudioBuss next = buss.nextInBucket;
                                    buss.nextInBucket = null;
                                    if (prev == null) {
                                        this.bucketHeads[bucket] = next;
                                    } else {
                                        prev.nextInBucket = next;
                                    }
                                    if (next == null) {
                                        this.bucketTails[bucket] = prev;
                                    }
                                    buss = next;
                                } else {
                                    pending |= 0x1 << bucket;
                                    prev = buss;
                                    buss = buss.nextInBucket;
                                }
                            }
                        }
                    }
                    bucket += 4;
                    round++;
                }
                pass--;
            }
            for (bucket = 0; bucket < 8; bucket++) {
                @Pc(218) AudioBuss head = this.bucketHeads[bucket];
                this.bucketHeads[bucket] = this.bucketTails[bucket] = null;
                while (head != null) {
                    @Pc(232) AudioBuss next = head.nextInBucket;
                    head.nextInBucket = null;
                    head = next;
                }
            }
        }
        if (this.resortCountdown < 0) {
            this.resortCountdown = 0;
        }
        if (this.mixBuss != null) {
            this.mixBuss.fill(samples, 0, 256);
        }
        this.lastFillTime = SystemTimer.safetime();
    }

    @OriginalMember(owner = "client!cd", name = "c", descriptor = "()V")
    protected void closeDevice() {
        /* empty */
    }
}
