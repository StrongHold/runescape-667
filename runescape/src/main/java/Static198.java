import com.jagex.core.constants.LocLayer;
import com.jagex.core.constants.LocShapes;
import com.jagex.game.Location;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static198 {

    // $FF: synthetic field
    @OriginalMember(owner = "client!gca", name = "l", descriptor = "Ljava/lang/Class;")
    public static Class locClass;

    @OriginalMember(owner = "client!gca", name = "e", descriptor = "I")
    public static int anInt3276 = 0;

    @OriginalMember(owner = "client!gca", name = "a", descriptor = "(IIIIIIII)V")
    public static void animateLocation(@OriginalArg(0) int level, @OriginalArg(4) int x, @OriginalArg(1) int z, @OriginalArg(2) int shape, @OriginalArg(5) int rotation, @OriginalArg(7) int layer, @OriginalArg(3) int animation) {
        if (x < 0 || z < 0 || x >= Static720.mapWidth - 1 || z >= Static501.mapLength - 1) {
            return;
        }

        if (Static334.activeTiles == null) {
            return;
        }

        if (layer == LocLayer.WALL) {
            @Pc(52) Location wall = (Location) Static302.getWall(level, x, z);
            @Pc(58) Location adjacentWall = (Location) Static619.method1510(level, x, z);

            if (wall != null && shape != LocShapes.WALL_L) {
                if (wall instanceof DynamicWall) {
                    ((DynamicWall) wall).entity.animate(animation);
                } else {
                    Static235.setLocChange(x, z, level, wall.getId(), shape, rotation, animation, layer);
                }
            }

            if (adjacentWall != null) {
                if (adjacentWall instanceof DynamicWall) {
                    ((DynamicWall) adjacentWall).entity.animate(animation);
                } else {
                    Static235.setLocChange(x, z, level, adjacentWall.getId(), shape, rotation, animation, layer);
                }
            }
        } else if (layer == LocLayer.WALLDECOR) {
            @Pc(52) Location wallDecor = Static114.getWallDecor(level, x, z);

            if (wallDecor != null) {
                if (wallDecor instanceof DynamicWallDecor) {
                    ((DynamicWallDecor) wallDecor).entity.animate(animation);
                } else {
                    @Pc(279) int id = wallDecor.getId();

                    if (shape == LocShapes.WALLDECOR_STRAIGHT_NOOFFSET || shape == LocShapes.WALLDECOR_STRAIGHT_OFFSET) {
                        Static235.setLocChange(x, z, level, id, LocShapes.WALLDECOR_STRAIGHT_NOOFFSET, rotation, animation, layer);
                    } else if (shape == 6) {
                        Static235.setLocChange(x, z, level, id, LocShapes.WALLDECOR_STRAIGHT_NOOFFSET, rotation + 4, animation, layer);
                    } else if (shape == 7) {
                        Static235.setLocChange(x, z, level, id, LocShapes.WALLDECOR_STRAIGHT_NOOFFSET, (rotation + 2 & 0x3) + 4, animation, layer);
                    } else if (shape == 8) {
                        Static235.setLocChange(x, z, level, id, LocShapes.WALLDECOR_STRAIGHT_NOOFFSET, rotation + 4, animation, layer);
                        Static235.setLocChange(x, z, level, id, LocShapes.WALLDECOR_STRAIGHT_NOOFFSET, (rotation + 2 & 0x3) + 4, animation, layer);
                    }
                }
            }
        } else if (layer == LocLayer.GROUND) {
            @Pc(52) Location centrepiece = (Location) Static578.getEntity(level, x, z, locClass == null ? (locClass = getClass("com.jagex.game.Location")) : locClass);

            if (centrepiece != null) {
                if (shape == LocShapes.CENTREPIECE_DIAGONAL) {
                    shape = LocShapes.CENTREPIECE_STRAIGHT;
                }

                if (centrepiece instanceof DynamicLocation) {
                    ((DynamicLocation) centrepiece).entity.animate(animation);
                } else {
                    Static235.setLocChange(x, z, level, centrepiece.getId(), shape, rotation, animation, layer);
                }
            }
        } else if (layer == LocLayer.GROUNDDECOR) {
            @Pc(52) Location groundDecor = (Location) Static687.getGroundDecor(level, x, z);

            if (groundDecor != null) {
                if (groundDecor instanceof DynamicGroundDecor) {
                    ((DynamicGroundDecor) groundDecor).entity.animate(animation);
                } else {
                    Static235.setLocChange(x, z, level, groundDecor.getId(), shape, rotation, animation, layer);
                }
            }
        }
    }

    static Class getClass(String name) {
        Class instance;
        try {
            instance = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw (NoClassDefFoundError) new NoClassDefFoundError().initCause(ex);
        }
        return instance;
    }

    @OriginalMember(owner = "client!gca", name = "a", descriptor = "(IIZ)Z")
    public static boolean method2957(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1) {
        if (Static116.method2142(arg0, arg1)) {
            return (arg0 & 0xB000) != 0 | Static54.method1183(arg1, arg0) | Static194.method2908(arg0, arg1) ? true : (Static139.method2358(arg0, arg1) | Static401.method5575(arg0, arg1)) & (arg1 & 0x37) == 0;
        } else {
            return false;
        }
    }
}
