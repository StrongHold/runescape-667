import com.jagex.Static14;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.util.SystemTimer;
import com.jagex.game.runetek6.sound.OggKateStream;
import com.jagex.game.runetek6.sound.OggStream;
import com.jagex.sound.QueueBuss;
import jagtheora.ogg.OggPacket;
import jagtheora.ogg.OggPage;
import jagtheora.ogg.OggStreamState;
import jagtheora.ogg.OggSyncState;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.io.IOException;

@OriginalClass("client!lf")
public abstract class Video {

    @OriginalMember(owner = "client!lf", name = "h", descriptor = "Z")
    public boolean aBoolean799;

    @OriginalMember(owner = "client!lf", name = "y", descriptor = "Lclient!un;")
    public OggKateStream subtitleStream;

    @OriginalMember(owner = "client!lf", name = "i", descriptor = "Z")
    public boolean stopped;

    @OriginalMember(owner = "client!lf", name = "c", descriptor = "Z")
    public boolean aBoolean801;

    @OriginalMember(owner = "client!lf", name = "v", descriptor = "Z")
    public boolean aBoolean802;

    @OriginalMember(owner = "client!lf", name = "w", descriptor = "Lclient!ik;")
    public OggVorbisStream audioStream;

    @OriginalMember(owner = "client!lf", name = "k", descriptor = "Lclient!mda;")
    public OggTheoraStream videoStream;

    @OriginalMember(owner = "client!lf", name = "b", descriptor = "Ljava/lang/String;")
    public String language;

    @OriginalMember(owner = "client!lf", name = "u", descriptor = "[B")
    public final byte[] pageBuffer;

    @OriginalMember(owner = "client!lf", name = "F", descriptor = "Lclient!jagtheora/ogg/OggSyncState;")
    public final OggSyncState syncState;

    @OriginalMember(owner = "client!lf", name = "t", descriptor = "Lclient!jagtheora/ogg/OggPage;")
    public final OggPage page;

    @OriginalMember(owner = "client!lf", name = "G", descriptor = "Lclient!jagtheora/ogg/OggPacket;")
    public final OggPacket packet;

    @OriginalMember(owner = "client!lf", name = "A", descriptor = "Lclient!av;")
    public final IterableHashTable streams;

    @OriginalMember(owner = "client!lf", name = "<init>", descriptor = "(I)V")
    protected Video(@OriginalArg(0) int pageBufferSize) {
        if (!Static14.loadNativeLibrary("jagtheora")) {
            throw new RuntimeException("Failed to load jagtheora library");
        }

        this.pageBuffer = new byte[pageBufferSize];
        this.syncState = new OggSyncState();
        this.page = new OggPage();
        this.packet = new OggPacket();
        this.streams = new IterableHashTable(8);
    }

