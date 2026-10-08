import java.util.Optional;

/**
 * Whether a baked model's poses are played by bones, and the joints they are played on.
 *
 * <p>Bones are used where every vertex of every pose lands within {@link SkinWriter#TOLERANCE_FINE} of
 * where the client puts it when its joint's move, turn and scale are played back. Otherwise the
 * poses are kept as morph targets, which are exact and large.
 */
public record Bones(SkinWriter.Joints joints, double deviationFine) {

    public static Optional<Bones> of(PoseBaker baker) {
        var skinning = baker.skinning();
        if (skinning.isEmpty() || baker.poses().isEmpty()) {
            return Optional.empty();
        }

        var joints = SkinWriter.joints(skinning.get(), baker.framePoses(), baker.poses());
        var deviationFine = SkinWriter.worstDeviationFine(skinning.get(), joints, baker.framePoses(), baker.poses());
        if (deviationFine > SkinWriter.TOLERANCE_FINE) {
            return Optional.empty();
        }
        return Optional.of(new Bones(joints, deviationFine));
    }

    public static String describe(PoseBaker baker, Optional<Bones> bones) {
        if (baker.poses().isEmpty()) {
            return "no poses";
        } else if (bones.isPresent()) {
            var chains = bones.get().joints().chained().stream().filter(chained -> chained).count();
            return "bones: " + (bones.get().joints().count() - 1) + " joints, " + chains + " of them chains, worst "
                + String.format("%.2f", bones.get().deviationFine()) + " units from the client's pose";
        } else {
            var skinning = baker.skinning();
            var deviationFine = skinning.map(held -> SkinWriter.worstDeviationFine(held,
                SkinWriter.joints(held, baker.framePoses(), baker.poses()), baker.framePoses(), baker.poses())).orElse(0.0);
            return "morph targets: bones would land " + String.format("%.2f", deviationFine) + " units from the client's pose";
        }
    }
}
