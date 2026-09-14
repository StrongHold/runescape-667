import com.jagex.Entity;
import com.jagex.ParticleList;
import com.jagex.PickableEntity;
import com.jagex.core.constants.LocShapes;
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

@OriginalClass("client!hp")
public final class DynamicGroundDecor extends GroundDecor implements Location {

    @OriginalMember(owner = "client!hp", name = "V", descriptor = "Lclient!ke;")
    public BoundingCylinder cylinder;

    @OriginalMember(owner = "client!hp", name = "C", descriptor = "Z")
    public boolean transparent = true;

    @OriginalMember(owner = "client!hp", name = "eb", descriptor = "Lclient!sh;")
    public LocEntity entity;

    @OriginalMember(owner = "client!hp", name = "K", descriptor = "Z")
    public final boolean interactive;

    @OriginalMember(owner = "client!hp", name = "<init>", descriptor = "(Lclient!ha;Lclient!c;IIIIIZII)V")
    public DynamicGroundDecor(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) LocType type, @OriginalArg(2) int level, @OriginalArg(3) int virtualLevel, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z, @OriginalArg(7) boolean underwater, @OriginalArg(8) int arg8, @OriginalArg(9) int arg9) {
        super(x, y, z, level, virtualLevel, type.offsetY);
        this.entity = new LocEntity(toolkit, type, LocShapes.GROUNDDECOR, arg8, level, virtualLevel, this, underwater, arg9);
        this.interactive = type.active != LocInteractivity.NONINTERACTIVE && !underwater;
    }

    @OriginalMember(owner = "client!hp", name = "a", descriptor = "(IIZLclient!ha;)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit) {
        @Pc(12) Model model = this.entity.model(toolkit, false, true, arg2, 0x20000);
        if (model == null) {
            return false;
        } else {
            @Pc(20) Matrix matrix = toolkit.scratchMatrix();
            matrix.applyTranslation(super.x, super.y, super.z);
            return OrthoMode.enabled ? model.pickedOrtho(y, x, matrix, false, 0, OrthoMode.renderZoom) : model.picked(y, x, matrix, false, 0);
        }
    }

    @OriginalMember(owner = "client!hp", name = "i", descriptor = "(I)Z")
    @Override
    public boolean method9290(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            this.cylinder = null;
        }
        return false;
    }

    @OriginalMember(owner = "client!hp", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 != -5) {
            this.customise(null);
        }

        @Pc(21) Model model = this.entity.model(toolkit, true, true, true, 0x40000);
        if (model == null) {
            return;
        }

        @Pc(28) int x1 = super.x >> 9;
        @Pc(33) int z1 = super.z >> 9;
        @Pc(36) Matrix matrix = toolkit.scratchMatrix();
        matrix.applyTranslation(super.x, super.y, super.z);
        this.entity.method7681(z1, model, false, -9827, toolkit, x1, x1, z1, matrix);
    }

    @OriginalMember(owner = "client!hp", name = "a", descriptor = "(Lclient!gp;I)V")
    public void customise(@OriginalArg(0) LocTypeCustomisation customisation) {
        this.entity.customise(customisation);
    }

    @OriginalMember(owner = "client!hp", name = "a", descriptor = "(Lclient!ha;I)V")
    @Override
    public void removeShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.removeShadow(toolkit);
    }

    @OriginalMember(owner = "client!hp", name = "b", descriptor = "(Lclient!ha;I)V")
    @Override
    public void addShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.addShadow(toolkit);
    }

    @OriginalMember(owner = "client!hp", name = "a", descriptor = "(IZLclient!ha;IBILclient!eo;)V")
    @Override
    public void shareLight(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) Entity arg6) {
        if (arg4 < 101) {
            this.transparent = true;
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!hp", name = "a", descriptor = "(I)I")
    @Override
    public int getId() {
        return this.entity.id;
    }

    @OriginalMember(owner = "client!hp", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isStationary() {
        return false;
    }

    @OriginalMember(owner = "client!hp", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        return this.cylinder;
    }

    @OriginalMember(owner = "client!hp", name = "k", descriptor = "(I)I")
    @Override
    public int getMinY(@OriginalArg(0) int arg0) {
        if (arg0 != 2) {
            this.removeShadow(null);
        }
        return this.entity.getMinY();
    }

    @OriginalMember(owner = "client!hp", name = "e", descriptor = "(I)Z")
    @Override
    public boolean hardShadow() {
        return this.entity.hardShadow();
    }

    @OriginalMember(owner = "client!hp", name = "j", descriptor = "(I)V")
    @Override
    public void stopSharingLight(@OriginalArg(0) int arg0) {
        if (arg0 != 27811) {
            this.hardShadow();
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!hp", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit toolkit) {
        @Pc(22) Model model = this.entity.model(toolkit, false, true, true, 0x800);
        if (model == null) {
            return null;
        }

        @Pc(30) Matrix matrix = toolkit.scratchMatrix();
        matrix.applyTranslation(super.x, super.y, super.z);

        @Pc(44) PickableEntity entity = Static642.allocatePickableEntity(this.interactive, 1);
        @Pc(49) int x1 = super.x >> 9;
        @Pc(54) int z1 = super.z >> 9;

        this.entity.method7681(z1, model, true, -9827, toolkit, x1, x1, z1, matrix);

        if (OrthoMode.enabled) {
            model.renderOrtho(matrix, entity.pickingCylinders[0], OrthoMode.renderZoom, 0);
        } else {
            model.render(matrix, entity.pickingCylinders[0], 0);
        }

        if (this.entity.particleSystem != null) {
            @Pc(100) ParticleList local100 = this.entity.particleSystem.getList();

            if (OrthoMode.enabled) {
                toolkit.renderOrtho(local100, OrthoMode.renderZoom);
            } else {
                toolkit.render(local100);
            }
        }

        this.transparent = model.F() || this.entity.particleSystem != null;

        if (this.cylinder == null) {
            this.cylinder = BoundingCylinder.create(super.y, super.x, model, super.z);
        } else {
            BoundingCylinder.update(model, super.z, super.y, super.x, this.cylinder);
        }

        return entity;
    }

    @OriginalMember(owner = "client!hp", name = "c", descriptor = "(I)I")
    @Override
    public int getRotation() {
        return this.entity.rotation;
    }

    @OriginalMember(owner = "client!hp", name = "c", descriptor = "(B)I")
    @Override
    public int getSphereRadius(@OriginalArg(0) byte arg0) {
        if (arg0 != -21) {
            this.transparent = true;
        }
        return this.entity.getSphereRadius(true);
    }

    @OriginalMember(owner = "client!hp", name = "h", descriptor = "(I)Z")
    @Override
    public boolean isTransparent(@OriginalArg(0) int arg0) {
        return arg0 == 0 ? this.transparent : true;
    }

    @OriginalMember(owner = "client!hp", name = "b", descriptor = "(I)I")
    @Override
    public int getShape() {
        return this.entity.shape;
    }

    @OriginalMember(owner = "client!hp", name = "d", descriptor = "(I)V")
    @Override
    public void method6856() {
    }
}
