package sunlib.turtle.api;

import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import sunlib.turtle.utils.ApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-26
 * Time: PM12:34
 */
public class SunExerciseCategorizer implements ApiCategorizer {

    public List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();

    protected SunExerciseCategorizer() {
        addMatcher(
                ApiRequest.Type.GET,
                NanoHTTPD.Method.GET,
                "/org.sunshinelibrary.exercise/bootstrap");
        addMatcher(
                ApiRequest.Type.STATE,
                NanoHTTPD.Method.GET,
                "/state/.*");
        addMatcher(
                ApiRequest.Type.GET,
                NanoHTTPD.Method.GET,
                "/exercise/v1/root");
        addMatcher(
                ApiRequest.Type.GET_CACHE,
                NanoHTTPD.Method.GET,
                "/app/exercise/.*");
        addMatcher(
                ApiRequest.Type.GET_CACHE,
                NanoHTTPD.Method.GET,
                "/exercise/v1/user_data/.*");
        addMatcher(
                ApiRequest.Type.POST_CACHE,
                NanoHTTPD.Method.POST,
                "/exercise/v1/user_data/.*")
                .and("ts", null);
        addMatcher(
                ApiRequest.Type.BATCH_CACHE,
                NanoHTTPD.Method.GET,
                "/exercise/v1/(chapters|achievements|resources).*")
                .and("act", "(status|cache)")
                .not("ts", null);
        addMatcher(
                ApiRequest.Type.GET_CACHE,
                NanoHTTPD.Method.GET,
                "/exercise/v1/(subjects|chapters|lessons|user_info|achievements|resources).*")
                .and("act", null);
    }

    private RequestMatcher addMatcher(ApiRequest.Type type, NanoHTTPD.Method method, String pattern) {
        RequestMatcher ret = new RequestMatcher(type, method, pattern);
        matchers.add(ret);
        return ret;
    }

    public ApiRequest.Type getType(NanoHTTPD.Method method, String uri, Map<String, String> params) {
        for (RequestMatcher matcher : matchers) {
            if (matcher.match(method, uri, params)) {
                return matcher.type;
            }
        }
        return null;
    }

    @Override
    public ApiRequest.Type getRequestCategory(ApiRequest request) {
        return getType(request.method, request.uri, request.params);
    }

    public static class RequestMatcher {
        public NanoHTTPD.Method method;
        public ApiRequest.Type type;
        public Map<String, List<MatchItem>> matchItems = new HashMap<String, List<MatchItem>>();
        public String urlPattern;

        public RequestMatcher(ApiRequest.Type type, NanoHTTPD.Method method, String urlPattern) {
            this.type = type;
            this.method = method;
            this.urlPattern = urlPattern;
        }

        private void putItem(MatchItem item) {
            List<MatchItem> list = matchItems.get(item.key);
            if (list == null) {
                list = new ArrayList<MatchItem>();
                matchItems.put(item.key, list);
            }
            list.add(item);
        }

        public RequestMatcher and(String key, String value) {
            putItem(new MatchItem(key, value, true, true));
            return this;
        }

        public RequestMatcher or(String key, String value) {
            putItem(new MatchItem(key, value, false, true));
            return this;
        }

        public RequestMatcher not(String key, String value) {
            putItem(new MatchItem(key, value, true, false));
            return this;
        }

        public boolean match(NanoHTTPD.Method method, String uri, Map<String, String> params) {
            // check method
            if (!ObjectUtils.equals(method, this.method) || StringUtils.isEmpty(uri)) {
                return false;
            }

            // check url pattern
            if (!uri.matches(urlPattern)) {
                return false;
            }

            // check params
            for (String key : matchItems.keySet()) {
                List<MatchItem> items = matchItems.get(key);
                for (MatchItem item : items) {
                    if (!item.match(params.get(key))) {
                        return false;
                    }
                }
            }
            return true;
        }

    }

    public static class MatchItem {
        public String key;
        public String pattern;
        public boolean required = true;
        public boolean positive = true;

        public MatchItem(String param, String pattern, boolean required, boolean positive) {
            this.key = param;
            this.pattern = pattern;
            this.required = required;
            this.positive = positive;
        }

        /**
         * value pattern required positive result
         * *     null    *        *        !((value == null) ^ positive)
         * null  !null   *        *        false
         * !null !null   *        *        !(value.match(pattern) xor positive)
         */
        public boolean match(String value) {
            if (pattern == null) {
                return !((value == null) ^ positive);
            }
            return value != null
                    && !(value.matches(pattern) ^ positive);
        }

    }
}
