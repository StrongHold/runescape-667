import com.jagex.Class67;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereTypeList;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxTypeList;
import com.jagex.graphics.skybox.SkyBox;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!uc")
public final class Environment {

    @OriginalMember(owner = "client!dr", name = "c", descriptor = "Lclient!dla;")
    public static final ReferenceCache skyBoxCache = new ReferenceCache(8);

    @OriginalMember(owner = "client!ie", name = "j", descriptor = "Lclient!dla;")
    public static final ReferenceCache cubeMapCache = new ReferenceCache(8);

    @OriginalMember(owner = "client!kr", name = "a", descriptor = "(ZIIII)Lclient!gm;")
    public static SkyBox skyBox(@OriginalArg(1) int sphereOffsetY, @OriginalArg(2) int id, @OriginalArg(3) int sphereOffsetX, @OriginalArg(4) int sphereOffsetZ) {
        @Pc(31) long key = ((long) id & 0xFFFFL) | (((long) sphereOffsetZ & 0xFFFFL) << 16) | (((long) sphereOffsetX << 48) & (0xFFFFL << 48)) | (((long) sphereOffsetY & 0xFFFFL) << 32);
        @Pc(43) SkyBox skyBox = (SkyBox) skyBoxCache.get(key);
        if (skyBox == null) {
            skyBox = SkyBoxTypeList.instance.skyBox(SkyBoxSphereTypeList.instance, sphereOffsetZ, id, sphereOffsetY, sphereOffsetX);
            skyBoxCache.put(skyBox, key);
        }
        return skyBox;
    }

    @OriginalMember(owner = "client!lo", name = "a", descriptor = "(IIIIIII)Lclient!pu;")
    public static Class67 cubeMap(@OriginalArg(0) int texture4, @OriginalArg(2) int texture1, @OriginalArg(3) int texture2, @OriginalArg(4) int texture3, @OriginalArg(5) int texture5, @OriginalArg(6) int texture0) {
        @Pc(33) long key = (long) texture0 * 67481L ^ (long) texture1 * 97549L ^ (long) texture2 * 475427L ^ (long) texture3 * 986053L ^ (long) texture4 * 32147369L ^ (long) texture5 * 76724863L;
        @Pc(39) Class67 cubeMap = (Class67) cubeMapCache.get(key);
        if (cubeMap == null) {
            cubeMap = Static425.toolkit.method8008(texture0, texture1, texture2, texture3, texture4, texture5);
            cubeMapCache.put(cubeMap, key);
            return cubeMap;
        } else {
            return cubeMap;
        }
    }

    @OriginalMember(owner = "client!eu", name = "f", descriptor = "(I)V")
    public static void cacheReset() {
        cubeMapCache.reset();
        skyBoxCache.reset();
    }

    @OriginalMember(owner = "client!uc", name = "k", descriptor = "F")
    public float aFloat201 = 1.0F;

    @OriginalMember(owner = "client!uc", name = "m", descriptor = "F")
    public float aFloat203 = 1.0F;

    @OriginalMember(owner = "client!uc", name = "o", descriptor = "F")
    public float aFloat200 = 0.25F;

    @OriginalMember(owner = "client!uc", name = "e", descriptor = "I")
    public int sunY;

    @OriginalMember(owner = "client!uc", name = "t", descriptor = "Lclient!pu;")
    public Class67 cubeMap;

    @OriginalMember(owner = "client!uc", name = "i", descriptor = "I")
    public int sunX;

    @OriginalMember(owner = "client!uc", name = "d", descriptor = "F")
    public float reverseSunIntensity;

    @OriginalMember(owner = "client!uc", name = "s", descriptor = "Lclient!gm;")
    public SkyBox skyBox;

    @OriginalMember(owner = "client!uc", name = "p", descriptor = "I")
    public int sunColour;

    @OriginalMember(owner = "client!uc", name = "g", descriptor = "F")
    public float sunIntensity;

