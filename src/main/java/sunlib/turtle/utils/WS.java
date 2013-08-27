package sunlib.turtle.utils;

/**
 * User: fxp
 * Date: 13-8-22
 * Time: AM11:09
 */
public class WS {

    public static ApiRequest req(ApiRequest req) {
        return new ApiRequest(req);
    }

    public static ApiRequest url(String url) {
        return new ApiRequest(url);
    }
}
