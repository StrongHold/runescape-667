package com.jagex;

import com.jagex.core.io.Packet;
import com.jagex.core.util.Arrays;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!wp")
public final class IndexedImage {

    @OriginalMember(owner = "client!wp", name = "a", descriptor = "(Lclient!sb;II)[Lclient!wp;")
    public static IndexedImage[] load(@OriginalArg(0) js5 js5, @OriginalArg(1) int file, int group) {
        @Pc(5) byte[] data = js5.getfile(group, file);
        return data == null ? null : load(data);
    }

    @OriginalMember(owner = "client!wp", name = "a", descriptor = "([B)[Lclient!wp;")
    public static IndexedImage[] load(@OriginalArg(0) byte[] data) {
        @Pc(4) Packet packet = new Packet(data);
        packet.pos = data.length - 2;

        @Pc(14) int count = packet.g2();
        @Pc(17) IndexedImage[] images = new IndexedImage[count];
        for (@Pc(19) int i = 0; i < count; i++) {
            images[i] = new IndexedImage();
        }

        packet.pos = data.length - count * 8 - 7;
        @Pc(44) int scaleWidth = packet.g2();
        @Pc(48) int scaleHeight = packet.g2();
        @Pc(56) int paletteSize = (packet.g1() & 0xFF) + 1;

        for (@Pc(58) int i = 0; i < count; i++) {
            images[i].offX1 = packet.g2();
        }

        for (@Pc(72) int i = 0; i < count; i++) {
            images[i].offY1 = packet.g2();
        }

        for (@Pc(86) int i = 0; i < count; i++) {
            images[i].width = packet.g2();
        }

        for (@Pc(100) int i = 0; i < count; i++) {
            images[i].height = packet.g2();
        }

        for (@Pc(114) int i = 0; i < count; i++) {
            @Pc(119) IndexedImage image = images[i];
            image.offX2 = scaleWidth - image.width - image.offX1;
            image.offY2 = scaleHeight - image.height - image.offY1;
        }

        packet.pos = data.length - (count * 8) - ((paletteSize - 1) * 3) - 7;

        @Pc(160) int[] palette = new int[paletteSize];
        for (@Pc(162) int i = 1; i < paletteSize; i++) {
            palette[i] = packet.g3();
            if (palette[i] == 0) {
                palette[i] = 1;
            }
        }

        for (@Pc(183) int i = 0; i < count; i++) {
            images[i].palette = palette;
        }

        packet.pos = 0;

        for (@Pc(198) int i = 0; i < count; i++) {
            @Pc(203) IndexedImage image = images[i];
            @Pc(209) int size = image.width * image.height;
            image.raster = new byte[size];

            @Pc(217) int flags = packet.g1();
            boolean oblong = (flags & 0x1) != 0;
            boolean alpha = (flags & 0x2) != 0;

            if (alpha) {
                @Pc(271) boolean transparent = false;
                image.alpha = new byte[size];

                if (oblong) {
                    for (@Pc(245) int x = 0; x < image.width; x++) {
                        for (@Pc(295) int y = 0; y < image.height; y++) {
                            image.raster[x + (y * image.width)] = packet.g1b();
                        }
                    }

                    for (@Pc(295) int x = 0; x < image.width; x++) {
                        for (@Pc(352) int y = 0; y < image.height; y++) {
                            @Pc(367) byte b = image.alpha[x + (y * image.width)] = packet.g1b();
                            transparent |= b != -1;
                        }
                    }
                } else {
                    for (@Pc(245) int pixel = 0; pixel < size; pixel++) {
                        image.raster[pixel] = packet.g1b();
                    }

                    for (@Pc(295) int pixel = 0; pixel < size; pixel++) {
                        @Pc(305) byte b = image.alpha[pixel] = packet.g1b();
                        transparent |= b != -1;
                    }
                }

                if (!transparent) {
                    image.alpha = null;
                }
            } else {
                if (oblong) {
                    for (@Pc(227) int x = 0; x < image.width; x++) {
                        for (@Pc(245) int y = 0; y < image.height; y++) {
                            image.raster[x + (y * image.width)] = packet.g1b();
                        }
                    }
                } else {
                    for (@Pc(227) int pixel = 0; pixel < size; pixel++) {
                        image.raster[pixel] = packet.g1b();
                    }
                }
            }
        }

        return images;
    }

