import com.jagex.js5.js5;
import type.Archives;

import java.io.File;

/**
 * The archives of a cache on disk, for the config kinds ({@link Cache#js5}).
 */
public final class CacheArchives implements Archives {

    private final File cache;

    public CacheArchives(File cache) {
        this.cache = cache;
    }

    @Override
    public js5 js5(int archive) {
        return Cache.js5(cache, archive);
    }
}
