import com.jagex.core.io.Packet;
import com.jagex.js5.js5;
import com.jagex.sound.midi.MidiSong;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

@OriginalClass("client!kha")
public final class CutsceneAction_Sub13 extends CutsceneAction {

    @OriginalMember(owner = "client!kha", name = "i", descriptor = "Lclient!bn;")
    public MidiSong aClass2_Sub8_4;

    @OriginalMember(owner = "client!kha", name = "n", descriptor = "I")
    public final int anInt5431;

    @OriginalMember(owner = "client!kha", name = "k", descriptor = "I")
    public final int anInt5433;

    @OriginalMember(owner = "client!kha", name = "<init>", descriptor = "(Lclient!ge;)V")
    public CutsceneAction_Sub13(@OriginalArg(0) Packet arg0) {
        super(arg0);
        this.anInt5431 = arg0.g2();
        this.anInt5433 = arg0.g1();
    }

    @OriginalMember(owner = "client!kha", name = "a", descriptor = "(Z)Z")
    @Override
    public boolean ready() {
        if (this.aClass2_Sub8_4 == null) {
            this.aClass2_Sub8_4 = Static728.method1153(js5.MIDI_SONGS, this.anInt5431, 0);
        }
        if (this.aClass2_Sub8_4 == null) {
            return false;
        } else {
            return Static311.preloadSong(this.aClass2_Sub8_4);
        }
    }

    @OriginalMember(owner = "client!kha", name = "b", descriptor = "(I)V")
    @Override
    public void execute() {
        Static706.method9221(this.aClass2_Sub8_4, this.anInt5433);
    }
}
