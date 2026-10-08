import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.game.Animator;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class ShadowList {

    @OriginalMember(owner = "client!ld", name = "p", descriptor = "Lclient!dla;")
    public static final ReferenceCache recentUse = new ReferenceCache(32);

    @OriginalMember(owner = "client!wt", name = "i", descriptor = "I")
    public static int featureMask;

    @OriginalMember(owner = "client!tja", name = "a", descriptor = "(ILclient!ka;IIIIZLclient!ha;ILclient!gu;III)Lclient!ka;")
    public static Model model(@OriginalArg(0) int innerAlpha, @OriginalArg(1) Model model, @OriginalArg(2) int arg2, @OriginalArg(3) int innerColour, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(7) Toolkit arg6, @OriginalArg(8) int arg7, @OriginalArg(9) Animator animator, @OriginalArg(10) int arg9, @OriginalArg(11) int arg10, @OriginalArg(12) int outerColour) {
        if (model == null) {
            return null;
        }
        @Pc(12) int local12 = 2055;
        if (animator != null) {
            local12 = animator.functionMask() | 0x807;
            local12 &= 0xFFFFFDFF;
        }
        @Pc(58) long local58 = ((long) innerColour << 48) + ((long) outerColour << 32) + (long) (arg5 + (arg7 << 16) + (innerAlpha << 24));
        @Pc(60) ReferenceCache local60 = recentUse;
        @Pc(68) Model local68;
        synchronized (recentUse) {
            local68 = (Model) recentUse.get(local58);
        }
        if (local68 == null || arg6.compareFunctionMasks(local68.ua(), local12) != 0) {
            if (local68 != null) {
                local12 = arg6.combineFunctionMasks(local12, local68.ua());
            }
            @Pc(106) byte local106;
            if (arg5 == 1) {
                local106 = 9;
            } else if (arg5 == 2) {
                local106 = 12;
            } else if (arg5 == 3) {
                local106 = 15;
            } else if (arg5 == 4) {
                local106 = 18;
            } else {
                local106 = 21;
            }
            @Pc(143) int[] local143 = new int[]{64, 96, 128};
            @Pc(162) Mesh local162 = new Mesh(local106 * 3 + 1, -local106 + local106 * 6, 0);
            @Pc(169) int local169 = local162.addVertex(0, 0, 0);
            @Pc(173) int[][] local173 = new int[3][local106];
            @Pc(181) int local181;
            @Pc(185) int local185;
            @Pc(187) int local187;
            @Pc(211) int local211;
            for (@Pc(175) int local175 = 0; local175 < 3; local175++) {
                local181 = local143[local175];
                local185 = local143[local175];
                for (local187 = 0; local187 < local106; local187++) {
                    @Pc(195) int local195 = (local187 << 14) / local106;
                    @Pc(203) int local203 = Trig1.SIN[local195] * local181 >> 14;
                    local211 = local185 * Trig1.COS[local195] >> 14;
                    local173[local175][local187] = local162.addVertex(local211, 0, local203);
                }
            }
            for (local181 = 0; local181 < 3; local181++) {
                local185 = (local181 * 256 + 128) / 3;
                local187 = 256 - local185;
                @Pc(265) byte local265 = (byte) (innerAlpha * local185 + arg7 * local187 >> 8);
                @Pc(310) short local310 = (short) ((local185 * (innerColour & 0x7F) + local187 * (outerColour & 0x7F) & 0x7F00) + ((outerColour & 0x380) * local187 + (innerColour & 0x380) * local185 & 0x38000) + ((outerColour & 0xFC00) * local187 + (innerColour & 0xFC00) * local185 & 0xFC0000) >> 8);
                for (local211 = 0; local211 < local106; local211++) {
                    if (local181 == 0) {
                        local162.addFace(local169, local173[0][local211], local173[0][(local211 + 1) % local106], local310, (short) -1, local265, (byte) 1, (byte) -1);
                    } else {
                        local162.addFace(local173[local181 - 1][local211], local173[local181][(local211 + 1) % local106], local173[local181 - 1][(local211 + 1) % local106], local310, (short) -1, local265, (byte) 1, (byte) -1);
                        local162.addFace(local173[local181 - 1][local211], local173[local181][local211], local173[local181][(local211 + 1) % local106], local310, (short) -1, local265, (byte) 1, (byte) -1);
                    }
                }
            }
            local68 = arg6.createModel(local162, local12, featureMask, 64, 768);
            @Pc(440) ReferenceCache local440 = recentUse;
            synchronized (recentUse) {
                recentUse.put(local68, local58);
            }
        }
        @Pc(456) int local456 = model.V();
        @Pc(459) int local459 = model.RA();
        @Pc(462) int local462 = model.HA();
        @Pc(465) int local465 = model.G();
        if (animator == null) {
            local68 = local68.copy((byte) 3, local12, true);
            local68.O(local459 - local456 >> 1, 128, local465 - local462 >> 1);
            local68.H(local459 + local456 >> 1, 0, local465 + local462 >> 1);
        } else {
            local68 = local68.copy((byte) 3, local12, true);
            local68.O(local459 - local456 >> 1, 128, local465 - local462 >> 1);
            local68.H(local459 + local456 >> 1, 0, local462 + local465 >> 1);
            animator.animateShadow(local68);
        }
        if (arg9 != 0) {
            local68.FA(arg9);
        }
        if (arg2 != 0) {
            local68.VA(arg2);
        }
        if (arg4 != 0) {
            local68.H(0, arg4, 0);
        }
        return local68;
    }

    @OriginalMember(owner = "client!du", name = "b", descriptor = "(I)V")
    public static void cacheRemoveSoftReferences() {
        @Pc(1) ReferenceCache local1 = recentUse;
        synchronized (recentUse) {
            recentUse.removeSoftReferences();
        }
    }

    @OriginalMember(owner = "client!g", name = "a", descriptor = "(BI)V")
    public static void cacheClean(@OriginalArg(1) int maxAge) {
        @Pc(5) ReferenceCache local5 = recentUse;
        synchronized (recentUse) {
            recentUse.clean(maxAge);
        }
    }

    @OriginalMember(owner = "client!ji", name = "a", descriptor = "(II)V")
    public static void setFeatureMask(@OriginalArg(1) int featureMask) {
        ShadowList.featureMask = featureMask;
        @Pc(14) ReferenceCache local14 = recentUse;
        synchronized (recentUse) {
            recentUse.reset();
        }
    }

    @OriginalMember(owner = "client!lk", name = "a", descriptor = "(I)V")
    public static void cacheReset() {
        @Pc(1) ReferenceCache local1 = recentUse;
        synchronized (recentUse) {
            recentUse.reset();
        }
    }

    private ShadowList() {
        /* empty */
    }
}
