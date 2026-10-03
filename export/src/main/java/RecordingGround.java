import com.jagex.graphics.Ground;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Shadow;

import java.util.HashMap;
import java.util.Map;

/**
 * A ground that stands in for the client's own while the terrain is loaded, and keeps the HSL
 * colour of every vertex of every tile as the terrain hands it over, before the software ground
 * turns it into RGB in place. The GL toolkit colours a vertex from that HSL, scaling its
 * lightness by the light on the tile before the palette's gamma, and an engine that wants the
 * GL look needs the HSL to do the same.
 *
 * <p>Every call is passed on to the real ground, so the client builds exactly what it would have.
 */
public final class RecordingGround extends Ground {

    private final Ground real;
    private final Map<Long, int[]> hslByTile = new HashMap<>();

    public RecordingGround(Ground real) {
        super(real.sizeX, real.sizeZ, real.tileSize, real.tileHeights);
        this.real = real;
    }

    public Ground real() {
        return real;
    }

    /**
     * The HSL colour of each vertex of a tile, in the order the ground numbers its vertices, or
     * nothing for a tile the terrain never handed over.
     */
    public int[] hsl(int x, int z) {
        return hslByTile.get(key(x, z));
    }

    private static long key(int x, int z) {
        return (long) x << 32 | (z & 0xFFFFFFFFL);
    }

    @Override
    public void U(int x, int z, int[] offsetX, int[] offsetLevel, int[] offsetY, int[] waterDepths,
                  int[] blendedColours, int[] overlayBlendColours, int[] blendedTextures, int[] blendedSizes,
                  int waterColour, int waterDepth, int waterBias, boolean allowShadow) {
        hslByTile.put(key(x, z), blendedColours.clone());
        real.U(x, z, offsetX, offsetLevel, offsetY, waterDepths, blendedColours, overlayBlendColours, blendedTextures,
            blendedSizes, waterColour, waterDepth, waterBias, allowShadow);
    }

    @Override
    public void addTile(int x, int z, int[] offsetX, int[] offsetLevel, int[] offsetY, int[] depths, int[] faceA,
                        int[] faceB, int[] faceC, int[] colours, int[] blendedColours, int[] textures, int[] sizes,
                        int waterColour, int waterDepth, int waterBias) {
        real.addTile(x, z, offsetX, offsetLevel, offsetY, depths, faceA, faceB, faceC, colours, blendedColours,
            textures, sizes, waterColour, waterDepth, waterBias);
    }

    @Override
    public void wa(Shadow shadow, int arg1, int arg2, int arg3, int arg4, boolean arg5) {
        real.wa(shadow, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public void CA(Shadow shadow, int arg1, int arg2, int arg3, int arg4, boolean arg5) {
        real.CA(shadow, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public void method7868(PointLight light, int[] arg1) {
        real.method7868(light, arg1);
    }

    @Override
    public void renderTiles(int arg0, int arg1, int arg2, boolean[][] arg3, boolean arg4, int arg5) {
        real.renderTiles(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public void drawMinimap(int x1, int y1, int x2, int y2, boolean[][] visibility) {
        real.drawMinimap(x1, y1, x2, y2, visibility);
    }

    @Override
    public boolean method7874(Shadow shadow, int arg1, int arg2, int arg3) {
        return real.method7874(shadow, arg1, arg2, arg3);
    }

    @Override
    public void ka(int arg0, int arg1, int arg2) {
        real.ka(arg0, arg1, arg2);
    }

    @Override
    public Shadow fa(int arg0, int arg1, Shadow shadow) {
        return real.fa(arg0, arg1, shadow);
    }

    @Override
    public void YA() {
        real.YA();
    }

    @Override
    public void renderTile(int arg0, int arg1) {
        real.renderTile(arg0, arg1);
    }

    @Override
    public void renderTilesAtDepth(int arg0, int arg1, int arg2, boolean[][] arg3, boolean arg4, int arg5, int arg6) {
        real.renderTilesAtDepth(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }
}
