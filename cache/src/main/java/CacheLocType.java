import com.beust.jcommander.Parameter;
import com.jagex.core.io.BufferedFile;
import com.jagex.game.runetek6.config.loctype.LocType;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.core.io.FileOnDisk;
import com.jagex.core.io.Packet;
import com.jagex.js5.FileSystem_Client;
import com.jagex.js5.Js5Archive;
import com.jagex.js5.Js5Index;
import com.jagex.js5.js5;

import java.io.File;

/**
 * Lists the models one kind of location is built from.
 *
 * A location names a model for each shape it may be put down as, and a flight of stairs is put
 * down as several. A model that is never drawn is easier to find when every model the location
 * could have asked for is in front of you.
 */
public final class CacheLocType {

    /** How many kinds of location one group of the configuration holds. */
    private static final int PER_GROUP = 256;

    /** How many things the player may be offered to do with a location. */
    private static final int OPS = 5;

    public static final class Args implements Arguments {

        @Parameter(names = "--loc", description = "Which kind of location to list", required = true)
        private int loc;

        @Parameter(names = "--help", help = true, description = "Print this message")
        private boolean help;

        @Override
        public boolean help() {
            return help;
        }
    }

    public static void main(String[] arguments) throws Exception {
        var parsed = CommandLine.parse("listLocType", new Args(), arguments);

        if (parsed.isPresent()) {
            run(parsed.get());
        }
    }

    private static void run(Args args) throws Exception  {
        var id = args.loc;
        var cache = Cache.standard();
        var index = readIndex(cache, Js5Archive.CONFIG_LOC);

        var group = id / PER_GROUP;
        var packed = store(cache, Js5Archive.CONFIG_LOC).read(group);

        if (packed == null) {
            System.out.println("the cache holds no group " + group);
            return;
        }

        var file = fileFrom(js5.decodeContainer(packed), index, group, id % PER_GROUP);
        if (file == null) {
            System.out.println("group " + group + " holds no kind " + id);
            return;
        }

        report(id, new Packet(file));
    }

    /**
     * What the kind says about itself, read by the client's own reader.
     *
     * The list a kind belongs to decides one or two of the answers, so an empty one stands in for
     * it. Nothing here asks the list for another kind, which is the only thing it would need the
     * cache for.
     */
    private static void report(int id, Packet packet) {
        var type = new LocType();
        type.typeList = new LocTypeList(null, 0, true, null, null);
        type.ops = new String[OPS];
        type.decode(packet);
        type.postDecode();

        System.out.println("location " + id + " is called " + type.name
            + ", hillchange " + type.hillchange + ", hillskew " + type.hillskew
            + ", width " + type.width + ", length " + type.length
            + ", mirror " + type.mirror
            + ", ambient " + type.ambient + ", contrast " + type.contrast);

        if (type.models == null) {
            System.out.println("  it names no models of its own");
            return;
        }

        for (var shape = 0; shape < type.models.length; shape++) {
            var models = new StringBuilder();
            for (var model : type.models[shape]) {
                models.append(models.length() == 0 ? "" : ", ").append(model);
            }

            System.out.println("  shape " + type.modelShapes[shape] + ": " + models);
        }
    }

    /**
     * One file out of a group, where a group holds more than one.
     */
    static byte[] fileFrom(byte[] data, Js5Index index, int group, int wanted) {
        return Cache.split(data, index, group).get(wanted);
    }

    static FileSystem_Client store(File cache, int archive) throws Exception {
        var data = new FileOnDisk(new File(cache, "main_file_cache.dat2"), "r", Long.MAX_VALUE);
        var index = new FileOnDisk(
            new File(cache, "main_file_cache.idx" + archive), "r", Long.MAX_VALUE);

        return new FileSystem_Client(archive,
            new BufferedFile(data, 5200, 0), new BufferedFile(index, 6000, 0), 1 << 22);
    }

    static Js5Index readIndex(File cache, int archive) throws Exception {
        var data = new FileOnDisk(new File(cache, "main_file_cache.dat2"), "r", Long.MAX_VALUE);
        var master = new FileOnDisk(
            new File(cache, "main_file_cache.idx255"), "r", Long.MAX_VALUE);

        var store = new FileSystem_Client(255,
            new BufferedFile(data, 5200, 0), new BufferedFile(master, 6000, 0), 500000);

        var packed = store.read(archive);
        if (packed == null) {
            throw new IllegalStateException("The cache holds no index for archive " + archive + ".");
        }

        return new Js5Index(packed, Packet.getcrc(packed.length, packed), null);
    }

    private CacheLocType() {
        /* empty */
    }
}
