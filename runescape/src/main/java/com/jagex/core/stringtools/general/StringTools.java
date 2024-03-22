package com.jagex.core.stringtools.general;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class StringTools {

    @OriginalMember(owner = "client!oo", name = "a", descriptor = "(ILjava/lang/String;)Z")
    public static boolean isDecimal(@OriginalArg(1) String string) {
        return isNumeric(true, string, 10);
    }

    @OriginalMember(owner = "client!wda", name = "a", descriptor = "(ZILjava/lang/String;I)Z")
    public static boolean isNumeric(@OriginalArg(0) boolean ignorePlus, @OriginalArg(2) String string, @OriginalArg(3) int radix) {
        if (radix > 36) {
            throw new IllegalArgumentException("Invalid radix:" + radix);
        }

        @Pc(27) boolean negative = false;
        @Pc(29) boolean number = false;
        @Pc(31) int value = 0;
        @Pc(34) int length = string.length();

        for (@Pc(36) int index = 0; index < length; index++) {
            @Pc(41) char c = string.charAt(index);

            if (index == 0) {
                if (c == '-') {
                    negative = true;
                    continue;
                }

                if (c == '+' && ignorePlus) {
                    continue;
                }
            }

            @Pc(73) int i;
            if (c >= '0' && c <= '9') {
                i = c - '0';
            } else if (c >= 'A' && c <= 'Z') {
                i = (c + 10) - 'A';
            } else if (c >= 'a' && c <= 'z') {
                i = (c + 10) - 'a';
            } else {
                return false;
            }

            if (i >= radix) {
                return false;
            }

            if (negative) {
                i = -i;
            }

            @Pc(124) int t = i + value * radix;
            if (value != t / radix) {
                return false;
            }

            value = t;
            number = true;
        }

        return number;
    }

    @OriginalMember(owner = "client!sm", name = "a", descriptor = "(BZI)Ljava/lang/String;")
    public static String decimalWithSign(@OriginalArg(1) boolean makePositive, @OriginalArg(2) int number) {
        return makePositive && number >= 0 ? numberWithSign(number, 10, makePositive) : Integer.toString(number);
    }

    @OriginalMember(owner = "client!dd", name = "a", descriptor = "(ZIIZ)Ljava/lang/String;")
    public static String numberWithSign(@OriginalArg(1) int number, @OriginalArg(2) int radix, @OriginalArg(3) boolean makePositive) {
        if (radix > 36) {
            throw new IllegalArgumentException("Invalid radix:" + radix);
        }

        if (makePositive && number >= 0) {
            @Pc(51) int digits = 2;
            for (@Pc(55) int n = number / radix; n != 0; n /= radix) {
                digits++;
            }

            @Pc(66) char[] chars = new char[digits];
            chars[0] = '+';

            for (@Pc(74) int i = digits - 1; i > 0; i--) {
                @Pc(77) int n = number;
                number /= radix;

                @Pc(88) int delta = n - number * radix;
                if (delta >= 10) {
                    chars[i] = (char) ((delta - 10) + 'a');
                } else {
                    chars[i] = (char) (delta + '0');
                }
            }

            return new String(chars);
        } else {
            return Integer.toString(number, radix);
        }
    }

    @OriginalMember(owner = "client!uh", name = "a", descriptor = "(Ljava/lang/String;I)I")
    public static int parseDecimal(@OriginalArg(0) String string) {
        return parseInt(string, 10, true);
    }

    @OriginalMember(owner = "client!ah", name = "a", descriptor = "(Ljava/lang/String;IZ)I")
    public static int parseHexadecimal(@OriginalArg(0) String string) {
        return parseInt(string, 16, true);
    }

    @OriginalMember(owner = "client!iha", name = "a", descriptor = "(ILjava/lang/String;IZ)I")
    public static int parseInt(@OriginalArg(1) String string, @OriginalArg(2) int radix, boolean allowPlus) {
        if (radix > 36) {
            throw new IllegalArgumentException("Invalid radix:" + radix);
        }

        @Pc(29) boolean negative = false;
        @Pc(31) boolean valid = false;
        @Pc(39) int value = 0;
        @Pc(42) int length = string.length();

        for (@Pc(44) int index = 0; index < length; index++) {
            @Pc(49) char c = string.charAt(index);

            if (index == 0) {
                if (c == '-') {
                    negative = true;
                    continue;
                }
                if (c == '+' && allowPlus) {
                    continue;
                }
            }

            @Pc(104) int i;
            if (c >= '0' && c <= '9') {
                i = c - '0';
            } else if (c >= 'A' && c <= 'Z') {
                i = (c + 10) - 'A';
            } else if (c >= 'a' && c <= 'z') {
                i = (c + 10) - 'a';
            } else {
                throw new NumberFormatException();
            }

            if (i >= radix) {
                throw new NumberFormatException();
            }

            if (negative) {
                i = -i;
            }

            @Pc(136) int v = i + (radix * value);
            if (value != v / radix) {
                throw new NumberFormatException();
            }
            valid = true;
            value = v;
        }

        if (valid) {
            return value;
        } else {
            throw new NumberFormatException();
        }
    }

    @OriginalMember(owner = "client!hda", name = "a", descriptor = "(Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;")
    public static String replace(@OriginalArg(0) String string, @OriginalArg(2) String target, @OriginalArg(3) String replacement) {
        for (@Pc(13) int i = string.indexOf(target); i != -1; i = string.indexOf(target, i + replacement.length())) {
            string = string.substring(0, i) + replacement + string.substring(target.length() + i);
        }
        return string;
    }

    @OriginalMember(owner = "client!sa", name = "a", descriptor = "(IZJIZ)Ljava/lang/String;")
    public static String formatNumber(@OriginalArg(0) int language, @OriginalArg(1) boolean delimit, @OriginalArg(2) long value, @OriginalArg(3) int decimals) {
        @Pc(5) char decimalDelimiter = ',';
        @Pc(19) char thousandDelimiter = '.';
        if (language == 0) {
            decimalDelimiter = '.';
            thousandDelimiter = ',';
        }
        if (language == 2) {
            thousandDelimiter = ' ';
        }

        @Pc(32) boolean negative = false;
        if (value < 0L) {
            value = -value;
            negative = true;
        }

        @Pc(48) StringBuffer buffer = new StringBuffer(26);
        if (decimals > 0) {
            for (@Pc(55) int i = 0; i < decimals; i++) {
                @Pc(59) int v = (int) value;
                value /= 10L;

                buffer.append((char) ((v + '0') - ((int) value * 10)));
            }

            buffer.append(decimalDelimiter);
        }

        @Pc(55) int length = 0;
        while (true) {
            @Pc(59) int v = (int) value;
            value /= 10L;

            buffer.append((char) ((v + '0') - ((int) value * 10)));

            if (value == 0L) {
                if (negative) {
                    buffer.append('-');
                }

                return buffer.reverse().toString();
            }

            if (delimit) {
                length++;

                if (length % 3 == 0) {
                    buffer.append(thousandDelimiter);
                }
            }
        }
    }

    @OriginalMember(owner = "client!gf", name = "a", descriptor = "(ILjava/lang/String;)J")
    public static long longHash(@OriginalArg(1) String text) {
        @Pc(15) int length = text.length();
        @Pc(17) long hash = 0L;
        for (@Pc(19) int i = 0; i < length; i++) {
            hash = (long) text.charAt(i) + (hash << 5) - hash;
        }
        return hash;
    }

    @OriginalMember(owner = "client!gla", name = "a", descriptor = "(Ljava/lang/String;B)I")
    public static int intHash(@OriginalArg(0) String text) {
        @Pc(8) int length = text.length();
        @Pc(17) int hash = 0;
        for (@Pc(19) int i = 0; i < length; i++) {
            hash = text.charAt(i) + (hash << 5) - hash;
        }
        return hash;
    }

    private StringTools() {
        /* empty */
    }
}
