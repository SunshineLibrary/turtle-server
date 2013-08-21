package sunlib.turtle.cache.file;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
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

    public static final File tmpFolder = new File("cached");

    public TempFileCache() {
        super();
        if (!tmpFolder.exists()) {
            tmpFolder.mkdir();
        }
        System.out.println("Temp folder:" + tmpFolder.getAbsolutePath());
    }

    @Override
    public CachedFile get(String key) {
        CachedFile ret = null;
        File cachedFile = new File(tmpFolder, DigestUtils.md5Hex(key));
        if (cachedFile.exists()) {
            try {
                ret = new CachedFile(key, FileUtils.openInputStream(cachedFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    protected void put(CachedFile file) {
        try {
            String fileName = DigestUtils.md5Hex(file.getCacheId());
            FileUtils.copyInputStreamToFile(file.in, new File(tmpFolder, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
