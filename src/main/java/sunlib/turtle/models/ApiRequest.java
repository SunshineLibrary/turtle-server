package sunlib.turtle.models;

import sunlib.turtle.module.MaterialFactory;

import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Method;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */
public class ApiRequest {

    public Method method;
    public Material target;
    public Map<String, String> params;

    public ApiRequest(String uri, Method method, Map<String, String> header, Map<String, String> params, Map<String, String> files) {
        this.method = method;
        this.params = params;
        this.target = MaterialFactory.get(uri, params);
    }

    public ApiRequest param(String key, String value) {
        params.put(key, value);
        return this;
    }

    public ApiRequest params(Map<String, String> params) {
        params.putAll(params);
        return this;
    }

    public static enum Type {
        GET, GET_CACHE, POST_CACHE, BATCH_CACHE
    }
}