    @OriginalMember(owner = "client!lf", name = "b", descriptor = "(B)Lclient!kb;")
    public OggStream nextStream() throws IOException {
        if (this.stopped) {
            throw new IllegalStateException();
        }

        if (this.aBoolean799) {
            return null;
        }

        while (this.syncState.pageOut(this.page) <= 0) {
            @Pc(25) int length = this.readPage(this.pageBuffer);

            if (length == -1) {
                this.aBoolean799 = true;
                return null;
            } else if (length == 0) {
                return null;
            } else if (!this.syncState.write(this.pageBuffer, length)) {
                throw new RuntimeException("");
            }
        }

        @Pc(25) int serialNumber = this.page.getSerialNumber();

        if (!this.page.isBOS()) {
            @Pc(81) OggStream stream = (OggStream) this.streams.get(serialNumber);

            if (stream.state.pageIn(this.page)) {
                return stream;
            } else {
                throw new IllegalStateException();
            }
        }

        @Pc(99) OggStreamState state = new OggStreamState(serialNumber);
        if (!state.pageIn(this.page)) {
            throw new IllegalStateException();
        }
        if (state.packetPeek(this.packet) != 1) {
            throw new IllegalStateException();
        }

        @Pc(188) OggStream stream;
        if (this.videoStream == null && this.packet.isTheora()) {
            this.videoStream = new OggTheoraStream(state);
            stream = this.videoStream;
        } else if (this.audioStream == null && this.packet.isVorbis()) {
            this.audioStream = new OggVorbisStream(state);
            stream = this.audioStream;
        } else {
            @Pc(144) byte[] data = this.packet.getData();
            @Pc(148) StringBuffer buffer = new StringBuffer();
            for (@Pc(150) int i = 1; data.length > i && Character.isLetterOrDigit((char) data[i]); i++) {
                buffer.append((char) data[i]);
            }

            @Pc(175) String identifier = buffer.toString();
            if (identifier.equals("kate")) {
                stream = new OggKateStream(state);
            } else {
                stream = new OggOtherStream(state);
            }
        }

        this.streams.put(serialNumber, stream);
        return stream;
    }

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "(ZI)V")
    public final void method9174(@OriginalArg(0) boolean arg0) {
        if (this.audioStream != null) {
            @Pc(7) QueueBuss local7 = this.audioStream.method3960();

            if (local7 != null) {
                local7.setPaused(arg0);
            }
        }

        this.aBoolean802 = !this.aBoolean802;
    }

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "(I)V")
    public void tickVideo() throws IOException {
        while (this.videoStream.state.packetOut(this.packet) != 1) {
            @Pc(11) OggStream stream = this.nextStream();

            if (stream == null) {
                if (this.aBoolean799) {
                    this.handle();
                }
                return;
            }

            if (stream == this.subtitleStream) {
                this.tickSubtitles();
            }
        }

        this.videoStream.handle(this.packet);
    }

    @OriginalMember(owner = "client!lf", name = "d", descriptor = "(I)Z")
    public boolean isVideoBehind() {
        if (this.audioStream == null) {
            @Pc(42) double frameRate = this.videoStream.frameRate();
            return (frameRate == 0.0D) || ((double) SystemTimer.safetime() >= ((1000.0D / frameRate) + (double) this.videoStream.getLastDecodeTime()));
        } else {
            return !this.videoStream.isSetup() || (this.getTime() > this.videoStream.getTime());
        }
    }

    @OriginalMember(owner = "client!lf", name = "g", descriptor = "(I)Z")
    public final boolean method9177() {
        if (this.stopped || this.aBoolean799) {
            return this.audioStream == null || this.audioStream.method3965() <= 0;
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!lf", name = "c", descriptor = "(I)Lclient!un;")
    public final OggKateStream subtitleStream() {
        return this.subtitleStream;
    }

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "(Z)V")
    public final void tick() throws IOException {
        if (this.aBoolean802) {
            return;
        }

        while (!this.stopped) {
            @Pc(26) OggStream stream;

            if (this.aBoolean801) {
                stream = (OggStream) this.streams.get(this.page.getSerialNumber());
            } else {
                stream = this.nextStream();

                if (stream == null) {
                    if (this.aBoolean799) {
                        this.handle();
                    }
                    return;
                }

                if (stream == null) {
                    throw new IllegalStateException();
                }

                this.aBoolean801 = true;
            }

            if (stream == this.audioStream) {
                if (this.audioStream.method3965() >= 50) {
                    return;
                }

                while (this.audioStream.state.packetOut(this.packet) == 1) {
                    this.audioStream.handle(this.packet);
                    this.tickSubtitles();

                    if (this.videoStream != null) {
                        @Pc(185) double time = this.videoStream.getTime();

                        for (@Pc(187) int tick = 0; tick < 10 && this.isVideoBehind(); tick++) {
                            this.tickVideo();

                            if (this.stopped) {
                                return;
                            }
                        }

                        if (this.videoStream.getTime() > time) {
                            return;
                        }
                    }

                    if (this.audioStream.method3965() >= 50) {
                        return;
                    }
                }
            } else if (stream instanceof OggKateStream) {
                this.tickSubtitles();
            } else if (this.videoStream == stream) {
                if (this.audioStream == null && !this.aBoolean802) {
                    for (@Pc(88) int tick = 0; tick < 10 && this.isVideoBehind(); tick++) {
                        this.tickVideo();

                        if (this.stopped) {
                            return;
                        }
                    }
                    return;
                }
            } else {
                while (stream.state.packetOut(this.packet) == 1) {
                    if (stream.packetNumber == 1 && stream instanceof OggKateStream) {
                        this.setLanguage(this.language);
                    }

                    stream.handle(this.packet);
                }
            }

            this.aBoolean801 = false;
        }
    }

    @OriginalMember(owner = "client!lf", name = "c", descriptor = "(B)V")
    public void tickSubtitles() {
        for (@Pc(11) OggStream stream = (OggStream) this.streams.first(); stream != null; stream = (OggStream) this.streams.next()) {
            if (stream instanceof OggKateStream) {
                @Pc(21) OggKateStream kateStream = (OggKateStream) stream;

                while ((kateStream.packetNumber <= 8 || this.getTime() > (double) kateStream.getEndTime()) && kateStream.state.packetOut(this.packet) == 1) {
                    kateStream.handle(this.packet);
                }
            }
        }

        this.setLanguage(this.language);
    }

    @OriginalMember(owner = "client!lf", name = "e", descriptor = "(B)V")
    public final void stop() {
        if (this.stopped) {
            return;
        }

        for (@Pc(14) OggStream stream = (OggStream) this.streams.first(); stream != null; stream = (OggStream) this.streams.next()) {
            stream.stop();
            stream.state.cleanUp();
        }

        this.packet.cleanUp();
        this.page.cleanUp();
        this.syncState.cleanUp();
        this.stopped = true;
    }

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "(ILjava/lang/String;)V")
    public final void setLanguage(@OriginalArg(1) String language) {
        this.language = language;

        if (this.language == null) {
            this.subtitleStream = null;
        } else {
            if (this.subtitleStream != null && !this.language.equals(this.subtitleStream.getLanguage())) {
                this.subtitleStream = null;
            }

            if (this.subtitleStream == null) {
                for (@Pc(50) OggStream stream = (OggStream) this.streams.first(); stream != null; stream = (OggStream) this.streams.next()) {
                    if (stream instanceof OggKateStream) {
                        @Pc(57) OggKateStream kateStream = (OggKateStream) stream;
                        if (this.language.equals(kateStream.getLanguage())) {
                            this.subtitleStream = kateStream;
                            return;
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lf", name = "h", descriptor = "(I)D")
    public final double getTime() {
        if (this.audioStream != null) {
            return this.audioStream.getTime();
        } else if (this.videoStream != null) {
            return this.videoStream.getTime();
        } else {
            return 0.0D;
        }
    }

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "([BI)I")
    protected abstract int readPage(@OriginalArg(0) byte[] data) throws IOException;

    @OriginalMember(owner = "client!lf", name = "f", descriptor = "(B)Lclient!mda;")
    public final OggTheoraStream getVideoStream() {
        return this.videoStream;
    }

    @OriginalMember(owner = "client!lf", name = "a", descriptor = "(B)V")
    public void handle() {
        for (@Pc(7) OggStream stream = (OggStream) this.streams.first(); stream != null; stream = (OggStream) this.streams.next()) {
            if (stream != this.videoStream) {
                while (stream.state.packetOut() == 1) {
                    stream.handle(this.packet);
                }
            }
        }

        if (this.videoStream == null) {
            return;
        }

        for (@Pc(64) int tick = 0; tick < 10; tick++) {
            if (!this.isVideoBehind()) {
                return;
            }

            if (this.videoStream.state.packetOut() != 1) {
                this.stop();
                return;
            }

            this.videoStream.handle(this.packet);
        }
    }

    @OriginalMember(owner = "client!lf", name = "b", descriptor = "(I)Lclient!ik;")
    public final OggVorbisStream getAudioStream() {
        return this.audioStream;
    }
}
