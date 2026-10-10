import type.ConfigKind;
import type.TypeFile;

import java.io.File;
import java.util.Map;

/**
 * The file of one entry of a config type, read out of a cache on disk ({@link TypeFile}), for the
 * exports that write a type with the models and sequences it names.
 */
public final class TypeFiles {

    public static Map<String, Object> of(File cache, ConfigKind<?> kind, int id) {
        var archive = kind.archive();
        var data = Cache.js5(cache, archive.archive()).getfile(archive.fileOf(id), archive.groupOf(id));
        if (data == null) {
            throw new IllegalStateException("The cache holds no " + kind.directory() + " " + id + ".");
        }
        return TypeFile.of(kind, id, data);
    }

    private TypeFiles() {
        /* empty */
    }
}
