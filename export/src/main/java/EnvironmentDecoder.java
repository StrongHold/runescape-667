import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.lighttype.LightTypeList;
import com.jagex.math.ColourUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reads what follows the tiles in a map square's file: how the map square is lit, what fog and
 * sky it has, and the lights placed on it.
 *
 * <p>The client reads this in {@code MapRegion.decodeStaticEnvironment}, and on the software
 * toolkit it throws most of it away: the sun, the ambient light and the fog are only kept when
 * the light detail option is on and the toolkit has lights, which the software one has not, and
 * a light is made through the toolkit, which returns none. So it is read here again, byte for
 * byte as the client reads it, and the reading must end exactly where the data ends, or the
 * layout here has drifted from the client's and the export fails.
 *
 * <p>Every number is as the file holds it. The sun's direction is in the client's frame, and a
 * light's position is in the client's units from the map square's south west corner, with its y
 * the ground's height at its tile less the height the file gives it, as the client places it.
 */
public final class EnvironmentDecoder {

    private static final int LIGHTING = 0;
    private static final int LIGHTS = 1;
    private static final int BLOOM = 2;
    private static final int SKY_BOX = 128;
    private static final int CAMERA_HEIGHTS = 129;

    private static final int LEVELS = 4;
    private static final int CAMERA_HEIGHT_STEPS = 16;
    private static final int CAMERA_HEIGHTS_CLEARED = 0;
    private static final int CAMERA_HEIGHTS_GIVEN = 1;
    private static final int CAMERA_HEIGHTS_AS_BELOW = 2;
    private static final int LIGHT_SHIFT = 2;
    private static final int TILE_SHIFT = 9;
    private static final int LIGHT_TYPE_PRESET = 31;

    /** The defaults the client gives an environment that says nothing about itself. */
    private static final int DEFAULT_SUN_COLOUR = 0xFFFFFF;
    private static final float DEFAULT_AMBIENT = 1.1523438F;
    private static final float DEFAULT_SUN_INTENSITY = 0.69921875F;
    private static final float DEFAULT_REVERSE_SUN_INTENSITY = 1.2F;
    private static final int DEFAULT_SUN_X = -50;
    private static final int DEFAULT_SUN_Y = -60;
    private static final int DEFAULT_SUN_Z = -50;
    private static final int DEFAULT_FOG_COLOUR = 13156520;
    private static final float DEFAULT_BLOOM_WHITE_POINT = 1.0F;
    private static final float DEFAULT_BLOOM_STRENGTH = 0.25F;
    private static final float DEFAULT_BLOOM_THRESHOLD = 1.0F;

    /**
     * How a map square is lit and what surrounds it.
     *
     * @param sun the direction the sun's light comes from, in the client's frame, and its colour
     *     and strengths: {@code sunIntensity} for faces that look at it and
     *     {@code reverseSunIntensity} for those that look away.
     * @param ambient how much light every face gets whichever way it looks, as a factor.
     * @param fogRange how far before the far plane the fog starts, in the file's units, which the
     *     hardware toolkits take as {@code (fogRange + 256) * 4} of the client's units.
     * @param bloom the GL toolkit's bloom, which the D3D toolkit leaves undrawn.
     * @param skyBox the sky box, where the file names one.
     * @param cubeMap the six textures of the reflection cube map, where the file names them.
     * @param cameraHeights for each level, what the camera is kept above across the map square,
     *     as a 16 by 16 grid of one value for each four tiles square, in steps of 32 of the
     *     client's units, or null for a level the file gives none, which the client takes as 0.
     *     The client reads it in {@code MapRegion.decodeStaticEnvironment} and its camera keeps
     *     its pitch above what stands around its focus by it ({@code Static723.clampPlayerCamera}).
     */
    public record Environment(int[] sun, int sunColour, float sunIntensity, float reverseSunIntensity, float ambient,
                              int fogColour, int fogRange, Bloom bloom, Optional<SkyBox> skyBox, Optional<int[]> cubeMap,
                              List<Light> lights, int[][][] cameraHeights) {
    }

