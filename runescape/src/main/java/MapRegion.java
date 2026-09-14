import com.jagex.Client;
import com.jagex.core.constants.LocLayer;
import com.jagex.core.constants.LocShapes;
import com.jagex.core.constants.TileFlag;
import com.jagex.core.io.Packet;
import com.jagex.game.Location;
import com.jagex.game.collision.CollisionMap;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayTypeList;
import com.jagex.game.runetek6.config.lighttype.LightType;
import com.jagex.game.runetek6.config.lighttype.LightTypeList;
import com.jagex.game.runetek6.config.loctype.LocInteractivity;
import com.jagex.game.runetek6.config.loctype.LocOcclusionMode;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Ground;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!taa")
public final class MapRegion extends Terrain {

    @OriginalMember(owner = "client!pf", name = "q", descriptor = "[I")
    public static final int[] WALLDECOR_STRAIGHT_XOFFSET = {1, 0, -1, 0};

    @OriginalMember(owner = "client!tea", name = "g", descriptor = "[I")
    public static final int[] WALLDECOR_STRAIGHT_ZOFFSET = {0, -1, 0, 1};

    @OriginalMember(owner = "client!mr", name = "e", descriptor = "[I")
    public static final int[] WALLDECOR_DIAGONAL_XOFFSET = {1, -1, -1, 1};

    @OriginalMember(owner = "client!uba", name = "e", descriptor = "[I")
    public static final int[] WALLDECOR_DIAGONAL_ZOFFSET = {-1, -1, 1, 1};

    @OriginalMember(owner = "client!th", name = "b", descriptor = "Z")
    public static final boolean occlude = false;

    @OriginalMember(owner = "client!ui", name = "m", descriptor = "Z")
    public static final boolean forceOcclusion = false;

    // $FF: synthetic field
    @OriginalMember(owner = "client!taa", name = "O", descriptor = "Ljava/lang/Class;")
    public static Class locClass;

    @OriginalMember(owner = "client!aaa", name = "L", descriptor = "Lclient!taa;")
    public static MapRegion active;

    @OriginalMember(owner = "client!taa", name = "K", descriptor = "I")
    public int minLevel = 99;

    @OriginalMember(owner = "client!taa", name = "<init>", descriptor = "(IIIZ)V")
    public MapRegion(@OriginalArg(0) int levels, @OriginalArg(1) int mapWidth, @OriginalArg(2) int mapLength, @OriginalArg(3) boolean underwater) {
        super(levels, mapWidth, mapLength, underwater, FloorOverlayTypeList.instance, FloorUnderlayTypeList.instance);
    }

    @OriginalMember(owner = "client!aq", name = "a", descriptor = "(ZIII)I")
    public static int rotateLightX(@OriginalArg(1) int x, @OriginalArg(2) int z, @OriginalArg(3) int rotation) {
        @Pc(3) int maskedRotation = rotation & 0x3;
        if (maskedRotation == 0) {
            return x;
        } else if (maskedRotation == 1) {
            return z;
        } else if (maskedRotation == 2) {
            return 4095 - x;
        } else {
            return 4095 - z;
        }
    }

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(IIII)I")
    public static int rotateLightZ(@OriginalArg(2) int x, @OriginalArg(0) int z, @OriginalArg(1) int rotation) {
        @Pc(3) int maskedRotation = rotation & 0x3;
        if (maskedRotation == 0) {
            return z;
        } else if (maskedRotation == 1) {
            return 4095 - x;
        } else if (maskedRotation == 2) {
            return 4095 - z;
        } else {
            return x;
        }
    }

