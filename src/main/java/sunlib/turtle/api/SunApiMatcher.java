package sunlib.turtle.api;

import fi.iki.elonen.NanoHTTPD;

import static sunlib.turtle.models.ApiRequest.Type.*;

/**
 * User: fxp
 * Date: 13-8-20
 * Time: PM12:01
 */
public class SunApiMatcher extends ApiRequestTypeMatcher {

    static SunApiMatcher matcher = new SunApiMatcher();

    private SunApiMatcher() {
        add(GET, NanoHTTPD.Method.GET, "/exercise/v1/root\\?callback=[^&]+");
        add(GET, NanoHTTPD.Method.GET, "/pack/v1");

        /* Use cached content when available. Fetch update and cache on cache miss. Can block. */

        // GET  http://127.0.0.1/exercise/v1/subjects/1?ts=123456789
        // GET  http://127.0.0.1/exercise/v1/chapters/1?ts=123456789
        // GET  http://127.0.0.1/exercise/v1/lessons/1?ts=123456789
        add(GET_CACHE, NanoHTTPD.Method.GET, "/exercise/v1/(subjects|chapters|lessons)/[a-z0-9]+\\?ts=[0-9]+&callback=[^&]+");

        // GET  http://127.0.0.1/exercise/v1/lessons/1/%2E%2C%2D.jpg?ts=123456789
        add(GET_CACHE, NanoHTTPD.Method.GET, "/exercise/v1/lessons/[a-z0-9]+/.+\\.(png|jpg|jpeg|mp4|mp3)\\?ts=[0-9]+");

        // GET  http://127.0.0.1/pack/v1/subjects/1?ts=123456789
        // GET  http://127.0.0.1/pack/v1/folders/1?ts=123456789
        // GET  http://127.0.0.1/pack/v1/pieces/1?ts=123456789
        add(GET_CACHE, NanoHTTPD.Method.GET, "/pack/v1/(subjects|folders|pieces)/[a-z0-9]+\\?ts=[0-9]+");

        // GET  http://127.0.0.1/pack/v1/pieces/1/2e9201af2920ed0192c2.mp4?ts=123456789
        add(GET_CACHE, NanoHTTPD.Method.GET, "/exercise/v1/lessons/[a-z0-9]+/[a-z0-9]+\\.(png|jpg|jpeg|mp4|mp3)\\?ts=[0-9]+");

        // GET  http://127.0.0.1/exercise/v1/user_data/subjects/1
        // GET  http://127.0.0.1/exercise/v1/user_data/achievements/1
        add(GET_CACHE, NanoHTTPD.Method.GET, "/exercise/v1/user_data/(subjects|chapters|lesson|achievements)/[a-z0-9]+");

        // GET  http://127.0.0.1/pack/v1/user_data/subjects/1
        // GET  http://127.0.0.1/pack/v1/user_data/achievements/1
        add(GET_CACHE, NanoHTTPD.Method.GET, "/pack/v1/user_data/(subjects|folders|pieces|achievements)/[a-z0-9]+");

        /* Update cached content and push update to server. Cannot block; queue up post requests. */

        // POST http://127.0.0.1/exercise/v1/user_data/subjects/1
        // POST http://127.0.0.1/exercise/v1/user_data/achievements/1
        add(POST_CACHE, NanoHTTPD.Method.POST, "/exercise/v1/user_data/(subjects|chapters|lesson|achievements)/[a-z0-9]+");

        // POST http://127.0.0.1/pack/v1/user_data/subjects/1
        // POST http://127.0.0.1/pack/v1/user_data/achievements/1
        add(POST_CACHE, NanoHTTPD.Method.POST, "/pack/v1/user_data/(subjects|folders|pieces|achievements)/[a-z0-9]+");

        /* Prefetch all required resources base on the manifest. Cannot block; returns cache status. */

        // GET  http://127.0.0.1/exercise/v1/chapters/1?action=cache
        // GET  http://127.0.0.1/exercise/v1/chapters/1?action=status
        // GET  http://127.0.0.1/pack/v1/folders/1?action=cache
        // GET  http://127.0.0.1/pack/v1/folders/1?action=status
        add(BATCH_CACHE, NanoHTTPD.Method.GET, "/exercise/v1/(chapters|achievements)/[a-z0-9]+\\?ts=[0-9]+&act=(cache|status)&callback=[^&]+");

    }

    public static SunApiMatcher getMatcher() {
        return matcher;
    }
}
