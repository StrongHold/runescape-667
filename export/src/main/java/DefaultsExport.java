import com.jagex.core.io.Packet;
import com.jagex.game.runetek6.config.defaults.AudioDefaults;
import com.jagex.game.runetek6.config.defaults.DefaultsGroup;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.game.runetek6.config.defaults.MapDefaults;
import com.jagex.game.runetek6.config.defaults.WearposDefaults;
import com.jagex.js5.Js5Archive;
import type.Values;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Writes the groups of the defaults archive the client decodes, each one file of every field its
 * decoder reads under the client's name: the map's ({@code MapDefaults}), the audio's
 * ({@code AudioDefaults}) and the worn slots' ({@code WearposDefaults}). It also checks the graphics
 * defaults ({@code GraphicsDefaults}), which {@code exportHitmarks} writes.
 *
 * <p>Each group's decoder reads every code in one loop of its own, so each group is read here by
 * a reader of its codes too, which stops at a code it does not know or at bytes it leaves unread,
 * and the values it reads are checked against the client's own decoder. The client also leaves the
 * microtransaction and error groups undecoded.
 */
public final class DefaultsExport {

    /**
     * The codes of the graphics defaults: their payloads are read only to check the group.
     */
    private static final int GRAPHICS_HITMARK_POSITIONS = 1;

    private DefaultsExport() {
        /* empty */
    }

    public static void write(File cache, Path directory) throws Exception {
        Files.createDirectories(directory);
        var defaults = Cache.js5(cache, Js5Archive.DEFAULTS);

        var map = map(defaults.getfile(DefaultsGroup.MAP));
        MapDefaults.decode(defaults.getfile(DefaultsGroup.MAP));
        same("map", map.get("skyboxes"), Values.of(MapDefaults.skyboxes));
        same("map", map.get("maleTitleEnums"), Values.of(MapDefaults.maleTitleEnums));
        same("map", map.get("femaleTitleEnums"), Values.of(MapDefaults.femaleTitleEnums));
        write(directory.resolve("map.json"), map);

        var audio = audio(defaults.getfile(DefaultsGroup.AUDIO));
        AudioDefaults.decode(defaults.getfile(DefaultsGroup.AUDIO));
        same("audio", audio.get("themeMusic"), AudioDefaults.themeMusic);
        write(directory.resolve("audio.json"), audio);

        var wearpos = wearpos(defaults.getfile(DefaultsGroup.WEARPOS));
        var client = new WearposDefaults(defaults);
        same("wearpos", wearpos.get("hidden"), Values.of(client.hidden));
        same("wearpos", wearpos.get("leftHandSlot"), client.leftHandSlot);
        same("wearpos", wearpos.get("rightHandSlot"), client.rightHandSlot);
        same("wearpos", wearpos.get("animationHiddenLeftHandSlots"), Values.of(client.animationHiddenLeftHandSlots));
        same("wearpos", wearpos.get("animationHiddenRightHandSlots"), Values.of(client.animationHiddenRightHandSlots));
        write(directory.resolve("wearpos.json"), wearpos);

        checkGraphics(defaults.getfile(DefaultsGroup.GRAPHICS));
        new GraphicsDefaults(defaults);
        System.out.println("wrote the map, audio and worn slot defaults to " + directory.toAbsolutePath().normalize()
            + ", and checked the graphics defaults, which exportHitmarks writes");
    }