    @OriginalMember(owner = "client!wba", name = "a", descriptor = "(Lclient!th;)V")
    public static void registerLight(@OriginalArg(0) EnvironmentLight environmentLight) {
        if (Static319.anInt5080 < 65535) {
            @Pc(7) PointLight light = environmentLight.light;
            EnvironmentLight.aEnvironmentLightArray1[Static319.anInt5080] = environmentLight;
            Static279.aBooleanArray11[Static319.anInt5080] = false;
            Static319.anInt5080++;

            @Pc(22) int minLevel = environmentLight.level;
            if (environmentLight.aBoolean716) {
                minLevel = 0;
            }

            @Pc(30) int maxLevel = environmentLight.level;
            if (environmentLight.aBoolean717) {
                maxLevel = Static299.tileMaxLevel - 1;
            }

            for (@Pc(39) int level = minLevel; level <= maxLevel; level++) {
                @Pc(42) int spanIndex = 0;
                @Pc(54) int minZ = light.getZ() + EnvironmentLight.anInt3993 - light.getRange() >> EnvironmentLight.anInt1066;
                if (minZ < 0) {
                    spanIndex = -minZ;
                    minZ = 0;
                }

                @Pc(74) int maxZ = light.getZ() + light.getRange() - EnvironmentLight.anInt3993 >> EnvironmentLight.anInt1066;
                if (maxZ >= Static662.tileMaxZ) {
                    maxZ = Static662.tileMaxZ - 1;
                }

                for (@Pc(83) int z = minZ; z <= maxZ; z++) {
                    @Pc(90) short span = environmentLight.aShortArray131[spanIndex++];
                    @Pc(106) int minX = (light.getX() + EnvironmentLight.anInt3993 - light.getRange() >> EnvironmentLight.anInt1066) + (span >>> 8);
                    @Pc(114) int maxX = minX + (span & 0xFF) - 1;
                    if (minX < 0) {
                        minX = 0;
                    }
                    if (maxX >= Static619.tileMaxX) {
                        maxX = Static619.tileMaxX - 1;
                    }

                    for (@Pc(127) int x = minX; x <= maxX; x++) {
                        @Pc(136) long lightFlags = Client.tileLightFlags[level][x][z];
                        if ((lightFlags & 0xFFFFL) == 0L) {
                            Client.tileLightFlags[level][x][z] = lightFlags | (long) Static319.anInt5080;
                        } else if ((lightFlags & 0xFFFF0000L) == 0L) {
                            Client.tileLightFlags[level][x][z] = lightFlags | (long) Static319.anInt5080 << 16;
                        } else if ((lightFlags & 0xFFFF00000000L) == 0L) {
                            Client.tileLightFlags[level][x][z] = lightFlags | (long) Static319.anInt5080 << 32;
                        } else if ((lightFlags & 0xFFFF000000000000L) == 0L) {
                            Client.tileLightFlags[level][x][z] = lightFlags | (long) Static319.anInt5080 << 48;
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!ua", name = "a", descriptor = "(IIII)I")
    public static int rotateZoneX(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(3) int rotation) {
        @Pc(3) int maskedRotation = rotation & 0x3;
        if (maskedRotation == 0) {
            return x;
        } else if (maskedRotation == 1) {
            return z;
        } else if (maskedRotation == 2) {
            return 7 - x;
        } else {
            return 7 - z;
        }
    }

    @OriginalMember(owner = "client!qm", name = "a", descriptor = "(IIBI)I")
    public static int rotateZoneZ(@OriginalArg(1) int x, @OriginalArg(0) int z, @OriginalArg(3) int rotation) {
        @Pc(7) int maskedRotation = rotation & 0x3;
        if (maskedRotation == 0) {
            return z;
        } else if (maskedRotation == 1) {
            return 7 - x;
        } else if (maskedRotation == 2) {
            return 7 - z;
        } else {
            return x;
        }
    }

    @OriginalMember(owner = "client!bka", name = "a", descriptor = "(IIBIIII)I")
    public static int rotateLocX(@OriginalArg(5) int x, @OriginalArg(1) int z, @OriginalArg(0) int width, @OriginalArg(3) int length, @OriginalArg(6) int rotation, @OriginalArg(4) int locRotation) {
        @Pc(3) int maskedRotation = rotation & 0x3;
        if ((locRotation & 0x1) == 1) {
            @Pc(10) int temp = width;
            width = length;
            length = temp;
        }

        if (maskedRotation == 0) {
            return x;
        } else if (maskedRotation == 1) {
            return z;
        } else if (maskedRotation == 2) {
            return 7 + 1 - x - width;
        } else {
            return 7 + 1 - z - length;
        }
    }

    @OriginalMember(owner = "client!qga", name = "a", descriptor = "(IIIIIII)I")
    public static int rotateLocZ(@OriginalArg(5) int x, @OriginalArg(1) int z, @OriginalArg(4) int width, @OriginalArg(2) int length, @OriginalArg(0) int rotation, @OriginalArg(3) int locRotation) {
        if ((locRotation & 0x1) == 1) {
            @Pc(12) int temp = width;
            width = length;
            length = temp;
        }

        @Pc(20) int maskedRotation = rotation & 0x3;
        if (maskedRotation == 0) {
            return z;
        } else if (maskedRotation == 1) {
            return 1 + 7 - width - x;
        } else if (maskedRotation == 2) {
            return 1 + 7 - length - z;
        } else {
            return x;
        }
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(III[Lclient!eq;Lclient!ha;[B)V")
    public void loadLocations(@OriginalArg(0) int offsetX, @OriginalArg(1) int offsetZ, @OriginalArg(3) CollisionMap[] collisionMaps, @OriginalArg(4) Toolkit toolkit, @OriginalArg(5) byte[] data) {
        @Pc(8) Packet packet = new Packet(data);
        @Pc(18) int id = -1;
        while (true) {
            @Pc(22) int idOffset = packet.gExtended1or2();
            if (idOffset == 0) {
                return;
            }
            id += idOffset;

            @Pc(30) int coord = 0;
            while (true) {
                @Pc(34) int coordOffset = packet.gsmart();
                if (coordOffset == 0) {
                    break;
                }
                coord += coordOffset - 1;

                @Pc(46) int locZ = coord & 0x3F;
                @Pc(52) int locX = coord >> 6 & 0x3F;
                @Pc(56) int locLevel = coord >> 12;
                @Pc(60) int shapeAndRotation = packet.g1();
                @Pc(64) int locShape = shapeAndRotation >> 2;
                @Pc(68) int locRotation = shapeAndRotation & 0x3;
                @Pc(73) int x = locX + offsetX;
                @Pc(77) int z = locZ + offsetZ;
                if (x > 0 && z > 0 && x < super.width - 1 && z < super.length - 1) {
                    @Pc(102) CollisionMap collisionMap = null;
                    if (!super.underwater) {
                        @Pc(107) int actualLevel = locLevel;
                        if ((Static280.tileFlags[1][x][z] & TileFlag.BRIDGE) == 2) {
                            actualLevel = locLevel - 1;
                        }
                        if (actualLevel >= 0) {
                            collisionMap = collisionMaps[actualLevel];
                        }
                    }
                    this.loadLocation(x, z, locLevel, locLevel, id, locShape, locRotation, -1, collisionMap, toolkit);
                }
            }
        }
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(ILclient!ge;ILclient!ha;I)V")
    public void decodeStaticEnvironment(@OriginalArg(0) int z, @OriginalArg(1) Packet packet, @OriginalArg(2) int x, @OriginalArg(3) Toolkit toolkit) {
        if (super.underwater) {
            return;
        }
        @Pc(10) boolean mapLoaded = false;
        @Pc(12) Environment environment = null;
        while (packet.data.length > packet.pos) {
            @Pc(28) int code = packet.g1();
            if (code != 0) {
                if (code == 1) {
                    @Pc(86) int count = packet.g1();
                    for (@Pc(504) int i = 0; i < count; i++) {
                        @Pc(512) EnvironmentLight environmentLight = new EnvironmentLight(toolkit, packet, 2);
                        if (environmentLight.preset == 31) {
                            @Pc(523) LightType lightType = LightTypeList.instance.list(packet.g2());
                            environmentLight.updateParameters(lightType.ambient, lightType.pattern, lightType.amplitude, lightType.frequency);
                        }
                        if (toolkit.getMaxLights() > 0) {
                            @Pc(543) PointLight light = environmentLight.light;
                            @Pc(149) int worldX = (x << 9) + light.getX();
                            @Pc(153) int worldZ = (z << 9) + light.getZ();
                            @Pc(290) int lightX = worldX >> 9;
                            @Pc(567) int lightZ = worldZ >> 9;
                            if (lightX >= 0 && lightZ >= 0 && super.width > lightX && super.length > lightZ) {
                                light.setPosition(worldX, worldZ, super.tileHeights[environmentLight.level][lightX][lightZ] - light.getY());
                                registerLight(environmentLight);
                            }
                        }
                    }
                } else if (code == 2) {
                    if (environment == null) {
                        environment = new Environment();
                    }
                    environment.decodeBloomParams(packet);
                } else if (code == 128) {
                    if (environment == null) {
                        environment = new Environment();
                    }
                    environment.decodeSkyBox(packet);
                } else if (code == 129) {
                    if (super.aByteArrayArrayArray12 == null) {
                        super.aByteArrayArrayArray12 = new byte[4][][];
                    }
                    mapLoaded = true;
                    for (@Pc(86) int level = 0; level < 4; level++) {
                        @Pc(91) byte mode = packet.g1b();
                        if (mode == 0 && super.aByteArrayArrayArray12[level] != null) {
                            @Pc(143) int minX = x;
                            @Pc(147) int maxX = x + 64;
                            @Pc(149) int minZ = z;
                            @Pc(153) int maxZ = z + 64;
                            if (x < 0) {
                                minX = 0;
                            } else if (x >= super.width) {
                                minX = super.width;
                            }
                            if (z < 0) {
                                minZ = 0;
                            } else if (z >= super.length) {
                                minZ = super.length;
                            }
                            if (maxX < 0) {
                                maxX = 0;
                            } else if (super.width <= maxX) {
                                maxX = super.width;
                            }
                            if (maxZ < 0) {
                                maxZ = 0;
                            } else if (maxZ >= super.length) {
                                maxZ = super.length;
                            }
                            while (maxX > minX) {
                                while (minZ < maxZ) {
                                    super.aByteArrayArrayArray12[level][minX][minZ] = 0;
                                    minZ++;
                                }
                                minX++;
                            }
                        } else if (mode == 1) {
                            if (super.aByteArrayArrayArray12[level] == null) {
                                super.aByteArrayArrayArray12[level] = new byte[super.width + 1][super.length + 1];
                            }
                            for (@Pc(143) int localX = 0; localX < 64; localX += 4) {
                                for (@Pc(147) int localZ = 0; localZ < 64; localZ += 4) {
                                    @Pc(280) byte height = packet.g1b();
                                    for (@Pc(153) int tileX = localX + x; tileX < localX + x + 4; tileX++) {
                                        for (@Pc(290) int tileZ = z + localZ; tileZ < z + localZ + 4; tileZ++) {
                                            if (tileX >= 0 && super.width > tileX && tileZ >= 0 && super.length > tileZ) {
                                                super.aByteArrayArrayArray12[level][tileX][tileZ] = height;
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (mode == 2) {
                            if (super.aByteArrayArrayArray12[level] == null) {
                                super.aByteArrayArrayArray12[level] = new byte[super.width + 1][super.length + 1];
                            }
                            if (level > 0) {
                                @Pc(143) int minX = x;
                                @Pc(147) int maxX = x + 64;
                                @Pc(149) int minZ = z;
                                @Pc(153) int maxZ = z + 64;
                                if (maxX < 0) {
                                    maxX = 0;
                                } else if (super.width <= maxX) {
                                    maxX = super.width;
                                }
                                if (x < 0) {
                                    minX = 0;
                                } else if (x >= super.width) {
                                    minX = super.width;
                                }
                                if (z < 0) {
                                    minZ = 0;
                                } else if (z >= super.length) {
                                    minZ = super.length;
                                }
                                if (maxZ < 0) {
                                    maxZ = 0;
                                } else if (maxZ >= super.length) {
                                    maxZ = super.length;
                                }
                                while (minX < maxX) {
                                    while (maxZ > minZ) {
                                        super.aByteArrayArrayArray12[level][minX][minZ] = super.aByteArrayArrayArray12[level - 1][minX][minZ];
                                        minZ++;
                                    }
                                    minX++;
                                }
                            }
                        }
                    }
                } else {
                    throw new IllegalStateException("");
                }
            } else if (environment == null) {
                environment = new Environment(packet);
            } else {
                environment.decodeLighting(packet);
            }
        }
        if (environment != null) {
            for (@Pc(28) int zoneX = 0; zoneX < 8; zoneX++) {
                for (@Pc(86) int zoneZ = 0; zoneZ < 8; zoneZ++) {
                    @Pc(504) int mapZoneX = zoneX + (x >> 3);
                    @Pc(143) int mapZoneZ = zoneZ + (z >> 3);
                    if (mapZoneX >= 0 && super.width >> 3 > mapZoneX && mapZoneZ >= 0 && super.length >> 3 > mapZoneZ) {
                        Static108.method2064(mapZoneZ, mapZoneX, environment);
                    }
                }
            }
        }
        if (!mapLoaded && super.aByteArrayArrayArray12 != null) {
            for (@Pc(28) int level = 0; level < 4; level++) {
                if (super.aByteArrayArrayArray12[level] != null) {
                    for (@Pc(86) int zoneX = 0; zoneX < 16; zoneX++) {
                        for (@Pc(504) int zoneZ = 0; zoneZ < 16; zoneZ++) {
                            @Pc(143) int mapZoneX = (x >> 2) + zoneX;
                            @Pc(147) int mapZoneZ = (z >> 2) + zoneZ;
                            if (mapZoneX >= 0 && mapZoneX < 26 && mapZoneZ >= 0 && mapZoneZ < 26) {
                                super.aByteArrayArrayArray12[level][mapZoneX][mapZoneZ] = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(IILclient!eq;ILclient!ha;IIIIII)V")
    public void loadLocation(@OriginalArg(5) int x, @OriginalArg(3) int z, @OriginalArg(9) int level, @OriginalArg(8) int virtualLevel, @OriginalArg(1) int id, @OriginalArg(0) int shape, @OriginalArg(6) int rotation, @OriginalArg(10) int animation, @OriginalArg(2) CollisionMap collisionMap, @OriginalArg(4) Toolkit toolkit) {
        boolean animatingBackground = ClientOptions.instance.animateBackground.getValue() != 0;
        if (!animatingBackground && !Static696.isTileVisibleFrom(z, Static164.areaLevel, x, virtualLevel)) {
            return;
        }

        if (level < this.minLevel) {
            this.minLevel = level;
        }

        @Pc(40) LocType locType = LocTypeList.instance.list(id);
        boolean texturesDisabled = ClientOptions.instance.textures.getValue() == 0;
        if (texturesDisabled && locType.istexture) {
            return;
        }

        @Pc(65) int locWidth;
        @Pc(68) int locLength;
        if (rotation == 1 || rotation == 3) {
            locLength = locType.width;
            locWidth = locType.length;
        } else {
            locWidth = locType.width;
            locLength = locType.length;
        }

        @Pc(100) int x0;
        @Pc(94) int x1;
        if (x + locWidth <= super.width) {
            x1 = (locWidth + 1 >> 1) + x;
            x0 = (locWidth >> 1) + x;
        } else {
            x1 = x + 1;
            x0 = x;
        }

        @Pc(123) int z0;
        @Pc(121) int z1;
        if (super.length < locLength + z) {
            z1 = z + 1;
            z0 = z;
        } else {
            z0 = z + (locLength >> 1);
            z1 = z + (locLength + 1 >> 1);
        }

        @Pc(143) Ground ground = Static246.ground[virtualLevel];
        int bottomLeftHeight = ground.getHeight(x0, z0);
        int bottomRightHeight = ground.getHeight(x1, z0);
        int topLeftHeight = ground.getHeight(x0, z1);
        int topRightHeight = ground.getHeight(x1, z1);
        @Pc(170) int averageHeight = (bottomLeftHeight + bottomRightHeight + topLeftHeight + topRightHeight) >> 2;

        @Pc(179) int absX = (x << 9) + (locWidth << 8);
        @Pc(187) int absZ = (locLength << 8) + (z << 9);

        @Pc(204) boolean copyNormals = Static404.renderShadows && !super.underwater && locType.sharelight;

        if (locType.hasSounds()) {
            SoundManager.addSounds(level, x, z, null, null, locType, rotation);
        }

        @Pc(248) boolean isStatic = animation == -1 && !locType.hasAnimations() && locType.multiloc == null && !locType.animated && !locType.aBoolean91;

        boolean skipWall = LocShapes.isWall(shape) && locType.occlude != LocOcclusionMode.ALL;
        boolean skipRoof = LocShapes.isRoof(shape) && locType.occlude == LocOcclusionMode.ROOFS;
        if (occlude && (skipWall || skipRoof)) {
            return;
        }

        if (shape == LocShapes.GROUNDDECOR) {
            if (ClientOptions.instance.groundDecor.getValue() == 0 && locType.active == LocInteractivity.NONINTERACTIVE && locType.blockwalk != 1 && !locType.forcedecor) {
                return;
            }

            @Pc(325) GroundDecor decor;
            if (isStatic) {
                @Pc(341) StaticGroundDecor staticDecor = new StaticGroundDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, rotation, copyNormals);

                decor = staticDecor;

                if (staticDecor.hardShadow()) {
                    staticDecor.addShadow(toolkit);
                }
            } else {
                decor = new DynamicGroundDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, rotation, animation);
            }

            Static61.setGroundDecor(level, x, z, decor);

            if (locType.blockwalk == 1 && collisionMap != null) {
                collisionMap.flagGroundDecor(x, z);
            }
        } else if (shape == LocShapes.CENTREPIECE_STRAIGHT || shape == LocShapes.CENTREPIECE_DIAGONAL) {
            @Pc(420) PositionEntity loc;
            @Pc(384) StaticLocation staticLoc = null;

            @Pc(424) int shadowValue;
            if (isStatic) {
                @Pc(416) StaticLocation staticLocation = new StaticLocation(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, x, x + locWidth - 1, z, z + locLength - 1, shape, rotation, copyNormals);
                staticLoc = staticLocation;
                loc = staticLocation;
                shadowValue = staticLocation.shadowValue();
            } else {
                shadowValue = 15;
                loc = new DynamicLocation(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, x, x + locWidth - 1, z, z + locLength - 1, shape, rotation, animation);
            }

            if (Static102.addPositionEntity(loc, false)) {
                if (staticLoc != null && staticLoc.hardShadow()) {
                    staticLoc.addShadow(toolkit);
                }

                if (locType.shadow && Static404.renderShadows) {
                    if (shadowValue > 30) {
                        shadowValue = 30;
                    }

                    for (@Pc(492) int locX = 0; locX <= locWidth; locX++) {
                        for (@Pc(495) int locZ = 0; locZ <= locLength; locZ++) {
                            ground.ka(x + locX, z + locZ, shadowValue);
                        }
                    }
                }
            }

            if (locType.blockwalk != 0 && collisionMap != null) {
                collisionMap.flagLoc(x, z, locWidth, locLength, locType.blockrange, !locType.breakroutefinding);
            }
        } else if (shape >= LocShapes.ROOF_STRAIGHT && shape <= LocShapes.ROOF_FLAT || shape >= LocShapes.ROOFEDGE_STRAIGHT && shape <= LocShapes.ROOFEDGE_SQUARECORNER) {
            @Pc(420) PositionEntity loc;
            @Pc(384) StaticLocation staticLoc;

            if (isStatic) {
                staticLoc = new StaticLocation(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, x, x + locWidth - 1, z, z + locLength - 1, shape, rotation, copyNormals);

                if (staticLoc.hardShadow()) {
                    staticLoc.addShadow(toolkit);
                }

                loc = staticLoc;
            } else {
                loc = new DynamicLocation(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, x, locWidth + x - 1, z, z + locLength - 1, shape, rotation, animation);
            }

            Static102.addPositionEntity(loc, false);

            boolean occludeRoofs = locType.occlude == LocOcclusionMode.ROOFS;
            if (Static404.renderShadows && !super.underwater && shape >= LocShapes.ROOF_STRAIGHT && shape <= LocShapes.ROOF_FLAT && shape != LocShapes.ROOF_DIAGONAL_WITH_ROOFEDGE && level > 0 && !occludeRoofs) {
                super.occluderFlags[level][x][z] = (byte) (super.occluderFlags[level][x][z] | 0x4);
            }

            if (locType.blockwalk != 0 && collisionMap != null) {
                collisionMap.flagLoc(x, z, locWidth, locLength, locType.blockrange, !locType.breakroutefinding);
            }
        } else if (shape == LocShapes.WALL_STRAIGHT) {
            @Pc(774) Wall wall;

            @Pc(744) int occlusionMode = locType.occlude;
            if (forceOcclusion && locType.occlude == LocOcclusionMode.NONE) {
                occlusionMode = LocOcclusionMode.ALL;
            }

            if (isStatic) {
                @Pc(772) StaticWall staticWall = new StaticWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation, copyNormals);
                wall = staticWall;

                if (staticWall.hardShadow()) {
                    staticWall.addShadow(toolkit);
                }
            } else {
                wall = new DynamicWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation, animation);
            }

            Static584.setWall(level, x, z, wall, null);

            if (rotation == 0) {
                if (Static404.renderShadows && locType.shadow) {
                    ground.ka(x, z, 50);
                    ground.ka(x, z + 1, 50);
                }

                if (occlusionMode == LocOcclusionMode.ALL && !super.underwater) {
                    Static177.addLocationOccluder(1, locType.occlusionOffset, z, x, level, locType.occlusionHeight);
                }
            } else if (rotation == 1) {
                if (Static404.renderShadows && locType.shadow) {
                    ground.ka(x, z + 1, 50);
                    ground.ka(x + 1, z - -1, 50);
                }

                if (occlusionMode == LocOcclusionMode.ALL && !super.underwater) {
                    Static177.addLocationOccluder(2, -locType.occlusionOffset, z + 1, x, level, locType.occlusionHeight);
                }
            } else if (rotation == 2) {
                if (Static404.renderShadows && locType.shadow) {
                    ground.ka(x + 1, z, 50);
                    ground.ka(x + 1, z + 1, 50);
                }

                if (occlusionMode == LocOcclusionMode.ALL && !super.underwater) {
                    Static177.addLocationOccluder(1, -locType.occlusionOffset, z, x + 1, level, locType.occlusionHeight);
                }
            } else if (rotation == 3) {
                if (Static404.renderShadows && locType.shadow) {
                    ground.ka(x, z, 50);
                    ground.ka(x + 1, z, 50);
                }

                if (occlusionMode == LocOcclusionMode.ALL && !super.underwater) {
                    Static177.addLocationOccluder(2, locType.occlusionOffset, z, x, level, locType.occlusionHeight);
                }
            }

            if (locType.blockwalk != 0 && collisionMap != null) {
                collisionMap.flagWall(locType.blockrange, !locType.breakroutefinding, z, rotation, shape, x);
            }

            if (locType.walloff != 64) {
                Static411.method5666(level, x, z, locType.walloff);
            }
        } else if (shape == LocShapes.WALL_DIAGONALCORNER) {
            @Pc(1079) Wall wall;
            @Pc(1096) StaticWall staticWall;

            if (isStatic) {
                staticWall = new StaticWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation, copyNormals);
                wall = staticWall;

                if (staticWall.hardShadow()) {
                    staticWall.addShadow(toolkit);
                }
            } else {
                wall = new DynamicWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation, animation);
            }

            Static584.setWall(level, x, z, wall, null);

            if (locType.shadow && Static404.renderShadows) {
                if (rotation == 0) {
                    ground.ka(x, z + 1, 50);
                } else if (rotation == 1) {
                    ground.ka(x + 1, z - -1, 50);
                } else if (rotation == 2) {
                    ground.ka(x + 1, z, 50);
                } else if (rotation == 3) {
                    ground.ka(x, z, 50);
                }
            }

            if (locType.blockwalk != 0 && collisionMap != null) {
                collisionMap.flagWall(locType.blockrange, !locType.breakroutefinding, z, rotation, shape, x);
            }
        } else if (shape == LocShapes.WALL_L) {
            @Pc(1079) Wall wall;
            @Pc(774) Wall adjacentWall;

            @Pc(424) int rotation90 = (rotation + 1) & 0x3;

            if (isStatic) {
                @Pc(1267) StaticWall staticWall = new StaticWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation + 4, copyNormals);
                @Pc(1283) StaticWall adjacentStaticWall = new StaticWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation90, copyNormals);

                if (staticWall.hardShadow()) {
                    staticWall.addShadow(toolkit);
                }

                adjacentWall = adjacentStaticWall;

                if (adjacentStaticWall.hardShadow()) {
                    adjacentStaticWall.addShadow(toolkit);
                }

                wall = staticWall;
            } else {
                wall = new DynamicWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation + 4, animation);
                adjacentWall = new DynamicWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation90, animation);
            }

            Static584.setWall(level, x, z, wall, adjacentWall);

            boolean occludesAll = (locType.occlude == LocOcclusionMode.ALL) || (forceOcclusion && locType.occlude == LocOcclusionMode.NONE);
            if (occludesAll && !super.underwater) {
                if (rotation == 0) {
                    Static177.addLocationOccluder(1, locType.occlusionOffset, z, x, level, locType.occlusionHeight);
                    Static177.addLocationOccluder(2, locType.occlusionOffset, z + 1, x, level, locType.occlusionHeight);
                } else if (rotation == 1) {
                    Static177.addLocationOccluder(1, locType.occlusionOffset, z, x + 1, level, locType.occlusionHeight);
                    Static177.addLocationOccluder(2, locType.occlusionOffset, z + 1, x, level, locType.occlusionHeight);
                } else if (rotation == 2) {
                    Static177.addLocationOccluder(1, locType.occlusionOffset, z, x + 1, level, locType.occlusionHeight);
                    Static177.addLocationOccluder(2, locType.occlusionOffset, z, x, level, locType.occlusionHeight);
                } else if (rotation == 3) {
                    Static177.addLocationOccluder(1, locType.occlusionOffset, z, x, level, locType.occlusionHeight);
                    Static177.addLocationOccluder(2, locType.occlusionOffset, z, x, level, locType.occlusionHeight);
                }
            }

            if (locType.blockwalk != 0 && collisionMap != null) {
                collisionMap.flagWall(locType.blockrange, !locType.breakroutefinding, z, rotation, shape, x);
            }

            if (locType.walloff != 64) {
                Static411.method5666(level, x, z, locType.walloff);
            }
        } else if (shape == LocShapes.WALL_SQUARECORNER) {
            @Pc(1079) Wall wall;

            if (isStatic) {
                @Pc(1096) StaticWall staticWall = new StaticWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation, copyNormals);

                if (staticWall.hardShadow()) {
                    staticWall.addShadow(toolkit);
                }

                wall = staticWall;
            } else {
                wall = new DynamicWall(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, shape, rotation, animation);
            }

            Static584.setWall(level, x, z, wall, null);

            if (locType.shadow && Static404.renderShadows) {
                if (rotation == 0) {
                    ground.ka(x, z + 1, 50);
                } else if (rotation == 1) {
                    ground.ka(x + 1, z - -1, 50);
                } else if (rotation == 2) {
                    ground.ka(x + 1, z, 50);
                } else if (rotation == 3) {
                    ground.ka(x, z, 50);
                }
            }

            if (locType.blockwalk != 0 && collisionMap != null) {
                collisionMap.flagWall(locType.blockrange, !locType.breakroutefinding, z, rotation, shape, x);
            }
        } else if (shape == LocShapes.WALL_DIAGONAL) {
            @Pc(420) PositionEntity loc;

            if (isStatic) {
                @Pc(384) StaticLocation staticLoc = new StaticLocation(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, x, x, z, z, shape, rotation, copyNormals);

                if (staticLoc.hardShadow()) {
                    staticLoc.addShadow(toolkit);
                }

                loc = staticLoc;
            } else {
                loc = new DynamicLocation(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, x, locWidth + x - 1, z, locLength + z - 1, shape, rotation, animation);
            }

            Static102.addPositionEntity(loc, false);

            if (locType.occlude == LocOcclusionMode.ALL && !super.underwater) {
                @Pc(1723) byte occlusionType;
                if ((rotation & 0x1) == 0) {
                    occlusionType = 8;
                } else {
                    occlusionType = 16;
                }

                Static177.addLocationOccluder(occlusionType, 0, z, x, level, locType.occlusionHeight);
            }

            if (locType.blockwalk != 0 && collisionMap != null) {
                collisionMap.flagLoc(x, z, locWidth, locLength, locType.blockrange, !locType.breakroutefinding);
            }

            if (locType.walloff != 64) {
                Static411.method5666(level, x, z, locType.walloff);
            }
        } else if (shape == LocShapes.WALLDECOR_STRAIGHT_NOOFFSET) {
            @Pc(1813) WallDecor decor;

            if (isStatic) {
                @Pc(1801) StaticWallDecor staticDecor = new StaticWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, 0, 0, shape, rotation);

                if (staticDecor.hardShadow()) {
                    staticDecor.addShadow(toolkit);
                }

                decor = staticDecor;
            } else {
                decor = new DynamicWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, 0, 0, shape, rotation, animation);
            }

            Static177.setWallDecor(level, x, z, decor, null);
        } else if (shape == LocShapes.WALLDECOR_STRAIGHT_OFFSET) {
            @Pc(1813) WallDecor decor;

            @Pc(1844) int wallOffset = 65;
            @Pc(1850) Location wall = (Location) Static302.getWall(level, x, z);
            if (wall != null) {
                wallOffset = LocTypeList.instance.list(wall.getId()).walloff + 1;
            }

            if (isStatic) {
                @Pc(1916) StaticWallDecor staticDecor = new StaticWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, WALLDECOR_STRAIGHT_XOFFSET[rotation] * wallOffset, WALLDECOR_STRAIGHT_ZOFFSET[rotation] * wallOffset, shape, rotation);

                if (staticDecor.hardShadow()) {
                    staticDecor.addShadow(toolkit);
                }

                decor = staticDecor;
            } else {
                decor = new DynamicWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, WALLDECOR_STRAIGHT_XOFFSET[rotation] * wallOffset, wallOffset * WALLDECOR_STRAIGHT_ZOFFSET[rotation], shape, rotation, animation);
            }

            Static177.setWallDecor(level, x, z, decor, null);
        } else if (shape == LocShapes.WALLDECOR_DIAGONAL_OFFSET) {
            @Pc(1813) WallDecor decor;

            @Pc(1844) int wallOffset = 33;
            @Pc(1850) Location wall = (Location) Static302.getWall(level, x, z);
            if (wall != null) {
                wallOffset = (LocTypeList.instance.list(wall.getId()).walloff / 2) + 1;
            }

            if (isStatic) {
                @Pc(1916) StaticWallDecor staticDecor = new StaticWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, WALLDECOR_STRAIGHT_XOFFSET[rotation] * wallOffset, wallOffset * WALLDECOR_STRAIGHT_ZOFFSET[rotation], shape, rotation + 4);
                decor = staticDecor;

                if (staticDecor.hardShadow()) {
                    staticDecor.addShadow(toolkit);
                }
            } else {
                decor = new DynamicWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, WALLDECOR_DIAGONAL_XOFFSET[rotation] * wallOffset, WALLDECOR_DIAGONAL_ZOFFSET[rotation] * wallOffset, shape, rotation + 4, animation);
            }

            Static177.setWallDecor(level, x, z, decor, null);
        } else if (shape == LocShapes.WALLDECOR_DIAGONAL_NOOFFSET) {
            @Pc(1813) WallDecor decor;
            @Pc(1844) int oppositeRotation = (rotation + 2) & 0x3;

            if (isStatic) {
                @Pc(2068) StaticWallDecor staticDecor = new StaticWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, 0, 0, shape, oppositeRotation + 4);
                decor = staticDecor;

                if (staticDecor.hardShadow()) {
                    staticDecor.addShadow(toolkit);
                }
            } else {
                decor = new DynamicWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, 0, 0, shape, oppositeRotation + 4, animation);
            }

            Static177.setWallDecor(level, x, z, decor, null);
        } else if (shape == LocShapes.WALLDECOR_DIAGONAL_BOTH) {
            @Pc(492) int oppositeRotation = rotation + 2 & 0x3;

            @Pc(495) int wallOffset = 33;
            @Pc(2134) Location wall = (Location) Static302.getWall(level, x, z);
            if (wall != null) {
                wallOffset = (LocTypeList.instance.list(wall.getId()).walloff / 2) + 1;
            }

            @Pc(2178) WallDecor primaryDecor;
            @Pc(2200) WallDecor secondaryDecor;
            if (isStatic) {
                primaryDecor = new StaticWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, WALLDECOR_DIAGONAL_XOFFSET[rotation] * wallOffset, WALLDECOR_DIAGONAL_ZOFFSET[rotation] * wallOffset, shape, rotation + 4);
                secondaryDecor = new StaticWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, 0, 0, shape, oppositeRotation + 4);

                if (primaryDecor.hardShadow()) {
                    primaryDecor.addShadow(toolkit);
                }

                if (secondaryDecor.hardShadow()) {
                    secondaryDecor.addShadow(toolkit);
                }
            } else {
                primaryDecor = new DynamicWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, WALLDECOR_DIAGONAL_XOFFSET[rotation] * wallOffset, WALLDECOR_DIAGONAL_ZOFFSET[rotation] * wallOffset, shape, rotation + 4, animation);
                secondaryDecor = new DynamicWallDecor(toolkit, locType, level, virtualLevel, absX, averageHeight, absZ, super.underwater, 0, 0, shape, oppositeRotation + 4, animation);
            }

            Static177.setWallDecor(level, x, z, primaryDecor, secondaryDecor);
        }
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(IILclient!ha;ILclient!ge;IIIII)V")
    public void decodeEnvironment(@OriginalArg(0) int x, @OriginalArg(2) Toolkit toolkit, @OriginalArg(3) int pointerZ, @OriginalArg(4) Packet packet, @OriginalArg(5) int level, @OriginalArg(6) int pointerX, @OriginalArg(7) int pointerRotation, @OriginalArg(8) int pointerLevel, @OriginalArg(9) int z) {
        if (super.underwater) {
            return;
        }
        @Pc(10) boolean mapLoaded = false;
        @Pc(12) Environment environment = null;
        @Pc(18) int pointerSquareX = (pointerX & 0x7) * 8;
        @Pc(24) int pointerSquareZ = (pointerZ & 0x7) * 8;
        while (packet.pos < packet.data.length) {
            @Pc(35) int code = packet.g1();

            if (code == 0) {
                if (environment == null) {
                    environment = new Environment(packet);
                } else {
                    environment.decodeLighting(packet);
                }
            } else if (code == 1) {
                @Pc(63) int count = packet.g1();
                if (count <= 0) {
                    continue;
                }

                for (@Pc(70) int i = 0; i < count; i++) {
                    @Pc(78) EnvironmentLight envLight = new EnvironmentLight(toolkit, packet, 2);
                    if (envLight.preset == 31) {
                        @Pc(91) LightType type = LightTypeList.instance.list(packet.g2());
                        envLight.updateParameters(type.ambient, type.pattern, type.amplitude, type.frequency);
                    }

                    if (toolkit.getMaxLights() > 0) {
                        @Pc(108) PointLight light = envLight.light;
                        @Pc(116) int lightX = light.getX() >> 9;
                        @Pc(122) int lightZ = light.getZ() >> 9;

                        if ((pointerLevel == envLight.level) && (lightX >= pointerSquareX) && (lightX < (pointerSquareX + 8)) && (lightZ >= pointerSquareZ) && (lightZ < (pointerSquareZ + 8))) {
                            @Pc(176) int worldX = (x << 9) + rotateLightX(light.getX() & 0xFFF, light.getZ() & 0xFFF, pointerRotation);
                            lightX = worldX >> 9;

                            @Pc(200) int worldZ = (z << 9) + rotateLightZ(light.getX() & 0xFFF, light.getZ() & 0xFFF, pointerRotation);
                            lightZ = worldZ >> 9;

                            if (lightX >= 0 && lightZ >= 0 && lightX < super.width && lightZ < super.length) {
                                light.setPosition(worldX, worldZ, super.tileHeights[pointerLevel][lightX][lightZ] - light.getY());
                                registerLight(envLight);
                            }
                        }
                    }
                }
            } else if (code == 2) {
                if (environment == null) {
                    environment = new Environment();
                }
                environment.decodeBloomParams(packet);
            } else if (code == 128) {
                if (environment == null) {
                    environment = new Environment();
                }
                environment.decodeSkyBox(packet);
            } else if (code == 129) {
                if (super.aByteArrayArrayArray12 == null) {
                    super.aByteArrayArrayArray12 = new byte[4][][];
                }
                for (@Pc(63) int mapLevel = 0; mapLevel < 4; mapLevel++) {
                    @Pc(311) byte mode = packet.g1b();
                    if (mode == 0 && super.aByteArrayArrayArray12[level] != null) {
                        if (pointerLevel >= mapLevel) {
                            @Pc(327) int minX = x;
                            @Pc(331) int maxX = x + 7;
                            @Pc(116) int minZ = z;
                            if (x < 0) {
                                minX = 0;
                            } else if (x >= super.width) {
                                minX = super.width;
                            }
                            if (maxX < 0) {
                                maxX = 0;
                            } else if (maxX >= super.width) {
                                maxX = super.width;
                            }

                            @Pc(122) int maxZ = z + 7;
                            if (z < 0) {
                                minZ = 0;
                            } else if (z >= super.length) {
                                minZ = super.length;
                            }

                            if (maxZ < 0) {
                                maxZ = 0;
                            } else if (super.length <= maxZ) {
                                maxZ = super.length;
                            }
                            while (maxX > minX) {
                                while (maxZ > minZ) {
                                    super.aByteArrayArrayArray12[level][minX][minZ] = 0;
                                    minZ++;
                                }
                                minX++;
                            }
                        }
                    } else if (mode == 1) {
                        if (super.aByteArrayArrayArray12[level] == null) {
                            super.aByteArrayArrayArray12[level] = new byte[super.width + 1][super.length + 1];
                        }

                        for (@Pc(327) int blockX = 0; blockX < 64; blockX += 4) {
                            for (@Pc(331) int blockZ = 0; blockZ < 64; blockZ += 4) {
                                @Pc(466) byte height = packet.g1b();
                                if (mapLevel <= pointerLevel) {
                                    for (@Pc(122) int localX = blockX; localX < blockX + 4; localX++) {
                                        for (@Pc(176) int localZ = blockZ; localZ < blockZ + 4; localZ++) {
                                            if (localX >= pointerSquareX && pointerSquareX + 8 > localX && localZ >= pointerSquareZ && pointerSquareZ + 8 > localZ) {
                                                @Pc(200) int tileX = x + rotateZoneX(localX & 0x7, localZ & 0x7, pointerRotation);
                                                @Pc(534) int tileZ = z + rotateZoneZ(localX & 0x7, localZ & 0x7, pointerRotation);
                                                if (tileX >= 0 && tileX < super.width && tileZ >= 0 && tileZ < super.length) {
                                                    super.aByteArrayArrayArray12[level][tileX][tileZ] = height;
                                                    mapLoaded = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                throw new IllegalStateException("");
            }
        }

        if (environment != null) {
            Static108.method2064(z >> 3, x >> 3, environment);
        }

        if (!mapLoaded && super.aByteArrayArrayArray12 != null && super.aByteArrayArrayArray12[level] != null) {
            @Pc(35) int maxX = x + 7;
            @Pc(63) int maxZ = z + 7;
            for (@Pc(70) int tileX = x; tileX < maxX; tileX++) {
                for (@Pc(327) int tileZ = z; tileZ < maxZ; tileZ++) {
                    super.aByteArrayArrayArray12[level][tileX][tileZ] = 0;
                }
            }
        }
    }

    @OriginalMember(owner = "client!taa", name = "b", descriptor = "(IIIII)Lclient!uv;")
    public Location getLoc(@OriginalArg(2) int level, @OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(3) int layer) {
        @Pc(5) Location location = null;
        if (layer == LocLayer.WALL) {
            location = (Location) Static302.getWall(level, x, z);
        }
        if (layer == LocLayer.WALLDECOR) {
            location = Static114.getWallDecor(level, x, z);
        }
        if (layer == LocLayer.GROUND) {
            location = (Location) Static578.getEntity(level, x, z, locClass == null ? (locClass = getClass("com.jagex.game.Location")) : locClass);
        }
        if (layer == LocLayer.GROUNDDECOR) {
            location = (Location) Static687.getGroundDecor(level, x, z);
        }
        return location;
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(I[Lclient!eq;ILclient!ha;BII[BIII)V")
    public void loadChunkLocations(@OriginalArg(0) int pointerSquareX, @OriginalArg(1) CollisionMap[] collisionMaps, @OriginalArg(2) int level, @OriginalArg(3) Toolkit toolkit, @OriginalArg(5) int x, @OriginalArg(6) int z, @OriginalArg(7) byte[] data, @OriginalArg(8) int pointerRotation, @OriginalArg(9) int pointerLevel, @OriginalArg(10) int pointerSquareZ) {
        @Pc(26) Packet packet = new Packet(data);
        @Pc(28) int id = -1;
        while (true) {
            @Pc(32) int idOffset = packet.gExtended1or2();
            if (idOffset == 0) {
                return;
            }
            id += idOffset;

            @Pc(40) int coord = 0;
            while (true) {
                @Pc(44) int coordOffset = packet.gsmart();
                if (coordOffset == 0) {
                    break;
                }
                coord += coordOffset - 1;

                @Pc(59) int locZ = coord & 0x3F;
                @Pc(65) int locX = coord >> 6 & 0x3F;
                @Pc(69) int locLevel = coord >> 12;
                @Pc(73) int shapeAndRotation = packet.g1();
                @Pc(77) int locShape = shapeAndRotation >> 2;
                @Pc(81) int locRotation = shapeAndRotation & 0x3;
                if ((locLevel == pointerLevel) && (locX >= pointerSquareX) && (locX < (pointerSquareX + 8)) && (locZ >= pointerSquareZ) && (locZ < (pointerSquareZ + 8))) {
                    @Pc(113) LocType locType = LocTypeList.instance.list(id);
                    @Pc(130) int tileX = rotateLocX(locX & 0x7, locZ & 0x7, locType.width, locType.length, pointerRotation, locRotation) + x;
                    @Pc(147) int tileZ = rotateLocZ(locX & 0x7, locZ & 0x7, locType.width, locType.length, pointerRotation, locRotation) + z;
                    if (tileX > 0 && tileZ > 0 && tileX < super.width - 1 && tileZ < super.length - 1) {
                        @Pc(173) CollisionMap collisionMap = null;
                        if (!super.underwater) {
                            @Pc(178) int actualLevel = level;
                            if ((Static280.tileFlags[1][tileX][tileZ] & TileFlag.BRIDGE) != 0) {
                                actualLevel = level - 1;
                            }
                            if (actualLevel >= 0) {
                                collisionMap = collisionMaps[actualLevel];
                            }
                        }
                        this.loadLocation(tileX, tileZ, level, level, id, locShape, (pointerRotation + locRotation) & 0x3, -1, collisionMap, toolkit);
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(ZLclient!ha;B)V")
    public void buildRoofOccluders(@OriginalArg(0) boolean skipOccluders, @OriginalArg(1) Toolkit toolkit) {
        Static323.method4624();

        if (!skipOccluders) {
            if (super.levels > 1) {
                for (@Pc(23) int x = 0; x < super.width; x++) {
                    for (@Pc(26) int z = 0; super.length > z; z++) {
                        if ((Static280.tileFlags[1][x][z] & TileFlag.BRIDGE) == 2) {
                            Static646.method8453(x, z);
                        }
                    }
                }
            }

            for (@Pc(23) int level = 0; super.levels > level; level++) {
                for (@Pc(26) int z = 0; super.length >= z; z++) {
                    for (@Pc(71) int x = 0; x <= super.width; x++) {
                        if ((super.occluderFlags[level][x][z] & TileFlag.REMOVE_ROOF) != 0) {
                            @Pc(88) int x1 = x;
                            @Pc(90) int x2 = x;

                            @Pc(92) int z1 = z;
                            @Pc(94) int z2 = z;
                            while (z1 > 0 && (super.occluderFlags[level][x][z1 - 1] & TileFlag.REMOVE_ROOF) != 0 && z - z1 < 10) {
                                z1--;
                            }
                            while (z2 < super.length && (super.occluderFlags[level][x][z2 + 1] & TileFlag.REMOVE_ROOF) != 0 && z2 - z1 < 10) {
                                z2++;
                            }

                            @Pc(163) boolean extendable = true;
                            while (extendable && x1 > 0 && x - x1 < 10) {
                                for (@Pc(163) int localZ = z1; localZ <= z2 && extendable; localZ++) {
                                    extendable = (super.occluderFlags[level][x1 - 1][localZ] & TileFlag.REMOVE_ROOF) != 0;
                                }
                                if (extendable) {
                                    x1--;
                                }
                            }

                            extendable = true;
                            while (extendable && super.width > x2 && x2 - x1 < 10) {
                                for (@Pc(163) int localZ = z1; localZ <= z2 && extendable; localZ++) {
                                    extendable = (super.occluderFlags[level][x2 + 1][localZ] & TileFlag.REMOVE_ROOF) != 0;
                                }
                                if (extendable) {
                                    x2++;
                                }
                            }

                            if ((x2 + 1 - x1) * (z2 + 1 - z1) >= 4) {
                                @Pc(163) int tileHeight = super.tileHeights[level][x1][z1];

                                Static269.method3911((z2 << 9) + 512, x1 << 9, tileHeight, z1 << 9, tileHeight, level, (x2 << 9) + 512);

                                for (@Pc(297) int localX = x1; localX <= x2; localX++) {
                                    for (@Pc(300) int localZ = z1; localZ <= z2; localZ++) {
                                        super.occluderFlags[level][localX][localZ] &= 0xFFFFFFFB;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Static348.method5107();
        }

        super.occluderFlags = null;
    }

    @OriginalMember(owner = "client!taa", name = "a", descriptor = "(IBILclient!eq;IILclient!ha;)V")
    public void removeLoc(@OriginalArg(4) int level, @OriginalArg(5) int x, @OriginalArg(2) int z, @OriginalArg(0) int layer, @OriginalArg(3) CollisionMap collisionMap, @OriginalArg(6) Toolkit toolkit) {
        @Pc(13) Location loc = this.getLoc(level, x, z, layer);
        if (loc == null) {
            return;
        }
        @Pc(22) LocType locType = LocTypeList.instance.list(loc.getId());
        @Pc(26) int shape = loc.getShape();
        @Pc(30) int rotation = loc.getRotation();
        if (locType.hasSounds()) {
            SoundManager.method8312(x, z, level, locType);
        }
        if (loc.hardShadow()) {
            loc.removeShadow(toolkit);
        }
        if (layer == LocLayer.WALL) {
            Static26.clearWalls(level, x, z);
            if (locType.blockwalk != 0) {
                collisionMap.unflagWall(z, rotation, shape, !locType.breakroutefinding, x, locType.blockrange);
            }
            if (locType.occlude == LocOcclusionMode.ALL) {
                if (rotation == 0) {
                    Static687.removeLocationOccluder(x, level, 1, z);
                } else if (rotation == 1) {
                    Static687.removeLocationOccluder(x, level, 2, z + 1);
                } else if (rotation == 2) {
                    Static687.removeLocationOccluder(x + 1, level, 1, z);
                } else if (rotation == 3) {
                    Static687.removeLocationOccluder(x, level, 2, z);
                }
            }
        } else if (layer == LocLayer.WALLDECOR) {
            Static173.clearWallDecor(level, x, z);
        } else if (layer == LocLayer.GROUND) {
            Static10.method130(level, x, z, locClass == null ? (locClass = getClass("com.jagex.game.Location")) : locClass);
            if (locType.blockwalk != 0 && super.width > locType.width + x && super.length > locType.width + z && x + locType.length < super.width && locType.length + z < super.length) {
                collisionMap.unflagLoc(x, z, locType.width, locType.length, rotation, locType.blockrange, !locType.breakroutefinding);
            }
            if (shape == LocShapes.WALL_DIAGONAL) {
                if ((rotation & 0x1) == 0) {
                    Static687.removeLocationOccluder(x, level, 8, z);
                } else {
                    Static687.removeLocationOccluder(x, level, 16, z);
                }
            }
        } else if (layer == LocLayer.GROUNDDECOR) {
            Static609.clearGroundDecor(level, x, z);
            if (locType.blockwalk == 1) {
                collisionMap.unflagGroundDecor(x, z);
            }
        }
    }

    static Class getClass(String name) {
        Class instance;
        try {
            instance = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw (NoClassDefFoundError) new NoClassDefFoundError().initCause(ex);
        }
        return instance;
    }
}
