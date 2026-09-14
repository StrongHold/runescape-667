import com.jagex.game.runetek6.config.defaults.MapDefaults;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static613 {

    @OriginalMember(owner = "client!tga", name = "a", descriptor = "(Lclient!ha;III)V")
    public static void method8239(@OriginalArg(0) Toolkit arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        Static665.aEnvironmentArrayArray1 = new Environment[arg2][arg1];
        Static425.toolkit = arg0;
        if (MapDefaults.skyboxes != null) {
            Static226.aClass67_9 = Environment.cubeMap(MapDefaults.skyboxes[4], MapDefaults.skyboxes[1], MapDefaults.skyboxes[2], MapDefaults.skyboxes[3], MapDefaults.skyboxes[5], MapDefaults.skyboxes[0]);
        }
        Static495.aSkyBox_4 = null;
        Static556.aEnvironment_2 = new Environment();
        Static508.method6750();
    }
}
