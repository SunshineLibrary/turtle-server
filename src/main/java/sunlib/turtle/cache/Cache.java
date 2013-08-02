package sunlib.turtle.cache;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public interface Cache {

    public Object get(String key);

    public void put(String key, Object data, int timestamp);
}
