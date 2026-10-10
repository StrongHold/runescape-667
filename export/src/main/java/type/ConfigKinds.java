package type;

import com.jagex.js5.Js5Archive;
import type.data.EnumKind;
import type.data.InvKind;
import type.data.ParamKind;
import type.data.QuestKind;
import type.data.StructKind;
import type.entity.BasKind;
import type.entity.IdkKind;
import type.entity.NpcKind;
import type.entity.SeqKind;
import type.entity.SpotAnimKind;
import type.ground.FloorOverlayKind;
import type.ground.FloorUnderlayKind;
import type.ground.LightKind;
import type.ground.SkyBoxKind;
import type.ground.SkyBoxSphereKind;
import type.loc.LocKind;
import type.map.MapElementKind;
import type.map.MsiKind;
import type.obj.ObjKind;
import type.particle.BillboardKind;
import type.particle.EffectorKind;
import type.particle.EmitterKind;
import type.ui.CursorKind;
import type.ui.HitmarkKind;
import type.ui.QuickChatCategoryKind;
import type.var.VarClanKind;
import type.var.VarClanSettingKind;
import type.var.VarbitKind;
import type.var.VarcKind;
import type.var.VarpKind;

import java.util.List;
import java.util.Map;

/**
 * Every config type the client decodes: those the export writes one file an entry of, and those
 * another export writes into one file of their own, which are read here only to check them.
 */
public final class ConfigKinds {

    /**
     * The groups of the config archive that hold entries the client never decodes, each with why,
     * which the export checks the archive against so that no group is left out unseen.
     */
    public static final Map<Integer, String> UNDECODED_CONFIG_GROUPS = Map.ofEntries(
        Map.entry(15, "The client counts its own string variables (VarcstrTypeList) and decodes none."),
        Map.entry(2, "No type list of the client reads this group."),
        Map.entry(7, "No type list of the client reads this group."),
        Map.entry(18, "No type list of the client reads this group."),
        Map.entry(20, "No type list of the client reads this group."),
        Map.entry(21, "No type list of the client reads this group."),
        Map.entry(22, "No type list of the client reads this group."),
        Map.entry(23, "No type list of the client reads this group."),
        Map.entry(24, "No type list of the client reads this group."),
        Map.entry(25, "No type list of the client reads this group."),
        Map.entry(37, "No type list of the client reads this group."),
        Map.entry(38, "No type list of the client reads this group."),
        Map.entry(39, "No type list of the client reads this group."),
        Map.entry(40, "No type list of the client reads this group."),
        Map.entry(41, "No type list of the client reads this group."),
        Map.entry(42, "No type list of the client reads this group."),
        Map.entry(43, "No type list of the client reads this group."),
        Map.entry(44, "No type list of the client reads this group."),
        Map.entry(45, "No type list of the client reads this group."),
        Map.entry(48, "No type list of the client reads this group."),
        Map.entry(50, "No type list of the client reads this group."),
        Map.entry(51, "No type list of the client reads this group."),
        Map.entry(53, "No type list of the client reads this group.")
    );

    /**
     * The kinds the export writes one file an entry of, but those whose types are in the default
     * package, which {@code TypeExport} adds.
     */
    public static List<ConfigKind<?>> written(Archives archives) {
        return List.of(
            new NpcKind(archives),
            new LocKind(archives),
            new ObjKind(archives),
            new SeqKind(),
            new SpotAnimKind(),
            new BasKind(archives),
            new IdkKind(),
            new FloorOverlayKind(archives),
            new FloorUnderlayKind(),
            new LightKind(),
            new MapElementKind(),
            new MsiKind(),
            new BillboardKind(),
            new CursorKind(),
            new QuickChatCategoryKind(Js5Archive.QUICKCHAT, 0),
            new QuickChatCategoryKind(Js5Archive.QUICKCHAT_GLOBAL, QuickChatCategoryKind.GLOBAL),
            new InvKind(),
            new ParamKind(),
            new StructKind(),
            new EnumKind(),
            new QuestKind(),
            new VarpKind(),
            new VarbitKind(),
            new VarcKind(),
            new VarClanKind(),
            new VarClanSettingKind()
        );
    }

    /**
     * The kinds another export writes into one file of their own: the hit splats
     * ({@code exportHitmarks}), the sky boxes with their spheres ({@code exportSkyBoxes}) and the
     * particle emitters and effectors (the texture export).
     */
    public static List<ConfigKind<?>> checked() {
        return List.of(
            new HitmarkKind(),
            new SkyBoxKind(),
            new SkyBoxSphereKind(),
            new EmitterKind(),
            new EffectorKind()
        );
    }

    private ConfigKinds() {
        /* empty */
    }
}
