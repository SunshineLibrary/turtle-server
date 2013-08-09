package sunlib.turtle.cache.data;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class MemoryDataCache extends DataCache {

    private static Map<String, String> mData;

    public MemoryDataCache() {
        mData = new HashMap<String, String>();
    }

    @Override
    public String get(String key) {
        return mData.get(key);
    }

    @Override
    protected void put(String key, String data, long timestamp) {
        mData.put(key, data);
    }
}
