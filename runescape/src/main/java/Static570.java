import com.jagex.IndexedImage;
import com.jagex.core.io.Packet;
import com.jagex.game.Class14;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static570 {

    @OriginalMember(owner = "client!rv", name = "p", descriptor = "Lclient!lga;")
    public static final Class225 aClass225_208 = new Class225(99, -1);

    @OriginalMember(owner = "client!rv", name = "a", descriptor = "(BLclient!ha;)V")
    public static void method7549(@OriginalArg(1) Toolkit arg0) {
        Static378.aClass70Array1 = new Class70[Static552.anIntArray753.length];
        for (@Pc(14) int local14 = 0; local14 < Static552.anIntArray753.length; local14++) {
            @Pc(19) int local19 = Static552.anIntArray753[local14];
            @Pc(24) FontMetrics local24 = Static238.method3468(local19, Static237.aJs5_87);
            @Pc(32) Class14 local32 = arg0.method8010(local24, IndexedImage.load(Static555.aJs5_106, local19), true);
            Static378.aClass70Array1[local14] = new Class70(local32, local24);
        }
    }

    @OriginalMember(owner = "client!rv", name = "c", descriptor = "(I)Lclient!nl;")
    public static Class27 method7550() {
        try {
            return new Class27_Sub3();
        } catch (@Pc(8) Throwable local8) {
            try {
                return (Class27) Class.forName("Class27_Sub2").getDeclaredConstructor().newInstance();
            } catch (@Pc(16) Throwable local16) {
                return new Class27_Sub1();
            }
        }
    }

    @OriginalMember(owner = "client!rv", name = "b", descriptor = "(I)Lclient!ge;")
    public static Packet method7552() {
        @Pc(8) Packet local8 = new Packet(518);
        Static219.anIntArray287 = new int[4];
        Static219.anIntArray287[2] = (int) (Math.random() * 9.9999999E7D);
        Static219.anIntArray287[0] = (int) (Math.random() * 9.9999999E7D);
        Static219.anIntArray287[3] = (int) (Math.random() * 9.9999999E7D);
        Static219.anIntArray287[1] = (int) (Math.random() * 9.9999999E7D);
        local8.p1(10);
        local8.p4(Static219.anIntArray287[0]);
        local8.p4(Static219.anIntArray287[1]);
        local8.p4(Static219.anIntArray287[2]);
        local8.p4(Static219.anIntArray287[3]);
        return local8;
    }

    @OriginalMember(owner = "client!rv", name = "a", descriptor = "(I)V")
    public static void method7553() {
        Static473.A_WEIGHTED_CACHE___157.removeSoftReferences();
        Static312.A_WEIGHTED_CACHE___106.removeSoftReferences();
        Static449.A_WEIGHTED_CACHE___146.removeSoftReferences();
        Static444.A_WEIGHTED_CACHE___145.removeSoftReferences();
    }
}
