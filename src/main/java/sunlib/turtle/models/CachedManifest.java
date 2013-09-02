package sunlib.turtle.models;

import java.util.ArrayList;
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
    public List<ManifestItem> manifest = new ArrayList<ManifestItem>();

    public CachedManifest(String key, List<ManifestItem> manifest) {
        this.key = key;
        this.manifest = manifest;
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
