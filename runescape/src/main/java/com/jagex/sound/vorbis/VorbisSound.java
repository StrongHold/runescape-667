package com.jagex.sound.vorbis;

import com.jagex.core.datastruct.key.Node;
import com.jagex.core.io.Packet;
import com.jagex.js5.js5;
import com.jagex.math.IntMath;
import com.jagex.sound.VariableRateSoundPacket;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!uj")
public final class VorbisSound extends Node {

    @OriginalMember(owner = "client!uj", name = "B", descriptor = "I")
    public static int blocksize_1;

    @OriginalMember(owner = "client!uj", name = "q", descriptor = "[F")
    public static float[] C_1;

    @OriginalMember(owner = "client!uj", name = "n", descriptor = "[I")
    public static int[] modeMapping;

    @OriginalMember(owner = "client!uj", name = "I", descriptor = "[B")
    public static byte[] data;

    @OriginalMember(owner = "client!uj", name = "L", descriptor = "I")
    public static int bitPos;

    @OriginalMember(owner = "client!uj", name = "N", descriptor = "[Lclient!l;")
    public static VorbisResidue[] residues;

    @OriginalMember(owner = "client!uj", name = "G", descriptor = "[I")
    public static int[] BITREVERSE_1;

    @OriginalMember(owner = "client!uj", name = "K", descriptor = "[F")
    public static float[] A_1;

    @OriginalMember(owner = "client!uj", name = "M", descriptor = "[F")
    public static float[] currentWindow;

    @OriginalMember(owner = "client!uj", name = "C", descriptor = "[F")
    public static float[] A_0;

    @OriginalMember(owner = "client!uj", name = "p", descriptor = "I")
    public static int blocksize_0;

    @OriginalMember(owner = "client!uj", name = "k", descriptor = "[Z")
    public static boolean[] modeBlockflag;

    @OriginalMember(owner = "client!uj", name = "v", descriptor = "[F")
    public static float[] C_0;

    @OriginalMember(owner = "client!uj", name = "P", descriptor = "[Lclient!bk;")
    public static VorbisFloor[] floors;

    @OriginalMember(owner = "client!uj", name = "o", descriptor = "[Lclient!tr;")
    public static VorbisMapping[] mappings;

    @OriginalMember(owner = "client!uj", name = "J", descriptor = "[I")
    public static int[] BITREVERSE_0;

    @OriginalMember(owner = "client!uj", name = "Q", descriptor = "I")
    public static int pos;

    @OriginalMember(owner = "client!uj", name = "z", descriptor = "[Lclient!oca;")
    public static VorbisCodebook[] codebooks;

    @OriginalMember(owner = "client!uj", name = "x", descriptor = "[F")
    public static float[] B_1;

    @OriginalMember(owner = "client!uj", name = "y", descriptor = "[F")
    public static float[] B_0;

    @OriginalMember(owner = "client!uj", name = "A", descriptor = "Z")
    public static boolean headerDecoded = false;

    @OriginalMember(owner = "client!uj", name = "a", descriptor = "(Lclient!sb;I)Lclient!uj;")
    public static VorbisSound create(@OriginalArg(0) js5 vorbis, @OriginalArg(1) int id) {
        if (decodeHeader(vorbis)) {
            @Pc(14) byte[] data = vorbis.getfile(id);
            return data == null ? null : new VorbisSound(data);
        } else {
            vorbis.fileready(id);
            return null;
        }
    }

    @OriginalMember(owner = "client!uj", name = "a", descriptor = "(Lclient!sb;II)Lclient!uj;")
    public static VorbisSound create(@OriginalArg(0) js5 vorbis, @OriginalArg(1) int groupId, @OriginalArg(2) int fileId) {
        if (decodeHeader(vorbis)) {
            @Pc(16) byte[] data = vorbis.getfile(fileId, groupId);
            return data == null ? null : new VorbisSound(data);
        } else {
            vorbis.requestdownload(fileId, groupId);
            return null;
        }
    }

