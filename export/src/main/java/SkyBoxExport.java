import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import com.jagex.core.constants.ModeGame;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereType;
import com.jagex.game.runetek6.config.skyboxspheretype.SkyBoxSphereTypeList;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxType;
import com.jagex.game.runetek6.config.skyboxtype.SkyBoxTypeList;
import com.jagex.js5.Js5Archive;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Writes every sky box type: what the client draws behind the scene of a map square whose
 * environment names one ({@code Environment.decodeSkyBox}).
 *
 * <p>Each type is decoded with the client's own type lists ({@code SkyBoxTypeList.list} and
 * {@code SkyBoxSphereTypeList.list}), and its spheres are written inside it, as only sky boxes use
 * them ({@code SkyBoxTypeList.skyBox}). A texture or a model that a type names must be in the cache,
 * or the export stops.
 */
public final class SkyBoxExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out = Path.of("build", "skyboxes.json");

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    private static final int LANGUAGE = 0;

    /**
     * What the client keeps in a field that names nothing: no texture, no model, no light.
     */
    private static final int NONE = -1;

    /**
     * What a sphere draws ({@code SkyBoxSphere.buildSprite}): a texture as a sprite, a lit textured
     * sphere, or a lit model.
     */
    private static final int SPHERE_MODEL = 2;

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportSkyBoxes", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var cache = args.where.cache();
        var config = Cache.js5(cache, Js5Archive.CONFIG);
        var skyBoxes = new SkyBoxTypeList(ModeGame.RUNESCAPE, LANGUAGE, config);
        var sphereTypes = new SkyBoxSphereTypeList(ModeGame.RUNESCAPE, LANGUAGE, config);
        var textureSource = new Js5TextureSource(
            Cache.js5(cache, Js5Archive.MATERIALS),
            Cache.js5(cache, Js5Archive.TEXTURES),
            Cache.js5(cache, Js5Archive.SPRITES));
        var models = Set.of(boxed(Cache.groupsOf(Cache.index(cache, Js5Archive.MODELS))));

        var types = new LinkedHashMap<String, Object>();
        var missing = new TreeSet<String>();
        var meshes = new TreeSet<Integer>();
        var limit = config.fileLimit(Js5ConfigGroup.SKYBOXTYPE);
        for (var id = 0; id < limit; id++) {
            if (config.getfile(id, Js5ConfigGroup.SKYBOXTYPE) != null) {
                var type = skyBoxes.list(id);
                var spheres = new ArrayList<Map<String, Object>>();
                for (var sphereId : type.sphereIds == null ? new int[0] : type.sphereIds) {
                    var sphere = sphereTypes.list(sphereId);
                    spheres.add(sphere(sphere));
                    var held = sphere.renderType == SPHERE_MODEL ? models.contains(sphere.contentId)
                        : isTexture(textureSource, sphere.contentId);
                    if (!held) {
                        missing.add("type " + id + " has a sphere that names " + sphere.contentId);
                    }
                }
                types.put(Integer.toString(id), type(type, spheres));
                if (type.texture != NONE && !isTexture(textureSource, type.texture)) {
                    missing.add("type " + id + " names texture " + type.texture);
                }
                if (type.meshId != NONE) {
                    meshes.add(type.meshId);
                    if (!models.contains(type.meshId)) {
                        missing.add("type " + id + " names model " + type.meshId);
                    }
                }
            }
        }

        if (!missing.isEmpty()) {
            System.out.println("A sky box type names what the cache does not hold: " + String.join(", ", missing));
            System.exit(1);
        }

        var file = new LinkedHashMap<String, Object>();
        file.put("types", types);
        if (args.out.getParent() != null) {
            Files.createDirectories(args.out.getParent());
        }
        Files.writeString(args.out, Json.write(file), StandardCharsets.UTF_8);
        System.out.println("wrote " + types.size() + " sky box types, whose meshes are models " + meshes + ", to "
            + args.out.toAbsolutePath().normalize());
    }

    /**
     * A type's every field, under the client's names, with null for a field that names nothing.
     */
    private static Map<String, Object> type(SkyBoxType type, List<Map<String, Object>> spheres) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("texture", orNull(type.texture));
        fields.put("tileMode", type.tileMode);
        fields.put("meshId", orNull(type.meshId));
        fields.put("lightSphereIndex", orNull(type.lightSphereIndex));
        fields.put("spheres", spheres);
        return fields;
    }

    /**
     * A sphere's every field, under the client's names.
     */
    private static Map<String, Object> sphere(SkyBoxSphereType sphere) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("renderType", sphere.renderType);
        fields.put("contentId", sphere.contentId);
        fields.put("x", sphere.x);
        fields.put("y", sphere.y);
        fields.put("z", sphere.z);
        fields.put("size", sphere.size);
        fields.put("colour", sphere.colour);
        fields.put("infinite", sphere.infinite);
        fields.put("rotateX", sphere.rotateX);
        fields.put("rotateY", sphere.rotateY);
        fields.put("rotateZ", sphere.rotateZ);
        return fields;
    }

    /**
     * Whether the cache holds a texture that can be drawn, as the texture export writes each one.
     */
    private static boolean isTexture(Js5TextureSource source, int id) {
        return id >= 0 && id < source.textureCount() && source.getMetrics(id) != null && source.textureAvailable(id);
    }

    private static Object orNull(int value) {
        return value == NONE ? null : value;
    }

    private static Integer[] boxed(int[] values) {
        var boxed = new Integer[values.length];
        for (var at = 0; at < values.length; at++) {
            boxed[at] = values[at];
        }
        return boxed;
    }
}
