import com.jagex.core.datastruct.key.LruCache;
import com.jagex.core.datastruct.key.Node2;
import com.jagex.core.io.Packet;
import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qea")
public final class Js5TextureSource implements TextureSource {

    @OriginalMember(owner = "client!bs", name = "i", descriptor = "Lclient!d;")
    public static TextureSource instance;

    @OriginalMember(owner = "client!qea", name = "k", descriptor = "Lclient!ts;")
    public final LruCache recentUse = new LruCache(256);

    @OriginalMember(owner = "client!qea", name = "b", descriptor = "Lclient!sb;")
    public final js5 sprites;

    @OriginalMember(owner = "client!qea", name = "n", descriptor = "Lclient!sb;")
    public final js5 textures;

    @OriginalMember(owner = "client!qea", name = "l", descriptor = "I")
    public final int textureCount;

    @OriginalMember(owner = "client!qea", name = "f", descriptor = "[Lclient!fa;")
    public final TextureMetrics[] textureMetrics;

    @OriginalMember(owner = "client!qea", name = "<init>", descriptor = "(Lclient!sb;Lclient!sb;Lclient!sb;)V")
    public Js5TextureSource(@OriginalArg(0) js5 materials, @OriginalArg(1) js5 textures, @OriginalArg(2) js5 sprites) {
        this.sprites = sprites;
        this.textures = textures;

        @Pc(24) Packet packet = new Packet(materials.getfile(0, 0));
        this.textureCount = packet.g2();
        this.textureMetrics = new TextureMetrics[this.textureCount];
        for (@Pc(36) int i = 0; i < this.textureCount; i++) {
            if (packet.g1() == 1) {
                this.textureMetrics[i] = new TextureMetrics();
            }
        }
        for (@Pc(65) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].disableable = packet.g1() == 0;
            }
        }
        for (@Pc(98) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].small = packet.g1() == 1;
            }
        }
        for (@Pc(132) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].skipFaces = packet.g1() == 1;
            }
        }
        for (@Pc(168) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].brightness = packet.g1b();
            }
        }
        for (@Pc(200) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].alpha = packet.g1b();
            }
        }
        for (@Pc(228) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].effectType = packet.g1b();
            }
        }
        for (@Pc(256) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].effectParam1 = packet.g1b();
            }
        }
        for (@Pc(288) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].averageColour = (short) packet.g2();
            }
        }
        for (@Pc(321) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].speedU = packet.g1b();
            }
        }
        for (@Pc(353) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].speedV = packet.g1b();
            }
        }
        for (@Pc(381) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].unusedFlag = packet.g1() == 1;
            }
        }
        for (@Pc(421) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].transposed = packet.g1() == 1;
            }
        }
        for (@Pc(455) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].mipmap = packet.g1b();
            }
        }
        for (@Pc(483) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].repeatsU = packet.g1() == 1;
            }
        }
        for (@Pc(521) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].repeatsV = packet.g1() == 1;
            }
        }
        for (@Pc(559) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].hdr = packet.g1() == 1;
            }
        }
        for (@Pc(595) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].colorOp = packet.g1();
            }
        }
        for (@Pc(627) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].effectParam2 = packet.g4();
            }
        }
        for (@Pc(655) int i = 0; i < this.textureCount; i++) {
            if (this.textureMetrics[i] != null) {
                this.textureMetrics[i].alphaBlendMode = packet.g1();
            }
        }
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(IB)Lclient!vm;")
    public Texture list(@OriginalArg(0) int id) {
        @Pc(10) Node2 cached = this.recentUse.get(id);
        if (cached != null) {
            return (Texture) cached;
        }

        @Pc(30) byte[] data = this.textures.getfile(id);
        if (data == null) {
            return null;
        } else {
            @Pc(43) Texture texture = new Texture(new Packet(data));
            this.recentUse.put(texture, id);
            return texture;
        }
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(IZIIBF)[I")
    @Override
    public int[] rgbOutput(@OriginalArg(0) int width, @OriginalArg(1) boolean reverse, @OriginalArg(2) int height, @OriginalArg(3) int id, @OriginalArg(5) float gamma) {
        return this.list(id).rgbOutput(this.sprites, gamma, this.textureMetrics[id].transposed, this, reverse, height, width);
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(I)I")
    @Override
    public int textureCount() {
        return this.textureCount;
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(II)Z")
    @Override
    public boolean textureAvailable(@OriginalArg(1) int id) {
        @Pc(8) Texture texture = this.list(id);
        return texture != null && texture.available(this.sprites, this);
    }

    @OriginalMember(owner = "client!qea", name = "b", descriptor = "(II)Lclient!fa;")
    @Override
    public TextureMetrics getMetrics(@OriginalArg(0) int id) {
        return this.textureMetrics[id];
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(IIFBIZ)[F")
    @Override
    public float[] floatArgbOutput(@OriginalArg(0) int height, @OriginalArg(1) int id, @OriginalArg(2) float gamma, @OriginalArg(4) int width) {
        return this.list(id).floatArgbOutput(height, this, this.sprites, this.textureMetrics[id].transposed, width);
    }

    @OriginalMember(owner = "client!qea", name = "a", descriptor = "(FIIZZI)[I")
    @Override
    public int[] argbOutput(@OriginalArg(0) float gamma, @OriginalArg(1) int id, @OriginalArg(2) int width, @OriginalArg(5) int height) {
        return this.list(id).argbOutput(width, gamma, this.textureMetrics[id].transposed, height, this, this.sprites);
    }
}
