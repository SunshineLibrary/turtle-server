package sunlib.turtle.utils;

/**
 * User: fxp
 * Date: 13-8-29
 * Time: PM2:48
 */
public class WS {

    public static ApiRequest req(String host, int port, ApiRequest req) {
        return new ApiRequest(host, port, req);
    }

    public static ApiRequest url(String host, int port, String uri) {
        return new ApiRequest(host, port, uri);
    }
}
