import com.jagex.core.constants.MainLogicStep;
import com.jagex.game.runetek6.config.fonttype.FontTypeList;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;

public final class Static667 {

    @OriginalMember(owner = "client!vc", name = "a", descriptor = "(ZZLjava/lang/String;I)V")
    public static void setToolkit(@OriginalArg(1) boolean inactive, @OriginalArg(2) String arg1, @OriginalArg(3) int toolkit) {
        Static419.method5757();
        Static231.freeToolkit();
        Static208.method3106();
        Static595.setToolkit(arg1, inactive, toolkit);
        FontTypeList.method2569();
        FontTypeList.method7549(Toolkit.active);
        Fonts.init(Toolkit.active);
        Sprites.init(js5.SPRITES, Toolkit.active);
        MiniMenu.resetSprites();
        MiniMenu.setIcons(Sprites.nameIcons);
        InterfaceManager.redrawAll();
        Static296.updateFeatureMask();
        if (MainLogicManager.step == MainLogicStep.STEP_LOGIN_SCREEN) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGIN_SCREEN_MAP_BUILD);
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOBBY_SCREEN_MAP_BUILD);
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME) {
            MainLogicManager.setStep(MainLogicStep.STEP_LOGGING_IN_FROM_LOBBYSCREEN_TO_GAME_MAP_BUILD);
        } else if (MainLogicManager.step == MainLogicStep.STEP_GAME_SCREEN) {
            MainLogicManager.setStep(MainLogicStep.STEP_GAME_SCREEN_MAP_BUILD);
        } else if (MainLogicManager.step == MainLogicStep.STEP_LOADING_1 || MainLogicManager.step == MainLogicStep.STEP_LOADING_2) {
            Static143.method3572();
        }
    }
}
