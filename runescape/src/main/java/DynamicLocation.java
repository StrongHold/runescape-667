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

@OriginalClass("client!oe")
public final class DynamicLocation extends PositionEntity implements Location {

    @OriginalMember(owner = "client!tea", name = "a", descriptor = "(IZI)B")
    private static byte method8215(@OriginalArg(0) int shape, @OriginalArg(2) int rotation) {
        if (shape == LocShapes.WALL_DIAGONAL) {
            return (byte) ((rotation & 0x1) == 0 ? 1 : 2);
        } else {
            return 0;
        }
    }

    @OriginalMember(owner = "client!oe", name = "P", descriptor = "Lclient!ke;")
    public BoundingCylinder cylinder;

    @OriginalMember(owner = "client!oe", name = "X", descriptor = "Z")
    public boolean transparent = true;

    @OriginalMember(owner = "client!oe", name = "hb", descriptor = "Lclient!sh;")
    public LocEntity entity;

    @OriginalMember(owner = "client!oe", name = "S", descriptor = "Z")
    public boolean interactive;

    @OriginalMember(owner = "client!oe", name = "<init>", descriptor = "(Lclient!ha;Lclient!c;IIIIIZIIIIIII)V")
    public DynamicLocation(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) LocType type, @OriginalArg(2) int level, @OriginalArg(3) int virtualLevel, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z, @OriginalArg(7) boolean underwater, @OriginalArg(8) int x1, @OriginalArg(9) int x2, @OriginalArg(10) int z1, @OriginalArg(11) int z2, @OriginalArg(12) int shape, @OriginalArg(13) int rotation, @OriginalArg(14) int animation) {
        super(level, virtualLevel, x, y, z, x1, x2, z1, z2, type.raiseobject == 1, method8215(shape, rotation));
        this.entity = new LocEntity(toolkit, type, shape, rotation, super.level, virtualLevel, this, underwater, animation);
        this.interactive = type.active != LocInteractivity.NONINTERACTIVE && !underwater;
    }

    @OriginalMember(owner = "client!oe", name = "a", descriptor = "(IZLclient!ha;IBILclient!eo;)V")
    @Override
    public void shareLight(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) Entity arg6) {
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!oe", name = "a", descriptor = "(IIZLclient!ha;)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit) {
        @Pc(12) Model local12 = this.entity.model(toolkit, false, true, arg2, 0x20000);
        if (local12 == null) {
            return false;
        } else {
            @Pc(20) Matrix local20 = toolkit.scratchMatrix();
            local20.applyTranslation(super.x, super.y, super.z);
            return OrthoMode.enabled ? local12.pickedOrtho(y, x, local20, false, 0, OrthoMode.renderZoom) : local12.picked(y, x, local20, false, 0);
        }
    }

    @OriginalMember(owner = "client!oe", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit toolkit) {
        @Pc(14) Model local14 = this.entity.model(toolkit, false, true, true, 0x800);
        if (local14 == null) {
            return null;
        }

        @Pc(30) Matrix matrix = toolkit.scratchMatrix();
        matrix.applyTranslation(super.x, super.y, super.z);

        @Pc(44) PickableEntity local44 = Static642.allocatePickableEntity(this.interactive, 1);
        this.entity.method7681(super.z2, local14, true, -9827, toolkit, super.x2, super.x1, super.z1, matrix);

        if (OrthoMode.enabled) {
            local14.renderOrtho(matrix, local44.pickingCylinders[0], OrthoMode.renderZoom, 0);
        } else {
            local14.render(matrix, local44.pickingCylinders[0], 0);
        }

        if (this.entity.particleSystem != null) {
            @Pc(94) ParticleList particles = this.entity.particleSystem.getList();
            if (OrthoMode.enabled) {
                toolkit.renderOrtho(particles, OrthoMode.renderZoom);
            } else {
                toolkit.render(particles);
            }
        }

        this.transparent = local14.F() || this.entity.particleSystem != null;
        if (this.cylinder == null) {
            this.cylinder = BoundingCylinder.create(super.y, super.x, local14, super.z);
        } else {
            BoundingCylinder.update(local14, super.z, super.y, super.x, this.cylinder);
        }

        return local44;
    }

    @OriginalMember(owner = "client!oe", name = "h", descriptor = "(I)Z")
    @Override
    public boolean isTransparent(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            this.cylinder = null;
        }
        return this.transparent;
    }

    @OriginalMember(owner = "client!oe", name = "c", descriptor = "(I)I")
    @Override
    public int getRotation() {
        return this.entity.rotation;
    }

    @OriginalMember(owner = "client!oe", name = "e", descriptor = "(I)Z")
    @Override
    public boolean hardShadow() {
        return this.entity.hardShadow();
    }

    @OriginalMember(owner = "client!oe", name = "i", descriptor = "(I)Z")
    @Override
    public boolean method9290(@OriginalArg(0) int arg0) {
        return arg0 == 0 ? false : false;
    }

    @OriginalMember(owner = "client!oe", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1) {
        @Pc(12) Model model = this.entity.model(arg0, true, true, true, 0x40000);
        if (arg1 == -5 && model != null) {
            @Pc(22) Matrix matrix = arg0.scratchMatrix();
            matrix.applyTranslation(super.x, super.y, super.z);
            this.entity.method7681(super.z2, model, false, -9827, arg0, super.x2, super.x1, super.z1, matrix);
        }
    }

    @OriginalMember(owner = "client!oe", name = "b", descriptor = "(I)I")
    @Override
    public int getShape() {
        return this.entity.shape;
    }

    @OriginalMember(owner = "client!oe", name = "a", descriptor = "(I)I")
    @Override
    public int getId() {
        return this.entity.id;
    }

    @OriginalMember(owner = "client!oe", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 >= -93) {
            this.entity = null;
        }
        return this.cylinder;
    }

    @OriginalMember(owner = "client!oe", name = "j", descriptor = "(I)V")
    @Override
    public void stopSharingLight(@OriginalArg(0) int arg0) {
        if (arg0 != 27811) {
            this.interactive = false;
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!oe", name = "a", descriptor = "(ILclient!gp;)V")
    public void customise(@OriginalArg(1) LocTypeCustomisation customisation) {
        this.entity.customise(customisation);
    }

    @OriginalMember(owner = "client!oe", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isStationary() {
        return false;
    }

    @OriginalMember(owner = "client!oe", name = "a", descriptor = "(Lclient!ha;I)V")
    @Override
    public void removeShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.removeShadow(toolkit);
    }

    @OriginalMember(owner = "client!oe", name = "c", descriptor = "(B)I")
    @Override
    public int getSphereRadius(@OriginalArg(0) byte arg0) {
        if (arg0 != -21) {
            this.interactive = true;
        }
        return this.entity.getSphereRadius(true);
    }

    @OriginalMember(owner = "client!oe", name = "b", descriptor = "(Lclient!ha;I)V")
    @Override
    public void addShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.addShadow(toolkit);
    }

    @OriginalMember(owner = "client!oe", name = "d", descriptor = "(I)V")
    @Override
    public void method6856() {
    }

    @OriginalMember(owner = "client!oe", name = "k", descriptor = "(I)I")
    @Override
    public int getMinY(@OriginalArg(0) int arg0) {
        if (arg0 != 2) {
            this.cylinder = null;
        }
        return this.entity.getMinY();
    }
}
