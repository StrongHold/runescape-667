/**
 * Where every vertex of a model is in one pose, in the client's units and frame.
 *
 * The client hands out a posed model in buffers that it fills again for the next pose it is
 * asked for, so a pose is copied out of them as soon as it has been made.
 */
public record Pose(int[] x, int[] y, int[] z) {

    public static Pose of(JavaModel model) {
        return new Pose(model.vertexX.clone(), model.vertexY.clone(), model.vertexZ.clone());
    }
}
