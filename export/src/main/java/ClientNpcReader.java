import com.jagex.core.constants.ModeGame;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.game.runetek6.config.defaults.WearposDefaults;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.js5.Js5Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Builds NPCs out of the cache the way the client builds one it is about to draw.
 *
 * The client asks {@code NPCType.getModel} for an NPC's model each time it draws one. That reads
 * every mesh the NPC is made of, moves each one as the NPC's base animation set says, merges them,
 * builds a model from them with the NPC's colours and textures swapped, and poses a copy of it
 * with the NPC's sequences. The same method is driven here, on type lists read from the cache and
 * with the software toolkit that {@link ClientModelReader} sets up.
 */
public final class ClientNpcReader {

    /**
     * English, which every NPC's name is read in.
     */
    private static final int LANGUAGE = 0;

    /**
     * Asks the toolkit for copies that it has not lit.
     *
     * Before the software toolkit hands out a copy of a model, it lights the model, which turns
     * each face's colour into a lit colour at each corner and drops the colours, the shading types
     * and the normals the export reads. It leaves a model unlit when the caller names one of the
     * functions that lighting would get in the way of, and 0x10000, keeping each face's shading
     * type, is one of them. It changes nothing about where the vertices go.
     */
    private static final int KEEP_UNLIT = 0x10000;

    /**
     * The scale the client draws an NPC at when its type does not change it.
     */
    private static final int FULL_SCALE = 128;

    private final ClientModelReader models;
    private final NPCTypeList npcs;
    private final BASTypeList bases;

    public ClientNpcReader(File cache) {
        this.models = new ClientModelReader(cache);
        this.npcs = new NPCTypeList(ModeGame.RUNESCAPE, LANGUAGE, true,
            Cache.js5(cache, Js5Archive.CONFIG_NPC), Cache.js5(cache, Js5Archive.MODELS));
        this.bases = new BASTypeList(ModeGame.RUNESCAPE, LANGUAGE, Cache.js5(cache, Js5Archive.CONFIG),
            new WearposDefaults(Cache.js5(cache, Js5Archive.DEFAULTS)));
        Animator.setSeqTL(new SeqTypeList(ModeGame.RUNESCAPE, LANGUAGE, Cache.js5(cache, Js5Archive.CONFIG_SEQ),
            Cache.js5(cache, Js5Archive.ANIMS), Cache.js5(cache, Js5Archive.BASES)));
    }

    public NPCType type(int id) {
        return npcs.list(id);
    }

    public Js5TextureSource textures() {
        return models.textures();
    }

    /**
     * Builds the NPC still, or in the pose an animator is at.
     *
     * The client hands the sequence an NPC stands, turns or walks with to {@code getModel} as its
     * movement animator. It scales the model only after it has posed it, and an NPC the client
     * draws is always posed, because it always has a movement sequence. So the still model is
     * scaled the same way with nothing posed, and differs from each pose by the pose alone.
     */
    public Poser poser(NPCType type) {
        return new Poser() {
            @Override
            public JavaModel still() {
                var model = model(type, null);
                if (type.scaleH != FULL_SCALE || type.scaleV != FULL_SCALE) {
                    model.O(type.scaleH, type.scaleV, type.scaleH);
                }
                return model;
            }

            @Override
            public JavaModel posed(SequenceAnimator animator) {
                return model(type, animator);
            }

            @Override
            public JavaModel unscaledStill() {
                return model(type, null);
            }

            @Override
            public double[] scale() {
                return new double[] {type.scaleH / (double) FULL_SCALE, type.scaleV / (double) FULL_SCALE,
                    type.scaleH / (double) FULL_SCALE};
            }
        };
    }

    /**
     * Each sequence the NPC moves with, named by what its base animation set uses it for, in the
     * order the set holds them. A sequence used for several things is listed once for each.
     */
    public List<Movement> movements(NPCType type) {
        var movements = new ArrayList<Movement>();

        if (type.basId != -1) {
            var set = bases.list(type.basId);
            add(movements, "stand", set.ready);
            if (set.readyAnimations != null) {
                for (var idle : set.readyAnimations) {
                    add(movements, "idle", idle);
                }
            }
            add(movements, "stand turn cw", set.readyTurnCw);
            add(movements, "stand turn ccw", set.readyTurnCcw);
            addGait(movements, "walk", set.walk, set.walkTurnCw, set.walkTurnCcw,
                set.walkFollowTurn180, set.walkFollowTurnCw, set.walkFollowTurnCcw);
            addGait(movements, "run", set.run, set.runTurnCw, set.runTurnCcw,
                set.runFollowTurn180, set.runFollowTurnCw, set.runFollowTurnCcw);
            addGait(movements, "crawl", set.crawl, set.crawlTurnCw, set.crawlTurnCcw,
                set.crawlFollowTurn180, set.crawlFollowTurnCw, set.crawlFollowTurnCcw);
        }

        return List.copyOf(movements);
    }

    /**
     * One sequence of a base animation set, and what the set uses it for.
     */
    public record Movement(String role, int sequence) {
    }

    /**
     * The model the client shows of the NPC's head when it talks (`NPCType.headModel`), with the
     * NPC's colours and textures swapped as on its body, or nothing when the type has no head.
     */
    public Optional<JavaModel> head(NPCType type) {
        if (type.headModels == null) {
            return Optional.empty();
        } else {
            var model = type.headModel(KEEP_UNLIT, null, null, models.toolkit(), null);
            if (model == null) {
                throw new IllegalStateException("NPC " + type.id + " has a head mesh the cache does not hold.");
            }
            return Optional.of((JavaModel) model);
        }
    }

    private JavaModel model(NPCType type, SequenceAnimator animator) {
        var model = type.getModel(null, models.toolkit(), bases, null, 0, null, null, animator, KEEP_UNLIT, null);
        if (model == null) {
            throw new IllegalStateException("NPC " + type.id + " has a mesh the cache does not hold.");
        }
        return (JavaModel) model;
    }

    /**
     * The sequences of one way of moving. The client plays a turn sequence while the NPC turns as
     * it moves. While the NPC keeps facing something it follows, it plays a follow sequence when
     * it moves to one side of the way it faces or away from it.
     */
    private static void addGait(List<Movement> movements, String gait, int straight, int turnCw, int turnCcw,
                                int followTurn180, int followTurnCw, int followTurnCcw) {
        add(movements, gait, straight);
        add(movements, gait + " turn cw", turnCw);
        add(movements, gait + " turn ccw", turnCcw);
        add(movements, gait + " backwards", followTurn180);
        add(movements, gait + " sideways cw", followTurnCw);
        add(movements, gait + " sideways ccw", followTurnCcw);
    }

    private static void add(List<Movement> movements, String role, int sequence) {
        if (sequence != -1) {
            movements.add(new Movement(role, sequence));
        }
    }
}
