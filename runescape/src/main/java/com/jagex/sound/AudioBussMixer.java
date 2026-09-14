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
    public synchronized void fill(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        do {
            if (this.nextTaskTime < 0) {
                this.fillAll(arg0, arg1, arg2);
                return;
            }
            if (this.position + arg2 < this.nextTaskTime) {
                this.position += arg2;
                this.fillAll(arg0, arg1, arg2);
                return;
            }
            @Pc(33) int local33 = this.nextTaskTime - this.position;
            this.fillAll(arg0, arg1, local33);
            arg1 += local33;
            arg2 -= local33;
            this.position += local33;
            this.rebaseTasks();
            @Pc(60) AudioBussTask local60 = (AudioBussTask) this.tasks.first();
            synchronized (local60) {
                @Pc(68) int local68 = local60.run(this);
                if (local68 < 0) {
                    local60.time = 0;
                    this.removeTask(local60);
                } else {
                    local60.time = local68;
                    this.insertTask(local60.next, local60);
                }
            }
        } while (arg2 != 0);
    }

    @OriginalMember(owner = "client!nn", name = "e", descriptor = "()I")
    public synchronized int count() {
        return this.busses.size();
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(Lclient!dea;)V")
    public synchronized void addFirst(@OriginalArg(0) AudioBuss arg0) {
        this.busses.addFirst(arg0);
    }

    @OriginalMember(owner = "client!nn", name = "b", descriptor = "(Lclient!dea;)V")
    public synchronized void remove(@OriginalArg(0) AudioBuss arg0) {
        arg0.unlink();
    }

    @OriginalMember(owner = "client!nn", name = "b", descriptor = "(I)V")
    public void skipAll(@OriginalArg(0) int arg0) {
        for (@Pc(5) AudioBuss local5 = (AudioBuss) this.busses.first(); local5 != null; local5 = (AudioBuss) this.busses.next()) {
            local5.skip(arg0);
        }
    }

    @OriginalMember(owner = "client!nn", name = "c", descriptor = "([III)V")
    public void fillAll(@OriginalArg(0) int[] arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        for (@Pc(5) AudioBuss local5 = (AudioBuss) this.busses.first(); local5 != null; local5 = (AudioBuss) this.busses.next()) {
            local5.mix(arg0, arg1, arg2);
        }
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss method9135() {
        return (AudioBuss) this.busses.next();
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(Lclient!ie;Lclient!ada;)V")
    public void insertTask(@OriginalArg(0) Node arg0, @OriginalArg(1) AudioBussTask arg1) {
        while (arg0 != this.tasks.sentinel && ((AudioBussTask) arg0).time <= arg1.time) {
            arg0 = arg0.next;
        }
        Node.addBefore(arg0, arg1);
        this.nextTaskTime = ((AudioBussTask) this.tasks.sentinel.next).time;
    }

    @OriginalMember(owner = "client!nn", name = "c", descriptor = "()Lclient!dea;")
    @Override
    public AudioBuss method9133() {
        return (AudioBuss) this.busses.first();
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(I)V")
    @Override
    public synchronized void skip(@OriginalArg(0) int arg0) {
        do {
            if (this.nextTaskTime < 0) {
                this.skipAll(arg0);
                return;
            }
            if (this.position + arg0 < this.nextTaskTime) {
                this.position += arg0;
                this.skipAll(arg0);
                return;
            }
            @Pc(29) int local29 = this.nextTaskTime - this.position;
            this.skipAll(local29);
            arg0 -= local29;
            this.position += local29;
            this.rebaseTasks();
            @Pc(50) AudioBussTask local50 = (AudioBussTask) this.tasks.first();
            synchronized (local50) {
                @Pc(58) int local58 = local50.run(this);
                if (local58 < 0) {
                    local50.time = 0;
                    this.removeTask(local50);
                } else {
                    local50.time = local58;
                    this.insertTask(local50.next, local50);
                }
            }
        } while (arg0 != 0);
    }

    @OriginalMember(owner = "client!nn", name = "f", descriptor = "()V")
    public void rebaseTasks() {
        if (this.position <= 0) {
            return;
        }
        for (@Pc(8) AudioBussTask local8 = (AudioBussTask) this.tasks.first(); local8 != null; local8 = (AudioBussTask) this.tasks.next()) {
            local8.time -= this.position;
        }
        this.nextTaskTime -= this.position;
        this.position = 0;
    }

    @OriginalMember(owner = "client!nn", name = "a", descriptor = "(Lclient!ada;)V")
    public void removeTask(@OriginalArg(0) AudioBussTask arg0) {
        arg0.unlink();
        arg0.close();
        @Pc(9) Node local9 = this.tasks.sentinel.next;
        if (local9 == this.tasks.sentinel) {
            this.nextTaskTime = -1;
        } else {
            this.nextTaskTime = ((AudioBussTask) local9).time;
        }
    }
}
