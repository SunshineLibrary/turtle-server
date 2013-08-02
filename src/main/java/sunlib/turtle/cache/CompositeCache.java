package sunlib.turtle.cache;

import sunlib.turtle.cache.data.DataCache;
import sunlib.turtle.cache.file.FileCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class CompositeCache implements Cache{

    @Inject DataCache mDataCache;
    @Inject FileCache mFileCache;

    @Override
    public Object get(String key) {
        String data = mDataCache.get(key);
        if (data != null) {
            return data;
        }

        File file = mFileCache.get(key);
        if (file != null) {
            return file;
        }

        return null;
    }

    @Override
    public void put(String key, Object data, int timestamp) {
        if (data instanceof String) {
            mDataCache.put(key, data, timestamp);
        } else if (data instanceof File) {
            mFileCache.put(key, data, timestamp);
        }
    }
}
