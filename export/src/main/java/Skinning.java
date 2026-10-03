import com.jagex.AnimBase;
import com.jagex.AnimFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Turns one frame of a sequence into a transform for each label of a model, so that the frame
 * can be played by bones rather than stored as a morph target.
 *
 * <p>The client animates a model label by label: every vertex carries one label, a frame lists
 * transforms, and each transform names the labels it moves. A transform is a pivot, which the
 * client takes as the middle of the vertices of the labels it names, or a move, a turn about the
 * last pivot, or a scale about it. Each is affine, so what a frame does to a label is affine too,
 * and that transform is worked out here by following the frame's transforms in order on a copy
 * of the vertices, as the client does, and folding each into the matrix of every label it names.
 *
 * <p>The client works in integers, a sixteenth of a unit at a time, and this works in doubles, so
 * a vertex moved by the transform lands within a unit or so of where the client puts it.
 * {@link #deviation} measures that against the client's own result.
 *
 * <p>The client turns a label about z, then x, then y when the model's rotation is even, and about
 * x, then z, then y when it is odd. The transforms here are those of the asset, which stands at
 * rotation 0.
 *
 * <p>Where the client scales a model after it has posed it, as it does an NPC, the frames are
 * followed on the unscaled vertices, and each label's transform is then taken into the scaled
 * model, where a move of a frame is scaled with the rest.
 */
public final class Skinning {

    private static final int PIVOT = 0;
    private static final int MOVE = 1;
    private static final int TURN = 2;
    private static final int SCALE = 3;
    private static final double TURN_UNITS = 16384.0;
    private static final double SCALE_UNITS = 128.0;

    /**
     * An affine transform of a vertex, as a 3 by 4 matrix: the first three columns turn and scale,
     * the last moves.
     */
    public record Affine(double[] m) {

        public static Affine identity() {
            return new Affine(new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0});
        }

        /** This transform after another: the other first, then this. */
        public Affine after(Affine other) {
            var a = m;
            var b = other.m;
            var out = new double[12];
            for (var row = 0; row < 3; row++) {
                for (var column = 0; column < 3; column++) {
                    out[column * 3 + row] = a[row] * b[column * 3] + a[3 + row] * b[column * 3 + 1]
                        + a[6 + row] * b[column * 3 + 2];
                }
                out[9 + row] = a[row] * b[9] + a[3 + row] * b[10] + a[6 + row] * b[11] + a[9 + row];
            }
            return new Affine(out);
        }

        public double[] apply(double x, double y, double z) {
            return new double[] {
                m[0] * x + m[3] * y + m[6] * z + m[9],
                m[1] * x + m[4] * y + m[7] * z + m[10],
                m[2] * x + m[5] * y + m[8] * z + m[11]
            };
        }

        public static Affine move(double x, double y, double z) {
            return new Affine(new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1, x, y, z});
        }

        public static Affine scale(double x, double y, double z) {
            return new Affine(new double[] {x, 0, 0, 0, y, 0, 0, 0, z, 0, 0, 0});
        }

        /** A turn about x as the client turns: y' = y cos - z sin, z' = y sin + z cos. */
        public static Affine turnX(double angle) {
            var sin = Math.sin(angle);
            var cos = Math.cos(angle);
            return new Affine(new double[] {1, 0, 0, 0, cos, sin, 0, -sin, cos, 0, 0, 0});
        }

        /** A turn about y as the client turns: x' = x cos + z sin, z' = z cos - x sin. */
        public static Affine turnY(double angle) {
            var sin = Math.sin(angle);
            var cos = Math.cos(angle);
            return new Affine(new double[] {cos, 0, -sin, 0, 1, 0, sin, 0, cos, 0, 0, 0});
        }

        /** A turn about z as the client turns: x' = x cos + y sin, y' = y cos - x sin. */
        public static Affine turnZ(double angle) {
            var sin = Math.sin(angle);
            var cos = Math.cos(angle);
            return new Affine(new double[] {cos, -sin, 0, sin, cos, 0, 0, 0, 1, 0, 0, 0});
        }

        /** A transform about a pivot: move the pivot to the origin, do this, move it back. */
        public Affine about(double[] pivot) {
            return move(pivot[0], pivot[1], pivot[2]).after(this).after(move(-pivot[0], -pivot[1], -pivot[2]));
        }
    }

    /**
     * The transform of each label in one frame, and the frame's transforms as the vertices saw
     * them.
     */
    public record FramePose(Map<Integer, Affine> ofLabel) {

        public Affine of(int label) {
            return ofLabel.getOrDefault(label, Affine.identity());
        }
    }

    private final int[] x;
    private final int[] y;
    private final int[] z;
    private final double[] scale;
    private final int[][] vertexLabels;
    private final int[] labelOfVertex;

    /**
     * @param still the model with no sequence playing and before any scale, whose vertices every
     *     frame starts from.
     * @param scale the scale the client applies after posing, along each axis.
     * @param vertexLabels the vertices of each label, as the client's model holds them.
     */
    public Skinning(JavaModel still, double[] scale, int[][] vertexLabels) {
        this.x = still.vertexX.clone();
        this.y = still.vertexY.clone();
        this.z = still.vertexZ.clone();
        this.scale = scale.clone();
        this.vertexLabels = vertexLabels;
        this.labelOfVertex = new int[still.vertexCount];
        java.util.Arrays.fill(labelOfVertex, -1);
        for (var label = 0; label < vertexLabels.length; label++) {
            for (var vertex : vertexLabels[label]) {
                if (vertex < labelOfVertex.length) {
                    labelOfVertex[vertex] = label;
                }
            }
        }
    }

    /** The label of each vertex, or -1 for a vertex no label names. */
    public int[] labelOfVertex() {
        return labelOfVertex.clone();
    }

    /**
     * The middle of a label's vertices in the still model, before any scale the client applies
     * after posing, in the client's units: what a pivot on the label starts from. A label with
     * no vertex is at the origin.
     */
    public double[] labelCentre(int label) {
        var vertices = label < vertexLabels.length ? vertexLabels[label] : new int[0];
        var sum = new double[3];
        var count = 0;
        for (var vertex : vertices) {
            if (vertex < labelOfVertex.length) {
                sum[0] += x[vertex];
                sum[1] += y[vertex];
                sum[2] += z[vertex];
                count++;
            }
        }
        return count == 0 ? sum : new double[] {sum[0] / count, sum[1] / count, sum[2] / count};
    }

    /** How many vertices carry a label, which is the label's weight in a pivot over several. */
    public int labelCount(int label) {
        var count = 0;
        if (label < vertexLabels.length) {
            for (var vertex : vertexLabels[label]) {
                if (vertex < labelOfVertex.length) {
                    count++;
                }
            }
        }
        return count;
    }

    /** The scale the client applies after posing, along each axis. */
    public double[] poseScale() {
        return scale.clone();
    }

    /**
     * A frame's transforms as the client reads them, six numbers each in the frame's order: the
     * group, its x, y and z values, the pivot group it applies first or -1, and its tween bits,
     * where 1 means the client does not tween into the transform and 2 that it does not tween
     * out of it.
     */
    public static List<Integer> rawTransforms(AnimFrame frame) {
        var raw = new ArrayList<Integer>(frame.transformCount * 6);
        for (var index = 0; index < frame.transformCount; index++) {
            raw.add((int) frame.groups[index]);
            raw.add((int) frame.xValues[index]);
            raw.add((int) frame.yValues[index]);
            raw.add((int) frame.zValues[index]);
            raw.add((int) frame.origins[index]);
            raw.add((int) frame.tweenFlags[index]);
        }
        return raw;
    }

    /** The kind of transform each group of a base holds: 0 a pivot, 1 a move, 2 a turn, 3 a scale, and others the engine leaves alone. */
    public static List<Integer> groupTypes(AnimBase base) {
        var types = new ArrayList<Integer>(base.transformCount);
        for (var group = 0; group < base.transformCount; group++) {
            types.add(base.transformTypes[group]);
        }
        return types;
    }

    /** The labels each group of a base names. */
    public static List<List<Integer>> groupLabels(AnimBase base) {
        var groups = new ArrayList<List<Integer>>(base.transformCount);
        for (var group = 0; group < base.transformCount; group++) {
            var labels = new ArrayList<Integer>();
            for (var label : base.transformLabels[group]) {
                labels.add(label);
            }
            groups.add(labels);
        }
        return groups;
    }

    public int labels() {
        return vertexLabels.length;
    }

    /**
     * Follows the frame's transforms on a copy of the still vertices, and folds each into the
     * transform of every label it names, taken into the scaled model.
     */
    public FramePose pose(AnimFrame frame) {
        var base = frame.base;
        var px = toDoubles(x);
        var py = toDoubles(y);
        var pz = toDoubles(z);
        var ofLabel = new HashMap<Integer, Affine>();
        var pivot = new double[3];

        for (var index = 0; index < frame.transformCount; index++) {
            var group = frame.groups[index];
            var origin = frame.origins[index];
            if (origin != -1) {
                pivot = pivotOf(base.transformLabels[origin], px, py, pz, 0, 0, 0);
            }

            var type = base.transformTypes[group];
            var labels = base.transformLabels[group];
            var fx = frame.xValues[index];
            var fy = frame.yValues[index];
            var fz = frame.zValues[index];
            if (type == PIVOT) {
                pivot = pivotOf(labels, px, py, pz, fx, fy, fz);
            } else if (type == MOVE || type == TURN || type == SCALE) {
                var step = switch (type) {
                    case MOVE -> Affine.move(fx, fy, fz);
                    case TURN -> Affine.turnY(radians(fy)).after(Affine.turnX(radians(fx))).after(Affine.turnZ(radians(fz)))
                        .about(pivot);
                    default -> Affine.scale(fx / SCALE_UNITS, fy / SCALE_UNITS, fz / SCALE_UNITS).about(pivot);
                };
                for (var label : labels) {
                    if (label < vertexLabels.length) {
                        ofLabel.merge(label, step, (held, next) -> next.after(held));
                        for (var vertex : vertexLabels[label]) {
                            var moved = step.apply(px[vertex], py[vertex], pz[vertex]);
                            px[vertex] = moved[0];
                            py[vertex] = moved[1];
                            pz[vertex] = moved[2];
                        }
                    }
                }
            }
        }
        var scaled = new HashMap<Integer, Affine>();
        var into = Affine.scale(scale[0], scale[1], scale[2]);
        var outOf = Affine.scale(1 / scale[0], 1 / scale[1], 1 / scale[2]);
        ofLabel.forEach((label, transform) -> scaled.put(label, into.after(transform).after(outOf)));
        return new FramePose(Map.copyOf(scaled));
    }

    /**
     * How far, at most, a vertex moved by its label's transform lands from where the client put
     * it in the pose, in the client's units.
     */
    public double deviation(FramePose framePose, Pose pose) {
        var worst = 0.0;
        for (var vertex = 0; vertex < labelOfVertex.length; vertex++) {
            var label = labelOfVertex[vertex];
            var moved = framePose.of(label).apply(x[vertex] * scale[0], y[vertex] * scale[1], z[vertex] * scale[2]);
            worst = Math.max(worst, Math.abs(moved[0] - pose.x()[vertex]));
            worst = Math.max(worst, Math.abs(moved[1] - pose.y()[vertex]));
            worst = Math.max(worst, Math.abs(moved[2] - pose.z()[vertex]));
        }
        return worst;
    }

    /**
     * How far, at most, a vertex of one label lands from the client's pose when the label's
     * transform is taken through some way of writing it, over every pose.
     */
    public double deviationOfLabel(int label, List<FramePose> framePoses, List<Pose> poses,
                                   java.util.function.UnaryOperator<Affine> through) {
        var worst = 0.0;
        for (var target = 0; target < framePoses.size(); target++) {
            var written = framePoses.get(target).ofLabel().get(label);
            if (written != null) {
                var affine = through.apply(written);
                var pose = poses.get(target);
                for (var vertex : vertexLabels[label]) {
                    if (vertex < labelOfVertex.length) {
                        var moved = affine.apply(x[vertex] * scale[0], y[vertex] * scale[1], z[vertex] * scale[2]);
                        worst = Math.max(worst, Math.abs(moved[0] - pose.x()[vertex]));
                        worst = Math.max(worst, Math.abs(moved[1] - pose.y()[vertex]));
                        worst = Math.max(worst, Math.abs(moved[2] - pose.z()[vertex]));
                    }
                }
            }
        }
        return worst;
    }

    /** The vertex that lands furthest from the client's, and its label, for finding out why. */
    public String describeWorst(FramePose framePose, Pose pose) {
        var worst = 0.0;
        var at = -1;
        for (var vertex = 0; vertex < labelOfVertex.length; vertex++) {
            var label = labelOfVertex[vertex];
            var moved = framePose.of(label).apply(x[vertex] * scale[0], y[vertex] * scale[1], z[vertex] * scale[2]);
            var off = Math.max(Math.abs(moved[0] - pose.x()[vertex]), Math.max(Math.abs(moved[1] - pose.y()[vertex]), Math.abs(moved[2] - pose.z()[vertex])));
            if (off > worst) {
                worst = off;
                at = vertex;
            }
        }
        var label = at == -1 ? -1 : labelOfVertex[at];
        var moved = at == -1 ? new double[3] : framePose.of(label).apply(x[at] * scale[0], y[at] * scale[1], z[at] * scale[2]);
        return "vertex " + at + " label " + label + " still " + (at == -1 ? "" : x[at] + "," + y[at] + "," + z[at])
            + " client " + (at == -1 ? "" : pose.x()[at] + "," + pose.y()[at] + "," + pose.z()[at])
            + " mine " + (int) moved[0] + "," + (int) moved[1] + "," + (int) moved[2]
            + " labelTouched " + framePose.ofLabel().containsKey(label);
    }

    private double[] pivotOf(int[] labels, double[] px, double[] py, double[] pz, int ox, int oy, int oz) {
        var sum = new double[3];
        var count = 0;
        for (var label : labels) {
            if (label < vertexLabels.length) {
                for (var vertex : vertexLabels[label]) {
                    sum[0] += px[vertex];
                    sum[1] += py[vertex];
                    sum[2] += pz[vertex];
                    count++;
                }
            }
        }
        if (count == 0) {
            return new double[] {ox, oy, oz};
        }
        return new double[] {sum[0] / count + ox, sum[1] / count + oy, sum[2] / count + oz};
    }

    private static double radians(int angle) {
        return angle * 2.0 * Math.PI / TURN_UNITS;
    }

    private static double[] toDoubles(int[] values) {
        var out = new double[values.length];
        for (var i = 0; i < values.length; i++) {
            out[i] = values[i];
        }
        return out;
    }

    /**
     * The labels a frame names that no vertex of the model carries, which happens where a base
     * is shared by models of differing parts.
     */
    public static List<Integer> unusedLabels(AnimFrame frame, int[][] vertexLabels) {
        var unused = new ArrayList<Integer>();
        for (var index = 0; index < frame.transformCount; index++) {
            for (var label : frame.base.transformLabels[frame.groups[index]]) {
                if (label >= vertexLabels.length) {
                    unused.add(label);
                }
            }
        }
        return unused;
    }
}
