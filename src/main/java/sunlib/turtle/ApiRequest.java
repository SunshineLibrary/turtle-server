package sunlib.turtle;

import java.util.HashMap;
import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */
public class ApiRequest {

    public String target;
    public Map<String, Object> params;

    public ApiRequest(String target) {
        this.target = target;
        this.params = new HashMap<String, Object>();
    }

    public ApiRequest param(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public ApiRequest params(Map<String, Object> params) {
        params.putAll(params);
        return this;
    }
}