    /**
     * How the GL toolkit makes the bright parts of the scene glow, as {@code Toolkit.setBloomParams}
     * hands it to its bloom pass ({@code GlBloomFilter}).
     *
     * @param whitePoint the luminance the tone map takes to white: the combining shader scales a
     *     pixel by {@code l * (1 + l / whitePoint) / (l + 1)} over its luminance {@code l}.
     * @param strength how much of the blurred glow is added over the scene.
     * @param threshold the luminance below which a pixel adds nothing to the glow.
     */
    public record Bloom(float whitePoint, float strength, float threshold) {
    }

    public record SkyBox(int id, int sphereOffsetX, int sphereOffsetY, int sphereOffsetZ, int rotation) {
    }

    /**
     * One light placed on the map square.
     *
     * @param level the level it lights.
     * @param spansLevelsAbove whether it also lights the levels above, and {@code spansLevelsBelow}
     *     those below.
     * @param x where it is, in the client's units from the map square's south west corner.
     * @param y its height, in the client's units with y down, as the client places it: the
     *     ground's height at its tile less the height the file gives it.
     * @param radius how far it reaches, in tiles.
     * @param rowSpans for each row of tiles across its reach, which tiles it lights, as the file
     *     packs them: the first tile in the high byte and how many in the low.
     * @param colour its colour, as the client's palette gives it.
     * @param phase where in its flicker it starts, out of 2048.
     * @param preset which of the client's flickers it uses, or 31 for one named by a light type.
     * @param ambient the least of its flicker, and {@code pattern}, {@code amplitude} and
     *     {@code frequency} the rest of it, as the preset or the light type gives them.
     */
    public record Light(int level, boolean spansLevelsAbove, boolean spansLevelsBelow, int x, int y, int z, int radius,
                        int[] rowSpans, int colour, int phase, int preset, int lightType, int ambient, int pattern,
                        int amplitude, int frequency) {
    }

    /**
     * Reads the environment from where the packet stands to the end of its data.
     *
     * @param tileHeights the heights of the region's tile corners by level, which a light's
     *     height is measured from.
     * @param originX where the map square starts in the region, in tiles.
     */
    public static Environment decode(Packet packet, LightTypeList lightTypes, int[][][] tileHeights, int originX,
                                     int originZ) {
        var sun = new int[] {DEFAULT_SUN_X, DEFAULT_SUN_Y, DEFAULT_SUN_Z};
        var sunColour = DEFAULT_SUN_COLOUR;
        var sunIntensity = DEFAULT_SUN_INTENSITY;
        var reverseSunIntensity = DEFAULT_REVERSE_SUN_INTENSITY;
        var ambient = DEFAULT_AMBIENT;
        var fogColour = DEFAULT_FOG_COLOUR;
        var fogRange = 0;
        var bloom = new Bloom(DEFAULT_BLOOM_WHITE_POINT, DEFAULT_BLOOM_STRENGTH, DEFAULT_BLOOM_THRESHOLD);
        Optional<SkyBox> skyBox = Optional.empty();
        Optional<int[]> cubeMap = Optional.empty();
        var lights = new ArrayList<Light>();
        var cameraHeights = new int[LEVELS][][];

        while (packet.pos < packet.data.length) {
            var code = packet.g1();
            if (code == LIGHTING) {
                var flags = packet.g1();
                sunColour = (flags & 0x1) == 0 ? DEFAULT_SUN_COLOUR : packet.g4();
                ambient = (flags & 0x2) == 0 ? DEFAULT_AMBIENT : packet.g2() / 256.0F;
                sunIntensity = (flags & 0x4) == 0 ? DEFAULT_SUN_INTENSITY : packet.g2() / 256.0F;
                reverseSunIntensity = (flags & 0x8) == 0 ? DEFAULT_REVERSE_SUN_INTENSITY : packet.g2() / 256.0F;
                if ((flags & 0x10) == 0) {
                    sun = new int[] {DEFAULT_SUN_X, DEFAULT_SUN_Y, DEFAULT_SUN_Z};
                } else {
                    sun = new int[] {packet.g2s(), packet.g2s(), packet.g2s()};
                }
                fogColour = (flags & 0x20) == 0 ? DEFAULT_FOG_COLOUR : packet.g4();
                fogRange = (flags & 0x40) == 0 ? 0 : packet.g2();
                if ((flags & 0x80) == 0) {
                    cubeMap = Optional.empty();
                } else {
                    cubeMap = Optional.of(new int[] {packet.g2(), packet.g2(), packet.g2(), packet.g2(), packet.g2(), packet.g2()});
                }
            } else if (code == LIGHTS) {
                var count = packet.g1();
                for (var i = 0; i < count; i++) {
                    lights.add(light(packet, lightTypes, tileHeights, originX, originZ));
                }
            } else if (code == BLOOM) {
                bloom = new Bloom(packet.g1() * 8 / 255.0F, packet.g1() * 8 / 255.0F, packet.g1() * 8 / 255.0F);
            } else if (code == SKY_BOX) {
                skyBox = Optional.of(new SkyBox(packet.g2(), packet.g2s(), packet.g2s(), packet.g2s(), packet.g2()));
            } else if (code == CAMERA_HEIGHTS) {
                for (var level = 0; level < LEVELS; level++) {
                    var mode = packet.g1b();
                    if (mode == CAMERA_HEIGHTS_CLEARED) {
                        cameraHeights[level] = null;
                    } else if (mode == CAMERA_HEIGHTS_GIVEN) {
                        cameraHeights[level] = cameraHeightGrid(packet);
                    } else if (mode == CAMERA_HEIGHTS_AS_BELOW) {
                        cameraHeights[level] = level == 0 || cameraHeights[level - 1] == null ? null
                            : cameraHeights[level - 1].clone();
                    } else {
                        throw new IllegalStateException("The camera heights have a mode the client does not read: " + mode);
                    }
                }
            } else {
                throw new IllegalStateException("The environment has a code the client does not read: " + code);
            }
        }

        if (packet.pos != packet.data.length) {
            throw new IllegalStateException("The environment reads " + (packet.pos - packet.data.length)
                + " bytes past the end of the map square's file");
        }
        return new Environment(sun, sunColour, sunIntensity, reverseSunIntensity, ambient, fogColour, fogRange, bloom,
            skyBox, cubeMap, List.copyOf(lights), cameraHeights);
    }

