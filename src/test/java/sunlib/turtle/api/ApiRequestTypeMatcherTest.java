package sunlib.turtle.api;

import fi.iki.elonen.NanoHTTPD;
import org.junit.Before;
import org.junit.Test;
import sunlib.turtle.utils.ApiRequest;

import static org.junit.Assert.assertEquals;

/**
 * User: Bowen
 * Date: 13-8-15
 */

public class ApiRequestTypeMatcherTest {

    SunExerciseCategorizer categorizer;

    @Before
    public void setUp() {
        categorizer = (SunExerciseCategorizer) TurtleAPI.newCategorizer("sunlib-exercise");
    }

    @Test
    public void testGetRoot() throws Exception {
        ApiRequest request = new ApiRequest("/exercise/v1/root");
        request.param("ts", "1");
        request.method = NanoHTTPD.Method.GET;
        ApiRequest.Type type = categorizer.getRequestCategory(request);
        assertEquals(type, ApiRequest.Type.GET);
    }

    @Test
    public void testGetMaterial() throws Exception {
        ApiRequest request = new ApiRequest("/exercise/v1/lessons/123");
        request.param("ts", "1");
        request.method = NanoHTTPD.Method.GET;
        ApiRequest.Type type = categorizer.getRequestCategory(request);
        assertEquals(type, ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testGetMaterialFile() throws Exception {
        ApiRequest request = new ApiRequest("/exercise/v1/lessons/05be6bf8-bc78-11e2-9d5a-00163e011797/test.mp4");
        request.method = NanoHTTPD.Method.GET;
        ApiRequest.Type type = categorizer.getRequestCategory(request);
        assertEquals(type, ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testGetUserInfo() throws Exception {
        ApiRequest request = new ApiRequest("/exercise/v1/user_info");
        request.param("ts", "1");
        request.method = NanoHTTPD.Method.GET;
        ApiRequest.Type type = categorizer.getRequestCategory(request);
        assertEquals(type, ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testGetUserData() throws Exception {
        ApiRequest request = new ApiRequest("/exercise/v1/user_data/user_info");
        request.method = NanoHTTPD.Method.GET;
        ApiRequest.Type type = categorizer.getRequestCategory(request);
        assertEquals(type, ApiRequest.Type.GET_CACHE);
    }

    @Test
    public void testPostUserData() throws Exception {
        ApiRequest request = new ApiRequest("/exercise/v1/user_data/user_info");
        request.method = NanoHTTPD.Method.POST;
        ApiRequest.Type type = categorizer.getRequestCategory(request);
        assertEquals(type, ApiRequest.Type.POST_CACHE);
    }
}
