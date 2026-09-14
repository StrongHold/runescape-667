import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.Node_Sub1_Sub27;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.util.Random;

/**
 * Scatters short straight strokes over the whole texture, each one starting at a random texel, run
 * at a random angle around a base angle, and shaded with a ramp along its length.
 */
@OriginalClass("client!kga")
public final class TextureOpScratch extends TextureOp {

    @OriginalMember(owner = "client!kga", name = "F", descriptor = "I")
    public int angle = 0;

    @OriginalMember(owner = "client!kga", name = "R", descriptor = "I")
    public int seed = 0;

    @OriginalMember(owner = "client!kga", name = "J", descriptor = "I")
    public int angleVariance = 4096;

    @OriginalMember(owner = "client!kga", name = "H", descriptor = "I")
    public int length = 16;

    @OriginalMember(owner = "client!kga", name = "P", descriptor = "I")
    public int count = 2000;

    @OriginalMember(owner = "client!kga", name = "<init>", descriptor = "()V")
    public TextureOpScratch() {
        super(0, true);
    }

    @OriginalMember(owner = "client!kga", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.seed = arg1.g1();
        } else if (arg2 == 1) {
            this.count = arg1.g2();
        } else if (arg2 == 2) {
            this.length = arg1.g1();
        } else if (arg2 == 3) {
            this.angle = arg1.g2();
        } else if (arg2 == 4) {
            this.angleVariance = arg1.g2();
        }
        if (arg0) {
            this.monochromeOutput(-52, 90);
        }
    }

    @OriginalMember(owner = "client!kga", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(20) int halfAngleVariance = this.angleVariance >> 1;
            @Pc(25) int[][] rows = super.monochromeCache.get();
            @Pc(32) Random random = new Random(this.seed);
            for (@Pc(34) int stroke = 0; stroke < this.count; stroke++) {
                @Pc(60) int strokeAngle = this.angleVariance > 0 ? this.angle + Node_Sub1_Sub27.method8326(-5208, this.angleVariance, random) - halfAngleVariance : this.angle;
                @Pc(66) int angleIndex = strokeAngle >> 4 & 0xFF;
                @Pc(71) int startX = Node_Sub1_Sub27.method8326(-5208, EnvironmentLight.anInt9289, random);
                @Pc(76) int startY = Node_Sub1_Sub27.method8326(-5208, EnvironmentLight.anInt53, random);
                @Pc(87) int endX = startX + (Static24.anIntArray33[angleIndex] * this.length >> 12);
                @Pc(98) int endY = startY + (this.length * Static222.anIntArray289[angleIndex] >> 12);
                @Pc(103) int deltaY = endY - startY;
                @Pc(108) int deltaX = endX - startX;
                if (deltaX != 0 || deltaY != 0) {
                    if (deltaY < 0) {
                        deltaY = -deltaY;
                    }
                    if (deltaX < 0) {
                        deltaX = -deltaX;
                    }
                    @Pc(134) boolean steep = deltaY > deltaX;
                    @Pc(138) int local138;
                    @Pc(140) int local140;
                    if (steep) {
                        local138 = startX;
                        local140 = endX;
                        startX = startY;
                        endX = endY;
                        startY = local138;
                        endY = local140;
                    }
                    if (startX > endX) {
                        local138 = startX;
                        local140 = startY;
                        startX = endX;
                        endX = local138;
                        startY = endY;
                        endY = local140;
                    }
                    local138 = startY;
                    local140 = endX - startX;
                    @Pc(179) int rise = endY - startY;
                    @Pc(184) int error = -local140 / 2;
                    @Pc(188) int shadeStep = 2048 / local140;
                    @Pc(198) int shadeOffset = 1024 - (Node_Sub1_Sub27.method8326(-5208, 4096, random) >> 2);
                    if (rise < 0) {
                        rise = -rise;
                    }
                    @Pc(214) int minorStep = endY > startY ? 1 : -1;
                    for (@Pc(216) int major = startX; major < endX; major++) {
                        @Pc(228) int shade = shadeStep * (major - startX) + shadeOffset + 1024;
                        @Pc(232) int wrappedMajor = EnvironmentLight.anInt8580 & major;
                        @Pc(236) int wrappedMinor = local138 & EnvironmentLight.anInt7343;
                        if (steep) {
                            rows[wrappedMinor][wrappedMajor] = shade;
                        } else {
                            rows[wrappedMajor][wrappedMinor] = shade;
                        }
                        error += rise;
                        if (error > 0) {
                            local138 += minorStep;
                            error += -local140;
                        }
                    }
                }
            }
        }
        if (arg0 < 107) {
            this.angleVariance = -21;
        }
        return output;
    }

    @OriginalMember(owner = "client!kga", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        Static481.method6475();
    }
}
