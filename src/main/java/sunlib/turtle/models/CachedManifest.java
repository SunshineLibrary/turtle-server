package sunlib.turtle.models;

import java.util.HashMap;
import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-21
 * Time: PM2:50
 */
public class CachedManifest {

    public String key;
    public boolean is_cached = false;
    public int progress = 0;
    public Map<String, String> manifest = new HashMap<String, String>();

}
