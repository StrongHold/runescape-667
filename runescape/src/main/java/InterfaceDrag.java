import com.jagex.Client;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.event.mouse.MouseMonitor;

public final class InterfaceDrag {

    @OriginalMember(owner = "client!taa", name = "b", descriptor = "(Z)V")
    public static void tick() {
        InterfaceManager.redraw(InterfaceManager.dragSource);
        InterfaceManager.dragTicks++;
        if (Static702.aBoolean797 && InterfaceManager.aBoolean428) {
            @Pc(30) int offsetX = 0;
            @Pc(32) int offsetY = 0;
            if (OrthoMode.toolkitActive) {
                offsetX = OrthoMode.method2283();
                offsetY = Static422.method5771();
            }
            @Pc(46) int dragX = offsetX + MouseMonitor.instance.getRecordedX();
            @Pc(52) int dragY = MouseMonitor.instance.getRecordedY() + offsetY;
            dragX -= InterfaceManager.dragStartX;
            dragY -= InterfaceManager.dragStartY;
            if (InterfaceManager.dragParentX > dragX) {
                dragX = InterfaceManager.dragParentX;
            }
            if (InterfaceManager.dragSource.width + dragX > InterfaceManager.dragParentX - -InterfaceManager.dragLayer.width) {
                dragX = InterfaceManager.dragLayer.width + InterfaceManager.dragParentX - InterfaceManager.dragSource.width;
            }
            if (dragY < InterfaceManager.dragParentY) {
                dragY = InterfaceManager.dragParentY;
            }
            if (InterfaceManager.dragParentY + InterfaceManager.dragLayer.height < dragY - -InterfaceManager.dragSource.height) {
                dragY = InterfaceManager.dragParentY + InterfaceManager.dragLayer.height - InterfaceManager.dragSource.height;
            }
            @Pc(119) int mouseX = InterfaceManager.dragLayer.scrollX + dragX - InterfaceManager.dragParentX;
            @Pc(127) int mouseY = InterfaceManager.dragLayer.scrollY + dragY - InterfaceManager.dragParentY;
            @Pc(197) HookRequest hook;
            if (MouseMonitor.instance.isDown()) {
                if (InterfaceManager.dragSource.dragDeadTime < InterfaceManager.dragTicks) {
                    @Pc(141) int deltaX = dragX - InterfaceManager.dragLastX;
                    @Pc(146) int deltaY = dragY - InterfaceManager.dragLastY;
                    if (InterfaceManager.dragSource.dragDeadZone < deltaX || -InterfaceManager.dragSource.dragDeadZone > deltaX || InterfaceManager.dragSource.dragDeadZone < deltaY || deltaY < -InterfaceManager.dragSource.dragDeadZone) {
                        InterfaceManager.dragging = true;
                    }
                }
                if (InterfaceManager.dragSource.onDrag != null && InterfaceManager.dragging) {
                    hook = new HookRequest();
                    hook.source = InterfaceManager.dragSource;
                    hook.mouseX = mouseX;
                    hook.mouseY = mouseY;
                    hook.arguments = InterfaceManager.dragSource.onDrag;
                    ScriptRunner.executeHookInner(hook);
                    return;
                }
            } else {
                if (InterfaceManager.dragging) {
                    InterfaceManager.endTargetMode();
                    if (InterfaceManager.dragSource.onDragComplete != null) {
                        hook = new HookRequest();
                        hook.target = InterfaceManager.dragTarget;
                        hook.mouseX = mouseX;
                        hook.source = InterfaceManager.dragSource;
                        hook.mouseY = mouseY;
                        hook.arguments = InterfaceManager.dragSource.onDragComplete;
                        ScriptRunner.executeHookInner(hook);
                    }
                    if (InterfaceManager.dragTarget != null && getDragDepthLayer(InterfaceManager.dragSource) != null) {
                        Static710.ifButtonDSend(InterfaceManager.dragSource, InterfaceManager.dragTarget);
                    }
                } else if ((Client.mouseButtons == 1 || MiniMenu.topEntryIsIfButtonX1()) && MiniMenu.innerEntryCount > 2) {
                    MiniMenu.method6223(InterfaceManager.dragStartX + InterfaceManager.dragLastX, InterfaceManager.dragLastY + InterfaceManager.dragStartY);
                } else if (MiniMenu.isPopulated()) {
                    MiniMenu.method6223(InterfaceManager.dragLastX + InterfaceManager.dragStartX, InterfaceManager.dragLastY + InterfaceManager.dragStartY);
                }
                InterfaceManager.dragSource = null;
            }
        } else if (InterfaceManager.dragTicks > 1) {
            InterfaceManager.dragSource = null;
        }
    }

    @OriginalMember(owner = "client!client", name = "a", descriptor = "(Lclient!hda;)Lclient!hda;")
    public static Component getDragDepthLayer(@OriginalArg(0) Component component) {
        @Pc(4) int depth = InterfaceManager.serverActiveProperties(component).getDragDepth();
        if (depth == 0) {
            return null;
        }
        for (@Pc(11) int i = 0; i < depth; i++) {
            component = InterfaceList.list(component.layer);
            if (component == null) {
                return null;
            }
        }
        return component;
    }
}
