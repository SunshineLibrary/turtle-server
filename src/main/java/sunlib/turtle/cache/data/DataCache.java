package sunlib.turtle.cache.data;

import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.Cacheable;
import sunlib.turtle.models.CachedText;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */
public abstract class DataCache implements Cache {

    public abstract CachedText get(String key);

    protected abstract void put(CachedText text);

    public void put(Cacheable cacheable) {
        if (cacheable instanceof CachedText) {
            put((CachedText) cacheable);
        }
    }
}
