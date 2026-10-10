package type.entity;

import com.jagex.core.constants.ModeGame;
import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.Js5ConfigGroup;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.game.runetek6.config.defaults.WearposDefaults;
import com.jagex.js5.Js5Archive;
import type.Archives;
import type.Codes;
import type.ConfigArchive;
import type.ConfigKind;
import type.Decoded;
import type.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The base animation sets ({@code BASTypeList}): every field under the name the client gives it,
 * one file for each set however many NPCs share it: the sequences it names, which are in the
 * sequence library, the sprites of its bars, how it follows hills and turns, and how it wears what
 * it holds. A list the set leaves unset is empty, and a slot of a worn table it leaves empty is an
 * empty list.
 */
public final class BasKind implements ConfigKind<BASType> {

    private static final int LANGUAGE = 0;

    private static final Codes CODES = Codes.of()
        .code(1, "ready", "walk")
        .code(2, "crawl")
        .code(3, "crawlFollowTurn180")
        .code(4, "crawlFollowTurnCcw")
        .code(5, "crawlFollowTurnCw")
        .code(6, "run")
        .code(7, "runFollowTurn180")
        .code(8, "runFollowTurnCcw")
        .code(9, "runFollowTurnCw")
        .code(26, "hillWidth", "hillHeight")
        .code(27, "wornTransformations")
        .code(28, "invObjSlots")
        .code(29, "yawAcceleration")
        .code(30, "yawMaxSpeed")
        .code(31, "rollAcceleration")
        .code(32, "rollMaxSpeed")
        .code(33, "rollTargetAngle")
        .code(34, "pitchAcceleration")
        .code(35, "pitchMaxSpeed")
        .code(36, "pitchTargetAngle")
        .code(37, "movementAcceleration")
        .code(38, "readyTurnCcw")
        .code(39, "readyTurnCw")
        .code(40, "walkFollowTurn180")
        .code(41, "walkFollowTurnCcw")
        .code(42, "walkFollowTurnCw")
        .code(43, "hitbarSprite")
        .code(44, "timerbarSprite")
        .code(45, "characterHeight")
        .code(46, "crawlTurnCcw")
        .code(47, "crawlTurnCw")
        .code(48, "runTurnCcw")
        .code(49, "runTurnCw")
        .code(50, "walkTurnCcw")
        .code(51, "walkTurnCw")
        .code(52, "readyAnimations", "readyAnimationWeights")
        .code(53, "animateShadow")
        .code(54, "hillMaxAngleX", "hillMaxAngleY")
        .code(55, "maxWornRotation")
        .code(56, "graphicOffsets");

    private static final Map<String, String> UNWRITTEN = Map.of(
        "typeList", "The type list the set belongs to.",
        "transformMatrices", "The client builds the matrices of the worn transformations once for each toolkit.",
        "toolkitIndex", "The toolkit the matrices were built for.",
        "idleAnimationTotalWeight", "The sum of readyAnimationWeights, which the decoder adds up."
    );

    private final BASTypeList list;

    public BasKind(Archives archives) {
        this.list = new BASTypeList(ModeGame.RUNESCAPE, LANGUAGE, archives.js5(Js5Archive.CONFIG),
            new WearposDefaults(archives.js5(Js5Archive.DEFAULTS)));
    }

    @Override
    public String directory() {
        return "bas";
    }

    @Override
    public ConfigArchive archive() {
        return ConfigArchive.oneGroup(Js5Archive.CONFIG, Js5ConfigGroup.BASTYPE);
    }

    @Override
    public Codes codes() {
        return CODES;
    }

    @Override
    public BASType create(int id) {
        var type = new BASType();
        type.typeList = list;
        return type;
    }

    @Override
    public void decode(BASType type, int id, int code, Packet packet) {
        type.decode(packet, code);
    }

    @Override
    public Map<String, Object> json(Decoded<BASType> decoded) {
        var set = decoded.decoded();
        var data = new LinkedHashMap<String, Object>();
        data.put("ready", set.ready);
        data.put("readyTurnCw", set.readyTurnCw);
        data.put("readyTurnCcw", set.readyTurnCcw);
        data.put("readyAnimations", Lists.ints(set.readyAnimations));
        data.put("readyAnimationWeights", Lists.ints(set.readyAnimationWeights));
        data.put("walk", set.walk);
        data.put("walkTurnCw", set.walkTurnCw);
        data.put("walkTurnCcw", set.walkTurnCcw);
        data.put("walkFollowTurn180", set.walkFollowTurn180);
        data.put("walkFollowTurnCw", set.walkFollowTurnCw);
        data.put("walkFollowTurnCcw", set.walkFollowTurnCcw);
        data.put("run", set.run);
        data.put("runTurnCw", set.runTurnCw);
        data.put("runTurnCcw", set.runTurnCcw);
        data.put("runFollowTurn180", set.runFollowTurn180);
        data.put("runFollowTurnCw", set.runFollowTurnCw);
        data.put("runFollowTurnCcw", set.runFollowTurnCcw);
        data.put("crawl", set.crawl);
        data.put("crawlTurnCw", set.crawlTurnCw);
        data.put("crawlTurnCcw", set.crawlTurnCcw);
        data.put("crawlFollowTurn180", set.crawlFollowTurn180);
        data.put("crawlFollowTurnCw", set.crawlFollowTurnCw);
        data.put("crawlFollowTurnCcw", set.crawlFollowTurnCcw);
        data.put("animateShadow", set.animateShadow);
        data.put("hillWidth", set.hillWidth);
        data.put("hillHeight", set.hillHeight);
        data.put("hillMaxAngleX", set.hillMaxAngleX);
        data.put("hillMaxAngleY", set.hillMaxAngleY);
        data.put("yawAcceleration", set.yawAcceleration);
        data.put("yawMaxSpeed", set.yawMaxSpeed);
        data.put("rollAcceleration", set.rollAcceleration);
        data.put("rollMaxSpeed", set.rollMaxSpeed);
        data.put("rollTargetAngle", set.rollTargetAngle);
        data.put("pitchAcceleration", set.pitchAcceleration);
        data.put("pitchMaxSpeed", set.pitchMaxSpeed);
        data.put("pitchTargetAngle", set.pitchTargetAngle);
        data.put("movementAcceleration", set.movementAcceleration);
        data.put("characterHeight", set.characterHeight);
        data.put("hitbarSprite", set.hitbarSprite);
        data.put("timerbarSprite", set.timerbarSprite);
        data.put("wornTransformations", Lists.slots(set.wornTransformations));
        data.put("maxWornRotation", Lists.ints(set.maxWornRotation));
        data.put("graphicOffsets", Lists.slots(set.graphicOffsets));
        data.put("invObjSlots", Lists.ints(set.invObjSlots));
        return data;
    }

    @Override
    public Map<String, String> unwritten() {
        return UNWRITTEN;
    }
}
