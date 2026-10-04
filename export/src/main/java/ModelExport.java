import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

import java.nio.file.Path;
import java.util.List;

/**
 * Writes one model out of the cache as a glTF file with its buffer beside it, which Godot, Blender, three.js and most
 * other engines and tools import as it is.
 */
public final class ModelExport {

    public static final class Args implements Arguments {

        @ParametersDelegate
        private final CacheArgs where = new CacheArgs();

        @ParametersDelegate
        private final TextureArgs textures = new TextureArgs();

        @Parameter(names = "--model", description = "Which model to write, by its group in the models archive", required = true)
        private int model;

        @Parameter(
            names = "--out",
            description = "The file to write, relative to the export module when not absolute"
        )
        private Path out;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("exportModel", new Args(), arguments);

        if (parsed.isPresent()) {
            export(parsed.get());
        }
    }

    private static void export(Args args) throws Exception {
        var reader = new ClientModelReader(args.where.cache());
        var model = reader.read(args.model)
            .orElseThrow(() -> new IllegalStateException("The cache holds no model " + args.model + "."));

        var out = args.out == null ? Path.of("build", "models", args.model + ".gltf") : args.out;
        var gltf = new GltfBuilder();
        var materials = new GltfMaterials(gltf, reader.textures(), args.textures.library(reader.textures()), out);
        var result = ModelToGltf.convert(model, gltf, materials, List.of());
        if (gltf.empty()) {
            throw new IllegalStateException("Model " + args.model + " has no face the client draws.");
        }

        var name = "model " + args.model;
        GltfFile.write(out, gltf.document(name), gltf.bin());

        System.out.println("wrote " + out.toAbsolutePath().normalize());
        System.out.println("  " + result.faces() + " faces in " + result.primitives() + " primitives");
        for (var skip : result.skipped().entrySet()) {
            System.out.println("  " + skip.getValue() + " faces left out, " + skip.getKey());
        }
    }

    private ModelExport() {
        /* empty */
    }
}