    @OriginalMember(owner = "client!wp", name = "b", descriptor = "(Lclient!sb;I)Lclient!wp;")
    public static IndexedImage loadFirst(@OriginalArg(0) js5 js5, @OriginalArg(1) int file) {
        @Pc(4) byte[] data = js5.getfile(file);
        return data == null ? null : load(data)[0];
    }

    @OriginalMember(owner = "client!wp", name = "b", descriptor = "(Lclient!sb;II)Lclient!wp;")
    public static IndexedImage loadFirst(@OriginalArg(0) js5 js5, @OriginalArg(1) int group, @OriginalArg(2) int file) {
        @Pc(5) byte[] data = js5.getfile(file, group);
        return data == null ? null : load(data)[0];
    }

    @OriginalMember(owner = "client!wp", name = "a", descriptor = "(Lclient!sb;I)[Lclient!wp;")
    public static IndexedImage[] load(@OriginalArg(0) js5 js5, @OriginalArg(1) int file) {
        @Pc(4) byte[] data = js5.getfile(file);
        return data == null ? null : load(data);
    }

    @OriginalMember(owner = "client!wp", name = "e", descriptor = "I")
    public int width;

    @OriginalMember(owner = "client!wp", name = "i", descriptor = "I")
    public int offY1;

    @OriginalMember(owner = "client!wp", name = "c", descriptor = "I")
    public int offY2;

    @OriginalMember(owner = "client!wp", name = "d", descriptor = "I")
    public int height;

    @OriginalMember(owner = "client!wp", name = "g", descriptor = "I")
    public int offX2;

    @OriginalMember(owner = "client!wp", name = "b", descriptor = "I")
    public int offX1;

    @OriginalMember(owner = "client!wp", name = "h", descriptor = "[B")
    public byte[] raster;

    @OriginalMember(owner = "client!wp", name = "f", descriptor = "[I")
    public int[] palette;

    @OriginalMember(owner = "client!wp", name = "a", descriptor = "[B")
    public byte[] alpha;

    @OriginalMember(owner = "client!wp", name = "a", descriptor = "()V")
    public void flipHorizontally() {
        @Pc(2) byte[] local2 = this.raster;

        if (this.alpha != null) {
            @Pc(59) byte[] local59 = this.alpha;

            for (@Pc(18) int y = (this.height >> 1) - 1; y >= 0; y--) {
                @Pc(28) int local28 = y * this.width;
                @Pc(32) int local32 = (this.height - y - 1) * this.width;

                for (@Pc(86) int x = -this.width; x < 0; x++) {
                    @Pc(37) byte local37 = local2[local28];
                    local2[local28] = local2[local32];
                    local2[local32] = local37;
                    local37 = local59[local28];
                    local59[local28] = local59[local32];
                    local59[local32] = local37;
                    local28++;
                    local32++;
                }
            }
        } else {
            for (@Pc(12) int y = (this.height >> 1) - 1; y >= 0; y--) {
                @Pc(18) int local18 = y * this.width;
                @Pc(28) int local28 = (this.height - y - 1) * this.width;

                for (@Pc(32) int x = -this.width; x < 0; x++) {
                    @Pc(37) byte local37 = local2[local18];
                    local2[local18] = local2[local28];
                    local2[local28] = local37;
                    local18++;
                    local28++;
                }
            }
        }

        @Pc(12) int temp = this.offY1;
        this.offY1 = this.offY2;
        this.offY2 = temp;
    }

    @OriginalMember(owner = "client!wp", name = "f", descriptor = "()I")
    public int offsetX() {
        return this.width + this.offX1 + this.offX2;
    }

