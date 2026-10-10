import com.jagex.core.constants.LocShapes;
import com.jagex.core.constants.ModeGame;
import com.jagex.game.Animator;
import com.jagex.game.runetek6.config.flotype.FloorOverlayTypeList;
import com.jagex.game.runetek6.config.flutype.FloorUnderlayTypeList;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.game.runetek6.config.vartype.bit.VarBitTypeListClient;
import com.jagex.game.runetek6.config.vartype.player.VarPlayerTypeListClient;
import com.jagex.graphics.Mesh;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import type.loc.LocKind;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds the model of a location type, for one of its shapes, as the location's own asset: the
 * part of what the client builds that is the same wherever the location is placed.
 *
 * The client builds a placed location in {@code LocType.model}: it reads and merges the meshes
 * of the shape, mirrors the model if the type says so, turns it for its rotation, swaps its
 * colours and textures, scales it and moves it by the type's offsets. Then {@code modelAndShadow}
 * bends it to the ground and moves it again. Everything from the turn on depends on where and
 * how the location is placed, so the asset stops before it, and an importer does the rest as
 * {@link LocPlacing} describes.
 */
public final class ClientLocReader {

    private static final int LANGUAGE = 0;

    /**
     * Every transform the client may ask of a location's model, so that a copy of the asset can
     * be turned, scaled, moved and bent as the client does, plus the functions that keep it
     * unlit for the export to read.
     */
    public static final int EVERY_FUNCTION = 0x1F01F | 0x4000 | 0x8000 | 0x80000 | 0x800 | 0x10000;

    private static final int FIRST_FULL_SCALE_VERSION = 13;
    private static final int AMBIENT_BASE = 64;
    private static final int CONTRAST_BASE = 850;

    private final ClientModelReader models;
    private final js5 meshes;
    private final File cache;
    private final LocKind kind;

    public ClientLocReader(File cache) {
        this.cache = cache;
        this.kind = new LocKind(new CacheArchives(cache));
        this.models = new ClientModelReader(cache);
        this.meshes = Cache.js5(cache, Js5Archive.MODELS);

        var config = Cache.js5(cache, Js5Archive.CONFIG);
        FloorOverlayTypeList.instance = new FloorOverlayTypeList(ModeGame.RUNESCAPE, LANGUAGE, config);
        FloorUnderlayTypeList.instance = new FloorUnderlayTypeList(ModeGame.RUNESCAPE, LANGUAGE, config);
        LocTypeList.instance = new LocTypeList(ModeGame.RUNESCAPE, LANGUAGE, true,
            Cache.js5(cache, Js5Archive.CONFIG_LOC), meshes);
        VarBitTypeListClient.instance = new VarBitTypeListClient(ModeGame.RUNESCAPE, LANGUAGE,
            Cache.js5(cache, Js5Archive.CONFIG_STRUCT));
        VarPlayerTypeListClient.instance = new VarPlayerTypeListClient(ModeGame.RUNESCAPE, LANGUAGE, config);
        TimedVarDomain.instance = new TimedVarDomain();
        Animator.setSeqTL(new SeqTypeList(ModeGame.RUNESCAPE, LANGUAGE, Cache.js5(cache, Js5Archive.CONFIG_SEQ),
            Cache.js5(cache, Js5Archive.ANIMS), Cache.js5(cache, Js5Archive.BASES)));
    }

    public ClientModelReader models() {
        return models;
    }

    public JavaToolkit toolkit() {
        return models.toolkit();
    }

    public Js5TextureSource textures() {
        return models.textures();
    }

    public LocType type(int id) {
        return LocTypeList.instance.list(id);
    }

    /**
     * The file of a location type, every field of it as the decoder reads it ({@link LocKind}).
     */
    public Map<String, Object> typeFile(int id) {
        return TypeFiles.of(cache, kind, id);
    }

    /**
     * The shapes a type has a model for, in the order it lists them. Most types have one.
     */
    public static List<Integer> shapes(LocType type) {
        var shapes = new ArrayList<Integer>();
        if (type.modelShapes != null) {
            for (var shape : type.modelShapes) {
                shapes.add((int) shape);
            }
        }
        return List.copyOf(shapes);
    }

