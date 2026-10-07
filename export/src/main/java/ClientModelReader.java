import com.jagex.game.runetek6.config.billboardtype.BillboardTypeList;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.TextureSource;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import java.io.File;
import java.util.Optional;

/**
 * Builds models out of the cache the way the client builds one it is about to draw.
 *
 * The client reads a mesh, brings an old one up to the current scale, and hands it to its
 * toolkit, which works out what each face is drawn with. The software toolkit does that here
 * because it needs no window. It is given the client's own texture source, so a textured face
 * gets its texture coordinates and the texture's metrics, as it does in the client.
 */
public final class ClientModelReader {

    /**
     * Meshes older than this hold their vertices at a quarter of the scale of the world, and the
     * client multiplies them up before it builds a model from them.
     */
    private static final int FIRST_FULL_SCALE_VERSION = 13;

    /**
     * Asks the toolkit for nothing beyond the model as it is held. The function mask names the
     * transforms a caller means to apply later, and an exported model is never transformed.
     */
    private static final int NO_FUNCTIONS = 0;

    /**
     * Asks the toolkit to keep the labels of the vertices (0x20) and of the faces (0x180), which
     * the frames of a sequence move and recolour by.
     */
    private static final int LABELS = 0x20 | 0x180;

    /**
     * Leaves textures on. Feature 0x40 is the low detail setting that drops every texture the
     * metrics mark as one that can be turned off.
     */
    private static final int TEXTURES_ON = 0;

    /**
     * The ambient light and contrast most of the client's models are built with. Neither changes
     * the geometry, the colours or the texture coordinates that are exported, only the lighting
     * that the toolkit would work out later.
     */
    private static final int AMBIENT = 64;
    private static final int CONTRAST = 768;

    private final js5 models;
    private final Js5TextureSource textures;
    private final JavaToolkit toolkit;

    public ClientModelReader(File cache) {
        this.models = Cache.js5(cache, Js5Archive.MODELS);
        this.textures = new Js5TextureSource(
            Cache.js5(cache, Js5Archive.MATERIALS),
            Cache.js5(cache, Js5Archive.TEXTURES),
            Cache.js5(cache, Js5Archive.SPRITES));
        Palette.install();
        this.toolkit = new JavaToolkit((TextureSource) textures);
        BillboardTypeList.configClient = Cache.js5(cache, Js5Archive.CONFIG_BILLBOARD);
    }

    /**
     * The model the client would draw for a mesh, or nothing where the cache holds no such mesh.
     */
    public Optional<JavaModel> read(int id) {
        var mesh = Mesh.load(id, models);

        if (mesh == null) {
            return Optional.empty();
        } else {
            if (mesh.version < FIRST_FULL_SCALE_VERSION) {
                mesh.upscale();
            }

            return Optional.of((JavaModel) toolkit.createModel(mesh, NO_FUNCTIONS, TEXTURES_ON, AMBIENT, CONTRAST));
        }
    }

    /**
     * The model as {@link #read} builds it, with the labels of its vertices and faces kept, or
     * nothing where the cache holds no such mesh.
     */
    public Optional<JavaModel> readLabelled(int id) {
        var mesh = Mesh.load(id, models);

        if (mesh == null) {
            return Optional.empty();
        } else {
            if (mesh.version < FIRST_FULL_SCALE_VERSION) {
                mesh.upscale();
            }

            return Optional.of((JavaModel) toolkit.createModel(mesh, LABELS, TEXTURES_ON, AMBIENT, CONTRAST));
        }
    }

    public Js5TextureSource textures() {
        return textures;
    }

    /**
     * The toolkit models are built with, for a reader of some other kind of thing that the client
     * builds models for.
     */
    public JavaToolkit toolkit() {
        return toolkit;
    }
}
