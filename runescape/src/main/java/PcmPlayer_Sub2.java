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
    public final int anInt4104;

    @OriginalMember(owner = "client!hq", name = "<init>", descriptor = "(Lclient!vq;I)V")
    public PcmPlayer_Sub2(@OriginalArg(0) SignLink arg0, @OriginalArg(1) int arg1) {
        Static253.pcmDevice = (PcmDevice) arg0.method8976();
        this.anInt4104 = arg1;
    }

    @OriginalMember(owner = "client!hq", name = "b", descriptor = "(I)V")
    @Override
    public void method3588(@OriginalArg(0) int arg0) throws Exception {
        if (arg0 > 32768) {
            throw new IllegalArgumentException();
        }
        Static253.pcmDevice.open(arg0, this.anInt4104);
    }

    @OriginalMember(owner = "client!hq", name = "d", descriptor = "()I")
    @Override
    protected int method3587() {
        return Static253.pcmDevice.position(this.anInt4104);
    }

    @OriginalMember(owner = "client!hq", name = "a", descriptor = "()V")
    @Override
    protected void method3583() {
        Static253.pcmDevice.discardBuffer(this.anInt4104);
    }

    @OriginalMember(owner = "client!hq", name = "a", descriptor = "(Ljava/awt/Component;)V")
    @Override
    public void method3593(@OriginalArg(0) Component arg0) throws Exception {
        Static253.pcmDevice.init(Audio.sampleRate, QueueBuss.stereo, arg0);
    }

    @OriginalMember(owner = "client!hq", name = "b", descriptor = "()V")
    @Override
    protected void method3590() {
        Static253.pcmDevice.write(this.anInt4104, super.anIntArray315);
    }

    @OriginalMember(owner = "client!hq", name = "c", descriptor = "()V")
    @Override
    protected void method3596() {
        Static253.pcmDevice.close(this.anInt4104);
    }
}
