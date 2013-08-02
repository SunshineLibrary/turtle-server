package sunlib.turtle.cache.file;

import javax.inject.Singleton;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class TempFileCache extends FileCache {

    @Override
    public File get(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void put(String key, File file, int timestamp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