    @OriginalMember(owner = "client!uj", name = "c", descriptor = "(I)I")
    public static int gbit(@OriginalArg(0) int n) {
        @Pc(1) int value = 0;
        @Pc(3) int read = 0;
        @Pc(8) int bitOff;

        while (n >= 8 - bitPos) {
            bitOff = 8 - bitPos;
            @Pc(14) int mask = (0x1 << bitOff) - 1;
            value += (data[pos] >> bitPos & mask) << read;
            bitPos = 0;
            pos++;
            read += bitOff;
            n -= bitOff;
        }

        if (n > 0) {
            bitOff = (0x1 << n) - 1;
            value += (data[pos] >> bitPos & bitOff) << read;
            bitPos += n;
        }

        return value;
    }

    @OriginalMember(owner = "client!uj", name = "b", descriptor = "([B)V")
    public static void decodeHeader(@OriginalArg(0) byte[] arg0) {
        setData(arg0);
        blocksize_0 = 0x1 << gbit(4);
        blocksize_1 = 0x1 << gbit(4);
        currentWindow = new float[blocksize_1];

        for (@Pc(17) int i = 0; i < 2; i++) {
            @Pc(24) int n = i == 0 ? blocksize_0 : blocksize_1;
            @Pc(28) int n_2 = n >> 1;
            @Pc(32) int n_4 = n >> 2;
            @Pc(36) int n_8 = n >> 3;

            @Pc(39) float[] A = new float[n_2];
            for (@Pc(41) int k = 0; k < n_4; k++) {
                A[k * 2] = (float) Math.cos((double) (k * 4) * 3.141592653589793D / (double) n);
                A[k * 2 + 1] = -((float) Math.sin((double) (k * 4) * 3.141592653589793D / (double) n));
            }

            @Pc(84) float[] B = new float[n_2];
            for (@Pc(86) int k = 0; k < n_4; k++) {
                B[k * 2] = (float) Math.cos((double) (k * 2 + 1) * 3.141592653589793D / (double) (n * 2));
                B[k * 2 + 1] = (float) Math.sin((double) (k * 2 + 1) * 3.141592653589793D / (double) (n * 2));
            }

            @Pc(136) float[] C = new float[n_4];
            for (@Pc(138) int k = 0; k < n_8; k++) {
                C[k * 2] = (float) Math.cos((double) (k * 4 + 2) * 3.141592653589793D / (double) n);
                C[k * 2 + 1] = -((float) Math.sin((double) (k * 4 + 2) * 3.141592653589793D / (double) n));
            }

            @Pc(185) int[] bitreverse = new int[n_8];
            @Pc(191) int bits = IntMath.countBits(n_8 - 1);
            for (@Pc(193) int j = 0; j < n_8; j++) {
                bitreverse[j] = reverseBits(bits, j);
            }

            if (i == 0) {
                A_0 = A;
                B_0 = B;
                C_0 = C;
                BITREVERSE_0 = bitreverse;
            } else {
                A_1 = A;
                B_1 = B;
                C_1 = C;
                BITREVERSE_1 = bitreverse;
            }
        }

        @Pc(24) int codebookCount = gbit(8) + 1;
        codebooks = new VorbisCodebook[codebookCount];
        for (@Pc(28) int local28 = 0; local28 < codebookCount; local28++) {
            codebooks[local28] = new VorbisCodebook();
        }

        @Pc(32) int timeCount = gbit(6) + 1;
        for (@Pc(36) int i = 0; i < timeCount; i++) {
            gbit(16);
        }

        @Pc(269) int floorCount = gbit(6) + 1;
        floors = new VorbisFloor[floorCount];
        for (@Pc(41) int i = 0; i < floorCount; i++) {
            floors[i] = new VorbisFloor();
        }

        @Pc(290) int residueCount = gbit(6) + 1;
        residues = new VorbisResidue[residueCount];
        for (@Pc(86) int i = 0; i < residueCount; i++) {
            residues[i] = new VorbisResidue();
        }

        @Pc(311) int mappingCount = gbit(6) + 1;
        mappings = new VorbisMapping[mappingCount];
        for (@Pc(138) int i = 0; i < mappingCount; i++) {
            mappings[i] = new VorbisMapping();
        }

        @Pc(332) int modeCount = gbit(6) + 1;
        modeBlockflag = new boolean[modeCount];
        modeMapping = new int[modeCount];
        for (@Pc(191) int i = 0; i < modeCount; i++) {
            modeBlockflag[i] = g1() != 0;
            gbit(16);
            gbit(16);
            modeMapping[i] = gbit(8);
        }

        headerDecoded = true;
    }