    @OriginalMember(owner = "client!uc", name = "v", descriptor = "F")
    public float ambient;

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "I")
    public int sunZ;

    @OriginalMember(owner = "client!uc", name = "f", descriptor = "I")
    public int fogColour;

    @OriginalMember(owner = "client!uc", name = "c", descriptor = "I")
    public int fogRange;

    @OriginalMember(owner = "client!uc", name = "<init>", descriptor = "()V")
    public Environment() {
        this.sunY = -60;
        this.cubeMap = Static226.aClass67_9;
        this.sunX = -50;
        this.reverseSunIntensity = 1.2F;
        this.skyBox = Static495.aSkyBox_4;
        this.sunColour = Static68.anInt4096;
        this.sunIntensity = 0.69921875F;
        this.ambient = 1.1523438F;
        this.sunZ = -50;
        this.fogColour = Scene.DEFAULT_FOG_COLOUR;
        this.fogRange = 0;
    }

    @OriginalMember(owner = "client!uc", name = "<init>", descriptor = "(Lclient!ge;)V")
    public Environment(@OriginalArg(0) Packet packet) {
        this.decodeLighting(packet);
    }

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "(Lclient!ge;I)V")
    public void decodeSkyBox(@OriginalArg(0) Packet packet) {
        @Pc(17) int id = packet.g2();
        @Pc(21) int sphereOffsetX = packet.g2s();
        @Pc(25) int sphereOffsetY = packet.g2s();
        @Pc(29) int sphereOffsetZ = packet.g2s();
        @Pc(33) int rotation = packet.g2();
        Static436.anInt3852 = rotation;
        this.skyBox = skyBox(sphereOffsetY, id, sphereOffsetX, sphereOffsetZ);
    }

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "(Lclient!ge;Z)V")
    public void decodeBloomParams(@OriginalArg(0) Packet packet) {
        this.aFloat201 = (float) (packet.g1() * 8) / 255.0F;
        this.aFloat200 = (float) (packet.g1() * 8) / 255.0F;
        this.aFloat203 = (float) (packet.g1() * 8) / 255.0F;
    }

    @OriginalMember(owner = "client!uc", name = "b", descriptor = "(Lclient!ge;I)V")
    public void decodeLighting(@OriginalArg(0) Packet packet) {
        @Pc(7) int flags = packet.g1();
        if (ClientOptions.instance.lightDetail.getValue() == 1 && Static425.toolkit.getMaxLights() > 0) {
            if ((flags & 0x1) == 0) {
                this.sunColour = Static68.anInt4096;
            } else {
                this.sunColour = packet.g4();
            }
            if ((flags & 0x2) == 0) {
                this.ambient = 1.1523438F;
            } else {
                this.ambient = (float) packet.g2() / 256.0F;
            }
            if ((flags & 0x4) == 0) {
                this.sunIntensity = 0.69921875F;
            } else {
                this.sunIntensity = (float) packet.g2() / 256.0F;
            }
            if ((flags & 0x8) == 0) {
                this.reverseSunIntensity = 1.2F;
            } else {
                this.reverseSunIntensity = (float) packet.g2() / 256.0F;
            }
        } else {
            if ((flags & 0x1) != 0) {
                packet.g4();
            }
            if ((flags & 0x2) != 0) {
                packet.g2();
            }
            if ((flags & 0x4) != 0) {
                packet.g2();
            }
            if ((flags & 0x8) != 0) {
                packet.g2();
            }
            this.reverseSunIntensity = 1.2F;
            this.ambient = 1.1523438F;
            this.sunIntensity = 0.69921875F;
            this.sunColour = Static68.anInt4096;
        }
        if ((flags & 0x10) == 0) {
            this.sunX = -50;
            this.sunZ = -50;
            this.sunY = -60;
        } else {
            this.sunX = packet.g2s();
            this.sunY = packet.g2s();
            this.sunZ = packet.g2s();
        }
        if ((flags & 0x20) == 0) {
            this.fogColour = Scene.DEFAULT_FOG_COLOUR;
        } else {
            this.fogColour = packet.g4();
        }
        if ((flags & 0x40) == 0) {
            this.fogRange = 0;
        } else {
            this.fogRange = packet.g2();
        }
        if ((flags & 0x80) == 0) {
            this.cubeMap = Static226.aClass67_9;
            return;
        }
        @Pc(251) int texture0 = packet.g2();
        @Pc(255) int texture1 = packet.g2();
        @Pc(261) int texture2 = packet.g2();
        @Pc(265) int texture3 = packet.g2();
        @Pc(271) int texture4 = packet.g2();
        @Pc(275) int texture5 = packet.g2();
        this.cubeMap = cubeMap(texture4, texture1, texture2, texture3, texture5, texture0);
    }

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "(BLclient!uc;)Z")
    public boolean equalTo(@OriginalArg(1) Environment other) {
        return this.sunColour == other.sunColour && other.ambient == this.ambient && this.sunIntensity == other.sunIntensity && this.reverseSunIntensity == other.reverseSunIntensity && other.aFloat200 == this.aFloat200 && this.aFloat201 == other.aFloat201 && this.aFloat203 == other.aFloat203 && this.fogColour == other.fogColour && other.fogRange == this.fogRange && other.cubeMap == this.cubeMap && other.skyBox == this.skyBox;
    }
}
