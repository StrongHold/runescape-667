import com.jagex.PickableEntity;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.graphics.BoundingCylinder;
import com.jagex.graphics.EnvironmentLight;
import com.jagex.graphics.Ground;
import com.jagex.graphics.Matrix;
import com.jagex.graphics.Model;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!sv")
public final class ObjStackEntity extends Class8_Sub2_Sub5 {

    @OriginalMember(owner = "client!sv", name = "ib", descriptor = "I")
    public int firstId;

    @OriginalMember(owner = "client!sv", name = "cb", descriptor = "I")
    public int thirdCount;

    @OriginalMember(owner = "client!sv", name = "Q", descriptor = "I")
    public int firstCount;

    @OriginalMember(owner = "client!sv", name = "S", descriptor = "I")
    public int secondCount;

    @OriginalMember(owner = "client!sv", name = "db", descriptor = "I")
    public int thirdId = -1;

    @OriginalMember(owner = "client!sv", name = "X", descriptor = "Z")
    public boolean transparent = false;

    @OriginalMember(owner = "client!sv", name = "fb", descriptor = "I")
    public int sphereRadius = 0;

    @OriginalMember(owner = "client!sv", name = "kb", descriptor = "I")
    public int secondId = -1;

    @OriginalMember(owner = "client!sv", name = "O", descriptor = "I")
    public int anInt8885 = 0;

    @OriginalMember(owner = "client!sv", name = "<init>", descriptor = "(IIIII)V")
    public ObjStackEntity(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int z, @OriginalArg(3) int level, @OriginalArg(4) int virtualLevel) {
        super(x, y, z, level, virtualLevel);
    }

    @OriginalMember(owner = "client!sv", name = "d", descriptor = "(B)I")
    @Override
    public int getPickSizeShift() {
        @Pc(9) ObjType firstType = ObjTypeList.instance.list(this.firstId);
        @Pc(12) int picksizeshift = firstType.picksizeshift;

        if (this.secondId != -1) {
            @Pc(22) ObjType secondType = ObjTypeList.instance.list(this.secondId);

            if (secondType.picksizeshift > picksizeshift) {
                picksizeshift = secondType.picksizeshift;
            }
        }

        if (this.thirdId != -1) {
            @Pc(48) ObjType thirdType = ObjTypeList.instance.list(this.thirdId);

            if (thirdType.picksizeshift > picksizeshift) {
                picksizeshift = thirdType.picksizeshift;
            }
        }

        return picksizeshift;
    }

    @OriginalMember(owner = "client!sv", name = "h", descriptor = "(I)Z")
    @Override
    public boolean isTransparent(@OriginalArg(0) int arg0) {
        if (arg0 != 0) {
            this.sphereRadius = -87;
        }
        return this.transparent;
    }

    @OriginalMember(owner = "client!sv", name = "c", descriptor = "(B)I")
    @Override
    public int getSphereRadius(@OriginalArg(0) byte arg0) {
        return arg0 == -21 ? this.sphereRadius : -65;
    }

    @OriginalMember(owner = "client!sv", name = "d", descriptor = "(Lclient!ha;I)V")
    @Override
    public void method9289(@OriginalArg(0) Toolkit arg0, @OriginalArg(1) int arg1) {
        if (arg1 == -5) {
            ;
        }
    }

    @OriginalMember(owner = "client!sv", name = "c", descriptor = "(Lclient!ha;I)Lclient!ke;")
    @Override
    public BoundingCylinder getCylinder(@OriginalArg(0) Toolkit toolkit, @OriginalArg(1) int arg1) {
        if (arg1 > -93) {
            this.getCylinder(null, 61);
        }
        return null;
    }

    @OriginalMember(owner = "client!sv", name = "k", descriptor = "(I)I")
    @Override
    public int getMinY(@OriginalArg(0) int arg0) {
        return arg0 == 2 ? -10 : -14;
    }

