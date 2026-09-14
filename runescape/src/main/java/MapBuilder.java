import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.core.constants.AreaMode;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.util.SystemTimer;
import com.jagex.game.LocalisedText;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class MapBuilder {

    @OriginalMember(owner = "client!ph", name = "d", descriptor = "(I)V")
    public static void build() {
        Static314.noTimeout(false);
        Static593.anInt8763 = 0;

        @Pc(10) boolean loaded = true;
        for (@Pc(12) int i = 0; i < Static319.aByteArrayArray16.length; i++) {
            if (Static267.mapGroups[i] != -1 && Static319.aByteArrayArray16[i] == null) {
                Static319.aByteArrayArray16[i] = js5.MAPS.getfile(0, Static267.mapGroups[i]);
                if (Static319.aByteArrayArray16[i] == null) {
                    Static593.anInt8763++;
                    loaded = false;
                }
            }

            if (Static266.locationGroups[i] != -1 && Static118.aByteArrayArray3[i] == null) {
                Static118.aByteArrayArray3[i] = js5.MAPS.getfile(Static22.anIntArrayArray11[i], 0, Static266.locationGroups[i]);
                if (Static118.aByteArrayArray3[i] == null) {
                    loaded = false;
                    Static593.anInt8763++;
                }
            }

            if (Static68.underwaterMapGroups[i] != -1 && Static177.aByteArrayArray5[i] == null) {
                Static177.aByteArrayArray5[i] = js5.MAPS.getfile(0, Static68.underwaterMapGroups[i]);
                if (Static177.aByteArrayArray5[i] == null) {
                    Static593.anInt8763++;
                    loaded = false;
                }
            }

            if (Static298.underwaterLocationGroups[i] != -1 && Static421.aByteArrayArray19[i] == null) {
                Static421.aByteArrayArray19[i] = js5.MAPS.getfile(0, Static298.underwaterLocationGroups[i]);
                if (Static421.aByteArrayArray19[i] == null) {
                    Static593.anInt8763++;
                    loaded = false;
                }
            }

            if (Static376.npcGroups != null && Static363.aByteArrayArray22[i] == null && Static376.npcGroups[i] != -1) {
                Static363.aByteArrayArray22[i] = js5.MAPS.getfile(Static22.anIntArrayArray11[i], 0, Static376.npcGroups[i]);
                if (Static363.aByteArrayArray22[i] == null) {
                    Static593.anInt8763++;
                    loaded = false;
                }
            }
        }

        if (Minimap.elements == null) {
            if (Static162.aClass2_Sub2_Sub13_2 == null || !js5.WORLDMAPDATA.groupExists(Static162.aClass2_Sub2_Sub13_2.file + "_staticelements")) {
                Minimap.elements = new MapElementList(0);
            } else if (js5.WORLDMAPDATA.requestgroupdownload(Static162.aClass2_Sub2_Sub13_2.file + "_staticelements")) {
                Minimap.elements = MapElementList.load(Static174.mapMembers, js5.WORLDMAPDATA, Static162.aClass2_Sub2_Sub13_2.file + "_staticelements");
            } else {
                loaded = false;
                Static593.anInt8763++;
            }
        }

        if (!loaded) {
            Static213.anInt3472 = 1;
            return;
        }

        loaded = true;
        Static13.anInt150 = 0;
        for (@Pc(282) int i = 0; i < Static319.aByteArrayArray16.length; i++) {
            @Pc(287) byte[] data = Static118.aByteArrayArray3[i];
            @Pc(299) int x;

            if (data != null) {
                x = (Static89.zoneIds[i] >> 8) * 64 - WorldMap.areaBaseX;
                @Pc(310) int z = (Static89.zoneIds[i] & 0xFF) * 64 - WorldMap.areaBaseZ;

                if (Static117.areaMode != AreaMode.STATIC_AREA) {
                    x = 10;
                    z = 10;
                }

                loaded &= Static213.method3141(data, x, Static720.mapWidth, z, Static501.mapLength);
            }

            data = Static421.aByteArrayArray19[i];
            if (data != null) {
                x = (Static89.zoneIds[i] >> 8) * 64 - WorldMap.areaBaseX;
                @Pc(310) int z = (Static89.zoneIds[i] & 0xFF) * 64 - WorldMap.areaBaseZ;

                if (Static117.areaMode != AreaMode.STATIC_AREA) {
                    z = 10;
                    x = 10;
                }

                loaded &= Static213.method3141(data, x, Static720.mapWidth, z, Static501.mapLength);
            }
        }

        if (!loaded) {
            Static213.anInt3472 = 2;
            return;
        }

        if (Static213.anInt3472 != 0) {
            MessageBox.draw(Toolkit.active, LocalisedText.LOADING.localise(Client.language) + "<br>(100%)", true, Fonts.p12Metrics, Fonts.p12);
        }

        Static557.method7331();
        client.cacheReset();
        VideoManager.stop();

        @Pc(430) boolean underwater = false;
        if (Toolkit.active.method7990() && ClientOptions.instance.waterDetail.getValue() == 2) {
            for (@Pc(310) int i = 0; i < Static319.aByteArrayArray16.length; i++) {
                if (Static421.aByteArrayArray19[i] != null || Static177.aByteArrayArray5[i] != null) {
                    underwater = true;
                    break;
                }
            }
        }

        @Pc(310) int renderDistance;
        if (ClientOptions.instance.fog.getValue() == 1) {
            renderDistance = Static571.FOG_RENDER_DISTANCE[Static537.buildArea];
        } else {
            renderDistance = Static506.RENDER_DISTANCE[Static537.buildArea];
        }
        if (Toolkit.active.increaseRenderDistance()) {
            renderDistance++;
        }

        Static21.initScene(Toolkit.active, Static455.anInt6915, Static720.mapWidth, Static501.mapLength, renderDistance, underwater, Toolkit.active.getMaxLights() > 0);
        Static483.method6490(Static699.w2Debug);
        if (Static699.w2Debug != 0) {
            Fonts.setDebugFont(Fonts.p11);
        } else {
            Fonts.setDebugFont(null);
        }

        for (@Pc(519) int level = 0; level < 4; level++) {
            Client.collisionMaps[level].reset();
        }

        Static305.resetTileFlags();
        SoundManager.removeActiveStreams(false);
        Static508.method6750();
        Static112.aBoolean197 = false;
        Static557.method7331();
        System.gc();
        Static314.noTimeout(true);
        Static699.method9139();
        Static439.anInt6674 = ClientOptions.instance.hardShadows.getValue();
        Static428.aBoolean487 = GameShell.maxmemory >= 96;
        Static50.aBoolean566 = ClientOptions.instance.waterDetail.getValue() == 2;
        Static305.aBoolean371 = ClientOptions.instance.lightDetail.getValue() == 1;
        AnimatedBackground.level = ClientOptions.instance.animateBackground.getValue() == 1 ? -1 : Static164.areaLevel;
        Static718.groundBlending = ClientOptions.instance.groundBlending.getValue() == 1;
        Static196.aBoolean262 = ClientOptions.instance.textures.getValue() == 1;
        MapRegion.active = new MapRegion(4, Static720.mapWidth, Static501.mapLength, false);
        if (Static117.areaMode == AreaMode.STATIC_AREA) {
            Static73.decodeStaticArea(Static319.aByteArrayArray16, MapRegion.active);
        } else {
            Static693.decodeDynamicArea(Static319.aByteArrayArray16, MapRegion.active);
        }
        Static92.method1757(Static720.mapWidth >> 4, Static501.mapLength >> 4);
        Static159.method2575();

        if (underwater) {
            Static379.method5355(true);
            Static134.aMapRegion_3 = new MapRegion(1, Static720.mapWidth, Static501.mapLength, true);
            if (Static117.areaMode == AreaMode.STATIC_AREA) {
                Static73.decodeStaticArea(Static177.aByteArrayArray5, Static134.aMapRegion_3);
                Static314.noTimeout(true);
            } else {
                Static693.decodeDynamicArea(Static177.aByteArrayArray5, Static134.aMapRegion_3);
                Static314.noTimeout(true);
            }
            Static134.aMapRegion_3.addHeightOffsets(MapRegion.active.tileHeights[0]);
            Static134.aMapRegion_3.createGrounds(null, Toolkit.active, null);
            Static379.method5355(false);
        }

        MapRegion.active.createGrounds(underwater ? Static134.aMapRegion_3.tileHeights : null, Toolkit.active, Client.collisionMaps);
        if (Static117.areaMode == AreaMode.STATIC_AREA) {
            Static314.noTimeout(true);
            Static338.method4994(Static118.aByteArrayArray3, MapRegion.active);
            if (Static363.aByteArrayArray22 != null) {
                Static369.method3847();
            }
        } else {
            Static314.noTimeout(true);
            Static101.method2001(Static118.aByteArrayArray3, MapRegion.active);
        }
        client.cacheReset();
        if (GameShell.maxmemory < 96) {
            Static358.method9191();
        }
        Static314.noTimeout(true);
        MapRegion.active.load(Toolkit.active, underwater ? Static693.underwaterGround[0] : null, null);
        MapRegion.active.buildRoofOccluders(false, Toolkit.active);
        Static314.noTimeout(true);
        if (underwater) {
            Static379.method5355(true);
            Static314.noTimeout(true);
            if (Static117.areaMode == AreaMode.STATIC_AREA) {
                Static338.method4994(Static421.aByteArrayArray19, Static134.aMapRegion_3);
            } else {
                Static101.method2001(Static421.aByteArrayArray19, Static134.aMapRegion_3);
            }
            client.cacheReset();
            Static314.noTimeout(true);
            Static134.aMapRegion_3.load(Toolkit.active, null, Static706.floor[0]);
            Static134.aMapRegion_3.buildRoofOccluders(true, Toolkit.active);
            Static314.noTimeout(true);
            Static379.method5355(false);
        }
        Static207.method4432();
        @Pc(825) int topLevel = MapRegion.active.minLevel;
        if (topLevel > Camera.renderingLevel) {
            topLevel = Camera.renderingLevel;
        }
        if (Camera.renderingLevel - 1 > topLevel) {
            topLevel = Camera.renderingLevel - 1;
        }
        if (ClientOptions.instance.animateBackground.getValue() == 0) {
            Static3.method87(topLevel);
        } else {
            Static3.method87(0);
        }
        for (@Pc(852) int level = 0; level < 4; level++) {
            for (@Pc(855) int x = 0; x < Static720.mapWidth; x++) {
                for (@Pc(858) int z = 0; z < Static501.mapLength; z++) {
                    Static468.updateObjCount(level, x, z);
                }
            }
        }
        Static77.method1561();
        Static557.method7331();
        Static197.method2949();
        client.cacheReset();
        Static442.method5969();
        @Pc(920) ClientMessage message;
        if (GameShell.frame != null && ServerConnection.GAME.connection != null && MainLogicManager.step == 12) {
            message = ClientMessage.create(ClientProt.DETECT_MODIFIED_CLIENT, ServerConnection.GAME.isaac);
            message.bitPacket.p4(1057001181);
            ServerConnection.GAME.send(message);
        }

        if (Static117.areaMode == AreaMode.STATIC_AREA) {
            int chunkX1 = (Static62.areaCenterX - (Static720.mapWidth >> 4)) / 8;
            int chunkX2 = (Static62.areaCenterX + (Static720.mapWidth >> 4)) / 8;
            @Pc(961) int chunkZ1 = (Static525.areaCenterZ - (Static501.mapLength >> 4)) / 8;
            @Pc(969) int chunkZ2 = ((Static501.mapLength >> 4) + Static525.areaCenterZ) / 8;
            for (@Pc(973) int chunkX = chunkX1 - 1; chunkX <= chunkX2 + 1; chunkX++) {
                for (@Pc(978) int chunkZ = chunkZ1 - 1; chunkZ <= chunkZ2 + 1; chunkZ++) {
                    if (chunkX < chunkX1 || chunkX > chunkX2 || chunkZ < chunkZ1 || chunkZ2 < chunkZ) {
                        js5.MAPS.requestGroup("m" + chunkX + "_" + chunkZ);
                        js5.MAPS.requestGroup("l" + chunkX + "_" + chunkZ);
                    }
                }
            }
        }

        if (MainLogicManager.step == MainLogicStep.STEP_LOGIN_SCREEN_MAP_BUILD) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGIN_SCREEN);
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN_MAP_BUILD) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOBBY_SCREEN);
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME_MAP_BUILD) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME);
        } else {
            MainLogicManager.setStep(MainLogicStep.STEP_GAME_SCREEN);

            if (ServerConnection.GAME.connection != null) {
                message = ClientMessage.create(ClientProt.MAP_BUILD_COMPLETE, ServerConnection.GAME.isaac);
                ServerConnection.GAME.send(message);
            }
        }

        WorldMap.restoreLastArea();
        Static557.method7331();
        Static199.doneslowupdate();
        Static75.hasOpaqueStationaryEntities = true;
        if (Static28.aBoolean43) {
            debugconsole.addline("Took: " + (SystemTimer.safetime() - Static690.aLong318) + "ms");
            Static28.aBoolean43 = false;
        }
    }
}
