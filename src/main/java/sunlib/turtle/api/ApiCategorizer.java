package sunlib.turtle.api;

import sunlib.turtle.utils.ApiRequest;

/**
 * User: fxp
 * Date: 13-8-26
 * Time: PM12:32
 */
public interface ApiCategorizer {

    public ApiRequest.Type getRequestCategory(ApiRequest request);

}