    /**
     * Builds the asset of one shape of a type, still or posed, as a {@link Poser} for the baker.
     *
     * @param turned whether the asset is turned 45 degrees before it is posed, which is what the
     *     client does to a wall decoration placed diagonally, before the frames of its sequence
     *     move it. The frames are not turned with it, so the asset for such a placement is turned
     *     first and animated after, and an importer does not turn it again.
     */
    public Poser poser(LocType type, int shape, boolean turned) {
        return new Poser() {
            @Override
            public JavaModel still() {
                return build(type, shape, null, turned, scaledInAsset(type));
            }

            @Override
            public JavaModel posed(SequenceAnimator animator) {
                return build(type, shape, animator, turned, scaledInAsset(type));
            }

            @Override
            public JavaModel unscaledStill() {
                return build(type, shape, null, turned, false);
            }

            @Override
            public double[] scale() {
                return scaledInAsset(type)
                    ? new double[] {type.resizex / (double) FULL_SCALE, type.resizey / (double) FULL_SCALE,
                        type.resizez / (double) FULL_SCALE}
                    : new double[] {1, 1, 1};
            }
        };
    }

    /**
     * Whether the type's scale is in its asset rather than left to the importer. The client scales
     * a location before it poses it, so a frame's move is not scaled, and bones that play the
     * frames on an unscaled asset would move too far once the importer scaled it. An animated
     * location is therefore scaled in its asset, with its bones worked out to match, and its
     * extras tell the importer there is nothing left to scale. A location that never moves is
     * left to the importer, which scales it along the axes of the world as the client does.
     */
    public static boolean scaledInAsset(LocType type) {
        return type.hasAnimations();
    }

    private static final int FULL_SCALE = 128;

    /**
     * Whether a shape of a type needs an asset of its own for a diagonal placement: a wall
     * decoration that animates, as {@link #poser} explains.
     */
    public static boolean needsTurnedAsset(LocType type, int shape) {
        return type.hasAnimations() && shape == LocShapes.WALLDECOR_STRAIGHT_NOOFFSET;
    }

    /**
     * The asset model: the shape's meshes merged, mirrored if the type says so, recoloured and
     * retextured, at rotation 0, before the scale, the offsets and the bend. Nothing where the
     * type names a mesh the cache does not hold, as the client builds nothing then either.
     *
     * The client poses an animated location after it has turned, scaled and moved it, and the
     * pose here is taken before, which is the same thing for a turn, as the client turns the
     * frames to match, but not for a scale that the frames would move the parts of.
     * {@link LocPlacing} checks each placement against the client for that reason.
     */
    private JavaModel build(LocType type, int shape, SequenceAnimator animator, boolean turned, boolean scaled) {
        var index = ClientLocReader.shapes(type).indexOf(shape);
        if (index == -1 || type.models[index].length == 0) {
            return null;
        }

        var parts = new Mesh[type.models[index].length];
        for (var i = 0; i < parts.length; i++) {
            var mesh = Mesh.load(type.models[index][i] & 0xFFFF, meshes);
            if (mesh == null) {
                return null;
            }
            if (mesh.version < FIRST_FULL_SCALE_VERSION) {
                mesh.upscale();
            }
            parts[i] = mesh;
        }
        var mesh = parts.length == 1 ? parts[0] : new Mesh(parts, parts.length);

        var functions = EVERY_FUNCTION | (animator == null ? 0 : animator.functionMask());
        var built = toolkit().createModel(mesh, functions, LocTypeList.instance.featureMask,
            type.ambient + AMBIENT_BASE, type.contrast + CONTRAST_BASE);
        var model = (JavaModel) built.copy((byte) (animator == null ? 0 : 1), functions, true);

        if (type.mirror) {
            model.v();
        }
        if (type.recol_s != null) {
            for (var i = 0; i < type.recol_s.length; i++) {
                if (type.recol_d_palette == null || type.recol_d_palette.length <= i) {
                    model.ia(type.recol_s[i], type.recol_d[i]);
                } else {
                    model.ia(type.recol_s[i], LocType.clientpalette[type.recol_d_palette[i] & 0xFF]);
                }
            }
        }
        if (type.retex_s != null) {
            for (var i = 0; i < type.retex_s.length; i++) {
                model.aa(type.retex_s[i], type.retex_d[i]);
            }
        }
        if (type.colourShiftPercentage != 0) {
            model.adjustColours(type.targetHue, type.targetSaturation, type.targetLightness,
                type.colourShiftPercentage & 0xFF);
        }

        if (turned) {
            model.k(LocPlacing.EIGHTH_TURN);
        }
        if (scaled && (type.resizex != FULL_SCALE || type.resizey != FULL_SCALE || type.resizez != FULL_SCALE)) {
            model.O(type.resizex, type.resizey, type.resizez);
        }
        if (animator != null) {
            animator.animate(model, 0);
        }
        return model;
    }
}
