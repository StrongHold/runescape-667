import com.jagex.Client;
import com.jagex.Entity;
import com.jagex.core.constants.MaxScreenSize;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static468 {

    @OriginalMember(owner = "client!op", name = "l", descriptor = "[Lclient!eo;")
    public static Entity[] dynamicEntities;

    @OriginalMember(owner = "client!op", name = "a", descriptor = "(ZIII)V")
    public static void updateObjCount(@OriginalArg(1) int level, @OriginalArg(3) int localX, @OriginalArg(2) int localZ) {
        @Pc(8) int absoluteX = localX + WorldMap.areaBaseX;
        @Pc(12) int absoluteZ = localZ + WorldMap.areaBaseZ;

        if (Static334.activeTiles == null || localX < 0 || localZ < 0 || localX >= Static720.mapWidth || Static501.mapLength <= localZ || ClientOptions.instance.animateBackground.getValue() == 0 && level != PlayerEntity.self.level) {
            return;
        }

        @Pc(67) long key = (level << 28) | (absoluteZ << 14) | absoluteX;
        @Pc(73) ObjStack stack = (ObjStack) Static497.objStacks.get(key);
        if (stack == null) {
            Static638.removeObjStack(level, localX, localZ);
            return;
        }

        @Pc(88) ObjStackEntry firstEntry = (ObjStackEntry) stack.objs.first();
        if (firstEntry == null) {
            Static638.removeObjStack(level, localX, localZ);
            return;
        }

        @Pc(103) ObjStackEntity entity = (ObjStackEntity) Static638.removeObjStack(level, localX, localZ);
        if (entity == null) {
            entity = new ObjStackEntity(localX << 9, Static246.ground[level].getHeight(localX, localZ), localZ << 9, level, level);
        } else {
            entity.secondId = entity.thirdId = -1;
        }

        entity.firstCount = firstEntry.count;
        entity.firstId = firstEntry.id;

        label56:
        while (true) {
            @Pc(146) ObjStackEntry secondEntry = (ObjStackEntry) stack.objs.next();
            if (secondEntry == null) {
                break;
            }

            if (secondEntry.id != entity.firstId) {
                entity.secondCount = secondEntry.count;
                entity.secondId = secondEntry.id;

                while (true) {
                    @Pc(171) ObjStackEntry thirdEntry = (ObjStackEntry) stack.objs.next();
                    if (thirdEntry == null) {
                        break label56;
                    }

                    if (entity.firstId != thirdEntry.id && thirdEntry.id != entity.secondId) {
                        entity.thirdCount = thirdEntry.count;
                        entity.thirdId = thirdEntry.id;
                    }
                }
            }
        }

        @Pc(209) int averageHeight = Static102.averageHeight(level, (localX << 9) - -256, (localZ << 9) + 256);
        entity.level = (byte) level;
        entity.y = averageHeight;
        entity.virtualLevel = (byte) level;
        entity.z = localZ << 9;
        entity.anInt8885 = 0;
        entity.x = localX << 9;

        if (Static441.isBridgeAt(localZ, localX)) {
            entity.virtualLevel++;
        }

        Static157.setObjStack(level, localX, localZ, averageHeight, entity);
    }

    @OriginalMember(owner = "client!op", name = "a", descriptor = "(ZZ)V")
    public static void method7643() {
        ClientOptions.instance.update(0, ClientOptions.instance.animateBackgroundDefault);
        ClientOptions.instance.update(0, ClientOptions.instance.animateBackground);
        ClientOptions.instance.update(1, ClientOptions.instance.removeRoofs);
        ClientOptions.instance.update(1, ClientOptions.instance.removeRoofsOverride);
        ClientOptions.instance.update(0, ClientOptions.instance.groundDecor);
        ClientOptions.instance.update(0, ClientOptions.instance.fog);
        ClientOptions.instance.update(0, ClientOptions.instance.groundBlending);
        ClientOptions.instance.update(0, ClientOptions.instance.idleAnimations);
        ClientOptions.instance.update(0, ClientOptions.instance.flickeringEffects);
        ClientOptions.instance.update(0, ClientOptions.instance.spotShadows);
        ClientOptions.instance.update(0, ClientOptions.instance.hardShadows);
        ClientOptions.instance.update(0, ClientOptions.instance.textures);
        ClientOptions.instance.update(0, ClientOptions.instance.lightDetail);
        ClientOptions.instance.update(0, ClientOptions.instance.waterDetail);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingMode);
        ClientOptions.instance.update(0, ClientOptions.instance.antialiasingQuality);
        ClientOptions.instance.update(0, ClientOptions.instance.particles);
        ClientOptions.instance.update(0, ClientOptions.instance.buildArea);
        ClientOptions.instance.update(0, ClientOptions.instance.bloom);
        ClientOptions.instance.update(0, ClientOptions.instance.skydetail);
        Static376.method5313();
        ClientOptions.instance.update(MaxScreenSize._800x600, ClientOptions.instance.maxScreenSize);
        ClientOptions.instance.update(1, ClientOptions.instance.graphicsQuality);
        Static296.updateFeatureMask();
        InterfaceManager.loginOpened();
        Client.changingWindowMode = true;
    }

    @OriginalMember(owner = "client!op", name = "a", descriptor = "(BLclient!eba;)I")
    public static int toGlPixelFormat(@OriginalArg(1) Class92 format) {
        if (format == Static685.aClass92_16) {
            return 6407;
        } else if (format == Static172.aClass92_8) {
            return 6408;
        } else if (format == Static679.aClass92_15) {
            return 6406;
        } else if (format == Static661.aClass92_10) {
            return 6409;
        } else if (format == Static482.aClass92_13) {
            return 6410;
        } else if (format == Static42.aClass92_3) {
            return 6145;
        } else {
            throw new IllegalStateException();
        }
    }

    @OriginalMember(owner = "client!op", name = "a", descriptor = "(IIB)I")
    public static int shadeHsl(@OriginalArg(0) int hsl, @OriginalArg(1) int lightness) {
        lightness = (hsl & 0x7F) * lightness >> 7;
        if (lightness < 2) {
            lightness = 2;
        } else if (lightness > 126) {
            lightness = 126;
        }
        return (hsl & 0xFF80) + lightness;
    }
}
