package sunlib.turtle.cache.file;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import sunlib.turtle.models.CachedFile;

import javax.inject.Singleton;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class TempFileCache extends FileCache {

    public static final File tmpFolder = new File("cached");
    static FilenameFilter hiddenFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return !name.startsWith(".");
        }
    };

    public TempFileCache() {
        super();
        if (!tmpFolder.exists()) {
            tmpFolder.mkdir();
        }
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
    public Set<String> keySet() {
        File[] files = tmpFolder.listFiles(hiddenFilter);
        Set<String> fileNames = new HashSet<String>();
        for (File file : files) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void put(CachedFile file) {
        try {
            String fileName = DigestUtils.md5Hex(file.getCacheId());
            FileUtils.copyFile(file.file, new File(tmpFolder, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        throw new RuntimeException("not implemented");
    }
}
