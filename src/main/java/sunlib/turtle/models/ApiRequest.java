package sunlib.turtle.models;

import fi.iki.elonen.NanoHTTPD;
import sunlib.turtle.api.SunApiMatcher;

import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Method;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */
public class ApiRequest {

    public Method method;
    public String uri;
    public Map<String, String> params;
    public Type type;

    public ApiRequest(String uri, Method method, Map<String, String> header, Map<String, String> params, Map<String, String> files) {
        this.method = method;
        this.params = params;
        this.uri = uri;
        // Get request type from SunApiProvider
        //this.target = MaterialFactory.get(uri, params);
        if (params != null && params.size() > 0) {
            uri = uri + "?" + params.get(NanoHTTPD.QUERY_STRING_PARAMETER);
//            params.remove(NanoHTTPD.QUERY_STRING_PARAMETER);
//            StringBuilder paramsPart = new StringBuilder();
//            paramsPart.append("?");
//            for (String key : params.keySet()) {
//                paramsPart.append(key)
//                        .append("=")
//                        .append(params.get(key));
//            }
//            uri = uri + paramsPart.toString();
        }
        this.type = SunApiMatcher.getMatcher().match(method, uri);
    }

    public String getCacheId() {
        return uri;
    }

    public ApiRequest param(String key, String value) {
        params.put(key, value);
        return this;
    }

    public ApiRequest params(Map<String, String> params) {
        params.putAll(params);
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", method, uri, params);
    }

    public static enum Type {
        GET, GET_CACHE, POST_CACHE, BATCH_CACHE
    }
}
