package sunlib.turtle;

import java.util.Map;

/**
 * User: fxp
 * Date: 13-7-31
 * Time: PM4:14
 */
public class Cache {

    private static Map<String, Object> data;

    public static Object get(String id) {
        return data.get(id);
    }

    public static void put(String id, Object obj, int time) {
        data.put(id, obj);
    }

}
