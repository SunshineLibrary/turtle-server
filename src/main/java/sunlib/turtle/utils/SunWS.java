package sunlib.turtle.utils;

/**
 * User: fxp
 * Date: 13-8-22
 * Time: AM11:09
 */
public class SunWS extends WS {

    public static ApiRequest url(String uri) {
        return new ApiRequest("127.0.0.1", 8000, uri);
    }
}
