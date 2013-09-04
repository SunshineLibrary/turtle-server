package sunlib.turtle.api;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sunlib.turtle.TurtleServer;
import sunlib.turtle.models.Manifest;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;
import sunlib.turtle.utils.WS;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: fxp
 * Date: 13-8-30
 * Time: PM3:20
 */
public class AppAPITest {

    public static String API_BASE = "/exercise/v1/";
    public static ApiRequest API_GET_ROOT = getRequest("root");
    public static ApiRequest API_GETSTATUS_ROOT =
            getRequest("resources")
                    .param("act", "status")
                    .param("ts", "1");
    public static ApiRequest API_GETCACHE_ROOT =
            getRequest("root")
                    .param("act", "cache")
                    .param("ts", "1");
    public static ApiRequest API_GET_USERINFO =
            getRequest("user_info")
                    .param("ts", "1");
    public static ApiRequest API_GET_CHAPTER =
            getRequest("chapters/1011")
                    .param("ts", "1");
    NanoHTTPD server = null;

    public static ApiRequest getRequest(String uri) {
        return WS.url("127.0.0.1", 3001, API_BASE + uri);
    }

    @Before
    public void before() {
        server = ServerRunner.run(TurtleServer.class);
    }

    public void testGetApi(ApiRequest request, boolean success) throws IOException, URISyntaxException {
        System.out.println("TEST:" + request);
        ApiResponse resp = null;
        resp = request.get();
        assertTrue(success);
        assertTrue(!StringUtils.isEmpty((String) resp.getData().getContent()));
    }

    public void testGETUrl(String uri, String ts, boolean success) throws IOException, URISyntaxException {
        ApiResponse resp = getRequest(uri).param("ts", ts).get();
        assertEquals(success, resp.success);
    }

    public void testCached(String uri, String ts, boolean success) throws IOException, URISyntaxException {
        ApiResponse resp = getRequest("/state/cache").get();
        List<String> cached = new Gson().fromJson((String) resp.getData().getContent(), new ArrayList<String>().getClass());
        assertEquals(success, cached.contains(uri));
    }

    public void testGETApiWithFailedResult(ApiRequest request) throws IOException, URISyntaxException {
        testGetApi(request, false);
    }

    public void testGETApiWithSuccessResult(ApiRequest request) throws IOException, URISyntaxException {
        testGetApi(request, true);
    }

    @Test
    public void testGetRoot() throws Exception {
        System.out.println("testBootstrap");
        // GET root
        testGETApiWithSuccessResult(API_GET_ROOT);

        // GET user_info
        testGETApiWithSuccessResult(API_GET_USERINFO);

        // GET_STATUS resources
        testGETApiWithSuccessResult(API_GETSTATUS_ROOT);

        // make sure here's no such resources yet
        Manifest resp = API_GETSTATUS_ROOT.get().fromJSON(Manifest.class);
        assertFalse(!resp.is_cached);
        assertTrue(resp.manifest.size() > 0);
        String aResource = resp.manifest.get(0);

        // Before caching
        testCached(aResource, aResource, false);

        // GET_CACHE resources
        testGETApiWithSuccessResult(API_GETCACHE_ROOT);

        // GET_STATUS resources
        for (int i = 0; i < 10; i++) {
            testGETApiWithSuccessResult(API_GETSTATUS_ROOT);
        }

        // after caching
        resp = API_GETSTATUS_ROOT.get().fromJSON(Manifest.class);
        assertTrue(resp.is_cached);
        assertEquals(resp.progress, 100);

        testGETApiWithSuccessResult(API_GETCACHE_ROOT);
        testCached(aResource, aResource, true);
        System.out.println("complete testBootstrap");

    }

    @After
    public void after() {
        server.stop();
    }

}
