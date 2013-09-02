package sunlib.turtle.api;

import fi.iki.elonen.NanoHTTPD;
import org.junit.Before;
import org.junit.Test;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.SunWS;

import static org.junit.Assert.assertEquals;

/**
 * User: Bowen
 * Date: 13-8-15
 */

public class ApiCategorizerTest {

    public static String API_BASE = "/exercise/v1/";
    public static SunExerciseCategorizer categorizer = (SunExerciseCategorizer) TurtleAPI.newCategorizer("sunlib-exercise");

    public static ApiRequest getRequest(String uri) {
        return SunWS.url(API_BASE + uri);
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetRoot() throws Exception {
        ApiRequest request = getRequest("root")
                .param("ts", "1")
                .method(NanoHTTPD.Method.GET);
        assertEquals(categorizer.getRequestCategory(request), ApiRequest.Type.GET);
    }

    @Test
    public void testGetMaterial() throws Exception {
        ApiRequest request = getRequest("lessons/123")
                .param("ts", "1")
                .method(NanoHTTPD.Method.GET);
        assertEquals(categorizer.getRequestCategory(request), ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testGetMaterialFile() throws Exception {
        ApiRequest request = getRequest("lessons/05be6bf8-bc78-11e2-9d5a-00163e011797/test.mp4")
                .param("ts", "1")
                .method(NanoHTTPD.Method.GET);
        assertEquals(categorizer.getRequestCategory(request), ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testGetUserInfo() throws Exception {
        ApiRequest request = getRequest("user_info")
                .param("ts", "1")
                .method(NanoHTTPD.Method.GET);
        assertEquals(categorizer.getRequestCategory(request), ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testGetUserData() throws Exception {
        ApiRequest request = getRequest("user_data/user_info")
                .param("ts", "1")
                .method(NanoHTTPD.Method.GET);
        assertEquals(categorizer.getRequestCategory(request), ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testPostUserData() throws Exception {
        ApiRequest request = getRequest("user_data/user_info")
                .method(NanoHTTPD.Method.POST);
        assertEquals(categorizer.getRequestCategory(request), ApiRequest.Type.POST_CACHE);
    }
}
