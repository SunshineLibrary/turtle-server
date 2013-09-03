package sunlib.turtle.utils;

import com.google.gson.annotations.SerializedName;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Method;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */
public class ApiRequest implements Serializable {

    static Logger logger = LogManager.getLogger(ApiRequest.class.getName());
    public Method method;
    public String host;
    public int port;
    public String uri;
    public Map<String, String> headers = new HashMap<String, String>();
    public Map<String, String> params = new HashMap<String, String>();
    public Type type;

    public ApiRequest(String host, int port, String uri) {
        this.host = host;
        this.port = port;
        this.uri = uri;
    }

    public ApiRequest(String host, int port, String uri, Method method, Map<String, String> header, Map<String, String> params, Map<String, String> files) {
        this.host = host;
        this.port = port;
        this.uri = uri;
        this.method = method;
        this.params = params;
        // Get request type from SunApiProvider
//        if (params != null && params.size() > 0) {
//            uri = uri + "?" + params.get(NanoHTTPD.QUERY_STRING_PARAMETER);
//        }
//        this.type = SunApiMatcher.getMatcher().match(method, uri);
    }

    public ApiRequest(String host, int port, ApiRequest req) {
        this.host = host;
        this.port = port;
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
        String ts = params.get("ts");
        return uri + ((StringUtils.isEmpty(ts)) ? "" : ts);
    }

    public ApiRequest param(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public ApiRequest params(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    public ApiRequest headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", method, uri, params);
    }

    public ApiResponse get() {
        ApiResponse ret = null;
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(host).setPort(port).setPath(uri);
        if (params != null) {
            for (String param : params.keySet()) {
                if (!"callback".equals(param)) {
                    builder.setParameter(param, params.get(param));
                }
            }
        }
        try {
            URI uri = builder.build();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            HttpResponse resp = httpclient.execute(httpget);

            logger.info("API:{},{}", httpget.getURI(), params);

            HttpEntity entity = resp.getEntity();
            Header[] contentTypes = resp.getHeaders("content-type");
            if (contentTypes != null) {
                String type = contentTypes[0].getValue();
                if (!isJSON(type)) {
                    // A file need cache
                    ret = new ApiResponse(
                            true,
                            new CachedFile(getCacheId(), entity.getContent()))
                            .mime(type);
                } else {
                    // Some text need cache
                    String text = EntityUtils.toString(entity, "UTF8");
                    ret = new ApiResponse(
                            true,
                            new CachedText(getCacheId(), text))
                            .mime(type);
                }
            } else {
                // Some text need cache
                String text = EntityUtils.toString(entity, "UTF8");
                ret = new ApiResponse(
                        true,
                        new CachedText(getCacheId(), text))
                        .mime(NanoHTTPD.MIME_JSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = new ApiResponse(false, CachedText.getStatus(500));
        }
        return ret;
    }

    public boolean isJSON(String type) {
        return (type.contains("json"));
    }

    public void addCallback(String callback) {
        params.put("callback", callback);
    }

    public ApiRequest method(NanoHTTPD.Method method) {
        this.method = method;
        return this;
    }

    public static enum Type {
        GET, GET_CACHE, POST_CACHE, BATCH_CACHE, STATE
    }
}
