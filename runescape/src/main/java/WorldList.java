import com.jagex.core.io.ConnectionInfo;
import com.jagex.core.io.Packet;
import com.jagex.core.util.SystemTimer;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.world.Country;
import com.jagex.game.world.GameWorld;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class WorldList {

    @OriginalMember(owner = "client!qf", name = "H", descriptor = "Z")
    public static boolean loaded = false;

    @OriginalMember(owner = "client!pja", name = "Z", descriptor = "I")
    public static int minId;

    @OriginalMember(owner = "client!wc", name = "z", descriptor = "I")
    public static int maxId;

    @OriginalMember(owner = "client!dfa", name = "d", descriptor = "[Lclient!pq;")
    public static GameWorld[] worlds;

    @OriginalMember(owner = "client!paa", name = "e", descriptor = "I")
    public static int count;

    @OriginalMember(owner = "client!lu", name = "G", descriptor = "I")
    public static int checksum;

    @OriginalMember(owner = "client!kma", name = "m", descriptor = "Lclient!ge;")
    public static Packet packet;

    @OriginalMember(owner = "client!kq", name = "i", descriptor = "[Lclient!pq;")
    public static GameWorld[] activeWorlds = new GameWorld[0];

    @OriginalMember(owner = "client!ema", name = "o", descriptor = "Z")
    public static boolean fetching = false;

    @OriginalMember(owner = "client!li", name = "g", descriptor = "J")
    public static long lastReply;

    @OriginalMember(owner = "client!bv", name = "n", descriptor = "I")
    public static int iterator = 999999;

    @OriginalMember(owner = "client!bw", name = "Q", descriptor = "Z")
    public static boolean pingWorlds = false;

    @OriginalMember(owner = "client!tha", name = "a", descriptor = "(II)Lclient!pq;")
    public static GameWorld list(@OriginalArg(1) int id) {
        return loaded && id >= minId && id <= maxId ? worlds[id - minId] : null;
    }

    @OriginalMember(owner = "client!jj", name = "a", descriptor = "(ILclient!ge;)V")
    public static void decode(@OriginalArg(1) Packet packet) {
        @Pc(7) int countyCount = packet.gsmart();
        Country.countries = new Country[countyCount];
        for (@Pc(12) int i = 0; i < countyCount; i++) {
            Country.countries[i] = new Country();
            Country.countries[i].flag = packet.gsmart();
            Country.countries[i].name = packet.gjstr2();
        }

        minId = packet.gsmart();
        maxId = packet.gsmart();
        count = packet.gsmart();
        worlds = new GameWorld[maxId + 1 - minId];

        for (@Pc(63) int i = 0; i < count; i++) {
            @Pc(68) int id = packet.gsmart();
            @Pc(76) GameWorld world = worlds[id] = new GameWorld();
            world.country = packet.g1();
            world.flags = packet.g4();
            world.id = minId + id;
            world.activity = packet.gjstr2();
            world.address = packet.gjstr2();
        }

        checksum = packet.g4();
        loaded = true;
    }

    @OriginalMember(owner = "client!dja", name = "a", descriptor = "([BBZ)V")
    public static void decodeWorldList(@OriginalArg(0) byte[] data, @OriginalArg(2) boolean update) {
        if (packet == null) {
            packet = new Packet(20000);
        }

        packet.pdata(data.length, data, 0);

        if (update) {
            decodeWorlds(packet.data);
            activeWorlds = new GameWorld[count];

            @Pc(31) int ptr = 0;
            for (@Pc(33) int id = minId; id <= maxId; id++) {
                @Pc(38) GameWorld world = list(id);

                if (world != null) {
                    activeWorlds[ptr++] = world;
                }
            }

            fetching = false;
            lastReply = SystemTimer.safetime();
            packet = null;
        }
    }

    @OriginalMember(owner = "client!vk", name = "a", descriptor = "(I[B)Z")
    public static boolean decodeWorlds(@OriginalArg(1) byte[] arg0) {
        @Pc(8) Packet packet = new Packet(arg0);

        @Pc(12) int version = packet.g1();
        if (version != 2) {
            return false;
        }

        @Pc(30) boolean worldsUpdated = packet.g1() == 1;
        if (worldsUpdated) {
            decode(packet);
        }

        decodePopulations(packet);
        return true;
    }

    @OriginalMember(owner = "client!via", name = "a", descriptor = "(ILclient!ge;)V")
    public static void decodePopulations(@OriginalArg(1) Packet arg1) {
        for (@Pc(1) int i = 0; i < count; i++) {
            @Pc(6) int id = arg1.gsmart();
            @Pc(10) int population = arg1.g2();
            if (population == 65535) {
                population = -1;
            }

            if (worlds[id] != null) {
                worlds[id].population = population;
            }
        }
    }

    @OriginalMember(owner = "client!fda", name = "a", descriptor = "(BIIZIZI)V")
    public static void quicksort(@OriginalArg(1) int end, @OriginalArg(2) int start, @OriginalArg(3) boolean primaryDescending, @OriginalArg(4) int primaryComparison, @OriginalArg(5) boolean secondaryDescending, @OriginalArg(6) int secondaryComparison) {
        if (end <= start) {
            return;
        }

        @Pc(13) int pivotIndex = (start + end) / 2;
        @Pc(15) int mid = start;

        @Pc(19) GameWorld pivot = activeWorlds[pivotIndex];
        activeWorlds[pivotIndex] = activeWorlds[end];
        activeWorlds[end] = pivot;

        for (@Pc(31) int i = start; i < end; i++) {
            if (WorldComparator.compare(pivot, primaryComparison, secondaryComparison, secondaryDescending, activeWorlds[i], primaryDescending) <= 0) {
                @Pc(47) GameWorld temp = activeWorlds[i];
                activeWorlds[i] = activeWorlds[mid];
                activeWorlds[mid++] = temp;
            }
        }

        activeWorlds[end] = activeWorlds[mid];
        activeWorlds[mid] = pivot;
        quicksort(mid - 1, start, primaryDescending, primaryComparison, secondaryDescending, secondaryComparison);
        quicksort(end, mid + 1, primaryDescending, primaryComparison, secondaryDescending, secondaryComparison);
    }

    @OriginalMember(owner = "client!wv", name = "a", descriptor = "(B)Lclient!pq;")
    public static GameWorld first() {
        iterator = 0;
        return next();
    }

    @OriginalMember(owner = "client!rja", name = "d", descriptor = "(I)Lclient!pq;")
    public static GameWorld next() {
        return iterator < activeWorlds.length ? activeWorlds[iterator++] : null;
    }

    @OriginalMember(owner = "client!gia", name = "a", descriptor = "(BZIIZ)V")
    public static void quicksortWorldList(@OriginalArg(3) int primaryComparison, @OriginalArg(1) boolean primaryDescending, @OriginalArg(2) int secondaryComparison, @OriginalArg(4) boolean secondaryDescending) {
        quicksort(activeWorlds.length - 1, 0, primaryDescending, primaryComparison, secondaryDescending, secondaryComparison);
        Static419.pingWorldIndex = 0;
        Static522.pingRequest = null;
    }

    @OriginalMember(owner = "client!ep", name = "f", descriptor = "(I)V")
    public static void selectAutoWorld() {
        if (GameShell.signLink.signed && ConnectionInfo.auto.world != -1) {
            client.connectTo(ConnectionInfo.auto.world, ConnectionInfo.auto.address);
        }
    }
}
