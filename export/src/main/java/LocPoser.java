import com.jagex.core.constants.LocShapes;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.loctype.LocType;

/**
 * Builds a location the client animates, as {@code LocEntity.model} builds it every frame.
 *
 * The client builds an animated location through {@code LocType.wallModel}: it copies the
 * location's model, poses the copy with the animator, and only then bends it to the ground under
 * it and moves it as its type says. So a location on a slope is bent after it is posed, and every
 * frame here goes through the same method, with the animator held at that frame. The still model
 * goes through it too, with no animator.
 */
public final class LocPoser implements Poser {

    private final LocType type;
    private final LocEntity entity;
    private final JavaToolkit toolkit;
    private final int functionMask;

    /**
     * @param type the type the location is built as, which for a location that takes the look of
     *     another is the one it has taken.
     * @param functionMask what the client asks of the model, such as that it is left unlit.
     */
    public LocPoser(LocType type, LocEntity entity, JavaToolkit toolkit, int functionMask) {
        this.type = type;
        this.entity = entity;
        this.toolkit = toolkit;
        this.functionMask = functionMask;
    }

    @Override
    public JavaModel still() {
        return build(null);
    }

    @Override
    public JavaModel posed(SequenceAnimator animator) {
        return build(animator);
    }

    private JavaModel build(Animator animator) {
        var floor = LocGround.floor(entity.underwater, entity.virtualLevel);
        var ceiling = LocGround.ceiling(entity.underwater, entity.virtualLevel);
        var diagonal = entity.shape == LocShapes.CENTREPIECE_DIAGONAL;
        var model = type.wallModel(diagonal ? entity.rotation + 4 : entity.rotation, entity.entity.z,
            diagonal ? LocShapes.CENTREPIECE_STRAIGHT : entity.shape, entity.entity.x, ceiling, animator, toolkit,
            floor, null, functionMask, floor.averageHeight(entity.entity.x, entity.entity.z));
        if (model == null) {
            throw new IllegalStateException("Location " + type.id + " has a mesh the cache does not hold.");
        }
        return (JavaModel) model;
    }
}
