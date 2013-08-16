package sunlib.turtle.api;

import fi.iki.elonen.NanoHTTPD;
import org.junit.Before;
import org.junit.Test;
import sunlib.turtle.models.ApiRequest;

import static fi.iki.elonen.NanoHTTPD.Method.*;
import static org.junit.Assert.assertEquals;

/**
 * User: Bowen
 * Date: 13-8-15
 */

public class ApiRequestTypeMatcherTest {

    ApiRequestTypeMatcher matcher;

    @Before
    public void setUp() {
        matcher = new ApiRequestTypeMatcher();
    }

    @Test
    public void testMatchPattern() throws Exception {
        matcher.add(ApiRequest.Type.GET, GET, "/exercise/v1");
        matcher.add(ApiRequest.Type.GET, GET, "/exercise/v1/subjects/[a-z0-9]+");
        matcher.add(ApiRequest.Type.GET_CACHE, GET, "/exercise/v1/subjects/[a-z0-9]+\\?ts=[0-9]+");
        matcher.add(ApiRequest.Type.GET_CACHE, POST, "/exercise/v1/chapters/[a-z0-9]+\\?ts=[0-9]+");

        assertEquals(ApiRequest.Type.GET, matcher.match(GET, "/exercise/v1"));
        assertEquals(ApiRequest.Type.GET, matcher.match(GET, "/exercise/v1/subjects/12"));
        assertEquals(ApiRequest.Type.GET_CACHE, matcher.match(GET, "/exercise/v1/subjects/12?ts=123456"));
        assertEquals(ApiRequest.Type.GET_CACHE, matcher.match(POST, "/exercise/v1/chapters/1e293de919c0a203?ts=123456"));
    }
}
