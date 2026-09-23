import com.beust.jcommander.Parameter;

import java.util.Arrays;
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

    /** The model described when none is named, which is the one the scenes are built around. */
    private static final int A_MODEL_WORTH_LOOKING_AT = 32421;

    public static final class Args implements Arguments {

        @Parameter(names = "--model", description = "Which model to describe")
        private int model = A_MODEL_WORTH_LOOKING_AT;

        @Parameter(names = "--scanned", description = "Say which models have been looked at instead")
        private boolean scanned;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("describeModel", new Args(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(Args args) throws Exception  {
        if (args.scanned) {
            CacheMesh.sayWhichAreScanned();
        } else {
            describe(args.model);
        }
    }

    private static void describe(int group) throws Exception {
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
            Arrays.sort(corners);
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

    private CacheModel() {
        /* empty */
    }
}
