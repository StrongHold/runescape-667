import com.jagex.ParticleList;
import com.jagex.PickableEntity;
import com.jagex.game.Location;
import com.jagex.game.runetek6.config.loctype.LocInteractivity;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeCustomisation;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qg")
public final class DynamicWallDecor extends WallDecor implements Location {

    @OriginalMember(owner = "client!qg", name = "I", descriptor = "Lclient!ke;")
    public BoundingCylinder cylinder;

    @OriginalMember(owner = "client!qg", name = "R", descriptor = "Z")
    public boolean transparent = true;

    @OriginalMember(owner = "client!qg", name = "bb", descriptor = "Lclient!sh;")
    public LocEntity entity;

    @OriginalMember(owner = "client!qg", name = "H", descriptor = "Z")
    public final boolean interactive;

    @OriginalMember(owner = "client!qg", name = "<init>", descriptor = "(Lclient!ha;Lclient!c;IIIIIZIIIII)V")
    public DynamicWallDecor(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) LocType type, @OriginalArg(2) int level, @OriginalArg(3) int virtualLevel, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z, @OriginalArg(7) boolean underwater, @OriginalArg(8) int arg8, @OriginalArg(9) int arg9, @OriginalArg(10) int shape, @OriginalArg(11) int rotation, @OriginalArg(12) int animation) {
        super(x, y, z, level, virtualLevel, arg8, arg9);
        this.entity = new LocEntity(toolkit, type, shape, rotation, super.level, virtualLevel, this, underwater, animation);
        this.interactive = type.active != LocInteractivity.NONINTERACTIVE && !underwater;
    }

    @OriginalMember(owner = "client!qg", name = "c", descriptor = "(B)I")
    @Override
    public int getSphereRadius(@OriginalArg(0) byte arg0) {
        return arg0 == -21 ? this.entity.getSphereRadius(true) : -55;
    }

    @OriginalMember(owner = "client!qg", name = "b", descriptor = "(I)I")
    @Override
    public int getShape() {
        return this.entity.shape;
    }

    @OriginalMember(owner = "client!qg", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit arg0) {
        @Pc(14) Model local14 = this.entity.model(arg0, false, true, true, 0x800);
        if (local14 == null) {
            return null;
        }
        @Pc(22) Matrix local22 = arg0.scratchMatrix();
        local22.applyTranslation(super.x + super.aShort101, super.y, super.z + super.aShort102);
        @Pc(42) PickableEntity local42 = Static642.allocatePickableEntity(this.interactive, 1);
        @Pc(55) int local55 = super.x >> 9;
        @Pc(60) int local60 = super.z >> 9;
        this.entity.method7681(local60, local14, true, -9827, arg0, local55, local55, local60, local22);
        if (OrthoMode.enabled) {
            local14.renderOrtho(local22, local42.pickingCylinders[0], OrthoMode.renderZoom, 0);
        } else {
            local14.render(local22, local42.pickingCylinders[0], 0);
        }
        if (this.entity.particleSystem != null) {
            @Pc(106) ParticleList local106 = this.entity.particleSystem.getList();
            if (OrthoMode.enabled) {
                arg0.renderOrtho(local106, OrthoMode.renderZoom);
            } else {
                arg0.render(local106);
            }
        }
        this.transparent = local14.F() || this.entity.particleSystem != null;
        if (this.cylinder == null) {
            this.cylinder = BoundingCylinder.create(super.y, super.x, local14, super.z);
        } else {
            BoundingCylinder.update(local14, super.z, super.y, super.x, this.cylinder);
        }
        return local42;
    }

    @OriginalMember(owner = "client!qg", name = "c", descriptor = "(I)I")
    @Override
    public int getRotation() {
        return this.entity.rotation;
    }

    @OriginalMember(owner = "client!qg", name = "d", descriptor = "(I)V")
    @Override
    public void method6856() {
    }

    @OriginalMember(owner = "client!qg", name = "a", descriptor = "(Lclient!ha;I)V")
    @Override
    public void removeShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.removeShadow(toolkit);
    }

    @OriginalMember(owner = "client!qg", name = "a", descriptor = "(IIZLclient!ha;)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit) {
        @Pc(17) Model local17 = this.entity.model(toolkit, false, !arg2, arg2, 0x20000);
        if (local17 == null) {
            return false;
        } else {
            @Pc(25) Matrix local25 = toolkit.scratchMatrix();
            local25.applyTranslation(super.aShort101 + super.x, super.y, super.z + super.aShort102);
            return OrthoMode.enabled ? local17.pickedOrtho(y, x, local25, false, 0, OrthoMode.renderZoom) : local17.picked(y, x, local25, false, 0);
        }
    }

    @OriginalMember(owner = "client!qg", name = "a", descriptor = "(Lclient!gp;B)V")
    public void method6862(@OriginalArg(0) LocTypeCustomisation arg0) {
        this.entity.customise(arg0);
    }

    @OriginalMember(owner = "client!qg", name = "e", descriptor = "(I)Z")
    @Override
    public boolean hardShadow() {
        return this.entity.hardShadow();
    }

    @OriginalMember(owner = "client!qg", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isStationary() {
        return false;
    }

    @OriginalMember(owner = "client!qg", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        return arg1 >= -93 ? null : this.cylinder;
    }

    @OriginalMember(owner = "client!qg", name = "b", descriptor = "(Lclient!ha;I)V")
    @Override
    public void addShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.addShadow(toolkit);
    }

    @OriginalMember(owner = "client!qg", name = "h", descriptor = "(I)Z")
    @Override
    public boolean isTransparent(@OriginalArg(0) int arg0) {
        return arg0 == 0 ? this.transparent : true;
    }

    @OriginalMember(owner = "client!qg", name = "k", descriptor = "(I)I")
    @Override
    public int getMinY(@OriginalArg(0) int arg0) {
        return arg0 == 2 ? this.entity.getMinY() : 14;
    }

    @OriginalMember(owner = "client!qg", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1) {
        if (arg1 != -5) {
            this.method6862(null);
        }
        @Pc(21) Model local21 = this.entity.model(arg0, false, true, true, 0x40000);
        if (local21 == null) {
            return;
        }
        @Pc(28) int local28 = super.x >> 9;
        @Pc(33) int local33 = super.z >> 9;
        @Pc(36) Matrix local36 = arg0.scratchMatrix();
        local36.applyTranslation(super.x, super.y, super.z);
        this.entity.method7681(local33, local21, false, arg1 - 9822, arg0, local28, local28, local33, local36);
    }

    @OriginalMember(owner = "client!qg", name = "a", descriptor = "(I)I")
    @Override
    public int getId() {
        return this.entity.id;
    }
}
