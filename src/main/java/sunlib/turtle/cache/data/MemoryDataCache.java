package sunlib.turtle.cache.data;

import sunlib.turtle.models.CachedText;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class MemoryDataCache extends DataCache {

    private static Map<String, CachedText> mData = new HashMap<String, CachedText>();

    @Override
    public CachedText get(String key) {
        return mData.get(key);
    }

    @Override
    public Set<String> keySet() {
        return mData.keySet();
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void put(CachedText text) {
        mData.put(text.getCacheId(), text);
    }
}
