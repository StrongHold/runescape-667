import java.util.Optional;

/**
 * Whether a baked model's poses are played by bones, and the joints they are played on.
 *
 * <p>Bones are used where every vertex of every pose lands within {@link SkinWriter#TOLERANCE} of
 * where the client puts it when its joint's move, turn and scale are played back. Otherwise the
 * poses are kept as morph targets, which are exact and large.
 */
public record Bones(SkinWriter.Joints joints, double deviation) {

    public static Optional<Bones> of(PoseBaker baker) {
        var skinning = baker.skinning();
        if (skinning.isEmpty() || baker.poses().isEmpty()) {
            return Optional.empty();
        }

        var joints = SkinWriter.joints(skinning.get(), baker.framePoses(), baker.poses());
        var deviation = SkinWriter.worstDeviation(skinning.get(), joints, baker.framePoses(), baker.poses());
        if (deviation > SkinWriter.TOLERANCE) {
            return Optional.empty();
        }
        return Optional.of(new Bones(joints, deviation));
    }

    public static String describe(PoseBaker baker, Optional<Bones> bones) {
        if (baker.poses().isEmpty()) {
            return "no poses";
        } else if (bones.isPresent()) {
            var chains = bones.get().joints().chained().stream().filter(chained -> chained).count();
            return "bones: " + (bones.get().joints().count() - 1) + " joints, " + chains + " of them chains, worst "
                + String.format("%.2f", bones.get().deviation()) + " units from the client's pose";
        } else {
            var skinning = baker.skinning();
            var deviation = skinning.map(held -> SkinWriter.worstDeviation(held,
                SkinWriter.joints(held, baker.framePoses(), baker.poses()), baker.framePoses(), baker.poses())).orElse(0.0);
            return "morph targets: bones would land " + String.format("%.2f", deviation) + " units from the client's pose";
        }
    }
}
