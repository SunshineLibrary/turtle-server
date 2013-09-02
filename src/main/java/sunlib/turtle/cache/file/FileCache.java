package sunlib.turtle.cache.file;

import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.Cacheable;
import sunlib.turtle.models.CachedFile;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */
public abstract class FileCache implements Cache {

    public abstract CachedFile get(String key);

    protected abstract void put(CachedFile file);

    public void put(Cacheable cacheable) {
        if (cacheable instanceof CachedFile) {
            put((CachedFile) cacheable);
        }
    }

}