    @OriginalMember(owner = "client!uj", name = "a", descriptor = "(Lclient!sb;)Z")
    public static boolean decodeHeader(@OriginalArg(0) js5 arg0) {
        if (headerDecoded) {
            return true;
        }

        @Pc(7) byte[] data = arg0.getfile(0, 0);
        if (data == null) {
            return false;
        }

        decodeHeader(data);
        return true;
    }

    /**
     * @see <a href="https://xiph.org/vorbis/doc/Vorbis_I_spec.html#x1-1200009.2.2">float32_unpack</a>
     */
    @OriginalMember(owner = "client!uj", name = "a", descriptor = "(I)F")
    public static float float32Unpack(@OriginalArg(0) int v) {
        @Pc(3) int mantissa = v & 0x1FFFFF;
        @Pc(7) int sign = v & Integer.MIN_VALUE;
        @Pc(13) int exponent = (v >> 21) & 0x3FF;
        if (sign != 0) {
            mantissa = -mantissa;
        }
        return (float) ((double) mantissa * Math.pow(2.0D, exponent - 788));
    }

    @OriginalMember(owner = "client!uj", name = "a", descriptor = "([BI)V")
    public static void setData(@OriginalArg(0) byte[] data) {
        VorbisSound.data = data;
        pos = 0;
        bitPos = 0;
    }

    @OriginalMember(owner = "client!uj", name = "b", descriptor = "()I")
    public static int g1() {
        @Pc(7) int value = data[pos] >> bitPos & 0x1;
        bitPos++;
        pos += bitPos >> 3;
        bitPos &= 0x7;
        return value;
    }

    @OriginalMember(owner = "client!fk", name = "a", descriptor = "(III)I")
    public static int reverseBits(@OriginalArg(0) int bits, @OriginalArg(2) int n) {
        @Pc(14) int reversed = 0;
        while (bits > 0) {
            reversed = n & 0x1 | reversed << 1;
            bits--;
            n >>>= 0x1;
        }
        return reversed;
    }

    @OriginalMember(owner = "client!uj", name = "E", descriptor = "Z")
    public boolean previousNoDecodeFloor;

    @OriginalMember(owner = "client!uj", name = "F", descriptor = "I")
    public int previousWindowEnd;

    @OriginalMember(owner = "client!uj", name = "l", descriptor = "Z")
    public boolean negativeBitRate;

    @OriginalMember(owner = "client!uj", name = "r", descriptor = "I")
    public int minBitRate;

    @OriginalMember(owner = "client!uj", name = "R", descriptor = "I")
    public int sampleRate;

    @OriginalMember(owner = "client!uj", name = "s", descriptor = "I")
    public int maxBitRate;

    @OriginalMember(owner = "client!uj", name = "O", descriptor = "I")
    public int nominalBitRate;

    @OriginalMember(owner = "client!uj", name = "u", descriptor = "[[B")
    public byte[][] packets;

    @OriginalMember(owner = "client!uj", name = "D", descriptor = "[F")
    public float[] previousWindow;

    @OriginalMember(owner = "client!uj", name = "H", descriptor = "I")
    public int previousWindowSize;

    @OriginalMember(owner = "client!uj", name = "w", descriptor = "I")
    public int currentOff;

    @OriginalMember(owner = "client!uj", name = "t", descriptor = "[B")
    public byte[] packetData;

    @OriginalMember(owner = "client!uj", name = "m", descriptor = "I")
    public int currentPacket;

