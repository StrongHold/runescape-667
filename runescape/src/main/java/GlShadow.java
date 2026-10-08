import com.jagex.graphics.Shadow;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!ph")
public final class GlShadow extends Shadow {

    @OriginalMember(owner = "client!ph", name = "J", descriptor = "I")
    public int anInt7344;

    @OriginalMember(owner = "client!ph", name = "C", descriptor = "I")
    public int anInt7345;

    @OriginalMember(owner = "client!ph", name = "H", descriptor = "I")
    public int anInt7350;

    @OriginalMember(owner = "client!ph", name = "G", descriptor = "I")
    public int anInt7353;

    @OriginalMember(owner = "client!ph", name = "E", descriptor = "[B")
    public final byte[] aByteArray85;

    @OriginalMember(owner = "client!ph", name = "<init>", descriptor = "(Lclient!qha;II)V")
    public GlShadow(@OriginalArg(0) GlToolkit arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2) {
        this.aByteArray85 = new byte[arg1 * arg2];
    }

    @OriginalMember(owner = "client!ph", name = "a", descriptor = "(IIIIIII)V")
    public void fillTriangle(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5) {
        @Pc(7) int local7 = 0;
        if (arg3 != arg1) {
            local7 = (arg2 - arg0 << 16) / (arg3 - arg1);
        }
        @Pc(33) int local33 = 0;
        if (arg4 != arg3) {
            local33 = (arg5 - arg2 << 16) / (arg4 - arg3);
        }
        @Pc(53) int local53 = 0;
        if (arg1 != arg4) {
            local53 = (arg0 - arg5 << 16) / (arg1 - arg4);
        }
        if (arg1 <= arg3 && arg1 <= arg4) {
            if (arg4 > arg3) {
                arg5 = arg0 <<= 0x10;
                if (arg1 < 0) {
                    arg0 -= local7 * arg1;
                    arg5 -= arg1 * local53;
                    arg1 = 0;
                }
                arg2 <<= 0x10;
                if (arg3 < 0) {
                    arg2 -= arg3 * local33;
                    arg3 = 0;
                }
                if (arg1 != arg3 && local7 > local53 || arg1 == arg3 && local53 > local33) {
                    arg4 -= arg3;
                    arg3 -= arg1;
                    arg1 = this.anInt7345 * arg1;
                    while (true) {
                        arg3--;
                        if (arg3 < 0) {
                            while (true) {
                                arg4--;
                                if (arg4 < 0) {
                                    return;
                                }
                                Static483.method6491(arg2 >> 16, this.aByteArray85, arg1, arg5 >> 16);
                                arg1 += this.anInt7345;
                                arg2 += local33;
                                arg5 += local53;
                            }
                        }
                        Static483.method6491(arg0 >> 16, this.aByteArray85, arg1, arg5 >> 16);
                        arg5 += local53;
                        arg1 += this.anInt7345;
                        arg0 += local7;
                    }
                } else {
                    arg4 -= arg3;
                    arg3 -= arg1;
                    arg1 *= this.anInt7345;
                    while (true) {
                        arg3--;
                        if (arg3 < 0) {
                            while (true) {
                                arg4--;
                                if (arg4 < 0) {
                                    return;
                                }
                                Static483.method6491(arg5 >> 16, this.aByteArray85, arg1, arg2 >> 16);
                                arg1 += this.anInt7345;
                                arg5 += local53;
                                arg2 += local33;
                            }
                        }
                        Static483.method6491(arg5 >> 16, this.aByteArray85, arg1, arg0 >> 16);
                        arg0 += local7;
                        arg1 += this.anInt7345;
                        arg5 += local53;
                    }
                }
            } else {
                arg2 = arg0 <<= 0x10;
                arg5 <<= 0x10;
                if (arg1 < 0) {
                    arg0 -= local7 * arg1;
                    arg2 -= local53 * arg1;
                    arg1 = 0;
                }
                if (arg4 < 0) {
                    arg5 -= local33 * arg4;
                    arg4 = 0;
                }
                if ((arg1 == arg4 || local53 >= local7) && (arg4 != arg1 || local33 <= local7)) {
                    arg3 -= arg4;
                    arg4 -= arg1;
                    arg1 *= this.anInt7345;
                    while (true) {
                        arg4--;
                        if (arg4 < 0) {
                            while (true) {
                                arg3--;
                                if (arg3 < 0) {
                                    return;
                                }
                                Static483.method6491(arg5 >> 16, this.aByteArray85, arg1, arg0 >> 16);
                                arg5 += local33;
                                arg0 += local7;
                                arg1 += this.anInt7345;
                            }
                        }
                        Static483.method6491(arg2 >> 16, this.aByteArray85, arg1, arg0 >> 16);
                        arg0 += local7;
                        arg2 += local53;
                        arg1 += this.anInt7345;
                    }
                } else {
                    arg3 -= arg4;
                    arg4 -= arg1;
                    arg1 *= this.anInt7345;
                    while (true) {
                        arg4--;
                        if (arg4 < 0) {
                            while (true) {
                                arg3--;
                                if (arg3 < 0) {
                                    return;
                                }
                                Static483.method6491(arg0 >> 16, this.aByteArray85, arg1, arg5 >> 16);
                                arg1 += this.anInt7345;
                                arg5 += local33;
                                arg0 += local7;
                            }
                        }
                        Static483.method6491(arg0 >> 16, this.aByteArray85, arg1, arg2 >> 16);
                        arg2 += local53;
                        arg0 += local7;
                        arg1 += this.anInt7345;
                    }
                }
            }
        } else if (arg3 > arg4) {
            if (arg1 < arg3) {
                arg2 = arg5 <<= 0x10;
                arg0 <<= 0x10;
                if (arg4 < 0) {
                    arg2 -= arg4 * local33;
                    arg5 -= local53 * arg4;
                    arg4 = 0;
                }
                if (arg1 < 0) {
                    arg0 -= arg1 * local7;
                    arg1 = 0;
                }
                if (local53 > local33) {
                    arg3 -= arg1;
                    arg1 -= arg4;
                    arg4 = this.anInt7345 * arg4;
                    while (true) {
                        arg1--;
                        if (arg1 < 0) {
                            while (true) {
                                arg3--;
                                if (arg3 < 0) {
                                    return;
                                }
                                Static483.method6491(arg0 >> 16, this.aByteArray85, arg4, arg2 >> 16);
                                arg0 += local7;
                                arg2 += local33;
                                arg4 += this.anInt7345;
                            }
                        }
                        Static483.method6491(arg5 >> 16, this.aByteArray85, arg4, arg2 >> 16);
                        arg2 += local33;
                        arg4 += this.anInt7345;
                        arg5 += local53;
                    }
                } else {
                    arg3 -= arg1;
                    arg1 -= arg4;
                    arg4 = this.anInt7345 * arg4;
                    while (true) {
                        arg1--;
                        if (arg1 < 0) {
                            while (true) {
                                arg3--;
                                if (arg3 < 0) {
                                    return;
                                }
                                Static483.method6491(arg2 >> 16, this.aByteArray85, arg4, arg0 >> 16);
                                arg2 += local33;
                                arg0 += local7;
                                arg4 += this.anInt7345;
                            }
                        }
                        Static483.method6491(arg2 >> 16, this.aByteArray85, arg4, arg5 >> 16);
                        arg4 += this.anInt7345;
                        arg5 += local53;
                        arg2 += local33;
                    }
                }
            } else {
                arg0 = arg5 <<= 0x10;
                if (arg4 < 0) {
                    arg5 -= arg4 * local53;
                    arg0 -= arg4 * local33;
                    arg4 = 0;
                }
                arg2 <<= 0x10;
                if (arg3 < 0) {
                    arg2 -= arg3 * local7;
                    arg3 = 0;
                }
                if (local53 > local33) {
                    arg1 -= arg3;
                    arg3 -= arg4;
                    arg4 = this.anInt7345 * arg4;
                    while (true) {
                        arg3--;
                        if (arg3 < 0) {
                            while (true) {
                                arg1--;
                                if (arg1 < 0) {
                                    return;
                                }
                                Static483.method6491(arg5 >> 16, this.aByteArray85, arg4, arg2 >> 16);
                                arg2 += local7;
                                arg4 += this.anInt7345;
                                arg5 += local53;
                            }
                        }
                        Static483.method6491(arg5 >> 16, this.aByteArray85, arg4, arg0 >> 16);
                        arg4 += this.anInt7345;
                        arg0 += local33;
                        arg5 += local53;
                    }
                } else {
                    arg1 -= arg3;
                    arg3 -= arg4;
                    arg4 *= this.anInt7345;
                    while (true) {
                        arg3--;
                        if (arg3 < 0) {
                            while (true) {
                                arg1--;
                                if (arg1 < 0) {
                                    return;
                                }
                                Static483.method6491(arg2 >> 16, this.aByteArray85, arg4, arg5 >> 16);
                                arg2 += local7;
                                arg5 += local53;
                                arg4 += this.anInt7345;
                            }
                        }
                        Static483.method6491(arg0 >> 16, this.aByteArray85, arg4, arg5 >> 16);
                        arg0 += local33;
                        arg5 += local53;
                        arg4 += this.anInt7345;
                    }
                }
            }
        } else if (arg4 < arg1) {
            arg0 = arg2 <<= 0x10;
            if (arg3 < 0) {
                arg2 -= arg3 * local33;
                arg0 -= arg3 * local7;
                arg3 = 0;
            }
            arg5 <<= 0x10;
            if (arg4 < 0) {
                arg5 -= arg4 * local53;
                arg4 = 0;
            }
            if ((arg3 == arg4 || local7 >= local33) && (arg3 != arg4 || local7 <= local53)) {
                arg1 -= arg4;
                arg4 -= arg3;
                arg3 *= this.anInt7345;
                while (true) {
                    arg4--;
                    if (arg4 < 0) {
                        while (true) {
                            arg1--;
                            if (arg1 < 0) {
                                return;
                            }
                            Static483.method6491(arg0 >> 16, this.aByteArray85, arg3, arg5 >> 16);
                            arg3 += this.anInt7345;
                            arg0 += local7;
                            arg5 += local53;
                        }
                    }
                    Static483.method6491(arg0 >> 16, this.aByteArray85, arg3, arg2 >> 16);
                    arg2 += local33;
                    arg3 += this.anInt7345;
                    arg0 += local7;
                }
            } else {
                arg1 -= arg4;
                arg4 -= arg3;
                arg3 *= this.anInt7345;
                while (true) {
                    arg4--;
                    if (arg4 < 0) {
                        while (true) {
                            arg1--;
                            if (arg1 < 0) {
                                return;
                            }
                            Static483.method6491(arg5 >> 16, this.aByteArray85, arg3, arg0 >> 16);
                            arg5 += local53;
                            arg3 += this.anInt7345;
                            arg0 += local7;
                        }
                    }
                    Static483.method6491(arg2 >> 16, this.aByteArray85, arg3, arg0 >> 16);
                    arg0 += local7;
                    arg2 += local33;
                    arg3 += this.anInt7345;
                }
            }
        } else {
            arg5 = arg2 <<= 0x10;
            if (arg3 < 0) {
                arg5 -= arg3 * local7;
                arg2 -= arg3 * local33;
                arg3 = 0;
            }
            arg0 <<= 0x10;
            if (arg1 < 0) {
                arg0 -= arg1 * local53;
                arg1 = 0;
            }
            if (local33 <= local7) {
                arg4 -= arg1;
                arg1 -= arg3;
                arg3 = this.anInt7345 * arg3;
                while (true) {
                    arg1--;
                    if (arg1 < 0) {
                        while (true) {
                            arg4--;
                            if (arg4 < 0) {
                                return;
                            }
                            Static483.method6491(arg0 >> 16, this.aByteArray85, arg3, arg2 >> 16);
                            arg0 += local53;
                            arg2 += local33;
                            arg3 += this.anInt7345;
                        }
                    }
                    Static483.method6491(arg5 >> 16, this.aByteArray85, arg3, arg2 >> 16);
                    arg2 += local33;
                    arg3 += this.anInt7345;
                    arg5 += local7;
                }
            } else {
                arg4 -= arg1;
                arg1 -= arg3;
                arg3 = this.anInt7345 * arg3;
                while (true) {
                    arg1--;
                    if (arg1 < 0) {
                        while (true) {
                            arg4--;
                            if (arg4 < 0) {
                                return;
                            }
                            Static483.method6491(arg2 >> 16, this.aByteArray85, arg3, arg0 >> 16);
                            arg2 += local33;
                            arg0 += local53;
                            arg3 += this.anInt7345;
                        }
                    }
                    Static483.method6491(arg2 >> 16, this.aByteArray85, arg3, arg5 >> 16);
                    arg5 += local7;
                    arg3 += this.anInt7345;
                    arg2 += local33;
                }
            }
        }
    }

