package sunlib.turtle.cache;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

public interface Cache {

    public Object get(String key);

    public void put(String key, Object data, long timestamp);
}
