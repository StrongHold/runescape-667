import com.jagex.collect.HashTable;
import com.jagex.collect.Node;
import com.jagex.core.crypto.Whirlpool;
import com.jagex.core.util.SystemTimer;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!pm")
public final class ResourceProvider_Sub1 extends ResourceProvider {

    @OriginalMember(owner = "client!pm", name = "t", descriptor = "[B")
    public byte[] aByteArray88;

    @OriginalMember(owner = "client!pm", name = "i", descriptor = "Lclient!pj;")
    public Js5Index aJs5Index_1;

    @OriginalMember(owner = "client!pm", name = "M", descriptor = "Z")
    public boolean aBoolean568;

    @OriginalMember(owner = "client!pm", name = "g", descriptor = "I")
    public int anInt7473 = 0;

    @OriginalMember(owner = "client!pm", name = "A", descriptor = "Lclient!av;")
    public final HashTable aHashTable_36 = new HashTable(16);

    @OriginalMember(owner = "client!pm", name = "h", descriptor = "I")
    public int anInt7475 = 0;

    @OriginalMember(owner = "client!pm", name = "F", descriptor = "Lclient!sia;")
    public final Deque aDeque_41 = new Deque();

    @OriginalMember(owner = "client!pm", name = "m", descriptor = "J")
    public long aLong239 = 0L;

    @OriginalMember(owner = "client!pm", name = "j", descriptor = "I")
    public final int anInt7465;

    @OriginalMember(owner = "client!pm", name = "E", descriptor = "Lclient!af;")
    public final Class9 aClass9_3;

    @OriginalMember(owner = "client!pm", name = "n", descriptor = "Z")
    public boolean aBoolean567;

    @OriginalMember(owner = "client!pm", name = "v", descriptor = "Lclient!sia;")
    public Deque aDeque_42;

    @OriginalMember(owner = "client!pm", name = "x", descriptor = "Z")
    public final boolean aBoolean569;

    @OriginalMember(owner = "client!pm", name = "K", descriptor = "Lclient!pla;")
    public final Class295 aClass295_2;

    @OriginalMember(owner = "client!pm", name = "s", descriptor = "I")
    public final int anInt7463;

    @OriginalMember(owner = "client!pm", name = "D", descriptor = "I")
    public final int anInt7472;

    @OriginalMember(owner = "client!pm", name = "H", descriptor = "Lclient!iba;")
    public final Class174 aClass174_3;

    @OriginalMember(owner = "client!pm", name = "O", descriptor = "[B")
    public final byte[] aByteArray89;

    @OriginalMember(owner = "client!pm", name = "B", descriptor = "Lclient!af;")
    public final Class9 aClass9_2;

    @OriginalMember(owner = "client!pm", name = "C", descriptor = "Lclient!tw;")
    public DoublyLinkedNode_Sub2_Sub17 aClass2_Sub2_Sub17_1;

    @OriginalMember(owner = "client!pm", name = "<init>", descriptor = "(ILclient!af;Lclient!af;Lclient!pla;Lclient!iba;I[BIZ)V")
    public ResourceProvider_Sub1(@OriginalArg(0) int arg0, @OriginalArg(1) Class9 arg1, @OriginalArg(2) Class9 arg2, @OriginalArg(3) Class295 arg3, @OriginalArg(4) Class174 arg4, @OriginalArg(5) int arg5, @OriginalArg(6) byte[] arg6, @OriginalArg(7) int arg7, @OriginalArg(8) boolean arg8) {
        this.anInt7465 = arg0;
        this.aClass9_3 = arg1;
        if (this.aClass9_3 == null) {
            this.aBoolean567 = false;
        } else {
            this.aBoolean567 = true;
            this.aDeque_42 = new Deque();
        }
        this.aBoolean569 = arg8;
        this.aClass295_2 = arg3;
        this.anInt7463 = arg5;
        this.anInt7472 = arg7;
        this.aClass174_3 = arg4;
        this.aByteArray89 = arg6;
        this.aClass9_2 = arg2;
        if (this.aClass9_2 != null) {
            this.aClass2_Sub2_Sub17_1 = this.aClass174_3.method3825(this.aClass9_2, this.anInt7465);
        }
    }

