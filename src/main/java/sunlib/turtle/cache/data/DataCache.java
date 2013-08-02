package sunlib.turtle.cache.data;

import sunlib.turtle.cache.Cache;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */
public abstract class DataCache implements Cache {

    public abstract String get(String key);

    protected abstract void put(String key, String data, int timestamp);

    public void put(String key, Object data, int timestamp) {
        put(key, (String) data, timestamp);
    }
}
