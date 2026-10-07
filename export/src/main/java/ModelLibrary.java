import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The models every type shares, one glTF file for each model of the models archive in one
 * directory, as {@link LibraryModelToGltf} writes it.
 *
 * <p>A type names its models by id, as the client's types do, and an engine builds the type's
 * model from them. So a model that many types use, recoloured for each, is held once. A model is
 * written once, under its id, and a model that is already in the library is left as it is, so an
 * edited model survives a re-export.
 *
 * <p>The file's one node wears the mesh and the skin of its labels, and its {@code extras} carry
 * {@code maxVertex}, how many of the client's vertices come before those that only a texture space
 * or a particle names, the client's own top and bottom of the model, {@code minY} and {@code maxY}
 * over the vertices before {@code maxVertex}, which the bend of a location measures it by, and its emitters, effectors and billboards, as
 * {@link ParticleSources} and {@link BillboardSources} describe them.
 */
public final class ModelLibrary {

    /**
     * The version of the format of a model file, which its {@code asset} carries.
     */
    public static final int VERSION = 1;

    private static final int MATRIX_FLOATS = 16;
    private static final int[] DIAGONAL = {0, 5, 10, 15};

    private final ClientModelReader reader;
    private final TextureLibrary textures;
    private final Path directory;

    public ModelLibrary(ClientModelReader reader, TextureLibrary textures, Path directory) {
        this.reader = reader;
        this.textures = textures;
        this.directory = directory;
    }

    /**
     * The directory the library is kept in unless told another, under the export module's build
     * directory.
     */
    public static Path defaultDirectory() {
        return Path.of("build", "models");
    }

    /**
     * Where a model's file is, written if the library does not hold it yet, or nothing where the
     * cache holds no such model or the client draws none of its faces.
     */
    public Optional<Path> file(int id) {
        var file = directory.resolve(id + ".gltf");
        if (Files.exists(file)) {
            return Optional.of(file);
        } else {
            return write(id, file).map(ignored -> file);
        }
    }

    /**
     * Writes one model's file, or nothing where the cache holds no such model or the client draws
     * none of its faces.
     */
    public Optional<LibraryModelToGltf.Result> write(int id, Path file) {
        var read = reader.readLabelled(id);
        if (read.isEmpty()) {
            return Optional.empty();
        }

        var model = read.get();
        var gltf = new GltfBuilder();
        gltf.formatVersion(VERSION);
        var materials = new GltfMaterials(gltf, reader.textures(), textures, file);
        var result = LibraryModelToGltf.convertInto(gltf, materials, model);
        if (gltf.empty()) {
            return Optional.empty();
        }

        var name = "model " + id;
        var joints = new ArrayList<Integer>();
        for (var label : result.labels()) {
            joints.add(gltf.node(Map.of("name", label == -1 ? "unlabelled" : "label " + label)));
        }
        var identities = new float[MATRIX_FLOATS * joints.size()];
        for (var joint = 0; joint < joints.size(); joint++) {
            for (var diagonal : DIAGONAL) {
                identities[joint * MATRIX_FLOATS + diagonal] = 1.0F;
            }
        }
        var skin = gltf.skin(joints, gltf.animationData(identities, "MAT4", MATRIX_FLOATS, false), Map.of());

        var node = new LinkedHashMap<String, Object>();
        node.put("name", name);
        node.put("mesh", gltf.mesh(name));
        node.put("skin", skin);
        node.put("children", joints);
        node.put("extras", extras(model));
        var document = gltf.document(List.of(gltf.node(node)), name, Map.of());

        try {
            GltfFile.write(file, document, gltf.bin());
        } catch (IOException failure) {
            throw new UncheckedIOException("Could not write model " + id + " to " + file, failure);
        }
        return Optional.of(result);
    }

    private static Map<String, Object> extras(JavaModel model) {
        var extras = new LinkedHashMap<String, Object>();
        extras.put("maxVertex", model.maxVertex);
        extras.put("minY", model.fa());
        extras.put("maxY", model.EA());
        if (model.emitters != null) {
            extras.put("emitters", ParticleSources.emitters(model));
        }
        if (model.effectors != null) {
            extras.put("effectors", ParticleSources.effectors(model));
        }
        if (model.billboardFaces != null) {
            extras.put("billboards", BillboardSources.billboards(model));
        }
        return extras;
    }
}