    @OriginalMember(owner = "client!uj", name = "<init>", descriptor = "([B)V")
    public VorbisSound(@OriginalArg(0) byte[] arg0) {
        this.decode(arg0);
    }

    @OriginalMember(owner = "client!uj", name = "a", descriptor = "([B)V")
    public void decode(@OriginalArg(0) byte[] data) {
        @Pc(4) Packet local4 = new Packet(data);
        this.sampleRate = local4.g4();
        this.maxBitRate = local4.g4();
        this.nominalBitRate = local4.g4();
        this.minBitRate = local4.g4();

        if (this.minBitRate < 0) {
            this.minBitRate = ~this.minBitRate;
            this.negativeBitRate = true;
        }

        @Pc(40) int count = local4.g4();
        this.packets = new byte[count][];
        for (@Pc(46) int i = 0; i < count; i++) {
            @Pc(49) int totalSize = 0;
            @Pc(53) int size;

            do {
                size = local4.g1();
                totalSize += size;
            } while (size >= 255);

            @Pc(64) byte[] packetData = new byte[totalSize];
            local4.gdata(0, totalSize, packetData);
            this.packets[i] = packetData;
        }
    }

    @OriginalMember(owner = "client!uj", name = "a", descriptor = "([I)Lclient!sq;")
    public VariableRateSoundPacket method8502(@OriginalArg(0) int[] maxSamples) {
        if (maxSamples != null && maxSamples[0] <= 0) {
            return null;
        }

        if (this.packetData == null) {
            this.previousWindowSize = 0;
            this.previousWindow = new float[blocksize_1];
            this.packetData = new byte[this.maxBitRate];
            this.currentOff = 0;
            this.currentPacket = 0;
        }

        while (this.currentPacket < this.packets.length) {
            if (maxSamples != null && maxSamples[0] <= 0) {
                return null;
            }

            @Pc(42) float[] data = this.decode(this.currentPacket);
            if (data != null) {
                @Pc(47) int off = this.currentOff;

                @Pc(50) int length = data.length;
                if (length > this.maxBitRate - off) {
                    length = this.maxBitRate - off;
                }

                for (@Pc(63) int i = 0; i < length; i++) {
                    @Pc(73) int v = (int) (data[i] * 128.0F + 128.0F);
                    if ((v & 0xFFFFFF00) != 0) {
                        v = ~v >> 31;
                    }
                    this.packetData[off++] = (byte) (v - 128);
                }

                if (maxSamples != null) {
                    maxSamples[0] -= off - this.currentOff;
                }

                this.currentOff = off;
            }

            this.currentPacket++;
        }

        this.previousWindow = null;
        @Pc(129) byte[] data = this.packetData;
        this.packetData = null;
        return new VariableRateSoundPacket(this.sampleRate, data, this.nominalBitRate, this.minBitRate, this.negativeBitRate);
    }

