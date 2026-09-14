import com.jagex.sign.SignLink;
import com.jagex.game.runetek6.sound.Audio;
import com.jagex.sound.QueueBuss;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

import java.awt.Component;

@OriginalClass("client!hq")
public final class PcmPlayer_Sub2 extends PcmPlayer {

    @OriginalMember(owner = "client!hq", name = "J", descriptor = "I")
    public final int deviceIndex;

    @OriginalMember(owner = "client!hq", name = "<init>", descriptor = "(Lclient!vq;I)V")
    public PcmPlayer_Sub2(@OriginalArg(0) SignLink signLink, @OriginalArg(1) int deviceIndex) {
        Static253.pcmDevice = (PcmDevice) signLink.method8976();
        this.deviceIndex = deviceIndex;
    }

    @OriginalMember(owner = "client!hq", name = "b", descriptor = "(I)V")
    @Override
    public void method3588(@OriginalArg(0) int capacity) throws Exception {
        if (capacity > 32768) {
            throw new IllegalArgumentException();
        }
        Static253.pcmDevice.open(capacity, this.deviceIndex);
    }

    @OriginalMember(owner = "client!hq", name = "d", descriptor = "()I")
    @Override
    protected int position() {
        return Static253.pcmDevice.position(this.deviceIndex);
    }

    @OriginalMember(owner = "client!hq", name = "a", descriptor = "()V")
    @Override
    protected void discardBuffer() {
        Static253.pcmDevice.discardBuffer(this.deviceIndex);
    }

    @OriginalMember(owner = "client!hq", name = "a", descriptor = "(Ljava/awt/Component;)V")
    @Override
    public void method3593(@OriginalArg(0) Component component) throws Exception {
        Static253.pcmDevice.init(Audio.sampleRate, QueueBuss.stereo, component);
    }

    @OriginalMember(owner = "client!hq", name = "b", descriptor = "()V")
    @Override
    protected void write() {
        Static253.pcmDevice.write(this.deviceIndex, super.anIntArray315);
    }

    @OriginalMember(owner = "client!hq", name = "c", descriptor = "()V")
    @Override
    protected void closeDevice() {
        Static253.pcmDevice.close(this.deviceIndex);
    }
}
