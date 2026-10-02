/**
 * Builds one thing the client animates, either as it stands with nothing playing or as it stands
 * at one frame of a sequence.
 *
 * Both models are built by the client's own code through the same path, so that they differ by
 * the pose alone, and both are handed out in buffers the client fills again on the next call.
 */
public interface Poser {

    /**
     * The model with no sequence playing.
     */
    JavaModel still();

    /**
     * The model posed at the frame an animator is held at.
     */
    JavaModel posed(SequenceAnimator animator);

    /**
     * The still model before any scale the client applies after posing, which a frame's
     * transforms are measured against. The same as {@link #still} unless the client scales.
     */
    default JavaModel unscaledStill() {
        return still();
    }

    /**
     * The scale the client applies after posing, along x, y and z, as a fraction.
     */
    default double[] scale() {
        return new double[] {1, 1, 1};
    }
}
