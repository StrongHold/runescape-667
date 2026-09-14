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
    public static Class67 method5301(@OriginalArg(0) int texture4, @OriginalArg(2) int texture1, @OriginalArg(3) int texture2, @OriginalArg(4) int texture3, @OriginalArg(5) int texture5, @OriginalArg(6) int texture0) {
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
    public int anInt9533;

    @OriginalMember(owner = "client!uc", name = "t", descriptor = "Lclient!pu;")
    public Class67 aClass67_10;

    @OriginalMember(owner = "client!uc", name = "i", descriptor = "I")
    public int anInt9535;

    @OriginalMember(owner = "client!uc", name = "d", descriptor = "F")
    public float aFloat202;

    @OriginalMember(owner = "client!uc", name = "s", descriptor = "Lclient!gm;")
    public SkyBox aSkyBox_5;

    @OriginalMember(owner = "client!uc", name = "p", descriptor = "I")
    public int anInt9537;

    @OriginalMember(owner = "client!uc", name = "g", descriptor = "F")
    public float aFloat205;

    @OriginalMember(owner = "client!uc", name = "v", descriptor = "F")
    public float aFloat204;

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "I")
    public int anInt9534;

    @OriginalMember(owner = "client!uc", name = "f", descriptor = "I")
    public int anInt9538;

    @OriginalMember(owner = "client!uc", name = "c", descriptor = "I")
    public int anInt9539;

    @OriginalMember(owner = "client!uc", name = "<init>", descriptor = "()V")
    public Environment() {
        this.anInt9533 = -60;
        this.aClass67_10 = Static226.aClass67_9;
        this.anInt9535 = -50;
        this.aFloat202 = 1.2F;
        this.aSkyBox_5 = Static495.aSkyBox_4;
        this.anInt9537 = Static68.anInt4096;
        this.aFloat205 = 0.69921875F;
        this.aFloat204 = 1.1523438F;
        this.anInt9534 = -50;
        this.anInt9538 = Scene.DEFAULT_FOG_COLOUR;
        this.anInt9539 = 0;
    }

    @OriginalMember(owner = "client!uc", name = "<init>", descriptor = "(Lclient!ge;)V")
    public Environment(@OriginalArg(0) Packet packet) {
        this.method8386(packet);
    }

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "(Lclient!ge;I)V")
    public void method8384(@OriginalArg(0) Packet packet) {
        @Pc(17) int id = packet.g2();
        @Pc(21) int sphereOffsetX = packet.g2s();
        @Pc(25) int sphereOffsetY = packet.g2s();
        @Pc(29) int sphereOffsetZ = packet.g2s();
        @Pc(33) int rotation = packet.g2();
        Static436.anInt3852 = rotation;
        this.aSkyBox_5 = skyBox(sphereOffsetY, id, sphereOffsetX, sphereOffsetZ);
    }

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "(Lclient!ge;Z)V")
    public void decodeBloomParams(@OriginalArg(0) Packet packet) {
        this.aFloat201 = (float) (packet.g1() * 8) / 255.0F;
        this.aFloat200 = (float) (packet.g1() * 8) / 255.0F;
        this.aFloat203 = (float) (packet.g1() * 8) / 255.0F;
    }

    @OriginalMember(owner = "client!uc", name = "b", descriptor = "(Lclient!ge;I)V")
    public void method8386(@OriginalArg(0) Packet packet) {
        @Pc(7) int flags = packet.g1();
        if (ClientOptions.instance.lightDetail.getValue() == 1 && Static425.toolkit.getMaxLights() > 0) {
            if ((flags & 0x1) == 0) {
                this.anInt9537 = Static68.anInt4096;
            } else {
                this.anInt9537 = packet.g4();
            }
            if ((flags & 0x2) == 0) {
                this.aFloat204 = 1.1523438F;
            } else {
                this.aFloat204 = (float) packet.g2() / 256.0F;
            }
            if ((flags & 0x4) == 0) {
                this.aFloat205 = 0.69921875F;
            } else {
                this.aFloat205 = (float) packet.g2() / 256.0F;
            }
            if ((flags & 0x8) == 0) {
                this.aFloat202 = 1.2F;
            } else {
                this.aFloat202 = (float) packet.g2() / 256.0F;
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
            this.aFloat202 = 1.2F;
            this.aFloat204 = 1.1523438F;
            this.aFloat205 = 0.69921875F;
            this.anInt9537 = Static68.anInt4096;
        }
        if ((flags & 0x10) == 0) {
            this.anInt9535 = -50;
            this.anInt9534 = -50;
            this.anInt9533 = -60;
        } else {
            this.anInt9535 = packet.g2s();
            this.anInt9533 = packet.g2s();
            this.anInt9534 = packet.g2s();
        }
        if ((flags & 0x20) == 0) {
            this.anInt9538 = Scene.DEFAULT_FOG_COLOUR;
        } else {
            this.anInt9538 = packet.g4();
        }
        if ((flags & 0x40) == 0) {
            this.anInt9539 = 0;
        } else {
            this.anInt9539 = packet.g2();
        }
        if ((flags & 0x80) == 0) {
            this.aClass67_10 = Static226.aClass67_9;
            return;
        }
        @Pc(251) int texture0 = packet.g2();
        @Pc(255) int texture1 = packet.g2();
        @Pc(261) int texture2 = packet.g2();
        @Pc(265) int texture3 = packet.g2();
        @Pc(271) int texture4 = packet.g2();
        @Pc(275) int texture5 = packet.g2();
        this.aClass67_10 = method5301(texture4, texture1, texture2, texture3, texture5, texture0);
    }

    @OriginalMember(owner = "client!uc", name = "a", descriptor = "(BLclient!uc;)Z")
    public boolean method8388(@OriginalArg(1) Environment other) {
        return this.anInt9537 == other.anInt9537 && other.aFloat204 == this.aFloat204 && this.aFloat205 == other.aFloat205 && this.aFloat202 == other.aFloat202 && other.aFloat200 == this.aFloat200 && this.aFloat201 == other.aFloat201 && this.aFloat203 == other.aFloat203 && this.anInt9538 == other.anInt9538 && other.anInt9539 == this.anInt9539 && other.aClass67_10 == this.aClass67_10 && other.aSkyBox_5 == this.aSkyBox_5;
    }
}