    @OriginalMember(owner = "client!sv", name = "a", descriptor = "(IIZLclient!ha;)Z")
    @Override
    public boolean picked(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) boolean arg2, @OriginalArg(3) Toolkit toolkit) {
        if (arg2) {
            this.getCylinder(null, 104);
        }

        @Pc(16) Matrix matrix = toolkit.scratchMatrix();
        matrix.applyTranslation(super.x, super.y - 10, super.z);

        @Pc(32) ObjType firstType = ObjTypeList.instance.list(this.firstId);
        @Pc(42) Model model = firstType.model(null, 0x20000, null, this.firstCount, toolkit);
        if (model != null && (OrthoMode.enabled ? model.pickedOrtho(y, x, matrix, true, firstType.picksizeshift, OrthoMode.renderZoom) : model.picked(y, x, matrix, true, firstType.picksizeshift))) {
            return true;
        }

        if (this.secondId != -1) {
            @Pc(82) ObjType secondType = ObjTypeList.instance.list(this.secondId);
            model = secondType.model(null, 0x20000, null, this.secondCount, toolkit);

            if (model != null && (OrthoMode.enabled ? model.pickedOrtho(y, x, matrix, true, secondType.picksizeshift, OrthoMode.renderZoom) : model.picked(y, x, matrix, true, secondType.picksizeshift))) {
                return true;
            }
        }

        if (this.thirdId != -1) {
            @Pc(82) ObjType thirdType = ObjTypeList.instance.list(this.thirdId);
            model = thirdType.model(null, 0x20000, null, this.thirdCount, toolkit);

            if (model != null && (OrthoMode.enabled ? model.pickedOrtho(y, x, matrix, true, thirdType.picksizeshift, OrthoMode.renderZoom) : model.picked(y, x, matrix, true, thirdType.picksizeshift))) {
                return true;
            }
        }

        return false;
    }

    @OriginalMember(owner = "client!sv", name = "b", descriptor = "(B)Z")
    @Override
    public boolean isStationary() {
        return false;
    }

    @OriginalMember(owner = "client!sv", name = "a", descriptor = "(ILclient!ha;)Lclient!pea;")
    @Override
    public PickableEntity render(@OriginalArg(1) Toolkit toolkit) {
        @Pc(17) PositionEntityNode node = Static467.getHead(super.level, super.x >> EnvironmentLight.anInt1066, super.z >> EnvironmentLight.anInt1066);
        @Pc(29) GroundDecor local29 = Static687.getGroundDecor(super.level, super.x >> EnvironmentLight.anInt1066, super.z >> EnvironmentLight.anInt1066);

        @Pc(31) int y = 0;
        if (node != null && node.entity.aBoolean815) {
            y = node.entity.getMinY(2);
        }
        if (local29 != null && -y < local29.offsetY) {
            y = -local29.offsetY;
        }
        if (y != this.anInt8885) {
            super.y -= this.anInt8885;
            this.anInt8885 = y;
            super.y += y;
        }

        @Pc(94) Matrix matrix = toolkit.scratchMatrix();
        matrix.makeIdentity();

        if (this.anInt8885 == 0) {
            @Pc(110) Ground ground = Static246.ground[super.virtualLevel];
            @Pc(115) int local115 = this.sphereRadius << 1;

            @Pc(122) int southWestX = -local115 / 2;
            @Pc(127) int southWestZ = -local115 / 2;
            @Pc(139) int southWestHeight = ground.averageHeight(southWestX + super.x, super.z + southWestZ);

            @Pc(143) int southEastX = local115 / 2;
            @Pc(148) int southEastZ = -local115 / 2;
            @Pc(161) int southEastHeight = ground.averageHeight(southEastX + super.x, super.z + southEastZ);

            @Pc(166) int northWestX = -local115 / 2;
            @Pc(170) int northWestZ = local115 / 2;
            @Pc(182) int northWestHeight = ground.averageHeight(super.x + northWestX, northWestZ + super.z);

            @Pc(186) int northEastX = local115 / 2;
            @Pc(190) int northEastZ = local115 / 2;
            @Pc(203) int northEastHeight = ground.averageHeight(northEastX + super.x, super.z + northEastZ);

            @Pc(215) int southHeightMin = southWestHeight < southEastHeight ? southWestHeight : southEastHeight;
            @Pc(223) int northHeightMin = northWestHeight < northEastHeight ? northWestHeight : northEastHeight;
            @Pc(235) int eastHeightMin = southEastHeight < northEastHeight ? southEastHeight : northEastHeight;
            @Pc(243) int westHeightMin = northWestHeight <= southWestHeight ? northWestHeight : southWestHeight;

            if (local115 != 0) {
                @Pc(259) int rotateX = (int) (Math.atan2(southHeightMin - northHeightMin, local115) * 2607.5945876176133D) & 0x3FFF;

                if (rotateX != 0) {
                    matrix.rotateAxisX(rotateX);
                }
            }

            if (local115 != 0) {
                @Pc(285) int rotateZ = (int) (Math.atan2(westHeightMin - eastHeightMin, local115) * 2607.5945876176133D) & 0x3FFF;

                if (rotateZ != 0) {
                    matrix.rotateAxisZ(-rotateZ);
                }
            }

            @Pc(297) int translateY = northEastHeight + southWestHeight;
            if (northWestHeight + southEastHeight < translateY) {
                translateY = southEastHeight + northWestHeight;
            }

            translateY = (translateY >> 1) - super.y;

            if (translateY != 0) {
                matrix.translate(0, translateY, 0);
            }
        }

        matrix.translate(super.x, super.y - 10, super.z);
        @Pc(345) PickableEntity local345 = Static642.allocatePickableEntity(true, 3);
        this.sphereRadius = 0;
        this.transparent = false;

        @Pc(369) Model model;
        if (this.thirdId != -1) {
            model = ObjTypeList.instance.list(this.thirdId).model(null, 0x800, null, this.thirdCount, toolkit);

            if (model != null) {
                if (OrthoMode.enabled) {
                    model.renderOrtho(matrix, local345.pickingCylinders[2], OrthoMode.renderZoom, 0);
                } else {
                    model.render(matrix, local345.pickingCylinders[2], 0);
                }

                this.transparent |= model.F();
                this.sphereRadius = model.ma();
            }
        }

        if (this.secondId != -1) {
            model = ObjTypeList.instance.list(this.secondId).model(null, 0x800, null, this.secondCount, toolkit);

            if (model != null) {
                if (OrthoMode.enabled) {
                    model.renderOrtho(matrix, local345.pickingCylinders[1], OrthoMode.renderZoom, 0);
                } else {
                    model.render(matrix, local345.pickingCylinders[1], 0);
                }

                this.transparent |= model.F();

                if (model.ma() > this.sphereRadius) {
                    this.sphereRadius = model.ma();
                }
            }
        }

        model = ObjTypeList.instance.list(this.firstId).model(null, 0x800, null, this.firstCount, toolkit);

        if (model != null) {
            if (OrthoMode.enabled) {
                model.renderOrtho(matrix, local345.pickingCylinders[0], OrthoMode.renderZoom, 0);
            } else {
                model.render(matrix, local345.pickingCylinders[0], 0);
            }

            this.transparent |= model.F();

            if (model.ma() > this.sphereRadius) {
                this.sphereRadius = model.ma();
            }
        }

        return local345;
    }
}
