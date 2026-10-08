import com.jagex.AnimBase;
import com.jagex.AnimFrame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Writes the bones of a skinned mesh, and its sequences as animations of those bones.
 *
 * <p>Every label of the model that a vertex carries becomes one joint, a node under the mesh's
 * node, and every vertex is bound wholly to the joint of its label. A vertex of no label is bound
 * to a joint that never moves. The joints stand at the origin when nothing plays, so each one's
 * transform in a frame is the label's transform as {@link Skinning} works it out, written as a
 * move, a turn and a scale, which is all a glTF joint can be given. A frame that scales a label
 * after turning it, or scales it unevenly and then turns it, cannot be written that way exactly,
 * and {@link #worstDeviationFine} measures what the written joints land each vertex at against the
 * client's own pose.
 */
public final class SkinWriter {

    private static final String STEP = "STEP";

    /**
     * The joint of each client vertex, the joints in the order they are numbered, and which of
     * them are chains.
     *
     * @param chained for each joint, whether it is a chain of three nodes, because one node's
     *     move, turn and scale cannot hold what some frame does to its label.
     */
    public record Joints(int[] ofVertex, List<Integer> labels, List<Boolean> chained) {

        public int count() {
            return labels.size();
        }
    }

    /**
     * Numbers a joint for every label the model's vertices carry, and one more for the vertices
     * that carry none, which is the first. A label is given a chain where a single node would
     * land any of its vertices further than the tolerance from the client's pose in any frame.
     */
    public static Joints joints(Skinning skinning, List<Skinning.FramePose> framePoses, List<Pose> poses) {
        var labelOfVertex = skinning.labelOfVertex();
        var numbers = new TreeMap<Integer, Integer>();
        var labels = new ArrayList<Integer>();
        labels.add(-1);
        for (var label : labelOfVertex) {
            if (label != -1 && !numbers.containsKey(label)) {
                numbers.put(label, labels.size());
                labels.add(label);
            }
        }

        var ofVertex = new int[labelOfVertex.length];
        for (var vertex = 0; vertex < ofVertex.length; vertex++) {
            ofVertex[vertex] = labelOfVertex[vertex] == -1 ? 0 : numbers.get(labelOfVertex[vertex]);
        }

        var chained = new ArrayList<Boolean>();
        for (var label : labels) {
            chained.add(label != -1 && skinning.deviationOfLabelFine(label, framePoses, poses, Trs::through) > TOLERANCE_FINE);
        }
        return new Joints(ofVertex, List.copyOf(labels), List.copyOf(chained));
    }

    /**
     * A skin as written: its number, which the mesh's node names to wear it, the node of each
     * joint, which the skin binds, and the node the mesh's node holds as a child for each joint,
     * which for a chain is its outermost node.
     */
    public record Skin(int number, List<Integer> jointNodes, List<Integer> childNodes) {
    }

    /**
     * Adds the nodes of every joint, and a skin that binds the mesh to them. A chain is three
     * nodes, one under the next: the outer one is moved and turned, the middle one scaled, and
     * the inner one turned again, and the inner one is the joint the skin binds. The skin's
     * extras say which label each joint carries, where the label's vertices are centred and how
     * many there are, and the scale the client applies after posing, which is what an engine
     * needs to follow a frame's transforms itself.
     */
    public static Skin write(GltfBuilder gltf, Joints joints, Skinning skinning) {
        var jointNodes = new ArrayList<Integer>();
        var childNodes = new ArrayList<Integer>();
        for (var joint = 0; joint < joints.count(); joint++) {
            var label = joints.labels().get(joint);
            var name = label == -1 ? "unlabelled" : "label " + label;
            if (joints.chained().get(joint)) {
                var inner = gltf.node(named(name + " turn"));
                var middle = new LinkedHashMap<String, Object>(named(name + " scale"));
                middle.put("children", List.of(inner));
                var outer = new LinkedHashMap<String, Object>(named(name));
                outer.put("children", List.of(gltf.node(middle)));
                jointNodes.add(inner);
                childNodes.add(gltf.node(outer));
            } else {
                var node = gltf.node(named(name));
                jointNodes.add(node);
                childNodes.add(node);
            }
        }

        var identity = new float[16 * jointNodes.size()];
        for (var joint = 0; joint < jointNodes.size(); joint++) {
            identity[joint * 16] = 1;
            identity[joint * 16 + 5] = 1;
            identity[joint * 16 + 10] = 1;
            identity[joint * 16 + 15] = 1;
        }
        var inverseBindMatrices = gltf.animationData(identity, "MAT4", 16, false);
        return new Skin(gltf.skin(jointNodes, inverseBindMatrices, labelExtras(joints, skinning)), List.copyOf(jointNodes),
            List.copyOf(childNodes));
    }

    private static Map<String, Object> labelExtras(Joints joints, Skinning skinning) {
        var centres = new ArrayList<List<Float>>();
        var counts = new ArrayList<Integer>();
        for (var label : joints.labels()) {
            var centre = label == -1 ? new double[3] : skinning.labelCentre(label);
            centres.add(List.of((float) centre[0], (float) centre[1], (float) centre[2]));
            counts.add(label == -1 ? 0 : skinning.labelCount(label));
        }
        var scale = skinning.poseScale();
        var extras = new LinkedHashMap<String, Object>();
        extras.put("labels", joints.labels());
        extras.put("labelCentres", centres);
        extras.put("labelCounts", counts);
        extras.put("poseScale", List.of((float) scale[0], (float) scale[1], (float) scale[2]));
        return extras;
    }

    private static Map<String, Object> named(String name) {
        return Map.of("name", name);
    }

    /**
     * How far, at most, the bones may land a vertex from the client's pose before the poses are
     * kept as morph targets instead, in the client's units. The client's own integer arithmetic
     * accounts for about one unit.
     */
    public static final double TOLERANCE_FINE = 2.5;

    /**
     * Adds an animation that moves every joint through the frames of a clip, each frame from the
     * moment the client moves on to it. A channel whose value never changes through the clip is
     * left out, as the node already stands at it.
     *
     * @param framePoses the transform of each label in each target, in the order the targets are
     *     numbered.
     * @param frames the client's frame behind each target, in the same order, whose transforms
     *     the animation's extras carry so that an engine can tween them as the client does.
     * @param colourTargets how many morph targets the mesh keeps for the colours of its frames,
     *     whose weights the animation sets as well, or 0 where it keeps none.
     */
    public static void writeClip(GltfBuilder gltf, String name, PoseBaker.Clip clip, Joints joints,
                                 List<Skinning.FramePose> framePoses, List<AnimFrame> frames, Skin skin, int meshNode,
                                 int colourTargets, Map<String, Object> extras) {
        var keys = clip.keys();
        var times = AnimationWriter.keyTimes(clip);
        var input = gltf.animationData(times, "SCALAR", 1, true);

        var samplers = new ArrayList<Map<String, Object>>();
        var channels = new ArrayList<Map<String, Object>>();
        if (colourTargets > 0) {
            var weights = AnimationWriter.weightsSampler(gltf, clip, colourTargets);
            addChannel(gltf, samplers, channels, meshNode, "weights", weights.input(), weights.output());
        }
        for (var joint = 1; joint < joints.count(); joint++) {
            var label = joints.labels().get(joint);
            var transforms = new ArrayList<Skinning.Affine>();
            for (var key = 0; key <= keys.size(); key++) {
                var target = keys.get(Math.min(key, keys.size() - 1)).target();
                transforms.add(framePoses.get(target).of(label));
            }

            if (joints.chained().get(joint)) {
                var chains = transforms.stream().map(Chain::of).toList();
                var inner = skin.jointNodes().get(joint);
                var outer = skin.childNodes().get(joint);
                var middle = gltf.childOf(outer);
                track(gltf, samplers, channels, outer, "translation", input, chains, Chain::translation, 3, IDENTITY_MOVE);
                track(gltf, samplers, channels, outer, "rotation", input, chains, Chain::outerRotation, 4, IDENTITY_TURN);
                track(gltf, samplers, channels, middle, "scale", input, chains, Chain::scale, 3, IDENTITY_SCALE);
                track(gltf, samplers, channels, inner, "rotation", input, chains, Chain::innerRotation, 4, IDENTITY_TURN);
            } else {
                var parts = transforms.stream().map(Trs::of).toList();
                var node = skin.jointNodes().get(joint);
                track(gltf, samplers, channels, node, "translation", input, parts, Trs::translation, 3, IDENTITY_MOVE);
                track(gltf, samplers, channels, node, "rotation", input, parts, Trs::rotation, 4, IDENTITY_TURN);
                track(gltf, samplers, channels, node, "scale", input, parts, Trs::scale, 3, IDENTITY_SCALE);
            }
        }

        gltf.animation(name, samplers, channels, AnimationWriter.sequenceExtras(clip, frameExtras(clip, frames, extras)));
    }

    /**
     * The frames of a clip as the client reads them, one list of raw transforms per key, with
     * the groups of the base they share, so that an engine can replay and tween them itself.
     * Every frame of a sequence the client tweens shares one base, since the client drops the
     * next frame when its base differs.
     */
    private static Map<String, Object> frameExtras(PoseBaker.Clip clip, List<AnimFrame> frames, Map<String, Object> given) {
        var keys = clip.keys();
        var raw = new ArrayList<List<Integer>>();
        AnimBase base = null;
        for (var key : keys) {
            var frame = frames.get(key.target());
            if (base == null) {
                base = frame.base;
            } else if (base != frame.base) {
                throw new IllegalStateException("Sequence " + clip.sequence().id + " has frames of two bases, which is not supported.");
            }
            raw.add(Skinning.rawTransforms(frame));
        }
        var extras = new LinkedHashMap<String, Object>(given);
        extras.put("frames", raw);
        extras.put("groupTypes", base == null ? List.of() : Skinning.groupTypes(base));
        extras.put("groupLabels", base == null ? List.of() : Skinning.groupLabels(base));
        extras.put("groupShadowed", base == null ? List.of() : Skinning.groupShadowed(base));
        return extras;
    }

    private static final float[] IDENTITY_MOVE = {0, 0, 0};
    private static final float[] IDENTITY_TURN = {0, 0, 0, 1};
    private static final float[] IDENTITY_SCALE = {1, 1, 1};

    /**
     * Adds one channel of a node, unless every key of it is the value the node stands at anyway.
     */
    private static <T> void track(GltfBuilder gltf, List<Map<String, Object>> samplers, List<Map<String, Object>> channels,
                                  int node, String path, int input, List<T> keys, java.util.function.Function<T, float[]> part,
                                  int components, float[] identity) {
        var values = new float[keys.size() * components];
        var still = true;
        for (var key = 0; key < keys.size(); key++) {
            var value = part.apply(keys.get(key));
            System.arraycopy(value, 0, values, key * components, components);
            for (var component = 0; component < components; component++) {
                still &= Math.abs(value[component] - identity[component]) < 1e-6F;
            }
        }
        if (!still) {
            var type = components == 4 ? "VEC4" : "VEC3";
            addChannel(gltf, samplers, channels, node, path, input, gltf.animationData(values, type, components, false));
        }
    }

    private static void addChannel(GltfBuilder gltf, List<Map<String, Object>> samplers,
                                   List<Map<String, Object>> channels, int node, String path, int input, int output) {
        samplers.add(Map.of("input", input, "output", output, "interpolation", STEP));
        channels.add(Map.of("sampler", samplers.size() - 1, "target", Map.of("node", node, "path", path)));
    }

    /**
     * How far, at most, a vertex moved by its written joint lands from where the client put it,
     * over every pose, in the client's units.
     */
    public static double worstDeviationFine(Skinning skinning, Joints joints, List<Skinning.FramePose> framePoses,
                                        List<Pose> poses) {
        var worst = 0.0;
        for (var joint = 1; joint < joints.count(); joint++) {
            var label = joints.labels().get(joint);
            java.util.function.UnaryOperator<Skinning.Affine> through = joints.chained().get(joint)
                ? Chain::through
                : Trs::through;
            var deviationFine = skinning.deviationOfLabelFine(label, framePoses, poses, through);
            worst = Math.max(worst, deviationFine);
        }
        return worst;
    }

    /**
     * A transform as a chain of three nodes holds it: the outer node's move and turn, the middle
     * node's scale, and the inner node's turn. Any affine transform is a turn, then an axis scale,
     * then a turn, by its singular value decomposition, and that is read off here.
     */
    public record Chain(float[] translation, float[] outerRotation, float[] scale, float[] innerRotation) {

        public static Chain of(Skinning.Affine affine) {
            var linear = Trs.gltfLinear(affine);
            var svd = Svd.of(linear);
            return new Chain(Trs.gltfTranslation(affine), Trs.quaternion(svd.u()), toFloats(svd.sigma()),
                Trs.quaternion(svd.vTransposed()));
        }

        /** The transform the chain gives a vertex, back in the client's frame, for measuring. */
        public static Skinning.Affine through(Skinning.Affine affine) {
            var chain = of(affine);
            var outer = Trs.rotationMatrix(chain.outerRotation());
            var middle = new double[] {chain.scale()[0], 0, 0, 0, chain.scale()[1], 0, 0, 0, chain.scale()[2]};
            var inner = Trs.rotationMatrix(chain.innerRotation());
            return Trs.fromGltf(Trs.multiply(Trs.multiply(outer, middle), inner), chain.translation());
        }

        private static float[] toFloats(double[] values) {
            var out = new float[values.length];
            for (var i = 0; i < values.length; i++) {
                out[i] = (float) values[i];
            }
            return out;
        }
    }

    /**
     * A transform as a glTF joint holds it: a move, a turn as a quaternion, and a scale, in the
     * viewer's frame of metres with y up and north along -z.
     *
     * <p>The client's frame is the mirror of glTF's in y and z. A mirror of two axes is a half
     * turn about the third, so a transform is taken into glTF's frame by a half turn about x on
     * each side: the turn part is conjugated by it, and the move has its y and z negated.
     */
    public record Trs(float[] translation, float[] rotation, float[] scale) {

        private static final double FINE_PER_METRE = 512.0;
        private static final double[] FLIP = {1, -1, -1};

        public static Trs of(Skinning.Affine affine) {
            var linear = gltfLinear(affine);
            var scale = new double[3];
            for (var column = 0; column < 3; column++) {
                scale[column] = Math.sqrt(linear[column * 3] * linear[column * 3]
                    + linear[column * 3 + 1] * linear[column * 3 + 1] + linear[column * 3 + 2] * linear[column * 3 + 2]);
            }
            if (determinant(linear) < 0) {
                scale[0] = -scale[0];
            }

            var r = new double[9];
            for (var column = 0; column < 3; column++) {
                for (var row = 0; row < 3; row++) {
                    r[column * 3 + row] = scale[column] == 0 ? (row == column ? 1 : 0) : linear[column * 3 + row] / scale[column];
                }
            }
            return new Trs(gltfTranslation(affine), quaternion(r),
                new float[] {(float) scale[0], (float) scale[1], (float) scale[2]});
        }

        /** The transform the joint gives a vertex, back in the client's frame, for measuring. */
        public static Skinning.Affine through(Skinning.Affine affine) {
            var trs = of(affine);
            var r = rotationMatrix(trs.rotation());
            var linear = new double[9];
            for (var column = 0; column < 3; column++) {
                for (var row = 0; row < 3; row++) {
                    linear[column * 3 + row] = r[column * 3 + row] * trs.scale()[column];
                }
            }
            return fromGltf(linear, trs.translation());
        }

        /** The turning and scaling part of a transform, taken into glTF's frame. */
        static double[] gltfLinear(Skinning.Affine affine) {
            var m = affine.m();
            var linear = new double[9];
            for (var column = 0; column < 3; column++) {
                for (var row = 0; row < 3; row++) {
                    linear[column * 3 + row] = m[column * 3 + row] * FLIP[row] * FLIP[column];
                }
            }
            return linear;
        }

        static float[] gltfTranslation(Skinning.Affine affine) {
            var m = affine.m();
            return new float[] {(float) (m[9] / FINE_PER_METRE), (float) (-m[10] / FINE_PER_METRE),
                (float) (-m[11] / FINE_PER_METRE)};
        }

        /** A transform in glTF's frame, taken back into the client's frame and units. */
        static Skinning.Affine fromGltf(double[] linear, float[] translation) {
            var m = new double[12];
            for (var column = 0; column < 3; column++) {
                for (var row = 0; row < 3; row++) {
                    m[column * 3 + row] = linear[column * 3 + row] * FLIP[row] * FLIP[column];
                }
            }
            m[9] = translation[0] * FINE_PER_METRE;
            m[10] = -translation[1] * FINE_PER_METRE;
            m[11] = -translation[2] * FINE_PER_METRE;
            return new Skinning.Affine(m);
        }

        static double determinant(double[] l) {
            return l[0] * (l[4] * l[8] - l[7] * l[5]) - l[3] * (l[1] * l[8] - l[7] * l[2]) + l[6] * (l[1] * l[5] - l[4] * l[2]);
        }

        /** The product of two 3 by 3 matrices held column by column. */
        static double[] multiply(double[] a, double[] b) {
            var out = new double[9];
            for (var column = 0; column < 3; column++) {
                for (var row = 0; row < 3; row++) {
                    out[column * 3 + row] = a[row] * b[column * 3] + a[3 + row] * b[column * 3 + 1] + a[6 + row] * b[column * 3 + 2];
                }
            }
            return out;
        }

        /** The rotation matrix, column by column, of a unit quaternion (x, y, z, w). */
        static double[] rotationMatrix(float[] q) {
            double x = q[0];
            double y = q[1];
            double z = q[2];
            double w = q[3];
            return new double[] {
                1 - 2 * (y * y + z * z), 2 * (x * y + z * w), 2 * (x * z - y * w),
                2 * (x * y - z * w), 1 - 2 * (x * x + z * z), 2 * (y * z + x * w),
                2 * (x * z + y * w), 2 * (y * z - x * w), 1 - 2 * (x * x + y * y)
            };
        }

        /** A unit quaternion (x, y, z, w) of a rotation matrix held column by column. */
        static float[] quaternion(double[] r) {
            var m00 = r[0];
            var m10 = r[1];
            var m20 = r[2];
            var m01 = r[3];
            var m11 = r[4];
            var m21 = r[5];
            var m02 = r[6];
            var m12 = r[7];
            var m22 = r[8];
            var trace = m00 + m11 + m22;
            double x;
            double y;
            double z;
            double w;
            if (trace > 0) {
                var s = 0.5 / Math.sqrt(trace + 1.0);
                w = 0.25 / s;
                x = (m21 - m12) * s;
                y = (m02 - m20) * s;
                z = (m10 - m01) * s;
            } else if (m00 > m11 && m00 > m22) {
                var s = 2.0 * Math.sqrt(1.0 + m00 - m11 - m22);
                w = (m21 - m12) / s;
                x = 0.25 * s;
                y = (m01 + m10) / s;
                z = (m02 + m20) / s;
            } else if (m11 > m22) {
                var s = 2.0 * Math.sqrt(1.0 + m11 - m00 - m22);
                w = (m02 - m20) / s;
                x = (m01 + m10) / s;
                y = 0.25 * s;
                z = (m12 + m21) / s;
            } else {
                var s = 2.0 * Math.sqrt(1.0 + m22 - m00 - m11);
                w = (m10 - m01) / s;
                x = (m02 + m20) / s;
                y = (m12 + m21) / s;
                z = 0.25 * s;
            }
            var length = Math.sqrt(x * x + y * y + z * z + w * w);
            return new float[] {(float) (x / length), (float) (y / length), (float) (z / length), (float) (w / length)};
        }
    }

    /**
     * The singular value decomposition of a 3 by 3 matrix, L = U S V^T, with U and V^T turns
     * and S an axis scale, found by Jacobi's rotations on L^T L. A reflection in L is kept in S
     * as a negative scale, so that U and V^T are proper turns.
     */
    public record Svd(double[] u, double[] sigma, double[] vTransposed) {

        private static final int SWEEPS = 30;

        public static Svd of(double[] l) {
            var a = Trs.multiply(transpose(l), l);
            var v = new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1};
            for (var sweep = 0; sweep < SWEEPS; sweep++) {
                var off = Math.abs(a[3]) + Math.abs(a[6]) + Math.abs(a[7]);
                if (off < 1e-12) {
                    break;
                }
                for (var p = 0; p < 3; p++) {
                    for (var q = p + 1; q < 3; q++) {
                        rotate(a, v, p, q);
                    }
                }
            }

            var sigma = new double[] {Math.sqrt(Math.max(a[0], 0)), Math.sqrt(Math.max(a[4], 0)), Math.sqrt(Math.max(a[8], 0))};
            var lv = Trs.multiply(l, v);
            var u = new double[9];
            var missing = new ArrayList<Integer>();
            for (var column = 0; column < 3; column++) {
                if (sigma[column] > 1e-9) {
                    for (var row = 0; row < 3; row++) {
                        u[column * 3 + row] = lv[column * 3 + row] / sigma[column];
                    }
                } else {
                    sigma[column] = 0;
                    missing.add(column);
                }
            }
            completeColumns(u, missing);
            if (Trs.determinant(u) < 0) {
                sigma[0] = -sigma[0];
                u[0] = -u[0];
                u[1] = -u[1];
                u[2] = -u[2];
            }
            if (Trs.determinant(v) < 0) {
                sigma[0] = -sigma[0];
                v[0] = -v[0];
                v[1] = -v[1];
                v[2] = -v[2];
            }
            return new Svd(u, sigma, transpose(v));
        }

        /**
         * Fills the columns of a turn that a scale of zero left undefined, at right angles to the
         * rest, so that the matrix is a proper turn whatever the scale flattened.
         */
        private static void completeColumns(double[] u, List<Integer> missing) {
            if (missing.size() == 3) {
                System.arraycopy(new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1}, 0, u, 0, 9);
            } else if (missing.size() == 2) {
                var kept = 3 - missing.get(0) - missing.get(1);
                var k = column(u, kept);
                var other = Math.abs(k[0]) < 0.9 ? new double[] {1, 0, 0} : new double[] {0, 1, 0};
                var first = unit(cross(k, other));
                var second = unit(cross(k, first));
                var columns = new double[][] {null, null, null};
                columns[kept] = k;
                columns[missing.get(0)] = first;
                columns[missing.get(1)] = second;
                for (var column = 0; column < 3; column++) {
                    System.arraycopy(columns[column], 0, u, column * 3, 3);
                }
            } else if (missing.size() == 1) {
                var gap = missing.get(0);
                var next = column(u, (gap + 1) % 3);
                var after = column(u, (gap + 2) % 3);
                System.arraycopy(unit(cross(next, after)), 0, u, gap * 3, 3);
            }
        }

        private static double[] column(double[] m, int column) {
            return new double[] {m[column * 3], m[column * 3 + 1], m[column * 3 + 2]};
        }

        private static double[] cross(double[] a, double[] b) {
            return new double[] {a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]};
        }

        private static double[] unit(double[] v) {
            var length = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
            return length == 0 ? new double[] {1, 0, 0} : new double[] {v[0] / length, v[1] / length, v[2] / length};
        }

        /** One Jacobi rotation that zeroes the (p, q) entry of a symmetric matrix. */
        private static void rotate(double[] a, double[] v, int p, int q) {
            var apq = a[q * 3 + p];
            if (Math.abs(apq) < 1e-15) {
                return;
            }
            var app = a[p * 3 + p];
            var aqq = a[q * 3 + q];
            var theta = (aqq - app) / (2 * apq);
            var t = Math.signum(theta) / (Math.abs(theta) + Math.sqrt(theta * theta + 1));
            if (theta == 0) {
                t = 1;
            }
            var c = 1 / Math.sqrt(t * t + 1);
            var s = t * c;

            var rotation = new double[] {1, 0, 0, 0, 1, 0, 0, 0, 1};
            rotation[p * 3 + p] = c;
            rotation[q * 3 + q] = c;
            rotation[q * 3 + p] = s;
            rotation[p * 3 + q] = -s;
            var rotated = Trs.multiply(Trs.multiply(transpose(rotation), a), rotation);
            System.arraycopy(rotated, 0, a, 0, 9);
            var turned = Trs.multiply(v, rotation);
            System.arraycopy(turned, 0, v, 0, 9);
        }

        private static double[] transpose(double[] m) {
            return new double[] {m[0], m[3], m[6], m[1], m[4], m[7], m[2], m[5], m[8]};
        }
    }

    private SkinWriter() {
        /* empty */
    }
}