    @OriginalMember(owner = "client!pm", name = "c", descriptor = "(I)I")
    public int method6644() {
        if (this.index() == null) {
            return this.aClass2_Sub2_Sub17_1 == null ? 0 : this.aClass2_Sub2_Sub17_1.method8972();
        } else {
            return 100;
        }
    }

    @OriginalMember(owner = "client!pm", name = "f", descriptor = "(I)I")
    public int method6645() {
        return this.aJs5Index_1 == null ? 0 : this.aJs5Index_1.groupCount;
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(II)V")
    @Override
    public void requestGroup(@OriginalArg(1) int groupId) {
        if (this.aClass9_3 == null) {
            return;
        }
        for (@Pc(23) Node local23 = this.aDeque_41.first(65280); local23 != null; local23 = this.aDeque_41.next()) {
            if (local23.key == (long) groupId) {
                return;
            }
        }
        @Pc(50) Node local50 = new Node();
        local50.key = (long) groupId;
        this.aDeque_41.addLast(local50);
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(I)I")
    public int method6647() {
        if (this.aJs5Index_1 == null) {
            return 0;
        } else if (this.aBoolean567) {
            @Pc(29) Node local29 = this.aDeque_42.first(65280);
            return local29 == null ? 0 : (int) local29.key;
        } else {
            return this.aJs5Index_1.groupCount;
        }
    }

    @OriginalMember(owner = "client!pm", name = "d", descriptor = "(I)V")
    public void method6648() {
        if (this.aDeque_42 != null) {
            if (this.index() == null) {
                return;
            }
            @Pc(33) boolean local33;
            @Pc(38) Node local38;
            @Pc(44) int local44;
            @Pc(147) Node local147;
            if (this.aBoolean567) {
                local33 = true;
                for (local38 = this.aDeque_42.first(65280); local38 != null; local38 = this.aDeque_42.next()) {
                    local44 = (int) local38.key;
                    if (this.aByteArray88[local44] == 0) {
                        this.method6651(local44, 1);
                    }
                    if (this.aByteArray88[local44] == 0) {
                        local33 = false;
                    } else {
                        local38.remove();
                    }
                }
                while (this.aJs5Index_1.fileCounts.length > this.anInt7475) {
                    if (this.aJs5Index_1.fileCounts[this.anInt7475] == 0) {
                        this.anInt7475++;
                    } else {
                        if (this.aClass174_3.anInt4243 >= 250) {
                            local33 = false;
                            break;
                        }
                        if (this.aByteArray88[this.anInt7475] == 0) {
                            this.method6651(this.anInt7475, 1);
                        }
                        if (this.aByteArray88[this.anInt7475] == 0) {
                            local147 = new Node();
                            local147.key = (long) this.anInt7475;
                            local33 = false;
                            this.aDeque_42.addLast(local147);
                        }
                        this.anInt7475++;
                    }
                }
                if (local33) {
                    this.anInt7475 = 0;
                    this.aBoolean567 = false;
                }
            } else if (this.aBoolean568) {
                local33 = true;
                for (local38 = this.aDeque_42.first(65280); local38 != null; local38 = this.aDeque_42.next()) {
                    local44 = (int) local38.key;
                    if (this.aByteArray88[local44] != 1) {
                        this.method6651(local44, 2);
                    }
                    if (this.aByteArray88[local44] == 1) {
                        local38.remove();
                    } else {
                        local33 = false;
                    }
                }
                while (this.aJs5Index_1.fileCounts.length > this.anInt7475) {
                    if (this.aJs5Index_1.fileCounts[this.anInt7475] == 0) {
                        this.anInt7475++;
                    } else {
                        if (this.aClass295_2.method6625()) {
                            local33 = false;
                            break;
                        }
                        if (this.aByteArray88[this.anInt7475] != 1) {
                            this.method6651(this.anInt7475, 2);
                        }
                        if (this.aByteArray88[this.anInt7475] != 1) {
                            local147 = new Node();
                            local147.key = (long) this.anInt7475;
                            this.aDeque_42.addLast(local147);
                            local33 = false;
                        }
                        this.anInt7475++;
                    }
                }
                if (local33) {
                    this.anInt7475 = 0;
                    this.aBoolean568 = false;
                }
            } else {
                this.aDeque_42 = null;
            }
        }
        if (!this.aBoolean569 || SystemTimer.safetime() < this.aLong239) {
            return;
        }
        for (@Pc(366) DoublyLinkedNode_Sub2_Sub17 local366 = (DoublyLinkedNode_Sub2_Sub17) this.aHashTable_36.first(); local366 != null; local366 = (DoublyLinkedNode_Sub2_Sub17) this.aHashTable_36.next()) {
            if (!local366.aBoolean778) {
                if (local366.aBoolean776) {
                    if (!local366.aBoolean777) {
                        throw new RuntimeException();
                    }
                    local366.remove();
                } else {
                    local366.aBoolean776 = true;
                }
            }
        }
        this.aLong239 = SystemTimer.safetime() + 1000L;
    }

    @OriginalMember(owner = "client!pm", name = "d", descriptor = "(B)I")
    public int method6649() {
        return this.anInt7473;
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(IB)[B")
    @Override
    public byte[] fetchgroup(@OriginalArg(0) int groupId) {
        @Pc(9) DoublyLinkedNode_Sub2_Sub17 local9 = this.method6651(groupId, 0);
        if (local9 == null) {
            return null;
        } else {
            @Pc(26) byte[] local26 = local9.method8971();
            local9.remove();
            return local26;
        }
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(III)Lclient!tw;")
    public DoublyLinkedNode_Sub2_Sub17 method6651(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        @Pc(19) DoublyLinkedNode_Sub2_Sub17 local19 = (DoublyLinkedNode_Sub2_Sub17) this.aHashTable_36.get((long) arg0);
        if (local19 != null && arg1 == 0 && !local19.aBoolean777 && local19.aBoolean778) {
            local19.remove();
            local19 = null;
        }
        if (local19 == null) {
            if (arg1 == 0) {
                if (this.aClass9_3 == null || this.aByteArray88[arg0] == -1) {
                    if (this.aClass295_2.method6630()) {
                        return null;
                    }
                    local19 = this.aClass295_2.method6633(this.anInt7465, arg0, true, (byte) 2);
                } else {
                    local19 = this.aClass174_3.method3825(this.aClass9_3, arg0);
                }
            } else if (arg1 == 1) {
                if (this.aClass9_3 == null) {
                    throw new RuntimeException();
                }
                local19 = this.aClass174_3.method3830(this.aClass9_3, arg0);
            } else if (arg1 == 2) {
                if (this.aClass9_3 == null) {
                    throw new RuntimeException();
                }
                if (this.aByteArray88[arg0] != -1) {
                    throw new RuntimeException();
                }
                if (this.aClass295_2.method6625()) {
                    return null;
                }
                local19 = this.aClass295_2.method6633(this.anInt7465, arg0, false, (byte) 2);
            } else {
                throw new RuntimeException();
            }
            this.aHashTable_36.put((long) arg0, local19);
        }
        if (local19.aBoolean778) {
            return null;
        }
        @Pc(194) byte[] local194 = local19.method8971();
        @Pc(224) int local224;
        @Pc(254) byte[] local254;
        @Pc(263) byte[] local263;
        @Pc(265) int local265;
        @Pc(383) DoublyLinkedNode_Sub2_Sub17_Sub1 local383;
        if (!(local19 instanceof DoublyLinkedNode_Sub2_Sub17_)) {
            try {
                label157:
                {
                    if (local194 != null && local194.length > 2) {
                        Static10.aCRC32_1.reset();
                        Static10.aCRC32_1.update(local194, 0, local194.length - 2);
                        local224 = (int) Static10.aCRC32_1.getValue();
                        if (this.aJs5Index_1.groupCrcs[arg0] != local224) {
                            throw new RuntimeException();
                        }
                        if (this.aJs5Index_1.groupHashes == null || this.aJs5Index_1.groupHashes[arg0] == null) {
                            break label157;
                        }
                        local254 = this.aJs5Index_1.groupHashes[arg0];
                        local263 = Whirlpool.digest(local194, local194.length - 2, 0);
                        local265 = 0;
                        while (true) {
                            if (local265 >= 64) {
                                break label157;
                            }
                            if (local254[local265] != local263[local265]) {
                                throw new RuntimeException();
                            }
                            local265++;
                        }
                    }
                    throw new RuntimeException();
                }
                this.aClass295_2.anInt7453 = 0;
                this.aClass295_2.anInt7452 = 0;
            } catch (@Pc(498) RuntimeException local498) {
                this.aClass295_2.method6619();
                local19.remove();
                if (local19.aBoolean777 && !this.aClass295_2.method6630()) {
                    local383 = this.aClass295_2.method6633(this.anInt7465, arg0, true, (byte) 2);
                    this.aHashTable_36.put((long) arg0, local383);
                }
                return null;
            }
            local194[local194.length - 2] = (byte) (this.aJs5Index_1.groupVersions[arg0] >>> 8);
            local194[local194.length - 1] = (byte) this.aJs5Index_1.groupVersions[arg0];
            if (this.aClass9_3 != null) {
                this.aClass174_3.method3829(local194, arg0, this.aClass9_3);
                if (this.aByteArray88[arg0] != 1) {
                    this.anInt7473++;
                    this.aByteArray88[arg0] = 1;
                }
            }
            if (!local19.aBoolean777) {
                local19.remove();
            }
            return local19;
        }
        try {
            if (local194 == null || local194.length <= 2) {
                throw new RuntimeException();
            }
            Static10.aCRC32_1.reset();
            Static10.aCRC32_1.update(local194, 0, local194.length - 2);
            local224 = (int) Static10.aCRC32_1.getValue();
            if (local224 != this.aJs5Index_1.groupCrcs[arg0]) {
                throw new RuntimeException();
            }
            if (this.aJs5Index_1.groupHashes != null && this.aJs5Index_1.groupHashes[arg0] != null) {
                local254 = this.aJs5Index_1.groupHashes[arg0];
                local263 = Whirlpool.digest(local194, local194.length - 2, 0);
                for (local265 = 0; local265 < 64; local265++) {
                    if (local263[local265] != local254[local265]) {
                        throw new RuntimeException();
                    }
                }
            }
            @Pc(307) int local307 = (local194[local194.length - 1] & 0xFF) + ((local194[local194.length - 2] & 0xFF) << 8);
            if (local307 != (this.aJs5Index_1.groupVersions[arg0] & 0xFFFF)) {
                throw new RuntimeException();
            }
            if (this.aByteArray88[arg0] != 1) {
                this.anInt7473++;
                this.aByteArray88[arg0] = 1;
            }
            if (!local19.aBoolean777) {
                local19.remove();
            }
            return local19;
        } catch (@Pc(355) Exception local355) {
            this.aByteArray88[arg0] = -1;
            local19.remove();
            if (local19.aBoolean777 && !this.aClass295_2.method6630()) {
                local383 = this.aClass295_2.method6633(this.anInt7465, arg0, true, (byte) 2);
                this.aHashTable_36.put((long) arg0, local383);
            }
            return null;
        }
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(BI)I")
    @Override
    public int completePercentage(@OriginalArg(1) int groupId) {
        @Pc(19) DoublyLinkedNode_Sub2_Sub17 local19 = (DoublyLinkedNode_Sub2_Sub17) this.aHashTable_36.get((long) groupId);
        return local19 == null ? 0 : local19.method8972();
    }

    @OriginalMember(owner = "client!pm", name = "a", descriptor = "(Z)V")
    public void method6653() {
        if (this.aDeque_42 == null || this.index() == null) {
            return;
        }
        for (@Pc(21) Node local21 = this.aDeque_41.first(65280); local21 != null; local21 = this.aDeque_41.next()) {
            @Pc(29) int local29 = (int) local21.key;
            if (local29 < 0 || local29 >= this.aJs5Index_1.groupLimit || this.aJs5Index_1.fileCounts[local29] == 0) {
                local21.remove();
            } else {
                if (this.aByteArray88[local29] == 0) {
                    this.method6651(local29, 1);
                }
                if (this.aByteArray88[local29] == -1) {
                    this.method6651(local29, 2);
                }
                if (this.aByteArray88[local29] == 1) {
                    local21.remove();
                }
            }
        }
    }

    @OriginalMember(owner = "client!pm", name = "b", descriptor = "(B)Lclient!pj;")
    @Override
    public Js5Index index() {
        if (this.aJs5Index_1 != null) {
            return this.aJs5Index_1;
        }
        if (this.aClass2_Sub2_Sub17_1 == null) {
            if (this.aClass295_2.method6630()) {
                return null;
            }
            this.aClass2_Sub2_Sub17_1 = this.aClass295_2.method6633(255, this.anInt7465, true, (byte) 0);
        }
        if (this.aClass2_Sub2_Sub17_1.aBoolean778) {
            return null;
        }
        @Pc(53) byte[] local53 = this.aClass2_Sub2_Sub17_1.method8971();
        if (this.aClass2_Sub2_Sub17_1 instanceof DoublyLinkedNode_Sub2_Sub17_) {
            try {
                if (local53 == null) {
                    throw new RuntimeException();
                }
                this.aJs5Index_1 = new Js5Index(local53, this.anInt7463, this.aByteArray89);
                if (this.anInt7472 != this.aJs5Index_1.version) {
                    throw new RuntimeException();
                }
            } catch (@Pc(162) RuntimeException local162) {
                this.aJs5Index_1 = null;
                if (this.aClass295_2.method6630()) {
                    this.aClass2_Sub2_Sub17_1 = null;
                } else {
                    this.aClass2_Sub2_Sub17_1 = this.aClass295_2.method6633(255, this.anInt7465, true, (byte) 0);
                }
                return null;
            }
        } else {
            try {
                if (local53 == null) {
                    throw new RuntimeException();
                }
                this.aJs5Index_1 = new Js5Index(local53, this.anInt7463, this.aByteArray89);
            } catch (@Pc(76) RuntimeException local76) {
                this.aClass295_2.method6619();
                this.aJs5Index_1 = null;
                if (this.aClass295_2.method6630()) {
                    this.aClass2_Sub2_Sub17_1 = null;
                } else {
                    this.aClass2_Sub2_Sub17_1 = this.aClass295_2.method6633(255, this.anInt7465, true, (byte) 0);
                }
                return null;
            }
            if (this.aClass9_2 != null) {
                this.aClass174_3.method3829(local53, this.anInt7465, this.aClass9_2);
            }
        }
        if (this.aClass9_3 != null) {
            this.aByteArray88 = new byte[this.aJs5Index_1.groupLimit];
            this.anInt7473 = 0;
        }
        this.aClass2_Sub2_Sub17_1 = null;
        return this.aJs5Index_1;
    }

    @OriginalMember(owner = "client!pm", name = "c", descriptor = "(B)V")
    public void method6654() {
        if (this.aClass9_3 != null) {
            this.aBoolean568 = true;
            if (this.aDeque_42 == null) {
                this.aDeque_42 = new Deque();
            }
        }
    }
}