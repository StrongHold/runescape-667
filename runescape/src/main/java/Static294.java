import com.jagex.Client;
import com.jagex.game.camera.CameraMode;
import com.jagex.game.camera.Shake;
import com.jagex.core.io.Packet;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.DelayedStateChange;
import com.jagex.game.LocalisedText;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.texture.TextureOp;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static294 {

    @OriginalMember(owner = "client!jg", name = "a", descriptor = "(Lclient!tca;II[B)Lclient!fr;")
    public static Class135 method4338(@OriginalArg(0) GlxToolkit toolkit, @OriginalArg(3) byte[] program) {
        if (program == null) {
            return null;
        }
        @Pc(16) int programId = OpenGL.glGenProgramARB();
        OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, programId);
        OpenGL.glProgramRawARB(34336, 34933, program);
        OpenGL.glGetIntegerv(OpenGL.GL_PROGRAM_ERROR_POSITION_ARB, Static564.anIntArray653, 0);
        if (Static564.anIntArray653[0] == -1) {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, 0);
            return new Class135(toolkit, 34336, programId);
        } else {
            OpenGL.glBindProgramARB(OpenGL.GL_VERTEX_PROGRAM_ARB, 0);
            return null;
        }
    }

    @OriginalMember(owner = "client!jg", name = "a", descriptor = "(IIZIII)V")
    public static void method4339(@OriginalArg(1) int y, @OriginalArg(2) boolean login, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int x) {
        if (Static334.activeTiles == null) {
            Toolkit.active.fillRect(x, y, width, height, -16777216);
            return;
        }
        @Pc(19) boolean blank = false;
        if (CutsceneManager.state == 0) {
            if (PlayerEntity.self.x < 0 || PlayerEntity.self.x >= Static720.mapWidth * 512 || PlayerEntity.self.z < 0 || PlayerEntity.self.z >= Static501.mapLength * 512) {
                blank = true;
            }
        } else if (!CutsceneManager.aBoolean480) {
            blank = true;
        }
        if (blank) {
            Toolkit.active.fillRect(x, y, width, height, -16777216);
            return;
        }
        Static198.anInt3276++;
        if ((PlayerEntity.self != null) && (Minimap.flagX == (((PlayerEntity.self.x + 256) - (PlayerEntity.self.getSize() * 256)) >> 9)) && (Minimap.flagY == ((PlayerEntity.self.z - ((PlayerEntity.self.getSize() - 1) * 256)) >> 9))) {
            Minimap.flagX = -1;
            Minimap.flagY = -1;
            DelayedStateChange.resetMapFlag();
        }
        Static710.buildScene();
        if (!login) {
            Static527.method7083();
        }
        Static498.method6643(x, true, y, height, width);
        @Pc(153) int viewportX = Static32.anInt773;
        @Pc(155) int viewportWidth = Static242.anInt3971;
        OrthoMode.renderZoom = OrthoMode.zoom;
        @Pc(159) int viewportY = Static290.anInt4657;
        @Pc(161) int viewportHeight = Static200.anInt3305;
        @Pc(169) int local169;
        @Pc(204) int local204;
        if (Camera.mode == CameraMode.MODE_DEFAULT) {
            local169 = (int) Camera.playerCameraPitch;
            if (Static188.anInt3103 >> 8 > local169) {
                local169 = Static188.anInt3103 >> 8;
            }
            if (Shake.enabled[4] && local169 < Shake.amplitude[4] + 128) {
                local169 = Shake.amplitude[4] + 128;
            }
            local204 = Static288.anInt4621 + (int) Camera.playerCameraYaw & 0x3FFF;
            Camera.method4606(local169, (local169 >> 3) * 3 + 600 << 2, viewportHeight, Static494.anInt7409, local204, Static38.anInt920, Static102.averageHeight(Camera.renderingLevel, PlayerEntity.self.x, PlayerEntity.self.z) - 200);
        } else if (Camera.mode == CameraMode.MODE_FOLLOWCOORD) {
            local169 = (int) Camera.playerCameraPitch;
            if (Static188.anInt3103 >> 8 > local169) {
                local169 = Static188.anInt3103 >> 8;
            }
            if (Shake.enabled[4] && Shake.amplitude[4] + 128 > local169) {
                local169 = Shake.amplitude[4] + 128;
            }
            local204 = (int) Camera.playerCameraYaw & 0x3FFF;
            Camera.method4606(local169, (local169 >> 3) * 3 + 600 << 2, viewportHeight, Static494.anInt7409, local204, Static38.anInt920, Static102.averageHeight(Camera.renderingLevel, Camera.anInt6262, Camera.anInt4018) - 200);
        } else if (Camera.mode == CameraMode.MODE_SMOOTH_RESET) {
            Camera.update(viewportHeight);
        }
        local169 = Camera.x;
        local204 = Camera.y;
        @Pc(325) int savedCameraZ = Camera.z;
        @Pc(327) int savedCameraPitch = Camera.pitch;
        @Pc(329) int savedCameraYaw = Camera.yaw;
        @Pc(375) int local375;
        for (@Pc(331) int direction = 0; direction < Shake.DIRECTIONS; direction++) {
            if (Shake.enabled[direction]) {
                local375 = (int) ((double) -Shake.center[direction] + (double) (Shake.center[direction] * 2 + 1) * Math.random() + Math.sin((double) Shake.frequency[direction] / 100.0D * (double) Shake.time[direction]) * (double) Shake.amplitude[direction]);
                if (direction == 3) {
                    Camera.yaw = local375 + Camera.yaw & 0x3FFF;
                }
                if (direction == 0) {
                    Camera.x += local375 << 2;
                }
                if (direction == 1) {
                    Camera.y += local375 << 2;
                }
                if (direction == 2) {
                    Camera.z += local375 << 2;
                }
                if (direction == 4) {
                    Camera.pitch += local375;
                    if (Camera.pitch < 1024) {
                        Camera.pitch = 1024;
                    } else if (Camera.pitch > 3072) {
                        Camera.pitch = 3072;
                    }
                }
            }
        }
        if (Camera.x < 0) {
            Camera.x = 0;
        }
        if (Camera.z < 0) {
            Camera.z = 0;
        }
        if (Camera.x > (Static619.tileMaxX << 9) - 1) {
            Camera.x = (Static619.tileMaxX << 9) - 1;
        }
        if (Camera.z > (Static662.tileMaxZ << 9) - 1) {
            Camera.z = (Static662.tileMaxZ << 9) - 1;
        }
        Static276.hideRoofsForFrame();
        Toolkit.active.KA(viewportX, viewportY, viewportWidth + viewportX, viewportY + viewportHeight);
        Static501.method6716(true);
        local375 = Static251.anInt4037;
        Static460.aMatrix_10.createCamera(Camera.x, Camera.y, Camera.z, -Camera.pitch & 0x3FFF, -Camera.yaw & 0x3FFF, -Camera.roll & 0x3FFF);
        Toolkit.active.setCamera(Static460.aMatrix_10);
        Toolkit.active.DA(viewportWidth / 2 + viewportX, viewportHeight / 2 + viewportY, Static428.anInt6495 << 1, Static428.anInt6495 << 1);
        if (OrthoMode.toolkitActive) {
            OrthoMode.method6448(Static251.anInt4037);
            if (Static162.anInt2800 != OrthoMode.renderZoom) {
                Static75.hasOpaqueStationaryEntities = true;
            }
            Static162.anInt2800 = OrthoMode.renderZoom;
            Toolkit.active.GA(local375);
            Toolkit.active.ya();
        } else if (Static456.aSkyBox_3 == null) {
            Toolkit.active.GA(local375);
            Toolkit.active.ya();
        } else {
            Toolkit.active.xa(1.0F);
            Toolkit.active.ZA(0xFFFFFF, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
            Static456.aSkyBox_3.method3159(viewportY, Toolkit.active, Camera.yaw, viewportWidth, local375, Camera.roll, viewportHeight, viewportX, Static436.anInt3852 << 3, Camera.pitch);
        }
        Static557.method7331();
        Static527.method7081(Static428.anInt6495 << 1, viewportHeight / 2 + viewportY, Static428.anInt6495 << 1, viewportX + viewportWidth / 2);
        Static620.method8324(-Camera.yaw & 0x3FFF, Camera.z, -Camera.roll & 0x3FFF, Camera.y, -Camera.pitch & 0x3FFF, Camera.x);
        Static159.method2575();
        @Pc(688) byte roofStamp = ClientOptions.instance.removeRoofsOverride.getValue() == 2 ? (byte) Static198.anInt3276 : 1;
        if (OrthoMode.toolkitActive) {
            Static648.method8483(-Camera.yaw & 0x3FFF, -Camera.roll & 0x3FFF, -Camera.pitch & 0x3FFF);
            OrthoMode.method6324(roofStamp, Camera.x, Camera.z, Camera.y, PlayerEntity.self.z >> 9, Static457.anIntArray552, Static682.anIntArray817, TimeUtils.clock, PlayerEntity.self.level + 1, Static482.anIntArray588, ClientOptions.instance.flickeringEffects.getValue() == 0, OrthoMode.renderZoom, Static9.anIntArray18, Static328.aByteArrayArrayArray4, Static153.anIntArray235, PlayerEntity.self.x >> 9);
        } else {
            SceneRenderer.renderScene(TimeUtils.clock, Camera.x, Camera.y, Camera.z, Static328.aByteArrayArrayArray4, Static482.anIntArray588, Static9.anIntArray18, Static457.anIntArray552, Static153.anIntArray235, Static682.anIntArray817, PlayerEntity.self.level + 1, roofStamp, PlayerEntity.self.x >> 9, PlayerEntity.self.z >> 9, ClientOptions.instance.flickeringEffects.getValue() == 0, OrthoMode.enabled ? OrthoMode.renderZoom : -1, 0, false);
        }
        Static557.method7331();
        if (MainLogicManager.step == 11) {
            Static205.method3091(viewportWidth, viewportHeight, viewportY, viewportX);
            OverlayManager.render(viewportY, viewportWidth, viewportX, viewportHeight);
            Static233.method3407(viewportWidth, viewportY, viewportX, viewportHeight);
            Static208.method3105(viewportWidth, viewportX, viewportY, viewportHeight);
        }
        Static712.clearDynamicEntities();
        Camera.z = savedCameraZ;
        Camera.x = local169;
        Camera.pitch = savedCameraPitch;
        Camera.y = local204;
        Camera.yaw = savedCameraYaw;
        Static288.aBoolean356 = false;
        if (Static426.aBoolean72 && Client.js5WorkerThread.urgentCount() == 0) {
            Static426.aBoolean72 = false;
        }
        if (Static426.aBoolean72) {
            Toolkit.active.fillRect(viewportX, viewportY, viewportWidth, viewportHeight, -16777216);
            MessageBox.draw(Toolkit.active, LocalisedText.LOADING.localise(Client.language), false, Fonts.p12Metrics, Fonts.p12);
        }
        Static501.method6716(false);
    }

    @OriginalMember(owner = "client!jg", name = "a", descriptor = "(ILclient!ge;)Lclient!pf;")
    public static TextureOp readTextureOp(@OriginalArg(1) Packet packet) {
        packet.g1();
        @Pc(13) int type = packet.g1();
        @Pc(17) TextureOp op = Static96.newTextureOp(type);
        op.cacheSize = packet.g1();
        @Pc(32) int count = packet.g1();
        for (@Pc(34) int i = 0; i < count; i++) {
            @Pc(40) int opcode = packet.g1();
            op.method9416(false, packet, opcode);
        }
        op.postDecode();
        return op;
    }
}
