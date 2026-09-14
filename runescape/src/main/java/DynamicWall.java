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

@OriginalClass("client!db")
public final class DynamicWall extends Wall implements Location {

    @OriginalMember(owner = "client!bw", name = "H", descriptor = "[I")
    private static final int[] anIntArray111 = {16, 32, 64, 128};

    @OriginalMember(owner = "client!lca", name = "s", descriptor = "[I")
    private static final int[] anIntArray748 = {1, 2, 4, 8};

    @OriginalMember(owner = "client!ln", name = "a", descriptor = "(III)I")
    private static int method5290(@OriginalArg(0) int rotation, @OriginalArg(1) int shape) {
        if (shape == LocShapes.WALL_DIAGONALCORNER || shape == LocShapes.WALL_SQUARECORNER) {
            return anIntArray111[rotation & 0x3];
        } else {
            return anIntArray748[rotation & 0x3];
        }
    }

    @OriginalMember(owner = "client!db", name = "N", descriptor = "Lclient!ke;")
    public BoundingCylinder cylinder;

    @OriginalMember(owner = "client!db", name = "R", descriptor = "Z")
    public boolean transparent = true;

    @OriginalMember(owner = "client!db", name = "jb", descriptor = "Lclient!sh;")
    public LocEntity entity;

    @OriginalMember(owner = "client!db", name = "J", descriptor = "Z")
    public final boolean interactive;

