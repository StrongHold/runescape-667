import com.jagex.graphics.Mesh;

/**
 * One upright panel of a wall, built here rather than loaded from the cache.
 *
 * The client builds a wall as a model per panel and stands them against one another, and the two
 * panels of a corner meet along an upright edge. A panel leans away from the middle, so the two
 * of a corner face different ways and the edge they share shows as a hard line until they are
 * told they meet.
 *
 * The edge the two of a corner meet along is at the origin of every panel. `reach` says how far
 * away the other edge stands and which side of the middle it is on, `depth` how far back it
 * leans, and `shift` moves the whole panel along so that a pair can be made to meet somewhere
 * other than at the origin.
 */
public record PanelMesh(int reach, int depth, int height, int shift, short colour) {

    private static final int VERTICES = 4;
    private static final int FACES = 2;

    public Mesh build() {
        var mesh = new Mesh(VERTICES, FACES, 0);

        mesh.vertexCount = VERTICES;
        mesh.maxVertex = VERTICES;
        mesh.faceCount = FACES;
        mesh.texSpaceCount = 0;
        mesh.globalPriority = 0;

        var leftX = reach < 0 ? reach : 0;
        var rightX = reach < 0 ? 0 : reach;
        var leftZ = reach < 0 ? depth : 0;
        var rightZ = reach < 0 ? 0 : depth;

        put(mesh, 0, rightX, -height, rightZ);
        put(mesh, 1, leftX, -height, leftZ);
        put(mesh, 2, leftX, height, leftZ);
        put(mesh, 3, rightX, height, rightZ);

        face(mesh, 0, 0, 1, 2);
        face(mesh, 1, 0, 2, 3);

        return mesh;
    }

    private void put(Mesh mesh, int index, int x, int y, int z) {
        mesh.vertexX[index] = x + shift;
        mesh.vertexY[index] = y;
        mesh.vertexZ[index] = z;
        mesh.vertexLabel[index] = -1;
    }

    /**
     * The corners are given anticlockwise as the eye sees them, which is the way round that says
     * a face is the outside of the panel rather than the inside.
     */
    private void face(Mesh mesh, int index, int a, int b, int c) {
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