    @OriginalMember(owner = "client!wp", name = "a", descriptor = "(I)V")
    public void scale(@OriginalArg(0) int amount) {
        @Pc(2) int offsetX = this.offsetX();
        @Pc(5) int offsetY = this.offsetY();
        if (this.width == offsetX && this.height == offsetY) {
            return;
        }

        @Pc(16) int local16 = amount;
        if (amount > this.offX1) {
            local16 = this.offX1;
        }

        @Pc(25) int local25 = amount;
        if (amount + this.offX1 + this.width > offsetX) {
            local25 = offsetX - this.offX1 - this.width;
        }

        @Pc(44) int local44 = amount;
        if (amount > this.offY1) {
            local44 = this.offY1;
        }

        @Pc(53) int local53 = amount;
        if (amount + this.offY1 + this.height > offsetY) {
            local53 = offsetY - this.offY1 - this.height;
        }

        @Pc(77) int scaledWidth = this.width + local16 + local25;
        @Pc(84) int scaledHeight = this.height + local44 + local53;
        @Pc(89) byte[] scaledRaster = new byte[scaledWidth * scaledHeight];

        if (this.alpha != null) {
            @Pc(136) byte[] scaledAlpha = new byte[scaledWidth * scaledHeight];

            for (@Pc(100) int y = 0; y < this.height; y++) {
                @Pc(108) int local108 = y * this.width;
                @Pc(110) int local110 = (y + local44) * scaledWidth + local16;
                for (@Pc(154) int x = 0; x < this.width; x++) {
                    scaledAlpha[local110] = this.alpha[local108];
                    scaledRaster[local110++] = this.raster[local108++];
                }
            }

            this.alpha = scaledAlpha;
        } else {
            for (@Pc(94) int y = 0; y < this.height; y++) {
                @Pc(100) int local100 = y * this.width;
                @Pc(108) int local108 = (y + local44) * scaledWidth + local16;
                for (@Pc(110) int x = 0; x < this.width; x++) {
                    scaledRaster[local108++] = this.raster[local100++];
                }
            }
        }

        this.offX1 -= local16;
        this.offY1 -= local44;
        this.offX2 -= local25;
        this.offY2 -= local53;
        this.width = scaledWidth;
        this.height = scaledHeight;
        this.raster = scaledRaster;
    }

    @OriginalMember(owner = "client!wp", name = "c", descriptor = "(I)V")
    public void setShadowColour(@OriginalArg(0) int arg0) {
        @Pc(1) int local1 = -1;
        @Pc(8) int local8;
        @Pc(69) int local69;
        @Pc(63) int local63;
        if (this.palette.length < 255) {
            for (local8 = 0; local8 < this.palette.length; local8++) {
                if (this.palette[local8] == arg0) {
                    local1 = local8;
                    break;
                }
            }
            if (local1 == -1) {
                local1 = this.palette.length;
                @Pc(38) int[] local38 = new int[this.palette.length + 1];
                Arrays.copy(this.palette, 0, local38, 0, this.palette.length);
                this.palette = local38;
                local38[local1] = arg0;
            }
        } else {
            local8 = Integer.MAX_VALUE;
            local63 = arg0 >> 16 & 0xFF;
            local69 = arg0 >> 8 & 0xFF;
            @Pc(73) int local73 = arg0 & 0xFF;
            for (@Pc(75) int local75 = 0; local75 < this.palette.length; local75++) {
                @Pc(81) int local81 = this.palette[local75];
                @Pc(87) int local87 = local81 >> 16 & 0xFF;
                @Pc(93) int local93 = local81 >> 8 & 0xFF;
                @Pc(97) int local97 = local81 & 0xFF;
                @Pc(101) int local101 = local63 - local87;
                if (local101 < 0) {
                    local101 = -local101;
                }
                @Pc(110) int local110 = local69 - local93;
                if (local110 < 0) {
                    local110 = -local110;
                }
                @Pc(119) int local119 = local73 - local97;
                if (local119 < 0) {
                    local119 = -local119;
                }
                @Pc(130) int local130 = local101 + local110 + local119;
                if (local130 < local8) {
                    local8 = local130;
                    local1 = local75;
                }
            }
        }
        for (local8 = this.height - 1; local8 > 0; local8--) {
            local63 = local8 * this.width;
            for (local69 = this.width - 1; local69 > 0; local69--) {
                if (this.palette[this.raster[local69 + local63] & 0xFF] == 0 && this.palette[this.raster[local69 + local63 - this.width - 1] & 0xFF] != 0) {
                    this.raster[local69 + local63] = (byte) local1;
                }
            }
        }
    }

