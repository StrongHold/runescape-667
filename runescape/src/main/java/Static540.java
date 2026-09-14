import com.jagex.core.stringtools.general.StringTools;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static540 {

    @OriginalMember(owner = "client!r", name = "a", descriptor = "(Ljava/lang/String;BILjava/lang/String;)I")
    public static int compare(@OriginalArg(0) String b, @OriginalArg(2) int language, @OriginalArg(3) String a) {
        @Pc(6) int lengthA = a.length();
        @Pc(9) int lengthB = b.length();
        @Pc(11) int indexA = 0;
        @Pc(13) int indexB = 0;
        @Pc(22) char pendingA = 0;
        @Pc(24) char pendingB = 0;
        while (indexA - pendingA < lengthA || lengthB > indexB - pendingB) {
            if (indexA - pendingA >= lengthA) {
                return -1;
            }
            if (lengthB <= indexB - pendingB) {
                return 1;
            }
            @Pc(62) char charA;
            if (pendingA == '\u0000') {
                charA = a.charAt(indexA++);
            } else {
                charA = pendingA;
            }
            @Pc(77) char charB;
            if (pendingB == '\u0000') {
                charB = b.charAt(indexB++);
            } else {
                charB = pendingB;
            }
            pendingA = StringTools.transliteral(charA);
            pendingB = StringTools.transliteral(charB);
            charA = Static322.stripAccent(language, charA);
            charB = Static322.stripAccent(language, charB);
            if (charA != charB && Character.toUpperCase(charA) != Character.toUpperCase(charB)) {
                charA = Character.toLowerCase(charA);
                charB = Character.toLowerCase(charB);
                if (charB != charA) {
                    return StringTools.intHash(language, charA) - StringTools.intHash(language, charB);
                }
            }
        }
        @Pc(149) int shortestLength = Math.min(lengthA, lengthB);
        for (@Pc(151) int i = 0; i < shortestLength; i++) {
            if (language == 2) {
                indexA = lengthA - i - 1;
                indexB = lengthB - i - 1;
            } else {
                indexB = i;
                indexA = i;
            }
            @Pc(180) char charA = a.charAt(indexA);
            @Pc(184) char charB = b.charAt(indexB);
            if (charA != charB && Character.toUpperCase(charA) != Character.toUpperCase(charB)) {
                charA = Character.toLowerCase(charA);
                charB = Character.toLowerCase(charB);
                if (charA != charB) {
                    return StringTools.intHash(language, charA) - StringTools.intHash(language, charB);
                }
            }
        }
        @Pc(239) int lengthDifference = lengthA - lengthB;
        if (lengthDifference != 0) {
            return lengthDifference;
        }
        for (@Pc(246) int i = 0; i < shortestLength; i++) {
            @Pc(251) char charA = a.charAt(i);
            @Pc(255) char charB = b.charAt(i);
            if (charA != charB) {
                return StringTools.intHash(language, charA) - StringTools.intHash(language, charB);
            }
        }
        return 0;
    }

}
