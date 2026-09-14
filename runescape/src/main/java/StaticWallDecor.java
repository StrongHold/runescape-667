import com.jagex.PickableEntity;
import com.jagex.game.Location;
import com.jagex.game.runetek6.config.loctype.LocInteractivity;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Model;
import com.jagex.graphics.ModelAndShadow;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!im")
public final class StaticWallDecor extends WallDecor implements Location {

    @OriginalMember(owner = "client!im", name = "T", descriptor = "Lclient!ke;")
    public BoundingCylinder cylinder;

    @OriginalMember(owner = "client!im", name = "P", descriptor = "S")
    public final short id;

    @OriginalMember(owner = "client!im", name = "ib", descriptor = "B")
    public byte rotation;

    @OriginalMember(owner = "client!im", name = "jb", descriptor = "B")
    public byte shape;

    @OriginalMember(owner = "client!im", name = "K", descriptor = "Z")
    public final boolean interactive;

    @OriginalMember(owner = "client!im", name = "Y", descriptor = "Z")
    public final boolean underwater;

    @OriginalMember(owner = "client!im", name = "kb", descriptor = "Z")
    public final boolean hardShadow;

    @OriginalMember(owner = "client!im", name = "M", descriptor = "Lclient!ka;")
    public Model model;

    @OriginalMember(owner = "client!im", name = "eb", descriptor = "Lclient!r;")
    public Shadow shadow;

