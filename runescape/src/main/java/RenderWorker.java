import com.jagex.Entity;
import com.jagex.core.datastruct.Node;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Ground;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!bl")
public final class RenderWorker implements Runnable {

    @OriginalMember(owner = "client!bl", name = "b", descriptor = "J")
    public long completionTime;

    @OriginalMember(owner = "client!bl", name = "d", descriptor = "Lclient!qe;")
    public RenderQueue queue;

    @OriginalMember(owner = "client!bl", name = "e", descriptor = "[I")
    public final int[] screenCoords = new int[3];

    @OriginalMember(owner = "client!bl", name = "c", descriptor = "Z")
    public volatile boolean paused = true;

    @OriginalMember(owner = "client!bl", name = "h", descriptor = "Z")
    public volatile boolean running = true;

    @OriginalMember(owner = "client!bl", name = "g", descriptor = "[Lclient!lca;")
    public final PointLight[] pointLights = new PointLight[8];

    @OriginalMember(owner = "client!bl", name = "f", descriptor = "Z")
    public volatile boolean busy = false;

    @OriginalMember(owner = "client!bl", name = "a", descriptor = "I")
    public final int threadId;

    @OriginalMember(owner = "client!bl", name = "i", descriptor = "Lclient!ha;")
    public final Toolkit toolkit;

    @OriginalMember(owner = "client!bl", name = "<init>", descriptor = "(ILclient!ha;)V")
    public RenderWorker(@OriginalArg(0) int threadId, @OriginalArg(1) Toolkit toolkit) {
        this.threadId = threadId;
        this.toolkit = toolkit;
    }

    @OriginalMember(owner = "client!bl", name = "run", descriptor = "()V")
    @Override
    public void run() {
        while (this.running) {
            this.process();
        }
    }

    @OriginalMember(owner = "client!bl", name = "c", descriptor = "()V")
    public void method1101() {
        this.paused = false;
        this.running = false;
        synchronized (this) {
            this.notify();
        }
    }

    @OriginalMember(owner = "client!bl", name = "b", descriptor = "()Z")
    public boolean method1102() {
        return this.queue == null || !this.busy && this.queue.isEmpty();
    }

    @OriginalMember(owner = "client!bl", name = "a", descriptor = "()V")
    public void process() {
        this.toolkit.linkThreads(this.threadId);
        while (!this.paused && this.running) {
            if (this.queue == null || this.queue.isEmpty()) {
                this.busy = false;
                this.completionTime = Static272.aClass13_1.method5161();
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (@Pc(187) InterruptedException local187) {
                        /* empty */
                    }
                }
            } else {
                this.busy = true;
                @Pc(23) Node task = this.queue.poll();
                if (task instanceof Entity) {
                    @Pc(29) Entity entity = (Entity) task;
                    if (entity.aBoolean812) {
                        entity.method9289(Static665.aToolkit_15, -5);
                    } else {
                        Static658.method8591(entity, this.pointLights);

                        if (Fonts.debug != null) {
                            Fonts.debug.render(this.queue.name, entity.anInt10692, entity.anInt10698, 0xFF000000, 0xFFFFFF00);
                        }
                    }
                } else {
                    @Pc(62) int level = ((GroundRenderTask) task).level;
                    if (level >= 1 && level <= 4) {
                        @Pc(76) Ground ground = Static246.ground[level - 1];
                        for (@Pc(78) int offsetX = 0; offsetX < Static35.anInt813 + Static35.anInt813; offsetX++) {
                            for (@Pc(81) int offsetZ = 0; offsetZ < Static35.anInt813 + Static35.anInt813; offsetZ++) {
                                if (Static433.aBooleanArrayArrayArray5[level - 1][offsetX][offsetZ]) {
                                    @Pc(98) int x = Static403.anInt6246 + offsetX - Static35.anInt813;
                                    @Pc(104) int z = Static550.anInt8271 + offsetZ - Static35.anInt813;
                                    if (x >= 0 && x < ground.anInt8894 && z >= 0 && z < ground.anInt8892) {
                                        Static665.aToolkit_15.H(x << EnvironmentLight.anInt1066, ground.getHeight(x, z), z << EnvironmentLight.anInt1066, this.screenCoords);
                                        if (Static356.method5199(this.screenCoords[0]) == this.threadId - 1) {
                                            ground.method7875(x, z);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.toolkit.method8016(this.threadId);
        while (this.paused && this.running) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (@Pc(216) InterruptedException local216) {
                    /* empty */
                }
            }
        }
    }

    @OriginalMember(owner = "client!bl", name = "f", descriptor = "()J")
    public long method1104() {
        return this.completionTime;
    }

    @OriginalMember(owner = "client!bl", name = "a", descriptor = "(Lclient!qe;)V")
    public void method1105(@OriginalArg(0) RenderQueue queue) {
        if (this.queue != null) {
            this.queue.setWorker(null);
        }
        this.queue = queue;
        if (this.queue != null) {
            this.queue.setWorker(this);
        }
    }

    @OriginalMember(owner = "client!bl", name = "d", descriptor = "()V")
    public void method1106() {
        this.paused = true;
        synchronized (this) {
            this.notify();
        }
    }

    @OriginalMember(owner = "client!bl", name = "e", descriptor = "()V")
    public void method1107() {
        this.paused = false;
        synchronized (this) {
            this.notify();
        }
    }
}
