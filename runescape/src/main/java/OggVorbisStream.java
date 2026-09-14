import com.jagex.game.runetek6.sound.Audio;
import com.jagex.game.runetek6.sound.OggStream;
import com.jagex.sound.DoublyLinkedNode_Sub2_Sub8;
import com.jagex.sound.QueueBuss;
import com.jagex.sound.SampleRateConverter;
import jagtheora.ogg.OggPacket;
import jagtheora.ogg.OggStreamState;
import jagtheora.vorbis.DSPState;
import jagtheora.vorbis.VorbisBlock;
import jagtheora.vorbis.VorbisComment;
import jagtheora.vorbis.VorbisInfo;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ik")
public final class OggVorbisStream extends OggStream {

    @OriginalMember(owner = "client!ik", name = "C", descriptor = "Lclient!wc;")
    public QueueBuss aClass2_Sub6_Sub5_1;

    @OriginalMember(owner = "client!ik", name = "O", descriptor = "Lclient!jagtheora/vorbis/DSPState;")
    public DSPState dspState;

    @OriginalMember(owner = "client!ik", name = "B", descriptor = "I")
    public int anInt4396;

    @OriginalMember(owner = "client!ik", name = "Q", descriptor = "Lclient!lg;")
    public SampleRateConverter converter;

    @OriginalMember(owner = "client!ik", name = "N", descriptor = "D")
    public double aDouble14;

    @OriginalMember(owner = "client!ik", name = "K", descriptor = "Lclient!jagtheora/vorbis/VorbisBlock;")
    public VorbisBlock vorbisBlock;

    @OriginalMember(owner = "client!ik", name = "v", descriptor = "Lclient!jagtheora/vorbis/VorbisInfo;")
    public final VorbisInfo vorbisInfo = new VorbisInfo();

    @OriginalMember(owner = "client!ik", name = "E", descriptor = "Lclient!jagtheora/vorbis/VorbisComment;")
    public final VorbisComment vorbisComment = new VorbisComment();

    @OriginalMember(owner = "client!ik", name = "<init>", descriptor = "(Lclient!jagtheora/ogg/OggStreamState;)V")
    public OggVorbisStream(@OriginalArg(0) OggStreamState state) {
        super(state);
    }

    @OriginalMember(owner = "client!ik", name = "b", descriptor = "(I)V")
    @Override
    public void stop() {
        if (this.vorbisBlock != null) {
            this.vorbisBlock.cleanUp();
        }

        if (this.dspState != null) {
            this.dspState.cleanUp();
        }

        this.vorbisComment.cleanUp();
        this.vorbisInfo.cleanUp();

        if (this.aClass2_Sub6_Sub5_1 != null) {
            this.aClass2_Sub6_Sub5_1.finish();
        }
    }

    @OriginalMember(owner = "client!ik", name = "c", descriptor = "(I)Lclient!wc;")
    public QueueBuss method3960() {
        return this.aClass2_Sub6_Sub5_1;
    }

    @OriginalMember(owner = "client!ik", name = "b", descriptor = "(ILclient!jagtheora/ogg/OggPacket;)V")
    @Override
    protected void decode(@OriginalArg(1) OggPacket packet) {
        if (super.packetNumber < 3) {
            @Pc(137) int ret = this.vorbisInfo.headerIn(this.vorbisComment, packet);
            if (ret < 0) {
                throw new IllegalStateException(String.valueOf(ret));
            }

            if (super.packetNumber == 2) {
                if (this.vorbisInfo.channels > 2 || this.vorbisInfo.channels < 1) {
                    throw new RuntimeException(String.valueOf(this.vorbisInfo.channels));
                }
                this.dspState = new DSPState(this.vorbisInfo);
                this.vorbisBlock = new VorbisBlock(this.dspState);
                this.converter = new SampleRateConverter(this.vorbisInfo.rate, Audio.sampleRate);
                this.aClass2_Sub6_Sub5_1 = new QueueBuss(this.vorbisInfo.channels);
            }
            return;
        }
        if (this.vorbisBlock.synthesis(packet) == 0) {
            this.dspState.blockIn(this.vorbisBlock);
        }
        @Pc(35) float[][] local35 = this.dspState.pcmOut(this.vorbisInfo.channels);
        this.aDouble14 = this.dspState.granuleTime();
        if (this.aDouble14 == -1.0D) {
            this.aDouble14 = (float) this.anInt4396 / (float) this.vorbisInfo.rate;
        }
        this.dspState.read(local35[0].length);
        this.anInt4396 += local35[0].length;
        @Pc(85) DoublyLinkedNode_Sub2_Sub8 local85 = this.aClass2_Sub6_Sub5_1.allocBlock(local35[0].length, this.aDouble14);
        Static373.method5300(local35, local85.aShortArrayArray3);
        for (@Pc(93) int local93 = 0; local93 < this.vorbisInfo.channels; local93++) {
            local85.aShortArrayArray3[local93] = this.converter.convert(local85.aShortArrayArray3[local93]);
        }
        this.aClass2_Sub6_Sub5_1.add(local85);
    }

    @OriginalMember(owner = "client!ik", name = "b", descriptor = "(B)D")
    public double getTime() {
        @Pc(13) double local13 = this.aDouble14;
        if (this.aClass2_Sub6_Sub5_1 != null) {
            local13 = this.aClass2_Sub6_Sub5_1.time(false);
            if (local13 < 0.0D) {
                local13 = this.aDouble14;
            }
        }
        return (double) -(256.0F / (float) Audio.sampleRate) + local13;
    }

    @OriginalMember(owner = "client!ik", name = "h", descriptor = "(I)I")
    public int method3965() {
        return this.aClass2_Sub6_Sub5_1 == null ? 0 : this.aClass2_Sub6_Sub5_1.size();
    }
}
