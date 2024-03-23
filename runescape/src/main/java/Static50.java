import com.jagex.core.util.TimeUtils;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.graphics.Mesh;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static50 {

    @OriginalMember(owner = "client!bm", name = "b", descriptor = "Lclient!ss;")
    public static final ClientProt A_CLIENT_PROT___90 = new ClientProt(85, 2);

    @OriginalMember(owner = "client!bm", name = "c", descriptor = "Z")
    public static boolean aBoolean565 = true;

    @OriginalMember(owner = "client!bm", name = "e", descriptor = "Z")
    public static boolean aBoolean566 = false;

    @OriginalMember(owner = "client!bm", name = "a", descriptor = "(ILclient!dv;I[I)Lclient!uea;")
    public static Class369 method6635(@OriginalArg(1) Mesh arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int[] arg2) {
        @Pc(7) int[] local7 = null;
        @Pc(9) int[] local9 = null;
        @Pc(11) int[] local11 = null;
        @Pc(13) float[][] local13 = null;
        if (arg0.faceAlpha != null) {
            @Pc(25) int local25 = arg0.texSpaceCount;
            @Pc(28) int[] local28 = new int[local25];
            @Pc(31) int[] local31 = new int[local25];
            @Pc(34) int[] local34 = new int[local25];
            @Pc(37) int[] local37 = new int[local25];
            @Pc(40) int[] local40 = new int[local25];
            @Pc(43) int[] local43 = new int[local25];
            for (@Pc(45) int local45 = 0; local45 < local25; local45++) {
                local28[local45] = Integer.MAX_VALUE;
                local31[local45] = -2147483647;
                local34[local45] = Integer.MAX_VALUE;
                local37[local45] = -2147483647;
                local40[local45] = Integer.MAX_VALUE;
                local43[local45] = -2147483647;
            }
            local11 = new int[local25];
            local13 = new float[local25][];
            @Pc(97) int local97;
            @Pc(158) int local158;
            for (@Pc(89) int local89 = 0; local89 < arg1; local89++) {
                local97 = arg2[local89];
                if (arg0.faceAlpha[local97] != -1) {
                    @Pc(111) int local111 = arg0.faceAlpha[local97] & 0xFF;
                    for (@Pc(113) int local113 = 0; local113 < 3; local113++) {
                        @Pc(134) short local134;
                        if (local113 == 0) {
                            local134 = arg0.faceA[local97];
                        } else if (local113 == 1) {
                            local134 = arg0.faceB[local97];
                        } else {
                            local134 = arg0.faceC[local97];
                        }
                        @Pc(153) int local153 = arg0.vertexX[local134];
                        local158 = arg0.vertexY[local134];
                        @Pc(163) int local163 = arg0.vertexZ[local134];
                        if (local153 < local28[local111]) {
                            local28[local111] = local153;
                        }
                        if (local153 > local31[local111]) {
                            local31[local111] = local153;
                        }
                        if (local34[local111] > local158) {
                            local34[local111] = local158;
                        }
                        if (local158 > local37[local111]) {
                            local37[local111] = local158;
                        }
                        if (local40[local111] > local163) {
                            local40[local111] = local163;
                        }
                        if (local163 > local43[local111]) {
                            local43[local111] = local163;
                        }
                    }
                }
            }
            local7 = new int[local25];
            local9 = new int[local25];
            for (local97 = 0; local97 < local25; local97++) {
                @Pc(268) byte local268 = arg0.texMappingType[local97];
                if (local268 > 0) {
                    local7[local97] = (local28[local97] + local31[local97]) / 2;
                    local9[local97] = (local34[local97] + local37[local97]) / 2;
                    local11[local97] = (local43[local97] + local40[local97]) / 2;
                    @Pc(340) float local340;
                    @Pc(326) float local326;
                    @Pc(334) float local334;
                    if (local268 == 1) {
                        local158 = arg0.texSpaceScaleX[local97];
                        local326 = 64.0F / (float) arg0.texSpaceScaleY[local97];
                        if (local158 == 0) {
                            local340 = 1.0F;
                            local334 = 1.0F;
                        } else if (local158 <= 0) {
                            local334 = 1.0F;
                            local340 = (float) -local158 / 1024.0F;
                        } else {
                            local340 = 1.0F;
                            local334 = (float) local158 / 1024.0F;
                        }
                    } else if (local268 == 2) {
                        local326 = 64.0F / (float) arg0.texSpaceScaleY[local97];
                        local334 = 64.0F / (float) arg0.texSpaceScaleZ[local97];
                        local340 = 64.0F / (float) arg0.texSpaceScaleX[local97];
                    } else {
                        local340 = (float) arg0.texSpaceScaleX[local97] / 1024.0F;
                        local326 = (float) arg0.texSpaceScaleY[local97] / 1024.0F;
                        local334 = (float) arg0.texSpaceScaleZ[local97] / 1024.0F;
                    }
                    local13[local97] = Static395.method9163(local340, local326, local334, arg0.texSpaceDefA[local97], arg0.texSpaceDefC[local97], arg0.aByteArray27[local97] & 0xFF, arg0.texSpaceDefB[local97]);
                }
            }
        }
        return new Class369(local7, local9, local11, local13);
    }

    @OriginalMember(owner = "client!bm", name = "a", descriptor = "(Lclient!cg;B)V")
    public static void method6638(@OriginalArg(0) Class8_Sub2_Sub1_Sub2 arg0) {
        @Pc(8) Animator local8 = arg0.animator;
        if (local8.isAnimating() && local8.tick(1) && local8.isFinished()) {
            if (arg0.ready) {
                local8.update(true, arg0.method9317().ready());
                arg0.ready = local8.isAnimating();
            }
            local8.resetImmediately();
        }
        @Pc(75) Animator local75;
        for (@Pc(50) int local50 = 0; local50 < arg0.aClass199Array3.length; local50++) {
            if (arg0.aClass199Array3[local50].anInt4930 != -1) {
                local75 = arg0.aClass199Array3[local50].aAnimator_7;
                if (local75.isDelayed()) {
                    @Pc(88) SpotAnimationType local88 = SpotAnimationTypeList.instance.list(arg0.aClass199Array3[local50].anInt4930);
                    @Pc(92) SeqType local92 = local75.getAnimation();
                    if (local88.loopSeq) {
                        if (local92.animatingPrecedence == 3) {
                            if (arg0.anInt10762 > 0 && TimeUtils.clock >= arg0.anInt10759 && arg0.anInt10755 < TimeUtils.clock) {
                                local75.update(true, -1);
                                arg0.aClass199Array3[local50].anInt4930 = -1;
                                continue;
                            }
                        } else if (local92.animatingPrecedence == 1 && arg0.anInt10762 > 0 && arg0.anInt10759 <= TimeUtils.clock && TimeUtils.clock > arg0.anInt10755) {
                            continue;
                        }
                    }
                }
                if (local75.tick(1) && local75.isFinished()) {
                    local75.update(true, -1);
                    arg0.aClass199Array3[local50].anInt4930 = -1;
                }
            }
        }
        local75 = arg0.aAnimator_11;
        if (local75.isAnimating()) {
            label83:
            {
                @Pc(214) SeqType local214 = local75.getAnimation();
                if (local214.animatingPrecedence == 3) {
                    if (arg0.anInt10762 > 0 && arg0.anInt10759 <= TimeUtils.clock && TimeUtils.clock > arg0.anInt10755) {
                        arg0.anIntArray869 = null;
                        local75.update(true, -1);
                        break label83;
                    }
                } else if (local214.animatingPrecedence == 1) {
                    if (arg0.anInt10762 > 0 && TimeUtils.clock >= arg0.anInt10759 && arg0.anInt10755 < TimeUtils.clock) {
                        local75.method9091(1);
                        break label83;
                    }
                    local75.method9091(0);
                }
                if (local75.tick(1) && local75.isFinished()) {
                    arg0.anIntArray869 = null;
                    local75.update(true, -1);
                }
            }
        }
        for (@Pc(313) int local313 = 0; local313 < arg0.aClass152_Sub2_Sub1Array3.length; local313++) {
            @Pc(320) Animator_Sub2_Sub1 local320 = arg0.aClass152_Sub2_Sub1Array3[local313];
            if (local320 != null) {
                if (local320.anInt10508 > 0) {
                    local320.anInt10508--;
                } else if (local320.tick(1) && local320.isFinished()) {
                    arg0.aClass152_Sub2_Sub1Array3[local313] = null;
                }
            }
        }
    }

}
