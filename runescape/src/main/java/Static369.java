import com.jagex.Client;
import com.jagex.IndexedImage;
import com.jagex.core.io.Packet;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.JavaObjSprite;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;

public final class Static369 {

    @OriginalMember(owner = "client!lla", name = "e", descriptor = "[B")
    public static byte[] aByteArray43;

    @OriginalMember(owner = "client!lla", name = "a", descriptor = "(I)V")
    public static void method3847() {
        @Pc(11) int zoneCount = Static363.aByteArrayArray22.length;
        for (@Pc(13) int zone = 0; zone < zoneCount; zone++) {
            if (Static363.aByteArrayArray22[zone] != null) {
                @Pc(20) int zoneIndex = -1;
                for (@Pc(22) int i = 0; i < Static183.npcZoneCount; i++) {
                    if (Static89.zoneIds[zone] == Static119.anIntArray199[i]) {
                        zoneIndex = i;
                        break;
                    }
                }
                if (zoneIndex == -1) {
                    Static119.anIntArray199[Static183.npcZoneCount] = Static89.zoneIds[zone];
                    zoneIndex = Static183.npcZoneCount++;
                }
                @Pc(66) Packet packet = new Packet(Static363.aByteArrayArray22[zone]);
                @Pc(68) int npcIndex = 0;
                while (Static363.aByteArrayArray22[zone].length > packet.pos && npcIndex < 511 && NPCList.size < 1023) {
                    @Pc(88) int slot = zoneIndex | npcIndex++ << 6;
                    @Pc(94) int coord = packet.g2();
                    @Pc(98) int level = coord >> 14;
                    @Pc(104) int localX = coord >> 7 & 0x3F;
                    @Pc(108) int localZ = coord & 0x3F;
                    @Pc(121) int x = localX + (Static89.zoneIds[zone] >> 8) * 64 - WorldMap.areaBaseX;
                    @Pc(135) int z = (Static89.zoneIds[zone] & 0xFF) * 64 + localZ - WorldMap.areaBaseZ;
                    @Pc(142) NPCType type = NPCTypeList.instance.list(packet.g2());
                    @Pc(149) NPCEntityNode existing = (NPCEntityNode) NPCList.local.get(slot);
                    if (existing == null && (type.movementCapabilities & 0x1) > 0 && level == Static164.areaLevel && x >= 0 && type.size + x < Static720.mapWidth && z >= 0 && z + type.size < Static501.mapLength) {
                        @Pc(197) NPCEntity npc = new NPCEntity();
                        npc.slot = slot;
                        @Pc(205) NPCEntityNode node = new NPCEntityNode(npc);
                        NPCList.local.put(slot, node);
                        NPCList.entities[NPCList.newSize++] = node;
                        NPCList.slots[NPCList.size++] = slot;
                        npc.cutsceneClock = TimeUtils.clock;
                        npc.setupNewNPCType(type);
                        npc.setSize(npc.type.size);
                        npc.yawSpeed = npc.type.yawSpeed << 3;
                        npc.turn((npc.type.spawnDirection + 4 & 0x80600007) << 11, true);
                        npc.clearPath(true, z, x, level, npc.getSize());
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!lla", name = "a", descriptor = "(Lclient!ha;I)V")
    public static void updateObjSprites(@OriginalArg(0) Toolkit toolkit) {
        if (JavaToolkit.objSprites.size() == 0) {
            return;
        }

        if (ClientOptions.instance.toolkit.getValue() == ToolkitType.JAVA) {
            for (@Pc(31) JavaObjSprite sprite = (JavaObjSprite) JavaToolkit.objSprites.first(); sprite != null; sprite = (JavaObjSprite) JavaToolkit.objSprites.next()) {
                ObjTypeList.instance.sprite(sprite.outline, toolkit, toolkit, sprite.objWearCol ? PlayerEntity.self.playerModel : null, false, sprite.graphicShadow, sprite.invCount, false, sprite.objNumMode, Fonts.p11, sprite.objId);
                sprite.unlink();
            }

            InterfaceManager.redrawAll();
            return;
        }

        if (Static158.objSpriteToolkit == null) {
            @Pc(85) Canvas canvas = new Canvas();
            canvas.setSize(36, 32);
            Static158.objSpriteToolkit = Static255.create(ToolkitType.JAVA, js5.SHADERS, canvas, Js5TextureSource.instance, 0);
            Fonts.objSpriteFont = Static158.objSpriteToolkit.createFont(FontMetrics.loadGroup(js5.FONTMETRICS, Fonts.p11FullGroup), IndexedImage.load(js5.SPRITES, Fonts.p11FullGroup, 0), true);
        }

        for (@Pc(31) JavaObjSprite sprite = (JavaObjSprite) JavaToolkit.objSprites.first(); sprite != null; sprite = (JavaObjSprite) JavaToolkit.objSprites.next()) {
            ObjTypeList.instance.sprite(sprite.outline, Static158.objSpriteToolkit, toolkit, sprite.objWearCol ? PlayerEntity.self.playerModel : null, false, sprite.graphicShadow, sprite.invCount, false, sprite.objNumMode, Fonts.objSpriteFont, sprite.objId);
            sprite.unlink();
        }
    }

    @OriginalMember(owner = "client!lla", name = "b", descriptor = "(B)V")
    public static void method3852() {
        if (Client.ssKey != null) {
            LoginManager.checkLobbySession();
        } else if (LoginManager.socialNetworkId == -1) {
            LoginManager.doLobbyLogin(LoginManager.password, LoginManager.username);
        } else {
            LoginManager.doLobbySnLogin();
        }
    }
}
