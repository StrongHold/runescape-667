import com.jagex.Entity;
import com.jagex.core.datastruct.LinkedList;
import com.jagex.core.datastruct.Node;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qe")
public final class RenderQueue {

    @OriginalMember(owner = "client!qe", name = "i", descriptor = "I")
    public volatile int size;

    @OriginalMember(owner = "client!qe", name = "g", descriptor = "Lclient!bl;")
    public RenderWorker worker;

    @OriginalMember(owner = "client!qe", name = "j", descriptor = "Lclient!fla;")
    public final LinkedList tasks = new LinkedList();

    @OriginalMember(owner = "client!qe", name = "f", descriptor = "Ljava/lang/String;")
    public final String name;

    @OriginalMember(owner = "client!qe", name = "<init>", descriptor = "(Ljava/lang/String;)V")
    public RenderQueue(@OriginalArg(0) String name) {
        this.name = name;
    }

    @OriginalMember(owner = "client!qe", name = "a", descriptor = "(Lclient!ru;B)V")
    public void addGround(@OriginalArg(0) GroundRenderTask task) {
        @Pc(2) LinkedList local2 = this.tasks;
        synchronized (this.tasks) {
            this.tasks.add(task);
            this.size++;
        }
        if (this.worker != null) {
            @Pc(31) RenderWorker local31 = this.worker;
            synchronized (this.worker) {
                this.worker.notify();
            }
        }
    }

    @OriginalMember(owner = "client!qe", name = "a", descriptor = "(Z)Lclient!ep;")
    public Node poll() {
        @Pc(14) LinkedList local14 = this.tasks;
        synchronized (this.tasks) {
            @Pc(21) Node task = this.tasks.first();
            task.unlink();
            this.size--;
            return task;
        }
    }

    @OriginalMember(owner = "client!qe", name = "a", descriptor = "(Lclient!eo;B)V")
    public void method6809(@OriginalArg(0) Entity entity) {
        entity.aBoolean812 = true;
        @Pc(18) LinkedList local18 = this.tasks;
        synchronized (this.tasks) {
            this.tasks.add(entity);
            this.size++;
        }
        if (this.worker != null) {
            @Pc(43) RenderWorker local43 = this.worker;
            synchronized (this.worker) {
                this.worker.notify();
            }
        }
    }

    @OriginalMember(owner = "client!qe", name = "b", descriptor = "(I)Z")
    public boolean isEmpty() {
        return this.size == 0;
    }

    @OriginalMember(owner = "client!qe", name = "a", descriptor = "(ZLclient!bl;)V")
    public void setWorker(@OriginalArg(1) RenderWorker worker) {
        this.worker = worker;
    }

    @OriginalMember(owner = "client!qe", name = "a", descriptor = "(Lclient!eo;I)V")
    public void method6812(@OriginalArg(0) Entity entity) {
        entity.aBoolean812 = false;
        @Pc(9) LinkedList local9 = this.tasks;
        synchronized (this.tasks) {
            this.tasks.add(entity);
            this.size++;
        }
        if (this.worker != null) {
            @Pc(39) RenderWorker local39 = this.worker;
            synchronized (this.worker) {
                this.worker.notify();
            }
        }
    }
}
