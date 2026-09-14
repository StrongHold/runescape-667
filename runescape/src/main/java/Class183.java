import com.ms.com.ComFailException;
import com.ms.com._Guid;
import com.ms.directX.DSBufferDesc;
import com.ms.directX.DSCursors;
import com.ms.directX.DirectSound;
import com.ms.directX.DirectSoundBuffer;
import com.ms.directX.WaveFormatEx;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Component;

@OriginalClass("client!ir")
public final class Class183 implements PcmDevice {

    @OriginalMember(owner = "client!ir", name = "m", descriptor = "I")
    public int sampleRate;

    @OriginalMember(owner = "client!ir", name = "h", descriptor = "I")
    public int frameBytes;

    @OriginalMember(owner = "client!ir", name = "e", descriptor = "I")
    public int channels;

    @OriginalMember(owner = "client!ir", name = "k", descriptor = "[I")
    public int[] silence;

    @OriginalMember(owner = "client!ir", name = "i", descriptor = "[Lcom/ms/directX/DSBufferDesc;")
    public final DSBufferDesc[] bufferDescs = new DSBufferDesc[2];

    @OriginalMember(owner = "client!ir", name = "j", descriptor = "[Z")
    public final boolean[] playing = new boolean[2];

    @OriginalMember(owner = "client!ir", name = "l", descriptor = "[Lcom/ms/directX/DSCursors;")
    public final DSCursors[] cursors = new DSCursors[2];

    @OriginalMember(owner = "client!ir", name = "d", descriptor = "[I")
    public final int[] writePositions = new int[2];

    @OriginalMember(owner = "client!ir", name = "b", descriptor = "[Lcom/ms/directX/DirectSoundBuffer;")
    public final DirectSoundBuffer[] buffers = new DirectSoundBuffer[2];

    @OriginalMember(owner = "client!ir", name = "g", descriptor = "[I")
    public final int[] bufferSizes = new int[2];

    @OriginalMember(owner = "client!ir", name = "c", descriptor = "[[B")
    public final byte[][] bytes = new byte[2][];

    @OriginalMember(owner = "client!ir", name = "f", descriptor = "Lcom/ms/directX/DirectSound;")
    public final DirectSound directSound = new DirectSound();

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "Lcom/ms/directX/WaveFormatEx;")
    public final WaveFormatEx waveFormat = new WaveFormatEx();

    @OriginalMember(owner = "client!ir", name = "<init>", descriptor = "()V")
    public Class183() throws Exception {
        for (@Pc(41) int index = 0; index < 2; index++) {
            this.bufferDescs[index] = new DSBufferDesc();
        }
        for (@Pc(57) int index = 0; index < 2; index++) {
            this.cursors[index] = new DSCursors();
        }
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(I[I)V")
    @Override
    public void write(@OriginalArg(0) int index, @OriginalArg(1) int[] samples) {
        @Pc(2) int count = samples.length;
        if (count != this.channels * 256) {
            throw new IllegalArgumentException();
        }
        @Pc(21) int offset = this.writePositions[index] * this.frameBytes;
        for (@Pc(23) int sampleIndex = 0; sampleIndex < count; sampleIndex++) {
            @Pc(28) int sample = samples[sampleIndex];
            if ((sample + 8388608 & 0xFF000000) != 0) {
                sample = sample >> 31 ^ 0x7FFFFF;
            }
            this.bytes[index][offset + sampleIndex * 2] = (byte) (sample >> 8);
            this.bytes[index][offset + sampleIndex * 2 + 1] = (byte) (sample >> 16);
        }
        this.buffers[index].writeBuffer(offset, count * 2, this.bytes[index], 0);
        this.writePositions[index] = this.writePositions[index] + count / this.channels & 0xFFFF;
        if (!this.playing[index]) {
            this.buffers[index].play(1);
            this.playing[index] = true;
        }
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(II)I")
    @Override
    public int position(@OriginalArg(0) int index) {
        if (!this.playing[index]) {
            return 0;
        }
        this.buffers[index].getCurrentPosition(this.cursors[index]);
        @Pc(25) int playPosition = this.cursors[index].write / this.frameBytes;
        @Pc(35) int queued = this.writePositions[index] - playPosition & 0xFFFF;
        if (this.bufferSizes[index] < queued) {
            @Pc(63) int gap = playPosition - this.writePositions[index] & 0xFFFF;
            while (gap > 0) {
                gap -= 256;
                this.write(index, this.silence);
            }
            queued = this.writePositions[index] - playPosition & 0xFFFF;
        }
        return queued;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(IZLjava/awt/Component;B)V")
    @Override
    public void init(@OriginalArg(0) int sampleRate, @OriginalArg(1) boolean stereo, @OriginalArg(2) Component component) throws Exception {
        if (this.sampleRate != 0) {
            return;
        }
        if (sampleRate < 8000 || sampleRate > 48000) {
            throw new IllegalArgumentException();
        }
        this.channels = stereo ? 2 : 1;
        this.frameBytes = stereo ? 4 : 2;
        this.silence = new int[this.channels * 256];
        this.directSound.initialize(null);
        this.directSound.setCooperativeLevel(component, 2);
        for (@Pc(60) int index = 0; index < 2; index++) {
            this.bufferDescs[index].flags = 16384;
        }
        this.waveFormat.avgBytesPerSec = sampleRate * this.frameBytes;
        this.waveFormat.formatTag = 1;
        this.waveFormat.channels = this.channels;
        this.waveFormat.blockAlign = this.frameBytes;
        this.waveFormat.samplesPerSec = sampleRate;
        this.sampleRate = sampleRate;
        this.waveFormat.bitsPerSample = 16;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(IZ)V")
    @Override
    public void close(@OriginalArg(0) int index) {
        if (this.buffers[index] == null) {
            return;
        }
        try {
            this.buffers[index].stop();
        } catch (@Pc(12) ComFailException ignored) {
            /* empty */
        }
        this.buffers[index] = null;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(III)V")
    @Override
    public void open(@OriginalArg(0) int bufferSize, @OriginalArg(1) int index) throws Exception {
        if (this.sampleRate == 0 || this.buffers[index] != null) {
            throw new IllegalStateException();
        }
        @Pc(22) int byteCount = this.frameBytes * 65536;
        if (this.bytes[index] == null || this.bytes[index].length != byteCount) {
            this.bytes[index] = new byte[byteCount];
            this.bufferDescs[index].bufferBytes = byteCount;
        }
        this.buffers[index] = this.directSound.createSoundBuffer(this.bufferDescs[index], this.waveFormat);
        this.playing[index] = false;
        this.writePositions[index] = 0;
        this.bufferSizes[index] = bufferSize;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(IB)V")
    @Override
    public void discardBuffer(@OriginalArg(0) int index) {
        try {
            this.buffers[index].stop();
        } catch (@Pc(12) ComFailException ignored) {
            /* empty */
        }
        this.playing[index] = false;
        this.buffers[index].setCurrentPosition(0);
        this.writePositions[index] = 0;
    }
}
