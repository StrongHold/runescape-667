package com.jagex.game.news;

import com.jagex.Client;
import com.jagex.sign.SignedResource;
import com.jagex.sign.SignedResourceStatus;
import com.jagex.core.constants.ModeWhere;
import com.jagex.core.constants.Ports;
import com.jagex.core.datastruct.StringList;
import com.jagex.core.io.ConnectionInfo;
import com.jagex.game.news.NewsItem;
import com.jagex.game.runetek6.client.GameShell;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@OriginalClass("client!tu")
public final class NewsReader implements Runnable {

    @OriginalMember(owner = "client!tu", name = "a", descriptor = "[Lclient!rj;")
    public NewsItem[] items;

    @OriginalMember(owner = "client!tu", name = "f", descriptor = "Z")
    public volatile boolean finished;

    @OriginalMember(owner = "client!tu", name = "c", descriptor = "Lclient!oba;")
    public SignedResource resource;

    @OriginalMember(owner = "client!tu", name = "g", descriptor = "Ljava/lang/Thread;")
    public Thread thread;

    @OriginalMember(owner = "client!tu", name = "a", descriptor = "(II)Lclient!rj;")
    public NewsItem getItem(@OriginalArg(1) int index) {
        return this.items == null || index < 0 || this.items.length <= index ? null : this.items[index];
    }

    @OriginalMember(owner = "client!tu", name = "run", descriptor = "()V")
    @Override
    public void run() {
        try {
            @Pc(16) BufferedReader reader = new BufferedReader(new InputStreamReader((DataInputStream) this.resource.result));
            @Pc(19) String line = reader.readLine();

            @Pc(22) StringList list = StringList.createLinear();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }

            @Pc(39) String[] lines = list.toArray();
            if (lines.length % 3 != 0) {
                return;
            }

            this.items = new NewsItem[lines.length / 3];
            for (@Pc(57) int i = 0; i < lines.length; i += 3) {
                this.items[i / 3] = new NewsItem(lines[i], lines[i + 1], lines[i + 2]);
            }
        } catch (@Pc(92) IOException ignored) {
            /* empty */
        }

        this.finished = true;
    }

    @OriginalMember(owner = "client!tu", name = "b", descriptor = "(I)Z")
    public boolean ready() {
        if (this.finished) {
            return true;
        }

        if (this.resource == null) {
            try {
                @Pc(23) int port = ModeWhere.LIVE == Client.modeWhere ? Ports.HTTP : ConnectionInfo.game.world + Ports.HTTP_OFFSET;
                this.resource = GameShell.signLink.openStream(new URL("http://" + ConnectionInfo.game.address + ":" + port + "/news.ws?game=" + Client.modeGame.id));
            } catch (@Pc(54) MalformedURLException ignored) {
                return true;
            }
        }

        if (this.resource == null || this.resource.status == SignedResourceStatus.ERROR) {
            return true;
        } else if (this.resource.status == SignedResourceStatus.SUCCESS) {
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.start();
            }
            return this.finished;
        } else {
            return false;
        }
    }
}
