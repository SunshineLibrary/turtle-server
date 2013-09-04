package sunlib.turtle.models;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: fxp
 * Date: 13-8-10
 * Time: PM5:09
 */
public class CachedFile implements Cacheable {

    public String key;
    public File file;
    public String mime;

    public CachedFile(String key, File file) {
        this.key = key;
        this.file = file;
    }

    public CachedFile(String key, InputStream in) throws IOException {
        this.key = key;
        File tmp = File.createTempFile("turtle_temp_", ".file");
        FileUtils.copyInputStreamToFile(in, tmp);
        this.file = tmp;
    }

    @Override
    public String getCacheId() {
        return key;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Long getTimeStamp() {
        return null;
    }

    @Override
    public Object getContent() {
        return file;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
