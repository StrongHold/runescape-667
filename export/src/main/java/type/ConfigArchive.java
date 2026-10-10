package type;

/**
 * Where the entries of a config type are kept in the cache: every file of one group of an
 * archive, numbered by file, or every file of every group of an archive, numbered by the group
 * and the file together, the file in the low bits. In either case the ids start at a first id.
 *
 * @param archive the archive (`Js5Archive`)
 * @param group the one group, or {@link #EVERY_GROUP}
 * @param fileBits how many low bits of an id are the file, where every group holds entries
 * @param firstId the id of the archive's first entry, which the type list adds to the file's number
 */
public record ConfigArchive(int archive, int group, int fileBits, int firstId) {

    public static final int EVERY_GROUP = -1;

    /**
     * One group of an archive, as the type list reads it ({@code configClient.getfile(id, group)}).
     */
    public static ConfigArchive oneGroup(int archive, int group) {
        return new ConfigArchive(archive, group, 0, 0);
    }

    /**
     * Every group of an archive, as a type list that splits an id into a group and a file reads it
     * ({@code configClient.getfile(id & mask, id >>> fileBits)}).
     */
    public static ConfigArchive everyGroup(int archive, int fileBits) {
        return new ConfigArchive(archive, EVERY_GROUP, fileBits, 0);
    }

    /**
     * The same entries numbered from another first id, as the quick chat lists number the entries
     * of their global archive from 32,768.
     */
    public ConfigArchive from(int first) {
        return new ConfigArchive(archive, group, fileBits, first);
    }

    public boolean holdsEveryGroup() {
        return group == EVERY_GROUP;
    }

    /**
     * The group that holds an entry.
     */
    public int groupOf(int id) {
        if (holdsEveryGroup()) {
            return (id - firstId) >>> fileBits;
        } else {
            return group;
        }
    }

    /**
     * The file of its group that is an entry.
     */
    public int fileOf(int id) {
        if (holdsEveryGroup()) {
            return (id - firstId) & ((1 << fileBits) - 1);
        } else {
            return id - firstId;
        }
    }

    /**
     * The id of the entry that is a file of a group.
     */
    public int id(int group, int file) {
        if (holdsEveryGroup()) {
            return firstId + (group << fileBits | file);
        } else {
            return firstId + file;
        }
    }
}
