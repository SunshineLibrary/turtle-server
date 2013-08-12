package sunlib.turtle.cache;

import sunlib.turtle.cache.data.DataCache;
import sunlib.turtle.cache.file.FileCache;
import sunlib.turtle.models.Cacheable;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class CompositeCache implements Cache {

    @Inject
    DataCache mDataCache;
    @Inject
    FileCache mFileCache;

    @Override
    public Cacheable get(String key) {
        CachedText data = mDataCache.get(key);
        if (data != null) {
            return data;
        }
        CachedFile file = mFileCache.get(key);
        if (file != null) {
            return file;
        }
        return null;
    }

    @Override
    public void put(Cacheable cacheable) {
        if (cacheable instanceof CachedText) {
            mDataCache.put(cacheable);
        } else if (cacheable instanceof CachedFile) {
            mFileCache.put(cacheable);
        } else {
            // TODO Should not be here!
        }
    }
}