    /** One level's camera heights as the file packs them: a value for each four tiles square, x then z, unsigned. */
    private static int[][] cameraHeightGrid(Packet packet) {
        var grid = new int[CAMERA_HEIGHT_STEPS][CAMERA_HEIGHT_STEPS];
        for (var x = 0; x < CAMERA_HEIGHT_STEPS; x++) {
            for (var z = 0; z < CAMERA_HEIGHT_STEPS; z++) {
                grid[x][z] = packet.g1b() & 0xFF;
            }
        }
        return grid;
    }

    private static Light light(Packet packet, LightTypeList lightTypes, int[][][] tileHeights, int originX, int originZ) {
        var packedLevel = packet.g1();
        var spansLevelsBelow = (packedLevel & 0x10) != 0;
        var spansLevelsAbove = (packedLevel & 0x8) != 0;
        var level = packedLevel & 0x7;
        var x = packet.g2() << LIGHT_SHIFT;
        var z = packet.g2() << LIGHT_SHIFT;
        var height = packet.g2() << LIGHT_SHIFT;
        var radius = packet.g1();
        var rowSpans = new int[radius * 2 + 1];
        for (var row = 0; row < rowSpans.length; row++) {
            rowSpans[row] = packet.g2();
        }
        var colour = ColourUtils.HSL_TO_RGB[packet.g2()];
        var packed = packet.g1();
        var phase = (packed & 0xE0) << 3;
        var preset = packed & 0x1F;

        var flicker = new EnvironmentLightFlicker(preset);
        var lightType = -1;
        if (preset == LIGHT_TYPE_PRESET) {
            lightType = packet.g2();
            var type = lightTypes.list(lightType);
            flicker = new EnvironmentLightFlicker(type.ambient, type.pattern, type.amplitude, type.frequency);
        }

        var tileX = originX + (x >> TILE_SHIFT);
        var tileZ = originZ + (z >> TILE_SHIFT);
        var y = tileHeights[level][tileX][tileZ] - height;
        return new Light(level, spansLevelsAbove, spansLevelsBelow, x, y, z, radius, rowSpans, colour, phase, preset,
            lightType, flicker.ambient(), flicker.pattern(), flicker.amplitude(), flicker.frequency());
    }

    private EnvironmentDecoder() {
        /* empty */
    }
}
