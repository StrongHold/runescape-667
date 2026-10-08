import com.jagex.ChangeLocationRequest;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static544 {

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(Lclient!hma;ZI)V")
    public static void processLocChange(@OriginalArg(0) ChangeLocationRequest request, @OriginalArg(1) boolean customisation) {
        if (request.pendingRemoval) {
            if (request.originalId < 0 || Static235.loadedModels(request.originalId, request.originalShape)) {
                if (customisation) {
                    Static706.setLocCustomisation(request.layer, null, request.z, request.level, request.x);
                } else {
                    Static235.setLocChange(request.x, request.z, request.level, request.originalId, request.originalShape, request.originalRotation, -1, request.layer);
                }

                request.unlink();
            }
        } else if (request.pendingApply && request.x >= 1 && request.z >= 1 && Static720.mapWidth - 2 >= request.x && Static501.mapLength - 2 >= request.z && (request.id < 0 || Static235.loadedModels(request.id, request.shape))) {
            if (customisation) {
                Static706.setLocCustomisation(request.layer, request.customisation, request.z, request.level, request.x);
            } else {
                Static235.setLocChange(request.x, request.z, request.level, request.id, request.shape, request.rotation, -1, request.layer);
            }

            request.pendingApply = false;

            if (!customisation && request.originalId == request.id && request.originalId == -1) {
                request.unlink();
            } else if (!customisation && request.originalId == request.id && request.rotation == request.originalRotation && request.shape == request.originalShape) {
                request.unlink();
            }
        }
    }
}
