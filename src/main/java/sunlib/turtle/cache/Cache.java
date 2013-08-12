package sunlib.turtle.cache;

import sunlib.turtle.models.Cacheable;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

public interface Cache {

    public Cacheable get(String key);

    public void put(Cacheable cacheable);
}
