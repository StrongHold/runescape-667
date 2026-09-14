import com.jagex.game.Location;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static458 {

    @OriginalMember(owner = "client!oi", name = "o", descriptor = "Lclient!hc;")
    public static final CutsceneActionType LOC_ADD = new CutsceneActionType(20);

    @OriginalMember(owner = "client!oi", name = "a", descriptor = "(Z[[[Lclient!pha;)V")
    public static void uploadLocationModels(@OriginalArg(1) Tile[][][] tiles) {
        for (@Pc(12) int level = 0; level < tiles.length; level++) {
            @Pc(17) Tile[][] levelTiles = tiles[level];
            for (@Pc(19) int x = 0; x < levelTiles.length; x++) {
                for (@Pc(22) int z = 0; z < levelTiles[x].length; z++) {
                    @Pc(29) Tile tile = levelTiles[x][z];
                    if (tile != null) {
                        if (tile.groundDecor instanceof Location) {
                            ((Location) tile.groundDecor).method6856();
                        }
                        if (tile.wallDecor instanceof Location) {
                            tile.wallDecor.method6856();
                        }
                        if (tile.wallDecor2 instanceof Location) {
                            tile.wallDecor2.method6856();
                        }
                        if (tile.wall instanceof Location) {
                            ((Location) tile.wall).method6856();
                        }
                        if (tile.adjacentWall instanceof Location) {
                            ((Location) tile.adjacentWall).method6856();
                        }
                        for (@Pc(91) PositionEntityNode node = tile.head; node != null; node = node.node) {
                            @Pc(95) PositionEntity entity = node.entity;
                            if (entity instanceof Location) {
                                ((Location) entity).method6856();
                            }
                        }
                    }
                }
            }
        }
    }
}
