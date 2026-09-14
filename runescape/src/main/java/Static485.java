import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.graphics.JavaObjSprite;
import com.jagex.graphics.Toolkit;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static485 {

    @OriginalMember(owner = "client!pf", name = "C", descriptor = "[I")
    public static int[] occlusionDepthBuffer;

    @OriginalMember(owner = "client!pf", name = "a", descriptor = "(Lclient!hda;ILclient!ha;)V")
    public static void method9415(@OriginalArg(0) Component component, @OriginalArg(2) Toolkit toolkit) {
        @Pc(38) boolean local38 = ObjTypeList.instance.getCachedSprite(component.objWearCol ? PlayerEntity.self.playerModel : null, toolkit, component.objNumMode, component.invObject, component.outline, component.invCount, component.graphicShadow | 0xFF000000) == null;
        if (local38) {
            JavaToolkit.objSprites.addLast(new JavaObjSprite(component.invObject, component.invCount, component.outline, component.graphicShadow | 0xFF000000, component.objNumMode, component.objWearCol));
            InterfaceManager.redraw(component);
        }
    }

}
