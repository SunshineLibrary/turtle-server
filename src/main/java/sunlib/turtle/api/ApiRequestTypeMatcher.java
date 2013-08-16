package sunlib.turtle.api;

import sunlib.turtle.models.ApiRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static fi.iki.elonen.NanoHTTPD.Method;

/**
 * User: Bowen
 * Date: 13-8-14
 */
public class ApiRequestTypeMatcher {

    private Map<Pattern, ApiRequest.Type> mGetPatternMap;
    private Map<Pattern, ApiRequest.Type> mPostPatternMap;

    public ApiRequestTypeMatcher() {
        mGetPatternMap = new HashMap<Pattern, ApiRequest.Type>();
        mPostPatternMap = new HashMap<Pattern, ApiRequest.Type>();
    }

    public void add(ApiRequest.Type type, Method method, String pattern) {
        Map<Pattern, ApiRequest.Type> patternMap = getPatternMap(method);
        if (patternMap == null)
            return;

        Pattern p = Pattern.compile(pattern);
        if (! patternMap.containsKey(pattern)) {
            patternMap.put(p, type);
        }
    }

    public ApiRequest.Type match(Method method, String url) {
        Map<Pattern, ApiRequest.Type> patternMap = getPatternMap(method);
        if (patternMap == null)
            return null;

        for (Map.Entry<Pattern, ApiRequest.Type> entry: patternMap.entrySet()) {
            if (entry.getKey().matcher(url).matches()) {
                return entry.getValue();
            }
        }

        return null;
    }

    protected Map<Pattern, ApiRequest.Type> getPatternMap(Method method) {
        switch(method) {
            case GET:
                return mGetPatternMap;
            case POST:
                return mPostPatternMap;
            default:
                return null;
        }
    }
}
