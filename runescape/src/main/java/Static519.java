import com.jagex.graphics.Renderer;
import com.jagex.sign.SignLink;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static519 {

    @OriginalMember(owner = "client!qfa", name = "b", descriptor = "(B)I")
    public static int autosetup() {
        @Pc(5) boolean sseAllowed = false;
        @Pc(7) boolean glAllowed = false;
        @Pc(9) boolean d3dAllowed = false;
        if (GameShell.signLink.signed && !GameShell.signLink.microsoftjava) {
            sseAllowed = true;
            if (SystemInfo.instance.totalMemory < 512 && SystemInfo.instance.totalMemory != 0) {
                sseAllowed = false;
            }
            if (SignLink.osNameLower.startsWith("win")) {
                d3dAllowed = true;
                glAllowed = true;
            } else {
                glAllowed = true;
            }
        }
        if (Static698.aBoolean792) {
            glAllowed = false;
        }
        if (Static78.aBoolean139) {
            d3dAllowed = false;
        }
        if (Static449.aBoolean511) {
            sseAllowed = false;
        }
        if (!sseAllowed && !glAllowed && !d3dAllowed) {
            return Static625.method8337();
        }
        @Pc(82) int sseScore = -1;
        @Pc(84) int glScore = -1;
        @Pc(86) int d3dScore = -1;
        if (sseAllowed) {
            try {
                sseScore = Static363.profileToolkit(1000, ToolkitType.SSE);
            } catch (@Pc(95) Exception local95) {
                /* empty */
            }
        }
        if (d3dAllowed) {
            try {
                d3dScore = Static363.profileToolkit(1000, ToolkitType.D3D);
                if (ClientOptions.instance.toolkit.getValue() == ToolkitType.D3D) {
                    @Pc(114) Renderer renderer = Toolkit.active.renderer();
                    @Pc(119) long driverVersion = renderer.driverVersion & 0xFFFFFFFFFFFFL;
                    @Pc(122) int vendor = renderer.vendor;
                    if (vendor == 4318) {
                        glAllowed &= driverVersion >= 64425238954L;
                    } else if (vendor == 4098) {
                        glAllowed &= driverVersion >= 60129613779L;
                    }
                }
            } catch (@Pc(161) Exception local161) {
                /* empty */
            }
        }
        if (glAllowed) {
            try {
                glScore = Static363.profileToolkit(1000, ToolkitType.GL);
            } catch (@Pc(171) Exception local171) {
                /* empty */
            }
        }
        if (sseScore == -1 && glScore == -1 && d3dScore == -1) {
            return Static625.method8337();
        }
        glScore = (int) ((float) glScore * 1.1F);
        d3dScore = (int) ((float) d3dScore * 1.1F);
        if (sseScore > d3dScore && sseScore > glScore) {
            return Static611.method8228(sseScore);
        } else if (d3dScore > glScore) {
            return Static399.autosetupHardware(ToolkitType.D3D, d3dScore);
        } else {
            return Static399.autosetupHardware(ToolkitType.GL, glScore);
        }
    }

    @OriginalMember(owner = "client!qfa", name = "a", descriptor = "(III)Z")
    public static boolean colourBufferMutable(@OriginalArg(0) int arg0, @OriginalArg(1) int functionMask, @OriginalArg(2) int flags) {
        if (Static280.method4087(flags, functionMask)) {
            return Static77.method1560(flags, functionMask) | (functionMask & 0x9000) != 0 | Static433.method5601(flags, functionMask) ? true : (flags & 0x37) == 0 & (Static526.normalsMutable(flags, functionMask) | (functionMask & 0x2000) != 0 | Static220.method3197(functionMask, flags));
        } else {
            return false;
        }
    }
}
