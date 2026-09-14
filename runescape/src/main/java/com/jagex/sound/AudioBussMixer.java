package com.jagex.sound;

import com.jagex.core.datastruct.key.Deque;
import com.jagex.core.datastruct.key.Node;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!nn")
public final class AudioBussMixer extends AudioBuss {

    @OriginalMember(owner = "client!nn", name = "p", descriptor = "Lclient!sia;")
    public final Deque busses = new Deque();

    @OriginalMember(owner = "client!nn", name = "o", descriptor = "Lclient!sia;")
    public final Deque tasks = new Deque();

    @OriginalMember(owner = "client!nn", name = "q", descriptor = "I")
    public int position = 0;

    @OriginalMember(owner = "client!nn", name = "r", descriptor = "I")
    public int nextTaskTime = -1;

    @OriginalMember(owner = "client!nn", name = "b", descriptor = "()I")
    @Override
    public int method9132() {
        return 0;
    }

    @OriginalMember(owner = "client!nn", name = "b", descriptor = "([III)V")
    @Override
    public synchronized void fill(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int length) {
        do {
            if (this.nextTaskTime < 0) {
                this.fillAll(mix, offset, length);
                return;
            }
            if (this.position + length < this.nextTaskTime) {
                this.position += length;
                this.fillAll(mix, offset, length);
                return;
            }
            @Pc(33) int count = this.nextTaskTime - this.position;
            this.fillAll(mix, offset, count);
            offset += count;
            length -= count;
            this.position += count;
            this.rebaseTasks();
            @Pc(60) AudioBussTask task = (AudioBussTask) this.tasks.first();
            synchronized (task) {
                @Pc(68) int time = task.run(this);
                if (time < 0) {
                    task.time = 0;
                    this.removeTask(task);
                } else {
                    task.time = time;
                    this.insertTask(task.next, task);
                }
            }
        } while (length != 0);
    }

    @OriginalMember(owner = "client!nn", name = "e", descriptor = "()I")
    public synchronized int count() {
        return this.busses.size();
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(Lclient!dea;)V")
    public synchronized void addFirst(@OriginalArg(0) AudioBuss buss) {
        this.busses.addFirst(buss);
    }

    @OriginalMember(owner = "client!nn", name = "b", descriptor = "(Lclient!dea;)V")
    public synchronized void remove(@OriginalArg(0) AudioBuss buss) {
        buss.unlink();
    }

    @OriginalMember(owner = "client!nn", name = "b", descriptor = "(I)V")
    public void skipAll(@OriginalArg(0) int length) {
        for (@Pc(5) AudioBuss buss = (AudioBuss) this.busses.first(); buss != null; buss = (AudioBuss) this.busses.next()) {
            buss.skip(length);
        }
    }

    @OriginalMember(owner = "client!nn", name = "c", descriptor = "([III)V")
    public void fillAll(@OriginalArg(0) int[] mix, @OriginalArg(1) int offset, @OriginalArg(2) int length) {
        for (@Pc(5) AudioBuss buss = (AudioBuss) this.busses.first(); buss != null; buss = (AudioBuss) this.busses.next()) {
            buss.mix(mix, offset, length);
        }
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss nextSubStream() {
        return (AudioBuss) this.busses.next();
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(Lclient!ie;Lclient!ada;)V")
    public void insertTask(@OriginalArg(0) Node node, @OriginalArg(1) AudioBussTask task) {
        while (node != this.tasks.sentinel && ((AudioBussTask) node).time <= task.time) {
            node = node.next;
        }
        Node.addBefore(node, task);
        this.nextTaskTime = ((AudioBussTask) this.tasks.sentinel.next).time;
    }

    @OriginalMember(owner = "client!nn", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss firstSubStream() {
        return (AudioBuss) this.busses.first();
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(I)V")
    @Override
    public synchronized void skip(@OriginalArg(0) int length) {
        do {
            if (this.nextTaskTime < 0) {
                this.skipAll(length);
                return;
            }
            if (this.position + length < this.nextTaskTime) {
                this.position += length;
                this.skipAll(length);
                return;
            }
            @Pc(29) int count = this.nextTaskTime - this.position;
            this.skipAll(count);
            length -= count;
            this.position += count;
            this.rebaseTasks();
            @Pc(50) AudioBussTask task = (AudioBussTask) this.tasks.first();
            synchronized (task) {
                @Pc(58) int time = task.run(this);
                if (time < 0) {
                    task.time = 0;
                    this.removeTask(task);
                } else {
                    task.time = time;
                    this.insertTask(task.next, task);
                }
            }
        } while (length != 0);
    }

    @OriginalMember(owner = "client!nn", name = "f", descriptor = "()V")
    public void rebaseTasks() {
        if (this.position <= 0) {
            return;
        }
        for (@Pc(8) AudioBussTask task = (AudioBussTask) this.tasks.first(); task != null; task = (AudioBussTask) this.tasks.next()) {
            task.time -= this.position;
        }
        this.nextTaskTime -= this.position;
        this.position = 0;
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(Lclient!ada;)V")
    public void removeTask(@OriginalArg(0) AudioBussTask task) {
        task.unlink();
        task.close();
        @Pc(9) Node node = this.tasks.sentinel.next;
        if (node == this.tasks.sentinel) {
            this.nextTaskTime = -1;
        } else {
            this.nextTaskTime = ((AudioBussTask) node).time;
        }
    }
}
