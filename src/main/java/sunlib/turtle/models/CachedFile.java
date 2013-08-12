package sunlib.turtle.models;

import java.io.File;

/**
 * User: fxp
 * Date: 13-8-10
 * Time: PM5:09
 */
public class CachedFile implements Cacheable {

    private File realFile;
    private String key;

    public CachedFile(String key, File file) {
        this.key = key;
        this.realFile = file;
    }

    @Override
    public String getCacheId() {
        return key;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Long getTimeStamp() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getContent() {
        return realFile;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
