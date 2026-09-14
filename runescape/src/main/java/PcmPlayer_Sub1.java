import com.jagex.game.runetek6.sound.Audio;
import com.jagex.math.IntMath;
import com.jagex.sound.QueueBuss;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.awt.Component;

@OriginalClass("client!cb")
public final class PcmPlayer_Sub1 extends PcmPlayer {

    @OriginalMember(owner = "client!cb", name = "J", descriptor = "Ljavax/sound/sampled/SourceDataLine;")
    public SourceDataLine line;

    @OriginalMember(owner = "client!cb", name = "M", descriptor = "[B")
    public byte[] bytes;

    @OriginalMember(owner = "client!cb", name = "L", descriptor = "I")
    public int capacity;

    @OriginalMember(owner = "client!cb", name = "N", descriptor = "Ljavax/sound/sampled/AudioFormat;")
    public AudioFormat format;

    @OriginalMember(owner = "client!cb", name = "I", descriptor = "Z")
    public boolean soundMax = false;

    @OriginalMember(owner = "client!cb", name = "a", descriptor = "()V")
    @Override
    protected void discardBuffer() throws LineUnavailableException {
        this.line.flush();
        if (!this.soundMax) {
            return;
        }
        this.line.close();
        this.line = null;
        @Pc(38) Info info = new Info(Static64.aClass3 == null ? (Static64.aClass3 = getClass("javax.sound.sampled.SourceDataLine")) : Static64.aClass3, this.format, this.capacity << (QueueBuss.stereo ? 2 : 1));
        this.line = (SourceDataLine) AudioSystem.getLine(info);
        this.line.open();
        this.line.start();
    }

    @OriginalMember(owner = "client!cb", name = "b", descriptor = "()V")
    @Override
    protected void write() {
        @Pc(1) short count = 256;
        if (QueueBuss.stereo) {
            count = 512;
        }
        for (@Pc(9) int index = 0; index < count; index++) {
            @Pc(17) int sample = this.anIntArray315[index];
            if ((sample + 8388608 & 0xFF000000) != 0) {
                sample = sample >> 31 ^ 0x7FFFFF;
            }
            this.bytes[index * 2] = (byte) (sample >> 8);
            this.bytes[index * 2 + 1] = (byte) (sample >> 16);
        }
        this.line.write(this.bytes, 0, count << 1);
    }

    @OriginalMember(owner = "client!cb", name = "b", descriptor = "(I)V")
    @Override
    public void method3588(@OriginalArg(0) int capacity) throws LineUnavailableException {
        try {
            @Pc(23) Info info = new Info(Static64.aClass3 == null ? (Static64.aClass3 = getClass("javax.sound.sampled.SourceDataLine")) : Static64.aClass3, this.format, capacity << (QueueBuss.stereo ? 2 : 1));
            this.line = (SourceDataLine) AudioSystem.getLine(info);
            this.line.open();
            this.line.start();
            this.capacity = capacity;
        } catch (@Pc(39) LineUnavailableException exception) {
            if (Static171.method2670(capacity) == 1) {
                this.line = null;
                throw exception;
            } else {
                this.method3588(IntMath.nextPow2(capacity));
            }
        }
    }

    static Class getClass(String name) {
        Class instance;
        try {
            instance = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw (NoClassDefFoundError) new NoClassDefFoundError().initCause(ex);
        }
        return instance;
    }

    @OriginalMember(owner = "client!cb", name = "c", descriptor = "()V")
    @Override
    protected void closeDevice() {
        if (this.line != null) {
            this.line.close();
            this.line = null;
        }
    }

    @OriginalMember(owner = "client!cb", name = "d", descriptor = "()I")
    @Override
    protected int position() {
        return this.capacity - (this.line.available() >> (QueueBuss.stereo ? 2 : 1));
    }

    @OriginalMember(owner = "client!cb", name = "a", descriptor = "(Ljava/awt/Component;)V")
    @Override
    public void method3593(@OriginalArg(0) Component component) {
        @Pc(1) javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        if (mixers != null) {
            for (@Pc(8) int index = 0; index < mixers.length; index++) {
                @Pc(20) javax.sound.sampled.Mixer.Info mixer = mixers[index];
                if (mixer != null) {
                    @Pc(28) String name = mixer.getName();
                    if (name != null && name.toLowerCase().indexOf("soundmax") >= 0) {
                        this.soundMax = true;
                    }
                }
            }
        }
        this.format = new AudioFormat((float) Audio.sampleRate, 16, QueueBuss.stereo ? 2 : 1, true, false);
        this.bytes = new byte[0x100 << (QueueBuss.stereo ? 2 : 1)];
    }
}
