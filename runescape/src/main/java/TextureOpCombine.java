import com.jagex.Client;
import com.jagex.core.io.Packet;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.texture.TextureOp;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Combines two source layers with the selected blend mode: add, subtract, multiply, divide,
 * screen, overlay, dodge, burn, darken, lighten, difference or exclusion.
 */
@OriginalClass("client!md")
public final class TextureOpCombine extends TextureOp {

    @OriginalMember(owner = "client!md", name = "L", descriptor = "I")
    public int mode = 6;

    @OriginalMember(owner = "client!md", name = "<init>", descriptor = "()V")
    public TextureOpCombine() {
        super(2, false);
    }

    @OriginalMember(owner = "client!md", name = "a", descriptor = "(ZLclient!ge;I)V")
    @Override
    public void method9416(@OriginalArg(0) boolean arg0, @OriginalArg(1) Packet arg1, @OriginalArg(2) int arg2) {
        if (arg2 == 0) {
            this.mode = arg1.g1();
        } else if (arg2 == 1) {
            super.monochrome = arg1.g1() == 1;
        }
    }

    @OriginalMember(owner = "client!md", name = "a", descriptor = "(II)[I")
    @Override
    public int[] monochromeOutput(@OriginalArg(0) int arg0, @OriginalArg(1) int y) {
        @Pc(11) int[] output = super.monochromeCache.get(y);
        if (arg0 <= 107) {
            Client.additionalInfo = null;
        }
        if (super.monochromeCache.dirty) {
            @Pc(28) int[] first = this.method9422(y, 0);
            @Pc(34) int[] second = this.method9422(y, 1);
            @Pc(37) int local37 = this.mode;
            if (local37 == 1) {
                for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                    output[local37] = first[local37] + second[local37];
                }
            } else if (local37 == 2) {
                for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                    output[local37] = first[local37] - second[local37];
                }
            } else if (local37 == 3) {
                for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                    output[local37] = second[local37] * first[local37] >> 12;
                }
            } else {
                @Pc(202) int secondValue;
                if (local37 == 4) {
                    for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                        secondValue = second[local37];
                        output[local37] = secondValue == 0 ? 4096 : (first[local37] << 12) / secondValue;
                    }
                } else if (local37 == 5) {
                    for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                        output[local37] = 4096 - ((4096 - second[local37]) * (-first[local37] + 4096) >> 12);
                    }
                } else if (local37 == 6) {
                    for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                        secondValue = second[local37];
                        output[local37] = secondValue < 2048 ? secondValue * first[local37] >> 11 : 4096 - ((4096 - secondValue) * (-first[local37] + 4096) >> 11);
                    }
                } else {
                    @Pc(330) int firstValue;
                    if (local37 == 7) {
                        for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                            firstValue = first[local37];
                            output[local37] = firstValue == 4096 ? 4096 : (second[local37] << 12) / (4096 - firstValue);
                        }
                    } else if (local37 == 8) {
                        for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                            firstValue = first[local37];
                            output[local37] = firstValue == 0 ? 0 : 4096 - (4096 - second[local37] << 12) / firstValue;
                        }
                    } else if (local37 == 9) {
                        for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                            firstValue = first[local37];
                            secondValue = second[local37];
                            output[local37] = secondValue > firstValue ? firstValue : secondValue;
                        }
                    } else if (local37 == 10) {
                        for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                            secondValue = second[local37];
                            firstValue = first[local37];
                            output[local37] = secondValue < firstValue ? firstValue : secondValue;
                        }
                    } else if (local37 == 11) {
                        for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                            firstValue = first[local37];
                            secondValue = second[local37];
                            output[local37] = secondValue < firstValue ? firstValue - secondValue : -firstValue + secondValue;
                        }
                    } else if (local37 == 12) {
                        for (local37 = 0; local37 < EnvironmentLight.anInt9289; local37++) {
                            secondValue = second[local37];
                            firstValue = first[local37];
                            output[local37] = firstValue + secondValue - (firstValue * secondValue >> 11);
                        }
                    }
                }
            }
        }
        return output;
    }

    @OriginalMember(owner = "client!md", name = "a", descriptor = "(IZ)[[I")
    @Override
    public int[][] method9414(@OriginalArg(0) int y) {
        @Pc(11) int[][] output = super.colourCache.get(y);
        if (super.colourCache.dirty) {
            @Pc(30) int[][] first = this.method9413(0, y);
            @Pc(36) int[][] second = this.method9413(1, y);
            @Pc(40) int[] outputRed = output[0];
            @Pc(44) int[] outputGreen = output[1];
            @Pc(48) int[] outputBlue = output[2];
            @Pc(52) int[] firstRed = first[0];
            @Pc(56) int[] firstGreen = first[1];
            @Pc(60) int[] firstBlue = first[2];
            @Pc(64) int[] secondRed = second[0];
            @Pc(68) int[] secondGreen = second[1];
            @Pc(72) int[] secondBlue = second[2];
            @Pc(75) int local75 = this.mode;
            if (local75 == 1) {
                for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                    outputRed[local75] = secondRed[local75] + firstRed[local75];
                    outputGreen[local75] = firstGreen[local75] + secondGreen[local75];
                    outputBlue[local75] = secondBlue[local75] + firstBlue[local75];
                }
            } else if (local75 == 2) {
                for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                    outputRed[local75] = firstRed[local75] - secondRed[local75];
                    outputGreen[local75] = firstGreen[local75] - secondGreen[local75];
                    outputBlue[local75] = firstBlue[local75] - secondBlue[local75];
                }
            } else if (local75 == 3) {
                for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                    outputRed[local75] = firstRed[local75] * secondRed[local75] >> 12;
                    outputGreen[local75] = secondGreen[local75] * firstGreen[local75] >> 12;
                    outputBlue[local75] = secondBlue[local75] * firstBlue[local75] >> 12;
                }
            } else {
                @Pc(317) int secondRedValue;
                @Pc(309) int secondGreenValue;
                @Pc(313) int secondBlueValue;
                if (local75 == 4) {
                    for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                        secondGreenValue = secondGreen[local75];
                        secondBlueValue = secondBlue[local75];
                        secondRedValue = secondRed[local75];
                        outputRed[local75] = secondRedValue == 0 ? 4096 : (firstRed[local75] << 12) / secondRedValue;
                        outputGreen[local75] = secondGreenValue == 0 ? 4096 : (firstGreen[local75] << 12) / secondGreenValue;
                        outputBlue[local75] = secondBlueValue == 0 ? 4096 : (firstBlue[local75] << 12) / secondBlueValue;
                    }
                } else if (local75 == 5) {
                    for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                        outputRed[local75] = 4096 - ((4096 - secondRed[local75]) * (-firstRed[local75] + 4096) >> 12);
                        outputGreen[local75] = 4096 - ((4096 - secondGreen[local75]) * (4096 - firstGreen[local75]) >> 12);
                        outputBlue[local75] = 4096 - ((4096 - firstBlue[local75]) * (-secondBlue[local75] + 4096) >> 12);
                    }
                } else if (local75 == 6) {
                    for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                        secondRedValue = secondRed[local75];
                        secondBlueValue = secondBlue[local75];
                        secondGreenValue = secondGreen[local75];
                        outputRed[local75] = secondRedValue >= 2048 ? 4096 - ((4096 - secondRedValue) * (-firstRed[local75] + 4096) >> 11) : secondRedValue * firstRed[local75] >> 11;
                        outputGreen[local75] = secondGreenValue < 2048 ? firstGreen[local75] * secondGreenValue >> 11 : 4096 - ((4096 - secondGreenValue) * (-firstGreen[local75] + 4096) >> 11);
                        outputBlue[local75] = secondBlueValue >= 2048 ? 4096 - ((4096 - secondBlueValue) * (-firstBlue[local75] + 4096) >> 11) : secondBlueValue * firstBlue[local75] >> 11;
                    }
                } else {
                    @Pc(580) int firstRedValue;
                    @Pc(576) int firstGreenValue;
                    @Pc(572) int firstBlueValue;
                    if (local75 == 7) {
                        for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                            firstBlueValue = firstBlue[local75];
                            firstGreenValue = firstGreen[local75];
                            firstRedValue = firstRed[local75];
                            outputRed[local75] = firstRedValue == 4096 ? 4096 : (secondRed[local75] << 12) / (4096 - firstRedValue);
                            outputGreen[local75] = firstGreenValue == 4096 ? 4096 : (secondGreen[local75] << 12) / (4096 - firstGreenValue);
                            outputBlue[local75] = firstBlueValue == 4096 ? 4096 : (secondBlue[local75] << 12) / (4096 - firstBlueValue);
                        }
                    } else if (local75 == 8) {
                        for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                            firstBlueValue = firstBlue[local75];
                            firstRedValue = firstRed[local75];
                            firstGreenValue = firstGreen[local75];
                            outputRed[local75] = firstRedValue == 0 ? 0 : 4096 - (4096 - secondRed[local75] << 12) / firstRedValue;
                            outputGreen[local75] = firstGreenValue == 0 ? 0 : 4096 - (4096 - secondGreen[local75] << 12) / firstGreenValue;
                            outputBlue[local75] = firstBlueValue == 0 ? 0 : 4096 - (4096 - secondBlue[local75] << 12) / firstBlueValue;
                        }
                    } else if (local75 == 9) {
                        for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                            firstGreenValue = firstGreen[local75];
                            secondGreenValue = secondGreen[local75];
                            firstBlueValue = firstBlue[local75];
                            secondRedValue = secondRed[local75];
                            firstRedValue = firstRed[local75];
                            secondBlueValue = secondBlue[local75];
                            outputRed[local75] = secondRedValue > firstRedValue ? firstRedValue : secondRedValue;
                            outputGreen[local75] = secondGreenValue <= firstGreenValue ? secondGreenValue : firstGreenValue;
                            outputBlue[local75] = secondBlueValue <= firstBlueValue ? secondBlueValue : firstBlueValue;
                        }
                    } else if (local75 == 10) {
                        for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                            secondBlueValue = secondBlue[local75];
                            firstRedValue = firstRed[local75];
                            secondGreenValue = secondGreen[local75];
                            secondRedValue = secondRed[local75];
                            firstBlueValue = firstBlue[local75];
                            firstGreenValue = firstGreen[local75];
                            outputRed[local75] = firstRedValue > secondRedValue ? firstRedValue : secondRedValue;
                            outputGreen[local75] = secondGreenValue < firstGreenValue ? firstGreenValue : secondGreenValue;
                            outputBlue[local75] = firstBlueValue <= secondBlueValue ? secondBlueValue : firstBlueValue;
                        }
                    } else if (local75 == 11) {
                        for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                            secondRedValue = secondRed[local75];
                            firstGreenValue = firstGreen[local75];
                            secondGreenValue = secondGreen[local75];
                            firstBlueValue = firstBlue[local75];
                            firstRedValue = firstRed[local75];
                            secondBlueValue = secondBlue[local75];
                            outputRed[local75] = secondRedValue < firstRedValue ? firstRedValue - secondRedValue : secondRedValue - firstRedValue;
                            outputGreen[local75] = secondGreenValue >= firstGreenValue ? secondGreenValue - firstGreenValue : firstGreenValue + -secondGreenValue;
                            outputBlue[local75] = firstBlueValue <= secondBlueValue ? secondBlueValue - firstBlueValue : firstBlueValue - secondBlueValue;
                        }
                    } else if (local75 == 12) {
                        for (local75 = 0; local75 < EnvironmentLight.anInt9289; local75++) {
                            firstGreenValue = firstGreen[local75];
                            secondGreenValue = secondGreen[local75];
                            secondBlueValue = secondBlue[local75];
                            firstRedValue = firstRed[local75];
                            firstBlueValue = firstBlue[local75];
                            secondRedValue = secondRed[local75];
                            outputRed[local75] = secondRedValue + firstRedValue - (firstRedValue * secondRedValue >> 11);
                            outputGreen[local75] = firstGreenValue + secondGreenValue - (secondGreenValue * firstGreenValue >> 11);
                            outputBlue[local75] = secondBlueValue + firstBlueValue - (secondBlueValue * firstBlueValue >> 11);
                        }
                    }
                }
            }
        }
        return output;
    }
}
