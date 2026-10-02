/**
 * Where every vertex of a model is in one pose, in the client's units and frame, and the colour
 * and alpha of every face in it.
 *
 * A frame of a sequence can move the vertices of a part of the model, and it can also change the
 * colour or the alpha of the faces of a part, which is how a flame burns in place: its tongues
 * are all there in the mesh, and the frames fade them in and out in turn.
 *
 * The client hands out a posed model in buffers that it fills again for the next pose it is
 * asked for, so a pose is copied out of them as soon as it has been made.
 *
 * @param colour the client's HSL colour of each face.
 * @param alpha the alpha of each face, where 0 is opaque and 255 is invisible.
 */
public record Pose(int[] x, int[] y, int[] z, short[] colour, byte[] alpha) {

    public static Pose of(JavaModel model) {
        return new Pose(model.vertexX.clone(), model.vertexY.clone(), model.vertexZ.clone(),
            model.faceColour.clone(), model.faceAlpha == null ? new byte[model.faceCount] : model.faceAlpha.clone());
    }
}
