import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.constants.ModeWhere;
import com.jagex.core.util.JavaScript;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.io.IOException;

public final class Static314 {

    private static final int FRAME_COUNT = 16;

    private static final int FRAME_SIZE = 128 * 128;

    @OriginalMember(owner = "client!jw", name = "b", descriptor = "(B)V")
    public static void tbrefresh() {
        if (Client.modeWhere != ModeWhere.LOCAL) {
            try {
                JavaScript.call("tbrefresh", client.aClient1);
            } catch (@Pc(34) Throwable ignored) {
                /* empty */
            }
        }
    }

    @OriginalMember(owner = "client!jw", name = "a", descriptor = "(BFFFFI[BIIFILclient!tk;I)V")
    public static void generateNoiseFrames(@OriginalArg(1) float yFrequency, @OriginalArg(2) float zFrequency, @OriginalArg(3) float amplitude, @OriginalArg(4) float xFrequency, @OriginalArg(6) byte[] dest, @OriginalArg(8) int offset, @OriginalArg(9) float persistence, @OriginalArg(11) NoiseGenerator noise) {
        for (@Pc(5) int frame = 0; frame < FRAME_COUNT; frame++) {
            Static364.generateNoiseFrame(yFrequency, xFrequency, frame, noise, dest, persistence, zFrequency, offset, amplitude);
            offset += FRAME_SIZE;
        }
    }

    @OriginalMember(owner = "client!jw", name = "a", descriptor = "(Z)V")
    public static void method4567() {
        if (debugconsole.output != null) {
            try {
                debugconsole.output.close();
            } catch (@Pc(10) IOException ignored) {
                /* empty */
            }
        }
        debugconsole.output = null;
    }

    @OriginalMember(owner = "client!jw", name = "a", descriptor = "(ZI)V")
    public static void noTimeout(@OriginalArg(0) boolean forceSend) {
        Static557.updatePcmPlayers();

        if (!MainLogicStep.isAtGameScreen(MainLogicManager.step)) {
            return;
        }

        @Pc(13) ServerConnection[] connections = ServerConnection.VALUES;
        for (@Pc(15) int i = 0; i < connections.length; i++) {
            @Pc(20) ServerConnection connection = connections[i];

            connection.idleWriteTicks++;
            if (connection.idleWriteTicks < 50 && !forceSend) {
                return;
            }

            connection.idleWriteTicks = 0;

            if (!connection.errored && connection.connection != null) {
                @Pc(59) ClientMessage message = ClientMessage.create(ClientProt.NO_TIMEOUT, connection.isaac);

                connection.send(message);

                try {
                    connection.flush();
                } catch (@Pc(68) IOException ignored) {
                    connection.errored = true;
                }
            }
        }

        Static557.updatePcmPlayers();
    }
}
