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
        for (@Pc(41) int local41 = 0; local41 < 2; local41++) {
            this.bufferDescs[local41] = new DSBufferDesc();
        }
        for (@Pc(57) int local57 = 0; local57 < 2; local57++) {
            this.cursors[local57] = new DSCursors();
        }
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(I[I)V")
    @Override
    public void write(@OriginalArg(0) int arg0, @OriginalArg(1) int[] arg1) {
        @Pc(2) int local2 = arg1.length;
        if (local2 != this.channels * 256) {
            throw new IllegalArgumentException();
        }
        @Pc(21) int local21 = this.writePositions[arg0] * this.frameBytes;
        for (@Pc(23) int local23 = 0; local23 < local2; local23++) {
            @Pc(28) int local28 = arg1[local23];
            if ((local28 + 8388608 & 0xFF000000) != 0) {
                local28 = local28 >> 31 ^ 0x7FFFFF;
            }
            this.bytes[arg0][local21 + local23 * 2] = (byte) (local28 >> 8);
            this.bytes[arg0][local21 + local23 * 2 + 1] = (byte) (local28 >> 16);
        }
        this.buffers[arg0].writeBuffer(local21, local2 * 2, this.bytes[arg0], 0);
        this.writePositions[arg0] = this.writePositions[arg0] + local2 / this.channels & 0xFFFF;
        if (!this.playing[arg0]) {
            this.buffers[arg0].play(1);
            this.playing[arg0] = true;
        }
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(II)I")
    @Override
    public int position(@OriginalArg(0) int arg0) {
        if (!this.playing[arg0]) {
            return 0;
        }
        this.buffers[arg0].getCurrentPosition(this.cursors[arg0]);
        @Pc(25) int local25 = this.cursors[arg0].write / this.frameBytes;
        @Pc(35) int local35 = this.writePositions[arg0] - local25 & 0xFFFF;
        if (this.bufferSizes[arg0] < local35) {
            @Pc(63) int local63 = local25 - this.writePositions[arg0] & 0xFFFF;
            while (local63 > 0) {
                local63 -= 256;
                this.write(arg0, this.silence);
            }
            local35 = this.writePositions[arg0] - local25 & 0xFFFF;
        }
        return local35;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(IZLjava/awt/Component;B)V")
    @Override
    public void init(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Component arg2) throws Exception {
        if (this.sampleRate != 0) {
            return;
        }
        if (arg0 < 8000 || arg0 > 48000) {
            throw new IllegalArgumentException();
        }
        this.channels = arg1 ? 2 : 1;
        this.frameBytes = arg1 ? 4 : 2;
        this.silence = new int[this.channels * 256];
        this.directSound.initialize(null);
        this.directSound.setCooperativeLevel(arg2, 2);
        for (@Pc(60) int local60 = 0; local60 < 2; local60++) {
            this.bufferDescs[local60].flags = 16384;
        }
        this.waveFormat.avgBytesPerSec = arg0 * this.frameBytes;
        this.waveFormat.formatTag = 1;
        this.waveFormat.channels = this.channels;
        this.waveFormat.blockAlign = this.frameBytes;
        this.waveFormat.samplesPerSec = arg0;
        this.sampleRate = arg0;
        this.waveFormat.bitsPerSample = 16;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(IZ)V")
    @Override
    public void close(@OriginalArg(0) int arg0) {
        if (this.buffers[arg0] == null) {
            return;
        }
        try {
            this.buffers[arg0].stop();
        } catch (@Pc(12) ComFailException local12) {
        }
        this.buffers[arg0] = null;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(III)V")
    @Override
    public void open(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) throws Exception {
        if (this.sampleRate == 0 || this.buffers[arg1] != null) {
            throw new IllegalStateException();
        }
        @Pc(22) int local22 = this.frameBytes * 65536;
        if (this.bytes[arg1] == null || this.bytes[arg1].length != local22) {
            this.bytes[arg1] = new byte[local22];
            this.bufferDescs[arg1].bufferBytes = local22;
        }
        this.buffers[arg1] = this.directSound.createSoundBuffer(this.bufferDescs[arg1], this.waveFormat);
        this.playing[arg1] = false;
        this.writePositions[arg1] = 0;
        this.bufferSizes[arg1] = arg0;
    }

    @OriginalMember(owner = "client!ir", name = "a", descriptor = "(IB)V")
    @Override
    public void discardBuffer(@OriginalArg(0) int arg0) {
        try {
            this.buffers[arg0].stop();
        } catch (@Pc(12) ComFailException ignored) {
        }
        this.playing[arg0] = false;
        this.buffers[arg0].setCurrentPosition(0);
        this.writePositions[arg0] = 0;
    }
}
