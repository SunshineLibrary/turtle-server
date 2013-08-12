package sunlib.turtle.cache.file;

import sunlib.turtle.models.CachedFile;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class AndroidFileCache extends FileCache {
    @Override
    public CachedFile get(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void put(CachedFile file) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
