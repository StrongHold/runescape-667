import com.jagex.AnimFrame;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.seqtype.SeqType;

/**
 * An animator that holds one sequence at the start of any one of its frames.
 *
 * The client moves an animator through a sequence a cycle at a time and poses a model with the
 * frame it has reached and with how far it is through it. At the very start of a frame nothing
 * has been tweened towards the next one, so the model takes the frame's pose exactly.
 */
final class SequenceAnimator extends Animator {

    /**
     * The client starts a sequence with no delay and with its frames played in order.
     */
    private static final int NO_DELAY = 0;
    private static final int LOOP_NORMALLY = 0;
    private static final boolean IN_ORDER = false;

    SequenceAnimator(int sequence) {
        super(false);
        update(sequence, NO_DELAY, LOOP_NORMALLY, IN_ORDER);
    }

    SeqType sequence() {
        return getAnimation();
    }

    /**
     * The frame the animator is held at, as the client reads it from the cache.
     */
    AnimFrame frame() {
        return primarySequences.frameset.frames[primarySequences.frame];
    }

    /**
     * Moves to the start of a frame, with the next frame chosen as {@code Animator.tick} chooses
     * it, and says whether the client can find the frame in the cache.
     */
    boolean show(int frame) {
        var sequence = getAnimation();
        currentFrame = frame;
        frameOffset = 0;
        nextFrame = frame + 1;

        if (nextFrame >= sequence.frames.length) {
            nextFrame -= sequence.loopOffset;
            if (nextFrame < 0 || nextFrame >= sequence.frames.length) {
                nextFrame = -1;
            }
        }

        resetSequences();
        return resolveSequences();
    }
}
