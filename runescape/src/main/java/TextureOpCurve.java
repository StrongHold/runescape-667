import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Remaps the intensity of a source layer through a curve. The curve is given as a list of markers,
 * each one an input intensity paired with an output intensity, and the span between two markers is
 * filled in by linear, cosine or cubic interpolation. The result is baked into a lookup table of
 * 257 entries, one for every input intensity truncated to eight bits, plus the top of the range.
 */
@OriginalClass("client!wfa")
public final class TextureOpCurve extends TextureOp {

    @OriginalMember(owner = "client!wfa", name = "K", descriptor = "[[I")
    public int[][] markers;

    @OriginalMember(owner = "client!wfa", name = "M", descriptor = "[I")
    public int[] trailingMarker;

    @OriginalMember(owner = "client!wfa", name = "G", descriptor = "[I")
    public int[] leadingMarker;

    @OriginalMember(owner = "client!wfa", name = "O", descriptor = "[S")
    public final short[] lookup = new short[257];

    @OriginalMember(owner = "client!wfa", name = "R", descriptor = "I")
    public int interpolation = 0;

    @OriginalMember(owner = "client!wfa", name = "<init>", descriptor = "()V")
    public TextureOpCurve() {
        super(1, true);
    }

    @OriginalMember(owner = "client!wfa", name = "g", descriptor = "(I)V")
    public void updateEndMarkers(@OriginalArg(0) int offset) {
        @Pc(8) int[] first = this.markers[0];
        @Pc(13) int[] second = this.markers[1];
        @Pc(22) int[] secondLast = this.markers[offset + this.markers.length];
        @Pc(31) int[] last = this.markers[this.markers.length - 1];
        this.leadingMarker = new int[]{first[0] + first[0] - second[0], -second[1] - -first[1] + first[1]};
        this.trailingMarker = new int[]{secondLast[0] + secondLast[0] - last[0], secondLast[1] + -last[1] + secondLast[1]};
    }

    @OriginalMember(owner = "client!wfa", name = "c", descriptor = "(I)V")
    @Override
    public void postDecode() {
        if (this.markers == null) {
            this.markers = new int[][]{new int[2], {4096, 4096}};
        }
        if (this.markers.length < 2) {
            throw new RuntimeException("Curve operation requires at least two markers");
        }
        if (this.interpolation == 2) {
            this.updateEndMarkers(7 ^ 0xFFFFFFF9);
        }
        Static481.method6475();
        this.buildLookup();
    }

    @OriginalMember(owner = "client!wfa", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (super.monochromeCache.dirty) {
            @Pc(21) int[] source = this.method9422(y, 0);
            for (@Pc(23) int x = 0; x < EnvironmentLight.anInt9289; x++) {
                @Pc(31) int index = source[x] >> 4;
                if (index < 0) {
                    index = 0;
                }
                if (index > 256) {
                    index = 256;
                }
                output[x] = this.lookup[index];
            }
        }
        if (arg0 <= 107) {
            FriendChat.count = 6;
        }
        return output;
    }

    @OriginalMember(owner = "client!wfa", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg0) {
            Static706.floor = null;
        }
        if (arg2 != 0) {
            return;
        }
        this.interpolation = arg1.g1();
        this.markers = new int[arg1.g1()][2];
        for (@Pc(31) int index = 0; index < this.markers.length; index++) {
            this.markers[index][0] = arg1.g2();
            this.markers[index][1] = arg1.g2();
        }
    }

    @OriginalMember(owner = "client!wfa", name = "a", descriptor = "(Z)V")
    public void buildLookup() {
        @Pc(8) int local8 = this.interpolation;
        @Pc(30) int index;
        @Pc(28) int input;
        @Pc(69) int[] start;
        @Pc(74) int[] end;
        @Pc(83) int local83;
        @Pc(87) int local87;
        @Pc(91) int local91;
        @Pc(100) int local100;
        if (local8 == 2) {
            for (local8 = 0; local8 < 257; local8++) {
                input = local8 << 4;
                for (index = 1; this.markers.length - 1 > index && input >= this.markers[index][0]; index++) {
                }
                start = this.markers[index - 1];
                end = this.markers[index];
                local83 = this.markerAt(index - 2)[1];
                local87 = start[1];
                local91 = end[1];
                local100 = this.markerAt(index + 1)[1];
                @Pc(118) int fraction = (input - start[0] << 12) / (end[0] - start[0]);
                @Pc(124) int fractionSquared = fraction * fraction >> 12;
                @Pc(133) int cubic = local100 + local87 - local83 - local91;
                @Pc(141) int quadratic = local83 - local87 - cubic;
                @Pc(145) int linear = local91 - local83;
                @Pc(157) int cubicTerm = fractionSquared * (cubic * fraction >> 12) >> 12;
                @Pc(163) int quadraticTerm = fractionSquared * quadratic >> 12;
                @Pc(169) int linearTerm = linear * fraction >> 12;
                @Pc(177) int value = linearTerm + quadraticTerm + cubicTerm + local87;
                if (value <= -32768) {
                    value = -32767;
                }
                if (value >= 32768) {
                    value = 32767;
                }
                this.lookup[local8] = (short) value;
            }
        } else if (local8 == 1) {
            for (local8 = 0; local8 < 257; local8++) {
                input = local8 << 4;
                for (index = 1; index < this.markers.length - 1 && this.markers[index][0] <= input; index++) {
                }
                start = this.markers[index - 1];
                end = this.markers[index];
                local83 = (input - start[0] << 12) / (end[0] - start[0]);
                local87 = 4096 - Static24.anIntArray33[local83 >> 5 & 0xFF] >> 1;
                local91 = 4096 - local87;
                local100 = local87 * end[1] + local91 * start[1] >> 12;
                if (local100 <= -32768) {
                    local100 = -32767;
                }
                if (local100 >= 32768) {
                    local100 = 32767;
                }
                this.lookup[local8] = (short) local100;
            }
        } else {
            for (local8 = 0; local8 < 257; local8++) {
                input = local8 << -1753698556;
                for (index = 1; index < this.markers.length - 1 && input >= this.markers[index][0]; index++) {
                }
                start = this.markers[index - 1];
                end = this.markers[index];
                local83 = (input - start[0] << 12) / (end[0] - start[0]);
                local87 = 4096 - local83;
                local91 = local83 * end[1] + local87 * start[1] >> 12;
                if (local91 <= -32768) {
                    local91 = -32767;
                }
                if (local91 >= 32768) {
                    local91 = 32767;
                }
                this.lookup[local8] = (short) local91;
            }
        }
    }

    @OriginalMember(owner = "client!wfa", name = "b", descriptor = "(II)[I")
    public int[] markerAt(@OriginalArg(1) int index) {
        if (index < 0) {
            return this.leadingMarker;
        } else if (index >= this.markers.length) {
            return this.trailingMarker;
        } else {
            return this.markers[index];
        }
    }
}