    @OriginalMember(owner = "client!db", name = "<init>", descriptor = "(Lclient!ha;Lclient!c;IIIIIZIII)V")
    public DynamicWall(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) LocType type, @OriginalArg(2) int level, @OriginalArg(3) int virtualLevel, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int z, @OriginalArg(7) boolean underwater, @OriginalArg(8) int shape, @OriginalArg(9) int rotation, @OriginalArg(10) int animation) {
        super(x, y, z, level, virtualLevel, method5290(rotation, shape));
        this.entity = new LocEntity(toolkit, type, shape, rotation, super.level, virtualLevel, this, underwater, animation);
        this.interactive = type.active != LocInteractivity.NONINTERACTIVE && !underwater;
    }

    @OriginalMember(owner = "client!db", name = "a", descriptor = "(Lclient!gp;I)V")
    public void customise(@OriginalArg(0) LocTypeCustomisation customisation) {
        this.entity.customise(customisation);
    }

    @OriginalMember(owner = "client!db", name = "c", descriptor = "(I)I")
    @Override
    public int getRotation() {
        return this.entity.rotation;
    }

    @OriginalMember(owner = "client!db", name = "b", descriptor = "(Lclient!ha;I)V")
    @Override
    public void addShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.addShadow(toolkit);
    }

    @OriginalMember(owner = "client!db", name = "a", descriptor = "(I)I")
    @Override
    public int getId() {
        return this.entity.id;
    }

    @OriginalMember(owner = "client!db", name = "j", descriptor = "(I)V")
    @Override
    public void stopSharingLight(@OriginalArg(0) int arg0) {
        if (arg0 != 27811) {
            this.picked(-29, 50, false, null);
        }
        throw new IllegalStateException();
    }

    @OriginalMember(owner = "client!db", name = "i", descriptor = "(I)Z")
    @Override
    public boolean method9290(@OriginalArg(0) int arg0) {
        return arg0 != 0;
    }

    @OriginalMember(owner = "client!db", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit arg0) {
        @Pc(14) Model model = this.entity.model(arg0, false, true, true, 0x800);
        if (model == null) {
            return null;
        }

        @Pc(22) Matrix matrix = arg0.scratchMatrix();
        matrix.applyTranslation(super.x, super.y, super.z);
        @Pc(36) PickableEntity local36 = Static642.allocatePickableEntity(this.interactive, 1);

        @Pc(49) int localX = super.x >> 9;
        @Pc(54) int localZ = super.z >> 9;
        this.entity.method7681(localZ, model, true, -9827, arg0, localX, localX, localZ, matrix);

        if (OrthoMode.enabled) {
            model.renderOrtho(matrix, local36.pickingCylinders[0], OrthoMode.renderZoom, 0);
        } else {
            model.render(matrix, local36.pickingCylinders[0], 0);
        }

        if (this.entity.particleSystem != null) {
            @Pc(100) ParticleList particles = this.entity.particleSystem.getList();

            if (OrthoMode.enabled) {
                arg0.renderOrtho(particles, OrthoMode.renderZoom);
            } else {
                arg0.render(particles);
            }
        }

        this.transparent = model.F() || this.entity.particleSystem != null;

        if (this.cylinder == null) {
            this.cylinder = BoundingCylinder.create(super.y, super.x, model, super.z);
        } else {
            BoundingCylinder.update(model, super.z, super.y, super.x, this.cylinder);
        }

        return local36;
    }

    @OriginalMember(owner = "client!db", name = "h", descriptor = "(I)Z")
    @Override
    public boolean isTransparent(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            Static98.anIntArray176 = null;
        }
        return this.transparent;
    }

    @OriginalMember(owner = "client!db", name = "a", descriptor = "(IZLclient!ha;IBILclient!eo;)V")
    @Override
    public void shareLight(@OriginalArg(0) int arg0, @OriginalArg(1) boolean arg1, @OriginalArg(2) Toolkit arg2, @OriginalArg(3) int arg3, @OriginalArg(4) byte arg4, @OriginalArg(5) int arg5, @OriginalArg(6) Entity arg6) {
        if (arg4 >= 101) {
            throw new IllegalStateException();
        }
    }

    @OriginalMember(owner = "client!db", name = "a", descriptor = "(IIZLclient!ha;)Z")
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

    @OriginalMember(owner = "client!db", name = "c", descriptor = "(B)I")
    @Override
    public int getSphereRadius(@OriginalArg(0) byte arg0) {
        if (arg0 != -21) {
            this.getSphereRadius((byte) -62);
        }
        return this.entity.getSphereRadius(true);
    }

    @OriginalMember(owner = "client!db", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1) {
        if (arg1 != -5) {
            return;
        }
        @Pc(17) Model local17 = this.entity.model(arg0, true, true, true, 0x40000);
        if (local17 == null) {
            return;
        }
        @Pc(24) int local24 = super.x >> 9;
        @Pc(29) int local29 = super.z >> 9;
        @Pc(32) Matrix local32 = arg0.scratchMatrix();
        local32.applyTranslation(super.x, super.y, super.z);
        this.entity.method7681(local29, local17, false, -9827, arg0, local24, local24, local29, local32);
    }

    @OriginalMember(owner = "client!db", name = "a", descriptor = "(Lclient!ha;I)V")
    @Override
    public void removeShadow(@OriginalArg(0) Toolkit toolkit) {
        this.entity.removeShadow(toolkit);
    }

    @OriginalMember(owner = "client!db", name = "d", descriptor = "(I)V")
    @Override
    public void method6856() {
    }

    @OriginalMember(owner = "client!db", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isStationary() {
        return false;
    }

    @OriginalMember(owner = "client!db", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 >= -93) {
            this.cylinder = null;
        }
        return this.cylinder;
    }

    @OriginalMember(owner = "client!db", name = "e", descriptor = "(I)Z")
    @Override
    public boolean hardShadow() {
        return this.entity.hardShadow();
    }

    @OriginalMember(owner = "client!db", name = "k", descriptor = "(I)I")
    @Override
    public int getMinY(@OriginalArg(0) int arg0) {
        return arg0 == 2 ? this.entity.getMinY() : 103;
    }

    @OriginalMember(owner = "client!db", name = "b", descriptor = "(I)I")
    @Override
    public int getShape() {
        return this.entity.shape;
    }
}