    @OriginalMember(owner = "client!wp", name = "b", descriptor = "()[I")
    public int[] toArgb() {
        @Pc(2) int local2 = this.offsetX();
        @Pc(8) int[] local8 = new int[local2 * this.offsetY()];
        @Pc(13) int local13;
        @Pc(19) int local19;
        @Pc(29) int local29;
        @Pc(31) int local31;
        if (this.alpha == null) {
            for (local13 = 0; local13 < this.height; local13++) {
                local19 = local13 * this.width;
                local29 = this.offX1 + (local13 + this.offY1) * local2;
                for (local31 = 0; local31 < this.width; local31++) {
                    @Pc(96) int local96 = this.palette[this.raster[local19++] & 0xFF];
                    if (local96 == 0) {
                        local8[local29++] = 0;
                    } else {
                        local8[local29++] = local96 | 0xFF000000;
                    }
                }
            }
        } else {
            for (local13 = 0; local13 < this.height; local13++) {
                local19 = local13 * this.width;
                local29 = this.offX1 + (local13 + this.offY1) * local2;
                for (local31 = 0; local31 < this.width; local31++) {
                    local8[local29++] = this.alpha[local19] << 24 | this.palette[this.raster[local19] & 0xFF];
                    local19++;
                }
            }
        }
        return local8;
    }

    @OriginalMember(owner = "client!wp", name = "c", descriptor = "()I")
    public int offsetY() {
        return this.height + this.offY1 + this.offY2;
    }

    @OriginalMember(owner = "client!wp", name = "b", descriptor = "(I)V")
    public void setOutlineColour(@OriginalArg(0) int arg0) {
        @Pc(1) int local1 = -1;
        @Pc(8) int local8;
        @Pc(69) int local69;
        @Pc(73) int local73;
        @Pc(75) int local75;
        if (this.palette.length < 255) {
            for (local8 = 0; local8 < this.palette.length; local8++) {
                if (this.palette[local8] == arg0) {
                    local1 = local8;
                    break;
                }
            }
            if (local1 == -1) {
                local1 = this.palette.length;
                @Pc(38) int[] local38 = new int[this.palette.length + 1];
                Arrays.copy(this.palette, 0, local38, 0, this.palette.length);
                this.palette = local38;
                local38[local1] = arg0;
            }
        } else {
            local8 = Integer.MAX_VALUE;
            @Pc(63) int local63 = arg0 >> 16 & 0xFF;
            local69 = arg0 >> 8 & 0xFF;
            local73 = arg0 & 0xFF;
            for (local75 = 0; local75 < this.palette.length; local75++) {
                @Pc(81) int local81 = this.palette[local75];
                @Pc(87) int local87 = local81 >> 16 & 0xFF;
                @Pc(93) int local93 = local81 >> 8 & 0xFF;
                @Pc(97) int local97 = local81 & 0xFF;
                @Pc(101) int local101 = local63 - local87;
                if (local101 < 0) {
                    local101 = -local101;
                }
                @Pc(110) int local110 = local69 - local93;
                if (local110 < 0) {
                    local110 = -local110;
                }
                @Pc(119) int local119 = local73 - local97;
                if (local119 < 0) {
                    local119 = -local119;
                }
                @Pc(130) int local130 = local101 + local110 + local119;
                if (local130 < local8) {
                    local8 = local130;
                    local1 = local75;
                }
            }
        }
        local8 = 0;
        @Pc(152) byte[] local152 = new byte[this.width * this.height];
        for (local69 = 0; local69 < this.height; local69++) {
            for (local73 = 0; local73 < this.width; local73++) {
                local75 = this.raster[local8] & 0xFF;
                if (this.palette[local75] == 0) {
                    if (local73 > 0 && this.palette[this.raster[local8 - 1] & 0xFF] != 0) {
                        local75 = local1;
                    } else if (local69 > 0 && this.palette[this.raster[local8 - this.width] & 0xFF] != 0) {
                        local75 = local1;
                    } else if (local73 < this.width - 1 && this.palette[this.raster[local8 + 1] & 0xFF] != 0) {
                        local75 = local1;
                    } else if (local69 < this.height - 1 && this.palette[this.raster[local8 + this.width] & 0xFF] != 0) {
                        local75 = local1;
                    }
                }
                local152[local8++] = (byte) local75;
            }
        }
        this.raster = local152;
    }

