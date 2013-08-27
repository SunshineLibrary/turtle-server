package sunlib.turtle.utils;

import sunlib.turtle.models.Cacheable;

import java.util.HashMap;
import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */
public class ApiResponse {

    public boolean success;
    public Map<String, String> header = new HashMap<String, String>();
    public Cacheable content;

    public ApiResponse(boolean success, Cacheable result) {
        this.success = success;
        this.content = result;
    }

    public ApiResponse(ApiResponse resp, Cacheable content) {
        this.success = resp.success;
        this.header = resp.header;
        this.content = resp.content;
        this.content = content;
    }

    public Cacheable getData() {
        return content;
    }

}
