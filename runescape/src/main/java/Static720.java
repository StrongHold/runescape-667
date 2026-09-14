import com.jagex.Client;
import com.jagex.sign.SignedResource;
import com.jagex.sign.SignedResourceStatus;
import com.jagex.core.io.FileOnDisk;
import com.jagex.core.io.Packet;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.runetek6.client.GameShell;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.IOException;

public final class Static720 {

    @OriginalMember(owner = "client!wr", name = "n", descriptor = "I")
    public static int mapWidth = 104;

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(ILjava/awt/Canvas;)V")
    public static void method9397(@OriginalArg(1) Canvas arg0) {
        @Pc(14) Dimension local14 = arg0.getSize();
        OrthoMode.method5454(local14.height, local14.width);
        if (OrthoMode.mode == 1) {
            OrthoMode.toolkit.addCanvas(arg0, OrthoMode.surfaceWidth, OrthoMode.surfaceHeight);
        } else {
            OrthoMode.toolkit.addCanvas(arg0, OrthoMode.orthoWidth, OrthoMode.orthoHeight);
        }
    }

    @OriginalMember(owner = "client!wr", name = "a", descriptor = "(I)Lclient!kv;")
    public static ClientOptions method9398() {
        @Pc(13) FileOnDisk local13 = null;
        @Pc(19) ClientOptions local19 = new ClientOptions(Client.modeGame, 0);
        try {
            @Pc(25) SignedResource resource = GameShell.signLink.openPrefs("", true);
            while (resource.status == SignedResourceStatus.IDLE) {
                TimeUtils.sleep(1L);
            }

            if (resource.status == SignedResourceStatus.SUCCESS) {
                local13 = (FileOnDisk) resource.result;
                @Pc(51) byte[] local51 = new byte[(int) local13.length()];
                @Pc(66) int local66;
                for (@Pc(53) int local53 = 0; local53 < local51.length; local53 += local66) {
                    local66 = local13.read(local51.length - local53, local51, local53);
                    if (local66 == -1) {
                        throw new IOException("EOF");
                    }
                }
                local19 = new ClientOptions(new Packet(local51), Client.modeGame, 0);
            }
        } catch (@Pc(97) Exception local97) {
        }
        try {
            if (local13 != null) {
                local13.close();
            }
        } catch (@Pc(104) Exception local104) {
        }
        return local19;
    }
}
