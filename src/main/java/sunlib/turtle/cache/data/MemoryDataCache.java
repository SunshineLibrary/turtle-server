package sunlib.turtle.cache.data;

import sunlib.turtle.models.CachedText;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

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
    protected void put(CachedText text) {
        mData.put(text.getCacheId(), text);
    }
}
