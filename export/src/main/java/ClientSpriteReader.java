import com.jagex.IndexedImage;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.js5;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Reads sprites out of the cache the way the client reads them.
 *
 * Each group of the sprites archive is one sprite, held in its file 0. The client reads it with
 * {@code IndexedImage.load}, which gives one paletted image for each frame, and its toolkits turn
 * each into colours by the same rule ({@code JavaToolkit.createSprite}, {@code GlToolkit.createSprite}):
 * a frame that carries no alpha shows palette entry 0 as a hole and every other entry opaque, and a
 * frame that carries alpha takes each pixel's alpha byte as it is, whatever its palette entry. The
 * client turns a palette colour of 0 into 1 as it reads the palette, so that only entry 0 is ever a
 * hole, and that colour of 1 is kept here.
 */
public final class ClientSpriteReader {

    /** The file of a sprite's group that holds it. */
    private static final int SPRITE_FILE = 0;

    private static final int OPAQUE = 0xFF000000;

    /**
     * The names the client asks the sprites archive for, by the hash of the name the archive's
     * index keeps for each group ({@code Sprites.init}, {@code Fonts.init}).
     */
    private static final List<String> NAMES = List.of(
        "hitbar_default",
        "timerbar_default",
        "headicons_pk",
        "headicons_prayer",
        "hint_headicons",
        "hint_mapmarkers",
        "mapflag",
        "cross",
        "mapdots",
        "scrollbar",
        "name_icons",
        "floorshadows",
        "compass",
        "otherlevel",
        "hint_mapedge",
        "p11_full",
        "p12_full",
        "b12_full"
    );

    private final js5 sprites;
    private final Map<Integer, String> names;

    public ClientSpriteReader(File cache) {
        this.sprites = Cache.js5(cache, Js5Archive.SPRITES);
        this.names = new HashMap<>();
        for (var name : NAMES) {
            var id = sprites.getgroupid(name);
            if (id != -1) {
                names.put(id, name);
            }
        }
    }

    /** The client's own archive, for a check that reads the same sprites through the client. */
    public js5 archive() {
        return sprites;
    }

    /** Every group id the sprites archive holds, in order. */
    public int[] ids(File cache) throws Exception {
        return Cache.groupsOf(Cache.index(cache, Js5Archive.SPRITES));
    }

    /** The name the client asks for a sprite by, where it asks by name. */
    public Optional<String> name(int id) {
        return Optional.ofNullable(names.get(id));
    }

    /** The sprite the client reads from a group, or nothing where the group holds no sprite. */
    public Optional<SpriteArchive> read(int id) {
        var images = IndexedImage.load(sprites, id, SPRITE_FILE);
        if (images == null) {
            return Optional.empty();
        } else {
            var frames = Arrays.stream(images).map(ClientSpriteReader::frame).toList();
            return Optional.of(new SpriteArchive(id, name(id), frames));
        }
    }

    private static SpriteFrame frame(IndexedImage image) {
        var pixels = new int[image.width * image.height];
        for (var i = 0; i < pixels.length; i++) {
            pixels[i] = colour(image, i);
        }
        return new SpriteFrame(image.offX1, image.offY1, image.width, image.height, image.offsetX(), image.offsetY(), image.alpha != null, pixels);
    }

    private static int colour(IndexedImage image, int pixel) {
        var rgb = image.palette[image.raster[pixel] & 0xFF];
        if (image.alpha != null) {
            return (image.alpha[pixel] & 0xFF) << 24 | rgb;
        } else if (rgb == 0) {
            return 0;
        } else {
            return OPAQUE | rgb;
        }
    }
}
