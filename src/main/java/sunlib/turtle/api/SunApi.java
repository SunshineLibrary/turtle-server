package sunlib.turtle.api;

import sunlib.turtle.models.ApiResponse;

import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-20
 * Time: PM5:40
 */
public class SunApi {


    public static SunApi getApi() {
        return new SunApi();
    }

    public SunApi param(String key, String value) {
        return this;
    }

    public ApiResponse get(String uri, Map<String, String> params) {
        return null;
    }

}
