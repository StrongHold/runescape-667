import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;

/**
 * A source of textures that owes nothing to the cache, so that both toolkits can be driven through
 * their texture paths without a cache to read.
 *
 * Every texture is built from its own number, so the same number always gives the same picture and
 * the same metrics, and two toolkits asked the same question are answering about the same texture.
 *
 * The first few numbers are laid out to exercise one thing each. Nought stands still, one slides
 * across, two slides down and not across, and three differs from nought in the two numbers a model
 * compares when it swaps one texture for another.
 *
 * Two of the numbers decide whether a face wearing the texture is drawn with it at all, and both
 * are answered as no here so that a texture always shows. A face whose texture may be turned off
 * is drawn flat, and a face whose texture is marked the other way is dropped from the model
 * entirely, which leaves a model that looks exactly like an untextured one.
 *
 * A blend mode of two says the texture carries an alpha of its own, and a face wearing one is
 * drawn through what is behind it place by place rather than over it. Every third number has one.
 *
 * Every odd number is one the player is allowed to turn off, so a caller that wants a texture it
 * can see whatever the scene asks for wants an even one.
 */
public final class HandTextureSource implements TextureSource {

    /** How many textures this pretends to hold, which is more than the toolkit has room for. */
    public static final int COUNT = 256;

    private static final int SIZE = 128;

    @Override
    public int textureCount() {
        return COUNT;
    }

    @Override
    public boolean textureAvailable(int id) {
        return id >= 0 && id < COUNT;
    }

    @Override
    public TextureMetrics getMetrics(int id) {
        if (!textureAvailable(id)) {
            return null;
        }

        var plain = plainOf(id);

        var metrics = new TextureMetrics();
        metrics.aShort37 = (short) SIZE;
        metrics.alphaBlendMode = plain % 3;
        metrics.effectType = (byte) effectOf(id);
        metrics.effectParam1 = (byte) (plain & 7);
        metrics.effectParam2 = plain * 17;
        metrics.small = (plain & 8) != 0;
        metrics.alpha = (byte) (plain == 3 ? 200 : 96);
        metrics.aByte57 = (byte) (plain == 3 ? 7 : 24);
        metrics.speedU = (byte) (plain == 1 ? 3 : 0);
        metrics.speedV = (byte) (plain == 2 ? 5 : 0);
        /*
         * Whether the player is allowed to turn this texture off. Every other one may be, so that
         * a scene can ask for either kind by the number it names.
         */
        metrics.disableable = (plain & 1) != 0;
        metrics.aBoolean234 = false;
        metrics.aBoolean239 = (plain & 64) != 0;
        metrics.aBoolean236 = (plain & 1) != 0;
        metrics.aBoolean235 = (plain & 2) != 0;
        metrics.aByte53 = (byte) (plain & 15);
        metrics.aBoolean237 = (plain & 4) != 0;
        metrics.aBoolean238 = (plain & 128) != 0;
        metrics.colorOp = plain;
        return metrics;
    }

    /**
     * The numbers whose textures stand for water, one for each of the three effects the client
     * counts as water. A face of the ground above the water wearing one of them is seen through.
     *
     * Each is the plain texture below in every way but the effect it names, and shows the same
     * picture, so a scene laying one of them differs from a scene laying the plain one in the
     * effect alone.
     */
    public static final int WATER_STILL = 240;
    public static final int WATER_MOVING = 242;
    public static final int WATER_DEEP = 244;

    /** The texture the three that stand for water are copied from. */
    private static final int WATER_COPIES = 6;

    /**
     * The number a texture takes everything but its effect from, which for one that stands for
     * water is the plain texture it copies.
     */
    private static int plainOf(int id) {
        if (id == WATER_STILL || id == WATER_MOVING || id == WATER_DEEP) {
            return WATER_COPIES;
        } else {
            return id;
        }
    }

    /**
     * What a texture does to the colour of whatever wears it, which for three of them is stand
     * for water.
     */
    private static int effectOf(int id) {
        if (id == WATER_STILL) {
            return 4;
        } else if (id == WATER_MOVING) {
            return 8;
        } else if (id == WATER_DEEP) {
            return 9;
        } else {
            return id & 3;
        }
    }

    /**
     * A picture with a hard edged pattern and a hole in it, so that a gathered texel differs from
     * the one it came from and an empty texel has something to bleed into.
     */
    @Override
    public int[] rgbOutput(int width, boolean opaque, int height, int id, float brightness) {
        var pixels = new int[SIZE * SIZE];

        for (var down = 0; down < SIZE; down++) {
            for (var across = 0; across < SIZE; across++) {
                pixels[down * SIZE + across] = texel(plainOf(id), across, down);
            }
        }

        return pixels;
    }

    @Override
    public int[] argbOutput(float brightness, int id, int width, int height) {
        return rgbOutput(width, false, height, id, brightness);
    }

    @Override
    public float[] floatArgbOutput(int a, int b, float c, int d) {
        return null;
    }

    private static int texel(int id, int across, int down) {
        var hole = across >= 48 && across < 80 && down >= 48 && down < 80;
        if (hole) {
            return 0;
        }

        var red = (across * 2 + id) & 0xFF;
        var green = (down * 2 + id * 3) & 0xFF;
        var blue = ((across ^ down) + id * 7) & 0xFF;
        var alpha = ((across / 16 + down / 16) & 1) == 0 ? 0xFF : 0x40;
        return alpha << 24 | red << 16 | green << 8 | blue;
    }
}