    /**
     * @see <a href="https://xiph.org/vorbis/doc/Vorbis_I_spec.html#x1-730004.3.1">packet type, mode and window decode</a>
     */
    @OriginalMember(owner = "client!uj", name = "b", descriptor = "(I)[F")
    public float[] decode(@OriginalArg(0) int packet) {
        setData(this.packets[packet]);
        g1();

        @Pc(15) int modeNumber = gbit(IntMath.countBits(modeMapping.length - 1));
        @Pc(19) boolean blockflag = modeBlockflag[modeNumber];
        @Pc(25) int n = blockflag ? blocksize_1 : blocksize_0;

        @Pc(27) boolean previousWindowFlag = false;
        @Pc(29) boolean nextWindowFlag = false;
        if (blockflag) {
            previousWindowFlag = g1() != 0;
            nextWindowFlag = g1() != 0;
        }

        @Pc(47) int windowCenter = n >> 1;
        @Pc(59) int leftWindowStart;
        @Pc(67) int leftWindowEnd;
        @Pc(71) int leftN;
        if (blockflag && !previousWindowFlag) {
            leftWindowStart = (n >> 2) - (blocksize_0 >> 2);
            leftWindowEnd = (n >> 2) + (blocksize_0 >> 2);
            leftN = blocksize_0 >> 1;
        } else {
            leftWindowStart = 0;
            leftWindowEnd = windowCenter;
            leftN = n >> 1;
        }

        @Pc(94) int rightWindowStart;
        @Pc(104) int rightWindowEnd;
        @Pc(108) int rightN;
        if (blockflag && !nextWindowFlag) {
            rightWindowStart = n - (n >> 2) - (blocksize_0 >> 2);
            rightWindowEnd = n + (blocksize_0 >> 2) - (n >> 2);
            rightN = blocksize_0 >> 1;
        } else {
            rightWindowStart = windowCenter;
            rightWindowEnd = n;
            rightN = n >> 1;
        }

        /**
         * @see <a href="https://xiph.org/vorbis/doc/Vorbis_I_spec.html#x1-740004.3.2">floor curve decode</a>
         */

        @Pc(123) VorbisMapping mapping = mappings[modeMapping[modeNumber]];
        @Pc(126) int submapNumber = mapping.mux;
        @Pc(131) int floorNumber = mapping.submapFloor[submapNumber];
        @Pc(140) boolean noDecodeFloor = !floors[floorNumber].decode();
        boolean noDecodeResidue = noDecodeFloor;

        for (@Pc(144) int i = 0; i < mapping.submaps; i++) {
            @Pc(152) VorbisResidue residue = residues[mapping.submapResidue[i]];
            @Pc(154) float[] v = currentWindow;
            residue.decode(v, n >> 1, noDecodeResidue);
        }

        if (!noDecodeFloor) {
            @Pc(171) int submap = mapping.mux;
            @Pc(176) int floor = mapping.submapFloor[submap];
            floors[floor].computeCurve(currentWindow, n >> 1);
        }

        if (noDecodeFloor) {
            for (@Pc(171) int local171 = n >> 1; local171 < n; local171++) {
                currentWindow[local171] = 0.0F;
            }
        } else {
            // https://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.47.7258
            @Pc(171) int n_2 = n >> 1;
            @Pc(176) int n_4 = n >> 2;
            @Pc(212) int n_8 = n >> 3;

            @Pc(214) float[] u = currentWindow;
            for (@Pc(216) int k = 0; k < n_2; k++) {
                u[k] *= 0.5F;
            }
            for (@Pc(230) int k = n_2; k < n; k++) {
                u[k] = -u[n - k - 1];
            }

            @Pc(252) float[] A = blockflag ? A_1 : A_0;
            @Pc(258) float[] B = blockflag ? B_1 : B_0;
            @Pc(264) float[] C = blockflag ? C_1 : C_0;

            @Pc(270) int[] bitreverse = blockflag ? BITREVERSE_1 : BITREVERSE_0;

            for (@Pc(272) int k = 0; k < n_4; k++) {
                @Pc(289) float u1 = u[k * 4] - u[n - k * 4 - 1];
                @Pc(307) float u2 = u[k * 4 + 2] - u[n - k * 4 - 3];
                @Pc(313) float A2k = A[k * 2];
                @Pc(321) float A2k_1 = A[k * 2 + 1];
                u[n - k * 4 - 1] = u1 * A2k - u2 * A2k_1;
                u[n - k * 4 - 3] = u1 * A2k_1 + u2 * A2k;
            }

            for (@Pc(359) int k = 0; k < n_8; k++) {
                @Pc(307) float vn_2_3_4k = u[n_2 + k * 4 + 3];
                @Pc(313) float vn_2_1_4k = u[n_2 + k * 4 + 1];

                @Pc(321) float vn4k_3 = u[k * 4 + 3];
                @Pc(396) float vn4k_1 = u[k * 4 + 1];
                u[n_2 + k * 4 + 3] = vn_2_3_4k + vn4k_3;
                u[n_2 + k * 4 + 1] = vn_2_1_4k + vn4k_1;

                @Pc(430) float An_2_4_4k = A[n_2 - k * 4 - 4];
                @Pc(440) float An_2_3_4k = A[n_2 - k * 4 - 3];
                u[k * 4 + 3] = (vn_2_3_4k - vn4k_3) * An_2_4_4k - (vn_2_1_4k - vn4k_1) * An_2_3_4k;
                u[k * 4 + 1] = (vn_2_1_4k - vn4k_1) * An_2_4_4k + (vn_2_3_4k - vn4k_3) * An_2_3_4k;
            }

            @Pc(486) int ld_n = IntMath.countBits(n - 1);

            for (@Pc(488) int l = 0; l < ld_n - 3; l++) {
                @Pc(495) int k0 = n >> l + 2;
                @Pc(499) int k1 = 0x8 << l;

                for (@Pc(501) int s = 0; s < 0x2 << l; s++) {
                    @Pc(510) int n_k0_2s = n - k0 * 2 * s;
                    @Pc(520) int n_k0_2s_1 = n - k0 * (s * 2 + 1);
                    for (@Pc(522) int r = 0; r < n >> l + 4; r++) {
                        @Pc(527) int _4r = r * 4;
                        @Pc(535) float wn_k0_2s_1_4r = u[n_k0_2s - _4r - 1];
                        @Pc(543) float wn_k0_2s_3_4r = u[n_k0_2s - _4r - 3];

                        @Pc(551) float wn_k0_2s_1_1_4r = u[n_k0_2s_1 - _4r - 1];
                        @Pc(559) float wn_k0_2s_1_3_4r = u[n_k0_2s_1 - _4r - 3];
                        u[n_k0_2s - _4r - 1] = wn_k0_2s_1_4r + wn_k0_2s_1_1_4r;
                        u[n_k0_2s - _4r - 3] = wn_k0_2s_3_4r + wn_k0_2s_1_3_4r;

                        @Pc(585) float Ark1 = A[r * k1];
                        @Pc(593) float Ark1_1 = A[r * k1 + 1];
                        u[n_k0_2s_1 - _4r - 1] = (wn_k0_2s_1_4r - wn_k0_2s_1_1_4r) * Ark1 - (wn_k0_2s_3_4r - wn_k0_2s_1_3_4r) * Ark1_1;
                        u[n_k0_2s_1 - _4r - 3] = (wn_k0_2s_3_4r - wn_k0_2s_1_3_4r) * Ark1 + (wn_k0_2s_1_4r - wn_k0_2s_1_1_4r) * Ark1_1;
                    }
                }
            }

            for (@Pc(495) int i = 1; i < n_8 - 1; i++) {
                @Pc(499) int j = bitreverse[i];

                if (i < j) {
                    @Pc(501) int _8i = i * 8;
                    @Pc(510) int _8j = j * 8;

                    @Pc(673) float u8_1 = u[_8i + 1];
                    u[_8i + 1] = u[_8j + 1];
                    u[_8j + 1] = u8_1;

                    @Pc(695) float u8_3 = u[_8i + 3];
                    u[_8i + 3] = u[_8j + 3];
                    u[_8j + 3] = u8_3;

                    @Pc(717) float u8_5 = u[_8i + 5];
                    u[_8i + 5] = u[_8j + 5];
                    u[_8j + 5] = u8_5;

                    @Pc(739) float u8_7 = u[_8i + 7];
                    u[_8i + 7] = u[_8j + 7];
                    u[_8j + 7] = u8_7;
                }
            }

            for (@Pc(499) int k = 0; k < n_2; k++) {
                u[k] = u[k * 2 + 1];
            }

            for (@Pc(501) int k = 0; k < n_8; k++) {
                u[n - k * 2 - 1] = u[k * 4];
                u[n - k * 2 - 2] = u[k * 4 + 1];
                u[n - n_4 - k * 2 - 1] = u[k * 4 + 2];
                u[n - n_4 - k * 2 - 2] = u[k * 4 + 3];
            }

            for (@Pc(510) int k = 0; k < n_8; k++) {
                @Pc(673) float C2k = C[k * 2];
                @Pc(868) float C2k_1 = C[k * 2 + 1];
                @Pc(876) float un_2_2k = u[n_2 + k * 2];
                @Pc(535) float un_2_2k_1 = u[n_2 + k * 2 + 1];
                @Pc(543) float u_n_2k_2 = u[n - k * 2 - 2];
                @Pc(551) float u_n_2k_1 = u[n - k * 2 - 1];

                @Pc(559) float v = C2k_1 * (un_2_2k - u_n_2k_2) + C2k * (un_2_2k_1 + u_n_2k_1);
                u[n_2 + k * 2] = (un_2_2k + u_n_2k_2 + v) * 0.5F;
                u[n - k * 2 - 2] = (un_2_2k + u_n_2k_2 - v) * 0.5F;
                v = C2k_1 * (un_2_2k_1 + u_n_2k_1) - C2k * (un_2_2k - u_n_2k_2);
                u[n_2 + k * 2 + 1] = (v + (un_2_2k_1 - u_n_2k_1)) * 0.5F;
                u[n - k * 2 - 1] = (v + (u_n_2k_1 - un_2_2k_1)) * 0.5F;
            }

            for (@Pc(520) int k = 0; k < n_4; k++) {
                u[k] = u[k * 2 + n_2] * B[k * 2] + u[k * 2 + n_2 + 1] * B[k * 2 + 1];
                u[n_2 - k - 1] = u[k * 2 + n_2] * B[k * 2 + 1] - u[k * 2 + n_2 + 1] * B[k * 2];
            }

            for (@Pc(522) int k = 0; k < n_4; k++) {
                u[n + k - n_4] = -u[k];
            }

            for (@Pc(527) int k = 0; k < n_4; k++) {
                u[k] = u[n_4 + k];
            }

            for (@Pc(1111) int k = 0; k < n_4; k++) {
                u[n_4 + k] = -u[n_4 - k - 1];
            }

            for (@Pc(1131) int k = 0; k < n_4; k++) {
                u[n_2 + k] = u[n - k - 1];
            }

            for (@Pc(1150) int i = leftWindowStart; i < leftWindowEnd; i++) {
                @Pc(559) float v = (float) Math.sin((((double) (i - leftWindowStart) + 0.5D) / (double) leftN) * 0.5D * 3.141592653589793D);
                currentWindow[i] *= (float) Math.sin((double) v * 1.5707963267948966D * (double) v);
            }

            for (@Pc(1188) int i = rightWindowStart; i < rightWindowEnd; i++) {
                @Pc(585) float v = (float) Math.sin(((((double) (i - rightWindowStart) + 0.5D) / (double) rightN) * 0.5D * 3.141592653589793D) + 1.5707963267948966D);
                currentWindow[i] *= (float) Math.sin((double) v * 1.5707963267948966D * (double) v);
            }
        }

        @Pc(1228) float[] v = null;
        if (this.previousWindowSize > 0) {
            @Pc(176) int totalSize = this.previousWindowSize + n >> 2;
            v = new float[totalSize];

            if (!this.previousNoDecodeFloor) {
                for (@Pc(212) int i = 0; i < this.previousWindowEnd; i++) {
                    @Pc(1254) int offset = (this.previousWindowSize >> 1) + i;
                    v[i] += this.previousWindow[offset];
                }
            }

            if (!noDecodeFloor) {
                for (@Pc(212) int i = leftWindowStart; i < (n >> 1); i++) {
                    @Pc(1254) int offset = v.length + i - (n >> 1);
                    v[offset] += currentWindow[i];
                }
            }
        }

        @Pc(154) float[] temp = this.previousWindow;
        this.previousWindow = currentWindow;
        currentWindow = temp;
        this.previousWindowSize = n;
        this.previousWindowEnd = rightWindowEnd - (n >> 1);
        this.previousNoDecodeFloor = noDecodeFloor;
        return v;
    }
}
