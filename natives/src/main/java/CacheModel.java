import com.jagex.graphics.Mesh;

import java.util.TreeMap;

/**
 * Says what one model out of the cache is made of, so that a model the client draws wrongly can
 * be turned into the reason it does.
 *
 * What is drawn wrongly is often decided by something no scene here reaches: how many faces meet
 * at the same place, whether any of them is drawn through what is behind it, and what the mesh
 * says about the order they go down in.
 */
public final class CacheModel {

    private CacheModel() {
        /* empty */
    }

    public static void main(String[] args) throws Exception {
        var group = Integer.parseInt(args.length > 0 ? args[0] : "32421");
        var mesh = CacheMesh.group(group);

        if (mesh == null) {
            System.out.println("no model " + group + " in the cache");
            return;
        }

        System.out.println("model " + group + ": " + mesh.faceCount + " faces, "
            + mesh.vertexCount + " vertices, global priority " + mesh.globalPriority);

        var alpha = 0;
        var textured = 0;
        var priorities = new TreeMap<Integer, Integer>();

        for (var face = 0; face < mesh.faceCount; face++) {
            if (mesh.faceAlpha != null && mesh.faceAlpha[face] != 0) {
                alpha++;
            }
            if (mesh.faceTexture != null && mesh.faceTexture[face] != -1) {
                textured++;
            }
            var priority = mesh.facePriority == null ? 0 : mesh.facePriority[face];
            priorities.merge((int) priority, 1, Integer::sum);
        }

        System.out.println("  " + alpha + " faces drawn through what is behind them, "
            + textured + " wearing a texture");
        System.out.println("  priorities: " + priorities);

        var shared = new TreeMap<String, Integer>();
        for (var face = 0; face < mesh.faceCount; face++) {
            var corners = new int[] {mesh.faceA[face], mesh.faceB[face], mesh.faceC[face]};
            java.util.Arrays.sort(corners);
            shared.merge(corners[0] + "," + corners[1] + "," + corners[2], 1, Integer::sum);
        }

        var doubled = 0;
        for (var count : shared.values()) {
            if (count > 1) {
                doubled += count;
            }
        }

        System.out.println("  " + doubled + " faces stand on the same three corners as another");
        System.out.println("  billboards: "
            + (mesh.billboards == null ? 0 : mesh.billboards.length));
    }
}