    @OriginalMember(owner = "client!im", name = "<init>", descriptor = "(Lclient!ha;Lclient!c;IIIIIZIIII)V")
    public StaticWallDecor(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) LocType type, @OriginalArg(2) int level, @OriginalArg(3) int virtualLevel, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z, @OriginalArg(7) boolean underwater, @OriginalArg(8) int offsetX, @OriginalArg(9) int offsetZ, @OriginalArg(10) int shape, @OriginalArg(11) int rotation) {
        super(x, y, z, level, virtualLevel, offsetX, offsetZ);
        super.z = z;
        this.id = (short) type.id;
        this.rotation = (byte) rotation;
        super.x = x;
        this.shape = (byte) shape;
        this.interactive = type.active != LocInteractivity.NONINTERACTIVE && !underwater;
        this.underwater = underwater;
        this.hardShadow = toolkit.hardShadow() && type.hardshadow && !this.underwater && ClientOptions.instance.hardShadows.getValue() != 0;

        @Pc(81) ModelAndShadow modelAndShadow = this.modelAndShadow(toolkit, 0x800, this.hardShadow);
        if (modelAndShadow != null) {
            this.model = modelAndShadow.model;
            this.shadow = modelAndShadow.shadow;
        }
    }

    @OriginalMember(owner = "client!im", name = "c", descriptor = "(I)I")
    @Override
    public int getRotation() {
        return this.rotation;
    }

    @OriginalMember(owner = "client!im", name = "a", descriptor = "(IILclient!ha;)Lclient!ka;")
    public Model getModel(@OriginalArg(0) int functionMask, @OriginalArg(2) Toolkit toolkit) {
        if (this.model != null && toolkit.compareFunctionMasks(this.model.ua(), functionMask) == 0) {
            return this.model;
        } else {
            @Pc(35) ModelAndShadow modelAndShadow = this.modelAndShadow(toolkit, functionMask, false);
            return modelAndShadow == null ? null : modelAndShadow.model;
        }
    }

    @OriginalMember(owner = "client!im", name = "d", descriptor = "(I)V")
    @Override
    public void method6856() {
        if (this.model != null) {
            this.model.method7479();
        }
    }

    @OriginalMember(owner = "client!im", name = "k", descriptor = "(I)I")
    @Override
    public int getMinY(@OriginalArg(0) int arg0) {
        if (arg0 != 2) {
            this.model = null;
        }
        return this.model == null ? 0 : this.model.fa();
    }

    @OriginalMember(owner = "client!im", name = "e", descriptor = "(I)Z")
    @Override
    public boolean hardShadow() {
        return this.hardShadow;
    }

    @OriginalMember(owner = "client!im", name = "a", descriptor = "(IIZLclient!ha;)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit) {
        if (arg2) {
            this.removeShadow(null);
        }
        @Pc(18) Model model = this.getModel(131072, toolkit);
        if (model == null) {
            return false;
        } else {
            @Pc(23) Matrix matrix = toolkit.scratchMatrix();
            matrix.applyTranslation(super.x, super.y, super.z);
            return OrthoMode.enabled ? model.pickedOrtho(y, x, matrix, false, 0, OrthoMode.renderZoom) : model.picked(y, x, matrix, false, 0);
        }
    }

    @OriginalMember(owner = "client!im", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (this.cylinder == null) {
            this.cylinder = BoundingCylinder.create(super.y, super.x, this.getModel(0, toolkit), super.z);
        }
        return this.cylinder;
    }

    @OriginalMember(owner = "client!im", name = "b", descriptor = "(Lclient!ha;I)V")
    @Override
    public void addShadow(@OriginalArg(0) Toolkit toolkit) {
        @Pc(37) Shadow shadow;
        if (this.shadow == null && this.hardShadow) {
            @Pc(29) ModelAndShadow modelAndShadow = this.modelAndShadow(toolkit, 0x40000, true);
            shadow = modelAndShadow == null ? null : modelAndShadow.shadow;
        } else {
            shadow = this.shadow;
            this.shadow = null;
        }
        if (shadow != null) {
            Static630.method8357(shadow, super.virtualLevel, super.x, super.z, null);
        }
    }

    @OriginalMember(owner = "client!im", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 != -5) {
            this.rotation = 89;
        }
    }

    @OriginalMember(owner = "client!im", name = "h", descriptor = "(I)Z")
    @Override
    public boolean isTransparent(@OriginalArg(0) int arg0) {
        if (arg0 == 0) {
            return this.model == null ? false : this.model.F();
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!im", name = "a", descriptor = "(Lclient!ha;I)V")
    @Override
    public void removeShadow(@OriginalArg(0) Toolkit toolkit) {
        @Pc(28) Shadow shadow;
        if (this.shadow == null && this.hardShadow) {
            @Pc(20) ModelAndShadow modelAndShadow = this.modelAndShadow(toolkit, 0x40000, true);
            shadow = modelAndShadow == null ? null : modelAndShadow.shadow;
        } else {
            shadow = this.shadow;
            this.shadow = null;
        }
        if (shadow != null) {
            Static292.method4618(shadow, super.virtualLevel, super.x, super.z, null);
        }
    }

    @OriginalMember(owner = "client!im", name = "a", descriptor = "(I)I")
    @Override
    public int getId() {
        return this.id & 0xFFFF;
    }

    @OriginalMember(owner = "client!im", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isStationary() {
        if (this.model == null) {
            return true;
        } else {
            return !this.model.r();
        }
    }

    @OriginalMember(owner = "client!im", name = "a", descriptor = "(BLclient!ha;IZ)Lclient!od;")
    public ModelAndShadow modelAndShadow(@OriginalArg(1) Toolkit toolkit, @OriginalArg(2) int functionMask, @OriginalArg(3) boolean addShadow) {
        @Pc(17) LocType type = LocTypeList.instance.list(this.id & 0xFFFF);
        @Pc(29) Ground floor = LocGround.floor(this.underwater, super.virtualLevel);
        @Pc(24) Ground ceiling = LocGround.ceiling(this.underwater, super.virtualLevel);
        return type.modelAndShadow(this.rotation, super.z, super.x, floor, addShadow, super.y, this.shape, toolkit, null, functionMask, ceiling);
    }

    @OriginalMember(owner = "client!im", name = "b", descriptor = "(I)I")
    @Override
    public int getShape() {
        return this.shape;
    }

    @OriginalMember(owner = "client!im", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit toolkit) {
        if (this.model == null) {
            return null;
        }
        @Pc(12) Matrix matrix = toolkit.scratchMatrix();
        matrix.applyTranslation(super.x + super.aShort101, super.y, super.aShort102 + super.z);
        @Pc(41) PickableEntity entity = Static642.allocatePickableEntity(this.interactive, 1);
        if (OrthoMode.enabled) {
            this.model.renderOrtho(matrix, entity.pickingCylinders[0], OrthoMode.renderZoom, 0);
        } else {
            this.model.render(matrix, entity.pickingCylinders[0], 0);
        }
        return entity;
    }

    @OriginalMember(owner = "client!im", name = "c", descriptor = "(B)I")
    @Override
    public int getSphereRadius(@OriginalArg(0) byte arg0) {
        if (arg0 != -21) {
            this.shape = 110;
        }
        return this.model == null ? 0 : this.model.ma();
    }
}
