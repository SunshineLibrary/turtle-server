package sunlib.turtle.models;

import java.util.List;

/**
 * User: fxp
 * Date: 13-8-21
 * Time: PM2:50
 */
public class CachedManifest implements Cacheable {

    public String key;
    public boolean is_cached = false;
    public int progress = 0;
    public int total;
    public int left;
    public List<ManifestItem> manifest;

    public CachedManifest() {
    }

    @Override
    public String getCacheId() {
        return key;
    }

    @Override
    public Long getTimeStamp() {
        return null;
    }

    @Override
    public Object getContent() {
        return manifest;
    }
}
