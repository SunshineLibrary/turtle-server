package sunlib.turtle.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-26
 * Time: AM11:32
 */
public enum KeyValues {
    GLOBAL;
    Map<String, Serializable> keyValues = new HashMap<String, Serializable>();

    public void put(String key, Serializable value) {
        keyValues.put(key, value);
    }

    public Serializable get(String key) {
        return keyValues.get(key);
    }
}
