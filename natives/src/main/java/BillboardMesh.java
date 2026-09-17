import com.jagex.game.runetek6.config.billboardtype.BillboardType;
import com.jagex.game.runetek6.config.billboardtype.BillboardTypeList;
import com.jagex.graphics.Mesh;
import com.jagex.graphics.MeshBillboard;

/**
 * A mesh carrying billboards, which are squares the client hangs off a face and keeps turned
 * towards the eye.
 *
 * The kinds a billboard may be are held in the cache, and the client reads one before it hands the
 * numbers over. The kinds here are put straight into the list the client reads from instead, so
 * that a scene needs no game data for them.
 *
 * Three hang off the shape: one wearing a texture, one wearing none, and one whose kind says the
 * face it hangs off is not to be drawn at all.
 */
public record BillboardMesh(int size) {

    public static final BillboardMesh INSTANCE = new BillboardMesh(150);

    /** The numbers the kinds are put into the client's list under. */
    private static final int PLAIN = 900;
    private static final int TEXTURED = 901;
    private static final int INSTEAD_OF_THE_FACE = 902;

    /** A texture the player may not turn off, so that it shows whatever the scene asks for. */
    private static final int WEARING = 6;

    private static final int VERTICES = 9;
    private static final int FACES = 3;

    /** How far from the eye a billboard is brought, out of the whole of its distance. */
    private static final int TOWARDS = 4;

    public Mesh build() {
        kind(PLAIN, -1, false);
        kind(TEXTURED, WEARING, false);
        kind(INSTEAD_OF_THE_FACE, WEARING, true);

        var mesh = new Mesh(VERTICES, FACES, 0);
        mesh.vertexCount = VERTICES;
        mesh.maxVertex = VERTICES;
        mesh.faceCount = FACES;
        mesh.texSpaceCount = 0;
        mesh.globalPriority = 0;

        for (var face = 0; face < FACES; face++) {
            var middle = (face - 1) * size * 3;

            put(mesh, face * 3, middle - size, -size, 0);
            put(mesh, face * 3 + 1, middle + size, -size, 0);
            put(mesh, face * 3 + 2, middle, size, 0);

            face(mesh, face, face * 3, face * 3 + 2, face * 3 + 1);
        }

        mesh.billboards = new MeshBillboard[] {
            new MeshBillboard(PLAIN, 0, 0, TOWARDS),
            new MeshBillboard(TEXTURED, 1, 0, TOWARDS),
            new MeshBillboard(INSTEAD_OF_THE_FACE, 2, 0, TOWARDS)
        };

        return mesh;
    }

    private static void kind(int id, int texture, boolean insteadOfTheFace) {
        var type = new BillboardType();
        type.width = 90;
        type.height = 70;
        type.texture = texture;
        type.blendMode = 2;
        type.anInt9697 = 1;
        type.hideFace = insteadOfTheFace;
        BillboardTypeList.recentUse.put(type, id);
    }

    private void put(Mesh mesh, int index, int x, int y, int z) {
        mesh.vertexX[index] = x;
        mesh.vertexY[index] = y;
        mesh.vertexZ[index] = z;
        mesh.vertexLabel[index] = -1;
    }

    private void face(Mesh mesh, int index, int a, int b, int c) {
        mesh.faceA[index] = (short) a;
        mesh.faceB[index] = (short) b;
        mesh.faceC[index] = (short) c;
        mesh.faceColour[index] = (short) ((10 << 10) | (7 << 7) | 90);
        mesh.faceTexture[index] = -1;
        mesh.faceTexSpace[index] = -1;
        mesh.shadingType[index] = 0;
        mesh.facePriority[index] = 0;
        mesh.faceAlpha[index] = 0;
        mesh.faceLabel[index] = -1;
    }
}