    @OriginalMember(owner = "client!ph", name = "a", descriptor = "(IIBII)V")
    public void method6544(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(3) int arg2, @OriginalArg(4) int arg3) {
        this.anInt7345 = arg0 - arg3;
        this.anInt7350 = arg3;
        this.anInt7344 = arg2 - arg1;
        this.anInt7353 = arg1;
    }

    @OriginalMember(owner = "client!ph", name = "a", descriptor = "(III)Z")
    public boolean method6545(@OriginalArg(0) int arg0, @OriginalArg(2) int arg1) {
        return arg0 * arg1 <= this.aByteArray85.length;
    }

    @OriginalMember(owner = "client!ph", name = "g", descriptor = "(I)V")
    public void method6546() {
        @Pc(7) int local7 = -1;
        @Pc(13) int local13 = this.aByteArray85.length - 8;
        while (local13 > local7) {
            local7++;
            this.aByteArray85[local7] = 0;
            local7++;
            this.aByteArray85[local7] = 0;
            local7++;
            this.aByteArray85[local7] = 0;
            local7++;
            this.aByteArray85[local7] = 0;
            local7++;
            this.aByteArray85[local7] = 0;
            local7++;
            this.aByteArray85[local7] = 0;
            local7++;
            this.aByteArray85[local7] = 0;
            local7++;
            this.aByteArray85[local7] = 0;
        }
        while (local7 < this.aByteArray85.length - 1) {
            local7++;
            this.aByteArray85[local7] = 0;
        }
    }
}
