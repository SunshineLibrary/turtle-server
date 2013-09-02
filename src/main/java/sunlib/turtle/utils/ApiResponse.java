package sunlib.turtle.utils;

import com.google.gson.Gson;
import sunlib.turtle.models.Cacheable;
import sunlib.turtle.models.CachedText;

import java.util.HashMap;
import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */

public class ApiResponse {

    public static final String CONTENT_TYPE = "Content-Type";
    public boolean success;
    public String uri;
    public String ts;
    public Map<String, String> requestHeaders = new HashMap<String, String>();
    public Map<String, String> responseHeaders = new HashMap<String, String>();
    public Cacheable content;
    public String mime;

    public ApiResponse(boolean success, Cacheable result) {
        this.success = success;
        this.content = result;
    }

    public ApiResponse mime(String mime) {
        this.responseHeaders.put(CONTENT_TYPE, mime);
        return this;
    }

    public String getContentType() {
        return this.responseHeaders.get(CONTENT_TYPE);
    }

    public Cacheable getData() {
        return content;
    }

    public <T> T fromJSON(Class<T> c) {
        if (content instanceof CachedText) {
            return new Gson().fromJson((String) content.getContent(), c);
        }
        return null;
    }

}