    /**
     * The map's defaults: the six textures of the default sky box ({@code skyboxes}) and the
     * enums of the titles a male and a female player can take, an empty list where the group gives
     * none.
     */
    private static Map<String, Object> map(byte[] data) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("skyboxes", List.of());
        fields.put("maleTitleEnums", List.of());
        fields.put("femaleTitleEnums", List.of());
        var packet = new Packet(data);
        for (var code = packet.g1(); code != 0; code = packet.g1()) {
            if (code == 1) {
                var textures = new ArrayList<Integer>();
                for (var i = 0; i < 6; i++) {
                    textures.add(packet.g2());
                }
                fields.put("skyboxes", textures);
            } else if (code == 4) {
                fields.put("maleTitleEnums", ids(packet));
            } else if (code == 5) {
                fields.put("femaleTitleEnums", ids(packet));
            } else {
                throw unknown("map", code, data);
            }
        }
        return ended("map", packet, data, fields);
    }

    /**
     * The audio's defaults: the song the client plays at the start, the first that the group
     * gives. A group that gives several stops the export, as the client would drop the rest.
     */
    private static Map<String, Object> audio(byte[] data) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("themeMusic", -1);
        var packet = new Packet(data);
        var given = 0;
        for (var code = packet.g1(); code != 0; code = packet.g1()) {
            if (code == 1) {
                fields.put("themeMusic", packet.g2());
                given++;
            } else {
                throw unknown("audio", code, data);
            }
        }
        if (given > 1) {
            throw new IllegalStateException("The audio defaults give " + given + " theme songs, and the client plays the first.");
        }
        return ended("audio", packet, data, fields);
    }

    /**
     * The worn slots' defaults: the slots each slot hides ({@code hidden}), the slots of the left
     * and the right hand, and the slots a sequence that shows an object in either hand hides.
     */
    private static Map<String, Object> wearpos(byte[] data) {
        var fields = new LinkedHashMap<String, Object>();
        fields.put("hidden", List.of());
        fields.put("leftHandSlot", -1);
        fields.put("rightHandSlot", -1);
        fields.put("animationHiddenLeftHandSlots", List.of());
        fields.put("animationHiddenRightHandSlots", List.of());
        var packet = new Packet(data);
        for (var code = packet.g1(); code != 0; code = packet.g1()) {
            if (code == 1) {
                fields.put("hidden", bytes(packet));
            } else if (code == 3) {
                fields.put("leftHandSlot", packet.g1());
            } else if (code == 4) {
                fields.put("rightHandSlot", packet.g1());
            } else if (code == 5) {
                fields.put("animationHiddenLeftHandSlots", bytes(packet));
            } else if (code == 6) {
                fields.put("animationHiddenRightHandSlots", bytes(packet));
            } else {
                throw unknown("wearpos", code, data);
            }
        }
        return ended("wearpos", packet, data, fields);
    }

    /**
     * Stops at a code of the graphics defaults that the file {@code exportHitmarks} writes cannot
     * hold, or at bytes the group leaves unread. Code 4 gives nothing, and the group must not hold
     * it, as the file has no field for it.
     */
    private static void checkGraphics(byte[] data) {
        var packet = new Packet(data);
        var positions = 4;
        for (var code = packet.g1(); code != 0; code = packet.g1()) {
            if (code == GRAPHICS_HITMARK_POSITIONS) {
                packet.pos += positions * 4;
            } else if (code == 2) {
                packet.g2();
            } else if (code == 3) {
                positions = packet.g1();
            } else if (code == 5 || code == 6) {
                packet.g3();
            } else if (code == 7) {
                for (var i = 0; i < 10 * 4; i++) {
                    packet.g2();
                    var colours = packet.g2();
                    packet.pos += colours * 2;
                }
            } else if (code == 8 || code == 10) {
                /* empty */
            } else if (code == 9 || code == 11) {
                packet.g1();
            } else {
                throw unknown("graphics", code, data);
            }
        }
        ended("graphics", packet, data, Map.of());
    }

    private static List<Integer> ids(Packet packet) {
        var ids = new ArrayList<Integer>();
        var count = packet.g1();
        for (var i = 0; i < count; i++) {
            var id = packet.g2();
            ids.add(id == 65535 ? -1 : id);
        }
        return ids;
    }

    private static List<Integer> bytes(Packet packet) {
        var values = new ArrayList<Integer>();
        var count = packet.g1();
        for (var i = 0; i < count; i++) {
            values.add(packet.g1());
        }
        return values;
    }

    private static IllegalStateException unknown(String group, int code, byte[] data) {
        return new IllegalStateException("The " + group + " defaults hold code " + code
            + ", which no field of the export holds. The group is " + HexFormat.of().formatHex(data) + ".");
    }

    private static Map<String, Object> ended(String group, Packet packet, byte[] data, Map<String, Object> fields) {
        if (packet.pos != data.length) {
            throw new IllegalStateException("The " + group + " defaults end at byte " + packet.pos + " of " + data.length + ".");
        }
        return fields;
    }

    private static void same(String group, Object read, Object client) {
        if (!Objects.equals(read, client)) {
            throw new IllegalStateException("The " + group + " defaults read " + read + " where the client reads " + client + ".");
        }
    }

    private static void write(Path file, Map<String, Object> fields) throws Exception {
        Files.writeString(file, Json.write(fields), StandardCharsets.UTF_8);
    }
}
