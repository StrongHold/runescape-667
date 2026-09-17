import com.jagex.graphics.Mesh;

/**
 * A mesh whose faces stand two to a place.
 *
 * Every face here is drawn twice over, on the same three corners and in a different colour. Which
 * of the two is seen is settled by how far away each is reckoned to be, and the two answers come
 * out of the same corners by different routes, so they are a hair apart rather than equal. Turning
 * a mesh like this is the only way to ask which of the two a toolkit shows.
 *
 * The client builds scenery this way wherever a thing is meant to look the same from both sides,
 * and a model out of the cache has more than a hundred faces standing two to a place.
 */
public record DoubledMesh(int size) {

    public static final DoubledMesh INSTANCE = new DoubledMesh(160);

    /** How many places a face stands in, and how many faces stand in each. */
    private static final int PLACES = 6;
    private static final int OVER = 2;

    private static final int FACES = PLACES * OVER;
    private static final int VERTICES = PLACES * 3;

    public Mesh build() {
        var mesh = new Mesh(VERTICES, FACES, 0);
        mesh.vertexCount = VERTICES;
        mesh.maxVertex = VERTICES;
        mesh.faceCount = FACES;
        mesh.texSpaceCount = 0;
        mesh.globalPriority = 0;

        for (var place = 0; place < PLACES; place++) {
            var middle = (place % 3 - 1) * size * 2;
            var depth = (place / 3) * size;

            put(mesh, place * 3, middle - size, -size, depth);
            put(mesh, place * 3 + 1, middle + size, -size, depth);
            put(mesh, place * 3 + 2, middle, size, depth);

            /*
             * The two faces of a place are wound the same way and named in a different order, so
             * each reckons how far away it is from a different corner first.
             */
            face(mesh, place * OVER, place * 3, place * 3 + 2, place * 3 + 1, place);
            face(mesh, place * OVER + 1, place * 3 + 1, place * 3, place * 3 + 2, place + PLACES);
        }

        return mesh;
    }

    private void put(Mesh mesh, int index, int x, int y, int z) {
        mesh.vertexX[index] = x;
        mesh.vertexY[index] = y;
        mesh.vertexZ[index] = z;
        mesh.vertexLabel[index] = -1;
    }

    private void face(Mesh mesh, int index, int a, int b, int c, int shade) {
        mesh.faceA[index] = (short) a;
        mesh.faceB[index] = (short) b;
        mesh.faceC[index] = (short) c;
        mesh.faceColour[index] = (short) ((shade * 5 << 10) | (7 << 7) | (40 + shade * 7));
        mesh.faceTexture[index] = -1;
        mesh.faceTexSpace[index] = -1;
        mesh.shadingType[index] = 0;

        /* One of every pair is named as going down first, so the two differ by that as well. */
        mesh.facePriority[index] = (byte) (index % OVER);
        mesh.faceAlpha[index] = 0;
        mesh.faceLabel[index] = -1;
    }
}
