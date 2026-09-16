import com.jagex.graphics.Mesh;

/**
 * A mesh built here rather than loaded from the cache, so that a scene needs no game data.
 *
 * It is a square standing upright, split into two faces of different colours, with a third face
 * behind it at a different depth. Two faces that share an edge say whether the rasteriser fills
 * the shared edge once, and a face behind says whether it is hidden.
 *
 * `spare` adds vertices that belong to no face, the way a model that hangs billboards or
 * particles carries them. They stand well outside the shape, so anything that measures the model
 * by walking every vertex it holds rather than only the ones the faces use says so.
 */
public record FlatMesh(int size, int spare) {

    public static final FlatMesh INSTANCE = new FlatMesh(160, 0);

    private static final int VERTICES = 7;
    private static final int FACES = 3;

    /** How far out of the shape a vertex that belongs to no face stands. */
    private static final int ASIDE = 5000;

    public Mesh build() {
        var mesh = new Mesh(VERTICES + spare, FACES, 0);

        mesh.vertexCount = VERTICES + spare;
        mesh.maxVertex = VERTICES;
        mesh.faceCount = FACES;
        mesh.texSpaceCount = 0;
        mesh.globalPriority = 0;

        put(mesh, 0, -size, -size, 0);
        put(mesh, 1, size, -size, 0);
        put(mesh, 2, size, size, 0);
        put(mesh, 3, -size, size, 0);

        put(mesh, 4, -size / 2, -size / 2, size);
        put(mesh, 5, size, -size / 2, size);
        put(mesh, 6, size / 2, size, size);

        face(mesh, 0, 0, 1, 2, hsl(0, 7, 96));
        face(mesh, 1, 0, 2, 3, hsl(21, 7, 96));
        face(mesh, 2, 4, 5, 6, hsl(42, 7, 96));

        for (var extra = 0; extra < spare; extra++) {
            var sign = extra % 2 == 0 ? ASIDE : -ASIDE;
            put(mesh, VERTICES + extra, sign, sign, sign);
        }

        return mesh;
    }

    private void put(Mesh mesh, int index, int x, int y, int z) {
        mesh.vertexX[index] = x;
        mesh.vertexY[index] = y;
        mesh.vertexZ[index] = z;
        mesh.vertexLabel[index] = -1;
    }

    /**
     * A colour as the client packs it: six bits of hue, three of saturation and seven of
     * lightness. A face with no lightness is black however bright its hue.
     */
    private static short hsl(int hue, int saturation, int lightness) {
        return (short) ((hue << 10) | (saturation << 7) | lightness);
    }

    /**
     * The colour is the packed form the client keeps its artwork in, so a face is flat and its
     * shade is whatever the toolkit decides rather than whatever we asked for.
     */
    private void face(Mesh mesh, int index, int a, int b, int c, short colour) {
        mesh.faceA[index] = (short) a;
        mesh.faceB[index] = (short) b;
        mesh.faceC[index] = (short) c;
        mesh.faceColour[index] = colour;
        mesh.faceTexture[index] = -1;
        mesh.faceTexSpace[index] = -1;
        mesh.shadingType[index] = 0;
        mesh.facePriority[index] = 0;
        mesh.faceAlpha[index] = 0;
        mesh.faceLabel[index] = -1;
    }
}
