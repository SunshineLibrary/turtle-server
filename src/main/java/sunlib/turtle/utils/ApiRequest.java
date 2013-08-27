package sunlib.turtle.utils;

import fi.iki.elonen.NanoHTTPD;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import sunlib.turtle.api.SunExerciseCategorizer;
import sunlib.turtle.api.TurtleAPI;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Method;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */
public class ApiRequest {

    public static SunExerciseCategorizer categorizer = (SunExerciseCategorizer) TurtleAPI.newCategorizer("sunlib-exercise");
    public Method method;
    public String uri;
    public Map<String, String> params = new HashMap<String, String>();
    public Type type;
    public ApiResponse response;

    public ApiRequest(String uri) {
        this.uri = uri;
    }

    public ApiRequest(String uri, Method method, Map<String, String> header, Map<String, String> params, Map<String, String> files) {
        this.method = method;
        this.params = params;
        this.uri = uri;
        // Get request type from SunApiProvider
//        if (params != null && params.size() > 0) {
//            uri = uri + "?" + params.get(NanoHTTPD.QUERY_STRING_PARAMETER);
//        }
//        this.type = SunApiMatcher.getMatcher().match(method, uri);
        this.type = categorizer.getType(method, uri, params);
    }

    public ApiRequest(ApiRequest req) {
        this.uri = req.uri;
        for (String key : req.params.keySet()) {
            this.params.put(key, req.params.get(key));
        }
    }

    public ApiRequest setParameter(String name, String value) {
        params.put(name, value);
        return this;
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

    public void get(Class c) {
//        ApiResponse resp= get();
//        if(resp.getData() instanceof  CachedText){
//
//        } else{
//            return null;
//        }
//        new Gson().fromJson();
    }

    public ApiResponse get() {
        ApiResponse ret = null;
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("192.168.3.19").setPort(3000).setPath(uri);
        if (params != null) {
            for (String param : params.keySet()) {
                if (!"callback".equals(param)) {
                    builder.setParameter(param, params.get(param));
                }
            }
        }
        URI uri = null;
        try {
            uri = builder.build();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            HttpResponse resp = httpclient.execute(httpget);
            HttpEntity entity = resp.getEntity();
            Header[] contentTypes = resp.getHeaders("content-type");
            if (contentTypes != null) {
                String type = contentTypes[0].getValue();
                if (!isJSON(type)) {
                    // A file need cache
                    ret = new ApiResponse(true,
                            new CachedFile(getCacheId(), entity.getContent()));
                } else {
                    // Some text need cache
                    String text = EntityUtils.toString(entity, "UTF8");
                    ret = new ApiResponse(true, new CachedText(getCacheId(), text));
                }
            } else {
                // Some text need cache
                String text = EntityUtils.toString(entity, "UTF8");
                ret = new ApiResponse(true, new CachedText(getCacheId(), text));
            }
        } catch (Exception e) {
//            e.printStackTrace();
            ret = new ApiResponse(false, CachedText.getStatus(500));
        }
        return ret;
    }

    public boolean isJSON(String type) {
        return (type.contains("json"));
    }

    public static enum Type {
        GET, GET_CACHE, POST_CACHE, BATCH_CACHE, STATE
    }
}
