package sunlib.turtle.cache.file;

import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

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

    File tmpFolder = Files.createTempDir();

    public TempFileCache() {
        super();
        System.out.println("Temp folder:" + tmpFolder.getAbsolutePath());
    }

    @Override
    public File get(String key) {
        File ret = new File(tmpFolder, key);
        if (!ret.exists()) {
            ret = null;
        }
        return ret;
    }

    @Override
    protected void put(String key, File file, long timestamp) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        try {
            Files.move(file, new File(tmpFolder, key));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
