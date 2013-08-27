package sunlib.turtle.handler;

import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

public interface RequestHandler {

    public ApiResponse handleRequest(ApiRequest request);

    public Object fetchResponse(ApiRequest request);

    public void stop();
}
