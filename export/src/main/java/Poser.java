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
}
