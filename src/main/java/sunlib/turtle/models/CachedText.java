package sunlib.turtle.models;

import java.util.HashMap;
import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-10
 * Time: PM5:09
 */
public class CachedText implements Cacheable {

    public String key;
    public String content;
    private static Map<Integer, String> HTTP_STATUS = new HashMap<Integer, String>() {{
        put(400, "bad request");
        put(506, "offline");
    }};

    public CachedText(String key, String content) {
        this.key = key;
        this.content = content;
    }

    @Override
    public String getCacheId() {
        return key;
    }

    @Override
    public Long getTimeStamp() {
        return null;
    }

    @Override
    public Object getContent() {
        return content;
    }

    public static CachedText getStatus(int statusCode) {
        return new CachedText("HTTP " + statusCode, HTTP_STATUS.get(statusCode));
    }
}
