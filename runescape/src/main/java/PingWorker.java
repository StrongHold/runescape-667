import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.Node;
import jagex3.jagmisc.jagmisc;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.net.InetAddress;

@OriginalClass("client!lha")
public final class PingWorker implements Runnable {

    @OriginalMember(owner = "client!lha", name = "h", descriptor = "Lclient!sia;")
    public final Deque requests = new Deque();

    @OriginalMember(owner = "client!lha", name = "d", descriptor = "Ljava/lang/Thread;")
    public Thread thread = new Thread(this);

    @OriginalMember(owner = "client!lha", name = "<init>", descriptor = "()V")
    public PingWorker() {
        this.thread.setDaemon(true);
        this.thread.start();
    }

    @OriginalMember(owner = "client!lha", name = "run", descriptor = "()V")
    @Override
    public void run() {
        while (true) {
            @Pc(8) Deque local8 = this.requests;
            @Pc(43) PingRequest request;
            synchronized (this.requests) {
                @Pc(15) Node node;
                for (node = this.requests.removeFirst(); node == null; node = this.requests.removeFirst()) {
                    try {
                        this.requests.wait();
                    } catch (@Pc(23) InterruptedException ignored) {
                        /* empty */
                    }
                }
                if (!(node instanceof PingRequest)) {
                    return;
                }
                request = (PingRequest) node;
            }
            @Pc(69) int ping;
            try {
                @Pc(54) byte[] address = InetAddress.getByName(request.address).getAddress();
                ping = jagmisc.ping(address[0], address[1], address[2], address[3], 1000L);
            } catch (@Pc(71) Throwable ex) {
                ping = 1000;
            }
            request.ping = ping;
        }
    }

    @OriginalMember(owner = "client!lha", name = "a", descriptor = "(I)V")
    public void close() {
        if (this.thread == null) {
            return;
        }
        this.add(new Node());
        try {
            this.thread.join();
        } catch (@Pc(23) InterruptedException ignored) {
            /* empty */
        }
        this.thread = null;
    }

    @OriginalMember(owner = "client!lha", name = "a", descriptor = "(ILclient!ie;)V")
    public void add(@OriginalArg(1) Node node) {
        @Pc(2) Deque local2 = this.requests;
        synchronized (this.requests) {
            this.requests.addLast(node);
            this.requests.notify();
        }
    }

    @OriginalMember(owner = "client!lha", name = "a", descriptor = "(BLjava/lang/String;)Lclient!cja;")
    public PingRequest ping(@OriginalArg(1) String address) {
        if (this.thread == null) {
            throw new IllegalStateException("");
        } else if (address == null) {
            throw new IllegalArgumentException("");
        } else {
            @Pc(32) PingRequest request = new PingRequest(address);
            this.add(request);
            return request;
        }
    }
}
