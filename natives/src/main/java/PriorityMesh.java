import com.jagex.graphics.Mesh;

/**
 * Two faces lying exactly on top of one another, listed in one order and given priorities in the
 * other.
 *
 * The client hands over a priority for every face and a priority for the mesh as a whole. Where
 * the two faces of a pair stand at the same distance, which of them ends up on top is decided by
 * whichever is drawn second, so a toolkit that draws them in the order the mesh lists them and one
 * that draws them by their priority disagree about the colour of every pixel they share.
 *
 * Three pairs are laid side by side: one where the second face listed has the higher priority, one
 * where the first does, and one where neither has any. The third says what the pairs look like
 * when nothing sorts them.
 */
public record PriorityMesh(int size) {

    public static final PriorityMesh INSTANCE = new PriorityMesh(150);

    /**
     * Three pairs are drawn solid and three more are drawn through what is behind them, because
     * a face that lets anything through is put in a pass of its own and may be sorted there even
     * where a solid one is not.
     */
    private static final int PAIRS = 6;

    /** How much of a face in the second three shows, counted the way the client counts it. */
    private static final byte THROUGH = 80;
    private static final int VERTICES = PAIRS * 4;
    private static final int FACES = PAIRS * 2;

    /** How far apart the pairs stand, out of the size of one of them. */
    private static final int BESIDE = 3;

    public Mesh build() {
        var mesh = new Mesh(VERTICES, FACES, 0);

        mesh.vertexCount = VERTICES;
        mesh.maxVertex = VERTICES;
        mesh.faceCount = FACES;
        mesh.texSpaceCount = 0;
        mesh.globalPriority = 0;

        for (var pair = 0; pair < PAIRS; pair++) {
            var middle = (pair % (PAIRS / 2) - 1) * size * BESIDE;
            var up = pair < PAIRS / 2 ? -size * 2 : size * 2;

            put(mesh, pair * 4, middle - size, up - size, 0);
            put(mesh, pair * 4 + 1, middle + size, up - size, 0);
            put(mesh, pair * 4 + 2, middle + size, up + size, 0);
            put(mesh, pair * 4 + 3, middle - size, up + size, 0);

            face(mesh, pair * 2, pair * 4, pair * 4 + 2, pair * 4 + 1, hsl(0, 7, 90));
            face(mesh, pair * 2 + 1, pair * 4, pair * 4 + 3, pair * 4 + 2, hsl(21, 7, 90));
        }

        /*
         * The two faces of a pair cover the same half of the square each, so a second face over
         * the first is made by giving both of them all four corners.
         */
        for (var pair = 0; pair < PAIRS; pair++) {
            mesh.faceB[pair * 2] = (short) (pair * 4 + 3);
            mesh.faceC[pair * 2 + 1] = (short) (pair * 4 + 1);
        }

        var priorities = new byte[] {0, 9, 9, 0, 0, 0};
        for (var face = 0; face < FACES; face++) {
            mesh.facePriority[face] = priorities[face % priorities.length];

            if (face >= FACES / 2) {
                mesh.faceAlpha[face] = THROUGH;
            }
        }

        return mesh;
    }

    private void put(Mesh mesh, int index, int x, int y, int z) {
        mesh.vertexX[index] = x;
        mesh.vertexY[index] = y;
        mesh.vertexZ[index] = z;
        mesh.vertexLabel[index] = -1;
    }

    private static short hsl(int hue, int saturation, int lightness) {
        return (short) ((hue << 10) | (saturation << 7) | lightness);
    }

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
