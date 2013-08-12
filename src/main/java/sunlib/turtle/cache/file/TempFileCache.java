package sunlib.turtle.cache.file;

import com.google.common.io.Files;
import sunlib.turtle.models.CachedFile;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class TempFileCache extends FileCache {

    public static final File tmpFolder = new File("./cached/");

    public TempFileCache() {
        super();
        if (!tmpFolder.exists()) {
            tmpFolder.mkdir();
        }
        System.out.println("Temp folder:" + tmpFolder.getAbsolutePath());
    }

    @Override
    public CachedFile get(String key) {
        File ret = new File(tmpFolder, key);
        if (!ret.exists()) {
            ret = null;
        }
        return new CachedFile(key, ret);
    }

    @Override
    protected void put(CachedFile file) {
        try {
            Files.move((File) file.getContent(), new File(tmpFolder, file.getCacheId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
