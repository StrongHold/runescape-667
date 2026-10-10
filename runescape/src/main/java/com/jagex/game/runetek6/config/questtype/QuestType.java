package com.jagex.game.runetek6.config.questtype;

import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.StringNode;
import com.jagex.core.io.Packet;
import com.jagex.math.IntMath;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!la")
public final class QuestType {

    @OriginalMember(owner = "client!la", name = "p", descriptor = "[I")
    public int[] anIntArray428;

    @OriginalMember(owner = "client!la", name = "x", descriptor = "[I")
    public int[] anIntArray429;

    @OriginalMember(owner = "client!la", name = "h", descriptor = "[[I")
    public int[][] progressVarbits;

    @OriginalMember(owner = "client!la", name = "j", descriptor = "Lclient!av;")
    public IterableHashTable params;

    @OriginalMember(owner = "client!la", name = "m", descriptor = "[I")
    public int[] anIntArray430;

    @OriginalMember(owner = "client!la", name = "w", descriptor = "[Ljava/lang/String;")
    public String[] aStringArray27;

    @OriginalMember(owner = "client!la", name = "e", descriptor = "[I")
    public int[] anIntArray431;

    @OriginalMember(owner = "client!la", name = "o", descriptor = "[I")
    public int[] anIntArray432;

    @OriginalMember(owner = "client!la", name = "g", descriptor = "[Ljava/lang/String;")
    public String[] aStringArray28;

    @OriginalMember(owner = "client!la", name = "t", descriptor = "Ljava/lang/String;")
    public String sortedName;

    @OriginalMember(owner = "client!la", name = "c", descriptor = "[I")
    public int[] anIntArray434;

    @OriginalMember(owner = "client!la", name = "s", descriptor = "[I")
    public int[] requiredQuests;

    @OriginalMember(owner = "client!la", name = "r", descriptor = "[[I")
    public int[][] progressVarps;

    @OriginalMember(owner = "client!la", name = "b", descriptor = "[I")
    public int[] anIntArray436;

    @OriginalMember(owner = "client!la", name = "q", descriptor = "[[I")
    public int[][] requiredStats;

    @OriginalMember(owner = "client!la", name = "n", descriptor = "Ljava/lang/String;")
    public String name;

    @OriginalMember(owner = "client!la", name = "f", descriptor = "I")
    public int icon = -1;

    @OriginalMember(owner = "client!la", name = "a", descriptor = "(I)V")
    public void postDecode() {
        if (this.sortedName == null) {
            this.sortedName = this.name;
        }
    }

    @OriginalMember(owner = "client!la", name = "a", descriptor = "(ILclient!ge;B)V")
    public void decode(@OriginalArg(0) int code, @OriginalArg(1) Packet packet) {
        if (code == 1) {
            this.name = packet.gjstr2();
        } else if (code == 2) {
            this.sortedName = packet.gjstr2();
        } else if (code == 3) {
            @Pc(29) int local29 = packet.g1();
            this.progressVarps = new int[local29][3];
            for (@Pc(36) int local36 = 0; local36 < local29; local36++) {
                this.progressVarps[local36][0] = packet.g2();
                this.progressVarps[local36][1] = packet.g4();
                this.progressVarps[local36][2] = packet.g4();
            }
        } else if (code == 4) {
            @Pc(29) int local29 = packet.g1();
            this.progressVarbits = new int[local29][3];
            for (@Pc(36) int local36 = 0; local36 < local29; local36++) {
                this.progressVarbits[local36][0] = packet.g2();
                this.progressVarbits[local36][1] = packet.g4();
                this.progressVarbits[local36][2] = packet.g4();
            }
        } else if (code == 5) {
            packet.g2();
        } else if (code == 6) {
            packet.g1();
        } else if (code == 7) {
            packet.g1();
        } else if (code == 8) {
            /* empty */
        } else if (code == 9) {
            packet.g1();
        } else if (code == 10) {
            @Pc(29) int local29 = packet.g1();
            this.anIntArray436 = new int[local29];
            for (@Pc(36) int local36 = 0; local36 < local29; local36++) {
                this.anIntArray436[local36] = packet.g4();
            }
        } else if (code == 12) {
            packet.g4();
        } else if (code == 13) {
            @Pc(29) int local29 = packet.g1();
            this.requiredQuests = new int[local29];
            for (@Pc(36) int local36 = 0; local36 < local29; local36++) {
                this.requiredQuests[local36] = packet.g2();
            }
        } else if (code == 14) {
            @Pc(29) int local29 = packet.g1();
            this.requiredStats = new int[local29][2];
            for (@Pc(36) int local36 = 0; local36 < local29; local36++) {
                this.requiredStats[local36][0] = packet.g1();
                this.requiredStats[local36][1] = packet.g1();
            }
        } else if (code == 15) {
            packet.g2();
        } else if (code == 17) {
            this.icon = packet.g2();
        } else if (code == 18) {
            @Pc(29) int local29 = packet.g1();
            this.aStringArray28 = new String[local29];
            this.anIntArray432 = new int[local29];
            this.anIntArray431 = new int[local29];
            this.anIntArray429 = new int[local29];
            for (@Pc(36) int local36 = 0; local36 < local29; local36++) {
                this.anIntArray432[local36] = packet.g4();
                this.anIntArray429[local36] = packet.g4();
                this.anIntArray431[local36] = packet.g4();
                this.aStringArray28[local36] = packet.gjstr();
            }
        } else if (code == 19) {
            @Pc(29) int local29 = packet.g1();
            this.anIntArray434 = new int[local29];
            this.aStringArray27 = new String[local29];
            this.anIntArray430 = new int[local29];
            this.anIntArray428 = new int[local29];
            for (@Pc(36) int local36 = 0; local36 < local29; local36++) {
                this.anIntArray434[local36] = packet.g4();
                this.anIntArray430[local36] = packet.g4();
                this.anIntArray428[local36] = packet.g4();
                this.aStringArray27[local36] = packet.gjstr();
            }
        } else if (code == 249) {
            @Pc(29) int count = packet.g1();
            if (this.params == null) {
                @Pc(36) int local36 = IntMath.nextPow2(count);
                this.params = new IterableHashTable(local36);
            }

            for (@Pc(36) int i = 0; i < count; i++) {
                @Pc(224) boolean string = packet.g1() == 1;
                @Pc(228) int id = packet.g3();

                @Pc(237) Node param;
                if (string) {
                    param = new StringNode(packet.gjstr());
                } else {
                    param = new IntNode(packet.g4());
                }

                this.params.put(id, param);
            }
        }
    }

    @OriginalMember(owner = "client!la", name = "a", descriptor = "(Lclient!ge;I)V")
    public void decode(@OriginalArg(0) Packet packet) {
        while (true) {
            @Pc(3) int code = packet.g1();
            if (code == 0) {
                return;
            }

            this.decode(code, packet);
        }
    }
}