    @OriginalMember(owner = "client!wp", name = "g", descriptor = "()V")
    public void rotate() {
        @Pc(6) byte[] raster = new byte[this.width * this.height];
        @Pc(8) int pixel = 0;

        if (this.alpha != null) {
            @Pc(52) byte[] alpha = new byte[this.width * this.height];
            for (@Pc(19) int x = 0; x < this.width; x++) {
                for (@Pc(60) int y = this.height - 1; y >= 0; y--) {
                    raster[pixel] = this.raster[x + (y * this.width)];
                    alpha[pixel++] = this.alpha[x + (y * this.width)];
                }
            }

            this.raster = raster;
            this.alpha = alpha;
        } else {
            for (@Pc(13) int x = 0; x < this.width; x++) {
                for (@Pc(19) int y = this.height - 1; y >= 0; y--) {
                    raster[pixel++] = this.raster[x + (y * this.width)];
                }
            }

            this.raster = raster;
        }

        @Pc(13) int temp = this.offY1;
        this.offY1 = this.offX1;
        this.offX1 = this.offY2;
        this.offY2 = this.offX2;
        this.offX2 = this.offY1;
        temp = this.height;
        this.height = this.width;
        this.width = temp;
    }

    @OriginalMember(owner = "client!wp", name = "d", descriptor = "()V")
    public void flipVertically() {
        @Pc(2) byte[] local2 = this.raster;
        @Pc(29) byte local29;
        @Pc(10) int local10;
        @Pc(16) int local16;
        @Pc(23) int local23;
        if (this.alpha == null) {
            for (local10 = this.height - 1; local10 >= 0; local10--) {
                local16 = local10 * this.width;
                local23 = (local10 + 1) * this.width;
                while (local16 < local23) {
                    local23--;
                    local29 = local2[local16];
                    local2[local16] = local2[local23];
                    local2[local23] = local29;
                    local16++;
                }
            }
        } else {
            @Pc(50) byte[] local50 = this.alpha;
            for (local16 = this.height - 1; local16 >= 0; local16--) {
                local23 = local16 * this.width;
                @Pc(68) int local68 = (local16 + 1) * this.width;
                while (local23 < local68) {
                    local68--;
                    local29 = local2[local23];
                    local2[local23] = local2[local68];
                    local2[local68] = local29;
                    local29 = local50[local23];
                    local50[local23] = local50[local68];
                    local50[local68] = local29;
                    local23++;
                }
            }
        }
        local10 = this.offX1;
        this.offX1 = this.offX2;
        this.offX2 = local10;
    }

    @OriginalMember(owner = "client!wp", name = "e", descriptor = "()V")
    public void method9389() {
        @Pc(2) int local2 = this.offsetX();
        @Pc(5) int local5 = this.offsetY();
        if (this.width == local2 && this.height == local5) {
            return;
        }
        @Pc(19) byte[] local19 = new byte[local2 * local5];
        @Pc(29) int local29;
        @Pc(35) int local35;
        @Pc(45) int local45;
        if (this.alpha == null) {
            for (@Pc(80) int local80 = 0; local80 < this.height; local80++) {
                local29 = local80 * this.width;
                local35 = (local80 + this.offY1) * local2 + this.offX1;
                for (local45 = 0; local45 < this.width; local45++) {
                    local19[local35++] = this.raster[local29++];
                }
            }
        } else {
            @Pc(27) byte[] local27 = new byte[local2 * local5];
            for (local29 = 0; local29 < this.height; local29++) {
                local35 = local29 * this.width;
                local45 = (local29 + this.offY1) * local2 + this.offX1;
                for (@Pc(47) int local47 = 0; local47 < this.width; local47++) {
                    local19[local45] = this.raster[local35];
                    local27[local45++] = this.alpha[local35++];
                }
            }
            this.alpha = local27;
        }
        this.offX1 = this.offX2 = this.offY1 = this.offY2 = 0;
        this.width = local2;
        this.height = local5;
        this.raster = local19;
    }
}
