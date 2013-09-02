package sunlib.turtle.cache.file;

import com.google.common.io.Files;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * User: fxp
 * Date: 13-8-31
 * Time: PM2:21
 */
public class TempFileManager implements AbstractFileManager {
    public static File tmpFolder;
    private static TempFileManager instance = new TempFileManager();

    public TempFileManager() {
        try {
            instance.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TempFileManager getInstance() {
        return instance;
    }

    public File put(String key, File file) {
        File dst = new File(tmpFolder, DigestUtils.md5Hex(key));
        try {
            FileUtils.copyFile(file, dst);
        } catch (IOException e) {
            e.printStackTrace();
            dst = null;
        }
        return dst;
    }

    public File get(String key) {
        File ret = new File(tmpFolder, DigestUtils.md5Hex(key));
        return (ret.exists()) ? ret : null;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void setUp() throws Exception {
        tmpFolder = Files.createTempDir();
    }

}
