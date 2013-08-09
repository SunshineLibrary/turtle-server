package sunlib.turtle.cache.file;

import sunlib.turtle.cache.Cache;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */
public abstract class FileCache implements Cache {

    public abstract File get(String key);

    protected abstract void put(String key, File file, long timestamp);

    public void put(String key, Object file, long timestamp) {
        put(key, (File) file, timestamp);
    }

}
