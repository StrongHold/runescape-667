import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static322 {

    @OriginalMember(owner = "client!kd", name = "m", descriptor = "S")
    public static short aShort135;

    @OriginalMember(owner = "client!kd", name = "a", descriptor = "(IIC)C")
    public static char stripAccent(@OriginalArg(1) int language, @OriginalArg(2) char c) {
        if (c >= 'À' && c <= 'ÿ') {
            if (c >= 'À' && c <= 'Æ') {
                return 'A';
            }
            if (c == 'Ç') {
                return 'C';
            }
            if (c >= 'È' && c <= 'Ë') {
                return 'E';
            }
            if (c >= 'Ì' && c <= 'Ï') {
                return 'I';
            }
            if (c >= 'Ò' && c <= 'Ö') {
                return 'O';
            }
            if (c >= 'Ù' && c <= 'Ü') {
                return 'U';
            }
            if (c == 'Ý') {
                return 'Y';
            }
            if (c == 'ß') {
                return 's';
            }
            if (c >= 'à' && c <= 'æ') {
                return 'a';
            }
            if (c == 'ç') {
                return 'c';
            }
            if (c >= 'è' && c <= 'ë') {
                return 'e';
            }
            if (c >= 'ì' && c <= 'ï') {
                return 'i';
            }
            if (c >= 'ò' && c <= 'ö') {
                return 'o';
            }
            if (c >= 'ù' && c <= 'ü') {
                return 'u';
            }
            if (c == 'ý' || c == 'ÿ') {
                return 'y';
            }
        }

        if (c == 'Œ') {
            return 'O';
        } else if (c == 'œ') {
            return 'o';
        } else if (c == 'Ÿ') {
            return 'Y';
        } else {
            return c;
        }
    }

}
